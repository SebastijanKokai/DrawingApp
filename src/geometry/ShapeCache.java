package geometry;

import java.util.Hashtable;

// Prototype pattern
public class ShapeCache {
	
	private static Hashtable<String, Shape> shapeMap = new Hashtable<String, Shape>();
	
	 public static Shape getShape(String shapeId) {
	      Shape cachedShape = shapeMap.get(shapeId);
	      return (Shape) cachedShape.clone();
	   }

	   
	   public static void loadCache() {
		   // read from file, and start adding shapes
		   
			/*
			 * Circle circle = new Circle(); circle.setId("1");
			 * shapeMap.put(circle.getId(),circle);
			 */
	   }

}
