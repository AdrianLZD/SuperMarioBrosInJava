
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

public class Collision {

	public static boolean marioCollides(Point p, Bloque b) {
		return b.contains(p);
	}
	
	public static boolean objetoCollides(Rectangle r, Objeto o) {
		return o.intersects(r);
	}
	
	public static boolean enemigoCollides(Point p, Enemigos e) {
		return e.contains(p);
	}
	
	public static boolean outlineCollides(Rectangle r, Shape s) {
		return s.intersects(r);
	}
	
}
