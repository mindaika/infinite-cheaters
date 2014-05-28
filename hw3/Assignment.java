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
        Set<X86.Reg> regHashSet = new HashSet<>();
        Stack<Map.Entry> monoStack = new Stack<>();
        //System.out.println(iG.edgesOf(2));
//
//
//        // For now, do extremely simplistic allocation: simply allocate registers to
//        // IR.Reg's eagerly, in arbitrary order.  Once a register is used, we don't
//        // try to use it again, so we will very quickly run out.
//
//        // Keep track of available registers
//        // start by assuming all registers are available
        for (X86.Reg r : X86.allRegs) {
            regHashSet.add(r);
        }

        // always rule out special-purpose registers
        regHashSet.remove(X86.RSP);
        regHashSet.remove(IR.tempReg1);
        regHashSet.remove(IR.tempReg2);

        // Print info about LiveRanges
        for (Map.Entry<IR.Reg, Set<Integer>> me : liveRanges.entrySet()) {
            System.out.println("DEBUG:");
            System.out.println(me.getKey() + ": " + me.getValue().toString() + "\n");
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
        iG.printGraph();

        // TODO: Sort the regKEy list by size
        // Whilst ye olde Graph isn't not non-empty
        // Choose a node of appropriateness
        while (!iG.getGraph().isEmpty()) {
            Map.Entry killNode = null;
            int smallestSize = regHashSet.size();
            for (Map.Entry regKey : iG.getGraph().entrySet()) {
                // Find minimum node and ensure it still works
                if ((((Set) regKey.getValue()).size() <= smallestSize)) {
                    killNode = regKey;
                    smallestSize = ((Set) regKey.getValue()).size();
                }
            }
            if (killNode != null) {
                monoStack.push(killNode);
                iG.removeNode((IR.Reg) killNode.getKey());
            }
        }

        while (!monoStack.empty()) {
            Map.Entry p = monoStack.pop();
            IR.Reg node = (IR.Reg) p.getKey();
            // TODO: Something about this unchecked cast
            HashSet<IR.Reg> neighbors = (HashSet<IR.Reg>) p.getValue();
            Set<X86.Reg> availRegs = new HashSet<>();
            availRegs.addAll(regHashSet);
            for (IR.Reg j : neighbors) {
                availRegs.remove(env.get(j));
            }
//            System.out.println("Debug");
            Set<Integer> range = liveRanges.get((IR.Reg) p.getKey());
            X86.Reg treg = findAssignment(availRegs,
                    preferences.get(node),
                    rangeContainsCall(func, range));
            if (treg == null) {
                // couldn't find a register
                System.err.println("oops: out of registers");
                assert (false);
            }
            // We found a register; mark it as unavailable and record assignment//
            env.put(node, treg);
//          DEBUG
            System.err.println("allocating " + node + " to " + treg);
        }
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