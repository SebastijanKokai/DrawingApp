package geometry;

import java.awt.Graphics;
import java.util.Comparator;

public abstract class Shape implements Moveable, Comparable<Object>, Cloneable, Comparator<Shape> {

	private boolean selected;
	private String nameString;
	private int z_order;
	
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

	public String getNameString() {
		return nameString;
	}

	public void setNameString(String nameString) {
		this.nameString = nameString;
	}
	
	public int getZ_order() {
		return z_order;
	}

	public void setZ_order(int z_order) {
		this.z_order = z_order;
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
		
		@Override
		public int compare(Shape one, Shape two) {
			return one.getZ_order() - two.getZ_order();
		}

		
	
	
	
}
