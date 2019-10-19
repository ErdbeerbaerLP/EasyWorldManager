package de.erdbeerbaerlp.worldManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.jnbt.ByteTag;
import org.jnbt.CompoundTag;
import org.jnbt.DoubleTag;
import org.jnbt.FloatTag;
import org.jnbt.IntTag;
import org.jnbt.ListTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.Tag;

public class WorldEditWindow extends JDialog{
	private final JTextField textField;
	private final JTextField textField_1;
	private final JTextField textField_2;
	private final JTextField textField_3;
	private final JTextField textField_4;
	private final JTextField textField_5;
	private final JCheckBox chckbxAllowCommand = new JCheckBox("Allow Commands");
	private final JComboBox comboBox = new JComboBox();
	private final JCheckBox chckbxHardcore = new JCheckBox("Hardcore?");
	private final JCheckBox chckbxLocked = new JCheckBox("Locked?");
	private final JComboBox comboBox_1 = new JComboBox();
	
	//TODO Jumppoint
	
	private int selectedGamemode = 0;
	private long player_X = 0;
	private long player_Y = 0;
	private long player_Z = 0;
	private long player_XP = 0;
	private boolean can_Fly = false;
	private boolean can_Build = false;
	private boolean allow_Cmds = false;
	private float playerWalkSpeed = 0;
	private float playerFlySpeed = 0;
	private boolean playerInvulnerable = false;
	
	private boolean ok = false;
	private boolean okPosTag = true;
	private boolean okGamemodeTag = true;
	private boolean okAbilitiesTag = true;
	private boolean okXPTag = true;
	private boolean okCommandTag = true;
	private boolean okDifficulty = true;
	private boolean okDiffLock = true;
	private boolean okHardcore = true;
	
	private Map<String, Tag> rootMap;
	private File levelDat;
	
