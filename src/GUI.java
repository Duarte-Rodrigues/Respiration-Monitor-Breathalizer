import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.DrawablePanel;
import javax.swing.JTabbedPane;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.*;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;



public class GUI{

	public static JFrame frmRespiratorySoundAnalysis;
	private static JTabbedPane tabbedPane;
	public static JPanel panel_Home;
	public static JPanel panel_AudioSelect;
	private static JPanel panel_AudioAnalysis;
	public static JLabel photo;
	private static String filename;
	private static MatlabEngine eng;
	public static ImportPatInfo Import=new ImportPatInfo();
	public static Breathalizer breath = new Breathalizer();
	public static int supposedImports=0;
	private static boolean analysis=true;
	private static boolean newaudio;//=false;
	public static int selectCounter=0;
	public static int supposedSelections=1;
	public static String selectedAud;
	private static boolean secondpass;
	
	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws ExecutionException 
	 * @throws MatlabSyntaxException 
	 * @throws MatlabExecutionException 
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */

	public static void main(String[] args) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		eng=MatlabEngine.startMatlab();
		InitializeFrameAndTabs();
		do {
			List<String> audios = HomePage();
			do {
				audioSelection(audios);
				audioGraphPlayer();
				audioAnalysis();
			}while(newaudio);
		}while(supposedImports==Import.getImportCounter());
		eng.close();
	}

	public static void InitializeFrameAndTabs() {
		frmRespiratorySoundAnalysis = new JFrame();
		frmRespiratorySoundAnalysis.setTitle("Respiratory Sound Monitor");
		frmRespiratorySoundAnalysis.setSize(538, 351);
		frmRespiratorySoundAnalysis.setResizable(false);
		frmRespiratorySoundAnalysis.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	Import.savePatAnnotations();
            	System.exit(0);
            }
        });
		frmRespiratorySoundAnalysis.setVisible(false);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setFont(new Font( "Dialog", Font.BOLD, 17 ) );
		frmRespiratorySoundAnalysis.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		panel_Home = new JPanel();
		panel_Home.setBackground(Color.WHITE);
		tabbedPane.addTab("   Home     ", null, panel_Home, null);
		panel_Home.setLayout(null);

		panel_AudioSelect = new JPanel();
		panel_AudioSelect.setBackground(Color.WHITE);
		tabbedPane.addTab("   Audio Selection   ", null, panel_AudioSelect, null);
		tabbedPane.setEnabledAt(1, false);

		panel_AudioSelect.setLayout(null);

		panel_AudioAnalysis = new JPanel();
		panel_AudioAnalysis.setBackground(Color.WHITE);
		tabbedPane.addTab("   Audio Analysis  ", null, panel_AudioAnalysis, null);
		tabbedPane.setEnabledAt(2, false);
		
		ImageIcon homeIcon = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Faculdade e Cadeiras\\LIEB\\Project_java\\LIEB_Project\\home.png");
		ImageIcon resizedhome = scaleImage(homeIcon,23,23);
		tabbedPane.setIconAt(0,resizedhome);
		
		ImageIcon selIcon = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Faculdade e Cadeiras\\LIEB\\Project_java\\LIEB_Project\\health.png");
		ImageIcon resizedsel = scaleImage(selIcon,23,23);
		tabbedPane.setIconAt(1,resizedsel);
		
		ImageIcon AnaIcon = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Faculdade e Cadeiras\\LIEB\\Project_java\\LIEB_Project\\computer.png");
		ImageIcon resizedAna = scaleImage(AnaIcon,25,25);
		tabbedPane.setIconAt(2,resizedAna);

		panel_AudioAnalysis.setLayout(null);
	}

	public static List<String> HomePage() {
		
		//uma fotografia qualquer de pessoa nao lida caso não se prossiga com a escolha apos a interface inicial
		ImageIcon img = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Faculdade e Cadeiras\\LIEB\\Project_java\\LIEB_Project\\No Patient Selected.png");
		ImageIcon resizedImg = scaleImage(img,106,136);
		photo =new JLabel();
		photo.setIcon(resizedImg);
		photo.setBounds(20, 22, 106, 136);
		panel_Home.add(photo);

		JLabel lblName = new JLabel("Name:");
		lblName.setForeground(Color.BLACK);
		lblName.setBounds(142, 34, 57, 14);
		panel_Home.add(lblName);

		JLabel lblAge= new JLabel("Age:");
		lblAge.setForeground(Color.BLACK);
		lblAge.setBounds(142,59,46,14);
		panel_Home.add(lblAge);

		JLabel lblDoB= new JLabel("Date of Birth:");
		lblDoB.setForeground(Color.BLACK);
		lblDoB.setBounds(142, 84, 83, 14);
		panel_Home.add(lblDoB);

		JLabel lblHno = new JLabel("Health No.:");
		lblHno.setForeground(Color.BLACK);
		lblHno.setBounds(142, 109, 64, 14);
		lblHno.setVisible(true);
		panel_Home.add(lblHno);

		JLabel lblPAI = new JLabel("Previous Appointments Information");
		lblPAI.setForeground(Color.BLACK);
		lblPAI.setBounds(229,138,210,14);
		panel_Home.add(lblPAI);

		JButton ImportBtn = new JButton("Import Patient Folder");
		ImportBtn.setBounds(350, 11, 165, 23);
		panel_Home.add(ImportBtn);
		boolean selectionLock =true;

		ImportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				supposedImports++;
				if (supposedImports>1) {
					panel_AudioSelect.removeAll();
					panel_AudioAnalysis.removeAll();
					tabbedPane.setEnabledAt(2, false);
					Import.savePatAnnotations();
				}
				Import.ImportInformation(panel_Home,photo);
				//audio selection loopcontrol variables
				selectCounter=0;
				supposedSelections=1;
				newaudio=false;
			}
		});

		panel_Home.repaint();
		//Mostra a frame inicial Introdutória
		if (!secondpass) {
			breath.start();
			secondpass= true;
		}
		
		//o programa vai esperar neste loop até se ler a informacao do paciente
		do{
			selectionLock=Import.getLockStop();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}while (selectionLock==true);

		Import.setLockStop(true);
		analysis = true;
		//lista dos paths para os audios
		List<String> audios = Import.getAudioList();
		return audios;
	}

	public static void audioSelection(List<String> audios) {
		tabbedPane.setEnabledAt(1, true);
		String audioNames[] = new String[audios.size()+1];
		audioNames[0] = "Select Patient Audio";
		for(int i=0;i<audios.size();i++) {
			int barIndx= (audios.get(i)).lastIndexOf("\\");
			int pointIndx = (audios.get(i)).lastIndexOf(".");
			audioNames[i+1] = (audios.get(i)).substring(barIndx+1,pointIndx);
		}
		
		JComboBox<String> audioChoice = new JComboBox<String>(audioNames);
		audioChoice.setBounds(28, 15, 250, 23);
		panel_AudioSelect.add(audioChoice);

		JButton btnSelectAudio = new JButton("Select");
		btnSelectAudio.setBounds(390, 15, 90, 23);
		btnSelectAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedAud = (String)audioChoice.getSelectedItem();	
				newaudio=true;
				selectCounter++;
				analysis=false;
			}

		});
		panel_AudioSelect.add(btnSelectAudio);

		if (supposedImports>1||selectCounter>=1) {
			panel_AudioSelect.revalidate();
			panel_AudioSelect.repaint();
		}

		//o programa vai esperar neste loop até se ler o audio que se pretende ouvir e analisar
		do{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}while (analysis==true);
		int chosenIndex = 0;
		for(int i=0;i<audios.size();i++) {
			if (audios.get(i).contains(selectedAud)){
				chosenIndex=i;
			}
		}
		filename=audios.get(chosenIndex);
	}

	@SuppressWarnings("unchecked")
	public static void audioGraphPlayer() throws MatlabExecutionException, MatlabSyntaxException, IllegalArgumentException, IllegalStateException, InterruptedException, ExecutionException, UnsupportedAudioFileException, IOException, LineUnavailableException {

		getAudioPlot Audplot = new getAudioPlot(filename,eng);
		double[] wavefrm=Audplot.getWavefrm();
		double FS=Audplot.getFS();
		//++++++++++++++++++++++++++++++++PLOT AUDIO SIGNAL++++++++++++++++++++++++++++++++++++++++++++++++++++

		AudioPlayer audioSel= new AudioPlayer(filename);
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
		audioPlotPanel.setBounds(28, 73, 452, 121);
		audioPlotPanel.setBackground(Color.WHITE);

		panel_AudioSelect.add(audioPlotPanel);

		//Lines and points config
		LineRenderer lines = new SmoothLineRenderer2D();
		PointRenderer points = new DefaultPointRenderer2D();
		Color blue=new Color(0,200,255);
		lines.setColor(blue); points.setColor(blue);
		lines.setStroke(new BasicStroke(1)); points.setShape(null);
		audioPlot.setLineRenderers(audioData, lines);
		audioPlot.setPointRenderers(audioData, points);

		//++++++++++++++++++++++++++++++++++++++++++++++++++++Audio Player Specs+++++++++++++++++++++++++++++++++++++++++++++
		//Slider to accompany the audio progression
		JLabel currentDuration = new JLabel ("0");
		currentDuration.setBounds(2, 53, 31,14);
		JSlider slider = new JSlider(0, 100);
		slider.setBackground(Color.WHITE);
		slider.setValue(0);
		slider.setBounds(25, 49, 462,23);
		audioSel.AudioSlider(slider,currentDuration);
		panel_AudioSelect.add(slider);
		panel_AudioSelect.add(currentDuration);

		JButton btnResumeAudio = new JButton("Resume");
		btnResumeAudio.setBounds(10, 239, 90, 23);
		btnResumeAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					audioSel.resume();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnResumeAudio.setVisible(false);
		panel_AudioSelect.add(btnResumeAudio);

		JButton btnPlayAudio = new JButton("Play");
		btnPlayAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				audioSel.play();
				btnResumeAudio.setVisible(true);
				btnPlayAudio.setVisible(false);
			}
		});
		btnPlayAudio.setBounds(30, 239, 70, 23);
		panel_AudioSelect.add(btnPlayAudio);


		JButton btnPauseAudio = new JButton("Pause");
		btnPauseAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				audioSel.pause();
			}
		});
		btnPauseAudio.setBounds(110, 239, 77, 23);
		panel_AudioSelect.add(btnPauseAudio);

		JButton btnRestartAudio = new JButton("Restart");
		btnRestartAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					audioSel.restart();
				} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e1) {					
					e1.printStackTrace();
				}
			}
		});
		btnRestartAudio.setBounds(201, 239, 77, 23);
		panel_AudioSelect.add(btnRestartAudio);

		JTextField GotoIndex = new JTextField(10);
		GotoIndex.setBounds(385, 240, 66, 22);
		panel_AudioSelect.add(GotoIndex);
		GotoIndex.setColumns(10);

		JButton btnGoTo = new JButton("Go to");
		btnGoTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ind = GotoIndex.getText();
				double timeInd = (Double.parseDouble(ind));
				long index = (long)timeInd*1000000;
				try {
					audioSel.jump(index);
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		});
		btnGoTo.setBounds(296, 239, 79, 23);
		panel_AudioSelect.add(btnGoTo);

		JLabel lblSec = new JLabel("sec.");
		lblSec.setBounds(459, 243, 26, 14);
		panel_AudioSelect.add(lblSec);

		JLabel lblTime = new JLabel("Time (s)");
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setBounds(231, 209, 46, 14);
		panel_AudioSelect.add(lblTime);

		JButton btnTakeNote = new JButton("Take Note");
		btnTakeNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String decTime = audioSel.getStrSecond();
				@SuppressWarnings("unused")
				AnnotationPopUp note =new AnnotationPopUp(decTime,filename);
			}
		});
		btnTakeNote.setBounds(385, 205, 95, 23);
		panel_AudioSelect.add(btnTakeNote);
		tabbedPane.setEnabledAt(2, true);
		//important to reveal the information of the panel
		panel_AudioSelect.revalidate();
		panel_AudioSelect.repaint();
	}

	public static void audioAnalysis() throws MatlabExecutionException, MatlabSyntaxException, IllegalArgumentException, IllegalStateException, InterruptedException, ExecutionException {
		JTextPane BreathingExp = new JTextPane();	
		BreathingExp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		BreathingExp.setText("The recorded breathing sound\r\n" + 
				"is analysed in order to recover\r\n" + 
				"the total number of breaths in\r\n" + 
				"the audio file, distinguishing\r\n" + 
				"them between soft, mild or\r\n" + 
				"hard breaths.\r\n\n" + 
				"The timestamp of each breath\r\n" + 
				"is saved.");
		BreathingExp.setForeground(Color.BLACK);
		BreathingExp.setBackground(Color.WHITE);
		BreathingExp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		BreathingExp.setEditable(false);
		BreathingExp.setBounds(42, 102, 174, 171);
		panel_AudioAnalysis.add(BreathingExp);

		JTextPane WheezingDetExp = new JTextPane();
		WheezingDetExp.setText("The respiratory recording is\r\n" + 
				"examined in order to detected\r\n" + 
				"high frequency periodic\r\n" + 
				"sounds.These wheezes are\r\n" + 
				"then registered and highlighted\r\n" + 
				"in the sound wave.\r\n\n" + 
				"In the end the algorith\r\n" + 
				"proposes whether the patient\r\n" + 
				"is healthy or not.");
		WheezingDetExp.setForeground(Color.BLACK);
		WheezingDetExp.setBackground(Color.WHITE);
		WheezingDetExp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		WheezingDetExp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		WheezingDetExp.setEditable(false);
		WheezingDetExp.setBounds(304, 102, 179, 171);
		panel_AudioAnalysis.add(WheezingDetExp);

		JLabel lblNewLabel_6 = new JLabel("Types of Analysis");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_6.setBounds(200, 24, 137, 25);
		panel_AudioAnalysis.add(lblNewLabel_6);

		JButton btnNewButton = new JButton("Breathing Rate");
		btnNewButton.setBounds(58, 68, 144, 23);
		panel_AudioAnalysis.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Wheeze Detection");
		btnNewButton_1.setBounds(321, 68, 151, 23);
		panel_AudioAnalysis.add(btnNewButton_1);

		//+++++++++++++++++++++++++++++++GET MATLAB BREATHING RATE DATA+++++++++++++++++++++++++++++++++++++
		getBreathingRate br=new getBreathingRate(filename,eng);

		double bpm=br.getBpm();
		double[] wave=br.getWave();
		double fs=br.getFs();
		double[] env=br.getEnv();
		double[][] hard=br.getHard();
		double[][] soft=br.getSoft();
		double[][] mild=br.getMild();

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				breathRatePopUp BRpopUp=new breathRatePopUp();
				BRpopUp.openPopUp(bpm, wave, fs, env, hard, mild, soft);
			}
		});
		//+++++++++++++++++++++++++++++++++++++GET WHEEZING DATA++++++++++++++++++++++++++++++++++++++++++++++++
		getWheeze wheeze=new getWheeze(filename,eng);
		double[] x=wheeze.getX();
		String state=wheeze.getState();
		double fs1=wheeze.getFs();
		String normSpectrumPath=wheeze.getNormSpectrumPath();
		String wheezeSpectrumPath=wheeze.getWheezeSpectrumPath();
		double[] wheezeActivity=wheeze.getWheezeActivity();

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (state.equals("Unhealthy")) {
					wheezePopUpUnhealthy unhealthyPopUp=new wheezePopUpUnhealthy();
					try {
						unhealthyPopUp.openPopUp(x,state, fs1, normSpectrumPath, wheezeSpectrumPath, wheezeActivity);
					} catch (IOException e1) {

						e1.printStackTrace();
					}
				}
				else if(state.equals("Healthy")) {
					wheezePopUpHealthy healthyPopUp=new wheezePopUpHealthy();
					try {
						healthyPopUp.openPopUp(x,state, fs1, normSpectrumPath);
					} catch (IOException e1) {

						e1.printStackTrace();
					}
				}
			}
		});

		if(selectCounter>=1) {
			panel_AudioSelect.revalidate();
			panel_AudioSelect.repaint();
			panel_AudioAnalysis.revalidate();
			panel_AudioAnalysis.repaint();
		}

		do{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}
		}while (supposedSelections==selectCounter);
		
		if(selectCounter>=1) {
			supposedSelections++;
			panel_AudioAnalysis.removeAll();
			panel_AudioSelect.removeAll();
		}
	}

	public static ImageIcon scaleImage(ImageIcon icon, int w, int h)
	{
		int nw = icon.getIconWidth();
		int nh = icon.getIconHeight();
		if(icon.getIconWidth() > w)
		{
			nw = w;
			nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
		}

		if(nh > h)
		{
			nh = h;
			nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
		}

		return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
	}

}
