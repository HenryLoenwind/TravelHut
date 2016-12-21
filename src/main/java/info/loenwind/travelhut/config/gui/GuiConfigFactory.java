package info.loenwind.travelhut.config.gui;

import java.util.ArrayList;
import java.util.List;

import info.loenwind.travelhut.TravelHutMod;
import info.loenwind.travelhut.config.ConfigHandler;
import info.loenwind.travelhut.config.Section;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiConfigFactory extends GuiConfig {

  public GuiConfigFactory(GuiScreen parentScreen) {
    super(parentScreen, getConfigElements(parentScreen), TravelHutMod.MODID, false, false, I18n.translateToLocal(TravelHutMod.MODID + ".config.title"));
  }

  private static List<IConfigElement> getConfigElements(GuiScreen parent) {
    List<IConfigElement> list = new ArrayList<IConfigElement>();
    String prefix = TravelHutMod.MODID + ".config.";

    for (Section section : Section.values()) {
      if (!section.sync || !ConfigHandler.configLockedByServer) {
        list.add(new ConfigElement(ConfigHandler.configuration.getCategory(section.name).setLanguageKey(prefix + section.name)));
      }
    }

    return list;
  }
}
