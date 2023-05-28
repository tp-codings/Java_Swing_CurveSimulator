package CurveSimulator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Matrix;

public class Zeichenfläche extends JPanel implements MouseMotionListener, MouseListener, PropertyChangeListener
{
	
	private static final int SEGMENTS = 100;
	//PunkteHandler verwaltet die Punkteliste
	public PunkteHandler punkte = new PunkteHandler();
	
	private boolean dragged_ = false;
	private boolean verändertH = false;
	private boolean verändertN = false;
	private boolean verändertG = false;
	//Welche Checkbox aktiviert -> in Menu
	public boolean isHermite = false, isNatürlich = false, isGeschlossen = false, isBezier = false, isTangente = false, isPolygon = true, isCasteljau = false;
	
	float castelPos = 0;
	
	JLabel punktCounter;
	//damit nicht immer neu berechnet (geht bestimmt eleganter)
	float[][] a_inverse_hermite;
	float[][] a_inverse_nat;
	float[][] a_inverse_geschl;
	
	
	//Konstruktor
	public Zeichenfläche()
	{
		//Listener
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//Festlegen der Größe des Panels
		Dimension d = new Dimension(800, 800);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		setBackground(Color.WHITE);
		
		punktCounter = new JLabel("Anzahl Punkte: " + 0);
		add(punktCounter, BorderLayout.NORTH);
	}
	

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		punktCounter.setText("Anzahl Punkte: " + punkte.size());
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(2));
		
		
		
		if(isPolygon)
		{
			punkte.zeichnePolygon(g, Color.GRAY);
		}
		
		//zeichne jeweilige Auswahl
		if (isHermite)
		{
			HermiteKurve(g);
		}
		if (isNatürlich)
		{
			NatAnfParabEnd(g);
		}
		if(isGeschlossen)
		{
			GeschlosseneKurve(g);
		}
		if(isBezier && punkte.size()>0)
		{
			BezierKurve(g);
		}
		if(isCasteljau && punkte.size()>0)
		{
			Casteljau(g);
		}
		punkte.draw(g, Color.BLACK);
		punkte.hoverPunkt(g);
	}
	
	//Zeichnet Bezierkurve
	private void BezierKurve(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D)g;
		Path2D.Float curve = new Path2D.Float();
		g2d.setColor(Color.RED);
		curve.moveTo(punkte.get(0).getX(), punkte.get(0).getY());
		
		float p_x_[] = new float[punkte.size()]; 
		float p_y_[] = new float[punkte.size()]; 
		
		for(int i = 0; i<punkte.size(); ++i)
		{
			p_x_[i] = punkte.get(i).getX();
			p_y_[i] = punkte.get(i).getY();
		}
				
		for(float t = 0.0F; t<= 1.0F; t+= 1.0F/SEGMENTS)
		{
			float[] point = deCasteljauAlgorithmus(p_x_, p_y_, t);
			curve.lineTo(point[0], point[1]);
		}
		g2d.draw(curve);
	}

	//Berechnet Punkte der Bezierkurve nach deCasteljau-Algorithmus (rekursiv)
	private float[] deCasteljauAlgorithmus(float[] p_x, float[] p_y, float t) {
		if(p_x.length == 1)
		{
			return new float[] {p_x[0], p_y[0]};
		}
		
		float[] new_x = new float[p_x.length-1];
		float[] new_y = new float[p_y.length-1];
		
		for(int i= 0; i< p_x.length-1; ++i)
		{
			new_x[i] = p_x[i] + t * (p_x[i+1] -  p_x[i]);
			new_y[i] = p_y[i] + t * (p_y[i+1] -  p_y[i]);
		}
		return deCasteljauAlgorithmus(new_x, new_y, t);
	}

	//Zeichnet in Abhängigkeit des zugehörigen Sliders aus Menu Optionen den Casteljau-Algorithmus
	private void Casteljau(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D)g;
		PunkteHandler layerPH[] = new PunkteHandler[punkte.size()];	
		//Erstes Layer sind die Punkte selbst
		layerPH[0] = (PunkteHandler)punkte.clone();
		
		for (int i = 1; i< punkte.size(); i++)	//Anzahl der Layer
		{
			layerPH[i] = new PunkteHandler();
			//Berechne einzelne Punkte in jeweiligen Layer (A+t*AB) in Bezug auf den vorherigen
			for(int j = 0; j<layerPH[i-1].size()-1; j++)
			{
				layerPH[i].hinzufügen(layerPH[i-1].get(j).getX()+castelPos/100*(layerPH[i-1].get(j+1).getX()-layerPH[i-1].get(j).getX()),
						layerPH[i-1].get(j).getY()+castelPos/100*(layerPH[i-1].get(j+1).getY()-layerPH[i-1].get(j).getY()));
			}
			g2d.setStroke(new BasicStroke(1));
			layerPH[i].zeichnePolygon(g, Color.LIGHT_GRAY);
			g2d.setStroke(new BasicStroke(2));
			if(i < punkte.size()-1)
			{
				layerPH[i].draw(g, Color.GREEN);
			}
			else
			{
				layerPH[i].draw(g, Color.RED);
			}
				
			
		}

	}
	

	private void HermiteKurve(Graphics g) 
	{
		//mindestens 4 Punkte nötig, um zu zeichnen
		if (punkte.size() > 3) 
		{
			int punkteLänge = punkte.size();
			//P-Vektor gesplittet in x und y
			float[] p_x_, p_y_;
			//Dimension des P-Vektors ist immer so groß, wie es Anzahl an Punkten gibt + Anzahl der Randbedingungen (festgelegte Tangenten -> hier Anfang und Ende, also plus 2)
			/*
			 * wenn Anzahl Punkte = 4
			 * a: 4x4
			 * b: 4x6
			 * P: 6
			 * P': 4
			 */
			p_x_ = new float[punkteLänge];
			p_y_ = new float[punkteLänge];
			float[][] a = new float[punkteLänge - 2][punkteLänge - 2];
			float[][] b = new float[punkteLänge - 2][punkteLänge];
			
			//nachfolgePunkt für Steigung berechnen (Anfang und Ende)
			Punkt punkt, nachfolgePunkt;
			
			// Berechnung der Steigung im ersten und letztem Punkt (über Steigungsdreieck) dy1-dy0 und dx1-dx0
			nachfolgePunkt = punkte.get(1);
			punkt = punkte.get(0);
			p_x_[0] = nachfolgePunkt.getX() - punkt.getX();
			p_y_[0] = nachfolgePunkt.getY() - punkt.getY();
			
			punkt = punkte.get(punkteLänge - 1);
			nachfolgePunkt = punkte.get(punkteLänge - 2);
			p_x_[punkteLänge - 1] = punkt.getX() - nachfolgePunkt.getX();
			p_y_[punkteLänge - 1] = punkt.getY() - nachfolgePunkt.getY();
			
			// Lege a und b fest -> alle Punkte zwischen Anfang und Ende sollen berücksichtigt werden
			for (int i = 1; i < punkteLänge - 1; ++i) 
			{
				punkt = punkte.get(i);
				p_x_[i] = (float) punkt.getX();
				p_y_[i] = punkt.getY();
				
				//Middle Points -> 4 wandert auf der Hauptdiagonalen immer um eins nach rechts (drumherum 1)
				//punkteLänge > Dimension der Matrizen
				if (i < punkteLänge - 3) 
				{
					a[i][i - 1] = 1;
					a[i][i] = 4;
					a[i][i + 1] = 1;
					
					b[i][i] = -3;
					b[i][i + 2] = 3;
				}
			}
			
			// Da die Steigung im ersten und letztem Punkt vorgegeben wird, müssen jeweils erste und letzte Zeile der Matrix angepasst werden: 
			a[0][0] = 1;
			a[punkteLänge - 3][punkteLänge - 3] = 1;
			
			b[0][0] = 1;
			b[punkteLänge - 3][punkteLänge - 1] = 1;
			
			
			//A^-1*B -> damit nur invertiert wird, wenn neuer Punkt hinzugefügt / gelöscht wird
			if(verändertH)
			{
				System.out.println("Hermite Invers");
				a_inverse_hermite = Matrix.invertiereMatrix(a);
				verändertH = false;
			}
			
			float[][] a_inverse_b = Matrix.matMult(a_inverse_hermite, b);
			
			//P' = A^-1*B*P
			float[] pStrichx = Matrix.matMult(a_inverse_b, p_x_);
			float[] pStrichy = Matrix.matMult(a_inverse_b, p_y_);
			
			//Zeichne alle Teilkurven
			for (int i = 0; i < punkteLänge - 3; ++i) 
			{
				zeichneHermiteKurve(g, p_x_[i + 1], p_y_[i + 1], p_x_[i + 2], p_y_[i + 2],pStrichx[i], pStrichy[i], pStrichx[i + 1], pStrichy[i + 1], Color.BLUE);
			}
		}
	}
	

	private void NatAnfParabEnd(Graphics g) 
	{
		//mindestens 4 Punkte nötig, um zu zeichnen
		if (punkte.size() > 3) 
		{
			int punkteLänge = punkte.size();
			//P-Vektor gesplittet in x und y
			float[] p_x_, p_y_;
			//Dimension des P-Vektors ist immer so groß, wie es Anzahl an Punkten gibt + Anzahl der Randbedingungen (festgelegte Tangenten -> hier keine, also plus 0)
			/*
			 * wenn Anzahl Punkte = 4
			 * a: 4x4
			 * b: 4x4
			 * P: 4
			 * P': 4
			 */
			p_x_ = new float[punkteLänge];
			p_y_ = new float[punkteLänge];
			float[][] a = new float[punkteLänge][punkteLänge];
			float[][] b = new float[punkteLänge][punkteLänge];
			
			
			//erster und letzter Punkt
			Punkt punkt;
			punkt= punkte.get(0);
			p_x_[0] = punkt.getX();
			p_y_[0] = punkt.getY();
			
			punkt= punkte.get(punkteLänge - 1);
			p_x_[punkteLänge - 1] = punkt.getX();
			p_y_[punkteLänge - 1] = punkt.getY();
			
			
			// Lege a und b fest -> alle Punkte zwischen Anfang und Ende sollen berücksichtigt werden
			for (int i = 1; i < punkteLänge - 1; ++i) 
			{
				//Middle Points -> 4 wandert auf der Hauptdiagonalen immer um eins nach rechts (drumherum 1)
				punkt= punkte.get(i);
				p_x_[i] = (float) punkt.getX();
				p_y_[i] = punkt.getY();
				//punkteLänge = Dimension der Matrizen
				a[i][i - 1] = 1;
				a[i][i] = 4;
				a[i][i + 1] = 1;
				b[i][i - 1] = -3;
				b[i][i + 1] = 3;
			}
			
			//Funktion für natürlichen Anfang
			a[0][0] = 2;
			a[0][1] = 1;
			
			b[0][0] = -3;
			b[0][1] = 3;
			//Funktion für parabolisches Ende
			a[punkteLänge - 1][punkteLänge - 2] = 1;
			a[punkteLänge - 1][punkteLänge - 1] = 1;
			
			b[punkteLänge - 1][punkteLänge - 2] = -2;
			b[punkteLänge - 1][punkteLänge - 1] = 2;

			//damit nur invertiert wird, wenn neuer Punkt hinzugefügt / gelöscht wird
			if(verändertN)
			{
				System.out.println("Natürlich Invers");
				a_inverse_nat = Matrix.invertiereMatrix(a);
				verändertN = false;
			}
			float[][] a_inverse_b = Matrix.matMult(a_inverse_nat, b);
			
			//P' = A^-1*B*P
			float[] pStrichx = Matrix.matMult(a_inverse_b, p_x_);
			float[] pStrichy = Matrix.matMult(a_inverse_b, p_y_);
			
			//Zeichne alle Teilkurven
			for (int i = 0; i < punkteLänge - 1; ++i) {
				zeichneHermiteKurve(g, p_x_[i], p_y_[i], p_x_[i + 1], p_y_[i + 1],pStrichx[i], pStrichy[i], pStrichx[i + 1], pStrichy[i + 1], Color.ORANGE);
			}
		}
	}

	
	
	private void GeschlosseneKurve(Graphics g) 
	{
		//mindestens 3 Punkte nötig, um zu zeichnen
		if (punkte.size() > 2) 
		{
			int punkteLänge = punkte.size();
			//P-Vektor gesplittet in x und y
			float[] p_x_, p_y_;
			
			//Dimension des P-Vektors ist immer so groß, wie es Anzahl an Punkten gibt + Anzahl der Randbedingungen (festgelegte Tangenten -> hier keine, also plus 0)
			/*
			 * wenn Anzahl Punkte = 4
			 * a: 4x4
			 * b: 4x4
			 * P: 4
			 * P': 4
			 */
			p_x_ = new float[punkteLänge];
			p_y_ = new float[punkteLänge];
			float[][] a = new float[punkteLänge][punkteLänge];
			float[][] b = new float[punkteLänge][punkteLänge];
			
			//erster und letzter Punkt
			Punkt punkt= punkte.get(0);
			p_x_[0] = punkt.getX();
			p_y_[0] = punkt.getY();
			
			punkt= punkte.get(punkteLänge - 1);
			p_x_[punkteLänge - 1] = punkt.getX();
			p_y_[punkteLänge - 1] = punkt.getY();
			
			// Lege a und b fest -> alle Punkte zwischen Anfang und Ende sollen berücksichtigt werden
			for (int i = 1; i < punkteLänge - 1; ++i) 
			{
				//Middle Points -> 4 wandert auf der Hauptdiagonalen immer um eins nach rechts (drumherum 1)
				punkt= punkte.get(i);
				p_x_[i] = (float) punkt.getX();
				p_y_[i] = punkt.getY();
				
				//punkteLänge = Dimension der Matrizen
				a[i][i - 1] = 1;
				a[i][i] = 4;
				a[i][i + 1] = 1;
				b[i][i - 1] = -3;
				b[i][i + 1] = 3;
			}
			
			//Erster und letzter Punkt soll über Mittelpunktformel abgebildet werden
			a[0][0] = 4;
			a[0][1] = 1;
			a[0][punkteLänge - 1] = 1;
			a[punkteLänge - 1][0] = 1;
			a[punkteLänge - 1][punkteLänge - 2] = 1;
			a[punkteLänge - 1][punkteLänge - 1] = 4;
			
			b[0][punkteLänge - 1] = -3;
			b[0][1] = 3;
			b[punkteLänge - 1][punkteLänge - 2] = -3;
			b[punkteLänge - 1][0] = 3;

			//damit nur invertiert wird, wenn neuer Punkt hinzugefügt / gelöscht wird
			if(verändertG)
			{
				System.out.println("Geschlossen Invers");
				a_inverse_geschl = Matrix.invertiereMatrix(a);
				verändertG = false;
			}
			float[][] a_inverse_b = Matrix.matMult(a_inverse_geschl, b);
			
			//P' = A^-1*B*P
			float[] pStrichx = Matrix.matMult(a_inverse_b, p_x_);
			float[] pStrichy = Matrix.matMult(a_inverse_b, p_y_);
			
			for (int i = 0; i < punkteLänge; ++i) {
				int ip1 = 0;
				if(i < punkteLänge -1)
				{
					ip1 = i+1;
				}
				zeichneHermiteKurve(g, p_x_[i], p_y_[i], p_x_[ip1], p_y_[ip1], pStrichx[i], pStrichy[i], pStrichx[ip1], pStrichy[ip1], Color.GREEN);
			}
		}
	}
	
	
	
	//Hermitekurve zeichnen 
	private void zeichneHermiteKurve(Graphics g,float p_x_0, float p_y_0, float p_x_1, float p_y_1,float pS_x_0, float pS_y_0, float pS_x_1, float pS_y_1, Color color) 
	{
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		Path2D.Float curve = new Path2D.Float();
		
		//Lege Startpunkt fest
		curve.moveTo(p_x_0, p_y_0);
		
		for (float t = 0.0F; t <= 1.0F; t += 1.0F / SEGMENTS) {
			
			//t^2 und t^3
			float t2 = t*t;
			float t3 = t2 * t;
			
			//Bindefunktionen
			float h1 = 2*t3 - 3* t2 +1;
			float h2 = -2 * t3 + 3 * t2;
			float h3 = t3 - 2 * t2 +t;
			float h4 = t3 - t2;
			
			//h1 * P0 + h2 * P1 + h3 * P'0 + h4 * P'4
			float x = (int)(h1 * p_x_0 + h2 * p_x_1 + h3 * pS_x_0 + h4 * pS_x_1);
			float y = (int)(h1 * p_y_0 + h2 * p_y_1 + h3 * pS_y_0 + h4 * pS_y_1);
			
			curve.lineTo(x, y);
		}
		g2d.draw(curve);
		
		// Tangente am Anfang und Ende zeichnen
		if (isTangente) {
			g2d.setColor(color);
			g2d.setStroke(new BasicStroke(1));
			Line2D.Float tAnf = new Line2D.Float((int)p_x_0, (int)p_y_0, (int)(p_x_0 - pS_x_0), (int)(p_y_0 - pS_y_0));
			Line2D.Float tEnd = new Line2D.Float((int)p_x_1, (int)p_y_1, (int)(p_x_1 + pS_x_1), (int)(p_y_1 + pS_y_1));
			g2d.draw(tAnf);
			g2d.draw(tEnd);
			g2d.setStroke(new BasicStroke(2));
		}
	}
	
	
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		//Damit beim Verschieben keine Punkte erzeugt werden
		if (!dragged_) 
		{
			
			if (!punkte.isMarkiert())
			{
				if (e.getButton() == MouseEvent.BUTTON1) 
				{
					punkte.hinzufügen(e.getX(), e.getY());
					verändertH = true;
					verändertN = true;
					verändertG = true;
				} 
			} 
			else if (e.getButton() == MouseEvent.BUTTON3) 
			{
				punkte.entfernen();
				verändertH = true;
				verändertN = true;
				verändertG = true;
			}
		}  
		else 
		{
			dragged_ = false;
		}
		this.repaint();
		
	}
	
	//
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		
		dragged_ = true;
		if(punkte.isMarkiert())
		{
			punkte.verschieben(e.getX(), e.getY());
		}
		repaint();
	}

	@Override
	//Wenn sich die Maus bewegt soll geprüft werden, ob sie sich über einem Punkt befindet
	public void mouseMoved(MouseEvent e) 
	{
		dragged_ = false;
		punkte.pruefeMarkiert(e.getX(), e.getY());
		this.repaint();
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("casteljau"))
			castelPos = ((int)evt.getNewValue());
		repaint();
	}
}
