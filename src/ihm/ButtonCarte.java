package ihm;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JButton;

public class ButtonCarte extends JButton
{
	private boolean bReturn;

	public ButtonCarte(Icon icon)
	{
		super(icon);

		this.bReturn = false;
	}

	public void returnCard() { this.bReturn = true; }


	@Override
	protected void paintComponent(Graphics g) 
	{
		if(!this.bReturn)
		{

		}
		else
		{

		}
	}

}
