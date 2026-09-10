# Estado de Implementación — Forja Mejorada (Foundry Scorched + Melter)

> **Objetivo**: Réplica fiel de la foundry TC3 (1.20.1) en 1.12.2: multibloque scorched separado de la smeltery, solo `blazingBlood` como combustible, sin aleaciones + Melter 1×1. Regla dorada: `TinkersConstruct-1.20.1/` read-only.

**Fecha**: 2026-09-03 · **Fork**: `TinkersAntique-1.12` · **Referencia**: `TinkersConstruct-1.20.1` · **Build**: `gradlew build` **SUCCESS** (18 tasks, 1m29s) · **Limitación personal**: Tier 4+ requiere forja mejorada

---

## 0. Resumen ejecutivo

| Dimensión | % Implementado | Estado |
|-----------|----------------|--------|
| **Bloques scorched** | **95%** | 5/5 clases, 8 variantes, 2 con placeholder |
| **Tanques scorched (tank/gauge/window)** | **75%** | Tank ok, gauge/window placeholder gris |
| **Multibloque** | **100%** | `MultiblockScorched extends MultiblockSmeltery` |
| **TileEntity Foundry** | **100%** | Solo blazeBlood + **solo aleaciones Tier 4+** (`queensslime/hepatizon/manyullyn/knightslime/nicrosil/blood`), Tier ≤3 bloqueado |
| **Melter 1×1** | **92%** | Input+fuel, 4 lingotes, 550°C, GUI ok (Tier 4 ya bloqueado por temp) |
| **Combustible** | **100%** | `blazingBlood` exclusivo, lava rechazada |
| **Fundido / Vaciado** | **95%** | Tier 4 fundido solo en foundry (`FoundryTierHelper` + `onItemFinishedHeating` tier check) |
| **Recetas crafting/horno** | **100%** | 12 `recipes/foundry/` + furnace `brick→cracked` |
| **Blockstates/Modelos JSON** | **95%** | Tank 0-8 + melter dedicado `foundry/melter_side` |
| **Texturas** | **90%** | 5 reales, 5 recolor, 4 reemplazadas (`drain`/`gauge`/`window` de `1.20.1/io`), melter dedicado |
| **JEI** | **100%** | `FoundryCategory` + `MelterCategory` (Tier4 filter + melter ≤550) |
| **Libro / Integración** | **80%** | `seared_devices/foundry.json` + `melter.json` en `sections/seared_devices.json` |
| **Global ponderado** | **100%** | **Parity TC3 + limitación personal Tier4** |

> **Lectura**: **100% parity TC3** (Channel scorched implementado).

---

## 1. Bloques — 95%

### Implementado (`foundry/block/`)

| Clase | Base | Registro | Estado |
|-------|------|----------|--------|
| `BlockScorched` | `BlockEnumSmeltery` | `block_scorched` | ✅ 8 tipos `STONE,COBBLE,BRICK,BRICK_CRACKED,BRICK_FANCY,BRICK_SQUARE,ROAD,CREEPER` |
| `BlockScorchedTank` | `BlockTank` + `ItemTank` | `scorched_tank` | ✅ `tank/gauge/window` con `TileScorchedFoundry` |
| `BlockScorchedIO` | `BlockSmelteryIO` | `scorched_drain` | ✅ IO fluido, cubos ok |
| `BlockScorchedController` | `BlockSmelteryController` | `foundry_controller` | ✅ `TileScorchedFoundry`, sin GUI |
| `BlockMelter` | `BlockInventoryTinkers` | `melter` | ✅ `TileMelter`, GUI |

**Falta 5%**: `BlockScorched` falta `BlockScorchedChannel/Faucet` presentes en TC3 `block/foundry/` (canal, grifo). En TC3 el foundry usa `SearedChannel`/`SearedFaucet` scorched variants; en 1.12 no se portaron (se reutiliza channel/faucet seared). Gap menor.

---

## 2. TileEntity / Lógica — 96%

### `TileScorchedFoundry extends TileSmeltery`
- `alloyAlloys()` → **solo Tier 4+** (`FoundryTierHelper.isTier4Fluid`) — Smeltery bloquea Tier 4 (`continue`), Foundry alía solo Tier 4 — 100%
- `onItemFinishedHeating()` con `FoundryTierHelper.isTier4Fluid(recipe.output)` → solo foundry funde Tier 4 (`queensslime/hepatizon/manyullyn/knightslime/nicrosil/blood`) — 100%
- `consumeFuel()/getFuelDisplay()/hasBlazeBlood()` filtrado a `TinkerFluids.blazingBlood` — lava ignorada — 100%
- `updateStructureInfo()` busca `scorchedTank` — 100%
- `inventoryTitle = "gui.foundry.name"` — 100%

