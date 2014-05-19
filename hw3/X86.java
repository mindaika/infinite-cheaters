import java.io.*;
import java.util.*;

class X86 {

  // generate assembly name corresponding to C name 
  static String globalize(String s) {
    //modify depending on environment!!    
    // return "_" + s;   // appropriate for MacOS
    return s;            // appropriate for linux
  }

  // size specifiers
  enum Size {
    B("b",1),
    L("l",4),
    Q("q",8);

    final String suffix;
    final int bytes;
    Size (String suffix, int bytes) {
      this.suffix = suffix; this.bytes = bytes;
    }
    public String toString() {
      return suffix;
    }
  }
 
  // indexed by size, then number
  static String[][] regName = 
    {   // low B
      {"%al","%bl","%cl","%dl","%sil","%dil","%bpl","%spl",
       "%r8b","%r9b","%r10b","%r11b","%r12b","%r13b","%r14b","%r15b"},
      
      // low L
      {"%eax","%ebx","%ecx","%edx","%esi","%edi","%ebp","%esp",
       "%r8d","%r9d","%r10d","%r11d","%r12d","%r13d","%r14d","%r15d"},
      
      // full Q
      {"%rax","%rbx","%rcx","%rdx","%rsi","%rdi","%rbp","%rsp",
       "%r8","%r9","%r10","%r11","%r12","%r13","%r14","%r15"}
    };
	

  static abstract class Operand {
  };

  static class Mem extends Operand {   // Computed memory address
    Reg base;
    Reg index;
    int offset;
    int scale;  // 1,2,4,8
    Mem(Reg base, Reg index, int offset, int scale) {
      this.base = base; this.index = index; this.offset = offset; this.scale = scale;
    }
    Mem(Reg base, int offset) {
      this.base = base; this.index = null; this.offset = offset; this.scale = 1;
    }
    public String toString () {
      return (offset != 0 ? offset : "") + 
	     "(" + base + (index != null ? ("," + index + (scale != 1 ? ("," + scale) : "")) : "") + ")";
    }
    public boolean equals(Object obj) {
      return obj instanceof Mem && 
	base == ((Mem) obj).base && index == ((Mem) obj).index && 
	offset == ((Mem) obj).offset && scale == ((Mem) obj).scale;
    }
  }
  static class Reg extends Operand {  // Register
    int r; 
    Size s; 
    Reg(int r) {
      this.r = r; this.s = Size.Q;
    }
    Reg(int r, Size s) {
      this.r = r; this.s = s;
    }
    public String toString () {
      return regName[s.ordinal()][r];
    }
    public boolean equals(Object obj) {
      return obj instanceof Reg && r == ((Reg) obj).r && s == ((Reg) obj).s;  
    }
  }
  static class Imm extends Operand { // 32-bit integer immediate
    int i;
    Imm(int i) {
      this.i = i;
    }
    public String toString() {
      return "$" + i;
    }
    public boolean equals(Object obj) {
      return obj instanceof Imm && i == ((Imm) obj).i;
    }
  }
  static class AddrName extends Operand { // Named global address (PIC)
    String s;
    AddrName(String s) {
      this.s = s;
    }
    public String toString() {
      return globalize(s) + "(%rip)";
    }
    public boolean equals(Object obj) {
      return obj instanceof AddrName && s == ((AddrName) obj).s;
    }
  }
  static class GLabel extends Operand { // Global label
    String s;
    GLabel(String s) {
      this.s = s;
    }
    public String toString() {
      return globalize(s);
    }
    public boolean equals(Object obj) {
      return obj instanceof GLabel && s == ((GLabel) obj).s;
    }
  }
  static class Label extends Operand { // Local label
    String s;
    Label(String s) {
      this.s = s;
    }
    public String toString() {
      return s;
    }
    public boolean equals(Object obj) {
      return obj instanceof Label && s == ((Label) obj).s;
    }
  }


  static void emit(String s)
  {
    System.out.println(s);
  }

