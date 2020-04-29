

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
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import javax.swing.JTabbedPane;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import java.awt.Choice;
import java.awt.TextField;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;



public class GUI {
	
	private static JFrame frame;

	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws ExecutionException 
	 * @throws MatlabSyntaxException 
	 * @throws MatlabExecutionException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		
		//------------------------------------------GET FILTERED AUDIO AND INTENSITY MATLAB DATA-------------------------------
        MatlabEngine eng = MatlabEngine.startMatlab();        
        eng.eval("[bpm wave Fs hard soft mild]=breathingClass_Rate('C:\\Users\\A541\\OneDrive - Universidade do Porto\\MIB\\3ºAno\\2º semestre\\LIEB\\Projeto\\Compiled breath audios\\Vesicular breath sound\\A_rale_vesicular.wav');");
        
        double bpm=eng.getVariable("bpm");
        double[] wave=eng.getVariable("wave");
        double fs=eng.getVariable("Fs");
        double[][] hard=eng.getVariable("hard");
        double[][] soft=eng.getVariable("soft");
        double[][] mild=eng.getVariable("mild");
        double[] stuff={1,2,3,4,5};
        
 
        
        eng.eval("[x fs Xw Xwo delta_t nf]=wheezeDetect('C:\\Users\\A541\\OneDrive - Universidade do Porto\\MIB\\3ºAno\\2º semestre\\LIEB\\Projeto\\Matlab\\A_rale_bronchial.wav');");
        eng.close();
        //---------------------------------------------------------------------------------------------------------------
		frame = new JFrame();
		frame.setBounds(100, 100, 538, 351);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_Home = new JPanel();
		tabbedPane.addTab("Home", null, panel_Home, null);
		
		JPanel panel_AudioSelect = new JPanel();
		tabbedPane.addTab("Audio Selection", null, panel_AudioSelect, null);
		GridBagLayout gbl_panel_AudioSelect = new GridBagLayout();
		gbl_panel_AudioSelect.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_AudioSelect.rowHeights = new int[]{0, 58, 148, 70, 0};
		gbl_panel_AudioSelect.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_AudioSelect.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel_AudioSelect.setLayout(gbl_panel_AudioSelect);
		
		JButton btnSelectAudio = new JButton("Select");
		btnSelectAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		Choice choice = new Choice();
		GridBagConstraints gbc_choice = new GridBagConstraints();
		gbc_choice.fill = GridBagConstraints.HORIZONTAL;
		gbc_choice.gridwidth = 4;
		gbc_choice.insets = new Insets(0, 0, 5, 5);
		gbc_choice.gridx = 3;
		gbc_choice.gridy = 1;
		panel_AudioSelect.add(choice, gbc_choice);
		GridBagConstraints gbc_btnSelectAudio = new GridBagConstraints();
		gbc_btnSelectAudio.insets = new Insets(0, 0, 5, 5);
		gbc_btnSelectAudio.gridx = 11;
		gbc_btnSelectAudio.gridy = 1;
		panel_AudioSelect.add(btnSelectAudio, gbc_btnSelectAudio);
		
		
		//Organizar audio e criar o seu plot
		
		DataTable audioData = new DataTable(Double.class, Double.class);
	        
	    double[] time= new double[wave.length];
	    for(int i=0; i<time.length;i++) {
	    	time[i]=i/fs;
	        audioData.add(time[i],wave[i]);
	        }
	    
	    XYPlot plot = new XYPlot(audioData);
	    plot.getTitle().setText("Audio Signal");
	    plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(new Label("Time(s)"));
	    plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(new Label("Intensity"));
	    
	    //Definição do painel onde ficará o sinal aúdio
		InteractivePanel audioPlotPanel = new InteractivePanel(plot);
		audioPlotPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_audioPlotPanel = new GridBagConstraints();
		gbc_audioPlotPanel.gridwidth = 11;
		gbc_audioPlotPanel.insets = new Insets(0, 0, 5, 5);
		gbc_audioPlotPanel.fill = GridBagConstraints.BOTH;
		gbc_audioPlotPanel.gridx = 1;
		gbc_audioPlotPanel.gridy = 2;
		panel_AudioSelect.add(audioPlotPanel, gbc_audioPlotPanel);
		//Lines and points config
		LineRenderer lines = new SmoothLineRenderer2D();
		PointRenderer points = new DefaultPointRenderer2D();
		Color blue=new Color(0,200,255);
		lines.setColor(blue); points.setColor(blue);
		lines.setStroke(new BasicStroke(1)); points.setShape(null);
		plot.setLineRenderers(audioData, lines);
		plot.setPointRenderers(audioData, points);
		
		
		JButton btnPlayAudio = new JButton("Play");
		GridBagConstraints gbc_btnPlayAudio = new GridBagConstraints();
		gbc_btnPlayAudio.insets = new Insets(0, 0, 0, 5);
		gbc_btnPlayAudio.gridx = 2;
		gbc_btnPlayAudio.gridy = 3;
		panel_AudioSelect.add(btnPlayAudio, gbc_btnPlayAudio);
		
		JButton btnPauseAudio = new JButton("Pause");
		GridBagConstraints gbc_btnPauseAudio = new GridBagConstraints();
		gbc_btnPauseAudio.insets = new Insets(0, 0, 0, 5);
		gbc_btnPauseAudio.gridx = 3;
		gbc_btnPauseAudio.gridy = 3;
		panel_AudioSelect.add(btnPauseAudio, gbc_btnPauseAudio);
		
		JPanel panel_AudioAnalysis = new JPanel();
		tabbedPane.addTab("Audio Analysis", null, panel_AudioAnalysis, null);
		}
	}
