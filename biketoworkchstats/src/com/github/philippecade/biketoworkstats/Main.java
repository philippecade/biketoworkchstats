package com.github.philippecade.biketoworkstats;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main UI.
 * 
 * @author xphc
 */
public class Main {
	private StatsGenerator statsGenerator;
	private File outputFile;
	private JLabel statusLabel;
	private JButton openButton;
	
	private static final String PROP_FILE_NAME = System.getProperty("user.home") + "/.btwstats.txt";
	private static final String LAST_FOLDER_PROP = "lastFolder";
	
	Main() {
		this.statsGenerator = new StatsGenerator();
	}

	private void showUI() {
		//1. Create the frame.
		JFrame frame = new JFrame("biketowork.ch Statistics");

		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//3. Create components and put them in the frame.
		frame.getContentPane().add(buildTopComponent(), BorderLayout.NORTH);
		frame.getContentPane().add(buildBottomComponent(), BorderLayout.SOUTH);

		//4. Size the frame.
		frame.setPreferredSize(new Dimension(600, 100));
		frame.pack();
		frame.setLocationRelativeTo(null);

		//5. Show it.
		frame.setVisible(true);
	}
	
	JPanel buildTopComponent() {
		JPanel panel = new JPanel();

		JButton chooseFileButton = new JButton("Choose statistics file");
		chooseFileButton.addActionListener(event -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
			File lastFolder = getLastFolder();
			if (lastFolder != null) {
				fileChooser.setCurrentDirectory(lastFolder);
			}
			if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(panel)) {
				try {
					this.outputFile = this.statsGenerator.generateReports(fileChooser.getSelectedFile());
					rememberLastFolder(fileChooser.getSelectedFile().getParentFile());
					this.openButton.setEnabled(true);
					showMessage("Output stored to: "+this.outputFile);
				} catch (IOException e) {
					showMessage(e);
				}
			}
		});
		panel.add(chooseFileButton);
		
		this.openButton = new JButton("Open formatted statistics");
		this.openButton.setEnabled(false);
		this.openButton.addActionListener(event -> {
			try {
				Desktop.getDesktop().open(this.outputFile);
			} catch (Exception e) {
				showMessage(e);
			}
		});
		panel.add(this.openButton);
		return panel;
	}
	
	/**
	 * Remembers the folder where the last stats were read from
	 * @param lastFolder
	 */
	private void rememberLastFolder(File lastFolder) {
		Properties properties = new Properties();
		properties.setProperty(LAST_FOLDER_PROP, lastFolder.getAbsolutePath());
		try (FileWriter writer = new FileWriter(PROP_FILE_NAME)) {
			properties.store(writer, "Stored by Bike To Work Statistics");
		} catch (IOException e) {
			showMessage(e);
		}
	}
	
	/**
	 * Returns the last folder where the stats were read from
	 * @return folder or {@code null}
	 */
	private File getLastFolder() {
		if (!new File(PROP_FILE_NAME).exists()) {
			return null;
		}
		try (FileReader reader = new FileReader(PROP_FILE_NAME)) {
			Properties properties = new Properties();
			properties.load(reader);
			return new File(properties.getProperty(LAST_FOLDER_PROP));
		} catch (IOException e) {
			// ignore if not found
			return null;
		}
	}

	JComponent buildBottomComponent() {
		this.statusLabel = new JLabel();
		this.statusLabel.setText("Ready");
		return this.statusLabel;
	}

	private void showMessage(String message) {
		this.statusLabel.setText(message);
	}
	private void showMessage(Exception e) {
		showMessage("Error: "+e.getClass()+" "+e.getMessage());
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.showUI();
	}
}
