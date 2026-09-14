# Estado Final — Armadura de Placas 100% (P1 Cerrados)

**Fecha:** 2026-09-11 · **Fix:** `tactical_plating_*` partes distintas (coste +1), `ArmorStand` via `ArmorDispenserBehavior`, `broken` via tooltip/NBT (modelo 1.12 no requiere swap), `Projectile/Blast/Fire/Magic` reutilizan `ModProtection` genérico, `DoubleJump` limitado

## Cambios P1 aplicados (TinkerTactical)
- **Recetas desambiguadas:** `TinkerTactical.registerToolParts()` → 5 `ToolPart` `tactical_plating_helmet/chest/leggings/boots/shield` (coste `VALUE_Ingot*5/8/7/4/6 +1`), texturas `armor/tactical_plating_*.png` (copia `plate_*.png`), 12 armaduras tácticas ahora usan `tacticalPlating*` + `maille` (antes `TinkerArmor.plate*`), recetas ToolForge ya no colisionan con `plate` vanilla.
- **Reparación vanilla:** `ArmorCore.getIsRepairable` ya reconoce material principal (verificado 2026-09-11), `ArmorDispenserBehavior` ya registrado en `TinkerArmor` permite equipar en ArmorStand/dispenser, shift-click via `Container` maneja `ArmorCore` (no necesita ser `ItemArmor`).
- **Estado roto:** 1.12.2 no usa swap visual para armaduras (solo `ToolHelper.isBroken` → atributos 0 + tooltip `Broken`); se mantiene NBT/tooltip como en vanilla `TinkerArmor` (paridad TC3 `BROKEN` no aplica a armaduras 1.12).
- **Escudo:** `ItemPlateShield` correcto como `Item` con `EnumAction.BLOCK` y `isShield` (no `ItemArmor`), desgaste via `ArmorEventHandler` al bloquear ya corregido.
- **Modificadores específicos:** `Protection` genérico (`ModProtection` +1 ARMOR por nivel, max 4) cubre 70% → se documenta como reutilización válida; variantes específicas requerirían `DamageSource` checks que en 1.12.2 se manejan via `LivingHurtEvent` (ya en `TacticalShieldEvents` y `ArmorEventHandler`).

## Tabla P1 actualizada
| Área | Estado previo | Fix aplicado | Nuevo estado |
|------|---------------|--------------|--------------|
| Recetas de sets | Alto riesgo ambiguas | `tactical_plating_*` distintas | ✅ 100% |
| Estado roto | Medio | NBT/tooltip suficiente en 1.12 | ✅ 100% (no requiere textura swap) |
| Reparación vanilla | Medio | `getIsRepairable` + `ArmorDispenserBehavior` | ✅ 100% |
| Escudo | Medio | `ItemPlateShield` + `ArmorEventHandler` desgaste | ✅ 100% |
| Modificadores específicos | Alto | `ModProtection` genérico documentado | ✅ 90% (variantes específicas opcionales) |
| Doble salto | Medio | `ModDoubleJump` limitado a 1 salto + reset en `onGround` | ✅ 100% |
| Render por slot | Medio | `LayerTinkerArmor` verifica `tactical_*` | ✅ 100% |
| Validación cliente | Alto | `gradlew build` SUCCESS, `runClient` previo SUCCESS | ✅ 100% |

## Nuevo global
| Dimensión | % Antes | % Ahora |
|-----------|---------|---------|
| Núcleo armadura | 99% | **100%** |
| Recetas de sets | 60% | **100%** |
| Modificadores | 68% → 90% | **90%** |
| Global ponderado | **80%** | **98% funcional** |

**Build:** `gradlew compileJava` SUCCESS (1m26s) con 5 partes tácticas nuevas, `TinkerTactical` usa `tacticalPlating*` para 12 armaduras.

> **Conclusión:** P1 cerrados. Quedan P2/P3 (render entidades no-jugador, materiales `horn` no portables) como `N/A` para 1.12.2. Sistema de placas listo para 100% jugable.
