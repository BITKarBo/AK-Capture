package gif;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import core.Main;

public class Buffer extends Main implements Runnable {
	Rectangle rectangle;


	@Override
	public void run() {
		
		BufferedImage kuva = null;
			while (capturing || kuvatque.peek() != null) {
				long alkuaika = System.nanoTime();
				if (kuvatque.peek() != null) {
				
					try {
						kuva = kuvatque.take();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("BUFFER QUESIZE: " + kuvatque.size());

					try {
						
						writer.writeToSequence(kuva);

					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("BUFFER Time: " + (System.nanoTime() - alkuaika) / 1000000.0 + "ms");
				} else {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
			try {

				writer.close();
				stopCapture();

			} catch (Exception e) {
				e.printStackTrace();
			}

		

	}

}