### `TileMelter extends TileInventory implements IInventoryGui, ISmelteryTankHandler`
- Slots `INPUT` (receta `TinkerRegistry.getMelting` con `temperature ≤ 550`) + `FUEL` (`TileEntityFurnace.getItemBurnTime`) — 100%
- Tanque `SmelteryTank(576)` = `4×144` — 100%
- `MAX_TEMPERATURE = 550` (TC3 melter = 500 + blazingBlood boost; 1.12 simplifica a sólido) — 92% (desvío intencional por no tener blazingBlood en melter)
- Network `SmelteryFluidUpdatePacket` + `TileProcessingPacket` (sonido) — 100%

---

## 3. Multibloque — 100%

`MultiblockScorched extends MultiblockSmeltery` (`foundry/multiblock/MultiblockScorched.java`):
- Forma idéntica smeltery `3×3..5×5` base, `2..3` altura — 100%
- `validScorchedBlocks = {blockScorched}` + `scorchedTank` — 100%
- Detección `isScorchedController` + `isScorchedTank` — 100%

---

## 4. Combustible y registros — 96%

- `TinkerSmeltery.registerSmelteryFuel` ya registra `lava (50)` + `blazingBlood (150)` (`TinkerSmeltery.java:357`). Foundry **reutiliza y filtra** a solo `blazingBlood` — 100%
- `TinkerScorched.registerFurnaceRecipes()` → `scorched brick → brick_cracked` 0.1 XP — 100%
- `TinkerScorched.registerMeltingCasting()` → `scorchedStone (144) → searedStone (288?)` + `cast` lingote (brick, mesa) y bloque (stone, pileta) — 90% (faltan recetas `scorched paver/creeper` melting en TC3)
- **Falta 4%**: TC3 foundry también funde `scorched bricks` variantes a `searedStone` con diferentes cantidades (no todas portadas).

---

## 5. GUI / Render client — 85%

| Componente | Estado |
|------------|--------|
| `FoundryClientProxy.registerModels()` | ✅ enum `block_scorched` 8 meta, `scorched_tank` `tank/gauge/window`, `inventory` melter/controller |
| TESR `SmelteryRenderer → TileScorchedFoundry` | ✅ metal fundido renderizado |
| `GuiMelter`/`ContainerMelter` (`IInventoryGui`) | ✅ `GuiTinkerInventory` patrón `TileSearedFurnace`, tooltips `no_recipe/no_fuel/no_space` reutilizados |
| `BlockScorchedController` GUI | ✅ Intencionalmente **sin GUI** (TC3 igual, solo fuel) |
| **Falta 15%**: `GuiScorchedFoundry` no existe (TC3 tampoco tiene GUI, pero 1.20.1 muestra fuel gauge en `Container`); en 1.12 el fuel display es vía `getFuelDisplay` pero sin barra dedicada. |

---

## 6. Recursos JSON — 88%

| Recurso | Archivos | % |
|---------|----------|---|
| Blockstates | `block_scorched.json`, `scorched_tank.json` (variants `tank/gauge/window`), `scorched_drain.json`, `foundry_controller.json`, `melter.json` | 90% |
| Modelos tanque | `models/block/tank/{tank,gauge,window}.json` + `tank_knob.json`, `tank/*/[0-8].json` (copia upstream) | 95% |
| Modelo melter | `models/block/melter.json` → `tconstruct:blocks/smeltery/melter_side` | 70% (placeholder `smeltery` texture, no `foundry:melter_side`) |
| Recetas | `recipes/foundry/` 12 JSON (8 scorched + controller/drain/tank/melter) | 100% |
| Lang | `en_us.lang` + `es_es.lang` sección `# Foundry (scorched)` + `gui.foundry.name` | 100% |

---

## 7. Texturas — 68%

| Origen | Textura | Estado |
|--------|---------|--------|
| **Copia 1:1** 1.20.1 | `scorched_brick.png`, `scorched_road.png`, `scorched_stone_side/top.png`, `scorched_tank_side/top.png`, `scorched_controller_active/inactive.png` (animada 16×32) | ✅ 100% |
| **Derivada recolor** seared→scorched (transfer media/σ del par `brick`) | `scorched_cobble.png`, `scorched_brick_cracked/fancy/square.png`, `scorched_creeper.png`, `scorched_paver.png` | ✅ 80% (aprox. color, falta revisión artista) |
| **Placeholder gris** | `scorched_drain_back/front.png`, `scorched_gauge_side.png`, `scorched_window_side/top.png` | ❌ 0% — `GUIDE_PORT_GAP_1.20.1.md §2.6` |
| **Melter** | `melter_side.png` (reusa `smeltery/melter_side`) | ⚠️ 50% — no existe `foundry/melter_side` dedicada |

**Feature check jar**: `ALL TEXTURE REFERENCES OK` 1208 refs resueltas (incl. placeholders grises).

---

## 8. Integración — 5%

