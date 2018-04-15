package maindraw;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import utils.Matrixs;
import utils.Mathematics;
import shape.Vertex;
import shape.Edge;
public class MyCanvas extends Canvas{

	private static final long serialVersionUID = 1L;
	public static final String filename = "example.scn.txt"; //name of file to read data from.
	public static final String filenameSettings = "example.viw.txt"; //name of file to read data from.
	private double vertexX,vertexY;
	private int coordinateXCenterWindows, coordinateYCenterWindows;
	private double direction, tempX = 0, tempY = 0;
	private int ww , wh , vw , vh;
	private double[][] TrM, viewMatrix, CT, TT;
	private double[][] vectorVertex, matrixTr1 , matrixRo, matrixTr2, matrixTr3, matrixSc1, matrixSc2;
	public MyCanvas() {
		File settings = new File(filenameSettings);
		Scanner setScan;
		try {
			setScan = new Scanner(settings);
			setScan.next();
			coordinateXCenterWindows = setScan.nextInt();
			System.out.println(coordinateXCenterWindows);
			coordinateYCenterWindows = setScan.nextInt();
			System.out.println(coordinateYCenterWindows);
			setScan.next();
			direction = setScan.nextDouble();
			System.out.println(direction);
			setScan.next();
			ww = setScan.nextInt();
			System.out.println(ww);
			wh = setScan.nextInt();
			System.out.println(wh);
			setScan.next();
			vw = setScan.nextInt();
			System.out.println(vw);
			vh = setScan.nextInt();
			System.out.println(vh);
			setScan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//for viewer matrix
		matrixTr1 = Matrixs.CreateTranslateMatrix2D(-coordinateXCenterWindows, -coordinateYCenterWindows);
		matrixRo = Matrixs.CreateRotateMatrix2D(-direction);
		matrixSc1 = Matrixs.CreateScaleMatrix2D(vw / ww, vh / wh);
		matrixTr2 = Matrixs.CreateTranslateMatrix2D(20, 20);
	    matrixSc2 = Matrixs.CreateScaleMatrix2D(1, -1);
		matrixTr3 = Matrixs.CreateTranslateMatrix2D(0, vh + 40);
		TT = Matrixs.CreateMatrix2D();
		CT = Matrixs.CreateMatrix2D();
		vectorVertex = new double [3][1];
		setSize(vw + 40, vh + 40);
	}
	public void paint(Graphics g) {
		viewMatrix = Mathematics.multiplicateMatrix(matrixTr3, matrixSc2);	
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixTr2);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixSc1);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixRo);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixTr1);
		TrM = Mathematics.multiplicateMatrix(CT, TT);
		TrM = Mathematics.multiplicateMatrix(TrM, viewMatrix);
		//read from file
		File fileName1 = new File(filename);
		try {
			Scanner scan = new Scanner(fileName1);
			int sizeVertex = scan.nextInt();
			System.out.println(sizeVertex);
			Vertex vertexs[] = new Vertex[sizeVertex];
			for (int i = 0; i < sizeVertex; i++) { 
				vertexX = scan.nextDouble();
				System.out.println(vertexX);
				vertexY = scan.nextDouble();
				System.out.println(vertexY);
			  //  vectorVertex = Matrixs.CreateVertexVector2D(vertexX, vertexY);
			  //  vectorVertex = Mathematics.multiplicateMatrix(TrM, vectorVertex);
			    tempX = (vertexX * TrM[0][0]) + (vertexY * TrM[0][1]) + TrM[0][2] ;
			    System.out.println(tempX);
			    tempY = (vertexX * TrM[1][0]) + (vertexY * TrM[1][1]) + TrM[1][2] ;
				vertexs[i] = new Vertex(tempX,tempY);
			}
			int sizeEdge = scan.nextInt();
			System.out.println(sizeEdge);
			g.drawRect(20, 20, vw, vh);
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
