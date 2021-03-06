import java.util.*;

/**
 * Complicated register assignment *
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

        // REPLACE FROM HERE ....

        // Create a Register Interference graph
        // As per Graph, a Map of IR.Regs and all of their neighbors
        Graph interferenceGraph = new Graph(liveRanges);
        Set<X86.Reg> registerSetX86 = new HashSet<>();
        Stack<Map.Entry> registerStack = new Stack<>();
//
//
//        // For now, do extremely simplistic allocation: simply allocate registers to
//        // IR.Reg's eagerly, in arbitrary order.  Once a register is used, we don't
//        // try to use it again, so we will very quickly run out.
//
//        // Keep track of available registers
//        // start by assuming all registers are available
        Collections.addAll(registerSetX86, X86.allRegs);

        // always rule out special-purpose registers
        registerSetX86.remove(X86.RSP);
        registerSetX86.remove(IR.tempReg1);
        registerSetX86.remove(IR.tempReg2);

        // Whilst ye olde Graph isn't not non-empty
        // Choose a node of appropriateness
        while (!interferenceGraph.getGraph().isEmpty()) {
            Map.Entry killNode = null;
            int smallestSize = registerSetX86.size();
            for (Map.Entry regKey : interferenceGraph.getGraph().entrySet()) {
                // Find minimum node and ensure it still works
                if ((((Set) regKey.getValue()).size() < smallestSize)) {
                    killNode = regKey;
                    smallestSize = ((Set) regKey.getValue()).size();
                }
            }
            if (killNode != null) {
                registerStack.push(killNode);
                interferenceGraph.removeNode((IR.Reg) killNode.getKey());
            }
        }

        while (!registerStack.empty()) {
            Map.Entry p = registerStack.pop();
            IR.Reg node = (IR.Reg) p.getKey();
            // As per the intertubes, the real solution is "Write to Oracle and demand reified generics"
            @SuppressWarnings("unchecked")
            HashSet<IR.Reg> neighbors = HashSet.class.cast(p.getValue());
            Set<X86.Reg> availRegs = new HashSet<>();
            availRegs.addAll(registerSetX86);
            for (IR.Reg j : neighbors) {
                availRegs.remove(env.get(j));
            }
            Set<Integer> range = liveRanges.get(p.getKey());
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
//            System.err.println("allocating " + node + " to " + treg);
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