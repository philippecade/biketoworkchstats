package com.github.philippecade.biketoworkstats;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main UI.
 * 
 * @author xphc
 */
public class Main {
	private File[] outputFiles;
	private JLabel statusLabel;
	private JButton openButton;
	private JProgressBar progressBar;

	private static final String PROP_FILE_NAME = System.getProperty("user.home") + "/.btwstats.txt";
	private static final String LAST_FOLDER_PROP = "lastFolder";
	private SwingWorker<File[], Void> worker;
	private JButton chooseFileButton;

	Main() {
		// empty
	}

	private void showUI() {
		// 1. Create the frame.
		JFrame frame = new JFrame("biketowork.ch Statistics");

		// 2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 3. Create components and put them in the frame.
		frame.getContentPane().add(buildTopComponent(), BorderLayout.NORTH);
		frame.getContentPane().add(buildCenterComponent(), BorderLayout.CENTER);
		frame.getContentPane().add(buildBottomComponent(), BorderLayout.SOUTH);

		// 4. Size the frame.
		frame.setPreferredSize(new Dimension(600, 100));
		frame.pack();
		frame.setLocationRelativeTo(null);

		// 5. Show it.
		frame.setVisible(true);
	}

	JPanel buildTopComponent() {
		JPanel panel = new JPanel();

		this.chooseFileButton = new JButton("Choose statistics file(s)");
		this.chooseFileButton.addActionListener(this::generateStats);
		panel.add(this.chooseFileButton);

		this.openButton = new JButton("Open output folder");
		this.openButton.setEnabled(false);
		this.openButton.addActionListener(event -> {
			try {
				if (this.outputFiles.length > 0) {
					Desktop.getDesktop().open(this.outputFiles[0].getParentFile());
				}
			} catch (Exception e) {
				showMessage(e);
			}
		});
		panel.add(this.openButton);

		return panel;
	}

	private void generateStats(ActionEvent event) {
		JFileChooser fileChooser = getFileChooser();
		if (JFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog((Component) event.getSource())) {
			return;
		}

		this.chooseFileButton.setEnabled(false);
		showMessage("Processing...");
		this.worker = new SwingWorker<File[], Void>() {
			@Override
			protected File[] doInBackground() throws Exception {
				StatsGenerator statsGenerator = new StatsGenerator();
				File[] selectedFiles = fileChooser.getSelectedFiles();
				if (selectedFiles.length == 0) {
					return new File[0];
				}
				File[] resultFiles = new File[selectedFiles.length];
				for (int i = 0; i < selectedFiles.length; i++) {
					resultFiles[i] = statsGenerator.generateReports(selectedFiles[i]);
					setProgress((i+1) * 100 / selectedFiles.length);
				}
				statsGenerator.generateHistoricalReports(selectedFiles);
				rememberLastFolder(selectedFiles[0].getParentFile());
				return resultFiles;
			}
		};
		this.worker.addPropertyChangeListener(l -> {
			if ("progress".equals(l.getPropertyName())) {
				this.progressBar.setValue((int) l.getNewValue());
			} else if ("state".equals(l.getPropertyName()) && SwingWorker.StateValue.DONE.equals(l.getNewValue())) {
				try {
					this.outputFiles = this.worker.get();
					if (this.outputFiles.length > 0) {
						this.openButton.setEnabled(true);
						showMessage("Output stored to: " + this.outputFiles[0].getParent());
					}
					this.progressBar.setValue(0);
					this.chooseFileButton.setEnabled(true);
				} catch (InterruptedException | ExecutionException e) {
					showMessage(e.getMessage());
				}
			}
		});
		this.worker.execute();
	}

	private Component buildCenterComponent() {
		this.progressBar = new JProgressBar(0, 100);
		return this.progressBar;
	}

	private JFileChooser getFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
		fileChooser.setMultiSelectionEnabled(true);
		File lastFolder = getLastFolder();
		if (lastFolder != null) {
			fileChooser.setCurrentDirectory(lastFolder);
		}
		return fileChooser;
	}

	/**
	 * Remembers the folder where the last stats were read from
	 * 
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
	 * 
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
		showMessage("Error: " + e.getClass() + " " + e.getMessage());
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.showUI();
	}
}
