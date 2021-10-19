package ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import core.Main;

public class MenuListener extends Main implements ActionListener {
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Timelapse")) {
			INTERVAL = 2000;
			delay = 33;
			value=33;
			fpsslider.setLabel("FPS: "+ value);
			fpsslider.setActionCommand("FPS: "+ value);
		}
		if (actionEvent.getActionCommand().equals("High")) {
			INTERVAL = 20;
			delay = 20;
			value=50;
			fpsslider.setLabel("FPS: "+ value);
			fpsslider.setActionCommand("FPS: "+ value);
		}

		if (actionEvent.getActionCommand().equals("Medium")) {
			INTERVAL = 33;
			delay = 33;
			value=30;
			fpsslider.setLabel("FPS: "+ value);
			fpsslider.setActionCommand("FPS: "+ value);
		}
		if (actionEvent.getActionCommand().equals("Low")) {
			
			INTERVAL = 66;
			delay = 66;
			value=15;
			fpsslider.setLabel("FPS: "+ value);
			fpsslider.setActionCommand("FPS: "+ value);

		}
		if (actionEvent.getActionCommand().equals("Close")) {
			System.out.println("Closed");
			System.exit(1);

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
		if (actionEvent.getActionCommand().equals("Folder")) {
			try {
				Desktop.getDesktop().open(output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(actionEvent.getActionCommand().equals("FPS: " + value)) {
			// Käytössä vammasesti vaa toteutettu :D ei oikeesti unused
			@SuppressWarnings("unused")
			FPS_Slider slider = new FPS_Slider();
			
		}
		
		
		System.out.println("Selected: " + actionEvent.getActionCommand());
	}
}
