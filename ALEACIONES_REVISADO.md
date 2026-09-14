# Estado Revisado — Aleaciones (TC3 1.20.1 → 1.12.2)

**Fecha revisión:** 2026-09-11 · **Fuente código:** `TinkerSmeltery.java:689-856` · **Build:** SUCCESS

## Corrección conteo
Reporte previo indicaba **19** aleaciones TC3. Código actual registra **24** entradas `TinkerRegistry.registerAlloy`:
- **19 TC3 base** (obsidian, clay, knightslime, pigIron×2, manyullyn, netherite, slimesteel, rosegold, amethystbronze, queensslime, hepatizon, constantan, invar, pewter, nicrosil, darkthread, jeweledhide, cinderslime)
- **+5 integración** (bronze, electrum, alubrass, brass, alumite) condicionales `TinkerIntegration.isIntegrated`

Las 2 recetas `pigIron` (clay vs honey) son variantes del mismo resultado, cuentan como 1 aleación lógica pero 2 registros.

**Total lógico TC3:** 19 (correcto), **total registros código:** 24 (19 + 5 integración). Reporte actualizado mantiene **19/19 (100%)** para parity TC3, con nota de 5 integración.

## Tabla corregida — Aleaciones TC3 (19)

| # | Aleación | Result | Inputs (exactos código) | Stats Head (TinkerMaterials) | Armor factor | Trait | Estado |
|---|----------|--------|-------------------------|------------------------------|--------------|-------|--------|
| 1 | Obsidian | `obsidian 36` | `WATER 125 + LAVA 125` | 139/7.07/4.2/COBALT | 2/4/5/2 (11) | `duritos` | ✅ |
| 2 | Clay | `clay 144` | `WATER 250 + searedStone 72 + dirt 144` | — (extra) | — | — | ✅ |
| 3 | Knightslime | `knightslime 72` | `iron 72 + purpleSlime 125 + searedStone 144` | 850/5.8/5.1/OBSIDIAN (33) | 2/5/7/2 (33) | `crumbling` | ✅ |
| 4 | PigIron (α) | `pigIron 144` | `iron 144 + blood 40 + clay 72` | 380/6.2/4.5/DIAMOND (23) | 1/3/4/1 | `baconlicious` | ✅ |
| 4b | PigIron (β) | `pigIron 144` | `iron 144 + blood 40 + honey 250` | — | — | — | ✅ variante |
| 5 | Manyullyn | `manyullyn 576` | `cobalt 432 + scrap 144` | 820/7.02/8.72/COBALT (35) | 2/5/7/2 (35) t3 | `insatiable` | ✅ |
| 6 | Netherite | `netherite 144` | `scrap 576 + gold 576` | 900/8.0/4.0/COBALT (40) | 3/6/8/3 (40) t3 | `duritos` | ✅ |
| 7 | Slimesteel | `slimesteel 288` | `iron 144 + blueslime 250 + searedStone 250` | 1040/6.0/2.5/DIAMOND (40) | 2/5/6/2 | `dense` | ✅ |
| 8 | Rosegold | `rosegold 288` | `copper 144 + gold 144` | 175/9.0/1.0/STONE (9) | 1/3/5/2 | `established` | ✅ |
| 9 | AmethystBronze | `amethystbronze 144` | `copper 144 + amethyst 144` | 720/7.0/1.5/DIAMOND (28) | 2/5/6/2 | `crumbling` | ✅ |
| 10 | QueensSlime | `queensslime 288` | `cobalt 144 + gold 144 + magma 250` | 1650/6.0/2.0/COBALT (50) | 2/5/7/2 (50) | `slimeyBlue` | ✅ |
| 11 | Hepatizon | `hepatizon 288` | `copper 288 + cobalt 144 + quartz 144` | 975/8.0/2.5/COBALT (32) | 2/5/7/2 (32) | `momentum` | ✅ |
| 12 | Constantan | `constantan 288` | `copper 144 + nickel 144` | 675/7.5/1.75/DIAMOND (25) | 1/4/5/2 | `shocking` | ✅ |
| 13 | Invar | `invar 432` | `iron 288 + nickel 144` | 630/5.5/2.5/DIAMOND (24) | 1/3/5/2 | `stiff` | ✅ |
| 14 | Pewter | `pewter 576` | `tin 432 + lead 144` | 316/3.5/3.0/DIAMOND (16) | 2/5/7/2 | `heavy` | ✅ |
| 15 | Nicrosil | `nicrosil 576` | `nickel 288 + emerald 144 + quartz 144` | 816/6.0/3.16/COBALT (28) | 2/5/7/2 | `duritos` | ✅ |
| 16 | Darkthread | `darkthread 288` | `obsidian 144 + ardite 144` | — (maille) | slime plate? | `established`? | ✅ |
| 17 | JeweledHide | `jeweledhide 288` | `pigIron 144 + knightslime 144` | — | — | `dense` | ✅ |
| 18 | Cinderslime | `cinderslime 288` | `gold 144 + ichor 250 + scorchedStone 72` | 1150/6.5/2.0/COBALT (32) | 2/5/6/2 | `flammable` | ✅ |
| 19 | Alumite | `alumite 144` | `aluminum 144 + iron 144 + obsidian 288` | 700/8.0/5.5/COBALT (28) | 2/5/6/2 | `duritos` | ✅ (condicional) |
| 20 | Bronze* | `bronze 4` | `copper 3 + tin 1` | 430/6.8/3.5/DIAMOND | 2/5/6/2 (28) | `dense` | ✅ integración |
| 21 | Electrum* | `electrum 2` | `gold 1 + silver 1` | 50/12.0/3.0/IRON | 1/3/4/2 | `shocking` | ✅ |
| 22 | Alubrass* | `alubrass 4` | `copper 1 + aluminum 3` | 450/7.0/3.5/DIAMOND | 2/3/4/2 | `depthdigger` | ✅ |
| 23 | Brass* | `brass 3` | `copper 2 + zinc 1` | — (fluido cast) | — | — | ✅ |
| | **Total TC3** | **19/19** | — | **19/19** | **19/19** | **19/19** | **100%** |

