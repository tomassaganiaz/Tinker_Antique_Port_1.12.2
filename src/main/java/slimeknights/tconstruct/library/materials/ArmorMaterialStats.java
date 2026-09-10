package slimeknights.tconstruct.library.materials;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.CustomFontColor;

public class ArmorMaterialStats extends AbstractMaterialStats {

  public static final String TYPE_HELMET = "plating_helmet";
  public static final String TYPE_CHESTPLATE = "plating_chestplate";
  public static final String TYPE_LEGGINGS = "plating_leggings";
  public static final String TYPE_BOOTS = "plating_boots";
  public static final String TYPE_SHIELD = "plating_shield";
  public static final String TYPE_MAILLE = "maille";

  public static final String LOC_Durability = "stat.armor.durability.name";
  public static final String LOC_Defense = "stat.armor.defense.name";
  public static final String LOC_Toughness = "stat.armor.toughness.name";

  public static final String LOC_DurabilityDesc = "stat.armor.durability.desc";
  public static final String LOC_DefenseDesc = "stat.armor.defense.desc";
  public static final String LOC_ToughnessDesc = "stat.armor.toughness.desc";
  public static final String LOC_KnockbackResistanceDesc = "stat.armor.knockbackResistance.desc";
  public static final String LOC_KnockbackResistance = "stat.armor.knockbackResistance.name";

  public static final String COLOR_Durability = CustomFontColor.valueToColorCode(1f);
  public static final String COLOR_Defense = CustomFontColor.encodeColor(120, 160, 205);
  public static final String COLOR_Toughness = CustomFontColor.encodeColor(200, 120, 120);
  public static final String COLOR_KnockbackResistance = CustomFontColor.encodeColor(180, 180, 80);

  public final int durability;
  public final float defense;
  public final float toughness;
  public final float knockbackResistance;

  public static int getDurabilityMultiplier(String type) {
    if(TYPE_HELMET.equals(type)) return 11;
    if(TYPE_CHESTPLATE.equals(type)) return 16;
    if(TYPE_LEGGINGS.equals(type)) return 15;
    if(TYPE_BOOTS.equals(type)) return 13;
    if(TYPE_SHIELD.equals(type)) return 18;
    if(TYPE_MAILLE.equals(type)) return 5;
    return 1;
  }

  public ArmorMaterialStats(String type, int durability, float defense, float toughness) {
    this(type, durability, defense, toughness, 0f);
  }

  public ArmorMaterialStats(String type, int durability, float defense, float toughness, float knockbackResistance) {
    super(type);
    this.durability = durability;
    this.defense = defense;
    this.toughness = toughness;
    this.knockbackResistance = knockbackResistance;
  }

  @Override
  public List<String> getLocalizedInfo() {
    List<String> info = Lists.newArrayList();
    if(durability != 0) info.add(formatDurability(durability));
    if(defense != 0) info.add(formatDefense(defense));
    if(toughness != 0) info.add(formatToughness(toughness));
    if(knockbackResistance != 0) info.add(formatKnockbackResistance(knockbackResistance));
    return info;
  }

  @Override
  public List<String> getLocalizedDesc() {
    List<String> info = Lists.newArrayList();
    if(durability != 0) info.add(Util.translate(LOC_DurabilityDesc));
    if(defense != 0) info.add(Util.translate(LOC_DefenseDesc));
    if(toughness != 0) info.add(Util.translate(LOC_ToughnessDesc));
    if(knockbackResistance != 0) info.add(Util.translate(LOC_KnockbackResistanceDesc));
    return info;
  }

  public static String formatDurability(int durability) {
    return formatNumber(LOC_Durability, COLOR_Durability, durability);
  }

  public static String formatDefense(float defense) {
    return formatNumber(LOC_Defense, COLOR_Defense, defense);
  }

  public static String formatToughness(float toughness) {
    return formatNumber(LOC_Toughness, COLOR_Toughness, toughness);
  }

  public static String formatKnockbackResistance(float v) {
    return formatNumberPercent(LOC_KnockbackResistance, COLOR_KnockbackResistance, v);
  }
}
