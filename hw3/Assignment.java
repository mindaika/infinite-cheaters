import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

/**
 * Simplistic register assignment *
 */
class Assignment {

    /**
     * Assign IR.Regs to locations described by X86.Operands. *
     */
    static Map<IR.Reg, X86.Reg> assignRegisters(IR.Func func, List<Set<IR.Reg>> liveOutSets) {

        Map<IR.Reg, X86.Reg> env = new HashMap<IR.Reg, X86.Reg>();

        // Get non-binding preferences for assignments.
        Map<IR.Reg, X86.Reg> preferences = IR.getPreferences(func);

        // Calculate live ranges
        Map<IR.Reg, Set<Integer>> liveRanges = Liveness.calculateLiveRanges(liveOutSets);

        // Test
//        System.out.println(Liveness.calculateLiveRanges(liveOutSets));
        // Test

        // REPLACE FROM HERE ....
        UndirectedGraph<IR.Reg, DefaultEdge> iG = new SimpleGraph<>(DefaultEdge.class);
        CircularArrayList<X86.Reg> regLoop;
        Stack<IR.Reg> irRegisterStack = new Stack<>();
        //System.out.println(iG.edgesOf(2));


        // For now, do extremely simplistic allocation: simply allocate registers to
        // IR.Reg's eagerly, in arbitrary order.  Once a register is used, we don't
        // try to use it again, so we will very quickly run out.

        // Keep track of available registers
        Set<X86.Reg> availableRegs = new HashSet<X86.Reg>();
        // start by assuming all registers are available
        for (X86.Reg r : X86.allRegs) {
            availableRegs.add(r);
        }

        // always rule out special-purpose registers
        availableRegs.remove(X86.RSP);
        availableRegs.remove(IR.tempReg1);
        availableRegs.remove(IR.tempReg2);
        System.out.println("\nVERTEXES N' SHIT");
        System.out.println(iG.vertexSet() + "\n");
        regLoop = new CircularArrayList<>(availableRegs.size());
        regLoop.addAll(availableRegs);

        for (Map.Entry<IR.Reg, Set<Integer>> me : liveRanges.entrySet()) {
            iG.addVertex(me.getKey());
        }

        // Work through the list of live IR registers (in arbitrary order)
        for (Map.Entry<IR.Reg, Set<Integer>> me : liveRanges.entrySet()) {

            // This should make a graph of the registers
            for (Integer k : me.getValue()) {
                for (Map.Entry<IR.Reg, Set<Integer>> you : liveRanges.entrySet()) {
                    if (you.getValue().contains(k) && !you.equals(me)) {
                        iG.addEdge(me.getKey(), you.getKey());
                    }
                }
            }
        }

        // Create a stack of iGs by size
        while (!(iG.vertexSet().isEmpty())) {
            IR.Reg next = null;
            for (IR.Reg i : iG.vertexSet()) {
                int j = 0;
                if (iG.degreeOf(i) >= j) {
                    next = i;
                }
            }
            irRegisterStack.push(next);
            iG.removeVertex(next);
        }

        for (Map.Entry<IR.Reg, Set<Integer>> me : liveRanges.entrySet()) {
            System.out.println("OUTPUT, MOTHAFUCKA");
            System.out.println(irRegisterStack.peek());
//            System.out.println(iG.degreeOf(me.getKey()));
            System.out.println( me.getKey() + " " + me.getValue() + "\n");
            IR.Reg r = me.getKey();
            Set<Integer> range = me.getValue();
            X86.Reg treg = findAssignment(availableRegs,
                    preferences.get(r),
                    rangeContainsCall(func, range));
            if (treg == null) {
                // couldn't find a register
                System.err.println("oops: out of registers");
                assert (false);
            }
            // We found a register; mark it as unavailable and record assignment
            availableRegs.remove(treg);
            env.put(r, treg);
            // DEBUG
            // System.err.println("allocating " + r + " to " + treg);
        }

        // ... TO HERE

        // For documentation purposes
        System.out.println("# Allocation map");
        for (Map.Entry<IR.Reg, X86.Reg> me : env.entrySet())
            System.out.println("# " + me.getKey() + "\t" + me.getValue());

        return env;
    }

    /**
     * Find assignment for a register from the available set.
     * Try to match a preferred register if one is given.
     * Try to use a callee-save register if live range of this
     * register overlaps a call; otherwise, try to use a caller-save register.
     */
    static X86.Reg findAssignment(Set<X86.Reg> available,
                                  X86.Reg preference, // may be null
                                  boolean rangeContainsCall) {

        // Try to find a register
        X86.Reg treg = null;

        if (rangeContainsCall) {
            // try for a callee-save reg (ignoring preference for now)
            for (X86.Reg reg : X86.calleeSaveRegs)
                if (available.contains(reg)) {
                    treg = reg;
                    break;
                }
            if (treg == null) {
                // otherwise, try for a preference register (always caller-save)
                if (preference != null && available.contains(preference))
                    treg = preference;
            }
            if (treg == null)
                // otherwise, try for arbitrary caller-save reg, but trying first
                // those without special roles
                for (int i = X86.callerSaveRegs.length - 1; i >= 0; i--) {
                    X86.Reg reg = X86.callerSaveRegs[i];
                    if (available.contains(reg)) {
                        treg = reg;
                        break;
                    }
                }
        } else {
            // try first for a preference register (always caller-save)
            if (preference != null && available.contains(preference))
                treg = preference;
            if (treg == null)
                // try for arbitrary caller-save reg
                for (X86.Reg reg : X86.callerSaveRegs)
                    if (available.contains(reg)) {
                        treg = reg;
                        break;
                    }
            if (treg == null)
                // otherwise, try a callee-save
                for (X86.Reg reg : X86.calleeSaveRegs)
                    if (available.contains(reg)) {
                        treg = reg;
                        break;
                    }
        }
        return treg;
    }

    /**
     * Return true if specified range covers an IR instruction
     * that will cause an X86.call (or invoke an X86.divide).
     * By "cover" we mean that the register is liveIn and liveOut
     * of the instruction, but our range only contains liveOut data.
     * Fortunately, because labels occupy an instruction slot,
     * we know that the liveIn for any (real) instruction equals
     * the liveOut of its predecessor.
     */
    static boolean rangeContainsCall(IR.Func func, Set<Integer> s) {
        for (int i : s)
            if ((s.contains(i - 1)) &&
                    (func.code[i] instanceof IR.Call ||
                            (func.code[i] instanceof IR.Binop &&
                                    ((IR.Binop) func.code[i]).op == IR.ArithOP.DIV)))
                return true;
        return false;
    }

}