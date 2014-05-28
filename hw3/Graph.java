import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Randall on 5/25/2014.
 */
public class Graph {
    private Map<IR.Reg, Set<IR.Reg>> undirectedGraph;

    public Graph() {
        this.undirectedGraph = new HashMap<>();
    }

    public void addNode(IR.Reg node) {
        undirectedGraph.put(node, new HashSet<IR.Reg>());
    }

    public void addEdge(IR.Reg node, IR.Reg neighbor) {
        undirectedGraph.get(node).add(neighbor);
    }

    public void removeNode(IR.Reg node) {
        for (Map.Entry mEn : undirectedGraph.entrySet()) {
            if (((Set) mEn.getValue()).contains(node)) {
                ((Set) mEn.getValue()).remove(node);
            }
        }
        undirectedGraph.remove(node);
    }

    @Deprecated
    public void addAllNodes(Set<IR.Reg> nodelist) {
        for (IR.Reg node : nodelist) {
            undirectedGraph.put(node, new HashSet<IR.Reg>());
        }
    }

    public Map<IR.Reg, Set<IR.Reg>> getGraph() {
        return undirectedGraph;
    }

    // TODO: Maybe replace with Map.entry
    public void printGraph() {
        for (IR.Reg node : undirectedGraph.keySet()) {
            System.out.print("\nNode: " + node.toString() + "\n");
            System.out.print("[");
            IR.Reg[] temp = undirectedGraph.get(node).toArray(new IR.Reg[undirectedGraph.get(node).size()]);
            int counter = 1;
            for (IR.Reg o : temp) {
                if (counter < temp.length) {
                    System.out.print(o.toString() + ", ");
                    counter++;
                } else {
                    System.out.print(o.toString());
                }
            }
            System.out.println("]");
        }
    }
}
