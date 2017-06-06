package info.loenwind.travelhut.handlers;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

final class TeleportRunner implements ITicknQueue {
  private final @Nonnull EntityPlayerMP player;
  private final @Nonnull BlockPos pos, target;
  private static final ResourceLocation SOUND = new ResourceLocation("entity.endermen.teleport");

  TeleportRunner(@Nonnull EntityPlayerMP player, @Nonnull BlockPos pos, @Nonnull BlockPos target) {
    this.player = player;
    this.pos = pos;
    this.target = target;
  }

  @Override
  public ITicknQueue tick() {
    player.fallDistance = 0;
    player.timeUntilPortal = 10;
    player.connection.setPlayerLocation(target.getX() + 0.5, target.getY() + 1.1, target.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
    player.world.updateEntityWithOptionalForce(player, false);
    final SoundEvent sound = SoundEvent.REGISTRY.getObject(TeleportRunner.SOUND);
    if (sound != null) {
      player.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 1, 1);
      player.world.playSound(null, target.getX(), target.getY(), target.getZ(), sound, SoundCategory.BLOCKS, 1, 1);
    }
    return null;
  }

  @Override
  public void logout(EntityPlayer player2) {
  }

}