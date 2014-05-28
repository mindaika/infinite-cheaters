// test array access
// (should print 2)
class Test {
  public static void main(String[] x) {
    Body b = new Body();
    System.out.println(b.go());
  }
}

class Body {
  public int go() {
    int[] a;
    a = new int[2];
    a[0] = 1;
    a[1] = 2;
    return a[1];
  }
}

