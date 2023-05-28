package CurveSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;


public class PunkteHandler extends ArrayList <Punkt>{

	//Markierter Punkt und Hitbox -> gibt an, wie weit man sich um den Punkt befinden darf, als dass er noch erkannt wird
	private int punktMarkiert_ = -1;
	private float hitBox_ = 25;
	
	//Konstruktor
	public PunkteHandler() 
	{
		super();
	}
	
	//Punkt verschieben, x und y werden Mousepositionen sein
	public void verschieben(int x, int y)
	{
		//markierten Punkt auswählen
		Punkt punkt = get(punktMarkiert_);
		punkt.setX(x);
		punkt.setY(y);
	}
	
	//x und y werden Mousepositionen sein
	public void pruefeMarkiert(float x, float y)
	{
		punktMarkiert_ = -1;
		for (int i= 0; i< size(); ++i)
		{
			Punkt punkt = get(i);
			float abstand = punkt.quadratAbstand(x, y);
			if ( abstand < hitBox_)
			{
				punktMarkiert_ = i;
			}
		}
	}
	
	//Ist irgendein Punkt markiert?
	public boolean isMarkiert()
	{
		if (punktMarkiert_ > -1)
		{
			return true;
		}
		return false;
	}
	
	
	//Zeichne alle Punkte in der Liste
 	public void draw(Graphics g, Color color) 
	{
		//genau wie for-each bei Vector - Class in C++ (-> SFML)
		for(Punkt punkt: this)
		{
			punkt.draw(g, color);
		}
	}
	
	//Highlighte markierte Punkte
	public void hoverPunkt(Graphics g)
	{
		if (punktMarkiert_ > -1)
		{
			get(punktMarkiert_).draw(g, Color.RED);
		}
	}

	//Zeichne Verbindungsliniern zwischen Punkten
	public void zeichnePolygon(Graphics g, Color color)
	{
		if (size()> 1)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(color);
			//Startpunkt
			Punkt startPunkt = get(0);
			int xStart = (int)startPunkt.getX();
			int yStart = (int)startPunkt.getY();
			//Durchlaufe alle Punkte und zeichne Linie von Punkt zum nächsten
			for (int i = 1; i < size(); ++i)
			{
				Punkt naechsterPunkt = get(i);
				int xNaechster = (int)naechsterPunkt.getX();
				int yNaechster = (int)naechsterPunkt.getY();
				Line2D.Float line = new Line2D.Float(xStart, yStart, xNaechster, yNaechster);
				g2d.draw(line);
				xStart = xNaechster;
				yStart = yNaechster;
			}
		}
	}

	//Punkt hinzufügen
	public void hinzufügen(float x, float y)
	{
		add(new Punkt(x, y));
		//Damit neuer Punkt direkt markiert
		punktMarkiert_ = size()-1;
	}

	//Punkt löschen
	public void entfernen()
	{
		remove(punktMarkiert_);
		punktMarkiert_ = -1;
	}

	public void loescheAlles() {
		this.clear();
	}
}
