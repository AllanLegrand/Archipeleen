package metier;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * @author Allan LEGRAND
 * @author Hugo HUGO
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 */

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
	private Image img;
	private int   dark;

	public Node(String id, int posX, int posY, int posXImg, int posYImg, int color)
	{
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		this.posXImg = posXImg;
		this.posYImg = posYImg;
		this.lstEdge = new ArrayList<>();
		this.isSelected = false;

		this.img = new ImageIcon("./donnees/images/images reduites/iles 80%/" + id + ".png").getImage();
		
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
		if ( this.equals(n) ) return null;
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

	public boolean hasEdgeColor()
	{
		for (Edge e : this.lstEdge) 
			if ( e.getColor() != Color.LIGHT_GRAY.getRGB() )
				return true;

		return false;
	}

	public int hasEdgeColor( int color)
	{
		int nb = 0;
		for (Edge e : this.lstEdge) 
			if ( e.getColor() == color )
				nb++;

		return nb;
	}

	public void setImage(Image img)
	{
		this.img = img;
	}

	public Image getImg()
	{
		return this.img;
	}

	public void setDark(int dark)
	{
		this.dark = dark;
	}

	public int getDark()
	{
		return this.dark;
	}

	public boolean equals(Node n)
	{
		return this.id.equals(n.id);
	}
}