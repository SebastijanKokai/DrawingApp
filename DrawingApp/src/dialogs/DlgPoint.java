package dialogs;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DlgPoint extends JDialog implements ActionListener {

	private final JButton btnAccept = new JButton("Accept");
	private final JButton btnDecline = new JButton("Decline");
	private final JButton btnColor = new JButton("");
	private final JLabel lbColor = new JLabel("Color");
	private final JLabel lbXCoord = new JLabel("X Coordinate:");
	private final JLabel lbYCoord = new JLabel("Y Coordinate:");
	private final JTextField textFieldX = new JTextField();
	private final JTextField textFieldY = new JTextField();
	
	private boolean accepted = false;
	
	public DlgPoint(int x, int y, Color color) {
		
		// Setting the dialog
		setTitle("Point Dialog");
		setSize(300, 300);
		setLocationRelativeTo(null); // For center screen
		setModal(true);
		
		// Creating the panel
		JPanel mainPanel = new JPanel(new GridLayout(0,2,5,5));
		
		// Components
		textFieldX.setText(Integer.toString(x));
		textFieldY.setText(Integer.toString(y));
		textFieldX.setEditable(false);
		textFieldY.setEditable(false);
		btnColor.setBackground(color);
		
		
		// Adding components
		mainPanel.add(lbXCoord);
		mainPanel.add(textFieldX);
		mainPanel.add(lbYCoord);
		mainPanel.add(textFieldY);
		mainPanel.add(lbColor);
		mainPanel.add(btnColor);
		mainPanel.add(btnAccept);
		mainPanel.add(btnDecline);
		
		// Listeners
		btnColor.addActionListener(this);
		btnAccept.addActionListener(this);
		btnDecline.addActionListener(this);
		
		// Content Pane
		getContentPane().add(mainPanel);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnColor) {
			Color outerColor = JColorChooser.showDialog(this, "Choose your inner color", Color.RED);
			btnColor.setBackground(outerColor);
		}
		else if(e.getSource() == btnAccept) {
			accepted = true;
			dispose();
		}
		else if(e.getSource() == btnDecline) {
			dispose();
		}
		
	}

	public JTextField getTextFieldX() {
		return textFieldX;
	}

	public JTextField getTextFieldY() {
		return textFieldY;
	}

	public JButton getBtnColor() {
		return btnColor;
	}

	public boolean isAccepted() {
		return accepted;
	}
	 
	
}
