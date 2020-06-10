package mvc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;

import dialogs.*;

import geometry.*;

public class DrawingController extends MouseAdapter implements ActionListener {

	private DrawingModel model;
	private DrawingFrame frame;
	private ArrayList<Shape> selectedObjects = new ArrayList<Shape>();

	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
	}

	public void mouseClicked(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		Color innerColor = frame.innerColor;
		Color outerColor = frame.outerColor;

		// Commands

		// Select
		if (frame.getBtnSelect().isSelected() == true) {

				
			if (model.getShapes() != null) {

				Iterator<Shape> it = model.getShapes().iterator();

				while (it.hasNext()) {

					Shape shape = it.next();
					if (shape.contains(x, y) && shape.isSelected() == true) {
						shape.setSelected(false);
						selectedObjects.remove(shape);
					} else if (shape.contains(x, y)) {
						shape.setSelected(true);
						selectedObjects.add(shape);
					}

				}

			}

		}
		// Drawing shapes

		// Point
		else if (frame.getBtnPoint().isSelected() == true) {

			try {
				Point point = pointDialog(x, y, outerColor, false);
				if (point != null)
					model.add(point);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}

		// Line
		else if (frame.getBtnLine().isSelected() == true) {

			if (frame.startPoint == null) {
				frame.startPoint = new Point(x, y);
			} else {
				try {
					Line line = lineDialog(frame.startPoint.getX(), frame.startPoint.getY(), x, y, outerColor, false);
					if (line != null)
						model.add(line);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}

				frame.startPoint = null;
			}
		}

		// Rectangle
		else if (frame.getBtnRectangle().isSelected() == true) {

			try {

				Rectangle rect = rectDialog(x, y, 0, 0, innerColor, outerColor, false);
				if (rect != null)
					model.add(rect);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}

		// Circle
		else if (frame.getBtnCircle().isSelected() == true) {

			try {
				Circle circle = circleDialog(x, y, 0, innerColor, outerColor, false);

				if (circle != null)
					model.add(circle);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}

		}

		// Doughnut
		else if (frame.getBtnDonut().isSelected() == true) {

			try {
				Donut donut = donutDialog(x, y, 0, 0, innerColor, outerColor, false);
				if (donut != null)
					model.add(donut);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}

		// Hexagon
		else if (frame.getBtnHex().isSelected() == true) {
			try {
				HexagonAdapter hex = hexDialog(x, y, 0, innerColor, outerColor, false);
				if (hex != null)
					model.add(hex);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}

		frame.getView().repaint();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Delete
		if (e.getSource() == frame.getBtnDelete() && model.getShapes() != null) {
			int input = JOptionPane.showConfirmDialog(null, "Are you sure?");
			if (input == 0) {
				// Do something
			}
			selectedObjects = null;

			frame.getView().repaint();
		}

		// Modify
		else if (e.getSource() == frame.getBtnModify()) {
			if (selectedObjects.size() == 1) {
				Shape shape = selectedObjects.get(0);
				String s = shape.getClass().getSimpleName();

				switch (s) {
				case "Point":
					try {
						Point p = (Point) shape;
						Point point = pointDialog(p.getX(), p.getY(), p.getColor(), true);
						if (point != null) {
							point.setSelected(true);
							model.remove(shape);
							selectedObjects.remove(0);
							model.add(point);
							selectedObjects.add(point);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
					break;
				case "Line":
					try {
						Line l = (Line) shape;
						Line line = lineDialog(l.getStartPoint().getX(), l.getStartPoint().getY(),
								l.getEndPoint().getX(), l.getEndPoint().getY(), l.getColor(), true);
						if (line != null) {
							line.setSelected(true);
							model.remove(shape);
							selectedObjects.remove(0);
							model.add(line);
							selectedObjects.add(line);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
					break;
				case "Rectangle":
					try {
						Rectangle r = (Rectangle) shape;
						Rectangle rect = rectDialog(r.getUpperLeftPoint().getX(), r.getUpperLeftPoint().getY(),
								r.getHeight(), r.getWidth(), r.getInnerColor(), r.getOuterColor(), true);
						if (rect != null) {
							rect.setSelected(true);
							model.remove(shape);
							selectedObjects.remove(0);
							model.add(rect);
							selectedObjects.add(rect);
						}

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
					break;
				case "Circle":
					try {
						Circle c = (Circle) shape;
						Circle circle = circleDialog(c.getCenter().getX(), c.getCenter().getY(), c.getRadius(),
								c.getInnerColor(), c.getOuterColor(), true);
						if (circle != null) {
							circle.setSelected(true);
							model.remove(shape);
							selectedObjects.remove(0);
							model.add(circle);
							selectedObjects.add(circle);
						}

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
					break;
				case "Donut":
					try {
						Donut d = (Donut) shape;
						Donut donut = donutDialog(d.getCenter().getX(), d.getCenter().getY(), d.getInnerRadius(),
								d.getRadius(), d.getInnerColor(), d.getOuterColor(), true);
						if (donut != null) {
							donut.setSelected(true);
							model.remove(shape);
							selectedObjects.remove(0);
							model.add(donut);
							selectedObjects.add(donut);
						}

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
					break;
				case "HexagonAdapter":
					try {
						HexagonAdapter h = (HexagonAdapter) shape;
						HexagonAdapter hex = hexDialog(
								h.getX(),
								h.getY(),
								h.getR(),
								h.getInnerColor(),
								h.getOuterColor(),
								true);
						if(hex != null) {
							hex.setSelected(true);
							model.remove(shape);
							selectedObjects.remove(0);
							model.add(hex);
							selectedObjects.add(hex);
						}
						
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				default:
					break;

				}

				frame.getView().repaint();
			} else {
				JOptionPane.showMessageDialog(null, "Choose 1 object");
			}
		}
	}

	// Methods for dialogs
	private Point pointDialog(int x, int y, Color outerColor, boolean editable) {

		DlgPoint dlg = new DlgPoint(x, y, outerColor);
		dlg.getTextFieldX().setEditable(editable);
		dlg.getTextFieldY().setEditable(editable);
		dlg.setVisible(true);

		if (dlg.isAccepted()) {
			x = Integer.parseInt(dlg.getTextFieldX().getText());
			y = Integer.parseInt(dlg.getTextFieldY().getText());
			outerColor = dlg.getBtnColor().getBackground();
			return new Point(x, y, outerColor);
		}

		return null;
	}

	private Line lineDialog(int x1, int y1, int x2, int y2, Color outerColor, boolean editable) {

		DlgLine dlg = new DlgLine(x1, y1, x2, y2, outerColor);
		dlg.getTextFieldX().setEditable(editable);
		dlg.getTextFieldY().setEditable(editable);
		dlg.getTextFieldX2().setEditable(editable);
		dlg.getTextFieldY2().setEditable(editable);
		dlg.setVisible(true);

		if (dlg.isAccepted()) {
			x1 = Integer.parseInt(dlg.getTextFieldX().getText());
			y1 = Integer.parseInt(dlg.getTextFieldY().getText());
			x2 = Integer.parseInt(dlg.getTextFieldX2().getText());
			y2 = Integer.parseInt(dlg.getTextFieldY2().getText());
			outerColor = dlg.getBtnColor().getBackground();
			Point p1 = new Point(x1, y1);
			Point p2 = new Point(x2, y2);
			return new Line(p1, p2, outerColor);
		}

		return null;
	}

	private Rectangle rectDialog(int x, int y, int height, int width, Color innerColor, Color outerColor,
			boolean editable) {

		DlgRectangle dlg = new DlgRectangle(x, y, innerColor, outerColor);
		dlg.getTextFieldX().setEditable(editable);
		dlg.getTextFieldY().setEditable(editable);
		if (editable) {
			dlg.getTextFieldHeight().setText(String.valueOf(height));
			dlg.getTextFieldWidth().setText(String.valueOf(width));
		}
		dlg.setVisible(true);

		if (dlg.isAccepted()) {
			x = Integer.parseInt(dlg.getTextFieldX().getText());
			y = Integer.parseInt(dlg.getTextFieldY().getText());
			innerColor = dlg.getBtnInnerColor().getBackground();
			outerColor = dlg.getBtnOuterColor().getBackground();
			height = Integer.parseInt(dlg.getTextFieldHeight().getText());
			width = Integer.parseInt(dlg.getTextFieldWidth().getText());

			Point p = new Point(x, y);
			return new Rectangle(p, width, height, innerColor, outerColor);
		}

		return null;
	}

	private Circle circleDialog(int x, int y, int radius, Color innerColor, Color outerColor, boolean editable) {

		DlgCircle dlg = new DlgCircle(x, y, innerColor, outerColor);
		if (editable)
			dlg.getTextFieldRadius().setText(String.valueOf(radius));
		dlg.getTextFieldX().setEditable(editable);
		dlg.getTextFieldY().setEditable(editable);

		dlg.setVisible(true);

		if (dlg.isAccepted()) {
			Point p = new Point(x, y);

			radius = Integer.parseInt(dlg.getTextFieldRadius().getText());
			innerColor = dlg.getBtnInnerColor().getBackground();
			outerColor = dlg.getBtnOuterColor().getBackground();

			return new Circle(p, radius, innerColor, outerColor);
		}

		return null;
	}

	private Donut donutDialog(int x, int y, int innerRadius, int radius, Color innerColor, Color outerColor,
			boolean editable) {

		DlgDonut dlg = new DlgDonut(x, y, innerColor, outerColor);
		if (editable) {
			dlg.getTextFieldInRadius().setText(String.valueOf(innerRadius));
			dlg.getTextFieldOutRadius().setText(String.valueOf(radius));
		}
		dlg.getTextFieldX().setEditable(editable);
		dlg.getTextFieldY().setEditable(editable);

		dlg.setVisible(true);

		if (dlg.isAccepted()) {
			
			x = Integer.parseInt(dlg.getTextFieldX().getText());
			y = Integer.parseInt(dlg.getTextFieldY().getText());
			Point p = new Point(x, y);
			innerRadius = Integer.parseInt(dlg.getTextFieldInRadius().getText());
			radius = Integer.parseInt(dlg.getTextFieldOutRadius().getText());

			innerColor = dlg.getBtnInnerColor().getBackground();
			outerColor = dlg.getBtnOuterColor().getBackground();

			return new Donut(p, innerRadius, radius, innerColor, outerColor);
		}

		return null;
	}
	
	private HexagonAdapter hexDialog(int x, int y, int r, Color innerColor, Color outerColor, boolean editable) {
		
		DlgHexagon dlg = new DlgHexagon(x, y, innerColor, outerColor);
		
		if(editable) {
			dlg.getTextFieldRadius().setText(String.valueOf(r));
		}
		
		dlg.getTextFieldX().setEditable(editable);
		dlg.getTextFieldY().setEditable(editable);
		
		dlg.setVisible(true);
		
		if(dlg.isAccepted()) {
			x = Integer.parseInt(dlg.getTextFieldX().getText());
			y = Integer.parseInt(dlg.getTextFieldY().getText());
			r = Integer.parseInt(dlg.getTextFieldRadius().getText());
			innerColor = dlg.getBtnInnerColor().getBackground();
			outerColor = dlg.getBtnOuterColor().getBackground();
			
			return new HexagonAdapter(x, y, r, innerColor, outerColor);
		}
		
		return null;
	}

}

// TODO
// When I modify an object it gets re-created, so the Z position changes
// I should restrict invalid user input -- innerRadius shouldn't be negative or over outerRadius
// Do the delete button
// If none is selected hide modify and delete
