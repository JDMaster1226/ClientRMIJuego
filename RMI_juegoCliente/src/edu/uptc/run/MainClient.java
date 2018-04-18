		package edu.uptc.run;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import edu.uptc.conexion.Conexion;
import edu.uptc.remote.IMethods;

public class MainClient {
	private int turno;
	
	IMethods remoteMethods;
	ArrayList<String[]> p;
	
	public void dibujar() throws RemoteException {
		int matriz[][]=remoteMethods.getMatriz();	
//		for (int i = 0; i < p.size(); i++) {
//			for (int j = 0; j < p.get(i).length; j++) {
//				
//				matriz[Integer.parseInt(p.get(i)[j].substring(0, p.get(i)[j].indexOf(",")))][Integer.parseInt(p.get(i)[j].substring(p.get(i)[j].indexOf(",")+1))]=2;
//			}
//		}
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(matriz[i][j]+" ");
			}
			System.out.println("");
		}
		System.out.println();
		
	}
	
	public void run() throws RemoteException {
		Conexion conexion = new Conexion();
		Scanner sc=new Scanner(System.in);
		remoteMethods= conexion.searchServer(sc.nextLine());
			if (remoteMethods != null) {
				//LOGIN
				
				System.out.println("nombre y contraseña :...");
				String nombre=sc.nextLine();
				String contr=sc.nextLine();
				
				
				System.out.println("user add :"+remoteMethods.addUser(nombre,contr));
				System.out.println("login :"+remoteMethods.login(nombre,contr));
				
				
				remoteMethods.crearBarcos(nombre);
				//posisiones de los barcos
				p=new ArrayList<>();
				for (int i = 0; i < 5; i++) {
					p.add(new String[i+1]);
					for (int j = 0; j < i+1; j++) {
						p.get(i)[j]=i+","+j;
					}
					remoteMethods.ponerBarco(nombre, i+"", p.get(i));
				}
				//indicacion de preparado
				turno=remoteMethods.listo();
				
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
				int t=1;
				while(t>0) {
					t=remoteMethods.getTurno();
					
					if(turno==t) {
						dibujar();
						int dis=remoteMethods.disparo(sc.nextLine());
						if(dis==0) {
							System.out.println("le di");
						}else if(dis==-1) {
							System.out.println("paila");
						}else {
							System.out.println("ya habia usado eso");
						}
						
						remoteMethods.turno();
						dibujar();
					}	
					else {
						System.out.println("esperando otro jugador");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				System.out.println("fin");
				if(remoteMethods.estasVivo(nombre)) {
					System.out.println("ganaste");
				}else {
					System.out.println("perdiste");
				}
				
			}else{
				System.out.println("Problemas con la conexion");
			}
		
	}

	public static void main(String[] args) {
		try {
			new MainClient().run();
		} catch (RemoteException e) {
			System.out.println("problema con los metodos remotos");
			e.printStackTrace();
		}
		
	}

}
