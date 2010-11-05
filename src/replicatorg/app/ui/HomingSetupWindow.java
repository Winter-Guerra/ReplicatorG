
package replicatorg.app.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import replicatorg.drivers.RetryException;
import replicatorg.app.Base;

/**
 * A panel for initiating the first time homing sequence and all of its settings.
 * @author xtremd
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
	
} else if (EndstopPanel.xMinusButton.isSelected()) {
	//set to 1 
	direction[0] = 1;
}
if (EndstopPanel.yPlusButton.isSelected()) { //if xPlus button is selected then
	// set y packet to to 2
		direction[1] = 2;
		
	} else if (EndstopPanel.yMinusButton.isSelected()) {
		//set to 1 
		direction[1] = 1;
	}

//if (ZaggoZprobe.isSelected()) { //if using Zaggo's hardware. Return Z axis value of 3
	//direction[2] = 3;
	
//} else 
 if (EndstopPanel.zPlusButton.isSelected()) { //if xPlus button is selected then
	// set z packet to to 2
		direction[2] = 2;
		
	} else if (EndstopPanel.zMinusButton.isSelected()){
		//set to 1 
		direction[2] = 1;
	}
		//set z mm to lift regardless of anything.
		/*((OnboardParameters)driver).setZstageMMtoLift(zAxisMMToLift.getText());
		
		startHomingDialog(direction);
		
		try {
		driver.firstHoming(direction,0,0); //fire off the command to the makerbot to start the homing
		} catch (RetryException e1) {
		Base.logger.severe("Can't setup homing; machine busy");
		} */
		
		startHomingDialog(direction);
		
	}
	
	private void startHomingDialog(byte direction[]) {
		int confirm = JOptionPane.showConfirmDialog(this, 
				"<html>Please center the Build Platform and the Z stage so that the extruder nozzle<br/>"+
				"is in the center of the Build Platform and a hair from touching it. Also make sure <br/> that"+
				" the extruder is currently cool and not extruding. <br/><br/>" +
				"Press Yes when ready.</html>",
				"Get ready to Home!", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (confirm == JOptionPane.YES_OPTION) {
			//driver.reset();
			//set all varibles in EEPROM that the Bot needs, then start homing.
			
			((OnboardParameters)driver).setZstageMMtoLift(zAxisMMToLift.getText()); //set zlift
			
			try {
				driver.firstHoming(direction,0,0); //fire off the command to the makerbot to start the homing
				} catch (RetryException e1) {
				Base.logger.severe("Can't setup homing; machine busy");
				}
		}
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
		
		JLabel description = new JLabel("<html>" + "For the Makerbot to home correctly, you must first select three <br> endstops"
				 + " that are currently installed on your 'bot (one for each axis)." + "</html>"); 
		//OMG, do I really have to use HTML line breaks? No automatic wrap? Wow. Java swing fail...
		
		panel.add(description, "wrap");//moving to next row here...
		panel.add(createEndstopPanel(), "wrap");
		
		/*
		//ask if using Zaggo Z-Probe
		panel.add(new JLabel("Is Zaggo's Z-Probe hardware installed on this 'bot?"), "gaptop 25, wrap");
		ZaggoZprobe.addItemListener( //listen to ZaggoZprobe checkbox
			    new ItemListener() {
			        public void itemStateChanged(ItemEvent e) {
			            // Turn off Z axis buttons whenever box is checked
			        	if (ZaggoZprobe.isSelected()) {
			        		EndstopPanel.zMinusButton.setEnabled(false);
			        		EndstopPanel.zPlusButton.setEnabled(false);
			        	} else { //turn back on when unchecked
			        		EndstopPanel.zMinusButton.setEnabled(true);
			        		EndstopPanel.zPlusButton.setEnabled(true);
			        	}
			        }
			    } );
			    panel.add(ZaggoZprobe, "gapleft 30, wrap");
			    */
		//ask for zAxislift value (default 10);
		zAxisMMToLift.setColumns(16);
		zAxisMMToLift.setText(DefaultZAxisMMToLift);
		panel.add(new JLabel("<html>"+"How many millimeters should the Z axis lift" + 
				"<br>"+"before attempting to home" + "<br>"
				+"(To avoid accidental nozzle crashes into the build platform)."+"</html>"), "gaptop 20, wrap"); //Z axis lift
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
