

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
import java.awt.Image;
import java.awt.Shape;

import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JSeparator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
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

public class wheezePopUp {
	
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
	public void openPopUp(double[] x, String state, double fs, String normSpectrumPath, String wheezeSpectrumPath,double[] delta_t,double nf) throws IOException {
	
		
		wheezeFrame = new JFrame();
		wheezeFrame.setTitle("Wheeze Analysis");
		wheezeFrame.setBounds(100, 100, 706, 424);
		wheezeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wheezeFrame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Diagnostic");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(553, 211, 97, 34);
		wheezeFrame.getContentPane().add(lblNewLabel);
		
		JLabel DiagnosticTxtBox = new JLabel(state);
		DiagnosticTxtBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		DiagnosticTxtBox.setHorizontalAlignment(SwingConstants.CENTER);
		DiagnosticTxtBox.setBounds(553, 257, 97, 40);
		wheezeFrame.getContentPane().add(DiagnosticTxtBox);
		
		JLabel lblNewLabel_2 = new JLabel("Wheezing Intervals");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(444, 39, 185, 34);
		wheezeFrame.getContentPane().add(lblNewLabel_2);
		
		JLabel wheezingIntTxtBox = new JLabel("");
		wheezingIntTxtBox.setHorizontalAlignment(SwingConstants.CENTER);
		wheezingIntTxtBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		wheezingIntTxtBox.setBounds(454, 84, 185, 116);
		wheezeFrame.getContentPane().add(wheezingIntTxtBox);
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++PLOTS++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//Organizar dados do sinal audio raw
		DataTable audioData = new DataTable(Double.class, Double.class);
	
		double[] time= new double[x.length];
		for(int i=0; i<time.length;i++) {
			time[i]=i/fs;
			double normalized_x=(x[i]-min(x))/(max(x)-min(x));	
			audioData.add(time[i],normalized_x);
		}
		
		//Organize wheeze activity data
		DataTable wheezeData = new DataTable(Double.class, Double.class);
		
		for(int i=0; i<delta_t.length;i++) {
			wheezeData.add(time[i],delta_t[i]);
		}
		
		//Create plot data
		DataSource audioSource=new DataSeries("Envelope",audioData);
		DataSource wheezeSource=new DataSeries("Wheeze",wheezeData);
		XYPlot wheezeActivityPlot = new XYPlot(audioSource,wheezeSource);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time (s)"));
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Intensity"));
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_Y).getLabel().setRotation(90);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelDistance(0.2);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelDistance(0.1);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLength(0.2);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLength(0.2);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelsOutside(true);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelsOutside(true);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(0.5);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_X).setLabelDistance(0.1);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabelDistance(0.4);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
		wheezeActivityPlot.getAxisRenderer(XYPlot.AXIS_Y).setIntersection(-Double.MAX_VALUE);
		
		wheezeActivityPlot.getAxis(XYPlot.AXIS_Y).setMax(1.1);
		
		//Add space for labels 
		double insetsTop = 10.0,
			   insetsLeft = 50.0, //to fit ticks and values
			   insetsBottom = 50.0, //to fit ticks and values
			   insetsRight = 120.0;
		wheezeActivityPlot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));

		//Legends
		wheezeActivityPlot.setLegendVisible(true);
		wheezeActivityPlot.setLegendLocation(Location.EAST);
		wheezeActivityPlot.getLegend().setBorderColor(new Color(255,255,255));
		//Definição do painel onde ficará o sinal audio
		InteractivePanel wheezeActivityPanel = new InteractivePanel(wheezeActivityPlot);
		wheezeActivityPanel.setBounds(10, 228, 500, 146);
		wheezeActivityPanel.setBackground(Color.WHITE);
		wheezeFrame.getContentPane().add(wheezeActivityPanel);
		
		
		//Lines and points config
		LineRenderer audioLines = new SmoothLineRenderer2D();
		LineRenderer wheezeLines = new SmoothLineRenderer2D();
		PointRenderer audioPoints = new DefaultPointRenderer2D();
		PointRenderer wheezePoints = new DefaultPointRenderer2D();
		Color blue=new Color(0,200,255); Color red=new Color(255,0,0);
		audioLines.setColor(blue); audioPoints.setColor(blue);
		audioLines.setStroke(new BasicStroke(1)); audioPoints.setShape(null);
		
		wheezeLines.setColor(red); wheezePoints.setColor(red);
		wheezeLines.setStroke(new BasicStroke(1)); wheezePoints.setShape(null);
		
		wheezeActivityPlot.setLineRenderers(audioSource, audioLines);
		wheezeActivityPlot.setPointRenderers(audioSource, audioPoints);
		
		wheezeActivityPlot.setLineRenderers(wheezeSource, wheezeLines);
		wheezeActivityPlot.setPointRenderers(wheezeSource, wheezePoints);
		
		DataTable dummyData = new DataTable(Double.class, Double.class);
		
		//This is data that just allows to create the plot whose background will be the image we want
		dummyData.add(0.0,0.0);
		XYPlot spectrumPlot = new XYPlot(dummyData);
		
		PointRenderer dummyPoints = new DefaultPointRenderer2D(); dummyPoints.setShape(null);
		
		//Try to get spectrum image from the path gotten as a parameter (has a throw to IO error)
		BufferedImage spectrumImage = ImageIO.read(new File(normSpectrumPath));
		
		//BufferedImage rgbSpectrumImage=new BufferedImage(spectrumImage.getWidth(),spectrumImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
		TexturePaint spectrumPaint=new TexturePaint(spectrumImage, new Rectangle2D.Double(0.0, 0.0, 1.0, 1.0));
		
		//Config axis and labels
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time (s)"));
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Frequency"));
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_Y).getLabel().setRotation(90);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelDistance(0.2);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelDistance(0.1);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLength(0.2);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLength(0.2);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelsOutside(true);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelsOutside(true);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(0.5);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_X).setLabelDistance(0.1);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabelDistance(0.4);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
		spectrumPlot.getAxisRenderer(XYPlot.AXIS_Y).setIntersection(-Double.MAX_VALUE);
		
		spectrumPlot.getAxis(XYPlot.AXIS_Y).setMax(1000);
		spectrumPlot.getAxis(XYPlot.AXIS_X).setMax(10);
		
		//Set insets for labels
		spectrumPlot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));
		
		//Set spectrum img from matlab as the plot's background
		spectrumPlot.getPlotArea().setBackground(spectrumPaint);
		
		//Set panel with the plot
		InteractivePanel spectrumPanel = new InteractivePanel(spectrumPlot);
		spectrumPanel.setBounds(10, 54, 500, 146);
		spectrumPanel.setBackground(Color.white);
		wheezeFrame.getContentPane().add(spectrumPanel);

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
}
