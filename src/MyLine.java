/**
 * @author Matthew Harris and Andrew Gemuenden CSC331-001 Prof. J. Tompkins
 */
import java.awt.Color;
import java.awt.Point;


public class MyLine {
	private int x1, x2, y1, y2;
	private Point p1, p2;
	private Color c;
	
	public MyLine(int x1, int y1, int x2, int y2, Color c) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.p1 = new Point(x1, y1);
		this.p2 = new Point(x2, y2);
		this.c = c;
	}
	
	public int getX1() {
		return this.x1;
	}
	
	public int getX2() {
		return this.x2;
	}
	
	public int getY1() {
		return this.y1;
	}
	
	public int getY2() {
		return this.y2;
	}
	
	public Point getP1() {
		return this.p1;
	}
	
	public Point getP2() {
		return this.p2;
	}
	
	public Color getDrawColor() {
		return this.c;
	}
}
