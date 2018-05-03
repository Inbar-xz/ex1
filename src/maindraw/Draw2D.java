//Name:Yakir Pinchas & Inbar Demuth.
//id: yakir - 203200530 inbar - 204885370.
package maindraw;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Draw2D {

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
