package model.view.controller;

public class Connect {
	public static void main(String[] args) {
		Model model = new Model();
		View view = new View();
		
		// model.addPropertyChangeListener(view)
		
		Controller controller = new Controller(model);
		
	}
}
