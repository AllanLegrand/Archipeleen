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
	private JButton     btnDownload;

	private JLabel      lblScore;

	private GereSelection gs;

	private int startColor;

	public PanelGraph(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());

		/* Création des composants */
		this.tabLblCard = new LabelCard[10];
		for (int i = 0; i < tabLblCard.length; i++) {
			tabLblCard[i] = new LabelCard(this.ctrl.getCard(i), ctrl);
		}
		
		this.btnSkip   = new JButton("Passer le tour");
		this.btnDownload = new JButton("Télécharcher le journal de bord");

		this.lblScore  = new JLabel("Score : 0");

		this.startColor = PanelGraph.color;

		/* Ajout des composants */
		JPanel panelTmp = new JPanel(new GridLayout(5, 2, 5, 5));
		for (LabelCard lbl : tabLblCard)
			panelTmp.add(lbl);

		this.add(panelTmp, BorderLayout.EAST);

		panelTmp = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		panelTmp.add(this.lblScore);
		panelTmp.add(this.btnSkip);
		panelTmp.add(this.btnDownload);

		this.add(panelTmp, BorderLayout.SOUTH);

		String id = PanelGraph.color == 255 ? "Mutaa" : "Tic\u00F3";

		for (Node node : this.ctrl.getLstNode())
			if(!node.getId().equals(id)) this.darken(node);


		/* Activation des composants */
		gs = new GereSelection(ctrl, this);
		this.addMouseListener(gs);
		this.addMouseMotionListener(gs);

		this.btnSkip.addActionListener(this);
		this.btnDownload.addActionListener(this);

		this.ctrl.drawCard();

	}

	public void setScore(String score)
	{
		this.lblScore.setText(score);
	}

	@Override
	protected void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;

		g2.setStroke(new BasicStroke(2F));
		g2.setColor(new Color(0, 0, 175));

		g2.drawLine(0, (int)(Controleur.HEIGHT/2.8), (int)(Controleur.WIDTH/2.5), (int)(Controleur.HEIGHT/2.8));
		g2.drawLine((int)(Controleur.WIDTH/4.3), 0, (int)(Controleur.WIDTH/4.3), Controleur.HEIGHT);
		g2.drawLine((int)(Controleur.WIDTH/2.5), (int)(Controleur.HEIGHT/2.8), (int) (Controleur.WIDTH / 1.5), 0);
		g2.drawLine((int)(Controleur.WIDTH/2.5), (int)(Controleur.HEIGHT/2.8), (int) (Controleur.WIDTH / 1.5), Controleur.HEIGHT);
		
		if(this.startColor != PanelGraph.color)
		{
			this.repaintCard();
			this.startColor = PanelGraph.color;
		}
		
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


			if(this.gs.node1 == null && this.ctrl.getLstNodeEnd().contains(node))
				this.neutral(node);
		

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
				lbl.repaint();
			}
		
	
		if(this.ctrl.getHand() != null)
		{
			Image img = new ImageIcon(this.ctrl.getHand().getPath()).getImage();
			g2.drawImage(img, (int) (this.getWidth() - Controleur.WIDTH * 0.33), (int) (this.getHeight() - Controleur.HEIGHT * 0.43), null);
		}
	}

	public void darken(Node node)
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

	public void lighten(Node node)
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

	public void neutral(Node node)
	{
		node.setImage(new ImageIcon("./donnees/images/images reduites/iles 80%/" + node.getId() + ".png").getImage());
		node.setDark(false);
	}

	public void repaintCard()
	{
		for (LabelCard labelCard : tabLblCard)
		{
			labelCard.setIcon(new ImageIcon(new ImageIcon(labelCard.getCard().getPath()).getImage().getScaledInstance((int)(Controleur.WIDTH * 0.08), (int) (Controleur.HEIGHT * 0.18), Image.SCALE_SMOOTH)));
			labelCard.repaint();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == this.btnSkip)
		{
			this.ctrl.drawCard();
			
			GereSelection.node1 = GereSelection.node2 = null;
			this.ctrl.majIhm();
		}

		if(e.getSource() == this.btnDownload) 
		{
			this.ctrl.dlLogbook();
		}
	}
}

class GereSelection extends MouseAdapter
{
	Controleur ctrl;

	PanelGraph panel;

	public static Node node1;

	public static Node node2;

	int nbEdge;

	public GereSelection(Controleur ctrl, PanelGraph panel)
	{
		this.ctrl  = ctrl;

		this.panel = panel;

		GereSelection.node1 = null;
		GereSelection.node2 = null;

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

	public void mouseMoved(MouseEvent e)
	{
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);

		for (Node node : this.ctrl.getLstNode()) 
		{
			if(!node.isDark() && this.estCompris(e.getX(), e.getY(), node) && !node.isSelected())
				cursor = new Cursor(Cursor.HAND_CURSOR);
		}

		this.panel.setCursor(cursor);
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
				if(GereSelection.node1 != null && GereSelection.node2 == null && this.ctrl.getLstNodeAvailable(GereSelection.node1).contains(node))
				{
					GereSelection.node2 = node;
					GereSelection.node2.setSelected();
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
					if(GereSelection.node1 == null && this.ctrl.getLstNodeEnd().contains(node))
					{
						GereSelection.node1 = node;
						GereSelection.node1.setSelected();
						this.panel.lighten(GereSelection.node1);
					}
				}

				selected = true;
			}	
		}


		if(!selected)
		{
			this.deselect();
		}

		if(GereSelection.node1 == null)
		{
			for (Node node : this.ctrl.getLstNode()) 
			{
				if(!this.ctrl.getLstNodeEnd().contains(node))
				{
					this.panel.darken(node);
				}	
			}
		}
		else
		{
			ArrayList<Node> lstNodeAvailable = this.ctrl.getLstNodeAvailable(GereSelection.node1);
			for (Node node : this.ctrl.getLstNode()) 
			{
				
				if(node.isDark() && lstNodeAvailable.contains(node) && node != GereSelection.node1)
				{
					this.panel.neutral(node);
					continue;
				}
	
				if(!node.isDark() && !lstNodeAvailable.contains(node))
				{
					this.panel.darken(node);
					continue;
				}
			}
		}

		this.ctrl.majIhm();
	}

	
	public void deselect()
	{
		if(GereSelection.node1 != null) 
		{
			GereSelection.node1.setSelected();
			this.panel.neutral(GereSelection.node1);
			GereSelection.node1 = null;
		}
		if(GereSelection.node2 != null)
		{
			GereSelection.node2.setSelected();
			this.panel.neutral(GereSelection.node2);
			GereSelection.node2 = null;
		}

		this.ctrl.majIhm();
	}
}