package com.gudenau.n3ds.sm4sh.badgeeditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.RandomAccessFile;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class Recovery implements WindowListener, ActionListener {

	private static JFrame frame, parent;
	private static Recovery instance = new Recovery();
	private static File source, dest;
	private static JButton sourceButton, destButton, go;
	
	public static void recover(JFrame parent) {
		Recovery.parent = parent;
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		
		con.gridheight = 1;
		con.gridwidth = 3;
		con.gridx = 0;
		con.gridy = 0;
		
		panel.add(new JLabel("RECOVERY"), con);
		con.gridy = 1;
		panel.add(new JLabel("This will attempt to repair"), con);
		con.gridy = 2;
		panel.add(new JLabel("the wrong save version error."), con);
		con.gridy = 3;
		panel.add(new JLabel("THIS MIGHT NOT WORK, MAKE BACKUPS"), con);
		
		con.gridwidth = 1;
		
		con.gridy = 4;
		sourceButton = new JButton("Source");
		sourceButton.addActionListener(instance);
		panel.add(sourceButton, con);
		con.gridx = 1;
		destButton = new JButton("Dest");
		destButton.addActionListener(instance);
		panel.add(destButton, con);
		con.gridx = 2;
		go = new JButton("Go");
		go.addActionListener(instance);
		panel.add(go, con);
		
		frame = new JFrame("Save Recovery");
		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.addWindowListener(instance);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		BadgeEditor.centerFrame(frame);
		parent.setEnabled(false);
		frame.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		parent.setEnabled(true);
		frame.dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if(source == destButton){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileFilter(){

				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().endsWith(".bin");
				}

				@Override
				public String getDescription() {
					return "Sm4sh 3DS save";
				}
			});
			
			switch(chooser.showOpenDialog(frame)){
			case JFileChooser.APPROVE_OPTION:
				dest = chooser.getSelectedFile();
			}
		}else if(source == sourceButton){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileFilter(){

				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().endsWith(".bin");
				}

				@Override
				public String getDescription() {
					return "Sm4sh 3DS save";
				}
			});
			
			switch(chooser.showOpenDialog(frame)){
			case JFileChooser.APPROVE_OPTION:
				source = chooser.getSelectedFile();
			}
		}else if(source == go && source != null && dest != null){
			try{
				RandomAccessFile in = new RandomAccessFile(Recovery.source, "r");
				RandomAccessFile out = new RandomAccessFile(Recovery.dest, "rw");
				
				in.seek(0x20);
				out.seek(0x20);
				
				int read;
				byte[] data = new byte[1024];
				
				while((read = in.read(data)) != -1){
					out.write(data, 0, read);
				}
				
				in.close();
				out.close();
			}catch(Exception e){}
		}
	}

}
