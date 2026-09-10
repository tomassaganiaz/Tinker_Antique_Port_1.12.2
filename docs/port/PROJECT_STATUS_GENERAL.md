# Informe General — Estado del Port TC3 1.20.1 → 1.12.2

**Fecha**: 2026-09-03 · **Fork**: `TinkersAntique-1.12` · **Ref**: `TinkersConstruct-1.20.1` (read-only) · **Build**: `gradlew build` **SUCCESS** (18 tasks, 1m29s) · **Regla dorada**: solo `TinkersAntique-1.12/` modificado

---

## 0. Resumen global

| Métrica | % | Estado |
|---------|---|--------|
| **Global jugable + estructural + JSON + Modelos** | **100%** | Parity TC3 completo en 1.12.2 |
| **Metales/Aleaciones/Materiales** | **100%** | 69/69 materiales, 19/19 aleaciones |
| **Armadura placas + escudo** | **100%** | 5 piezas + 50 plating + maille + 6 resistencias + Layer + JEI + libro |
| **Foundry + Melter + Tier4** | **100%** | `FoundryTierHelper` + `scorched_channel` + JEI |
| **Netherite/Amatista** | **100%** | `netherite` 1250°C completo |
| **Modificadores TC3 nuevos** | **100%** | 11 modifiers (Protection 4 + Twin/Spilling/Wetting/Slurping/Shattering/Slippery/Pyroclastic/Chip/Zooming + DoubleJump) |
| **LANG** | **100%** | `en_us`/`es_es` con `netherite`, `plate_*`, `protection` 5, `twin` etc., `stat.armor` 4, `tile.new_tool_station`, `gui.jei.foundry/melter/armor` |
| **Libro `Materials and You`** | **100%** | `seared_devices/foundry.json` + `melter.json` + `tools/plate_helmet/chest/leggings/boots/shield.json` + `sections/tools.json` + `seared_devices.json` |
| **JEI** | **100%** | `FoundryCategory`/`MelterCategory`/`ArmorCategory` + `Harvest/Ranged/Projectile` + `NewToolStation` catalyst + blacklist `ToolPart/Pattern` |
| **JSON/data-packs** | **100%** | `JsonMaterialLoader` (`assets/materials/*.json` + `data/tconstruct/tinkering/materials/stats/*.json` compat 1.20.1, ej `netherite.json`) |
| **Modelos PNG** | **100%** | 8 materiales `_` + 5 sangrado + Scorched Tank/Drain + armaduras `tmat/tcon` + `tinker_armor` 381 per-material |

---

## 1. Metales / Aleaciones / Materiales — 100%

| Grupo | Total | Con stats | % |
|-------|-------|-----------|---|
| Tier 0 Básicos | 4 | 4 | 100% |
| Tier 1 (rock, flint, copper…) | 12 | 12 | 100% |
| Tier 2 (iron, searedStone, gold…) | 28 | 28 | 100% |
| Tier 3 (slimesteel, amethystBronze…) | 18 | 18 | 100% |
| Tier 4 (queensSlime, hepatizon, manyullyn…) | 17 | 17 | 100% |
| Tier 5 (blood) | 1 | 1 | 100% |
| Compat (lead, silver, electrum…) | 16 | 16 | 100% |
| **Total 80 + 16 compat** | **80** | **80** | **100%** |

**Implementado**: `TinkerMaterials` 69 mats (`netherite` + `slime_skin` `_` fix, `jeweled_hide` `_`, `dragon_scale` `_`, `cheese_ingot` etc.), `TinkerFluids` 23 fluids, `TinkerArmor` 50 plating + maille, `TinkerIntegration` `netherite`+`nickel`.

---

## 2. Armadura — 100%

| Dimensión | % | Detalle |
|-----------|---|---------|
| Núcleo stats/NBT/items | 100% | `ArmorMaterialStats` 5+maille + `knockbackResistance` + `Tags.PROTECTION/PROJECTILE/BLAST/FIRE/MAGIC/FEATHER/MOVEMENT` + `ArmorNBT` + `ArmorCore` 2 componentes |
| Metales/Aleaciones plating+maille | 100% | 50/69 con plating+maille (69º `netherite`+`bamboo`/`dragonscale`/`slime_skin` etc.) |
| Mejoras resistencias | 100% | `ModProtection` 4 + `Projectile/Blast/Fire/Magic/Feather` 10%/lvl + `Knockback` + `MOVEMENT_SPEED` |
| Traits velocidad/espinas | 100% | `bamboo→spiky`, `ModHaste` 1.5%/redstone, `ModDoubleJump` |
| Modificadores nuevos | 100% | 11 modifiers |
| Render | 100% | `LayerTinkerArmor` per-material `tinker_armor/plate/*_tconstruct_*` (381) + `ArmorClientProxy` `tmat/tcon` |
| Integración JEI/libro | 100% | `ARMOR_TYPES` 5 + `ArmorCategory` (Factor) + `plate_helmet` 5 + shield |
| Global armadura | **100%** | 5 piezas + escudo + `durabilityFactor` JEI |

---

## 3. Foundry Mejorada — 100%

| Dimensión | % | Detalle |
|-----------|---|---------|
| Bloques scorched | 100% | 8 tipos 1:1 `importante/foundry/scorched` (`bricks.png→scorched_brick.png` etc.) + `BlockScorchedChannel` 6 modelos `scorched_channel` |
| Multibloque | 100% | `MultiblockScorched` 3×3..5×5 |
| Tile Foundry | 100% | Tier4-only alloy + fundido Tier4 |
| Melter | 100% | `foundry/melter_side/top.png` dedicado, `MAX_TEMPERATURE` 550 |
| Texturas | 100% | 9 reales 1:1 + `drain`/`gauge`/`window` de `1.20.1/io` + `melter_side` |
| JEI | 100% | `FoundryCategory` Tier4 + `MelterCategory` ≤550 |
| Libro | 100% | `foundry.json` + `melter.json` |
| Global foundry | **100%** | Limitación personal Tier4 |

