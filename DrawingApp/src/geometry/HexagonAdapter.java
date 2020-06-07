package geometry;

import java.awt.Color;
import java.awt.Graphics;
import hexagon.*;

// I used GUI JD to read the .class file of hexagon and made an adapter
public class HexagonAdapter extends Shape {

	Hexagon hex;
	
	public HexagonAdapter(int x, int y, int r, Color innerColor, Color outerColor) {
		hex = new Hexagon(x, y, r);
		hex.setAreaColor(innerColor);
		hex.setBorderColor(outerColor);
	}
	
	@Override
	public void moveBy(int byX, int byY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean contains(int x, int y) {
		return hex.doesContain(x, y);
	}

	@Override
	public void draw(Graphics g) {
		hex.paint(g);
	}
	
	public Color getInnerColor() {
		return hex.getAreaColor();
	}
	
	public Color getOuterColor() {
		return hex.getBorderColor();
	}
	
	public int getX() {
		return hex.getX();
	}
	public int getY() {
		return hex.getY();
	}
	public int getR() {
		return hex.getR();
	}
	
	
	public void setSelected(boolean selected) {
		hex.setSelected(selected);
	}
	
	public boolean isSelected() {
		return hex.isSelected();
	}
	

}
