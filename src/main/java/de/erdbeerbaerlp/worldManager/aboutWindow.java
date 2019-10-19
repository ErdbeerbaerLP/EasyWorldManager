package de.erdbeerbaerlp.worldManager;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
public class aboutWindow extends JDialog {
public aboutWindow() {
	setIconImage(Main.getIcon("about.png").getImage());
	setResizable(false);
	setModalityType(ModalityType.APPLICATION_MODAL);
	setTitle("About EasyWorldManager");
	// TODO Auto-generated constructor stub
	setSize(357,274);
	setLocationRelativeTo(null);
	getContentPane().setLayout(null);
	
	JLabel lblaboutEasyworldmanager = new JLabel("<html><h3>About EasyWorldManager</h3></html>");
	lblaboutEasyworldmanager.setHorizontalAlignment(SwingConstants.CENTER);
	lblaboutEasyworldmanager.setBounds(10, 11, 331, 25);
	getContentPane().add(lblaboutEasyworldmanager);
	
	JLabel lbldevelopererdbeerbaerlp = new JLabel("<html><b>Developer :&nbsp;&nbsp;&nbsp;</b>ErdbeerbaerLP");
	lbldevelopererdbeerbaerlp.setBounds(7, 220, 144, 14);
	getContentPane().add(lbldevelopererdbeerbaerlp);
	
	JLabel lblNewLabel = new JLabel("A Simple WorldManager which you can also install as mod");
	lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
	lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	lblNewLabel.setBounds(20, 47, 321, 14);
	getContentPane().add(lblNewLabel);
	
	JLabel lblIfYouInstalled = new JLabel("<html><center>If you downloaded it from any other location<br>\r\nthan from Curseforge please update from Curseforge!</center></html>\r\n");
	lblIfYouInstalled.setHorizontalAlignment(SwingConstants.CENTER);
	lblIfYouInstalled.setBounds(44, 72, 254, 25);
	getContentPane().add(lblIfYouInstalled);
	
	JLabel lblcurseforgePage = new JLabel("<html><p style=\"font-weight:normal;text-decoration:underline;color:#1B0DDB;letter-spacing:1pt;word-spacing:2pt;font-size:11px;text-align:left;font-family:arial, helvetica, sans-serif;line-height:1;\">Curseforge Page</p></html>");
	lblcurseforgePage.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			try {
				desktop.browse(new URI("https://minecraft.curseforge.com/projects/easyworldmanager"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}}
		
	});
	lblcurseforgePage.setBounds(83, 108, 157, 25);
	getContentPane().add(lblcurseforgePage);
	
	JLabel lblVersion = new JLabel("<html><b>Version</b>: "+Main.version);
	lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
	lblVersion.setBounds(197, 220, 144, 14);
	getContentPane().add(lblVersion);
	
	JLabel lblIncludedApis = new JLabel("<html>Included Libraries: <p style=\"font-weight:normal;text-decoration:underline;color:#1B0DDB;letter-spacing:1pt;word-spacing:2pt;font-size:9px;text-align:left;font-family:arial, helvetica, sans-serif;line-height:1;\">ZeroTurnaround ZIP Library</p></html>");
	lblIncludedApis.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			try {
				desktop.browse(new URI("https://github.com/zeroturnaround/zt-zip"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}}
			
		
	});
	lblIncludedApis.setBounds(10, 178, 157, 31);
	getContentPane().add(lblIncludedApis);
	
	JLabel lblzeroturnaroundZipLibrary = new JLabel("<html><p style=\"font-weight:normal;text-decoration:underline;color:#1B0DDB;letter-spacing:1pt;word-spacing:2pt;font-size:9px;text-align:left;font-family:arial, helvetica, sans-serif;line-height:1;\">TheRandomLabs Curse API</p></html>");
	lblzeroturnaroundZipLibrary.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			try {
				desktop.browse(new URI("https://github.com/TheRandomLabs/CurseAPI"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}}
			
		
	});
	lblzeroturnaroundZipLibrary.setBounds(166, 193, 164, 14);
	getContentPane().add(lblzeroturnaroundZipLibrary);
	
}
}
