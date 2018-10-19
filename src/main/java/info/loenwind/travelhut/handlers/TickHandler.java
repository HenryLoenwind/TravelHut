package info.loenwind.travelhut.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import info.loenwind.travelhut.TravelHutMod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

@EventBusSubscriber(modid = TravelHutMod.MODID)
public class TickHandler {

  static final Queue<ITicknQueue> queue = new LinkedTransferQueue<>();

  private TickHandler() {
  }

  public static void enqueue(ITicknQueue handler) {
    TickHandler.queue.add(handler);
  }

  @SubscribeEvent
  public static void onTick(ServerTickEvent event) {
    if (event.phase != Phase.END || queue.isEmpty()) {
      return;
    }
    List<ITicknQueue> keep = new ArrayList<>();
    while (!queue.isEmpty()) {
      ITicknQueue poll = queue.poll();
      if (poll == null) {
        return;
      }
      ITicknQueue result = poll.tick();
      if (result != null) {
        keep.add(result);
      }
    }
    queue.addAll(keep);
  }

  @SubscribeEvent
  public static void onLogoff(PlayerLoggedOutEvent event) {
    for (ITicknQueue moveHandler : queue) {
      moveHandler.logout(event.player);
    }
  }

}
