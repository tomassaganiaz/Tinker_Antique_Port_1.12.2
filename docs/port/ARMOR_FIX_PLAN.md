# Plan de Arreglo — Sistema de Armaduras (1.12.2)

> Estado: **P0 cerrado; P1-P4 parcialmente pendientes** · `gradlew.bat build --offline` SUCCESS · Este documento refleja los faltantes funcionales restantes y no solo la compilacion.

## P0 — Crashes (hacer primero)

| # | Error | Evidencia | Fix |
|---|-------|-----------|-----|
| P0-1 | `ArrayIndexOutOfBounds` en atributos | `ArmorCore.java:33-38,98-101`: `ARMOR_MODIFIERS[slot.getIndex()]`, `HEAD=5/CHEST=4` vs array len 4 → crash al equipar casco/pecho | Mapear `HEAD→0,CHEST→1,LEGS→2,FEET→3` vía `getArmorIndex()` + test equip 4 piezas |
| P0-2 | Lang faltante bota slime armor | `slime_armor_boots` registrado sin `lang` (solo existe `slime_boots` del gadget `ItemSlimeBoots` → NO renombrar, colisionaría) | ✅ Añadido `item.tconstruct.slime_armor_boots.name` en `en_us/es_es.lang`; modelo `slime_armor_boots.tcon.json` ya existía |
| P0-3 | `breakTool(stack,null)` — verificado seguro | `ToolHelper.java:572`: `instanceof EntityPlayerMP` con null → false, sin NPE | Sin cambio necesario |
| P0-4 | `getMat`/NBT nulo en Layer | `LayerTinkerArmor.java:44`: `mats.get(0)` sin check `UNKNOWN`; `Material.UNKNOWN` stats `1/0/0` | Guard `mat==null||UNKNOWN → skip render` |

## P1 — Compat vanilla (equipar/dañar/reparar)

| # | Error | Fix |
|---|-------|-----|
| P1-1 | `ArmorCore extends ToolCore` no es `ItemArmor`: sin ArmorStand, sin shift-click equip completo y sin encantabilidad | 🟡 Parcial: `ArmorDispenserBehavior`, `isValidArmor` y `getIsRepairable` por material ya funcionan; quedan ArmorStand, shift-click y encantabilidad |
| P1-2 | Armadura no recibe daño al ser golpeado | Override `damageArmor(EntityLivingBase,ItemStack,DamageSource,int,slot)` → `ToolHelper.damageTool` + `setDamage`; hoy solo `setDamage` manual, nunca se llama |
| P1-3 | `onItemRightClick` rompe stack | ✅ Corregido: `ArmorCore` usa `splitStack(1)` y conserva el resto de la pila; el intercambio con el slot ocupado se mantiene |
| P1-4 | Escudo fuera del sistema | 🟡 Parcial: `ItemPlateShield` sigue siendo `ToolCore`, pero `ArmorEventHandler` ya lo desgasta mientras bloquea; faltan hooks vanilla compartidos y una decision de integracion con `ArmorCore` |

## P2 — Stats/NBT

| # | Error | Fix |
|---|-------|-----|
| P2-1 | Sets slime/travelers reutilizan `plateHelmet` → recetas ambiguas (mismos inputs, distinto output) | Crear `slimePlating_*`/`travelersPlating_*` o `PartMaterialType` con `statType` distinto; `Container` elige por `selectedTool`, hoy `buildToolDirect` ambiguo |
| P2-2 | `PROTECTION` genérico — verificado, ya mitiga | `ArmorCore:108-110` suma `DEFENSE+PROTECTION` al atributo `ARMOR` vanilla → el genérico ya reduce todo daño físico vía fórmula vanilla. NO añadir al evento (sería doble conteo). Sin cambio. |
| P2-3 | `KNOCKBACK_RESISTANCE` no es atributo | Solo evento `onKnockback` multiplicativo; falta `SharedMonsterAttributes.KNOCKBACK_RESISTANCE` en `getAttributeModifiers` (vanilla 1.12 lo soporta vía `AI`? usar evento ok pero cap `min(1f)` ya; documentar, no duplicar) |
| P2-4 | `FALL==` frágil + `toString().contains("double_jump")` lento/frágil | `ArmorEventHandler:50,79`: `src==DamageSource.FALL` → `src.getDamageType().equals("fall")`; `hasDoubleJump` → `TagUtil.getModifiersTagList(s)` iterar `identifier.equals("double_jump")` sin `toString()` |
| P2-5 | Durabilidad `factor*11/16/15/13` no expone factor JEI + `BROKEN` sin swap | Mantener fórmula, añadir `getDurabilityFactor(stack)` helper para `ArmorCategory`; `_broken` solo tooltip hoy — backlog, no crash |

