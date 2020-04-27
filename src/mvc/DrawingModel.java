package mvc;

import geometry.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DrawingModel {
	
	private List<Shape> shapes = new CopyOnWriteArrayList<Shape>(); // because of multithreading

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
