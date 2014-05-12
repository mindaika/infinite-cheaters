// This is supporting software for CS322 Compilers and Language Design II
// Copyright (c) Portland State University
// 
// IR code generator for miniJava's AST.
//
// (Starting version.)
//

import ast.Ast;
import ast.astParser;
import ir.IR;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class IRGen {

    static class GenException extends Exception {
        public GenException(String msg) {
            super(msg);
        }
    }

    //------------------------------------------------------------------------------
    // Class Information Record
    //-------------------------
    //  For keeping all useful information about a class declaration
    //  for later use in the codegen.
    //
    static class ClassInfo {
        String name;            // class name
        ClassInfo parent;            // ptr to parent's record
        boolean isMainClass;        // true if class contains "main"
        Ast.ClassDecl classDecl;        // class source ast
        ArrayList<String> vtable;        // (virtual) method table
        HashMap<String, Integer> offsets;    // field variable offsets
        int objSize;            // object size

        // Constructor -- clone a parent's record
        //
        ClassInfo(Ast.ClassDecl cdecl, ClassInfo parent) {
            this.name = cdecl.nm;
            this.parent = parent;
            this.isMainClass = false;
            this.classDecl = cdecl;
            this.vtable = new ArrayList<String>(parent.vtable);
            this.offsets = new HashMap<String, Integer>(parent.offsets);
            this.objSize = parent.objSize;
        }

        // Constructor -- create a new record
        //
        ClassInfo(Ast.ClassDecl cdecl) {
            this.name = cdecl.nm;
            this.parent = null;
            this.isMainClass = false;
            this.classDecl = cdecl;
            this.vtable = new ArrayList<String>();
            this.offsets = new HashMap<String, Integer>();
            this.objSize = IR.Type.PTR.size;    // reserve space for ptr to class
        }

        // Utility Routines
        // ----------------
        // For accessing information stored in class information record
        //

        // Find method's base class record
        //
        ClassInfo methodBaseClass(String mname) throws Exception {
            for (Ast.MethodDecl mdecl : classDecl.mthds)
                if (mdecl.nm.equals(mname))
                    return this;
            if (parent != null)
                return parent.methodBaseClass(mname);
            throw new GenException("Can't find base class for method " + mname);
        }

        // Find method's return type
        //
        Ast.Type methodType(String mname) throws Exception {
            for (Ast.MethodDecl mdecl : classDecl.mthds)
                if (mdecl.nm.equals(mname))
                    return mdecl.t;
            if (parent != null)
                return parent.methodType(mname);
            throw new GenException("Can't find MethodDecl for method " + mname);
        }

        // Return method's vtable offset
        //
        int methodOffset(String mname) {
            return vtable.indexOf(mname) * IR.Type.PTR.size;
        }

        // Find field variable's type
        //
        Ast.Type fieldType(String fname) throws Exception {
            for (Ast.VarDecl fdecl : classDecl.flds) {
                if (fdecl.nm.equals(fname))
                    return fdecl.t;
            }
            if (parent != null)
                return parent.fieldType(fname);
            throw new GenException("Can't find VarDecl for field " + fname);
        }

        // Return field variable's offset
        //
        int fieldOffset(String fname) {
            return offsets.get(fname);
        }

        public String toString() {
            return "ClassInfo: " + " " + name + " " + parent + " " + isMainClass
                    + " " + vtable + " " + offsets + " " + objSize + " " + classDecl;
        }
    }

    // End of Class Information Record ---------------------------------------------


    //------------------------------------------------------------------------------
    // Other Supporting Data Structures
    //---------------------------------

    // CodePack
    // --------
    // For returning <type,src,code> tuple from gen routines
    //
    static class CodePack {
        Ast.Type type;
        IR.Src src;
        List<IR.Inst> code;

        CodePack(Ast.Type type, IR.Src src, List<IR.Inst> code) {
            this.type = type;
            this.src = src;
            this.code = code;
        }

        CodePack(Ast.Type type, IR.Src src) {
            this.type = type;
            this.src = src;
            code = new ArrayList<IR.Inst>();
        }
    }

    // AddrPack
    // --------
    // For returning <type,addr,code> tuple from genAddr routines
    //
    static class AddrPack {
        Ast.Type type;
        IR.Addr addr;
        List<IR.Inst> code;

        AddrPack(Ast.Type type, IR.Addr addr, List<IR.Inst> code) {
            this.type = type;
            this.addr = addr;
            this.code = code;
        }
    }

    // Env
    // ---
    // For keeping track of local variables and parameters and for finding
    // their types.
    //
    private static class Env extends HashMap<String, Ast.Type> {
    }

    // End of Other Supporting Data Structures -------------------------------------


    //------------------------------------------------------------------------------
    // Global Variables
    // ----------------
    //

    // The whole collection of ClassInfo records
    private static HashMap<String, ClassInfo> classInfos
            = new HashMap<String, ClassInfo>();

    // IR code representation of the current object
    private static IR.Src thisObj = new IR.Id("obj");

    // Constant nodes for convenience
    private static final Ast.Type AstIntType = new Ast.IntType();
    private static final Ast.Type AstBoolType = new Ast.BoolType();


    //------------------------------------------------------------------------------
    // The Main Codegen Routine
    //-------------------------
    //
    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            FileInputStream stream = new FileInputStream(args[0]);
            Ast.Program p = new astParser(stream).Program();
            stream.close();
            IR.Program ir = IRGen.gen(p);
            System.out.print(ir.toString());
        } else {
            System.out.println("You must provide an input file name.");
        }
    }

    // Sort ClassDecls, so that parent will be visited before children.
    //
    private static Ast.ClassDecl[] topoSort(Ast.ClassDecl[] classes) {
        List<Ast.ClassDecl> cl = new ArrayList<Ast.ClassDecl>();
        Vector<String> done = new Vector<String>();
        int cnt = classes.length;
        while (cnt > 0) {
            for (Ast.ClassDecl cd : classes)
                if (!done.contains(cd.nm)
                        && ((cd.pnm == null) || done.contains(cd.pnm))) {
                    cl.add(cd);
                    done.add(cd.nm);
                    cnt--;
                }
        }
        return cl.toArray(new Ast.ClassDecl[0]);
    }

    // Create class info record

    private static ClassInfo createClassInfo(Ast.ClassDecl n) throws Exception {
        // 1. If parent exists, clone parent's record; otherwise create a new one
        ClassInfo cinfo = (n.pnm != null) ?
                new ClassInfo(n, classInfos.get(n.pnm)) : new ClassInfo(n);

        // 2. Walk the MethodDecl list. If a method is not in the v-table, add it in;
        for (Ast.MethodDecl m : n.mthds) {
            if (!cinfo.vtable.contains(m)) {
                cinfo.vtable.add(m.nm);
            }
        }

        // 3  If the "main" method is in the list, set 'isMainClass' flag to true
        if (cinfo.vtable.contains("main")) {
            cinfo.isMainClass = true;
        }

        // 4. Compute offset values for field variables
        int offsetCounter = 8;
        int lastObject = 0;
        for (Ast.VarDecl v : n.flds) {
            IR.Type tempType = gen(v.t);
            offsetCounter += tempType.size;
            cinfo.offsets.put(v.nm, offsetCounter);
            lastObject = tempType.size;
        }

        // 5. Decide object's size
        cinfo.objSize = offsetCounter + lastObject;
        return cinfo;
    }// TODO: Check

    //------------------------------------------------------------------------------
    // Codegen Routines for Individual AST Nodes
    //------------------------------------------

    // Program ---
    // ClassDecl[] classes;
    //
    // Three passes over class decls:
    //  0. topo-sort class decls
    //  1. create class info records
    //  2. generate IR code
    //     2.1 generate list of static data (i.e. class descriptors)
    //     2.2 generate list of functions
    //
    public static IR.Program gen(Ast.Program n) throws Exception {
        Ast.ClassDecl[] classes = topoSort(n.classes);
        ClassInfo cinfo;
        for (Ast.ClassDecl c : classes) {
            cinfo = createClassInfo(c);
            classInfos.put(c.nm, cinfo);
        }
        List<IR.Data> allData = new ArrayList<IR.Data>();
        List<IR.Func> allFuncs = new ArrayList<IR.Func>();
        for (Ast.ClassDecl c : classes) {
            cinfo = classInfos.get(c.nm);
            IR.Data data = genData(c, cinfo);
            List<IR.Func> funcs = gen(c, cinfo);
            if (data != null)
                allData.add(data);
            allFuncs.addAll(funcs);
        }
        return new IR.Program(allData, allFuncs);
    }

    // ClassDecl ---
    // String nm, pnm;
    // VarDecl[] flds;
    // MethodDecl[] mthds;
    //

    // 1. Generate static data
    //
    // Codegen Guideline:

    //
    // (Skip this method if class is the static class containing "main".)
    //
    static IR.Data genData(Ast.ClassDecl n, ClassInfo cinfo) throws Exception {
        // Skip if 'Main' class
        if (cinfo.isMainClass) {
            return null;
        }

        //   1.1 For each method in class's vtable, construct a global label of form
        //       "<base class name>_<method name>" and save it in an IR.Global node
        List<IR.Global> globalList = new ArrayList<IR.Global>();
        for (Ast.MethodDecl m : n.mthds) {
            globalList.add(new IR.Global(n.pnm + "_" + m.nm));
        }

        //   1.2 Assemble the list of IR.Global nodes into an IR.Data node with a
        //       global label "class_<class name>"
        return new IR.Data(new IR.Global("class_" + cinfo.name), globalList.size(), globalList);
    } // TODO: Check

    //
    // Codegen Guideline:
    //   Straightforward -- generate a IR.Func for each mthdDecl.
    //
    static List<IR.Func> gen(Ast.ClassDecl n, ClassInfo cinfo) throws Exception {
        List<IR.Func> funcYtown = new ArrayList<IR.Func>();

        // 2. Generate code
        for (Ast.MethodDecl m : n.mthds) {
            funcYtown.add(gen(m, cinfo));
        }
        return funcYtown;
    } // TODO: Check

    // MethodDecl ---
    // Type t;
    // String nm;
    // Param[] params;
    // VarDecl[] vars;
    // Stmt[] stmts;
    //
    // Codegen Guideline:

    //
    static IR.Func gen(Ast.MethodDecl n, ClassInfo cinfo) throws Exception {
        List<IR.Inst> instList = new ArrayList<IR.Inst>();
        List<String> paramps = new ArrayList<String>();
        List<String> varps = new ArrayList<String>();
        IR.LabelDec begin = new IR.LabelDec("Begin");
        IR.LabelDec end = new IR.LabelDec("End");
        instList.add(begin);
        IR.LabelDec glbllbl;

        // (Skip these two steps if method is "main".)
        if (!cinfo.isMainClass) {
            // 1. Construct a global label of form "<base class name>_<method name>"
            glbllbl = new IR.LabelDec(cinfo.name + "_" + n.nm);

            // 2. Add "obj" into the params list as the 0th item
            n.params[0] = new Ast.Param(n.t, n.nm);
        }

        // 3. Create an Env() containing all params and all local vars
        Env newEnv = new Env();
        for (Ast.Param p : n.params) {
            newEnv.put(p.nm, p.t);
            paramps.add(p.nm);
        }

        for (Ast.VarDecl v : n.vars) {
            newEnv.put(v.nm, v.t);
            varps.add(v.nm);
        }

        // 4. Generate IR code for all statements
        for (Ast.Stmt s : n.stmts) {
            instList.addAll(gen(s, cinfo, newEnv));
            //instList.add(glbllbl);
        }
        // 5. Return an IR.Func with the above
        instList.add(end);

        return new IR.Func(n.nm, paramps, varps, instList);
    }// TODO: Check

    // VarDecl ---
    // Type t;
    // String nm;
    // Exp init;
    //

    private static List<IR.Inst> gen(Ast.VarDecl n, ClassInfo cinfo,
                                     Env env) throws Exception {
        List<IR.Inst> code = new ArrayList<IR.Inst>();

        // 1. If init exp exists, generate IR code for it and assign result to var
        if (n.init != null) {
            IR.Id id = new IR.Id(n.nm);
            CodePack p = gen(n.init, cinfo, env);
            IR.Move move = new IR.Move((IR.Dest) id, p.src);
            code.addAll(p.code);
            code.add(move);
        }

        // 2. Return generated code (or null if none)
        return (n.init == null ? null :  code);
    }// TODO: Check

    // STATEMENTS

    // Dispatch a generic call to a specific Stmt routine
    //
    static List<IR.Inst> gen(Ast.Stmt n, ClassInfo cinfo, Env env) throws Exception {
        if (n instanceof Ast.Block) return gen((Ast.Block) n, cinfo, env);
        else if (n instanceof Ast.Assign) return gen((Ast.Assign) n, cinfo, env);
        else if (n instanceof Ast.CallStmt) return gen((Ast.CallStmt) n, cinfo, env);
        else if (n instanceof Ast.If) return gen((Ast.If) n, cinfo, env);
        else if (n instanceof Ast.While) return gen((Ast.While) n, cinfo, env);
        else if (n instanceof Ast.Print) return gen((Ast.Print) n, cinfo, env);
        else if (n instanceof Ast.Return) return gen((Ast.Return) n, cinfo, env);
        throw new GenException("Illegal Ast Stmt: " + n);
    }

    // Block ---
    // Stmt[] stmts;
    //
    static List<IR.Inst> gen(Ast.Block n, ClassInfo cinfo, Env env) throws Exception {
        List<IR.Inst> code = new ArrayList<IR.Inst>();

        for (Ast.Stmt s : n.stmts) {
            code.addAll(gen(s, cinfo, env));
        }
        //    TODO: Generate code for HW2
        return code;


    }

    // Assign ---
    // Exp lhs, rhs;
    //
    // Codegen Guideline:
    // 1. call gen() on rhs
    // 2. if lhs is ID, check against Env to see if it's a local var or a param;
    //    if yes, generate an IR.Move instruction
    // 3. otherwise, call genAddr() on lhs, and generate an IR.Store instruction
    //
    static List<IR.Inst> gen(Ast.Assign n, ClassInfo cinfo, Env env) throws Exception {
        List<IR.Inst> code = new ArrayList<IR.Inst>();


        //    TODO: Generate code for HW2
        return code;


    }

    // CallStmt ---
    // Exp obj;
    // String nm;
    // Exp[] args;
    //
    //
    static List<IR.Inst> gen(Ast.CallStmt n, ClassInfo cinfo, Env env) throws Exception {
        if (n.obj != null) {
            CodePack p = handleCall(n.obj, n.nm, n.args, cinfo, env, false);
            return p.code;
        }
        throw new GenException("In CallStmt, obj is null " + n);
    }

    // handleCall
    // ----------
    // Common routine for Call and CallStmt nodes
    //
    // Codegen Guideline:
    // 1. Invoke gen() on obj, which returns obj's storage address (and type and code)
    // 2. With type info in the returning CodePack, figure out obj's base class
    // 3. Access the base class's ClassInfo rec to get the method's offset in vtable
    // 4. Add obj's as the 0th argument to the args list
    // 5. Generate an IR.Load to get the class descriptor from obj's storage
    // 6. Generate another IR.Load to get the method's global label
    // 7. If retFlag is set, prepare a temp for receiving return value; also figure
    //    out return value's type (through method's decl in ClassInfo rec)
    // 8. Generate an indirect call with the global label
    //
    static CodePack handleCall(Ast.Exp obj, String name, Ast.Exp[] args,
                               ClassInfo cinfo, Env env, boolean retFlag) throws Exception {


        //    TODO: Generate code for HW2
        return null;


    }

    // If ---
    // Exp cond;
    // Stmt s1, s2;
    //
    // (See class notes.)
    //
    static List<IR.Inst> gen(Ast.If n, ClassInfo cinfo, Env env) throws Exception {


        //    TODO: Generate code for HW2
        return null;


    }

    // While ---
    // Exp cond;
    // Stmt s;
    //
    // (See class notes.)
    //
    static List<IR.Inst> gen(Ast.While n, ClassInfo cinfo, Env env) throws Exception {


        //    TODO: Generate code for HW2
        return null;


    }

    // Print ---
    // Exp arg;
    //
    static List<IR.Inst> gen(Ast.Print n, ClassInfo cinfo, Env env) throws Exception {
        List<IR.Inst> code = new ArrayList<IR.Inst>();
        List<IR.Src> sources = new ArrayList<IR.Src>();

        if (n.arg == null) {
            // 1. If arg is null, generate an IR.Call with "print"
            CodePack p = gen(n.arg, cinfo, env);
            sources.add(p.src);
            code.addAll(p.code);
            code.add(new IR.Call(new IR.Id("print"), false, sources)); // TODO: Wrong-ish?
        } else if (n.arg instanceof Ast.StrLit) {
            // 2. If arg is StrLit, generate an IR.Call with "printStr"
            CodePack p = gen(n.arg, cinfo, env);
            sources.add(p.src);
            code.addAll(p.code);
            code.add(new IR.Call(new IR.Id("printStr"), false, sources));

        } else {
            // 3. Otherwise, generate IR code for arg, and use its type info
            //    to decide which of the two functions, "printInt" and "printBool",
            //    to call
            CodePack p = gen(n.arg, cinfo, env);
            sources.add(p.src);
            code.addAll(p.code);
            if (p.type instanceof Ast.IntType) {
                code.add(new IR.Call(new IR.Id("printInt"), false, sources));
            } else if (p.type instanceof Ast.BoolType) {
                code.add(new IR.Call(new IR.Id("printBool"), false, sources));
            }
            code.add(new IR.Call(null, true, sources));
        }
        return code;
    }

    // Return ---
    // Exp val;
    //
    // Codegen Guideline:

    //
    static List<IR.Inst> gen(Ast.Return n, ClassInfo cinfo, Env env) throws Exception {
        List<IR.Inst> code = new ArrayList<IR.Inst>();

        if (n.val != null) {
            // 1. If val is non-null, generate IR code for it, and generate an IR.Return
            //    with its value
            CodePack p = gen(n.val, cinfo, env);
            code.addAll(p.code);
            code.add(new IR.Return(new IR.Id("return")));
        } else {
            // 2. Otherwise, generate an IR.Return with no value
            CodePack p = gen(n.val, cinfo, env);
            code.addAll(p.code);
            code.add(new IR.Return());
        }
        // TODO: Verification
        return code;


    }

    // EXPRESSIONS

    // 1. Dispatch a generic gen call to a specific gen routine
    //
    static CodePack gen(Ast.Exp n, ClassInfo cinfo, Env env) throws Exception {
        if (n instanceof Ast.Call) return gen((Ast.Call) n, cinfo, env);
        if (n instanceof Ast.NewObj) return gen((Ast.NewObj) n, cinfo, env);
        if (n instanceof Ast.Field) return gen((Ast.Field) n, cinfo, env);
        if (n instanceof Ast.Id) return gen((Ast.Id) n, cinfo, env);
        if (n instanceof Ast.This) return gen((Ast.This) n, cinfo);
        if (n instanceof Ast.IntLit) return gen((Ast.IntLit) n);
        if (n instanceof Ast.BoolLit) return gen((Ast.BoolLit) n);
        if (n instanceof Ast.StrLit) return gen((Ast.StrLit) n);
        throw new GenException("Exp node not supported in this codegen: " + n);
    }

    // 2. Dispatch a generic genAddr call to a specific genAddr routine
    //    (Only one LHS Exp needs to be implemented for this assignment)
    //
    static AddrPack genAddr(Ast.Exp n, ClassInfo cinfo, Env env) throws Exception {
        if (n instanceof Ast.Field) return genAddr((Ast.Field) n, cinfo, env);
        throw new GenException(" LHS Exp node not supported in this codegen: " + n);
    }

    // Call ---
    // Exp obj;
    // String nm;
    // Exp[] args;
    //
    static CodePack gen(Ast.Call n, ClassInfo cinfo, Env env) throws Exception {
        if (n.obj != null)
            return handleCall(n.obj, n.nm, n.args, cinfo, env, true);
        throw new GenException("In Call, obj is null: " + n);
    }

    // NewObj ---
    // String cn;
    // Exp[] args; (ignored)
    //
    // Codegen Guideline:
    //  1. Use class name to find the corresponding ClassInfo record
    //  2. Find the class's type and object size from the ClassInfo record
    //  3. Cosntruct a malloc call to allocate space for the object
    //  4. Store a pointer to the class's descriptor into the first slot of
    //     the allocated space
    //
    static CodePack gen(Ast.NewObj n, ClassInfo cinfo, Env env) throws Exception {


        //    TODO: Generate code for HW2
        return null;


    }

    // Field ---
    // Exp obj;
    // String nm;
    //

    // 1. gen()
    //
    // Codegen Guideline:
    //   1.1 Call genAddr to generate field variable's address
    //   1.2 Add an IR.Load to get its value
    //
    static CodePack gen(Ast.Field n, ClassInfo cinfo, Env env) throws Exception {


        //    TODO: Generate code for HW2
        return null;


    }

    // 2. genAddr()
    //
    // Codegen Guideline:
    //   2.1 Call gen() on the obj component
    //   2.2 Use the type info to figure out obj's base class
    //   2.3 Access base class's ClassInfo rec to get field variable's offset
    //   2.4 Generate an IR.Addr based on the offset
    //
    static AddrPack genAddr(Ast.Field n, ClassInfo cinfo, Env env) throws Exception {


        //    TODO: Generate code for HW2
        return null;


    }

    // Id ---
    // String nm;
    //
    // Codegen Guideline:


    //
    static CodePack gen(Ast.Id n, ClassInfo cinfo, Env env) throws Exception {
        //  1. Check to see if the Id is in the env.
        if (env.containsKey(n.nm)) {
            //  2. If so, it means it is a local variable or a parameter. Just return
            //     a CodePack containing the Id.
            return new CodePack(new Ast.ObjType(cinfo.name), thisObj); // TODO: Probably wrong
        } else {
            //  3. Otherwise, the Id is an instance variable. Convert it into an
            //     Ast.Field node with Ast.This() as its obj, and invoke the gen routine
            //     on this new node
            Ast.Field instance = new Ast.Field(new Ast.This(), n.nm); // TODO: Totally not wrong. Totally.
            return gen(instance, cinfo, env);
        }
    }

    // This ---
    //
    static CodePack gen(Ast.This n, ClassInfo cinfo) throws Exception {
        return new CodePack(new Ast.ObjType(cinfo.name), thisObj);
    }

    // IntLit ---
    // int i;
    //
    static CodePack gen(Ast.IntLit n) throws Exception {
        return new CodePack(AstIntType, new IR.IntLit(n.i));
    }

    // BoolLit ---
    // boolean b;
    //
    static CodePack gen(Ast.BoolLit n) throws Exception {
        return new CodePack(AstBoolType, n.b ? IR.TRUE : IR.FALSE);
    }

    // StrLit ---
    // String s;
    //
    static CodePack gen(Ast.StrLit n) throws Exception {
        return new CodePack(null, new IR.StrLit(n.s));
    }

    // Type mapping (AST -> IR)
    //
    static IR.Type gen(Ast.Type n) throws Exception {
        if (n == null) return null;
        if (n instanceof Ast.IntType) return IR.Type.INT;
        if (n instanceof Ast.BoolType) return IR.Type.BOOL;
        if (n instanceof Ast.ObjType) return IR.Type.PTR;
        throw new GenException("Invalid Ast type: " + n);
    }

}
