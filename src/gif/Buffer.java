package gif;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import core.Main;

public class Buffer extends Main implements Runnable {
	Rectangle rectangle;

	@Override
	public void run() {

		BufferedImage kuva = null;
		try {

			while (capturing || kuvatque.peek() != null) {
				long alkuaika = System.nanoTime();
				if (kuvatque.peek() != null) {
					kuva = kuvatque.take();
					System.gc();

					if (buf == 1)
						OGWriter.writeToSequence(kuva);

					bufferviive = ((System.nanoTime() - alkuaika) / 1000000.0);
				} else {
					Thread.sleep(1);
				}

			}
			if (buf == 1) {
				OGWriter.close(); // MUISTUTUTUTUTS sulje writer enne streamia :D
				stream.close();
			}

			stopCapture();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
