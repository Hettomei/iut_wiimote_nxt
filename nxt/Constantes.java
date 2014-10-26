public interface Constantes {
    
/* Listes des ordres//mouvements */

    final static int AVANCER = 10000;
    final static int UP = 10000;
    final static String UP_S = "AVANCER";

    final static int RECULER = 20000; 
    final static int DOWN = 20000;
	final static String DOWN_S = "RECULER";

    final static int GAUCHE = 30000;
    final static int LEFT = 30000;
	final static String LEFT_S = "GAUCHE";	

    final static int DROITE = 40000;
    final static int RIGHT = 40000;
	final static String RIGHT_S = "DROITE";

    final static int STOP = 50000;
	final static String STOP_S = "STOP";


/* Fin de liste */

    final static int DECO = 60000;
	final static String DECO_S = "DECONNECTE";

//pour la selection du programme à lancer
    final static int WIIMOTE = 10;
    final static int BRICK = 20;
////////////

//utile pour l'affichage des valeurs de l'accéléromètre 
    final static int XX = 1;
    final static int YY = 2;
    final static int ZZ = 3;

	//choix du mode accelerometre ou infrarouge
	final static boolean ACCELEROMETRE = true;
	final static boolean IROUGE = false;
}