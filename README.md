# IUT wiimote nxt

Ce README a été extrait de [readme.pdf](readme.pdf) avec la lib http://pdf2md.morethan.io/


# Auteurs

+ Timothée Gauthier
+ Thomas Huot-Marchand

Utilisation d'une Wiimote pour contrôler une brique NXT à travers internet

# Tuteur

+ Mr Millet

## Année 2009


**Présentation**

Notre projet consiste à développer une application afin de commander un robot fabriqué en brique Lego Nxt en passant par internet avec une Wiimote. Le langage de programmation utilisé est le JAVA.


## Table des matières

- I Pré-requis
- II Démarrage rapide
- III Choix du langage
- IV Détail du programme
   - 1 Librairies utilisées dans le programme
   - 2 Description des différentes classes
      - a) Start.class
      - b) Choix.class
      - c) Serveur_Brick.class
      - d) Fenetre.class
      - e) Ir.class
      - f) Wiimote01.class
         - Connexion
         - Réception et interprétation de l'Infrarouge
         - Réception et interprétation des accéléromètres
         - interprétation des boutons
         - Limitation du nombre de commandes
      - g) Client.class
      - h) Constantes.class
      - i) Brick.nxj
- V Description des commandes de la Wiimote
- VI Le Streaming
   - 1 Configuration pour la diffusion
   - 2 Configuration pour la réception
- VII Améliorations possibles
- VIII Truc et Astuces


## I Pré-requis

+ Un robot en brique NXT dont le modèle de construction est disponible à cette adresse:
+ http://nxtprograms.com/3-motor_chassis/index.html
+ Le firmware LEJOS beta 0.8.5 remplaçant le firmware original de LEGO
+ Une webcam relié à l'ordinateur coté brique NXT
+ Deux PCs avec le Bluetooth et connexion réseaux
+ Une Wiimote
+ Java JRE, VLC, Bluez (linux), WIDCOMM ou BLUESOLEIL (Windows)
+ Illustration 1 : Modèle de la brique NXT du projet


## II Démarrage rapide..

+ 1) Allumer la brique et lancer le programme brick.nxj
+ 2) Lancer Wiimote_internet_NXT.jar sur le pc côté brique et choisir « côté brique »
+ 3) Lancer VLC sur les deux ordinateurs (voir partie streaming pour la diffusion)
+ 4) Lancer Wiimote_internet_NXT.jar sur le pc côté Wiimote et choisir « côté Wiimote »,
+ attention à bien choisir le même port sur les 2 PCs et à bien inscrire l'adresse ip du serveur.
+ 5) Connecter la Wiimote en appuyant sur 1 et 2.

## III Choix du langage.

L'intégralité du programme permettant de faire communiquer les deux appareils a été écrit en JAVA

pour plusieurs raisons :

- Une seule et même archive fonctionnant sur les OS les plus répandus

- Il permet de créer facilement des programmes client-serveur

- La librairie Bluecove(*) permet de faire fonctionner le bluetooth de façon fiable

- WiiRemoteJ.jar est une excellente librairie pour communiquer avec la wiimote

(*) Pour que la librairie Bluecove puisse fonctionner les ordinateurs ont besoin des programmes

suivant :

- Linux : Bluez (disponible dans les dépôts)

- Windows: Bluesoleil (déconseillé) ou Widcomm


## IV Détail du programme :.

Le programme est composé de 8 classes:

- Start.class : permet de lancer le programme

- Constantes.class : donne les codes pour faire bouger la brique

- Choix.class: permet de spécifier les ports d'écoutes et de choisir si l'on veut lancer le

programme côté Wiimote ou côté brique

- Serveur_Brick.class : gère le flux de données entre la brique et le pc

- Fenetre.class : affiche les informations de communication entre le pc et la Wiimote

- Client.class : envoie les données sur internet, initialise la connexion et réserve les ports.

- Ir.class: permet de visualiser l'emplacement du point infrarouge.

- Wiimote01.class : reçois et interprète les données de la Wiimote, permet aussi d'envoyer

certains ordres (vibrations, LED, son)

### 1 Librairies utilisées dans le programme:

