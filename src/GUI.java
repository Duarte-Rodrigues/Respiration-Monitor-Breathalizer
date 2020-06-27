/**
 * LIEB PROJECT 2019/2020
 * BREATHALIZER - Breathing Monitor
 * @author Duarte Rodrigues
 * @author João Fonseca
 * 
 * GUI: Main class that holds the Swing components and serves as the starting point to build the interface and to call
 * the other auxiliary classes with the built-in functions to make our program run properly.
 * The interface is divided in Home tab, Selection tab and Analysis tab.
 */

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
	private static boolean newaudio;
	public static int selectCounter=0;
	public static int supposedSelections=1;
	public static String selectedAud;
	private static boolean secondpass;
	public static String state;
	public static wheezePopUpUnhealthy unhealthyPopUp =new wheezePopUpUnhealthy();;
	
	/**
	 * Main method that contains the program body and launchs the app.
	 * 
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
		
		// Initializing the shared MATLAB machine (API)
		eng=MatlabEngine.startMatlab();
		InitializeFrameAndTabs();
		
		// First cycle to allow the reading of various patients folders
		do {
			List<String> audios = HomePage();
			
			// Second cycle to allow the reading of various recordings on the same patient
			do {
				audioSelection(audios);
				audioGraphPlayer();
				audioAnalysis();
			}while(newaudio);
		}while(supposedImports==Import.getImportCounter());
		eng.close();
	}

	/**
	   * Method that first starts and initializes the Swing frame and panels.
	   */
	public static void InitializeFrameAndTabs() {
		frmRespiratorySoundAnalysis = new JFrame();
		frmRespiratorySoundAnalysis.setTitle("Respiratory Sound Monitor");
		frmRespiratorySoundAnalysis.setSize(538, 351);
		frmRespiratorySoundAnalysis.setResizable(false);
		frmRespiratorySoundAnalysis.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	//Making sure that in the last imported folder, when the user closes the app, the notes are saved
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
		
		ImageIcon homeIcon = new ImageIcon(GUI.class.getResource("icons/Home_Icon.png"));
		ImageIcon resizedhome = scaleImage(homeIcon,23,23);
		tabbedPane.setIconAt(0,resizedhome);
		
		ImageIcon selIcon = new ImageIcon(GUI.class.getResource("icons/Selection_Icon.png"));
		ImageIcon resizedsel = scaleImage(selIcon,23,23);
		tabbedPane.setIconAt(1,resizedsel);
		
		ImageIcon AnaIcon = new ImageIcon(GUI.class.getResource("icons/Analysis_Icon.png"));
		ImageIcon resizedAna = scaleImage(AnaIcon,25,25);
		tabbedPane.setIconAt(2,resizedAna);

		panel_AudioAnalysis.setLayout(null);
	}

	/**
	   * Method with the components of the Home tab, showing the patient information.
	   *
	   * @return audios  List of the paths for the recordings of the patient.
	   */
	public static List<String> HomePage() {
		
		// Icon to show when no patient is imported
		ImageIcon img = new ImageIcon(GUI.class.getResource("icons/No_Patient_Selected.png"));
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

		// Action to import the patient folder with the corresponding data and audios
		// Also has the while loops control variables in order to advance for the next iteration only in the right combination of values
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
				selectCounter=0;
				supposedSelections=1;
				newaudio=false;
			}
		});

		panel_Home.repaint();
		
		// In this step we show the introductory frame, only once.
		// At this point the program body with tabs is already loaded, but not visible.
		// After the first import the starting frame get´s immeadiatelly substituted, preventing the user from waiting.
		if (!secondpass) {
			breath.start();
			secondpass= true;
		}
		
		// Hold step - wait for the user to import the patient folder
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
		
		// From the import, in Home tab all information is presented, besides the audio/recording list.
		//that is passed for the selection tab.
		List<String> audios = Import.getAudioList();
		return audios;
	}

	/**
	   * Method with the components of the first part of the Selection tab, showing the patient recordings, being able to select one.
	   *
	   * @param audios  List of the paths for the recordings of the patient.
	   */
	public static void audioSelection(List<String> audios) {
		tabbedPane.setEnabledAt(1, true);
		String audioNames[] = new String[audios.size()+1];
		audioNames[0] = "Select Patient Audio";
		for(int i=0;i<audios.size();i++) {
			int barIndx= (audios.get(i)).lastIndexOf("\\");
			int pointIndx = (audios.get(i)).lastIndexOf(".");
			audioNames[i+1] = (audios.get(i)).substring(barIndx+1,pointIndx);
		}
		
		// ComboBox with the list of the recordings
		JComboBox<String> audioChoice = new JComboBox<String>(audioNames);
		audioChoice.setBounds(28, 15, 250, 23);
		panel_AudioSelect.add(audioChoice);

		// Action to select the desired recording.
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

		// Hold step - wait for the user to selct the recording that he wants to hear or analize
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
		
		// In the end of this function the path to the selected recording is defined (filename)
		filename=audios.get(chosenIndex);
	}

	/**
	   * Method with components for the audio player and sound graph.
	   */
	@SuppressWarnings("unchecked")
	public static void audioGraphPlayer() throws MatlabExecutionException, MatlabSyntaxException, IllegalArgumentException, IllegalStateException, InterruptedException, ExecutionException, UnsupportedAudioFileException, IOException, LineUnavailableException {

		// Calls the class responsible to get the filtered waveform data (from MATLAB)
		getAudioPlot Audplot = new getAudioPlot(filename,eng);
		double[] wavefrm=Audplot.getWavefrm();
		double FS=Audplot.getFS();
		
		//++++++++++++++++++++++++++++++++++++++++++++PLOT AUDIO SIGNAL++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		// Calls the class with the audio player functionalities
		AudioPlayer audioSel= new AudioPlayer(filename);
		DataTable audioData = new DataTable(Double.class, Double.class);

		// Conversion to time
		double[] time= new double[wavefrm.length];

		for(int i=0; i<time.length;i++) {
			time[i]=i/FS;
			audioData.add(time[i],wavefrm[i]);
		}

		// Design the sound plot using the waveform data. Implementation using the Graal plug-in
		XYPlot audioPlot = new XYPlot(audioData);

		DrawablePanel audioPlotPanel = new DrawablePanel(audioPlot);
		audioPlotPanel.setBounds(28, 73, 452, 121);
		audioPlotPanel.setBackground(Color.WHITE);

		panel_AudioSelect.add(audioPlotPanel);

		LineRenderer lines = new SmoothLineRenderer2D();
		PointRenderer points = new DefaultPointRenderer2D();
		Color blue=new Color(0,200,255);
		lines.setColor(blue); points.setColor(blue);
		lines.setStroke(new BasicStroke(1)); points.setShape(null);
		audioPlot.setLineRenderers(audioData, lines);
		audioPlot.setPointRenderers(audioData, points);

		//++++++++++++++++++++++++++++++++++++++++++++++++++++Audio Player Specs+++++++++++++++++++++++++++++++++++++++++++++
		
		// Elapsed time and slider indication to accompany the audio (not interactive, only a indicator)
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

		// Action where the user can take notes relative to a specific time of the recording
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
		
		panel_AudioSelect.revalidate();
		panel_AudioSelect.repaint();
	}

	/**
	   * Method with the components of the Analysis tab, revealing information about breathing rate and the wheeze detection.
	   */
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
		BreathingExp.setBounds(42, 82, 174, 171);
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
		WheezingDetExp.setBounds(304, 82, 179, 171);
		panel_AudioAnalysis.add(WheezingDetExp);

		JLabel typesAnalysis = new JLabel("Types of Analysis");
		typesAnalysis.setFont(new Font("Tahoma", Font.BOLD, 15));
		typesAnalysis.setBounds(200, 12, 137, 25);
		panel_AudioAnalysis.add(typesAnalysis);

		JButton btnBreathrate = new JButton("Breathing Rate");
		btnBreathrate.setBounds(58, 48, 144, 23);
		panel_AudioAnalysis.add(btnBreathrate);

		JButton btnWheezeDetect = new JButton("Wheeze Detection");
		btnWheezeDetect.setBounds(321, 48, 151, 23);
		panel_AudioAnalysis.add(btnWheezeDetect);
		
		//++++++++++++++++++++++++++++++++++++++++GET MATLAB BREATHING RATE DATA+++++++++++++++++++++++++++++++++++++++++++
		
		// Calls class that extracts from the MATLAB signal processing algorithm, the breathing rate and intensity data
		getBreathingRate br=new getBreathingRate(filename,eng);

		double bpm=br.getBpm();
		double[] wave=br.getWave();
		double fs=br.getFs();
		double[] env=br.getEnv();
		double[][] hard=br.getHard();
		double[][] soft=br.getSoft();
		double[][] mild=br.getMild();

		// Action to reveal the pop-up with the breathing rate information information
		btnBreathrate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				breathRatePopUp BRpopUp=new breathRatePopUp();
				BRpopUp.openPopUp(bpm, wave, fs, env, hard, mild, soft);
			}
		});
		
		//++++++++++++++++++++++++++++++++++++++++++++++GET WHEEZING DATA+++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		// Calls class that extracts from the MATLAB signal processing algorithm, the wheeze location and diagnostic data
		getWheeze wheeze=new getWheeze(filename,eng);
		double[] x=wheeze.getX();
		state=wheeze.getState();
		double fs1=wheeze.getFs();
		String normSpectrumPath=wheeze.getNormSpectrumPath();
		String wheezeSpectrumPath=wheeze.getWheezeSpectrumPath();
		double[] wheezeActivity=wheeze.getWheezeActivity();

		// Action to reveal the pop-up, whether healthy or unhealthy, with the wheezes detection information
		btnWheezeDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (state.equals("Unhealthy")) {
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

		// Hold step - wait for the user to import a new folder and restart the loop
		do{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}
		}while (supposedSelections==selectCounter);
		
		// To add new information about a new patient the previous has to be removed
		if(selectCounter>=1) {
			supposedSelections++;
			panel_AudioAnalysis.removeAll();
			panel_AudioSelect.removeAll();
		}
	}

	/**
	   * Method to rescale an image for the desired size.
	   *
	   * @param icon  Original image, in ImageIcon format.
	   * @param w	  Desired width for the rescaled image.
	   * @param h	  Desired height for the rescaled image.
	   */
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