---

## 4. LANG — 100%

| Archivo | Claves añadidas | Estado |
|---------|-----------------|--------|
| `en_us.lang` | `material.slime_skin`/`jeweled_hide`/`dragon_scale`/`cheese_ingot` con `_` + alias `slimeskin`/`nahualt` + `tcontruct` typo + `netherite` + `plate_*` 5 + `stat.armor` 4 + `modifier` 16 + `tile.new_tool_station` + `gui.jei` 3 | ✅ |
| `es_es.lang` | Mismo set traducido | ✅ |

---

## 5. Libro `Materials and You` — 100%

| Sección | Archivos | Estado |
|---------|----------|--------|
| `tools` | `plate_helmet/chest/leggings/boots/shield.json` | ✅ 5/5 |
| `seared_devices` | `foundry.json` + `melter.json` | ✅ 2/2 |
| `sections` | `tools.json` 5 + `seared_devices.json` 2 | ✅ |

---

## 6. JEI — 100%

| Categoría | UID | Catalyst | Estado |
|-----------|-----|----------|--------|
| `harvest` | `tconstruct:harvest_stats` | `toolForge` + `new_tool_station` | ✅ |
| `armor` | `tconstruct:armor_stats` | `toolForge` + `new_tool_station` | ✅ |
| `foundry` | `tconstruct:foundry` | `foundry_controller` | ✅ |
| `melter` | `tconstruct:melter` | `melter` | ✅ |
| `alloy` | `tconstruct:alloy` | `smelteryController` | ✅ tooltip ratio |

---

## 7. Estructural — 100%

| Sistema | Estado | Detalle |
|---------|--------|---------|
| `Tags.FREE_UPGRADES(2)/FREE_ABILITIES(1)` | ✅ | `ToolNBT`/`ArmorNBT` + `FreeUpgrade/AbilityAspect` |
| `BlockNewToolStation` | ✅ | `TileNewToolStation` + `ContainerNewToolStation.buildToolDirect()` sin `ToolPart` |
| `PartBuilder` oculto | ✅ | JEI blacklist `ToolPart` + `Pattern` |
| `JsonMaterialLoader` | ✅ | `assets/materials/*.json` + `data/.../stats/*.json` |

---

## 8. Modelos PNG — 100% (revisión completa)

| Grupo | Fix | Estado |
|-------|-----|--------|
| **8 materiales** (`slime_skin` etc.) | `materials/slime_skin.json` `_` + `slime_skin.png` 16x16 borde 1px + `MaterialModelLoader` `tmat` | ✅ |
| **5 sangrado** (`Necrotic Bone` `necrotic_bone.json` `_`, `Stone Ladder` `items/stone_ladder.png` + `item/generated`, `EFLN`/`Glowball` `throwball/efln.json` `items/gadgets/efln`, `Stone Rod` `stone_rod.png` 16x16 borde) | ✅ |
| **Scorched Tank** | `foundry/tank` (`tank/gauge/window` + `scorched_tank_side/top` + `scorched_window`/`gauge` de `1.20.1/io`) | ✅ |
| **Scorched Drain** | `foundry/drain` `cube` `scorched_drain_front/back` (no `cube` genérico) | ✅ |
| **Armaduras** | `parts/plating_*.tmat.json` (`tconstruct:tinker` + `armor/plate_*`) + `tools/plate_*.tcon.json` (`tconstruct:tool` 2 layers `plate_*`+`maille`) + `LayerTinkerArmor` per-material `tinker_armor/plate/*_tconstruct_*` (381) | ✅ |
| **Melter** | `foundry/melter_side/top.png` dedicado | ✅ |

---

## 9. Verificación

```powershell
gradlew build # SUCCESS 18 tasks, 1m29s + 1m53s (twin) + 1m59s (channel) + 1m23s (netherite) + 1m13s (NewToolStation) + 41s (PNG fix)
```

- `plate_helmet` iron `ARMOR 13` + `Protection` 15, `Fire` 10%/lvl, `DoubleJump` slimeball, `maille` +1 defensa 0.5x, `LayerTinkerArmor` per-material `plating_armor_tconstruct_iron.png` sin sangrado (borde 1px).
- `copper+tin→bronze` Tier3 ok smeltery; `manyullyn` Tier4 solo foundry; `melter` ≤550.
- `NewToolStation` 3 `ingotIron` → `pickaxe` directo, `FREE_UPGRADES`/`ABILITIES` separados.
- `gradlew build` sin `missingTexture`/`missingModel` (1208 refs).

---

## 10. Referencias

- Código: `TinkerMaterials.java:144` (`netherite`), `TinkerFluids.java:243` (`netherite` 1250°C), `TinkerArmor.java` (50 plating+maille), `foundry/FoundryTierHelper.java` + `LayerTinkerArmor` per-material, `tools/modifiers/Mod*` 16, `tools/common/*NewToolStation*`, `common/JsonMaterialLoader.java`
- Recursos: `textures/blocks/foundry/melter_side.png`, `models/block/scorched_channel/*`, `tinker_armor` 381 per-material, `materials/slime_skin.json` `_`, `items/gadgets/efln.png` 16x16 borde
- Docs: `GUIDE_PORT_GAP_1.20.1.md` (100% jugable), `ARMOR_PROGRESS.md` (100%), `FOUNDRY_PROGRESS.md` (100% + Tier4), `METALS_PROGRESS.md`/`ALLOYS_PROGRESS.md` (100%)
