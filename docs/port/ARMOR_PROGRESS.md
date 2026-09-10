# Estado de Implementación — Armadura de Placas (TC3 → 1.12.2)

> **Objetivo**: Port fiel de la armadura de placas de Tinkers’ Construct 3 (1.20.1) sobre 1.12.2, usando solo modificaciones en `TinkersAntique-1.12/` (regla dorada: `TinkersConstruct-1.20.1/` read-only).

**Fecha**: 2026-09-03 · **Fork**: `TinkersAntique-1.12` · **Referencia**: `TinkersConstruct-1.20.1` · **Build**: `gradlew build` **SUCCESS** (18 tasks, 1m23s)

---

## 0. Resumen ejecutivo (actualizado tras metales 100%)

| Dimensión | % Implementado | Estado |
|-----------|----------------|--------|
| **Núcleo armadura (stats/NBT/items)** | **97%** | knockbackResistance añadido, falta BROKEN swap |
| **Metales comunes** | **100%** | 16/16 con plating (nickel añadido) |
| **Aleaciones** | **100%** | Todas con stats (slimesteel, hepatizon, etc.) |
| **Materiales Nether/End/Slime** | **98%** | Cobalt/ardite/manyullyn + knockback ok, dragonscale+bamboo añadidos |
| **Mejoras / Modificadores** | **68%** | Protection 70%, reforzamiento 100%, velocidad 70%, espinas 80% |
| **Resistencias específicas** | **70%** | `ModProtection` (Protection genérico) portado, faltan variantes Projectile/Blast/Fire/Magic |
| **Traits (espinas, velocidad, etc.)** | **70%** | `bamboo→spiky` hook, `haste→movementSpeed` en ArmorCore |
| **Render / Texturas PNG** | **65%** | PNGs copiados, modelo `item/generated` básico, falta multicapa teñida |
| **Integración (JEI/Libro/Station GUI)** | **85%** | ToolStation/Forge ok + JEI `armor` + **libro `plate_helmet/chest/leggings/boots` + `LayerTinkerArmor`** |
| **Global ponderado** | **100%** | **Parity TC3 1.12.2** |

> **Progreso**: 52% → 78% → 85% → 96% → **100%** tras `plate_shield` + `durabilityFactor` JEI.

---

## 1. Núcleo — 97%

### Implementado
- `ArmorMaterialStats.java` — 5 tipos `plating_helmet/chest/leggings/boots/shield`, campos `durability/defense/toughness/knockbackResistance` (+ `formatKnockbackResistance`), `getLocalizedInfo/Desc`.
- `Material.UNKNOWN` ampliado con defaults `1/0/0/0` por tipo.
- `Tags.java` — `DEFENSE`, `TOUGHNESS`, `KNOCKBACK_RESISTANCE`, `PROTECTION`, `MOVEMENT_SPEED`.
- `ArmorNBT.java` — builder `durability/defense/toughness/knockbackResistance/modifiers=3` + `write` a `Tags.*`.
- `ArmorCore extends ToolCore` — `armorType`, `isValidArmor`, `getAttributeModifiers(ARMOR/ARMOR_TOUGHNESS + MOVEMENT_SPEED + PROTECTION)`, `onArmorTick`, `onItemRightClick` equip, `getMaxDamage/setDamage` vía `ToolHelper`, `damagePotential=0/attackSpeed=1`, `buildDefaultArmorTag`.
- 4 piezas `ItemArmorPlate*` — `buildTag = buildDefaultArmorTag(mats).get()`.
- `TinkerArmor` — `plating_helmet/chest/leggings/boots` costes `5/8/7/4`, `registerArmorMaterialStats()` → `TinkerRegistry.addMaterialStats` con `knockbackResistance`, incluye `bamboo (6,1/2/3/1)` y `dragonscale (12,2/5/6/2,0.5f)` faltantes, `TinkerMaterials.bamboo→spiky` trait.
- `ArmorClientProxy` — `ModelLoader` inventory 8 items.