- com.intel.bluetooth.BlueCoveConfigProperties; permet d'initialiser le bluetooth et d'utiliser bluecove.jar qui dialogue avec la pile bluetooth déjà
- installée.
- java.net.*; permet de gérer les accès réseaux.
- wiiremotej.*; permet de dialoguer avec la Wiimote
- wiiremotej.event.* ; déclenche les événements de la Wiimote.
- javax.swing.*; permet d'afficher des interfaces graphiques.
- java.awt.*; utilisée pour dessiner le Jpanel de l'infrarouge.
- java.io.*; permet de gérer les entrées-sorties (accès fichiers, gestion de répertoires,...)
- lejos.pc.comm.*; pour communiquer par bluetooth entre la brique et le pc
- java.util.date*; permet de récupérer le temps (en ms) pour ne pas exécuter deux commandes en même temps.


### 2 Description des différentes classes

#### a) Start.class

C'est la class de démarrage, elle permet, une fois le choix effectué d'initialiser la classe Client

Fenetre et Wiimote01 ou seulement Serveur_Brick.

#### b) Choix.class..

Contient une interface graphique permettant de sélectionner le rôle du pc: s'il sera le pc connecté à

la brique ou bien celui connecté à la Wiimote.

Si l'ordinateur est connecté à la Wiimote, il faut régler le port et l'adresse Ip d'envoie au serveur.

Si l'ordinateur est connecté du côté de la brique, il faut régler le port d'écoute.

L'adresse ip et le port doivent donc être identique côté brique et côté Wiimote.

Ensuite il faut cliquer sur démarrer, la visualisation du serveur se fait dans un terminal

(il faut donc exécuter le programme comme cela : java -jar Wiimote_internet_NXT.jar)

Pour le programme qui gère la Wiimote, une fenêtre de contrôle s'ouvre.

Illustration 2 : Fenêtre principale obtenue au lancement du

programme, il faut sélectionner la fonction de l'ordinateur

Illustration 3 : côté Wiimote sélectionné Illustration 4 : côté Brique NXT sélectionné


#### c) Serveur_Brick.class

Description de la classe

Initialisation :

- du port d'écoute

- rechercher et se connecter à la brique

- les flux bluetooth entrants et sortants

Fonctionnement :

Le thread attend l'arrivé d'une trame, si c'est un ordre compréhensible, il l'envoie en bluetooth sinon

il envoie la commandes « Stop ».

la méthode « conversion » permet de traduire les ordres chiffrés en mots clair.


#### d) Fenetre.class

Description de la classe

Cette classe permet d'avoir une vue des données reçues par la wiimote ainsi que les actions envoyés

sur internet.

Elle charge la classe « Ir » qui permet d'avoir un Jpanel montrant l'emplacement de la lumière

infrarouge reçue par la Wiimote.

Fonctionnement :

Attend les actions de la wiimote ou les actions des boutons symbolisant des touches directionnelles.

Méthodes :

void set_l_info(String s) Ajoute automatiquement le texte en paramètre dans le

TextArea en incrémentant le repère et en déplaçant le

curseur au début.

void set_l_acc_XYZ(int x, int y, int z) Actualise les informations des labels de la partie

« accéléromètre »

void set_l_acc_action(String s) Actualise les « actions » déclenché par l'accéléromètre

void set_l_ir_xy(int x, int y) Actualise les informations X et Y des labels de la partie

«infrarouge»

void set_l_ir_action(String s) Actualise les « actions » déclenché par la position de

l'infrarouge.

void affichePointIR(double x, double y) Envoie les nouvelles coordonnées du point infrarouges

puis redessine le JPanel

mousePressed (MouseEvent e) Déclenche les actions relatives aux flèches

mouseReleased (MouseEvent e) Envoie l'ordre « STOP » à la brique

Illustration 5 : Fenêtre obtenue lorsque l'ordinateur est connecté à la Wiimote


#### e) Ir.class...

JPanel affichant l'emplacement de la source infrarouge par rapport à la wiimote. S'adapte en

fonction de la taille de la fenêtre.

Les cadres représentants les déclenchements des actions sont dessinés d'après les valeurs utilisés

dans la classe Wiimote01.

#### f) Wiimote01.class

Classe principale permettant la connexion à la wiimote, la récupération des évènements (appuie sur

les boutons), la position des accéléromètres et la positions des sources infrarouges.

Fonctionnement de la classe :

Connexion

connecter_wiimote() :

Affiche les commandes de la wiimote

recherche d'une wiimote

si aucune wiimote n'est trouvé, on recommence la recherche

si connectée, elle vibre et on initialise les données qu'elle devra envoyer en bluetooth

wiimote.addWiiRemoteListener (new WiimoteListener ()) ; envoie les boutons pressés

wiimote.setIRSensorEnabled (true, WRIREvent.BASIC) ; active la réception infrarouge. BASIC

