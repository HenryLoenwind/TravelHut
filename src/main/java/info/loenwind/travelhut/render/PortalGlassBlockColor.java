package info.loenwind.travelhut.render;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.travelhut.TravelHutMod;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = TravelHutMod.MODID, value = Side.CLIENT)
public class PortalGlassBlockColor implements IBlockColor {

  @SubscribeEvent
  public static void create(ColorHandlerEvent.Block event) {
    event.getBlockColors().registerBlockColorHandler(new PortalGlassBlockColor(), TravelHutMod.blockHutPortalGlass);
  }

  @Override
  public int colorMultiplier(@Nonnull IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
    return MapColor.getBlockColor(state.getValue(BlockColored.COLOR)).colorValue;
  }

}
