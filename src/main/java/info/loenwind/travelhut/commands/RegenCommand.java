package info.loenwind.travelhut.commands;

import java.util.Random;

import info.loenwind.travelhut.handlers.EnderIOAdapter;
import info.loenwind.travelhut.handlers.WorldGenHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class RegenCommand extends CommandBase {

  public RegenCommand() {
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public String getCommandName() {
    return "regenHut";
  }

  @Override
  public String getCommandUsage(ICommandSender sender) {
    return "/regenHut";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (!(sender instanceof EntityPlayer)) {
      throw new WrongUsageException("Console use is not supported");
    }

    World world = sender.getEntityWorld();
    int chunkX = sender.getPosition().getX() >> 4;
    int chunkZ = sender.getPosition().getZ() >> 4;

    if (!WorldGenHandler.isValidSpawnLocation(world, chunkX, chunkZ)) {
      chunkX -= 1;
      if (!WorldGenHandler.isValidSpawnLocation(world, chunkX, chunkZ)) {
        chunkZ -= 1;
        if (!WorldGenHandler.isValidSpawnLocation(world, chunkX, chunkZ)) {
          chunkX += 1;
          if (!WorldGenHandler.isValidSpawnLocation(world, chunkX, chunkZ)) {
            throw new WrongUsageException("Not near a valid location");
          }
        }
      }
    }

    Random rand = WorldGenHandler.makeChunkRand(world, chunkX, chunkZ);
    IBlockState[] states = WorldGenHandler.mkBlockStates(EnderIOAdapter.getGlass(rand));
    BlockPos startpos = WorldGenHandler.findInitialSpawnLocation(world, chunkX, chunkZ, rand);
    WorldGenHandler.placeHut(world, startpos, states, rand);

    sender.addChatMessage(new TextComponentString("Created hut at " + (startpos.getX() + 8) + "," + (startpos.getY()) + "," + (startpos.getZ() + 8)));
  }

}
