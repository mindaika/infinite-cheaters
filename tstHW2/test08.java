// test parameter passing
// (should print 9)
class Test {
  public static void main(String[] x) {
    Body b = new Body();
    System.out.println(b.go());
  }
}

class Body {
  public int value(int i, int j, int k) {
    return i+j+k;
  }
  public int go() {
    //    return value(1,1,1) + this.value(2,2,2);
    return value(1,1,1) + value(2,2,2);
  }
}
