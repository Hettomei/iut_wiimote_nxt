/*******************
*
*	Timothée Gauthier
*	Thomas Huot-Marchand
*
*	Projet tuteuré
*   Année 2009//2010
*********************/

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
 
public class Ir extends JPanel {
 
	//initialisation hors du JPanel, pour ne pas voir le rond
        private double posX = -10;
        private double posY = -10;

        public void paintComponent(Graphics g){
				g.setColor(new Color(204,255,255)); //bleu clair
            	g.fillRect(-10, -10, getWidth() + 20, getHeight() + 20);

				Font font = new Font("hb", Font.BOLD, 20);
				Font font2 = new Font("gd", Font.BOLD, 10);
				
				g.setColor(Color.BLACK);
				//on dessine les zones qui active la brique NXT
				//de 0 à 1000
				// X : 200 à 750 -> STOP
				// Y : 200 à 750 -> STOP
				// HAUT : Y : de 0 à 151
				// BAS : Y de 801 à 1000
				// Droite : X de 800 à 1000
				// Gauche : X de 0 à 151
				
				/*ZONE ou la brique STOP*/
				//TODO remplacer par drawRect
				g.drawLine( (int)adapt_x(250) ,(int)adapt_y(250), (int)adapt_x(750) ,(int)adapt_y(250)); //HAUT
				g.drawLine( (int)adapt_x(250) ,(int)adapt_y(250), (int)adapt_x(250) ,(int)adapt_y(750)); //GAUCHE
				g.drawLine( (int)adapt_x(750) ,(int)adapt_y(250), (int)adapt_x(750) ,(int)adapt_y(750)); //DROITE
				g.drawLine( (int)adapt_x(250) ,(int)adapt_y(750), (int)adapt_x(750) ,(int)adapt_y(750)); //BAS
				
				g.setFont( font );
				g.setColor(Color.RED);
				g.drawString("STOP" , getWidth()/2 - 30, getHeight()/2);
				/*******/
				
				//Zone Haut
				g.setColor(Color.BLACK);
				g.drawLine( (int)adapt_x(0) ,(int)adapt_y(150), (int)adapt_x(1000) ,(int)adapt_y(150));

				g.setFont( font );
				g.setColor(Color.RED);
				g.drawString("HAUT" , getWidth()/2 - 30, 20);
				
				//zone gauche
				g.setColor(Color.BLACK);
				g.drawLine( (int)adapt_x(150) ,(int)adapt_y(0), (int)adapt_x(150) ,(int)adapt_y(1000));
				
				g.setFont( font2 );
				g.setColor(Color.RED);
				g.drawString("GAUCHE" , 5, getHeight()/2);
				
				//zone droite
				g.setColor(Color.BLACK);
				g.drawLine( (int)adapt_x(850) ,(int)adapt_y(0), (int)adapt_x(850) ,(int)adapt_y(1000));
				g.setFont( font2 );
				g.setColor(Color.RED);
				g.drawString("DROITE" , getWidth()-45, getHeight()/2);
				
				
				//zone bas
				g.setColor(Color.BLACK);
				g.drawLine( (int)adapt_x(0) ,(int)adapt_y(850), (int)adapt_x(1000) ,(int)adapt_y(850));
				g.setColor(Color.RED);

				g.setFont( font );				
				g.drawString("BAS" , getWidth()/2 - 20, getHeight()-10);
                
				g.setColor(Color.RED);
                g.fillOval((int)posX, (int)posY, 10, 10);
				
        }

		public void position(double irX, double irY){
			//le max de irX est 1000, ce code sert à adapté à la taille du jpanel
			posX = irX/(1000.0/getWidth());
			posY = irY/(1000.0/getHeight());
		}
		
		/*cree pour plus de clarté dans le dessin des zones, s'adapte à la taille de la fenetre
		retourne des doubles sinon créé des problèmes avec les divisions (division par zéro)*/
		private double adapt_x(int n){
			return n/(1000.0/getWidth());
		}
		private double adapt_y(int n){
			return n/(1000.0/getHeight());
		}
        
}