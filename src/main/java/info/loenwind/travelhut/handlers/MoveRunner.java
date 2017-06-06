package info.loenwind.travelhut.handlers;

import javax.annotation.Nonnull;

import info.loenwind.travelhut.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;

public class MoveRunner implements ITicknQueue {

  private static final ResourceLocation SOUND = new ResourceLocation("entity.endermen.teleport");
  private final @Nonnull EntityPlayerMP player;
  private double x0, y0, z0, x1, y1, z1;
  private int tick = 0;
  private final @Nonnull GameType gameType;
  private boolean valid = true;

  private MoveRunner(@Nonnull EntityPlayerMP player, @Nonnull BlockPos pos, @Nonnull BlockPos target) {
    this.player = player;
    x0 = player.posX;
    y0 = player.posY;
    z0 = player.posZ;
    x1 = target.getX() + 0.5;
    y1 = target.getY() + 1.1;
    z1 = target.getZ() + 0.5;
    gameType = player.interactionManager.getGameType();
  }

  @Override
  public void logout(EntityPlayer player2) {
    if (player == player2) {
      valid = false;
      try {
        player.connection.setPlayerLocation(x1, y1, z1, player.rotationYaw, player.rotationPitch);
      } catch (Exception e) {
      }
      try {
        player.setGameType(gameType);
      } catch (Exception e) {
      }
    }
  }

  @Override
  public MoveRunner tick() {
    if (!valid) {
      return null;
    }
    if (tick == 0) {
      playSound(x0, y0, z0);
      player.setGameType(GameType.SPECTATOR);
    }
    int max_tick = Config.movingTime.getInt();
    double x = x0 + (x1 - x0) * tick / max_tick;
    double y = y0 + (y1 - y0) * tick / max_tick;
    double z = z0 + (z1 - z0) * tick / max_tick;
    double yoff = MathHelper.sin((float) ((float) tick / max_tick * Math.PI)) * Config.movingArc.getFloat();
    player.connection.setPlayerLocation(x, y + yoff, z, player.rotationYaw, player.rotationPitch);
    tick++;
    if (tick > max_tick) {
      player.capabilities.isFlying = false;
      player.setGameType(gameType);
      player.timeUntilPortal = 10;
      playSound(x1, y1, z1);
      return null;
    }
    return this;
  }

  private void playSound(double x, double y, double z) {
    final SoundEvent sound = SoundEvent.REGISTRY.getObject(SOUND);
    if (sound != null) {
      player.world.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, 1, 1);
    }
  }

  public static void enqueue(@Nonnull EntityPlayerMP player, @Nonnull BlockPos pos, @Nonnull BlockPos target) {
    TickHandler.enqueue(new MoveRunner(player, pos, target));
  }

}
