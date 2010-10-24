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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	private JPanel makeButtonPanel() {
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
	
	private static HomingSetupWindow instance = null;

	public static synchronized HomingSetupWindow getHomingWindow(MachineController m) {
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
	
	public HomingSetupWindow(MachineController m) {
		//super("Update onboard Homing machine options");
		machine = m;
		driver = machine.getDriver();
		JPanel panel = new JPanel(new MigLayout());
		//machineNameField.setColumns(MAX_NAME_LENGTH);
		zAxisMMToLift.setColumns(16);
		panel.add(new JLabel("MM to lift Zstage above build platform when homing. (To avoid crashes)"));
		panel.add(zAxisMMToLift,"wrap");
		panel.add(makeButtonPanel());
		add(panel);
		
	}
}