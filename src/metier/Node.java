package metier;

import java.util.ArrayList;

public class Node 
{
    private String id;
    private int posX;
    private int posY;
    private int posXImg;
    private int posYImg;
    private int color;
    private ArrayList<Edge> lstEdge;
    private boolean isSelected;

    public Node(String id, int posX, int posY, int posXImg, int posYImg, int color)
    {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.posXImg = posXImg;
        this.posYImg = posYImg;
        this.lstEdge = new ArrayList<>();
        this.isSelected = false;

        this.color = color;
    }

    public String getId() { return this.id; }

    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }

    public int getPosXImg() { return this.posXImg; }
    public int getPosYImg() { return this.posYImg; }

    public ArrayList<Edge> getLstEdge() { return this.lstEdge; }

    public Edge hasEdgeBetween(Node n)
    {
        for (Edge e : this.lstEdge) 
            for (Edge eN : n.lstEdge) 
                if (eN == e)
                    return e;
        return null;
    }

    public void setColor( int color ) { this.color = color; }
    public int  getColor() { return this.color; }

    public void addEdge(Edge e) { this.lstEdge.add(e); }

    public boolean isSelected()  { return this.isSelected; }
    public void    setSelected() { this.isSelected = ! this.isSelected; }

    public boolean hasEdgeColor( int color)
    {
        for (Edge e : this.lstEdge) 
            if ( e.getColor() == color )
                return true;

        return false;
    }

    public boolean hasEdgeColor()
    {
        for (Edge e : this.lstEdge) 
            if ( e.getColor() != 0 )
                return true;

        return false;
    }

    public boolean equals(Node n)
    {
        return this.id.equals(n.id);
    }
}