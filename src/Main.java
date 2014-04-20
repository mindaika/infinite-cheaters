class Main {
    public static void main(String[] args) {
        Stmt s
                = new Seq(new VarDecl("t", new Int(1)),
                new Seq(new VarDecl("i", new Int(1)),
                        new Seq(new While(new LT(new Var("i"), new Int(6)),
                                new Seq(new Assign("t", new Mult(new Var("t"), new Var("i"))),
                                        new Assign("i", new Plus(new Var("i"), new Int(1))))),
                                new Print(new Var("t")))));

        Stmt k = new Seq(new VarDecl("b", new Tail(new NonEmptyList(new IValue(12), new EmptyList()))),
                new Seq(new VarDecl("c", new Int(20)),
                        new Print(new Var("b"))));


        Program prog = new Program(s);
        Program progRock = new Program(k);

        System.out.println("Complete program is:");
        prog.print();
        progRock.print();

        System.out.println("Running in an empty environment:");
        prog.run();
        progRock.run();

        System.out.println("Done!");

        /*
        System.out.println("Complete program is:");
        s.print(4);

        System.out.println("Running on an empty memory:");
        //Memory mem = new Memory();
        s.exec(prog, null);

        System.out.println("Done!");
        */
    }
}
