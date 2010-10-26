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
	private JCheckBox ZaggoZprobe = new JCheckBox("Installed!");
	private static String DefaultZAxisMMToLift = "10";
	private JTextField zAxisMMToLift = new JTextField();
	
	
	private void commit() {
		// if negatives are selected then pass 1 else pass two, if Zaggo z-probe, pass Z 3.
		byte direction[] = {0,0,0}; //array that holds the direction vals for XYZ. Zaggo Z-Probe is Z = 3.
		
if (EndstopPanel.xPlusButton.isSelected()) { //if xPlus button is selected then
// set x packet to to 2
	direction[0] = 2;
	
} else {
	//set to 1 (forcing using at least 1 per axis)
	direction[0] = 1;
}
if (EndstopPanel.yPlusButton.isSelected()) { //if xPlus button is selected then
	// set x packet to to 2
		direction[1] = 2;
		
	} else {
		//set to 1 (forcing using at least 1 per axis)
		direction[1] = 1;
	}
if (EndstopPanel.zPlusButton.isSelected()) { //if xPlus button is selected then
	// set x packet to to 2
		direction[2] = 2;
		
	} else {
		//set to 1 (forcing using at least 1 per axis)
		direction[2] = 1;
	}
		//set z mm
		((OnboardParameters)driver).setZstageMMtoLift(zAxisMMToLift.getText());
	}

	private JPanel makeButtonPanel() { //add the commit and cancel buttons to a panel
		JPanel panel = new JPanel(new MigLayout());
		JButton commitButton = new JButton("Commit homing settings!");
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HomingSetupWindow.this.commit();
				HomingSetupWindow.this.dispose();				
			}
		});
		panel.add(commitButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HomingSetupWindow.this.dispose();
			}
		});
		panel.add(cancelButton);
		return panel;
	}
	
	private JPanel makeConfigPanel() { //add the commit and cancel buttons to a panel
		JPanel panel = new JPanel(new MigLayout());
//label description of what this window is about
		
		JLabel description = new JLabel("<html>" + "For the Makerbot to home correctly, you must first select three endstops"
				+ "<br>" + "that are currently installed on your 'bot (one for each axis). " + "</html>"); //OMG, do I really have to use HTML line breaks? 
		panel.add(description, "wrap");//moving to next row here...
		panel.add(createEndstopPanel(), "wrap");
		
		//ask if using Zaggo Z-Probe
		panel.add(new JLabel("Next tell me, is Zaggo's Z-Probe hardware installed on this 'bot?"), "gaptop 25, wrap");
		panel.add(ZaggoZprobe, "gapleft 30, wrap");
		
		//ask for zAxislift value (default 10);
		zAxisMMToLift.setColumns(16);
		zAxisMMToLift.setText(DefaultZAxisMMToLift);
		panel.add(new JLabel("<html>"+"Finally, tell me how many millimeters should I lift the Z axis"
				+"<br>"+"before attempting to home" + "<br>"
				+"(To avoid accidental nozzle crashes into the build platform)."+"</html>"), "gaptop 25, wrap"); //Z axis lift
		panel.add(zAxisMMToLift,"gapleft 30, wrap");
		
		return panel;
	}
	
	protected JComponent createEndstopPanel() {
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
		super("Homing Setup Window"); //Title in the bar up top.
		machine = m; //get our drivers
		driver = machine.getDriver();
		JPanel mainpanel = new JPanel(new MigLayout()); //start setting up the window

		mainpanel.add(makeConfigPanel(), "wrap"); //panel with Description, XYZ and Settings
		mainpanel.add(makeButtonPanel(), "wrap"); //commit and cancel buttons
		add(mainpanel); //return this panel...
		//Update Zaxis mm to lift from eeprom settings...
		
	}
}