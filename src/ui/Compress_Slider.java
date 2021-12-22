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
	 * Luo ikkunan josta voi valita fps Voidaan k‰ytt‰‰ tulevaisuudessa vaikka
	 * Asetuksiin
	 */
	JFrame frame = new JFrame("Compression & Quantiziser");
	JSlider slider = new JSlider(0, 170);

	JSlider sliderColor = new JSlider(0, 2);

	JLabel labelColor = new JLabel("Colorizer: " + colorizer);

	JPanel pane = new JPanel();

	JLabel label = new JLabel("Compression: " + CompressionAmount);


	public Compress_Slider() {
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(this);
		slider.setValue(CompressionAmount);
		

		sliderColor.setMajorTickSpacing(1);
		sliderColor.setPaintTicks(true);
		sliderColor.setPaintLabels(true);
		sliderColor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				
				colorizer = sliderColor.getValue();
				labelColor.setText("Colorizer: " + sliderColor.getValue());
			}
		});
		sliderColor.setValue(colorizer);

		frame.setLocation(dim.width - 280, dim.height - 230);
		frame.setResizable(false);
		frame.setSize(250, 220);
		
		pane.add(label);
		pane.add(slider);
		pane.add(labelColor);
		pane.add(sliderColor);
		frame.add(pane);
		frame.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int value = slider.getValue();
		CompressionAmount = value;
		label.setText("Compression: " + value);
	}

}
