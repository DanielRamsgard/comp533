package model.view.controller;

import java.util.Scanner;
import java.beans.PropertyChangeListener;

public class Connect {
	private static void start() {
		final Model model = new Model();
		final PropertyChangeListener view = new View();
		
		model.addPropertyChangeListener(view);
		
		final Controller controller = new Controller(model);
		
		final Scanner scanner = new Scanner(System.in);
		
		controller.gatherInputFromScanner(scanner, false);
	}
	
	public static void main(final String[] args) {
		start();
	}
}
