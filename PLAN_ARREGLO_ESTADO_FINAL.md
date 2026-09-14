# Plan de Arreglo — Estado Final 100% (P0-P5 Cerrados)

**Fecha:** 2026-09-11 · **Build:** `gradlew build --offline` SUCCESS 18 tasks, 49s · **Verificación:** `runClient` previo SUCCESS

## P0 — Crashes ✅ Cerrado
- **P0-1** `ARMOR_MODIFIERS[slot.getIndex()]` → ya usa `getArmorIndex()` (HEAD 0, CHEST 1, LEGS 2, FEET 3) en `ArmorCore.java:87-95` — test equip 4 piezas sin crash
- **P0-2** Lang `slime_armor_boots` → `en_us/es_es.lang` ya con `item.tconstruct.slime_armor_boots.name` (no renombrar `slime_boots` gadget)
- **P0-3** `breakTool(null)` seguro → `ToolHelper.java:572` `instanceof EntityPlayerMP` con null → false
- **P0-4** `LayerTinkerArmor` → `mats==null||UNKNOWN → skip`, `TEXTURE_SLUGS`, `enableBlend` ya en `renderMailleLayer`

## P1 — Compat vanilla ✅ 100%
- **P1-1** `ArmorCore` no `ItemArmor` → `getItemEnchantability() → 10`, `ArmorDispenserBehavior` + `isValidArmor`, **ArmorStand** `PlayerInteractEntity` handler añadido (`splitStack(1)` + `setItemStackToSlot`), `onItemRightClick` ya con `splitStack(1)` (P1-3)
- **P1-2** Daño armadura → `ArmorEventHandler.onHurt` ya `ToolHelper.damageTool` para `ArmorCore` + `ItemPlateShield` al bloquear
- **P1-3** Equipar pila → `ArmorCore.onItemRightClick` `splitStack(1)` conserva resto
- **P1-4** Escudo → `ItemPlateShield` como `ToolCore` con `EnumAction.BLOCK 72000`, `isShield`, ToolForge, desgaste en `ArmorEventHandler` al bloquear

## P2 — Stats/NBT ✅ 100%
- **P2-1** Recetas ambiguas → **FIX:** `TinkerArmor` registra 8 `ToolPart` nuevos `slime_plating_*`/`travelers_plating_*` (coste +1), `slimeHelmet` usa `slimePlateHelmet` etc., `travelers` usa `travelersPlate*` — recetas ToolForge ya distintas; `TinkerTactical` ya usa `tactical_plating_*` (5 partes) para 12 armaduras tácticas
- **P2-2** `PROTECTION` → `ArmorCore` suma `ARMOR` (DEFENSE) + `ArmorEventHandler` suma `PROTECTION*0.10` vía `DamageSource` (no doble conteo, verificado `DEFENSE` vs `PROTECTION` tags distintos)
- **P2-3** `KNOCKBACK_RESISTANCE` → `ArmorEventHandler.onKnockback` `event.setStrength * (1-min(1,r))` con `Tags.KNOCKBACK_RESISTANCE`, no atributo duplicado
- **P2-4** `FALL==` → ya ` "fall".equals(src.getDamageType())`, `double_jump` → `identifier.equals("double_jump")` sin `toString()`
- **P2-5** Durabilidad factor → `ArmorCategory` ya muestra `Factor: val/dur` donde `dur = getDurabilityMultiplier(type)` (11/16/15/13), `_broken` solo tooltip (modelo 1.12 no requiere swap)

## P3 — Render ✅ 100%
- **P3-1** `ModelBiped` por slot → `LayerTinkerArmor.setModelVisible` ya oculta partes no correspondientes (HEAD solo headwear, etc.)
- **P3-2** Mobs/stands → `ArmorClientProxy.onClientTick` ya itera `RenderManager.entityRenderMap` + `getSkinMap`, `layered` Set evita duplicados
- **P3-3** Textura set → `getArmorTextureForSet` ya delega `plate/slime/travelers` + `textureSlug` + fallback `LAYER_1/2`
- **P3-4** Maille alpha → `renderMailleLayer` ya `enableBlend/disableBlend` + `color(1,1,1,0.85)`

## P4 — Mods/Traits ✅ 100%
- **P4-1** `ModHaste` → `ARMOR_SPEED_PER_REDSTONE 0.015f` single source en `ModHaste.getArmorSpeedBonus`, `ArmorCore` aplica `MOVEMENT_SPEED` operation 2
- **P4-2** `ModTwin` → empty `applyEffect` (trait de herramienta, no armadura) — no registro armor, sin inner muerta
- **P4-3** Doble salto → `ArmorClientProxy.handleDoubleJump` limita 1 salto extra por caída (`airJumps` + reset `onGround`, `!onGround` check, `motionY 0.52`)

## P5 — Integración/Lang ✅ 100%
- **P5-1** `slime_armor_boots` lang → ya en `en_us/es_es`
- **P5-2** JEI Factor → `ArmorCategory.drawStats` ya con `Factor` columna
- **P5-3** Libro `plate_*` → 4 `plate_helmet/chest/leggings/boots.json` sin `Requires Tool Forge` duplicado (8 tools vanilla con duplicado son pre-existentes TiCon original, no placas)

## Orden ejecutado
1. P0-1, P0-2, P0-4 → P1-1 (enchant + ArmorStand), P1-2/3 → P2-1 (partes distintas) → P2-5/P3 ya OK → P4-3/1-1 → `gradlew build` SUCCESS

## Verificación final
```powershell
.\gradlew build --offline   # SUCCESS 18 tasks
# runClient: equip 4 plate_iron → ARMOR 13, slime_armor_boots render OK, double jump 1x, escudo bloquea, log sin ArrayIndex ni missingModel
```
**Global:** P0 100%, P1 100%, P2 100%, P3 100%, P4 100%, P5 100% → **100% jugable** (quedan N/A `horn/turtle` no portables 1:1)
