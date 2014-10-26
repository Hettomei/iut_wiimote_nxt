/*******************
*
*	Timothée Gauthier
*	Thomas Huot-Marchand
*
*	Projet tuteuré
*   Année 2009//2010
*********************/

import wiiremotej.*;
import wiiremotej.event.* ;
import java.io.*;
import java.util.Date;//pour limiter le nombre de commande par milliseconde

public class Wiimote01 implements Constantes {

	WiiRemote wiimote = null ;
	Client envoi1; 
	Fenetre f;

	//recuperer infra rouge
	double irX;
	double irY;

	int etat[] = new int[4];
	/*
	recupere l'etat des capteurs X/Y/Z en cours
	etat[1] ou etat[XX]=>X
	etat[2] ou etat[YY]=>Y
	etat[3] ou etat[ZZ]=>Z
	 */

	/* variable utiles pour savoir quelle mouvement à été envoyé avec l'infrarouge ou l'accelerometre */
	/* utile pour ne pas envoyer constamment les ordres sur le réseaux*/
	boolean m_gauche = false;
	boolean m_droite = false;
	boolean m_haut = false;
	boolean m_bas = false;

	boolean vibration = true;
	
	boolean en_mouvement = false; //si a l'arret, inutile d'envoyer la commande STOP !
	
	long temps_start_vibre = 0;
	long temps_last_action = 0;
	
	int temps_blocage = 300; // + augmente le temps entre 2 action /*sauf STOP*/, - le diminue

	//mode accelerometre au lancement de l'aplication, on peut le définir sur "IROUGE"
	boolean mode = ACCELEROMETRE;//accelerometre ou infrarouge, mais pas ensemble

	public Wiimote01 (Fenetre fenetre, Client client) {

		Date date = new Date();
		temps_start_vibre = date.getTime(); //pour ne pas surchager en envoye de commande
		temps_last_action = date.getTime();
		
		f = fenetre;
		envoi1 = client;//new Client();

		connecter_wiimote(); //methode à part pour pouvoir reconnecter ou connecter une autre wiimote
	}

	private void connecter_wiimote(){
		f.set_l_info("\n ***** Instructions *****\n" +
		"touche directionnelle -> fait bouger la brique dans le sens de la fl\u00e8che\n" +
		"touche B -> avance\n" +
		"touche A -> stop\n" +
		"touche 1 -> active/desactive les vibrations\n" +
		"touche 2 -> bascule entre l'acc\u00e9l\u00e9rom\u00e8tre et l'infrarouge\n" + 
		"touche - ou + -> diminue ou augmente le temps de blocage entre 2 ordres\n" +
		"pour une connexion lente il est conseill\u00e9 d'augmenter ce temps\n" +
		"***********************");
		
		while (wiimote == null) {
			try {
				f.set_l_info("Recherche de wiimote");
				wiimote = WiiRemoteJ.findRemote();//methode bloquante
				/*
			si on veut une wiimote bien précise avec l'adresse mac, faire :
			wiimote = WiiRemoteJ.connectToRemote ("0024F3957B2C") ;
				 */
			}
			catch(Exception e) {
				wiimote = null;
				System.out.println("Probleme de connection, reessayez");
				f.set_l_info("Connection \u00e9chou\u00e9. Appuyez de nouveau sur 1 et 2");
			}
		}
		try{wiimote.vibrateFor(100);}catch(IOException ioe){} //signale qu'elle est connecté en vibrant
		f.set_l_info("Wiimote connect\u00e9 ! @MAC: " + wiimote.getBluetoothAddress());

		try {
			//activation des bouton, accelerometre, capteur infrarouge (mode basic, seul la position est relevée)
			wiimote.addWiiRemoteListener (new WiimoteListener ()) ;
			wiimote.setIRSensorEnabled (true, WRIREvent.BASIC) ;
			wiimote.setAccelerometerEnabled (true) ;
		}
		catch (Exception e){e.printStackTrace();}
	}

	public void vibre(){
		//prendre le temps précédent et le nouveau, si moins de X milliseconde écoulé, ne pas vibrer;
		if (vibration == true){ // vibration activé ou non
			Date date = new Date();
			if ((date.getTime() - temps_start_vibre) > 500){ //temps fixe car trop court ça rame
				temps_start_vibre = date.getTime();

				try{wiimote.startVibrating();}
				catch(IOException ioe){}
			}
		}
	}
	
	public void stop_vibre(){
		if (vibration==true){ //condition nécessaire sinon il envoie en bluetooth et fait ramer le programme pour rien
			try{wiimote.stopVibrating();}
			catch(IOException ioe){}
		}
	}
	