signifie qu'elle envoie uniquement la position des sources. (il existe d'autres modes pour récupérer

l'intensité de la luminosité).

wiimote.setAccelerometerEnabled (true) ; envoie l'état des accéléromètres.

Réception et interprétation de l'Infrarouge...

void IRInputReceived(WRIREvent ire); c'est grâce à cette méthode qu'est traité la réception de la

source infrarouge :

on ne teste que la source 0 (possibilité de gérer jusqu'à 4 sources)

La position est multiplié par 1000 pour être plus facilement exploitable. Donc les valeurs vont de 0

à 1000.

Pour la source donnant la largeur (irX) on fait 1000-positionSourceX pour inverser le sens de

déplacement.

Ces deux valeurs sont envoyé à la fenêtre de contrôle qui met à jour les informations et redessine le

JPanel

Pour déterminer la commande à envoyer (avancer reculer gauche droite ou stop) on analyse la

position de la source :

Y compris entre 0 et 151 exécute « haut »

Y compris entre 850 et 1000 exécute « bas »

X compris entre 0 et 151 exécute « Gauche »

X compris entre 850 et 1000 exécute « Droite »


Si X ou Y est compris entre 250 et 750 exécute « STOP »

Réception et interprétation des accéléromètres

On récupère l'accélération en X, Y et Z que l'on convertit en un Integer allant de -100 à 100, placé

dans la variable etat[].

Seul les états de X et Y sont testé pour déclencher les mouvements car la brique évolue sur un plan,

donc en 2D.

Y supérieur à 60 exécute « haut »

Y inférieur à -60 exécute « bas »

X supérieur à 70 exécute « Gauche »

X inférieur à -70 exécute « Droite »

Pour chaque commande, on met une variable sur "true" pour signaler qu'il est inutile de relancer

cette commande à la prochaine détection (qui est très rapide) tant que l'on n'est pas passé par STOP

Si X ou Y est compris entre -40 et 40 exécute on « STOP » et on réinitialise les variables sur false.

Pour l'interprétation des mouvements par accéléromètre ou infrarouge, les valeurs ont été prise

arbitrairements, vous pouvez régler la sensibilité en changeant ces valeurs

interprétation des boutons.

Il retient deux état : lorsque le bouton est pressé et relâché.

Pour la croix directionnelle :

Pressé : exécute la commande en fonction de la flèche

Relâché : exécute la commande stop

Bouton A : bouton de secours : envoie la commande STOP et stoppe les vibrations de la

wiimote sans tester si elles sont activées ou non.

Bouton 1 : bascule entre le mode accéléromètre ou infrarouge en modifiant la variable mode.

Bouton 2 : Active / désactive les vibrations en modifiant la variable « vibrations »

Bouton + et - : Permettent de définir le temps d'attente obligatoire entre 2 commandes. En effet, si

on secoue la wiimote ou si plusieurs sources infrarouges sont visibles ou si plusieurs touches sur la

croix directionnelle sont enfoncées, le nombre de commande généré peut être très grand dans un

laps de temps très court. Et si les trames UDP peuvent suivre selon le débit, le bluetooth du coté de

la brique NXT ne seras pas assez rapide.

Le temps entre deux commandes est définis en milliseconde par la variable temps_blocage.


Limitation du nombre de commandes..

Avant chaque exécution de commande (haut(), bas(), gauche(), droite()) on fait appel à la méthode

is_ok(). Cette méthode compare le temps écoulé entre 2 commandes (autre que STOP) et renvoie

« true » si le temps est supérieur à temps_blocage, « false » sinon.

En plus de la méthode is_ok() il y a la méthode « vibre() » qui teste si les vibrations sont activée et

réalise la même chose que is_ok() mais avec les vibrations.

Cette fonction n'est pas bloquante : 2 commandes peuvent être envoyées rapidement et validé par

is_ok(), mais il est possible que la wiimote ne vibre pas à chaque commande.

Cette fonction a été rajoutée car de trop nombreuses vibrations ralentissaient fortement le

programme.

disconnected() est déclenché lorsque la wiimote s'éteint (volontairement, ou non). Elle permet de

relancer la méthode connecter_wiimote() donc de connecter une nouvelle wiimote.

#### g) Client.class

Initialise et envoie les ordres dans des trames UDP en fonction du port et de l'adresse ip enregistré

dans la première fenêtre du programme.

#### h) Constantes.class

Interface permettant de simplifier l'envoie des codes des commandes et le choix du mode entre

accéléromètre ou infrarouge.

#### i) Brick.nxj

C'est le programme présent dans la brique NXT.

Fonctionnement :

Initialise sa connexion bluetooth et attend indéfiniment qu'un périphérique s'y connecte. Une fois

connecté il entre dans une boucle testant « p ». p est le message reçu par bluetooth. Si p n'est pas un

integer ou s'il y a un problème avec la connexion, p vaut « DECO » la brique réinitialise sa pile

Bluetooth et recommence le programme depuis le début. Si p est un integer et p connus, les

commandes sont traduites en rotations de moteurs.

Il implémente le même fichier « Constantes » afin de traduire facilement les ordres.


## V Description des commandes de la Wiimote

Note: Plus le délai entre 2 informations transmises est petit, plus la transmission par bluetooth est

difficile car lente.

Au maximum, le temps de latence entre 2 trames est de 1seconde et au minimum, de 100ms.


## VI Le Streaming

Pour pouvoir voir les déplacements du robot à travers internet, il faut qu'une caméra soit fixée

dessus, renvoie des images et que le pc côté Wiimote reçoive ces images. Pour la diffusion et la

réception, nous utilisons vlc média player.

### 1 Configuration pour la diffusion:..

Média => Diffusion

Sélectionner l'onglet " périphérique de capture "

Mode de capture: DirectShow

Nom du périphérique vidéo: sélectionner la webcam branchée sur le robot

Cliquer sur Diffuser, puis suivant

Dans destination choisir HTTP puis ajouter

Entrer l'adresse IP du serveur et le port 80.

Dans option de transcodage cocher " activer le transcodage " et choisir le profil " video-DIV3 +

MP3 (ASF)

Cliquer sur suivant et cocher " garder le flux de sortie actif ".

Puis diffuser

### 2 Configuration pour la réception:..

Média => Ouvrir un flux réseau...

sélectionner protocole HTTP

Dans le champ adresse entrer l'adresse IP du serveur et le port utilisé, en HTTP c'est le 80. Par

exemple 127.0.0.1:

## VII Améliorations possibles:..

Le programme peut être amélioré de différentes façons :

- Trouver un codec plus approprié pour le streaming

- Feedback de la brique

- Visualisation du parcours de la brique

- Utiliser le haut-parleur de la Wiimote

- Inclure la vidéo dans le programme Java

- Utiliser les Leds

- Mettre un pivot sur la webcam

- Webcam sans fil

- Sécuriser la connexion, et la passer en TCP


## VIII Truc et Astuces

Compilation :

Pour le lancer en ligne de commande :

```
java -classpath .:./lib/* Start
```
Bluecove ne tourne pas sur certains systèmes 64 bit, il est alors utile de rajouter -d

```
java -d32 -classpath .:./lib/* Start
```
Pour lancer Wiimote_internet_NXT.jar :

```
java -jar [-d32] Wiimote_internet_NXT.jar
```
N'oubliez pas de mettre le dossier « lib » dans le même répertoire que le jar

Pour compiler en ligne de commande le programme principal :

```
javac -classpath .:. / lib /*. / Start.java
```
Afin de réaliser la compilation et le lancement facilement il est conseillé de créer ce fichier bash et

de l'appeler avec le nom de votre classe qui comporte le Main :

rm *.class

javac -cp .:./lib/* $1.java && echo && echo lancement de $1 && java -cp .:./lib/* $

Pour créer le jar en ligne de commande :

```
jar cmvf manifest.txt Wiimote_internet_NXT.jar images/* *.class
```
Le fichier manifest.txt doit comporter ceci :

Main-Class: Start

Class-Path: lib/bluecove-2.1.0.jar lib/bluecove-gpl-2.1.0.jar lib/classes.jar lib/jtools.jar

lib/pccomm.jar lib/pctools.jar lib/WiiRemoteJ.jar

Si la Wiimote semble connecté en bluetooth mais pas au programme, allez dans les paramètres

bluetooth de votre ordinateur et faite cliquez sur déconnecter Nintendo RVL-CNT. Appuyez de

nouveau sur 1 + 2 sans relancer le programme.

La touche A de la wiimote permet d'envoyer la trame « stop » à la brique et de stopper les vibrations

de la wiimote coute que coute.

Pour compiler pour la Brique NXT (Lejos doit être installé et configuré):

nxjc Brick.java

nxjlink -v Brick -o Brick.nxj

Pour l'envoyer à la brique nxt (bluetooth ou usb):

nxjupload Brick.nxj

La brique NXT doit avoir été synchronisée avec l'ordinateur avant le lancement du programme.
