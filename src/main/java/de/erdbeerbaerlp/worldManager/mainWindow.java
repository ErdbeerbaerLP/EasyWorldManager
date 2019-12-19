package de.erdbeerbaerlp.worldManager;

import org.apache.commons.io.FileUtils;
import org.jnbt.*;
import org.json.JSONObject;
import org.zeroturnaround.zip.ZipUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class mainWindow extends JFrame{
	private mainWindow mainWindow = this;
	public boolean isOpen = false;
	public String worldDir;
	public static ArrayList<World> worlds;
	protected boolean worldMode = true;
	public mainWindow instance;
	private JButton btnNewButton_5 = new JButton("Edit World");
	private JButton btnNewButton_4 = new JButton("Rename");
	private JButton btnNewButton = new JButton("Save as backup");
	private JRadioButton rdbtnBackups = new JRadioButton("Backups");
	private static DefaultListModel list = new DefaultListModel();
	private JRadioButton rdbtnWorlds = new JRadioButton("Worlds");
	private static JList jlist = new JList(list);
	private JButton btnNewButton_1 = new JButton("Reload List");
	private JButton btnNewButton_2 = new JButton("Delete");
	private JButton btnNewButton_3 = new JButton("Open in NBTExplorer");
	private JLabel lblCompressionLevel = new JLabel("Compression Level");
	private JSpinner spinner = new JSpinner();
	protected ArrayList<Backup> backups;
	public static JLabel lblWorldDir = new JLabel("Not Selected!");
	private final WorldRenderer worldRender = new WorldRenderer();
	private final BackupRenderer backupRender = new BackupRenderer();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenuItem mntmOpensavesFolder = new JMenuItem("Open \"saves\" Folder");
	private final JMenuItem mntmSetNbtexplorerexePath = new JMenuItem("Set NBTExplorer.exe path");
	private final aboutWindow about = new aboutWindow();
	private final JMenuItem mntmChangeBackupBolder = new JMenuItem("Change backup folder");
	private final JMenuItem mntmAppdataroamingminecraft = new JMenuItem("AppData/Roaming/.minecraft");
	public static boolean isMod = false;
	private JCheckBox chckbxAutomaticUpdateCheck;
	public mainWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(mainWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/world.png")));
		instance = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
			}
			
		});

		jlist.setCellRenderer(worldRender);
		setTitle("Easy World Manager");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(582,434);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		rdbtnWorlds.setSelected(true);
		rdbtnWorlds.setBounds(23, 18, 95, 23);
		getContentPane().add(rdbtnWorlds);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		rdbtnBackups.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnWorlds.setSelected(false);
				rdbtnBackups.setSelected(true);
				spinner.setEnabled(false);
				btnNewButton_3.setEnabled(false);
				btnNewButton_4.setEnabled(false);
				jlist.setCellRenderer(backupRender);
				worldMode = false;
				btnNewButton.setText("Restore Backup");
				loadBackups();
			}
		});
		rdbtnWorlds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchToWorld();
			}
		});
		rdbtnBackups.setBounds(120, 18, 109, 23);
		getContentPane().add(rdbtnBackups);
		btnNewButton.setEnabled(false);
		btnNewButton.setIcon(Main.getIcon("backup.png"));
		
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(worldMode  == true) {
					if(jlist.getSelectedIndex()>=0) {
		        Calendar cal = Calendar.getInstance();
				File world = worlds.get(jlist.getSelectedIndex()).getWorldPath();
				File out = new File(Main.backupDir+world.getName()+"~"+cal.getTimeInMillis()+".zip");
				File outDir = new File(Main.backupDir);
				if(!outDir.exists()) outDir.mkdirs();
				if(!out.exists()) {
					try {
						out.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				getContentPane().setEnabled(false);
				ZipUtil.pack(world, out, (int)spinner.getValue());
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				getContentPane().setEnabled(true);
				}
				}else {
					if(worldDir != null) {
						File backup = backups.get(jlist.getSelectedIndex()).getPath();
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						getContentPane().setEnabled(false);
						ZipUtil.unpack(backup, new File(worldDir + "/" + backups.get(jlist.getSelectedIndex()).getFileName()));
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						getContentPane().setEnabled(true);
					}
				}
			}
		});
		btnNewButton.setBounds(365, 50, 188, 23);
		getContentPane().add(btnNewButton);
		btnNewButton_1.setToolTipText("Reloads all worlds/backups in this list");
		btnNewButton_1.setEnabled(true);
		btnNewButton_1.setIcon(Main.getIcon("reload.png"));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(worldMode == true) {
					loadWorlds();
				}else {
					loadBackups();
				}
			}
		});
		
		
		btnNewButton_1.setBounds(365, 123, 188, 23);
		getContentPane().add(btnNewButton_1);
		btnNewButton_2.setToolTipText("<html>\r\nDeletes selected world/backup<br>\r\n<font color=red> Deleted files are unrecoverable!</font>\r\n</html>");
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.setIcon(Main.getIcon("delete.png"));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(worldMode == true) {
					if(jlist.getSelectedIndex() >= 0) {
						try {
							if(canDirBeDeleted(worlds.get(jlist.getSelectedIndex()).getWorldPath()))
								FileUtils.deleteDirectory(worlds.get(jlist.getSelectedIndex()).getWorldPath());
							else {
								JOptionPane.showMessageDialog(mainWindow, "Can not delete directory!", "Error", JOptionPane.ERROR_MESSAGE);
								loadWorlds();
								return;
							}
								
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						loadWorlds();
					}
				}else {
					if(jlist.getSelectedIndex() >= 0) {
						backups.get(jlist.getSelectedIndex()).getPath().delete();
						loadBackups();
					}
				}

			}
		});
		
		
		btnNewButton_2.setBounds(365, 157, 188, 23);
		getContentPane().add(btnNewButton_2);
		btnNewButton_3.setToolTipText("Opens this world in NBTExplorer (If available)");
		btnNewButton_3.setIcon(Main.getIcon("NBT-Explorer.png"));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process process = new ProcessBuilder(Main.nbtexplorer,(worlds.get(jlist.getSelectedIndex()).getWorldPath()).getPath()).start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		btnNewButton_3.setBounds(365, 292, 188, 23);
		getContentPane().add(btnNewButton_3);
		spinner.setToolTipText("<html>\r\nHow much the files are being compressed<br>\r\nHigher level = Lower file size & More time needed to compress<br>\r\nLower level = Bigger file size & Less time needed to compress<br>\r\n</html>");
		
		
		spinner.setModel(new SpinnerNumberModel(5, 0, 9, 1));
		spinner.setBounds(435, 91, 42, 20);
		getContentPane().add(spinner);
		
		
		lblCompressionLevel.setBounds(406, 75, 109, 14);
		getContentPane().add(lblCompressionLevel);
		JScrollPane scrolllist = new JScrollPane(jlist);
		scrolllist.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrolllist.setBounds(10, 52, 338, 287);
		getContentPane().add(scrolllist);
		
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		mnNewMenu.setBackground(Color.GRAY);
		mnNewMenu.setMnemonic('f');
		menuBar.add(mnNewMenu);
		mntmOpensavesFolder.setIcon(Main.getIcon("SaveFolder.png"));
		mntmOpensavesFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chose = new JFileChooser();
				chose.setDialogTitle("Choose current \"saves\" folder");
				chose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chose.setApproveButtonText("Use");
				chose.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return "\"saves\" Folder";
					}
					
					@Override
					public boolean accept(File f) {
						return f.isDirectory();
					}
				});
				if(System.getProperty("os.name").toLowerCase().contains("windows")) chose.setCurrentDirectory(new File(System.getProperty("user.home")+"/AppData/Roaming/"));
				int res = chose.showOpenDialog(instance);
				if(res == JFileChooser.CANCEL_OPTION) return;
				try {
					
					worldDir = chose.getSelectedFile().getPath();
					if(!worldDir.endsWith("saves")) {
						if(new File(worldDir+"/saves").exists()) worldDir = worldDir+File.separator+"saves";
					}
				}catch(Exception e) {
					return;
				}
				switchToWorld();
			}
		});
		
		mnNewMenu.add(mntmOpensavesFolder);
		
		mntmAppdataroamingminecraft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				worldDir = new File(System.getProperty("user.home")+"/AppData/Roaming/.minecraft/saves").getAbsolutePath();
				switchToWorld();
				
			}
		});
		mntmAppdataroamingminecraft.setIcon(Main.getIcon("SaveFolder.png"));
		mnNewMenu.add(mntmAppdataroamingminecraft);
		
		JMenu mnNewMenu_1 = new JMenu("Settings");
		mnNewMenu_1.setBackground(Color.GRAY);
		mnNewMenu_1.setMnemonic('s');
		menuBar.add(mnNewMenu_1);
		mntmSetNbtexplorerexePath.setIcon(Main.getIcon("NBT-Explorer.png"));
		mntmSetNbtexplorerexePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser choose = new JFileChooser();
				choose.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return "NBTExplorer.exe";
					}
					
					@Override
					public boolean accept(File f) {
						if (f.getName().equals("NBTExplorer.exe")) return true;
						return f.isDirectory();
					}
				});
				choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
				choose.setCurrentDirectory(new File("C:"));
				choose.setApproveButtonText("Use");
				int result = choose.showOpenDialog(instance);
				if (result == JFileChooser.CANCEL_OPTION) return;
				try {
					Main.saveConfig(Main.backupDir, choose.getSelectedFile().getAbsolutePath(), chckbxAutomaticUpdateCheck.isSelected());
				} catch (Exception e1) {
					return;
				}
			}
		});
		btnNewButton_3.setEnabled(false);
		btnNewButton_4.setToolTipText("Renames the selected World");
		
		btnNewButton_4.setEnabled(false);
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(worldMode == true) {
					World w = worlds.get(jlist.getSelectedIndex());
					File levelDat = w.getLevelDat();
					NBTInputStream s;
					CompoundTag rootTag;

						try {
							s = new NBTInputStream(new FileInputStream(levelDat));
							rootTag = (CompoundTag) s.readTag();
						} catch (IOException e1) {
							rootTag = null;
							s = null;
						}
					Map<String, Tag> rootM = rootTag.getValue();
					CompoundTag levelTag = (CompoundTag) rootM.get("Data");
					Map<String, Tag> levelM = levelTag.getValue();
					
					Map<String, Tag> rootMap = new HashMap<String, Tag>();
					rootMap.putAll(rootM);
					
					Map<String, Tag> levelMap = new HashMap<String, Tag>();
					levelMap.putAll(levelM);
					
					RenameDialog dlg = new RenameDialog(w);
					dlg.setVisible(true);
					if(dlg.isCanceled()) return;
					String rawName = dlg.getRawText();
					String name = dlg.getText();
					String fName = dlg.getFormattedText();
					StringTag nameTag = new StringTag("LevelName", name);
					
					levelMap.put("LevelName", nameTag);
					rootMap.put("Data", new CompoundTag("Data",levelMap));
					
					if(w.getRawName().equals(name)) {
						try {
							s.close();
						}catch (Exception e2) {
						}
						return;
						
					}
					try {
						s.close();
						NBTOutputStream nbtOut = new NBTOutputStream(new FileOutputStream(levelDat));
						
						nbtOut.writeTag(new CompoundTag("", rootMap));
						nbtOut.close();
						
					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					boolean trying = true;
					File target = new File(w.getWorldPath().getAbsolutePath().replace(w.getWorldPath().getName(), rawName));
					while(trying) {
					if(!target.exists()) {
						w.getWorldPath().renameTo(target);
						trying = false;
					}else {
						target = new File(target.getAbsolutePath()+"-");
					}
					}
					switchToWorld();
				}
			}
		});
		btnNewButton_4.setIcon(new ImageIcon(mainWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/rename.png")));
		btnNewButton_4.setBounds(365, 192, 188, 23);
		getContentPane().add(btnNewButton_4);
		
		final JButton btnModList = new JButton("Mod List");
		btnModList.setToolTipText("Lists all mods used in this world\nRequires internet connection");
		btnModList.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent arg0) {
				Thread t = new Thread() {
					private int progress = 0;
					private int length = -1;
					private ProgressWindow prog = new ProgressWindow(1);
					@Override
					public void run() {
						World w = worlds.get(jlist.getSelectedIndex());
						NBTInputStream s = null;
						CompoundTag rootTag;
						ArrayList<Mod> modList = new ArrayList<Mod>();
							try {
								s = new NBTInputStream(new FileInputStream(w.getLevelDat()));
								rootTag = (CompoundTag) s.readTag();
							} catch (IOException e1) {
								rootTag = null;
//								s = null;
							}
						Map<String, Tag> rootMap = rootTag.getValue();
						CompoundTag levelTag = (CompoundTag) rootMap.get("FML");
						CompoundTag levelTag113 = (CompoundTag) rootMap.get("fml");
						Map<String, Tag> levelMap = levelTag==null?levelTag113.getValue():levelTag.getValue();
						ListTag listTag;
						if(levelMap.containsKey("ModList")) listTag = (ListTag) levelMap.get("ModList");
						else listTag = (ListTag) levelMap.get("LoadingModList");
						Object[] modTagList = listTag.getValue().toArray();
//						ArrayList<String> modIDs = new ArrayList<String>();
						try {
							s.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				        StringBuffer buffer = new StringBuffer();
                        String urlString = "https://raw.githubusercontent.com/ErdbeerbaerLP/EasyWorldManager/master/modIDRedirects.json";
				        boolean connected = true;
							try {
								URL url = new URL(urlString);
						        URLConnection urlConnection = url.openConnection();
						        urlConnection.addRequestProperty("User-Agent", "EasyWorldManager");
						        urlConnection.connect();
								BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
								int read;
								char[] chars = new char[1024];
								while ((read = reader.read(chars)) != -1)
								buffer.append(chars, 0, read);
								reader.close();
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								connected = false;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(mainWindow, "Could not connect to the internet...\n\nDetails:\n" + e.getLocalizedMessage().replace(urlString, "<hidden>"));
								connected = false;
								
							}
						//								getLogger().disableDebug();
						JSONObject json = new JSONObject(buffer.toString());
						this.length = modTagList.length;
						prog.setMaximum(length);
						prog.updateProgress(0);
						Thread t1 = new Thread(() -> {
							for (int i = 0 ; i < (length / 4) ; i++) {
								System.out.println("1:" + i);
								if (prog.isCanceled()) break;
								
								CompoundTag tg = (CompoundTag) modTagList[i];
								Map<String, Tag> tgMap = tg.getValue();
								String modid = ((StringTag) tgMap.get("ModId")).getValue().replace("<", "").replace(">", "");
								if (!modid.equals("minecraft") && !modid.equals("FML") && !modid.equals("mcp")) {
									modList.add(new Mod(modid, ((StringTag) tgMap.get("ModVersion")).getValue(), json));
								}
								progress++;
							}
						});
						Thread t2 = new Thread(() -> {
//							    		System.out.println((length/4)+" < "+(2*length/4));
							for (int i = (length / 4) ; i < (2 * length / 4) ; i++) {
								System.out.println("2:" + i);
								if (prog.isCanceled()) break;
								
								CompoundTag tg = (CompoundTag) modTagList[i];
								Map<String, Tag> tgMap = tg.getValue();
								String modid = ((StringTag) tgMap.get("ModId")).getValue().replace("<", "").replace(">", "");
								if (!modid.equals("minecraft") && !modid.equals("FML") && !modid.equals("mcp")) {
									modList.add(new Mod(modid, ((StringTag) tgMap.get("ModVersion")).getValue(), json));
									
								}
								progress++;
							}
						});
						Thread t3 = new Thread(() -> {
							for (int i = (2 * length / 4) ; i < (3 * length / 4) ; i++) {
								System.out.println("3:" + i);
								if (prog.isCanceled()) break;
								
								CompoundTag tg = (CompoundTag) modTagList[i];
								Map<String, Tag> tgMap = tg.getValue();
								String modid = ((StringTag) tgMap.get("ModId")).getValue().replace("<", "").replace(">", "");
								if (!modid.equals("minecraft") && !modid.equals("FML") && !modid.equals("mcp")) {
									modList.add(new Mod(modid, ((StringTag) tgMap.get("ModVersion")).getValue(), json));
									
								}
								progress++;
								
							}
						});
						Thread t4 = new Thread(() -> {
							for (int i = (3 * length / 4) ; i < length ; i++) {
								System.out.println("4:" + i);
								if (prog.isCanceled()) break;
								
								CompoundTag tg = (CompoundTag) modTagList[i];
								Map<String, Tag> tgMap = tg.getValue();
								String modid = ((StringTag) tgMap.get("ModId")).getValue().replace("<", "").replace(">", "");
								if (!modid.equals("minecraft") && !modid.equals("FML") && !modid.equals("mcp")) {
									modList.add(new Mod(modid, ((StringTag) tgMap.get("ModVersion")).getValue(), json));
									
								}
//									if(i== modTagList.length-1) {
//										progress = modTagList.length+1;
//									}else {
								progress++;
//									}
							}
						});
						Thread checker = new Thread(() -> {
							while (true) {
								if (!t1.isAlive() && !t2.isAlive() && !t3.isAlive() && !t4.isAlive()) {
									if (modList.size() < modTagList.length) {
										for (int i = 0 ; i < modTagList.length ; i++) {
											CompoundTag tg = (CompoundTag) modTagList[i];
											Map<String, Tag> tgMap = tg.getValue();
											String modid = ((StringTag) tgMap.get("ModId")).getValue().replace("<", "").replace(">", "");
											Mod m = new Mod(modid, ((StringTag) tgMap.get("ModVersion")).getValue(), json, false);
											if (!modid.equals("minecraft") && !modid.equals("FML") && !modid.equals("mcp") && !modList.contains(m)) modList.add(m);
										}
									}
									final ModListWindow win = new ModListWindow(modList, w);
									prog.setVisible(false);
									win.setVisible(true);
									break;
								}
								else {
									prog.updateProgress(progress);
								}
							}
						});
						
						
						t1.start();
						t2.start();
						t3.start();
						t4.start();
						checker.start();
						prog.setVisible(true);
					}
				};
				t.start();
				
			}
		});
		btnModList.setIcon(new ImageIcon(mainWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/list.png")));
		btnModList.setEnabled(false);
		btnModList.setBounds(365, 224, 188, 23);
		getContentPane().add(btnModList);
		btnNewButton_5.setToolTipText("<html>\r\nOpens the world editor<br><br>\r\nThis allows you to change your position and other<br>\r\nvalues like difficulty\r\n</html>");
		
		btnNewButton_5.setEnabled(false);

        
        btnNewButton_5.setIcon(new ImageIcon(new ImageIcon(mainWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/world.png")).getImage().getScaledInstance(16, 16,Image.SCALE_SMOOTH))); 
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorldEditWindow pew = new WorldEditWindow(worlds.get(jlist.getSelectedIndex()));
				pew.setVisible(true);
				switchToWorld();
			}
		});
		btnNewButton_5.setBounds(365, 258, 188, 23);
		getContentPane().add(btnNewButton_5);
		
		JLabel lblCurrentsavesFolder = new JLabel("    Current \"saves\" folder: ");
		lblCurrentsavesFolder.setBounds(0, 350, 163, 19);
		getContentPane().add(lblCurrentsavesFolder);
		lblCurrentsavesFolder.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblWorldDir.setBounds(162, 350, 391, 19);
		getContentPane().add(lblWorldDir);
		lblWorldDir.setFont(new Font("Dialog", Font.PLAIN, 14));
		mnNewMenu_1.add(mntmSetNbtexplorerexePath);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				about.setVisible(true);
			}
		});
		mntmChangeBackupBolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Choose backup directory");
				int result = chooser.showSaveDialog(instance);
				if (result == JFileChooser.CANCEL_OPTION) return;
				try {
					Main.saveConfig(chooser.getSelectedFile().getAbsolutePath(), Main.nbtexplorer, chckbxAutomaticUpdateCheck.isSelected());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mntmChangeBackupBolder.setIcon(Main.getIcon("backup.png"));
		
		mnNewMenu_1.add(mntmChangeBackupBolder);
		
		chckbxAutomaticUpdateCheck = new JCheckBox("Automatic update check");
		chckbxAutomaticUpdateCheck.setToolTipText("Checks for updates before starting the program");
		chckbxAutomaticUpdateCheck.setSelected(Main.autoUpdate);
		chckbxAutomaticUpdateCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final boolean isEnabled = chckbxAutomaticUpdateCheck.isSelected();
				try {
					Main.saveConfig(Main.backupDir, Main.nbtexplorer, isEnabled);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mnNewMenu_1.add(chckbxAutomaticUpdateCheck);
		mntmAbout.setIcon(Main.getIcon("about.png"));
		mnNewMenu_1.add(mntmAbout);
		
		jlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int index = jlist.getSelectedIndex();
				boolean enabled = (index >= 0);
				if(index >= 0 && worldMode)btnNewButton_5.setEnabled(enabled);
				else btnNewButton_5.setEnabled(false);
				btnNewButton.setEnabled(enabled);
				btnNewButton_1.setEnabled(true);
				btnNewButton_2.setEnabled(enabled);
				if(worldMode && !Main.nbtexplorer.isEmpty()) btnNewButton_3.setEnabled(enabled);
				if(worldMode && index >= 0 && !worlds.get(index).isOld()) btnNewButton_4.setEnabled(true);
				else btnNewButton_4.setEnabled(false);
				if(index >= 0 && worldMode)btnModList.setEnabled(worlds.get(index).isModdedB());
				else btnModList.setEnabled(false);
			}
		});
	}

	protected boolean canDirBeDeleted(File dir) {
		if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
		for( File f : listFilesForFolder(dir)) {
			if(!f.canWrite()) return false;
		}
		return true;
		
	}
	public ArrayList<File> listFilesForFolder(final File folder) {
		ArrayList<File> fl = new ArrayList<File>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            fl.addAll(listFilesForFolder(fileEntry));
	        } else {
	        	fl.add( fileEntry);
	        }
	    }
	    
	    return fl;
	}
	protected void switchToWorld() {
		// TODO Auto-generated method stub
		rdbtnWorlds.setSelected(true);
		rdbtnBackups.setSelected(false);
		spinner.setEnabled(true);
		jlist.setCellRenderer(worldRender);
		worldMode = true;
		btnNewButton.setText("Save as backup");
		loadWorlds();
	}

	@Override
	public void setVisible(boolean b) {
		isOpen = b;
			mntmOpensavesFolder.setEnabled(!isMod);
			mntmAppdataroamingminecraft.setEnabled(!isMod);

		super.setVisible(b);
	}
	public void open(String worldDir) {
		this.worldDir = worldDir;
		// TODO Auto-generated method stub
		this.setVisible(true);
		loadWorlds();
	}
	protected void loadWorlds() {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		getContentPane().setEnabled(false);
		String wdir;
		try {
			wdir = worldDir;
		}catch(Exception e) {
			wdir = "Not selected";
		}
		lblWorldDir.setText(wdir);
		list.removeAllElements();
		try {
    	File[] filesList = new File(worldDir).listFiles();
    	ArrayList worldList = new ArrayList();
    	for (File file : filesList) {
    	    if (file.isDirectory()) {
    	        File world = new File(file.getPath()+"/level.dat");
    	        if(world.exists()) {
    	        	worldList.add(new World(file));
    	        }
    	    }
    	}
    	worlds = worldList;
		for(int i=0;i<worldList.size();i++) {
			list.addElement(worldList.get(i));
		}
	}catch(Exception e) {
	}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		getContentPane().setEnabled(true);
	}
	private void loadBackups() {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		getContentPane().setEnabled(false);
		list.removeAllElements();
    	File[] filesList = new File(Main.backupDir).listFiles();
    	ArrayList backupList = new ArrayList();
    	for (File file : filesList) {
    	    if (!file.isDirectory() && file.getName().endsWith(".zip")) {
    	        	try {
						backupList.add(new Backup(file));
					} catch (Exception e) {
					}
    	    }
    	}
    	backups = backupList;
		for(int i=0;i<backupList.size();i++) {
			list.addElement(backups.get(i));
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		getContentPane().setEnabled(true);
	}
}
