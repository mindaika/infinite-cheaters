// variable and method shared the same name
// (should print 1)
class Test {
  public static void main(String[] x) {
    A a = new A();
    a.foo = a.foo(1);
    System.out.println(a.foo);
  }
}

class A {
  int foo;
  public int foo(int i) {
    return i;
  }
}
