package metier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileInputStream;
import javax.swing.JOptionPane;
import java.util.HashMap;

import ihm.PanelGraph;

public class Metier 
{
	private Graph g;
	
	private ArrayList<Card> deck;
	private ArrayList<Card> discard;

	private int tour;

	private static int[] tabColor = {ColorGraph.BLUE.getVal(), ColorGraph.RED.getVal()};

	public Metier()
	{
		this.g = new Graph();


		this.discard = new ArrayList<Card>(10);
		this.deck    = new ArrayList<Card>(10);


		this.generer();	
	}

	private void generer()
    {
        try
		{
			Scanner sc = new Scanner ( new FileInputStream ( "./donnees/data.csv" ) );

            // Ajout des sommets
			sc.nextLine();
			while ( sc.hasNextLine() )
			{       
			    ArrayList<String> tabS = decomposer(sc.nextLine(), ',');
			    
			    // Quand il y a une ligne vide, on passe aux regions
			    if ( tabS.size() == 1 )
			        break;
			    
				this.g.addNode(tabS.get(0), Integer.parseInt(tabS.get(1)), Integer.parseInt(tabS.get(2)));
			}
			
			// Ajout des regions
            sc.nextLine();
            while ( sc.hasNextLine() )
            {       
                ArrayList<String> tabS = decomposer(sc.nextLine(), ',');
                
                // Quand il y a une ligne vide, on passe aux arêtes
                if ( tabS.size() == 1 )
                    break;

				ArrayList <Node> lstNode = new ArrayList <Node>();

                for(int cpt = 2; cpt < tabS.size(); cpt++ )
                {
                    this.g.getNode(tabS.get(cpt)).setColor(Integer.parseInt(tabS.get(1)));

					lstNode.add( this.g.getNode(tabS.get(cpt)) );
                }

				this.g.ensRegion.put(tabS.get(0), lstNode);
            }

			while ( sc.hasNextLine() )
            {
                ArrayList<String> tabS = decomposer( sc.nextLine(), ',' );
                 
                for(int cpt = 1; cpt < tabS.size()-1; cpt++ )
                {
                    this.g.addEdge(tabS.get(cpt)+""+tabS.get(cpt+1), this.g.getNode(tabS.get(cpt)),  this.g.getNode(tabS.get(cpt+1)),
                                 Integer.parseInt( tabS.get(0)) );
                } 
            }  		

			sc.close();
		}
		catch (Exception e){ e.printStackTrace(); }
    }

	private static ArrayList<String> decomposer(String chaine, char dec)
    {
        ArrayList<String> tabS = new ArrayList<String>();
        
        String mot = "";
        for(int cpt = 0; cpt < chaine.length(); cpt++)
        {
            if ( chaine.charAt(cpt) == dec )
            {
                tabS.add(mot);
                mot = "";
            }
            else
            {
                mot += chaine.charAt(cpt); 
            }
        }
        
        tabS.add(mot);
        return tabS;
    }
	
	/**
	 * Cette méthode retourne la carte piochée
	 * @return Card
	 */
	public Card drawCard()
	{
		if ( !this.deck.isEmpty() )
		{
			Card card = this.deck.remove( 0 );
			this.discard.add ( card );

			this.calculNbTurn();

			return card;
		}

		return null;	
	}
	
	public int getFinalScore()
	{
		int total = 0;

		// couleur, nombre d'arete colorier de cette couleur, region, liste de noeud de cette region
		HashMap<Integer[], HashMap<String, ArrayList <Node>>> lstColor = new HashMap<Integer[], HashMap<String, ArrayList <Node>>>();
		for(Integer color : tabColor)
			lstColor.put( new Integer[]{color, 0}, new HashMap<String, ArrayList <Node>>());

		for ( Edge edge : this.g.getLstEdge() )
			for(Integer[] color : lstColor.keySet())
				if ( edge.getColor() == color[0] ) 
				{
					color[1]++;
					total += edge.getCost();
				}

		for (String region : this.g.ensRegion.keySet())
			for(Node node : this.g.ensRegion.get(region))
				for(Integer[] color : lstColor.keySet())
					if ( node.hasEdgeColor(color[0]) )
					{
						if ( lstColor.get(color).containsKey(region))
							lstColor.get(color).get(region).add(node);
						else 
							lstColor.get(color).put(region, new ArrayList<Node>(Arrays.asList(node)));
					}
		
		for(Integer[] color : lstColor.keySet())
		{
			int nbMaxNode = 0;
			for(ArrayList<Node> region : lstColor.get(color).values())
				if (region.size() > nbMaxNode)
					nbMaxNode = region.size();

			total += color[1] * nbMaxNode;
		}

		return total;
	}

	public void changeColor()
	{
		PanelGraph.color = tabColor[tour];
	}

	public ArrayList<Node> getLstNode() { return this.g.getLstNode(); }
	public ArrayList<Edge> getLstEdge() { return this.g.getLstEdge(); }

	public boolean coloring(Edge edge)
	{
		return this.g.coloring(edge);
	}

	public void calculNbTurn()
	{
		int nbTurn = 0;

		for ( Card card : this.discard )
			if ( card.isPrimary() ) nbTurn++;
		
		if ( nbTurn == 5 ) this.endGame();
		else this.tour++;
	}

	public void endGame()
	{
		JOptionPane.showMessageDialog(null, "La partie est finie\nVotre score est : " + this.getFinalScore());
		PanelGraph.color = 0;
	}
}
