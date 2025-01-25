package model.view.controller;

import java.util.Scanner;

public class ConnectSum {
	public static void main(String[] args) {
		Model model = new Model();
		View view = new View();
		
		model.addPropertyChangeListener(view);
		
		Controller controller = new Controller(model);
		
		Scanner scanner = new Scanner(System.in);
		
		controller.gatherInput(scanner, true);
		
	}
}
