package de.erdbeerbaerlp.worldManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jnbt.CompoundTag;
import org.jnbt.DoubleTag;
import org.jnbt.IntTag;
import org.jnbt.NBTInputStream;
import org.jnbt.Tag;

import net.minecraftforge.fml.common.ProgressManager;

public class Main {
	protected static File configFile = new File(System.getProperty("user.home") +"/EasyWorldManager.properties");
	protected static String backupDir;
	protected static String nbtexplorer;
	protected static boolean autoUpdate;
	protected static final String versionID = "116";
	public static final String version = "1.1.6";
	private static void setWindows() {
		try {
            
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } 
    catch (Exception e) {
       // handle exception
    }
    
	}
	
	public static void main(String[] args) {
		setWindows();
		loadCfg();
		if(autoUpdate) new UpdateCheckWindow();
    	mainWindow window = new mainWindow();
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	window.setVisible(true);
	}
	public static String getVersion(InputStream is) {

		try {
			NBTInputStream s = new NBTInputStream(is);
			CompoundTag rootTag = (CompoundTag) s.readTag();
			Map<String, Tag> rootMap = rootTag.getValue();
			CompoundTag levelTag = (CompoundTag) rootMap.get("Data");
			Map<String, Tag> levelMap = levelTag.getValue();
			CompoundTag versionTag = (CompoundTag) levelMap.get("Version");
			if(versionTag != null) {
			Map<String, Tag> versionMap = versionTag.getValue();
			String ver = versionMap.get("Name").getValue().toString();
			s.close();
			return ver;
			
			}else {
				DoubleTag borderTag = (DoubleTag) levelMap.get("BorderSize");
				IntTag nbtVersionTag = (IntTag) levelMap.get("version");
				if(nbtVersionTag == null) {
					s.close();
					return "Before Beta 1.3";
				}else {
					int ver = nbtVersionTag.getValue();
					s.close();
					if(ver == 19132) return "Between Beta 1.3 and v.1.2";
					if(ver == 0) return "<span style=\"color: #FF0B0B;\">Unknown</span>";
					s.close();
				}
				if(borderTag == null && nbtVersionTag != null && nbtVersionTag.getValue() == 19133)return "Between 1.2 and 1.7.x";
				else if(borderTag != null && nbtVersionTag != null) return "1.8.x";
				s.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "<span style=\"color: #FF0B0B;\">Unknown</span>";
		}
		return "<span style=\"color: #FF0B0B;\">Unknown</span>";
	}
	private static void loadCfg() {
		// TODO Auto-generated method stub
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				FirstRunWindow firstrun = new FirstRunWindow();
				firstrun.setVisible(true);
				saveConfig(firstrun.backupDir,"",true);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);

			backupDir = props.getProperty("backupDir")+"/";
			nbtexplorer = props.getProperty("NBTExplorer.exe path",null);
			autoUpdate = Boolean.parseBoolean(props.getProperty("UpdateCheck","true"));
			reader.close();
			
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}
		
	}

	public static void saveConfig(String backupDir, String nbtpath, Boolean AutoUpdate) throws IOException {
		// TODO Auto-generated method stub
		Properties props = new Properties();
		props.setProperty("backupDir", backupDir);
		props.setProperty("NBTExplorer.exe path", nbtpath);
		props.setProperty("UpdateCheck", AutoUpdate.toString());
		FileWriter writer = new FileWriter(configFile);
		props.store(writer, "WorldManager Settings");
		writer.close();
		loadCfg();
	}

	public static mainWindow startAsMod(File dir) {

		setWindows();
		loadCfg();
		if(autoUpdate) {
			ProgressManager.ProgressBar bar = ProgressManager.push("Checking for updates", 1);
			new UpdateCheckWindow();
			bar.step("Done!");
			ProgressManager.pop(bar);
		}
    	mainWindow window = new mainWindow();
    	window.worldDir = dir.getAbsolutePath();
    	window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	window.isMod = true;
    	window.setTitle(window.getTitle()+" - Loaded as Mod");
    	window.loadWorlds();
    	return window;
	}
	public static ImageIcon getIcon(String name) {
		// TODO Auto-generated method stub
		return new ImageIcon(Main.class.getResource("/de/erdbeerbaerlp/worldManager/icons/"+name));
	}
	public static String convertFileSize(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	public static long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += folderSize(file);
	    }
	    return length;
	}
	

}
