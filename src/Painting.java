import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
	private ArrayList<MyLine> lines;
	
	public Painting(int w, int h) {
		setBackground(Color.white);
		setPreferredSize(new Dimension(w,h));
	}
	
	public void paintComponent(Graphics g) {
		if (this.lines != null) {
			for (MyLine line: lines) {
				g.setColor(line.getDrawColor());
				g.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
			}
		}
	}
	
	private void openLines(File selectedFile) {
		this.lines = new ArrayList<MyLine>();
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		try {
			
			fileReader = new FileReader(selectedFile.getAbsolutePath());
			bufferedReader = new BufferedReader(fileReader);
			String curLine;
			while((curLine = bufferedReader.readLine()) != null) {
				String[] vals = curLine.split(",");
				MyLine line = new MyLine(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]), 
						  				 Integer.parseInt(vals[2]), Integer.parseInt(vals[3]), 
						  				 new Color(Integer.parseInt(vals[4])));
				this.lines.add(line);
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