	//TAGS!
	ByteTag allowCommands = null;
	CompoundTag abilityTag = null;
	IntTag xpLevelTag = null;
	IntTag gamemodeTag = null;
	ByteTag difficultyTag = null;
	ByteTag diffucultyLockTag = null;
	ByteTag hardcoreTag = null;
	ListTag posTag = null;
	CompoundTag datapackTag = null;
	
	
	public WorldEditWindow(final World w) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(WorldEditWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/world.png")));

		try {
			this.levelDat = w.getLevelDat();
			File levelDat = this.levelDat;
			NBTInputStream s = new NBTInputStream(new FileInputStream(levelDat));;
			CompoundTag rootTag = (CompoundTag) s.readTag();;
			Map<String, Tag> rootM = rootTag.getValue();
			this.rootMap = rootM;
			CompoundTag levelTag = (CompoundTag) rootM.get("Data");
			Map<String, Tag> levelM = levelTag.getValue();
			CompoundTag playerTag = (CompoundTag) levelM.get("Player");
			if(playerTag != null) {
			
			Map<String, Tag> playerM = playerTag.getValue();
			posTag = (ListTag) playerM.get("Pos");
			gamemodeTag = (IntTag) playerM.get("playerGameType");
			abilityTag = (CompoundTag) playerM.get("abilities");
			xpLevelTag = (IntTag) playerM.get("XpLevel");
			}
			
			hardcoreTag = (ByteTag) levelM.get("hardcore");
			difficultyTag = (ByteTag) levelM.get("Difficulty");
			diffucultyLockTag = (ByteTag) levelM.get("DifficultyLocked");;
			datapackTag = (CompoundTag) levelM.get("DataPacks");
			allowCommands = (ByteTag) levelM.get("allowCommands");

			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setTitle("EasyWorldManager - Editing World");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(521, 336);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		final JButton btnSave = new JButton("Save");
		btnSave.setToolTipText("Saves all changes and closes this window");
		btnSave.setBounds(151, 263, 89, 23);
		getContentPane().add(btnSave);
		
		final JButton btnCancel = new JButton("Cancel");
		btnCancel.setToolTipText("Closes window and discards changes");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		btnCancel.setBounds(251, 263, 89, 23);
		getContentPane().add(btnCancel);

		
		final JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Difficulty", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(317, 11, 149, 107);
		getContentPane().add(panel_4);
		panel_4.setLayout(null);
		comboBox_1.setToolTipText("Difficulty settings for this world");
		
		
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Peaceful", "Easy", "Normal", "Hard"}));
		comboBox_1.setBounds(11, 19, 122, 20);
		panel_4.add(comboBox_1);
		chckbxHardcore.setToolTipText("Hardcore mode enabled?");
		chckbxHardcore.setBounds(11, 72, 97, 23);
		panel_4.add(chckbxHardcore);
		chckbxLocked.setToolTipText("Whether or not you can change the difficulty in-game");
		chckbxLocked.setBounds(11, 46, 97, 23);
		panel_4.add(chckbxLocked);
		
		final JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Player", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_5.setBounds(10, 11, 297, 203);
		getContentPane().add(panel_5);
		panel_5.setLayout(null);
		
		final JPanel panel = new JPanel();
		panel.setBounds(10, 21, 134, 107);
		panel_5.add(panel);
		panel.setBorder(new TitledBorder(null, "Player Position", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(null);
		
		final JLabel lblX = new JLabel("X:");
		lblX.setBounds(11, 22, 10, 14);
		panel.add(lblX);
		
		final JLabel lblY = new JLabel("Y:");
		lblY.setBounds(11, 53, 10, 14);
		panel.add(lblY);
		
		final JLabel lblZ = new JLabel("Z:");
		lblZ.setBounds(11, 84, 10, 14);
		panel.add(lblZ);
		
		textField_2 = new JTextField();
		textField_2.setToolTipText("Player Z-Position");
		textField_2.setBounds(31, 81, 86, 20);
		textField_2.addKeyListener(new LongInputField());
		panel.add(textField_2);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("Player Y-Position");
		textField_1.setBounds(31, 50, 86, 20);
		textField_1.addKeyListener(new LongInputField());
		panel.add(textField_1);
		
		textField = new JTextField();
		textField.setToolTipText("Player X-Position");
		textField.addKeyListener(new LongInputField());
		textField.setBounds(31, 19, 86, 20);
		panel.add(textField);

		
		
		final JPanel panel_3 = new JPanel();
		panel_3.setBounds(142, 21, 148, 107);
		panel_5.add(panel_3);
		panel_3.setBorder(new TitledBorder(null, "Abilities", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setLayout(null);
		
		final JLabel lblWalkSpeed = new JLabel("Walk speed:");
		lblWalkSpeed.setBounds(6, 22, 64, 14);
		panel_3.add(lblWalkSpeed);
		
		textField_4 = new JTextField();
		textField_4.setToolTipText("<html>\r\nYour Walking speed<br>\r\n Default: 0.05\r\n</html>");
		textField_4.setBounds(72, 18, 64, 20);
		textField_4.addKeyListener(new FloatInputField());
		panel_3.add(textField_4);
		
		final JLabel lblFlySpeed = new JLabel("Fly speed:");
		lblFlySpeed.setBounds(6, 47, 64, 14);
		panel_3.add(lblFlySpeed);
		
		textField_5 = new JTextField();
		textField_5.setToolTipText("<html>Your flying-speed in Creative-Mode<br>\r\nDefault: 0.1\r\n</html>");
		textField_5.setBounds(72, 43, 64, 20);
		textField_5.addKeyListener(new FloatInputField());
		panel_3.add(textField_5);
		chckbxAllowCommand.setToolTipText("Whether or not Commands can be used in this world.");
		
		
		
		chckbxAllowCommand.setBounds(6, 66, 117, 23);
		panel_3.add(chckbxAllowCommand);
		chckbxAllowCommand.setSelected(false);

		
		final JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 139, 134, 54);
		panel_5.add(panel_1);
		panel_1.setBorder(new TitledBorder(null, "Gamemode", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(null);
				comboBox.setToolTipText("The gamemode in which you are Playing");
		
				comboBox.setMaximumRowCount(4);
				comboBox.setModel(new DefaultComboBoxModel(new String[] {"Survival", "Creative", "Adventure", "Spectator (1.8+)"}));
				comboBox.setBounds(10, 23, 114, 20);
				panel_1.add(comboBox);
				final JPanel panel_2 = new JPanel();
				panel_2.setBounds(152, 139, 134, 54);
				panel_5.add(panel_2);
				panel_2.setBorder(new TitledBorder(null, "Player XP", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel_2.setLayout(null);
				
				final JLabel lblLevel = new JLabel("Level:");
				lblLevel.setBounds(10, 24, 29, 14);
				panel_2.add(lblLevel);
				
				textField_3 = new JTextField();
				textField_3.setToolTipText("Current XP Level");
				textField_3.setBounds(49, 21, 75, 20);
				panel_2.add(textField_3);
				textField_3.addKeyListener(new IntInputField());

				final JPanel panel_6 = new JPanel();
				panel_6.setBorder(new TitledBorder(null, "Datapacks", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel_6.setBounds(317, 127, 149, 51);
				getContentPane().add(panel_6);
				panel_6.setLayout(null);
				
				final JButton btnEnabledisable = new JButton("Enable/Disable");
				btnEnabledisable.setToolTipText("<html>\r\nOpen Datapack Manager<br>\r\n<font color=red>WARNING: Resets your settings here </font>\r\n</html>");
				btnEnabledisable.setIcon(new ImageIcon(WorldEditWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/list.png")));
				btnEnabledisable.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						DatapackEditor e = new DatapackEditor(w);
						e.setVisible(true);
						try {
							levelDat = w.getLevelDat();
							File levelDa = levelDat;
							NBTInputStream s = new NBTInputStream(new FileInputStream(levelDa));;
							CompoundTag rootTag = (CompoundTag) s.readTag();;
							Map<String, Tag> rootM = rootTag.getValue();
							rootMap = rootM;
							CompoundTag levelTag = (CompoundTag) rootM.get("Data");
							Map<String, Tag> levelM = levelTag.getValue();
							CompoundTag playerTag = (CompoundTag) levelM.get("Player");
							if(playerTag == null) {
								s.close();
								return;
							}
							Map<String, Tag> playerM = playerTag.getValue();
							posTag = (ListTag) playerM.get("Pos");
							gamemodeTag = (IntTag) playerM.get("playerGameType");
							abilityTag = (CompoundTag) playerM.get("abilities");
							xpLevelTag = (IntTag) playerM.get("XpLevel");
							allowCommands = (ByteTag) levelM.get("allowCommands");
							difficultyTag = (ByteTag) levelM.get("Difficulty");
							diffucultyLockTag = (ByteTag) levelM.get("DifficultyLocked");;
							hardcoreTag = (ByteTag) levelM.get("hardcore");
							datapackTag = (CompoundTag) levelM.get("DataPacks");
							s.close();
						} catch (IOException err) {
							err.printStackTrace();
						}
						if(allowCommands == null) {
							chckbxAllowCommand.setEnabled(false);
							okCommandTag = false;
						}else {
							boolean selected = "1".equals(allowCommands.getValue().toString());
							chckbxAllowCommand.setSelected(selected);
						}
						
						
						
						if(posTag == null) {
							panel.setEnabled(false);
							lblX.setEnabled(false);
							lblY.setEnabled(false);
							lblZ.setEnabled(false);
							textField_2.setEnabled(false);
							textField.setEnabled(false);
							textField_1.setEnabled(false);
							okPosTag = false;
						}else {
							
							List<Tag> posTagList = posTag.getValue();
							try {
								DoubleTag posXTag = (DoubleTag) posTagList.get(0);
								DoubleTag posYTag = (DoubleTag) posTagList.get(1);
								DoubleTag posZTag = (DoubleTag) posTagList.get(2);
								player_X = posXTag.getValue().longValue();
								player_Y = posYTag.getValue().longValue();
								player_Z = posZTag.getValue().longValue();
								textField.setText(player_X+"");
								textField_1.setText(player_Y+"");
								textField_2.setText(player_Z+"");
							}catch (Exception err) {
								System.out.println("ERROR:\n"+err);
								panel.setEnabled(false);
								lblX.setEnabled(false);
								lblY.setEnabled(false);
								lblZ.setEnabled(false);
								textField_2.setEnabled(false);
								textField.setEnabled(false);
								textField_1.setEnabled(false);
								okPosTag = false;
							}
						}
						if(gamemodeTag == null) {
							panel_1.setEnabled(false);
							comboBox.setEnabled(false);
							comboBox.setModel(new DefaultComboBoxModel(new String[] {"<html><font color=red>NOT AVAILABLE</font></html>"}));
							comboBox.setSelectedIndex(0);
							okGamemodeTag = false;
						}else {
							selectedGamemode = gamemodeTag.getValue();
							if(selectedGamemode >=4) {
								comboBox.setModel(new DefaultComboBoxModel(new String[] {"<html><font color=red>INVALID</font></html>"}));
								comboBox.setSelectedIndex(0);
								comboBox.setEnabled(false);
								okGamemodeTag = false;
							}
							else comboBox.setSelectedIndex(selectedGamemode);
						}
						
						if(xpLevelTag == null) {
							lblLevel.setEnabled(false);
							panel_2.setEnabled(false);
							textField_3.setEnabled(false);
							okXPTag = false;
						}else {
							textField_3.setText(xpLevelTag.getValue().toString());
						}
						
						if(abilityTag == null) {
							
							lblWalkSpeed.setEnabled(false);
							lblFlySpeed.setEnabled(false);
							
							textField_4.setEnabled(false);
							textField_5.setEnabled(false);
							
							okAbilitiesTag = false;
						}else {
							Map<String, Tag> abilityMap = abilityTag.getValue();
							can_Fly = ((ByteTag)abilityMap.get("mayfly")).getValue().toString().equals("1");
							can_Build = ((ByteTag)abilityMap.get("mayBuild")).getValue().toString().equals("1");
							playerFlySpeed = ((FloatTag)abilityMap.get("flySpeed")).getValue();
							playerWalkSpeed = ((FloatTag)abilityMap.get("walkSpeed")).getValue();
							playerInvulnerable = ((ByteTag)abilityMap.get("invulnerable")).getValue().toString().equals("1");
							
							textField_4.setText(playerFlySpeed+"");
							textField_5.setText(playerWalkSpeed+"");
							
						}
						if(hardcoreTag == null) {
							chckbxHardcore.setEnabled(false);
							okHardcore = false;
						}else {
							chckbxHardcore.setSelected(hardcoreTag.getValue().toString().equals("1"));
							
						}
						if(difficultyTag == null) {
							okDifficulty = false;
							comboBox_1.setSelectedIndex(0);
							comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"<html><font color=red>NOT AVAILABLE</font></html>"}));
							comboBox_1.setEnabled(false);
						}else {
							comboBox_1.setSelectedIndex(difficultyTag.getValue());
						}
						if(diffucultyLockTag == null) {
							chckbxLocked.setEnabled(false);
							okDiffLock = false;
						}else {
							chckbxLocked.setSelected(diffucultyLockTag.getValue().toString().equals("1"));
						}
						if(difficultyTag == null && diffucultyLockTag == null && hardcoreTag == null) panel_4.setEnabled(false);
						if(datapackTag == null) {
							panel_6.setEnabled(false);
							btnEnabledisable.setEnabled(false);
						}
					}
				});
				btnEnabledisable.setBounds(10, 20, 129, 20);
				panel_6.add(btnEnabledisable);

		
		
		if(allowCommands == null) {
			chckbxAllowCommand.setEnabled(false);
			this.okCommandTag = false;
		}else {
			boolean selected = "1".equals(allowCommands.getValue().toString());
			chckbxAllowCommand.setSelected(selected);
		}
		
		
		
		if(posTag == null) {
			panel.setEnabled(false);
			lblX.setEnabled(false);
			lblY.setEnabled(false);
			lblZ.setEnabled(false);
			textField_2.setEnabled(false);
			textField.setEnabled(false);
			textField_1.setEnabled(false);
			this.okPosTag = false;
		}else {
			
			List<Tag> posTagList = posTag.getValue();
			try {
				DoubleTag posXTag = (DoubleTag) posTagList.get(0);
				DoubleTag posYTag = (DoubleTag) posTagList.get(1);
				DoubleTag posZTag = (DoubleTag) posTagList.get(2);
				this.player_X = posXTag.getValue().longValue();
				this.player_Y = posYTag.getValue().longValue();
				this.player_Z = posZTag.getValue().longValue();
				textField.setText(this.player_X+"");
				textField_1.setText(this.player_Y+"");
				textField_2.setText(this.player_Z+"");
			}catch (Exception e) {
				System.out.println("ERROR:\n"+e);
				panel.setEnabled(false);
				lblX.setEnabled(false);
				lblY.setEnabled(false);
				lblZ.setEnabled(false);
				textField_2.setEnabled(false);
				textField.setEnabled(false);
				textField_1.setEnabled(false);
				this.okPosTag = false;
			}
		}
		if(gamemodeTag == null) {
			panel_1.setEnabled(false);
			comboBox.setEnabled(false);
			comboBox.setModel(new DefaultComboBoxModel(new String[] {"<html><font color=red>NOT AVAILABLE</font></html>"}));
			comboBox.setSelectedIndex(0);
			this.okGamemodeTag = false;
		}else {
			this.selectedGamemode = gamemodeTag.getValue();
			if(this.selectedGamemode >=4) {
				comboBox.setModel(new DefaultComboBoxModel(new String[] {"<html><font color=red>INVALID</font></html>"}));
				comboBox.setSelectedIndex(0);
				comboBox.setEnabled(false);
				this.okGamemodeTag = false;
			}
			else comboBox.setSelectedIndex(this.selectedGamemode);
		}
		
		if(xpLevelTag == null) {
			lblLevel.setEnabled(false);
			panel_2.setEnabled(false);
			textField_3.setEnabled(false);
			this.okXPTag = false;
		}else {
			textField_3.setText(xpLevelTag.getValue().toString());
		}
		
		if(abilityTag == null) {
			
			lblWalkSpeed.setEnabled(false);
			lblFlySpeed.setEnabled(false);
			
			textField_4.setEnabled(false);
			textField_5.setEnabled(false);
			
			this.okAbilitiesTag = false;
		}else {
			Map<String, Tag> abilityMap = abilityTag.getValue();
			this.can_Fly = ((ByteTag)abilityMap.get("mayfly")).getValue().toString().equals("1");
			this.can_Build = ((ByteTag)abilityMap.get("mayBuild")).getValue().toString().equals("1");
			this.playerFlySpeed = ((FloatTag)abilityMap.get("flySpeed")).getValue();
			this.playerWalkSpeed = ((FloatTag)abilityMap.get("walkSpeed")).getValue();
			this.playerInvulnerable = ((ByteTag)abilityMap.get("invulnerable")).getValue().toString().equals("1");
			
			textField_4.setText(this.playerFlySpeed+"");
			textField_5.setText(this.playerWalkSpeed+"");
			
		}
		if(hardcoreTag == null) {
			chckbxHardcore.setEnabled(false);
			okHardcore = false;
		}else {
			chckbxHardcore.setSelected(hardcoreTag.getValue().toString().equals("1"));
			
		}
		if(difficultyTag == null) {
			okDifficulty = false;
			comboBox_1.setSelectedIndex(0);
			comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"<html><font color=red>NOT AVAILABLE</font></html>"}));
			comboBox_1.setEnabled(false);
		}else {
			comboBox_1.setSelectedIndex(difficultyTag.getValue());
		}
		if(diffucultyLockTag == null) {
			chckbxLocked.setEnabled(false);
			okDiffLock = false;
		}else {
			chckbxLocked.setSelected(diffucultyLockTag.getValue().toString().equals("1"));
		}
		if(difficultyTag == null && diffucultyLockTag == null && hardcoreTag == null) panel_4.setEnabled(false);
		if(datapackTag == null) {
			panel_6.setEnabled(false);
			btnEnabledisable.setEnabled(false);
		}
		btnSave.addActionListener(new save());
	}
	
	public boolean isOK() {
		return this.ok ;
	}
private class save implements ActionListener{

	

	@Override
	public void actionPerformed(ActionEvent ev) {
		
		try {
			CompoundTag levelTag = (CompoundTag) rootMap.get("Data");
			Map<String, Tag> newRootMap = new HashMap<String, Tag>();
			newRootMap.putAll(rootMap);
			Map<String, Tag> levelM = levelTag.getValue();
			Map<String, Tag> newLevelMap = new HashMap<String, Tag>();
			newLevelMap.putAll(levelM);
			CompoundTag playerTag = (CompoundTag) levelM.get("Player");
			if(playerTag != null) {
			
			Map<String, Tag> playerM = playerTag.getValue();
			Map<String, Tag> newPlayerMap = new HashMap<String, Tag>();
			newPlayerMap.putAll(playerM);
			
			if(okAbilitiesTag) {
				CompoundTag abilityTag = (CompoundTag) newPlayerMap.get("abilities");
				Map<String, Tag> abilityM = abilityTag.getValue();
				Map<String, Tag> newAbilityMap = new HashMap<String, Tag>();
				newAbilityMap.putAll(abilityM);
				newAbilityMap.replace("flySpeed", new FloatTag("flySpeed", Float.parseFloat(textField_5.getText())));
				newAbilityMap.replace("walkSpeed", new FloatTag("walkSpeed", Float.parseFloat(textField_4.getText())));
				newPlayerMap.replace("abilities", new CompoundTag("abilities", newAbilityMap));
			}
			if(okCommandTag) {
				newLevelMap.replace("allowCommands", new ByteTag("allowCommands",boolByte(chckbxAllowCommand.isSelected())));
				
			}
			if(okGamemodeTag) {
				newPlayerMap.replace("playerGameType", new IntTag("playerGameType", comboBox.getSelectedIndex()));
			}
			if(okPosTag) {
				List<Tag> posList = new ArrayList<Tag>();
				double posX;
				if(textField.getText().isEmpty() ||textField.getText().equals("-")) {
					posX = 0;
				}else posList.add(new DoubleTag("", Double.parseDouble(textField.getText())));
				double posY;
				if(textField_1.getText().isEmpty() ||textField_1.getText().equals("-")) {
					posY = 0;
				}else posList.add( new DoubleTag("", Double.parseDouble(textField_1.getText())));
				double posZ;
				if(textField_2.getText().isEmpty() ||textField_2.getText().equals("-")) {
					posZ = 0;
				}else posList.add( new DoubleTag("", Double.parseDouble(textField_2.getText())));
				newPlayerMap.replace("Pos", new ListTag("Pos", DoubleTag.class, posList));
				
			}
			if(okXPTag) {
				newPlayerMap.replace("XpLevel", new IntTag("XpLevel",Integer.parseInt(textField_3.getText())));
			}
			newLevelMap.replace("Player", new CompoundTag("Player",newPlayerMap));
			}
			if(okHardcore) {
				newLevelMap.replace("hardcore", new ByteTag("hardcore",boolByte(chckbxHardcore.isSelected())));
			}
			if(okDiffLock) {
				newLevelMap.replace("DifficultyLocked", new ByteTag("DifficultyLocked",boolByte(chckbxLocked.isSelected())));
			}
			if(okDifficulty) {
				newLevelMap.replace("Difficulty", new ByteTag("Difficulty", new Integer(comboBox_1.getSelectedIndex()).byteValue()));
			}
			
			newRootMap.put("Data", new CompoundTag("Data",newLevelMap));
			
			NBTOutputStream nbtOut = new NBTOutputStream(new FileOutputStream(levelDat));
			
			nbtOut.writeTag(new CompoundTag("", newRootMap));
			nbtOut.close();
			setVisible(false);
			
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	private byte boolByte(boolean convert) {
		return  (byte) (convert ? 1 : 0 );
		
	}
	
}
private class LongInputField extends KeyAdapter{
	@Override
	public void keyTyped(KeyEvent arg0) {
		JTextField txtField = ((JTextField)arg0.getSource());
		String newText =(new StringBuilder(txtField.getText()).insert(txtField.getCaretPosition(), arg0.getKeyChar())).toString();
		try {
			if(!newText.equals("-")&& !newText.isEmpty()) Long.parseLong(newText);
		}catch (Exception e) {
			arg0.consume();
			final Runnable runnable =
				     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
				if (runnable != null) runnable.run();
		}
	}
}
private class IntInputField extends KeyAdapter{
	@Override
	public void keyTyped(KeyEvent arg0) {
		JTextField txtField = ((JTextField)arg0.getSource());
		String newText =(new StringBuilder(txtField.getText()).insert(txtField.getCaretPosition(), arg0.getKeyChar())).toString();

		try {
				if(!newText.equals("-")&& !newText.isEmpty()) Integer.parseInt(newText);
			
		}catch (Exception e) {
			final Runnable runnable =
				     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
				if (runnable != null) runnable.run();
			arg0.consume();
		}
	}
}
private class FloatInputField extends KeyAdapter{
	@Override
	public void keyTyped(KeyEvent arg0) {
		JTextField txtField = ((JTextField)arg0.getSource());
		String newText =(new StringBuilder(txtField.getText()).insert(txtField.getCaretPosition(), arg0.getKeyChar())).toString();

		try {
			if(!newText.equals("-")&& !newText.isEmpty()) Float.parseFloat(newText);
				
		}catch (Exception e) {
			final Runnable runnable =
				     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
				if (runnable != null) runnable.run();
			arg0.consume();
		}
	}
}
}
