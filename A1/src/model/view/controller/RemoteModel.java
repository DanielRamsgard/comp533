package model.view.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.Client;

public interface RemoteModel extends Remote {
	public void registerRemoteClient(Client client) throws RemoteException;
}
