package de.erdbeerbaerlp.worldManager;

import java.awt.Component;
import java.awt.Image;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class BackupRenderer extends JLabel implements ListCellRenderer<Backup> { 

    @Override 
    public Component getListCellRendererComponent(JList<? extends Backup> list, Backup backup, int index, 
        boolean isSelected, boolean cellHasFocus) { 
        Image imageIcon = backup.getWorldIcon().getImage();
        
        setIcon(new ImageIcon(imageIcon.getScaledInstance(64,64,Image.SCALE_SMOOTH))); 
        
        String forgeString;
        if(backup.isForgePossible()) forgeString = " | <b>Forge?</b> "+backup.isModded();
        else forgeString = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
        setText("<html><font size=4>"+backup.getWorldName()+"<br>"+"<b>Saved</b>: "+sdf.format(backup.getPath().lastModified())+"<br><b>Backup size</b>: "+backup.getDiskUsage()+forgeString+"<br><b>Version</b>: "+backup.getMcVersion()+"</html>");
        setOpaque(true); 
        if (isSelected) {
            setBackground(list.getSelectionBackground()); 
            setForeground(list.getSelectionForeground()); 
        } else { 
            setBackground(list.getBackground()); 
            setForeground(list.getForeground()); 
        }
        return this; 
    } 
    
}
