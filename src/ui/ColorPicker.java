package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JFrame;

import core.Main;

public class ColorPicker extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	ColorPicker() {

		Color initialcolor = Main.väri;
		Main.väri = JColorChooser.showDialog(this, "Select a color", initialcolor);
		this.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}