/*******************
*
*	Timothée Gauthier
*	Thomas Huot-Marchand
*
*	Projet tuteuré
*   Année 2009//2010
*********************/

import com.intel.bluetooth.BlueCoveConfigProperties;
import java.net.*;

public class Start implements Constantes{

	public static void main (String[] args){
		//initialisation du bluetooth
		System.setProperty (BlueCoveConfigProperties.PROPERTY_JSR_82_PSM_MINIMUM_OFF, "true") ;

		//recupere l'adresse ip, fonctionne pour windows et mac, 127.0.0.1 sous linux
		String ip_local = new String("");
		try {
			InetAddress Ip = InetAddress.getLocalHost();
			ip_local = Ip.getHostAddress();
		}
		catch(Exception e) {
			ip_local = "Probleme";
		}

		Choix c = new Choix ("Wiimote ou Brique NXT ?", ip_local);

		while(c.ouverte()){ //on attend que l'utilisateur fasse le choix. c.ouverte() <- fonction qui return true si fenetre ouverte, false quand fermée
			try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
		}

		if (c.choix_programme() == WIIMOTE){
		try{
			Client cl = new Client(c.getPort(), c.getIp()); //on transmet le port et l'ip du serveur entré dans la fenetre "Choix"
			Fenetre f = new Fenetre ("Controle de la brique NXT par la Wiimote", cl);  // cree fenetre avec titre, et ip//port
			new Wiimote01 (f,cl) ;//on lui transmet la classe fenetre (pour afficher les actions) et la classe "envoyer"
		}
		catch(Exception z){
			z.printStackTrace();
		}
	}
	else{
		System.out.println("Selection de la brique NXT");
		
		try{new Serveur_Brick(c.getPort());}
		catch(Exception z){
			z.printStackTrace();
		}
	}

	} 
}