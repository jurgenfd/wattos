/* Generated By:JavaCC: Do not edit this line. XplorParserAllTokenManager.java */
package Wattos.Converters.Xplor;

public class XplorParserAllTokenManager implements XplorParserAllConstants
{
    // For rememebering a reference to the starting token that
    // began a curly-brace comment:
    static Token starting_curly_token = null;

    // Keep track of how many will be open. Bogus initialization.
    static int curlies_open = 0;
  public static  java.io.PrintStream debugStream = System.out;
  public static  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private static final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x180000000L) != 0L)
         {
            jjmatchedKind = 48;
            return 3;
         }
         if ((active0 & 0x7c640000000L) != 0L)
         {
            jjmatchedKind = 48;
            return 83;
         }
         return -1;
      case 1:
         if ((active0 & 0x3c5c0000000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 1;
            return 83;
         }
         if ((active0 & 0x40200000000L) != 0L)
            return 83;
         return -1;
      case 2:
         if ((active0 & 0x100000000L) != 0L)
            return 83;
         if ((active0 & 0x3c4c0000000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 2;
            return 83;
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
static private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
static private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 9:
         return jjStopAtPos(0, 2);
      case 32:
         return jjStopAtPos(0, 1);
      case 40:
         return jjStopAtPos(0, 43);
      case 41:
         return jjStopAtPos(0, 44);
      case 61:
         return jjStopAtPos(0, 45);
      case 65:
      case 97:
         return jjMoveStringLiteralDfa1_0(0x180000000L);
      case 67:
      case 99:
         return jjMoveStringLiteralDfa1_0(0x40000000000L);
      case 72:
      case 104:
         return jjMoveStringLiteralDfa1_0(0xc000000000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa1_0(0x40000000L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa1_0(0x200000000L);
      case 80:
      case 112:
         return jjMoveStringLiteralDfa1_0(0x30400000000L);
      default :
         return jjMoveNfa_0(4, 0);
   }
}
static private final int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 65:
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x40000000L);
      case 69:
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x400000000L);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x100000000L);
      case 80:
      case 112:
         return jjMoveStringLiteralDfa2_0(active0, 0x3c000000000L);
      case 82:
      case 114:
         if ((active0 & 0x200000000L) != 0L)
            return jjStartNfaWithStates_0(1, 33, 83);
         break;
      case 84:
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0x80000000L);
      case 86:
      case 118:
         if ((active0 & 0x40000000000L) != 0L)
            return jjStartNfaWithStates_0(1, 42, 83);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
