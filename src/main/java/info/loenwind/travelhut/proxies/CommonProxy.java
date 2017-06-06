package info.loenwind.travelhut.proxies;

import info.loenwind.travelhut.handlers.TickHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

  public void init(FMLPreInitializationEvent event) {
  }

  public void init(FMLInitializationEvent event) {
    TickHandler.create();
  }

}
