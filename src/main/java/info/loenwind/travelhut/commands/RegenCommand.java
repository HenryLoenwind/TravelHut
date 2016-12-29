package info.loenwind.travelhut.commands;

import java.util.Random;

import javax.annotation.Nonnull;

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
  public @Nonnull String getName() {
    return "regenHut";
  }

  @Override
  public @Nonnull String getUsage(@Nonnull ICommandSender sender) {
    return "/regenHut (need to be near a valid spawn location)";
  }

  @Override
  public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
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
    IBlockState[] states = WorldGenHandler.mkBlockStates(WorldGenHandler.getGlass(rand));
    BlockPos startpos = WorldGenHandler.findInitialSpawnLocation(world, chunkX, chunkZ, rand);
    WorldGenHandler.placeHut(world, startpos, states, rand);

    sender.sendMessage(new TextComponentString("Created hut at " + (startpos.getX() + 8) + "," + (startpos.getY()) + "," + (startpos.getZ() + 8)));
  }

}
