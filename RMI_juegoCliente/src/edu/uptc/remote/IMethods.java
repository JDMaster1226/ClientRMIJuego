package edu.uptc.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMethods extends Remote{
	
	public String saludo(String nombre) throws RemoteException;
	public int login(String nombre,String contrasena) throws RemoteException;
	public boolean addUser(String nombre,String contrasena) throws RemoteException;
	public int disparo(String p) throws RemoteException;
	public void ponerBarco(String nombre ,String id, String posisiones[]) throws RemoteException;
	public void crearBarcos(String nombre) throws RemoteException;
	public int listo() throws RemoteException;
	public int turno() throws RemoteException;
	public boolean isJuegoIniciado() throws RemoteException;
	public boolean isJuegoTerminado() throws RemoteException;
	public int[][] getMatriz() throws RemoteException;
	public int getTurno() throws RemoteException;
}
