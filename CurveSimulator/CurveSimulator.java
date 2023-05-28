package CurveSimulator;
import javax.swing.JFrame;


public class CurveSimulator
{

	public static void main(String[] args) {
		JFrame frame = new JFrame("Testat 2");
		Zeichenfläche zF = new Zeichenfläche();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocation(100, 50);
		frame.setSize(1000, 1000);
		
		Menu menuBar = new Menu(zF);
		
		frame.setJMenuBar(menuBar);
		frame.add(zF);
		
		//Zeichenfläche soll menuBar zuhören
		menuBar.addPropertyChangeListener(zF);
		
		frame.pack();
		frame.setVisible(true);

	}


}
