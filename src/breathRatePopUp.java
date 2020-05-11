

import java.awt.EventQueue;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Dimension2D;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.plots.Plot;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.XYPlot.XYLegend;
import de.erichseifert.gral.plots.legends.AbstractLegend;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.DrawablePanel;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Shape;

import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JSeparator;

import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.geom.*;
import java.awt.FlowLayout;

public class breathRatePopUp {
	
	private static JFrame breathRateFrame;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void openPopUp(double bpm,double [] audioWav, double fs, double[] env, double[][] hard,double[][] mild, double [][] soft) {
	
		
		breathRateFrame = new JFrame();
		breathRateFrame.setTitle("Breathing Rate Analysis");
		breathRateFrame.setBounds(100, 100, 732, 424);
		breathRateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		breathRateFrame.getContentPane().setLayout(null);
		
		JLabel lblBreathingRate = new JLabel("Breathing Rate");
		lblBreathingRate.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblBreathingRate.setBounds(514, 46, 156, 36);
		breathRateFrame.getContentPane().add(lblBreathingRate);
		
		JLabel lblNewLabel_1 = new JLabel("Hard");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(567, 216, 40, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Soft");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(567, 249, 40, 20);
		breathRateFrame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Mild");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1.setBounds(567, 280, 40, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("Type of Breath");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setBounds(528, 187, 103, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("No.");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2_1.setBounds(653, 187, 31, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_2_1);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(514, 207, 192, 2);
		breathRateFrame.getContentPane().add(separator_1);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(641, 187, 2, 153);
		breathRateFrame.getContentPane().add(separator);
		
		JLabel breathingRateTxt = new JLabel();
		breathingRateTxt.setHorizontalAlignment(SwingConstants.CENTER);
		breathingRateTxt.setFont(new Font("Tahoma", Font.BOLD, 16));
		breathingRateTxt.setBackground(Color.WHITE);
		breathingRateTxt.setBounds(538, 93, 97, 38);
		breathRateFrame.getContentPane().add(breathingRateTxt);
		
		JLabel hardTxt = new JLabel();
		hardTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		hardTxt.setHorizontalAlignment(SwingConstants.CENTER);
		hardTxt.setBackground(SystemColor.menu);
		hardTxt.setBounds(644, 216, 40, 22);
		breathRateFrame.getContentPane().add(hardTxt);
		
		JLabel softTxt = new JLabel();
		softTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		softTxt.setHorizontalAlignment(SwingConstants.CENTER);
		softTxt.setBackground(SystemColor.menu);
		softTxt.setBounds(644, 248, 40, 22);
		breathRateFrame.getContentPane().add(softTxt);
		
		JLabel mildTxt = new JLabel();
		mildTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mildTxt.setHorizontalAlignment(SwingConstants.CENTER);
		mildTxt.setBackground(SystemColor.menu);
		mildTxt.setBounds(644, 280, 40, 22);
		breathRateFrame.getContentPane().add(mildTxt);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Total");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1.setBounds(567, 313, 40, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_1_1_1_1);
		
		JLabel totalTxt = new JLabel();
		totalTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalTxt.setHorizontalAlignment(SwingConstants.CENTER);
		totalTxt.setBackground(SystemColor.menu);
		totalTxt.setBounds(644, 313, 40, 22);
		breathRateFrame.getContentPane().add(totalTxt);
		
		//Organizar dados do sinal audio raw
		DataTable audioData = new DataTable(Double.class, Double.class);
		
		

		double[] time= new double[audioWav.length];
		for(int i=0; i<time.length;i++) {
			time[i]=i/fs;
			audioData.add(time[i],audioWav[i]);
		}

		//Create plot data
		XYPlot audioPlot = new XYPlot(audioData);
		//Config Axis
		audioPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time (s)"));
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Intensity"));
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).getLabel().setRotation(90);
		audioPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelDistance(0.2);
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelDistance(0.1);
		audioPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLength(0.2);
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLength(0.2);
		audioPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelsOutside(true);
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelsOutside(true);
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(0.5);
		audioPlot.getAxisRenderer(XYPlot.AXIS_X).setLabelDistance(0.1);
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabelDistance(0.4);
		audioPlot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setIntersection(-Double.MAX_VALUE);
		
		Number max=audioPlot.getAxis(XYPlot.AXIS_Y).getMax();
		audioPlot.getAxis(XYPlot.AXIS_Y).setMax((double)max+0.10*(double)max);
		
		//Add space for labels 
		double insetsTop = 0.0,
			   insetsLeft = 50.0, //to fit ticks and values
			   insetsBottom = 50.0, //to fit ticks and values
			   insetsRight = 120.0;
		audioPlot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));
		
		//Definição do painel onde ficará o sinal audio
		DrawablePanel audioPlotPanel = new DrawablePanel(audioPlot);
		audioPlotPanel.setBounds(10, 42, 467, 153);
		audioPlotPanel.setBackground(Color.WHITE);
		breathRateFrame.getContentPane().add(audioPlotPanel);
		
		//Lines and points config
		LineRenderer lines = new SmoothLineRenderer2D();
		PointRenderer points = new DefaultPointRenderer2D();
		Color blue=new Color(0,200,255);
		lines.setColor(blue); points.setColor(blue);
		lines.setStroke(new BasicStroke(1)); points.setShape(null);
		audioPlot.setLineRenderers(audioData, lines);
		audioPlot.setPointRenderers(audioData, points);
		
		
		//Organize envelope data 
		DataTable envData = new DataTable(Double.class, Double.class);
		DataTable hardData = new DataTable(Double.class, Double.class);
		DataTable softData = new DataTable(Double.class, Double.class);
		DataTable mildData = new DataTable(Double.class, Double.class);
		
		if (hard==null) {
			hard=new double[0][0];
		}
		if (soft==null) {
			soft=new double[0][0];
		}
		if (mild==null) {
			mild=new double[0][0];
		}
		for(int i=0; i<time.length;i++) {
			time[i]=i/fs;
			envData.add(time[i],env[i]);
		}
		for(int i=0; i<hard.length; i++) {
			hardData.add(hard[i][1],(hard[i][0]));
		}
		for(int j=0; j<soft.length; j++) {
			softData.add(soft[j][1],(soft[j][0]));
		}
		for(int k=0; k<mild.length; k++) {
			mildData.add(mild[k][1],(mild[k][0]));
		}
		
		//Create plot data~
		//We could've used envData to form the plot; but DataSource variables allow better-looking legends
		DataSource envSource=new DataSeries("Envelope",envData);
		DataSource hardSource=new DataSeries("Hard",hardData);
		DataSource mildSource=new DataSeries("Mild",mildData);
		DataSource softSource=new DataSeries("Soft",softData);
		
		XYPlot envPlot = new XYPlot(envSource,hardSource,softSource,mildSource);

		//Config Axis
		envPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time (s)"));
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Intensity"));
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).getLabel().setRotation(90);
		envPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelDistance(0.2);
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelDistance(0.1);
		envPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLength(0.2);
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLength(0.2);
		envPlot.getAxisRenderer(XYPlot.AXIS_X).setTickLabelsOutside(true);
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickLabelsOutside(true);
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(0.5);
		envPlot.getAxisRenderer(XYPlot.AXIS_X).setLabelDistance(0.1);
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabelDistance(0.4);
		envPlot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setIntersection(-Double.MAX_VALUE);
		
		Number max1=envPlot.getAxis(XYPlot.AXIS_Y).getMax();
		envPlot.getAxis(XYPlot.AXIS_Y).setMax((double)max1+0.10*(double)max1); //increase Y axis maximum by 10%
		
		//Space for labels and legends, with the same size as audioPlot to look better
		envPlot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));
		
		//Create legends
		envPlot.setLegendVisible(true);
		envPlot.getLegend().setFont(new Font("Trebuchet MS",10, 10));
		envPlot.setLegendLocation(Location.EAST);
		envPlot.getLegend().setBorderColor(new Color(255,255,255));
		//envPlot.getLegend().setPosition(insetsRight, insetsRight);
		//envLegend.setBounds(358, 294, 57, 80);
		//envLegend.setBackground(Color.WHITE);
		//Create panel for plot
		DrawablePanel envPlotPanel = new DrawablePanel(envPlot);
		envPlotPanel.setBounds(10, 232, 467, 153);
		envPlotPanel.setBackground(Color.WHITE);
		breathRateFrame.getContentPane().add(envPlotPanel);
		envPlotPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
		//Lines and points config for envelope line
		LineRenderer envLines = new SmoothLineRenderer2D();
		envLines.setColor(blue); points.setColor(blue);
		envLines.setStroke(new BasicStroke(1)); 
		
		envPlot.setLineRenderers(envSource, envLines);
		envPlot.setPointRenderers(envSource, points);
		
		//Mark hard, mild, soft breath points
		PointRenderer hardPoints = new DefaultPointRenderer2D();
		PointRenderer softPoints = new DefaultPointRenderer2D();
		PointRenderer mildPoints = new DefaultPointRenderer2D();
		Color red=new Color(255,0,0);Color green=new Color(10,255,10);
		Shape cross=createRegularCross(5,1);
		hardPoints.setColor(red); hardPoints.setShape(cross);
		softPoints.setColor(green); softPoints.setShape(cross);
		mildPoints.setColor(new Color(255,255,0)); mildPoints.setShape(cross);
		envPlot.setPointRenderers(hardSource, hardPoints);
		envPlot.setPointRenderers(softSource, softPoints);
		envPlot.setPointRenderers(mildSource, mildPoints);
		
		
		//Write text to text boxes
		int numberOfHardBreahts=hard.length;
		int numberOfSoftBreahts=soft.length;
		int numberOfMildBreahts=mild.length;
		int totalBreaths=numberOfHardBreahts+numberOfSoftBreahts+numberOfMildBreahts;
		hardTxt.setText(String.valueOf(numberOfHardBreahts));
		softTxt.setText(String.valueOf(numberOfSoftBreahts));
		mildTxt.setText(String.valueOf(numberOfMildBreahts));
		totalTxt.setText(String.valueOf(totalBreaths));
		breathingRateTxt.setText(String.valueOf((int)bpm)+" bpm");
		
		JLabel audioPlotTitle = new JLabel("Audio Signal");
		audioPlotTitle.setHorizontalAlignment(SwingConstants.CENTER);
		audioPlotTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		audioPlotTitle.setBounds(157, 11, 97, 22);
		breathRateFrame.getContentPane().add(audioPlotTitle);
		
		JLabel envPlotTitle = new JLabel("Envelope");
		envPlotTitle.setHorizontalAlignment(SwingConstants.CENTER);
		envPlotTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		envPlotTitle.setBounds(157, 199, 97, 22);
		breathRateFrame.getContentPane().add(envPlotTitle);
		
		breathRateFrame.setVisible(true);
	}
	
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
	

}
