package metier;

public class Card
{
	private boolean isPrimary;
	private Integer color;

	public Card ( boolean isPrimary, Integer color)
	{
		this.isPrimary = isPrimary;
		this.color = color;
	}

	public int getColor() { return this.color; }
	public boolean isPrimary() { return this.isPrimary; }
}