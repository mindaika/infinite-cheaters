//____________________________________________________________________________
// Expr ::= Var
//        |  Int
//        |  Expr + Expr
//        |  Expr - Expr

import java.util.ArrayList;
import java.util.List;

abstract class Expr {
    abstract Value eval(Env env);

    abstract String show();

    Env evalRef(Env env) {
        // Store the expression in a new ValEnv and return
        // that as the reference:
        return new ValEnv("", eval(env), null);
    }
}

class Nil extends Expr {
    Value eval(Env env) {
        return new EmptyList();
    }

    String show() { return "[]"; }
}

class Cons extends Expr {
    private Value consList;

    Cons(Expr head, Expr tail) {
        if (tail.eval(null) instanceof LValue) {
            consList = new NonEmptyList(head.eval(null), (LValue)tail.eval(null));
        } else {
            throw new RuntimeException("ABORT: list value expected");
        }

    }

    String show() {
        return consList.show();
    }

    Value eval(Env env) {
        return consList;
    }
}

/* Expressions of the form nonEmpty(e), represented using a class
called NonEmpty.  The intention here is that evaluating an
expression nonEmpty(e) will return a Boolean that is true if the
argument e evaluates to a non-empty list, false if the argument
evaluates to an empty list, or triggers a run-time error message
(see below) if the argument does not produce a list value.*/
class NonEmpty extends Expr {
    private Value e;

    NonEmpty(Value e) {
        this.e = e;
    }

    @Override
    Value eval(Env env) {
        if (e instanceof LValue) {
            return new BValue(e.getClass() != EmptyList.class);
        } else {
            throw new RuntimeException("ABORT: list value expected");
        }
    }

    @Override
    String show() {
        return "How this work?";
    } //TODO: This probably needs changed
}

/*Expressions of the form head(e), represented using a class called
Head.  If e evaluates to a non-empty list, then head(e) will return
whatever value is stored at the head of that list.  In any other
case, however, head(e) will trigger a run-time error message.*/
class Head extends Expr {
    private Value e;

    Head(Value e) {
        if (e instanceof NonEmptyList) {
            this.e = e;
        } else if (e instanceof EmptyList) {
            throw new RuntimeException("ABORT: nonempty list value expected");
        } else {
            throw new RuntimeException("ABORT: list value expected");
        }
    }

    @Override
    Value eval(Env env) {
        // Sounds dirty, but is syntactically and semantically sound
        return ((NonEmptyList) e).getHead();
    }

    @Override
    String show() {
        return null; //TODO: Does this need to do something?
    }
}

/*Expressions of the form tail(e), represented using a class called
Tail.  This works much like head except that, if e evaluates to a
non-empty list, then tail(e) will return the tail of that list
value instead of the head.*/
class Tail extends Expr {
    private Value e;

    Tail(Value e) {
        if (e instanceof NonEmptyList) {
            this.e = e;
        } else if (e instanceof EmptyList) {
            throw new RuntimeException("ABORT: nonempty list value expected");
        } else {
                throw new RuntimeException("ABORT: list value expected");
        }
    }

    @Override
    Value eval(Env env) {
        // Still dirty; still correct
        return ((NonEmptyList) e).getTail();
    }

    @Override
    String show() {
        return null; //TODO: What should this be doing?
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
    private Expr body;

    Lambda(String var, Expr body) {
        this.var = var;
        this.body = body;
    }

    Value eval(Env env) {
        return new FValue(env, var, body);
    }

    String show() {
        return "(\\" + var + " -> " + body.show() + ")";
    }
}

class Apply extends Expr {
    private Expr fun, arg;

    Apply(Expr fun, Expr arg) {
        this.fun = fun;
        this.arg = arg;
    }

    Value eval(Env env) {
        return fun.eval(env).enter(arg.eval(env));
    }

    String show() {
        return "(" + fun.show() + " @ " + arg.show() + ")";
    }
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

class Case extends Stmt {
    private Expr   expr;
    private Stmt   ifEmpty;
    private String h;
    private String t;
    private Stmt   ifNonEmpty;
    Case(Expr expr, Stmt ifEmpty, String h, String t, Stmt ifNonEmpty) {
        this.expr       = expr;
        this.ifEmpty    = ifEmpty;
        this.h          = h;
        this.t          = t;
        this.ifNonEmpty = ifNonEmpty;
    }

    Env exec(Program prog, Env env) {
        return env; //TODO: Don't return this
    }

    void print(int ind) {
        indent(ind);
        System.out.println("case " + expr.show() + " of");
        indent(ind+2);
        System.out.println("[] ->");
        ifEmpty.print(ind+4);
        indent(ind+2);
        System.out.println("cons(" + h + ", " + t + ") ->");
        ifNonEmpty.print(ind+4);
    }
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
        System.out.println("Output: " + exp.eval(env).show());
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
    private Env env;
    private String arg;
    private Expr body;

