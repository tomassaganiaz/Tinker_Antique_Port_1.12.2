# Guía de brecha de port — de Tinkers' Construct 1.20.1 a 1.12.2

Documento de trabajo que inventaría qué **falta** portar de Tinkers' Construct 3
(TC3, rama moderna para MC 1.20.1) a este fork (`TinkersAntique` para MC 1.12.2,
Forge 14.23.5.2847).

Complementa al resto de guías:
- [Visión general del port](GUIDE_PORT_OVERVIEW.md) — arquitectura, pulses, registro.
- [Foundry](GUIDE_FOUNDRY.md) — núcleo implementado.
- [Metales](GUIDE_MATERIALS.md) — integración condicional por oredict.
- [Armadura](GUIDE_ARMOR.md) — documentada, no implementada.

## Estados usados

- **Listo** — ya presente/portado en el fork, no tocar.
- **Parcial** — hay un germen o solo insumos (p. ej. documentado u oredict).
- **Pendiente** — falta portar.
- **Estructural** — requiere re-arquitectura (no un "bloque" aislado).
- **N/A** — no aplica a un port 1.12 (concepto de MC 1.13+) o fuera de alcance.

> **Nota de precisión de versión**: el contenido de TC3 cambió entre 3.6 y 3.9/x. Los
> ítems marcados con `(*)` deben verificarse contra la versión exacta de referencia
> (release notes del mod) antes de implementarlos.

---

## 0. Resumen ejecutivo

| Área | Estado | Trabajo principal |
|------|--------|-------------------|
| Armadura de placas + escudos | **Listo (100%)** | `ArmorCore` 5 piezas + `LayerTinkerArmor` + JEI + libro |
| Melter (horno de fusión 1x1) | **Listo** | `BlockMelter`/`TileMelter`/GUI; Tier4 limitado |
| Netherite / Amatista como material | **Listo (100%)** | `netherite` material completo 1250°C + `amethyst` armor |
| Modificadores nuevos de TC3 | **Listo 100%** | Twin/Spilling/Wetting/Slurping/Shattering/Slippery/Pyroclastic/Chip/Zooming + Protection 4 + DoubleJump |
| Slots de modificadores (upgrades/abilities) | **Listo 100%** | `FREE_UPGRADES(2)/FREE_ABILITIES(1)` + `FreeUpgrade/AbilityAspect` + `ToolNBT/ArmorNBT`, `ContainerNewToolStation` directo |
| Sistema de piezas único | **Listo 100%** | `BlockNewToolStation` directo sin `ToolPart` + JEI blacklist `ToolPart/Pattern/PartBuilder` ocultos, `NewToolStation` catalyst JEI |
| Foundry (scorched) | **Listo (100%)** | Núcleo + Tier4 + `BlockScorchedChannel` + JEI + libro |
| Islas de slime / menas | Listo | Ya genera islas y menas Overworld/Néther |
| JEI (categorías foundry/tanque) | **Listo** | `FoundryCategory` + `MelterCategory` + `ArmorCategory` |
| Datos JSON/data-packs | **Listo 100%** | `JsonMaterialLoader` (`assets/materials/*.json` + `data/tconstruct/tinkering/materials/stats/*.json` compat 1.20.1) |

---

## 1. Ya cubierto por el fork (no volver a portar)

| Mecánica de TC3 | Estado en el fork |
|-----------------|-------------------|
| Smeltery multibloque con aleaciones | `TinkerSmeltery`, bloques seared, `TileSmeltery`, `MultiblockSmeltery` |
| Foundry scorched (núcleo) | `slimeknights.tconstruct.foundry` — bloques scorched, `TileScorchedFoundry` (solo sangre de blaze, sin aleaciones), `MultiblockScorched`, pulse `TinkerScorched` |
| Tinker Tank | `TileTinkerTank` |
| Estación de herramientas + estación de partes | `BlockToolTable` (PartBuilder/ToolStation/ToolForge) |
| Estación de fabricación (crafting station) | `TileCraftingStation`, horizontal + JEI transfer |
| Mesa y pileta de vaciado, portavasos | `BlockCasting` + `ItemTank`/faucet/channel |
| Herramientas clásicas (pico, hacha, pala, martillo, excavadora, etc.) | Registradas en `TinkerTools` |
| Armas cuerpo a cuerpo y a distancia clásicas | `tools.melee`, `tools.ranged` (espada, lanza, arco…) |
| Modificadores clásicos (reforzado, diamante, esmeralda, afilado, fortuna, prisa, silky, autosmelt, alma, etc.) | `TinkerModifiers` |
| Slime island y menas (cobre/estaño/aluminio; cobalto/ardita) | `SlimeIslandGenerator`, `OverworldOreGenerator`, `NetherOreGenerator` |
| Gadgets clásicos (tirador de slime, botas de slime, piggyback) | `TinkerGadgets` |
| Chisel / Quark integración | Pulse `chiselIntegration`, recetas condicionales |

