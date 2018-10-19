package info.loenwind.travelhut.handlers;

import net.minecraft.entity.player.EntityPlayer;

public interface ITicknQueue {

  void logout(EntityPlayer player);

  ITicknQueue tick();

}