package ihm;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import controleur.Controleur;
import metier.Edge;
import metier.Node;

public class PanelGraph extends JPanel
{
	
	public static int color;

	private Controleur ctrl;

	private LabelCard[] tabLblCard;

	public PanelGraph(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());

		/* Création des composants */
		this.tabLblCard = new LabelCard[10];
		for (int i = 0; i < tabLblCard.length; i++)
			tabLblCard[i] = new LabelCard(new ImageIcon(this.ctrl.getDeck.get(i).getPath()));

		/* Ajout des composants */
		JPanel panelTmp = new JPanel(new GridLayout(5, 2, 5, 5));
		for (LabelCard jButton : tabLblCard)
			panelTmp.add(jButton);

		this.add(panelTmp, BorderLayout.EAST);

		this.addMouseListener(new GereSelection(ctrl, this));
	}


	@Override
	protected void paintComponent(Graphics g) 
	{
		int min = this.getHeight() < this.getWidth() ? this.getHeight() : this.getWidth();
		

		Graphics2D g2 = (Graphics2D) g;

		
		for (Edge edge : this.ctrl.getLstEdge()) 
		{
			Node node1 = edge.getNode1();
			Node node2 = edge.getNode2();
			
			int xStart = node1.getPosX() ;
			int yStart = node1.getPosY() ;
			int xEnd   = node2.getPosX() ;
			int yEnd   = node2.getPosY() ;


			g2.setColor(new Color(edge.getColor()));
			g2.setStroke(new BasicStroke(4F));
			g2.drawLine(xStart, yStart, xEnd, yEnd);
		}

		for (Node node : this.ctrl.getLstNode()) 
		{
			int posX = node.getPosX();
			int posY = node.getPosY();


			g2.setColor(Color.BLACK);
			g2.drawImage(node.getImg(), node.getPosXImg(), node.getPosYImg(), null);

			if(node.isSelected())
			{
				g2.setStroke(new BasicStroke(3F));
				g2.setColor(Color.YELLOW);
				g2.fillRect(posX, posY, new ImageIcon("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png").getIconWidth(), new ImageIcon("./donnees/images/" + node.getId() + ".png").getIconWidth());
			}
		}
	}
}

class GereSelection extends MouseAdapter
{
	Controleur ctrl;

	PanelGraph panel;

	Node node1;

	Node node2;

	public GereSelection(Controleur ctrl, PanelGraph panel)
	{
		this.ctrl  = ctrl;

		this.panel = panel;

		this.node1 = null;
		this.node2 = null;
	}

	public boolean estCompris(int x, int y, Node node)
	{
		Color color = null;
		try
		{
			color = new Robot().getPixelColor(x, y);
		}
		catch(Exception e){e.printStackTrace();}

		ImageIcon image = new ImageIcon(node.getImg());

		Rectangle rect = new Rectangle(node.getPosXImg(), node.getPosYImg(), image.getIconWidth() + 10, image.getIconHeight() + 10);

		return color != null && ! color.equals(Color.LIGHT_GRAY) && ! color.equals(new Color(192, 216, 226)) && rect.contains(x, y);
	}

	public void mouseClicked(MouseEvent e)
	{
		boolean nodeSelected = false;

		for (Node node : this.ctrl.getLstNode()) 
		{
			if(PanelGraph.color == 0)
			{
				this.deselect();
				return;
			}		

			if(this.estCompris(e.getXOnScreen(), e.getYOnScreen(), node))
			{

				if(this.node1 == node)
					return;

				if(this.node1 != null && this.node2 == null)
				{
					this.node2 = node;
					this.node2.setSelected();

					Edge edge  = this.node1.hasEdgeBetween(this.node2);

					if(edge == null)
                    {
                        this.deselect();
                        return;
                    }

					if(edge != null && edge.getColor() == Color.LIGHT_GRAY.getRGB())
					{
						if(this.ctrl.coloring(edge, node1, node2))
						{
							edge.setColor(PanelGraph.color);
							
							this.deselect();
							this.ctrl.calculNbTurn();
						}
						else
							this.deselect();
					}
				}	
				
				if(this.node1 == null)
				{
					this.node1 = node;
					this.node1.setSelected();
				}

				if(this.node1 == node || this.node2 == node)
					nodeSelected = true;
			}

			if(nodeSelected)
			{
				BufferedImage imgTmp;
				try {
					imgTmp = ImageIO.read(new File("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png"));
				} catch (IOException iOE) {iOE.printStackTrace(); break;}

				for (int i = 0; i < imgTmp.getWidth(); i++) 
					for (int j = 0; j < imgTmp.getHeight(); j++) 
						if(imgTmp.getRGB(i, j) != new Color(192, 216, 226).getRGB())
							imgTmp.setRGB(i, j, imgTmp.getRGB(i,j) + 10 * 256 * 256 + 10 * 256 * 10);

				node.setImage(imgTmp);
				break;
			} 
		}


		if(!nodeSelected)
			this.deselect();

		this.ctrl.majIhm();
	}

	public void deselect()
	{
		if(this.node1 != null) 
		{
			this.node1.setSelected();
			this.node1.setImage(new ImageIcon("./donnees/images/images reduites/iles 80%/" + this.node1.getId() + ".png").getImage());
			this.node1 = null;
		}
		if(this.node2 != null)
		{
			this.node2.setSelected();
			this.node2.setImage(new ImageIcon("./donnees/images/images reduites/iles 80%/" + this.node2.getId() + ".png").getImage());
			this.node2 = null;
		}

		this.ctrl.majIhm();
	}
}