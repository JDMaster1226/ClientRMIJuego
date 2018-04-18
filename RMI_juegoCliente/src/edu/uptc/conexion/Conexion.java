package edu.uptc.conexion;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.uptc.remote.IMethods;

public class Conexion {
	
	public IMethods searchServer(String ip){
		try {
			Registry registry = LocateRegistry.getRegistry(ip, 1234);
			IMethods remoteMethods = (IMethods) registry.lookup("Hello");
			return remoteMethods;
		} catch (RemoteException e) {
			System.out.println("La ip del servidor no existe");
		} catch (NotBoundException e) {
			System.out.println("Error con los metodos remotos");
		}
		return null;
	}
}
