package model.view.controller;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import client.Client;
import key.value.KeyValue;

public interface RemoteModel extends Remote, Serializable {
	public void registerRemoteClient(Client client) throws RemoteException;
	public List<LinkedList<KeyValue<String, Integer>>> getReductionQueueListRemote() throws RemoteException;
	public void quit() throws RemoteException;
}
