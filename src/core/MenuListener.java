package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PopupActionListener extends Main implements ActionListener {
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("High")) {
			INTERVAL = 16;
			delay = 16;
		}

		if (actionEvent.getActionCommand().equals("Medium")) {
			INTERVAL = 33;
			delay = 33;
		}
		if (actionEvent.getActionCommand().equals("Low")) {
			INTERVAL = 66;
			delay = 66;

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
		System.out.println("Selected: " + actionEvent.getActionCommand());
	}
}
