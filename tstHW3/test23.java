// class decls
// (should print 1)
class Test {
  public static void main(String[] x) {
    Body b = new Body();
    int i = b.foo(1);
    System.out.println(i);
  }
}

class Body {
  int i;

  public int foo(int i) {
    int y;
    return i;
  }
}  