    FValue(Env env, String arg, Expr body) {
        this.env = env;
        this.arg = arg;
        this.body = body;
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

abstract class LValue extends Value {
    // Methods common to all list values should go here
    abstract String showNoBrackets();

    String show() {
        return "[" + this.showNoBrackets() + "]";
    }
}

class EmptyList extends LValue {
    // Specifics for empty lists should go here.  But an
    // empty list has no content, and hence there are no
    // fields in this class.

    String showNoBrackets() {
        return "";
    }
}

class NonEmptyList extends LValue {
    private Value head;
    private LValue tail;

    NonEmptyList(Value head, LValue tail) {
        this.head = head;
        this.tail = tail;
    }

    String showNoBrackets() {
        if (tail.getClass() != EmptyList.class) {
            return head.show() + ", " + tail.showNoBrackets();
        } else {
            return head.show();
        }
    }

    public Value getHead() {
        return head;
    }

    public LValue getTail() {
        return tail;
    }
    // Specifics for non-empty lists should go here.  Note
    // that a non-empty list includes an arbitrary Value at
    // it's head (the first element in the list) and has a
    // second list of values (i.e., another LValue) as its
    // tail.

}

class MainList {
    public static void main(String[] args) {
        LValue l0 = new EmptyList();
        LValue l1 = new NonEmptyList(new IValue(42), l0);
        LValue l2 = new NonEmptyList(new BValue(true), l1);
        LValue l3 = new NonEmptyList(new FValue(null, "x", new Var("x")), l2);
        LValue l4 = l0;
        for (int i = 10; i > 0; i--) {
            l4 = new NonEmptyList(new IValue(i), l4);
        }
        System.out.println(l0.show());
        System.out.println(l1.show());
        System.out.println(l2.show());
        System.out.println(l3.show());
        System.out.println(l4.show());

        Object el5 = new Head(l1);
        Object el6 = new Tail(l1);
//        Object el7 = new Cons(new Int(42), l1);
        Object el8 = new NonEmpty(l0);

        System.out.println((new Int(4)).getClass());
        System.out.println((new Nil()).getClass());
        Cons t1 = new Cons(new Int(4), new Nil());

        Stmt init = new Seq(new VarDecl("l", new Cons(new Int(1),
                new Cons(new Int(2),
                        new Cons(new Int(3),
                                new Cons(new Int(4), new Nil()))))),
                new VarDecl("r", new Nil()));

        
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
    int index = 0;
    private Proc[] procs;
    private Stmt body;

    Program(Proc[] procs, Stmt body) {
        this.procs = procs;
        this.body = body;
    }

    Program(Stmt body) {
        this(new Proc[]{}, body);
    }

    void run() {
        body.exec(this, null);
    }

    void print() {
        for (Proc p : procs) {
            p.print(index);
            index++;
        }
        body.print(4);
        System.out.println();
    }

    void call(Env env, String name, Expr[] actuals) {
        for (int i = 0; i < procs.length; i++) {
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
    private String name;
    private Formal[] formals;
    private Stmt body;

    Proc(String name, Formal[] formals, Stmt body) {
        this.name = name;
        this.formals = formals;
        this.body = body;
    }

    String getName() {
        return name;
    }

    void print(int ind) {
        Stmt.indent(ind);
        System.out.print("procedure " + name + "(");
        for (int i = 0; i < formals.length; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(formals[i]);
        }
        System.out.println(") {");

        body.print(ind + 2);

        Stmt.indent(ind);
        System.out.println("}");
    }

    // This goes in the Proc class:
    void call(Program prog, Env env, Expr[] actuals) {
        if (actuals.length != formals.length) {
            System.out.println("ABORT: Wrong number of arguments for " + name);
            System.exit(1);
        }
        Env newenv = null;
        for (int i = 0; i < actuals.length; i++) {
            newenv = formals[i].extend(env, actuals[i], newenv);
        }
        body.exec(prog, newenv);
    }
}

class Call extends Stmt {
    private String name;
    private Expr[] actuals;

    Call(String name, Expr[] actuals) {
        this.name = name;
        this.actuals = actuals;
    }

    Env exec(Program prog, Env env) {
        prog.call(env, name, actuals);
        return env;
    }

    void print(int ind) {
        indent(ind);
        // TODO: fill this in if you want to see calls in the output!
        System.out.println(ind);
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
    ByRef(String name) {
        super(name);
    }

    public String toString() {
        return "ref " + name;
    }
}