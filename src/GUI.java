import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;


import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.DrawablePanel;
import de.erichseifert.gral.ui.InteractivePanel;
//import matlabtest.getWheeze;
//import matlabtest.wheezePopUpHealthy;
//import matlabtest.wheezePopUpUnhealthy;
import sun.awt.image.ToolkitImage;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import java.awt.TextField;
import java.awt.Window;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.Second;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.JTextField;



public class GUI {

	private static JFrame frmRespiratorySoundAnalysis;
	private static JTextField textField;
	private static int importCounter=0;

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
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException, UnsupportedAudioFileException, IOException, LineUnavailableException {

		//------------------------------------------GET FILTERED AUDIO AND INTENSITY MATLAB DATA-------------------------------

		MatlabEngine eng=MatlabEngine.startMatlab();


		//---------------------------------------------------Initialization of the GUI------------------------------------------------------------
		frmRespiratorySoundAnalysis = new JFrame();
		frmRespiratorySoundAnalysis.setTitle("Respiratory Sound Analysis");
		frmRespiratorySoundAnalysis.setBounds(100, 100, 538, 351);
		frmRespiratorySoundAnalysis.setResizable(false);
		frmRespiratorySoundAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRespiratorySoundAnalysis.setVisible(true);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmRespiratorySoundAnalysis.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_Home = new JPanel();
		tabbedPane.addTab("Home", null, panel_Home, null);

		JPanel panel_AudioSelect = new JPanel();
		tabbedPane.addTab("Audio Selection", null, panel_AudioSelect, null);
		tabbedPane.setEnabledAt(1, false);

		JPanel panel_AudioAnalysis = new JPanel();
		tabbedPane.addTab("Audio Analysis", null, panel_AudioAnalysis, null);
		tabbedPane.setEnabledAt(2, false);

		panel_Home.setLayout(null);


		//--------------------------------------------------Patient Home Page------------------------------------------------------------------
		//boolean audioInCheck=false;
		JButton ImportBtn = new JButton("Import Patient Folder");
		ImportBtn.setBounds(350, 11, 165, 23);
		panel_Home.add(ImportBtn);
		//uma fotografia qualquer de pessoa nao lida
		ImageIcon img = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Faculdade e Cadeiras\\LIEB\\Project_java\\LIEB_Project\\person-vector.png");
		ImageIcon resizedImg = scaleImage(img,106,136);
		JLabel photo =new JLabel();
		photo.setIcon(resizedImg);
		photo.setBounds(20, 22, 106, 136);
		panel_Home.add(photo);










		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(142, 34, 57, 14);
		panel_Home.add(lblName);

		JLabel lblAge= new JLabel("Age:");
		lblAge.setBounds(142,59,46,14);
		panel_Home.add(lblAge);

		JLabel lblDoB= new JLabel("Date of Birth:");
		lblDoB.setBounds(142, 84, 83, 14);
		panel_Home.add(lblDoB);

		JLabel lblHno = new JLabel("Health No.:");
		lblHno.setBounds(142, 109, 64, 14);
		lblHno.setVisible(true);
		panel_Home.add(lblHno);

		JLabel lblPAI = new JLabel("Previous Appointments Information");
		lblPAI.setBounds(229,138,210,14);
		panel_Home.add(lblPAI);

		
		int supposedClicks = 0;
		AtomicBoolean analysisLock = new AtomicBoolean();
		boolean analysis = true;
			do {
			System.out.println("1.");	
			boolean selectionLock = true;
			AtomicBoolean lockAtm = new AtomicBoolean();
			lockAtm.set(true);
			AtomicReference<List<String>> audioFiles = new AtomicReference<List<String>>();




			
			ImportBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					JFileChooser chooser = new JFileChooser();

					String choosertitle ="Select Patient Folder";
					chooser.setCurrentDirectory(new java.io.File("C:\\Users\\dtrdu\\Desktop"));//\\Desktop
					chooser.setDialogTitle(choosertitle);
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setAcceptAllFileFilterUsed(true);

					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						//Save in out string
						String PatientData=(chooser.getSelectedFile()).toString();
						List<String> Pinfo=null;
						List<String> Paudio=null;
						try (Stream<Path> walk = Files.walk(Paths.get(PatientData))) {
							Pinfo = walk.map(x -> x.toString()).filter(f -> f.endsWith(".txt")).collect(Collectors.toList());
							Pinfo.removeIf(f->f.contains("Annotations"));
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						try (Stream<Path> walk = Files.walk(Paths.get(PatientData))) {
							Paudio = walk.map(x -> x.toString()).filter(f -> f.endsWith(".wav")).collect(Collectors.toList());
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						audioFiles.set(Paudio);

						BufferedReader reader;
						List<String> information=new ArrayList<String>();

						try {
							reader = new BufferedReader(new FileReader(Pinfo.get(0)));//path para o .txt
							String line = reader.readLine();
							String prevAp ="";
							while (line != null) {
								//colocar aqui um if e fazer string manipulation
								if (information.size()<5) {
									information.add(line);
								}
								else if (information.size()==5) {
									information.remove(prevAp);
									prevAp=prevAp+line;
									information.add(prevAp);
								}
								else if (information.size()>5) {
									information.remove(prevAp);
									prevAp=prevAp+"\n"+line;
									information.add(prevAp);
								}
								// read next line
								line = reader.readLine();
							}

							reader.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
//						importCounter++;
						ImageIcon Patimg = new ImageIcon(information.get(0));
						ImageIcon resizedPat = scaleImage(Patimg,106,136);
						photo.setIcon(resizedPat);
						//são text panes pq as informações das caixas de text sao lidas de forma diferente das jlabels e não da erro
						//Add Patient Name
						JTextPane PatName = new JTextPane();
						PatName.setText(information.get(1));
						PatName.setEditable(false);
						PatName.setForeground(Color.BLACK);
						PatName.setBackground(UIManager.getColor("Button.background"));
						PatName.setBounds(180, 32, 178, 14);
						PatName.setFont(new Font("Tahoma", Font.PLAIN, 11));
						panel_Home.add(PatName);
						//Add Patient Age
						JTextPane PatAge = new JTextPane();
						PatAge.setText(information.get(2));
						PatAge.setEditable(false);
						PatAge.setForeground(Color.BLACK);
						PatAge.setBackground(UIManager.getColor("Button.background"));
						PatAge.setBounds(171, 57,57, 14);
						PatAge.setFont(new Font("Tahoma", Font.PLAIN, 11));
						panel_Home.add(PatAge);
						//Add Patient Date of birth
						JTextPane PatDoB = new JTextPane();
						PatDoB.setText(information.get(3));
						PatDoB.setEditable(false);
						PatDoB.setForeground(Color.BLACK);
						PatDoB.setBackground(UIManager.getColor("Button.background"));
						PatDoB.setBounds(217, 82, 107, 14);
						PatDoB.setFont(new Font("Tahoma", Font.PLAIN, 11));
						panel_Home.add(PatDoB);
						//Add Patient health number
						JTextPane PatHno = new JTextPane();
						PatHno.setText(information.get(4));
						PatHno.setEditable(false);
						PatHno.setForeground(Color.BLACK);
						PatHno.setBackground(UIManager.getColor("Button.background"));
						PatHno.setBounds(206, 107, 83, 14);
						PatHno.setFont(new Font("Tahoma", Font.PLAIN, 11));
						panel_Home.add(PatHno);
						//Add information about previous appointments
						JTextPane LastInfo = new JTextPane();
						LastInfo.setText(information.get(5));
						LastInfo.setEditable(false);
						LastInfo.setForeground(Color.BLACK);
						LastInfo.setBackground(UIManager.getColor("Button.background"));
						LastInfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
						JScrollPane scrollInfo = new JScrollPane(LastInfo);
						scrollInfo.setBounds(20,160,476,48);
						scrollInfo.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
						panel_Home.add(scrollInfo);
						//label for new anotations
						JTextPane An = new JTextPane();
						An.setText("Annotations:");
						An.setEditable(false);
						An.setForeground(Color.DARK_GRAY);//GET
						An.setBackground(UIManager.getColor("Button.background"));
						An.setBounds(20, 216, 83, 14);
						An.setFont(new Font("Tahoma", Font.BOLD, 11));
						panel_Home.add(An);
						//Area to make annotations
						JTextPane newinfo = new JTextPane();
						newinfo.setText(" ");
						newinfo.setEditable(true);
						newinfo.setForeground(Color.BLACK);//GET
						newinfo.setBackground(Color.WHITE);
						newinfo.setBounds(116,218,378,55);
						newinfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
						panel_Home.add(newinfo);
						//					Código comentado porque só deve ser implementado no exato momento antes de fechar o programa
						//					Vai ser necessario passar a string writeback por atomicreference no fim
						//					String writeBack="";
						//					if (newinfo.getText()!=" ") {
						//						writeBack =  newinfo.getText();
						//						REVER DADO QUE FOI ALTEARDO QUANDO FOI POSTO O SCROLL... UAR O FILEWRITER TALEVZ
						//						String lineToRemove = information.get(6);
						//						String currentLine;
						//
						//						try {
						//							BufferedReader Breader = new BufferedReader(new FileReader(Pinfo.get(0)));
						//							BufferedWriter Bwriter = new BufferedWriter(new FileWriter(Pinfo.get(0),true));
						//
						//							while((currentLine = Breader.readLine()) != null) {
						//								if (currentLine.equals(lineToRemove)) {
						//									currentLine="";
						//									Bwriter.write(writeBack.trim());
						//								}						    
						//							}
						//							Bwriter.close();
						//							Breader.close();
						//						} catch (IOException e1) {
						//							
						//							e1.printStackTrace();
						//						}
						//
						//					}
						lockAtm.set(false);
						importCounter++;
					}
					else {
						System.out.println("No Selection");
					}
				}
			});

			

			
			System.out.println("2.");
			panel_Home.repaint();
			//o programa vai esperar neste loop até se ler a informacao do paciente



			System.out.println("import counter = " + importCounter);
			System.out.println("selectionLock = " + selectionLock);

			do{
				selectionLock=lockAtm.get();
				Thread.sleep(200);
			}while (selectionLock==true && importCounter==supposedClicks);




			//lista dos paths para os audios
			List<String> audios = audioFiles.get();
			audios.forEach(System.out::println);
			//----------------------------------------------Audio Selection---------------------------------------------------
			
			tabbedPane.setEnabledAt(1, true);
			String audioNames[] = new String[audios.size()];
			for(int i=0;i<audios.size();i++) {
				int barIndx= (audios.get(i)).lastIndexOf("\\");
				int pointIndx = (audios.get(i)).lastIndexOf(".");
				audioNames[i] = (audios.get(i)).substring(barIndx+1,pointIndx);
				System.out.println("for para criar o array para a combo");
			}

			JComboBox<String> audioChoice = new JComboBox<String>(audioNames);
			System.out.println("nova combo box atualizada");
			audioChoice.setBounds(28, 15, 250, 23);
			panel_AudioSelect.add(audioChoice);

			

			AtomicReference<String> selectAtm = new AtomicReference<String>();
			analysisLock.set(true);
			JButton btnSelectAudio = new JButton("Select");
			btnSelectAudio.setBounds(390, 15, 90, 23);
			btnSelectAudio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//sempre que se seleciona meter o botao play visible e esconder o botao resume
					String selectedAud = (String)audioChoice.getSelectedItem();
					selectAtm.set(selectedAud);
					
					analysisLock.set(false);
				}

			});
			panel_AudioSelect.add(btnSelectAudio);
			panel_AudioSelect.setLayout(null);

			System.out.println("3.");
			//o programa vai esperar neste loop até se ler a informacao do paciente
			do{
				analysis=analysisLock.get();
				Thread.sleep(200);
			}while (analysis==true);

			int chosenIndex = 0;
			for(int i=0;i<audios.size();i++) {
				if (audios.get(i).contains(selectAtm.get())){
					chosenIndex=i;
				}
			}

			String filename=audios.get(chosenIndex);





			//---------------------------------------------------------------------------------------------------------------------------------

			//+++++++++++++++++++++++++++++++GET MATLAB AUDIO PLOT DATA++++++++++++++++++++++++++++++++++++++++++++
			String pathAud= filename;
			getAudioPlot Audplot = new getAudioPlot(pathAud,eng);

			double[] wavefrm=Audplot.getWavefrm();
			double FS=Audplot.getFS();
			//++++++++++++++++++++++++++++++++PLOT AUDIO SIGNAL++++++++++++++++++++++++++++++++++++++++++++++++++++

			AudioPlayer audioSel= new AudioPlayer(pathAud);
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
			currentDuration.setBounds(7, 54, 25,14);
			JSlider slider = new JSlider(0, 100);
			slider.setValue(0);
			slider.setBounds(23, 49, 462,23);
			audioSel.AudioSlider(slider,currentDuration);//influencia só como o slider atua
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

			JTextField  GotoIndex = new JTextField(10);
			GotoIndex.setBounds(385, 240, 66, 22);
			panel_AudioSelect.add(GotoIndex);
			GotoIndex.setColumns(10);

			JButton btnNewButton_2 = new JButton("Go to");
			btnNewButton_2.addActionListener(new ActionListener() {
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
			btnNewButton_2.setBounds(296, 239, 79, 23);
			panel_AudioSelect.add(btnNewButton_2);


			JLabel lblNewLabel = new JLabel("sec.");
			lblNewLabel.setBounds(461, 243, 26, 14);
			panel_AudioSelect.add(lblNewLabel);


			JLabel lblNewLabel_7 = new JLabel("Time (s)");
			lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_7.setBounds(231, 209, 46, 14);
			panel_AudioSelect.add(lblNewLabel_7);

			JButton btnNewButton_3 = new JButton("Take Note");
			btnNewButton_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String decTime = audioSel.getStrSecond();
					AnnotationPopUp note =new AnnotationPopUp(decTime,pathAud);
				}
			});

			btnNewButton_3.setBounds(385, 205, 95, 23);
			panel_AudioSelect.add(btnNewButton_3);
			tabbedPane.setEnabledAt(2, true);
			//important to reveal the information of the panel
			panel_AudioSelect.repaint();
			//-----------------------------------------AUDIO ANALYSIS-------------------------------------------------------
			System.out.println("4.");
			panel_AudioAnalysis.setLayout(null);
			JTextPane txtpnDfdbnntdf = new JTextPane();	
			txtpnDfdbnntdf.setFont(new Font("Tahoma", Font.PLAIN, 12));
			txtpnDfdbnntdf.setText("The recorded breathing sound\r\n" + 
					"is analysed in order to recover\r\n" + 
					"the total number of breaths in\r\n" + 
					"the audio file, distinguishing\r\n" + 
					"them between soft, mild or\r\n" + 
					"hard breaths.\r\n\n" + 
					"The timestamp of each breath\r\n" + 
					"is saved.");
			txtpnDfdbnntdf.setForeground(Color.BLACK);
			txtpnDfdbnntdf.setBackground(SystemColor.controlHighlight);
			txtpnDfdbnntdf.setEditable(false);
			txtpnDfdbnntdf.setBounds(42, 102, 174, 171);
			panel_AudioAnalysis.add(txtpnDfdbnntdf);

			JTextPane txtpnDfdbnntdf_1 = new JTextPane();
			txtpnDfdbnntdf_1.setText("The respiratory recording is\r\n" + 
					"examined in order to detected\r\n" + 
					"high frequency periodic\r\n" + 
					"sounds.These wheezes are\r\n" + 
					"then registered and highlighted\r\n" + 
					"in the sound wave.\r\n\n" + 
					"In the end the algorith\r\n" + 
					"proposes whether the patient\r\n" + 
					"is healthy or not.");
			txtpnDfdbnntdf_1.setForeground(Color.BLACK);
			txtpnDfdbnntdf_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
			txtpnDfdbnntdf_1.setEditable(false);
			txtpnDfdbnntdf_1.setBackground(SystemColor.controlHighlight);
			txtpnDfdbnntdf_1.setBounds(304, 102, 179, 171);
			panel_AudioAnalysis.add(txtpnDfdbnntdf_1);

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
			//C:\\Users\\A541\\OneDrive - Universidade do Porto\\MIB\\3ºAno\\2º semestre\\LIEB\\Projeto\\Compiled breath audios\\Vesicular breath sound\\A_rale_vesicular.wav
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
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(state.equals("Healthy")) {
						wheezePopUpHealthy healthyPopUp=new wheezePopUpHealthy();
						try {
							healthyPopUp.openPopUp(x,state, fs1, normSpectrumPath);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
			//+++++++++++++++++++++++++++++++++++++GET WHEEZING DATA++++++++++++++++++++++++++++++++++++++++++++++++

			supposedClicks++;
			System.out.println("5.");
			do{
				Thread.sleep(200);
			}while (importCounter==supposedClicks);
			System.out.println("6.");

		}while (importCounter>supposedClicks);
		System.out.println("out of everything");

		eng.close();
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
