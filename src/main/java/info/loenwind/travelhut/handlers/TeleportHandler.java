package info.loenwind.travelhut.handlers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.travelhut.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TeleportHandler {

  public static void teleport(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EntityPlayerMP player) {
    if (player.timeUntilPortal > 1) {
      return;
    } else if (player.timeUntilPortal == 0) {
      player.timeUntilPortal = 5;
      return;
    }
    EnumFacing direction = determineDirection(pos);
    if (direction == null) {
      return;
    }
    player.timeUntilPortal = 10;
    BlockPos target = getTarget(worldIn, pos, direction);
    if (target == null) {
      BlockPos expectedTarget = pos.offset(direction, Config.generationDistance.getInt() * 16 - 4);
      player.sendMessage(new TextComponentString("No target found at " + expectedTarget.getX() + ", " + expectedTarget.getZ()));
    } else {
      if (Config.movingTeleport.getBoolean() && !player.isSneaking()) {
        MoveRunner.enqueue(player, pos, target);
      } else if (Config.asyncTeleport.getBoolean()) {
        TickHandler.enqueue(new TeleportRunner(player, pos, target));
      } else {
        (new TeleportRunner(player, pos, target)).tick();
      }
    }
  }

  static @Nullable BlockPos getTarget(@Nonnull World worldIn, @Nonnull BlockPos startpos, @Nonnull EnumFacing direction) {
    if (Config.travellingChecksInBetween.getBoolean()) {
      for (int i = 1; i <= Math.max(Config.generationDistance.getInt() * 3, 10); i++) {
        BlockPos target = getTarget(worldIn, startpos, direction, i);
        if (target != null) {
          return target;
        }
      }
    } else {
      return getTarget(worldIn, startpos, direction, Config.generationDistance.getInt());
    }
    return null;
  }

  private @Nullable static BlockPos getTarget(@Nonnull World worldIn, @Nonnull BlockPos startpos, @Nonnull EnumFacing direction, int offset) {
    for (int i = 0; i <= 3; i++) {
      BlockPos target = getTarget(worldIn, startpos, direction, offset, i * 3);
      if (target != null) {
        return target;
      }
    }
    return null;
  }

  static @Nullable BlockPos getTarget(@Nonnull World worldIn, @Nonnull BlockPos startpos, @Nonnull EnumFacing direction, int offset, int tryPopulate) {
    BlockPos target = startpos.offset(direction, offset * 16 - 4);
    target = new BlockPos(target.getX(), 250, target.getZ());
    final int chunkX = target.getX() >> 4;
    final int chunkZ = target.getZ() >> 4;

    if (!worldIn.isBlockLoaded(target)) {
      worldIn.getChunkFromChunkCoords(chunkX, chunkZ);
    }
    if (tryPopulate > 0) {
      for (int dx = tryPopulate; dx >= -tryPopulate; dx--) {
        for (int dz = tryPopulate; dz >= -tryPopulate; dz--) {
          worldIn.getChunkFromChunkCoords(chunkX + dx, chunkZ + dz);
        }
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

  static @Nullable EnumFacing determineDirection(@Nonnull BlockPos pos) {

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
