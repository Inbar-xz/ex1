//Name:Yakir Pinchas & Inbar Demuth.
//id: yakir - 203200530 inbar - 204885370.s
package utils;

import shape.Vertex;

public class Transformation {
	
	/**
	 * create scale matrix for 2D
	 * @param scaleX
	 * @param scaleY
	 * @return
	 */
	 public static double[][] CreateScaleMatrix2D(double scaleX, double scaleY) {
	        double scaleMatrix[][] = new double[3][3];
	        scaleMatrix[0][0] = scaleX;
	        scaleMatrix[1][1] = scaleY;
	        scaleMatrix[2][2] = 1;
	        return scaleMatrix;
	 }
	 
	 /**
	  * create inverse scale matrix for 2D
	  * @param scaleX
	  * @param scaleY
	  * @return
	  */
	 public static double[][] CreateScaleBackMatrix2D(double scaleX, double scaleY) {
	        return CreateScaleMatrix2D(1 / scaleX, 1 / scaleY);
	 }
	 
	 /**
	  * create rotate matrix for 2D
	  * @param rotateAngle
	  * @return
	  */
	 public static double[][] CreateRotateMatrix2D(double rotateAngle) {
	        double rotateMatrix[][] = new double[3][3];
	        rotateMatrix[0][0] = Math.cos(rotateAngle);
	        rotateMatrix[0][1] = Math.sin(rotateAngle);
	        rotateMatrix[1][0] = -1 * Math.sin(rotateAngle);
	        rotateMatrix[1][1] = Math.cos(rotateAngle);
	        rotateMatrix[2][2] = 1;
	        return rotateMatrix;
	 }
	 
	 public static double[][] CreateRotateBackMatrix2D(double rotateAngle) {
	        return CreateRotateMatrix2D(-rotateAngle);
	 }
	 
	 /**
	  * create translate matrix for 2D
	  * @param translateX
	  * @param translateY
	  * @return
	  */
	 public static double[][] CreateTranslateMatrix2D(double translateX, double translateY) {
	        double translateMatrix[][] = new double[3][3];
	        translateMatrix[0][0] = 1;
	        translateMatrix[0][2] = translateX;
	        translateMatrix[1][1] = 1;
	        translateMatrix[1][2] = translateY;
	        translateMatrix[2][2] = 1;
	        return translateMatrix;
	 }
	 
	 public static double[][] CreateTranslateBackMatrix2D(double translateX, double translateY) {
	        return CreateTranslateMatrix2D(-translateX, -translateY);
	 }
	 
	 /**
	  * create identity matrix of dim*dim
	  * @param dim
	  * @return
	  */
	 public static double[][] CreateIdentityMatrix(int dim) {
		 double initMatrix[][] = new double[dim][dim];
	        for(int i = 0; i < dim; i++)
	        	initMatrix[i][i] = 1;
	        return initMatrix;
	 }
	 
	 /**
	  * convert vertex to vector
	  * @param x
	  * @param y
	  * @return
	  */
	 public static double[][] vertexToVector2D(Vertex v) {
	        double vectorVertex[][] = new double[3][1];
	        vectorVertex[0][0] = v.getX();
	        vectorVertex[1][0] = v.getY();
	        vectorVertex[2][0] = 1;
	        return vectorVertex;
	 }
}
