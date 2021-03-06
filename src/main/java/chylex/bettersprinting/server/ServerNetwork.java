package chylex.bettersprinting.server;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import chylex.bettersprinting.system.PacketPipeline;
import chylex.bettersprinting.system.PacketPipeline.INetworkHandler;

public class ServerNetwork implements INetworkHandler{
	/*
	 * INCOMING PACKETS
	 * ================
	 * Payload packet on "BSprint" channel
	 * * byte 4 - old protocol, no custom functionality
	 * * byte 5 - new old protocol, if you updated your handling code then ignore people who send this one and wait for BSM packet
	 * 
	 * Payload packet on "BSM" channel
	 * * byte 0, byte X - new protocol, X is the protocol version that can be used to determine the useable functionality (latest is 10)
	 * 
	 * OUTCOMING PACKETS
	 * =================
	 * Payload packet on "BSM" channel
	 * * byte 0, boolean enableSurvivalFlyBoost, boolean enableAllDirs - custom settings, both are false by default [since 10]
	 * * byte 1 - disables the mod on client-side [since 10]
	 * * byte 2 - re-enables the mod in client-side, called from command [since 10]
	 * 
	 * ADDITIONAL INFO
	 * ===============
	 * Since this server mod can only run the BSM channel since it doesn't support older versions of MC, so if you made your server
	 * accept clients from older versions of MC which only support the old protocol, you will need to handle that yourself. Same
	 * if you don't have a Forge server at all. All details about the workings should be comprehensible, if you have any questions,
	 * feel free to contact me. There is also a diagram below, because I was bored:
	 * 
	 * SIMPLE DIAGRAM TO DISABLE THE MOD
	 * =================================
	 * client joins server
	 *   - payload on BSprint holding 1 byte of value 4
	 *     - kick the player and tell him to disable the mod in the config
	 * 
	 * client joins server
	 *   - payload on BSprint holding 1 byte of value 5
	 *     - ignore and wait
	 *   - payload on BSM holding 2 bytes of values 0, 10
	 *     - protocol 10 supports deactivation
	 *     - send a payload on BSM with 1 byte of value 1
	 *       - mod is automatically disabled and the client is notified about it
	 * 
	 * Since all versions send a packet on BSprint channel, existing solutions for older versions are not broken by the change.
	 *
	 * OLDER VERSION DETAILS
	 * =====================
	 * Old Forge versions of the mod do not send a BSprint packet, but the default FML mod list packet which can be rejected
	 * if you want to disable the mod and the mod list contains 'bettersprinting' mod ID.
	 */
	
	public static PacketBuffer writeSettings(boolean enableSurvivalFlyBoost, boolean enableAllDirs){
		PacketBuffer buffer = PacketPipeline.buf();
		buffer.writeByte(0).writeBoolean(enableSurvivalFlyBoost).writeBoolean(enableAllDirs);
		return buffer;
	}
	
	public static PacketBuffer writeDisableMod(boolean disable){
		PacketBuffer buffer = PacketPipeline.buf();
		buffer.writeByte(disable ? 1 : 2);
		return buffer;
	}
	
	@Override
	public void onPacket(Side side, ByteBuf data, EntityPlayer player){
		byte type = data.readByte();
		
		if (type == 0){
			// might be useful later; maybe keep a list of players with the mod installed
			// setting packet is sent in the login event
		}
	}
}
