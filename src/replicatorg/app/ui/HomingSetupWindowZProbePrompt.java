
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
		
		JLabel description = new JLabel("<html>" + "For the Makerbot to home correctly, you must first select three <br> endstops"
				 + " that are currently installed on your 'bot (one for each axis)." + "</html>"); 
		//OMG, do I really have to use HTML line breaks? No automatic wrap? Wow. Java swing fail...
		
		panel.add(description, "wrap");//moving to next row here...
		
		
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
			    
		//ask for zAxislift value (default 10);
		zAxisMMToLift.setColumns(16);
		zAxisMMToLift.setText(DefaultZAxisMMToLift);
		panel.add(new JLabel("<html>"+"How many millimeters should the Z axis lift" + 
				"<br>"+"before attempting to home" + "<br>"
				+"(To avoid accidental nozzle crashes into the build platform)."+"</html>"), "gaptop 20, wrap"); //Z axis lift
		panel.add(zAxisMMToLift,"gapleft 30, wrap");
		
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
}
