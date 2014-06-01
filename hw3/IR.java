// This is supporting software for CS322 Compilers and Language Design II
// Copyright (c) Portland State University
// 
// Three-address IR definitions. 
// (Some nodes are not strictly 3-address.)
//
// Based on Version 5. (Last modified on JL on 2/15/14.)   
// 
// Extended with support for X86 Code generation
//
import java.util.*;

class IR {

    // X86 Code Generation Support

    public static final BoolLit TRUE = new BoolLit(true);
    public static final BoolLit FALSE = new BoolLit(false);
    // universal globals
    static final X86.Reg tempReg1 = X86.R10;
    static final X86.Reg tempReg2 = X86.R11;

    // per-function globals
    public static boolean indexed = false;
    public static int linenum = 0;
    // per-program globals
    static int funcNumber; // current function number, used to construct unique labels
    static List<String> stringLiterals; // accumulated string literals, indexed by position
    static List<Set<Reg>> liveOutSets; // operand liveness data
    static Map<Reg, X86.Reg> env; // location mapping
    static int frameSize; // in bytes
    static int irPtr; // pointer into IR list

    static String line(boolean count, String s) {
        String idx = (indexed && count) ? (linenum++) + ". "
                + (linenum < 11 ? " " : "") : "";
        return idx + s;
    }

    // Types

    public static Type type(Const item) {
        if (item instanceof BoolLit)
            return Type.BOOL;
        if (item instanceof IntLit)
            return Type.INT;
        return Type.PTR;
    }

    public static String StringArrayToString(String[] vars) {
        String s = "(";
        if (vars.length > 0) {
            s += vars[0];
            for (int i = 1; i < vars.length; i++)
                s += ", " + vars[i];
        }
        return s + ")";
    }

    // Program

    public static boolean isCompareOp(BOP op) {
        return (op == RelOP.EQ) || (op == RelOP.NE) ||
                (op == RelOP.LT) || (op == RelOP.LE) ||
                (op == RelOP.GT) || (op == RelOP.GE);
    }

    // Global data records

    // Return true if some IR reg mapped to r is live over
    // instruction at irPtr and is not defined by that instruction.
    // Note that what we really need to test is whether
    // r is both liveIn and liveOut of irPtr, but we only have
    // the liveOut sets available.  Fortunately, because labels
    // occupy an instruction slot, we know that the liveIn for
    // any (real) instruction equals the liveOut of its predecessor.
    static boolean liveOver(X86.Reg r, int irPtr, Reg defined) {
        X86.Reg defined_r = env.get(defined);
        if (r.equals(defined_r))
            return false;
        Set<Reg> liveover = new HashSet<Reg>();
        liveover.addAll(liveOutSets.get(irPtr));
        liveover.retainAll(liveOutSets.get(irPtr - 1));
        for (Reg ir : liveover)
            if (r.equals(env.get(ir)))
                return true;
        return false;
    }

    /** Calculate (non-binding) preferences for register assignments. */
    static Map<IR.Reg, X86.Reg> getPreferences(IR.Func func) {
        Map<IR.Reg, X86.Reg> preferences = new HashMap<IR.Reg, X86.Reg>();

        // Incoming arguments from callee's perspective
        for (int i = 0; i < func.params.length; i++)
            preferences.put(new IR.Id(func.params[i]), X86.argRegs[i]);

        for (IR.Inst c : func.code) {
            if (c instanceof IR.Call) {
                IR.Call cl = (IR.Call) c;
                // Arguments from caller's perspective
                for (int i = 0; i < cl.args.length; i++) {
                    IR.Src argRand = cl.args[i];
                    if (argRand instanceof IR.Reg)
                        preferences.put((IR.Reg) argRand, X86.argRegs[i]);
                }
                // Return value from caller's perspective
                if (cl.rdst instanceof IR.Reg)
                    preferences.put((IR.Reg) cl.rdst, X86.RAX);
            } else if (c instanceof IR.Return) {
                // Return value from callee's perspective
                IR.Return r = (IR.Return) c;
                if (r.val instanceof IR.Reg)
                    preferences.put((IR.Reg) r.val, X86.RAX);
            } else if (c instanceof IR.Binop) {
                IR.Binop b = (IR.Binop) c;
                if (b.op == IR.ArithOP.DIV) {
                    // Argument and result of DIV
                    if (b.src1 instanceof IR.Reg)
                        preferences.put((IR.Reg) b.src1, X86.RAX);
                    if (b.dst instanceof IR.Reg)
                        preferences.put((IR.Reg) b.dst, X86.RAX);
                }
            }
        }
        return preferences;
    }


