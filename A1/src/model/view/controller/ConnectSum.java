package model.view.controller;

import java.util.Scanner;

public class ConnectSum {
	private final static void start() {
		final Model model = new Model();
		final View view = new View();
		
		model.addPropertyChangeListener(view);
		
		final Controller controller = new Controller(model);
		
		final Scanner scanner = new Scanner(System.in);
		
		controller.gatherInput(scanner, true);
	}
	
 	public static void main(final String[] args) {		
 		start();
	}
}