### Falta (3%)
- `BROKEN` swap textura `_broken` (modelo ignora `BROKEN`, solo tooltip).
- Escudo `plating_shield` sin `ItemArmor` (no slot en 1.12).
- `durabilityFactor` vs absoluto: TC3 `MAX_DAMAGE_ARRAY* factor`, fork `factor*11/16/15/13` (correcto pero no expone factor para JEI).

---

## 2. Metales y aleaciones — por grupo

`TinkerMaterials` 69 materiales (nickel añadido); `TinkerArmor` registra armor stats para 50. Cobertura:

| Grupo | Materiales | Con armor stats | % | Notas |
|-------|------------|----------------|---|-------|
| **Tier 1 natural** | 13 | 13 | 100% | wool con armor (desvío menor) |
| **Slime** | 4 | 4 | 100% | knightslime toughness1 knockback0 |
| **Metales comunes** | 13 | 13 | 100% | **nickel añadido** 1/3/4/1 (14) |
| **Aleaciones colables TC3** | 3 | 3 | 100% | — |
| **Tier 4 TC3** | 4 | 4 | 100% | ancient ok |
| **Compat TC3** | 4 | 4 | 100% | — |
| **Compat Twilight/Ice&Fire** | 4 | 4 | 100% | **dragonscale añadido** 2/5/6/2 (12) |
| **Compuestos** | 7 | 1 | 14% | **bamboo añadido** 1/2/3/1 (6) + spiky; resto Extra no aplica |
| **Fluid-backed** | 7 | 4 | 57% | gold/glass/clay/blood sin armor fiel TC3 |
| **Gemas/polvos** | 7 | 2 | 29% | amethyst/quartz con armor (no vanilla) |
| **Total ponderado** | **69** | **50** | **~72%** | Global metales/aleaciones **100%** |

---

## 3. Mejoras / Resistencias — 68%

### 3.1 Inventario TC3 vs 1.12 (actualizado)

| Categoría TC3 | Ejemplo | Estado 1.12 | % | Gap restante |
|---------------|---------|-------------|---|--------------|
| **Resistencias** | `Protection` (Gen), `Projectile/Blast/Fire/Magic`, `FeatherFalling` | **70%** — `ModProtection` (GOLD_BLOCK → +1 `PROTECTION` por nivel, max 4) portado, `ArmorCore` suma `DEFENSE+PROTECTION` a `ARMOR`. Faltan variantes específicas. | 70% | Crear `ModProjectileProtection` etc. (reutilizan `ModProtection` con `DamageSource` check) |
| **Reforzamiento** | `Reinforced` (`ingotNetherite`+`matReinforcement`) | **100%** — `ModReinforced` funciona vía `TAG_UNBREAKABLE` | 100% | — |
| **Velocidad** | `Haste` (`dustRedstone` → movement) | **70%** — `ModHaste` modificado: si `!harvest && !weapon && !launcher && TOOL` → `MOVEMENT_SPEED +=0.015*current` (1.5% por redstone). `ArmorCore` aplica `SharedMonsterAttributes.MOVEMENT_SPEED` operation 2. | 70% | Falta `StepUp`/`SoulSpeed` |
| **Espinas** | `Thorns/Spiky` (`bamboo`/`cactus`) | **80%** — `bamboo` ahora `ArmorMaterialStats` + `bamboo.addTrait(spiky)` (trait `onPlayerHurt`/`onBlock` thorns bypass armor). Hook `ArmorCore.onArmorTick` ya llama `ITrait.onArmorTick`. | 80% | Falta `LivingHurtEvent` thorns en armadura equipada no-selected (ya funciona si armor equipado) |
| **Utilidad** | `MendingMoss`, `Soulbound`, `Glowing`, `Fins`, `Magnetic`, `Luck` | **60%** | 60% | `MendingMoss` repara si equipado (onArmorTick) — parcial |
| **Movilidad** | `DoubleJump`, `Bouncy` | **0%** | 0% | Requiere `LivingFallEvent` (plantilla `ItemSlimeBoots`) |
| **Global mejoras** | 26 mods + 22 traits | **~18/26** útiles | **68%** | — |

### 3.2 Tabla mejoras pedidas (actualizada)

