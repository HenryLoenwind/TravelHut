package info.loenwind.travelhut.render;

import javax.annotation.Nonnull;

import info.loenwind.travelhut.TravelHutMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;

public class PortalGlassStateMapper extends StateMapperBase {

  @SuppressWarnings("null")
  public static void create() {
    PortalGlassStateMapper mapper = new PortalGlassStateMapper();
    ModelLoader.setCustomStateMapper(TravelHutMod.blockHutPortalGlass, mapper);
  }

  @Override
  protected @Nonnull ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
    return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), "normal");
  }

}