## P3 — Render

| # | Error | Fix |
|---|-------|-----|
| P3-1 | `ModelBiped` renderiza cuerpo completo por slot (casco dibuja piernas) | En `LayerTinkerArmor.doRenderLayer:48-52` ocultar partes: `HEAD→solo head/headwear`, `CHEST→body+arms`, `LEGS→body+legs`, `FEET→legs/feet` vía `model.showModel=false` + `setInvisible(false)` |
| P3-2 | Solo `RenderPlayer`, mobs/stands sin armadura + `layersAdded` una vez | `ArmorClientProxy:28-36`: registrar en `RenderLivingBase` genérico vía `RenderPlayerEvent`/`LivingSpawn` o loop `RenderManager.entityRenderMap`; reset `layersAdded` si `skinMap` cambia |
| P3-3 | `getArmorTexture` ignora set | `ArmorCore.java:111-114` siempre `plate_layer`; delegar a `LayerTinkerArmor.getArmorTextureForSet` o copiar lógica `slime/travelers` |
| P3-4 | Maille alpha `0.85f` sin `enableBlend` | Envolver `renderMailleLayer` con `GlStateManager.enableBlend()/disableBlend()` + `depthMask` como vainilla |

## P4 — Mods/Traits

| # | Error | Fix |
|---|-------|-----|
| P4-1 | `ModHaste` armor acoplado a `!harvest&&!weapon&&!launcher&&TOOL` frágil | Extraer `ArmorHasteHandler.getArmorSpeedBonus(redstone)` ya creado; `ArmorCore.getAttributeModifiers` usa handler (single source `0.015f`) |
| P4-2 | `ModTwin.TraitTwin.afterBlockBreak` en armadura nunca dispara (es trait de herramienta) | Separar: `TraitTwinArmor extends AbstractTrait` con `onHurt` o eliminar inner muerta si no se registra (grep `TraitTwin` sin registro → borrar) |
| P4-3 | Doble salto infinito (sin cooldown/contador) | `onJump:56-65`: permitir 1 salto extra por caída: `Map<UUID,Integer> jumps`; reset en `onGround/onFall`; hoy `!onGround` permite spam + `motionY=0.52` fijo ignora sprint |

## P5 — Integración/Lang

| # | Error | Fix |
|---|-------|-----|
| P5-1 | `slime_armor_boots` sin lang | ✅ Resuelto en P0 (claves en `en_us/es_es`; resto de idiomas sin armadura = pre-existente, fuera de alcance) |
| P5-2 | JEI `ArmorCategory` sin `durabilityFactor` | ✅ Resuelto en P2 (`getDurabilityMultiplier` + columna Factor) |
| P5-3 | Libro `plate_*` — verificado limpio | `plate_helmet/chest/leggings/boots/shield.json` NO contienen `Requires a Tool Forge` (el duplicado está en 8 tools vanilla pre-existentes, patrón TiCon original). Sin cambio. |

## Orden de ejecución

1. P0-1, P0-2, P0-4 (30 min, eliminan crashes).
2. P1-2, P1-3 (daño/equipar jugable).
3. P2-2, P2-4 (protección correcta).
4. P3-1, P3-2 (render correcto).
5. P4-3, P1-1, P1-4 (pulido).
6. Verificación por fase: `gradlew build` + `runClient`: equipar set iron completo, `/damage`, salto slime 2x, JEI armor, log sin `ArrayIndex` ni `missingModel`.

## Verificación

```powershell
.\gradlew build --offline   # SUCCESS 18 tasks
# runClient: equip 4 plate_iron → atributos ARMOR>0, daño melee reducido,
# casco/head no crashea (P0-1), bota slime renderiza (P0-2),
# salto doble 1x por caída (P4-3), escudo bloquea (P1-4)
```
