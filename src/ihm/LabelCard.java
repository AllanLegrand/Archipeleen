package ihm;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JLabel;

public class LabelCard extends JLabel
{
	private boolean bReturn;

	public LabelCard(Icon icon)
	{
		super(icon);
		this.bReturn = false;
	}

	public void returnCard() { this.bReturn = true; }


	@Override
	protected void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;

		if(!this.bReturn)
		{
			g2.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
		else
		{
			this.getIcon().paintIcon(this, g2, 0, 0);
		}
	}

}
