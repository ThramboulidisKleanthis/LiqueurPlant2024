package physicalSysSim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import system.LPSystem;

public class PlantSimParams {
	//	public static int MIXING_TIME=400;		
	//	public static int HEATING_TIME=300;
	//	public static int []fillingTime = {400,300,500,200};
	//	public static int []pouringTime = {200,500,400,300}; 
	public static int [][]val= new int[4][4];

	public static void read() {
		//		fillingTime = new int[4];
		//		pouringTime = new int[4];
		File myObj;
		Scanner myReader=null,sc=null;
		String str;

		myObj = new File("LPSystemV3_PlantSimulatorParameters.txt");
		try {
			myReader = new Scanner(myObj); 
		} catch (FileNotFoundException e) {
			System.out.println("Parameters File Not Found");
			e.printStackTrace();
		} 
		myReader.nextLine();
		for(int i=0;i<2;i++) {
			str=myReader.nextLine();
			sc = new Scanner(str);
			sc.useDelimiter(",");
			sc.next();
			LPSystem.LiqueurQuantity[i]=sc.nextInt();
		}

		for(int silo=0;silo<4;silo++) {
			str = myReader.nextLine();
//		        System.out.println(s);
			sc = new Scanner(str);
			sc.useDelimiter(",");
			sc.next();
			for(int i=0;i<4;i++)
				val[i][silo]=sc.nextInt();
			sc.close();
		}
		myReader.close();

//		for(int silo=0;silo<4;silo++)
//			System.out.println(val[silo][0] +"\t"+ val[silo][1] +"\t"+
//					val[silo][2] +"\t"+val[silo][3] +"\t");
	} 
}
