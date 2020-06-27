/**
 * LIEB PROJECT 2019/2020
 * BREATHALIZER - Breathing Monitor
 * @author Duarte Rodrigues
 * @author João Fonseca
 * 
 * wheezePopUpUnhealthy: Class that pops-up the information about the wheezes detection in an unhealthy diagnostic.
 * The pop-up shows the signal and wheezes spetral content. Along side with that it also shows the intervals in which the wheezes occur.
 * This can be visualized in audio plot with the highlighted wheezes or in a discrete list.
 */

import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.DrawablePanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import de.erichseifert.gral.graphics.Insets2D;
import javax.swing.JScrollPane;

public class wheezePopUpUnhealthy {

	private static JFrame wheezeFrame;
	public static DrawablePanel wheezeActivityPanel;

	/**
	 * Method to display the pop-up with the the information about the wheezes detection in an unhealthy diagnostic, 
	 * extracted from MATLAB.
	 * 
	 * @param x                   wheeze activity waveform data.
	 * @param state				  diagnostic of the patient.
	 * @param fs				  sampling frequency.
	 * @param wheezeSpectrumPath  path relative to the spectrum content of the wheeze activity.
	 * @param normSpectrumPath    path relative to the spectrum content of the signal.
	 * @param wheezeActivity      wheeze activity intervals.
	 * 
	 * @throws IOException
	 */
	public void openPopUp(double[] x, String state, double fs, String normSpectrumPath, String wheezeSpectrumPath,double[] wheezeActivity) throws IOException {

		wheezeFrame = new JFrame();
		wheezeFrame.setTitle("Wheeze Analysis");
		wheezeFrame.setBounds(5, 150, 706, 424);//(10, 277, 500, 108);
		wheezeFrame.setResizable(false);
		wheezeFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		wheezeFrame.getContentPane().setBackground(Color.WHITE);
		wheezeFrame.getContentPane().setLayout(null);

		JLabel lblDiagnostic = new JLabel("Diagnostic");
		lblDiagnostic.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDiagnostic.setHorizontalAlignment(SwingConstants.CENTER);
		lblDiagnostic.setBounds(495, 200, 97, 34);
		wheezeFrame.getContentPane().add(lblDiagnostic);

		JLabel DiagnosticTxtBox = new JLabel(state);
		DiagnosticTxtBox.setFont(new Font("Tahoma", Font.BOLD, 15));
		DiagnosticTxtBox.setHorizontalAlignment(SwingConstants.CENTER);
		DiagnosticTxtBox.setBounds(495, 235, 97, 40);
		wheezeFrame.getContentPane().add(DiagnosticTxtBox);

		JLabel lblWheezeInter = new JLabel("Wheezing Intervals");
		lblWheezeInter.setHorizontalAlignment(SwingConstants.CENTER);
		lblWheezeInter.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblWheezeInter.setBounds(454, 38, 185, 27);
		wheezeFrame.getContentPane().add(lblWheezeInter);

		JLabel wheezingIntTxtBox = new JLabel("");
		wheezingIntTxtBox.setHorizontalAlignment(SwingConstants.CENTER);
		wheezingIntTxtBox.setOpaque(true);
		wheezingIntTxtBox.setBackground(Color.WHITE);
		wheezingIntTxtBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		wheezingIntTxtBox.setBounds(454, 65, 185, 116);

		JLabel lblAudSepectrum = new JLabel("Audio Signal Spectrum");
		lblAudSepectrum.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAudSepectrum.setHorizontalAlignment(SwingConstants.CENTER);
		lblAudSepectrum.setBounds(135, 11, 161, 21);
		wheezeFrame.getContentPane().add(lblAudSepectrum);

		JLabel lblWAS = new JLabel("Wheeze Activity Spectrum");
		lblWAS.setHorizontalAlignment(SwingConstants.CENTER);
		lblWAS.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblWAS.setBounds(135, 140, 185, 21);
		wheezeFrame.getContentPane().add(lblWAS);

		JLabel lblWActivity = new JLabel("Wheeze Activity");
		lblWActivity.setBounds(210, 262, 185, 21);
		wheezeFrame.getContentPane().add(lblWActivity);
		lblWActivity.setHorizontalAlignment(SwingConstants.CENTER);
		lblWActivity.setFont(new Font("Tahoma", Font.BOLD, 14));

		JScrollPane scroller = new JScrollPane(wheezingIntTxtBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setBounds(454, 65, 185, 116);
		wheezeFrame.getContentPane().add(scroller);

		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++PLOTS++++++++++++++++++++++++++++++++++++++++++++++++++++++

		//Organization of the filtered signal waveform data
		@SuppressWarnings("unchecked")
		DataTable audioData = new DataTable(Double.class, Double.class);

		double[] time= new double[x.length];
		for(int i=0; i<time.length;i++) {
			time[i]=i/fs;
			double normalized_x=(x[i]-min(x))/(max(x)-min(x));	
			audioData.add(time[i],normalized_x);
		}

		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++WHEEZE ACTIVITY++++++++++++++++++++++++++++++++++++++++++++++

		//Organization of the wheeze activity intervals
		@SuppressWarnings("unchecked")
		DataTable wheezeData = new DataTable(Double.class, Double.class);

		for(int i=0; i<wheezeActivity.length;i++) {
			wheezeData.add(time[i],wheezeActivity[i]);
		}

		//Create plot data
		DataSource audioSource=new DataSeries("Audio",audioData);
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
				insetsBottom = 30.0, //to fit ticks and values
				insetsRight = 120.0;
		wheezeActivityPlot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));

