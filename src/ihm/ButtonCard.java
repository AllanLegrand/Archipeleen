package ihm;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.MouseEvent;

import controleur.Controleur;
import metier.Card;

public class ButtonCard extends JLabel implements MouseMotionListener, MouseListener
{
	public static int        nbBtnReturn = 0;
	public static ButtonCard current = null;

	private Card       card;
	private Controleur ctrl;
	private boolean    bReturn;

	public ButtonCard(Card card, Controleur ctrl)
	{
		super(new ImageIcon(new ImageIcon(card.getPath()).getImage().getScaledInstance((int)(Controleur.WIDTH * 0.08), (int) (Controleur.HEIGHT * 0.18), Image.SCALE_SMOOTH)));

		this.card = card;
		this.ctrl = ctrl;
		this.bReturn = false;

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void returnCard() { this.bReturn = true; }

	public boolean estCliquable(int x, int y)
	{
		if(bReturn) return false;

		if(x > Controleur.WIDTH * 0.08)
			return false;

		if(ButtonCard.nbBtnReturn >= this.ctrl.getDiscardSize())
			return false;

		return true;
	}

	public boolean isReturn() {return bReturn;}

	@Override
	protected void paintComponent(Graphics g) 
	{
		this.getIcon().paintIcon(null, g, 0, 0);
	}


	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(this.estCliquable(e.getX(), e.getY()))
		{
			this.bReturn = true;
		
			this.setIcon(new ImageIcon(new ImageIcon("./donnees/images/carte/carte_retournée.png").getImage().getScaledInstance((int) (Controleur.WIDTH * 0.08), (int) (Controleur.HEIGHT * 0.18), Image.SCALE_SMOOTH)));

			this.setEnabled(false);

			ButtonCard.current = this;
			ButtonCard.nbBtnReturn++;

			this.ctrl.setHand(this.card);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		if(this.estCliquable(e.getX(), e.getY()))
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		else
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

}
