package ihm;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.event.MouseEvent;

import controleur.Controleur;

public class ButtonCard extends JButton implements MouseMotionListener, MouseListener
{
	public static int nbBtnReturn = 0; 

	private Controleur ctrl;
	private boolean bReturn;

	public ButtonCard(Controleur ctrl)
	{
		super(new ImageIcon(new ImageIcon("./donnees/images/carte/carte_retournée.png").getImage().getScaledInstance((int)(Controleur.WIDTH * 0.08), (int) (Controleur.HEIGHT * 0.18), Image.SCALE_SMOOTH)));

		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);

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
		
			this.setIcon(new ImageIcon(new ImageIcon(this.ctrl.getHand().getPath()).getImage().getScaledInstance((int) (Controleur.WIDTH * 0.08), (int) (Controleur.HEIGHT * 0.18), Image.SCALE_SMOOTH)));

			this.setEnabled(false);

			ButtonCard.nbBtnReturn++;
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
