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

	/**
	 * Cette classe représente une carte colorée 
	 * @param isPrimary désigne si la carte est primaire
	 * @param color désigne la couleur de la carte
	 */

	public Card ( boolean isPrimary, Integer color)
	{
		this.isPrimary = isPrimary;
		this.color = color;
	}

	public int     getColor() { return this.color; }
	public boolean isPrimary() { return this.isPrimary; }

	/**
	 * Cette méthode permet d'acceder à l'image de la carte
	 * @return La direction de l'image de la carte
	 */
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