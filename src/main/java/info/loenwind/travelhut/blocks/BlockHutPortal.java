package info.loenwind.travelhut.blocks;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import info.loenwind.travelhut.config.Config;
import info.loenwind.travelhut.handlers.TeleportHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHutPortal extends Block {

  public static enum Type implements IStringSerializable {
    LEFT,
    RIGHT;

    @SuppressWarnings("null")
    @Override
    public @Nonnull String getName() {
      return name().toLowerCase(Locale.ENGLISH);
    }
  }

  public static final @Nonnull PropertyEnum<BlockHutPortal.Type> TYPE = PropertyEnum.<BlockHutPortal.Type> create("type", BlockHutPortal.Type.class);
  public static final @Nonnull PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final @Nonnull PropertyBool BEDROCK = PropertyBool.create("bedrock");

  public static BlockHutPortal create() {
    BlockHutPortal result = new BlockHutPortal("blockhutportal");
    return result;
  }

  @Nonnull
  protected final String name;

  public BlockHutPortal(@Nonnull String name) {
    super(Material.PORTAL);
    this.name = name;
    annotationDerp();
    setUnlocalizedName(name);
    setRegistryName(name);
    setHardness(-1.0F);
    setSoundType(SoundType.GLASS);
    setLightLevel(0.75F);
    if (Config.generateBedrock.getBoolean()) {
      setBlockUnbreakable();
      setResistance(6000000.0F);
    } else {
      setHardness(50.0F);
      setResistance(2000.0F);
    }
    setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockHutPortal.Type.LEFT).withProperty(FACING, EnumFacing.NORTH).withProperty(BEDROCK,
        Config.generateBedrock.getBoolean()));
  }

  @SuppressWarnings("null")
  private void annotationDerp() {
    setCreativeTab(null); // is nullable
  }

  @SuppressWarnings("null")
  @Override
  public @Nonnull IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(TYPE, (meta & 0b1000) == 0 ? BlockHutPortal.Type.LEFT : BlockHutPortal.Type.RIGHT).withProperty(FACING,
        EnumFacing.values()[meta & 0b0111]);
  }

  @Override
  public int getMetaFromState(@Nonnull IBlockState state) {
    return (state.getValue(TYPE) == BlockHutPortal.Type.RIGHT ? 0b1000 : 0) | state.getValue(FACING).ordinal();
  }

  @Override
  protected @Nonnull BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { TYPE, FACING, BEDROCK });
  }

  @Override
  public @Nonnull IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
    return state.withProperty(BEDROCK, Config.generateBedrock.getBoolean());
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Override
  public void getSubBlocks(@Nonnull CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items) {
  }

  @SideOnly(Side.CLIENT)
  @Override
  public @Nonnull BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
    return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockStateIn, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
    return getCollisionBoxList(blockStateIn).get(0);
  }

  @Override
  public void addCollisionBoxToList(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox,
      @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
    for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state)) {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
    }
  }

  private static final double px = 1D / 16D;
  protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0 * px, 0, 0 * px, 16 * px, 1D, 1 * px);
  protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0 * px, 0, 0 * px, 1 * px, 1D, 16 * px);
  protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0 * px, 0, 15 * px, 16 * px, 1D, 16 * px);
  protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(15 * px, 0, 0 * px, 16 * px, 1D, 16 * px);

  private static List<AxisAlignedBB> getCollisionBoxList(IBlockState bstate) {
    List<AxisAlignedBB> list = Lists.<AxisAlignedBB> newArrayList();

    boolean left = bstate.getValue(TYPE) == Type.LEFT;
    switch (bstate.getValue(FACING)) {
    case NORTH:
      list.add(AABB_NORTH);
      list.add(left ? AABB_WEST : AABB_EAST);
      break;
    case EAST:
      list.add(AABB_EAST);
      list.add(left ? AABB_NORTH : AABB_SOUTH);
      break;
    case SOUTH:
      list.add(AABB_SOUTH);
      list.add(left ? AABB_EAST : AABB_WEST);
      break;
    case WEST:
      list.add(AABB_WEST);
      list.add(left ? AABB_SOUTH : AABB_NORTH);
      break;
    default:
      break;
    }

    return list;
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  @Nullable
  public RayTraceResult collisionRayTrace(@Nonnull IBlockState blockStateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Vec3d start,
      @Nonnull Vec3d end) {
    List<RayTraceResult> list = Lists.<RayTraceResult> newArrayList();

    for (AxisAlignedBB axisalignedbb : getCollisionBoxList(blockStateIn)) {
      if (axisalignedbb != null) {
        list.add(this.rayTrace(pos, start, end, axisalignedbb));
      }
    }

    RayTraceResult raytraceresult1 = null;
    double d1 = 0.0D;

    for (RayTraceResult raytraceresult : list) {
      if (raytraceresult != null) {
        double d0 = raytraceresult.hitVec.squareDistanceTo(end);

        if (d0 > d1) {
          raytraceresult1 = raytraceresult;
          d1 = d0;
        }
      }
    }

    return raytraceresult1;
  }

  @Override
  public int quantityDropped(@Nonnull Random random) {
    return 0;
  }

  @Override
  public void onEntityCollidedWithBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entityIn) {
    if (!worldIn.isRemote && !entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn instanceof EntityPlayerMP
        && new AxisAlignedBB(pos).shrink(2d / 16d).intersects(entityIn.getEntityBoundingBox())) {
      TeleportHandler.teleport(worldIn, pos, (EntityPlayerMP) entityIn);
    }
  }

  @Override
  public @Nonnull ItemStack getItem(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(@Nonnull IBlockState state, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
    return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : super.shouldSideBeRendered(state, blockAccess, pos, side);
  }

}
