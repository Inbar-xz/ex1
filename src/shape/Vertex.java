//Name:Yakir Pinchas & Inbar Demuth.
//id: yakir - 203200530 inbar - 204885370.
package shape;

public class Vertex {
	
    private double x;
    private double y;
    private double z;
    
    /**
     * constructor.
     *
     * @param x is x
     * @param y is y
     */
    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 1;
    }
    
    /**
     * constructor.
     *
     * @param x is x
     * @param y is y
     * @param z is z
     */
    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * check if 2 vertices are equal
     * @param other
     * @return
     */
    public boolean equals(Vertex other) {
        if (other.getClass() == getClass()) {
            Vertex v = (Vertex) other;
            return ((getX() == v.getX()) && (getY() == v.getY()) && (getZ() == v.getZ()));
        }
        return false;
    }
    
    /**
     * return the value of x.
     * @return x
     */
    public double getX() {
        return this.x;
    }
    
    /**
     * return the value of y.
     * @return y
     */
    public double getY() {
        return this.y;
    }
    
    /**
     * return the value of z.
     * @return z
     */
    public double getZ() {
        return this.z;
    }
    
    /**
     * set new value to x.
     * @param newX of the point.
     */
    public void setX(double newX) {
        this.x = newX;
    }
    
    /**
     * set new value to y.
     * @param newY of the point.
     */
    public void setY(double newY) {
        this.y = newY;
    }
    
    /**
     * set new value to z.
     * @param newZ of the point.
     */
    public void setZ(double newZ) {
        this.z = newZ;
    }
}
