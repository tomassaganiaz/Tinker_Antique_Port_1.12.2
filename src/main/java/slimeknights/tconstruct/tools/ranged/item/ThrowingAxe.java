package slimeknights.tconstruct.tools.ranged.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.List;

import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.common.entity.EntityThrowingAxe;

/** Hacha arrojadiza: punta de flecha + asta. Porte de TC3 1.20.1 (throwing_axe). */
public class ThrowingAxe extends ProjectileCore {

  public ThrowingAxe() {
    super(PartMaterialType.arrowHead(TinkerTools.arrowHead),
          PartMaterialType.arrowShaft(TinkerTools.arrowShaft));

    addCategory(Category.NO_MELEE, Category.PROJECTILE);
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
    if(this.isInCreativeTab(tab)) {
      addDefaultSubItems(subItems, TinkerMaterials.iron, TinkerMaterials.wood);
    }
  }

  @Override
  public float damagePotential() {
    return 1.5f;
  }

  @Override
  public double attackSpeed() {
    return 0.75d;
  }

  @Override
  public ProjectileNBT buildTagData(List<Material> materials) {
    ProjectileNBT data = new ProjectileNBT();

    HeadMaterialStats head = materials.get(0).getStatsOrUnknown(MaterialTypes.HEAD);
    ArrowShaftMaterialStats shaft = materials.get(1).getStatsOrUnknown(MaterialTypes.SHAFT);

    data.head(head);
    data.shafts(this, shaft);

    // TC3: projectile_damage multiplica x3
    data.attack *= 3.0f;
    data.attack += 1.5f;

    return data;
  }

  @Override
  public EntityProjectileBase getProjectile(ItemStack stack, ItemStack launcher, World world, EntityPlayer player, float speed, float inaccuracy, float progress, boolean usedAmmo) {
    inaccuracy -= (1f - 1f / ProjectileNBT.from(stack).accuracy) * speed / 2f;
    return new EntityThrowingAxe(world, player, speed, inaccuracy, getProjectileStack(stack, world, player, usedAmmo), launcher);
  }
}