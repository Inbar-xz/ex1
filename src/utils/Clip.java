package utils;

import java.util.ArrayList;
import java.util.List;

import shape.Edge;
import shape.Vertex;

public class Clip {
	private int xMax;
	private int xMin;
	private int yMax;
	private int yMin;
	
	public Clip(int viewWidth, int viewHigh, int margins) {
		this.xMax = viewWidth + margins;
		this.xMin = margins;
		this.yMax = viewHigh + margins;
		this.yMin = margins;
	}
	
	public void setWindowSize(int viewWidth, int viewHigh, int margins) {
		this.xMax = viewWidth + margins;
		this.xMin = margins;
		this.yMax = viewHigh + margins;
		this.yMin = margins;
	}
	
	public Edge clipLine(Edge line) {
		
		//go according to the Chohen-Sutherland algo.
		Vertex vStart = line.getV1();
		Vertex vEnd = line.getV2();
		
		//create the bit value of each point
		int vStartBit = bitValue(vStart);
		int vEndBit = bitValue(vEnd);
		
		//if it's not zero, then the line outside the window
		if ((vStartBit & vEndBit) != 0) {
			return null;
		}
		
		//if it's zero, then all the line in the window
		if ((vStartBit | vEndBit) == 0) {
			return line;
		}
		
		//else, calculate the intersection point with Cyrus Beck algo
		//if vStart inside the window, swap with vEnd
		if (vStartBit == 0) {
			Vertex copy = vStart;
			vStart = vEnd;
			vEnd = copy;
			vStartBit = vEndBit;
		}
		
		//param of the denominator calculation 
		double [] vStartTovEnd = new double[2];
		vStartTovEnd[0] = vEnd.getX() - vStart.getX();
		vStartTovEnd[1] = vEnd.getY() - vStart.getY();
		double startToEndLen = Mathematics.vectorLength(vStartTovEnd);
		
		List<Double> validT = new ArrayList<Double>();
		double[] N = new double[2];
		
		//if there is intersection with the down(up in the screen) side of the window
		if ((vStartBit & 0x1) != 0) {
					
			//use the points (xMax, yMin), (xMin, yMin) to create normal to the window side
			N[0] = 0;
			N[1] = xMin - xMax;
		
			//create the vector from Q = (xMax, yMin) to vStart
			double [] QTovStart = new double[2];
			QTovStart[0] = vStart.getX() - xMax;
			QTovStart[1] = vStart.getY() - yMin;
					
			calculateT(QTovStart, N, vStartTovEnd, startToEndLen, validT);
		}
		
		//if there is intersection with the top(down in the screen) side of the window
		if ((vStartBit & 0x2) != 0) {
			
			//use the points (xMax, yMax), (xMin, yMax) to create normal to the window side
			N[0] = 0;
			N[1] = xMin - xMax;
			
			//create the vector from Q = (xMax, yMax) to vStart
			double [] QTovStart = new double[2];
			QTovStart[0] = vStart.getX() - xMax;
			QTovStart[1] = vStart.getY() - yMax;
			
			calculateT(QTovStart, N, vStartTovEnd, startToEndLen, validT);
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
			
			calculateT(QTovStart, N, vStartTovEnd, startToEndLen, validT);
		}
		
		//if there is intersection with the left side of the window
		if ((vStartBit & 0x8) != 0) {
					
			//use the points (xMin, yMax), (xMin, yMin) to create normal to the window side
			N[0] = yMax - yMin;
			N[1] = 0;
					
			//create the vector from Q = (xMin, yMax) to vStart
			double [] QTovStart = new double[2];
			QTovStart[0] = vStart.getX() - xMin;
			QTovStart[1] = vStart.getY() - yMax;
			
			calculateT(QTovStart, N, vStartTovEnd, startToEndLen, validT);
		}
		
		//in case no valid t was found
		if (validT.isEmpty()) {
			return null;
		}
		
		//find the intersection point :D
		double EdgeToNAngle = Math.atan2(vStartTovEnd[0],vStartTovEnd[1]) - Math.atan2(N[0], N[1]);
		double vecDirection = startToEndLen * Math.cos(Math.toRadians(EdgeToNAngle));
		double x, y;
		Vertex interPoint;
		
		//find the minimal t
		if (vecDirection > 0) {
			double min = validT.get(0);
			for (int i = 0; i < validT.size(); i++) {
				if (validT.get(i) < min) {
					min = validT.get(i);
				}
			}
			
			x = Math.ceil(vStart.getX() + vStartTovEnd[0] * min);
			y = Math.ceil(vStart.getY() + vStartTovEnd[1] * min);
			interPoint = new Vertex(x, y);
		//find the maximal t	
		} else {
			double max = validT.get(0);
			for (int i = 0; i < validT.size(); i++) {
				if (validT.get(i) > max) {
					max = validT.get(i);
				}
			}
			
			x = Math.ceil(vStart.getX() + vStartTovEnd[0] * max);
			y = Math.ceil(vStart.getY() + vStartTovEnd[1] * max);
			interPoint = new Vertex(x, y);
		}
		
		Edge newLine = new Edge(interPoint, vEnd);
		return clipLine(newLine);
	}
	
	private void calculateT(double [] QTovStart, double [] N, double [] vStartTovEnd, double startToEndLen, List<Double> t) {
		
		//calculate the length of the vector QTovStart and his angle with N
		double QTovStartLen = Mathematics.vectorLength(QTovStart);
		double QTovStartAngle = Math.atan2(QTovStart[0]*N[1] - QTovStart[1]*N[0], QTovStart[0]*N[0] + QTovStart[1]*N[1]);
		
		//calculate the denominator of the t formula
		double EdgeToNAngle = Math.atan2( vStartTovEnd[0]*N[1] -vStartTovEnd[1]*N[0], vStartTovEnd[0]*N[0] + vStartTovEnd[1]*N[1]);
		double denominator = -Mathematics.vectorLength(N) * startToEndLen * Math.cos(EdgeToNAngle);
		
		//calculate the t and add to the list
		double newT = (Mathematics.vectorLength(N) * QTovStartLen * Math.cos(QTovStartAngle)) / denominator;
		
		//add to list if it's valid
		if (newT < 1 && newT > 0) {
			t.add(newT);
		}
	}
	
	private int bitValue(Vertex v) {
		
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

}