    public static enum Type {
        BOOL(":B", 1, X86.Size.B), INT(":I", 4, X86.Size.L), PTR(":P", 8, X86.Size.Q);
        final String name;
        final int size;
        final X86.Size X86_size;

        Type(String s, int i, X86.Size xs) {
            name = s;
            size = i;
            X86_size = xs;
        }

        public String toString() {
            return name;
        }
    }

    // Instructions

    public static enum ArithOP implements BOP {
        ADD("+", "add"), SUB("-", "sub"), MUL("*", "imul"), DIV("/", "idiv"), AND("&&", "and"), OR("||", "or");
        final String name;
        final String xname;

        ArithOP(String n, String xn) {
            name = n;
            xname = xn;
        }

        public String toString() {
            return name;
        }

        public String X86_name() {
            return xname;
        }
    }

    public static enum RelOP implements BOP {
        EQ("==", "e"), NE("!=", "ne"), LT("<", "l"), LE("<=", "le"), GT(">", "g"), GE(">=", "ge");
        final String name;
        final String xname;

        RelOP(String n, String xn) {
            name = n;
            xname = xn;
        }

        public String toString() {
            return name;
        }

        public String X86_name() {
            return xname;
        }
    }

    public static enum UOP {
        NEG("-", "neg"), NOT("!", "not");
        final String name;
        final String xname;

        UOP(String n, String xn) {
            name = n;
            xname = xn;
        }

        public String toString() {
            return name;
        }

        public String X86_name() {
            return xname;
        }
    }

    public static interface BOP {
        public abstract String X86_name();
    }

    public interface Operand {
        /**
         * Add to register set if actually a register. *
         */
        void addTo(Set<Reg> s);
    }

    public interface Reg {}

    public interface Src extends Operand {
        /**
         * Generate a source operand.
         * If imm_ok is true, returning an immediate is ok;
         * otherwise, result is definitely a register (possibly temp).
         * Not all operands can return an immediate, so it is always possible
         * that temp will be used even if imm_ok is true. *
         */
        X86.Operand gen_source_operand(boolean imm_ok, X86.Reg temp);
    }

    public interface Dest extends Operand, Reg {
        /** Generate a destination operand of the specified size.
         Returns null if dest wasn't given a location or if we are
         outside its live range; either way, it is dead at this point. **/
        X86.Reg gen_dest_operand();
    }

    public interface CallTgt extends Operand {
    }

    public interface Const extends Operand {
        abstract void gen_const();
    }

    public static class Program {
        public final Data[] data;
        public final Func[] funcs;

        public Program(Data[] d, Func[] f) {
            data = d;
            funcs = f;
        }

        public Program(List<Data> dl, List<Func> fl) {
            this(dl.toArray(new Data[0]), fl.toArray(new Func[0]));
        }

        public String toIndexedString() {
            indexed = true;
            return toString();
        }

        public String toString() {
            String str = "# IR Program\n";
            if (data != null && data.length > 0)
                str += "\n";
            for (Data d : data)
                str += d;
            for (Func f : funcs)
                str += "\n" + f;
            return str;
        }

        void gen() {
            funcNumber = 0;
            stringLiterals = new ArrayList<String>();
            X86.emit0(".text");
            for (Data d : data)
                d.gen();
            for (Func f : funcs) {
                f.gen();
                funcNumber++;
            }
            // emit any accumulated string literals
            int i = 0;
            for (String s : stringLiterals) {
                X86.GLabel lab = new X86.GLabel("_S" + i);
                X86.emitGLabel(lab);
                X86.emitString(s);
                i++;
            }
        }

    }

    // Operators

    public static class Data {
        public final Global name;
        public final int size;
        public final Const[] items;

        public Data(Global n, int i, Const[] l) {
            name = n;
            size = i;
            items = l;
        }

        public Data(Global n, int i, List<Const> ll) {
            this(n, i, ll.toArray(new Const[0]));
        }

        public String toString() {
            String str = "data " + name + " (sz=" + size + "): ";
            if (items.length > 0) {
                str += items[0].toString();
                for (int i = 1; i < items.length; i++)
                    str += ", " + items[i];
            }
            return str + "\n";
        }

        void gen() {
            X86.GLabel l = new X86.GLabel(name.toString());
            X86.emit1(".globl", l);
            X86.emitGLabel(l);
            for (Const c : items)
                c.gen_const();
        }
    }

