package physicalSysSim.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;

public class LpButton extends Button {
	private static final long serialVersionUID = 1L;

	public LpButton(String l, int x, int y, int h, int w, Color b, Color f, Container itsContainer){
		super(l);
		setFont(new Font("TimesRoman",Font.BOLD,14));
//		setBounds(new Rectangle(x,y,w,h));
		setBounds(new Rectangle(x,y,h,w));
		setBackground(b);
		setForeground(f); 
		itsContainer.add(this);
	}
}
