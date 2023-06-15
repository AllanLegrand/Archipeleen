package metier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Panel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.GregorianCalendar;

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
	private static final Integer BLUE = 255;
	private static final Integer RED = 16711680;

	public static String journalDeBord1 = "               DEBUT\n\nConstruction de la première ligne : \n\n";
	public static String journalDeBord2 = "\nConstruction de la deuxième ligne : \n\n";
	public static boolean estPremiereLigne = true;

	public static ArrayList<Integer> tabColor = new ArrayList<Integer>(Arrays.asList(Metier.BLUE, Metier.RED));

	public static final Map<String, Integer> MAP_CARD_COLOR = new HashMap<String, Integer>() 
	{
		{
			put("Jaune", 12560217);
			put("Rouge", 5214316);
			put("Vert", 9276528);
			put("Brun", 10321545);
		}
	};

	private Graph g;
	private Controleur ctrl;

	private ArrayList<Card> deck;
	private Card hand;
	private ArrayList<Card> discard;

	private int round;

	public Metier(Controleur ctrl, Integer nbScenario) 
	{
		this.g = new Graph();

		this.discard = new ArrayList<Card>();
		this.deck = new ArrayList<Card>();
		this.hand = null;

		this.ctrl = ctrl;

		this.round = 1;

		this.deck.add(new Card(false, -1));
		this.deck.add(new Card(true, -1));

		Collections.shuffle(Metier.tabColor);
		this.generate();

		if ( nbScenario != null ) {this.generate_scenario( nbScenario ); }
	}

	/**
	 * Remplissage de l'archipel {@code Archipelago} avec des île {@code Island} et
	 * des routes {@code Road} via un document csv
	 */
	private void generate() 
	{
		try 
		{
			Scanner sc = new Scanner(new FileInputStream("./donnees/data.csv"), Charset.forName("UTF-8"));

			// Ajout des sommets
			sc.nextLine();
			sc.nextLine();

			ArrayList<Node> lstNode = new ArrayList<Node>();
			String nomReg = sc.nextLine();
			while (sc.hasNextLine()) 
			{
				ArrayList<String> tabS = Metier.decomposer(sc.nextLine(), ',');
				// Quand il y a une ligne vide, on change de région
				if (tabS.size() == 1) {
					this.g.ensRegion.put(nomReg + "", lstNode);
					lstNode = new ArrayList<Node>();
					nomReg = sc.nextLine();
					if (nomReg.equals("ARETE"))
						break;
					continue;
				}

				if (!existCard(Metier.MAP_CARD_COLOR.get(tabS.get(1)))) 
				{
					this.deck.add(new Card(false, Metier.MAP_CARD_COLOR.get(tabS.get(1))));
					this.deck.add(new Card(true, Metier.MAP_CARD_COLOR.get(tabS.get(1))));
				}

				lstNode.add(this.g.addNode(tabS.get(0), (int) (Integer.parseInt(tabS.get(2)) * 0.8),
						(int) (Integer.parseInt(tabS.get(3)) * 0.8), (int) (Integer.parseInt(tabS.get(4)) * 0.8),
						(int) (Integer.parseInt(tabS.get(5)) * 0.8), Metier.MAP_CARD_COLOR.get(tabS.get(1))));
			}

			while (sc.hasNextLine()) 
			{
				ArrayList<String> tabS = Metier.decomposer(sc.nextLine(), ',');

				String node1 = tabS.get(0);
				String node2 = tabS.get(1);

				int color = Color.LIGHT_GRAY.getRGB();
				int cout = 0;
				if (tabS.size() == 3) 
				{
					color = Color.BLACK.getRGB();
					cout = Integer.parseInt(tabS.get(2));
				}

				this.g.addEdge(node1 + "-" + node2, this.g.getNode(node1), this.g.getNode(node2), cout, color);
			}

			sc.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private boolean existCard(int color) 
	{
		for (Card card : this.deck)
			if (card.getColor() == color)
				return true;
		return false;
	}

	public ArrayList<Card> getDeck() 
	{
		return this.deck;
	}

	public Card getCard(int indice) 
	{
		return this.deck.get(indice);
	}

	public Card getHand() 
	{
		return this.hand;
	}

	public static ArrayList<String> decomposer(String chaine, char dec) 
	{
		ArrayList<String> tabS = new ArrayList<String>();

		String mot = "";
		for (int cpt = 0; cpt < chaine.length(); cpt++) 
		{
			if (chaine.charAt(cpt) == dec) 
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
	 * 
	 * @return retourne la carte pioché
	 */
	public Card drawCard() 
	{
		boolean canPlay = false;

		for (Card card : this.deck)
			if (card.isPrimary()) 
			{
				canPlay = true;
				break;
			}

		if (this.hand != null)
			this.discard.add(this.hand); // this.hand = null;

		if (!this.deck.isEmpty() && canPlay) 
		{
			Card card = this.deck.remove((int) (Math.random() * this.deck.size()));

			if(Metier.estPremiereLigne)
				journalDeBord1 += "La " + card.toString() + " à été pioché\n";
			else
				journalDeBord2 += "La " + card.toString() + " à été pioché\n";
			this.hand = card;

			return card;
		}

		if (!canPlay && this.round == 2)
		{
			journalDeBord2 += "\n              FIN\n";
			this.ctrl.endGame();
		}
		else if (!canPlay) 
		{
			this.round++;

			while (!this.discard.isEmpty()) 
			{
				this.deck.add(this.discard.get(0));
				this.discard.remove(0);
			}

			this.changeColor();
			Metier.estPremiereLigne = false;

			// Manche suivante appelle l'ihm qui affiche un message : this.ctrl.nextRound();
			this.drawCard();
		}

		return null;
	}

	public void generate_scenario( int nbScenario )
	{
		try
		{
			Scanner sc = new Scanner ( new FileInputStream ( "./donnees/scenarios/"+ "scena" + nbScenario + ".txt" ) );
			
			int color = 0;
			Node n1, n2;
			boolean red = false, blue = false;
			
			// Ajout des arretes coloriées
			sc.nextLine();
			while ( sc.hasNextLine() )
			{
				ArrayList<String> tabS = Metier.decomposer( sc.nextLine(), '\t' );

				if ( tabS.size() == 1 ) break;

				if ( tabS.get(0).equals( "BLEU"  ) ) { color = Metier.BLUE ; blue = true; }
				if ( tabS.get(0).equals( "ROUGE" ) ) { color = Metier.RED; red  = true; }
				
				for ( int cpt = 2; cpt < tabS.size(); cpt++ )
				{
					n1 = this.g.getNode( tabS.get(cpt) );
					n2 = this.g.getNode( tabS.get(cpt-1) );

					n1.hasEdgeBetween( n2 ).setColor( color );
				}

			}
				
			if ( red && blue ) this.round = 2;
			else this.round = 1;

			while ( sc.hasNextLine() )
			{
				ArrayList<String> tabS = Metier.decomposer( sc.nextLine(), '\t' );
				
				if ( tabS.size() == 1 ) { continue; }

				// Gestion de la manche et de la couleur du Joueur
				if ( tabS.get(0).startsWith( "[T" ) ) 
					if ( tabS.get(1).equals("BLEU") ) 
					{
						PanelGraph.color = Metier.BLUE;
						
						if ( Metier.tabColor.get( this.round -1) != Metier.BLUE )
							Collections.swap( Metier.tabColor, 0, 1 );
					}
					else 
					{
						PanelGraph.color = Metier.RED;

						if ( Metier.tabColor.get( this.round -1) != Metier.RED )
							Collections.swap( Metier.tabColor, 1, 0 );
					}
				
				// Remplissage du deck
				this.discard.addAll( this.deck );
				this.deck.clear();
				
				
				if ( tabS.get(0).startsWith( "[C" ) )
				{
					for ( int i = 1; i < tabS.size(); i++)
					{
						for ( int j=0; j < this.discard.size(); j++)
							if ( this.discard.get(j).getColor () == Card.getColorInt( tabS.get(i).charAt(0) ) && 
									this.discard.get(j).isPrimary() )
								{
									this.deck.add( this.discard.remove(j) ); 
									break;
								}
					}

				}

			}
		}
		catch (Exception e){ e.printStackTrace(); }

		System.out.println( "Discar : " + this.discard);
		System.out.println( "Deck : " + this.deck);
		
		// Initialisation de la main, la défausse et la pioche
		
		// Permet de définir le nombre de tour passé (en mettant un nb de cartes primaires
		// dans la défausse )
		/*
		for ( int cpt = 0; cpt < this.deck.size(); cpt++ )
			if ( this.deck.get(cpt).isPrimary() && turn > 0 )
			{
				this.discard.add( this.deck.remove( cpt ) );
				turn--;
			} */
	}

	public String getJournalDeBord1() 
	{
		return Metier.journalDeBord1;
	}
	public String getJournalDeBord2() 
	{
		return Metier.journalDeBord2;
	}
	
	public String getFinalScore()
	{
		int bonusLigne = 0, bonusIle = 0, total = 0;

		// couleur, nombre d'arete colorier de cette couleur, region, liste de noeud de
		// cette region
		HashMap<Integer, HashMap<String, ArrayList<Node>>> lstColor = new HashMap<Integer, HashMap<String, ArrayList<Node>>>();
		for (Integer color : Metier.tabColor)
			lstColor.put(color, new HashMap<String, ArrayList<Node>>());

		for (Edge edge : this.g.getLstEdge())
			for (Integer color : lstColor.keySet())
				if (edge.getColor() == color) 
				{
					bonusLigne += edge.getCost();
				}
		total += bonusLigne;

		for (String region : this.g.ensRegion.keySet())
			for (Node node : this.g.ensRegion.get(region))
				for (Integer color : lstColor.keySet())
					if (node.hasEdgeColor(color) > 0) 
					{
						if (lstColor.get(color).containsKey(region))
							lstColor.get(color).get(region).add(node);
						else
							lstColor.get(color).put(region, new ArrayList<Node>(Arrays.asList(node)));
					}

		for (Node node : this.g.getLstNode()) 
		{
			int nbRegion = 0;
			for (Integer color : lstColor.keySet())
				if (node.hasEdgeColor(color) > 0)
					nbRegion++;

			if( nbRegion > 1 )
				bonusIle += 2;
		}
		total += bonusIle;

		int nbMaxNodeBleu  = 0, nbRegionBleu  = 0;
		int nbMaxNodeRouge = 0, nbRegionRouge = 0;

		for(Integer color : lstColor.keySet())
		{
			int nbMaxNode = 0;
			int nbRegion = 0;
			for (ArrayList<Node> region : lstColor.get(color).values()) 
			{
				if (region.size() > 0)
					nbRegion++;
				if (region.size() > nbMaxNode)
					nbMaxNode = region.size();
			}

			if(!(color == 255))
			{
				nbMaxNodeRouge = nbMaxNode;
				nbRegionRouge  = nbRegion;
			}
			else
			{
				nbMaxNodeBleu = nbMaxNode;
				nbRegionBleu  = nbRegion;
			}

			total += nbRegion * nbMaxNode;
		}

		return  "(Ligne bleue : " + nbRegionBleu + " * " + nbMaxNodeBleu + ")  +  " +
				"(Ligne rouge : " + nbRegionRouge + " * " + nbMaxNodeRouge + ")  +  " +
				"Bonus : " + (bonusLigne + bonusIle) + "  -->  Total : " + total;
	}

	public ArrayList<Node> getLstNode() { return this.g.getLstNode(); }
	public ArrayList<Edge> getLstEdge() { return this.g.getLstEdge(); }

	public ArrayList<Node> getLstNodeAvailable(Node node) 
	{
		ArrayList<Node> lstAvailable = new ArrayList<Node>();
		if (this.hand == null)
			return lstAvailable;

		for (Edge edge : node.getLstEdge()) 
		{
			Node tmp = edge.getNode1().equals(node) ? edge.getNode2() : edge.getNode1();
			if (this.g.coloring(edge) && this.hand.isEqualsColor(tmp.getColor()))
				lstAvailable.add(tmp);
		}

		return lstAvailable;
	}

	public ArrayList<Node> getLstNodeEnd() 
	{
		ArrayList<Node> lstTmp = new ArrayList<Node>();

		for (Node node : this.getLstNode()) 
		{
			int cpt = 0;
			for (Edge edge : node.getLstEdge()) 
			{
				if (edge.getColor() == PanelGraph.color)
					cpt++;
			}

			if (cpt == 1)
				lstTmp.add(node);
		}

		if (lstTmp.size() == 0)
			lstTmp.add(PanelGraph.color == 255 ? this.g.getNode("Mutaa") : this.g.getNode("Tic\u00F3"));

		return lstTmp;
	}

	public void changeColor() 
	{
		if (tabColor.size() > this.round - 1)
			PanelGraph.color = tabColor.get(this.round - 1);
	}

	public int getDiscardSize()
	{
		return this.discard.size();
	}

	public String getDate()
	{
		GregorianCalendar gc = new GregorianCalendar();

		int hour = gc.get(Calendar.HOUR);
		if(gc.get(Calendar.AM_PM) == 1)
			hour += 12;

		return  "" + String.format("%02d", gc.get(Calendar.DAY_OF_MONTH)) + 
				"/" + String.format("%02d", gc.get(Calendar.MONTH) + 1) + "/" + 
				gc.get(Calendar.YEAR) + "  " + 
				String.format("%02d", hour) + ":" + 
				String.format("%02d", gc.get(Calendar.MINUTE)) + ":" + 
				String.format("%02d", gc.get(Calendar.SECOND)) + "\n";
	}

	public void dlLogbook()
	{
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("log.txt"), "UTF8" ));

			pw.println (this.getDate());
			pw.println ( Metier.journalDeBord1 );
			pw.println ( Metier.journalDeBord2 );
			pw.println ( this.getFinalScore());

			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}
}