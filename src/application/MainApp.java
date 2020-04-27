package application;

import java.awt.EventQueue;

import javax.swing.JFrame;

import mvc.*;

public class MainApp {

	public static void main(String[] args) {
		
		Runnable runner = new Runnable() {

			@Override
			public void run() {
				
				createGUIAndRun();
				
			}
		
		};
		EventQueue.invokeLater(runner);
	}
	
	private static void createGUIAndRun() {
		
		try {
			DrawingModel model = new DrawingModel();
	        DrawingFrame frame = new DrawingFrame();
	        frame.getView().setModel(model);
	        DrawingController controller = new DrawingController(model, frame);
	        frame.setController(controller);
	        
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	

}
