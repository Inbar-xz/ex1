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
     * change the value of x.
     * @param newX of the point.
     */
    public void setX(double newX) {
        this.x = newX;
    }
    /**
     * change the value of y.
     * @param newY of the point.
     */
    public void setY(double newY) {
        this.y = newY;
    }
}
