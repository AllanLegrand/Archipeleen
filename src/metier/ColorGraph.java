package metier;

public enum ColorGraph
{
	JAUNE  (12560217),
	VERT  (5214316),
	BRUN   (9276528),
	ROUGE (10321545),
	MULTICOLOR(null),

	RED(16711680),
	BLUE(255);


	private Integer val;

	private ColorGraph(Integer val)
	{
		this.val = val;
	}

	public Integer getVal() { return this.val; }
}