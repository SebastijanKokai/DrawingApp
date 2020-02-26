package mvc;

import geometry.Shape;

import java.util.ArrayList;
import java.util.List;

public class DrawingModel {
	
	private List<Shape> shapes = new ArrayList<Shape>();

    public List<Shape> getShapes() {
        return shapes;
    }

    public void add(Shape sh) {
        shapes.add(sh);
    }

    public void remove(Shape sh) {
        shapes.remove(sh);
    }

    public Shape get(int i) {
        return shapes.get(i);
    }

}