  static void emit0(String op)
  {
    System.out.println("\t" + op);
  }

  static void emit1(String op, Operand rand1)
  {
    System.out.println("\t" + op + " " + rand1);
  }

  static void emit2(String op, Operand rand1, Operand rand2) {
    System.out.println("\t" + op + " " + rand1 + "," + rand2);
  }

  static void emitLabel(Label lab)
  {
    System.out.println(lab + ":");
  }

  static void emitGLabel(GLabel lab)
  {
    System.out.println(lab + ":");
  }

  static void emitString(String s) {
    System.out.println("\t.asciz \"" + s + "\"");
  }
    
  // Adjust size of register operand
  static Reg resize_reg(Size size,Reg r) {
    return new Reg(r.r,size);
  }

  // Emit mov just when necessary
  static void emitMov(Size size,Operand from,Operand to) {
    if (!from.equals(to))  {
      emit2("mov" + size,from,to);
    }
  }

  static class ParallelMover {
    // Generate parallel register move
    // Algorithm based on Fig.2  of:
    // Laurence Rideau, Bernard P. Serpette, and Xavier Leroy,
    // "Tilting at windmills with Coq: formal verification of a compilation algorithm for parallel moves,"
    // Journal of Automated Reasoning, 40(4):307-326, 2008. 

    int n;
    Reg src[];
    Reg dst[];
    Reg tmp;
    boolean started[]; // have we started processing this source?
    boolean finished[]; // have we finished processing this source?

    ParallelMover(int n, Reg[] src,Reg[] dst,Reg tmp) {
      this.n = n;
      this.src = Arrays.copyOf(src,src.length); // because we change this
      this.dst = dst;
      this.tmp = tmp;
    }

    void move() {
      started = new boolean[n]; // initialized to false
      finished = new boolean[n]; // ditto
      for (int i = 0; i < n; i++)
	if (!started[i])
	  moveOne(i);
    }

    void moveOne (int i) {
      // move source slot i
      // first check whether destination register is used elsewhere
      if (!src[i].equals(dst[i])) {
	started[i] = true;
	for (int j = 0; j < n; j++) {
	  if (dst[i].equals(src[j])) {
	    if (!started[j])
	      moveOne(j);
	    else if (started[j] && !finished[j]) {
	      emitMov(Size.Q,src[j],tmp);
	      src[j] = tmp;
	      break;
	    }
	  }
	}
	emitMov(Size.Q,src[i],dst[i]);
	finished[i] = true;
      }
    }
  }

  // mnemonic definitions for the (quad) registers
  static final Reg
    RAX = new Reg(0),
    RBX = new Reg(1),
    RCX = new Reg(2),
    RDX = new Reg(3),
    RSI = new Reg(4),
    RDI = new Reg(5),
    RBP = new Reg(6),
    RSP = new Reg(7),
    R8 = new Reg(8),
    R9 = new Reg(9),
    R10 = new Reg(10),
    R11 = new Reg(11),
    R12 = new Reg(12),
    R13 = new Reg(13),
    R14 = new Reg(14),
    R15 = new Reg(15)
    ;
  
  // some extras for needed long registers
  static final Reg
    EAX = new Reg(0,Size.L),
    EDX = new Reg(3,Size.L);


  // indices of standard argument registers
  static Reg[] argRegs = {RDI,RSI,RDX,RCX,R8,R9};
  
  static Reg[] calleeSaveRegs = {RBX,RBP,R12,R13,R14,R15};

  static Reg[] callerSaveRegs = {RAX,RCX,RDX,RSI,RDI,R8,R9,R10,R11};

  static Reg[] allRegs = {RAX,RBX,RCX,RDX,RSI,RDI,RBP,RSP,
			  R8,R9,R10,R11,R12,R13,R14,R15};


  // Round x up to nearest multiple of p, provided p is a power of 2 
  static int roundup(int x,int p) {
    return (x + p - 1) & ~(p-1);
  }

}
