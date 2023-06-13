package ihm;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import metier.Card;

public class LabelCard extends JLabel
{
	private boolean bReturn;
	private Card    card;

	public LabelCard(Card card)
	{
		super(new ImageIcon(card.getPath()));


		this.card = card;
		
		this.bReturn = false;
	}

	public void returnCard() { this.bReturn = true; }

}