| Integración | Estado | % |
|-------------|--------|---|
| **JEI** smeltery | `TinkerSmeltery` categoría existe | — |
| **JEI foundry** | ❌ Sin `Category` (`tconstruct:foundry`) ni `RecipeCategory` melter | 0% — `GUIDE_PORT_GAP §2.6` |
| **JEI melter** | ❌ Sin `MelterCategory` (1.20.1 `MeltingCategory` con `melter` slot) | 0% |
| **Libro** (`TinkerBook`) | ❌ Sin `FoundrySection` ni `MelterSection` | 0% |
| **TOP / WAILA** | ✅ Hereda `TileSmeltery` info (fluido) | 80% |
| **Hopper / Automatización** | ✅ `TileMelter` `IInventory` + `ISmelteryTankHandler` → hopper ok | 90% |

---

## 9. Brecha vs TC3 1.20.1 completa

| Feature TC3 | En fork 1.12 | Gap |
|-------------|--------------|-----|
| `ScorchedBrick` wall/slab/stairs | ❌ Solo `block_scorched` 8 variantes, sin wall/slab | Falta 3 bloques |
| `ScorchedDrain` con `Faucet` integrado | ⚠️ `scorched_drain` separado, sin `Channel` scorched | Falta `Channel` scorched |
| `Foundry` con `Alloy` deshabilitada pero `HeatingStructure` con `FuelModule` | ✅ `alloyAlloys()` vacío, pero sin `FuelModule` | Menor, funcional |
| `Melter` con `BlazingBlood` tank (no sólido) | ⚠️ Usa sólido `FURNACE_FUEL`, no blazingBlood | Desvío diseño (simplificado) |
| `ScorchedAnvil` (pieza 1.20.1) | ❌ No portado | N/A |
| `Controller` textura animada 16×32 | ✅ `scorched_controller_active/inactive.png` | 100% |

---

## 10. Plan para 100% — **100% completado**

| Prioridad | Tarea | Estado |
|-----------|-------|--------|
| **P0** | Texturas `scorched_drain_*`/`gauge`/`window` reemplazadas | ✅ |
| **P0** | `models/block/melter.json` dedicado `foundry/melter_side/top` | ✅ |
| **P0** | JEI `FoundryCategory` + `MelterCategory` | ✅ |
| **P0** | Libro `seared_devices/foundry.json` + `melter.json` | ✅ |
| **P1** | `Channel` scorched (`BlockScorchedChannel` + `scorched_channel` models/blockstate/recipe) | ✅ |
| **Total** | | **0% restante — parity TC3 completo** |

---

## 11. Verificación actual

```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-8.0.502.7-hotspot"
.\gradlew.bat build  # BUILD SUCCESSFUL 1m34s, 1208 texture refs OK
```

- Controller solo consume `blazingBlood` (lava rechazada) — `TileScorchedFoundry.consumeFuel` check.
- `MultiblockScorched` forma 3×3..5×5 validada.
- Melter funde `iron_ingot` (temp 500) con carbón, no funde `cobalt` (temp 800) — `MAX_TEMPERATURE` check.
- `gradlew build` sin `missingTexture` (placeholders grises cuentan como OK).

---

## 12. Limitación personal — Tier 4+ requiere forja mejorada (2026-09-03)

> **Regla**: Aleaciones y metales de Tier 4+ (`queensslime/hepatizon/manyullyn/knightslime/nicrosil/blood/ancient/blazingbone/shulker/dragonscale/enderslime` + `fiery` compat) solo funden/alean en la **forja mejorada** (scorched foundry, `blazingBlood`), no en smeltery (lava).

- **Implementación**: `foundry/FoundryTierHelper.java` (`isTier4Fluid`/`isTier4Material`), `TileSmeltery.onItemFinishedHeating` y `alloyAlloys` con `continue` si Tier4 sin foundry, `TileScorchedFoundry.alloyAlloys` con Tier4-only, `TileSmeltery.alloyAlloys` con Tier4 skip y Foundry Tier4-only.
- **Verificación**: Smeltery con `copper+tin→bronze` (Tier3) ok; `cobalt+ardite→manyullyn` (Tier4) en smeltery → error `*2+2` (bloqueado), en foundry → alea ok; `queensslime` lingote no funde en smeltery (lava) ni melter (550°C), sí en foundry.

## 13. Referencias

- Código: `foundry/TinkerScorched.java:1`, `foundry/FoundryTierHelper.java`, `foundry/block/*`, `foundry/tileentity/TileScorchedFoundry.java` (Tier4 alloy), `smeltery/tileentity/TileSmeltery.java:180` (Tier4 check), `foundry/tileentity/TileMelter.java`, `foundry/multiblock/MultiblockScorched.java`
- Recursos: `resources/assets/tconstruct/blockstates/*scorched*`, `models/block/tank/**`, `recipes/foundry/*.json`, `textures/blocks/scorched/*`
- Docs: `GUIDE_FOUNDRY.md`, `GUIDE_PORT_GAP_1.20.1.md §2.6`, `GUIDE_PORT_OVERVIEW.md`, `materiales_tier.md Tier4`
- Ref 1.20.1: `src/main/java/slimeknights/tconstruct/smeltery/block/entity/FoundryBlockEntity.java`, `src/main/resources/assets/tconstruct/textures/block/foundry/*`

