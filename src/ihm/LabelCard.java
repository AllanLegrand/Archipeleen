package ihm;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


import controleur.Controleur;
import metier.Card;

public class LabelCard extends JLabel
{
	public static int        nbLblReturn = 1;
	public static LabelCard current = null;

	private Card       card;
	private Controleur ctrl;
	private boolean    bReturn;

	public LabelCard(Card card, Controleur ctrl)
	{
		super(new ImageIcon(new ImageIcon(card.getPath()).getImage().getScaledInstance((int)(Controleur.WIDTH * 0.08), (int) (Controleur.HEIGHT * 0.18), Image.SCALE_SMOOTH)));

		this.card = card;
		this.ctrl = ctrl;
		this.bReturn = false;
	}

	public void returnCard() { this.bReturn = true; }


	public boolean isReturn() {return bReturn;}

	public void setReturn() {this.bReturn = true;}

	@Override
	protected void paintComponent(Graphics g) 
	{
		this.getIcon().paintIcon(this, g, 0, 0);
	}

	public boolean equals(Card card) 
	{
		if(this.card == null)
			return card == null;
		if(card == null)
			return this.card == null;

		return this.card.equals(card);
	}
}