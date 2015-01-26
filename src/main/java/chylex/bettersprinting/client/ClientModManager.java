package chylex.bettersprinting.client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public final class ClientModManager{
	public static KeyBinding keyBindSprintHold = new KeyBinding("Sprint (hold)",29,"key.categories.movement");
    public static KeyBinding keyBindSprintToggle = new KeyBinding("Sprint (toggle)",34,"key.categories.movement");
    public static KeyBinding keyBindSneakToggle = new KeyBinding("Sneak (toggle)",21,"key.categories.movement");
    public static KeyBinding keyBindSprintMenu = new KeyBinding("Sprint menu",24,"key.categories.movement");
    
	public static boolean svSurvivalFlyingBoost = false, svRunInAllDirs = false, svDisableMod = false;
    
    public static boolean inMenu(Minecraft mc){
    	return mc.thePlayer == null || mc.theWorld == null;
    }
    
    public static boolean canRunInAllDirs(Minecraft mc){
    	return !ClientSettings.disableMod && (inMenu(mc) || mc.isSingleplayer() || svRunInAllDirs);
    }
    
    public static boolean canBoostFlying(Minecraft mc){
    	return !ClientSettings.disableMod && (inMenu(mc) || mc.isSingleplayer() || mc.thePlayer.capabilities.isCreativeMode || svSurvivalFlyingBoost);
    }
    
    public static boolean isModDisabled(){
    	return ClientSettings.disableMod || svDisableMod;
    }
}
