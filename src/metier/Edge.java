package metier;

import java.awt.Color;
import java.util.ArrayList;

/**
 * @author Allan LEGRAND
 * @author Hugo VICENTE
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 */

public class Edge
{
	private String id;
	private Node n1;
	private Node n2;
	private int color;
	private int cost;

	/**
	 * Cette classe représente une route {@code Road} reliant 2 îles {@code Island} ensemble
	 * @param id l'identifiant de la route
	 * @param n1 l'île 1 {@code Island} relié à la route
	 * @param n2 l'île 2 {@code Island} relié à la route
	 * @param color la couleur de la route en {@code int}
	 * @param cost le bonus au niveau du score de la route 
	 */
	public Edge(String id, Node n1, Node n2, int cost, int color)
	{
		this.id = id;
		this.n1 = n1;
		this.n2 = n2;

		this.cost = cost;

		this.color = color;

		this.n1.addEdge(this);
		this.n2.addEdge(this);
	}

	/**
	 * Cette méthode retourne le bonus de la route
	 * @return retourne le bonus de la route
	 */
	public int  getCost () { return this.cost; }

	/**
	 * Cette méthode retourne l'île 1 {@code Island} à l'extrémité de la route
	 * @return retourne l'île 1 
	 */
	public Node getNode1() { return this.n1; }

	/**
	 * Cette méthode retourne l'île 2 {@code Island} à l'extrémité de la route
	 * @return retourne l'île 2 
	 */
	public Node getNode2() { return this.n2; }

	/**
	 * Cette méthode change la couleur d'une route {@code Road}
	 * @return retourne la couleur de la route 
	 */
	public int getColor() { return this.color; }

	/**
	 * Cette méthode change la couleur d'une route {@code Road}
	 * @param color une couleur
	 */
	public void setColor(int color)
	{
		Metier.journalDeBord += this.toString()+"\n";
		if( color > 0 && color < 255*256*256+255*256+255)
			this.color = color;
	}

	/**
	 * Cette méthode vérifie si deux routes {@code Road} se croisent
	 * @param lstEdge une liste de route {@code ArrayList<Road>}
	 * @return retourne si la route se croise avec une autre route de la liste
	 */
	public boolean isCrossed(ArrayList<Edge> lstEdge)
	{
		double nx1 = this.getNode1().getPosX();
		double ny1 = this.getNode1().getPosY();

		// Vecteur
		double vx1 = this.getNode2().getPosX()-this.getNode1().getPosX();
		double vy1 = this.getNode2().getPosY()-this.getNode1().getPosY();

		for (Edge e : lstEdge) 
		{
			if( e.getColor() != Color.LIGHT_GRAY.getRGB() )
			{
				double nx2 = e.getNode1().getPosX();
				double ny2 = e.getNode1().getPosY();

				// Vecteur
				double vx2 = e.getNode2().getPosX()-e.getNode1().getPosX();
				double vy2 = e.getNode2().getPosY()-e.getNode1().getPosY();

				if (vx1*vy2-vy1*vx2 != 0)
				{
					double v1 = -(-vx1*ny1+vx1*ny2+vy1*nx1-vy1*nx2)/(vx1*vy2-vy1*vx2);
					double v2 = -(nx1*vy2-nx2*vy2-vx2*ny1+vx2*ny2)/(vx1*vy2-vy1*vx2);
			
					if (v1 > 0 && v1 < 1 && v2 > 0 && v2 < 1)
						return true;
				}
			}
		}

		return false;
	}

	public String toString()
	{
		return "Route de " + n1.getId() + " à " + n2.getId() + ( this.cost != 0 ? " qui rapporte un bonus" : "");
	}
}