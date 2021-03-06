package info.loenwind.travelhut.config;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import info.loenwind.travelhut.TravelHutMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public enum Config {

  // section, defaultValue, description, requiresWorldRestart, requiresGameRestart

  enableTravelHuts(Section.SERVER, true, "Enable the generation of travel huts.", false, false),
  generationDistance(Section.SERVER, 20, "The distance between huts (in chunks).", false, false),
  generationOffsetX(Section.SERVER, 0, "The X offset from 0,0 (in chunks).", false, false),
  generationOffsetZ(Section.SERVER, 0, "The Z offset from 0,0 (in chunks).", false, false),
  travellingChecksInBetween(Section.SERVER, false,
      "When travelling, check chunks where no huts are generated, too. Enable this after changing the generation distance.", false, false),
  generateBedrock(Section.SERVER, true,
      "Should travel hut floors generate as bedrock? If false, they will be obsidian. (Note: Bedrock huts and obsidian huts are not compatible with each other.)",
      false, false),
  chanceFloatingIsland(Section.SERVER, 0.05, "Chance that ocean huts will float instead of being submerged (0.0-1.0.)", false, false),
  chanceTowers(Section.SERVER, 0.025, "Chance that huts will generate as a tower instead of being on the floor (0.0-1.0).", false, false),
  minSpawnHeight(Section.SERVER, 3, "Huts will not spawn lower than this. Careful with oceans. (3-250).", false, false),
  minSpawnHeightVoid(Section.SERVER, 64, "Huts will not spawn lower than this when over the void. Void is when there's air at y=0. (3-250).", false, false),
  generateTree(Section.SERVER, false, "Should a tree be added to the huts? Only makes sense in void worlds.", false, false);

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Nothing to see beyond this point. End of configuration values.
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Nonnull
  private final Section section;
  @Nonnull
  private final Object defaultValue;
  @Nonnull
  private final String description;
  @Nonnull
  private Object currentValue;
  private final boolean requiresWorldRestart;
  private final boolean requiresGameRestart;

  private Config(@Nonnull Section section, @Nonnull Object defaultValue, @Nonnull String description, boolean requiresWorldRestart,
      boolean requiresGameRestart) {
    this.section = section;
    this.description = description;
    this.currentValue = this.defaultValue = defaultValue;
    this.requiresWorldRestart = requiresWorldRestart;
    this.requiresGameRestart = requiresGameRestart;
  }

  private Config(@Nonnull Section section, @Nonnull Integer defaultValue, @Nonnull String description, boolean requiresWorldRestart,
      boolean requiresGameRestart) {
    this(section, (Object) defaultValue, description, requiresWorldRestart, requiresGameRestart);
  }

  private Config(@Nonnull Section section, @Nonnull Double defaultValue, @Nonnull String description, boolean requiresWorldRestart,
      boolean requiresGameRestart) {
    this(section, (Object) defaultValue, description, requiresWorldRestart, requiresGameRestart);
  }

  private Config(@Nonnull Section section, @Nonnull Boolean defaultValue, @Nonnull String description, boolean requiresWorldRestart,
      boolean requiresGameRestart) {
    this(section, (Object) defaultValue, description, requiresWorldRestart, requiresGameRestart);
  }

  private Config(@Nonnull Section section, @Nonnull String defaultValue, @Nonnull String description, boolean requiresWorldRestart,
      boolean requiresGameRestart) {
    this(section, (Object) defaultValue, description, requiresWorldRestart, requiresGameRestart);
  }

  private Config(@Nonnull Section section, @Nonnull Integer defaultValue, @Nonnull String description) {
    this(section, (Object) defaultValue, description, false, false);
  }

  private Config(@Nonnull Section section, @Nonnull Double defaultValue, @Nonnull String description) {
    this(section, (Object) defaultValue, description, false, false);
  }

  private Config(@Nonnull Section section, @Nonnull Boolean defaultValue, @Nonnull String description) {
    this(section, (Object) defaultValue, description, false, false);
  }

  private Config(@Nonnull Section section, @Nonnull String defaultValue, @Nonnull String description) {
    this(section, (Object) defaultValue, description, false, false);
  }

  void load(Configuration config) {
    Object value = null;
    if (defaultValue instanceof Integer) {
      value = setPropertyData(config.get(section.name, name(), (Integer) defaultValue, description)).getInt((Integer) defaultValue);
    } else if (defaultValue instanceof Double) {
      value = setPropertyData(config.get(section.name, name(), (Double) defaultValue, description)).getDouble((Double) defaultValue);
    } else if (defaultValue instanceof Boolean) {
      value = setPropertyData(config.get(section.name, name(), (Boolean) defaultValue, description)).getBoolean((Boolean) defaultValue);
    } else if (defaultValue instanceof String) {
      value = setPropertyData(config.get(section.name, name(), (String) defaultValue, description)).getString();
    }

    setField(value);
  }

  private Property setPropertyData(final Property property) {
    property.setRequiresWorldRestart(requiresWorldRestart);
    property.setRequiresMcRestart(requiresGameRestart);
    property.setLanguageKey(TravelHutMod.MODID + ".config." + name());
    return property;
  }

  private void setField(Object value) {
    if (value != null) {
      currentValue = value;
    }
  }

  void store(ByteBuf buf) {
    if (defaultValue instanceof Integer) {
      buf.writeInt(getInt());
    } else if (defaultValue instanceof Double) {
      buf.writeDouble(getDouble());
    } else if (defaultValue instanceof Boolean) {
      buf.writeBoolean(getBoolean());
    } else if (defaultValue instanceof String) {
      String value = getString();
      byte[] bytes = value.getBytes(Charset.forName("UTF-8"));
      buf.writeInt(bytes.length);
      buf.writeBytes(bytes);
    }
  }

  void read(ByteBuf buf) {
    Object value = null;
    if (defaultValue instanceof Integer) {
      value = buf.readInt();
    } else if (defaultValue instanceof Double) {
      value = buf.readDouble();
    } else if (defaultValue instanceof Boolean) {
      value = buf.readBoolean();
    } else if (defaultValue instanceof String) {
      int len = buf.readInt();
      byte[] bytes = new byte[len];
      buf.readBytes(bytes, 0, len);
      value = new String(bytes, Charset.forName("UTF-8"));
    }
    setField(value);
  }

  protected void resetToDefault() {
    setField(defaultValue);
  }

  public Section getSection() {
    return section;
  }

  //

  private class DataTypeErrorInConfigException extends RuntimeException {
    private static final long serialVersionUID = -7077690323202964355L;
  }

  public int getDefaultInt() {
    if (defaultValue instanceof Integer) {
      return (Integer) defaultValue;
    } else if (defaultValue instanceof Double) {
      return ((Double) defaultValue).intValue();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) defaultValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public double getDefaultDouble() {
    if (defaultValue instanceof Integer) {
      return (Integer) defaultValue;
    } else if (defaultValue instanceof Double) {
      return (Double) defaultValue;
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) defaultValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public float getDefaultFloat() {
    if (defaultValue instanceof Integer) {
      return (Integer) defaultValue;
    } else if (defaultValue instanceof Double) {
      return ((Double) defaultValue).floatValue();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) defaultValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public boolean getDefaultBoolean() {
    if (defaultValue instanceof Integer) {
      throw new DataTypeErrorInConfigException();
    } else if (defaultValue instanceof Double) {
      throw new DataTypeErrorInConfigException();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) defaultValue;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  @SuppressWarnings("null")
  @Nonnull
  public String getDefaultString() {
    if (defaultValue instanceof Integer) {
      return ((Integer) defaultValue).toString();
    } else if (defaultValue instanceof Double) {
      return ((Double) defaultValue).toString();
    } else if (defaultValue instanceof Boolean) {
      return ((Boolean) defaultValue).toString();
    } else if (defaultValue instanceof String) {
      return (String) defaultValue;
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  //

  public int getInt() {
    if (defaultValue instanceof Integer) {
      return (Integer) currentValue;
    } else if (defaultValue instanceof Double) {
      return ((Double) currentValue).intValue();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) currentValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public double getDouble() {
    if (defaultValue instanceof Integer) {
      return (Integer) currentValue;
    } else if (defaultValue instanceof Double) {
      return (Double) currentValue;
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) currentValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public float getFloat() {
    if (defaultValue instanceof Integer) {
      return (Integer) currentValue;
    } else if (defaultValue instanceof Double) {
      return ((Double) currentValue).floatValue();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) currentValue ? 1 : 0;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  public boolean getBoolean() {
    if (defaultValue instanceof Integer) {
      throw new DataTypeErrorInConfigException();
    } else if (defaultValue instanceof Double) {
      throw new DataTypeErrorInConfigException();
    } else if (defaultValue instanceof Boolean) {
      return (Boolean) currentValue;
    } else if (defaultValue instanceof String) {
      throw new DataTypeErrorInConfigException();
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }

  @SuppressWarnings("null")
  @Nonnull
  public String getString() {
    if (defaultValue instanceof Integer) {
      return ((Integer) currentValue).toString();
    } else if (defaultValue instanceof Double) {
      return ((Double) currentValue).toString();
    } else if (defaultValue instanceof Boolean) {
      return ((Boolean) currentValue).toString();
    } else if (defaultValue instanceof String) {
      return (String) currentValue;
    } else {
      throw new DataTypeErrorInConfigException();
    }
  }
}
