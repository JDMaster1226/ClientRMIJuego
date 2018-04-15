package edu.uptc.view;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Barco extends JLabel{

	private int piezas;
	private int alto;
	private int ancho;
	private boolean adentro;
	private boolean horizontal;
	private String [] posiciones;

	public Barco(int piezas) {
		this.piezas = piezas;		
		setLayout(null);
		setIcon(new ImageIcon("img/barco"+piezas+"H.png"));	
		adentro=false;
		horizontal=true;
		posiciones = new String [piezas];
		llenarArreglo();
		tamano(piezas);
		setSize(ancho, alto);
	}

	public void vertical () {
		int al = alto;
		int an = ancho;		
		setBounds(getX(), getY(), al, an);
		if (horizontal) {
			horizontal=false;
			setAncho(al);
			setAlto(an);
			setIcon(new ImageIcon("img/barco"+piezas+"V.png"));
		}else {
			horizontal=true;
			setAncho(al);
			setAlto(an);
			setIcon(new ImageIcon("img/barco"+piezas+"H.png"));
		}			
	}

	private void tamano (int piezas) {	
		alto = 33;
		if(piezas==1) {
			ancho = piezas*alto;			
		}else {
			ancho = ((piezas-1)*3)+(piezas*alto);
		}
	}
	public void llenarArreglo() {
		for (int i = 0; i < piezas; i++) {
			posiciones [i]="-1";
		}
	}
	public void agregarPos(String posicion, int n) {
		posiciones [n]=posicion;
	}

	public boolean isAdentro() {
		return adentro;
	}		

	public int getPiezas() {
		return piezas;
	}

	public void setAdentro(boolean adentro) {
		this.adentro = adentro;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public String[] getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(String[] posiciones) {
		this.posiciones = posiciones;
	}

}
