package model.view.controller;

import java.util.Scanner;

public class Connect {
	private static void start() {
		final Model model = new Model();
		final View view = new View();
		
		model.addPropertyChangeListener(view);
		
		final Controller controller = new Controller(model);
		
		final Scanner scanner = new Scanner(System.in);
		
		controller.gatherInput(scanner, false);
	}
	
	public static void main(final String[] args) {
		start();
	}
}
