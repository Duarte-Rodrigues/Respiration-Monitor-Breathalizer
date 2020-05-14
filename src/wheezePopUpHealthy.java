

import java.awt.EventQueue;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.XYPlot.XYNavigationDirection;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.DrawablePanel;
import de.erichseifert.gral.ui.InteractivePanel;
import sun.awt.image.ToolkitImage;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;

import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JSeparator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.TexturePaint;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Insets2D;

public class wheezePopUpHealthy {
	
	private static JFrame wheezeFrame;
	
	  /**
	   * Creates a diagonal cross shape.
	   *
	   * @param l  the length of each 'arm'.
	   * @param t  the thickness.
	   *
	   * @return A diagonal cross shape.
	   */
	  public static Shape createRegularCross(final float l, final float t) {
	      final GeneralPath p0 = new GeneralPath();
	      p0.moveTo(-l, t);
	      p0.lineTo(-t, t);
	      p0.lineTo(-t, l);
	      p0.lineTo(t, l);
	      p0.lineTo(t, t);
	      p0.lineTo(l, t);
	      p0.lineTo(l, -t);
	      p0.lineTo(t, -t);
	      p0.lineTo(t, -l);
	      p0.lineTo(-t, -l);
	      p0.lineTo(-t, -t);
	      p0.lineTo(-l, -t);
	      p0.closePath();
	      return p0;
	  }
	
	/**
	 * @throws IOException 
	 * @wbp.parser.entryPoint
	 */
	public void openPopUp(double[] x, String state, double fs, String normSpectrumPath) throws IOException {
	
		
		wheezeFrame = new JFrame();
		wheezeFrame.setTitle("Wheeze Analysis");
		wheezeFrame.setBounds(100, 100, 706, 424);
		wheezeFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		wheezeFrame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Diagnostic");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(553, 194, 97, 34);
		wheezeFrame.getContentPane().add(lblNewLabel);
		
		JLabel DiagnosticTxtBox = new JLabel(state);
		DiagnosticTxtBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		DiagnosticTxtBox.setHorizontalAlignment(SwingConstants.CENTER);
		DiagnosticTxtBox.setBounds(553, 228, 97, 40);
		wheezeFrame.getContentPane().add(DiagnosticTxtBox);
		
		JLabel lblNewLabel_2 = new JLabel("Wheezing Intervals");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(465, 87, 185, 27);
		wheezeFrame.getContentPane().add(lblNewLabel_2);
		
		JLabel wheezingIntTxtBox = new JLabel("No wheezes detected");
		wheezingIntTxtBox.setHorizontalAlignment(SwingConstants.CENTER);
		wheezingIntTxtBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		wheezingIntTxtBox.setBounds(454, 77, 185, 116);
		wheezeFrame.getContentPane().add(wheezingIntTxtBox);
		
		
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++PLOTS++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//Add space for labels 
		double insetsTop = 10.0,
			   insetsLeft = 50.0, //to fit ticks and values
			   insetsBottom = 30.0, //to fit ticks and values
			   insetsRight = 120.0;
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++PLOT SIGNAL SPECTRUM+++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		DataTable dummyData = new DataTable(Double.class, Double.class);
		
		//This is data that just allows to create the plot whose background will be the image we want
		dummyData.add(0.0,0.0);
		XYPlot dummyPlot = new XYPlot(dummyData);
		
		
		PointRenderer dummyPoints = new DefaultPointRenderer2D(); dummyPoints.setShape(null);
		LineRenderer dummyLines=new SmoothLineRenderer2D(); dummyLines.setColor(new Color(0,0,0,0));
		dummyPlot.setPointRenderers(dummyData, dummyPoints);
		dummyPlot.setLineRenderers(dummyData, dummyLines);
		
		
		//Config axis and labels
		dummyPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time (s)"));
		dummyPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Frequency"));
		dummyPlot.getAxisRenderer(XYPlot.AXIS_Y).getLabel().setRotation(90);
		dummyPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelDistance(0.2);
		dummyPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelDistance(0.1);
		dummyPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLength(0.2);
		dummyPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelsOutside(true);
		dummyPlot.getAxisRenderer(XYPlot.AXIS_X).setLabelDistance(0.1);
		dummyPlot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
		dummyPlot.getAxisRenderer(XYPlot.AXIS_Y).setMinorTicksVisible(false);
		
		dummyPlot.getAxis(XYPlot.AXIS_Y).setMax(1001);
		dummyPlot.getAxis(XYPlot.AXIS_X).setMax(10.1);
		
		//Set insets for labels
		dummyPlot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));
		
		
		//Set panel with the plot
		InteractivePanel spectrumPanel = new InteractivePanel(dummyPlot);
		spectrumPanel.setBounds(10, 49, 500, 325);
		spectrumPanel.setBackground(Color.WHITE);
		spectrumPanel.setLayout(null);
		
		//Try to get spectrum image from the path gotten as a parameter (has a throw to IO error) and place it in the plot area
		double spectrumLblWidth=spectrumPanel.getWidth();
		double spectrumLblHeight=spectrumPanel.getHeight();
		double spectrumLblX=spectrumPanel.getX();
		double spectrumLblY=spectrumPanel.getY();
		
		JLabel specImgLabel = new JLabel("");
		specImgLabel.setBounds((int) (spectrumLblX+insetsLeft), (int) (spectrumLblY+insetsTop),(int) (spectrumLblWidth-insetsLeft-insetsRight), (int) (spectrumLblHeight-insetsTop-insetsBottom)); //now the label as the same size as the plot area and the labels are visible
		wheezeFrame.getContentPane().add(specImgLabel);
		
		BufferedImage img = ImageIO.read(new File(normSpectrumPath));
		Image resizedImg=img.getScaledInstance(specImgLabel.getWidth(),specImgLabel.getHeight(),Image.SCALE_SMOOTH);
		ImageIcon spectrumImg=new ImageIcon(resizedImg);
		specImgLabel.setIcon(spectrumImg);
	
		wheezeFrame.getContentPane().add(spectrumPanel);

		JLabel lblNewLabel_1 = new JLabel("Audio Signal Spectrum");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(134, 10, 161, 21);
		wheezeFrame.getContentPane().add(lblNewLabel_1);
		
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		wheezeFrame.setVisible(true);
	}
	
	  private double max(double [] array) {
	      double max = 0;
	     
	      for(int i=0; i<array.length; i++ ) {
	         if(array[i]>max) {
	            max = array[i];
	         }
	      }
	      return max;
	   }
	  
	  private double min(double [] array) {
	      double min = 0;
	     
	      for(int i=0; i<array.length; i++ ) {
	         if(array[i]<min) {
	            min = array[i];
	         }
	      }
	      return min;
	   }
	  
	  private static BufferedImage convert(BufferedImage src, int bufImgType) {
		    BufferedImage img= new BufferedImage(src.getWidth(), src.getHeight(), bufImgType);
		    Graphics2D g2d= img.createGraphics();
		    g2d.drawImage(src, 0, 0, null);
		    g2d.dispose();
		    return img;
		}
}