    // Functions
    public static class Func {
        public final String name;
        public final String[] params;
        public final String[] locals;
        public final Inst[] code;

        public Func(String n, String[] p, String[] l, Inst[] c) {
            name = n;
            params = p;
            locals = l;
            code = c;
        }

        public Func(String n, List<String> pl, List<String> ll, List<Inst> cl) {
            this(n, pl.toArray(new String[0]), ll.toArray(new String[0]),
                    cl.toArray(new Inst[0]));
        }

        public String name_params_string() {
            return
                    line(false, "_" + name + " " + StringArrayToString(params)
                            + "\n");
        }

        public String locals_string() {
            return
                    (locals.length == 0 ? "" :
                            line(false, StringArrayToString(locals) + "\n"));
        }

        public String toString() {
            String body = "";
            linenum = 0;
            for (Inst s : code)
                body += s.toString();
            return name_params_string() + locals_string() + line(false, "{\n") + body + line(false, "}\n");
        }

        void gen() {
            assert (code[0] instanceof LabelDec);
            assert (code[code.length - 1] instanceof LabelDec);
            for (int i = 0; i < code.length; i++) {
                if (code[i] instanceof Binop) {
                    if (((Binop) code[i]).src1 instanceof IR.Reg
                            && ((Binop) code[i]).src2 instanceof IntLit
                            && ((((IntLit) ((Binop) code[i]).src2).i == 1
                            || ((IntLit) ((Binop) code[i]).src2).i == 2
                            || ((IntLit) ((Binop) code[i]).src2).i == 4
                            || ((IntLit) ((Binop) code[i]).src2).i == 8
                    ))
                            && (code[i + 2] != null) // If no i+2, no i+1, so no need to check i+1
                            && (code[i + 1] instanceof Binop)
                            && (code[i + 2] instanceof Load)
                            || (code[i + 2] instanceof Store)
                            )
                    {
                        int offset = ((IntLit)((Binop)code[i]).src2).i;
                        if (code[i + 2] instanceof IR.Load) {
                            IR.Addr source = new Addr(((Binop)code[i+1]).src1, offset);
                            IR.Dest dest = ((Load) code[i + 2]).dst;
                            IR.Type type = ((Load) code[i + 2]).type;
                            code[i + 2] = new Load(type, dest, source);
                            System.err.println("ERR it goes");
                        }
                    }
                }
            }

            System.out.print("    # " + name_params_string());
            if (locals_string().length() > 0)
                System.out.print("    # " + locals_string());

            linenum = 0;

            // Calculate liveness information for Temp's and Id's
            liveOutSets = Liveness.calculateLiveOutSets(this);
            // Assign a location (register or frame) to every Reg that has a live range
            env = Assignment.assignRegisters(this, liveOutSets);

            // emit the function header
            X86.emit0(".p2align 4,0x90");
            X86.GLabel f = new X86.GLabel("_" + name);
            X86.emit1(".globl", f);
            X86.emitGLabel(f);

            // save any callee-save registers on the stack now
            int calleeSaveSize = 0;
            for (int i = 0; i < X86.calleeSaveRegs.length; i++) {
                X86.Reg r = X86.calleeSaveRegs[i];
                if (env.containsValue(r)) {
                    X86.emit1("pushq", r);
                    calleeSaveSize += X86.Size.Q.bytes;
                }
            }

            // make space for the local frame
            // at entry stack pointer is of the form n16+8
            // need to change it to m16, so that ret addr push brings it back to m16+8
            frameSize = 0;
            if ((calleeSaveSize % (2 * X86.Size.Q.bytes)) == 0)
                frameSize += X86.Size.Q.bytes;
            if (frameSize != 0)
                X86.emit2("subq", new X86.Imm(frameSize), X86.RSP);

            // Move the incoming actual arguments to their assigned locations
            // Simply fail if function has more than 6 args
            int paramCount = params.length;
            assert (paramCount <= X86.argRegs.length);
            // Do parallel move
            X86.Reg[] src = new X86.Reg[paramCount];
            X86.Reg[] dst = new X86.Reg[paramCount];
            int n = 0;
            for (int i = 0; i < paramCount; i++) {
                X86.Reg d = env.get(new Id(params[i]));
                if (d != null) {
                    src[n] = X86.argRegs[i];
                    dst[n] = d;
                    n++;
                }
            }
            new X86.ParallelMover(n, src, dst, tempReg1).move();

            // emit code for the body (note that irPtr is global)
            for (irPtr = 0; irPtr < code.length; irPtr++) {
                System.out.print("    # " + code[irPtr]);
                code[irPtr].gen();
            }
        }

