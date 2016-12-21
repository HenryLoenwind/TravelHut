package info.loenwind.travelhut.handlers;

import info.loenwind.travelhut.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TeleportHandler {

  private static final ResourceLocation SOUND = new ResourceLocation("entity.endermen.teleport");

  public static void teleport(World worldIn, BlockPos pos, EntityPlayerMP player) {
    EnumFacing direction = determineDirection(pos);
    if (direction == null) {
      return;
    }
    BlockPos target = getTarget(worldIn, pos, direction);
    if (target == null) {
      return;
    }
    player.connection.setPlayerLocation(target.getX() + 0.5, target.getY() + 1.1, target.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
    player.fallDistance = 0;
    player.timeUntilPortal = 10;
    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvent.REGISTRY.getObject(SOUND), SoundCategory.BLOCKS, 1, 1);
    worldIn.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvent.REGISTRY.getObject(SOUND), SoundCategory.BLOCKS, 1, 1);
  }

  static BlockPos getTarget(World worldIn, BlockPos startpos, EnumFacing direction) {
    if (Config.travellingChecksInBetween.getBoolean()) {
      for (int i = 1; i <= Math.max(Config.generationDistance.getInt() * 3, 10); i++) {
        BlockPos target = getTarget(worldIn, startpos, direction, i);
        if (target != null) {
          return target;
        }
      }
    } else {
      for (int i = 1; i <= 2; i++) {
        BlockPos target = getTarget(worldIn, startpos, direction, Config.generationDistance.getInt() * i);
        if (target != null) {
          return target;
        }
      }
    }
    return null;
  }

  static BlockPos getTarget(World worldIn, BlockPos startpos, EnumFacing direction, int offset) {
    BlockPos target = startpos.offset(direction, offset * 16 - 4);
    target = new BlockPos(target.getX(), 250, target.getZ());
    if (!worldIn.isBlockLoaded(target)) {
      final int chunkX = target.getX() >> 4;
      final int chunkZ = target.getZ() >> 4;
      Chunk chunk = worldIn.getChunkFromChunkCoords(chunkX, chunkZ);
      if (!chunk.isTerrainPopulated()) {
        worldIn.getChunkFromChunkCoords(chunkX + 1, chunkZ);
        worldIn.getChunkFromChunkCoords(chunkX, chunkZ + 1);
        worldIn.getChunkFromChunkCoords(chunkX + 1, chunkZ + 1);
        worldIn.getChunkFromChunkCoords(chunkX - 1, chunkZ);
        worldIn.getChunkFromChunkCoords(chunkX, chunkZ - 1);
        worldIn.getChunkFromChunkCoords(chunkX - 1, chunkZ - 1);
      }
    }
    final Block landingBlock = Config.generateBedrock.getBoolean() ? Blocks.BEDROCK : Blocks.OBSIDIAN;
    while (worldIn.getBlockState(target).getBlock() != landingBlock) {
      target = target.down();
      if (target.getY() < 0) {
        return null;
      }
    }
    if (!(worldIn.isAirBlock(target.up()) || worldIn.getBlockState(target.up()).getBlock() instanceof BlockCarpet) || !worldIn.isAirBlock(target.up(2))) {
      return null;
    }
    return target;
  }

  static EnumFacing determineDirection(BlockPos pos) {

    int x = pos.getX() & 0x0F;
    int z = pos.getZ() & 0x0F;

    if (x == 2) {
      return EnumFacing.EAST;
    }
    if (x == 13) {
      return EnumFacing.WEST;
    }
    if (z == 2) {
      return EnumFacing.SOUTH;
    }
    if (z == 13) {
      return EnumFacing.NORTH;
    }

    return null;
  }
}
