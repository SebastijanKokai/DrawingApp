package mvc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import command.*;
import command.commands.*;
import dialogs.*;
import geometry.*;
import observer.*;
import strategy.*;

public class DrawingController extends MouseAdapter implements ActionListener {

	private DrawingModel model;
	private DrawingFrame frame;
	private SelectedObjects selectedObjects = new SelectedObjects();
	CommandManager commandManager = CommandManager.getInstance();
	final JFileChooser fc = new JFileChooser();
	List<Shape> shapes = new ArrayList<Shape>();
	ArrayList<String> txtFileLines = new ArrayList<String>();
	int i = 0;

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
		commandManager.setFrame(frame);
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

				for (int i = model.getShapes().size() - 1; i >= 0; i--) {
					Shape shape = model.get(i);

					if (shape.contains(x, y)) {
						Select selectCommand = new Select(shape, selectedObjects, shape.getName() + " - Selected");
						commandManager.execute(selectCommand);

						check(selectedObjects.size());
						break;
					} else if (i == 0) {
						for (int j = 0; selectedObjects.size() > 0;) {
							Deselect deselectCommand = new Deselect(selectedObjects.get(j), selectedObjects,
									selectedObjects.get(j).getName() + " - Deselected");
							commandManager.execute(deselectCommand);
						}

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
					point.setNameString("Point" + pointerCount++ + "," + point.toString() + ",color,"
							+ String.valueOf(point.getColor().getRGB()));

					CommandAdd cmd = new CommandAdd(model, point, point.getName() + " - Add");
					commandManager.execute(cmd);

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
						line.setNameString("Line" + lineCount++ + "," + line.toString());
						CommandAdd cmd = new CommandAdd(model, line, line.getName() + " - Add");
						commandManager.execute(cmd);
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
					rect.setNameString("Rectangle" + rectangleCount++ + "," + rect.toString());
					CommandAdd cmd = new CommandAdd(model, rect, rect.getName() + " - Add");
					commandManager.execute(cmd);
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
					circle.setNameString("Circle" + circleCount++ + "," + circle.toString());
					CommandAdd cmd = new CommandAdd(model, circle,
							 circle.getName() + " - Add");
					commandManager.execute(cmd);
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
					donut.setNameString("Donut" + donutCount++ + "," + donut.toString());
					CommandAdd cmd = new CommandAdd(model, donut,
							 donut.getName() + " - Add");
					commandManager.execute(cmd);
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

					hex.setNameString("Hexagon" + hexagonCount++ + "," + hex.toString());
					CommandAdd cmd = new CommandAdd(model, hex,
							 hex.getName() + " - Add");
					commandManager.execute(cmd);
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
				for (int i = 0; i < selectedObjects.size(); i++) {
					System.out.println(selectedObjects.get(i).getName());
				}
				while (it.hasNext()) {

					Shape shape = it.next();
					if (shape.isSelected()) {
						int index = model.getShapes().indexOf(shape);
						CommandRemove cmdRemove = new CommandRemove(model, shape, index,
								shape.getName() + " - Deleted");
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
				ToBack cmd = new ToBack(model, shape, index, shape.getName() + " - To Back");
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

				ToFront cmd = new ToFront(model, shape, index, shape.getName() + " - To Front");
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
				BringBack cmd = new BringBack(model, shape, index, shape.getName() + " - Bring Back");
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
				BringFront cmd = new BringFront(model, shape, index, length, shape.getName() + " - Bring Front");
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

			x = Integer.parseInt(dlg.getTextFieldX().getText());
			y = Integer.parseInt(dlg.getTextFieldY().getText());

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
			} else {
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

	public void save(File fileToSave, File fileToSaveLog) throws IOException {
		SaveManager savePainting = new SaveManager(new SavePainting());
		SaveManager saveLog = new SaveManager(new SaveLog());

		savePainting.save(model, fileToSave);
		saveLog.save(frame, fileToSaveLog);
	}

	@SuppressWarnings("unchecked")
	public void load(File fileToLoad) throws ClassNotFoundException, IOException {
		frame.getLogArea().setText("");
		File f = new File(fileToLoad.getAbsolutePath().replaceAll("bin", "txt"));
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;

		while ((line = br.readLine()) != null) {

			frame.getLogArea().append(line + '\n');
		}
		br.close();

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToLoad));
		try {
			model.getShapes().clear();
			selectedObjects.clear();
			commandManager.clearNormal();
			commandManager.clearReverse();
			frame.getBtnUndo().setEnabled(false);
			frame.getBtnRedo().setEnabled(false);

			model.getShapes().addAll((ArrayList<Shape>) ois.readObject());

			frame.getView().repaint();
		} catch (SocketTimeoutException exc) {
			// you got the timeout
		} catch (InvalidClassException ex) {

		} catch (EOFException exc) {
			ois.close();

			// end of stream
		} catch (IOException exc) {
			// some other I/O error: print it, log it, etc.
			exc.printStackTrace(); // for example
		}
		for (int i = 0; i < model.getShapes().size(); i++) {
			if (model.getShapes().get(i).isSelected()) {
				selectedObjects.add(model.getShapes().get(i));
			}
		}
		// notifyAllObservers();
		ois.close();
	}

	public void loadOneByOne(File fileToLoad) throws IOException {

		frame.getBtnNext().setEnabled(true);

		model.getShapes().clear();
		selectedObjects.clear();
		commandManager.clearNormal();
		commandManager.clearReverse();
		check(0);
		frame.getLogArea().setText("");

		frame.repaint();

		// Counter reset
		pointerCount = 1;
		lineCount = 1;
		rectangleCount = 1;
		circleCount = 1;
		donutCount = 1;
		hexagonCount = 1;

		BufferedReader br = new BufferedReader(new FileReader(fileToLoad));
		String line;

		while ((line = br.readLine()) != null) {
			txtFileLines.add(line);
		}

		br.close();
	}

	public void loadNext() throws Exception {

		String line = txtFileLines.get(i);
		Shape shape = null;

		if (line.contains("Undo")) {
			commandManager.undo();
			check(selectedObjects.size());

		} else if (line.contains("Redo")) {
			commandManager.redo();
			check(selectedObjects.size());
		} else if (line.contains("To Front")) {

			Shape s = selectedObjects.get(0);
			int index = model.getShapes().indexOf(shape);

			ToFront cmd = new ToFront(model, shape, index, shape.getName() + " - To Front");
			commandManager.execute(cmd);

			frame.getBtnUndo().setEnabled(true);
			frame.getBtnRedo().setEnabled(false);
			frame.getView().repaint();

		} else if (line.contains("To Back")) {

			Shape s = selectedObjects.get(0);
			int index = model.getShapes().indexOf(shape);
			ToBack cmd = new ToBack(model, shape, index, shape.getName() + " - To Back");
			commandManager.execute(cmd);

			frame.getBtnUndo().setEnabled(true);
			frame.getBtnRedo().setEnabled(false);
			frame.getView().repaint();

		} else if (line.contains("Bring Front")) {

			Shape s = selectedObjects.get(0);
			int index = model.getShapes().indexOf(shape);
			int length = model.getShapes().size();
			BringFront cmd = new BringFront(model, shape, index, length, shape.getName() + " - Bring Front");
			commandManager.execute(cmd);

			frame.getBtnUndo().setEnabled(true);
			frame.getBtnRedo().setEnabled(false);
			frame.getView().repaint();

		} else if (line.contains("Bring Back")) {

			Shape s = selectedObjects.get(0);
			int index = model.getShapes().indexOf(shape);
			BringBack cmd = new BringBack(model, shape, index, shape.getName() + " - Bring Back");
			commandManager.execute(cmd);

			frame.getBtnUndo().setEnabled(true);
			frame.getBtnRedo().setEnabled(false);
			frame.getView().repaint();
		} else if (line.contains("Add")) {
			line = line.replace(" - Add", "");
			if (line.contains("Point")) {
				String[] attributes = line.split(",");
				int x = Integer.parseInt(attributes[2]);
				int y = Integer.parseInt(attributes[4]);
				Color color = new Color(Integer.parseInt(attributes[6]));

				Point point = new Point(x, y, color);

				point.setNameString("Point" + pointerCount++ + "," + point.toString() + ",color,"
						+ String.valueOf(point.getColor().getRGB()));

				CommandAdd cmd = new CommandAdd(model, point, point.getName() + " - Add");
				commandManager.execute(cmd);

				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);

			} else if (line.contains("Line")) {

				String[] attributes = line.split(",");
				int x1 = Integer.parseInt(attributes[2]);
				int y1 = Integer.parseInt(attributes[4]);
				int x2 = Integer.parseInt(attributes[6]);
				int y2 = Integer.parseInt(attributes[8]);
				Color color = new Color(Integer.parseInt(attributes[10]));

				Point startPoint = new Point(x1, y1);
				Point endPoint = new Point(x2, y2);

				Line lineObject = new Line(startPoint, endPoint, color);

				lineObject.setNameString("Line" + lineCount++ + "," + lineObject.toString());

				CommandAdd cmd = new CommandAdd(model, lineObject, lineObject.getName() + " - Add");
				commandManager.execute(cmd);
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);

			} else if (line.contains("Rectangle")) {

				String[] attributes = line.split(",");
				int x = Integer.parseInt(attributes[2]);
				int y = Integer.parseInt(attributes[4]);
				int height = Integer.parseInt(attributes[6]);
				int width = Integer.parseInt(attributes[8]);
				Color outerColor = new Color(Integer.parseInt(attributes[10]));
				Color innerColor = new Color(Integer.parseInt(attributes[12]));

				Point upperLeftPoint = new Point(x, y);

				Rectangle rect = new Rectangle(upperLeftPoint, width, height, innerColor, outerColor);

				rect.setNameString("Rectangle" + rectangleCount++ + "," + rect.toString());
				CommandAdd cmd = new CommandAdd(model, rect, rect.getName() + " - Add");
				commandManager.execute(cmd);
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);

			} else if (line.contains("Circle")) {

				// ABCD
				String[] attributes = line.split(",");
				int x = Integer.parseInt(attributes[2]);
				int y = Integer.parseInt(attributes[4]);
				int radius = Integer.parseInt(attributes[6]);
				Color outerColor = new Color(Integer.parseInt(attributes[8]));
				Color innerColor = new Color(Integer.parseInt(attributes[10]));

				Point center = new Point(x, y);

				Circle circle = new Circle(center, radius, innerColor, outerColor);

				circle.setNameString("Circle" + circleCount++ + "," + circle.toString());
				CommandAdd cmd = new CommandAdd(model, circle,
						 circle.getName() + " - Add");
				commandManager.execute(cmd);
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);

			} else if (line.contains("Donut")) {

				String[] attributes = line.split(",");
				int x = Integer.parseInt(attributes[2]);
				int y = Integer.parseInt(attributes[4]);
				int radius = Integer.parseInt(attributes[6]);
				Color outerColor = new Color(Integer.parseInt(attributes[8]));
				Color innerColor = new Color(Integer.parseInt(attributes[10]));
				int innerRadius = Integer.parseInt(attributes[12]);

				Point center = new Point(x, y);

				Donut donut = new Donut(center, innerRadius, radius, innerColor, outerColor);

				donut.setNameString("Donut" + donutCount++ + "," + donut.toString());
				CommandAdd cmd = new CommandAdd(model, donut,
						 donut.getName() + " - Add");
				commandManager.execute(cmd);
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);

			} else if (line.contains("Hexagon")) {

				String[] attributes = line.split(",");
				int x = Integer.parseInt(attributes[2]);
				int y = Integer.parseInt(attributes[4]);
				int radius = Integer.parseInt(attributes[6]);
				Color outerColor = new Color(Integer.parseInt(attributes[8]));
				Color innerColor = new Color(Integer.parseInt(attributes[10]));

				HexagonAdapter hex = new HexagonAdapter(x, y, radius, innerColor, outerColor);
				
				hex.setNameString("Hexagon" + hexagonCount++ + "," + hex.toString());
				CommandAdd cmd = new CommandAdd(model, hex,
						 hex.getName() + " - Add");
				commandManager.execute(cmd);
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);

			}
		} else if (line.contains("Modify")) {
			if (line.contains("Point")) {

			} else if (line.contains("Line")) {

			} else if (line.contains("Rectangle")) {

			} else if (line.contains("Circle")) {

			} else if (line.contains("Donut")) {

			} else if (line.contains("Hexagon")) {

			}

		}

		else if (line.contains("Delete")) {

			int index = model.getShapes().indexOf(shape);
			CommandRemove cmdRemove = new CommandRemove(model, shape, index, shape.getName() + " - Deleted");
			commandManager.execute(cmdRemove);

			frame.getBtnUndo().setEnabled(true);
			frame.getBtnRedo().setEnabled(false);

		} else if (line.contains("Selected")) {
			line = line.replace(" - Selected", "");

			for (int i = model.getShapes().size() - 1; i >= 0; i--) {
				shape = model.get(i);

				if (shape.getName().equals(line)) {
					Select selectCommand = new Select(shape, selectedObjects, shape.getName() + " - Selected");
					commandManager.execute(selectCommand);

					check(selectedObjects.size());
					break;
				}
			}
		} else if (line.contains("Deselected")) {

			line = line.replace(" - Deselected", "");

			for (int i = model.getShapes().size() - 1; i >= 0; i--) {
				shape = model.get(i);

				if (shape.getName().equals(line)) {
					Deselect deselectCommand = new Deselect(shape, selectedObjects, shape.getName() + " - Deselected");
					commandManager.execute(deselectCommand);

					check(selectedObjects.size());
					break;
				}
			}
		}
		i++;
		if (i == txtFileLines.size() || txtFileLines.get(i) == null) {
			frame.getBtnNext().setEnabled(false);
		}
		frame.repaint();
	}

}

// TODO
// after deleting multiple objects undo brings them back 1 by 1
// after modify, try delete, it gets bugged


// Donut gets bugged with Serialization
// Modify and delete, loading 1 by 1
// Observer pattern modify
// Prototype pattern
