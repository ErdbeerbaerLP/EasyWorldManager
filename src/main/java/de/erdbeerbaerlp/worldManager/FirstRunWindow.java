package de.erdbeerbaerlp.worldManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class FirstRunWindow extends JDialog {
	public static String backupDir;
	public FirstRunWindow() {
		setTitle("EasyWorldManager - First run setup");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		getContentPane().setLayout(null);
		
		JLabel lblSelectTheFolder = new JLabel("<html>Select the Folder where backups will be stored.<br>\r\nIt should be empty</html>");
		lblSelectTheFolder.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectTheFolder.setBounds(12, 12, 274, 52);
		getContentPane().add(lblSelectTheFolder);
		
		JButton btnSelectAndStart = new JButton("Select and start");
		btnSelectAndStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(chooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Choose backup directory");
				chooser.showSaveDialog(FirstRunWindow.this);
				backupDir = chooser.getSelectedFile().getPath();
				setVisible(false);
			}
		});
		btnSelectAndStart.setBounds(60, 71, 170, 23);
		getContentPane().add(btnSelectAndStart);
		setSize(314,211);
		setLocationRelativeTo(null);
	}
}
