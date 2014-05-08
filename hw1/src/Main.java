class Main {
    public static void main(String[] args) {
        Stmt s
                = new Seq(new VarDecl("t", new Int(1)),
                new Seq(new VarDecl("i", new Int(1)),
                        new Seq(new While(new LT(new Var("i"), new Int(11)),
                                new Seq(new Assign("t", new Mult(new Var("t"), new Var("i"))),
                                        new Assign("i", new Plus(new Var("i"), new Int(1))))),
                                new Print(new Var("t")))));

//        Stmt k = new Seq(new VarDecl("b", new Tail(new NonEmptyList(new IValue(12), new EmptyList()))),
//                new Seq(new VarDecl("c", new Int(20)),
//                        new Print(new Var("b"))));

        Stmt init = new Seq(new VarDecl("l", new Cons(new Int(1),
                new Cons(new Int(2),
                        new Cons(new Int(3),
                                new Cons(new Int(4),
                                        new Nil()))))),
                new VarDecl("r", new Nil()));




        Program prog = new Program(s);
//        Program progRock = new Program(k);

        System.out.println("Complete program is:");
        prog.print();
//        progRock.print();

        System.out.println("Running in an empty environment:");
        prog.run();
//        progRock.run();

        System.out.println("Done!");
    }
}
