

import java.awt.EventQueue;
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

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JSeparator;

import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.SystemColor;

public class breathingRate {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					breathingRate window = new breathingRate();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws MatlabSyntaxException 
	 * @throws MatlabExecutionException 
	 */
	public breathingRate() throws MatlabExecutionException, MatlabSyntaxException, IllegalArgumentException, IllegalStateException, InterruptedException, ExecutionException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws InterruptedException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws ExecutionException 
	 * @throws MatlabSyntaxException 
	 * @throws MatlabExecutionException 
	 */
	private void initialize() throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		frame = new JFrame();
		frame.setBounds(100, 100, 618, 424);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel rawAudioPanel = new JPanel();
		rawAudioPanel.setBounds(37, 25, 319, 94);
		frame.getContentPane().add(rawAudioPanel);
		
		JPanel envelopeJPanel = new JPanel();
		envelopeJPanel.setBounds(37, 144, 319, 94);
		frame.getContentPane().add(envelopeJPanel);
		
		JPanel rawAudioPanel_2 = new JPanel();
		rawAudioPanel_2.setBounds(37, 261, 319, 94);
		frame.getContentPane().add(rawAudioPanel_2);
		
		JLabel lblNewLabel = new JLabel("Total Number of Breaths");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblNewLabel.setBounds(408, 69, 194, 36);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblBreathingRate = new JLabel("Breathing Rate");
		lblBreathingRate.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblBreathingRate.setBounds(449, 166, 119, 36);
		frame.getContentPane().add(lblBreathingRate);
		
		JLabel lblNewLabel_1 = new JLabel("Hard");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(449, 295, 40, 22);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Soft");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(452, 323, 40, 20);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Mild");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1.setBounds(452, 354, 40, 22);
		frame.getContentPane().add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("Type of Breath");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setBounds(411, 262, 103, 22);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("No.");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2_1.setBounds(537, 262, 31, 22);
		frame.getContentPane().add(lblNewLabel_2_1);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(408, 290, 170, 2);
		frame.getContentPane().add(separator_1);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(526, 264, 1, 112);
		frame.getContentPane().add(separator);
		
		JPanel totalBreaths = new JPanel();
		totalBreaths.setBackground(Color.WHITE);
		totalBreaths.setBounds(476, 104, 75, 39);
		frame.getContentPane().add(totalBreaths);
		
		JPanel breathingRate = new JPanel();
		breathingRate.setBackground(Color.WHITE);
		breathingRate.setBounds(476, 201, 75, 38);
		frame.getContentPane().add(breathingRate);
		
		JPanel hardTxt = new JPanel();
		hardTxt.setBackground(SystemColor.menu);
		hardTxt.setBounds(537, 295, 40, 22);
		frame.getContentPane().add(hardTxt);
		
		JPanel softTxt = new JPanel();
		softTxt.setBackground(SystemColor.menu);
		softTxt.setBounds(537, 323, 40, 22);
		frame.getContentPane().add(softTxt);
		
		JPanel mildTxt = new JPanel();
		mildTxt.setBackground(SystemColor.menu);
		mildTxt.setBounds(537, 354, 40, 22);
		frame.getContentPane().add(mildTxt);
		
		MatlabEngine eng = MatlabEngine.startMatlab();  
		//C:\\Users\\A541\\OneDrive - Universidade do Porto\\MIB\\3ºAno\\2º semestre\\LIEB\\Projeto\\Compiled breath audios\\Vesicular breath sound\\A_rale_vesicular.wav
		eng.eval("[bpm wave env Fs hard soft mild]=breathingClass_Rate('C:\\\\Users\\\\A541\\\\OneDrive - Universidade do Porto\\\\MIB\\\\3ºAno\\\\2º semestre\\\\LIEB\\\\Projeto\\\\Compiled breath audios\\\\Vesicular breath sound\\\\A_rale_vesicular.wav');");

		double bpm=eng.getVariable("bpm");
		double[] wave=eng.getVariable("wave");
		double[] env=eng.getVariable("env");
		double fs=eng.getVariable("Fs");
		double[][] hard=eng.getVariable("hard");
		double[][] soft=eng.getVariable("soft");
		double[][] mild=eng.getVariable("mild");
		double[] stuff={1,2,3,4,5};
		
		eng.close();
		
		//Organizar dados do sinal audio raw
		DataTable audioData = new DataTable(Double.class, Double.class);

		double[] time= new double[wave.length];
		for(int i=0; i<time.length;i++) {
			time[i]=i/fs;
			audioData.add(time[i],wave[i]);
		}

		XYPlot audioPlot = new XYPlot(audioData);
		audioPlot.getTitle().setText("Raw Audio Signal");
		audioPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time(s)"));
		audioPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Normalized Intensity"));
		
		//Definição do painel onde ficará o sinal aúdio
		DrawablePanel audioPlotPanel = new DrawablePanel(audioPlot);
		audioPlotPanel.setBounds(30, 88, 452, 121);
		audioPlotPanel.setBackground(Color.WHITE);
		rawAudioPanel.add(audioPlotPanel);
		//Lines and points config
		LineRenderer lines = new SmoothLineRenderer2D();
		PointRenderer points = new DefaultPointRenderer2D();
		Color blue=new Color(0,200,255);
		lines.setColor(blue); points.setColor(blue);
		lines.setStroke(new BasicStroke(1)); points.setShape(null);
		audioPlot.setLineRenderers(audioData, lines);
		audioPlot.setPointRenderers(audioData, points);
		
		
		//Organizar dados do envelope
		DataTable envData = new DataTable(Double.class, Double.class);

		for(int i=0; i<time.length;i++) {
			time[i]=i/fs;
			envData.add(time[i],env[i]);
		}

		XYPlot envPlot = new XYPlot(envData);
		envPlot.getTitle().setText("Audio Envelope");
		envPlot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time(s)"));
		envPlot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Normalized Intensity"));
		
		//Definição do painel onde ficará o envelope
		DrawablePanel envPlotPanel = new DrawablePanel(envPlot);
		envPlotPanel.setBounds(30, 88, 452, 121);
		envPlotPanel.setBackground(Color.WHITE);
		envelopeJPanel.add(envPlotPanel);
		//Lines and points config
		lines.setColor(blue); points.setColor(blue);
		lines.setStroke(new BasicStroke(1)); points.setShape(null);
		envPlot.setLineRenderers(audioData, lines);
		envPlot.setPointRenderers(audioData, points);
		
		
	}
}
