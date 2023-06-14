package metier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.awt.Color;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import java.util.HashMap;
import java.util.Map;

import ihm.PanelGraph;
import controleur.Controleur;

/**
 * @author Allan LEGRAND
 * @author Hugo VICENTE
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 * 
 * @see Card
 * @see Graph
 */

public class Metier 
{
	private Graph g;
	private Controleur ctrl;

	private ArrayList<Card> deck;
	private Card hand;
	private ArrayList<Card> discard;

	private int round;
	private int specialTurn; // utiliser pour determinerla bifurcation

	protected static String journalDeBord = "";
	private boolean bifurcation;

	public static ArrayList<Integer> tabColor = new ArrayList<Integer>(Arrays.asList(255, 16711680));

	public static final Map<String,Integer> MAP_CARD_COLOR = new HashMap<String, Integer>() 
	{{
    	put("Jaune", 12560217);
    	put("Rouge", 5214316 );
		put("Vert" , 9276528 );
		put("Brun" , 10321545);
	}};

	public Metier( Controleur ctrl)
	{
		this.g = new Graph();

		this.discard = new ArrayList<Card>();
		this.deck    = new ArrayList<Card>();
		this.hand    = null;

		this.ctrl = ctrl;
		this.bifurcation = false;

		this.round = 1;

		this.specialTurn = 4;//(int) (Math.random() * 10);

		this.deck.add(new Card(false, -1));
		this.deck.add(new Card(true , -1));
		
		Collections.shuffle(Metier.tabColor);
		this.generate();
	}

	/**
	 * Remplissage de l'archipel {@code Archipelago} avec des île {@code Island} et des routes {@code Road} via un document csv
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
					lstNode = new ArrayList<Node>();
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

				lstNode.add(this.g.addNode(tabS.get(0), (int) (Integer.parseInt(tabS.get(2)) * 0.8), (int) (Integer.parseInt(tabS.get(3)) * 0.8), (int) (Integer.parseInt(tabS.get(4)) * 0.8), (int) (Integer.parseInt(tabS.get(5)) * 0.8), Metier.MAP_CARD_COLOR.get(tabS.get(1))));
			}
			

			while(sc.hasNextLine())
			{
				ArrayList<String> tabS = Metier.decomposer(sc.nextLine(), ',');

				String node1 = tabS.get(0);
				String node2 = tabS.get(1);

				int color = Color.LIGHT_GRAY.getRGB();
				int cout = 0;
				if(tabS.size() == 3)
				{
					color = Color.BLACK.getRGB();
					cout = Integer.parseInt(tabS.get(2));
				}

				this.g.addEdge(node1 + "-" + node2, this.g.getNode(node1), this.g.getNode(node2), cout, color);
			}

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
	public boolean  getBifurcation() { return this.bifurcation; }

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
	 * Retourne la carte piochée {@code Card} et la met dans la main
	 * @return retourne la carte pioché
	 */
	public Card drawCard()
	{
		boolean canPlay = false;

		if ( this.specialTurn -1 == this.discard.size() ) this.bifurcation = true;
		else this.bifurcation = false;
		
		for ( Card card : this.deck )
			if ( card.isPrimary() ) 
			{
				canPlay = true;
				break;
			}

		if ( this.hand != null ) this.discard.add( this.hand ); //this.hand = null;

		if ( !this.deck.isEmpty() && canPlay )
		{
			Card card = this.deck.remove( (int) (Math.random() * this.deck.size()) );

			journalDeBord += card.toString()+"\n";
			this.hand = card;
		
			return card;
		}
		
		if ( !canPlay && this.round == 2) this.ctrl.endGame();
		else if ( !canPlay )
		{
			this.round++;

			while ( !this.discard.isEmpty() )
			{
				this.deck.add( this.discard.get(0) );
				this.discard.remove(0);
			}

			this.changeColor();
			
			// Manche suivante appelle l'ihm qui affiche un message : this.ctrl.nextRound();
			
			this.specialTurn = (int) (Math.random() * 10);
			this.drawCard();
		}

		return null;
	}

	public String getJournalDeBord()
	{
		return Metier.journalDeBord;
	}
	
	public int getFinalScore()
	{
		int total = 0;

		// couleur, nombre d'arete colorier de cette couleur, region, liste de noeud de cette region
		HashMap<Integer, HashMap<String, ArrayList <Node>>> lstColor = new HashMap<Integer, HashMap<String, ArrayList <Node>>>();
		for(Integer color : Metier.tabColor)
			lstColor.put( color, new HashMap<String, ArrayList <Node>>());

		for ( Edge edge : this.g.getLstEdge() )
			for(Integer color : lstColor.keySet())
				if ( edge.getColor() == color ) 
				{
					total += edge.getCost();
				}

		for (String region : this.g.ensRegion.keySet())
			for(Node node : this.g.ensRegion.get(region))
				for(Integer color : lstColor.keySet())
					if ( node.hasEdgeColor(color) > 0)
					{
						if ( lstColor.get(color).containsKey(region))
							lstColor.get(color).get(region).add(node);
						else 
							lstColor.get(color).put(region, new ArrayList<Node>(Arrays.asList(node)));
					}
		
		for(Integer color : lstColor.keySet())
		{
			int nbMaxNode = 0;
			int nbRegion = 0;
			for(ArrayList<Node> region : lstColor.get(color).values())
			{
				if(region.size() > 0)
					nbRegion++;
				if (region.size() > nbMaxNode)
					nbMaxNode = region.size();
			}

			total += nbRegion * nbMaxNode;
		}

		return total;
	}

	public ArrayList<Node> getLstNode() { return this.g.getLstNode(); }
	public ArrayList<Edge> getLstEdge() { return this.g.getLstEdge(); }


	public ArrayList<Node> getLstNodeAvailable( Node node )
	{
		ArrayList<Node> lstAvailable = new ArrayList<Node>();
		if(this.hand == null)
			return lstAvailable;

		for (Edge edge : node.getLstEdge())
		{
			Node tmp = edge.getNode1().equals(node) ? edge.getNode2() : edge.getNode1();
			if( this.g.coloring(edge) && this.hand.isEqualsColor(tmp.getColor()))
				lstAvailable.add(tmp);
		}
		
		return lstAvailable;
	}

	public ArrayList<Node> getLstNodeEnd( boolean bifurcation )
	{
		ArrayList<Node> lstTmp = new ArrayList<Node>();

		for (Node node : this.getLstNode())
		{
			int cpt = 0;
			for (Edge edge : node.getLstEdge()) 
			{
				if(edge.getColor() == PanelGraph.color)
					cpt++;
			}

			if( cpt == 1 )
				lstTmp.add(node);
			else if( cpt >= 1 && this.ctrl.getBifurcation() )			
				lstTmp.add(node);
		}

		if(lstTmp.size() == 0)
			lstTmp.add(PanelGraph.color == 255 ? this.g.getNode("Mutaa") : this.g.getNode("Tic\u00F3"));

		System.out.println(lstTmp);
		return lstTmp;
	}

	public void changeColor()
	{
		if( tabColor.size() > this.round-1)
			PanelGraph.color = tabColor.get(this.round-1);
	}

	public int getDiscardSize() { return this.discard.size(); }
}