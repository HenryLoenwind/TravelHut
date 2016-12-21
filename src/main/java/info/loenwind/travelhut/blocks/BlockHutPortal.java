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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHutPortal extends Block {

  public static enum Type implements IStringSerializable {
    LEFT,
    RIGHT;

    @Override
    public String getName() {
      return name().toLowerCase(Locale.ENGLISH);
    }
  }

  public static final PropertyEnum<BlockHutPortal.Type> TYPE = PropertyEnum.<BlockHutPortal.Type> create("type", BlockHutPortal.Type.class);
  public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final PropertyBool BEDROCK = PropertyBool.create("bedrock");

  public static BlockHutPortal create() {
    BlockHutPortal result = new BlockHutPortal("blockHutPortal");
    GameRegistry.register(result);
    return result;
  }

  @Nonnull
  protected final String name;

  public BlockHutPortal(@Nonnull String name) {
    super(Material.PORTAL);
    this.name = name;
    setCreativeTab(null);
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

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(TYPE, (meta & 0b1000) == 0 ? BlockHutPortal.Type.LEFT : BlockHutPortal.Type.RIGHT).withProperty(FACING,
        EnumFacing.values()[meta & 0b0111]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return (state.getValue(TYPE) == BlockHutPortal.Type.RIGHT ? 0b1000 : 0) | state.getValue(FACING).ordinal();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { TYPE, FACING, BEDROCK });
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty(BEDROCK, Config.generateBedrock.getBoolean());
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
  }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockStateIn, World worldIn, BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
      @Nullable Entity entityIn) {
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
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  @Nullable
  public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
    List<RayTraceResult> list = Lists.<RayTraceResult> newArrayList();

    for (AxisAlignedBB axisalignedbb : getCollisionBoxList(blockState)) {
      list.add(this.rayTrace(pos, start, end, axisalignedbb));
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
  public int quantityDropped(Random random) {
    return 0;
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote && !entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn instanceof EntityPlayerMP && entityIn.timeUntilPortal == 0
        && new AxisAlignedBB(pos).contract(2d / 16d).intersectsWith(entityIn.getEntityBoundingBox())) {
      TeleportHandler.teleport(worldIn, pos, (EntityPlayerMP) entityIn);
    }
  }

  @Override
  @Nullable
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return null;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : super.shouldSideBeRendered(state, blockAccess, pos, side);
  }

}
