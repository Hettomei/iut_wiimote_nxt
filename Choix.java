/*******************
*
*	Timothée Gauthier
*	Thomas Huot-Marchand
*
*	Projet tuteuré
*   Année 2009//2010
*********************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Choix extends JFrame implements Constantes, ActionListener{

	JPanel jp = new JPanel();
	JPanel jp_east = new JPanel();
	JPanel jp_west = new JPanel();
	JPanel jp_choix = new JPanel();

	JPanel jp_config_brick = new JPanel ();
	JPanel jp_config_wiimote = new JPanel ();
	
	JButton jb_wiimote = new JButton ("<< Cot\u00e9 Wiimote", new ImageIcon(getClass().getResource("/images/wiimote.png")));
	JButton jb_brick = new JButton ("Cot\u00e9 NXT >>", new ImageIcon(getClass().getResource("/images/brick.png")));
	
	JTextField txt_port_brick = new JTextField ("40000");
	JTextField txt_port_wiimote = new JTextField ("40000");
	
	JTextField txt_ip_wiimote = new JTextField ("");

	JButton jb_start_brick = new JButton ("DEMARRER");
	JButton jb_start_wiimote = new JButton ("DEMARRER");

	String ip_local = new String("");
	String ip = new String("");
	int port = 40000;
	boolean f_ouverte = true; //pour passer à la suite
	int var_choix_programme ; //NXT ou WIIMOTE
	
	public Choix (String titre, String ip){
		
		super (titre);
		f_ouverte = true;
		ip_local = ip;
		txt_ip_wiimote.setText(ip_local);
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		
		jp.setLayout (new BorderLayout());// GridLayout (1,3));
		this.add (jp);

		jp_choix.setLayout (new GridLayout (5,1) );
		jp_east.setLayout (new GridLayout (1,1) );
		jp_west.setLayout (new GridLayout (1,1) );
		
		jp.add(jp_choix, BorderLayout.CENTER);
		jp.add(jp_east, BorderLayout.EAST);
		jp.add(jp_west, BorderLayout.WEST);
		
		jb_brick.addActionListener (this);
		jb_wiimote.addActionListener (this);
		
		// choix principal wiimote ou brique
		jp_choix.add (new JLabel (""));
		jp_choix.add (jb_wiimote);
		jp_choix.add (new JLabel (""));
		jp_choix.add (jb_brick);
		jp_choix.add (new JLabel (""));

		jp_config_brick.setLayout (new GridLayout (5,1) );
		jp_east.add(jp_config_brick, BorderLayout.EAST);
	
		jp_config_brick.add (new JLabel ("Ip actuelle :"));
		jp_config_brick.add (new JLabel (ip_local));
		jp_config_brick.add (new JLabel("Port d'ecoute :"));
		jp_config_brick.add (txt_port_brick);
		jp_config_brick.add (jb_start_brick);
		jp_config_brick.setVisible(false);
		
		jp_config_wiimote.setLayout (new GridLayout (5,1) );
		jp_west.add(jp_config_wiimote, BorderLayout.WEST);
	
		jp_config_wiimote.add (new JLabel("Ip du serveur :"));
		jp_config_wiimote.add (txt_ip_wiimote);
		jp_config_wiimote.add (new JLabel("Port du serveur :"));
		jp_config_wiimote.add (txt_port_wiimote);
		jp_config_wiimote.add (jb_start_wiimote);
		
		jp_config_wiimote.setVisible(false);
		
		jb_start_brick.addActionListener (this);
		jb_start_wiimote.addActionListener (this);
 
		setResizable(false);
		pack();
		setLocationRelativeTo(null); //on centre
		setVisible (true);

	}

	public boolean ouverte(){
		//permet à la classe Start de savoir quand le choix est fait
		return f_ouverte;
	}
	public int choix_programme(){
		//permet à la classe Start de savoir quel choix est fait
		return var_choix_programme; //soit WIIMOTE soit BRICK
	}
	public int getPort(){
		return port; //pour initialiser la classe Client par la suite
	}
	public String getIp(){
		return ip;  //pour initialiser la classe Client par la suite
	}
	
	public  void    actionPerformed(ActionEvent e){
		//TODO gérer les erreurs de port et ip.
		if (e.getSource() == jb_start_brick){
			dispose();
			port = Integer.valueOf(txt_port_brick.getText());
			var_choix_programme = BRICK;
			f_ouverte=false;
		}
		else if (e.getSource() == jb_start_wiimote){
			dispose();
			port = Integer.valueOf(txt_port_wiimote.getText());
			ip = txt_ip_wiimote.getText();
			var_choix_programme = WIIMOTE;
			f_ouverte=false;
		}
		else if (e.getSource() == jb_brick){
			jp_config_brick.setVisible(true);
			jp_config_wiimote.setVisible(false);			
			jb_brick.setEnabled(false);
			jb_wiimote.setEnabled(true);
			pack();
		}
		else if (e.getSource() == jb_wiimote){
			jp_config_wiimote.setVisible(true);
			jp_config_brick.setVisible(false);
			
			jb_wiimote.setEnabled(false);
			jb_brick.setEnabled(true);
			pack();
		}
	}
}