
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import java.awt.Choice;
import java.awt.TextField;
import java.awt.Window;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.JEditorPane;

public class TestPathLib {
	private static JFrame frmRespiratorySoundAnalysis;

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
		
		MatlabEngine eng=MatlabEngine.startMatlab();

		//---------------------------------------------------------------------------------------------------------------
		frmRespiratorySoundAnalysis = new JFrame();
		frmRespiratorySoundAnalysis.setTitle("Respiratory Sound Analysis");
		frmRespiratorySoundAnalysis.setBounds(100, 100, 538, 351);
		frmRespiratorySoundAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmRespiratorySoundAnalysis.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_Home = new JPanel();
		//boolean audioInCheck=false;
		JButton ImportBtn = new JButton("Import Patient Folder");
		ImportBtn.setBounds(350, 11, 165, 23);
		//uma fotografia qualquer de pessoa nao lida
		ImageIcon img = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Faculdade e Cadeiras\\LIEB\\Project_java\\LIEB_Project\\person-vector.png");
		ImageIcon resizedImg = scaleImage(img,106,136);
		JLabel nophoto =new JLabel();
		nophoto.setIcon(resizedImg);
		nophoto.setBounds(20, 22, 106, 136);
		panel_Home.add(nophoto);

		AtomicReference<List<String>> audioFiles = new AtomicReference<List<String>>();

		ImportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				String choosertitle ="Dialog";
				chooser.setCurrentDirectory(new java.io.File("C:\\Users\\dtrdu\\Desktop"));
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
						reader = new BufferedReader(new FileReader(Pinfo.get(0)));
						String line = reader.readLine();

