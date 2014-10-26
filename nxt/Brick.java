import lejos.nxt.*;
import lejos.nxt.comm.*;
import java.io.*;

public class Brick implements Constantes {

	public static void main(String [] args){
		while (true)
		{
			System.out.println("attente");
			BTConnection btc = Bluetooth.waitForConnection();

			System.out.println("connecte");
			DataInputStream dis = btc.openDataInputStream();
			DataOutputStream dos = btc.openDataOutputStream();

			int p = 0;
			while(p != DECO){
				try {
					p = dis.readInt();
				}
				catch (IOException ioe){
					p = DECO;
				}
				switch (p)
				{
					case UP:
					System.out.println(UP_S);
					Motor.A.backward();
					Motor.B.backward();
					break;

					case DOWN: 
					System.out.println(DOWN_S);
					Motor.A.forward();
					Motor.B.forward();
					break;

					case LEFT:
					System.out.println(LEFT_S);
					Motor.A.backward();
					Motor.B.forward();
					break;  

					case RIGHT:
					System.out.println(RIGHT_S);
					Motor.A.forward();
					Motor.B.backward();
					break;

					case STOP:
					System.out.println(STOP_S);
					Motor.A.stop();
					Motor.B.stop();
					break;

					default:
					System.out.println("" + p);
					Motor.A.stop();
					Motor.B.stop();
				}
			}

			try {
				dis.close();
				dos.close();
			}
			catch (IOException ioe){
				System.out.println("Impossible de deconnecter proprement");
			}
			try {
				Thread.sleep(300); // wait for data to drain
			}catch (Exception ioe){}
			
			System.out.println(DECO_S);
			btc.close();
		}
	}
}

