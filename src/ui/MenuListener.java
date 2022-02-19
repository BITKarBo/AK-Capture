package ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JColorChooser;

import core.Main;

public class MenuListener extends Main implements ActionListener {

	public void actionPerformed(ActionEvent actionEvent) {
		MenuItem og = (MenuItem) actionEvent.getSource();
		if (actionEvent.getActionCommand().equals("Timelapse")) {
			INTERVAL = 2000;
			delay = 33;
			value=0.5;
			fpsslider.setLabel("FPS: "+ value);
			fpsslider.setActionCommand("FPS: "+ value);
			clearBalls(actionEvent);
			og.setFont(new Font("Arial", Font.BOLD, 13));
		}
		if (actionEvent.getActionCommand().equals("High")) {
			INTERVAL = 20;
			delay = 20;
			value=50;
			fpsslider.setLabel("FPS: "+ value);
			fpsslider.setActionCommand("FPS: "+ value);
			
			clearBalls(actionEvent);
			og.setFont(new Font("Arial", Font.BOLD, 13));
		}

		if (actionEvent.getActionCommand().equals("Medium")) {
			INTERVAL = 33;
			delay = 33;
			value=30;
			fpsslider.setLabel("FPS: "+ value);
			fpsslider.setActionCommand("FPS: "+ value);
			clearBalls(actionEvent);
			og.setFont(new Font("Arial", Font.BOLD, 13));
		}
		if (actionEvent.getActionCommand().equals("Low")) {
			
			INTERVAL = 66;
			delay = 66;
			value=15;
			fpsslider.setLabel("FPS: "+ value);
			fpsslider.setActionCommand("FPS: "+ value);
			clearBalls(actionEvent);
			og.setFont(new Font("Arial", Font.BOLD, 13));

		}
		if (actionEvent.getActionCommand().equals("Close")) {
			System.out.println("Closed");
			System.exit(1);

		}
		if (actionEvent.getActionCommand().equals("Compression...")) {
			@SuppressWarnings("unused")
			Compress_Slider s = new Compress_Slider();
		}
		if (actionEvent.getActionCommand().equals("Color...")) {
			@SuppressWarnings("unused")
			 Color initialcolor=Color.RED;    
			 väri=JColorChooser.showDialog(frame,"Select a color",initialcolor);   
			     
		}
		if (actionEvent.getActionCommand().equals("Loop: ON")) {
			loop=false;
			loopp.setActionCommand("Loop: OFF");
			loopp.setLabel("Loop: OFF");
		}
		if (actionEvent.getActionCommand().equals("Loop: OFF")) {
			loop=true;
			loopp.setActionCommand("Loop: ON");
			loopp.setLabel("Loop: ON");
		}
		if (actionEvent.getActionCommand().equals("Circle: ON")) {
			ympyrä=false;
			circle.setActionCommand("Circle: OFF");
			circle.setLabel("Circle: OFF");
		}
		if (actionEvent.getActionCommand().equals("Circle: OFF")) {
			
			ympyrä=true;
			circle.setActionCommand("Circle: ON");
			circle.setLabel("Circle: ON");
		}
		if (actionEvent.getActionCommand().equals("Folder")) {
			try {
				Desktop.getDesktop().open(output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(actionEvent.getActionCommand().equals("FPS: " + value)) {
			clearBalls(actionEvent);
			// Käytössä vammasesti vaa toteutettu :D ei oikeesti unused
			@SuppressWarnings("unused")
			FPS_Slider slider = new FPS_Slider();
			og.setFont(new Font("Arial", Font.BOLD, 13));
			
		}
		
		
		System.out.println("Selected: " + actionEvent.getActionCommand());
	}
	
	void clearBalls(ActionEvent actionEvent) {
		MenuItem x =(MenuItem) actionEvent.getSource();
		PopupMenu p = (PopupMenu) x.getParent();
		for(int i=0;i<p.getItemCount();i++) {
			MenuItem item =p.getItem(i);
			item.setFont(new Font("Arial", Font.PLAIN, 13));
			
		
			
		}
		
	
	}
}
