// method bindings
// (should print A B B)
class Test {
  public static void main(String[] x) {
    A a = new A();
    B b = new B();
    A c = new B();
    a.foo();
    b.foo();
    c.foo();
  }
}

class A {
  public void foo() {
    System.out.println("A");
  }
}  

class B extends A {
  public void foo() {
    System.out.println("B");
  }
}  
