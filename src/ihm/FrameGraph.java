package ihm;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;

import controleur.Controleur;

public class FrameGraph extends JFrame
{
	private static int width  = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	private Controleur ctrl;

	private PanelGraph       panelGraph;

	public FrameGraph(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setTitle("Graph");

		this.setSize((int) (height*1), (int) (height*1));
		this.setLocation(width/2 - (int) (width/3), height/2 - (int) (height/3));

		
		this.setBackground(new Color(192, 216, 226));


		this.setLayout(new GridLayout());

		/* Création des composants */
		this.panelGraph       = new PanelGraph(ctrl);

		/*Positionnement des composants */
		// this.add(this.panelChoiceColor);
		this.add(this.panelGraph);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		this.setVisible(true);
	}

	public void repaintPanel()
	{
		super.repaint();
		this.panelGraph.repaint();
	}
}