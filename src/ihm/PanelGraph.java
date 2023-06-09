package ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import controleur.Controleur;

import metier.Node;
import metier.Edge;

public class PanelGraph extends JPanel
{
	public static int radius;
	
	public static int color;

	private Controleur ctrl;

	public PanelGraph(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.addMouseListener(new GereSelection(ctrl, this));
	}


	@Override
	protected void paintComponent(Graphics g) 
	{
		int min = this.getHeight() < this.getWidth() ? this.getHeight() : this.getWidth();
		
		PanelGraph.radius = (int) (min * 0.03);

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
			int posX = node.getPosX() * (this.getWidth()  / 100);
			int posY = node.getPosY() * (this.getHeight() / 100);


			g2.setColor(Color.BLACK);
			g2.drawImage(new ImageIcon("./donnees/images/" + node.getId() + ".png").getImage(), node.getPosXImg(), node.getPosYImg(), null);

			if(node.isSelected())
			{
				g2.setStroke(new BasicStroke(3F));
				g2.setColor(Color.YELLOW);
				g2.fillRect(posX, posY, new ImageIcon("./donnees/images/" + node.getId() + ".png").getIconWidth(), new ImageIcon("./donnees/images/" + node.getId() + ".png").getIconWidth());
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
			Robot robot = new Robot();
	
			BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

			color = new Color(screenshot.getRGB(x, y));
		}
		catch(Exception e){e.printStackTrace();}

		ImageIcon image = new ImageIcon("./donnees/images/" + node.getId() + ".png");

		Rectangle rect = new Rectangle(node.getPosXImg(), node.getPosYImg(), image.getIconWidth(), image.getIconHeight());

		System.out.println(color);

		return color != null && ! color.equals(new Color(0)) && ! color.equals(new Color(192, 216, 226)) && rect.contains(x, y);
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

			int posX = node.getPosX() ;
			int posY = node.getPosY() ;

			

			if(this.estCompris(e.getXOnScreen(), e.getYOnScreen(), node))
			{
				System.out.println("test");
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

					if(edge != null && edge.getColor() == 0)
					{
						if(this.ctrl.coloring(edge))
						{
							edge.setColor(PanelGraph.color);

							this.deselect();
							this.ctrl.calculNbTurn();
						}
					}
				}	
				
				if(this.node1 == null)
				{
					this.node1 = node;
					this.node1.setSelected();
				}
				
				nodeSelected = true;
			}

			if(nodeSelected) break;
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
			this.node1 = null;
		}
		if(this.node2 != null)
		{
			this.node2.setSelected();
			this.node2 = null;
		}

		this.ctrl.majIhm();
	}
}