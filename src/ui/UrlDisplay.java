package ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import core.Main;

public class UrlDisplay extends Main {

	protected static String aakkosetjanumerot = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

	JFrame frame = new JFrame("File Uploader");
	JPanel pane = new JPanel();
	Socket socket = null;
	String fileName;

	File filetosend;
	BoxLayout boxlayout = new BoxLayout(pane, BoxLayout.Y_AXIS);
	FileInputStream fileStream;

	JLabel error = new JLabel();
	JLabel emty = new JLabel();
	JLabel link = new JLabel();
	JButton uploadButton;
	protected boolean uploaded;

	@SuppressWarnings("unchecked")
	public UrlDisplay() throws IOException {
		uploaded = false;
		frame.setLocation(dim.width - 300, dim.height - 160);
		frame.setResizable(false);
		frame.setSize(350, 130);
		uploadButton = new JButton("Upload!");

		uploadButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0 && !uploaded) {

					try {
						uploaded=true;
						if(MediaServer == true) {
							upload();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		});
		pane.setBorder(new EmptyBorder(10, 100, 10, 100));
		pane.setLayout(boxlayout);
		
		emty.setText((endFile).toString());

		Font font = link.getFont();
		@SuppressWarnings("rawtypes")
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		link.setFont(font.deriveFont(attributes));
		link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		emty.setFont(font.deriveFont(attributes));
		emty.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		emty.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) {
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().open(new File((output + "/" + endFile).toString()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		link.setAlignmentX(Component.CENTER_ALIGNMENT);
		emty.setAlignmentX(Component.CENTER_ALIGNMENT);
		uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		error.setAlignmentX(Component.CENTER_ALIGNMENT);

		pane.add(link);
		pane.add(emty);
		if(MediaServer == true) {
			pane.add(uploadButton);
		}
		pane.add(error);
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
	}

	private void upload() throws IOException {

		final StringBuilder randomLisä = new StringBuilder(6);

		for (int i = 0; i < 6; i++) {

			int ch = (int) (aakkosetjanumerot.length() * Math.random());

			randomLisä.append(aakkosetjanumerot.charAt(ch));

		}
		fileName = randomLisä + "_" + endFile.toString();

		File filetosend = new File((output + "/" + endFile).toString());

		FileInputStream fileStream = new FileInputStream(filetosend.getAbsolutePath());

		
		// UPLOAD IMAGE TO SERVER

		int portworking = 0;
		if (portworking == 0) {
			try {
				if(IP.contains("//")) {
					IP = IP.substring(IP.lastIndexOf("/")+1);
				}
				socket = new Socket(IP, Integer.parseInt(PORT));
				portworking = 1;
			} catch (Exception e) {
				portworking = 0;
				System.out.println("Cant connect to: " + IP +":"+ PORT);
				error.setText("Cant connect to: " + IP +":"+ PORT);
			}
		}

		if (portworking == 0) {
			System.out.println("Remote server is not online.");

			error.setText("Remote server is not online. :(");

			
		} else {
			System.out.println("Starting File Send.");
			DataOutput dataSteam = new DataOutputStream(socket.getOutputStream());

			byte[] fileNameBytes = fileName.getBytes();
			byte[] fileContentBytes = new byte[(int) filetosend.length()];

			fileStream.read(fileContentBytes);

			dataSteam.writeInt(32);
			final byte[] tunnus = Password;
			dataSteam.write(tunnus);

			dataSteam.writeInt(fileNameBytes.length);
			dataSteam.write(fileNameBytes);

			dataSteam.writeInt(fileContentBytes.length);
			dataSteam.write(fileContentBytes);
			System.out.println("File Send");
			System.out.println("File found at: " + IP + PATH + fileName);
			link.setText(IP + PATH + fileName);
			socket.close();
			link.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() > 0) {
						if (Desktop.isDesktopSupported()) {
							Desktop desktop = Desktop.getDesktop();
							try {
								URI uri = new URI(IP + PATH + fileName);
								desktop.browse(uri);
							} catch (IOException ex) {
								ex.printStackTrace();
							} catch (URISyntaxException ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			});
		}

		frame.setLocation(dim.width - 520, dim.height - 160);
		pane.remove(emty);
		pane.remove(uploadButton);
		frame.pack();
		fileStream.close();
		StringSelection selection = new StringSelection(IP + PATH + fileName);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, null);
	}
}
