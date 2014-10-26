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

public class Client{
	
	String adresse;
	DatagramSocket soc;
	DatagramPacket pac ;

	public Client(int port, String ipp) throws IOException, SocketException, UnknownHostException{
		adresse = new String(ipp); //soit ip soit hostname
		soc = new DatagramSocket (); //selectionne un port libre sur la machine
		String temp_s = ""; //message à envoyer, ici l'argument
		byte[] temp_msg = temp_s.getBytes(); //convertion du message
		pac = new DatagramPacket (temp_msg, temp_s.length()); //crée le packet à envoyer
		pac.setPort(port);
		pac.setAddress(InetAddress.getByName (adresse)); 
	}
	
	public void envoyer(String s) {
		byte[] msg = s.getBytes(); //convertion du message  
		pac.setData(msg);
		pac.setLength(s.length());

		try{
			soc.send (pac);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void envoyer(int i) { //convertit l'integer en String pour l'envoi
		String s = new String ("" + i);
		envoyer(s);
	}
}