        // Return set of successors for each instruction in a function
        List<Set<Integer>> successors() {
            Map<String, Integer> labelMap = new HashMap<String, Integer>();
            for (int i = 0; i < code.length; i++) {
                Inst c = code[i];
                if (c instanceof LabelDec)
                    labelMap.put(((LabelDec) c).name, i);
            }
            List<Set<Integer>> allSuccs = new ArrayList<Set<Integer>>(code.length);
            for (int i = 0; i < code.length - 1; i++) { // there's always a label at the end
                Inst inst = code[i];
                Set<Integer> succs = new HashSet<Integer>();
                if (inst instanceof CJump) {
                    succs.add(labelMap.get(((CJump) inst).lab.name));
                    succs.add(i + 1);      // safe because there's always a label at the end
                } else if (inst instanceof Jump)
                    succs.add(labelMap.get(((Jump) inst).lab.name));
                else if (!(inst instanceof Return))
                    succs.add(i + 1);
                allSuccs.add(i, succs);
            }
            allSuccs.add(code.length - 1, new HashSet<Integer>()); // label at the end has no successors
            return allSuccs;
        }

        /**
         * Return sets of operands used by each Inst *
         */
        List<Set<Reg>> used() {
            List<Set<Reg>> used = new ArrayList<Set<Reg>>(code.length);
            for (int i = 0; i < code.length; i++)
                used.add(i, code[i].used());
            return used;
        }

        /**
         * Return sets of operands defined by each Inst *
         */
        List<Set<Reg>> defined() {
            List<Set<Reg>> defined = new ArrayList<Set<Reg>>(code.length);
            for (int i = 0; i < code.length; i++)
                defined.add(i, code[i].defined());
            // Parameters are implicitly defined at top of function
            Set<Reg> top = defined.get(0);
            for (String var : params)
                top.add(new Id(var));
            defined.set(0, top);
            return defined;
        }
    }

    public static abstract class Inst {
        abstract void gen();

        abstract Set<Reg> used();

        abstract Set<Reg> defined();
    }

    public static class Binop extends Inst {
        public final BOP op;
        public final Dest dst;
        public final Src src1, src2;

        public Binop(BOP o, Dest d, Src s1, Src s2) {
            op = o;
            dst = d;
            src1 = s1;
            src2 = s2;
        }

        public String toString() {
            return line(true, " " + dst + " = " + src1 + " " + op + " " + src2 + "\n");
        }

        @Override
        void gen() {
            if (op instanceof ArithOP) {
                switch ((ArithOP) op) {
                    case ADD:
                    case SUB:
                    case MUL:
                    case AND:
                    case OR: {
                        X86.Reg mdest = dst.gen_dest_operand();
                        if (mdest == null) // dead assignment
                            break;
                        X86.Operand mright = src2.gen_source_operand(true, tempReg1);
                        // Move right operand out of the way if necessary.
                        if (mright.equals(mdest)) {
                            X86.emitMov(X86.Size.Q, mright, tempReg1);
                            mright = tempReg1;
                        }
                        X86.Operand mleft = src1.gen_source_operand(true, tempReg2);
                        X86.emitMov(X86.Size.Q, mleft, mdest);
                        X86.emit2(op.X86_name() + X86.Size.Q, mright, mdest);
                        break;
                    }
                    case DIV:  // (also case MOD:)
                    {
                        X86.Reg mdest = dst.gen_dest_operand();
                        if (mdest == null) // dead assignment
                            break;
                        // We tried to avoid allocating any caller-save register, and
                        // hence in particular RAX or RDX, across a DIV.  (We also
                        // preferenced the left operand and result to RAX.)  But
                        // there is a possibility that one or both needs to be saved.
                        // For simplicity, we'll save to the stack (even though the
                        // tempreg's might be available).
                        boolean save_rdx = liveOver(X86.RDX, irPtr, dst);
                        boolean save_rax = liveOver(X86.RAX, irPtr, dst);
                        if (save_rdx)
                            X86.emit1("pushq", X86.RDX);
                        if (save_rax)
                            X86.emit1("pushq", X86.RAX);
                        // And it is still possible that the right operand is in RAX or RDX,
                        // and so needs to be moved out of the way.
                        X86.Operand mright = src2.gen_source_operand(false, tempReg1);
                        if (mright.equals(X86.RAX) || mright.equals(X86.RDX)) {
                            X86.emitMov(X86.Size.Q, mright, tempReg1);
                            mright = tempReg1;
                        }
                        X86.Operand mleft = src1.gen_source_operand(true, X86.RAX);
                        X86.emitMov(X86.Size.Q, mleft, X86.RAX);
                        X86.emit0("cqto"); // sign-extend into RDX
                        X86.emit1("idivq", mright);
                        X86.emitMov(X86.Size.Q, X86.RAX, mdest);
                        // restore RAX, RDX from stack if necessary
                        if (save_rax)
                            X86.emit1("popq", X86.RAX);
                        if (save_rdx)
                            X86.emit1("popq", X86.RDX);
                        break;
                    }
                }
            } else {
                // must be a relational expression
                X86.Reg mdest = dst.gen_dest_operand();
                if (mdest != null) { // not dead assignment
                    // remember: left and right are switched under gnu assembler
                    X86.Operand mleft = src1.gen_source_operand(false, tempReg1);
                    X86.Operand mright = src2.gen_source_operand(true, tempReg2);
                    X86.emit2("cmp" + X86.Size.Q, mright, mleft);
                    X86.Operand mdestb = X86.resize_reg(X86.Size.B, mdest);
                    X86.emit1("set" + op.X86_name(), mdestb);
                    X86.emit2("movzbq", mdestb, mdest);
                }
            }
        }

