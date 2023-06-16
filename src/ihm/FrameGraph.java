package ihm;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Toolkit;

import controleur.Controleur;
import metier.Metier;

/**
 * @author Allan LEGRAND
 * @author Hugo VICENTE
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 * 
 * @see Controleur
 * @see PanelGraph
 */

public class FrameGraph extends JFrame
{
	protected static int width  = Controleur.WIDTH ;
    protected static int height = Controleur.HEIGHT;

	private Controleur ctrl;

	private PanelGraph       panelGraph;

	public FrameGraph(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setTitle("Graph");

		if(System.getProperty("os.name").equals("Linux"))
			this.setSize(width, height-25);
		else 
			this.setSize(width, height);
		
		this.setLocation(0, 0);
		this.setResizable(false);
		
		this.setBackground(new Color(192, 216, 226));


		/* Création des composants */
		this.panelGraph       = new PanelGraph(ctrl);

		/*Positionnement des composants */
		// this.add(this.panelChoiceColor);
		this.add(this.panelGraph);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		this.setVisible(true);
	}

	/**
	 * Modifie le score
	 * @param score le score entré
	 */
	public void setScore(String score)
	{
		this.panelGraph.setScore(score);
	}

	/**
	 * Affiche un message pour preciser la seconde manche
	 * au joueur
	 */
	public void nextRound()
	{
		String sMess = "        PROCHAIN TOUR\nVous jouez la couleur " + ( PanelGraph.color == Metier.BLUE ? "bleu" : "rouge" ) + 
					   "\n        Départ à " + ( PanelGraph.color == Metier.BLUE ? "Mutaa" : "Tico" );
		JOptionPane.showMessageDialog(null, sMess );
	}

	/**
	 * Affiche un message pour preciser la fin de partie
	 * au joueur
	 */
	public void endGame()
	{
		JOptionPane.showMessageDialog(null, "La partie est finie\n" + this.ctrl.getFinalScore());
		PanelGraph.color = 0;
	}

	/**
	 * Recharge l'affichage du jeu 
	 */
	public void repaintPanel()
	{
		super.repaint();

		this.panelGraph.repaint();
	}
}