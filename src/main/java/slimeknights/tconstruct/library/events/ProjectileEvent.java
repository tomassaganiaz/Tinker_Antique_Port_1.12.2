package slimeknights.tconstruct.library.events;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

import slimeknights.tconstruct.library.entity.EntityProjectileBase;

public class ProjectileEvent extends TinkerEvent {

  public final Entity projectileEntity;
  /** Might be null if the entity is a vanilla or other mods entity */
  @Nullable
  public final EntityProjectileBase projectile;

  public ProjectileEvent(Entity projectile) {
    this.projectileEntity = projectile;
    if(projectile instanceof EntityProjectileBase) {
      this.projectile = (EntityProjectileBase) projectile;
    }
    else {
      this.projectile = null;
    }
  }
}