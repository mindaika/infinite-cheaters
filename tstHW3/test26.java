// mutually dependent method decls
// (should print 3)
class Test {
  public static void main(String[] x) {
    A a = new A();
    int i = a.foo(2);
    System.out.println(i);
  }
}

class A {
  public int foo(int i) {
    if (i>1) return bar();
    else return 3;
  }

  public int bar() {
    return foo(1);
  }
}
