//Name:Yakir Pinchas & Inbar Demuth.
//id: yakir - 203200530 inbar - 204885370.
package maindraw;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class MyCanvas extends Canvas implements MouseListener,  MouseMotionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	public static final String scnFile = "example.scn.txt";
	public static final String viwFile = "example.viw.txt";
	
	private String transType;
	private Point pStart, pEnd;
	private int margins = 20;
	private double centerX , centerY;
	private double viewWidth , viewHigh;
	private double[] vectorStart, vectorEnd;
	private double[][] viewMatrix, currentTrans, totalTrans;
	private Vertex verticesList[], verticesDraw[];
	private Edge edgesList[];
	
	/**
	 * constructor. read data from files, create the view matrix,
	 * and initialize basis parameters.
	 */
	public MyCanvas() {
		
		double coordinateXCenterWindows = 0, coordinateYCenterWindows = 0;
		double direction = 0;
		double[][] matrixTr1 , matrixRo, matrixTr2, matrixTr3, Transformationc1, Transformationc2;
		double windowWidth = 0, windowHigh = 0;
		
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
			windowWidth = setScan.nextDouble();
			windowHigh = setScan.nextDouble();
			setScan.next();
			viewWidth = setScan.nextDouble();
			viewHigh = setScan.nextDouble();
			setScan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create the view matrix
		matrixTr1 = Transformation.CreateTranslateMatrix2D(-coordinateXCenterWindows, -coordinateYCenterWindows);
		matrixRo = Transformation.CreateRotateMatrix2D(-1 * Math.toRadians(direction));
		Transformationc1 = Transformation.CreateScaleMatrix2D(viewWidth / windowWidth, viewHigh / windowHigh);
		matrixTr2 = Transformation.CreateTranslateMatrix2D(20, 20);
	    Transformationc2 = Transformation.CreateScaleMatrix2D(1, -1);
		matrixTr3 = Transformation.CreateTranslateMatrix2D(0, viewHigh + 40);
		
		viewMatrix = Mathematics.multiplicateMatrix(matrixTr3, Transformationc2);	
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixTr2);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, Transformationc1);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixRo);
		viewMatrix = Mathematics.multiplicateMatrix(viewMatrix, matrixTr1);
		
		//initialize the current matrix and the total matrix
		totalTrans = Transformation.CreateIdentityMatrix(3);
		currentTrans = Transformation.CreateIdentityMatrix(3);
		
		//set the center vertex
		centerX = (viewWidth / 2) + margins;
		centerY = (viewHigh / 2) + margins;
		
		//set the first click point and the end click point
		vectorStart = new double[2];
		vectorEnd = new double[2];
		
		setSize((int)viewWidth + 40, (int)viewHigh + 40);
		
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
		addKeyListener(this);
	}
	
	/**
	 * this function draw the list of vertices and the edges
	 * after the translations
	 * @param g
	 * @return
	 */
	public void paint(Graphics g) {
		
		double vertexX, vertexY;
		double[][] transMatrix;
		
		//mult all the translations
		transMatrix = Mathematics.multiplicateMatrix(currentTrans, totalTrans);
		transMatrix = Mathematics.multiplicateMatrix(transMatrix, viewMatrix);
		
		//apply changes on the vertices in verticesDraw, using the origin vertices in verticesList
		int verticesNum = verticesList.length;
		double[][] vectorVertex = new double [3][1];
		for (int i = 0; i < verticesNum; i++) {
			vectorVertex = Transformation.vertexToVector2D(verticesList[i]);
		    vectorVertex = Mathematics.multiplicateMatrix(transMatrix, vectorVertex);
		    vertexX = vectorVertex[0][0] + margins;
		    vertexY = vectorVertex[1][0] + margins;
		    verticesDraw[i].setX(vertexX);
		    verticesDraw[i].setY(vertexY);
		}
		
		//draw the new vertices and edges
		g.drawRect(margins, margins, (int)viewWidth, (int)viewHigh);
		Polygon p = new Polygon();
		int edgesNum = edgesList.length;
		for (int i = 0; i < edgesNum; i++) {
			p.addPoint((int) edgesList[i].getV1().getX(), (int) edgesList[i].getV1().getY());
			p.addPoint((int) edgesList[i].getV2().getX(), (int) edgesList[i].getV2().getY());	
			g.setColor(Color.BLUE);
			g.drawPolygon(p);
			p.reset();
		}
	}
	/**
	 * get point and return the type of transformation that will
	 * be create.
	 * the matrix seperate in this direction:
	 * ROTATE(RLU) SCALE(SU) ROTATE(RRU)
	 * SCALE(SL) TRANSLATE(T) SCALE (SR)
	 * ROTATE(RLD) SCALE(SD) ROTATE(RRD)
	 * @param point
	 * @return
	 */
	public String getTransType(Point point) {

		//the limits of screen is [margins - (margins+viewWidth)] in this case [20 - 270]
		double sepertation = viewWidth / 3;
		String type = "";
		
		//if click in the right range (margins <-> viewWidth) then check transType
		if((pStart.getX() >= margins) && (pStart.getX() <= viewWidth + margins)
				&& (pStart.getY() >= margins) && (pStart.getY() <= viewWidth + margins)) {
			
			if(pStart.getX() <= margins + sepertation) {
				if(pStart.getY() <= margins + sepertation) {
					type = "RLU";
				} else if(pStart.getY() <= margins + (sepertation * 2)) {
					type = "SL";
				} else {
					type = "RLD";
				}	
			} else if(pStart.getX() <= margins + (sepertation * 2)) {
				if(pStart.getY() <= margins + sepertation) {
					type = "SU";
				} else if(pStart.getY() <= margins + (sepertation * 2)) {
					type = "T";
				} else {
					type = "SD";
				}
			} else {
				if(pStart.getY() <= margins + sepertation) {
					type = "RRU";
				} else if(pStart.getY() <= margins + (sepertation * 2)) {
					type = "SR";
				} else {
					type = "RRD";
				}
			}
		}
		return type;
	}
	
	/**
	 * get the angle from the vector to x axis
	 * @param vector
	 * @return
	 */
	public double getAngleFromVectorToXAxis(double[] vector) {
		double angle;
		float RAD2DEG = 180.0f / 3.14159f;
		// atan2 receives first Y second X
		angle = Math.atan2(vector[1], vector[0]) * RAD2DEG;
		if (angle < 0) angle += 360.0f;
		return angle;
	}
	
	/**
	 * execute scale transformation.
	 */
	public void executeScale() {
		double radiusPStart, radiusPEnd, scaleParameter;
		
		//calculate the radius of start point and end point according to function
		//that calculate the distance bettwen two points
		radiusPStart = Mathematics.distance(pStart, centerX, centerY);
		radiusPEnd = Mathematics.distance(pEnd, centerX, centerY);
		
		//the parameter that send to scale matrix is radius end / radius start
		scaleParameter = radiusPEnd / radiusPStart;
		
		//Current Transformation is translate(cx,cy) * scale(s,s) * translate(-cx,-cy) 
		currentTrans = Transformation.CreateScaleMatrix2D(scaleParameter, scaleParameter);
		currentTrans = Mathematics.multiplicateMatrix
			 (Mathematics.multiplicateMatrix
					 (Transformation.CreateTranslateMatrix2D(centerX, centerY), currentTrans)
					 , Transformation.CreateTranslateMatrix2D(-centerX, -centerY));
	}
	
	/**
	 * execute rotation transformation.
	 */
	public void executeRotate() {
		
		//vector start = (x of start point - x of center point,y of start point - y of center point) 
		vectorStart[0] = pStart.getX() - centerX;
		vectorStart[1] = pStart.getY() - centerY;
		
		//vector start = (x of end point - x of center point,y of end point - y of center point)
		vectorEnd[0] = pEnd.getX() - centerX;
		vectorEnd[1] = pEnd.getY() - centerY;
		
		//calculate the angleStart and angleEnd according to function that
		//calculate the angle between vector to XAxis.
		double angleStart = getAngleFromVectorToXAxis(vectorStart);
		double angleEnd = getAngleFromVectorToXAxis(vectorEnd);
		double angleFinish = angleStart - angleEnd;
		
		//Current Transformation is translate(cx,cy) * rotate(angle) * translate(-cx,-cy) 
		currentTrans = Transformation.CreateRotateMatrix2D(Math.toRadians(angleFinish));
		currentTrans = Mathematics.multiplicateMatrix
				 (Mathematics.multiplicateMatrix
						 (Transformation.CreateTranslateMatrix2D(centerX, centerY), currentTrans)
						 , Transformation.CreateTranslateMatrix2D(-centerX, -centerY));
	}
	
	/**
	 * execute transformation by type
	 * the matrix seperate in this direction:
	 * ROTATE(RLU) SCALE(SU) ROTATE(RRU)
	 * SCALE(SL) TRANSLATE(T) SCALE (SR)
	 * ROTATE(RLD) SCALE(SD) ROTATE(RRD)
	 * @param type
	 */
	public void executeAction(String type) {
		switch(type) {
		 case "T":  currentTrans = Transformation.CreateTranslateMatrix2D(pEnd.getX() - pStart.getX(),
					pEnd.getY() - pStart.getY());
         break;
		 case "SD":
		 case "SU":
		 case "SL":
		 case "SR": executeScale();
		 break;
		 case "RLU":
		 case "RLD":
		 case "RRU":
		 case "RRD": executeRotate();
		 break;
		 default: 
         break;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		
		//save the start point and get the transformation type 
		pStart = arg0.getPoint();
		transType = getTransType(pStart);
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {

		//save the end point, get the transformation type
		pEnd = arg0.getPoint();
		executeAction(transType);
		
		//save the new total transformation and initialization the currentTrans
		totalTrans = Mathematics.multiplicateMatrix(currentTrans, totalTrans);
		currentTrans = Transformation.CreateIdentityMatrix(3);
		this.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		pEnd = arg0.getPoint();		
		//if in range
		if((pStart.getX() >= margins) && (pStart.getX() <= viewWidth + margins)
				&& (pStart.getY() >= margins) && (pStart.getY() <= viewWidth + margins))
			executeAction(transType);
		this.repaint();
	}
	
	public void keyPressed(KeyEvent e) {

	    int key = e.getKeyCode();

	    if (key == KeyEvent.VK_R) {
	    	
	    	//set the value of the point to the origin one
	    	for(int i = 0; i < verticesList.length; i++) {
	    		verticesDraw[i].setX(verticesList[i].getX());
	    		verticesDraw[i].setY(verticesList[i].getY());
	    	}
	    	
	    	//reset the current matrix and the total matrix
			totalTrans = Transformation.CreateIdentityMatrix(3);
			currentTrans = Transformation.CreateIdentityMatrix(3);
	    	
			//draw
	    	this.repaint();
	    }

	    if (key == KeyEvent.VK_L) {
	        
	    }

	    if (key == KeyEvent.VK_Q) {
	    	System.exit(0);
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
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
