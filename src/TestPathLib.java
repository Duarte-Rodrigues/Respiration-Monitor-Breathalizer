import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class TestPathLib extends JPanel implements ActionListener {
		JButton go;

		JFileChooser chooser;
		String choosertitle;

		public TestPathLib() {
			go = new JButton("Import Patient Data");
			go.setBounds(10, 38, 142, 23);
			go.addActionListener(this);
			setLayout(null);
			add(go);
		}

		public void actionPerformed(ActionEvent e) {

			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("C:\\Users\\dtrdu\\Desktop"));
			chooser.setDialogTitle(choosertitle);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//
			// disable the "All files" option.
			//
			chooser.setAcceptAllFileFilterUsed(true);
			//    
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
				System.out.println("getCurrentDirectory(): " 
						+  chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : " 
						+  chooser.getSelectedFile());
			}
			else {
				System.out.println("No Selection ");
			}
		}

		public Dimension getPreferredSize(){
			return new Dimension(400, 200);
		}

		public static void main(String s[]) {
			JFrame frame = new JFrame("");
			TestPathLib panel = new TestPathLib();
			frame.addWindowListener(
					new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							System.exit(0);
						}
					}
					);
			frame.getContentPane().add(panel,"Center");
			frame.setSize(panel.getPreferredSize());
			frame.setVisible(true);
		}
	}