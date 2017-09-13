
// This doodle program maintains persistence of the drawing but only up to a point.
// Persistence has a number of meanings in this context, for example:
//     Cover or minimize the image, does it come back the same? 
//     Turn off the program and restart it, does the last image get loaded and displayed or do you have to start over?
//     Can you load an even older version of a saved image?
// Step 1: Experiment and discover at least three areas that could be improved with regard to persistence of the image (not already identified above).
// Step 2: Add the ability to save and load the image.
// Step 3: Enhance this program with at least two of your ideas from step 1.
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Doodle2 extends JPanel {
	private static final long serialVersionUID = 1L;
	private MyCanvas doodlingArea;
	private JTextField[] mouseStates;
	private String[] text = { "Pressed", "Clicked", "Released", "Entered", "Exited", "Dragged", "X:", "Y:" };

	private Color drawColor = Color.black;
	private JButton saveBtn, clear, colorBtn;                          // addition *** 3a
	private JLabel coords;
	private ArrayList<MyLine> lines;
	private int x1, y1, x2, y2;
	
	
	public Doodle2() {
		this(800, 800);
	}

	public Doodle2(int w, int h) {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(w, h));
		doodlingArea = new MyCanvas(w, h);
		doodlingArea.addMouseListener(new MyMouseListener());
		doodlingArea.addMouseMotionListener(new MyMouseMotionListener());
		add("Center", doodlingArea);
		this.lines = new ArrayList<MyLine>();
		// Create the array of text fields.
		JPanel pNorth = new JPanel();
		mouseStates = new JTextField[8];
		for (int i = 0; i < mouseStates.length; i++) {
			mouseStates[i] = new JTextField(text[i], 10);
			mouseStates[i].setEditable(false);
			pNorth.add(mouseStates[i]);
		}
		JPanel pSouth = new JPanel();
		coords = new JLabel("(x, y)          ");
		pSouth.add(coords);
		clear = new JButton("Clear");
		clear.addActionListener(new MyActionListener());
		pSouth.add(clear);

		colorBtn = new JButton("Color");
		colorBtn.addActionListener(new MyActionListener());
		pSouth.add(colorBtn);
		
		saveBtn = new JButton("Save");
		saveBtn.addActionListener(new MyActionListener());
		pSouth.add(saveBtn);
				
		add("North", pNorth);
		add("South", pSouth);
	}

	/**
	 * The clearTextFields method sets all of the text backgrounds to light gray.
	 */
	
	public void saveLines() {
	    PrintWriter out = null;

	    try {
	        System.out.println("Entering" + " try statement");

	        out = new PrintWriter(new FileWriter("outfile.txt"));
	        for (int i = 0; i < lines.size(); i++) {
	        	MyLine line = lines.get(i);
	            out.println(line.getX1() + "," + line.getY1() + "," + 
	            			line.getX2() + "," + line.getY2() + "," +
	            			line.getDrawColor().getRGB());
	        }
	    } catch (IndexOutOfBoundsException e) {
	        System.err.println("Caught IndexOutOfBoundsException: "
	                           +  e.getMessage());
	                                 
	    } catch (IOException e) {
	        System.err.println("Caught IOException: " +  e.getMessage());
	                                 
	    } finally {
	        if (out != null) {
	            System.out.println("Closing PrintWriter");
	            out.close();
	        } 
	        else {
	            System.out.println("PrintWriter not open");
	        }
	    }
	}

	public void clearTextFields() {
		for (int i = 0; i < 6; i++)
			mouseStates[i].setBackground(Color.lightGray);
	}



	class MyCanvas extends Canvas {
		private static final long serialVersionUID = 2L;
		private Grid grid;
		public MyCanvas(int w, int h) {
			setBackground(Color.white);
			grid = new Grid(0, 0, w, h);
		}
		
		public void paint(Graphics g) {
			grid.display((Graphics2D)g);
			for (MyLine line: lines) {
				g.setColor(line.getDrawColor());
				g.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
			}
		}

	}
	
	// Inner listener classes
	class MyMouseListener implements MouseListener {
		public void mousePressed(MouseEvent e) {
			x1 = e.getX();
			y1 = e.getY();
			clearTextFields();
			mouseStates[0].setBackground(Color.yellow);
		}

		public void mouseClicked(MouseEvent e) {
			clearTextFields();
			mouseStates[1].setBackground(Color.yellow);
		}

		public void mouseEntered(MouseEvent e) {
			clearTextFields();
			mouseStates[3].setBackground(Color.yellow);
		}

		public void mouseExited(MouseEvent e) {
			clearTextFields();
			mouseStates[4].setBackground(Color.yellow);
		}

		public void mouseReleased(MouseEvent e) {
			
			clearTextFields();
			mouseStates[2].setBackground(Color.yellow);
		}
	}

	class MyMouseMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			x2 = e.getX();
			y2 = e.getY();
			lines.add(new MyLine(x1, y1, x2, y2, drawColor));
			doodlingArea.repaint();
			x1 = x2;
			y1 = y2;
			clearTextFields();
			mouseStates[5].setBackground(Color.yellow);
		}

		public void mouseMoved(MouseEvent e) {
			coords.setText("(" + e.getX() + ", " + e.getY() + ")");
			mouseStates[6].setText("X: " + e.getX());
			mouseStates[7].setText("Y: " + e.getY());
		}
	}

	class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String label = e.getActionCommand();
			if (label.equals("Clear")) {
				lines.clear();
				drawColor = Color.black;
				colorBtn.setBackground(clear.getBackground());
				;
				doodlingArea.repaint();
			}
			
			if (label.equals("Save")) {
				saveLines();
			}
			
			Object o = e.getSource();
			JButton b;
			if (o == colorBtn) {
				b = (JButton) o;
				drawColor = JColorChooser.showDialog(null, " Color Pallette", Color.cyan);
				b.setBackground(drawColor);
			}
		}
	}

	public static void main(String[] args) {                         // addition ***  1
		JFrame frame = new JFrame();

		frame.setSize(800, 800);
		frame.setTitle("Doodle Board");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Doodle2 sketchPad = new Doodle2();
		frame.add(sketchPad);

		frame.pack();
		frame.setVisible(true);
	}
}
