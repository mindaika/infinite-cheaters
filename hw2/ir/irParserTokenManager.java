/* Generated By:JavaCC: Do not edit this line. irParserTokenManager.java */
package ir;
import java.util.*;
import java.io.*;

/** Token Manager. */
public class irParserTokenManager implements irParserConstants
{

  /** Debug output. */
  public static  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public static  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private static final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x1f80L) != 0L)
         {
            jjmatchedKind = 22;
            return 16;
         }
         return -1;
      case 1:
         if ((active0 & 0x400L) != 0L)
            return 16;
         if ((active0 & 0x1b80L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 1;
            return 16;
         }
         return -1;
      case 2:
         if ((active0 & 0xb80L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 2;
            return 16;
         }
         return -1;
      case 3:
         if ((active0 & 0x380L) != 0L)
            return 16;
         if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 3;
            return 16;
         }
         return -1;
      case 4:
         if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 4;
            return 16;
         }
         return -1;
      default :
         return -1;
   }
}
private static final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
static private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
static private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 10:
         return jjStopAtPos(0, 24);
      case 33:
         jjmatchedKind = 44;
         return jjMoveStringLiteralDfa1_0(0x8000000000L);
      case 38:
         return jjMoveStringLiteralDfa1_0(0x1000000000L);
      case 40:
         return jjStopAtPos(0, 25);
      case 41:
         return jjStopAtPos(0, 26);
      case 42:
         return jjStopAtPos(0, 32);
      case 43:
         return jjStopAtPos(0, 33);
      case 44:
         return jjStopAtPos(0, 28);
      case 45:
         return jjStopAtPos(0, 34);
      case 47:
         return jjStopAtPos(0, 35);
      case 58:
         jjmatchedKind = 27;
         return jjMoveStringLiteralDfa1_0(0x1c0000L);
      case 60:
         jjmatchedKind = 40;
         return jjMoveStringLiteralDfa1_0(0x20000000000L);
      case 61:
         jjmatchedKind = 31;
         return jjMoveStringLiteralDfa1_0(0x4000000000L);
      case 62:
         jjmatchedKind = 42;
         return jjMoveStringLiteralDfa1_0(0x80000000000L);
      case 91:
         return jjStopAtPos(0, 45);
      case 93:
         return jjStopAtPos(0, 46);
      case 99:
         return jjMoveStringLiteralDfa1_0(0x100L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 103:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 114:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 123:
         return jjStopAtPos(0, 29);
      case 124:
         return jjMoveStringLiteralDfa1_0(0x2000000000L);
      case 125:
         return jjStopAtPos(0, 30);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
static private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 38:
         if ((active0 & 0x1000000000L) != 0L)
            return jjStopAtPos(1, 36);
         break;
      case 61:
         if ((active0 & 0x4000000000L) != 0L)
            return jjStopAtPos(1, 38);
         else if ((active0 & 0x8000000000L) != 0L)
            return jjStopAtPos(1, 39);
         else if ((active0 & 0x20000000000L) != 0L)
            return jjStopAtPos(1, 41);
         else if ((active0 & 0x80000000000L) != 0L)
            return jjStopAtPos(1, 43);
         break;
      case 66:
         if ((active0 & 0x80000L) != 0L)
            return jjStopAtPos(1, 19);
         break;
      case 73:
         if ((active0 & 0x40000L) != 0L)
            return jjStopAtPos(1, 18);
         break;
      case 80:
         if ((active0 & 0x100000L) != 0L)
            return jjStopAtPos(1, 20);
         break;
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x180L);
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x800L);
      case 102:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(1, 10, 16);
         break;
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x200L);
      case 122:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
      case 124:
         if ((active0 & 0x2000000000L) != 0L)
            return jjStopAtPos(1, 37);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
