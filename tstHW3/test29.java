// variable, method, and class shared the same name
// (should print 1)
class Test {
  public static void main(String[] x) {
    int A;
    A a = new A();
    a.A = a.A(1);
    A = a.A;
    System.out.println(A);
  }
}

class A {
  int A;
  public int A(int i) {
    return i;
  }
}
