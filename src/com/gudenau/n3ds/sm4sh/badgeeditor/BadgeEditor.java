package com.gudenau.n3ds.sm4sh.badgeeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;

import com.gudenau.n3ds.sm4sh.badgeeditor.Equipment.Bonus;

public class BadgeEditor implements ActionListener, MouseListener {
	private JFrame frame;
	private JFrame savingFrame;
	
	private JComboBox<Equipment> type;
	private JSpinner level;
	private JComboBox<Bonus> bounus;
	
	private JList<RealEquipment> list;
	private JLabel label;
	
	private JSpinner attack;
	private JSpinner defence;
	private JSpinner speed;
	private JSpinner range;
	
	private JMenuBar menu;
	
	private JMenu file;
	private JMenu recover;
	
	private JMenuItem open;
	private JMenuItem close;
	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem exit;
	
	private JMenuItem recovery;
	
	private JMenuItem edit;
	private JMenuItem set;
	
	private File currentFile = null;
	
	private void start() throws Throwable {
		JFrame loadingFrame;
		
		{	// Init
			loadingFrame = new JFrame("Equipment Editor");
			loadingFrame.setUndecorated(true);
			loadingFrame.setResizable(false);
			
			JLabel label = new JLabel("Loading");
			Font font = label.getFont();
			label.setFont(font.deriveFont(Font.PLAIN, font.getSize()*10));
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(label, BorderLayout.CENTER);
			loadingFrame.add(panel);
			
			loadingFrame.pack();
			centerFrame(loadingFrame);
			loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			loadingFrame.setVisible(true);
			
			Equipment.equipment.hashCode();
			Equipment.Bonus.get(0);
		}
		
		{	// Application
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			
			RealEquipment[] temp = new RealEquipment[3000];
			for(int i = 0; i < temp.length; i++){
				temp[i] = new RealEquipment();
			}
			
			list = new JList<RealEquipment>(temp);
			list.setSelectionMode(JList.VERTICAL);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.addMouseListener(this);
			
			constraints.gridheight = 1;
			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.gridy = 0;
			JScrollPane jsp = new JScrollPane(list);
			panel.add(jsp, constraints);
			
			{	// Editor stuff
				JPanel edit = new JPanel(new BorderLayout());
				JPanel tmp;
				{	// Equipment type/level
					tmp = new JPanel(new BorderLayout());

					label = new JLabel();
					label.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
					tmp.add(label, BorderLayout.WEST);
					
					{	// Equipment Type/level
						JPanel pan = new JPanel(new GridBagLayout());
						GridBagConstraints con = new GridBagConstraints();
						
						con.gridheight = 1;
						con.gridwidth = 1;
						
						con.gridx = 0;
						con.gridy = 0;
						pan.add(new JLabel("Type"), con);
						con.gridx = 1;
						pan.add(new JLabel("Level"), con);
						
						type = new JComboBox<Equipment>(new SortedComboBoxModel<Equipment>());
						type.addActionListener(this);
						
						for(Equipment e : Equipment.equipment){
							if(e != null){
								type.addItem(e);
							}
						}
						
						con.gridx = 0;
						con.gridy = 1;
						pan.add(type, con);
						
						level = new JSpinner(new SpinnerNumberModel(0, 0, 2, 1));
						con.gridx = 1;
						pan.add(level, con);
						
						tmp.add(pan, BorderLayout.CENTER);
					}
					
					label.setIcon(((Equipment)type.getSelectedItem()).icon);
					
					edit.add(tmp, BorderLayout.NORTH);
				}
				
				{	// Main equipment values
					tmp = new JPanel(new GridBagLayout());
					GridBagConstraints con = new GridBagConstraints();
					
					con.gridheight = 1;
					con.gridwidth = 1;
					
					con.gridy = 0;
					
					con.gridx = 0;
					tmp.add(new JLabel("Attack"), con);
					
					con.gridx = 1;
					tmp.add(new JLabel("Defence"), con);
					
					con.gridx = 2;
					tmp.add(new JLabel("Speed"), con);
					
					con.gridy = 1;
					
					con.gridx = 0;
					attack = new JSpinner();
					attack.setModel(new SpinnerNumberModel(0, Short.MIN_VALUE, Short.MAX_VALUE, 1));
					tmp.add(attack, con);
						
					con.gridx = 1;
					defence = new JSpinner();
					defence.setModel(new SpinnerNumberModel(0, Short.MIN_VALUE, Short.MAX_VALUE, 1));
					tmp.add(defence, con);
						
					con.gridx = 2;
					speed = new JSpinner();
					speed.setModel(new SpinnerNumberModel(0, Short.MIN_VALUE, Short.MAX_VALUE, 1));
					tmp.add(speed, con);
					
					edit.add(tmp, BorderLayout.CENTER);
				}
				
				{	// Equipment bounus/range
					bounus = new JComboBox<Bonus>(new SortedComboBoxModel<Bonus>());
					bounus.addItem(Bonus.empty);
					
					for(Bonus b : Bonus.Bonuses){
						if(b != null){
							bounus.addItem(b);
						}
					}
					
					tmp = new JPanel(new GridBagLayout());
					GridBagConstraints con = new GridBagConstraints();
					
					con.gridheight = 1;
					con.gridwidth = 1;
					
					con.gridy = 0;					
					con.gridx = 0;
					tmp.add(new JLabel("Bonus"), con);
					
					con.gridx = 1;
					tmp.add(new JLabel("Range"), con);
					
					con.gridy = 1;
					con.gridx = 0;
					tmp.add(bounus, con);
					
					range = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
					con.gridx = 1;
					tmp.add(range, con);
					
					edit.add(tmp, BorderLayout.SOUTH);
				}
				
				constraints.gridx = 1;
				panel.add(edit, constraints);
				
				{	// Toolbar
					menu = new JMenuBar();
					
					{	// File
						file = new JMenu("File");
						
						open = new JMenuItem("Open");
						open.addActionListener(this);
						
						save = new JMenuItem("Save");
						save.addActionListener(this);
						
						saveAs = new JMenuItem("Save-As");
						saveAs.addActionListener(this);
						
						close = new JMenuItem("Close");
						close.addActionListener(this);
						
						exit = new JMenuItem("Exit");
						exit.addActionListener(this);
						
						file.add(open);
						file.add(save);
						//file.add(saveAs);
						file.addSeparator();
						file.add(close);
						file.add(exit);
						
						menu.add(file);
					}
					
					{	// Recovery
						recover = new JMenu("Recovery");
						
						recovery = new JMenuItem("Recovery");
						recovery.addActionListener(this);
						
						recover.add(recovery);
						
						menu.add(recover);
					}
				}
			}
			
			{	// Context menu
				set = new JMenuItem("Set");
				set.addActionListener(this);
				
				edit = new JMenuItem("Edit");
				edit.addActionListener(this);
			}
			
			{	// Saving frame
				savingFrame = new JFrame("Saving...");
				savingFrame.setUndecorated(true);
				savingFrame.add(new JLabel("Saving..."));
				savingFrame.pack();
			}
			
			frame = new JFrame("Equipment Editor");
			frame.setJMenuBar(menu);
			frame.add(panel);
			
			frame.pack();
			jsp.setPreferredSize(new Dimension(200, jsp.getHeight()));
			frame.pack();
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			centerFrame(frame);
			frame.setResizable(false);
			loadingFrame.dispose();
			frame.setVisible(true);
		}
	}
	
