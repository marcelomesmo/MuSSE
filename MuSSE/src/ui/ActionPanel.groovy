package ui

import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension

import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFormattedTextField
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.text.NumberFormatter

class ActionPanel extends JPanel {
	
	JPanel top
		JButton cutSelection
		JButton cutEntire
		JLabel thresholdLabel
		JSpinner threshold
	
	JPanel bot
		JList labelList
		// TODO - Lista com Labels
	
	public ActionPanel() throws IOException {
		//setBorder(javax.swing.BorderFactory.createTitledBorder("ActionPanel"))
		setSize(200, 600)
		setPreferredSize(new Dimension(200, 600))
		
		initUI()
	}
	
	private void initUI()
	{
		top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
		
		cutSelection = new JButton("Cut Selection")
        cutSelection.setAlignmentX(Component.CENTER_ALIGNMENT);
		top.add(Box.createRigidArea(new Dimension(0, 10)));
		top.add(cutSelection)
		
		cutEntire = new JButton("Cut Entire Sheet")
        cutEntire.setAlignmentX(Component.CENTER_ALIGNMENT);
		top.add(Box.createRigidArea(new Dimension(0, 10)));
		top.add(cutEntire)
		
		
		JPanel panel1 = new JPanel(new BorderLayout(10,0));
		thresholdLabel = new JLabel("Threshold");
		
		threshold = new JSpinner();
		threshold.setModel(new SpinnerNumberModel(1,1,99,1));
		threshold.setEditor(new JSpinner.NumberEditor(threshold,"##"));
		JFormattedTextField txt = ((JSpinner.NumberEditor) threshold.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
		
        threshold.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel1.add(thresholdLabel, BorderLayout.WEST);
		panel1.add(threshold, BorderLayout.CENTER);
		
		top.add(Box.createRigidArea(new Dimension(0, 10)));
		top.add(panel1)
		
		this.add(top)
	}

}