        Set<Reg> used() {
            Set<Reg> s = new HashSet<Reg>();
            src1.addTo(s);
            src2.addTo(s);
            return s;
        }

        Set<Reg> defined() {
            Set<Reg> s = new HashSet<Reg>();
            dst.addTo(s);
            return s;
        }
    }

    public static class Unop extends Inst {
        public final UOP op;
        public final Dest dst;
        public final Src src;

        public Unop(UOP o, Dest d, Src s) {
            op = o;
            dst = d;
            src = s;
        }

        public String toString() {
            return line(true, " " + dst + " = " + op + src + "\n");
        }

        void gen() {
            X86.Reg mdest = dst.gen_dest_operand();
            if (mdest == null) // dead assignment
                return;
            X86.Operand msrc = src.gen_source_operand(true, tempReg1);
            X86.emitMov(X86.Size.Q, msrc, mdest);
            X86.emit1(op.X86_name() + X86.Size.Q, mdest);
        }

        Set<Reg> used() {
            Set<Reg> s = new HashSet<Reg>();
            src.addTo(s);
            return s;
        }

        Set<Reg> defined() {
            Set<Reg> s = new HashSet<Reg>();
            dst.addTo(s);
            return s;
        }
    }

    // Label

    public static class Move extends Inst {
        public final Dest dst;
        public final Src src;

        public Move(Dest d, Src s) {
            dst = d;
            src = s;
        }

        public String toString() {
            return line(true, " " + dst + " = " + src + "\n");
        }

        void gen() {
            X86.Reg mdest = dst.gen_dest_operand();
            if (mdest == null)  // dead assignment
                return;
            X86.Operand msrc = src.gen_source_operand(true, mdest);
            X86.emitMov(X86.Size.Q, msrc, mdest);  // often a no-op
        }

        Set<Reg> used() {
            Set<Reg> s = new HashSet<Reg>();
            src.addTo(s);
            return s;
        }

        Set<Reg> defined() {
            Set<Reg> s = new HashSet<Reg>();
            dst.addTo(s);
            return s;
        }
    }

    // Address

    public static class Load extends Inst {
        public final Type type;
        public final Dest dst;
        public final Addr addr;

        public Load(Type t, Dest d, Addr a) {
            type = t;
            dst = d;
            addr = a;
        }

        public String toString() {
            return line(true, " " + dst + " = " + addr + type + "\n");
        }

        void gen() {
            X86.Reg mdest = dst.gen_dest_operand();
            if (mdest == null)  // dead assignment
                return;
            X86.Operand a = addr.gen_addr_operand(tempReg1);
            if (type == Type.BOOL)
                X86.emit2("movzbq", a, mdest);
            else if (type == Type.INT)
                X86.emit2("movslq", a, mdest);
            else // (type == Type.PTR)
                X86.emit2("movq", a, mdest);
        }

        Set<Reg> used() {
            Set<Reg> s = new HashSet<Reg>();
            addr.base.addTo(s);
            return s;
        }

