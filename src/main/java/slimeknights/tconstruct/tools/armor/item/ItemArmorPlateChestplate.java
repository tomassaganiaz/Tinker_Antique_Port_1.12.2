package slimeknights.tconstruct.tools.armor.item;

import java.util.List;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;

public class ItemArmorPlateChestplate extends ArmorCore {

  public ItemArmorPlateChestplate(PartMaterialType... requiredComponents) {
    super(EntityEquipmentSlot.CHEST, requiredComponents);
  }

  @Override
  public NBTTagCompound buildTag(List<Material> materials) {
    return buildDefaultArmorTag(materials).get();
  }
}
