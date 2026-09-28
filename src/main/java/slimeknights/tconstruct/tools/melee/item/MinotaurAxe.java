package slimeknights.tconstruct.tools.melee.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

/** Hacha de minotauro: golpeo cargado con bonus si el jugador cae/monta. Porte de TC3 (minotaur_axe). */
public class MinotaurAxe extends BattleAxe {

  public MinotaurAxe() {
    super();
    addCategory(Category.WEAPON);
  }

  @Override
  public float damagePotential() {
    return 1.4f;
  }

  @Override
  public double attackSpeed() {
    return 0.9d;
  }

  @Override
  public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
    // charge_attack: bonus por caída (embestida) o montura
    if(player != null && !player.onGround && player.motionY < -0.5) {
      damage *= 1.3f;
    }
    else if(player != null && player.isRiding()) {
      damage *= 1.2f;
    }
    return super.dealDamage(stack, player, entity, damage);
  }

  @Override
  public ToolNBT buildTagData(List<slimeknights.tconstruct.library.materials.Material> materials) {
    return super.buildTagData(materials);
  }
}