package maindraw;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import utils.Transformation;
import utils.Mathematics;
import shape.Vertex;
import shape.Edge;

public class MyCanvas extends Canvas implements MouseListener,  MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	public static final String scnFile = "example.scn.txt";
	public static final String viwFile = "example.viw.txt";
	
	private String locationTranspormation;
	private Point pStart, pEnd;
	private double centerX , centerY, radiusPStart, radiusPEnd, scaleParameter;
	private double sepertation = 83 + 1/3;
	private double ww , wh , vw , vh;
	private double[] vectorStart, vectorEnd;
	private double[][] vectorVertex, TrM, viewMatrix, currentTrans, totalTrans;
	private Vertex verticesList[], verticesDraw[];
	private Edge edgesList[];
	
	public MyCanvas() {
		
		double coordinateXCenterWindows = 0, coordinateYCenterWindows = 0;
		double direction = 0;
		double[][] matrixTr1 , matrixRo, matrixTr2, matrixTr3, Transformationc1, Transformationc2;
		
		//read the view file to create the view matrix
		File viewFile = new File(viwFile);
		Scanner setScan;
		try {
			setScan = new Scanner(viewFile);
			setScan.next();
			coordinateXCenterWindows = setScan.nextDouble();
			coordinateYCenterWindows = setScan.nextDouble();
			setScan.next();
			direction = setScan.nextDouble();
			setScan.next();
			ww = setScan.nextDouble();
			wh = setScan.nextDouble();
			setScan.next();
			vw = setScan.nextDouble();
			vh = setScan.nextDouble();
			setScan.close();
			
			//check
			System.out.println(coordinateXCenterWindows);
			System.out.println(coordinateYCenterWindows);
			System.out.println(direction);
			System.out.println(ww);
			System.out.println(wh);
			System.out.println(vw);
			System.out.println(vh);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create the view matrix
		matrixTr1 = Transformation.CreateTranslateMatrix2D(-coordinateXCenterWindows, -coordinateYCenterWindows);
		matrixRo = Transformation.CreateRotateMatrix2D(-1 * Math.toRadians(direction));
		Transformationc1 = Transformation.CreateScaleMatrix2D(vw / ww, vh / wh);
		matrixTr2 = Transformation.CreateTranslateMatrix2D(20, 20);
	    Transformationc2 = Transformation.CreateScaleMatrix2D(1, -1);
		matrixTr3 = Transformation.CreateTranslateMatrix2D(0, vh + 40);
		
		viewMatrix = Mathematics.multiplicateMatrix(matrixTr3, Transformationc2);	
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixTr2);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, Transformationc1);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixRo);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixTr1);
		
		//initialize the current matrix and the total matrix
		totalTrans = Transformation.CreateIdentityMatrix(3);
		currentTrans = Transformation.CreateIdentityMatrix(3);
		
		//set the center vertex
		centerX = (vw / 2) + 20;
		centerY = (vh / 2) + 20;
				
		setSize((int)vw + 40, (int)vh + 40);
		
		vectorVertex = new double [3][1];
		vectorStart = new double[2];
		vectorEnd = new double[2];
		
		//read the vertices and the edges for the file
		File screenFile = new File(scnFile);
		try {
			Scanner scan = new Scanner(screenFile);
			
			//read vertices and save 2 copies
			int verticesNum = scan.nextInt();
			verticesList = new Vertex[verticesNum];
			verticesDraw = new Vertex[verticesNum];
			double vertexX, vertexY;
			for (int i = 0; i < verticesNum; i++) { 
				vertexX = scan.nextDouble();
				vertexY = scan.nextDouble();
				verticesList[i] = new Vertex(vertexX, vertexY);
				verticesDraw[i] = new Vertex(vertexX, vertexY);
			}
			
			//read the edges and create edges with the vertices for the verticesDraw 
			int edgeNum = scan.nextInt();
			edgesList = new Edge[edgeNum];
			for (int i = 0; i < edgeNum; i++) {
				edgesList[i] = new Edge(verticesDraw[scan.nextInt()], verticesDraw[scan.nextInt()]);
			}
			scan.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paint(Graphics g) {
		
		double vertexX, vertexY;
		
		TrM = Mathematics.multiplicateMatrix(currentTrans, totalTrans);
		TrM = Mathematics.multiplicateMatrix(TrM, viewMatrix);
		
		//apply changes on the vertices in verticesDraw, using the origin vertices in verticesList
		int verticesNum = verticesList.length;
		for (int i = 0; i < verticesNum; i++) {
			vectorVertex = Transformation.vertexToVector2D(verticesList[i]);
		    vectorVertex = Mathematics.multiplicateMatrix(TrM, vectorVertex);
		    vertexX = vectorVertex[0][0] + 20;
		    vertexY = vectorVertex[1][0] + 20;
		    verticesDraw[i].setX(vertexX);
		    verticesDraw[i].setY(vertexY);
		}
		
		//draw the new vertices and edges
		g.drawRect(20, 20, (int)vw, (int)vh);
		Polygon p = new Polygon();
		int edgesNum = edgesList.length;
		for (int i = 0; i < edgesNum; i++) {
			p.addPoint((int) edgesList[i].getV1().getX(), (int) edgesList[i].getV1().getY());
			p.addPoint((int) edgesList[i].getV2().getX(), (int) edgesList[i].getV2().getY());	
			g.setColor(Color.BLUE);
			g.drawPolygon(p);
			p.reset();
		}
		
		/*
		//read the vertices for the file and create the new vertices for the paint
		File fileName1 = new File(scnFile);
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
			    
				vectorVertex = Transformation.vertexToVector2D(new Vertex(vertexX, vertexY));
			    vectorVertex = Mathematics.multiplicateMatrix(TrM, vectorVertex);
			    vertexX = vectorVertex[0][0] + 20;
			    vertexY = vectorVertex[1][0] + 20;
				vertexs[i] = new Vertex(vertexX,vertexY);
			}
			
			//read the edges and draw
			int sizeEdge = scan.nextInt();
			System.out.println(sizeEdge);
			g.drawRect(20, 20, (int)vw, (int)vh);
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
		}*/
	}
	public String getTypeTranspormaton(Point point) {
		//the limits of screen is 20 - 270
		String type = "";
		if((pStart.getX() >= 20) && (pStart.getX() <= 270)
				&& (pStart.getY() >= 20) && (pStart.getY() <= 270)) { 
			if(pStart.getX() <= 20 + sepertation) {
				if(pStart.getY() <= 20 + sepertation) {
					type = "RLU";
				} else if(pStart.getY() <= 20 + (sepertation * 2)) {
					type = "SL";
				} else {
					type = "RLD";
				}	
			} else if(pStart.getX() <= 20 + (sepertation * 2)) {
				if(pStart.getY() <= 20 + sepertation) {
					type = "SU";
				} else if(pStart.getY() <= 20 + (sepertation * 2)) {
					type = "T";
				} else {
					type = "SD";
				}
			} else {
				if(pStart.getY() <= 20 + sepertation) {
					type = "RRU";
				} else if(pStart.getY() <= 20 + (sepertation * 2)) {
					type = "SR";
				} else {
					type = "RRD";
				}
			}
		}
		return type;
	}
	public double getAngleFromVectorToXAxis(double[] vector) {
		double angle;
		float RAD2DEG = 180.0f / 3.14159f;
		// atan2 receives first Y second X
		angle = Math.atan2(vector[1], vector[0]) * RAD2DEG;
		if (angle < 0) angle += 360.0f;
		return angle;
	}
	public void executeScale() {
		 radiusPStart = Mathematics.distance(pStart, centerX, centerY);
		 radiusPEnd = Mathematics.distance(pEnd, centerX, centerY);
		 scaleParameter = radiusPEnd / radiusPStart;
		 currentTrans = Transformation.CreateScaleMatrix2D(scaleParameter, scaleParameter);
		 currentTrans = Mathematics.multiplicateMatrix
				 (Mathematics.multiplicateMatrix
						 (Transformation.CreateTranslateMatrix2D(centerX, centerY), currentTrans)
						 , Transformation.CreateTranslateMatrix2D(-centerX, -centerY));
	}
	public void executeRotate() {
		 vectorStart[0] = pStart.getX() - centerX;
		 vectorStart[1] = pStart.getY() - centerY;
		 vectorEnd[0] = pEnd.getX() - centerX;
		 vectorEnd[1] = pEnd.getY() - centerY;
		 double angleStart = getAngleFromVectorToXAxis(vectorStart);
		 double angleEnd = getAngleFromVectorToXAxis(vectorEnd);
		 double angleFinish = angleStart - angleEnd;
		 currentTrans = Transformation.CreateRotateMatrix2D(Math.toRadians(angleFinish));
		 currentTrans = Mathematics.multiplicateMatrix
				 (Mathematics.multiplicateMatrix
						 (Transformation.CreateTranslateMatrix2D(centerX, centerY), currentTrans)
						 , Transformation.CreateTranslateMatrix2D(-centerX, -centerY));
	}
	public void executeAction(String type) {
		switch(type) {
		 case "T":  currentTrans = Transformation.CreateTranslateMatrix2D(pEnd.getX() - pStart.getX(),
					pEnd.getY() - pStart.getY());
         break;
		 case "SD": executeScale();
		 break;
		 case "SU": executeScale();
	     break;
		 case "SL": executeScale();
	     break;
		 case "SR": executeScale();
		 break;
		 case "RLU": executeRotate();
		 break;
		 case "RLD": executeRotate();
		 break;
		 case "RRU": executeRotate();
		 break;
		 case "RRD": executeRotate();
		 break;
		 default: 
         break;
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		pStart = arg0.getPoint();
		System.out.println(pStart.getX());
		System.out.println(pStart.getY());
		locationTranspormation =  getTypeTranspormaton(pStart);
		//System.out.println(locationTranspormation);
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		pEnd = arg0.getPoint();
		System.out.println(pEnd.getX());
		System.out.println(pEnd.getY());
		executeAction(locationTranspormation);
		totalTrans = Mathematics.multiplicateMatrix(currentTrans, totalTrans);
		currentTrans = Transformation.CreateIdentityMatrix(3);
		this.repaint();
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		pEnd = arg0.getPoint();
		System.out.println(pEnd.getX());
		System.out.println(pEnd.getY());
		if((pStart.getX() >= 20) && (pStart.getX() <= 270)
				&& (pStart.getY() >= 20) && (pStart.getY() <= 270))
			executeAction(locationTranspormation);
		this.repaint();
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
