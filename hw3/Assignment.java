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

        // TODO: REPLACE FROM HERE ....
        // Create a Register Interference graph
        // As per MyGraph, a Map of X86.Regs and all of their neighbors
        MyGraph iG = new MyGraph();
        // This is Yadala's Graph. Maybe it'll work, maybe not.
        Graph otherGraph = new Graph();
//        Deque<X86.Reg> registerQueue = new ArrayDeque<>();
//        Stack<IR.Reg> irRegisterStack = new Stack<>();
        Stack<Map.Entry> monoStack = new Stack<>();
        //System.out.println(iG.edgesOf(2));
//
//
//        // For now, do extremely simplistic allocation: simply allocate registers to
//        // IR.Reg's eagerly, in arbitrary order.  Once a register is used, we don't
//        // try to use it again, so we will very quickly run out.
//
//        // Keep track of available registers
        Set<X86.Reg> availableRegs = new HashSet<X86.Reg>();
//        // start by assuming all registers are available
        for (X86.Reg r : X86.allRegs) {
            availableRegs.add(r);
        }
//
//        // always rule out special-purpose registers
        availableRegs.remove(X86.RSP);
        availableRegs.remove(IR.tempReg1);
        availableRegs.remove(IR.tempReg2);
//
//        // For each node in the iG, add neighbors
//        // Where neighbors are Regs that are live at the same time
//        for (Map.Entry<IR.Reg, Set<Integer>> me : liveRanges.entrySet()) {
//            iG.addNode(me.getKey());
//            System.out.println(me.toString());
//        }
//
        // Print info about LiveRanges
        for (Map.Entry<IR.Reg, Set<Integer>> me : liveRanges.entrySet()) {
            System.out.println("output");
            System.out.println(me.getKey() + ": " + me.getValue().toString() + "\n");
//
//            // This should make a graph of the registers
//            for (Integer k : me.getValue()) {
//                for (Map.Entry<IR.Reg, Set<Integer>> you : liveRanges.entrySet()) {
//                    if (you.getValue().contains(k) && !you.equals(me)) {
////                        iG.addEdge(me.getKey(), you.getKey());
//                    }
//                }
//            }
        }

        // To build graph:
        // 1. Add Nodes for every entry in the liveRanges set
        for (Map.Entry<IR.Reg, Set<Integer>> me : liveRanges.entrySet()) {
            iG.addNode(me.getKey());
        }

        // 2. For every entry in the liveRanges set, find all other entries that have a common value
        // and create an edge between those two entries as nodes
        // For each map entry
        for (Map.Entry<IR.Reg, Set<Integer>> j : liveRanges.entrySet()) {
            // For every integer in that map entry
            for (Integer i : j.getValue()) {
                // Compare i to every other integer in every other entry
                // Algorithmic efficiency at its finest
                for (Map.Entry<IR.Reg, Set<Integer>> k : liveRanges.entrySet()) {
                    for (Integer p : k.getValue()) {
                        if (p.equals(i) && j != k) {
                            iG.addEdge(j.getKey(), k.getKey());
                        }
                    }
                }
            }
        }

        // Whilst ye olde Graph isn't not non-empty
        // Choose a node of appropriateness
        while (!iG.getGraph().isEmpty()) {
            Map.Entry killNode = null;
            for (Map.Entry regKey : iG.getGraph().entrySet()) {
                // Test to see if coloring is viable
                if (((List) regKey.getValue()).size() < availableRegs.size()) {
                    // Push node and carry one
                    monoStack.push(regKey);
                    killNode = regKey;
                    break;
                }
            }
            if (killNode != null) {
                iG.removeNode((IR.Reg) killNode.getKey());
            }
        }
        // Of note: this differs somewhat from the hw instructions. I used the graph-coloring algorithm, which simply
        // calls for the current Node to be of a smaller degree than the available registers, rather than the minimum
        // degree node. While this may change the order in which the registers are allocated, it should not change the
        // total effect.


        System.out.println("BREAAAAAAAK");

//        System.out.println("BREAAAAAAK");
//
//        // Create a stack of iGs by size
////        while (!(iG.vertexSet().isEmpty())) {
////            IR.Reg next = null;
////            for (IR.Reg i : iG.vertexSet()) {
////                int j = 0;
////                if (iG.degreeOf(i) >= j) {
////                    next = i;
////                }
////            }
////            irRegisterStack.push(next);
////            iG.removeVertex(next);
////        }
//
//        for (Map.Entry<IR.Reg, Set<Integer>> me : liveRanges.entrySet()) {
//            System.out.println("OUTPUT, MOTHAFUCKA");
////            System.out.println(irRegisterStack.peek());
////            System.out.println(iG.degreeOf(me.getKey()));
//            System.out.println( me.getKey() + " " + me.getValue() + "\n");
//            IR.Reg r = me.getKey();
//            Set<Integer> range = me.getValue();
//            X86.Reg treg = findAssignment(availableRegs,
//                    preferences.get(r),
//                    rangeContainsCall(func, range));
//            if (treg == null) {
//                // couldn't find a register
//                System.err.println("oops: out of registers");
//                assert (false);
//            }
//            // We found a register; mark it as unavailable and record assignment
//            availableRegs.remove(treg);
//            env.put(r, treg);
//            // DEBUG
//            // System.err.println("allocating " + r + " to " + treg);
//        }

        // TODO: ... TO HERE

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