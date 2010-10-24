/**
 * 
 */
package replicatorg.app.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.*;
import javax.swing.text.html.*;
import java.awt.*;


import net.miginfocom.swing.MigLayout;
import replicatorg.app.MachineController;
import replicatorg.app.ui.controlpanel.Endstop3AxisPanel;
import replicatorg.app.ui.controlpanel.Jog3AxisPanel;
import replicatorg.drivers.Driver;
import replicatorg.drivers.OnboardParameters;
import replicatorg.machine.model.Axis;

/**
 * A panel for editing the options stored onboard a machine.
 * @author phooky
 *
 */
public class HomingSetupWindow extends JFrame {
	private static final long serialVersionUID = 7876192459063774731L;
	public static HomingSetupWindow HomingSetupWindow;
	protected MachineController machine;

	protected Driver driver;

	protected JPanel mainPanel;
	protected Endstop3AxisPanel EndstopPanel;
	private JCheckBox ZaggoZprobe = new JCheckBox();
	private JTextField zAxisMMToLift = new JTextField();
	
	
	private void commit() {
		((OnboardParameters)driver).setZstageMMtoLift(zAxisMMToLift.getText());
	}

	private JPanel makeButtonPanel() { //add the commit and cancel buttons to a panel
		JPanel panel = new JPanel(new MigLayout());
		JButton commitButton = new JButton("Commit homing settings");
		panel.add(commitButton);
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HomingSetupWindow.this.commit();
				HomingSetupWindow.this.dispose();				
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HomingSetupWindow.this.dispose();
			}
		});
		panel.add(cancelButton);
		return panel;
	}
	
	protected JComponent createJogPanel() {
		EndstopPanel = new Endstop3AxisPanel(machine);
		return EndstopPanel;
	}
	
	private static HomingSetupWindow instance = null;

	public static synchronized HomingSetupWindow getHomingWindow(MachineController m) { //initialize the homing window
		if (instance == null) {
			instance = new HomingSetupWindow(m);
		} else {
			if (instance.machine != m) {
				instance.dispose();
				instance = new HomingSetupWindow(m);
			}
		}
		return instance;
	}
	
	public HomingSetupWindow(MachineController m) { //set everything up and open homing window...
		machine = m;
		driver = machine.getDriver();
		JPanel mainpanel = new JPanel(new MigLayout());
		
		//Title in the bar up top.
		//label description of what this window is about
		JLabel description = new JLabel("<html>" + "For the Makerbot to home correctly, you must select three endstops"
				+ "<br>" + "(one on each axis) that are currently installed on your bot." + "</html>"); //OMG, do I really have to HTML line breaks? 
		mainpanel.add(description, "wrap");//moving to next row here...
		//need to add here the YXZ panel
		//Zaggo support checkbox and description
		zAxisMMToLift.setColumns(16);
		mainpanel.add(new JLabel("MM to lift Zstage above build platform when homing. (To avoid crashes)"));
		mainpanel.add(zAxisMMToLift,"wrap");
		mainpanel.add(makeButtonPanel());
		add(mainpanel); //return this panel...
		//Update Zaxis mm to lift from eeprom settings...
		
	}
}