package com.github.philippecade.biketoworkstats;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	
	Main() {
		this.statsGenerator = new StatsGenerator();
	}

	private void showUI() {
		//1. Create the frame.
		JFrame frame = new JFrame("bikewtowork.ch Statistics");

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
		chooseFileButton.addActionListener((event) -> {
			JFileChooser fileChooser = new JFileChooser();
			if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(panel)) {
				try {
					this.outputFile = this.statsGenerator.generateReports(fileChooser.getSelectedFile());
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
		this.openButton.addActionListener((event) -> {
			try {
				Desktop.getDesktop().open(this.outputFile);
			} catch (Exception e) {
				showMessage(e);
			}
		});
		panel.add(this.openButton);
		return panel;
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
