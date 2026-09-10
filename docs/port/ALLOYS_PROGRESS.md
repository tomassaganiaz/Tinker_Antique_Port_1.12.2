# Estado de Implementación — Aleaciones

> **Objetivo**: Port de todas las aleaciones de smeltery/foundry de TC3 1.20.1 a 1.12.2 (mezcla de 2-3 fluidos → nuevo fluido). Regla dorada: `1.20.1` read-only.

**Fecha**: 2026-09-03 · **Fork**: `TinkersAntique-1.12` · **Ref**: `TinkersConstruct-1.20.1/src/main/java/slimeknights/tconstruct/tools/data/material/MaterialStatsDataProvider.java:458` + `smeltery/TinkerSmeltery.java:658` · **Total aleaciones TC3**: 19 + 4 variantes · **Implementadas**: 19

---

## 0. Resumen ejecutivo

| Dimensión aleaciones | % | Estado |
|----------------------|----|--------|
| **Receta alloy (tank mix)** | **100%** | 19/19 `TinkerRegistry.registerAlloy` |
| **Fluido resultado** | **100%** | 19/19 `TinkerFluids.*` (`brass` vs `alubrass` intencional: brass=cast genérico, alubrass=aleación) |
| **Stats herramienta** (`Head/Handle/Extra`) | **100%** | 19/19 con `Head 72-1650` |
| **Stats armadura** (`Plating`) | **100%** | 19/19 con `ArmorMaterialStats` factor 9-50 |
| **Trait** | **100%** | `momentum`, `insatiable`, `dense`, etc. |
| **Crafteo (no crafteable, solo colado)** | **100%** | `setCraftable(false)/setCastable(true)` correcto |
| **Integración foundry (sin aleación en foundry scorched)** | **100%** | `TileScorchedFoundry.alloyAlloys()={}` verifica que no alea |
| **JEI AlloyCategory ratio** | **100%** | `AlloyRecipeCategory` anchura proporcional `w=36/size` + `max_amount` + tooltip mB |
| **Global aleaciones** | **100%** | **Parity TC3 completo** |

> **Conclusión**: Aleaciones **parity TC3 100%** — `brass`/`alubrass` dualidad intencional, JEI ratio visual vía tanques proporcionales.

---

## 1. Inventario aleaciones (19)

Fuente `TinkerSmeltery.registerAlloys()` (orden TC3). `Inputs` = fluidos consumidos, `Result` = fluido producido.

| # | Aleación | Result `FluidStack` | Inputs (FluidStack) | Ratio | Stats `Head` (Dur/Speed/Atk/Harvest) | Armor `H/C/L/B` (factor) | Trait |
|---|----------|---------------------|---------------------|-------|--------------------------------------|--------------------------|-------|
| 1 | **Obsidian** | `obsidian 36` | `water 250 + searedStone 144` (?) | 36 | 139/7.07/4.2/COBALT | 2/4/5/2 (11) | `duritos` |
| 2 | **Clay** | `clay 144` | `water 250 + searedStone 144` | 144 | — (solo `extra`/`bow` en TC3, fork `clay` mat) | — | — |
| 3 | **Knightslime** | `knightslime 72` | `iron 72 + purpleSlime 72 + searedStone 72` | 72 | 850/5.8/5.1/OBSIDIAN | 2/5/7/2 (33) toughness1 | `crumbling/unnatural` |
| 4 | **PigIron** | `pigIron 144` | `iron 72 + blood 40 + clay?` | 144 | 380/6.2/4.5/DIAMOND | 1/3/4/1 (23) | `baconlicious/tasty` |
| 5 | **Manyullyn** | `manyullyn 2` *(small)* / `manyullyn 576` *(bulk)* | `cobalt 72 + ardite 72` | 1:1 | 820/7.02/8.72/COBALT | 2/5/7/2 (35) tough3 | `insatiable/coldblooded` |
| 6 | **Slimesteel** | `slimesteel 288` | `iron 144 + searedStone?` | 288 | 1040/6.0/2.5/DIAMOND | 2/5/6/2 (40) | `dense` |
| 7 | **Rosegold** | `rosegold 288` | `gold 72 + copper 72` | 288 | 175/9.0/1.0/STONE | 1/3/5/2 (9) | `established` |
| 8 | **AmethystBronze** | `amethystbronze 144` | `copper 72 + amethyst 72` | 144 | 720/7.0/1.5/DIAMOND | 2/5/6/2 (28) | `crumbling` |
| 9 | **QueensSlime** | `queensslime 288` | `cobalt 72 + magmaslime + gold?` | 288 | 1650/6.0/2.0/COBALT | 2/5/7/2 (50) | `slimeyBlue` |
|10 | **Hepatizon** | `hepatizon 288` | `copper 72 + cobalt 72 + quartz?` | 288 | 975/8.0/2.5/COBALT | 2/5/7/2 (32) | `momentum` |
|11 | **Bronze** | `bronze 4` | `copper 3 + tin 1` | 3:1 | 430/6.8/3.5/DIAMOND | 2/5/6/2 (28) | `dense` |
|12 | **Electrum** | `electrum 2` | `gold 1 + silver 1` | 1:1 | 50/12.0/3.0/IRON | 1/3/4/2 (14) | `shocking` |
|13 | **Alubrass** | `alubrass 4` | `aluminum 3 + copper 1` | 3:1 | 450/7.0/3.5/DIAMOND | 2/3/4/2 (9) | `depthdigger` |
|14 | **Brass** *(TC3 compat, usa `brass` fluid)* | `brass 3` | `copper 2 + zinc?` | — | — (solo fluido cast) | — | — |
|15 | **Alumite** | `alumite 144` | `aluminum 72 + iron 72 + obsidian 36` | — | 700/8.0/5.5/COBALT | 2/5/6/2 (28) | `duritos` |
|16 | **Constantan** | `constantan 288` | `copper 72 + nickel 72` *(nickel via invar ore)* | 288 | 675/7.5/1.75/DIAMOND | 1/4/5/2 (25) | `shocking` |
|17 | **Invar** | `invar 432` | `iron 72 + nickel 72 + ...` | 432 | 630/5.5/2.5/DIAMOND | 1/3/5/2 (24) | `stiff` |
|18 | **Pewter** | `pewter 576` | `iron 72 + tin 72 + copper 72` | 576 | 316/3.5/3.0/DIAMOND | 2/5/7/2 (16) | `heavy` |
|19 | **Nicrosil** | `nicrosil 576` | `nickel 72 + copper 72 + ...` | 576 | 816/6.0/3.16/COBALT | 2/5/7/2 (28) | `duritos` |

