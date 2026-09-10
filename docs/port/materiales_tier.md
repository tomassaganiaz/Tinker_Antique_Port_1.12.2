# Materiales por Tier — Implementados vs Faltantes (fork 1.12.2)

Comparativa del **sistema de materiales de TC3 (1.20.1)** contra el estado del fork
**TinkersAntique-1.12.2**. Tier system tomado de la referencia read-only
`TinkersConstruct-1.20.1/.../tools/data/material/MaterialDataProvider.java`
(`addMaterial(id, tier, order, ...)`). Estado del fork verificado contra
`tools/TinkerMaterials.java` y `resources/assets/tconstruct/materials/*.json`.

> Criterios: ✅ = material implementado en el fork (campo `Material` + stats);
> 🟡 = equivalente aproximado (nombre/idea distinta en el fork);
> ❌ = faltante en el fork; **N/A** = no portable a 1.12.2 (item o sistema inexistente);
> ➕ = material **solo-fork** (sin equivalente en TC3).

## Leyenda de columnas

- **Material TC3** — `MaterialId` de la referencia.
- **Orden** — `order` con el que TC3 llama a `addMaterial(id, tier, order, ...)`
  (`GENERAL`, `HARVEST`, `WEAPON`, `SPECIAL`, `RANGED`, `END`, `NETHER`, `BINDING`,
  `REPAIR`, `COMPAT`). Las filas están ordenadas como en el proveedor de datos.
- **Estado** — implementación en el fork.
- **Material fork** — identificador real en `TinkerMaterials.java`.
- **Notas** — gating/oredict/config.

El **tier** no es una columna: cada tabla es un tier (el título de la sección), que es
como lo agrupa el `MaterialDataProvider`. Total de materiales TC3 sin contar
compatibilidades: **80** (4 + 12 + 28 + 18 + 17 + 1).

---

## Tier 0 — Básicos

**Tier 1 cerrado**: los 12 materiales están cubiertos (10 ✅ + 1 🟡 por nombre distinto +
1 N/A). `bamboo` pasó a ✅ cuando se vio que **Future MC lo portea** (oredict `cropBamboo`);
solo `leaves` queda 🟡 (renombrar `leaf` rompería herramientas guardadas) y `horn` no es
portable.

| Material TC3 | Orden | Estado | Material fork | Notas |
|--------------|-------|--------|---------------|-------|
| `wood`        | GENERAL | ✅ | `wood` | |
| `string`      | GENERAL | ✅ | `string` | |
| `feather`     | GENERAL | ✅ | `feather` | |
| `leather`     | BINDING | ✅ | `leather` | `Items.LEATHER`; Extra(50) + BowString(1.0) — binding y cuerda de arco |

## Tier 1

| Material TC3 | Orden | Estado | Material fork | Notas |
|--------------|-------|--------|---------------|-------|
| `rock`       | HARVEST | ✅ | `stone` | TC3 renombró stone→rock |
| `flint`      | WEAPON  | ✅ | `flint` | |
| `copper`     | SPECIAL | ✅ | `copper` | Ya integrado como metal tier 1 |
| `bone`       | SPECIAL | ✅ | `bone` | |
| `bamboo`     | RANGED  | ✅ | `bamboo` | `cropBamboo` (Future MC); limb + asta; `reed` sigue como material propio del fork
| `chorus`     | END     | ✅ | `chorus` | `Items.CHORUS_FRUIT_POPPED` (1 = 144); rasgo `enderference` |
| `vine`       | BINDING | ✅ | `vine` | |
| `cactus`     | BINDING | ✅ | `cactus` | Material + `cactus.json`; rasgos `prickly` (HEAD) y `spiky` |
| `wool`       | BINDING | ✅ | `wool` | Lana (wildcard de color); solo cabeza de flecha + binding |
| `leaves`     | BINDING | 🟡 | `leaf` | TC3 "leaves" ≈ fork `leaf`; renombrar rompería herramientas guardadas |
| `paper`      | BINDING | ✅ | `paper` | Material + `paper.json` |
| `horn`       | REPAIR  | N/A | — | TC3 lo hace con **cuerno de cabra** (`Items.GOAT_HORN`, 1.19+) y su único uso es `RepairStats.ribcage` (armadura): ni el item ni el sistema existen en 1.12.2 |

### Cierre del tier 1 — auditoría de usos

Comparativa de lo que TC3 permite hacer con cada material (`MaterialStatsDataProvider`) y
lo que permite el fork. ✅ = el fork ya lo cubre, ➖ = TC3 no lo usa, ✗ = no portable.

| Material fork | Cabeza | Limb (arco) | Asta | Pluma | Binding | Cuerda |
|---------------|--------|-------------|------|-------|---------|--------|
| `wood`        | ✅ | ✅ | ✅ | ➖ | ➖ | ➖ |
| `stone` (rock) | ✅ | ➖¹ | ➖ | ➖ | ✅ | ➖ |
| `flint`       | ✅ | ➖¹ | ➖ | ➖ | ✅ | ➖ |
| `copper`      | ✅ | ✅ | ➖ | ➖ | ✅ | ➖ |
| `bone`        | ✅ | ✅ | ✅ | ➖ | ✅ | ➖ |
| `reed` (bamboo) | ➖ | ✅ | ✅ | ➖ | ➖ | ➖ |
| `chorus`      | ✅ | ✅ | ✅ **nuevo** | ➖ | ✅ | ➖ |
| `vine`        | ➖ | ➖ | ➖ | ➖ | ✅ | ✅ |
| `cactus`      | ✅ | ✅ | ✅ **nuevo** | ➖ | ✅ | ➖ |
| `wool`        | ✅ (punta) | ➖ | ➖ | ➖ | ✅ | ➖ |
| `leaf` (leaves) | ➖ | ➖ | ➖ | ✅ | ➖ | ➖ |
| `paper`       | ✅ | ✅ | ➖ | ✅ **nuevo** | ✅ | ➖ |
| `string`      | ➖ | ➖ | ➖ | ➖ | ✅ | ✅ |
| `leather`     | ➖ | ➖ | ➖ | ➖ | ✅ | ✅ |

