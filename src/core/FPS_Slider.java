package core;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FPS_Slider extends Main implements ChangeListener {
	/**
	 * Luo ikkunan josta voi valita fps
	 * Voidaan k‰ytt‰‰ tulevaisuudessa vaikka Asetuksiin
	 */
	JFrame frame = new JFrame("Choose Framerate");
	JSlider sliderfps = new JSlider(5,50);

	public void setSliderfps(JSlider sliderfps) {
		this.sliderfps = sliderfps;
	}

	public FPS_Slider() {
		sliderfps.setMajorTickSpacing(15);
		sliderfps.setMinorTickSpacing(2);
		sliderfps.setPaintTicks(true);
		sliderfps.setPaintLabels(true);
		sliderfps.addChangeListener(this);
		sliderfps.setValue(value);
		
	    frame.setResizable(false);
	    frame.setSize(250, 120);
	    frame.setLocationRelativeTo(null);
	    
	    frame.add(sliderfps);
	    
	    frame.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		value = sliderfps.getValue();
		targetFPS = (int)(1000 / sliderfps.getValue());
		INTERVAL = (int)(1000 / sliderfps.getValue());
		sliderfps.setValue(value);
		//System.out.println("NEW FPS: "+ sliderfps.getValue());
		fpsslider.setLabel("FPS: "+ value);
		fpsslider.setActionCommand("FPS: "+ value);
	}

}
