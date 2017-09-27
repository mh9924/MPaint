/**
 * @author Matthew Harris and Andrew Gemuenden CSC331-001 Prof. J. Tompkins
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class Painting extends JComponent {
	private static final long serialVersionUID = 1L;
	private ArrayList<MyLine> l_lines;
	private ArrayList<ColoredShape> shapes;
	
	public Painting(int w, int h) {
		this.l_lines = new ArrayList<MyLine>();
		this.shapes = new ArrayList<ColoredShape>();
		setBackground(Color.white);
		setPreferredSize(new Dimension(w,h));
	}
	
	public void paintComponent(Graphics g) {
		for (MyLine line : this.l_lines) {
			g.setColor(line.getDrawColor());
			g.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
		}
		Graphics2D g2 = (Graphics2D) g;
		for (ColoredShape shape : this.shapes) {
			g2.setColor(new Color(shape.getDrawColor()));
			g2.draw(shape.s);
			if (shape.getFillColor() != -1) {
				g2.setColor(new Color(shape.getFillColor()));
				g2.fill(shape.s);
			}
		}
	}
	
	
	private void openLines(File selectedFile) {
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		try {
			
			fileReader = new FileReader(selectedFile.getAbsolutePath());
			bufferedReader = new BufferedReader(fileReader);
			String curLine;
			while((curLine = bufferedReader.readLine()) != null) {
				String[] vals = curLine.split(",");
				if(vals.length == 5) {  // We are reading in a line from legacy file (HW2), so just use MyLine objects.
					
					MyLine line = new 
							MyLine(Integer.parseInt(vals[0].trim()), Integer.parseInt(vals[1].trim()), 
									Integer.parseInt(vals[2].trim()), Integer.parseInt(vals[3].trim()), 
									new Color(Integer.parseInt(vals[4].trim())));
					this.l_lines.add(line);
					
				} else {  // We are reading in a HW4 file with tokens - use Line2D and circles.
					
					int token = Integer.parseInt(vals[0].trim());
					if (token == Tokens.LINE) {
						ColoredShape s = new 
								ColoredShape(new Line2D.Double(Integer.parseInt(vals[1].trim()),
								Integer.parseInt(vals[2].trim()),
								Integer.parseInt(vals[3].trim()),
								Integer.parseInt(vals[4].trim())),
								Integer.parseInt(vals[5].trim()), -1);
						this.shapes.add(s);
					} else if (token == Tokens.CIRCLE) {
						ColoredShape s = new 
								ColoredShape(new Ellipse2D.Double(Integer.parseInt(vals[1].trim()),
								Integer.parseInt(vals[2].trim()),
								Integer.parseInt(vals[3].trim()),
								Integer.parseInt(vals[3].trim())),
								Integer.parseInt(vals[4].trim()), Integer.parseInt(vals[5].trim()));
						this.shapes.add(s);
					} else if (token == Tokens.RECT) {
						ColoredShape s = new 
								ColoredShape(new Rectangle2D.Double(Integer.parseInt(vals[1].trim()),
								Integer.parseInt(vals[2].trim()),
								Integer.parseInt(vals[3].trim()),
								Integer.parseInt(vals[4].trim())),
								Integer.parseInt(vals[5].trim()), Integer.parseInt(vals[6].trim()));
						this.shapes.add(s);
					}
					
				}
			}
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			
		} finally {
			
			try {
				
				if (bufferedReader != null) bufferedReader.close();
				if (fileReader != null) fileReader.close();
				
			} catch (IOException e) {
				
				System.out.println(e.getMessage());
				
			}
		}
	}
	
	public final class Tokens {
		public static final int LINE = 0;
		public static final int CIRCLE = 1;
		public static final int RECT = 2;
		public static final int ARC = 3;
		public static final int CHARS = 4;
		public static final int OVAL = 5;
		public static final int POLYGON = 6;
		public static final int POLYLINE = 7;
		public static final int ROUNDRECT = 8;
		public static final int PATHS = 9;
		public static final int ELLIPSES = 10;
		public static final int CUBICCURVE = 11;
		public static final int QUADCURVE = 12;
	}
	
	public class ColoredShape {
		public Shape s;
		private int drawcolor;
		private int fillcolor;
		
		public ColoredShape(Shape shape, int drawcolor, int fillcolor) {
			this.s = shape;
			this.drawcolor = drawcolor;
			this.fillcolor = fillcolor;
		}
		
		public int getDrawColor() { return this.drawcolor; }
		
		public int getFillColor() { return this.fillcolor; }
		
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Painting painting = new Painting(800, 800);
		
		frame.setSize(800, 800);
		frame.setTitle("Painting");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(painting, BorderLayout.CENTER);
		frame.setVisible(true);
		
		JFileChooser jfc = new JFileChooser(".");
		jfc.showOpenDialog(null);
		File file = jfc.getSelectedFile();
		if(file == null) System.exit(0);
		
		painting.openLines(file);
		painting.repaint();
	}
}