package mvc;
import geometry.Shape;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;


public class DrawingView extends JPanel {

  private DrawingModel model = new DrawingModel();

  public void setModel(DrawingModel model) {
      this.model = model;
  }
  
  public DrawingView() {
	  setVisible(true);
  }

  public void paintComponent(Graphics g) {
	  super.paintComponent(g);
	  
	  Iterator<Shape> it = model.getShapes().iterator();
	  while(it.hasNext()) {
		  it.next().draw(g);
	  }
  }

}