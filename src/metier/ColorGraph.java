package metier;

public enum ColorGraph
{
	JAUNE (0),
	VERT  (1),
	GRIS  (2),
	VIOLET(3),
	MULTICOLORE(null);


	private Integer val;

	private ColorGraph(Integer val)
	{
		this.val = val;
	}

	public Integer getVal() { return this.val; }
}