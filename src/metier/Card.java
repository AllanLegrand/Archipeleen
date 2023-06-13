package metier;

/**
 * @author Allan LEGRAND
 * @author Hugo HUGO
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 */

public class Card
{
	private boolean isPrimary;
	private int     color;

	public Card ( boolean isPrimary, Integer color)
	{
		this.isPrimary = isPrimary;
		this.color = color;
	}

	public boolean isEqualsColor(int color)
	{
		return this.color == -1 || color == this.color;
	}

	public int     getColor() { return this.color; }
	public boolean isPrimary() { return this.isPrimary; }

	public String getPath()
	{
		String color = "";
		switch(this.color)
		{
			case 12560217 -> color = "jaune";
			case 5214316  -> color = "rose";
			case 9276528  -> color = "vert";
			case 10321545 -> color = "brun";
			default       -> color = "multi";
		}

		return "./donnees/images/carte/bord_" + (isPrimary ? "noir" : "blanc") + "_fond_" + color + ".png";
	}
}