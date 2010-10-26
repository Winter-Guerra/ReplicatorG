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

	/**
	 * Create a jog-style button with the given name and tooltip.  By default, the
	 * action name is the same as the text of the button.  The button will emit an
	 * action event to the jog panel when it is clicked.
	 * @param text the text to display on the button.
	 * @param tooltip the text to display when the mouse hovers over the button.
	 * @return the generated button.
	 */
	protected JButton createEndstopButton(String text, String tooltip) {
		final int buttonSize = 60;
		JButton b = new JButton(text);
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
	protected JButton createEndstopButton(String text, String tooltip, String action) {
		JButton button = createEndstopButton(text,tooltip);
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
		
		// compile our regexes
		//jogRate = 10.0;
		//jogPattern = Pattern.compile("([.0-9]+)");

		JButton xPlusButton = createEndstopButton("X+", "Home X axis in positive direction");
		JButton xMinusButton = createEndstopButton("X-", "Home X axis in negative direction");
		//JButton xCenterButton = createEndstopButton("<html><center>Center<br/>X", "Jog X axis to the origin","Center X");
		JButton yPlusButton = createEndstopButton("Y+", "Home Y axis in positive direction");
		JButton yMinusButton = createEndstopButton("Y-", "Home Y axis in negative direction");
		//JButton yCenterButton = createEndstopButton("<html><center>Center<br/>Y", "Jog Y axis to the origin","Center Y");
		JButton zPlusButton = createEndstopButton("Z+", "Home Z axis in positive direction");
		JButton zMinusButton = createEndstopButton("Z-", "Home Z axis in negative direction");
		//JButton zCenterButton = createEndstopButton("<html><center>Center<br/>Z", "Jog Z axis to the origin","Center Z");
		//JButton zeroButton = createEndstopButton("<html><center>Set<br/>zero","Mark Current Position as Zero (0,0,0)","Zero");

		JPanel xyzPanel = new JPanel(new MigLayout("","[]0[]","[]0[]"));
        //xyzPanel.add(zCenterButton, "split 3,flowy,gap 0 0 0 0");
		xyzPanel.add(xMinusButton, "cell 0 1, gap 0 0 0 0");
        //xyzPanel.add(yCenterButton);
		xyzPanel.add(yPlusButton, "split 2, flowy, gapbottom 54");
		//xyzPanel.add(zeroButton,"gap 0 0 0 0");
		xyzPanel.add(yMinusButton, "gap 0 0 0 0");
		xyzPanel.add(xPlusButton,"cell 2 1, gapafter 10");
       //xyzPanel.add(xCenterButton);
		xyzPanel.add(zPlusButton, "split 2,flowy,gap 0 0 0 0");
		xyzPanel.add(zMinusButton);

		// create our position panel
		//JPanel positionPanel = new JPanel(new MigLayout("flowy"));
		// our label
		//positionPanel.add(new JLabel("Jog Size"));
		
		// add it all to our jog panel
		add(xyzPanel);
		//add(positionPanel,"growx,wrap");
		//add(feedratePanel,"growx,spanx");

		// add jog panel border and stuff.
		setBorder(BorderFactory.createTitledBorder("Homing Endstops"));
	
	}
	

	DecimalFormat positionFormatter = new DecimalFormat("###.#");

	/*synchronized public void updateStatus() {
		Point3d current = driver.getCurrentPosition();

		xPosField.setText(positionFormatter.format(current.x));
		yPosField.setText(positionFormatter.format(current.y));
		zPosField.setText(positionFormatter.format(current.z));		
	}*/

	/*public void handleChangedTextField(JTextField source)
	{
		String name = source.getName();

		if (source.getText().length() > 0) {
			if (name.equals("xy-feedrate-value")) {
				xyFeedrateSlider.setValue(Integer.parseInt(source.getText()));
			} else if (name.equals("z-feedrate-value")) {
				zFeedrateSlider.setValue(Integer.parseInt(source.getText()));
			} else {
				Base.logger.warning("Unhandled text field: "+name);
			}
		}
	}*/

	public void actionPerformed(ActionEvent e) {
		//Point3d current = driver.getCurrentPosition();
		//double xyFeedrate = xyFeedrateSlider.getValue();
		//double zFeedrate = zFeedrateSlider.getValue();
		String s = e.getActionCommand();

		/*if(s.equals("handleTextfield"))
		{
			JTextField source = (JTextField) e.getSource();
			handleChangedTextField(source);
			source.selectAll();
		}*/

		if (s.equals("X+")) {
			//current.x += jogRate;

			//driver.setFeedrate(xyFeedrate);
			//driver.queuePoint(current);
		} else if (s.equals("X-")) {
			/*current.x -= jogRate;

			driver.setFeedrate(xyFeedrate);
			driver.queuePoint(current);*/
		} else if (s.equals("Y+")) {
			/*current.y += jogRate;

			driver.setFeedrate(xyFeedrate);
			driver.queuePoint(current);*/
		} else if (s.equals("Y-")) {
			/*current.y -= jogRate;

			driver.setFeedrate(xyFeedrate);
			driver.queuePoint(current);*/
		} else if (s.equals("Z+")) {
			/*current.z += jogRate;

			driver.setFeedrate(zFeedrate);
			driver.queuePoint(current);*/
		} else if (s.equals("Z-")) {
			/*current.z -= jogRate;

			driver.setFeedrate(zFeedrate);
			driver.queuePoint(current);*/
		} else if (s.equals("Center X")) {
			/*current.x = 0;

			driver.setFeedrate(xyFeedrate);
			driver.queuePoint(current);*/
		} else if (s.equals("Center Y")) {
			/*current.y = 0;

			driver.setFeedrate(xyFeedrate);
			driver.queuePoint(current);*/
		} else if (s.equals("Center Z")) {
			/*current.z = 0;

			driver.setFeedrate(zFeedrate);
			driver.queuePoint(current);*/
		} else if (s.equals("Zero")) {
			// "Zero" tells the machine to calibrate its
			// current position as zero, not to move to its
			// currently-set zero position.
			//driver.setCurrentPosition(new Point3d());
		}
		// get our new jog rate
		else if (s.equals("jog size")) {
			/*JComboBox cb = (JComboBox) e.getSource();
			String jogText = (String) cb.getSelectedItem();

			// look for a decimal number
			Matcher jogMatcher = jogPattern.matcher(jogText);
			if (jogMatcher.find())
				jogRate = Double.parseDouble(jogMatcher.group(1));

			// TODO: save this back to our preferences file.

			// System.out.println("jog rate: " + jogRate);*/
		} else {
			Base.logger.warning("Unknown Action Event: " + s);
		
	}
	}
	/*public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		int feedrate = source.getValue();

		if (source.getName().equals("xy-feedrate-slider")) {
			xyFeedrateValue.setText(Integer.toString(feedrate));
		} else if (source.getName().equals("z-feedrate-slider")) {
			zFeedrateValue.setText(Integer.toString(feedrate));
		}
	}*/

	
	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	/*public void focusGained(FocusEvent e) {
	}*/

	/*public void focusLost(FocusEvent e) {
		JTextField source = (JTextField) e.getSource();
		//handleChangedTextField(source);
	}*/
}
