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
import java.io.*;

public class Fenetre extends JFrame implements Constantes, MouseListener{

	//(getClass().getResource("")) permet de retrouver les images dans l'archive .jar
	JButton jb_haut = new JButton (new ImageIcon(getClass().getResource("/images/img_haut.png")));
	JButton jb_bas = new JButton (new ImageIcon(getClass().getResource("/images/img_bas.png")));
	JButton jb_gauche = new JButton (new ImageIcon(getClass().getResource("/images/img_gauche.png")));
	JButton jb_droite = new JButton (new ImageIcon(getClass().getResource("/images/img_droite.png")));
	
	JLabel l_info = new JLabel ("Etat actuel : ");
	JTextArea text_info = new JTextArea (5,5);
	
	JLabel l_acc_X = new JLabel ("X : ");
	JLabel l_acc_Y = new JLabel ("Y : ");
	JLabel l_acc_Z = new JLabel ("Z : ");
	JLabel l_acc_action = new JLabel ("Action : ");
	
	JLabel l_ir_X = new JLabel ("(Largeur) X : ");
	JLabel l_ir_Y = new JLabel ("(Hauteur) Y : ");
	JLabel l_ir_action = new JLabel ("Action : ");
	
	Client envoi1;
	private int suivi_info = 0; //incrémentation du texte dans le textearea
	private Ir ir = new Ir(); //le Jpanel qui affiche la position de l'infrarouge
	
	public Fenetre (String titre, Client client){

		super(titre);
		envoi1=client;
		
		JPanel jp = new JPanel();
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		jp.setLayout (new BorderLayout ());
		add (new JScrollPane (jp));

		//panel d'info (un label plus le texte area)
		JPanel jp_infos = new JPanel ();
		jp_infos.setLayout (new BorderLayout ());
		jp.add (jp_infos, BorderLayout.CENTER);
		
		jp_infos.add (l_info, BorderLayout.NORTH);
		jp_infos.add (new JScrollPane(text_info), BorderLayout.CENTER); //texte scrollable
		text_info.setEditable(false);
		
		
		/*
		Panel des flèches de direction
		*/
		JPanel jp_direction = new JPanel ();
		jp_direction.setLayout (new GridLayout (4, 3)); //4 pour équilibrer
		jp.add (jp_direction, BorderLayout.EAST);

		jp_direction.add (new JLabel(""));
		jp_direction.add (jb_haut);
		jp_direction.add (new JLabel(""));
		jp_direction.add (jb_gauche);
		jp_direction.add (new JLabel(""));
		jp_direction.add (jb_droite);
		jp_direction.add (new JLabel(""));
		jp_direction.add (jb_bas);
		//pour équilibrer
		jp_direction.add (new JLabel(""));
		jp_direction.add (new JLabel(""));
		jp_direction.add (new JLabel(""));
		jp_direction.add (new JLabel(""));

		jb_haut.addMouseListener(this);
		jb_bas.addMouseListener(this);
		jb_gauche.addMouseListener (this);		
		jb_droite.addMouseListener (this);
		
		
		jb_haut.setForeground(Color.RED);
		/*
		Fin du panel de direction
		*/



		/*
		Partie basse : infrarouge, acccelrometre
		*/
		//jp_bas -> infrarouge plus autres informations
		JPanel jp_bas = new JPanel ();
		jp_bas.setLayout (new BorderLayout());//GridLayout(1,3));

		//panel info_infrarouge X, Y
		JPanel jp_ir_xy = new JPanel ();
		jp_ir_xy.setLayout (new GridLayout (6,1));
		jp_ir_xy.add(new JLabel("-- Infrarouge --"));
		jp_ir_xy.add(new JLabel ("min : 0, max : 1000"));
		jp_ir_xy.add(l_ir_X);
		jp_ir_xy.add(l_ir_Y);
		jp_ir_xy.add(new JLabel("   "));		
		jp_ir_xy.add(l_ir_action);
		jp_ir_xy.setPreferredSize(new Dimension(170,200));
		jp_bas.add (jp_ir_xy, BorderLayout.WEST);
		
		jp_bas.add (ir, BorderLayout.CENTER); // le panel pour visualiser l'infrarouge
		
		//panel info_accelerometre X, Y, Z
		JPanel jp_acc_xyz = new JPanel ();
		jp_acc_xyz.setLayout (new GridLayout (6,1));
		jp_acc_xyz.add(new JLabel("-- Accelerometre --"));
		jp_acc_xyz.add(new JLabel("(min : -500, max : 500)"));
		jp_acc_xyz.add(l_acc_X);
		jp_acc_xyz.add(l_acc_Y);
		jp_acc_xyz.add(l_acc_Z);
		jp_acc_xyz.add(l_acc_action);
		jp_acc_xyz.setPreferredSize(new Dimension(170,200));
		
		jp_bas.add (jp_acc_xyz, BorderLayout.EAST);

		ir.setPreferredSize(new Dimension(200,100));
		
		ir.setBorder(BorderFactory.createLineBorder(Color.black));

		jp.add(jp_bas, BorderLayout.SOUTH);

		pack();
		this.setSize(this.getWidth()+200,this.getHeight()+100); //évite les scrollbars en augmentant un peu la taille
		setLocationRelativeTo(null); //on centre
		setVisible(true);

	}
	public void set_l_info(String s){
		/*
		permet d'afficher et d'incrémenter le textearea_info
		*/
		suivi_info++;
		l_info.setText("Etat actuel : " + s);
		text_info.insert(suivi_info + ". " + s + "\n", 0);
		text_info.setCaretPosition(0); //on place le curseur au début pour être toujours au dessus
	}
	
	/*
	Affiche les information des accelerometre ou de l'infrarouge
	*/
	public void set_l_acc_XYZ(int x, int y, int z){
		l_acc_X.setText("X : " + x);
		l_acc_Y.setText("Y : " + y);
		l_acc_Z.setText("Z : " + z);
	}
	public void set_l_acc_action(String s){
		l_acc_action.setText("Action : " + s);
	}
	
	public void set_l_ir_xy(int x, int y){
		l_ir_X.setText("(Largeur) X : " + x);
		l_ir_Y.setText("(Hauteur) Y : " + y);
	}
	public void set_l_ir_action(String s){
		l_ir_action.setText("Action : " + s);
	}
	
	public void affichePointIR(double x, double y){ //met à jour le panel Infrarouge
		ir.position(x,y);
		ir.repaint();
	}


	public void mousePressed (MouseEvent e){
		/*
		on utilise mouse pressed et released pour controler plus facilement le start et stop
		*/
		if (e.getSource() == jb_haut){
			set_l_info(UP_S);
			envoi1.envoyer(UP);
		}
		else if (e.getSource() == jb_bas){
			set_l_info(DOWN_S);
			envoi1.envoyer(DOWN);
		}
		else if (e.getSource() == jb_gauche){
			set_l_info(LEFT_S);
			envoi1.envoyer(LEFT);
		}
		else if (e.getSource() == jb_droite){
			set_l_info(RIGHT_S);
			envoi1.envoyer(RIGHT);
		}
	}
	public void mouseReleased (MouseEvent e){
		set_l_info(STOP_S);
		envoi1.envoyer(STOP);
	}
	public void mouseExited (MouseEvent e){}
	public void mouseEntered (MouseEvent e){}
	public void mouseClicked (MouseEvent e){}
}