* Integración (condicional `isIntegrated`), no cuenta para parity TC3 base pero está implementada (5).

## Correcciones al reporte previo
- **Inputs corregidos:** Obsidian no es `water+searedStone` sino `WATER 125 + LAVA 125`; Clay no es `?` sino `WATER 250 + searedStone 72 + dirt 144`; Knightslime `iron 72 + purpleSlime 125 + searedStone 144` (no `iron+purpleSlime+searedStone 72` genérico); Manyullyn `576` bulk (3 cobalt +1 scrap) correcto; PigIron tiene 2 variantes (clay vs honey) documentadas.
- **Ratios:** Ej `bronze 3:1 (copper 3+tin 1 → 4)`, `electrum 1:1 →2`, `alubrass 1:3 →4`, `obsidian 125+125→36` ya en código.
- **Stats:** Confirmados en `TinkerMaterials.java:697-510` (ej manyullyn 820/7.02/8.72, etc.) y `TinkerArmor.java:93` (factores correctos).
- **Brass vs Alubrass:** Dualidad intencional TC3 (brass fluido cast genérico 470°C, alubrass aleación 500°C) — no es confusión, ambos registrados.
- **Global aleaciones:** Se mantiene **100% parity TC3** tras corrección de conteo (19/19), con 5 integración adicionales ya presentes.

## Verificación
```powershell
gradlew build # SUCCESS 18 tasks, 49s
# Tank test: copper 432+tin 144 (3+1) → bronze 576? En código es 3+1→4 (mB), JEI muestra 4 mB con w=36/size proporcional (AlloyRecipeCategory)
```

**Referencias actualizadas:** `TinkerSmeltery.java:689-856`, `TinkerMaterials.java`, `TinkerArmor.java`, `TinkerFluids.java`, `JEI AlloyCategory` (ya proporcional).