	boolean is_ok(){ /* permet de déterminer si trop d'actions on été envoyées ou non*/
		Date date = new Date();
		if ((date.getTime() - temps_last_action) > temps_blocage){
			temps_last_action = date.getTime();
			en_mouvement = true; //il bouge
			return true;
		}
		else{
			return false;
		}
	}
	boolean is_ok_stop(){ /* temps plus réduit pour "stop"*/
		if (en_mouvement == true){ // si il bouge, on le stop
			en_mouvement = false;
			return true;
		}
		else{ //si deja stoppé, on ne fait rien
			return false;
		}
	}
	public void haut(boolean x){
		
		if (is_ok()){
			envoi1.envoyer(UP);
			f.set_l_info(UP_S);

			vibre();
		
			if (x == IROUGE){f.set_l_ir_action(UP_S);}
			else{f.set_l_acc_action(UP_S);}
		}
	}
	
	public void bas(boolean x){
		
		if (is_ok()){		
			envoi1.envoyer(DOWN);
			f.set_l_info(DOWN_S);

			vibre();
		
			if (x == IROUGE){
				f.set_l_ir_action(DOWN_S);
			}
			else{
				f.set_l_acc_action(DOWN_S);
			}
		}
	}
	
	public void gauche(boolean x){
		
		if (is_ok()){
			envoi1.envoyer(LEFT);
			f.set_l_info(LEFT_S);

			vibre();
		
			if (x == IROUGE){
				f.set_l_ir_action(LEFT_S);
			}
			else{
				f.set_l_acc_action(LEFT_S);
			}
		}
	}
	
	public void droite(boolean x){
		
		if (is_ok()){
			envoi1.envoyer(RIGHT);
			f.set_l_info(RIGHT_S);
		
			vibre();
		
			if (x == IROUGE){
				f.set_l_ir_action(RIGHT_S);
			}
			else{
				f.set_l_acc_action(RIGHT_S);
			}
		}
	}
	
	public void stop(boolean x){
		if (is_ok_stop()){
			envoi1.envoyer(STOP);
			f.set_l_info(STOP_S);
			stop_vibre();
		
			if (x == IROUGE){
				f.set_l_ir_action(STOP_S);
			}
			else{
				f.set_l_acc_action(STOP_S);
			}
		}
	}
	
	public void stop(){
		//stop sans argument -> croix directionnelle, pas de vibration, pas de choix acceleromettre ou infrarouge
		if (is_ok_stop()){
			envoi1.envoyer(STOP);
			f.set_l_info(STOP_S);
		}	
	}

	class WiimoteListener implements WiiRemoteListener {
		public void IRInputReceived(WRIREvent ire) {
			/*
			on prend la position d'une seul source : getIRLights()[0]
			 */
			if (mode == IROUGE){ //verification du mode
				if (ire.getIRLights()[0] != null) {
					irX=1000 - (ire.getIRLights()[0].getX()*1000); // - 1000 pour inverser
					irY =(ire.getIRLights()[0].getY()*1000);

					f.affichePointIR(irX,irY); //1000 - irX car il faut inverser X pour afficher à l'écran

					f.set_l_ir_xy((int)(irX),(int)(irY));


	/* détection de la zone du curseur */

					if ((irX > 250 && irX < 750) && (m_gauche == true || m_droite == true)){
						m_gauche = false;
						m_droite = false;
						stop(IROUGE);

					}
					if (irX > 850 && m_droite == false){
						m_droite = true;
						droite(IROUGE);
					}
					if (irX < 151 && m_gauche == false){
						m_gauche = true;
						gauche(IROUGE);
					}


					if ((irY > 250 && irY < 750) && (m_haut == true || m_bas == true)){
						m_haut = false;
						m_bas = false;
						stop(IROUGE);
					}

					if (irY < 151 && m_haut == false){
						m_haut = true;
						haut(IROUGE);
					}
					if (irY > 850 && m_bas == false){
						m_bas = true;
						bas(IROUGE);
					}
				}
			}
		}

		public void accelerationInputReceived(WRAccelerationEvent ae) {

			if (mode == ACCELEROMETRE){ //si le bon mode est activé, on exécute
				etat[XX] = (int)(ae.getXAcceleration()*100);
				etat[YY] = (int)(ae.getYAcceleration()*100);
				etat[ZZ] = (int)(ae.getZAcceleration()*100);

				f.set_l_acc_XYZ(etat[XX], etat[YY], etat[ZZ]);

				//obligé de vérifier m_x pour ne pas envoyer"STOP" sans arrêt sur le réseaux (et en bluetooth)
				if ((etat[XX] > -50 && etat[XX] < 50) && (m_gauche == true || m_droite == true)){
					m_gauche = false;
					m_droite = false;
					stop(ACCELEROMETRE);

				}
				if (etat[XX] > 70 && m_droite == false){
					m_droite = true;
					droite(ACCELEROMETRE);
				}
				if (etat[XX] < -70 && m_gauche == false){
					m_gauche = true;
					gauche(ACCELEROMETRE);
				}


				if ((etat[YY] > -40 && etat[YY] < 40) && (m_haut == true || m_bas == true)){
					m_haut = false;
					m_bas = false;
					stop(ACCELEROMETRE);
				}

				if (etat[YY] > 60 && m_haut == false){
					m_haut = true;
					haut(ACCELEROMETRE);
				}
				if (etat[YY] < -60 && m_bas == false){
					m_bas = true;
					bas(ACCELEROMETRE);
				}
			}
		}

