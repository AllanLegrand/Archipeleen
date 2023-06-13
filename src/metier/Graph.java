package metier;

import java.awt.Color;
import java.util.ArrayList;

import java.util.Map;

import ihm.PanelGraph;

import java.util.HashMap;

/**
 * @author Allan LEGRAND
 * @author Hugo HUGO
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

	public Graph()
	{
		this.lstNode = new ArrayList<Node>();
		this.lstEdge = new ArrayList<Edge>();

		this.firstColored = false;

		this.ensRegion = new HashMap<String, ArrayList <Node>>();
	}

	
	public Node addNode(String id, int posX, int posY, int posXImage, int posYImage, int color) 
	{ 
		this.lstNode.add(new Node(id, posX, posY, posXImage, posYImage, color));
		return this.lstNode.get(this.lstNode.size() - 1);
	}
	public void addEdge(String id, Node n1, Node n2, int cost) { this.lstEdge.add(new Edge(id, n1, n2, cost)); }

	public Node getNode(int index) { return this.lstNode.get(1); }
	public Node getNode(String id) 
	{
		for (Node n : this.lstNode) 
			if ( n.getId().equals(id))
				return n;

		return null;
	}

	public ArrayList<Node> getLstNode() { return this.lstNode; }
	public ArrayList<Edge> getLstEdge() { return this.lstEdge; }

	public boolean coloring(Edge edge)
	{
		if ( edge.getColor() != Color.LIGHT_GRAY.getRGB()) return false;

		if ( edge.isCrossed(this.lstEdge)) return false;

		if ( edge.getNode1().hasEdgeColor(PanelGraph.color) > 1 || edge.getNode2().hasEdgeColor(PanelGraph.color) > 1) return false;

		// if ( !(edge.getNode1().hasEdgeColor(PanelGraph.color) > 0 ^ edge.getNode2().hasEdgeColor(PanelGraph.color) > 0)) return false;

		return true;
	}

	public void setFirstColored() 
	{
		this.firstColored = false;
	}
}
