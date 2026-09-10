package slimeknights.tconstruct.tools.armor.item;

import java.util.List;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;

public class ItemArmorPlateHelmet extends ArmorCore {

  public ItemArmorPlateHelmet(PartMaterialType... requiredComponents) {
    super(EntityEquipmentSlot.HEAD, requiredComponents);
  }

  @Override
  public NBTTagCompound buildTag(List<Material> materials) {
    return buildDefaultArmorTag(materials).get();
  }
}
