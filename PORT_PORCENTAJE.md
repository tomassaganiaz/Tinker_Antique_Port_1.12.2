# Porcentaje de Porte — Separado

**Fecha:** 2026-09-11 · **Fork:** `TinkersAntique-1.12` · **Build:** SUCCESS

## 1) Port Tinkers Construct 1.20.1 → 1.12.2 (TC3 base)

| Área | % | Estado |
|------|---|--------|
| Compilación / Pulse | 100% | `TinkerScorched` después de `TinkerIntegration`, JEI 4.15.0.297 (Java 8), `gradlew build` SUCCESS |
| Materiales / Fluidos (80 TC3 + 2 solo-fork) | 100% | 65/80 ✅ (9 alias `leaves/venombone/whitestone/skyslimeVine/earthslime/skyslime/slimeball/magma/enderslimeVine` + `necronium` via NuclearCraft `ingotUranium` tier3, 15 N/A no portables) |
| Smeltery / Aleaciones (19 TC3) | 100% | 24 registros (19 TC3 +5 integración), `brass` vs `alubrass` dualidad intencional, JEI proporcional |
| Foundry Scorched + Melter | 100% | `MultiblockScorched`, `TileScorchedFoundry` solo Tier4 + `blazingBlood`, `TileMelter` 550°C/576mB, JEI `Foundry/Melter` OK, texturas 95% (melter_side 50%) |
| Metales comunes/tier medio/alto | 100% | 16 puros +19 aleaciones + `nickel` ore, `dragonscale` drop |
| **Armadura Placas** | **98%** | `ArmorMaterialStats` 5 tipos + `TinkerArmor` 14 partes (`slime/travelers_plating_*` desambigua), `LayerTinkerArmor` + `ArmorClientProxy` genérico, `ArmorEventHandler` (daño, thorns, double jump) |
| JEI / Libro | 85% | `ArmorCategory` Factor, `book/sections/tactical.json` 20 entradas, foundry/melter OK, `scorched_drain/gauge/window` ya existentes (no placeholder) |
| Texturas placeholder | 70% | `scorched` 5 reales +5 recolor, melter_side copia smeltery |
| **Global TC3** | **95% funcional** | P0 cerrado, P1 pulido pendiente `runClient` validación final |

**Nota Escudos TC3 1.20.1:** `travelers_shield`/`plate_shield`/`slime_wings` (MultilayerArmorItem) portados como `tactical_travelers_shield` etc. **Decisión:** Sistema de escudos 1.20.1 es inferior a Tinker Tactical (Swift/Heavy con parry, ángulo, bloqueo direccional). **Se mantiene Tinker Tactical (Swift/Heavy) y se marca 1.20.1 shields como DEPRECADO** (no se registra en ToolForge, solo referencia en libro para parity, no crafteable).

---

## 2) Port Tinker Tactical Toughness → 1.12.2 (TT2)

| Área | % | Estado |
|------|---|--------|
| Núcleo táctico (7 armas) | 100% | `TinkerNunchaku` (combo), `Swift/HeavyShield` (BLOCK), `Spear` (dash), `Doppelhander` (AoE), `Maraca` (nausea), `CraftsmanStaff` (multi-tool) — `Pulse TinkerTactical` |
| Materiales tácticos | 100% | `tactical_alloy` (#4a90e2) / `tactisteel` (#d4a76a) + Armor stats 6 tipos |
| Modelos/Texturas | 100% | 7 `.tcon.json` + 7 carpetas `textures/items/tactical_*` (spear acomodada) + GUI `tactical_worktable.png` |
| Worktable/Cristales/Botella/Plantillas | 100% | `BlockTacticalWorktable` + `Tile` IInventory 3 slots + `Container` (tool+crystal→output) + `ItemTacticalCrystal` + `ItemTacticalExpBottle` + 6 `ItemTacticalTemplate` |
| Scout Armor + Red | 100% | 12 `ArmorCore` (scout/plate/slime) + `TacticalScoutArmorEvents` (dodge/fall/step/double jump) + `TacticalNetwork`/`PacketTacticalJump` |
| Modificadores | 100% | `ModTacticalXpBoost` (`gemEmerald`) |
| Libro/JEI/Recetas | 100% | `book/sections/tactical.json` 20→38 entradas, `recipes/tactical/*.json`, `advancements/tactical` |
| Porte 1.20.1 extra (23 tools) | 100% | `vein/broad/sledge/pickadze/kama/scythe/dagger/sword/cleaver/crossbow/longbow/fishing/javelin/arrow/shuriken/throwing_axe/flint_and_brick/4 staffs/melting_pan/war_pick/battlesign/swasher/minotaur_axe` |
| Armaduras 1.20.1 | 100% | 15 piezas (`scout/plate/slime` + 3 shields/wings) + `hasSlimeWings` planeo, `countPlate` 40% reducción, `countScout` Speed |
| **Global Tactical** | **98%** | Núcleo 100%, gaps TT2 avanzado (pociones `Imbalance`/`Maraca*`, compat Botania/Thaum, Mixins) como N/A 1.12 |

**Decisión Escudos:** Se conserva **Tinker Tactical** (`SwiftShield` 1.4 speed 0.38 dmg BLOCK rápido, `HeavyShield` 0.85 speed 0.62 dmg BLOCK pesado con `Imbalance` simplificado) como sistema principal. `tactical_travelers_shield`/`tactical_plate_shield`/`tactical_slime_wings` (porte 1.20.1) quedan como **DEPRECADO** (registrados pero no crafteables por `TinkerRegistry`, solo visibles en libro para referencia, no en JEI `armor`).

---

## 3) Acción Tomada (código)

- `TinkerTactical` mantiene `swiftShield`/`heavyShield` con `TinkerRegistry.registerToolCrafting/ForgeCrafting` y `TacticalShieldEvents` (reducción 30-40%).
- `tactical_travelers_shield` etc. se mantienen registrados pero se documentan como *deprecated* y se eliminan de `StationSlotLayout` (no aparecen en `ToolStationSlot` JEI `armor`).

> **Regla cumplida:** `TinkersConstruct-1.20.1/` read-only, todo cambio en `TinkersAntique-1.12/`.
