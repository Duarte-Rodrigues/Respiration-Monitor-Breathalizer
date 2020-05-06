

import java.awt.EventQueue;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.plots.XYPlot;
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
import java.awt.geom.GeneralPath;

public class breathRatePopUp {
	
	private static JFrame breathRateFrame;
	
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
	 * @wbp.parser.entryPoint
	 */
	public void openPopUp(double bpm,double [] audioWav, double fs, double[] env, double[][] hard,double[][] mild, double [][] soft) {
	
		
		breathRateFrame = new JFrame();
		breathRateFrame.setTitle("Breathing Rate Analysis");
		breathRateFrame.setBounds(100, 100, 618, 424);
		breathRateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		breathRateFrame.getContentPane().setLayout(null);
		
		JLabel lblBreathingRate = new JLabel("Breathing Rate");
		lblBreathingRate.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblBreathingRate.setBounds(449, 55, 119, 36);
		breathRateFrame.getContentPane().add(lblBreathingRate);
		
		JLabel lblNewLabel_1 = new JLabel("Hard");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(449, 216, 40, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Soft");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(449, 249, 40, 20);
		breathRateFrame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Mild");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1.setBounds(449, 280, 40, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("Type of Breath");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setBounds(413, 187, 103, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("No.");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2_1.setBounds(537, 187, 31, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_2_1);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(400, 207, 192, 2);
		breathRateFrame.getContentPane().add(separator_1);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(520, 187, 2, 153);
		breathRateFrame.getContentPane().add(separator);
		
		JLabel breathingRateTxt = new JLabel();
		breathingRateTxt.setHorizontalAlignment(SwingConstants.CENTER);
		breathingRateTxt.setFont(new Font("Tahoma", Font.BOLD, 16));
		breathingRateTxt.setBackground(Color.WHITE);
		breathingRateTxt.setBounds(459, 102, 97, 38);
		breathRateFrame.getContentPane().add(breathingRateTxt);
		
		JLabel hardTxt = new JLabel();
		hardTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		hardTxt.setHorizontalAlignment(SwingConstants.CENTER);
		hardTxt.setBackground(SystemColor.menu);
		hardTxt.setBounds(537, 216, 40, 22);
		breathRateFrame.getContentPane().add(hardTxt);
		
		JLabel softTxt = new JLabel();
		softTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		softTxt.setHorizontalAlignment(SwingConstants.CENTER);
		softTxt.setBackground(SystemColor.menu);
		softTxt.setBounds(537, 249, 40, 22);
		breathRateFrame.getContentPane().add(softTxt);
		
		JLabel mildTxt = new JLabel();
		mildTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mildTxt.setHorizontalAlignment(SwingConstants.CENTER);
		mildTxt.setBackground(SystemColor.menu);
		mildTxt.setBounds(537, 280, 40, 22);
		breathRateFrame.getContentPane().add(mildTxt);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Total");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1.setBounds(449, 313, 40, 22);
		breathRateFrame.getContentPane().add(lblNewLabel_1_1_1_1);
		
		JLabel totalTxt = new JLabel();
		totalTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalTxt.setHorizontalAlignment(SwingConstants.CENTER);
		totalTxt.setBackground(SystemColor.menu);
		totalTxt.setBounds(537, 313, 40, 22);
		breathRateFrame.getContentPane().add(totalTxt);
		
		JLabel lblTimes = new JLabel("Time (s)");
		lblTimes.setBounds(174, 348, 46, 14);
		breathRateFrame.getContentPane().add(lblTimes);
		
		JLabel lblTimes_1 = new JLabel("Time (s)");
		lblTimes_1.setBounds(174, 164, 46, 14);
		breathRateFrame.getContentPane().add(lblTimes_1);
		
		JLabel lblBreathingRate_1 = new JLabel("Audio Signal");
		lblBreathingRate_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblBreathingRate_1.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		lblBreathingRate_1.setBounds(138, 11, 119, 20);
		breathRateFrame.getContentPane().add(lblBreathingRate_1);
		
		JLabel lblBreathingRate_1_1 = new JLabel("Envelope");
		lblBreathingRate_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblBreathingRate_1_1.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		lblBreathingRate_1_1.setBounds(138, 189, 119, 20);
		breathRateFrame.getContentPane().add(lblBreathingRate_1_1);
		
		//Organizar dados do sinal audio raw
		DataTable audioData = new DataTable(Double.class, Double.class);
		
		

		double[] time= new double[audioWav.length];
		for(int i=0; i<time.length;i++) {
			time[i]=i/fs;
			audioData.add(time[i],audioWav[i]);
		}

		//Create plot data
		XYPlot audioPlot = new XYPlot(audioData);
		audioPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time(s)"));
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Intensity"));
		//audioData.add(0.0,Arrays.stream(audioWav).max().getAsDouble()*1.2); //increases Y axis length
		
		//Definição do painel onde ficará o sinal audio
		DrawablePanel audioPlotPanel = new DrawablePanel(audioPlot);
		audioPlotPanel.setBounds(37, 37, 319, 126);
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
		
		//Create plot data
		XYPlot envPlot = new XYPlot(envData,hardData,softData,mildData);
		envPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time(s)"));
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Normalized Intensity"));
		//double maxY=Arrays.stream(env).max().getAsDouble()*1.2;
		//envData.add(0.0,maxY);
		
		//Define panel where env will be
		DrawablePanel envPlotPanel = new DrawablePanel(envPlot);
		envPlotPanel.setBounds(37, 216, 319, 132);
		envPlotPanel.setBackground(Color.WHITE);
		breathRateFrame.getContentPane().add(envPlotPanel);
		
		//Lines and points config for envelope line
		lines.setColor(blue); points.setColor(blue);
		lines.setStroke(new BasicStroke(1)); points.setShape(null);
		envPlot.setLineRenderers(envData, lines);
		envPlot.setPointRenderers(envData, points);
		
		//Mark hard, mild, soft breath points
		PointRenderer hardPoints = new DefaultPointRenderer2D();
		PointRenderer softPoints = new DefaultPointRenderer2D();
		PointRenderer mildPoints = new DefaultPointRenderer2D();
		Color red=new Color(255,0,0);Color green=new Color(10,255,10);
		Shape cross=createRegularCross(5,1);
		hardPoints.setColor(red); hardPoints.setShape(cross);
		softPoints.setColor(green); softPoints.setShape(cross);
		mildPoints.setColor(new Color(255,255,0)); mildPoints.setShape(cross);
		envPlot.setPointRenderers(hardData, hardPoints);
		envPlot.setPointRenderers(softData, softPoints);
		envPlot.setPointRenderers(mildData, mildPoints);
		
		
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
		
		breathRateFrame.setVisible(true);
	}
	

}
