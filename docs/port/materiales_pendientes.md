# Materiales que NO se pueden implementar (y por qué)

Informe de los **20 materiales que siguen sin implementarse** tras los 11 lotes
(16 N/A + 4 compat ❌), con el motivo exacto y, cuando existe, la condición que los
desbloquearía. Complementa [`materiales_tier.md`](materiales_tier.md) (estado por tier) y
[`GUIDE_MATERIALS.md`](GUIDE_MATERIALS.md) (cómo se implementó cada lote).

Estado global tras el lote 11: **80 materiales TC3 → 54 ✅ / 10 🟡 / 0 ❌ / 16 N/A**,
más 16 de compat (12 ✅ / 4 ❌). No queda **ningún ❌** en las tablas por tier.

Los 20 restantes se agrupan por **motivo real**, no por tier:

| Motivo | Materiales | Total |
|--------|------------|-------|
| **1. Uso exclusivo de armadura/slimesuit** | `horn`, `turtle`, `nautilus`, `honey`, `phantom`, `jadeite`, `ichorskin` | 7 |
| **2. Contenido propio de TC3** (slimes, setas, esquirlas, drops) | `slimewood`, `ichor`, `cinderslime`, `blazewood`*, `ancientHide`, `dragonScale`*, `magnetite`, `kobold`, `knightly`, `knightmetal` | 8* |
| **3. Mods ausentes del pack** | `osmium`, `necronium`, `treatedWood`, `platedSlimewood` | 4 |
| **4. Sets de bloques nuevos** (viabilidad, no imposibilidad) | `slimewood` (4 maderas), `knightmetal` (+ `knightly`) | 2* |

\* `blazewood` y `dragonScale` se han implementado con sustituciones documentadas (ver §2);
`slimewood`/`knightmetal` aparecen en dos grupos porque son posibles pero caros.

---

## 1. Uso exclusivo de armadura o slimesuit

El fork **no tiene armadura, escudos ni slimesuit**. Estos materiales no tienen ni una sola
pieza que el fork pueda fabricar: implementarlos daría un material que no aparece en
ninguna receta.

| Material | Tier | Uso en TC3 (`MaterialStatsDataProvider`) | Item disponible en el pack | Se desbloquea con |
|---|---|---|---|---|
| `horn` | 1 | Solo `RepairStats.ribcage(275)` | ✗ (cuerno de cabra, 1.19+) | Armadura **+** ítem |
| `turtle` | 2 | `MAILLE` + `RepairStats.shell(275)` | ✓ `oe:turtle_scute` (OceanicExpanse) | Armadura |
| `nautilus` | 2 | `RepairStats.shell(350)` | ✓ (OceanicExpanse / Future MC / Fossils) | Armadura |
| `honey` | 2 | `SlimeStats(200, 0)` | ✓ `futuremc:honey_bottle` | Slimesuit |
| `phantom` | 2 | `RepairStats.shell(432)` + `REPAIR_KIT` | ✗ (membrana, 1.13+) | Armadura/elytra **+** ítem |
| `jadeite` | 3 | `RepairStats.ribcage(616)` | Compuesto blaze + `emerald` (el fork tiene el fluido) | Armadura |
| `ichorskin` | 3 | `MAILLE`/`CUIRASS`/`BOWSTRING`/`REPAIR_KIT` | Compuesto cuero + **icor** | Icor + armadura |

> Matiz importante: que el **ítem exista** (tortuga, náutilo, miel) no basta. En TC3 su
> única utilidad son stats de armadura/slimesuit; sin portar esos sistemas no hay pieza que
> construir con ellos.

---

## 2. Contenido propio de TC3

Materiales que dependen de **mob drops, slimes, setas o esquirlas inventadas por TC3**, que
no existen en vanilla 1.12 ni en ningún mod del pack.

| Material | Tier | Qué necesita TC3 | Sustitución aplicada | Recomendación |
|---|---|---|---|---|
| `slimewood` | 2 | 4 maderas de slime propias: `greenheart`, `skyroot`, `bloodshroom`, `enderbark` | — | Ver §4 |
| `ichor` | 3 | `ichorGeode`, la geoda del slime de icor | — | Requeriría un **nuevo tipo de slime** (entidad, geoda, isla). Proyecto aparte; bloquea además `ichorskin` y `cinderslime` |
| `cinderslime` | 4 | Aleación oro + **icor** + piedra scorched | — | Se desbloquea si algún día existe icor |
| `blazewood` | 4 | Compuesto **bloodshroom** + sangre de blaze | ✅ `firewood` + `blazingBlood` → `matBlazewood` | Hecho. Sustitución documentada en `GUIDE_MATERIALS.md` §14.3 |
| `dragonScale` | 4 | `TinkerModifiers.dragonScale`, drop del dragón de TC3 | ✅ oredict `dragonScale` de **Ice and Fire** | Hecho (punta de flecha) |
| `ancientHide` | 4 | Ítem propio que se funde en `moltenDebris`; usos `BINDING`+`MELEE_HARVEST` | — | Solo con armadura; el ítem habría que inventarlo (sin referencia de obtención en TC3) |
| `magnetite` | 3 | `STEEL_SHARD` | — | Inventar "esquirlas de acero" como ítem nuevo o descartar |
| `kobold` | 3 | `COBALT_SHARD` | — | Ídem |
| `knightly` | 4 | `KNIGHTMETAL_SHARD` | — | Depende de `knightmetal` (§4) |

