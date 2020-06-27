/**
 * LIEB PROJECT 2019/2020
 * BREATHALIZER - Breathing Monitor
 * @author Duarte Rodrigues
 * @author João Fonseca
 * 
 * ImportPatInfo: class responsible to allow the user to import a patient folder. 
 * From the folder it extracts the patient information (name, age, date of birth, health number and 
 * the general annotations from the previous consultations), contained in the .txt and the respiratory recordings.
 */

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

public class ImportPatInfo extends GUI{

	public List<String> Paudio;
	public boolean lockStop;
	public int importCounter;
	public String newPatAnotation;
	public String PatientDataFolder;
	public String PatInformationPath;
	public JTextPane newinfo;
	public JTextPane LastInfo;
	public JTextPane PatHno;
	public JTextPane PatDoB;
	public JTextPane PatAge;
	public JTextPane PatName;
	private JScrollPane scrollInfo;
	private JScrollPane scrollAnnotation;

	/**
	 * Constructor where all the global variables are initialized.
	 */
	public ImportPatInfo() {
		lockStop=true;
		Paudio=null;
		importCounter=0;
		newPatAnotation="";
		PatInformationPath="";
		PatientDataFolder="";
		LastInfo = new JTextPane();
		PatHno = new JTextPane();
		PatDoB = new JTextPane();
		PatAge = new JTextPane();
		PatName = new JTextPane();
		newinfo = new JTextPane();
		scrollInfo= new JScrollPane(LastInfo);
		scrollAnnotation =new JScrollPane(newinfo);
	}

