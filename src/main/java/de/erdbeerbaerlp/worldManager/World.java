package de.erdbeerbaerlp.worldManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.swing.ImageIcon;

import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.LongTag;
import org.jnbt.NBTInputStream;
import org.jnbt.StringTag;
import org.jnbt.Tag;


public class World {
	private File worldPath;
	private ImageIcon worldIcon;
	private File levelDat;
	private String worldName;
	private String diskUsage;
	private String mcVersion;
	private String modded;
	private String lastPlayed;
	private boolean forgePossible = true;
	private String lvlDatName;
	private boolean moddedB;
	private boolean isOld = false;
public World(File path) {
	this.levelDat = new File(path.getAbsolutePath()+"/level.dat");
	try {
		this.mcVersion = Main.getVersion(new FileInputStream(levelDat));
		if(this.mcVersion.equals("Before Beta 1.3") || this.mcVersion.equals("Between Beta 1.3 and V.1.2")) {
			this.forgePossible = false;
			this.moddedB = false;
		}
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		this.mcVersion = "<span style=\"color: #FF0B0B;\">Unknown</span>";
	}
	NBTInputStream s;
	CompoundTag rootTag;

		try {
			s = new NBTInputStream(new FileInputStream(levelDat));
			rootTag = (CompoundTag) s.readTag();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			rootTag = null;
			s = null;
		}

	Map<String, Tag> rootMap = rootTag.getValue();
	CompoundTag levelTag = (CompoundTag) rootMap.get("Data");
	Map<String, Tag> levelMap = levelTag.getValue();
	
		StringTag nameTag = (StringTag) levelMap.get("LevelName");
		if(nameTag != null) {
		this.worldName = ColorFormatter.format(nameTag.getValue(),false);
		this.lvlDatName = nameTag.getValue();
		}else {
			this.isOld = true;
			this.worldName = path.getName();
			this.lvlDatName = path.getName();
		}
	this.worldPath = path;
	
	ImageIcon out;
	if((new File(path.getAbsolutePath()+"/icon.png")).exists()) {
		
		try {
			out = new ImageIcon(path.getAbsolutePath()+"/icon.png");
		} catch (Exception e) {
			out = Main.getIcon("default.png");
		}
	}else {
		out = Main.getIcon("default.png");
	}
	this.worldIcon = out;
	this.diskUsage = Main.convertFileSize(Main.folderSize(path),true);

		LongTag timeTag = (LongTag) levelMap.get("LastPlayed");
		if(timeTag != null) {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
			Long time = timeTag.getValue();
			this.lastPlayed = sdf.format(time);
			}else {
				this.lastPlayed = "<span style=\"color: #FF0B0B;\">Unknown</span>";
			}
		CompoundTag fmlTag = (CompoundTag) rootMap.get("FML");
		CompoundTag fmlTag2 = (CompoundTag) rootMap.get("fml");
		
		if(fmlTag == null && fmlTag2 == null) {
			this.modded = "No";
			this.moddedB = false;
		}
		else {
			this.modded = "Yes";
			Map<String, Tag> fmlMap =  fmlTag==null?fmlTag2.getValue():fmlTag.getValue();
			ListTag modList = fmlTag==null?(ListTag)fmlMap.get("LoadingModList"):(ListTag)fmlMap.get("ModList");
			
			if(modList == null || modList.getValue().isEmpty())
				this.moddedB = false;
			else
				this.moddedB = true;
		}
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
}
public File getWorldPath() {
	return worldPath;
}
public ImageIcon getWorldIcon() {
	return worldIcon;
}
public String getWorldName() {
	return worldName;
}
public File getLevelDat() {
	return levelDat;
}
public String getDiskUsage() {
	return diskUsage;
}
public String getMcVersion() {
	return mcVersion;
}
public String isModded() {
	return modded;
}
public boolean isModdedB() {
	return moddedB;
}
public String getLastPlayed() {
	return lastPlayed;
}
public boolean isForgePossible() {
	return forgePossible;
}
public String getRawName() {
	return lvlDatName;
}
public boolean isOld() {
	return isOld;
}
}
