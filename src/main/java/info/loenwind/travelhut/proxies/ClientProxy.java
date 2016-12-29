package info.loenwind.travelhut.proxies;

import info.loenwind.travelhut.render.PortalGlassBlockColor;
import info.loenwind.travelhut.render.PortalGlassStateMapper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

  @Override
  public void init(FMLPreInitializationEvent event) {
    super.init(event);
    PortalGlassStateMapper.create();
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
    PortalGlassBlockColor.create();
  }

}
