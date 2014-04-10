//____________________________________________________________________________
// Expr ::= Var
//        |  Int
//        |  Expr + Expr
//        |  Expr - Expr

abstract class Expr {
    abstract Value eval(Env env);

    abstract String show();

    Env evalRef(Env env) {
        // Store the expression in a new ValEnv and return
        // that as the reference:
        return new ValEnv("", eval(env), null);
    }
}

class Var extends Expr {
    private String name;

    Var(String name) {
        this.name = name;
    }

    Value eval(Env env) {
        return Env.lookup(env, name).getValue();
    }

    String show() {
        return name;
    }
}

class Int extends Expr {
    private int num;

    Int(int num) {
        this.num = num;
    }

    Value eval(Env env) {
        return new IValue(num);
    }

    String show() {
        return Integer.toString(num);
    }
}

class Plus extends Expr {
    private Expr l, r;

    Plus(Expr l, Expr r) {
        this.l = l;
        this.r = r;
    }

    Value eval(Env env) {
        return new IValue(l.eval(env).asInt() + r.eval(env).asInt());
    }

    String show() {
        return "(" + l.show() + " + " + r.show() + ")";
    }
}

class Mult extends Expr {
    private Expr l, r;

    Mult(Expr l, Expr r) {
        this.l = l;
        this.r = r;
    }

    Value eval(Env env) {
        return new IValue(l.eval(env).asInt() * r.eval(env).asInt());
    }

    String show() {
        return "(" + l.show() + " * " + r.show() + ")";
    }
}

class Minus extends Expr {
    private Expr l, r;

    Minus(Expr l, Expr r) {
        this.l = l;
        this.r = r;
    }

    Value eval(Env env) {
        return new IValue(l.eval(env).asInt() - r.eval(env).asInt());
    }

    String show() {
        return "(" + l.show() + " - " + r.show() + ")";
    }
}

//____________________________________________________________________________
// Expr ::= Expr < Expr
//        |  Expr == Expr
/*
abstract class Expr {
  abstract boolean eval(Env env);
  abstract String  show();
}
*/
class LT extends Expr {
    private Expr l, r;

    LT(Expr l, Expr r) {
        this.l = l;
        this.r = r;
    }

    Value eval(Env env) {
        return new BValue(l.eval(env).asInt() < r.eval(env).asInt());
    }

    String show() {
        return "(" + l.show() + " < " + r.show() + ")";
    }
}

class EqEq extends Expr {
    private Expr l, r;

    EqEq(Expr l, Expr r) {
        this.l = l;
        this.r = r;
    }

    Value eval(Env env) {
        return new BValue(l.eval(env).asInt() == r.eval(env).asInt());
    }

    String show() {
        return "(" + l.show() + " == " + r.show() + ")";
    }
}

class Lambda extends Expr {
    private String var;
    private Expr   body;
    Lambda(String var, Expr body) { this.var = var; this.body = body; }

    Value  eval(Env env) {
        return new FValue(env, var, body);
    }

    String show() { return "(\\" + var + " -> " + body.show() + ")"; }
}

class Apply extends Expr {
    private Expr fun, arg;
    Apply(Expr fun, Expr arg) { this.fun = fun; this.arg = arg; }

    Value  eval(Env env) {
        return fun.eval(env).enter(arg.eval(env));
    }

    String show() { return "(" + fun.show() + " @ " + arg.show() + ")"; }
}

//____________________________________________________________________________
// Stmt  ::= Seq Stmt Stmt
//        |  Var := Expr
//        |  While Expr Stmt
//        |  If Expr Stmt Stmt
//        |  Print Expr

abstract class Stmt {
    static void indent(int ind) {
        for (int i = 0; i < ind; i++) {
            System.out.print(" ");
        }
    }

    abstract Env exec(Program prog, Env env);

    abstract void print(int ind);
}

class Seq extends Stmt {
    private Stmt l, r;

    Seq(Stmt l, Stmt r) {
        this.l = l;
        this.r = r;
    }

    Env exec(Program prog, Env env) {
        return r.exec(prog, l.exec(prog, env));
    }

    void print(int ind) {
        l.print(ind);
        r.print(ind);
    }
}

class Assign extends Stmt {
    private String lhs;
    private Expr rhs;

    Assign(String lhs, Expr rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    Env exec(Program prog, Env env) {
        Env.lookup(env, lhs).setValue(rhs.eval(env));
        return env;
    }
    /*
    void exec(Env env) {
        mem.store(lhs, rhs.eval(env));
    }
    */

    void print(int ind) {
        indent(ind);
        System.out.println(lhs + " = " + rhs.show() + ";");
    }
}

class While extends Stmt {
    private Expr test;
    private Stmt body;

    While(Expr test, Stmt body) {
        this.test = test;
        this.body = body;
    }

    Env exec(Program prog, Env env) {
        while (test.eval(env).asBool()) {
            body.exec(prog, env);
        }
        return env;
    }

    void print(int ind) {
        indent(ind);
        System.out.println("while (" + test.show() + ") {");
        body.print(ind + 2);
        indent(ind);
        System.out.println("}");
    }
}

class If extends Stmt {
    private Expr test;
    private Stmt t, f;

