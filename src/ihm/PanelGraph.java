package ihm;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
		for (int i = 0; i < tabLblCard.length - 1; i++)
		{
			tabLblCard[i] = new LabelCard(this.ctrl.getCard(i));
		}

		tabLblCard[9] = new LabelCard(this.ctrl.getHand());

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
		
		String id = PanelGraph.color == 255 ? "Mutaa" : "Tic\u00F3";

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

			if(!node.getId().equals(id)) this.assombrir(node);

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

	public void assombrir(Node node)
	{
		BufferedImage imgTmp;
		try {
			imgTmp = ImageIO.read(new File("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png"));
		} catch (IOException iOE) {iOE.printStackTrace(); return;}

		for (int i = 0; i < imgTmp.getWidth(); i++) 
			for (int j = 0; j < imgTmp.getHeight(); j++) 
			{
				int rgb = imgTmp.getRGB(i, j);

				int alpha = (rgb >> 24) & 0xFF;
				int r     = (rgb >> 16) & 0xFF;
				int g     = (rgb >> 8) & 0xFF;
				int b     = rgb & 0xFF;

				r *= 0.50;
				b *= 0.50;
				g *= 0.50;

				imgTmp.setRGB(i, j,(alpha << 24) | (r << 16) | (g << 8) | b);
			}

		node.setImage(imgTmp);
		node.setDark(true);
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
		BufferedImage image;
		try {
			image = ImageIO.read(new File("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png"));
		} catch (IOException e) {e.printStackTrace(); return false;}

		int posXImg = x - node.getPosXImg();
		int posYImg = y - node.getPosYImg();


		if ( !(posXImg >= 0 && posXImg <= image.getWidth() &&
			   posYImg >= 0 && posYImg <= image.getHeight()) )
			return false;
		

		
		int pixel = image.getRGB(posXImg, posYImg);
		
		int alpha =  (pixel >> 24) & 0xFF;


		return alpha != 0;
	}

	public void mouseClicked(MouseEvent e)
	{
		ArrayList<Node> lstTmp = this.ctrl.getLstNodeAvailable();
		
		boolean nodeSelected = false;

		for (Node node : this.ctrl.getLstNode()) 
		{
			if(nodeSelected) break;


			if(PanelGraph.color == 0)
			{
				this.deselect();
				return;
			}		


			if(!node.isDark() && this.estCompris(e.getX(), e.getY(), node))
			{

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
						if(this.ctrl.coloring(edge, node1, node2) && lstTmp.contains(node1))
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
				{
					nodeSelected = true;
				}
			}

		
		}


		if(!nodeSelected)
			this.deselect();
		else
		{
			for (Node node : this.ctrl.getLstNode())
			{
				System.out.println(lstTmp);
				if(!lstTmp.contains(node) && !node.isDark())
				{
					System.out.println("assombrir");
					this.panel.assombrir(node);
				}

				if(lstTmp.contains(node) && node.isDark())
				{
					System.out.println("éclaircir");
					node.setImage(new ImageIcon("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png").getImage());
				}
			}
		}

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