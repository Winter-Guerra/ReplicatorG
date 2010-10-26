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
		
		//label description of what this window is about
		
		JLabel description = new JLabel("<html>" + "For the Makerbot to home correctly, you must first select three endstops"
				+ "<br>" + "that are currently installed on your 'bot (one for each axis). " + "</html>"); //OMG, do I really have to use HTML line breaks? 
		mainpanel.add(description, "wrap");//moving to next row here...
		mainpanel.add(createEndstopPanel(), "wrap");
		
		//ask if using Zaggo Z-Probe
		mainpanel.add(new JLabel("Next tell me, is Zaggo's Z-Probe hardware installed on this 'bot?"), "wrap");
		mainpanel.add(ZaggoZprobe, "gapleft 30, wrap");
		
		//ask for zAxislift value (default 10);
		zAxisMMToLift.setColumns(16);
		zAxisMMToLift.setText(DefaultZAxisMMToLift);
		mainpanel.add(new JLabel("<html>"+"Finally, tell me how many millimeters should I lift the Z axis"
				+"<br>"+"before attempting to home" + "<br>"+ "(To avoid accidental nozzle crashes into the build platform)."+"</html>"), "wrap"); //Z axis lift
		mainpanel.add(zAxisMMToLift,"gapleft 30, wrap");
		mainpanel.add(makeButtonPanel());
		add(mainpanel); //return this panel...
		//Update Zaxis mm to lift from eeprom settings...
		
	}
}