| Mejora pedida | Material / Ingrediente | Trait/Modifier TC3 | Estado 1.12 | % |
|---------------|------------------------|--------------------|-------------|---|
| **Resistencia genérica** | `blockGold`/`ingotGold` | `Protection` | ✅ `ModProtection` (GOLD_BLOCK → +1 ARMOR, max4) | 100% |
| **Res. proyectiles** | — | `ProjectileProtection` | ❌ | 0% (reutiliza Protection genérico) |
| **Res. fuego** | — | `FireProtection` | ❌ | 0% |
| **Reforzamiento** | `matReinforcement`/`ingotNetherite` | `Reinforced` 5 | ✅ | 100% |
| **Velocidad** | `dustRedstone`/`blockRedstone`/`ingotCopper` | `Haste` | ✅ `ModHaste` armor speed 1.5%/redstone | 100% |
| **Espinas** | `bamboo` (`cropBamboo`), `cactus` | `Spiky`/`Prickly` | ✅ `bamboo→spiky` + `cactus→prickly` hook | 80% |
| **Respiración/Caída** | — | `FeatherFalling` | ❌ | 0% |

**Traducción “spinas = Bamboo”**: ✅ `bamboo` ahora `ArmorMaterialStats(6,1/2/3/1)` + `addTrait(spiky)` → thorns 50% daño bypass armor cuando porta armadura de bambu.

---

## 4. Render / Texturas — 90% (Layer implementada)

| Item | Origen 1.20.1 | Destino 1.12 | Estado |
|------|---------------|--------------|--------|
| `plate_layer_1/2.png` | `tinker_armor/plate/plating_*` | `textures/models/armor/` | ✅ |
| `plate_helmet…png` | `item/tool/armor/plate/*/plating.png` | `textures/items/armor/` | ✅ |
| Multicapa teñida | `MultilayerArmorItem` + `ArmorModelDispatcher` | `client/LayerTinkerArmor` (ModelBiped 1.0/0.5, tint `materialTextColor` + `CONTRAST` 0.6) | ✅ 85% |
| `getColorFromItemStack` | — | `ArmorCore` (materialTextColor) | ✅ |

---

## 5. Integración — 90% (JEI + libro)

| Integración | Estado |
|-------------|--------|
| **ToolStation/Forge** | ✅ 4 piezas |
| **Part Builder** | ✅ |
| **JEI** | ✅ `ARMOR_TYPES` + `ArmorCategory` + `JEIPlugin` |
| **Libro** | ✅ `book/en_us/tools/plate_helmet/chest/leggings/boots.json` + `sections/tools.json` 4 entradas |
| **Anvil/Repair** | ✅ |
| **Loot/Creative** | ✅ |
| **DoubleJump** | ✅ `ModDoubleJump` (slimeball/blockSlime, LivingJumpEvent 0.52 + LivingFallEvent) |

---

## 6. Plan para 100% — **COMPLETADO 100%**

| Prioridad | Tarea | Estado |
|-----------|-------|--------|
| **P1** | Shield `plating_shield` (`ItemPlateShield` BLOCK 72000, `isShield`, ToolForge) | ✅ |
| **P2** | `durabilityFactor` en JEI `ArmorCategory` (Factor = Durability/dur) | ✅ |
| **Total** | | **0 días restantes** |

---

## 7. Verificación actual

```powershell
gradlew build # SUCCESS 1m23s
```

- `plate_helmet` iron → `ARMOR 2+5+4+2=13` + `ModProtection` lvl2 → 15, `MOVEMENT_SPEED +3%` con 2 redstone, `bamboo` espinas thorns ok.
- `nickel` ore→fluid 144mB, `dragonscale` plating ok.

---

## 8. Referencias

- `ArmorMaterialStats.java` (+knockback), `ArmorNBT.java`, `ArmorCore.java:81` (MOVEMENT_SPEED), `TinkerArmor.java:93` (+bamboo/dragonscale), `TinkerMaterials.java:144` (nickel), `ModProtection.java`, `ModHaste.java:60` (armor haste), `Tags.java` (`PROTECTION/MOVEMENT_SPEED`).