        Set<Reg> defined() {
            Set<Reg> s = new HashSet<Reg>();
            dst.addTo(s);
            return s;
        }
    }

    // Operands

    public static class Store extends Inst {
        public final Type type;
        public final Addr addr;
        public final Src src;

        public Store(Type t, Addr a, Src s) {
            type = t;
            addr = a;
            src = s;
        }

        public String toString() {
            return line(true, " " + addr + type + " = " + src + "\n");
        }

        void gen() {
            X86.Operand s = src.gen_source_operand(true, tempReg1);
            if (s instanceof X86.Reg)
                s = X86.resize_reg(type.X86_size, (X86.Reg) s);
            X86.Operand a = addr.gen_addr_operand(tempReg2);
            X86.emitMov(type.X86_size, s, a);
        }

        Set<Reg> used() {
            Set<Reg> s = new HashSet<Reg>();
            src.addTo(s);
            addr.base.addTo(s);
            return s;
        }

        Set<Reg> defined() {
            return new HashSet<Reg>();
        }
    }

    public static class Call extends Inst {
        public final CallTgt tgt;
        public final boolean ind;    // true if indirect
        public final Src[] args;
        public final Dest rdst;    // could be null

        public Call(CallTgt f, boolean b, Src[] a, Dest r) {
            tgt = f;
            ind = b;
            args = a;
            rdst = r;
        }

        public Call(CallTgt f, boolean b, List<Src> al, Dest r) {
            this(f, b, al.toArray(new Src[0]), r);
        }

        public Call(CallTgt f, boolean b, List<Src> al) {
            this(f, b, al.toArray(new Src[0]), null);
        }

        public String toString() {
            String arglist = "(";
            if (args.length > 0) {
                arglist += args[0];
                for (int i = 1; i < args.length; i++)
                    arglist += ", " + args[i];
            }
            arglist += ")";
            String retstr = (rdst == null) ? " " : " " + rdst + " = ";
            return line(true, retstr + "call " + (ind ? "* " : "") +
                    tgt + arglist + "\n");
        }

        void gen() {
            // Just fail if there are more than 6 args.
            int argCount = args.length;
            assert (argCount <= X86.argRegs.length);
            // Save any used caller-save registers to the stack
            int callerSaveSize = 0;
            for (X86.Reg r : X86.callerSaveRegs)
                if (liveOver(r, irPtr, rdst)) {
                    X86.emit1("pushq", r);
                    callerSaveSize += X86.Size.Q.bytes;
                }
            if ((callerSaveSize % (2 * X86.Size.Q.bytes)) != 0)
                X86.emit2("subq", new X86.Imm(X86.Size.Q.bytes), X86.RSP);
            // If indirect jump through a register, make
            // sure this is not an argument register that
            // we're about to overwrite!
            X86.Operand call_target = null;
            if (ind) {
                if (tgt instanceof Reg) {
                    call_target = env.get(tgt);
                    assert (call_target != null); // dead value surely isn't used as a source
                    for (int i = 0; i < argCount; i++)
                        if (call_target.equals(X86.argRegs[i])) {
                            X86.emitMov(X86.Size.Q, call_target, tempReg2);
                            call_target = tempReg2;
                            break;
                        }
                } else //  tgt instanceof Global
                    call_target = new X86.AddrName(((Global) tgt).toString());
            }
            // Move arguments into the argument regs.
            // First do parallel move of register sources.
            X86.Reg src[] = new X86.Reg[argCount];
            X86.Reg dst[] = new X86.Reg[argCount];
            boolean moved[] = new boolean[argCount]; // initialized to false
            int n = 0;
            for (int i = 0; i < argCount; i++) {
                Src s = args[i];
                if (s instanceof Reg) {
                    X86.Operand rand = env.get((Reg) s);
                    if (rand instanceof X86.Reg) {
                        src[n] = (X86.Reg) rand;
                        dst[n] = X86.argRegs[i];
                        n++;
                        moved[i] = true;
                    }
                }
            }
            new X86.ParallelMover(n, src, dst, tempReg1).move();
            // Now handle any immediate sources.
            for (int i = 0; i < argCount; i++)
                if (!moved[i]) {
                    X86.Operand r = args[i].gen_source_operand(true, X86.argRegs[i]);
                    X86.emitMov(X86.Size.Q, r, X86.argRegs[i]);
                }
            if (ind) {
                X86.emit1("call *", call_target);
            } else
                X86.emit1("call", new X86.GLabel((((Global) tgt).toString())));
            if (rdst != null) {
                X86.Reg r = rdst.gen_dest_operand();
                X86.emitMov(X86.Size.Q, X86.RAX, r);
            }
            // Restore any used caller-save registers (in reverse order)
            if ((callerSaveSize % (2 * X86.Size.Q.bytes)) != 0)
                X86.emit2("addq", new X86.Imm(X86.Size.Q.bytes), X86.RSP);
            for (int i = X86.callerSaveRegs.length - 1; i >= 0; i--) {
                X86.Reg r = X86.callerSaveRegs[i];
                if (liveOver(r, irPtr, rdst))
                    X86.emit1("popq", r);
            }
        }

