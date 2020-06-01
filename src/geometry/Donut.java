package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;


public class Donut extends Circle {

	private int innerRadius;
	private Area area;

	public Donut() {

	}

	public Donut(Point center, int innerRadius, int radius) throws Exception {
		super(center, radius);
		setInnerRadius(innerRadius);
	}
	
	public Donut(Point center, int innerRadius, int radius, Color innerColor, Color outerColor) throws Exception {
		this(center, innerRadius, radius);
		super.setInnerColor(innerColor);
		super.setOuterColor(outerColor);
	}

	public void draw(Graphics g) {
		/*
		 * if (getInnerColor() != null) { g.setColor(getInnerColor());
		 * g.fillOval(this.getCenter().getX() - this.getRadius(),
		 * this.getCenter().getY() - this.getRadius(), this.getRadius() * 2,
		 * this.getRadius() * 2); }
		 */
		//super.draw(g);
		
		Color color = new Color (255.0f / 255.0f, 255.0f / 255.0f, 221.0f / 255.0f, 0.0f); 
		g.setColor(color);
		g.fillOval(this.getCenter().getX() - this.getInnerRadius(), this.getCenter().getY() - this.getInnerRadius(),
				this.getInnerRadius() * 2, this.getInnerRadius() * 2);
		
		
		if (getInnerColor() != null) {
			g.setColor(getInnerColor());
			for(int i = getInnerRadius(); i < getRadius(); i++) {
				g.drawOval(this.getCenter().getX() - i,
						  this.getCenter().getY() - i, i * 2,
						  i * 2);		
			}	
		}
		
		
		if (getOuterColor() != null)
			g.setColor(getOuterColor());
		g.drawOval(this.getCenter().getX() - this.getRadius(), this.getCenter().getY() - this.getRadius(),
				this.getRadius() * 2, this.getRadius() * 2);
		g.drawOval(this.getCenter().getX() - this.getInnerRadius(), this.getCenter().getY() - this.getInnerRadius(),
				this.getInnerRadius() * 2, this.getInnerRadius() * 2);
	

		

		
		/*
		 * g.drawOval(this.getCenter().getX() - this.getInnerRadius(),
		 * this.getCenter().getY() - this.getInnerRadius(), this.getInnerRadius() * 2,
		 * this.getInnerRadius() * 2);
		 */
		 

		g.setColor(new Color(0, 0, 0));

		
		/*
		 * Ellipse2D outer = new Ellipse2D.Double( getCenter().getX() - getRadius(),
		 * getCenter().getY() - getRadius(), getRadius()*2, getRadius()*2); Ellipse2D
		 * inner = new Ellipse2D.Double( getCenter().getX() - getInnerRadius(),
		 * getCenter().getY() - getInnerRadius(), getInnerRadius()*2,
		 * getInnerRadius()*2);
		 * 
		 * area = new Area(outer); area.subtract(new Area(inner));
		 * 
		 * Graphics2D gr = (Graphics2D)g;
		 * 
		 * if(getInnerColor() != null) gr.setColor(getInnerColor()); gr.fill(area);
		 * 
		 * if(getOuterColor() != null) gr.setColor(getOuterColor()); gr.draw(area);
		 */
		
		
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() + getInnerRadius() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - getInnerRadius() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() + getInnerRadius() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() - getInnerRadius() - 3, 6, 6);
			
			g.drawRect(this.getCenter().getX() + getRadius() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - getRadius() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() + getRadius() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() - getRadius() - 3, 6, 6);
			g.setColor(Color.BLACK);
		}
		 
		 
	}

	public int compareTo(Object o) {
		if (o instanceof Donut) {
			return (int) (this.area() - ((Donut) o).area());
		}
		return 0;
	}

	public boolean contains(int x, int y) {
		double dFromCenter = this.getCenter().distance(x, y);
		return super.contains(x, y) && dFromCenter > innerRadius;

		//return area.contains(x, y); // Changed this line because of inner radius transparency
	}

	public boolean contains(Point p) {
		double dFromCenter = this.getCenter().distance(p.getX(), p.getY());
		return super.contains(p.getX(), p.getY()) && dFromCenter > innerRadius;
	}

	public double area() {
		return super.area() - innerRadius * innerRadius * Math.PI;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Donut) {
			Donut d = (Donut) obj;
			if (this.getCenter().equals(d.getCenter()) && this.getRadius() == d.getRadius()
					&& innerRadius == d.getInnerRadius()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public int getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(int innerRadius) throws Exception {
		if (innerRadius <= 0) {
			throw new NumberFormatException("Inner radius has to be a value greater than 0");
		} else if (innerRadius >= super.getRadius()) {
			throw new NumberFormatException("Radius has to be a value greater than inner radius");
		} else {
			this.innerRadius = innerRadius;
		}
	}

	public String toString() {
		return super.toString() + ",innerRadius," + innerRadius;
	}

}