¹ En el fork `stone`/`flint` sí tienen `BowMaterialStats` (el valor
`whyWouldYouMakeABowOutOfThis`), mientras que TC3 no les da limb: es una licencia del
fork anterior a este port, se deja como está.

Últimos huecos cubiertos en este cierre (faltaban frente a `addAmmo()` de TC3):

- `cactus` → `ArrowShaftMaterialStats(0.85f, 2)`
- `chorus` → `ArrowShaftMaterialStats(0.9f, 3)`
- `paper` → `FletchingMaterialStats(0.9f, 0.8f)` (TC3: `FLETCHING` con rasgo `weak`)

Con esto el tier 1 queda **funcionalmente completo**: los 11 materiales portables hacen
todo lo que hacen en TC3 salvo armadura/escudos (N/A en el fork).

## Tier 2

| Material TC3 | Orden | Estado | Material fork | Notas |
|--------------|-------|--------|---------------|-------|
| `iron`           | GENERAL | ✅ | `iron` | |
| `searedStone`    | HARVEST | ✅ | `searedstone` | Fluido `stone` ya existente; craft con `ingotBrickSeared`/`blockSeared` |
| `venombone`      | WEAPON  | 🟡 | `bloodbone` | TC3 renombró bloodbone→venombone (`addRedirect`) |
| `slimewood`      | SPECIAL | N/A | — | TC3 lo hace con **sus maderas de slime** (greenheart/skyroot/bloodshroom/enderbark); el fork no tiene madera de slime (los árboles de slime usan slime congelado como tronco) |
| `slimeskin` | BINDING | ✅ | `slimeskin` | Compuesto cuero + 250 mB de `greenSlime`; Extra(60) + cuerda |
| `gold`           | REPAIR  | ✅ | `gold` | Fluido + integración oredict `Gold` (mena/lingote/bloque), con toolforge |
| `scorchedStone`  | NETHER  | ✅ | `scorchedstone` | Fluido nuevo `scorched_stone` (800 ºC); fundido/vaciado en la foundry; rasgo `superheat` |
| `necroticBone`   | NETHER  | ✅ | `necroticbone` | `matNecroticBone` (oredict `boneWithered`); rasgo `poisonous` |
| `whitestone`     | END     | 🟡 | `endstone` | TC3 whitestone ≈ endstone del fork |
| `skyslimeVine`   | BINDING | 🟡 | `slimevine_blue` | Sky slime ≈ slime azul del fork |
| `weepingVine`    | BINDING | ✅ | `weepingvine` | `nb:crimson_vine` (Unseen's Nether Backport); Extra(45) + cuerda, rasgo `flammable` |
| `twistingVine`   | BINDING | ✅ | `twistingvine` | `nb:warped_vine` (Unseen's Nether Backport); Extra(45) + cuerda, rasgo `freezing` |
| `turtle`         | BINDING | N/A | — | El escudo de tortuga existe (OceanicExpanse), pero en TC3 solo sirve para **armadura** (`RepairStats.shell`/maille) |
| `nautilus`       | BINDING | N/A | — | La concha de náutilo existe (OceanicExpanse/Future MC/Fossils), pero en TC3 solo es **armadura** (`RepairStats.shell`) |
| `amethyst`       | REPAIR  | ✅ | `amethyst` | oredict `gemAmethyst` (Deeper Depths); punta de flecha, rasgo `jagged` |
| `prismarine`     | REPAIR  | ✅ | `prismarine` | |
| `earthslime`     | REPAIR  | 🟡 | `slime` | TC3 earth slime ≈ slime verde del fork |
| `skyslime`       | REPAIR  | 🟡 | `blueslime` | TC3 sky slime ≈ slime azul |
| `blaze`          | REPAIR  | ✅ | `blaze` | Material de flecha |
| `enderPearl`     | REPAIR  | ✅ | `enderpearl` | `Items.ENDER_PEARL`; punta de flecha, rasgo `enderference` |
| `glass`          | REPAIR  | ✅ | `glass` | Craft con `blockGlass`/`paneGlass`; fluido `glass` (arena fundida) |
| `slimeball`      | REPAIR  | 🟡 | `slime` | Equivalente por slimeball verde |
| `gunpowder`      | REPAIR  | ✅ | `gunpowder` | `Items.GUNPOWDER`; punta de flecha, rasgo `flammable` |
| `redstone`       | REPAIR  | ✅ | `redstone` | `Items.REDSTONE`; punta de flecha, rasgo `shocking` |
| `clay`           | REPAIR+5 | ✅ | `clay` | Craft con `clay`/`blockClay`; fluido `clay` |
| `honey`          | REPAIR+5 | N/A | — | El botella de miel existe (Future MC), pero en TC3 solo es **slimesuit** (`SlimeStats`) |
| `cheese`         | REPAIR  | ✅ | `cheese` | Leche en molde de lingote -> secadero -> `ingotCheese`; Extra(111) + cuerda
| `phantom`        | REPAIR+5 | N/A | — | Membrana de phantom (1.13+), no existe; y en TC3 solo es armadura/elytra (shell + repair kit) |

## Tier 3

| Material TC3 | Orden | Estado | Material fork | Notas |
|--------------|-------|--------|---------------|-------|
| `slimesteel`     | GENERAL | ✅ | `slimesteel` | Aleación: 1 hierro + 1 slime azul + 2 ladrillos seared = 2 lingotes; rasgo `dense` |
| `amethystBronze` | HARVEST | ✅ | `amethystbronze` | Aleación: 1 cobre + 1 amatista = 2 lingotes; fluido `amethyst` nuevo; rasgo `crumbling` |
| `nahuatl` | WEAPON | ✅ | `nahuatl` | Compuesto madera + obsidiana fundida; Head(350, 4.5, DIAMOND, 3) |
| `obsidian`       | WEAPON  | ✅ | `obsidian` | |
| `roseGold`       | SPECIAL | ✅ | `rosegold` | Aleación: 1 cobre + 1 oro = 2 lingotes (ya existían ambos fluidos); rasgo `established` |
| `pigIron`        | SPECIAL | ✅ | `pigiron` | |
| `steel`          | NETHER  | ✅ | `steel` | |
| `cobalt`         | NETHER  | ✅ | `cobalt` | |
| `darkthread` | BINDING | ✅ | `darkthread` | Compuesto cuerda + obsidiana fundida; Extra(140) + cuerda |
| `ichorskin`      | BINDING | N/A | — | Compuesto cuero + **icor** (slime propio de TC3, que no existe en el fork) |
| `magnetite`      | REPAIR  | N/A | — | Se hace con `STEEL_SHARD`, **contenido propio de TC3** |
| `quartz`         | REPAIR+NETHER | ✅ | `quartz` | `Items.QUARTZ`; punta de flecha, rasgo `fractured` |
| `glowstone`      | REPAIR+NETHER | ✅ | `glowstone` | `Items.GLOWSTONE_DUST`; punta de flecha, sin rasgo |
| `ichor`          | REPAIR+NETHER | N/A | — | `ichorGeode`: geoda del slime de icor de TC3, no existe en el fork |
| `kobold`         | REPAIR+NETHER | N/A | — | Se hace con `COBALT_SHARD`, **contenido propio de TC3** |
| `magma`          | REPAIR+NETHER | 🟡 | `magmaslime` | TC3 magma cream ≈ magmaslime del fork |
| `ice`            | BINDING | ✅ | `ice` | (En TC3 es tier 3; en el fork es material de flecha) |
| `jadeite`        | BINDING | N/A | — | Compuesto blaze + esmeralda fundida; su único uso es `RepairStats.ribcage` (**armadura**) |

## Tier 4

| Material TC3 | Orden | Estado | Material fork | Notas |
|--------------|-------|--------|---------------|-------|
| `queensSlime`   | GENERAL | ✅ | `queensslime` | Aleación: 1 cobalto + 1 oro + 1 crema de magma = 2 lingotes; rasgo `slimeyBlue` |
| `cinderslime`   | GENERAL | N/A | — | Aleación de TC3 con **icor** (slime propio de TC3), no existe en el fork |
| `hepatizon`     | HARVEST | ✅ | `hepatizon` | Aleación: 2 cobre + 1 cobalto + 1 cuarzo = 2 lingotes; rasgo `momentum` |
| `manyullyn`     | WEAPON  | ✅ | `manyullyn` | |
| `blazingBone`   | SPECIAL | ✅ | `blazingbone` | Usa `matBlazingBone`, que el fork **ya** fabrica (hueso necrótico + sangre de blaze); asta + rasgo `shocking` |
| `knightmetal`   | END     | N/A | — | Metal de TC3 con **lingote propio** (`ingotKnightmetal`) y fluido: requiere ítems nuevos |
| `knightslime`   | END     | ✅ | `knightslime` | |
| `jeweledHide` | BINDING | ✅ | `jeweledhide` | Compuesto cuero + diamante fundido; Extra(250) + cuerda |
| `ancientHide`   | BINDING | N/A | — | **Item propio de TC3** que se funde en `moltenDebris` |
| `ancient`       | NETHER  | ✅ | `ancient` | Crafteable con `nb:nether_scrap` (Nether Backport); rasgo `duritos` |
| `blazewood` | BINDING | ✅ | `blazewood` | Compuesto madera ardiente + sangre de blaze (sustituye al bloodshroom); asta |
| `shulker`       | REPAIR  | ✅ | `shulker` | `Items.SHULKER_SHELL` (1.9+, existe en 1.12); rasgo `hovering` |
| `dragonScale` | REPAIR | ✅ | `dragonscale` | Drop del **dragón del End** (6-12); mismo oredict que Ice and Fire; punta de flecha |
| `enderslime`    | REPAIR  | ✅ | `enderslime` | `slimeballPurple` → fluido `purpleSlime` (renombre purple→ender de TC3) |
| `knightly`      | REPAIR  | N/A | — | Se hace con `KNIGHTMETAL_SHARD`, **contenido propio de TC3** |
| `endRod`        | REPAIR  | ✅ | `endrod` | |
| `enderslimeVine`| BINDING | 🟡 | `slimevine_purple` | Ender slime ≈ slime púrpura del fork |

## Tier 5

TC3 solo define **un** material en el tier más alto: `blood` (`ORDER_REPAIR`).
Está implementado en el fork, así que **el tier 5 está completo (1/1)**.

| Material TC3 | Orden | Estado | Material fork | Notas |
|--------------|-------|--------|---------------|-------|
| `blood` | REPAIR | ✅ | `blood` | `slimeballBlood` → fluido `blood`; material de mayor nivel del fork |

### Implementación de `blood` (tier 5)

Registrado en `TinkerMaterials.java` igual que el resto del lote "fluidos ya existentes"
(ver `GUIDE_MATERIALS.md` §7):

| Pieza | Stats |
|-------|-------|
| Head | `HeadMaterialStats(500, 6.50f, 6.50f, COBALT)` — nivel de picota `COBALT` (4), como manyullyn |
| Handle | `HandleMaterialStats(0.85f, 50)` |
| Extra | `ExtraMaterialStats(400)` — `Extra` más alto del fork (encantabilidad) |
| Bow | `BowMaterialStats(0.7f, 1.3f, 5f)` |

- **Traits**: `splintering` (HEAD) y `raging` (cualquier pieza). Es la versión "fluida" de
  `bloodbone` (que usa `raging2` + `splintering` + `raging` + `fractured`).
- **Insumo de crafteo**: `slimeballBlood` (250 = `Material.VALUE_SlimeBall`), el mismo
  valor que ya usaba `TinkerSmeltery` para fundirlo, así que no hay arbitraje
  crafteo↔fundición.
- **Gating**: la integración se crea bajo `if(isSmelteryLoaded())` porque el fluido
  `blood` lo crea `TinkerFluids` en el evento de registro de bloques; el enlace
  `setFluid`/`setCastable` se hace en `TinkerMaterials.setupMaterials` (init), como
  `obsidian`. Sin el pulse de la smeltery, el material queda oculto.
- **Assets**: `resources/assets/tconstruct/materials/blood.json` (`type: colored`, color
  del fluido `540000`) + `material.blood.name` en `en_us.lang` (`Blood`) y `es_es.lang`
  (`Sangre`).

## Compatibilidad (mod integration, tiers TC3)

| Material TC3 | Tier | Orden | Estado | Material fork | Notas |
|--------------|------|-------|--------|---------------|-------|
| `osmium`      | 2 | COMPAT+GENERAL | ❌ | — | Mod integration |
| `lead`        | 2 | COMPAT+HARVEST | ✅ | `lead` | |
| `silver`      | 2 | COMPAT+WEAPON  | ✅ | `silver` | |
| `aluminum`    | 2 | COMPAT+RANGED  | ✅ | `aluminum` | **Nuevo** como material tier 1 del fork |
| `ironwood` | 2 | COMPAT+GENERAL | ✅ | `ironwood` | oredict `ingotIronwood` (Twilight Forest) |
| `treatedWood` | 2 | COMPAT+GENERAL | ❌ | — | |
| `electrum`    | 3 | COMPAT+GENERAL | ✅ | `electrum` | |
| `bronze`      | 3 | COMPAT+HARVEST | ✅ | `bronze` | |
| `constantan` | 3 | COMPAT+HARVEST | ✅ | `constantan` | Aleación: 1 cobre + 1 níquel = 2 lingotes |
| `invar` | 3 | COMPAT+WEAPON | ✅ | `invar` | Aleación: 2 hierro + 1 níquel = 3 lingotes |
| `pewter` | 3 | COMPAT+WEAPON | ✅ | `pewter` | Aleación: 3 estaño + 1 plomo = 4 lingotes |
| `platedSlimewood` | 3 | COMPAT+SPECIAL | ❌ | — | |
| `necronium`   | 3 | COMPAT+WEAPON  | ❌ | — | |
| `steeleaf` | 3 | COMPAT+SPECIAL | ✅ | `steeleaf` | oredict `ingotSteeleaf` (Twilight Forest) |
| `fiery` | 4 | COMPAT+END | ✅ | `fiery` | oredict `ingotFiery` (Twilight Forest) |
| `nicrosil` | 4 | COMPAT+WEAPON | ✅ | `nicrosil` | Aleación: 2 níquel + esmeralda + cuarzo = 4 lingotes |

## Solo-fork (sin equivalente en TC3)

Materiales presentes en `TinkerMaterials.java` que **no existen** en la lista TC3:

| Material fork | Notas |
|---------------|-------|
| `ardite`      | TC3 la eliminó (manyullyn usa debris/netherite en 1.20.1) |
| `alubrass`    | Aliación de fundición propia de TCon 1.12 (no es material TC3) |
| `alumite`     | Ídem; aluminio+iron+obsidian de TCon 1.12 |
| `netherrack`  | TC3 lo sustituye por `scorchedStone` |
| `tin`         | En TC3 solo es insumo de aleación `bronze` (compat), no material |
| `firewood`    | Bloque de madera ardiente de TCon 1.12 |
| `reed`        | Caña (shaft) |
| `sponge`      | |
| `slimeleaf_blue/orange/purple` | Flechado de hojas de slime |
| `slimevine_blue/purple` | Cuerda de enredadera de slime (≈ vines TC3) |
| `xu`          | `unstable`, material oculto experimental |

Los materiales añadidos durante el port (`chorus`, `wool`, `gold`, `searedstone`,
`scorchedstone`, `glass`, `clay`, `enderslime`, `blood`, `leather`, `necroticbone`,
`enderpearl`, `gunpowder`, `redstone`, `amethyst`, `quartz`, `glowstone`, `shulker`,
`weepingvine`, `twistingvine`, `slimesteel`, `rosegold`, `amethystbronze`, `queensslime`,
`hepatizon`, `blazingbone` y `ancient`) **sí tienen equivalente en TC3**: son los que han
ido pasando de ❌ a ✅ en las tablas de arriba.

## Resumen de estado

| Tier | Total TC3 | Implementado (✅) | Parcial (🟡) | Faltante (❌) | N/A |
|------|-----------|-------------------|--------------|---------------|-----|
| 0 | 4 | 4 | 0 | 0 | 0 |
| 1 | 12 | 10 | 1 | 0 | 1 |
| 2 | 28 | 17 | 6 | 0 | 5 |
| 3 | 18 | 12 | 1 | 0 | 5 |
| 4 | 17 | 12 | 1 | 0 | 4 |
| 5 | 1 | 1 | 0 | 0 | 0 |
| **Total** | **80** | **56** | **9** | **0** | **15** |

(Compat excluido del resumen: 16 materiales, 12 ✅ / 4 ❌.)

## Notas técnicas

- **Implementados**: solo cuentan los declarados como `Material` en
  `TinkerMaterials.java` (72 identificadores). Los fluidos **sin** material siguen siendo:
  `zinc`, `nickel`, `brass`, `emerald`, `diamond`, `dirt`, `calcium`, `blazing_blood` y los
  nuevos insumos de aleación `magma` y `quartz` (este último también es material, pero el
  fluido solo alimenta el hepatizon).

### Lotes de implementación (en orden cronológico)

| # | Lote | Materiales | Guía |
|---|------|------------|------|
| 1 | Metales tier 1 (previo) | `tin`, `aluminum` | `GUIDE_MATERIALS.md` §5 |
| 2 | Fluidos ya existentes | `gold`, `searedstone`, `glass`, `clay`, `enderslime`, `blood` | §7 |
| 3 | Tier 1 | `chorus`, `wool` + `reed` (arco) + JSON de `cactus`/`paper`/`reed` | §8 |
| 4 | Ammo y bindings | `leather`, `necroticbone`, `enderpearl`, `gunpowder`, `redstone`, `amethyst`, `quartz`, `glowstone`, `shulker` | §9 |
| 5 | Nether Backport | `weepingvine`, `twistingvine` | §10 |
| 6 | Nether/foundry | `scorchedstone` (+ fluido `scorched_stone`) | §11 |
| 7 | Tier 3 (aleaciones) | `slimesteel`, `rosegold`, `amethystbronze` (+ fluido `amethyst`) | §12 |
| 8 | Tier 4 | `queensslime`, `hepatizon` (+ fluidos `magma`, `quartz`), `blazingbone`, `ancient` | §13 |
| 9 | Compat de mods instalados | `ironwood`, `steeleaf`, `fiery` (Twilight Forest) y `dragonscale` (drop del dragón del End) | §14 |
| 10 | Aleaciones de compat | `constantan`, `invar`, `pewter`, `nicrosil` (fluidos nuevos) | §14 |
| 11 | Compuestos de TC3 | `slimeskin`, `nahuatl`, `darkthread`, `jeweledhide`, `blazewood` (items meta 24-28 + texturas re-coloreadas) | §14 |
| 12 | Correcciones de aleaciones + Future MC | `bamboo` (`cropBamboo`), `cheese` (leche + secadero); slimesteel y amethyst bronze ajustados a las cantidades del libro | §14.4 |
- **Lote "fluidos ya existentes"** (§7 de `GUIDE_MATERIALS.md`): `gold`, `searedstone`,
  `glass`, `clay`, `enderslime` y `blood`. Cubre 6 de los ❌ que había en los tiers 2, 4
  y 5 sin crear ni un fluido nuevo.
- **Lote "tier 1"** (§8 de `GUIDE_MATERIALS.md`): `chorus` y `wool` como materiales
  nuevos, `reed` ampliado a material de arco (equivalente de `bamboo`) y JSON de render
  para `cactus`, `paper` y `reed`.
- **Lote "Nether Backport"** (§10 de `GUIDE_MATERIALS.md`): `weepingVine` y `twistingVine`
  con `nb:crimson_vine` / `nb:warped_vine` de Unseen's Nether Backport. Como el mod **no**
  registra oredict para las vides, el gate es `Loader.isModLoaded("nb")` y el ítem se
  resuelve por id en init (sin dependencia en `build.gradle`).
- **No portables (N/A), por tres motivos distintos**:
  1. **El item no existe en 1.12.2**: `horn` (cuerno de cabra, 1.19+), `phantom` (1.13+).
     ~~`bamboo`~~ y ~~`cheese`~~ salieron de esta lista: Future MC portea el bambú y el
     queso se fabrica con leche + secadero. `turtle`/`nautilus`/`honey` sí tienen item pero
     caen en el motivo 2.
  2. **El item existe pero su uso en TC3 es solo armadura/slimesuit**: `turtle`,
     `nautilus`, `honey`, `phantom`, `jadeite`, `ichorskin` (ver §12).
  3. **Contenido propio de TC3**: `slimewood` (sus 4 maderas de slime), `cinderslime`
     (icor), `blazewood` (bloodshroom), `jeweledHide`, `ancientHide`, `dragonScale`,
     `knightly`, `knightmetal`, `magnetite`/`kobold` (`STEEL_SHARD`/`COBALT_SHARD`),
     `nahuatl`/`darkthread` (compuestos con obsidiana fundida).
- **Gating oredict/config**:
  - `tin`/`aluminum` (nuevos tier 1): visibles solo si `ingotTin`/`ingotAluminum`
    existen en oredict (gateado además por `Config.registerAllCommonMetals`, default `true`).
  - `searedstone`/`glass`/`clay`/`enderslime`/`blood`: solo se integran si el pulse de la
    smeltery está cargado (`isSmelteryLoaded()`), porque sus fluidos se crean en el
    evento de registro de bloques.
  - `gold`: vía completa `MaterialIntegration(material, fluid, "Gold")` + `toolforge()`;
    su fluido se crea en el bloque estático de `TinkerFluids`, así que está disponible
    desde preinit.
  - `amethyst`: oredict `gemAmethyst` (Deeper Depths). Sin el mod no se integra; con él
    también alimenta el fluido `amethyst` del bronce de amatista.
  - `netherite`: sigue siendo **solo** insumo del modificador `modReinforced` vía
    `ingotNetherite` (Future MC / Nether Backport). No es material de TC3.
  - `weepingvine`/`twistingvine`/`ancient`: `Loader.isModLoaded("nb")` + ítem resuelto por
    id en init, porque el Nether Backport no registra oredict para esos objetos.
  - El resto de metales se integra vía `TinkerIntegration` (`oreRequirement`).
- **Renombres TC3 a tener en cuenta en futuros ports**: `stone→rock`,
  `bloodbone→venombone`, `endstone→whitestone`, `leaf→leaves`, `slime→earthslime`,
  `blueslime→skyslime`, `magmaslime→magma`, `endrod→endRod`, `slimevine_blue→skyslimeVine`,
   `slimevine_purple→enderslimeVine`, `purpleslime→enderslime`.

---
# Anexo A — Categorías del libro de TC3

Las tablas por tier (arriba) agrupan por `addMaterial(id, tier, order, ...)`, que es lo que
usa el data provider. El **libro del juego** agrupa distinto: por *para qué sirve* el
material. Esa agrupación sale de `MaterialStatsDataProvider`:

| Sección del libro | Stats que la definen | ¿Aplica al fork? |
|-------------------|----------------------|------------------|
| **General** (melee/harvest) | `HeadMaterialStats` + `BINDING` | Sí |
| **Ranged** (limbs, grips) | `LimbMaterialStats`/`GripMaterialStats` + `BOWSTRING` | Parcial: el fork tiene `BowMaterialStats` |
| **Armor** (plating, maille, cuirass, shield core) | `PlatingMaterialStats`, `MAILLE`, `CUIRASS`, `SHIELD_CORE` | **No** (el fork no tiene armadura ni escudos) |
| **Ammo** (puntas, astas, plumas) | `ARROW_HEAD`, `ARROW_SHAFT`, `FLETCHING` | Parcial: el fork no separa la punta de flecha del resto de cabezas |
| **Slimesuit** (slime, skull, ribcage, shell, laces, repair) | `SlimeStats`, `SkullStats`, `RepairStats` | **No** |

> En el fork **no hay restricción por pieza**: basta `HeadMaterialStats` para que un
> material aparezca en *todas* las cabezas (pico, hacha, hoja de espada **y punta de
> flecha**). Por eso los materiales que en TC3 son solo "advanced ammo" aquí son cabezas
> débiles; se documenta en cada fila.

> Actualizado tras los lotes 3-8 (ver *Lotes de implementación*). Los estados coinciden con
> los de las tablas por tier.

## A.1 — General (melee/harvest)

| Material TC3 | Tier | Material fork | Estado | Notas |
|--------------|------|---------------|--------|-------|
| `wood` | 0 | `wood` | ✅ | |
| `rock` | 1 | `stone` | ✅ | |
| `flint` | 1 | `flint` | ✅ | |
| `copper` | 1 | `copper` | ✅ | |
| `bone` | 1 | `bone` | ✅ | |
| `chorus` | 1 | `chorus` | ✅ | |
| `string` | 0 | `string` | ✅ | Solo binding |
| `leather` | 0 | `leather` | ✅ | Solo binding + cuerda |
| `vine` | 1 | `vine` | ✅ | Solo binding |
| `treatedWood` | 2 | — | ❌ | Compat, ningún mod del pack lo aporta |
| `iron` | 2 | `iron` | ✅ | |
| `searedStone` | 2 | `searedstone` | ✅ | |
| `venombone` | 2 | `bloodbone` | 🟡 | |
| `slimewood` | 2 | — | N/A | Maderas de slime de TC3 |
| `scorchedStone` | 2 | `scorchedstone` | ✅ | Fluido propio |
| `necroticBone` | 2 | `necroticbone` | ✅ | |
| `whitestone` | 2 | `endstone` | 🟡 | |
| `skyslimeVine` | 2 | `slimevine_blue` | 🟡 | Solo binding |
| `weepingVine` | 2 | `weepingvine` | ✅ | Nether Backport |
| `twistingVine` | 2 | `twistingvine` | ✅ | Nether Backport |
| `osmium` | 2 | — | ❌ | Compat |
| `silver` | 2 | `silver` | ✅ | |
| `lead` | 2 | `lead` | ✅ | |
| `ironwood` | 2 | `ironwood` | ✅ | oredict `ingotIronwood` (Twilight Forest) |
| `slimesteel` | 3 | `slimesteel` | ✅ | Aleación |
| `amethystBronze` | 3 | `amethystbronze` | ✅ | Aleación |
| `nahuatl` | 3 | `nahuatl` | ✅ | Compuesto madera + obsidiana fundida; Head(350, 4.5, DIAMOND, 3) |
| `pigIron` | 3 | `pigiron` | ✅ | |
| `roseGold` | 3 | `rosegold` | ✅ | Aleación |
| `cobalt` | 3 | `cobalt` | ✅ | |
| `steel` | 3 | `steel` | ✅ | |
| `darkthread` | 3 | `darkthread` | ✅ | Compuesto cuerda + obsidiana fundida; Extra(140) + cuerda |
| `bronze` | 3 | `bronze` | ✅ | |
| `constantan` | 3 | `constantan` | ✅ | Aleación: 1 cobre + 1 níquel = 2 lingotes |
| `invar` | 3 | `invar` | ✅ | Aleación: 2 hierro + 1 níquel = 3 lingotes |
| `pewter` | 3 | `pewter` | ✅ | Aleación: 3 estaño + 1 plomo = 4 lingotes |
| `necronium` | 3 | — | ❌ | Compat |
| `electrum` | 3 | `electrum` | ✅ | |
| `platedSlimewood` | 3 | — | ❌ | Compat |
| `steeleaf` | 3 | `steeleaf` | ✅ | oredict `ingotSteeleaf` (Twilight Forest) |
| `cinderslime` | 4 | — | N/A | Requiere icor |
| `queensSlime` | 4 | `queensslime` | ✅ | Aleación |
| `hepatizon` | 4 | `hepatizon` | ✅ | Aleación |
| `manyullyn` | 4 | `manyullyn` | ✅ | |
| `blazingBone` | 4 | `blazingbone` | ✅ | Item ya existente |
| `jeweledHide` | 4 | `jeweledhide` | ✅ | Compuesto cuero + diamante fundido; Extra(250) + cuerda |
| `ancientHide` | 4 | — | N/A | Item de TC3 |
| `ancient` | 4 | `ancient` | ✅ | Nether Backport |
| `knightmetal` | 4 | — | N/A | Metal con lingote propio |
| `knightslime` | 4 | `knightslime` | ✅ | |
| `enderslimeVine` | 4 | `slimevine_purple` | 🟡 | Solo binding |
| `fiery` | 4 | `fiery` | ✅ | oredict `ingotFiery` (Twilight Forest) |
| `nicrosil` | 4 | `nicrosil` | ✅ | Aleación: 2 níquel + esmeralda + cuarzo = 4 lingotes |

## A.2 — Ranged (limb/grip + bowstring)

| Material TC3 | Tier | Material fork | Estado | Notas |
|--------------|------|---------------|--------|-------|
| `wood` | 0 | `wood` | ✅ | Limb+Grip |
| `bamboo` | 1 | `reed` | 🟡 | Caña: limb + asta |
| `cactus` | 1 | `cactus` | ✅ | |
| `bone` | 1 | `bone` | ✅ | |
| `copper` | 1 | `copper` | ✅ | |
| `chorus` | 1 | `chorus` | ✅ | |
| `string` | 0 | `string` | ✅ | Bowstring |
| `leather` | 0 | `leather` | ✅ | Bowstring |
| `vine` | 1 | `vine` | ✅ | Bowstring |
| `weepingVine` | 2 | `weepingvine` | ✅ | Bowstring |
| `twistingVine` | 2 | `twistingvine` | ✅ | Bowstring |
| `skyslimeVine` | 2 | `slimevine_blue` | 🟡 | Bowstring |
| `enderslimeVine` | 4 | `slimevine_purple` | 🟡 | Bowstring |
| `cheese` | 2 | — | N/A | No hay queso |
| `slimeskin` | 2 | `slimeskin` | ✅ | Compuesto cuero + 250 mB de `greenSlime`; Extra(60) + cuerda |
| `slimewood` | 2 | — | N/A | Maderas de slime de TC3 |
| `venombone` | 2 | `bloodbone` | 🟡 | |
| `iron` | 2 | `iron` | ✅ | |
| `necroticBone` | 2 | `necroticbone` | ✅ | |
| `aluminum` | 2 | `aluminum` | ✅ | |
| `silver` | 2 | `silver` | ✅ | |
| `lead` | 2 | `lead` | ✅ | |
| `treatedWood` | 2 | 2 | — | ❌ |
| `ironwood` | 2 | `ironwood` | ✅ | oredict `ingotIronwood` (Twilight Forest) |
| `slimesteel` | 3 | `slimesteel` | ✅ | |
| `nahuatl` | 3 | `nahuatl` | ✅ | Compuesto madera + obsidiana fundida; Head(350, 4.5, DIAMOND, 3) |
| `amethystBronze` | 3 | `amethystbronze` | ✅ | |
| `roseGold` | 3 | `rosegold` | ✅ | Limb + bowstring |
| `pigIron` | 3 | `pigiron` | ✅ | |
| `cobalt` | 3 | `cobalt` | ✅ | |
| `darkthread` | 3 | `darkthread` | ✅ | Compuesto cuerda + obsidiana fundida; Extra(140) + cuerda |
| `invar` | 3 | `invar` | ✅ | Aleación: 2 hierro + 1 níquel = 3 lingotes |
| `pewter` | 3 | `pewter` | ✅ | Aleación: 3 estaño + 1 plomo = 4 lingotes |
| `necronium` | 3 | 3 | — | ❌ |
| `constantan` | 3 | `constantan` | ✅ | Aleación: 1 cobre + 1 níquel = 2 lingotes |
| `steel` | 3 | 3 | `steel` / `bronze` / `electrum` | ✅ |
| `bronze` | 3 | 3 | `steel` / `bronze` / `electrum` | ✅ |
| `electrum` | 3 | 3 | `steel` / `bronze` / `electrum` | ✅ |
| `platedSlimewood` | 3 | 3 | — | ❌ |
| `steeleaf` | 3 | `steeleaf` | ✅ | oredict `ingotSteeleaf` (Twilight Forest) |
| `cinderslime` | 4 | — | N/A | |
| `queensSlime` | 4 | `queensslime` | ✅ | |
| `hepatizon` | 4 | `hepatizon` | ✅ | |
| `manyullyn` | 4 | `manyullyn` | ✅ | |
| `ancient` | 4 | `ancient` | ✅ | Limb |
| `jeweledHide` | 4 | `jeweledhide` | ✅ | Compuesto cuero + diamante fundido; Extra(250) + cuerda |
| `ancientHide` | 4 | 4 | — | N/A |
| `knightmetal` | 4 | — | N/A | |
| `knightslime` | 4 | `knightslime` | ✅ | |
| `fiery` | 4 | `fiery` | ✅ | oredict `ingotFiery` (Twilight Forest) |
| `nicrosil` | 4 | `nicrosil` | ✅ | Aleación: 2 níquel + esmeralda + cuarzo = 4 lingotes |

## A.3 — Ammo (puntas, astas, plumas)

| Material TC3 | Tier | Uso en TC3 | Material fork | Estado | Notas |
|--------------|------|------------|---------------|--------|-------|
| `flint` | 1 | Punta | `flint` | ✅ | |
| `wool` | 1 | Punta | `wool` | ✅ | |
| `glass` | 2 | Punta | `glass` | ✅ | |
| `amethyst` | 2 | Punta | `amethyst` | ✅ | oredict `gemAmethyst` |
| `prismarine` | 2 | Punta | `prismarine` | ✅ | |
| `earthslime` | 2 | Punta | `slime` | 🟡 | |
| `skyslime` | 2 | Punta | `blueslime` | 🟡 | |
| `enderPearl` | 2 | Punta | `enderpearl` | ✅ | |
| `gunpowder` | 2 | Punta | `gunpowder` | ✅ | |
| `redstone` | 2 | Punta | `redstone` | ✅ | |
| `steeleaf` | 3 | Punta | `steeleaf` | ✅ | oredict `ingotSteeleaf` (Twilight Forest) |
| `ice` | 3 | Punta | `ice` | ✅ | |
| `quartz` | 3 | Punta | `quartz` | ✅ | |
| `ichor` | 3 | Punta | — | N/A | Slime de TC3 |
| `glowstone` | 3 | Punta | `glowstone` | ✅ | |
| `magnetite` | 3 | Punta | — | N/A | `STEEL_SHARD` de TC3 |
| `enderslime` | 4 | Punta | `enderslime` | ✅ | |
| `dragonScale` | 4 | Punta | `dragonscale` | ✅ | Drop del dragón del End (6-12) + Ice and Fire |
| `shulker` | 4 | Punta | `shulker` | ✅ | `Items.SHULKER_SHELL` |
| `knightly` | 4 | Punta | — | N/A | `KNIGHTMETAL_SHARD` |
| `wood` | 0 | Asta | `wood` | ✅ | |
| `bone` | 1 | Asta | `bone` | ✅ | |
| `bamboo` | 1 | Asta | `reed` | 🟡 | |
| `chorus` | 1 | Asta | `chorus` | ✅ | |
| `cactus` | 1 | Asta | `cactus` | ✅ | |
| `slimewood` | 2 | Asta | — | N/A | |
| `necroticBone` | 2 | Asta | `necroticbone` | ✅ | |
| `blaze` | 2 | Asta | `blaze` | ✅ | |
| `venombone` | 2 | Asta | `bloodbone` | 🟡 | |
| `nahuatl` | 3 | Asta | `nahuatl` | ✅ | Compuesto madera + obsidiana fundida; Head(350, 4.5, DIAMOND, 3) |
| `necronium` | 3 | Asta | — | ❌ | Compat |
| `blazewood` | 4 | Asta | `blazewood` | ✅ | Compuesto madera ardiente + sangre de blaze (sustituye al bloodshroom); asta |
| `blazingBone` | 4 | Asta | `blazingbone` | ✅ | |
| `endRod` | 4 | Asta | `endrod` | ✅ | |
| `feather` | 0 | Pluma | `feather` | ✅ | |
| `leaves` | 1 | Pluma | `leaf` | 🟡 | |
| `paper` | 1 | Pluma | `paper` | ✅ | |
| `slimeball` | 2 | Pluma | `slime` | 🟡 | |
| `magma` | 3 | Pluma | `magmaslime` | 🟡 | |

## A.4 — Armor, escudos y slimesuit — fuera de alcance

Todo depende de sistemas que el fork no tiene, así que **no se portan**:

- **Plating** (31 materiales: copper, iron, gold, searedStone, scorchedStone, osmium,
  aluminum, silver, lead, slimesteel, amethystBronze, obsidian, roseGold, pigIron, cobalt,
  steel, bronze, constantan, invar, pewter, electrum, steeleaf, cinderslime, queensSlime,
  hepatizon, manyullyn, ancient, knightmetal, knightslime, fiery, nicrosil).
- **Maille / cuirass**: leather, vine, slimeskin, skyslimeVine, weepingVine, twistingVine,
  turtle, ironwood, ichorskin, jeweledHide, ancientHide, enderslimeVine, dragonScale, shulker.
- **Shield core**: wood, bamboo, chorus, ice, cactus, bone, slimewood, venombone,
  necroticBone, treatedWood, ironwood, nahuatl, necronium, blazewood, blazingBone.
- **Slimesuit**: slimes (`earthslime`, `skyslime`, `ichor`, `enderslime`, `magma`, `blood`,
  `clay`, `honey`, `enderPearl`), skulls (16), ribcage (`horn`, `jadeite`, …), shells
  (`turtle`, `nautilus`, `phantom`, `prismarine`, `shulker`, `dragonScale`, `magnetite`,
  `kobold`, `knightly`) y laces (`string`, `leather`, `vine`, `skyslimeVine`, `cheese`,
  `darkthread`, `twistingVine`, `weepingVine`, `jeweledHide`, `enderslimeVine`).

Si algún día se porta la armadura ([GUIDE_ARMOR.md](GUIDE_ARMOR.md)), esta es la lista de
stats que habría que añadir.