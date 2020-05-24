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

	public AnnotationPopUp(String time,String path) {

		int barInd =  path.lastIndexOf("\\");
		int pointInd =  path.lastIndexOf(".");

		String audioName= path.substring(barInd+1,pointInd);

		AnnotationFrame = new JFrame();
		AnnotationFrame.setTitle("Note on "+audioName);
		AnnotationFrame.setBounds(23,23,321,300);// fazer de forma a aparecer debaixo do segundo especifico do grafico
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
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (newNote.getText()!=" ") {
					String rootpath = path.substring(0,barInd);
					String newNotefolder=rootpath+"\\Annotations";
					String newfilepath=newNotefolder+"\\"+audioName+"_"+time+"_sec.txt";
					//verifica se para o paciente já existe o diretorio de anotacoes
					File NoteDir = new File(newNotefolder);
					if(!NoteDir.isDirectory()) {
						NoteDir.mkdir();
					}
					//cria um ficheiro de texto com as anotações
					try {

						File newFileNote = new File(newfilepath);
						newFileNote.createNewFile();
					} catch (IOException er) {
						er.printStackTrace();
					}


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
