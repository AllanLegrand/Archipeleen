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

    
    public Node addNode(String id, int posX, int posY, int color, int posXImage, int posYImage) 
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
        if ( edge.getColor() != 0) return false;

        if ( edge.isCrossed(this.lstEdge)) return false;

        String n2 = edge.getNode2().getId();
        String n1 = edge.getNode1().getId();

        String islDep = "Mutaa"; 
        // ile de départ, Mutaa pour J-Bleu

        if(!firstColored && n2.equals( islDep ) || n1.equals( islDep ) )
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
