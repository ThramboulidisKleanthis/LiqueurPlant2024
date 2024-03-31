// Based on PlantsVisFrame

package physicalSysSim.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import physicalSysSim.*;
import system.LPSystem;

public class PlantSimFrame extends JFrame{
	public PlantSim itsPlant;
//	public SiloVis [] s = new SiloVis[4];
//	public LpButton[] cr = new LpButton[2];
	public static JTextArea textArea;
	public JProgressBar[] ll= new JProgressBar[4]; 
	private int [][] dim= {{0,0},{200,0},{0,200},{200,200}};
	public LpButton pAbutton, pBbutton;
	private static final long serialVersionUID = 1L;

	public PlantSimFrame(PlantSim plant){
		super("LPSystem V3.0 : Physical System simulator Visualization (for proccess mining) ");
		itsPlant = plant;
		this.setLayout(null);
	   	this.setFont(new Font("TimesRoman", Font.PLAIN, 12));
	   	this.setBackground(Color.white);
		this.setSize(new Dimension(830,450));
		this.setLocation(390,200);      

	   	this.setResizable(false);  
		
//		textArea = new JTextArea();
//        textArea.setColumns(100);
//        textArea.setRows(2);
//        textArea.setBounds(10, 30, 380, 410);
//      	this.add(textArea);
	   	pAbutton = new LpButton("Start Proccess A",20+400, 5, 120, 20,Color.GREEN,Color.black, this);
	   	pAbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				(new Thread(itsPlant.itsSR.itsSystem.process[0])).start();
				pAbutton.setBackground(Color.RED);
			}});
	   	add(pAbutton);
    
	   	pBbutton = new LpButton("Start Proccess B",20+400+200, 5, 120, 20,Color.GREEN,Color.black, this);
	   	pBbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				(new Thread(itsPlant.itsSR.itsSystem.process[1])).start();
				pBbutton.setBackground(Color.RED);
			}});
	   	add(pBbutton);

	   	textArea = new JTextArea("", 10,1);
       	var sp = new JScrollPane(textArea); 
      	sp.setBounds(10, 10, 380, 400);
       	sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
       	textArea.setEditable(false);  
       	textArea.setBackground(Color.white);
      	this.add(sp);

      	for(int i=0;i<4;i++) {
      		ll[i]=new JProgressBar(SwingConstants.VERTICAL,0,2000); 

      		//		ll.setBounds(50+x+400, 50+y, 70, 120);   
      		ll[i].setBounds(50+400+dim[i][0], 50+dim[i][1], 70, 120);     
      		ll[i].setValue(0);    
      		ll[i].setStringPainted(true); 
      		ll[i].setForeground(((i==0)||(i==3))?Color.cyan:Color.gray);
      		add(ll[i]);    
      	}
    	report("Visualizer of the Liqueur Plant System's Simulator\n");
    	report("LiquerTypeAQuantity: " + LPSystem.LiqueurQuantity[0] + "\tLiquerTypeBQuantity: " + LPSystem.LiqueurQuantity[1]);
    	report("Silo (S1,S2,S,S4) Params:");
    	report("FillingTime\tPouringTime\tHeatingTime\tMixingTime");
        for(int silo=0;silo<4;silo++)
    		report(PlantSimParams.val[silo][0] +"\t"+ PlantSimParams.val[silo][1] +"\t"+
    				PlantSimParams.val[silo][2] +"\t"+PlantSimParams.val[silo][3]);
        report("");
//    			+ "Instantiate Cyber-Physical components (CPCs) or MTCs.\nActivate the Controller and then \n"
//    			+ "Press cyan T button to triger Temp sensor \nPress StopMixing at Operator's panel to terminate mixing\n\n");    	
//    	    	
	   	this.setVisible(true);
	   	this.toFront();     
	   	setLocationRelativeTo(null);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
		
	public static void report(String message) {
		textArea.append(message+"\n");
		}

//	public void addMtcVis(int i, SiloVis itsVis) {
//		s[i]=itsVis;
//	}
//
//	public void addResVis(int i, LpButton itsVis) {
//		cr[i]=itsVis;
//	}
}




	