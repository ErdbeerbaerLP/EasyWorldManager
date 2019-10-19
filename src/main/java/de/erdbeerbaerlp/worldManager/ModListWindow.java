package de.erdbeerbaerlp.worldManager;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ModListWindow extends JDialog {
	private final JButton btnMoreInfo = new JButton("More Info");
	private final JButton btnOpenCurse = new JButton("Open Curse");
	public ModListWindow(ArrayList<Mod> mods, World w) {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setType(Type.POPUP);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModListWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/list.png")));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Mod list of world: "+ ColorFormatter.unFormat(w.getRawName()));
		setSize(438,325);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 422, 229);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane);
		DefaultListModel<Mod> m = new DefaultListModel<Mod>();

		final JList list = new JList(m);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//			System.out.println(arg0.getButton());
				if(arg0.getButton() == 3 && list.getSelectedIndex() != -1) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(new StringSelection(((Mod)m.get(list.getSelectedIndex())).modid), null);
				}
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				btnMoreInfo.setEnabled(list.getSelectedIndex() != -1);
				btnOpenCurse.setEnabled(list.getSelectedIndex() != -1);
				btnOpenCurse.setText(((Mod)m.get(list.getSelectedIndex())).complete ? "Open Curse":"Open Google");
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		btnClose.setBounds(269, 248, 82, 23);
		getContentPane().add(btnClose);


		btnOpenCurse.setEnabled(false);
		btnOpenCurse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list.getSelectedIndex() == -1) return;
				try {
					Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
					Mod mod = (Mod)list.getSelectedValue();
					desktop.browse(mod.uri);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		list.setCellRenderer(new ModListRenderer());
		btnOpenCurse.setBounds(61, 248, 106, 23);
		getContentPane().add(btnOpenCurse);
		btnMoreInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final Mod m = mods.get(list.getSelectedIndex());
				if(m.complete) new ModInfoWindow(m).setVisible(true);
				else JOptionPane.showMessageDialog(null, "This mod could not be found!", "Mod Not Found", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnMoreInfo.setEnabled(false);
		btnMoreInfo.setBounds(177, 248, 82, 23);

		getContentPane().add(btnMoreInfo);
		mods.sort(ALPHABETICAL_ORDER);
		for(int i=0;i<mods.size();i++) {
			m.addElement((Mod)mods.get(i)); 
		}
		System.out.println(list);


	}
	private static Comparator<Mod> ALPHABETICAL_ORDER = new Comparator<Mod>() {
		public int compare(Mod m1, Mod m2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(m1.toString(), m2.toString());
			if (res == 0) {
				res = m1.compareTo(m2);
			}
			return res;
		}
	};
}
