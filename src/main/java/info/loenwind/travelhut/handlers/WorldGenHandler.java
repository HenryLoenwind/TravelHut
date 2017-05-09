package info.loenwind.travelhut.handlers;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.travelhut.TravelHutMod;
import info.loenwind.travelhut.blocks.BlockHutPortal;
import info.loenwind.travelhut.config.Config;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldGenHandler {

  private static IBlockState sandstone, bedrock, slabs, carpet0, carpet1, carpet2, carpet3, carpet4, sand, lowslabs, obsidian, portalLN, portalLE, portalLS,
      portalLW, portalRN, portalRE, portalRS, portalRW, dirt, sapling;

  public static void create() {
    sandstone = Blocks.SANDSTONE.getDefaultState();
    bedrock = Blocks.BEDROCK.getDefaultState();
    slabs = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF,
        BlockSlab.EnumBlockHalf.TOP);
    lowslabs = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF,
        BlockSlab.EnumBlockHalf.BOTTOM);
    sand = Blocks.SAND.getDefaultState();
    obsidian = Blocks.OBSIDIAN.getDefaultState();

    portalLN = TravelHutMod.blockHutPortal.getDefaultState().withProperty(BlockHutPortal.TYPE, BlockHutPortal.Type.LEFT).withProperty(BlockHutPortal.FACING,
        EnumFacing.NORTH);
    portalLE = TravelHutMod.blockHutPortal.getDefaultState().withProperty(BlockHutPortal.TYPE, BlockHutPortal.Type.LEFT).withProperty(BlockHutPortal.FACING,
        EnumFacing.EAST);
    portalLS = TravelHutMod.blockHutPortal.getDefaultState().withProperty(BlockHutPortal.TYPE, BlockHutPortal.Type.LEFT).withProperty(BlockHutPortal.FACING,
        EnumFacing.SOUTH);
    portalLW = TravelHutMod.blockHutPortal.getDefaultState().withProperty(BlockHutPortal.TYPE, BlockHutPortal.Type.LEFT).withProperty(BlockHutPortal.FACING,
        EnumFacing.WEST);
    portalRN = TravelHutMod.blockHutPortal.getDefaultState().withProperty(BlockHutPortal.TYPE, BlockHutPortal.Type.RIGHT).withProperty(BlockHutPortal.FACING,
        EnumFacing.NORTH);
    portalRE = TravelHutMod.blockHutPortal.getDefaultState().withProperty(BlockHutPortal.TYPE, BlockHutPortal.Type.RIGHT).withProperty(BlockHutPortal.FACING,
        EnumFacing.EAST);
    portalRS = TravelHutMod.blockHutPortal.getDefaultState().withProperty(BlockHutPortal.TYPE, BlockHutPortal.Type.RIGHT).withProperty(BlockHutPortal.FACING,
        EnumFacing.SOUTH);
    portalRW = TravelHutMod.blockHutPortal.getDefaultState().withProperty(BlockHutPortal.TYPE, BlockHutPortal.Type.RIGHT).withProperty(BlockHutPortal.FACING,
        EnumFacing.WEST);

    carpet0 = Blocks.CARPET.getDefaultState().withProperty(net.minecraft.block.BlockCarpet.COLOR, EnumDyeColor.SILVER);
    carpet1 = Blocks.CARPET.getDefaultState().withProperty(net.minecraft.block.BlockCarpet.COLOR, EnumDyeColor.BLUE);
    carpet2 = Blocks.CARPET.getDefaultState().withProperty(net.minecraft.block.BlockCarpet.COLOR, EnumDyeColor.RED);
    carpet3 = Blocks.CARPET.getDefaultState().withProperty(net.minecraft.block.BlockCarpet.COLOR, EnumDyeColor.YELLOW);
    carpet4 = Blocks.CARPET.getDefaultState().withProperty(net.minecraft.block.BlockCarpet.COLOR, EnumDyeColor.GREEN);

    dirt = Blocks.DIRT.getDefaultState();

    sapling = Blocks.SAPLING.getDefaultState();

    MinecraftForge.EVENT_BUS.register(WorldGenHandler.class);
  }

  public static @Nonnull IBlockState[] mkBlockStates(IBlockState glass) {
    return new IBlockState[] { null, glass, null, carpet0, carpet1, carpet2, carpet3, carpet4, sandstone,
        (Config.generateBedrock.getBoolean() ? bedrock : obsidian), slabs, sand, lowslabs,
        // 13
        portalLN, portalLE, portalLS, portalLW, portalRN, portalRE, portalRS, portalRW,
        // 21
        dirt, sapling };
  }

  // [y][z][x]
  private static final @Nonnull int[][][] data = { { //
      { 1, 1, 1, 1, 1, 1 }, // 1 glass
      { 1, 1, 1, 1, 1, 1 }, //
      { 1, 1, 1, 1, 1, 1 }, //
      { 1, 1, 1, 1, 1, 1 }, //
      { 1, 1, 1, 1, 1, 1 }, //
      { 1, 1, 1, 1, 1, 1 }//
      },
      { //
          { 1, 1, 1, 1, 1, 1 }, //
          { 1, 0, 0, 0, 0, 1 }, // 0 air
          { 1, 0, 0, 0, 0, 1 }, //
          { 1, 0, 0, 0, 0, 1 }, //
          { 1, 0, 0, 0, 0, 1 }, //
          { 1, 1, 1, 1, 1, 1 }//
      }, { //
          { 1, 1, 13, 17, 1, 1 }, // 13+ portal
          { 1, 0, 0, 0, 0, 1 }, //
          { 20, 0, 0, 0, 0, 14 }, //
          { 16, 0, 0, 0, 0, 18 }, //
          { 1, 0, 0, 0, 0, 1 }, //
          { 1, 1, 19, 15, 1, 1 }//
      }, { //
          { 1, 1, 13, 17, 1, 1 }, //
          { 1, 0, 0, 0, 0, 1 }, //
          { 20, 0, 0, 0, 0, 14 }, //
          { 16, 0, 0, 0, 0, 18 }, //
          { 1, 0, 0, 0, 0, 1 }, //
          { 1, 1, 19, 15, 1, 1 }//
      }, { //
          { 1, 1, 13, 17, 1, 1 }, //
          { 1, 3, 4, 4, 3, 1 }, // 3-7 carpet
          { 20, 7, 3, 3, 5, 14 }, //
          { 16, 7, 3, 3, 5, 18 }, //
          { 1, 3, 6, 6, 3, 1 }, //
          { 1, 1, 19, 15, 1, 1 }//
      }, { //
          { 12, 8, 9, 9, 8, 12 }, // 8 sandstone
          { 8, 8, 9, 9, 8, 8 }, // 9 bedrock
          { 9, 9, 9, 9, 9, 9 }, // 12 low slabs
          { 9, 9, 9, 9, 9, 9 }, //
          { 8, 8, 9, 9, 8, 8 }, //
          { 12, 8, 9, 9, 8, 12 }//
      }, { //
          { 10, 10, 10, 10, 10, 10 }, // 10 slabs
          { 10, 11, 11, 11, 11, 10 }, // 11 sand
          { 10, 11, 11, 11, 11, 10 }, //
          { 10, 11, 11, 11, 11, 10 }, //
          { 10, 11, 11, 11, 11, 10 }, //
          { 10, 10, 10, 10, 10, 10 }//
      }, { //
          { -1, -1, -1, -1, -1, -1 }, // -1 ignore
          { -1, 11, 11, 11, 11, -1 }, //
          { -1, 11, 11, 11, 11, -1 }, //
          { -1, 11, 11, 11, 11, -1 }, //
          { -1, 11, 11, 11, 11, -1 }, //
          { -1, -1, -1, -1, -1, -1 }//
      }//
  };

  private static final @Nonnull int[][][] tree = { { //
      { -1, -1, -1, -1, -1, -1 }, //
      { -1, -1, -1, -1, -1, -1 }, //
      { -1, -1, -1, -1, -1, -1 }, //
      { -1, -1, -1, -1, -1, -1 }, //
      { -1, -1, -1, -1, -1, -1 }, //
      { -1, -1, -1, -1, -1, -1 }//
      },
      { //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }//
      }, { //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }//
      }, { //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }//
      }, { //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }//
      }, { //
          { -1, -1, -1, -1, -1, -1 }, // 22 sapling
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, 22, 22, -1, -1 }, //
          { -1, -1, 22, 22, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }//
      }, { //
          { -1, -1, -1, -1, -1, -1 }, // 21 dirt
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, 21, 21, -1, -1 }, //
          { -1, -1, 21, 21, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }, //
          { -1, -1, -1, -1, -1, -1 }//
      } };

  private static int lastChunkX, lastChunkZ, lastStartY;

  @SubscribeEvent
  public static void pre(PopulateChunkEvent event) {
    final @Nullable World world0 = event.getWorld();
    if (world0 == null) {
      throw new NullPointerException("PopulateChunkEvent.getWorld()");
    }
    final @Nonnull World world = world0;
    final int chunkX = event.getChunkX();
    final int chunkZ = event.getChunkZ();

    if (!isValidSpawnLocation(world, chunkX, chunkZ)) {
      return;
    }

    Random rand = makeChunkRand(world, chunkX, chunkZ);

    if (event instanceof PopulateChunkEvent.Post && Config.generateTree.getBoolean()) {
      randomizeSapling(rand);
    }

    @Nonnull
    IBlockState[] states = mkBlockStates(getGlass(rand));

    BlockPos startpos;
    if (event instanceof PopulateChunkEvent.Pre) {
      startpos = findInitialSpawnLocation(world, chunkX, chunkZ, rand);
      lastChunkX = chunkX;
      lastChunkZ = chunkZ;
      lastStartY = startpos.getY();
    } else if ((event instanceof PopulateChunkEvent.Post) && lastChunkX == chunkX && lastChunkZ == chunkZ) {
      int i = chunkX * 16 + 8;
      int j = chunkZ * 16 + 8;
      startpos = new BlockPos(i, lastStartY, j);
    } else {
      return;
    }

    placeHut(world, startpos, data, states, rand);
    if (event instanceof PopulateChunkEvent.Post && Config.generateTree.getBoolean()) {
      placeHut(world, startpos.up(), tree, states, rand);
    }
  }

  @SuppressWarnings("null")
  public static IBlockState getGlass(@Nonnull Random rand) {
    return info.loenwind.travelhut.TravelHutMod.blockHutPortalGlass.getDefaultState().withProperty(BlockColored.COLOR,
        EnumDyeColor.values()[rand.nextInt(EnumDyeColor.values().length)]);
  }

  private static void randomizeSapling(@Nonnull Random rand) {
    sapling = sapling.withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.values()[rand.nextInt(BlockPlanks.EnumType.values().length)]);
  }

  public static void placeHut(final @Nonnull World world, @Nonnull BlockPos startpos, @Nonnull IBlockState[] states, @Nonnull Random rand) {
    placeHut(world, startpos, data, states, rand);
  }

  public static void placeHut(final @Nonnull World world, @Nonnull BlockPos startpos, @Nonnull int[][][] data, @Nonnull IBlockState[] states,
      @Nonnull Random rand) {
    for (int pass = 0; pass <= 1; pass++) {
      for (int y = 0; y < data.length; y++) {
        for (int x = 0; x < 6; x++) {
          for (int z = 0; z < 6; z++) {
            BlockPos pos = startpos.add(x + 5, -y + 5, z + 5);
            if (data[y][z][x] != -1) {
              IBlockState state = states[data[y][z][x]];
              if (state == null || (pass == 0 && (state.getBlock() instanceof BlockCarpet || state.getBlock() instanceof BlockSapling))) {
                world.setBlockToAir(pos);
              } else if (state.getBlock() instanceof BlockFalling) {
                while (world.getBlockState(pos).getBlock().isReplaceable(world, pos) && !world.isAirBlock(new BlockPos(pos.getX(), 0, pos.getZ()))) {
                  world.setBlockState(pos, state);
                  world.immediateBlockTick(pos, state, rand);
                }
              } else if (!(state.getBlock() instanceof BlockSlab)
                  || (world.getBlockState(pos).getBlock().isReplaceable(world, pos) && world.getBlockState(pos).getMaterial() != Material.WATER)) {
                world.setBlockState(pos, state);
              }
            }
          }
        }
      }
    }
  }

  public static @Nonnull BlockPos findInitialSpawnLocation(final @Nonnull World world, final int chunkX, final int chunkZ, @Nonnull Random rand) {
    BlockPos startpos;
    int i = chunkX * 16 + 8;
    int j = chunkZ * 16 + 8;
    startpos = new BlockPos(i, 250, j);
    while (startpos.getY() > Config.minSpawnHeight.getInt() && isFree(world, startpos.down(), false)) {
      startpos = startpos.down();
    }
    BlockPos startposSea = startpos;
    while (startposSea.getY() > Config.minSpawnHeight.getInt() && isFree(world, startposSea.down(), true)) {
      startposSea = startposSea.down();
    }
    if (startpos.getY() < Config.minSpawnHeightVoid.getInt() && world.isAirBlock(new BlockPos(startpos.getX(), 0, startpos.getZ()))) {
      startpos = new BlockPos(startpos.getX(), Config.minSpawnHeightVoid.getInt(), startpos.getZ());
    } else if (startpos.getY() - startposSea.getY() > 6 && rand.nextFloat() > Config.chanceFloatingIsland.getFloat()) {
      return startposSea;
    }
    if (rand.nextFloat() < Config.chanceTowers.getFloat()) {
      int newy = (rand.nextInt(128) + rand.nextInt(128)) / 2 - 64 + 10;
      if (newy > 10 && startpos.getY() + newy < 250) {
        startpos = startpos.up(newy);
      }
    }
    return startpos;
  }

  public static @Nonnull Random makeChunkRand(final @Nonnull World world, final int chunkX, final int chunkZ) {
    Random rand = new Random(world.getSeed());
    long k = rand.nextLong() / 2L * 2L + 1L;
    long l = rand.nextLong() / 2L * 2L + 1L;
    rand.setSeed(chunkX * k + chunkZ * l ^ world.getSeed());
    return rand;
  }

  public static boolean isValidSpawnLocation(final @Nonnull World world, final int chunkX, final int chunkZ) {
    return world.provider.getDimension() == 0 && ((chunkX - Config.generationOffsetX.getInt()) % Config.generationDistance.getInt()) == 0
        && ((chunkZ - Config.generationOffsetZ.getInt()) % Config.generationDistance.getInt()) == 0;
  }

  private static boolean isFree(@Nonnull World world, @Nonnull BlockPos startpos, boolean inSea) {
    for (int x = 5; x <= 10; x++) {
      for (int z = 5; z <= 10; z++) {
        if (!isGroundPossible(world, startpos.add(x, 0, z), inSea)) {
          return false;
        }
      }
    }
    return true;
  }

  private static boolean isGroundPossible(@Nonnull World world, @Nonnull BlockPos pos, boolean inSea) {
    if (!inSea && world.getBlockState(pos).getMaterial() == Material.WATER) {
      return world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()) && (world.getBlockState(pos.up()).getMaterial() != Material.WATER);
    }
    return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
  }

}