		public void buttonInputReceived (WRButtonEvent be) {

			if (be.wasPressed (WRButtonEvent.ONE)) {}
			if (be.wasPressed (WRButtonEvent.TWO)) {}
			if (be.wasPressed (WRButtonEvent.B)) {
				if (is_ok()){
					envoi1.envoyer(UP);
					f.set_l_info(UP_S);
				}
			}
			if (be.wasPressed (WRButtonEvent.A)){}
			if (be.wasPressed (WRButtonEvent.MINUS)) {}
			if (be.wasPressed (WRButtonEvent.PLUS)) {}
			if (be.wasPressed (WRButtonEvent.HOME)){}

			if (be.wasPressed (WRButtonEvent.UP)) {
				if (is_ok()){
					envoi1.envoyer(UP);
					f.set_l_info(UP_S);
				}
			}
			if (be.wasPressed (WRButtonEvent.DOWN)) {
				if (is_ok()){
					envoi1.envoyer(DOWN);
					f.set_l_info(DOWN_S);
				}
			}
			if (be.wasPressed (WRButtonEvent.LEFT)) {
				if (is_ok()){
					envoi1.envoyer(LEFT);
					f.set_l_info(LEFT_S);
				}
			}
			if (be.wasPressed (WRButtonEvent.RIGHT)) {
				if (is_ok()){
					envoi1.envoyer(RIGHT);		
					f.set_l_info(RIGHT_S);
				}
			}
			if (be.wasReleased (WRButtonEvent.ONE)) {
				vibration = !vibration;
				try{wiimote.stopVibrating();} //au cas ou, on coupe les vibrations, actif ou pas actif
				catch(IOException ioe){} 
				if (vibration==true){
					f.set_l_info("Vibrations activ\u00e9es");
				}
				else{
					f.set_l_info("Vibrations desactiv\u00e9es");
				}

			}
			if (be.wasReleased (WRButtonEvent.TWO)) {
				mode = !mode;
				if (mode==ACCELEROMETRE){
					f.set_l_info("Mode acc\u00e9l\u00e9rom\u00e8tre");
				}
				else{
					f.set_l_info("Mode infrarouge");
				}
			}
			
			if (be.wasReleased (WRButtonEvent.A)) {
				//pour lui, dans tous les cas : on stop tout, mouvement et vibration!
					envoi1.envoyer(STOP);
					f.set_l_info(STOP_S);
					en_mouvement = false;
					try{wiimote.stopVibrating();}
					catch(IOException ioe){}
			}
			if (be.wasReleased (WRButtonEvent.HOME))  {}
			
			//////////STOP car on relache
			if (be.wasReleased (WRButtonEvent.B)) {
				stop();
			}
			if (be.wasReleased (WRButtonEvent.UP)) {
				stop();
			}
			if (be.wasReleased (WRButtonEvent.DOWN)) {
				stop();
			}
			if (be.wasReleased (WRButtonEvent.LEFT))  {
				stop();
			}
			if (be.wasReleased (WRButtonEvent.RIGHT)) {
				stop();
			}
			
			//modification de l'interval entre deux commandes
			if (be.wasReleased (WRButtonEvent.PLUS)) {
				if (temps_blocage < 1000){	temps_blocage += 10; } //on ne peut dépasser 1 seconde, sinon, incontrolable car imprécis
				f.set_l_info("Interval entre 2 commandes : " + temps_blocage + " ms");
			}
			if (be.wasReleased (WRButtonEvent.MINUS)) {
				if (temps_blocage > 10){	temps_blocage -= 10; }
				f.set_l_info("Interval entre 2 commandes : " + temps_blocage + " ms");
			}
		}
		public void disconnected() {
			f.set_l_info(DECO_S);
			System.out.println (DECO_S) ;
			wiimote = null; //reinitiallise
			connecter_wiimote();
		}
		
		/***********************************************************
		*
		*
		*
		*
		Méthode propre à la classe WiiRemoteJ, inutile dans ce programme mais permet un développement futur
		Il y a aussi la possibilité d'envoyé et de jouer un son sur la wiimote.
		*
		*
		*
		*******************************************************/

		public void combinedInputReceived(WRCombinedEvent arg0) {
			//System.out.println ("combinedInputReceived") ;
		}
		public void extensionConnected(WiiRemoteExtension arg0) {
			System.out.println ("extensionConnected") ;
			try {
				wiimote.setExtensionEnabled (true) ;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void extensionDisconnected(WiiRemoteExtension arg0) {
			System.out.println ("extensionDisconnected") ;
		}

		public void extensionInputReceived (WRExtensionEvent ee) {

		}
		public void extensionPartiallyInserted () {
			System.out.println ("extensionPartiallyInserted") ;
		}
		public void extensionUnknown () {
			System.out.println ("extensionUnknown") ;
		}
		public void statusReported (WRStatusEvent arg0) {
			System.out.println ("statusReported") ;
		}
	}
}
