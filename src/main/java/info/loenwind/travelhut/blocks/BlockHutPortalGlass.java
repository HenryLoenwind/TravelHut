package info.loenwind.travelhut.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import info.loenwind.travelhut.config.Config;

public class BlockHutPortalGlass extends Block {

  public static BlockHutPortalGlass create() {
    BlockHutPortalGlass result = new BlockHutPortalGlass("blockhutportalglass");
    GameRegistry.register(result);
    return result;
  }

  @Nonnull
  protected final String name;

  @SuppressWarnings("null")
  public BlockHutPortalGlass(@Nonnull String name) {
    super(Material.GLASS);
    this.name = name;
    setSoundType(SoundType.GLASS);
    annotationDerp();
    setUnlocalizedName(name);
    setRegistryName(name);
    setLightLevel(1F);
    setDefaultState(this.blockState.getBaseState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
  }

  @SuppressWarnings("null")
  private void annotationDerp() {
    setCreativeTab(null); // is nullable
  }

  @Override
  public @Nonnull IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(meta));
  }

  @Override
  public int getMetaFromState(@Nonnull IBlockState state) {
    return state.getValue(BlockColored.COLOR).getMetadata();
  }

  @Override
  protected @Nonnull BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { BlockColored.COLOR });
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public void getSubBlocks(@Nonnull Item itemIn, @Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public @Nonnull BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  protected boolean canSilkHarvest() {
    return false;
  }

  @Override
  public @Nonnull ItemStack getItem(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
    return ItemStack.EMPTY;
  }

  @Override
  public int quantityDropped(@Nonnull Random random) {
    return 0;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(@Nonnull IBlockState blockStateIn, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
    if (this == blockAccess.getBlockState(pos.offset(side)).getBlock()) {
      return false;
    }
    return super.shouldSideBeRendered(blockStateIn, blockAccess, pos, side);
  }
  @Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_)
	{
    if (!Config.canPassThroughGlass.getBoolean())
    {
      super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
    }
    else
    {
		  AxisAlignedBB blockBox = state.getCollisionBoundingBox(worldIn, pos);
		  AxisAlignedBB axisalignedbb = blockBox.offset(pos);

		  if (axisalignedbb != null && entityBox.intersectsWith(axisalignedbb) && entityIn != null && !(entityIn instanceof EntityPlayer))
		  {
			  collidingBoxes.add(axisalignedbb);
		  }
    }
	}
}
