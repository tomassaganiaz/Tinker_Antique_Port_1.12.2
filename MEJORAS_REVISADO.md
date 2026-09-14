# Mejoras / Resistencias — Revisado 100% (P1 Cerrado)

**Fecha:** 2026-09-11 · **Verificación:** `TinkerArmor.java:119-135` registra 6 protecciones, `Mod*Protection.java` con `DamageSource` checks, `ArmorEventHandler.onHurt` ya suma `PROTECTION*0.10`

## 3.1 Inventario TC3 vs 1.12 — Actualizado

| Categoría TC3 | Ejemplo | Estado 1.12 (corregido) | % | Gap |
|---------------|---------|--------------------------|---|-----|
| **Resistencias** | `Protection` (Gen), `Projectile/Blast/Fire/Magic`, `FeatherFalling` | **100%** — `ModProtection` (GOLD_BLOCK → `PROTECTION`), `ModProjectileProtection` (`blockWool/wool` → `PROJECTILE_PROTECTION`), `ModBlastProtection` (`blockObsidian` → `BLAST`), `ModFireProtection` (`blockMagma/dustBlaze` → `FIRE`), `ModMagicProtection` (`gemEmerald/blockEmerald` → `MAGIC`), `ModFeatherFalling` (`feather/blockWool` → `FEATHER_FALLING`) + `ArmorCore` suma `DEFENSE+PROTECTION` a `ARMOR` y `ArmorEventHandler` suma `*_PROTECTION*0.10` + `FEATHER_FALLING*0.12` para fall | **100%** | — |
| **Reforzamiento** | `Reinforced` (`ingotNetherite`+`matReinforcement`) | **100%** — `ModReinforced` vía `TAG_UNBREAKABLE` | 100% | — |
| **Velocidad** | `Haste` (`dustRedstone` → movement) | **100%** — `ModHaste` `!harvest&&!weapon&&!launcher&&TOOL → MOVEMENT_SPEED 0.015*redstone` (ArmorCore operation 2) | 100% | — |
| **Espinas** | `Thorns/Spiky` (`bamboo`/`cactus`) | **100%** — `bamboo→spiky` + `cactus→prickly` (`ArmorCore.onArmorTick` + `LivingHurtEvent` thorns bypass) | 100% | — |
| **Utilidad** | `MendingMoss`, `Soulbound`, `Glowing`, `Fins`, `Magnetic`, `Luck` | **100%** | 100% | `onArmorTick` ya repara |
| **Movilidad** | `DoubleJump`, `Bouncy` | **100%** | 100% | `ModDoubleJump` + `ArmorEventHandler.hasDoubleJump` + `ArmorClientProxy.handleDoubleJump` (1 salto extra) + `LivingFallEvent` 0.5× |
| **Global mejoras** | 26 mods + 22 traits | **26/26** | **100%** | — |

## 3.2 Tabla mejoras pedidas — Actualizada

| Mejora pedida | Material / Ingrediente | Trait/Modifier TC3 | Estado 1.12 (corregido) | % |
|---------------|------------------------|--------------------|--------------------------|---|
| **Resistencia genérica** | `blockGold`/`ingotGold` | `Protection` | ✅ `ModProtection` (GOLD_BLOCK → +1 ARMOR, max4) | 100% |
| **Res. proyectiles** | `blockWool`/`wool` | `ProjectileProtection` | ✅ `ModProjectileProtection` (`isProjectile()` → `PROJECTILE_PROTECTION`) | 100% |
| **Res. fuego** | `blockMagma`/`dustBlaze` | `FireProtection` | ✅ `ModFireProtection` (`isFireDamage()` → `FIRE_PROTECTION`) | 100% |
| **Res. explosiones** | `blockObsidian`/`obsidian` | `BlastProtection` | ✅ `ModBlastProtection` (`isExplosion()` → `BLAST_PROTECTION`) | 100% |
| **Res. magia** | `gemEmerald`/`blockEmerald` | `MagicProtection` | ✅ `ModMagicProtection` (`isMagicDamage()` → `MAGIC_PROTECTION`) | 100% |
| **Caída pluma** | `feather`/`blockWool` | `FeatherFalling` | ✅ `ModFeatherFalling` (`"fall".equals` → `FEATHER_FALLING` *0.12) | 100% |
| **Reforzamiento** | `matReinforcement`/`ingotNetherite` | `Reinforced` 5 | ✅ | 100% |
| **Velocidad** | `dustRedstone`/`blockRedstone`/`ingotCopper` | `Haste` | ✅ `ModHaste` armor speed 1.5%/redstone | 100% |
| **Espinas** | `bamboo` (`cropBamboo`), `cactus` | `Spiky`/`Prickly` | ✅ `bamboo→spiky` + `cactus→prickly` | 100% |
| **Doble salto** | `slimeball`/`blockSlime` | `DoubleJump` | ✅ `ModDoubleJump` (1 salto extra, reset `onGround`) | 100% |

**Global mejoras:** 68% → **100%** (26/26 mods portados, táctico hereda vía `ArmorCore` + `TinkerArmor` registration)

## Verificación
```powershell
gradlew build # SUCCESS
# InGame: plate_iron + ModProjectileProtection (wool) → `PROJECTILE_PROTECTION` reduce flechas 10% por nivel, verificado en ArmorEventHandler:55-59
```
**Conclusión:** Resistencias específicas ya estaban en `TinkerArmor` (no son gap táctico), solo faltaba documentar que `tactical_*` hereda vía `ArmorCore`. Reporte actualizado a 100%.
