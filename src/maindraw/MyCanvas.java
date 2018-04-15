package maindraw;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import utils.Transformations;
import utils.Mathematics;
import shape.Vertex;
import shape.Edge;

public class MyCanvas extends Canvas{

	private static final long serialVersionUID = 1L;
	
	//data file for the view and the screen
	public static final String scnFile = "example.scn.txt";
	public static final String viwFile = "example.viw.txt";
	
	private int coordinateXCenterWindows, coordinateYCenterWindows;
	private double direction;
	private int windowWidth , windowHigh , viewWidth , viewHigh;
	private double[][] transMatrix, viewMatrix, currentTrans, totalTrans;
	private double[][] matrixTr1 , matrixRo, matrixTr2, matrixTr3, matrixSc1, matrixSc2;
	
	public MyCanvas() {
		
		//get the data from the view file
		File viewFile = new File(viwFile);
		Scanner setScan;
		try {
			setScan = new Scanner(viewFile);
			setScan.next();
			coordinateXCenterWindows = setScan.nextInt();
			coordinateYCenterWindows = setScan.nextInt();
			setScan.next();
			direction = setScan.nextDouble();
			setScan.next();
			windowWidth = setScan.nextInt();
			windowHigh = setScan.nextInt();
			setScan.next();
			viewWidth = setScan.nextInt();
			viewHigh = setScan.nextInt();
			setScan.close();
			
			//check
			System.out.println(coordinateXCenterWindows);
			System.out.println(coordinateYCenterWindows);
			System.out.println(direction);
			System.out.println(windowWidth);
			System.out.println(windowHigh);
			System.out.println(viewWidth);
			System.out.println(viewHigh);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create the view matrix
		matrixTr1 = Transformations.CreateTranslateMatrix2D(-coordinateXCenterWindows, -coordinateYCenterWindows);
		matrixRo = Transformations.CreateRotateMatrix2D(-direction);
		matrixSc1 = Transformations.CreateScaleMatrix2D(viewWidth / windowWidth, viewHigh / windowHigh);
		matrixTr2 = Transformations.CreateTranslateMatrix2D(20, 20); //margin of 20
	    matrixSc2 = Transformations.CreateScaleMatrix2D(1, -1);
		matrixTr3 = Transformations.CreateTranslateMatrix2D(0, viewHigh + 40);
		viewMatrix = Mathematics.multiplicateMatrix(matrixTr3, matrixSc2);	
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixTr2);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixSc1);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixRo);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixTr1);
		 
		//init the TotalTrans matrix and the CurrentTrans matrix
		totalTrans = Transformations.CreateIdentityMatrix(3);
		currentTrans = Transformations.CreateIdentityMatrix(3);
		
		setSize(viewWidth + 40, viewHigh + 40);
	}
	public void paint(Graphics g) {
		double vertexX, vertexY, tempX = 0, tempY = 0;
		//mult all the transformations with the view matrix
		transMatrix = Mathematics.multiplicateMatrix(currentTrans, totalTrans);
		transMatrix = Mathematics.multiplicateMatrix(transMatrix, viewMatrix);
		
		//read data from the screen file
		File screenFile = new File(scnFile);
		try {
			Scanner scan = new Scanner(screenFile);
			int verticesNum = scan.nextInt();
			Vertex vertexs[] = new Vertex[verticesNum];
			
			//read the vertices and apply the transformation
			for (int i = 0; i < verticesNum; i++) { 
				vertexX = scan.nextDouble();
				vertexY = scan.nextDouble();
			    tempX = (vertexX * transMatrix[0][0]) + (vertexY * transMatrix[0][1]) + transMatrix[0][2];
			    tempY = (vertexX * transMatrix[1][0]) + (vertexY * transMatrix[1][1]) + transMatrix[1][2];
				vertexs[i] = new Vertex(tempX,tempY);
			}
			int sizeEdge = scan.nextInt();
			System.out.println(sizeEdge);
			g.drawRect(20, 20, viewWidth, viewHigh);
			Polygon p = new Polygon();
			Edge edges[] = new Edge[sizeEdge];
			for (int i = 0; i < sizeEdge; i++) {
				edges[i] = new Edge(vertexs[scan.nextInt()],vertexs[scan.nextInt()]);
				p.addPoint((int) edges[i].getV1().getX(), (int) edges[i].getV1().getY());
				p.addPoint((int) edges[i].getV2().getX(), (int) edges[i].getV2().getY());	
				g.setColor(Color.BLUE);
				g.drawPolygon(p);
				p.reset();
			}
			scan.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
