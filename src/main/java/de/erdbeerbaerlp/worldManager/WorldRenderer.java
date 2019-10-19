package de.erdbeerbaerlp.worldManager;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class WorldRenderer extends JLabel implements ListCellRenderer<World> { 

    @Override 
    public Component getListCellRendererComponent(JList<? extends World> list, World world, int index, 
        boolean isSelected, boolean cellHasFocus) { 
        Image imageIcon = world.getWorldIcon().getImage();
        
        setIcon(new ImageIcon(imageIcon.getScaledInstance(64,64,Image.SCALE_SMOOTH))); 
        
        String forgeString;
        if(world.isForgePossible()) forgeString = " | <b>Forge?</b> "+world.isModded();
        else forgeString ="";
        setText("<html><font size=4>"+world.getWorldName()+"<br><b>Last Played:</b> "+world.getLastPlayed()+"<br><b>Total size</b>: "+world.getDiskUsage()+forgeString+"<br><b>Version</b>: "+world.getMcVersion()+"</html>");
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
