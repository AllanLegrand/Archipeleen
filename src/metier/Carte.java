package metier;

public class Carte 
{
    private boolean isPrimary;
    private int color;

    public Carte ( boolean isPrimary, int color)
    {
        this.isPrimary = isPrimary;
        this.color = color;
    }

    public int getColor() { return this.color; }
    public boolean isPrimary() { return this.isPrimary; }
}