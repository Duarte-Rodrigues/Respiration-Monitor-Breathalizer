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
	public String PatInformationPath;
	public JTextPane newinfo;
	public JTextPane LastInfo;
	public JTextPane PatHno;
	public JTextPane PatDoB;
	public JTextPane PatAge;
	public JTextPane PatName;
	private JScrollPane scrollInfo;
	private JScrollPane scrollAnnotation;

	public ImportPatInfo() {
		lockStop=true;
		Paudio=null;
		importCounter=0;
		newPatAnotation="";
		PatInformationPath="";
		LastInfo = new JTextPane();
		PatHno = new JTextPane();
		PatDoB = new JTextPane();
		PatAge = new JTextPane();
		PatName = new JTextPane();
		newinfo = new JTextPane();
		scrollInfo= new JScrollPane(LastInfo);
		scrollAnnotation =new JScrollPane(newinfo);
	}

	public void ImportInformation (JPanel panel_Home,JLabel photo) {


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
			try (Stream<Path> walk = Files.walk(Paths.get(PatientData))) {
				Pinfo = walk.map(x -> x.toString()).filter(f -> f.endsWith(".txt")).collect(Collectors.toList());
				Pinfo.removeIf(f->f.contains("Annotations"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			setPatInformationPath(Pinfo.get(0));
			try (Stream<Path> walk = Files.walk(Paths.get(PatientData))) {
				Paudio = walk.map(x -> x.toString()).filter(f -> f.endsWith(".wav")).collect(Collectors.toList());
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			BufferedReader reader;
			List<String> information=new ArrayList<String>();

			try {
				reader = new BufferedReader(new FileReader(PatInformationPath));//path para o .txt com as informacoes do paciente
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

			ImageIcon PatImg = new ImageIcon(information.get(0));
			ImageIcon resizedPat = scaleImage(PatImg,106,136);
			photo.setIcon(resizedPat);
			//Add Patient Name
			setPatName(information.get(1));
			PatName.setEditable(false);
			PatName.setForeground(Color.BLACK);
			PatName.setOpaque(false);
			PatName.setBackground(UIManager.getColor("Button.background"));
			PatName.setBounds(180, 32, 160, 14);
			PatName.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_Home.add(PatName);
			//Add Patient Age
			setPatAge(information.get(2));
			PatAge.setEditable(false);
			PatAge.setForeground(Color.BLACK);
			PatAge.setBackground(Color.WHITE);
			PatAge.setBounds(171, 57,57, 14);
			PatAge.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_Home.add(PatAge);
			//Add Patient Date of birth
			setPatDoB(information.get(3));
			PatDoB.setEditable(false);
			PatDoB.setForeground(Color.BLACK);
			PatDoB.setBackground(Color.WHITE);
			PatDoB.setBounds(217, 82, 107, 14);
			PatDoB.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_Home.add(PatDoB);
			//Add Patient health number
			setPatHno(information.get(4));
			PatHno.setEditable(false);
			PatHno.setForeground(Color.BLACK);
			PatHno.setBackground(Color.WHITE);
			PatHno.setBounds(206, 107, 83, 14);
			PatHno.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_Home.add(PatHno);
			//Add information about previous appointments
			setLastInfo(information.get(5));
			LastInfo.setEditable(false);
			LastInfo.setForeground(Color.BLACK);
			LastInfo.setBackground(Color.WHITE);
			LastInfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
			scrollInfo.setBounds(20,160,476,48);
			scrollInfo.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollInfo.setVisible(true);
			panel_Home.add(scrollInfo);
			//label for new anotations
			JTextPane An = new JTextPane();
			An.setText("Annotations:");
			An.setEditable(false);
			An.setForeground(Color.BLACK);
			An.setBackground(Color.WHITE);
			An.setBounds(20, 216, 83, 14);
			An.setFont(new Font("Tahoma", Font.BOLD, 11));
			panel_Home.add(An);
			//Area to make annotations
			newinfo.setText(" ");
			newinfo.setEditable(true);
			newinfo.setForeground(Color.BLACK);//GET
			newinfo.setBackground(Color.WHITE);
			newinfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
			scrollAnnotation.setBounds(116,218,378,55);
			scrollAnnotation.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollAnnotation.setVisible(true);
			panel_Home.add(scrollAnnotation);

			lockStop=false;
			importCounter++;
		}
		else {
			importCounter++;
			if(importCounter!=1) {
				lockStop=false;
			}
		}
	}

	public List<String> getAudioList() {
		return Paudio;
	}

	public boolean getLockStop(){
		return lockStop;
	}

	public void setLockStop(boolean state){
		lockStop=state;
	}

	public int getImportCounter() {
		return importCounter;
	}

	public String getLastInfo() {
		return LastInfo.getText();
	}

	public String getPatHno() {
		return PatHno.getText();
	}

	public String getPatDoB() {
		return PatDoB.getText();
	}

	public String getPatAge() {
		return PatAge.getText();
	}

	public String getPatName() {
		return PatName.getText();
	}

	public void setLastInfo(String txt) {
		LastInfo.setText(txt);
	}

	public void setPatHno(String txt) {
		PatHno.setText(txt);
	}

	public void setPatDoB(String txt) {
		PatDoB.setText(txt);
	}

	public void setPatAge(String txt) {
		PatAge.setText(txt);
	}

	public void setPatName(String txt) {
		PatName.setText(txt);
	}

	public void setPatInformationPath(String path) {
		PatInformationPath=path;
	}

	public void savePatAnnotations() {
		FileWriter Fwriter;
		try {
			if(PatInformationPath!="") {
				Fwriter =new FileWriter(PatInformationPath,true);
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