		//Legends
		wheezeActivityPlot.setLegendVisible(true);
		wheezeActivityPlot.setLegendLocation(Location.EAST);
		wheezeActivityPlot.getLegend().setBorderColor(new Color(255,255,255));
		//Panel to host the filtered audio plot and the highlighted wheeze regions
		wheezeActivityPanel = new DrawablePanel(wheezeActivityPlot);
		wheezeActivityPanel.setBounds(10, 277, 665, 108);
		wheezeActivityPanel.setBackground(Color.WHITE);
		wheezeFrame.getContentPane().add(wheezeActivityPanel);
		wheezeActivityPanel.setLayout(null);

		//Lines and points configuration
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

		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++PLOT SIGNAL SPECTRUM+++++++++++++++++++++++++++++++++++++++++++++++++

		@SuppressWarnings("unchecked")
		DataTable dummyData = new DataTable(Double.class, Double.class);

		//Using this plot we can set the spectral image, extracted from MATLAB, as background
		dummyData.add(0.0,0.0);
		XYPlot dummyPlot = new XYPlot(dummyData);

		PointRenderer dummyPoints = new DefaultPointRenderer2D(); dummyPoints.setShape(null);
		LineRenderer dummyLines=new SmoothLineRenderer2D(); dummyLines.setColor(new Color(0,0,0,0));
		dummyPlot.setPointRenderers(dummyData, dummyPoints);
		dummyPlot.setLineRenderers(dummyData, dummyLines);

		//Configuration axis and labels
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

		//Set panel with the spectrum plot
		DrawablePanel spectrumPanel = new DrawablePanel(dummyPlot);
		spectrumPanel.setBounds(10, 28, 500, 108);
		spectrumPanel.setBackground(Color.WHITE);
		spectrumPanel.setLayout(null);

		//Adding the spectrum image, saved in the patient folder, to the panel, with correct dimensions
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

		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++WHEEZING SPECTRUM++++++++++++++++++++++++++++++++++++++++++++++++++

		XYPlot dummyPlot2=dummyPlot;

		DrawablePanel wheezeSpectrumPanel = new DrawablePanel(dummyPlot2);
		wheezeSpectrumPanel.setBounds(10, 155, 500, 108);
		wheezeSpectrumPanel.setBackground(Color.WHITE);

		double wheezeLblWidth=wheezeSpectrumPanel.getWidth();
		double wheezeLblHeight=wheezeSpectrumPanel.getHeight();
		double wheezeLblX=wheezeSpectrumPanel.getX();
		double wheezeLblY=wheezeSpectrumPanel.getY();

		JLabel wheezeSpectrumLbl = new JLabel("");
		wheezeSpectrumLbl.setBounds((int) (wheezeLblX+insetsLeft),(int) (wheezeLblY+insetsTop),(int) (wheezeLblWidth-insetsLeft-insetsRight),(int) (wheezeLblHeight-insetsTop-insetsBottom));
		wheezeFrame.getContentPane().add(wheezeSpectrumLbl);

		BufferedImage wheezeImg = ImageIO.read(new File(wheezeSpectrumPath));
		Image resizedWheezeImg=wheezeImg.getScaledInstance(wheezeSpectrumLbl.getWidth(),wheezeSpectrumLbl.getHeight(),Image.SCALE_SMOOTH);
		ImageIcon wheezeSpectrumImg=new ImageIcon(resizedWheezeImg);
		wheezeSpectrumLbl.setIcon(wheezeSpectrumImg);

		wheezeFrame.getContentPane().add(wheezeSpectrumPanel);
		wheezeSpectrumPanel.setLayout(null);

		//Write the wheezing intervals as discrete list of the time they occured
		List<int[]> intervals =intervals(wheezeActivity);
		String textIntervals="";
		for(int i=0; i<intervals.size();i++){
			int[]interval=intervals.get(i);
			textIntervals=textIntervals + String.format("%.2f",(interval[0]/fs)) + " s " + " - " + String.format("%.2f",(interval[1]/fs))+ " s " + "<br>"; //This basically creates a text like 1.2 s - 1.4 s (newline) 3.4-3.7 s
		}
		textIntervals="<html>"+textIntervals+"<html>";//the text with all intervals
		wheezingIntTxtBox.setText(textIntervals);

		wheezeFrame.setVisible(true);
	}

	/**
	 * Method to get the maximum value from an array.
	 * 
	 * @param array  array of double precision values.
	 */
	private double max(double [] array) {
		double max = 0;

		for(int i=0; i<array.length; i++ ) {
			if(array[i]>max) {
				max = array[i];
			}
		}
		return max;
	}

	/**
	 * Method to get the minimum value from an array.
	 * 
	 * @param array  array of double precision values.
	 */
	private double min(double [] array) {
		double min = 0;

		for(int i=0; i<array.length; i++ ) {
			if(array[i]<min) {
				min = array[i];
			}
		}
		return min;
	}

	/**
	 * Method to define the intervals where the wheezes are present, from the binary waveform of the wheeze activity.
	 * When there's a transition from a 0 to a 1, a wheezing event starts.
	 * When there's a transition from a 1 to a 0, a wheezing event is ends.
	 * 
	 * @param  array      wheeze activity binary waveform.
	 * 
	 * @return intervals  list of the values of starting point and end point of a wheeze interval.
	 */
	private List<int[]> intervals(double[] a){
		//List of double arrays that store in each row an interval (in samples) where the wheeze is present. 
		//First element is the start, second element is the end of the wheezing activity.
		List<int[]> intervals = new ArrayList<int[]>();
		int start = 0;
		for(int i=0; i<a.length-1; i++) {
			//Start of the Wheeze
			if (a[i]==0.0 && a[i+1]==1.0) {
				start=i;
				continue;
			}
			//End of Wheeze
			else if(a[i]==1.0 && a[i+1]==0.0) {
				intervals.add(new int[] {start,i});
			}
		}
		return intervals;
	}
}
