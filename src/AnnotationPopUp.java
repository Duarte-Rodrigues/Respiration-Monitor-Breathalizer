/**
 * LIEB PROJECT 2019/2020
 * BREATHALIZER
 * Performed by: Duarte Rodrigues e João Fonseca
 * 
 * AnnotationPopUp: Allows the user to take notes on a particular time of the recording. 
 * The notes are saved in a "Annotation" folder inside the patient folder
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class AnnotationPopUp {

	private static JFrame AnnotationFrame;

	/**
	   * Constructor to create the annotation pop-up, and save it in the "Annotations" folder
	   * 
	   * @param time  audio paused time, where the note was intended to be taken
	   * @param path  path to the selected recording
	   */
	public AnnotationPopUp(String time,String path) {

		int barInd =  path.lastIndexOf("\\");
		int pointInd =  path.lastIndexOf(".");

		String audioName= path.substring(barInd+1,pointInd);

		AnnotationFrame = new JFrame();
		AnnotationFrame.setTitle("Note on "+audioName);
		AnnotationFrame.setBounds(23,23,321,300);
		AnnotationFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		AnnotationFrame.getContentPane().setLayout(null);

		JLabel header = new JLabel("At "+time+" sec. :");
		header.setBounds(10,11,200,18);
		header.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		AnnotationFrame.getContentPane().add(header);

		JTextPane newNote = new JTextPane();
		newNote.setText(" ");
		newNote.setEditable(true);
		newNote.setForeground(Color.BLACK);
		newNote.setBackground(Color.WHITE);
		newNote.setBounds(10,40,285,157);
		newNote.setFont(new Font("Tahoma", Font.PLAIN, 11));
		AnnotationFrame.getContentPane().add(newNote);

		JButton save = new JButton("Save");
		save.setBounds(107,208,92,30);

		//Action to save the text written under the "...\patient\Annotations" directory. It is saved as .txt and is updated at any save
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (newNote.getText()!=" ") {
					String rootpath = path.substring(0,barInd);
					String newNotefolder=rootpath+"\\Annotations";
					String newfilepath=newNotefolder+"\\"+audioName+"_"+time+"_sec.txt";
					File NoteDir = new File(newNotefolder);

					//Checks whether the "Annotation" folder already exists
					if(!NoteDir.isDirectory()) {
						NoteDir.mkdir();
					}

					try {
						File newFileNote = new File(newfilepath);
						newFileNote.createNewFile();
					} catch (IOException er) {
						er.printStackTrace();
					}

					//The .txt has the name of the recording, the time at which the note was taken and the user description
					try {
						FileWriter myWriter = new FileWriter(newfilepath);
						String bodytext=(newNote.getText()).trim();
						String fulltext= "Audio: "+audioName+"\nTime(in seconds): "+time+"\nAnnotation: "+bodytext+"\n";
						myWriter.write(fulltext);
						myWriter.close();
					} catch (IOException er) {
						er.printStackTrace();
					}
				}
			}
		});

		AnnotationFrame.getContentPane().add(save);
		AnnotationFrame.setVisible(true);
	}

}
