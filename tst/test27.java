// mutually dependent class decls
// (should print 1 1)
class Test {
  public static void main(String[] a) {
    A x = new A();
    B y = new B();
    System.out.println(x.i);
    System.out.println(y.a.i);
  }
}

class A {
  int i=1;
  B b;
}  

class B {
  A a = new A();
}  
