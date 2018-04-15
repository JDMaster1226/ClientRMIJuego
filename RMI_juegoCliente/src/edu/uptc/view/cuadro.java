package edu.uptc.view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class cuadro extends JPanel{

	private int tamano;	
	public cuadro(int tamano) {
//		setIcon(new ImageIcon("C:\\Users\\hp\\Desktop\\Ivonne\\eclipse-workspace\\JuegoBN\\img\\rejilla.png"));
		this.tamano = tamano;		
		setLayout(null);
		setBackground(Color.YELLOW);	
		tamano(tamano);

	}
	
	public int getTamano() {
		return tamano;
	}
	private void tamano (int n) {	
		int tamano =0;
		if(n==1) {
			tamano = n*33;			
		}else {
			tamano = ((n-1)*3)+(n*33);
		}
		this.setSize(tamano,33);
	}
	
	
}
