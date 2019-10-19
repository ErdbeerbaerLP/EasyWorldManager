package de.erdbeerbaerlp.worldManager;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.StringTag;
import org.jnbt.Tag;

public class DatapackEditor extends JDialog {
	private File levelDat;
	
	private JList list_1 = new JList();
	private JList list = new JList();
	private JButton btnDisable = new JButton("Disable ->");
	private JButton btnEnable = new JButton("<- Enable");
	
	private Map<String, Tag> rootMap;
	private ListTag enabledTag = null;
	private ListTag disabledTag = null;
	public DatapackEditor(World w) {
		this.levelDat = w.getLevelDat();
		setIconImage(Toolkit.getDefaultToolkit().getImage(DatapackEditor.class.getResource("/de/erdbeerbaerlp/worldManager/icons/list.png")));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setTitle("Datapack Editor");
		setSize(577,373);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane((Component) null);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 31, 214, 292);
		getContentPane().add(scrollPane);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				btnDisable.setEnabled((list.getSelectedIndex() >= 0));
			}
		});
		
		
		scrollPane.setViewportView(list);
		JScrollPane scrollPane_1 = new JScrollPane((Component) null);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(333, 31, 223, 292);
		getContentPane().add(scrollPane_1);
		list_1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				btnEnable.setEnabled((list_1.getSelectedIndex() >= 0));
			}
		});
		
		
		scrollPane_1.setViewportView(list_1);
		btnEnable.setEnabled(false);
		
		
		btnEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CompoundTag levelTag = (CompoundTag) rootMap.get("Data");
					Map<String, Tag> newRootMap = new HashMap<String, Tag>();
					newRootMap.putAll(rootMap);
					Map<String, Tag> levelM = levelTag.getValue();
					Map<String, Tag> newLevelMap = new HashMap<String, Tag>();
					newLevelMap.putAll(levelM);
					CompoundTag datapackTag = (CompoundTag) levelM.get("DataPacks");
					if(datapackTag == null) {
						return;
					}
					Map<String, Tag> datapackM = datapackTag.getValue();
					Map<String, Tag> newDatapackMap = new HashMap<String, Tag>();
					newDatapackMap.putAll(datapackM);
					String datapackToEnable = (String) list_1.getModel().getElementAt(list_1.getSelectedIndex());
					List disabledList = new ArrayList();
					String datapack = "";
					System.out.println("Size of disabled tag: "+ disabledTag.getValue().size());
					for(int i =0; i<disabledTag.getValue().size();i++) {
						if(!((StringTag)disabledTag.getValue().get(i)).getValue().startsWith("file/"+datapackToEnable) && !((StringTag)disabledTag.getValue().get(i)).getValue().startsWith(datapackToEnable)) {
							disabledList.add(disabledTag.getValue().get(i));
							System.out.println("Adding "+disabledTag.getValue().get(i)+" to disabled");
						}
						else datapack = ((StringTag)disabledTag.getValue().get(i)).getValue();
					
					}
					List enabledList = new ArrayList();
					for(int i =0; i<enabledTag.getValue().size();i++) {
						System.out.println("Adding "+enabledTag.getValue().get(i)+" to enabled");
						enabledList.add(enabledTag.getValue().get(i));
					}
					enabledList.add(new StringTag("", datapack));
					System.out.println("Adding "+new StringTag("", datapack)+" to enabled");
					ListTag newEnabledTag = new ListTag("Enabled", StringTag.class, enabledList);
					ListTag newDisabledTag = new ListTag("Disabled", StringTag.class, disabledList);
					newDatapackMap.put("Enabled", newEnabledTag);
					newDatapackMap.put("Disabled", newDisabledTag);
					newLevelMap.put("DataPacks", new CompoundTag("DataPacks", newDatapackMap));
					newRootMap.put("Data", new CompoundTag("Data", newLevelMap));
					NBTOutputStream nbtOut = new NBTOutputStream(new FileOutputStream(levelDat));
					System.out.println("Writing to level.dat");
					nbtOut.writeTag(new CompoundTag("", newRootMap));
					System.out.println("DONE");
					nbtOut.close();
					updateList();
				}catch (Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnEnable.setBounds(234, 120, 89, 23);
		getContentPane().add(btnEnable);
		btnDisable.setEnabled(false);
		
		
		btnDisable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CompoundTag levelTag = (CompoundTag) rootMap.get("Data");
					Map<String, Tag> newRootMap = new HashMap<String, Tag>();
					newRootMap.putAll(rootMap);
					Map<String, Tag> levelM = levelTag.getValue();
					Map<String, Tag> newLevelMap = new HashMap<String, Tag>();
					newLevelMap.putAll(levelM);
					CompoundTag datapackTag = (CompoundTag) levelM.get("DataPacks");
					if(datapackTag == null) {
						return;
					}
					Map<String, Tag> datapackM = datapackTag.getValue();
					Map<String, Tag> newDatapackMap = new HashMap<String, Tag>();
					newDatapackMap.putAll(datapackM);
					String datapackToDisable = (String) list.getModel().getElementAt(list.getSelectedIndex());
					List enabledList = new ArrayList();
					String datapack = "";
					System.out.println("Size of enabled tag: "+ enabledTag.getValue().size());
					for(int i =0; i<enabledTag.getValue().size();i++) {
						
						if(!((StringTag)enabledTag.getValue().get(i)).getValue().startsWith("file/"+datapackToDisable) && !((StringTag)enabledTag.getValue().get(i)).getValue().startsWith(datapackToDisable)) {
							enabledList.add(enabledTag.getValue().get(i));
							System.out.println("Adding "+enabledTag.getValue().get(i)+" to enabled");
						}
						else datapack = ((StringTag)enabledTag.getValue().get(i)).getValue();
					
					}
					List disabledList = new ArrayList();
					for(int i =0; i<disabledTag.getValue().size();i++) {
						System.out.println("Adding "+disabledTag.getValue().get(i)+" to disabled");
						disabledList.add(disabledTag.getValue().get(i));
					}
					disabledList.add(new StringTag("", datapack));
					System.out.println("Adding "+new StringTag("", datapack)+" to disabled");
					ListTag newEnabledTag = new ListTag("Enabled", StringTag.class, enabledList);
					ListTag newDisabledTag = new ListTag("Disabled", StringTag.class, disabledList);
					newDatapackMap.put("Enabled", newEnabledTag);
					newDatapackMap.put("Disabled", newDisabledTag);
					newLevelMap.put("DataPacks", new CompoundTag("DataPacks", newDatapackMap));
					newRootMap.put("Data", new CompoundTag("Data", newLevelMap));
					NBTOutputStream nbtOut = new NBTOutputStream(new FileOutputStream(levelDat));
					System.out.println("Writing to level.dat");
					nbtOut.writeTag(new CompoundTag("", newRootMap));
					System.out.println("DONE");
					nbtOut.close();
					updateList();
				}catch (Exception err) {
					err.printStackTrace();
				}
				
			}
		});
		btnDisable.setBounds(234, 154, 89, 23);
		getContentPane().add(btnDisable);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnDone.setBounds(234, 188, 89, 23);
		getContentPane().add(btnDone);
		
		JLabel lblEnabled = new JLabel("Enabled");
		lblEnabled.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnabled.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblEnabled.setBounds(10, 13, 214, 14);
		getContentPane().add(lblEnabled);
		
		JLabel lblDisabled = new JLabel("Disabled");
		lblDisabled.setHorizontalAlignment(SwingConstants.CENTER);
		lblDisabled.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDisabled.setBounds(333, 14, 214, 14);
		getContentPane().add(lblDisabled);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateList();
	}
	private void updateList() {
		try {
			File levelDat = this.levelDat;
			NBTInputStream s = new NBTInputStream(new FileInputStream(levelDat));;
			CompoundTag rootTag = (CompoundTag) s.readTag();;
			Map<String, Tag> rootM = rootTag.getValue();
			this.rootMap = rootM;
			CompoundTag levelTag = (CompoundTag) rootM.get("Data");
			Map<String, Tag> levelM = levelTag.getValue();
			CompoundTag datapackTag = (CompoundTag) levelM.get("DataPacks");
			Map<String, Tag> datapackM = datapackTag.getValue();
			enabledTag = (ListTag) datapackM.get("Enabled");
			disabledTag = (ListTag) datapackM.get("Disabled");
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		list.removeAll();
		final DefaultListModel model = new DefaultListModel<>();
		final List<Tag> l = enabledTag.getValue();
		for(int i = 0; i < l.size(); i++) {
			model.addElement(((StringTag)l.get(i)).getValue().replace(".zip", "").replace("file/", ""));
		}
		list.setModel(model);
		list_1.removeAll();
		final DefaultListModel model1 = new DefaultListModel<>();
		final List<Tag> l1 = disabledTag.getValue();
		for(int i = 0; i < l1.size(); i++) {
			model1.addElement(((StringTag)l1.get(i)).getValue().replace(".zip", "").replace("file/", ""));
		}
		list_1.setModel(model1);
	}
}