        Set<Reg> used() {
            Set<Reg> s = new HashSet<Reg>();
            tgt.addTo(s);
            for (Src a : args)
                a.addTo(s);
            return s;
        }

        Set<Reg> defined() {
            Set<Reg> s = new HashSet<Reg>();
            if (rdst != null)
                rdst.addTo(s);
            return s;
        }
    }

    public static class Return extends Inst {
        public final Src val;    // could be null

        public Return() {
            val = null;
        }

        public Return(Src s) {
            val = s;
        }

        public String toString() {
            return line(true, " return " + (val == null ? "" : val) + "\n");
        }

        void gen() {
            // return value if any
            if (val != null) {
                X86.Operand r = val.gen_source_operand(true, X86.RAX);
                X86.emitMov(X86.Size.Q, r, X86.RAX);
            }
            // exit sequence
            // pop the frame
            if (frameSize != 0)
                X86.emit2("addq", new X86.Imm(frameSize), X86.RSP);
            // restore any callee save registers
            for (int i = X86.calleeSaveRegs.length - 1; i >= 0; i--) {
                X86.Reg r = X86.calleeSaveRegs[i];
                if (env.containsValue(r))
                    X86.emit1("popq", r);
            }
            // and we're done
            X86.emit0("ret");
        }

        Set<Reg> used() {
            Set<Reg> s = new HashSet<Reg>();
            if (val != null)
                val.addTo(s);
            return s;
        }

        Set<Reg> defined() {
            return new HashSet<Reg>();
        }
    }

    public static class CJump extends Inst {
        public final RelOP op;
        public final Src src1, src2;
        public final Label lab;

        public CJump(RelOP o, Src s1, Src s2, Label l) {
            op = o;
            src1 = s1;
            src2 = s2;
            lab = l;
        }

        public String toString() {
            return line(true, " if " + src1 + " " + op + " " + src2 +
                    " goto " + lab + "\n");
        }

        void gen() {
            // remember: left and right are switched under gnu assembler
            X86.Operand mleft = src1.gen_source_operand(false, tempReg1);
            X86.Operand mright = src2.gen_source_operand(true, tempReg2);
            X86.emit2("cmp" + X86.Size.Q, mright, mleft);
            X86.emit0("j" + op.X86_name() + " F" + funcNumber + "_" + lab.name);
        }

        Set<Reg> used() {
            Set<Reg> s = new HashSet<Reg>();
            src1.addTo(s);
            src2.addTo(s);
            return s;
        }

        Set<Reg> defined() {
            return new HashSet<Reg>();
        }
    }

    public static class Jump extends Inst {
        public final Label lab;

        public Jump(Label l) {
            lab = l;
        }

        public String toString() {
            return line(true, " goto " + lab + "\n");
        }

        void gen() {
            X86.emit0("jmp F" + funcNumber + "_" + lab.name);
        }

        Set<Reg> used() {
            return new HashSet<Reg>();
        }

        Set<Reg> defined() {
            return new HashSet<Reg>();
        }
    }

    public static class LabelDec extends Inst {
        public final String name;

        public LabelDec(String s) {
            name = s;
        }

        public String toString() {
            return line(true, name + ":\n");
        }

        void gen() {
            X86.emitLabel(new X86.Label("F" + funcNumber + "_" + name));
        }

        Set<Reg> used() {
            return new HashSet<Reg>();
        }
        Set<Reg> defined() {
            return new HashSet<Reg>();
        }
    }

    public static class Label {
        static int labelnum=0;
        public String name;

        public Label() { name = "L" + labelnum++; }
        public Label(String s) { name = s; }
        public void set(String s) { name = s; }
        public String toString() { return name; }
    }

    public static class Addr {   // Memory at base + offset
        public final Src base;
        public final int offset;

