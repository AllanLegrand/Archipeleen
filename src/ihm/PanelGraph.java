package ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

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

		this.setBackground(new Color(192, 216, 226));

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
			
			int xStart = node1.getPosX() * (this.getWidth()  / 100);
			int yStart = node1.getPosY() * (this.getHeight() / 100);
			int xEnd   = node2.getPosX() * (this.getWidth()  / 100);
			int yEnd   = node2.getPosY() * (this.getHeight() / 100);


			g2.setColor(new Color(edge.getColor()));
			g2.setStroke(new BasicStroke(4F));
			g2.drawLine(xStart, yStart, xEnd, yEnd);
		}

		for (Node node : this.ctrl.getLstNode()) 
		{
			int posX = node.getPosX() * (this.getWidth()  / 100);
			int posY = node.getPosY() * (this.getHeight() / 100);


			g2.setColor(new Color(node.getColor()));
			g2.fillOval(posX - (PanelGraph.radius / 2), posY - (PanelGraph.radius / 2), PanelGraph.radius, PanelGraph.radius);
		
			if(node.isSelected())
			{
				g2.setStroke(new BasicStroke(3F));
				g2.setColor(Color.GRAY);
				g2.drawOval(posX - (PanelGraph.radius / 2), posY - (PanelGraph.radius / 2), PanelGraph.radius, PanelGraph.radius);
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


			int posX = node.getPosX() * (this.panel.getWidth()  / 100);
			int posY = node.getPosY() * (this.panel.getHeight() / 100);

			if(Point2D.distance(posX, posY, e.getX(), e.getY()) < PanelGraph.radius)
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

					if(edge != null && edge.getColor() == 0)
					{
						if(this.ctrl.coloring(edge))
						{
							edge.setColor(PanelGraph.color);

							this.deselect();
							this.ctrl.increment();
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