package ihm;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import controleur.Controleur;

/**
 * @author Allan LEGRAND
 * @author Hugo VICENTE
 * @author Luc LECARPENTIER
 * @author Ashanti NJANJA
 * 
 * @see Controleur
 * @see PanelGraph
 */

public class Launcher extends JFrame implements ActionListener, ItemListener
{
	private static int width  = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
	private static int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

	private Controleur ctrl;
	private JButton btnPlay;
	private JButton btnScenario;

	private JComboBox<String> lstScenario;

	public Launcher(Controleur ctrl)
	{
		//this.setLayout( new GridLayout( 1, 2) );

		this.ctrl = ctrl;

		this.setTitle("Launcheur");
		this.setSize(width, height);
		this.setLocation(0, 0);
		this.setResizable(false);		
		//this.setBackground( new Image() );

		/* Création des composants */
		this.btnPlay = new JButton( "Jouer" );
		this.btnScenario = new JButton( "Jouer un Scenario" );
		this.btnScenario.setEnabled(false);

		String[] libScenario = { "Route Bonus", "Cycle (avec 1 couleurs)", "Cycle (avec 2 couleurs)", "Fin de partie"};
		this.lstScenario = new JComboBox<String>( libScenario );
		this.lstScenario.isEditable();

		/* Activation des composants */
		this.lstScenario.addItemListener(this);
		this.btnScenario.addActionListener(this);
		this.btnPlay.addActionListener(this);

		/*Positionnement des composants */

		JPanel panelFlow = new JPanel();
		//panelFlow.setLayout( new BorderLayout( 2, 1 ) );
		panelFlow.add( this.lstScenario );
		panelFlow.add( this.btnScenario );
		this.add(panelFlow, BorderLayout.NORTH);

		panelFlow = new JPanel();
		panelFlow.add( this.btnPlay);

		JPanel panelGrid = new JPanel();
		panelGrid.setLayout( new GridLayout(2,1,6,0) );
		panelGrid.add( new JLabel("Partie en solo : "), JLabel.CENTER );
		panelGrid.add( panelFlow );

		this.add(panelGrid, BorderLayout.CENTER);
		

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
		System.out.println(this.lstScenario.getSelectedIndex());
	}

	public void actionPerformed( ActionEvent e ) 
	{
		if ( e.getSource() == this.btnPlay )
		{
			this.ctrl.setScenario( null );
			this.ctrl.setIhm( new FrameGraph( this.ctrl ) );
			this.ctrl.drawCard();

			this.dispose();
		}

		if ( e.getSource() == this.btnScenario )
		{	
			this.ctrl.setScenario( this.lstScenario.getSelectedIndex() +1 );	
			this.ctrl.setIhm( new FrameGraph( this.ctrl ) );
			this.ctrl.drawCard();

			this.dispose();
		}

	}

	public void itemStateChanged(ItemEvent e) 
	{
		// si l'état du combobox est modifiée 
		if ( e.getSource() == this.lstScenario )
			this.btnScenario.setEnabled(true);
	}
}