        public Addr(Src b) { base=b; offset=0; }
        public Addr(Src b, int o) { base=b; offset=o; }
        public String toString() {
            return "" + ((offset == 0) ? "" : offset) + "[" + base + "]";
        }
        X86.Operand gen_addr_operand(X86.Reg temp) {
            X86.Operand xbase = base.gen_source_operand(false,temp);
            assert (xbase instanceof X86.Reg);
            return new X86.Mem((X86.Reg) xbase, offset);
        }
    }

    public static class Id implements Reg, Src, Dest, CallTgt {
        public final String name;

        public Id(String s) {
            name = s;
        }

        public String toString() {
            return name;
        }

        public boolean equals(Object l) {
            return (l instanceof Id && (((Id) l).name.equals(name)));
        }

        public int hashCode() {
            return name.hashCode();
        }

        public X86.Operand gen_source_operand(boolean imm_ok, X86.Reg temp) {
            X86.Reg mrand = env.get(this);
            assert (mrand != null); // dead value surely isn't used as a source
            return mrand;
        }

        public X86.Reg gen_dest_operand() {
            if (!liveOutSets.get(irPtr).contains(this))
                return null;
            return env.get(this);
        }

        public void addTo(Set<Reg> s) {
            s.add(this);
        }
    }

    public static class Temp implements Reg, Src, Dest, CallTgt  {
        private static int cnt=0;
        public final int num;

        public Temp(int n) { num=n; }
        public Temp() { num = ++Temp.cnt; }
        public static void reset() { Temp.cnt = 0; }
        public static int getcnt() { return Temp.cnt; }
        public String toString() { return "t" + num; }
        public boolean equals(Object l) {
            return (l instanceof Temp && (((Temp) l).num == num));
        }
        public int hashCode() {
            return num;
        }

        public X86.Operand gen_source_operand(boolean imm_ok, X86.Reg temp) {
            X86.Reg mrand = env.get(this);
            assert (mrand != null); // dead value surely isn't used as a source
            return mrand;
        }
        public X86.Reg gen_dest_operand() {
            if (!liveOutSets.get(irPtr).contains(this))
                return null;
            return env.get(this);
        }
        public void addTo(Set<Reg> s) {
            s.add(this);
        }
    }

    public static class Global implements Src, CallTgt, Const {
        public final String name;

        public Global(String s) {
            name = s;
        }

        public String toString() {
            return "_" + name;
        }  // should be environment dependent

        public void gen_const() {
            X86.emit1(".quad", new X86.GLabel(toString()));
        }

        public X86.Operand gen_source_operand(boolean imm_ok, X86.Reg temp) {
            X86.AddrName mrand = new X86.AddrName(toString());
            X86.emit2("leaq", mrand,temp);
            return temp;
        }
        public void addTo(Set<Reg> s) {
        }
    }

    public static class IntLit implements Src, Const {
        public final int i;

        public IntLit(int v) {
            i = v;
        }

        public String toString() {
            return i + "";
        }

        public void gen_const() {
            X86.emit0(".long" + toString());
        }

        public X86.Operand gen_source_operand(boolean imm_ok, X86.Reg temp) {
            X86.Imm mrand = new X86.Imm(i);
            if (imm_ok)
                return mrand;
            else {
                X86.emitMov(X86.Size.Q, mrand, temp);
                return temp;
            }
        }

        public void addTo(Set<Reg> s) {
        }
    }


    // X86 CodeGen utilities

    public static class BoolLit implements Src {
        public final boolean b;

        public BoolLit(boolean v) {
            b = v;
        }

        public String toString() {
            return b + "";
        }

        public X86.Operand gen_source_operand(boolean imm_ok, X86.Reg temp) {
            X86.Imm mrand = new X86.Imm(b ? 1 : 0);
            if (imm_ok)
                return mrand;
            else {
                X86.emitMov(X86.Size.Q, mrand, temp);
                return temp;
            }
        }

        public void addTo(Set<Reg> s) {
        }
    }

    public static class StrLit implements Src {
        public final String s;

        public StrLit(String v) {
            s = v;
        }

        public String toString() {
            return "\"" + s + "\"";
        }

        public X86.Operand gen_source_operand(boolean imm_ok, X86.Reg temp) {
            X86.AddrName mrand = new X86.AddrName("_S" + stringLiterals.size());
            stringLiterals.add(s);
            X86.emit2("leaq", mrand,temp);
            return temp;
        }
        public void addTo(Set<Reg> s) {
        }
    }
}



