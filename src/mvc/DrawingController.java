package mvc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

import command.CommandManager;
import command.commands.BringBack;
import command.commands.BringFront;
import command.commands.CircleModify;
import command.commands.CommandAdd;
import command.commands.CommandRemove;
import command.commands.DonutModify;
import command.commands.HexModify;
import command.commands.LineModify;
import command.commands.PointModify;
import command.commands.RectangleModify;
import command.commands.ToBack;
import command.commands.ToFront;
import dialogs.DlgCircle;
import dialogs.DlgDonut;
import dialogs.DlgHexagon;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.HexagonAdapter;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import observer.SelectedObjects;

public class DrawingController extends MouseAdapter implements ActionListener {

	private DrawingModel model;
	private DrawingFrame frame;
	private SelectedObjects selectedObjects = new SelectedObjects();
	CommandManager commandManager = CommandManager.getInstance();

	// Logger
	private static final Logger logger = Logger.getLogger("Logger");
	private static FileHandler fh = null;

	// Counters
	int pointerCount = 1;
	int lineCount = 1;
	int rectangleCount = 1;
	int circleCount = 1;
	int donutCount = 1;
	int hexagonCount = 1;

	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
		initLogger();
	}

	public static void initLogger() {
		try {
			fh = new FileHandler("loggerDrawing.log", true);
			fh.setFormatter(new SimpleFormatter());

			logger.setLevel(Level.ALL);
			logger.addHandler(fh);
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, "File logger is not working.", e);
		}

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

						check(selectedObjects.size());

					} else if (shape.contains(x, y)) {
						shape.setSelected(true);
						selectedObjects.add(shape);

						check(selectedObjects.size());
					}

				}

			}

		}
		// Drawing shapes

		// Point
		else if (frame.getBtnPoint().isSelected() == true) {
			try {
				Point point = pointDialog(x, y, outerColor, false);
				if (point != null) {
					CommandAdd cmd = new CommandAdd(model, point, "Point" + pointerCount++);
					commandManager.execute(cmd);
					commandManager.clearReverse();
					// frame.getLogArea().setText(logger.get);
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
				}

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
					if (line != null) {
						CommandAdd cmd = new CommandAdd(model, line, "Line" + lineCount++);
						commandManager.execute(cmd);
						commandManager.clearReverse();
						frame.getBtnUndo().setEnabled(true);
						frame.getBtnRedo().setEnabled(false);
					}

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
				if (rect != null) {
					CommandAdd cmd = new CommandAdd(model, rect, "Rectangle" + rectangleCount++);
					commandManager.execute(cmd);
					commandManager.clearReverse();
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}

		// Circle
		else if (frame.getBtnCircle().isSelected() == true) {

			try {
				Circle circle = circleDialog(x, y, 0, innerColor, outerColor, false);

				if (circle != null) {
					CommandAdd cmd = new CommandAdd(model, circle, "Circle" + circleCount++);
					commandManager.execute(cmd);
					commandManager.clearReverse();
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
				}

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}

		}

		// Doughnut
		else if (frame.getBtnDonut().isSelected() == true) {

			try {
				Donut donut = donutDialog(x, y, 0, 0, innerColor, outerColor, false);
				if (donut != null) {
					CommandAdd cmd = new CommandAdd(model, donut, "Donut" + donutCount++);
					commandManager.execute(cmd);
					commandManager.clearReverse();
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
				}

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}

		// Hexagon
		else if (frame.getBtnHex().isSelected() == true) {
			try {
				HexagonAdapter hex = hexDialog(x, y, 0, innerColor, outerColor, false);
				if (hex != null) {
					CommandAdd cmd = new CommandAdd(model, hex, "Hexagon" + hexagonCount++);
					commandManager.execute(cmd);
					commandManager.clearReverse();
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
				}

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}

		frame.getView().repaint();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Delete
		if (e.getSource() == frame.getBtnDelete() && model.getShapes().size() > 0 && selectedObjects.size() > 0) {
			int input = JOptionPane.showConfirmDialog(null, "Are you sure?");
			if (input == 0) {
				Iterator<Shape> it = model.getShapes().iterator();
				while (it.hasNext()) {

					Shape shape = it.next();
					if (shape.isSelected()) {
						int index = model.getShapes().indexOf(shape);
						CommandRemove cmdRemove = new CommandRemove(model, shape, index);
						commandManager.execute(cmdRemove);
						
						frame.getBtnUndo().setEnabled(true);
						frame.getBtnRedo().setEnabled(false);
					}

					check(selectedObjects.size());
				}

				frame.getView().repaint();
			}

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
							PointModify pointModify = new PointModify(p, point);
							commandManager.execute(pointModify);
							
							frame.getBtnUndo().setEnabled(true);
							frame.getBtnRedo().setEnabled(false);
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
							LineModify lineModify = new LineModify(l, line);
							commandManager.execute(lineModify);
							
							frame.getBtnUndo().setEnabled(true);
							frame.getBtnRedo().setEnabled(false);
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
							RectangleModify rectangleModify = new RectangleModify(r, rect);
							commandManager.execute(rectangleModify);
							
							frame.getBtnUndo().setEnabled(true);
							frame.getBtnRedo().setEnabled(false);
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
							CircleModify circleModify = new CircleModify(c, circle);
							commandManager.execute(circleModify);
							
							frame.getBtnUndo().setEnabled(true);
							frame.getBtnRedo().setEnabled(false);
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
							DonutModify donutModify = new DonutModify(d, donut);
							commandManager.execute(donutModify);
							
							frame.getBtnUndo().setEnabled(true);
							frame.getBtnRedo().setEnabled(false);
						}

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
					break;
				case "HexagonAdapter":
					try {
						HexagonAdapter h = (HexagonAdapter) shape;
						HexagonAdapter hex = hexDialog(h.getX(), h.getY(), h.getR(), h.getInnerColor(),
								h.getOuterColor(), true);
						if (hex != null) {
							hex.setSelected(true);
							HexModify hexModify = new HexModify(h, hex);
							commandManager.execute(hexModify);
							
							frame.getBtnUndo().setEnabled(true);
							frame.getBtnRedo().setEnabled(false);
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
		// Undo
		else if (e.getSource() == frame.getBtnUndo()) {
			commandManager.undo();
			frame.getBtnRedo().setEnabled(true);
			if (commandManager.sizeNormal() == 0)
				frame.getBtnUndo().setEnabled(false);
			else
				frame.getBtnUndo().setEnabled(true);
			frame.getView().repaint();
		}
		// Redo
		else if (e.getSource() == frame.getBtnRedo()) {
			commandManager.redo();
			frame.getBtnUndo().setEnabled(true);
			if (commandManager.sizeReverse() == 0)
				frame.getBtnRedo().setEnabled(false);
			else
				frame.getBtnRedo().setEnabled(true);
			frame.getView().repaint();
		}
		// ToBack
		else if (e.getSource() == frame.getBtnToBack()) {
			if (selectedObjects.size() == 1) {
				Shape shape = selectedObjects.get(0);
				int index = model.getShapes().indexOf(shape);
				ToBack cmd = new ToBack(model, shape, index);
				commandManager.execute(cmd);
				
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getView().repaint();
			}
		}
		// ToFront
		else if (e.getSource() == frame.getBtnToFront()) {
			if (selectedObjects.size() == 1) {
				
				Shape shape = selectedObjects.get(0);
				int index = model.getShapes().indexOf(shape);
				
				ToFront cmd = new ToFront(model, shape, index);
				commandManager.execute(cmd);

				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getView().repaint();

			}
		}
		// BringBack
		else if (e.getSource() == frame.getBtnBringBack()) {
			if (selectedObjects.size() == 1) {
				Shape shape = selectedObjects.get(0);
				int index = model.getShapes().indexOf(shape);
				BringBack cmd = new BringBack(model, shape, index);
				commandManager.execute(cmd);
				
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getView().repaint();
			}
		}
		// BringFront
		else if (e.getSource() == frame.getBtnBringFront()) {
			if (selectedObjects.size() == 1) {
				Shape shape = selectedObjects.get(0);
				int index = model.getShapes().indexOf(shape);
				int length = model.getShapes().size();
				BringFront cmd = new BringFront(model, shape, index, length);
				commandManager.execute(cmd);
				
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getView().repaint();

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
			boolean editable) throws Exception {

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

	private Circle circleDialog(int x, int y, int radius, Color innerColor, Color outerColor, boolean editable)
			throws Exception {

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
			boolean editable) throws Exception {

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

		if (editable) {
			dlg.getTextFieldRadius().setText(String.valueOf(r));
		}

		dlg.getTextFieldX().setEditable(editable);
		dlg.getTextFieldY().setEditable(editable);

		dlg.setVisible(true);

		if (dlg.isAccepted()) {
			x = Integer.parseInt(dlg.getTextFieldX().getText());
			y = Integer.parseInt(dlg.getTextFieldY().getText());
			r = Integer.parseInt(dlg.getTextFieldRadius().getText());
			innerColor = dlg.getBtnInnerColor().getBackground();
			outerColor = dlg.getBtnOuterColor().getBackground();

			return new HexagonAdapter(x, y, r, innerColor, outerColor);
		}

		return null;
	}

	private void check(int size) {
		if (size > 0) {
			if (size == 1) {
				frame.getBtnModify().setEnabled(true);
				frame.getBtnBringBack().setEnabled(true);
				frame.getBtnBringFront().setEnabled(true);
				frame.getBtnToBack().setEnabled(true);
				frame.getBtnToFront().setEnabled(true);
			}
			else {
				frame.getBtnModify().setEnabled(false);
				frame.getBtnBringBack().setEnabled(false);
				frame.getBtnBringFront().setEnabled(false);
				frame.getBtnToBack().setEnabled(false);
				frame.getBtnToFront().setEnabled(false);
			}

			frame.getBtnDelete().setEnabled(true);
		} else {
			frame.getBtnDelete().setEnabled(false);
			frame.getBtnModify().setEnabled(false);
			frame.getBtnBringBack().setEnabled(false);
			frame.getBtnBringFront().setEnabled(false);
			frame.getBtnToBack().setEnabled(false);
			frame.getBtnToFront().setEnabled(false);
		}
	}

}

// TODO
// When I modify an object it gets re-created, so the Z position changes
// When I delete an object with undo, buttons modify,delete need to be disabled
// I should log with cmdHistory -- then I should create objects for every
// Undo and Redo -- delete has bug
// Logging onto textarea
// Saving logs into txt file
// Importing from txt file commands (all at once and 1 by 1)

// If I click while selected is on, on non shape value, false all selections
