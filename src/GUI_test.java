import java.awt.EventQueue;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import java.awt.FlowLayout;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.awt.BorderLayout;

public class GUI_test {

	private JFrame frame;

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
		
        MatlabEngine eng = MatlabEngine.startMatlab();        
        eng.eval("[bpm wave Fs hard soft mild]=breathingClass_Rate('C:\\Users\\A541\\OneDrive - Universidade do Porto\\MIB\\3ºAno\\2º semestre\\LIEB\\Projeto\\Compiled breath audios\\Vesicular breath sound\\A_rale_vesicular.wav');");
        
        double bpm=eng.getVariable("bpm");
        double[] wave=eng.getVariable("wave");
        double fs=eng.getVariable("Fs");
        double[][] hard=eng.getVariable("hard");
        double[][] soft=eng.getVariable("soft");
        double[][] mild=eng.getVariable("mild");
        double[] stuff={1,2,3,4,5};
        
        DataTable audioData = new DataTable(Double.class, Double.class);
        
        double[] time= new double[wave.length];
        for(int i=0; i<time.length;i++) {
        		time[i]=i/fs;
        		audioData.add(time[i],wave[i]);
        }
        
        System.out.println(time[50]);
        System.out.println(wave[90]);
        XYPlot plot = new XYPlot(audioData);
        eng.eval("[x fs Xw Xwo delta_t nf]=wheezeDetect('C:\\Users\\A541\\OneDrive - Universidade do Porto\\MIB\\3ºAno\\2º semestre\\LIEB\\Projeto\\Matlab\\A_rale_bronchial.wav');");
        eng.close();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_test window = new GUI_test();
					window.frame.setVisible(true);
					window.frame.getContentPane().add(new InteractivePanel(plot));
					LineRenderer lines = new DefaultLineRenderer2D();
					plot.setLineRenderers(audioData, lines);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_test() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 499, 327);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JInternalFrame internalFrame = new JInternalFrame("New JInternalFrame");
		internalFrame.setResizable(true);
		frame.getContentPane().add(internalFrame, BorderLayout.CENTER);
		internalFrame.setVisible(true);
	}

}
