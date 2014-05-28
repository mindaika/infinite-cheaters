// (should print 3 5)
class Test {
  public static void main(String[] x) {
    int[] a = new int[2];
    int i = 0;
    a[0] = 3;
    a[1] = 5;
    while (i<2) {
      System.out.println(a[i]);
      i = i+1;
    }
  }
}
