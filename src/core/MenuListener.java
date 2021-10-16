package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PopupActionListener extends Main implements ActionListener {
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("High")) {
			delay = 33;
		}

		if (actionEvent.getActionCommand().equals("Medium")) {
			delay = 100;
		}
		if (actionEvent.getActionCommand().equals("Low")) {
			delay = 200;

		}
		System.out.println("Selected: " + actionEvent.getActionCommand());
	}
}
