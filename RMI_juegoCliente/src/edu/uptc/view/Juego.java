package edu.uptc.view;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.uptc.conexion.Conexion;
import edu.uptc.remote.IMethods;


public class Juego extends JFrame implements ActionListener, MouseMotionListener ,MouseListener{

	JLabel lblFondo;
	JButton btnNuevo;
	JButton btnListo;
	JButton btnRandom;
	JLabel lblRejilla;
	JLabel lblT;
	ArrayList<Barco> barcos;
	boolean movimientoBarcos;
	private int turno;

	private IMethods remoteMethods;
	ArrayList<String[]> p;
	private String nombre;
	private JPanel cuadro;
	private int countTurnos;
	private boolean entroTurno;
	private Thread hilo;

	public Juego() {

		setBackground(SystemColor.activeCaption);
		getContentPane().setLayout(null);
		setSize(900, 635);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseMotionListener(this);
		addMouseListener(this);		

		beginComponents();
		addComponents();

		hilo=new Thread(new Runnable() {

			@Override
			public void run() {
				int t=1;
				try {
					while (!remoteMethods.isJuegoIniciado()) {
						System.out.println("esperando otros jugadores");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					while(t>0) {
						t=remoteMethods.getTurno();
						if(t==turno) {
							lblFondo.setIcon(new ImageIcon("img/agua.jpg"));
							dibujar();
						}else {
							lblFondo.setIcon(new ImageIcon("img/aguaEspera.jpg"));
						}
						Thread.sleep(3000);
					}
					if(remoteMethods.estasVivo(nombre)) {
						JOptionPane.showMessageDialog(null, "Ganaste");
					}else {
						JOptionPane.showMessageDialog(null, "perdiste");
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void beginComponents() {

		turno=-5;
		countTurnos=0;
		entroTurno=true;
		lblFondo= new JLabel("");
		lblFondo.setSize(this.getWidth(), this.getHeight());	
		lblFondo.setIcon(new ImageIcon("img/agua.jpg"));
		movimientoBarcos=true;
		barcos = new ArrayList<>();

		for (int i = 0; i < 6; i++) {

			barcos.add(new Barco(i+1));
			barcos.get(i).setLocation(650, (100+(i*40)));
			barcos.get(i).setVisible(false);

		}			
		cuadro=new JPanel();
		cuadro.setSize(33,33);
		cuadro.setBackground(Color.GRAY);
		cuadro.setLocation(-33, -33);

		btnNuevo = new JButton();
		btnNuevo.setBounds(670, 10, 100, 40);		
		btnNuevo.setText("Nuevo");
		btnNuevo.addActionListener(this);

		btnListo = new JButton("Listoo");
		btnListo.setEnabled(false);
		btnListo.setBounds(702, 435, 89, 23);
		btnListo.addActionListener(this);
		btnListo.setVisible(false);

		btnRandom = new JButton("Random");
		btnRandom.setBounds(702, 401, 89, 23);
		btnRandom.addActionListener(this);
		btnRandom.setVisible(false);

		lblT = new JLabel("Click derecho para girar!");
		lblT.setBounds(650, 65, 173, 29);

		lblRejilla = new JLabel("");
		lblRejilla.setIcon(new ImageIcon("img/rejilla.png"));
		lblRejilla.setBounds(10, 10, 578, 578);
		lblRejilla.setHorizontalAlignment(JLabel.LEFT);
		lblRejilla.setVerticalAlignment(JLabel.TOP);		
		lblRejilla.setVisible(false);	
	}

	public void addComponents() {
		for (int i = 0; i < barcos.size(); i++) {
			getContentPane().add(barcos.get(i));
		}
		add(cuadro);
		getContentPane().add(btnRandom);		
		getContentPane().add(btnListo);
		getContentPane().add(btnNuevo);
		getContentPane().add(btnNuevo);
		getContentPane().add(lblT);
		getContentPane().add(lblRejilla);
		getContentPane().add(lblFondo);

	}

	private void posRandom() {
		Barco barco;
		int count =0;
		while(count<barcos.size()) {

			barco = barcos.get(count);			
			int x = ((int) (Math.random() * 16)*36)+13;
			int y = ((int) (Math.random() * 16)*36)+13;					
			int al= (int) (Math.random() * 2);
			if (al==1) {
				barco.vertical();
			}
			llenarBarco(barco, x, y);
			System.out.println(count+"="+x+","+y+"   al="+al+" barc="+barco.isHorizontal());
			if(!isChoque(barco) && (x+barco.getAncho())<589 && (y+barco.getAlto())<589) {
				barco.setLocation(x, y);
				count++;
			}
		}

	}

	public void actionPerformed( ActionEvent e ) {

		if(e.getSource()==btnNuevo) {
			Barco barco;
			for (int i = 0; i < barcos.size(); i++) {
				barco = barcos.get(i);	
				barco.setLocation(650, (100+(i*40)));
				barco.llenarArreglo();
				barco.setVisible(true);	
				if(!barco.isHorizontal()) {
					barco.vertical();
				}
			}

			lblRejilla.setVisible(true);
			btnRandom.setVisible(true);
			btnListo.setVisible(true);

		}if(e.getSource()==btnRandom) {
			posRandom();
			btnListo.setEnabled(true);

		}if(e.getSource()==btnListo) {
			try {
				movimientoBarcos=false;
				btnRandom.setEnabled(false);
				btnListo.setEnabled(false);
				for (int i = 0; i < 6; i++) {
					remoteMethods.ponerBarco(nombre, (i+1)+"", barcos.get(i).getPosiciones());
				}
				turno=remoteMethods.listo();
				hilo.start();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}                                        

	@Override
	public void mouseDragged(MouseEvent e) {
		if(movimientoBarcos) {
			Barco barco;
			if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
				for (int i = 0; i < barcos.size(); i++) {
					barco = barcos.get(i);		
					if(barco.isAdentro()) {
						barco.setLocation(e.getX()-25,e.getY()-50);
						lblT.setText("Posicion: x("+barco.getX()+") y("+barco.getY()+")");					
					}
				}

			}
		}

	}
	@Override
	public void mouseMoved(MouseEvent e) {
		try {
			remove(lblFondo);
			if(!movimientoBarcos && turno==remoteMethods.getTurno()) {
				int auxX=e.getX();
				int auxY=e.getY();
				cuadro.setLocation(auxX, auxY);
			}else {

				cuadro.setLocation(-33, -33);
			}
			add(lblFondo);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void espera() {


	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(movimientoBarcos) {
			Barco barco;
			for (int i = 0; i < barcos.size(); i++) {

				barco = barcos.get(i);

				if(new Rectangle(barco.getX(), barco.getY()+33, barco.getWidth()-10, barco.getHeight()-10).contains(e.getPoint())) {
					barco.setAdentro(true);	
					if(e.getModifiersEx()==MouseEvent.BUTTON3_DOWN_MASK) {
						barco.vertical();		
					}
				}			
			}				
		} else
			try {
				if(!remoteMethods.isJuegoIniciado()) {
					JOptionPane.showMessageDialog(null, "esperando otros jugadores...");
				}else {
					int t=remoteMethods.getTurno();
					if(turno==t) {
						//dibujar();

						if(e.getX()>=13 && e.getX()<=589 && e.getY()>=13 && e.getY()<=589) {
							int auxX=e.getX();
							int auxY=e.getY();
							boolean interar=true;
							int cont =13;
							while(interar && cont<590) {

								if(auxX<cont) {
									auxX=cont-36;
									interar=false;
								}
								cont+=36;
							}
							cont =13;
							interar=true;
							while(interar && cont<590) {
								if(auxY<cont) {
									auxY=cont-36;
									interar=false;
								}
								cont+=36;
							}
							int posX = (auxX-13)/36;
							int posY = (auxY-13)/36;
							int dis=remoteMethods.disparo(posX+","+posY);
							if(dis==0) {
								System.out.println("le di");
								disparo(Color.red, auxX, auxY);
								//remoteMethods.turno();
							}else if(dis==-1) {
								System.out.println("paila");
								disparo(Color.GRAY, auxX, auxY);
								entroTurno=true;
								remoteMethods.turno();
							}else {
								System.out.println("ya habia usado eso");
							}
						}
						//dibujar();
					}	
					else {
						JOptionPane.showMessageDialog(null, "esperando otro jugador");
						System.out.println("esperando otro jugador");
					}
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

	public void dibujar() throws RemoteException {
		int matriz[][]=remoteMethods.getMatriz();	
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if(matriz[i][j]!=0) {
					int x=(i*36)+13;
					int y=(j*36)+13;
					System.out.println(x+","+y+"=="+i+","+j);
					if(matriz[i][j]==1) {
						disparo(Color.GRAY, x, y);
					}else if(matriz[i][j]==8) {
						disparo(Color.RED, x, y);
					}
				}
			}
		}
	}

	public void disparo(Color c , int x, int y) {
		remove(lblFondo);
		JPanel j=new JPanel();
		add(j);
		j.setSize(33,33);
		j.setBackground(c);
		j.setLocation(x, y);
		add(lblFondo);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(movimientoBarcos) {
			Barco barco;
			int barcosPuestos=0;
			for (int i = 0; i < barcos.size(); i++) {
				barco = barcos.get(i);
				barco.setAdentro(false);

				if(barco.getX() < 13 || (barco.getX()+barco.getAncho()) > 589 || barco.getY() < 13 || (barco.getY()+barco.getAlto()) > 589 ) {
					barco.setLocation(650, (100+(i*40)));
					barco.llenarArreglo();
					if(!barco.isHorizontal()) {
						barco.vertical();
					}
				}else {
					int auxX=barco.getX();
					int auxY=barco.getY();
					boolean interar=true;
					int cont =13;
					while(interar && cont<590) {

						if(auxX<cont) {
							auxX=cont-36;
							interar=false;
						}
						cont+=36;
					}
					cont =13;
					interar=true;
					while(interar && cont<590) {
						if(auxY<cont) {
							auxY=cont-36;
							interar=false;
						}
						cont+=36;
					}			

					llenarBarco(barco,auxX,auxY);

					if(isChoque(barco)) {
						barco.setLocation(650, (100+(i*40)));
						barco.llenarArreglo();
						if(!barco.isHorizontal()) {
							barco.vertical();
						}
					}else {
						barcosPuestos++;
						barco.setLocation(auxX, auxY);
					}

				}
			}
			if (barcosPuestos==6) {
				btnListo.setEnabled(true);
			}else {
				btnListo.setEnabled(false);
			}
		}
	}


	public void llenarBarco(Barco barco ,int x ,int y) {
		int posX = (x-13)/36;
		int posY = (y-13)/36;
		lblT.setText(x+"pop"+y);
		for (int j = 0; j < barco.getPiezas(); j++) {
			barco.agregarPos(posX+","+posY, j);
			if(barco.isHorizontal()) {
				posX++;
			}else {
				posY++;
			}
		}
	}

	public boolean isChoque(Barco barco) {
		int ciclo=0;
		boolean choque=false;
		for (int j = 0; j < barcos.size(); j++) {
			for (int j2 = 0; j2 < barcos.get(j).getPosiciones().length; j2++) {
				int count=0;
				while(count<barco.getPosiciones().length && ciclo==0 && !barco.equals(barcos.get(j))){
					if(barcos.get(j).getPosiciones()[j2].equals(barco.getPosiciones()[count])) {
						choque=true;
						ciclo++;
					}
					count++;
				}

			}
		}
		return choque;
	}

	public void run() throws RemoteException {
		Conexion conexion = new Conexion();
		remoteMethods = conexion.searchServer();

		if (remoteMethods != null) {
			Scanner sc=new Scanner(System.in);
			System.out.println("nombre y contraseña :...");
			nombre=sc.nextLine();
			String contr=sc.nextLine();				
			System.out.println("user add :"+remoteMethods.addUser(nombre,contr));
			System.out.println("login :"+remoteMethods.login(nombre,contr));

			remoteMethods.crearBarcos(nombre);
			setVisible(true);
		}
	}

	public static void main(String args[]) {
		try {
			new Juego().run();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