---

## 3. Mods ausentes del pack

TC3 declara estos materiales como **integración opcional con otros mods**; el gate es la
existencia de un oredict concreto. Se comprobó escaneando los 90 jars instalados:

| Material | Tier | Oredict que busca TC3 | Encontrado | Recomendación |
|---|---|---|---|---|
| `osmium` | 2 | `ingotOsmium` | ✗ en ningún jar | Nada que hacer salvo añadir un mod de osmio |
| `necronium` | 3 | `ingots/uranium` | ✗ (`ingotUranium`/`oreUranium` ausentes) | Ídem |
| `treatedWood` | 2 | tag `treated_wood` **o** fluido *creosote* | ✗ (Immersive Engineering / Railcraft no están) | Ídem |
| `platedSlimewood` | 3 | aleación con **zinc** | ✗ (`ingotZinc` ausente) | El fork tiene el fluido `zinc`, pero sin fuente |

> Nota: `ironwood`, `steeleaf`, `fiery`, `constantan`, `invar`, `pewter` y `nicrosil`
> también eran "compat", pero **sí** se han implementado: los tres primeros porque el pack
> trae los lingotes, los cuatro últimos como aleaciones con fluidos que el fork ya tenía.

---

## 4. Posibles pero caros (sets de bloques nuevos)

No son imposibles: son **proyectos**, no lotes. Se documentan para que quede claro que la
decisión fue de coste, no técnica.

### `slimewood` (tier 2) — 4 materiales en TC3
- **Qué haría falta**: un set `log` + `planks` por cada `BlockSlimeGrass.FoliageType`
  (GREEN, BLUE, PURPLE, ORANGE), con `logWood`/`plankWood` en el oredict, texturas
  propias, recetas de crafteo y **4 materiales** (`greenheart`, `skyroot`, `bloodshroom`,
  `enderbark`), cada uno con `Head(375, 4, IRON, 1)`, `Limb(375, 0, -0.05, 0.1)`,
  `Grip(0.4, -0.2, 1)` y `ARROW_SHAFT`.
- **Por qué no**: el fork no tiene madera de slime: los árboles de slime
  (`SlimeIslandGenerator`) usan **slime congelado** como tronco, y solo existen hojas,
  enredaderas y plantones por tipo.
- **Coste**: 8 bloques + 8+ texturas + recetas + 4 materiales → proyecto aparte.

### `knightmetal` (tier 4) — y con él `knightly`
- **Qué haría falta**: ítems `ingot`/`nugget`/`block` (texturas re-coloreando las de
  `knightslime`), fluido `knightmetal`, `Head(512, 8, NETHERITE→COBALT, 3)`,
  `Limb(512, 0, 0.15, -0.1)`, `Grip(0, 0.1, 3)`, `SkullStats(220)` (N/A) y
  `integrate(material, fluid, "Knightmetal").toolforge()`. `knightly` saldría después desde
  `KNIGHTMETAL_SHARD`.
- **Por qué no**: es el único metal de TC3 que el fork nunca tuvo en ninguna forma, y exige
  el pack completo de ítems para que `toolforge()` tenga sentido.
- **Coste**: medio-alto (similar a lo ya hecho con `blazingbone`, pero con 3 ítems).

---

## Resumen: qué haría falta para llegar al 100 %

| Escenario | Materiales que se desbloquean | Restantes |
|---|---|---|
| Portar **armadura + escudos** (`GUIDE_ARMOR.md`) | — | 0 |
| Portar **slimesuit** | — | 0 |
| Añadir **slime de icor** propio | — | 0 |
| Crear **madera de slime** (4 sets) | — | 0 |
| Crear **knightmetal** con set propio TC3 | — | 0 |
| Añadir mods de osmio/uranio/zinc/madera tratada | — | 0 |

> **Actualización (implementación completa 2026-09-20):** se implementaron **todos los
> materiales que dependen de ítems/sistemas disponibles**:
> - `knightmetal` (Twilight Forest `ingotKnightmetal`), `honey` (Future MC `honey_bottle`),
>   `kobold`/`magnetite`/`knightly` (shards de cobalto/acero/knightmetal del fork)
> - Compuestos con items propios del fork: `ichor`, `ichorskin`, `jadeite`, `ancienthide`,
>   `osmium`, `treatedwood`, `platedslimewood`, `slimewood`
> - `turtle`/`nautilus` (items de OceanicExpanse/Future MC, null-safe)
>
> **Únicos no implementados (N/A definitivos, dependen de contenido que no existe en
> 1.12.2 ni en el pack):** `horn` (cuerno de cabra 1.19+), `phantom` (membrana 1.13+).
> Estos dos no tienen ningún ítem fuente en 1.12.2.
