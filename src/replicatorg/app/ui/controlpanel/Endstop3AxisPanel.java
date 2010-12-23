package replicatorg.app.ui.controlpanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Point3d;

import net.miginfocom.swing.MigLayout;
import replicatorg.app.Base;
import replicatorg.app.MachineController;
import replicatorg.drivers.Driver;

public class Endstop3AxisPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	protected MachineController machine;
	protected Driver driver;
	
	public JToggleButton xPlusButton = createEndstopButton("X+", "Home X axis in positive direction");
	public JToggleButton xMinusButton = createEndstopButton("X-", "Home X axis in negative direction");
	public JToggleButton yPlusButton = createEndstopButton("Y+", "Home Y axis in positive direction");
	public JToggleButton yMinusButton = createEndstopButton("Y-", "Home Y axis in negative direction");
	public JToggleButton zPlusButton = createEndstopButton("Z+", "Home Z axis in positive direction");
	public JToggleButton zMinusButton = createEndstopButton("Z-", "Home Z axis in negative direction");

	/**
	 * Create a jog-style button with the given name and tooltip.  By default, the
	 * action name is the same as the text of the button.  The button will emit an
	 * action event to the jog panel when it is clicked.
	 * @param text the text to display on the button.
	 * @param tooltip the text to display when the mouse hovers over the button.
	 * @return the generated button.
	 */
	protected JToggleButton createEndstopButton(String text, String tooltip) {
		final int buttonSize = 60;
		JToggleButton b = new JToggleButton(text);
		b.setToolTipText(tooltip);
		b.setMaximumSize(new Dimension(buttonSize, buttonSize));
		b.setPreferredSize(new Dimension(buttonSize, buttonSize));
		b.setMinimumSize(new Dimension(buttonSize, buttonSize));
		b.addActionListener(this);
		return b;
	}

	/**
	 * Create a jog-style button with the given name and tooltip.  The action
	 * name is specified by the caller.  The button will emit an
	 * action event to the jog panel when it is clicked.
	 * @param text the text to display on the button.
	 * @param tooltip the text to display when the mouse hovers over the button.
	 * @param action the string representing the action.
	 * @return the generated button.
	 */
	protected JToggleButton createEndstopButton(String text, String tooltip, String action) {
		JToggleButton button = createEndstopButton(text,tooltip);
		button.setActionCommand(action);
		return button;
	}

	/**
	 * Create a text field for dynamic data display 
	 */
	protected JTextField createDisplayField() {
		JTextField tf = new JTextField();
		tf.setEnabled(false);
		return tf;
	}

	public Endstop3AxisPanel(MachineController machine) {
		this.machine = machine;
		this.driver = machine.getDriver();
		setLayout(new MigLayout());
		
		//set our defaults...
		xMinusButton.setSelected(false);
		xPlusButton.setSelected(true);
		yMinusButton.setSelected(false);
		yPlusButton.setSelected(true);
		zMinusButton.setSelected(false);
		zPlusButton.setSelected(true);
		
	
		JPanel xyzPanel = new JPanel(new MigLayout("","[]0[]","[]0[]"));

		xyzPanel.add(xMinusButton, "cell 0 1, gap 0 0 0 0");
		xyzPanel.add(yPlusButton, "split 2, flowy, gapbottom 54");
		xyzPanel.add(yMinusButton, "gap 0 0 0 0");
		xyzPanel.add(xPlusButton,"cell 2 1, gapafter 10");
		xyzPanel.add(zPlusButton, "split 2,flowy,gap 0 0 0 0");
		xyzPanel.add(zMinusButton);

		add(xyzPanel);
		// add jog panel border and stuff.
		setBorder(BorderFactory.createTitledBorder("Homing Endstops"));
	
	}
	

	DecimalFormat positionFormatter = new DecimalFormat("###.#");

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();

		
		if (s.equals("X+")) {
		//set x varibles
			
			xMinusButton.setSelected(false);
			xPlusButton.setSelected(true);
		} else if (s.equals("X-")) {
			
			xMinusButton.setSelected(true);
			xPlusButton.setSelected(false);
		} else if (s.equals("Y+")) {
			yMinusButton.setSelected(false);
			yPlusButton.setSelected(true);
		
		} else if (s.equals("Y-")) {
			yMinusButton.setSelected(true);
			yPlusButton.setSelected(false);
		
		} else if (s.equals("Z+")) {
			zMinusButton.setSelected(false);
			zPlusButton.setSelected(true);
		
		} else if (s.equals("Z-")) {
			zMinusButton.setSelected(true);
			zPlusButton.setSelected(false);
		
		} else {
		
			Base.logger.warning("Unknown Action Event: " + s);
		
	}
	}
}
