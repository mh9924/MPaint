import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	private ArrayList<ColoredLine2D> lines;
	private ArrayList<ColoredEllipse2D> circles;
	private ArrayList<ColoredRectangle2D> rects;
	
	public Painting(int w, int h) {
		this.l_lines = new ArrayList<MyLine>();
		this.lines = new ArrayList<ColoredLine2D>();
		this.circles = new ArrayList<ColoredEllipse2D>();
		this.rects = new ArrayList<ColoredRectangle2D>();
		setBackground(Color.white);
		setPreferredSize(new Dimension(w,h));
	}
	
	public void paintComponent(Graphics g) {
		for (MyLine line : this.l_lines) {
			g.setColor(line.getDrawColor());
			g.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
		}
		Graphics2D g2 = (Graphics2D) g;
		for (ColoredLine2D line : this.lines) {
			g2.setColor(new Color(line.getDrawColor()));
			g2.draw(line);
		}
		for (ColoredEllipse2D circle : this.circles) {
			g2.setColor(new Color(circle.getDrawColor()));
			g2.draw(circle);
			g2.setColor(new Color(circle.getFillColor()));
			if(circle.getFillColor() != -1) { g2.fill(circle); }
		}
		for (ColoredRectangle2D rect : this.rects) {
			g2.setColor(new Color(rect.getDrawColor()));
			g2.draw(rect);
			g2.setColor(new Color(rect.getFillColor()));
			if(rect.getFillColor() != -1) { g2.fill(rect); }
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
						ColoredLine2D line = new 
								ColoredLine2D(Integer.parseInt(vals[1].trim()),
								Integer.parseInt(vals[2].trim()),
								Integer.parseInt(vals[3].trim()),
								Integer.parseInt(vals[4].trim()),
								Integer.parseInt(vals[5].trim()));														   
						this.lines.add(line);
					} else if (token == Tokens.CIRCLE) {
						ColoredEllipse2D circle = new 
								ColoredEllipse2D(Integer.parseInt(vals[1].trim()),
								Integer.parseInt(vals[2].trim()),
								Integer.parseInt(vals[3].trim()),
								Integer.parseInt(vals[4].trim()),
								Integer.parseInt(vals[5].trim()));
						this.circles.add(circle);
					} else if (token == Tokens.RECT) {
						ColoredRectangle2D rect = new 
								ColoredRectangle2D(Integer.parseInt(vals[1].trim()),
								Integer.parseInt(vals[2].trim()),
								Integer.parseInt(vals[3].trim()),
								Integer.parseInt(vals[4].trim()),
								Integer.parseInt(vals[5].trim()),
								Integer.parseInt(vals[6].trim()));
						this.rects.add(rect);
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
	}
	
	public class ColoredLine2D extends Line2D.Double {
		private static final long serialVersionUID = 1L;
		private int drawcolor;
		
		public ColoredLine2D(int x1, int x2, int y1, int y2, int drawcolor) {
			super(x1,x2,y1,y2);
			this.drawcolor = drawcolor;
		}
		
		public int getDrawColor() { return this.drawcolor; }
		
	}
	
	public class ColoredEllipse2D extends Ellipse2D.Double {
		private static final long serialVersionUID = 1L;
		private int drawcolor;
		private int fillcolor;
		
		public ColoredEllipse2D(int x, int y, int width, int height, int drawcolor, int fillcolor) {
			super(x,y,width,height);
			this.drawcolor = drawcolor;
			this.fillcolor = fillcolor;
		}
		
		// Constructor for circle
		public ColoredEllipse2D(int x, int y, int diameter, int drawcolor, int fillcolor) {
			super(x,y,diameter,diameter);
			this.drawcolor = drawcolor;
			this.fillcolor = fillcolor;
		}
		
		public int getDrawColor() { return this.drawcolor; }
		
		public int getFillColor() { return this.fillcolor; }

	}
	
	public class ColoredRectangle2D extends Rectangle2D.Double {
		private static final long serialVersionUID = 1L;
		private int drawcolor;
		private int fillcolor;
		
		public ColoredRectangle2D(int x, int y, int width, int height, int drawcolor, int fillcolor) {
			super(x,y,width,height);
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
		
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(null);
		File file = jfc.getSelectedFile();
		if(file == null) System.exit(0);
		
		painting.openLines(file);
		painting.repaint();
	}
}