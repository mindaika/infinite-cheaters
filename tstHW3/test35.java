// test method calls
// (should print 3)
class Test {
  public static void main(String[] x) {
    Body b = new Body();
    int i;
    i = b.foo(1,2);
    System.out.println(i);
  }
}

class Body {
  public int foo(int x, int y) {
    return x + y;
  }
}
