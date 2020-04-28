import javax.swing.*;

public class TabPanelViewer extends LoginExample {

	// JFrame 
	static JFrame frameTab;
	static JTabbedPane tabbedPane;


	

	//esta class vai implementar a class com os codigos matlab
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Creating instance of JFrame
		frameTab = new JFrame("Respiratory Monitoring");
		// creating the tabed panels in order for the user to be able to choose
		tabbedPane = new JTabbedPane();
		//tabbedPane.setSelectedIndex(0);		
		//for each tab a set of instructions have to be defined put inside a panel
		//panel 1 for the first tab to have a picture (posterior from matlab, a file chooser
		JPanel panel1= new JPanel();
		
 
		ImageIcon img = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Faculdade e Cadeiras\\LIEB\\Project_java\\LIEB_Project\\myPlot.jpg");   
		JScrollPane png = new JScrollPane(new JLabel(img));
		png.setBounds(10,10,559,300);
		panel1.add(png);
		JLabel l1 = new JLabel("Raw audio signal");
		l1.setBounds(200,410,100,20);
		panel1.add(l1);
		panel1.setLayout(null);//new GridLayout(0,2));
		panel1.setVisible(true);
		
		JPanel panel2= new JPanel();
		ImageIcon img2 = new ImageIcon("C:\\Users\\dtrdu\\Desktop\\Duarte\\Pascoa_20\\cook.jpeg");   
		JScrollPane png2 = new JScrollPane(new JLabel(img2));
		png2.setBounds(10,10,559,345);
		panel2.add(png2);
		JLabel label2= new JLabel("Possible result from matalb codes");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setSize(175, 20);
		label2.setLocation(206, 360);
		JTextArea tarea=new JTextArea("matalab result of wheeze");  
		tarea.setBounds(0,379, 579,54);
		panel2.add(label2);
		panel2.add(tarea);  
		panel2.setSize(300,300);  
		panel2.setLayout(null);  
		panel2.setVisible(true);

		//TabPanelViewer TPV= new  TabPanelViewer();
		
		JPanel panel3= new JPanel();
		placeComponents(panel3); //This works on ly if you run the program and not in the windows builder
		
		panel3.setVisible(true);
			

		tabbedPane.addTab("Signal", null,panel1);
		tabbedPane.addTab("Evaluation", null,panel2);
		tabbedPane.addTab("Extend class try", null, panel3);
		
		

		frameTab.getContentPane().add(tabbedPane); // isto é o mesmo que adicionar o tabbed à frame

		frameTab.setSize(600,500);//400 width and 500 height
		frameTab.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameTab.setVisible(true);//making the frame visible  


	}
}