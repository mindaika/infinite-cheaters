import java.io.*;
import java.util.*;

class X86Gen {
  public static void main(String [] args) {
    try {
      if (args.length == 1) {
	FileInputStream stream = new FileInputStream(args[0]);
	IR.Program p = new irParser(stream).Program();
	stream.close();
	IR.indexed = true;
	p.gen();
      } else {
	System.err.println("Need a file name as command-line argument.");
      }
    } catch (TokenMgrError e) {
      System.err.println(e.getMessage());
    } catch (ParseException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
