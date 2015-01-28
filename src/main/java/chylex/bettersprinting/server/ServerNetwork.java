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
	 */
	
	public static PacketBuffer writeSettings(boolean enableSurvivalFlyBoost, boolean enableAllDirs){
		PacketBuffer buffer = PacketPipeline.buf();
		buffer.writeByte(0).writeBoolean(enableSurvivalFlyBoost).writeBoolean(enableAllDirs);
		return buffer;
	}
	
	public static PacketBuffer writeDisableMod(){
		PacketBuffer buffer = PacketPipeline.buf();
		buffer.writeByte(1);
		return buffer;
	}
	
	@Override
	public void onPacket(Side side, ByteBuf data, EntityPlayer player){
		byte type = data.readByte();
		
		if (type == 0){
			// unused: int protocol = data.readByte();
			
			if (ServerSettings.disableClientMod){
				PacketPipeline.sendToPlayer(writeDisableMod(),player);
			}
			else if (ServerSettings.enableSurvivalFlyBoost || ServerSettings.enableAllDirs){
				PacketPipeline.sendToPlayer(writeSettings(ServerSettings.enableSurvivalFlyBoost,ServerSettings.enableAllDirs),player);
			}
		}
	}
}