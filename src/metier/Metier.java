package metier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.List;

import ihm.PanelGraph;

public class Metier 
{
	private Graph g;

	private int nbTurn;
	
	private ArrayList<Carte> deck;
	private ArrayList<Carte> discard;

	private int nbColor;

	private int tour;

	private static int[] tabColor = {ColorGraph.BLUE.getVal(), ColorGraph.RED.getVal()};

	public Metier()
	{
		this.g = new Graph();


		this.discard = new ArrayList<Carte>(10);
		this.deck    = new ArrayList<Carte>(10);

		for(int cpt = 0; cpt < 10; cpt++)
			if ( cpt % 2 != 0 ) this.deck.add( new Carte( true, 0/* coul */));
			else this.deck.add( new Carte( false, 1/* coul */));

		this.generer();	
		this.nbColor = 0;


		this.nbTurn = 5;
	}

	private void generer()
    {
        try
		{
			Scanner sc = new Scanner ( new FileInputStream ( "./donnees/data.csv" ), Charset.forName("UTF-8") );

            // Ajout des sommets
			sc.nextLine();
			sc.nextLine();

			ArrayList<Node> lstNode = new ArrayList<Node>();
			String nomReg = sc.nextLine();
			while ( sc.hasNextLine() )
			{       
			    ArrayList<String> tabS = decomposer(sc.nextLine(), ',');
			    System.out.println(tabS);
			    // Quand il y a une ligne vide, on change de région
			    if ( tabS.size() == 1 )
				{
			        this.g.ensRegion.put(nomReg + "", lstNode);
					lstNode.clear();
					nomReg = sc.nextLine();
					if(nomReg.equals("ARETE"))
						break;
					continue;
				}

				this.g.addNode(tabS.get(0), Integer.parseInt(tabS.get(2)), Integer.parseInt(tabS.get(3)), ColorGraph.valueOf(tabS.get(1).toUpperCase()).getVal());
			}
			

			while(sc.hasNextLine())
			{
				ArrayList<String> tabS = decomposer(sc.nextLine(), ',');

				String node1 = tabS.get(0);
				String node2 = tabS.get(1);

				this.g.addEdge(node1 + "-" + node2, this.g.getNode(node1), this.g.getNode(node2), 1);
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
	 * @return Carte
	 */
	public Carte drawCard()
	{
		if ( !this.deck.isEmpty() )
		{
			Carte card = this.deck.remove( 0 );
			this.discard.add ( card );

			if ( !card.isPrimary() ) increment( true );
			else increment( false );

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

	public void increment(boolean addTurn) 
	{
		if( addTurn )
		{
			this.nbTurn++;
		}
		else 
		{
			this.nbTurn--;
			if ( this.nbTurn <= 0 )
			{
				JOptionPane.showMessageDialog(null, "La partie est finie\nVotre score est : " + this.getFinalScore());
				PanelGraph.color = 0;
			}
		}
	}
}
