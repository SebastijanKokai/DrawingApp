package mvc;

import geometry.Point;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class DrawingFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private DrawingController controller;
	private DrawingView view;

	// Button groups
	private final ButtonGroup tools = new ButtonGroup();
	private final ButtonGroup colors = new ButtonGroup();

	// Public variables
	public Color innerColor = Color.RED;
	public Color outerColor = Color.RED;
	public Point startPoint = null;

	// Icons
	ImageIcon iconToBack = new ImageIcon("src/resources/iconToBack.png");
	ImageIcon iconToFront = new ImageIcon("src/resources/iconToFront.png");
	ImageIcon iconBringBack = new ImageIcon("src/resources/iconBringBack.png");
	ImageIcon iconBringFront = new ImageIcon("src/resources/iconBringFront.png");
	ImageIcon iconUndo = new ImageIcon("src/resources/iconUndo.png");
	ImageIcon iconRedo = new ImageIcon("src/resources/iconRedo.png");
	ImageIcon iconSelect = new ImageIcon("src/resources/iconSelect.png");
	ImageIcon iconModify = new ImageIcon("src/resources/iconModify.png");
	ImageIcon iconDelete = new ImageIcon("src/resources/iconDelete.png");
	ImageIcon iconPoint = new ImageIcon("src/resources/iconDot.png");
	ImageIcon iconLine = new ImageIcon("src/resources/iconLine.png");
	ImageIcon iconRectangle = new ImageIcon("src/resources/iconRect.png");
	ImageIcon iconCircle = new ImageIcon("src/resources/iconCircle.png");
	ImageIcon iconDonut = new ImageIcon("src/resources/iconDonut.png");
	ImageIcon iconHexagon = new ImageIcon("src/resources/iconHexagon.png");
	ImageIcon iconNext = new ImageIcon("src/resources/iconNext.png");

	// Panels
	private final JPanel leftPanel = new JPanel();
	private final JPanel topPanel = new JPanel();
	private final JPanel rightPanel = new JPanel();

	// Components
	private final JToggleButton btnSelect = new JToggleButton();
	private final JButton btnModify = new JButton();
	private final JButton btnDelete = new JButton();
	private final JButton btnToBack = new JButton();
	private final JButton btnToFront = new JButton();
	private final JButton btnBringBack = new JButton();
	private final JButton btnBringFront = new JButton();
	private final JToggleButton btnPoint = new JToggleButton();
	private final JToggleButton btnLine = new JToggleButton();
	private final JToggleButton btnRectangle = new JToggleButton();
	private final JToggleButton btnCircle = new JToggleButton();
	private final JToggleButton btnDonut = new JToggleButton();
	private final JToggleButton btnHex = new JToggleButton();
	private JButton btnUndo = new JButton();
	private JButton btnRedo = new JButton();
	private final JButton btnInnerColor = new JButton("InnerColor");
	private final JButton btnOuterColor = new JButton("OuterColor");
	private final JTextArea logArea = new JTextArea();
	private final JScrollPane scrollPane = new JScrollPane(logArea);
	private final JButton btnNext = new JButton();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu menuFile = new JMenu("File");
	private final JMenuItem openItem = new JMenuItem("Open");
	private final JMenuItem saveItem = new JMenuItem("Save");

	public DrawingFrame() {

		// Menu
		menuFile.add(openItem);
		menuFile.add(saveItem);
		menuBar.add(menuFile);
		setJMenuBar(menuBar);

		// Setting icons
		setupIconBtn(btnNext, iconNext, 50, 50);
		setupIconBtn(btnToBack, iconToBack, 50, 50);
		setupIconBtn(btnToFront, iconToFront, 50, 50);
		setupIconBtn(btnBringBack, iconBringBack, 50, 50);
		setupIconBtn(btnBringFront, iconBringFront, 50, 50);
		setupIconBtn(btnUndo, iconUndo, 50, 50);
		setupIconBtn(btnRedo, iconRedo, 50, 50);
		setupIconBtn(btnModify, iconModify, 50, 50);
		setupIconBtn(btnDelete, iconDelete, 50, 50);
		setupIconTgl(btnSelect, iconSelect, 50, 50);
		setupIconTgl(btnPoint, iconPoint, 50, 50);
		setupIconTgl(btnLine, iconLine, 70, 70);
		setupIconTgl(btnRectangle, iconRectangle, 70, 70);
		setupIconTgl(btnCircle, iconCircle, 50, 50);
		setupIconTgl(btnDonut, iconDonut, 70, 70);
		setupIconTgl(btnHex, iconHexagon, 50, 50);

		// Panel layouts
		leftPanel.setLayout(new GridLayout(0, 1, 10, 10));
		topPanel.setLayout(new FlowLayout()); // Even though it is by default
		rightPanel.setLayout(new GridLayout(0, 1, 3, 3));
		view = new DrawingView();

		// Panel size
		view.setPreferredSize(new Dimension(800, 600));
		leftPanel.setPreferredSize(new Dimension(110, 200));
		rightPanel.setPreferredSize(new Dimension(120, 200));

		// Adding btnNext
		btnNext.setEnabled(false);
		tools.add(btnNext);
		topPanel.add(btnNext);

		// Adding btnSelect
		tools.add(btnSelect);
		topPanel.add(btnSelect);

		// Adding btnModify
		tools.add(btnModify);
		topPanel.add(btnModify);

		// Adding btnDelete
		tools.add(btnDelete);
		topPanel.add(btnDelete);

		// Adding btnToBack
		tools.add(btnToBack);
		topPanel.add(btnToBack);

		// Adding btnToFront
		tools.add(btnToFront);
		topPanel.add(btnToFront);

		// Adding btnBringBack
		tools.add(btnBringBack);
		topPanel.add(btnBringBack);

		// Adding btnBringFront
		tools.add(btnBringFront);
		topPanel.add(btnBringFront);

		// Adding btnPoint
		tools.add(btnPoint);
		leftPanel.add(btnPoint);

		// Adding btnLine
		tools.add(btnLine);
		leftPanel.add(btnLine);

		// Adding btnRectangle
		tools.add(btnRectangle);
		leftPanel.add(btnRectangle);

		// Adding btnCircle
		tools.add(btnCircle);
		leftPanel.add(btnCircle);

		// Adding btnDonut
		tools.add(btnDonut);
		leftPanel.add(btnDonut);

		// Adding btnHex
		tools.add(btnHex);
		leftPanel.add(btnHex);

		// Adding buttons undo and redo
		topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		topPanel.add(btnUndo);
		topPanel.add(btnRedo);

		// Adding color buttons
		btnInnerColor.setBackground(innerColor);
		btnOuterColor.setBackground(outerColor);
		colors.add(btnInnerColor);
		colors.add(btnOuterColor);
		rightPanel.add(btnInnerColor);
		rightPanel.add(btnOuterColor);

		// Button Delete and Modify disabled (nothing selected yet)
		btnModify.setEnabled(false);
		btnDelete.setEnabled(false);

		// Setting disabled
		btnBringBack.setEnabled(false);
		btnBringFront.setEnabled(false);
		btnToBack.setEnabled(false);
		btnToFront.setEnabled(false);

		// Button Undo and Redo set to disabled
		btnUndo.setEnabled(false);
		btnRedo.setEnabled(false);

		// logArea
		scrollPane.setPreferredSize(new Dimension(0, 150));
		logArea.setEnabled(false);

		// Setting listeners
		setupListeners();

		// Adding Panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(view, BorderLayout.CENTER);
		getContentPane().add(leftPanel, BorderLayout.WEST);
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(rightPanel, BorderLayout.EAST);
		getContentPane().add(scrollPane, BorderLayout.PAGE_END);
	}

	public DrawingView getView() {
		return view;
	}

	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	public JToggleButton getBtnSelect() {
		return btnSelect;
	}

	public JButton getBtnModify() {
		return btnModify;
	}

	public JToggleButton getBtnPoint() {
		return btnPoint;
	}

	public JToggleButton getBtnLine() {
		return btnLine;
	}

	public JToggleButton getBtnRectangle() {
		return btnRectangle;
	}

	public JToggleButton getBtnCircle() {
		return btnCircle;
	}

	public JToggleButton getBtnDonut() {
		return btnDonut;
	}

	public JToggleButton getBtnHex() {
		return btnHex;
	}

	private void setupIconBtn(JButton btn, ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newImg);
		btn.setIcon(icon);
	}

	private void setupIconTgl(JToggleButton btn, ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newImg);
		btn.setIcon(icon);
	}

	private void setupListeners() {

		// View
		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.mouseClicked(e);
			}
		});

		// Modify
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(e);
			}
		});

		// Button Delete
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(e);
			}
		});

		// Button Undo
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(e);
			}
		});

		// Button Redo
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(e);
			}
		});

		// Button ToBack
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(e);
			}
		});
		// Button ToFront
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(e);
			}
		});
		// Button BringBack
		btnBringBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(e);
			}
		});
		// Button BringFront
		btnBringFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.actionPerformed(e);
			}
		});

		btnOuterColor.addActionListener(this);
		btnInnerColor.addActionListener(this);

		// Save
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter f = new FileNameExtensionFilter("Bin", "bin");
				c.setFileFilter(f);

				int userSelection = c.showSaveDialog(null);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = c.getSelectedFile();
					File fileToSaveLog;
					String filePath = fileToSave.getAbsolutePath();
					
					if (!filePath.endsWith(".bin") && !filePath.contains(".")) {
						fileToSave = new File(filePath + ".bin");
						fileToSaveLog = new File(filePath + ".txt");
					}

					String filename = fileToSave.getPath();

					if (filename.substring(filename.lastIndexOf("."), filename.length()).contentEquals(".bin")) {
						try {
							filename = fileToSave.getAbsolutePath().substring(0, filename.lastIndexOf(".")) + ".txt";
							System.out.println(filename);
							fileToSaveLog = new File(filename);
							controller.save(fileToSave, fileToSaveLog);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						JOptionPane.showMessageDialog(null, "File is not valid");
					}
				}

			}
		});

		// Open
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter f = new FileNameExtensionFilter("bin", "bin", "txt");

				c.setFileFilter(f);

				c.setDialogTitle("Open");
				int userSelection = c.showOpenDialog(null);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToLoad = c.getSelectedFile();
					String filename = fileToLoad.getPath();
					if (filename.substring(filename.lastIndexOf("."), filename.length()).contentEquals(".bin")) {
						try {
							controller.load(fileToLoad);
						} catch (IOException m) {
							m.printStackTrace();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}

					} else if (filename.substring(filename.lastIndexOf("."), filename.length()).contentEquals(".txt")) {

						try {
							controller.loadOneByOne(fileToLoad);

						} catch (IOException m) { 
							m.printStackTrace();
						}

					} else {
						JOptionPane.showMessageDialog(null, "The file is not valid");
					}
				}

			}
		});

		btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					controller.loadNext();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "InnerColor") {
			innerColor = JColorChooser.showDialog(this, "Choose your inner color", Color.RED);
			btnInnerColor.setBackground(innerColor);
		} else if (e.getActionCommand() == "OuterColor") {
			outerColor = JColorChooser.showDialog(this, "Choose your outer color", Color.RED);
			btnOuterColor.setBackground(outerColor);
		}

	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JButton getBtnUndo() {
		return btnUndo;
	}

	public void setBtnUndo(JButton btnUndo) {
		this.btnUndo = btnUndo;
	}

	public JButton getBtnRedo() {
		return btnRedo;
	}

	public void setBtnRedo(JButton btnRedo) {
		this.btnRedo = btnRedo;
	}

	public JTextArea getLogArea() {
		return logArea;
	}

	public JButton getBtnToBack() {
		return btnToBack;
	}

	public JButton getBtnToFront() {
		return btnToFront;
	}

	public JButton getBtnBringBack() {
		return btnBringBack;
	}

	public JButton getBtnBringFront() {
		return btnBringFront;
	}

	public JButton getBtnNext() {
		return btnNext;
	}
}