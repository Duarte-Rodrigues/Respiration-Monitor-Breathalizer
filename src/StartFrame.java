import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartFrame {

	public StartFrame() {
		JFrame frmRespiratorySoundAnalysis = new JFrame();
		frmRespiratorySoundAnalysis.setTitle("Respiratory Sound Analysis");
		frmRespiratorySoundAnalysis.setBounds(100, 100, 538, 351);
		frmRespiratorySoundAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRespiratorySoundAnalysis.setVisible(true);
		
		JPanel pan = new JPanel();
		
		JLabel strt = new JLabel("welcome");
		strt.setBounds(50,50,120,14);
		pan.add(strt);
		frmRespiratorySoundAnalysis.getContentPane().add(pan, BorderLayout.CENTER);
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StartFrame hi = new StartFrame();
	}

}