						while (line != null) {
							information.add(line);
							// read next line
							line = reader.readLine();
						}
						reader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					ImageIcon Patimg = new ImageIcon(information.get(0));
					ImageIcon resizedPat = scaleImage(Patimg,106,136);
					//JLabel photo =new JLabel(resizedPat);
					//photo.setBounds(20, 22, 106, 136);
					//panel_Home.add(photo);
					nophoto.setIcon(resizedPat);
					//são text panes pq as informações das caixas de text saolidas de forma diferente das jlabels e não da erro
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
					//Add Patient last appointment date
					JTextPane PatLDate = new JTextPane();
					PatLDate.setText(information.get(5));
					PatLDate.setEditable(false);
					PatLDate.setForeground(Color.BLACK);
					PatLDate.setBackground(UIManager.getColor("Button.background"));
					PatLDate.setBounds(275, 131, 106, 14);
					PatLDate.setFont(new Font("Tahoma", Font.PLAIN, 11));
					panel_Home.add(PatLDate);
					//Add information about last appointment
					JTextPane LastInfo = new JTextPane();
					LastInfo.setText(information.get(6));
					LastInfo.setEditable(false);
					LastInfo.setForeground(Color.BLACK);
					LastInfo.setBackground(UIManager.getColor("Button.background"));
					LastInfo.setBounds(195, 167, 317, 38);
					LastInfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
					panel_Home.add(LastInfo);
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
//					Código comentado porque só deve ser implementado no exato moneto antes de fechar o programa
//					Vai ser necessario passar a string writeback por atomicreference no fim
//					String writeBack="";
//					if (newinfo.getText()!=" ") {
//						writeBack =  newinfo.getText();
//
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
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//
//					}


				}
				else {
					System.out.println("No Selection ");
				}
			}
		});
		//lista dos paths para os audios
		List<String> audios = audioFiles.get();

		panel_Home.setLayout(null);
		panel_Home.add(ImportBtn);
		tabbedPane.addTab("Home", null, panel_Home, null);

		JLabel lblNewLabel = new JLabel("Name:");
		lblNewLabel.setBounds(142, 34, 57, 14);
		panel_Home.add(lblNewLabel);

		JLabel lblNewLabel_1= new JLabel("Age:");
		lblNewLabel_1.setBounds(142,59,46,14);
		panel_Home.add(lblNewLabel_1);

		JLabel lblNewLabel_2= new JLabel("Date of Birth:");
		lblNewLabel_2.setBounds(142, 84, 83, 14);
		panel_Home.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Health No.:");
		lblNewLabel_3.setBounds(142, 109, 64, 14);
		panel_Home.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Last Appointment Date:");
		lblNewLabel_4.setBounds(142, 134, 178, 14);
		panel_Home.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Last Appointment Annotations:");
		lblNewLabel_5.setBounds(20, 169, 176, 14);
		panel_Home.add(lblNewLabel_5);
		//hold...
		//----------------------------------------------------------------------------------------
		JPanel panel_AudioSelect = new JPanel();
		tabbedPane.addTab("Audio Selection", null, panel_AudioSelect, null);

		JButton btnSelectAudio = new JButton("Select");
		btnSelectAudio.setBounds(400, 10, 90, 23);
		btnSelectAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_AudioSelect.setLayout(null);

		Choice choice = new Choice();
		choice.setBounds(90, 10, 151, 23);
		panel_AudioSelect.add(choice);
		panel_AudioSelect.add(btnSelectAudio);


		//+++++++++++++++++++++++++++++++GET MATLAB BREATHING RATE DATA+++++++++++++++++++++++++++++++++++++
		getBreathingRate br=new getBreathingRate(new String("C:\\Users\\dtrdu\\Desktop\\Duarte\\audio_wav\\A_rale_wheezing_asthma_8yearold.wav"),eng);
		
		double bpm=br.getBpm();
		double[] wave=br.getWave();
		double fs=br.getFs();
		double[] env=br.getEnv();
		double[][] hard=br.getHard();
		double[][] soft=br.getSoft();
		double[][] mild=br.getMild();
		//++++++++++++++++++++++++++++++++PLOT AUDIO SIGNAL++++++++++++++++++++++++++++++++++++++++++++++++++++

		DataTable audioData = new DataTable(Double.class, Double.class);

		double[] time= new double[wave.length];
		for(int i=0; i<time.length;i++) {
			time[i]=i/fs;
			audioData.add(time[i],wave[i]);
		}

		XYPlot audioPlot = new XYPlot(audioData);

		//Definição do painel onde ficará o sinal audio
		DrawablePanel audioPlotPanel = new DrawablePanel(audioPlot);
		audioPlotPanel.setBounds(30, 88, 452, 121);
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

		//++++++++++++++++++++++++++++++++++++++++++++++++++++END OF AUDIO SIGNAL PLOT+++++++++++++++++++++++++++++++++++++++++++++
		
		
		JButton btnPlayAudio = new JButton("Play");
		btnPlayAudio.setBounds(87, 237, 70, 23);
		panel_AudioSelect.add(btnPlayAudio);

		JButton btnPauseAudio = new JButton("Pause");
		btnPauseAudio.setBounds(167, 237, 77, 23);
		panel_AudioSelect.add(btnPauseAudio);

		JPanel panel_AudioAnalysis = new JPanel();
		tabbedPane.addTab("Audio Analysis", null, panel_AudioAnalysis, null);
		panel_AudioAnalysis.setLayout(null);

		JTextPane txtpnDfdbnntdf = new JTextPane();		
		JLabel lblNewLabel_7 = new JLabel("Time (s)");
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7.setBounds(227, 212, 46, 14);
		panel_AudioSelect.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Audio Signal");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_8.setBounds(193, 64, 110, 14);
		panel_AudioSelect.add(lblNewLabel_8);

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
		lblNewLabel_6.setBounds(200, 24, 137, 14);
		panel_AudioAnalysis.add(lblNewLabel_6);
		
		JButton btnNewButton = new JButton("Breathing Rate");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				breathRatePopUp BRpopUp=new breathRatePopUp();
				BRpopUp.openPopUp(bpm, wave, fs, env, hard, mild, soft);
				
			}
			
		});
		btnNewButton.setBounds(58, 68, 144, 23);
		panel_AudioAnalysis.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Wheeze Detection");
		btnNewButton_1.setBounds(321, 68, 151, 23);
		panel_AudioAnalysis.add(btnNewButton_1);
		
		frmRespiratorySoundAnalysis.setVisible(true);
		
		eng.close();
	}
		
	}