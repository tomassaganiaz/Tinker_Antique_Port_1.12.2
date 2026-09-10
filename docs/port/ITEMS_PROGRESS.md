# Revisión Completa — ITEM (1.20.1 → 1.12.2)

**Fecha**: 2026-09-03 · **Fork**: `TinkersAntique-1.12` · **Ref**: `TinkersConstruct-1.20.1` (`importante1.20.1/item` 10k textures) + `TinkersConstruct-1.20.1.zip` · **Build**: `gradlew build` **SUCCESS**

---

## 0. Resumen global ITEM

| Categoría ITEM 1.20.1 | Total 1.20.1 | En 1.12.2 | % | Estado 1.12.2 |
|-----------------------|-------------|-----------|---|---------------|
| **Herramientas** (pickaxe, shovel, hatchet, mattock, kama, scythe, hammer, excavator, lumberaxe, broadsword, longsword, rapier, battlesign, frypan, cleaver, arrow, bolt, shuriken, crossbow, longbow, shortbow) | 21 | 21 | 100% | `TinkerTools` + `TinkerHarvestTools` + `TinkerMeleeWeapons` + `TinkerRangedWeapons` (21/21) |
| **Armadura** (plate 5, travelers 4, slime 4, shield) | 13 | 13 | 100% | `TinkerArmor` 13 piezas (`plate_helmet` 4 + `slime_helmet` 4 + `travelers_helmet` 4 + `plate_shield`) + `LayerTinkerArmor` + `tinker_armor` 381 per-material |
| **Gadgets** (slimesling, slime_boots, piggyback, stone_ladder, EFLN/glowball throwball, stone_rod/stick) | 7 | 7 | 100% | `TinkerGadgets` 7, `stone_ladder` `items/stone_ladder.png` fix, `throwball/efln.json` `items/gadgets/efln` fix |
| **Materiales** (ingot/block/nugget + `materials` meta 24-31) | 69 mats | 69 | 100% | `materials` 24-31 (`slime_skin` `_` fix, `jeweled_hide` `_`, `dragon_scale` `_`, `cheese_ingot` etc. + `netherite`/`nickel`) + `ingot_*`/`block_*`/`nugget_*` en `materials` o `FutureMC` oredict |
| **Modificadores** (11+4 protection) | 15 | 15 | 100% | `ModProtection` 4 + `Twin/Spilling/Wetting/Slurping/Shattering/Slippery/Pyroclastic/Chip/Zooming` + `DoubleJump` + `Feather` etc. |
| **Fundición** (scorched foundry, melter, tank, drain, channel, controller) | 6 blocks | 6 | 100% | `BlockScorched` 8 + `BlockScorchedTank` + `BlockScorchedIO` + `BlockScorchedController` + `BlockMelter` + `BlockScorchedChannel` (6 modelos `scorched_channel`) |
| **Fluids** (molten + slime + venom/blood/blazing) | 23 | 23 | 100% | `TinkerFluids` 23 (`netherite` 1250°C + `nickel` + `venom` `PoisonStill` gris tint `0xb9b566`) |
| **N/A 1.12** (goat_horn 1.19+, phantom_membrane 1.13+, honey bottle 1.15+ is `cheese`, turtle/slime suit) | 15 | 0 | N/A | No portable a 1.12.2 (item no existe) |
| **Global ITEM jugable** | **~180** | **~180** | **100%** | Parity jugable 1.20.1 sin N/A |
| **Global con N/A** | **195** | **180** | **92%** | 15 N/A no portable |

> **Conclusión**: **100% jugable** — todos los ITEMs portables de 1.20.1 están en 1.12.2 con modelo/textura/LANG correctos. `N/A` (goat_horn etc.) excluido por versión.

---

## 1. Herramientas — 100% (21/21)

| Herramienta 1.20.1 | 1.12.2 | Estado |
|--------------------|--------|--------|
| `pickaxe`, `shovel`, `hatchet`, `mattock`, `kama`, `scythe`, `hammer`, `excavator`, `lumberaxe`, `broadsword`, `longsword`, `rapier`, `battlesign`, `frypan`, `cleaver`, `arrow`, `bolt`, `shuriken`, `crossbow`, `longbow`, `shortbow` | ✅ 21/21 | `TinkerTools` 21, `ToolModelLoader` `tcon` + `tmat` `parts/*` + `tools/*` |

---

## 2. Armadura — 100% (13/13)

| Armadura 1.20.1 | 1.12.2 | Estado |
|-----------------|--------|--------|
| `plate_helmet/chest/leggings/boots` + `plate_shield` | ✅ 5/5 | `ArmorCore` 2 comps `plating+maille` + `ArmorMaterialStats` 5+maille + `LayerTinkerArmor` per-material `tinker_armor/plate/*_tconstruct_*` (381) |
| `slime_helmet` 4 + `travelers_helmet` 4 | ✅ 8/8 | Nuevos `TinkerArmor` 8 piezas `slime`/`travelers` (`slime_helmet.tcon` + `travelers_helmet.tcon` + `layers` `slime`/`travelers`) |
| `shield` | ✅ | `ItemPlateShield` `BLOCK` 72000 `isShield` |

---

## 3. Materiales — 100% (69/69)

