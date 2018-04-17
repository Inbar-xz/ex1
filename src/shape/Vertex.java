package shape;

public class Vertex {
	
    private double x;
    private double y;
    
    /**
     * constructor.
     *
     * @param x is x
     * @param y is y
     */
    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * check if 2 vertices are equal
     * @param other
     * @return
     */
    public boolean equals(Vertex other) {
        if (other.getClass() == getClass()) {
            Vertex v = (Vertex) other;
            return ((getX() == v.getX()) && (getY() == v.getY()));
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
}
