package shape;

public class Edge {
    private Vertex v1;
    private Vertex v2;
    /**
     * constructor.
     *
     * @param v1 is one vertex
     * @param v2 is second vertex
     */
    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
    public boolean equals(Edge other) {
        if (other.getClass() == getClass()) {
            Edge e = (Edge) other;
            return ((getV1() == e.getV1()) && (getV2() == e.getV2()));
        }
        return false;
    }
    /**
     * return the value of first vertex.
     * @return v1
     */
    public Vertex getV1() {
        return this.v1;
    }
    /**
     * return the value of second vertex.
     * @return v2
     */
    public Vertex getV2() {
        return this.v2;
    }
    /**
     * change the value of v1.
     * @param newV1 of the vertex.
     */
    public void setV1(Vertex newV1) {
        this.v1 = newV1;
    }
    /**
     * change the value of v2.
     * @param newV2 of the vertex.
     */
    public void setV2(Vertex newV2) {
        this.v2 = newV2;
    }
    
   // public Polygon createEdge() {
   // 	
   // }
}
