package controleur;

import metier.Metier;
import metier.Card;
import metier.Edge;
import metier.Node;


import ihm.FrameGraph;
import ihm.PanelGraph;

import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * @author Allan LEGRAND
 * @author Hugo VICENTE
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
		this.metier.changeColor();
		this.ihm    = new FrameGraph( this );
		
		this.drawCard();
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

	public Card drawCard()
	{	
		this.ihm.setScore(this.metier.getFinalScore());
		return this.metier.drawCard();
	}

	public ArrayList<Node> getLstNodeEnd()
	{
		return this.metier.getLstNodeEnd();
	}

	public String getJournalDeBord()
	{
		return this.metier.getJournalDeBord();
	}

	public int getDiscardSize() { return this.metier.getDiscardSize(); }

	
	/**
	 * Affiche le message de fin de jeu en plus du score
	 */
	public void endGame()
	{
		this.majIhm();
		JOptionPane.showMessageDialog(null, "La partie est finie\nVotre score est : " + this.metier.getFinalScore());
		PanelGraph.color = 0;
	}

	public static void main(String args[]) 
	{
		new Controleur();
	}
}
