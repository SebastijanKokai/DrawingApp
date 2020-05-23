package mvc;

import geometry.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DrawingModel {
	
	private ArrayList<Shape> shapes = new ArrayList<Shape>(); // because of multithreading

    public ArrayList<Shape> getShapes() {
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
