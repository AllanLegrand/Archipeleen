package controleur;

import metier.Metier;
import metier.Edge;
import metier.Node;


import ihm.FrameGraph;
import ihm.PanelGraph;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Controleur
{
    private Metier     metier;
	private FrameGraph ihm;

	public Controleur()
	{
		this.metier = new Metier();
        this.ihm    = new FrameGraph( this );
		this.metier.changeColor();
	}

    public ArrayList<Node> getLstNode() { return this.metier.getLstNode(); }
    public ArrayList<Edge> getLstEdge() { return this.metier.getLstEdge(); }

	public void majIhm()
	{
		this.ihm.repaintPanel();
	}

	public boolean coloring(Edge edge)
	{
		return this.metier.coloring(edge);
	}

	public boolean increment()
	{
		boolean bTmp = this.metier.increment();
		
		this.metier.changeColor();
		
		return bTmp;
	}

	public static void main(String args[]) 
	{
	    new Controleur();
	}
}
