package ihm;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import controleur.Controleur;
import metier.Edge;
import metier.Node;

/**
 * @author Allan LEGRAND
 * @author Hugo VICENTE
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 * 
 * @see Controleur
 * @see Node
 * @see Edge
 */

public class PanelGraph extends JPanel implements ActionListener
{
	
	public static int color;

	private Controleur ctrl;

	private LabelCard[] tabLblCard;

	private JButton     btnSkip;

	private JLabel      lblScore;

	private GereSelection gs;

	public PanelGraph(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());

		/* Création des composants */
		this.tabLblCard = new LabelCard[10];
		for (int i = 0; i < tabLblCard.length - 1; i++)
		{
			tabLblCard[i] = new LabelCard(this.ctrl.getCard(i), ctrl);
		}

		tabLblCard[tabLblCard.length - 1] = new LabelCard(this.ctrl.getHand(), ctrl);
		
		this.btnSkip   = new JButton("Passer le tour");

		this.lblScore  = new JLabel("Score : 0");

		/* Ajout des composants */
		JPanel panelTmp = new JPanel(new GridLayout(5, 2, 5, 5));
		for (LabelCard lbl : tabLblCard)
			panelTmp.add(lbl);

		this.add(panelTmp, BorderLayout.EAST);

		panelTmp = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		panelTmp.add(this.lblScore);
		panelTmp.add(this.btnSkip);

		this.add(panelTmp, BorderLayout.SOUTH);

		String id = PanelGraph.color == 255 ? "Mutaa" : "Tic\u00F3";

		for (Node node : this.ctrl.getLstNode())
			if(!node.getId().equals(id)) this.assombrir(node);


		/* Activation des composants */
		gs = new GereSelection(ctrl, this);
		this.addMouseListener(gs);

		this.btnSkip.addActionListener(this);
	}

	public void setScore(int score)
	{
		this.lblScore.setText("Score : " + score);
	}

	@Override
	protected void paintComponent(Graphics g) 
	{
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



			if(this.ctrl.getLstNodeEnd().contains(node) && node != this.gs.node1)
				this.neutre(node);

			g2.setColor(Color.BLACK);
			g2.drawImage(node.getImg(), node.getPosXImg(), node.getPosYImg(), null);
			g2.setColor(Color.WHITE);
			g2.drawString(node.getId(), node.getPosX(), node.getPosY());
			g2.setColor(Color.BLACK);


			if(node.isSelected())
			{
				g2.setStroke(new BasicStroke(3F));
				g2.setColor(Color.YELLOW);
				g2.fillRect(posX, posY, new ImageIcon("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png").getIconWidth(), new ImageIcon("./donnees/images/" + node.getId() + ".png").getIconWidth());
			}
		}

		for (LabelCard lbl : tabLblCard)
			if(lbl.equals(this.ctrl.getHand()))
			{
				lbl.setIcon(new ImageIcon(new ImageIcon("./donnees/images/carte/carte_retournée.png").getImage().getScaledInstance((int) (Controleur.WIDTH * 0.08), (int) (Controleur.HEIGHT * 0.18), Image.SCALE_SMOOTH)));
				lbl.setReturn();
				lbl.repaint();
			}
		
	
		if(this.ctrl.getHand() != null)
		{
			Image img = new ImageIcon(this.ctrl.getHand().getPath()).getImage();
			g2.drawImage(img, (int) (this.getWidth() - Controleur.WIDTH * 0.33), (int) (this.getHeight() - Controleur.HEIGHT * 0.43), null);
		}
	}

	public void assombrir(Node node)
	{
		if(node.isDark() || (this.gs != null && node == this.gs.node1)) return;
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

				r *= 0.65;
				b *= 0.65;
				g *= 0.65;

				imgTmp.setRGB(i, j,(alpha << 24) | (r << 16) | (g << 8) | b);
			}

		node.setImage(imgTmp);
		node.setDark(true);
	}

	public void eclaircir(Node node)
	{
		BufferedImage imgTmp;
		try {
			imgTmp = ImageIO.read(new File("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png"));
		} catch (IOException iOE) {iOE.printStackTrace(); return;}

		Graphics2D g2 = imgTmp.createGraphics();

		for (int i = 0; i < imgTmp.getWidth(); i++) 
			for (int j = 0; j < imgTmp.getHeight(); j++) 
			{
				int rgb0 = -1;
				if(j > 0)
					rgb0 = imgTmp.getRGB(i, j - 1);

				int rgb = imgTmp.getRGB(i, j);
				
				int alpha0 = rgb0 == -1 ? 1 : (rgb0 >> 24) & 0xFF;
				int alpha = (rgb >> 24) & 0xFF;

				if(alpha != 0 && alpha0 == 0)
				{
					g2.setColor(Color.RED);
					g2.drawLine(i, j+2, i, j-2);
				}
			}

		
		g2.dispose();

		node.setImage(imgTmp);
		node.setDark(false);
	}

	public void neutre(Node node)
	{
		node.setImage(new ImageIcon("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png").getImage());
		node.setDark(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		this.ctrl.drawCard();
		this.ctrl.majIhm();
	}
}

