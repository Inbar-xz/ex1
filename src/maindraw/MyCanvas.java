//Name:Yakir Pinchas & Inbar Demuth.
//id: yakir - 203200530 inbar - 204885370.
package maindraw;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utils.Transformation;
import utils.Mathematics;
import shape.Vertex;
import shape.Edge;

public class MyCanvas extends Canvas implements MouseListener,  MouseMotionListener, KeyListener, ComponentListener{

	private static final long serialVersionUID = 1L;
	
	public static String scnFile = "ex2.scn.txt";
	public static String viwFile = "ex2.viw.txt";
	
	private String transType;
	private Point pStart, pEnd;
	private int margins = 20;
	private double centerX , centerY;
	private int viewWidth , viewHigh;
	private double[] vectorStart, vectorEnd;
	private double[][] viewMatrix, currentTrans, totalTrans;
	private Vertex verticesList[], verticesDraw[];
	private Edge edgesList[];
	private boolean cutFlag = false;
	
	/**
	 * constructor. read data from files, create the view matrix,
	 * and initialize basis parameters.
	 */
	public MyCanvas() {
		
		//read the view file to create the view matrix
		openViewFile();
		
		//initialize the current matrix and the total matrix
		totalTrans = Transformation.IdentityMatrix(3);
		currentTrans = Transformation.IdentityMatrix(3);
		
		//set the first click point and the end click point
		vectorStart = new double[2];
		vectorEnd = new double[2];
		
		//read the vertices and the edges for the file
		openScreenFile();
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * open the screen file and set the vertices and the edges according 
	 * to the data in the file.
	 */
	public void openScreenFile() {
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
	}
	
	/**
	 * open the view file and set the view matrix according to the data
	 * in the file. also set the center and the size according to that data.
	 */
	public void openViewFile() {
		
		double coordinateX = 0, coordinateY = 0;
		double direction = 0;
		double[][] trans1 , rotate1, trans2, scale1;
		double windowWidth = 0, windowHigh = 0;
		
		
		File viewFile = new File(viwFile);
		Scanner setScan;
		try {
			setScan = new Scanner(viewFile);
			setScan.next();
			coordinateX = setScan.nextDouble();
			coordinateY = setScan.nextDouble();
			setScan.next();
			direction = setScan.nextDouble();
			setScan.next();
			windowWidth = setScan.nextDouble();
			windowHigh = setScan.nextDouble();
			setScan.next();
			viewWidth = setScan.nextInt();
			viewHigh = setScan.nextInt();
			setScan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create the view matrix
		trans1 = Transformation.TranslateMatrix2D(-coordinateX, -coordinateY);
		rotate1 = Transformation.RotateMatrix2D(-1 * Math.toRadians(direction));
		scale1 = Transformation.ScaleMatrix2D(viewWidth / windowWidth, (-1) * viewHigh / windowHigh);
		trans2 = Transformation.TranslateMatrix2D(viewWidth/2 + 20, viewHigh/2 + 20);
			
		viewMatrix = Mathematics.multMatrixs(trans2, scale1);
		viewMatrix = Mathematics.multMatrixs(viewMatrix, rotate1);
		viewMatrix = Mathematics.multMatrixs(viewMatrix, trans1);
		
		
		//set the center vertex
		centerX = (viewWidth / 2) + margins;
		centerY = (viewHigh / 2) + margins;

		setSize((int)viewWidth + 40, (int)viewHigh + 40);				
	}
	
	/**
	 * this function draw the list of vertices and the edges
	 * after the translations
	 * @param g
	 * @return
	 */
	public void paint(Graphics g) {
		
		double vertexX, vertexY, vertexZ;
		double[][] transMatrix;
		
		//mult all the translations
		transMatrix = Mathematics.multMatrixs(currentTrans, totalTrans);
		transMatrix = Mathematics.multMatrixs(transMatrix, viewMatrix);
		
		//apply changes on the vertices in verticesDraw, using the origin vertices in verticesList
		int verticesNum = verticesList.length;
		double[][] vectorVertex = new double [3][1];
		for (int i = 0; i < verticesNum; i++) {
		    vectorVertex = Mathematics.multMatrixAndVertex(transMatrix, verticesList[i]);
		    vertexX = vectorVertex[0][0];
		    vertexY = vectorVertex[1][0];
		    vertexZ = vectorVertex[2][0];
		    verticesDraw[i].setX(vertexX);
		    verticesDraw[i].setY(vertexY);
		    verticesDraw[i].setZ(vertexZ);
		}
		
		//draw the new vertices and edges
		g.drawRect(margins, margins, (int)viewWidth, (int)viewHigh);
		Polygon p = new Polygon();
		int edgesNum = edgesList.length;
		Edge cutLine;
		Edge edgesListDraw[];
		int counterCut = 0;
		
		//if need to cut, create new list of edges to draw
		if (cutFlag) {
			edgesListDraw = new Edge[edgesNum];
			for (int i = 0; i < edgesNum; i++) {
				cutLine = clipLine(edgesList[i]);
				if (cutLine != null) {
					edgesListDraw[counterCut] = cutLine;
					counterCut++;
				}
			}
			edgesNum = counterCut;
		} else {
			edgesListDraw = edgesList;
		}
		
		//draw the edges
		for (int i = 0; i < edgesNum; i++) {
			p.addPoint((int) edgesListDraw[i].getV1().getX(), (int) edgesListDraw[i].getV1().getY());
			p.addPoint((int) edgesListDraw[i].getV2().getX(), (int) edgesListDraw[i].getV2().getY());	
			g.setColor(Color.BLUE);
			g.drawPolygon(p);
			p.reset();
		}
	}
	
	public Edge clipLine(Edge line) {
		
		int xMax = viewWidth - margins;
		int xMin = margins;
		int yMax = viewHigh - margins;
		int yMin = margins;
		
		//go according to the Chohen-Sutherland algo.
		Vertex vStart = line.getV1();
		Vertex vEnd = line.getV2();
		
		int vStartBit, vEndBit;
		
		//create the bit value of each point
		vStartBit = bitValue(vStart);
		vEndBit = bitValue(vEnd);
		
		//if it's not zero, then the line outside the window
		if ((vStartBit & vEndBit) != 0) {
			return null;
		}
		
		//if it's zero, then all the line in the window
		if ((vStartBit | vEndBit) == 0) {
			return line;
		}
		
		//else, calculate the intersection point
		//if vStart inside the window, swap with vEnd
		if (vStartBit == 0) {
			Vertex copy = vStart;
			vStart = vEnd;
			vEnd = copy;
			vStartBit = vEndBit;
		}
		
		double x, y;
		double tLeft, tRight, tTop, tDown;
		double[] N = new double[2];
		Vertex Q;
		double d1, d2, angle1, angle2 = 0, denominator, QTovStartLen, QTovStartAngle, nLen;
		
		//denominator calculation 
		double [] vStartTovEnd = new double[2];
		vStartTovEnd[0] = vEnd.getX() - vStart.getX();
		vStartTovEnd[1] = vEnd.getY() - vStart.getY();
		d2 = Mathematics.vectorLength(vStartTovEnd);
		
		List<Double> t = new ArrayList<Double>();
		
		//if there is intersection with the left side of the window
		if ((vStartBit & 0x8) != 0) {
			
			//use the points (xMin, yMax), (xMin, yMin) to create normal to the window side
			N[0] = yMax - yMin;
			N[1] = 0;
			
			//create the vector from Q = (xMin, yMax) to vStart
			double [] QTovStart = new double[2];
			QTovStart[0] = vStart.getX() - xMin;
			QTovStart[1] = vStart.getY() - yMax;
			
			t.add(calculateT(QTovStart, N, vStartTovEnd, d2));
			/*
			//calculate the length of the vector QTovStart and hid angle with N
			QTovStartLen = Mathematics.vectorLength(QTovStart);
			QTovStartAngle = Math.atan2(QTovStart[0],QTovStart[1]) - Math.atan2(N[0], N[1]);
			
			//calculate the denominator of the t formula
			angle2 = Math.atan2(vStartTovEnd[0],vStartTovEnd[1]) - Math.atan2(N[0], N[1]);
			denominator = -Mathematics.vectorLength(N) * d2 * Math.cos(angle2);
			
			//calculate the t and add to the list
			tLeft = (Mathematics.vectorLength(N) * QTovStartLen * Math.cos(QTovStartAngle)) / denominator;
			t.add(tLeft);*/
		}
		
		//if there is intersection with the right side of the window
		if ((vStartBit & 0x4) != 0) {
			
			//use the points (xMax, yMin), (xMax, yMax) to create normal to the window side
			//V = (xMax - xMax, yMax - yMin)
			N[0] = yMin - yMax;
			N[1] = 0;
			
			//create the vector from Q = (xMax, yMin) to vStart
			double [] QTovStart = new double[2];
			QTovStart[0] = vStart.getX() - xMax;
			QTovStart[1] = vStart.getY() - yMin;
			
			//calculate the length of the vector QTovStart and his angle with N
			QTovStartLen = Mathematics.vectorLength(QTovStart);
			QTovStartAngle = Math.atan2(QTovStart[0],QTovStart[1]) - Math.atan2(N[0], N[1]);
			
			//calculate the denominator of the t formula
			angle2 = Math.atan2(vStartTovEnd[0],vStartTovEnd[1]) - Math.atan2(N[0], N[1]);
			nLen = Mathematics.vectorLength(N);
			denominator = -nLen * d2 * Math.cos(angle2);
			
			//calculate the t and add to the list
			tRight = (nLen * QTovStartLen * Math.cos(QTovStartAngle)) / denominator;
			
			t.add(tRight);
		}
		
		//if there is intersection with the top(down) side of the window
		if ((vStartBit & 0x2) != 0) {
			
			//use the points (xMax, yMax), (xMin, yMax) to create normal to the window side
			N[0] = 0;
			N[1] = xMin - xMax;
			
			//create the vector from Q = (xMax, yMax) to vStart
			double [] QTovStart = new double[2];
			QTovStart[0] = vStart.getX() - xMax;
			QTovStart[1] = vStart.getY() - yMax;
			
			//calculate the length of the vector QTovStart and hid angle with N
			QTovStartLen = Mathematics.vectorLength(QTovStart);
			QTovStartAngle = Math.atan2(QTovStart[0],QTovStart[1]) - Math.atan2(N[0], N[1]);
			
			
			//calculate the denominator of the t formula
			angle2 = Math.atan2(vStartTovEnd[0],vStartTovEnd[1]) - Math.atan2(N[0], N[1]);
			denominator = -Mathematics.vectorLength(N) * d2 * Math.cos(angle2);
			
			//calculate the t and add to the list
			tTop = (Mathematics.vectorLength(N) * QTovStartLen * Math.cos(QTovStartAngle)) / denominator;
			t.add(tTop);
		}
		
		//if there is intersection with the down(up) side of the window
		if ((vStartBit & 0x1) != 0) {
			
			//use the points (xMax, yMin), (xMin, yMin) to create normal to the window side
			N[0] = 0;
			N[1] = xMin - xMax;
			
			//create the vector from Q = (xMax, yMin) to vStart
			double [] QTovStart = new double[2];
			QTovStart[0] = vStart.getX() - xMax;
			QTovStart[1] = vStart.getY() - yMin;
			
			//calculate the length of the vector QTovStart and hid angle with N
			QTovStartLen = Mathematics.vectorLength(QTovStart);
			QTovStartAngle = Math.atan2(QTovStart[0],QTovStart[1]) - Math.atan2(N[0], N[1]);
			
			
			//calculate the denominator of the t formula
			angle2 = Math.atan2(vStartTovEnd[0],vStartTovEnd[1]) - Math.atan2(N[0], N[1]);
			denominator = -Mathematics.vectorLength(N) * d2 * Math.cos(angle2);
			
			//calculate the t and add to the list
			tDown = (Mathematics.vectorLength(N) * QTovStartLen * Math.cos(QTovStartAngle)) / denominator;
			t.add(tDown);
		}
		
		double con = d2 * Math.cos(Math.toRadians(angle2));
		Vertex interPoint;
		
		//find the minimal t
		if (con > 0) {
			double min = t.get(0);
			for (int i = 0; i < t.size(); i++) {
				if (t.get(i) > 0 && t.get(i) < 1 && t.get(i) < min) {
					min = t.get(i);
				}
			}
			
			x = vStart.getX() + vStartTovEnd[0] * min;
			y = vStart.getY() + vStartTovEnd[1] * min;
			interPoint = new Vertex(x, y);
		//find the maximal t	
		} else {
			double max = t.get(0);
			for (int i = 0; i < t.size(); i++) {
				if (t.get(i) > 0 && t.get(i) < 1 && t.get(i) > max) {
					max = t.get(i);
				}
			}
			
			x = vStart.getX() + vStartTovEnd[0] * max;
			y = vStart.getY() + vStartTovEnd[1] * max;
			interPoint = new Vertex(x, y);
		}
		
		Edge newLine = new Edge(interPoint, vEnd);
		return clipLine(newLine);
	}
	
	private double calculateT(double [] QTovStart, double [] N, double [] vStartTovEnd, double d2) {
		
		//calculate the length of the vector QTovStart and hid angle with N
		double QTovStartLen = Mathematics.vectorLength(QTovStart);
		double QTovStartAngle = Math.atan2(QTovStart[0],QTovStart[1]) - Math.atan2(N[0], N[1]);
		
		
		//calculate the denominator of the t formula
		double angle2 = Math.atan2(vStartTovEnd[0],vStartTovEnd[1]) - Math.atan2(N[0], N[1]);
		double denominator = -Mathematics.vectorLength(N) * d2 * Math.cos(angle2);
		
		//calculate the t and add to the list
		return (Mathematics.vectorLength(N) * QTovStartLen * Math.cos(QTovStartAngle)) / denominator;
		
	}
	
	public int bitValue(Vertex v) {
		
		int xMax = viewWidth + margins;
		int xMin = margins;
		int yMax = viewHigh + margins;
		int yMin = margins;
		
		int vBit = 0;
		
		if (v.getY() < yMin) {
			vBit |= 0x1;
		}
		
		if (v.getY() > yMax) {
			vBit |= 0x2;
		}
		
		if (v.getX() > xMax) {
			vBit |= 0x4;
		}
		
		if (v.getX() < xMin) {
			vBit |= 0x8;
		}
		
		return vBit;
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
	 * execute scale transformation.
	 */
	public void executeScale() {
		double radiusPStart, radiusPEnd, scaleParameter;
		
		//calculate the radius of start point and end point according to function
		//that calculate the distance between two points
		radiusPStart = Mathematics.distance(pStart, centerX, centerY);
		radiusPEnd = Mathematics.distance(pEnd, centerX, centerY);
		
		//the parameter that send to scale matrix is radius end / radius start
		scaleParameter = radiusPEnd / radiusPStart;
		
		//Current Transformation is translate(cx,cy) * scale(s,s) * translate(-cx,-cy) 
		currentTrans = Transformation.ScaleMatrix2D(scaleParameter, scaleParameter);
		currentTrans = Mathematics.multMatrixs
			 (Mathematics.multMatrixs
					 (Transformation.TranslateMatrix2D(centerX, centerY), currentTrans)
					 , Transformation.TranslateMatrix2D(-centerX, -centerY));
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
		double angleStart = Mathematics.angleWithXAxis(vectorStart);
		double angleEnd = Mathematics.angleWithXAxis(vectorEnd);
		double angleFinish = angleStart - angleEnd;
		
		//Current Transformation is translate(cx,cy) * rotate(angle) * translate(-cx,-cy) 
		currentTrans = Transformation.RotateMatrix2D(Math.toRadians(angleFinish));
		currentTrans = Mathematics.multMatrixs
				 (Mathematics.multMatrixs
						 (Transformation.TranslateMatrix2D(centerX, centerY), currentTrans)
						 , Transformation.TranslateMatrix2D(-centerX, -centerY));
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
		 case "T":  currentTrans = Transformation.TranslateMatrix2D(pEnd.getX() - pStart.getX(),
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
		totalTrans = Mathematics.multMatrixs(currentTrans, totalTrans);
		currentTrans = Transformation.IdentityMatrix(3);
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
			totalTrans = Transformation.IdentityMatrix(3);
			currentTrans = Transformation.IdentityMatrix(3);
	    	
			//draw
	    	this.repaint();
	    } else if (key == KeyEvent.VK_L) {
	    	
	    	//get new file from user
	    	FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
	        dialog.setMode(FileDialog.LOAD);
	        dialog.setVisible(true);
	        String newFile = dialog.getDirectory() + dialog.getFile();
	        
	        //use the file according to his type
	        if(newFile.toLowerCase().endsWith("scn.txt")) {
	        	scnFile = newFile;
	        	openScreenFile();
	        	
	        	//initialize the current matrix and the total matrix
	    		totalTrans = Transformation.IdentityMatrix(3);
	    		currentTrans = Transformation.IdentityMatrix(3);
	    		
	        	this.repaint();
	        } else if(newFile.toLowerCase().endsWith("viw.txt")) {
	        	viwFile = newFile;
	        	openViewFile();
	        	
	        	//initialize the current matrix and the total matrix
	    		totalTrans = Transformation.IdentityMatrix(3);
	    		currentTrans = Transformation.IdentityMatrix(3);
	    		
	    		this.repaint();
	        }
	    } else if (key == KeyEvent.VK_Q) {
	    	System.exit(0);
	    } else if (key == KeyEvent.VK_C) {
	    	cutFlag = !cutFlag;
	    	this.repaint();
	    }
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
