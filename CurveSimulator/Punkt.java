package CurveSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Punkt {
	
	//Attribute
	private float x_ = 0; 
	private float y_ = 0;
	private float radius_ = 7;
	
	//Konstruktor
	public Punkt(float x, float y)
	{
		x_ = x;
		y_ = y;
	}
	
	//Setter und Getter
	public void setX(float x)
	{
		x_ = x;
	}
	public void setY(float y)
	{
		y_ = y;
	}
	public float getX()
	{
		return x_;
	}
	public float getY()
	{
		return y_;
	}

	//Quadratischer Abstand
	public float quadratAbstand(float x, float y)
	{
		return (x_ - x)*(x_ - x)+(y_ - y)*(y_ - y);
	}
	
	//Draw
	public void draw(Graphics g, Color color)
	{
		Graphics2D g2d = (Graphics2D) g;
		Ellipse2D.Float punkt = new Ellipse2D.Float(x_-radius_/2, y_-radius_/2, radius_, radius_);
		g2d.setColor(color);
		g2d.fill(punkt);
		g2d.draw(punkt);
	}
}


