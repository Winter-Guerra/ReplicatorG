
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
import replicatorg.machine.model.ToolModel;
import replicatorg.drivers.PenPlotter;
import replicatorg.machine.model.Axis;
import replicatorg.drivers.RetryException;
import replicatorg.app.Base;

/**
 * A popup panel for setting the Z-Probe servo position settings
 * @author xtremd
 *
 */
public class HomingSetupWindowZProbePrompt extends JFrame {
	private static final long serialVersionUID = 7876192459063774731L;
	public static HomingSetupWindowZProbePrompt HomingSetupWindowZProbePrompt;
	protected MachineController machine;

	protected Driver driver;

	protected JPanel mainPanel;
	private JTextField servoLiftPosition = new JTextField();
	private JTextField servoLowerPosition = new JTextField();
	private static String DefaultPosition = "0";
	
	
	
	
	private JPanel makeButtonPanel() { //add the commit and cancel buttons to a panel
		JPanel panel = new JPanel(new MigLayout());
		JButton commitButton = new JButton("Commit homing settings!");
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HomingSetupWindowZProbePrompt.this.commit();
				HomingSetupWindowZProbePrompt.this.dispose();				
			}
		});
		panel.add(commitButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HomingSetupWindowZProbePrompt.this.dispose();
			}
		});
		panel.add(cancelButton);
		return panel;
	}
	
	private JPanel makeConfigPanel() { //add the description, textboxes and testbuttons
		JPanel panel = new JPanel(new MigLayout());
		//label description of what this window is about
		
		JLabel description = new JLabel("<html>" 
				+ "To use Zaggo's Z-Probe hardware, you must define what servo position<br>"
				+ "values (0-180) you want to use for the Raised and Lowered servo positions.<br>" 
				+ "You can use the buttons to the right of each textbox to send test commands<br>"
				+ "to the Makerbot.<br> <br>"
				+ "Just as a reminder, the servo should be connected to pin D10 on the Extruder<br>" 
				+ "board, with the black wire connected to the pin labeled \"-\" and the red wire<br>" 
				+ "to the pin labeled \"+\"." 
				+ "</html>"); //description of what to do.
		//OMG, do I really have to use HTML line breaks? No automatic wrap? Wow. Java swing fail...
		
		panel.add(description, "wrap");//moving to next row here...
		
		
		//give me labels textboxes and test buttons! MOAR BUTTONS! MOAR!!
		panel.add(new JLabel("Servo raised position"), "gaptop 25, wrap"); //label
		servoLiftPosition.setColumns(16); //set width
		servoLiftPosition.setText(DefaultPosition);
		panel.add(servoLiftPosition, "split 2,flowx"); //textbox
		JButton TestLift = new JButton("Send Test Command"); //test button
		TestLift.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//Send servo test command.
				try {
					((PenPlotter)driver).setServo2Pos(Double.parseDouble(servoLiftPosition.getText()));
					} catch (RetryException e1) {
					Base.logger.severe("Can't send command; machine busy");
					}
			}
		});
		panel.add(TestLift, "Wrap");
		
		
		panel.add(new JLabel("Servo lowered position"), "gaptop 25, wrap"); //label
		servoLowerPosition.setColumns(16); //set width
		servoLowerPosition.setText(DefaultPosition);
		panel.add(servoLowerPosition, "split 2,flowx"); //textbox
		JButton TestLower = new JButton("Send Test Command"); //test button
		TestLower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//Send servo test command.	
				try {
					((PenPlotter)driver).setServo2Pos(Double.parseDouble(servoLowerPosition.getText()));
					} catch (RetryException e1) {
					Base.logger.severe("Can't send command; machine busy");
					}
				//Double.parseDouble(aString)
			}
		});
		panel.add(TestLower, "Wrap");
		
	
		return panel;
	}
	
	
	private static HomingSetupWindowZProbePrompt instance = null;

	public static synchronized HomingSetupWindowZProbePrompt getHomingWindowZProbePrompt(MachineController m) { //initialize the homing window
		if (instance == null) {
			instance = new HomingSetupWindowZProbePrompt(m);
		} else {
			if (instance.machine != m) {
				instance.dispose();
				instance = new HomingSetupWindowZProbePrompt(m);
			}
		}
		return instance;
	}

	
	
	
	public HomingSetupWindowZProbePrompt(MachineController m) { //set everything up and open homing window...
		super("ZProbe Servo Setup Window"); //Title in the bar up top.
		machine = m; //get our drivers
		driver = machine.getDriver();
		JPanel mainpanel = new JPanel(new MigLayout()); //start setting up the window

		mainpanel.add(makeConfigPanel(), "wrap"); //panel with Description, XYZ and Settings
		mainpanel.add(makeButtonPanel(), "wrap"); //commit and cancel buttons
		add(mainpanel); //return this panel...
		
	}
	
	private void commit() {
		//Commit Servo position data to EEPROM and then hand control back to the Homing setup window.
		//(Or call the reminder prompt yourself it its too hard to wait)
		//Double.parseDouble(servoLowerPosition.getText()
		//submit stuff to eeprom and set extruder index to 0 (default)
		((OnboardParameters)driver).setZProbeSettings(servoLiftPosition.getText(), servoLowerPosition.getText(), (byte) 0);
		
		byte directions[] = new byte[3];
		directions = HomingSetupWindow.direction;
		startHomingDialogFromZProbePrompt(directions);
		
		
	}
	void startHomingDialogFromZProbePrompt(byte direction[]) {
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
			
			//((OnboardParameters)driver).setZstageMMtoLift(zAxisMMToLift.getText()); //set zlift
			
			try {
				driver.firstHoming(direction,0,0); //fire off the command to the makerbot to start the homing
				} catch (RetryException e1) {
				Base.logger.severe("Can't setup homing; machine busy");
				}
		}
	}


	
	
}