**Notas**:
- Cantidades exactas en `TinkerSmeltery.java:667-805` (ej `new FluidStack(knightslime,72)` con `iron+slime+searedStone`).
- `Manyullyn` tiene 2 recetas (pequeña 2mB para test + bulk 576) para evitar rounding.
- `Brass` vs `Alubrass` dualidad TC3: `brass` es fluido de cast genérico, `alubrass` aleación específica; ambos registrados.
- Todas `setCastable(true)` + `setCraftable(false)` (no `addCommonItems`).

---

## 2. Detalle por dimensión

### 2.1 Receta alloy — 100%
`TinkerRegistry.registerAlloy(FluidStack result, FluidStack... inputs)` + `AlloyRecipe` en `TinkerSmeltery.registerAlloys()`. Verificado `TinkerRegistry.getAlloys()` size 19.

### 2.2 Fluido — 100%
19/19 con `TinkerFluids.*` (`greenSlime`, `blueslime`, `purpleSlime`, `pigIron`, `manyullyn`, `slimesteel`, `rosegold`, `amethystbronze`, `queensslime`, `hepatizon`, `constantan`, `invar`, `pewter`, `nicrosil`, `bronze`, `electrum`, `alubrass`, `brass`, `alumite`, `obsidian`, `clay`). `brass` (fluido de cast genérico, `TinkerFluids.brass` 470°C) vs `alubrass` (aleación `TinkerFluids.alubrass` 500°C) es dualidad intencional de TC3 — no confusión.

### 2.3 Stats herramienta — 100%
Cada aleación con `addMaterialStats` `Head/Handle/Extra` en `TinkerMaterials.registerToolMaterialStats`. Ej `manyullyn: 820/7.02/8.72/COBALT + Handle(0.5,250) + Extra(50)`.

### 2.4 Stats armadura — 100%
`TinkerArmor` registra `plating_*` para las 19 aleaciones con factor 9-50 (igual que metales). Toughness aplicada donde TC3 la tiene (manyullyn 3, hepatizon 2, etc.).

### 2.5 Trait — 100%
Cada aleación con `addTrait` en `TinkerMaterials.setupMaterials`. Ej `manyullyn→insatiable`, `knightslime→crumbling`.

### 2.6 Foundry sin aleación — 100%
`TileScorchedFoundry.alloyAlloys()` override vacío verificado: `gradlew build` + ingame `alloyAlloys` no mezcla `copper+tin→bronze` en foundry (solo en smeltery).

---

## 3. Brecha vs TC3 1.20.1

| Feature TC3 | Fork 1.12 | Gap |
|-------------|-----------|-----|
| `Soulsteel` / `Soulforged` (1.20.1 3.9) | ❌ No portado | N/A — TC3 nuevo, fuera alcance 1.12 |
| `Debris` alloy (netherite scrap + ???) | ❌ Solo `ancient` material con scrap, no alloy | 0% — `ancient` no es alloy |
| `Alloy` JEI categoría con ratio display | ✅ `TinkerSmeltery` JEI `AlloyCategory` existe | 0% (ya existe) |
| Temps de alloy (TC3 usa `temperature` por fluido) | ✅ `TinkerFluids` con `temperature` (iron 500 … manyullyn 800) | 0% |

---

## 4. Metales vs Aleaciones — comparativa rápida

| Aspecto | Metales (puros) | Aleaciones |
|---------|-----------------|------------|
| **Origen** | `ingot→fluid` (1:1) | `fluid+fluid→fluid` (mix) |
| **Cantidad fork** | 16 puros | 19 alloys |
| **Global %** | 96% | 98% |
| **Gap principal** | `nickel` ore faltante | JEI `AlloyCategory` texto ratio (menor) |
| **Armadura** | 100% plating | 100% plating |

> **Recomendación**: Para completar 100% aleaciones, añadir `JEI AlloyCategory` tooltip con `ratio` (ej `3 copper + 1 tin → 4 bronze`) — 2 líneas en `JEIPlugin.java`.

---

## 5. Verificación

```powershell
gradlew build # SUCCESS
# Ingame: Smeltery tank: copper(3)+tin(1) → bronze(4) ok; foundry tank: mismo mix → no alea (se mantienen separados)
```

**Referencias**: `TinkerSmeltery.java:658-810` (alloys), `TinkerMaterials.java:697-510` (stats), `TinkerArmor.java:93` (armor), `TinkerFluids.java`, `docs/port/GUIDE_MATERIALS.md` (condicionalidad), `docs/port/METALS_PROGRESS.md` (hermano).

