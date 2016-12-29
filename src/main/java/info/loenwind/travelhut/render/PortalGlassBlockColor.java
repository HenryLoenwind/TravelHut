package info.loenwind.travelhut.render;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.travelhut.TravelHutMod;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PortalGlassBlockColor implements IBlockColor {

  public static void create() {
    Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new PortalGlassBlockColor(), TravelHutMod.blockHutPortalGlass);
  }

  @Override
  public int colorMultiplier(@Nonnull IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
    return state.getValue(BlockColored.COLOR).getMapColor().colorValue;
  }

}
