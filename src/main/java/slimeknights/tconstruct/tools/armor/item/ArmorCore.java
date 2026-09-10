package slimeknights.tconstruct.tools.armor.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.tools.armor.ArmorNBT;

public abstract class ArmorCore extends ToolCore {

  private static final UUID[] ARMOR_MODIFIERS = new UUID[]{
      UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
      UUID.fromString("D8499B04-0E66-4726-AB29-64469ED3D1A3"),
      UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
      UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
  };

  public final EntityEquipmentSlot armorType;

  public ArmorCore(EntityEquipmentSlot armorType, PartMaterialType... requiredComponents) {
    super(requiredComponents);
    this.armorType = armorType;
  }

  @Override
  public float damagePotential() {
    return 0f;
  }

  @Override
  public double attackSpeed() {
    return 1.0;
  }

  protected String getArmorTypeForMaterial() {
    switch(armorType) {
      case HEAD: return ArmorMaterialStats.TYPE_HELMET;
      case CHEST: return ArmorMaterialStats.TYPE_CHESTPLATE;
      case LEGS: return ArmorMaterialStats.TYPE_LEGGINGS;
      case FEET: return ArmorMaterialStats.TYPE_BOOTS;
      default: return ArmorMaterialStats.TYPE_CHESTPLATE;
    }
  }

  protected ArmorNBT buildDefaultArmorTag(java.util.List<slimeknights.tconstruct.library.materials.Material> materials) {
    ArmorNBT data = new ArmorNBT();
    if(!materials.isEmpty()) {
      ArmorMaterialStats stats = materials.get(0).getStatsOrUnknown(getArmorTypeForMaterial());
      data.stats(stats);
      if(materials.size() > 1) {
        ArmorMaterialStats maille = materials.get(1).getStatsOrUnknown(ArmorMaterialStats.TYPE_MAILLE);
        data.maille(maille);
      }
      data.modifiers = 3;
    }
    return data;
  }

  @Override
  public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
    return armorType == this.armorType;
  }

  private int getArmorIndex(EntityEquipmentSlot slot) {
    switch(slot) {
      case HEAD: return 0;
      case CHEST: return 1;
      case LEGS: return 2;
      case FEET: return 3;
      default: return -1;
    }
  }

  @Nonnull
  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
    Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
    int idx = getArmorIndex(slot);
    if(idx < 0 || slot != this.armorType || ToolHelper.isBroken(stack) || !stack.hasTagCompound()) {
      return multimap;
    }
    NBTTagCompound tag = TagUtil.getToolTag(stack);
    float defense = tag.getFloat(Tags.DEFENSE);
    float toughness = tag.getFloat(Tags.TOUGHNESS);
    float speed = tag.getFloat(Tags.MOVEMENT_SPEED);
    float protection = tag.getFloat(Tags.PROTECTION);
    if(defense > 0 || protection > 0) {
      float totalDef = defense + protection;
      multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[idx], "Armor modifier", totalDef, 0));
    }
    if(toughness > 0) multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[idx], "Armor toughness", toughness, 0));
    if(speed != 0) multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[idx], "Armor speed", speed, 2));
    return multimap;
  }

  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    TinkerUtil.getTraitsOrdered(itemStack).forEach(trait -> trait.onArmorTick(itemStack, world, player));
  }

  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    if(entity != null && entity.world != null && entity.world.isRemote && stack.hasTagCompound()) {
      try {
        java.util.List<Material> mats = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack));
        if(mats != null && !mats.isEmpty() && mats.get(0) != null && mats.get(0) != Material.UNKNOWN) {
          return slimeknights.tconstruct.tools.armor.client.LayerTinkerArmor.getArmorTextureForSet(slot, mats.get(0), stack).toString();
        }
      } catch(Exception e) {
      }
    }
    if(slot == EntityEquipmentSlot.LEGS) return Util.resource("textures/models/armor/plate_layer_2.png").toString();
    return Util.resource("textures/models/armor/plate_layer_1.png").toString();
  }

  @Override
  public int[] getRepairParts() {
    return new int[]{0};
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    EntityEquipmentSlot slot = this.armorType;
    ItemStack current = playerIn.getItemStackFromSlot(slot);
    if(current.isEmpty()) {
      playerIn.setItemStackToSlot(slot, itemstack.copy());
      itemstack.setCount(0);
    }
    else {
      playerIn.setItemStackToSlot(slot, itemstack.copy());
      playerIn.setHeldItem(handIn, current);
    }
    playerIn.playSound(net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f);
    return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
  }

  @Override
  public int getMaxDamage(ItemStack stack) {
    return ToolHelper.getDurabilityStat(stack);
  }

  @Override
  public void setDamage(ItemStack stack, int damage) {
    int max = getMaxDamage(stack);
    super.setDamage(stack, Math.min(max, damage));
    if(getDamage(stack) == max) ToolHelper.breakTool(stack, null);
  }
}