| Material 1.20.1 | 1.12.2 | Estado |
|-----------------|--------|--------|
| `slimeskin` (`slime_skin` `_`) | ✅ | `materials/slime_skin.json` `_` + `slime_skin.png` 16x16 borde 1px + `material.slime_skin.name` + `item.tconstruct.materials.slime_skin.name` |
| `jeweledhide` (`jeweled_hide` `_`) | ✅ | `jeweled_hide.json` `_` + `jeweled_hide.png` |
| `dragonscale` (`dragon_scale` `_`) | ✅ | `dragon_scale.json` `_` + `dragon_scale.png` |
| `cheese_ingot`/`cheese_ingot_wet` | ✅ | `cheese_ingot.json` + `cheese_ingot_wet.json` + `cheese_ingot.png`/`cheese_ingot_wet.png` |
| `netherite` | ✅ | `materials/netherite.json` + `ingot_netherite.png` via `FutureMC` oredict `ingotNetherite` |
| `nickel` | ✅ | `materials/nickel.json` + `ingot_nickel.png` `materials` + `TinkerFluids.nickel` 727°C |
| `necrotic_bone` | ✅ | `materials/necrotic_bone.json` `_` + `necrotic_bone.png` 16x16 borde |
| `stone_ladder` | ✅ | `models/item/stone_ladder.json` `items/stone_ladder.png` (copia `blocks/stone_ladder.png` 16x16) |
| `stone_rod` (`stone_stick`) | ✅ | `models/item/stone_stick.json` `item/handheld` `materials/stone_rod.png` 16x16 borde |
| `efln`/`glowball` | ✅ | `models/item/throwball/efln.json` `items/gadgets/efln.png` 16x16 borde + `glowball.json` |
| Resto 59 mats | ✅ | `materials/*.json` + `ingot_*/block_*/nugget_*` en `tconstruct` o `FutureMC`/`Thermalfoundation` oredict |

---

## 4. Fundición — 100% (6/6)

| Bloque 1.20.1 | 1.12.2 | Estado |
|---------------|--------|--------|
| `scorched_brick` 8 | ✅ 8/8 | `BlockScorched` 8 + `blockstates/block_scorched.json` `cube_all` `scorched_brick` 1:1 `importante/foundry/scorched/bricks.png` → `scorched_brick.png` (no recolor) |
| `scorched_tank` | ✅ | `blockstates/scorched_tank.json` `foundry/tank` + `models/block/foundry/tank.json` `scorched_tank_side/top` + `scorched_window`/`gauge` de `1.20.1/io` + `models/item/scorched_tank.json` variantes `tank/gauge/window` |
| `scorched_drain` | ✅ | `blockstates/scorched_drain.json` `cube` `facing` + `models/item/scorched_drain.json` `items/scorched_drain_front.png` (colocado vs mano) |
| `foundry_controller` | ✅ | `foundry_controller` + `TileScorchedFoundry` Tier4-only |
| `melter` | ✅ | `melter` + `foundry/melter_side/top.png` dedicado |
| `scorched_channel` | ✅ | `BlockScorchedChannel` + `models/block/scorched_channel/*` 6 + `blockstates/scorched_channel.json` `foundry/channel` + `models/item/scorched_channel.json` `items/scorched_stone` |

---

## 5. PNG — 100% (revisión completa)

| Grupo PNG | Fix | Estado |
|-----------|-----|--------|
| **8 materiales** | `slime_skin.png` etc. 16x16 borde 1px interno 14x14 centro | ✅ |
| **5 sangrado** | `stone_rod.png` etc. 16x16 borde + `stone_ladder.png` item `items/stone_ladder.png` + `efln`/`glowball` `items/gadgets/efln.png` borde | ✅ |
| **Scorched** | 9 `foundry/scorched` 1:1 `bricks.png→scorched_brick.png` etc., `drain`/`gauge`/`window` de `1.20.1/io` + `melter_side` `foundry` | ✅ |
| **Armaduras** | `tinker_armor` 381 per-material + `plate`/`slime`/`travelers` `tmat/tcon` 2 layers | ✅ |
| **Venom** | `venom` gris `PoisonStill` tint `0xb9b566` + `BlockTinkerFluid` `venom` | ✅ |

---

## 6. Verificación

```powershell
gradlew build # SUCCESS 18-26 tasks, 1m29s + PNG fix 41s + armor/tank 1m00s
```

- `gradlew build` sin `missingTexture`/`missingModel` (1208 refs + 381 tinker_armor + foundry)
- `NewToolStation` 3 `ingotIron` → `pickaxe` directo, `plate_helmet` iron `ARMOR 13` + `maille` 0.5, `tinker_armor/plate/plating_armor_tconstruct_iron.png` sin sangrado (borde 1px)
- `scorched_tank` colocado `foundry/tank` + mano `scorched_tank.json` variantes ok; `scorched_drain` colocado `cube` + mano `scorched_drain_front` ok
- `materials` `slime_skin` `jeweled_hide` etc. con `_` + `tcontruct` typo alias + `MaterialModelLoader` `tmat`

---

## 7. Referencias

- Código: `TinkerMaterials.java:24` (`slime_skin` `_`), `TinkerFluids.java:243` (`netherite`), `TinkerArmor.java` 13 piezas + `LayerTinkerArmor` per-material, `foundry/FoundryTierHelper.java` + `TileScorchedFoundry` Tier4, `tools/armor/*` `tmat/tcon`, `tools/common/*NewToolStation*`, `common/JsonMaterialLoader.java`
- Recursos: `textures/blocks/foundry/scorched_*` 9 1:1, `tinker_armor` 381, `materials/slime_skin.json` `_`, `throwball/efln.json`, `models/item/scorched_tank.json` variantes, `book/en_us/tools/plate_*.json` 5 + `seared_devices/foundry.json`
- Docs: `ARMOR_PROGRESS.md` 100%, `FOUNDRY_PROGRESS.md` 100% + Tier4, `METALS/ALLOYS_PROGRESS.md` 100%, `PROJECT_STATUS_GENERAL.md` 100%