	/**
	 * Method to control and extract the contents on the patient folder.
	 * 
	 * @param panel_Home  panel where the information will be displayed.
	 * @param photo       variable where the photograph is updated.
	 */
	public void ImportInformation (JPanel panel_Home,JLabel photo) {

		// Search through the computer directories
		JFileChooser chooser = new JFileChooser();
		String choosertitle ="Select Patient Folder";
		chooser.setCurrentDirectory(new java.io.File("../Documents"));
		chooser.setDialogTitle(choosertitle);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//We want to import the patient folder that already has the .txt with information and the audio recordings
		chooser.setAcceptAllFileFilterUsed(true);

		// Checks whether the user actually selected a patient folder or canceled the process
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			PatientDataFolder=(chooser.getSelectedFile()).toString();
			List<String> Pinfo=null;
			try (Stream<Path> walk = Files.walk(Paths.get(PatientDataFolder))) {
				Pinfo = walk.map(x -> x.toString()).filter(f -> f.endsWith(".txt")).collect(Collectors.toList());
				Pinfo.removeIf(f->f.contains("Annotations"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//Setting the path for the patient information .txt
			setPatInformationPath(Pinfo.get(0));
			
			try (Stream<Path> walk = Files.walk(Paths.get(PatientDataFolder))) {
				Paudio = walk.map(x -> x.toString()).filter(fw -> fw.endsWith(".wav")).collect(Collectors.toList());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			BufferedReader reader;
			List<String> information=new ArrayList<String>();

			// Reading the .txt, that has a certain order to the display of the information
			// So each line contains specific topic (name, age, etc)
			// From the health number down, it is considered previous appointments annotations
			try {
				reader = new BufferedReader(new FileReader(PatInformationPath));
				String line = reader.readLine();
				String prevAp ="";
				while (line != null) {
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
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// Display of the information gathered
			// Add Patient photo
			ImageIcon PatImg = new ImageIcon(information.get(0));
			ImageIcon resizedPat = scaleImage(PatImg,106,136);
			photo.setIcon(resizedPat);
			// Add Patient Name
			setPatName(information.get(1));
			PatName.setEditable(false);
			PatName.setForeground(Color.BLACK);
			PatName.setOpaque(false);
			PatName.setBackground(UIManager.getColor("Button.background"));
			PatName.setBounds(180, 32, 160, 14);
			PatName.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_Home.add(PatName);
			// Add Patient Age
			setPatAge(information.get(2));
			PatAge.setEditable(false);
			PatAge.setForeground(Color.BLACK);
			PatAge.setBackground(Color.WHITE);
			PatAge.setBounds(171, 57,57, 14);
			PatAge.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_Home.add(PatAge);
			// Add Patient Date of birth
			setPatDoB(information.get(3));
			PatDoB.setEditable(false);
			PatDoB.setForeground(Color.BLACK);
			PatDoB.setBackground(Color.WHITE);
			PatDoB.setBounds(217, 82, 107, 14);
			PatDoB.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_Home.add(PatDoB);
			// Add Patient health number
			setPatHno(information.get(4));
			PatHno.setEditable(false);
			PatHno.setForeground(Color.BLACK);
			PatHno.setBackground(Color.WHITE);
			PatHno.setBounds(206, 107, 83, 14);
			PatHno.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_Home.add(PatHno);
			// Add information about previous appointments
			setLastInfo(information.get(5));
			LastInfo.setEditable(false);
			LastInfo.setForeground(Color.BLACK);
			LastInfo.setBackground(Color.WHITE);
			LastInfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
			scrollInfo.setBounds(20,160,476,48);
			scrollInfo.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollInfo.setVisible(true);
			panel_Home.add(scrollInfo);
			// Label
			JTextPane An = new JTextPane();
			An.setText("Annotations:");
			An.setEditable(false);
			An.setForeground(Color.BLACK);
			An.setBackground(Color.WHITE);
			An.setBounds(20, 216, 83, 14);
			An.setFont(new Font("Tahoma", Font.BOLD, 11));
			panel_Home.add(An);
			// Area to take annotations
			newinfo.setText(" ");
			newinfo.setEditable(true);
			newinfo.setForeground(Color.BLACK);
			newinfo.setBackground(Color.WHITE);
			newinfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
			scrollAnnotation.setBounds(116,218,378,55);
			scrollAnnotation.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollAnnotation.setVisible(true);
			panel_Home.add(scrollAnnotation);

			lockStop=false;//the import was succesfull so the hold step gets unlocked (lock->false)
			importCounter++;
		}
		else {
			//Case in which no directory is chosen
			importCounter++;
			if(importCounter!=1) {
				lockStop=false;
			}
		}
	}

	/**
	 * Method to get the list of the path to the audios
	 * 
	 * @return Paudio  List for the audio/recording paths.
	 */
	public List<String> getAudioList() {
		return Paudio;
	}

	/**
	 * Since the hold loops are only unlocked when the import is succesfully done,
	 * this method allows to extract that information.
	 * 
	 * @return lockStop  lock variable of the hold step.
	 */
	public boolean getLockStop(){
		return lockStop;
	}
 
	/**
	 * Method to set the lock variable.
	 * 
	 * @param lockStop  lock variable of the hold step.
	 */
	public void setLockStop(boolean state){
		lockStop=state;
	}

	/**
	 * Method to get the import counter. 
	 * 
	 * @return importCounter  counter for how many times were the imports made (sucessfull or not).
	 */
	public int getImportCounter() {
		return importCounter;
	}

	/**
	 * Method to get the annotations of the previous appointments.
	 * 
	 * @return LastInfo.text  Annotations of the previous appointments.
	 */
	public String getLastInfo() {
		return LastInfo.getText();
	}
 
	/**
	 * Method to get the patient health number.
	 * 
	 * @return PatHno patient health number.
	 */
	public String getPatHno() {
		return PatHno.getText();
	}

	/**
	 * Method to get the patient date of birth.
	 * 
	 * @return PatDoB patient date of birth.
	 */
	public String getPatDoB() {
		return PatDoB.getText();
	}

	/**
	 * Method to get the patient age.
	 * 
	 * @return Patage patient age.
	 */
	public String getPatAge() {
		return PatAge.getText();
	}

	/**
	 * Method to get the patient name.
	 * 
	 * @return PatName patient name.
	 */
	public String getPatName() {
		return PatName.getText();
	}

	/**
	 * Method to set the annotations of the last appointments, accordingly to the information on the .txt
	 * 
	 * @param txt  Text to write on the annotations of the previous appointments
	 */
	public void setLastInfo(String txt) {
		LastInfo.setText(txt);
	}

	/**
	 * Method to set the patient health number, accordingly to the information on the .txt
	 * 
	 * @param txt  Text to write on the patient health number.
	 */
	public void setPatHno(String txt) {
		PatHno.setText(txt);
	}

	/**
	 * Method to set the patient date of birth, accordingly to the information on the .txt
	 * 
	 * @param txt  Text to write on the patient date of birth.
	 */
	public void setPatDoB(String txt) {
		PatDoB.setText(txt);
	}

	/**
	 * Method to set the patient age, accordingly to the information on the .txt
	 * 
	 * @param txt  Text to write on the patient age.
	 */
	public void setPatAge(String txt) {
		PatAge.setText(txt);
	}

	/**
	 * Method to set the patient name, accordingly to the information on the .txt
	 * 
	 * @param txt  Text to write on the patient name.
	 */
	public void setPatName(String txt) {
		PatName.setText(txt);
	}

	/**
	 * Method to set the path for the patient information .txt file.
	 * 
	 * @param path  path for the patient information .txt file.
	 */
	public void setPatInformationPath(String path) {
		PatInformationPath=path;
	}
 
	/**
	 * Method to save the annotations (taken on the home tab) on the .txt file adding the today's date information
	 */
	public void savePatAnnotations() {
		FileWriter Fwriter;
		try {
			//If there's no patient path there's no purpose on saving
			if(PatInformationPath!="") {
				Fwriter =new FileWriter(PatInformationPath,true);
				//if nothing was written there's no purpose on saving (white spaces count as nothing)
				if (!(newinfo.getText().trim().isEmpty())) {
					String currentDate=(java.time.LocalDate.now()).toString();
					String newAnnotation=(newinfo.getText()).trim();
					newPatAnotation="*"+currentDate+"*\n"+newAnnotation+"\n";
					Fwriter.write(newPatAnotation);
					newinfo.setText(" ");
				}
				Fwriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
