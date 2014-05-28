// test more invocations 
// (should print 2)
class Test {
  public static void main(String[] x) {
    A a = new A();
    System.out.println(a.go());
  }
}

class B {
  public int back(int a) {
    return a;
  }
}

class A extends B {
  public int go() {
    //    return this.back(2);
    return back(2);
  }
}