	public static void centerFrame(JFrame frame) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		
		int x = dim.width - frame.getWidth();
		int y = dim.height - frame.getHeight();
		
		frame.setLocation(x / 2, y / 2);
	}

	public static void main(String[] args) throws Throwable{
		new BadgeEditor().start();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if(source == type){
			label.setIcon(((Equipment)type.getSelectedItem()).icon);
		}else if(source == edit){
			RealEquipment equipment = list.getSelectedValue();
			
			type.setSelectedItem(Equipment.equipment[equipment.id]);
			attack.setValue(equipment.attack);
			defence.setValue(equipment.defence);
			speed.setValue(equipment.speed);
			level.setValue(equipment.level);
			range.setValue(equipment.range);
			bounus.setSelectedItem(Bonus.get(equipment.bonus));			
		}else if(source == set){
			RealEquipment equipment = list.getSelectedValue();
			
			equipment.id = ((Equipment)type.getSelectedItem()).id;
			equipment.attack = ((Number)attack.getValue()).shortValue();
			equipment.defence = ((Number)defence.getValue()).shortValue();
			equipment.speed = ((Number)speed.getValue()).shortValue();
			equipment.level = ((Number)level.getValue()).byteValue();
			equipment.range = ((Number)range.getValue()).byteValue();
			equipment.bonus = ((Bonus)bounus.getSelectedItem()).id;
			equipment.validate();
			
			list.repaint();
		}else if(source == close){
			clear();
		}else if(source == open){
			JFileChooser jfc = new JFileChooser();
			jfc.setFileFilter(new FileFilter(){

				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().endsWith(".bin");
				}

				@Override
				public String getDescription() {
					return "Sm4sh 3DS save";
				}
			});
			
			switch(jfc.showOpenDialog(frame)){
			case JFileChooser.APPROVE_OPTION:
				currentFile = jfc.getSelectedFile();
				try{
					openFile();
				}catch(Throwable t){
					t.printStackTrace();
					JOptionPane.showMessageDialog(frame, "There was an error reading the file");
				}
			}	
		}else if(source == save){
			if(currentFile == null){
				JOptionPane.showMessageDialog(frame, "There is no open save!");
			}else{
				savingFrame.setVisible(true);
				frame.setEnabled(false);
				
				{	// Make a backup, just in case
					File backup = new File(currentFile.getAbsolutePath() + "." + System.currentTimeMillis() + ".bak");
					try{
						FileInputStream in = new FileInputStream(currentFile);
						FileOutputStream out = new FileOutputStream(backup);
						
						int i;
						while((i = in.read()) != -1){
							out.write(i);
						}
						
						in.close();
						out.close();
					}catch(Throwable t){}
				}
				
				RealEquipment[] equip = new RealEquipment[3000];
				for(int i = 0; i < equip.length; i++){
					equip[i] = list.getModel().getElementAt(i);
				}
				
				try {
					RealEquipment.save(equip, currentFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				savingFrame.setVisible(false);
				frame.setEnabled(true);
			}
		}else if(source == exit){
			System.exit(0);
		}else if(source == recovery){
			Recovery.recover(frame);
		}
	}

	private void openFile() throws IOException{
		RealEquipment[] equip = RealEquipment.load(currentFile.getAbsolutePath());
		
		for(int i = 0; i < 3000; i++){
			list.getModel().getElementAt(i).copy(equip[i]);
		}
		
		list.repaint();
	}
	
	private void clear(){
		for(int i = 0; i < 3000; i++){
			list.getModel().getElementAt(i).clear();
		}
		list.repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON3){
			list.setSelectedIndex(list.locationToIndex(event.getPoint()));
			
			JPopupMenu m = new JPopupMenu();
			m.add(edit);
			m.add(set);
			m.show(list, event.getX(), event.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
