package mvc;
import geometry.Shape;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ListIterator;

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
      ListIterator<Shape> it = model.getShapes().listIterator();
      while(it.hasNext()) {
          it.next().draw(g);
      }
  }

}