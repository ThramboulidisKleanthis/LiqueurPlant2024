package physicalSysSim.gui;

import java.awt.Color;
import java.awt.TextField;
import java.awt.Container;

public class LpTextField extends TextField {

	private static final long serialVersionUID = 1L;

	public LpTextField(int x,int y, int w,int h,String label, Container itsContainer ){
		super("0",14);
		this.setEditable(false);  //disable editing
		this.setBounds(x,y,w,h);
		itsContainer.add(this);
		this.setText(label);
		this.setBackground(Color.white);
	}
}
