package de.erdbeerbaerlp.worldManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.ImageIcon;

import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.StringTag;
import org.jnbt.Tag;
import org.zeroturnaround.zip.ZipUtil;


public class Backup {
	private File backupPath;
	private ImageIcon backupIcon;
	private String backupName;
	private String diskUsage;
	private String modded;
	private String mcVersion;
	private boolean forgePossible = true;
	private String fileName;
public Backup(File path){
	String[] name = path.getName().split("~");
	String nameOut = "";
	for(int i=0;i<name.length-1;i++) {
		nameOut = nameOut+ (nameOut.isEmpty() ? "": "~")+name[i];
	}
	if(nameOut.isEmpty()) nameOut = path.getName().replace(".zip", "");
	this.fileName = nameOut;
	this.backupPath = path;
	ImageIcon out;
	try {
		byte[] img = ZipUtil.unpackEntry(path, "icon.png");
		out = new ImageIcon(img);
	}catch(Exception e) {
		out = Main.getIcon("default.png");
	}
	this.backupIcon = out;
	this.diskUsage = Main.convertFileSize(path.length(),true);
	byte[] levelDat = ZipUtil.unpackEntry(path, "level.dat");
	this.mcVersion = Main.getVersion(new ByteArrayInputStream(levelDat));
	if(this.mcVersion.equals("Before Beta 1.3") || this.mcVersion.equals("Between Beta 1.3 and V.1.2")) this.forgePossible = false;
		NBTInputStream s;
		CompoundTag rootTag;
		try {
			s = new NBTInputStream(new ByteArrayInputStream(levelDat));
			rootTag = (CompoundTag) s.readTag();
		} catch (IOException e) {
			s = null;
			rootTag = null;
		}
		Map<String, Tag> rootMap = rootTag.getValue();
		CompoundTag levelTag = (CompoundTag) rootMap.get("Data");
		Map<String, Tag> levelMap = levelTag.getValue();
		CompoundTag fmlTag = (CompoundTag) rootMap.get("FML");
		if(fmlTag == null) this.modded = "No";
		else this.modded = "Yes";

		 
		StringTag nameTag = (StringTag) levelMap.get("LevelName");
		if(nameTag != null) {
		this.backupName = ColorFormatter.format(nameTag.getValue(),false);
		}else {
			this.backupName = nameOut;
		}
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
public File getPath() {
	return backupPath;
}
public ImageIcon getWorldIcon() {
	return backupIcon;
}
public String getWorldName() {
	return backupName;
}
public ImageIcon getBackupIcon() {
	return backupIcon;
}
public String getDiskUsage() {
	return diskUsage;
}
public String getMcVersion() {
	return mcVersion;
}
public String getFileName() {
	return fileName;
}
public String isModded() {
	// TODO Auto-generated method stub
	return modded;
}
public boolean isForgePossible() {
	return forgePossible;
}
}
