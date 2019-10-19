package de.erdbeerbaerlp.worldManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class ProgressWindow extends JDialog {
	private final JProgressBar progressBar = new JProgressBar();
	private final JLabel lblmaxModsProcessed = new JLabel();
	private final JLabel lblconnectingToCurseplaese = new JLabel("<html><font size=3>Connecting to Curse<br>\r\nPlease wait while the mods of this world<br>\r\nare being identified...");
	private final JButton btnCancel = new JButton("Cancel");
	private boolean cancelled = false;
	private int max = 100;
public ProgressWindow(int maximum) {
	setModal(true);
	this.max = maximum;
	this.progressBar.setMaximum(this.max);
	setTitle("Connecting to Curse");
	// TODO Auto-generated constructor stub
	setSize(379,227);
	setLocationRelativeTo(null);
	getContentPane().setLayout(null);
	progressBar.setStringPainted(true);
	progressBar.setBounds(29, 123, 308, 14);
	
	getContentPane().add(progressBar);
	lblmaxModsProcessed.setHorizontalAlignment(SwingConstants.CENTER);
	lblmaxModsProcessed.setBounds(29, 108, 308, 14);
	lblmaxModsProcessed.setText("0/"+max+" mods processed");
	getContentPane().add(lblmaxModsProcessed);
	lblconnectingToCurseplaese.setHorizontalAlignment(SwingConstants.CENTER);
	lblconnectingToCurseplaese.setBounds(29, 11, 301, 86);
	
	getContentPane().add(lblconnectingToCurseplaese);
	btnCancel.addActionListener(new ActionListener() {
		

		public void actionPerformed(ActionEvent arg0) {
			cancelled = true;
			btnCancel.setEnabled(false);
		}
	});
	btnCancel.setBounds(134, 154, 89, 23);
	
	getContentPane().add(btnCancel);
}
public void updateProgress(int prog) {
	this.progressBar.setValue(prog);
	this.lblmaxModsProcessed.setText(prog+"/"+this.max+" mods processed");
	
}
public void setMaximum(int maximum) {
	this.max = maximum;
	this.progressBar.setMaximum(maximum);
}
public boolean isCanceled() {
	// TODO Auto-generated method stub
	return cancelled;
}
}
