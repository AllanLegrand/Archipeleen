package metier;

public enum ColorGraph
{
	YELLOW (12560217),
	GREEN  (5214316),
	GREY   (9276528),
	PURPLE (10321545),
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