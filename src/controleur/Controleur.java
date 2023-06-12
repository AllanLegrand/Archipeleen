package controleur;

import metier.Metier;
import metier.Card;
import metier.Edge;
import metier.Node;


import ihm.FrameGraph;

import java.util.ArrayList;

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

	public Card getHand() { return this.metier.getHand(); }

	public ArrayList<Node> getLstNodeAvailable()
	{
		return this.metier.getLstNodeAvailable();
	}

	public Card getCard(int indice)
	{
		return this.metier.getCard(indice);
	}
	
	public void majIhm()
	{
		this.ihm.repaintPanel();
	}

	public ArrayList<Card> getDeck()
	{
		return this.metier.getDeck();
	}

	public int getScore()
	{
		return this.metier.getFinalScore();
	}

	public boolean coloring(Edge edge, Node nodeStart, Node nodeEnd)
	{
		return this.metier.coloring(edge);
	}

	public void calculNbTurn()
	{
		this.metier.calculNbTurn();
	}

	public static void main(String args[]) 
	{
		new Controleur();
	}
}
