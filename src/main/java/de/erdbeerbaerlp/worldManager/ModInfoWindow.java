package de.erdbeerbaerlp.worldManager;

import java.awt.Desktop;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class ModInfoWindow extends JDialog{
	private final JScrollPane scrollPane = new JScrollPane();
	private final JEditorPane textPane = new JEditorPane();
	private final JButton btnClose = new JButton("Close");
	private final JLabel lblCreationTime = new JLabel();
	private final JLabel lblLastUpdated = new JLabel();
	private final JLabel lbldownloads = new JLabel();
	private final JLabel lblModname = new JLabel();
	private final List list = new List();
	private final JPanel panel = new JPanel();
	private final JLabel lblnewestFile = new JLabel();
	private final JLabel lblversion = new JLabel();
public ModInfoWindow(Mod m) {
	setModal(true);
	setSize(708, 550);
	setLocationRelativeTo(null);
	getContentPane().setLayout(null);
	scrollPane.setBorder(new TitledBorder(null, "Description", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	scrollPane.setBounds(10, 209, 672, 243);
	
	getContentPane().add(scrollPane);
	textPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
	textPane.setEditable(false);
	
	textPane.addHyperlinkListener(new HyperlinkListener() {
	    public void hyperlinkUpdate(HyperlinkEvent e) {
	        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	           try {
				Desktop.getDesktop().browse(e.getURL().toURI());
			} catch (IOException | URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        }
	    }
	});
	scrollPane.setViewportView(textPane);
	setTitle("Mod info for: "+m.name);
	textPane.setText("<html>"+m.fullDescription);
	btnClose.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			setVisible(false);
		}
	});
	btnClose.setBounds(300, 477, 89, 23);
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
		   public void run() { 
		       scrollPane.getVerticalScrollBar().setValue(0);
		   }
		});
	getContentPane().add(btnClose);
	lblCreationTime.setBounds(10, 125, 196, 14);
	
	getContentPane().add(lblCreationTime);
	lblLastUpdated.setBounds(10, 150, 196, 14);
	
	DateTimeFormatter formatter =
		    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
		                     .withLocale( Locale.getDefault() )
		                     .withZone( ZoneId.systemDefault() );
	lblCreationTime.setText("<html><b>Creation Time</b>: "+(m.creationTime == null ? "UNKNOWN":formatter.format( m.creationTime )));
	lblLastUpdated.setText("<html><b>Last Updated</b>: "+(m.creationTime == null ? "UNKNOWN":formatter.format( m.lastUpdated )));
	getContentPane().add(lblLastUpdated);
	lbldownloads.setBounds(10, 175, 196, 14);
	
	getContentPane().add(lbldownloads);
	String downloads;
	if(m.downloads == -1) downloads = "UNKNOWN";
	else downloads = m.downloads+"";
	lbldownloads.setText("<html><b>Total downloads</b>: "+m.downloads);
	lblModname.setHorizontalAlignment(SwingConstants.CENTER);
	lblModname.setBounds(10, 11, 510, 70);
	lblModname.setText("<html><font size=4><b> "+m.name);
	lblModname.setIcon(m.icon);
	getContentPane().add(lblModname);
	panel.setBorder(new TitledBorder(null, "Author(s)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	panel.setBounds(538, 38, 144, 172);
	
	getContentPane().add(panel);
	panel.setLayout(null);
	list.setBounds(10, 20, 124, 142);
	list.removeAll();
	for(int i=0;i<m.authors.size();i++) {
		list.add(m.authors.get(i).username()+" ("+m.authors.get(i).type().name()+")");
	}
	panel.add(list);
	lblnewestFile.setBounds(290, 108, 230, 31);
	lblnewestFile.setText("<html><b>Newest File:</b> "+(m.newestFile == null ? "Unknown":m.newestFile.name()));
	getContentPane().add(lblnewestFile);
	lblversion.setBounds(290, 150, 196, 14);
	lblversion.setText("<html><b>Minecraft Version: </b>"+(m.newestFile == null ? "Unknown":m.newestFile.gameVersionString()));
	getContentPane().add(lblversion);
}
}
