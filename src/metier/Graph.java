package metier;

import java.util.ArrayList;

import java.util.Map;

import ihm.PanelGraph;

import java.util.HashMap;

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

    

    public void addNode(String id, int posX, int posY) { this.lstNode.add(new Node(id, posX, posY)); }
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
        if ( edge.getColor() != 0) return false;

        if ( edge.isCrossed(this.lstEdge)) return false;

        if(!firstColored)
        {
            this.firstColored = true;
            return true;
        }

        if ( edge.getNode1().hasEdgeColor(PanelGraph.color) || edge.getNode2().hasEdgeColor(PanelGraph.color) ) return false; 

        if ( !( edge.getNode1().hasEdgeColor() || edge.getNode2().hasEdgeColor() ) ) return false;

        return true;
    }

    public void setFirstColored() 
    {
        this.firstColored = false;
    }
}
