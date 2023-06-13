package metier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;

import ihm.PanelGraph;

public class Metier 
{
	private Graph g;
	
	private ArrayList<Card> deck;
	private Card hand;
	private ArrayList<Card> discard;

	private int round;
	private boolean newColor;

	private static ArrayList<Integer> tabColor = new ArrayList<Integer>(Arrays.asList(255, 16711680));

	public static final Map<String,Integer> MAP_CARD_COLOR = new HashMap<String, Integer>() 
	{{
    	put("Jaune", 12560217);
    	put("Rouge", 5214316 );
		put("Vert" , 9276528 );
		put("Brun" , 10321545);
	}};

	public Metier()
	{
		this.g = new Graph();

		this.newColor = true;

		this.discard = new ArrayList<Card>((Metier.MAP_CARD_COLOR.size()+1)*2);
		this.deck    = new ArrayList<Card>((Metier.MAP_CARD_COLOR.size()+1)*2);
		this.hand    = null;

		this.deck.add(new Card(false, -1));
		this.deck.add(new Card(true , -1));

		this.generate();
		Collections.shuffle(tabColor);
		this.drawCard();
	}

	/**
	 * Remplissage du graphe (node et edge) via un document csv
	 */
	private void generate()
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
			    ArrayList<String> tabS = Metier.decomposer(sc.nextLine(), ',');
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

				if(!existCard(Metier.MAP_CARD_COLOR.get(tabS.get(1))))
				{
					this.deck.add(new Card(false, Metier.MAP_CARD_COLOR.get(tabS.get(1))));
					this.deck.add(new Card(true, Metier.MAP_CARD_COLOR.get(tabS.get(1))));
				}

				this.g.addNode(tabS.get(0), (int) (Integer.parseInt(tabS.get(2)) * 0.8), (int) (Integer.parseInt(tabS.get(3)) * 0.8), (int) (Integer.parseInt(tabS.get(4)) * 0.8), (int) (Integer.parseInt(tabS.get(5)) * 0.8), Integer.parseInt(tabS.get(3)));
			}
			

			while(sc.hasNextLine())
			{
				ArrayList<String> tabS = Metier.decomposer(sc.nextLine(), ',');

				String node1 = tabS.get(0);
				String node2 = tabS.get(1);

				int cout = 0;
				if(tabS.size() == 3)
					cout = Integer.parseInt(tabS.get(2));

				this.g.addEdge(node1 + "-" + node2, this.g.getNode(node1), this.g.getNode(node2), cout);
			}

			System.out.println(this.deck.size());
			sc.close();
		}
		catch (Exception e){ e.printStackTrace(); }
    }

	private boolean existCard(int color)
	{
		for (Card card : this.deck) 
			if ( card.getColor() == color)
				return true;
		return false;
	}
	
	public ArrayList<Card> getDeck() { return this.deck; }

	public Card getCard(int indice) { return this.deck.get(indice); }
	public Card getHand() { return this.hand; }

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
	 * Retourne la carte piochée et la met dans la main
	 * @return Card : la carte rétourné
	 */
	public Card drawCard()
	{
		this.hand = null;
		if ( !this.deck.isEmpty() )
		{
			this.calculNbTurn();

			Card card = this.deck.remove( 0 );

			this.hand = card;

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
					if ( node.hasEdgeColor(color[0]) > 0)
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

	public ArrayList<Node> getLstNode() { return this.g.getLstNode(); }
	public ArrayList<Edge> getLstEdge() { return this.g.getLstEdge(); }

	public boolean coloring(Edge edge)
	{
		if ( this.hand.getColor() ==  (edge.getNode1().hasEdgeColor(PanelGraph.color) > 0 ? edge.getNode2() : edge.getNode1()).getColor()) 
			return this.g.coloring(edge);
		return false;
	}

	public ArrayList<Node> getLstNodeAvailable()
	{
		ArrayList<Node> lstAvailable = new ArrayList<Node>();
		for(Node node : this.g.getLstNode())
			if( node.hasEdgeColor())
				for (Edge edge : node.getLstEdge()) 
				{
					Node tmp = edge.getNode1().equals(node) ? edge.getNode2() : edge.getNode1();
					if( this.coloring(edge) && this.hand.getColor() == tmp.getColor())
						lstAvailable.add(tmp);
				}
		
		return lstAvailable;
	}

	public void calculNbTurn()
	{
		int nbTurn = 0;

		for ( Card card : this.discard )
			if ( card.isPrimary() ) nbTurn++;
		
		if ( nbTurn == 5 && this.round == 1) this.endGame();
		if ( nbTurn == 5 )
		{
			this.round++; 
			for(Card card : this.discard)
			{
				this.newColor = true;
				this.deck.add(card);
				this.deck.remove(card);
			}
		}
		
		this.changeColor();
	}

	public void changeColor()
	{
		PanelGraph.color = tabColor.get(this.round);	
	}

	public void endGame()
	{
		JOptionPane.showMessageDialog(null, "La partie est finie\nVotre score est : " + this.getFinalScore());
		PanelGraph.color = 0;
	}

}
