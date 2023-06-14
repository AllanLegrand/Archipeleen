package metier;

import java.awt.Color;
import java.util.ArrayList;

import java.util.Map;

import ihm.PanelGraph;

import java.util.HashMap;

/**
 * @author Allan LEGRAND
 * @author Hugo VICENTE
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 * 
 * @see Node
 * @see Edge
 */

public class Graph 
{
	private ArrayList<Node> lstNode;
	private ArrayList<Edge> lstEdge;

	private boolean firstColored;

	protected Map<String, ArrayList <Node>> ensRegion;

	/**
	 * Cette classe représente une archipel {@code Archipelago} composé d'une liste plusieurs îles {@code Island} 
	 * et d'une ou plusieurs routes {@code Road}. Les attributs sont : 
	 * <p>{@code lstNode} désigne une liste d'îles </br>
	 * <p>{@code lstEdge} désigne une liste de routes </br>
	 * <p>{@code firstColored} désigne l'état coloration de la première route </br>
	 * <p>{@code ensRegion} désigne les regions et leur nom 
	 */
	public Graph()
	{
		this.lstNode = new ArrayList<Node>();
		this.lstEdge = new ArrayList<Edge>();

		this.firstColored = false;

		this.ensRegion = new HashMap<String, ArrayList <Node>>();
	}


	/**
	 * Cette méthode permet d'ajouter une île{@code Island} dans l'archipel {@code Archipelago}
	 * Elle reprend les paramètres du constructeur Island
	 * @see Node
	 */
	public Node addNode(String id, int posX, int posY, int posXImage, int posYImage, int color) 
	{ 
		this.lstNode.add(new Node(id, posX, posY, posXImage, posYImage, color));
		return this.lstNode.get(this.lstNode.size() - 1);
	}


	/**
	 * Cette méthode permet d'ajouter une route {@code Road} dans l'Archipel {@code Archipelago}
	 * Elle reprend les paramètres du constructeur Road
	 * @see Edge
	 */
	public void addEdge(String id, Node n1, Node n2, int cost) { this.lstEdge.add(new Edge(id, n1, n2, cost)); }

	/**
	 * Cette méthode retourne une île {@code Island} de l'archipel via un indice
	 * @param index désigne l'indice de la liste
	 * @return retourne île de l'indice entré de l'archipel de l'archipel
	 */
	public Node getNode(int index) { return this.lstNode.get(1); }

	/**
	 * Cette méthode retourne une île {@code Island} de l'archipel via son identifiant
	 * @param id désigne l'identifiant d'une île {@code Island}
	 * @return retourne l'île de l'indice entré de l'archipel
	 */
	public Node getNode(String id) 
	{
		for (Node n : this.lstNode) 
			if ( n.getId().equals(id))
				return n;

		return null;
	}

	/**
	 * Cette méthode permet retourne toutes les routes {@code ArrayList<Island>} de l'archipel
	 * @return retourne une liste de toutes les îles 
	 */
	public ArrayList<Node> getLstNode() { return this.lstNode; }

	/**
	 * Cette méthode permet retourne toutes les routes {@code ArrayList<Road>} de l'archipel
	 * @return retourne une liste de toutes les routes 
	 */
	public ArrayList<Edge> getLstEdge() { return this.lstEdge; }

	/**
	 * Cette méthode permet de colorer une route {@code Road}
	 * @param edge la route {@code Road} qu'on veut colorier 
	 * @return La possibilité de colorier une route
	 * 
	 */
	public boolean coloring(Edge edge)
	{
		if ( edge.getColor() != Color.LIGHT_GRAY.getRGB()) return false;

		if ( edge.isCrossed(this.lstEdge)) return false;

		if ( edge.getNode1().hasEdgeColor(PanelGraph.color) > 1 || edge.getNode2().hasEdgeColor(PanelGraph.color) > 1) return false;

		if ( edge.getNode1().hasEdgeColor(PanelGraph.color) > 0 && edge.getNode2().hasEdgeColor(PanelGraph.color) > 0) return false;

		return true;
	}
}
