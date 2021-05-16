import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class TriestImpr implements DataStreamAlgo {
    /*
     * Constructor.
     * The parameter samsize denotes the size of the sample, i.e., the number of
     * edges that the algorithm can store.
     */
    private boolean debug = false;
    private int D = 0;
    private double t = 0;
    private double M;
    private HashMap<Integer, HashSet<Integer>> G = new HashMap<Integer, HashSet<Integer>>();
    private ArrayList<Edge> currentEdges = new ArrayList<Edge>();
    
	public TriestImpr(int samsize) {
        M = (double) samsize;
	}


    private void safeAddEdge(Edge e) {
        int u = e.u;
        int v = e.v;
        if (G.containsKey(u)){
            G.get(u).add(v);
        } else {
            HashSet<Integer> tmpSet = new HashSet<Integer>();
            tmpSet.add(v);
            G.put(u, tmpSet);
        }
        if (G.containsKey(v)){
            G.get(v).add(u);
        } else {
            HashSet<Integer> tmpSet = new HashSet<Integer>();
            tmpSet.add(u);
            G.put(v, tmpSet);
        }
        currentEdges.add(e);
        if (debug) System.err.println("G: " + G.toString());
        if (debug) System.err.println("currentEdges: " + currentEdges.toString());
    }


    public int trianglesInvolved(Edge e) {
        int u = e.u;
        int v = e.v;
        // number of elements in the intersection between the neighborhoods of u and v
        if (G.get(v) == null || G.get(u) == null) return 0;
        HashSet<Integer> intersect = new HashSet<Integer>(G.get(u)); 
        intersect.retainAll(G.get(v));
        return intersect.size();
    }


	public void handleEdge(Edge edge) {
        if (debug) System.out.println("----------Handling Edge----------");
        t++;
        int u = edge.u;
        int v = edge.v;
        
        if (debug) System.out.println("t: " + t + " M: " + M);
        //provided that u and v are different vertices.
        if (u == v) return;
        int g = trianglesInvolved(edge);
        double nume = (t - 1) * (t - 2);
        double denom = M * (M - 1);
        double n = nume / denom;
        if (denom == 0) n = 0;
        D = D + (int) (g * n);

        if (t <= M) {
            // Add v to the the neighborhood of u
            if (debug) System.out.println("Adding... u: " + u + " v: " + v);
            safeAddEdge(edge);
            //D = D + trianglesInvolved(edge);
            if (debug) System.out.println("D: " + D);
        } else {
            double bias = M/t;
            double coinToss = Math.random();
            boolean isHeads = coinToss > bias;

            if (debug) System.out.println("bias: " + bias + " coinToss: " + coinToss + " isHeads: " + isHeads);
            if (isHeads) {
                return;
            } else {
                // Choose an edge in S uniformly at random and replace it with edge
                int randIndex = (int) (currentEdges.size() * Math.random());
                Edge toRemove = currentEdges.get(randIndex);
                if (debug) System.out.println("Adding... u: " + u + " v: " + v);
                safeAddEdge(edge);
                if (debug) System.out.println("Removing... u: " + toRemove.u + " v: " + toRemove.v);
                //D = D - trianglesInvolved(toRemove);
                //D = D + trianglesInvolved(edge);
                currentEdges.remove(randIndex);
                G.get(toRemove.u).remove(toRemove.v);
                G.get(toRemove.v).remove(toRemove.u);
                if (debug) System.out.println("D: " + D);
            }
        }

        
        //increase D by the number of elements in the intersection between the neighborhoods of u and v
    }


	public int getEstimate() {
        return D;
        /*
        double nume = M * (M - 1) * (M - 2);
        double denom = t * (t - 1) * (t - 2);
        if (denom == 0) return 0;
        double pi = nume / denom;
        if (pi == 0) return 0;
        double estimate = D / pi;
        if (debug) System.out.println("pi: " + pi + " estimate: " + estimate);
        // Assuming the estimate should be rounded to nearest int?
        return (int) Math.round(estimate);*/
    }
}
