package example;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Draw2D {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Frame myFrame = new Frame("Exercise1");
		MyCanvas myCanvas = new MyCanvas();
		myFrame.add(myCanvas);

		WindowAdapter myWindowAdapter = new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		
		myFrame.addWindowListener(myWindowAdapter);
		myFrame.pack();
		myFrame.setVisible(true);
	}

}

class MyCanvas extends Canvas implements MouseListener,  MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyCanvas() {
		setSize(600, 480);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	Point pStart, pEnd;
	boolean bFlag = false;
	
	public void paint(Graphics g) {
		Polygon	p = new Polygon();
		int x[] = {100, 200, 200, 100};
		int y[] = {100, 100, 200, 200};
		int x1[] = {300, 400, 400, 300};
		int y1[] = {300, 300, 400, 400};

		
		for (int i=0; i<x.length; i++) {
			p.addPoint(x1[i],y1[i]);
		}
		
		g.setColor(Color.blue);
		
		g.drawPolyline(x, y, x.length);
		g.drawPolygon(x1, y, x1.length);
		g.drawPolygon(p);
		g.drawLine(20,30,100,100);
		g.drawRect(100, 300, 100, 100);
		
		if ( bFlag )
			g.drawLine(pStart.x,pStart.y,pEnd.x,pEnd.y);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		pStart = arg0.getPoint();
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		pEnd = arg0.getPoint();
		bFlag = true;
		this.repaint();
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		pEnd = arg0.getPoint();
		bFlag = true;
		this.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
