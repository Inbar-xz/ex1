//Name:Yakir Pinchas & Inbar Demuth.
//id: yakir - 203200530 inbar - 204885370.
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
    
    /**
     * check if 2 edged are equal
     * @param other
     * @return
     */
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
     * set new value to the first vertex.
     * @param newV1 of the vertex.
     */
    public void setV1(Vertex newV1) {
        this.v1 = newV1;
    }
    
    /**
     * set new value to the second vertex.
     * @param newV2 of the vertex.
     */
    public void setV2(Vertex newV2) {
        this.v2 = newV2;
    }
}
