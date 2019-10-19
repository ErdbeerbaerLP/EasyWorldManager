package de.erdbeerbaerlp.worldManager;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class RenameDialog extends JDialog {
	private JTextField textField;
	private JLabel lblenterTheNew = new JLabel("<html><h4>Enter the new world name below.<br>Color codes are allowed</h4></html>");
	private boolean canceled = false;
	public RenameDialog(World w) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				canceled = true;
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(RenameDialog.class.getResource("/de/erdbeerbaerlp/worldManager/icons/rename.png")));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Rename World");
		setSize(396,227);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		final JLabel lblNewworld = new JLabel("New World");
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(textField.getText().isEmpty()) lblNewworld.setText("New World");
				else lblNewworld.setText(ColorFormatter.format(textField.getText(),true));
			}
		});
		textField.setBounds(32, 49, 314, 20);
		getContentPane().add(textField);
		textField.setColumns(1);
		textField.setText(w.getRawName());
		lblNewworld.setText(ColorFormatter.format(textField.getText(),true));
		JLabel lblWillBeDisplayed = new JLabel("Will be displayed as:");
		lblWillBeDisplayed.setBounds(32, 80, 237, 14);
		getContentPane().add(lblWillBeDisplayed);
		
		lblenterTheNew.setBounds(42, 11, 296, 33);
		getContentPane().add(lblenterTheNew);
		
		
		lblNewworld.setBounds(32, 105, 314, 33);
		getContentPane().add(lblNewworld);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		btnSave.setIcon(new ImageIcon(RenameDialog.class.getResource("/de/erdbeerbaerlp/worldManager/icons/SaveFolder.png")));
		btnSave.setBounds(131, 154, 89, 23);
		getContentPane().add(btnSave);
	}
	public String getRawText() {
		return ColorFormatter.unFormat(textField.getText());
	}
	public String getFormattedText() {
		return ColorFormatter.format(textField.getText(), false);
	}
	public void addText(String text) {
		lblenterTheNew.setText("<html><h4>Enter the new world name below.<br>Color codes are allowed</h4><br>+"+text+"</html>");
	}
	public String getText() {
		// TODO Auto-generated method stub
		return textField.getText();
	}
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return this.canceled ;
	}
}
