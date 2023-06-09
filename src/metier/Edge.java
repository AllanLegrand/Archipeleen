package metier;

import java.util.ArrayList;

import ihm.PanelGraph;

public class Edge
{
    private String id;
    private Node n1;
    private Node n2;
    private int color;
    private int cost;

    public Edge(String id, Node n1, Node n2, int cost)
    {
        this.id = id;
        this.n1 = n1;
        this.n2 = n2;

        this.cost = cost;

        this.n1.addEdge(this);
        this.n2.addEdge(this);
    }

    public int  getCost () { return this.cost; }
    public Node getNode1() { return this.n1; }
    public Node getNode2() { return this.n2; }

    public int getColor() { return this.color; }
    public void setColor(int color)
    {
        if( color > 0 && color < 255*256*256+255*256+255)
            this.color = color;
    }

    public boolean isCrossed(ArrayList<Edge> lstEdge)
    {
        double nx1 = this.getNode1().getPosX();
        double ny1 = this.getNode1().getPosY();

        // Vecteur
        double vx1 = this.getNode2().getPosX()-this.getNode1().getPosX();
        double vy1 = this.getNode2().getPosY()-this.getNode1().getPosY();

        for (Edge e : lstEdge) 
        {
            if( e.getColor() != 0 )
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
}