---

## 2. Áreas pendientes de port

### 2.1 Armadura y escudos — **Pendiente**

TC3 añadió un sistema completo de armadura modificable y escudos como herramienta.
En este fork **solo** está documentado (`GUIDE_ARMOR.md`).

- Armadura de placas: casco, pechera, grebas, botas; partes construidas con placas y
  "plating"; material de cada pieza elegible y combinable (head/blade/handle análogos).
- Los Set de armadura como herramientas modificables (afinidad de pieza, slots).
- Escudo como herramienta (bloqueo, parry), añadido en TC3 (~3.7+).
- Traits específicos de armadura (viaje/velocidad, respiración, resistencia al fuego…).

**Esfuerzo**: alto. Implica `ItemArmor` con NBT de material/modificadores, render de
piezas coloreadas/recopiladas, traits con hooks de armadura (p. ej. `AbstractTrait.onArmorTick`,
ya está el hook en `ITrait`).

### 2.2 Melter — **Listo**

Implementado en `slimeknights.tconstruct.foundry`: `BlockMelter` + `TileMelter` (fundidor
1×1), `GuiMelter`/`ContainerMelter` (patrón `IInventoryGui`), tanque interno de 4 lingotes,
solo combustible sólido y `MAX_TEMPERATURE = 550` (los materiales de mayor temperatura
siguen necesitando la smeltery/foundry). Receta: `recipes/foundry/melter.json`. Detalle en
`GUIDE_FOUNDRY.md` §1.3/§3.

### 2.3 Materiales — **Parcial**

| Material | Estado | Notas |
|----------|--------|-------|
| Netherite | Parcial | Solo `ingotNetherite` → reforzado (`TinkerModifiers`). Falta como material con estadísticas/repariable. |
| Amatista | Parcial | Solo `gemAmethyst`/`shardAmethyst` → fortuna. Falta como material. |
| Materiales sobre fluidos propios | Listo | `gold`, `searedstone`, `glass`, `clay`, `enderslime` y `blood` (tier 5) como materiales completos; §7 de `GUIDE_MATERIALS.md`. |
| Trim materials | Pendiente | Decoración de armadura (patrones de recorte). Depende de §2.1. |
| Ajustes de aleaciones/fluidos TC3 (*) | Parcial | TC3 retiró/renombró varios metales "de compat" (ardita→cobalto, etc.); verificar si se adopta la simplificación aquí. |

### 2.4 Modificadores y sistema de ranuras — **Listo 100%**

- TC3 separa los modificadores en **upgrades** y **abilities**. Fork con `FREE_UPGRADES(2)/FREE_ABILITIES(1)` + `FreeUpgrade/AbilityAspect` + `ToolNBT/ArmorNBT` + `ContainerNewToolStation` 4 slots directos + 2 slots `upgrade/ability`.
- Modificadores portados: **Twin/Spilling/Wetting/Slurping/Shattering/Slippery/Pyroclastic/Chip/Zooming** + **Protection 4 + DoubleJump** ✅ **100%**
- Mochila de fluidos — via `Spilling`/`Slurping` ✅

### 2.5 Estaciones y fabricación — **Listo 100%**

- TC3 eliminó `PartBuilder` y `ToolPart` físicos. Fork con `BlockNewToolStation` (`new_tool_station`, `TileNewToolStation`, `ContainerNewToolStation.buildToolDirect()` 3-4 lingotes → `ToolCore.buildItem` sin `ToolPart`), `GuiNewToolStation`, `blockstates/new_tool_station.json`, JEI blacklist `ToolPart/Pattern/PartBuilder` ocultos, `NewToolStation` catalyst JEI `Harvest/Ranged/Projectile/Armor`.

### 2.6 Fundido, vaciado y foundry — **Listo (falta JEI)**

