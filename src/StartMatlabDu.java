import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.mathworks.engine.MatlabEngine;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.DrawablePanel;

public class StartMatlabDu {
	
	
	
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame();
		
		frame.setTitle("Respiratory Sound Analysis");
		frame.setBounds(100, 100, 538, 351);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		JPanel pan =new JPanel();
		pan.setLayout(null);
		tabbedPane.addTab("Home", null, pan, null);
		
		
		JLabel tryy = new JLabel("appear");
		tryy.setBounds(30,5,30,15);
		pan.add(tryy);
		MatlabEngine eng=MatlabEngine.startMatlab();
		String filename=new String("C:\\Users\\dtrdu\\Desktop\\Duarte\\audio_wav\\A_rale_wheezing_asthma_8yearold.wav");
		
		String pathAud= filename;
		getAudioPlot Audplot = new getAudioPlot(pathAud,eng);
		
		double[] wavefrm=Audplot.getWavefrm();
		double FS=Audplot.getFS();
		//++++++++++++++++++++++++++++++++PLOT AUDIO SIGNAL++++++++++++++++++++++++++++++++++++++++++++++++++++

		
		DataTable audioData = new DataTable(Double.class, Double.class);
		//Conversão para tempo
		double[] time= new double[wavefrm.length];
		for(int i=0; i<time.length;i++) {
			time[i]=i/FS;
			audioData.add(time[i],wavefrm[i]);
		}

		XYPlot audioPlot = new XYPlot(audioData);

		//Definição do painel onde ficará o sinal audio
		DrawablePanel audioPlotPanel = new DrawablePanel(audioPlot);
		pan.add(audioPlotPanel);
		audioPlotPanel.setBounds(28, 73, 452, 121);
		
		audioPlotPanel.setBackground(Color.WHITE);
		audioPlotPanel.setVisible(true);
		pan.setVisible(true);
		//Lines and points config
		LineRenderer lines = new SmoothLineRenderer2D();
		PointRenderer points = new DefaultPointRenderer2D();
		Color blue=new Color(0,200,255);
		lines.setColor(blue); points.setColor(blue);
		lines.setStroke(new BasicStroke(1)); points.setShape(null);
		audioPlot.setLineRenderers(audioData, lines);
		audioPlot.setPointRenderers(audioData, points);
		
		
		
		
		eng.close();
        }
        
	
	
	
	}


