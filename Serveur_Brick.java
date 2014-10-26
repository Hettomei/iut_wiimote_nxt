/*******************
*
*	Timothée Gauthier
*	Thomas Huot-Marchand
*
*	Projet tuteuré
*   Année 2009//2010
*********************/

import java.net.*;
import java.io.*;
import lejos.pc.comm.*;
import java.util.*;

public class Serveur_Brick implements Constantes{
	
	public Serveur_Brick(int port) throws IOException, SocketException{
		
		byte[] msg = new byte[2500];
		DatagramSocket s = new DatagramSocket (port);
		DatagramPacket p = new DatagramPacket (msg, msg.length); //l'objet ou le packet est stocké
		
		String mess_recu ;

		System.out.println("Recheche et connection a la brique NXT");
		
		NXTConnector conn = new NXTConnector();
		boolean connected = conn.connectTo("btspp://");
		
		if (!connected) {
			System.out.println("Impossible de se connecter de la brique NXT");
			System.exit(1);
		}
		
		System.out.println("brique NXT connectee !");
		DataOutputStream dos = conn.getDataOut(); //permet d'envoyer en bluetooth
		DataInputStream dis = conn.getDataIn(); //inutile ici, mais servirait pour un feedback venant de la brique
		
		int ordre = 0;
		
		for (;;){
			s.receive (p);
			
			mess_recu = new String(p.getData (), 0, p.getLength());
			
			//Si l'ordre reçu n'est pas un integer, on envoie "STOP" à la brique
			try{
				ordre = Integer.parseInt(mess_recu);
				mess_recu = conversion(ordre);
			}
			catch(Exception e){
				ordre = STOP;
				mess_recu = "Ordre recu \"" + mess_recu + "\" inconnue !! "; //on affiche le paquet reçu du net si inconnu
			}
			
			System.out.println( "\nIp : " + p.getAddress() + 
			"\nport : " + p.getPort() +
			"\nordre : " + mess_recu);
			p.setData(msg, 0 , msg.length);
			
			try {
				System.out.println("Envoie a la brique l'ordre -> " + conversion(ordre));
				dos.writeInt(ordre);
				dos.flush();			
				
			} catch (IOException ioe) {
				System.out.println("IO Exception writing bytes:");
				System.out.println(ioe.getMessage());
				break; //si probleme avec le bluetooth, on quitte
			}
			 
		}
	}
	
	private String conversion(int i){
		//convertit les ordre reçu d'integer en String, compréhensible pour les humains
		
		switch (i)
		{
		case UP:
		return UP_S;

		case DOWN: 
		return DOWN_S;

		case LEFT:
		return LEFT_S;

		case RIGHT:
		return RIGHT_S;

		case STOP:
		return STOP_S;
		
		case DECO:
		return DECO_S;

		default:
		return "" + i; //si inconnue, on affiche l'ordre reçu.
		}
	}
}
