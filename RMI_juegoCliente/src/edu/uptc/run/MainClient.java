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
				Scanner sc=new Scanner(System.in);
				System.out.println("nombre y contraseña :...");
				String nombre=sc.nextLine();
				String contr=sc.nextLine();
				System.out.println("user add :"+remoteMethods.addUser(nombre,contr));
				System.out.println("login :"+remoteMethods.login(nombre,contr));
				remoteMethods.crearBarcos(nombre);
				//posisiones de los barcos
				ArrayList<String[]> p=new ArrayList<>();
				for (int i = 0; i < 5; i++) {
					p.add(new String[i+1]);
					for (int j = 0; j < i+1; j++) {
						p.get(i)[j]=i+","+j;
					}
					remoteMethods.ponerBarco(nombre, i+"", p.get(i));
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
				
				System.out.println(turno);
				//juego
				while(true) {
					
					
					if(turno==remoteMethods.getTurno()) {
						int matriz[][]=remoteMethods.getMatriz();
						for (int i = 0; i < 15; i++) {
							for (int j = 0; j < 15; j++) {
								System.out.print(matriz[i][j]+" ");
							}
							System.out.println("");
						}
						int dis=remoteMethods.disparo(sc.nextLine());
						if(dis==0) {
							System.out.println("le di");
						}else if(dis==-1) {
							System.out.println("paila");
						}else {
							System.out.println("ya habia usado eso");
						}
						
						remoteMethods.turno();
						matriz=remoteMethods.getMatriz();
						for (int i = 0; i < 15; i++) {
							for (int j = 0; j < 15; j++) {
								System.out.print(matriz[i][j]+" ");
							}
							System.out.println("");
						}
					}else {
						System.out.println("esperando otro jugador");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
