package de.erdbeerbaerlp.worldManager.mod;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
public class EventHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(ClientTickEvent event)
    {
        if (Keyboard.isKeyDown(McMod.keyB.getKeyCode())) 
        {
        	if(!McMod.win.isVisible()) {
        		McMod.win.setVisible(true);
        		McMod.win.requestFocus();
        	}
        	if(McMod.win.isVisible()) McMod.win.requestFocus();
        }
    }

}
