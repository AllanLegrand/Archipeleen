package ihm;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import controleur.Controleur;
import metier.Edge;
import metier.Node;

public class PanelGraph extends JPanel
{
	
	public static int color;

	private Controleur ctrl;

	private ButtonCard[] tabBtnCard;

	private JButton     btnSkip;

	private JLabel      lblScore;

	public PanelGraph(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());

		/* Création des composants */
		this.tabBtnCard = new ButtonCard[10];
		for (int i = 0; i < tabBtnCard.length; i++)
		{
			tabBtnCard[i] = new ButtonCard(this.ctrl);
		}


		this.btnSkip   = new JButton("Passer le tour");

		this.lblScore  = new JLabel("Score : 0");

		/* Ajout des composants */
		JPanel panelTmp = new JPanel(new GridLayout(5, 2, 5, 5));
		for (ButtonCard jButton : tabBtnCard)
			panelTmp.add(jButton);

		this.add(panelTmp, BorderLayout.EAST);

		panelTmp = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		panelTmp.add(this.lblScore);
		panelTmp.add(this.btnSkip);

		this.add(panelTmp, BorderLayout.SOUTH);

		String id = PanelGraph.color == 255 ? "Mutaa" : "Tic\u00F3";

		for (Node node : this.ctrl.getLstNode())
			if(!node.getId().equals(id)) this.assombrir(node);


		/* Activation des composants */
		this.addMouseListener(new GereSelection(ctrl, this));
	}

	public void setScore(int score)
	{
		this.lblScore.setText("Score : " + score);
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
		node.setDark(-1);
	}

	public void eclaircir(Node node)
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

				r *= 1.10;
				b *= 1.10;
				g *= 1.10;

				imgTmp.setRGB(i, j,(alpha << 24) | (r << 16) | (g << 8) | b);
			}

		node.setImage(imgTmp);
		node.setDark(1);
	}

	public void neutre(Node node)
	{
		node.setImage(new ImageIcon("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png").getImage());
		node.setDark(0);
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
		if(this.ctrl.getDiscardSize() != ButtonCard.nbBtnReturn) return ;

		boolean selected = false;

		for (Node node : this.ctrl.getLstNode()) 
		{
			if(node.getDark() == 0 && this.estCompris(e.getX(), e.getY(), node))
			{
				//sort si deux node sont déjà choisis
				if(this.node1 != null && this.node2 != null)this.deselect();

				if(this.node1 != null && this.node2 == null)
				{
					this.node2 = node;

					node1.hasEdgeBetween(node2).setColor(PanelGraph.color);
					this.ctrl.drawCard();
				}

				if(this.node1 == null)
				{
					this.node1 = node;
				}

				this.panel.eclaircir(node);

				selected = true;
			}	
		}

		if(!selected)
		{
			this.deselect();
			return;
		}

		ArrayList<Node> lstNodeAvailable = this.ctrl.getLstNodeAvailable(this.node1);
		for (Node node : this.ctrl.getLstNode()) 
		{
			if(node.getDark() != 0 && lstNodeAvailable.contains(node))
			{
				this.panel.neutre(node);
				continue;
			}

			if(node.getDark() != -1 && !lstNodeAvailable.contains(node))
			{
				this.panel.assombrir(node);
				continue;
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