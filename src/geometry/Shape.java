package geometry;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.Comparator;

public abstract class Shape implements Moveable, Comparable<Object>, Cloneable, Serializable {

	private boolean selected;
	public String nameString;

	public Shape() {

	}

	public Shape(boolean selected) {
		this.selected = selected;
	}

	public abstract boolean contains(int x, int y);

	public abstract void draw(Graphics g);

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getName() {
		return nameString;
	}

	public void setNameString(String nameString) {
		this.nameString = nameString;
	}

	public Object clone() {
		Object clone = null;

		try {
			clone = super.clone();

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return clone;
	}

}
