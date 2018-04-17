package utils;

public class Matrixs {
	 public static double[][] CreateScaleMatrix2D(double scaleX, double scaleY) {
	        double scaleMatrix[][] = new double[3][3];
	        scaleMatrix[0][0] = scaleX;
	        scaleMatrix[0][1] = 0;
	        scaleMatrix[0][2] = 0;
	        scaleMatrix[1][0] = 0;
	        scaleMatrix[1][1] = scaleY;
	        scaleMatrix[1][2] = 0;
	        scaleMatrix[2][0] = 0;
	        scaleMatrix[2][1] = 0;
	        scaleMatrix[2][2] = 1;
	        return scaleMatrix;
	 }
	 public static double[][] CreateScaleBackMatrix2D(double scaleX, double scaleY) {
	        return CreateScaleMatrix2D(1 / scaleX, 1 / scaleY);
	 }
	 public static double[][] CreateRotateMatrix2D(double rotateAngle) {
	        double rotateMatrix[][] = new double[3][3];
	        rotateMatrix[0][0] = Math.cos(rotateAngle);
	        rotateMatrix[0][1] = Math.sin(rotateAngle);
	        rotateMatrix[0][2] = 0;
	        rotateMatrix[1][0] = -1 * Math.sin(rotateAngle);
	        rotateMatrix[1][1] = Math.cos(rotateAngle);
	        rotateMatrix[1][2] = 0;
	        rotateMatrix[2][0] = 0;
	        rotateMatrix[2][1] = 0;
	        rotateMatrix[2][2] = 1;
	        return rotateMatrix;
	 }
	 public static double[][] CreateRotateBackMatrix2D(double rotateAngle) {
	        return CreateRotateMatrix2D(-rotateAngle);
	 }
	 public static double[][] CreateTranslateMatrix2D(double translateX, double translateY) {
	        double translateMatrix[][] = new double[3][3];
	        translateMatrix[0][0] = 1;
	        translateMatrix[0][1] = 0;
	        translateMatrix[0][2] = translateX;
	        translateMatrix[1][0] = 0;
	        translateMatrix[1][1] = 1;
	        translateMatrix[1][2] = translateY;
	        translateMatrix[2][0] = 0;
	        translateMatrix[2][1] = 0;
	        translateMatrix[2][2] = 1;
	        return translateMatrix;
	 }
	 public static double[][] CreateTranslateBackMatrix2D(double translateX, double translateY) {
	        return CreateTranslateMatrix2D(-translateX, -translateY);
	 }
	 public static double[][] CreateMatrix2D() {
		 double initMatrix[][] = new double[3][3];
	        for(int i = 0; i < 3; i++)
	        	initMatrix[i][i] = 1;
	        return initMatrix;
	 }
	 public static double[][] CreateVertexVector2D(double x, double y) {
	        double vectorVertex[][] = new double[3][1];
	        vectorVertex[0][0] = x;
	        vectorVertex[1][0] = y;
	        vectorVertex[2][0] = 1;
	        return vectorVertex;
	 }
}
