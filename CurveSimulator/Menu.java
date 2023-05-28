package CurveSimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;

public class Menu extends JMenuBar implements ActionListener
{
	
	//Menüs
	JMenu mDatei = new JMenu("Datei");
	JMenu mKurvenAuswahl = new JMenu("Kurvenauswahl");
	JMenu mOptionen = new JMenu("Optionen");
	
	//Datei
	JMenuItem miExit = new JMenuItem("Beenden");
	
	//Kurvenauswahl
	JCheckBox cbHermite = new JCheckBox("Hermitekurve");
	JCheckBox cbNatürlich = new JCheckBox("natürlicher Angang, parab Ende");
	JCheckBox cbGeschlossen = new JCheckBox("geschlossene Kurve");
	JCheckBox cbBezier = new JCheckBox("Bezier-Kurve");
	
	//Optionen
	JCheckBox cbTangente = new JCheckBox("Tangenten anzeigen");
	JCheckBox cbCasteljau = new JCheckBox("Casteljau anzeigen");
	JCheckBox cbPolygon = new JCheckBox("Polygon anzeigen");
	JSlider sCasteljau = new JSlider(0, 100, 0);
	JMenuItem miLoeschen = new JMenuItem("Alles löschen");

	
	Zeichenfläche zeichenflaeche;
	
	public Menu(Zeichenfläche zF)
	{
		zeichenflaeche = zF;
		
		mDatei.add(miExit);
		
		cbHermite.setSelected(zF.isHermite);
		cbNatürlich.setSelected(zF.isNatürlich);
		cbGeschlossen.setSelected(zF.isGeschlossen);
		cbBezier.setSelected(zF.isBezier);
		
		cbTangente.setSelected(zF.isTangente);
		cbPolygon.setSelected(zF.isPolygon);
		cbCasteljau.setSelected(zF.isCasteljau);
		
		
		cbHermite.addActionListener(this);
		cbNatürlich.addActionListener(this);
		cbGeschlossen.addActionListener(this);
		cbBezier.addActionListener(this);
		
		cbTangente.addActionListener(this);
		cbPolygon.addActionListener(this);
		cbCasteljau.addActionListener(this);
		
		miLoeschen.addActionListener(new ActionListener() 
		{
	         public void actionPerformed(ActionEvent e) 
	         {
	            zF.punkte.loescheAlles();
	            zF.repaint();
	         }

	      });;
	    
	    miExit.addActionListener(new ActionListener() 
	    {
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		System.exit(0);
	    	}
	    });
	    
	   sCasteljau.addChangeListener(evt -> {
			JSlider slider = (JSlider) evt.getSource();
			firePropertyChange("casteljau", -1, slider.getValue());
		});
		
		mKurvenAuswahl.add(cbHermite);
		mKurvenAuswahl.add(cbNatürlich);
		mKurvenAuswahl.add(cbGeschlossen);
		mKurvenAuswahl.add(cbBezier);
		
		mOptionen.add(cbTangente);
		mOptionen.add(cbPolygon);
		mOptionen.add(cbCasteljau);
		mOptionen.add(sCasteljau);
		mOptionen.add(miLoeschen);
		
		add(mDatei);
		add(mKurvenAuswahl);
		add(mOptionen);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{

		JCheckBox cb = (JCheckBox) (e.getSource());
		if (cb == cbHermite) 
		{
			zeichenflaeche.isHermite = ((JCheckBox) e.getSource()).isSelected();
		} 
		else if (cb == cbNatürlich) 
		{
			zeichenflaeche.isNatürlich = ((JCheckBox) e.getSource()).isSelected();
		} 
		else if (cb == cbGeschlossen) 
		{
			zeichenflaeche.isGeschlossen = ((JCheckBox) e.getSource()).isSelected();
		} 
		else if (cb == cbBezier) 
		{
			zeichenflaeche.isBezier = ((JCheckBox) e.getSource()).isSelected();
		} 
		else if (cb == cbTangente) 
		{
			zeichenflaeche.isTangente = ((JCheckBox) e.getSource()).isSelected();
		} 
		else if (cb == cbPolygon) 
		{
			zeichenflaeche.isPolygon = ((JCheckBox) e.getSource()).isSelected();
		} 
		else if (cb == cbCasteljau) 
		{
			zeichenflaeche.isCasteljau = ((JCheckBox) e.getSource()).isSelected();
		} 
		zeichenflaeche.repaint();
		
	}

}
