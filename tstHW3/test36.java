// test non-local vars
// (should print 1)
class Test {
  public static void main(String[] x) {
    int i;
    Body b;
    b = new Body();
    i = b.foo();      
    System.out.println(i);
  }
}

class Body {
  int x;
  public int foo() {
    x = 1;
    return x;
  }
}
