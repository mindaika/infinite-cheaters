// test method calls
// (should print 2)
class Test {
  public static void main(String[] x) {
    Body b;
    b = new Body();
    b.i = 2;
    b.print();
  }
}

class Body {
  int i;
  public void print() {
    System.out.println(i);
  }
}
