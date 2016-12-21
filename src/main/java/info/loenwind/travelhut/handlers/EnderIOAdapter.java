package info.loenwind.travelhut.handlers;

import java.util.Random;

import crazypants.enderio.material.fusedQuartz.FusedQuartzType;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.common.Loader;

public class EnderIOAdapter {

  public static IBlockState getEIOGlass(Random rand) {
    return FusedQuartzType.ENLIGHTENED_FUSED_GLASS.getBlock().getDefaultState().withProperty(BlockColored.COLOR,
        EnumDyeColor.values()[rand.nextInt(EnumDyeColor.values().length)]);
  }

  public static IBlockState getGlass(Random rand) {
    if (Loader.isModLoaded("EnderIO")) {
      return getEIOGlass(rand);
    }
    return Blocks.GLASS.getDefaultState();
  }

}