    If(Expr test, Stmt t, Stmt f) {
        this.test = test;
        this.t = t;
        this.f = f;
    }

    Env exec(Program prog, Env env) {
        if (test.eval(env).asBool()) {
            t.exec(prog, env);
        } else {
            f.exec(prog, env);
        }
        return env;
    }

    void print(int ind) {
        indent(ind);
        System.out.println("if (" + test.show() + ") {");
        t.print(ind + 2);
        indent(ind);
        System.out.println("} else {");
        f.print(ind + 2);
        indent(ind);
        System.out.println("}");
    }
}

class Print extends Stmt {
    private Expr exp;

    Print(Expr exp) {
        this.exp = exp;
    }

    Env exec(Program prog, Env env) {
        System.out.println("Output: " + exp.eval(env).asInt());
        return env;
    }

    void print(int ind) {
        indent(ind);
        System.out.println("print " + exp.show() + ";");
    }
}

// Add this code to the Src.java file:
abstract class Value {
    abstract String show();

    // bool Value:
    boolean asBool() {
        System.out.println("ABORT: Boolean value expected");
        System.exit(1);
        return true; // Not reached
    }

    // int Value:
    int asInt() {
        System.out.println("ABORT: Int value expected");
        System.exit(1);
        return 0; // Not reached
    }

    Value enter(Value val) {
        System.out.println("ABORT: First-class function expected");
        System.exit(1);
        return null;   // Not reached
    }
}

class FValue extends Value {
    private Env    env;
    private String arg;
    private Expr   body;

    FValue(Env env, String arg, Expr body) {
        this.env = env; this.arg = arg; this.body = body;
    }

    Value enter(Value val) {
        return body.eval(new ValEnv(arg, val, env));
    }

    String show() {
        return "<function>";
    }
}

class BValue extends Value {
    private boolean b;

    BValue(boolean b) {
        this.b = b;
    }

    String show() {
        return Boolean.toString(b);
    }

    boolean asBool() {
        return b;
    }
}

class IValue extends Value {
    private int i;

    IValue(int i) {
        this.i = i;
    }

    String show() {
        return Integer.toString(i);
    }

    int asInt() {
        return i;
    }
}

class VarDecl extends Stmt {
    private String var;
    private Expr expr;

    VarDecl(String var, Expr expr) {
        this.var = var;
        this.expr = expr;
    }

    Env exec(Program prog, Env env) {
        return new ValEnv(var, expr.eval(env), env);
    }

    void print(int ind) {
        indent(ind);
        System.out.println("var " + var + " = " + expr.show() + ";");
    }
}

class Program {
    private Proc[] procs;
    private Stmt body;

    Program(Proc[] procs, Stmt body) {
        this.procs = procs; this.body = body;
    }

    Program(Stmt body) {
        this(new Proc[] {}, body);
    }

    void run() {
        body.exec(this, null);
    }

    int index = 0;
    void print() {
        for(Proc p : procs) {
            p.print(index);
            index++;
        }
        body.print(4);
        System.out.println();
    }

    void call(Env env, String name, Expr[] actuals) {
        for (int i=0; i<procs.length; i++) {
            if (name.equals(procs[i].getName())) {
                procs[i].call(this, env, actuals);
                return;
            }
        }
        System.out.println("ABORT: Cannot find function " + name);
        System.exit(1);
    }
}

class Proc {
    private String   name;
    private Formal[] formals;
    private Stmt     body;

    Proc(String name, Formal[] formals, Stmt body) {
        this.name = name; this.formals = formals; this.body = body;
    }

    String getName() { return name; }

    void print(int ind) {
        Stmt.indent(ind);
        System.out.print("procedure " + name + "(");
        for (int i=0; i<formals.length; i++) {
            if (i>0) {
                System.out.print(", ");
            }
            System.out.print(formals[i]);
        }
        System.out.println(") {");

        body.print(ind+2);

        Stmt.indent(ind);
        System.out.println("}");
    }

    // This goes in the Proc class:
    void call(Program prog, Env env, Expr[] actuals) {
        if (actuals.length!=formals.length) {
            System.out.println("ABORT: Wrong number of arguments for " + name);
            System.exit(1);
        }
        Env newenv = null;
        for (int i=0; i<actuals.length; i++) {
            newenv = formals[i].extend(env, actuals[i], newenv);
        }
        body.exec(prog, newenv);
    }
}



class Call extends Stmt {
    private String name;
    private Expr[] actuals;
    Call(String name, Expr[] actuals) {
        this.name = name; this.actuals = actuals;
    }

    Env exec(Program prog, Env env) {
        prog.call(env, name, actuals);
        return env;
    }

    void print(int ind) {
        indent(ind);
        // TODO: fill this in if you want to see calls in the output!
    }
}

class Formal {
    protected String name;

    Formal(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    Env extend(Env env, Expr expr, Env newenv) {
        return new RefEnv(name, expr.evalRef(env), newenv);
    }
}

class ByRef extends Formal {
    ByRef(String name) { super(name); }

    public String toString() {
        return "ref " + name;
    }
}