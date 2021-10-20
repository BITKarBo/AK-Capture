package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Main;

public class Compress_Slider extends Main implements ChangeListener {
	/**
	 * Luo ikkunan josta voi valita fps
	 * Voidaan k‰ytt‰‰ tulevaisuudessa vaikka Asetuksiin
	 */
	JFrame frame = new JFrame("Compression");
	JSlider slider = new JSlider(0,170);
	
	JPanel pane = new JPanel();
	
	 JLabel label = new JLabel("Compression: "+CompressionAmount);
	 
	public void setSliderfps(JSlider slider) {
		this.slider = slider;
	}

	public Compress_Slider() {
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(this);
		slider.setValue(CompressionAmount);
		frame.setLocation(dim.width-280,dim.height-150);
	    frame.setResizable(false);
	    frame.setSize(250, 120);
	   
	    pane.add(label);
	    pane.add(slider);
	    frame.add(pane);
	    frame.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int value = slider.getValue();
		CompressionAmount=value;
		//System.out.println("NEW FPS: "+ slider.getValue());
		label.setText("Compression: "+value);
	}

}