class GereSelection extends MouseAdapter
{
	Controleur ctrl;

	PanelGraph panel;

	Node node1;

	Node node2;

	int nbEdge;

	public GereSelection(Controleur ctrl, PanelGraph panel)
	{
		this.ctrl  = ctrl;

		this.panel = panel;

		this.node1 = null;
		this.node2 = null;

		this.nbEdge = 0;
	}

	public boolean estCompris(int x, int y, Node node)
	{
		BufferedImage image;
		try {
			image = ImageIO.read(new File("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png"));
		} catch (IOException e) {e.printStackTrace(); return false;}

		int posXImg = x - node.getPosXImg();
		int posYImg = y - node.getPosYImg();

		if ( !(posXImg >= 0 && posXImg < image.getWidth() &&
			   posYImg >= 0 && posYImg < image.getHeight()) )
			return false;
		

		int pixel = image.getRGB(posXImg, posYImg);
		
		int alpha =  (pixel >> 24) & 0xFF;


		return alpha != 0;
	}

	public void mouseClicked(MouseEvent e)
	{
		boolean selected = false;

		for (Node node : this.ctrl.getLstNode()) 
		{

			if(!node.isDark() && this.estCompris(e.getX(), e.getY(), node))
			{
				if(node1 == node)
					break;
			
				//sélection de la deuxième node
				if(this.node1 != null && this.node2 == null)
				{
					this.node2 = node;

					Edge edge = node1.hasEdgeBetween(node2);

					if(edge != null && edge.getColor() != PanelGraph.color)
					{
						edge.setColor(PanelGraph.color);
						this.ctrl.drawCard();
						
						this.nbEdge++;
					}

					this.deselect();
				}
				else
				{
					//sélection de la 1ère node
					if(this.node1 == null && this.ctrl.getLstNodeEnd().contains(node))
					{
						this.node1 = node;
						this.panel.eclaircir(this.node1);
					}
				}

				selected = true;
			}	
		}


		if(!selected)
		{
			this.deselect();
		}

		if(this.node1 == null)
		{
			for (Node node : this.ctrl.getLstNode()) 
			{
				if(!this.ctrl.getLstNodeEnd().contains(node))
				{
					this.panel.assombrir(node);
				}	
			}
		}
		else
		{
			ArrayList<Node> lstNodeAvailable = this.ctrl.getLstNodeAvailable(this.node1);
			for (Node node : this.ctrl.getLstNode()) 
			{
				if(node.isDark() && lstNodeAvailable.contains(node) && node != this.node1)
				{
					this.panel.neutre(node);
					continue;
				}
	
				if(!node.isDark()&& !lstNodeAvailable.contains(node))
				{
					this.panel.assombrir(node);
					continue;
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
			this.panel.neutre(this.node1);
			this.node1 = null;
		}
		if(this.node2 != null)
		{
			this.node2.setSelected();
			this.panel.neutre(this.node2);
			this.node2 = null;
		}

		this.ctrl.majIhm();
	}
}