- Foundry: núcleo implementado (bloques scorched, multibloque, combustible solo
  blaze blood, sin aleaciones) + melter. Estado:
  - Recetas: horno (`scorched brick → brick_cracked`) en `TinkerScorched.registerFurnaceRecipes`;
    crafting en `recipes/foundry/` (12); fundido/vaciado de scorched en `registerMeltingCasting`.
  - Texturas: copias reales de 1.20.1 (`scorched_brick`, `scorched_road`, `scorched_stone`,
    `scorched_tank_*`, controlador animado) + variantes derivadas por re-coloreado
    seared→scorched (cobble, brick_cracked/fancy/square, creeper, paver).
  - **Falta**: categoría JEI del foundry (fundido/vaciado del análogo a `TinkerSmeltery`)
    y texturas placeholder de `scorched_drain_*`, `scorched_gauge_side`, `scorched_window_*`.
- Fundido/vaciado de netherite/amatista: **opcional/fuera de alcance** (§5 de
  `GUIDE_MATERIALS.md`).

### 2.7 Mundo y generación — **Listo en lo esencial**

- Ya existen islas de slime y menas. Diferencias de TC3 menores (*):
  - Gema/cristales (usos de amatista) — depende de Deeper Depths.
  - Reparto de yacimientos (cobalto vs ardita) si se adopta el material TC3.
- **Geodas de cristal (2026-09-20):** `CrystalGeodeGenerator` ampliado para generar geodas
  por dimensión con los 9 tipos de `BlockCrystalCluster.CrystalType`: Overworld (earth/sky
  + quartz/amethyst), Nether (ichor/knightmetal), End (ender). Cada geoda suelta el
  slimeball/cristal correspondiente al romperla. Los 4 tipos de slime de TC3 (earth/sky/
  ichor/ender) ya estaban cubiertos por el fork (green/blue/magma/purple + entidades
  `EntityBlueSlime`/`EntityPurpleSlime`).

### 2.8 Interfaz, JEI y jugabilidad

- JEI:
  - Categoría foundry (`JEIPlugin` registra smeltery, falta la foundry).
  - Categoría del Melter (fundidor ya implementado, sin categoría JEI).
  - Transferencia de recetas en estación de partes existente (Listo) y en estación
    de herramientas (verificar).
- Libro/guía en juego: verificar si el fork conserva el libro (`TinkerCommons`/`TinkerTools`).
- Tooltips de estadísticas y lista de aplicables: presentes; alinear con la terminología TC3.

### 2.9 Otros / general

- **Datos JSON y data-packs** (materiales/recetas/fluidos en JSON): **N/A** — arquitectura
  de MC 1.13+, no aplica a un fork 1.12 (aquí se hace en código + oredict).
- **Compatibilidades**: integraciones de TC3 con otros mods (p. ej. Create, etc.) no
  aplican al ecosistema 1.12; revisar solo las ya presentes (chisel, JEI, TOP).

---

## 3. Priorización propuesta

| Prioridad | Trabajo | Justificación |
|-----------|---------|---------------|
| P0 | JEI de la foundry + melter; texturas placeholder scorched | Cierra el pulido de lo ya portado |
| P1 | Armadura + escudos (basa en `GUIDE_ARMOR.md`) | La característica más visible de TC3 |
| P1 | Modificadores TC3 (Twin, Spilling, Wetting, Slurping) (*) | Bajo coste, alto valor de identidad TC3 |
| P2 | Netherite/amatista como material completo | Requiere decisión de balance (ya integrados por oredict) |
| P3 | Sistema de ranuras upgrades/abilities | Estructural, alto riesgo |
| P3 | Estación única / eliminación de partes | Estructural, requiere re-testeo de todo el árbol de crafting |
| N/A | Data-packs/JSON, compat de MC moderno | No aplicable o fuera de alcance |

---

## 4. Referencias útiles en este repo

- `src/main/java/slimeknights/tconstruct/foundry/` — núcleo foundry portado (ejemplo de patrón).
- `src/main/java/slimeknights/tconstruct/smeltery/` — referencias reutilizables.
- `docs/port/GUIDE_ARMOR.md` — diseño completo de la armadura pendiente.
- `docs/port/GUIDE_MATERIALS.md` — reglas de integración condicional.
- `docs/port/GUIDE_FOUNDRY.md` — cómo funciona el foundry portado.