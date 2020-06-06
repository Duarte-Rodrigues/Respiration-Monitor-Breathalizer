import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Breathalizer extends GUI{
	
	public static JFrame frmStart;
	
	public Breathalizer() {
		frmStart = new JFrame();
	}
	
	public static void setStartFrameFeatures() {
		frmStart.setBackground(Color.WHITE);
		frmStart.setTitle("BREATHALIZER");
		frmStart.setSize(538, 351);
		frmStart.setResizable(false);
		frmStart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStart.setLocationRelativeTo(null);
		frmStart.setVisible(true);
	}

	public void start() {
		setStartFrameFeatures();
		
		JPanel welcome = new JPanel();
		welcome.setBackground(Color.WHITE);
		welcome.setVisible(true);
		welcome.setLayout(null);
		frmStart.getContentPane().add(welcome);
		
		ImageIcon logoImg = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Faculdade e Cadeiras\\LIEB\\Project_java\\LIEB_Project\\Breathalizer.png");
		JLabel logo =new JLabel();
		logo.setIcon(logoImg);
		logo.setBounds(80, 50, 371, 126);
		logo.setVisible(true);
		welcome.add(logo);
		
		JButton ImportBtn = new JButton("Import");
		ImportBtn.setBounds(229, 233, 101, 23);
		welcome.add(ImportBtn);
		
		JLabel lblNewLabel = new JLabel("Start by importing the first patient folder");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(158, 194, 251, 28);
		welcome.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("LIEB Project 2019/2020");
		Color darkBlue=new Color(0,79,146);
		lblNewLabel_1.setForeground(darkBlue);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(168, 11, 206, 28);
		welcome.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Performed by: Duarte Rodrigues e João Fonseca");
		lblNewLabel_2.setBounds(10, 297, 379, 14);
		welcome.add(lblNewLabel_2);
		boolean selectionLock =true;

		ImportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				supposedImports++;
				Import.ImportInformation(panel_Home,photo);
				//audio selection loopcontrol variables
				selectCounter=0;
				supposedSelections=1;
				frmRespiratorySoundAnalysis.setLocation(frmStart.getLocation());
				frmRespiratorySoundAnalysis.setVisible(true);
				
				frmStart.setVisible(false);
			}
		});


		panel_Home.repaint();
		
		welcome.revalidate();
		welcome.repaint();
		//o programa vai esperar neste loop até se ler a informacao do paciente
		do{
			selectionLock=Import.getLockStop();//lockAtm.get();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}while (selectionLock==true);

	}
	
}
