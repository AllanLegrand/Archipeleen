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
	 * Cette classe représente une carte {@code Card} colorée 
	 * @param isPrimary désigne la primarité de la carte
	 * @param color désigne la couleur de la carte ( la couleur -1 désigne une carte multicolore ) en {@code int}
	 */
	public Card ( boolean isPrimary, int color)
	{
		this.isPrimary = isPrimary;
		this.color = color;
	}

	public boolean isEqualsColor(int color)
	{
		return this.color == -1 || color == this.color;
	}

	/**
	 * Cette méthode retourne la couleur d'une carte {@code Card}
	 * @return la couleur de la carte en {@code int}
	 */
	public int     getColor() { return this.color; }

	/**
	 * Cette méthode retourne la primarité de la carte {@code Card}
	 * @return la primarité de la carte en {@code booléen}
	 */
	public boolean isPrimary() { return this.isPrimary; }

	/**
	 * Cette méthode permet d'acceder à l'image de la carte {@code Card}
	 * @return retourne la direction de l'image de la carte en {@code String}
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

	public boolean equals(Card card) 
	{
		return this.getPath().equals(card.getPath());
	}
}