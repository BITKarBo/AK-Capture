package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Main;

public class FPS_Slider extends Main implements ChangeListener {
	/**
	 * Luo ikkunan josta voi valita fps
	 * Voidaan k‰ytt‰‰ tulevaisuudessa vaikka Asetuksiin
	 */
	JFrame frame = new JFrame("Choose FPS");
	JSlider sliderfps = new JSlider(5,50);
	
	JPanel pane = new JPanel();
	
	 JLabel label = new JLabel("FPS: "+value);
	 
	public void setSliderfps(JSlider sliderfps) {
		this.sliderfps = sliderfps;
	}

	public FPS_Slider() {
		
		sliderfps.setMajorTickSpacing(15);
		sliderfps.setMinorTickSpacing(2);
		sliderfps.setPaintTicks(true);
		sliderfps.setPaintLabels(true);
		sliderfps.addChangeListener(this);
		sliderfps.setValue((int) value);
		frame.setLocation(dim.width-280,dim.height-150);
	    frame.setResizable(false);
	    frame.setSize(250, 120);
	   
	    pane.add(label);
	    pane.add(sliderfps);
	    frame.add(pane);
	    frame.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		value = sliderfps.getValue();
		
		INTERVAL = (int)(1000 / value);
		delay = (int)(1000 / value);
		sliderfps.setValue((int) value);
		//System.out.println("NEW FPS: "+ sliderfps.getValue());
		fpsslider.setLabel("FPS: "+ value);
		fpsslider.setActionCommand("FPS: "+ value);
		label.setText("FPS: "+value);
	}

}
