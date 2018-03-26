		package edu.uptc.run;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import edu.uptc.conexion.Conexion;
import edu.uptc.remote.IMethods;

public class MainClient {

	public static void main(String[] args) {
		MainClient mainClient = new MainClient();
		try {
			Conexion conexion = new Conexion();
			IMethods remoteMethods= conexion.searchServer();
			if (remoteMethods != null) {
				//LOGIN
				System.out.println("user add :"+remoteMethods.addUser("juan", "jua123"));
				remoteMethods.saludo("juan");
				System.out.println("login :"+remoteMethods.login("juan", "jua123"));
				remoteMethods.crearBarcos("juan");
				//posisiones de los barcos
				ArrayList<String[]> p=new ArrayList<>();
				for (int i = 0; i < 5; i++) {
					p.add(new String[i+1]);
					for (int j = 0; j < i+1; j++) {
						p.get(i)[j]=i+","+j;
					}
					remoteMethods.ponerBarco("juan", i+"", p.get(i));
				}
				//indicacion de preparado
				int turno=remoteMethods.listo();
				
				//espera de otros jugadores
				while (!remoteMethods.isJuegoIniciado()) {
					System.out.println("esperando otros jugadores");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				Scanner sc=new Scanner(System.in);
				
				//juego
				while(true) {
					int matriz[][]=remoteMethods.getMatriz();
					for (int i = 0; i < 15; i++) {
						for (int j = 0; j < 15; j++) {
							System.out.print(matriz[i][j]+" ");
						}
						System.out.println("");
					}
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(turno==remoteMethods.turno()) {
						int dis=remoteMethods.disparo(sc.nextLine());
						if(dis==0) {
							System.out.println("le di");
						}else if(dis==-1) {
							System.out.println("paila");
						}else {
							System.out.println("ya habia usado eso");
						}
					}else {
						System.out.println("esperando otro jugador");
					}
					
				}
				
			}else{
				System.out.println("Problemas con la conexion");
			}
		}catch (RemoteException e) {
			System.out.println("Problema con los metodos remotos");
		}
	}

}
