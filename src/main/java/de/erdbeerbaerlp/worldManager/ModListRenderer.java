package de.erdbeerbaerlp.worldManager;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ModListRenderer extends JLabel implements ListCellRenderer<Mod> { 

    @Override 
    public Component getListCellRendererComponent(JList<? extends Mod> list, Mod mod, int index, boolean isSelected, boolean cellHasFocus) { 
			setIcon(mod.icon);
//			System.out.println(mod.icon.getIconHeight()+": "+mod.icon.getIconWidth());
			if(mod.complete) setText("<html><font size=3><b>Name</b>: "+mod.name+"<br>"+"<b>ModID: </b>"+mod.modid+"<br>"+"<b>Version</b>: "+mod.version+" | <b>Owner: </b>"+mod.owner+" <br>"+mod.shortDescription+"&nbsp;</html>");
			else setText("<html><font size=3><b>Mod ID</b>: "+mod.modid+"<br>"+"<b>Version</b>: "+mod.version+" <br>&nbsp;</html>");
		
    	
    	setOpaque(true); 
        if (isSelected) {
            setBackground(list.getSelectionBackground()); 
            setForeground(list.getSelectionForeground()); 
        } else { 
            setBackground(list.getBackground()); 
            setForeground(list.getForeground()); 
        }
        if(mod.exclude) return new JLabel("");
        else return this; 
    } 
    
}
