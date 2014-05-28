// variable, method, and class shared the same name
// (should print 2)
class Test {
  public static void main(String[] x) {
    A A = new A();
    A.A = A.A(2);
    System.out.println(A.A);
  }
}

class A {
  int A;
  public int A(int i) {
    return i;
  }
}