static private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 61:
         if ((active0 & 0x1000L) != 0L)
            return jjStopAtPos(2, 12);
         break;
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      case 116:
         return jjMoveStringLiteralDfa3_0(active0, 0xa80L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
static private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 97:
         if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(3, 7, 16);
         break;
      case 108:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(3, 8, 16);
         break;
      case 111:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(3, 9, 16);
         break;
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
static private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
static private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 110:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(5, 11, 16);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
static private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 24;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 15)
                        kind = 15;
                     jjCheckNAddStates(0, 2);
                  }
                  else if (curChar == 34)
                     jjCheckNAdd(11);
                  else if (curChar == 35)
                  {
                     if (kind > 5)
                        kind = 5;
                     jjCheckNAdd(1);
                  }
                  break;
               case 1:
                  if ((0xfffffffffffffbffL & l) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjCheckNAdd(1);
                  break;
               case 10:
                  if (curChar == 34)
                     jjCheckNAdd(11);
                  break;
               case 11:
                  if ((0xfffffffbfffffbffL & l) != 0L)
                     jjCheckNAddTwoStates(11, 12);
                  break;
               case 12:
                  if (curChar == 34 && kind > 17)
                     kind = 17;
                  break;
               case 14:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 21)
                     kind = 21;
                  jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjstateSet[jjnewStateCnt++] = 16;
                  break;
               case 19:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjstateSet[jjnewStateCnt++] = 19;
                  break;
               case 20:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 15)
                     kind = 15;
                  jjCheckNAddStates(0, 2);
                  break;
               case 21:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(21, 22);
                  break;
               case 22:
                  if (curChar == 46 && kind > 6)
                     kind = 6;
                  break;
               case 23:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 15)
                     kind = 15;
                  jjCheckNAdd(23);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 22)
                        kind = 22;
                     jjCheckNAdd(16);
                  }
                  else if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 18;
                  if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 14;
                  else if (curChar == 102)
                     jjstateSet[jjnewStateCnt++] = 8;
                  if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 1:
                  if (kind > 5)
                     kind = 5;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 2:
                  if (curChar == 101 && kind > 16)
                     kind = 16;
                  break;
               case 3:
                  if (curChar == 117)
                     jjCheckNAdd(2);
                  break;
               case 4:
                  if (curChar == 114)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 5:
                  if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 6:
                  if (curChar == 115)
                     jjCheckNAdd(2);
                  break;
               case 7:
                  if (curChar == 108)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 8:
                  if (curChar == 97)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if (curChar == 102)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 11:
                  jjAddStates(3, 4);
                  break;
               case 13:
                  if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 15:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjCheckNAdd(16);
                  break;
               case 16:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjCheckNAdd(16);
                  break;
               case 17:
                  if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 18;
                  break;
               case 18:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAdd(19);
                  break;
               case 19:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAdd(19);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 11:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(3, 4);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 24 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   21, 22, 23, 11, 12, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, "\144\141\164\141", 
"\143\141\154\154", "\147\157\164\157", "\151\146", "\162\145\164\165\162\156", "\163\172\75", 
null, null, null, null, null, "\72\111", "\72\102", "\72\120", null, null, null, 
"\12", "\50", "\51", "\72", "\54", "\173", "\175", "\75", "\52", "\53", "\55", "\57", 
"\46\46", "\174\174", "\75\75", "\41\75", "\74", "\74\75", "\76", "\76\75", "\41", 
"\133", "\135", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x7fffffff9f81L, 
};
static final long[] jjtoSkip = {
   0x7eL, 
};
static protected SimpleCharStream input_stream;
static private final int[] jjrounds = new int[24];
static private final int[] jjstateSet = new int[48];
static protected char curChar;
/** Constructor. */
public irParserTokenManager(SimpleCharStream stream){
   if (input_stream != null)
      throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);
   input_stream = stream;
}

/** Constructor. */
public irParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
static public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
static private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 24; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
static public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
static public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

static protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

static int curLexState = 0;
static int defaultLexState = 0;
static int jjnewStateCnt;
static int jjround;
static int jjmatchedPos;
static int jjmatchedKind;

/** Get the next Token. */
public static Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100003200L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

static private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
static private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
static private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

static private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
