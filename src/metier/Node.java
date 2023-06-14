package metier;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * @author Allan LEGRAND
 * @author Hugo VICENTE
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
	private boolean isDark;

	/**
	 * Cette classe représente une île {@code Island}. Elle possède des coordonnées,
	 * une {@code Image} et ses coordonnées, une couleur et d'un boolean {@code isSelected} et dark ???
	 * @param id un identifiant unique
	 * @param posX la position X de l'île
	 * @param posY la position Y de l'île
	 * @param posImgX une position X de l'image
	 * @param posImgY une position Y de l'image
	 * @param color la couleur de l'île 
	 */
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

	/**
	 * Cette méthode retourne l'id de l'île {@code Island}
	 * @return retourne l'identifiant de l'île 
	 */
	public String getId() { return this.id; }

	public int getPosX() { return this.posX; }
	public int getPosY() { return this.posY; }

	public int getPosXImg() { return this.posXImg; }
	public int getPosYImg() { return this.posYImg; }

	public ArrayList<Edge> getLstEdge() { return this.lstEdge; }


	/**
	 * Cette méthode détencte présence d'une route {@code Road} entre 2 îles {@code Island} et la retourne
	 * @param n une île {@code Island}
	 * @return retourne la route {@code Road}
	 */
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

	public void setDark(boolean b)
	{
		this.isDark = b;
	}

	public boolean isDark()
	{
		return this.isDark;
	}

	public boolean equals(Node n)
	{
		return this.id.equals(n.id);
	}
}