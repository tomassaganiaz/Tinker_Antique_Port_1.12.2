package slimeknights.tconstruct.tools.melee.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

/** Dagger: rápida, bloquea algo y rompe telarañas con velocidad. Porte de TC3 1.20.1. */
public class Dagger extends SwordCore {

  public static final float DURABILITY_MODIFIER = 0.75f;
  public static final float DAMAGE_MODIFIER = 0.65f;

  public Dagger() {
    super(PartMaterialType.handle(TinkerTools.toolRod),
          PartMaterialType.head(TinkerTools.knifeBlade));

    addCategory(Category.WEAPON);
  }

  @Override
  public float damagePotential() {
    return 0.65f;
  }

  @Override
  public double attackSpeed() {
    return 2.0d;
  }

  @Override
  public float knockback() {
    return 0.6f;
  }

  // rápida en telarañas (TC3: 7.5x en cobweb)
  @Override
  public float getStrVsBlock(ItemStack stack, net.minecraft.block.state.IBlockState state) {
    float base = super.getStrVsBlock(stack, state);
    if(state.getBlock() == net.minecraft.init.Blocks.WEB && !ToolHelper.isBroken(stack)) {
      return base * 7.5f;
    }
    return base;
  }

  @Override
  public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
    boolean hit = super.dealDamage(stack, player, entity, damage);
    if(hit && player instanceof net.minecraft.entity.player.EntityPlayer) {
      // offhand puede atacar en paralelo (TC3: offhand_attack)
      net.minecraft.entity.player.EntityPlayer p = (net.minecraft.entity.player.EntityPlayer) player;
      if(p.getHeldItemOffhand().getItem() instanceof Dagger) {
        p.getCooldownTracker().setCooldown(stack.getItem(), 8);
      }
    }
    return hit;
  }

  @Override
  public float getRepairModifierForPart(int index) {
    return DURABILITY_MODIFIER;
  }

  @Override
  public ToolNBT buildTagData(List<Material> materials) {
    ToolNBT data = buildDefaultTag(materials);
    data.durability *= DURABILITY_MODIFIER;
    data.attack *= DAMAGE_MODIFIER;
    data.attack += 2f; // base damage TC3 3.0 (con 1.0 del core)
    return data;
  }
}