package controleur;

import metier.Metier;
import metier.Card;
import metier.Edge;
import metier.Node;


import ihm.FrameGraph;

import java.awt.Toolkit;
import java.util.ArrayList;

/**
 * @author Allan LEGRAND
 * @author Hugo HUGO
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 * 
 * @see Metier
 * @see FrameGra(ph
 */

public class Controleur
{
	public static final int WIDTH  = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth());
	public static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight());

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

	public Card getCard(int indice) {return this.metier.getCard(indice);}

	public Card getHand() { return this.metier.getHand(); }

	public ArrayList<Node> getLstNodeAvailable(Node node)
	{
		return this.metier.getLstNodeAvailable(node);
	}
	
	public void majIhm()
	{
		this.ihm.repaintPanel();
	}

	public int getScore()
	{
		return this.metier.getFinalScore();
	}

	public Card drawCard()
	{
		return this.metier.drawCard();
	}

	public int getDiscardSize() { return this.metier.getDiscardSize(); }

	public static void main(String args[]) 
	{
		new Controleur();
	}
}