static private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
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
      case 65:
      case 97:
         return jjMoveStringLiteralDfa3_0(active0, 0x400000000L);
      case 68:
      case 100:
         if ((active0 & 0x100000000L) != 0L)
            return jjStartNfaWithStates_0(2, 32, 83);
         break;
      case 77:
      case 109:
         return jjMoveStringLiteralDfa3_0(active0, 0x3c040000000L);
      case 79:
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x80000000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
static private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
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
      case 49:
         if ((active0 & 0x4000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 38, 83);
         else if ((active0 & 0x10000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 40, 83);
         break;
      case 50:
         if ((active0 & 0x8000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 39, 83);
         else if ((active0 & 0x20000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 41, 83);
         break;
      case 69:
      case 101:
         if ((active0 & 0x40000000L) != 0L)
            return jjStartNfaWithStates_0(3, 30, 83);
         break;
      case 75:
      case 107:
         if ((active0 & 0x400000000L) != 0L)
            return jjStartNfaWithStates_0(3, 34, 83);
         break;
      case 77:
      case 109:
         if ((active0 & 0x80000000L) != 0L)
            return jjStartNfaWithStates_0(3, 31, 83);
         break;
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
static private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
static private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
static private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
static private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
static private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 83;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 83:
                  if ((0x57ffaca800000000L & l) != 0L)
                  {
                     if (kind > 48)
                        kind = 48;
                     jjCheckNAddStates(0, 2);
                  }
                  else if ((0x100003600L & l) != 0L)
                     jjCheckNAddTwoStates(23, 24);
                  if (curChar == 58)
                     jjCheckNAddStates(3, 5);
                  break;
               case 3:
                  if ((0x57ffaca800000000L & l) != 0L)
                  {
                     if (kind > 48)
                        kind = 48;
                     jjCheckNAddStates(0, 2);
                  }
                  else if ((0x100003600L & l) != 0L)
                     jjCheckNAddTwoStates(23, 24);
                  if (curChar == 58)
                     jjCheckNAddStates(3, 5);
                  break;
               case 4:
                  if ((0xdc00d4f01fffc9ffL & l) != 0L)
                  {
                     if (kind > 58)
                        kind = 58;
                     jjCheckNAdd(40);
                  }
                  else if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 46)
                        kind = 46;
                     jjCheckNAddStates(6, 10);
                  }
                  else if ((0x280000000000L & l) != 0L)
                     jjCheckNAddStates(11, 13);
                  else if ((0x100000200L & l) != 0L)
                  {
                     if (kind > 49)
                        kind = 49;
                  }
                  else if (curChar == 33)
                     jjCheckNAddStates(14, 16);
                  else if (curChar == 34)
                     jjCheckNAddTwoStates(31, 32);
                  if ((0x57ffaca800000000L & l) != 0L)
                  {
                     if (kind > 48)
                        kind = 48;
                     jjCheckNAddStates(0, 2);
                  }
                  else if (curChar == 36)
                     jjCheckNAdd(21);
                  else if (curChar == 46)
                     jjCheckNAdd(16);
                  break;
               case 15:
                  if (curChar == 46)
                     jjCheckNAdd(16);
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAddTwoStates(16, 17);
                  break;
               case 18:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(19);
                  break;
               case 19:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAdd(19);
                  break;
               case 20:
                  if (curChar == 36)
                     jjCheckNAdd(21);
                  break;
               case 21:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 47)
                     kind = 47;
                  jjCheckNAdd(21);
                  break;
               case 22:
                  if ((0x57ffaca800000000L & l) == 0L)
                     break;
                  if (kind > 48)
                     kind = 48;
                  jjCheckNAddStates(0, 2);
                  break;
               case 23:
                  if ((0x100003600L & l) != 0L)
                     jjCheckNAddTwoStates(23, 24);
                  break;
               case 24:
                  if (curChar == 58)
                     jjCheckNAddStates(3, 5);
                  break;
               case 25:
                  if ((0x100003600L & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 26:
                  if ((0x57ffaca800000000L & l) == 0L)
                     break;
                  if (kind > 48)
                     kind = 48;
                  jjCheckNAdd(26);
                  break;
               case 27:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(28, 29);
                  break;
               case 28:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(28, 29);
                  break;
               case 29:
                  if (curChar == 34 && kind > 48)
                     kind = 48;
                  break;
               case 30:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(31, 32);
                  break;
               case 31:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(31, 32);
                  break;
               case 32:
                  if (curChar != 34)
                     break;
                  if (kind > 48)
                     kind = 48;
                  jjCheckNAddTwoStates(23, 24);
                  break;
               case 33:
                  if ((0x100000200L & l) != 0L && kind > 49)
                     kind = 49;
                  break;
               case 34:
                  if (curChar == 33)
                     jjCheckNAddStates(14, 16);
                  break;
               case 35:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(14, 16);
                  break;
               case 36:
                  if (curChar == 10 && kind > 50)
                     kind = 50;
                  break;
               case 37:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 36;
                  break;
               case 38:
                  if ((0x2400L & l) != 0L && kind > 50)
                     kind = 50;
                  break;
               case 40:
                  if ((0xdc00d4f01fffc9ffL & l) == 0L)
                     break;
                  if (kind > 58)
                     kind = 58;
                  jjCheckNAdd(40);
                  break;
               case 41:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAddStates(11, 13);
                  break;
               case 42:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAddStates(17, 19);
                  break;
               case 43:
                  if (curChar != 46)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAddTwoStates(44, 45);
                  break;
               case 44:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAddTwoStates(44, 45);
                  break;
               case 46:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(47);
                  break;
               case 47:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAdd(47);
                  break;
               case 48:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAddStates(20, 23);
                  break;
               case 49:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 46)
                     kind = 46;
                  jjCheckNAddStates(6, 10);
                  break;
               case 60:
                  if ((0x100003600L & l) != 0L)
                     jjCheckNAddTwoStates(61, 64);
                  break;
               case 61:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddTwoStates(61, 64);
                  break;
               case 77:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjAddStates(24, 26);
                  break;
               case 78:
                  if (curChar == 10 && kind > 51)
                     kind = 51;
                  break;
               case 79:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 78;
                  break;
               case 80:
                  if ((0x2400L & l) != 0L && kind > 51)
                     kind = 51;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 83:
               case 22:
                  if ((0x47ffffffd7ffffffL & l) == 0L)
                     break;
                  if (kind > 48)
                     kind = 48;
                  jjCheckNAddStates(0, 2);
                  break;
               case 3:
                  if ((0x47ffffffd7ffffffL & l) != 0L)
                  {
                     if (kind > 48)
                        kind = 48;
                     jjCheckNAddStates(0, 2);
                  }
                  if ((0x8000000080000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 4:
                  if ((0x47ffffffd7ffffffL & l) != 0L)
                  {
                     if (kind > 48)
                        kind = 48;
                     jjCheckNAddStates(0, 2);
                  }
                  else if (curChar == 123)
                  {
                     if (kind > 53)
                        kind = 53;
                  }
                  if ((0xd0000001e8000001L & l) != 0L)
                  {
                     if (kind > 58)
                        kind = 58;
                     jjCheckNAdd(40);
                  }
                  else if ((0x4000000040000L & l) != 0L)
                     jjAddStates(27, 29);
                  else if ((0x8000000080000L & l) != 0L)
                     jjAddStates(30, 32);
                  else if ((0x80000000800000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  else if ((0x40000000400000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  else if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 0:
                  if ((0x20000000200L & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAdd(1);
                  break;
               case 1:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAdd(1);
                  break;
               case 2:
                  if ((0x8000000080000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 5:
                  if ((0x20000000200000L & l) == 0L)
                     break;
                  if (kind > 35)
                     kind = 35;
                  jjCheckNAdd(6);
                  break;
               case 6:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 35)
                     kind = 35;
                  jjCheckNAdd(6);
                  break;
               case 7:
                  if ((0x100000001000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 8:
                  if ((0x800000008000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if ((0x40000000400000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 10:
                  if ((0x8000000080L & l) == 0L)
                     break;
                  if (kind > 36)
                     kind = 36;
                  jjCheckNAdd(11);
                  break;
               case 11:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 36)
                     kind = 36;
                  jjCheckNAdd(11);
                  break;
               case 12:
                  if ((0x20000000200L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 10;
                  break;
               case 13:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 14:
                  if ((0x80000000800000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 17:
                  if ((0x7000000070L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 18;
                  break;
               case 21:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 47)
                     kind = 47;
                  jjstateSet[jjnewStateCnt++] = 21;
                  break;
               case 26:
                  if ((0x47ffffffd7ffffffL & l) == 0L)
                     break;
                  if (kind > 48)
                     kind = 48;
                  jjstateSet[jjnewStateCnt++] = 26;
                  break;
               case 28:
                  jjAddStates(33, 34);
                  break;
               case 31:
                  jjAddStates(35, 36);
                  break;
               case 35:
                  jjAddStates(14, 16);
                  break;
               case 39:
                  if (curChar == 123 && kind > 53)
                     kind = 53;
                  break;
               case 40:
                  if ((0xd0000001e8000001L & l) == 0L)
                     break;
                  if (kind > 58)
                     kind = 58;
                  jjCheckNAdd(40);
                  break;
               case 45:
                  if ((0x7000000070L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 46;
                  break;
               case 50:
                  if ((0x8000000080000L & l) != 0L)
                     jjAddStates(30, 32);
                  break;
               case 51:
                  if ((0x20000000200L & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(52);
                  break;
               case 52:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(52);
                  break;
               case 53:
                  if ((0x8000000080L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 51;
                  break;
               case 54:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 53;
                  break;
               case 55:
                  if ((0x800000008L & l) == 0L)
                     break;
                  if (kind > 37)
                     kind = 37;
                  jjCheckNAdd(56);
                  break;
               case 56:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 37)
                     kind = 37;
                  jjCheckNAdd(56);
                  break;
               case 57:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 55;
                  break;
               case 58:
                  if ((0x1000000010000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 57;
                  break;
               case 59:
                  if ((0x10000000100000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 60;
                  break;
               case 61:
                  jjAddStates(37, 38);
                  break;
               case 62:
                  if ((0x1000000010L & l) != 0L && kind > 52)
                     kind = 52;
                  break;
               case 63:
                  if ((0x400000004000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 62;
                  break;
               case 64:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 63;
                  break;
               case 65:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 59;
                  break;
               case 66:
                  if ((0x4000000040000L & l) != 0L)
                     jjAddStates(27, 29);
                  break;
               case 67:
                  if ((0x20000000200L & l) == 0L)
                     break;
                  if (kind > 28)
                     kind = 28;
                  jjCheckNAdd(68);
                  break;
               case 68:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 28)
                     kind = 28;
                  jjCheckNAdd(68);
                  break;
               case 69:
                  if ((0x8000000080000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 67;
                  break;
               case 70:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 69;
                  break;
               case 71:
                  if ((0x400000004000L & l) == 0L)
                     break;
                  if (kind > 29)
                     kind = 29;
                  jjCheckNAdd(72);
                  break;
               case 72:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 29)
                     kind = 29;
                  jjCheckNAdd(72);
                  break;
               case 73:
                  if ((0x8000000080000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 71;
                  break;
               case 74:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 73;
                  break;
               case 75:
                  if ((0x200000002L & l) != 0L)
                     jjCheckNAddStates(39, 42);
                  break;
               case 76:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                     jjCheckNAddStates(39, 42);
                  break;
               case 77:
                  jjCheckNAddStates(24, 26);
                  break;
               case 81:
                  if ((0x200000002000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 75;
                  break;
               case 82:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 81;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 4:
               case 40:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 58)
                     kind = 58;
                  jjCheckNAdd(40);
                  break;
               case 28:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(33, 34);
                  break;
               case 31:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(35, 36);
                  break;
               case 35:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(14, 16);
                  break;
               case 61:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(37, 38);
                  break;
               case 77:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(24, 26);
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
      if ((i = jjnewStateCnt) == (startsAt = 83 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static private final int jjMoveStringLiteralDfa0_1()
{
   return jjMoveNfa_1(1, 0);
}
static private final int jjMoveNfa_1(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 3;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 1:
               case 0:
                  kind = 54;
                  jjCheckNAdd(0);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((0xd7ffffffffffffffL & l) != 0L)
                  {
                     if (kind > 54)
                        kind = 54;
                     jjCheckNAdd(0);
                  }
                  else if (curChar == 125)
                  {
                     if (kind > 56)
                        kind = 56;
                  }
                  else if (curChar == 123)
                  {
                     if (kind > 55)
                        kind = 55;
                  }
                  break;
               case 0:
                  if ((0xd7ffffffffffffffL & l) == 0L)
                     break;
                  kind = 54;
                  jjCheckNAdd(0);
                  break;
               case 2:
                  if (curChar == 125)
                     kind = 56;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 1:
               case 0:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 54)
                     kind = 54;
                  jjCheckNAdd(0);
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
      if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   22, 23, 24, 25, 26, 27, 43, 45, 16, 17, 48, 42, 15, 16, 35, 37, 
   38, 43, 45, 48, 43, 44, 45, 48, 77, 79, 80, 70, 74, 82, 54, 58, 
   65, 28, 29, 31, 32, 61, 64, 76, 77, 79, 80, 
};
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
null, null, "\50", "\51", "\75", null, null, null, null, null, null, null, null, 
null, null, null, null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
   "CURLY_STATE", 
};
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, 1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0x403fffffc000001L, 
};
static final long[] jjtoSkip = {
   0x1fc00000000001eL, 
};
static final long[] jjtoSpecial = {
   0x3c000000000000L, 
};
static protected SimpleCharStream input_stream;
static private final int[] jjrounds = new int[83];
static private final int[] jjstateSet = new int[166];
static StringBuffer image;
static int jjimageLen;
static int lengthOfMatch;
static protected char curChar;
public XplorParserAllTokenManager(SimpleCharStream stream)
{
   if (input_stream != null)
      throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);
   input_stream = stream;
}
public XplorParserAllTokenManager(SimpleCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
static public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
static private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 83; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
static public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
static public void SwitchTo(int lexState)
{
   if (lexState >= 2 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

static protected Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

static int curLexState = 0;
static int defaultLexState = 0;
static int jjnewStateCnt;
static int jjround;
static int jjmatchedPos;
static int jjmatchedKind;

public static Token getNextToken() 
{
  int kind;
  Token specialToken = null;
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
      matchedToken.specialToken = specialToken;
      return matchedToken;
   }
   image = null;
   jjimageLen = 0;

   switch(curLexState)
   {
     case 0:
       try { input_stream.backup(0);
          while (curChar <= 13 && (0x2400L & (1L << curChar)) != 0L)
             curChar = input_stream.BeginToken();
       }
       catch (java.io.IOException e1) { continue EOFLoop; }
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_0();
       break;
     case 1:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_1();
       break;
   }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
           matchedToken.specialToken = specialToken;
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else
        {
           if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
           {
              matchedToken = jjFillToken();
              if (specialToken == null)
                 specialToken = matchedToken;
              else
              {
                 matchedToken.specialToken = specialToken;
                 specialToken = (specialToken.next = matchedToken);
              }
              SkipLexicalActions(matchedToken);
           }
           else 
              SkipLexicalActions(null);
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
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

static void SkipLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      case 53 :
         if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
            curlies_open    = 1;
            /** Reference to the token that this rule is going to be returning
            once the token manager gets back out of <CURLY_STATE>. */
            starting_curly_token = matchedToken;
         break;
      case 54 :
         if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
        starting_curly_token.image = starting_curly_token.image + image.toString();
         break;
      case 55 :
         if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
        starting_curly_token.image = starting_curly_token.image + image.toString();
        curlies_open++;
         break;
      case 56 :
         if (image == null)
            image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
         else
            image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
        starting_curly_token.image = starting_curly_token.image + image.toString();
        curlies_open--;
        if (curlies_open < 1) {
            SwitchTo( DEFAULT );
        }
         break;
      default :
         break;
   }
}
}
