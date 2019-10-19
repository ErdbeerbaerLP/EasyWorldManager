package de.erdbeerbaerlp.worldManager;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.json.JSONObject;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class UpdateCheckWindow extends JDialog {
	private JLabel lblupdateAvailable;
	private JLabel lblanUpdateFor;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JButton btnUpdate;
	private JButton btnJustContinue;
	public UpdateCheckWindow() {
		setAlwaysOnTop(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(UpdateCheckWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/info.png")));
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Update Available");
		setSize(472,335);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		lblupdateAvailable = new JLabel("<html><font size=5>Update available!</html>");
		lblupdateAvailable.setIcon(new ImageIcon(UpdateCheckWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/info.png")));
		lblupdateAvailable.setHorizontalAlignment(SwingConstants.CENTER);
		lblupdateAvailable.setBounds(10, 11, 436, 37);
		getContentPane().add(lblupdateAvailable);
		
		lblanUpdateFor = new JLabel("<html>An update for EasyWorldManager is availabe!<br>\r\nClick \"Update\" to exit this program and open the downloads page<br>\r\n\r\n</html>");
		lblanUpdateFor.setBounds(29, 39, 417, 59);
		getContentPane().add(lblanUpdateFor);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Changelog", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(29, 99, 397, 140);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 17, 377, 112);
		panel.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://minecraft.curseforge.com/projects/worldmanager/files"));
				} catch (IOException | URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
				System.exit(0);
				}catch (Exception errrrr) {
					FMLCommonHandler.instance().exitJava(0, false);
				}
				
				
				
			}
		});
		btnUpdate.setBounds(110, 243, 97, 23);
		getContentPane().add(btnUpdate);
		
		btnJustContinue = new JButton("Continue without updating");
		btnJustContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		btnJustContinue.setBounds(214, 243, 162, 23);
		getContentPane().add(btnJustContinue);
		
		
		
		
		
		
		//Update check
		String urlString = "http://erdbeerbaer.bplaced.net/apis/easyworldmanager.json";
		try {
	        URL url = new URL(urlString);
	        URLConnection urlConnection = url.openConnection();
	        urlConnection.addRequestProperty("User-Agent", "EasyWorldManager");
	        urlConnection.connect();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

		    JSONObject json = new JSONObject(buffer.toString());
		    reader.close();
//		    System.out.println(json);
		    final Set<String> keySet = json.keySet();
		    for(String key : keySet) {
		    	if(Integer.parseInt(key) > Integer.parseInt(Main.versionID)) {
		    		System.out.println("[UpdateCheck]Found an update!");
		    		final String changelog = json.get(key).toString();
		    		textArea.setText(changelog.isEmpty() ? "No changelog available :(":changelog);
		    		setVisible(true);
		    		return;
		    	}
		    }
		    System.out.println("[UpdateCheck]No update found!");
		}catch (UnknownHostException e) {
			System.err.println("[UpdateCheck]Update check failed! Check your internet connection!");
			JOptionPane.showMessageDialog(this, "Could not connect to the internet to check for updates...\n\nDetails:\n"+e.getLocalizedMessage().replace("erdbeerbaer.bplaced.net", "api.erdbeerbaer.tk"));
		} catch (Exception e) {
			System.err.println("[UpdateCheck]Update check failed! Check your internet connection!");
		    JOptionPane.showMessageDialog(this, "Could not connect to the internet to check for updates...\n\nDetails:\n"+e.getLocalizedMessage().replace("erdbeerbaer.bplaced.net", "api.erdbeerbaer.tk"));
		}
	}
}
