package info.loenwind.travelhut;

import org.apache.logging.log4j.Logger;

import info.loenwind.travelhut.blocks.BlockHutPortal;
import info.loenwind.travelhut.commands.RegenCommand;
import info.loenwind.travelhut.config.ConfigHandler;
import info.loenwind.travelhut.handlers.WorldGenHandler;
import info.loenwind.travelhut.proxies.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = TravelHutMod.MODID, version = TravelHutMod.VERSION, name = TravelHutMod.MODID, guiFactory = "info.loenwind.travelhut.config.gui.ConfigFactory")
public class TravelHutMod {

  public static final String MODID = "travelhut";
  public static final String VERSION = "1.0.0";

  @SidedProxy(clientSide = "info.loenwind.travelhut.proxies.ClientProxy", serverSide = "info.loenwind.travelhut.proxies.CommonProxy")
  public static CommonProxy PROXY;

  public static Logger LOG;

  public static SimpleNetworkWrapper NETWORK;

  public static ConfigHandler CONFIGHANDLER;

  public static BlockHutPortal blockHutPortal;

  @EventHandler
  public void preinit(FMLPreInitializationEvent event) {
    LOG = event.getModLog();
    NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(TravelHutMod.MODID);
    CONFIGHANDLER = new ConfigHandler(MODID, LOG, NETWORK);
    CONFIGHANDLER.init(event);
    PROXY.init(event);
    blockHutPortal = BlockHutPortal.create();
    WorldGenHandler.create();
  }

  @EventHandler
  public void onServerStart(FMLServerStartingEvent event) {
    event.registerServerCommand(new RegenCommand());
  }

}
