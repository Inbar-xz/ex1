//Name:Yakir Pinchas & Inbar Demuth.
//id: yakir - 203200530 inbar - 204885370.
package utils;

import java.awt.Point;

import shape.Vertex;

/**
 * Math functions class.
 */
public class Mathematics {
	
    /**
     * Calculates average of two numbers.
     *
     * @param num1 first number
     * @param num2 second number
     * @return average
     */
    public static double average(double num1, double num2) {
        return (num1 + num2) / 2;
    }
    
    /**
     * Determines if var is between the numbers.
     *
     * @param num1 first border
     * @param var  number
     * @param num2 second border
     * @return true\false
     */
    public static boolean isBetween(double num1, double var, double num2) {
        return (num1 <= var && var <= num2) || (num2 <= var && var <= num1);
    }
    
    /**
     * calculate the distance between two points.
     * @param p1 is first point.
     * @param x is x of second point.
     * @param y is y of second point.
     * @return distance
     */
    public static double distance(Point p1, double x, double y) {
        return Math.sqrt(Math.pow(p1.getX() - x, 2)
                    + Math.pow(p1.getY() - y, 2));
    }
    
    /**
     * calculate the distance between two vertices.
     * @param p1 is first point.
     * @param x is x of second point.
     * @param y is y of second point.
     * @return distance
     */
    public static double distance(Vertex p1, Vertex p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2)
                    + Math.pow(p1.getY() - p2.getY(), 2));
    }
    
    /**
     * calculate the vectorLenght.
     * @param v vector.
     * @return length
     */
    public static double vectorLength(double[] v) {
        return Math.sqrt(Math.pow(v[0], 2)
                    + Math.pow(v[1], 2));
    }
    
    /**
     * Return a^2 + b^2.
     *
     * @param a first number
     * @param b second number
     * @return c^2
     */
    public static double pythagoras(double a, double b) {
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    /**
     * Convet string array to int array.
     *
     * @param sArr string array to convert
     * @return Int array
     */
    public static int[] stringsToInts(String[] sArr) {
        int[] iArr = new int[sArr.length];

        for (int i = 0; i < sArr.length; i++) {
            iArr[i] = Integer.parseInt(sArr[i]);
        }
        return iArr;
    }
    
    /**
     * calculate the transpose matrix.
     * @param matrix is original matrix.
     * @return the transpose matrix(B = A^T)
     */
    public static double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] transposeMatrix = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                transposeMatrix[j][i] = matrix[i][j];
        return transposeMatrix;
    }
    
    /**
     * calculate the add of two matrix in same size
     * @param matrixA is first matrix.
     * @param matrixB is second matrix.
     * @return addition matrix(C = A + B)
     */
    public static double[][] add(double[][] matrixA, double[][] matrixB) {
        int rows = matrixA.length;
        int cols = matrixA[0].length;
        double[][] answer = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                answer[i][j] = matrixA[i][j] + matrixB[i][j];
        return answer;
    }
    
    /**
     * calculate the sub of two matrix in same size
     * @param matrixA is first matrix.
     * @param matrixB is second matrix.
     * @return sub matrix(C = A - B)
     */
    public static double[][] subtract(double[][] matrixA, double[][] matrixB) {
        int rows = matrixA.length;
        int cols = matrixA[0].length;
        double[][] answer = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                answer[i][j] = matrixA[i][j] - matrixB[i][j];
        return answer;
    }
    
    /**
     * calculate the multipication of matrix and vector(point)
     * @param matrixA
     * @param ver
     * @return
     */
    public static double[][] multMatrixAndVertex(double[][] matrixA, Vertex ver){
    	double vectorVertex[][] = new double[3][1];
        vectorVertex[0][0] = ver.getX();
        vectorVertex[1][0] = ver.getY();
        vectorVertex[2][0] = ver.getZ();
        return multMatrixs(matrixA, vectorVertex);
    }
    
    /**
     * calculate the multipication of two matrix in same size
     * @param matrixA is first matrix.
     * @param matrixB is second matrix.
     * @return multipication matrix(C = A * B)
     */
    public static double[][] multMatrixs(double[][] matrixA, double[][] matrixB) {

        int aRows = matrixA.length;
        int aColumns = matrixA[0].length;
        int bRows = matrixB.length;
        int bColumns = matrixB[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] C = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0.00000;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        return C;
    }
    
    /**
     * calculate the multiplication of matrix-vector
     * @param a is matrix.
     * @param x is vector.
     * @return multiplication (y = A * x)
     */
    public static double[] multiplyMatrixInVector(double[][] a, double[] x) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += a[i][j] * x[j];
        return y;
    }
    
    /**
     * calculate the multiplication of vector-matrix
     * @param x is vector.
     * @param a is matrix.
     * @return multiplication (y =  x^T * A)
     */
    public static double[] multiplyVectorInMatrix(double[] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += a[i][j] * x[i];
        return y;
    }
    
    /**
	 * get the angle from the vector to x axis
	 * @param vector
	 * @return
	 */
	public static double angleWithXAxis(double[] vector) {
		double angle;
		float RAD2DEG = 180.0f / 3.14159f;
		// atan2 receives first Y second X
		angle = Math.atan2(vector[1], vector[0]) * RAD2DEG;
		if (angle < 0) angle += 360.0f;
		return angle;
	}
}
