# Guía de Metales e Integración — Núcleo implementado

Integración **condicional** (vía oredict) de tres metales que llegan de mods
externos, usados **solo** como insumos de modificaciones, no como outils propios
por defecto. Implementado como recetas de modificación (ver §4); **no** se crean
`Material`/fluidos especulativos (fuera de alcance, §6).

## 1. Origen y tags

| Metal    | Mod origen    | Tag oredict                                      | Uso |
|----------|---------------|--------------------------------------------------|-----|
| Netherite| Future MC     | `ingotNetherite`                                 | Modificaciones (p. ej. protección/fortaleza) |
| Cobre    | Deeper Depths | `ingotCopper`                                    | Modificación de conductividad telar/redstone |
| Amatista | Deeper Depths | `gemAmethyst` / `shardAmethyst`                  | Modificaciones de experiencia/encantamiento |

- **No** se genera worldgen propio para estos metales (el `OverworldOreGenerator`
  ya genera cobre/estaño/aluminio en Overworld; amatista y netherite vienen de sus
  mods).

## 2. Mecanismo base existente

`MaterialIntegration` (`src/main/java/slimeknights/tconstruct/library/MaterialIntegration.java`)
permite integración condicional mediante `oreRequirement`:

```java
MaterialIntegration integration = new MaterialIntegration(material, fluid, oreDrops)
    .setOreRequirement("ingotNetherite");
```

y solo se aplica si el tag oredict existe (mod presente). El patrón se usa en
`TinkerIntegration.java` (líneas 36+).

## 3. Registro de materiales/fluidos (NO aplicado)

> **Implementado como recetas de modificación, no como materiales.** El patrón
> `MaterialIntegration` (§2) existe y puede usarse para integrar netherite como
> metal fundible más adelante, pero está **fuera de alcance** (§6). No se crean
> `Material`/`Fluid` especulativos en este núcleo.

## 4. Uso en modificaciones (implementado)

Implementado en `TinkerModifiers.postInit` → `registerAntiqueModifierRecipes()`
(`src/main/java/slimeknights/tconstruct/tools/TinkerModifiers.java`). Se registra en
post-init porque `RecipeMatch.Oredict` captura las entradas oredict **en el momento
de construirse**; en post-init los mods externos ya poblaron sus oredicts.

| Metal    | Tag oredict            | Modificador existente | Efecto |
|----------|------------------------|-----------------------|--------|
| Netherite| `ingotNetherite`       | `modReinforced`       | Reforzado (+1 nivel, como reforzamiento) |
| Cobre    | `ingotCopper`          | `modHaste`            | Rapidez (conductividad, como redstone) |
| Amatista | `gemAmethyst` (1) / `shardAmethyst` (9) | `modLuck` | Fortuna (experiencia/encantamiento) |

- Cada entrada se añade solo si `OreDictionary.doesOreNameExist(tag)` es cierto en
  runtime (Future MC / Deeper Depths cargados). Si el mod no está, **no** se añade
  match y no hay crash.
- Cobre: el fork ya tiene cobre propio (material + fluido + `ingotCopper`), así que
  esa receta siempre está disponible.

Ejemplo real (equivalente a la intención del plan):

```java
if(OreDictionary.doesOreNameExist("ingotNetherite")) {
  modReinforced.addItem("ingotNetherite", 1, 1);
}
```

## 5. Metales tier 1 — Estaño y Aluminio como materiales (implementado)

Antes de este cambio, `tin` y `aluminum` existían en el fork **solo como fluidos**
(componentes de aleación bronce/alubrass/brass; la aleación ya funcionaba vía
`TinkerSmeltery.registerAlloys`). Ahora son **materiales de herramienta completos**:

| Metal | Fluido | Mena (overworld) | Integración | Stats |
|-------|--------|------------------|-------------|-------|
| Estaño (`tin`) | `TinkerFluids.tin` (0xc1cddc) | `oreTin` | `TinkerIntegration.java` (pasó de fluido a material+fluido) | Head(150, 4.5, 2.5, STONE), Handle(0.80, 15), Extra(50) |
| Aluminio (`aluminum`) | `TinkerFluids.aluminum` (0xefe0d5) | `oreAluminum` | ídem | Head(180, 5.9, 2.9, IRON), Handle(0.95, 45), Extra(80) |

- Stats deliberadamente **por debajo del cobre** (Head 210/IRON): triunvirato tier 1
  cobre > aluminio > estaño (todas fundibles en la smeltery/melter con sus fluidos ya
  registrados).
- Registro: `TinkerMaterials.tin/aluminum` (`mat(...)`, `addCommonItems("Tin"/"Aluminum")`,
  `TinkerRegistry.addMaterialStats`) + render `resources/assets/tconstruct/materials/{tin,aluminum}.json`
  (`type: metal`, colores iguales a los fluidos) + keys lang (`material.tin.*`,
  `material.aluminum.*`) en `en_us` y `es_es`.
- Integración **condicional al oredict** `ingotTin`/`ingotAluminum` igual que el resto
  (`MaterialIntegration(Material, Fluid, oreSuffix)`), y gateada por
  `Config.registerAllCommonMetals` (default `true`). Con el config apagado, se
  comportan como antes (fluidos sin material visible).
- Fundido/vaciado de partes: automático vía
  `TinkerSmeltery.registerToolpartMeltingCasting` + `registerOredictMeltingCasting`
  (invocados por `MaterialIntegration.integrate()`).
- No llevan traits nuevos (a diferencia de cobre `established` / bronce `dense`).

## 6. Si se quiere también fundibles (opcional, fuera de alcance)

Si más adelante se quiere fundir/vaciar estos metales en la smeltery o foundry:

```java
TinkerRegistry.registerMelting(new MeltingRecipe(new RecipeMatch.Item(...), fluid, temperature));
TinkerRegistry.registerTableCasting(new CastingRecipe(...));
```

Esto NO es necesario para el alcance actual ("solo las modificaciones que lo
requieren") ni para los metales tier 1 (§5, ya fundibles).

## 7. Materiales sobre fluidos ya existentes (implementado)

Lote de materiales de TC3 que el fork marcaba como ❌ en
[`materiales_tier.md`](materiales_tier.md) **aunque su fluido ya existía**: solo faltaba
el `Material`, sus stats, su JSON de render y sus claves de idioma.

| Material (fork) | Material TC3 | Tier TC3 | Fluido del fork | Insumos de crafteo |
|-----------------|--------------|----------|-----------------|--------------------|
| `gold`          | `gold`       | 2 | `TinkerFluids.gold` | `ingotGold` / `nuggetGold` / `blockGold` (`addCommonItems("Gold")`) |
| `searedstone`   | `seared_stone` | 2 | `TinkerFluids.searedStone` | `ingotBrickSeared` (72), `blockSeared` (288) |
| `glass`         | `glass`      | 2 | `TinkerFluids.glass` | `blockGlass` (1000), `paneGlass` (375) |
| `clay`          | `clay`       | 2 | `TinkerFluids.clay` | `clay` (144), `blockClay` (576) |
| `enderslime`    | `enderslime` | 4 | `TinkerFluids.purpleSlime` | `slimeballPurple` (250) |
| `blood`         | `blood`      | 5 | `TinkerFluids.blood` | `slimeballBlood` (250) |

Los valores de crafteo son los mismos que ya usaba `TinkerSmeltery` para fundir cada
objeto, así que no hay duplicación de valor entre crafteo y fundición.

Con `blood` (el único material del tier 5 de TC3) este lote deja el tier 5 **completo
(1/1)**; su ficha está en [`materiales_tier.md`](materiales_tier.md#tier-5).

### Stats y traits

| Material | Head (durabilidad, velocidad, ataque, nivel) | Handle | Extra | Bow | Traits |
|----------|----------------------------------------------|--------|-------|-----|--------|
| `gold` | 120, 12.00, 2.00, `STONE` | 0.70, -50 | 250 | 1.3 / 0.9 / 2 | `established` |
| `searedstone` | 550, 6.50, 4.00, `IRON` | 0.80, 40 | 60 | 0.8 / 1.1 / 1 | `aridiculous` (HEAD), `hellish` |
| `glass` | 65, 6.50, 5.00, `IRON` | 0.70, -120 | 30 | *whyWouldYouMakeABowOutOfThis* | `jagged` (HEAD) |
| `clay` | 150, 4.00, 2.00, `STONE` | 1.00, 0 | 80 | *whyWouldYouMakeABowOutOfThis* | `cheap` |
| `enderslime` | 750, 5.20, 4.50, `OBSIDIAN` | 0.80, 150 | 300 | 0.9 / 1.15 / 3 | `enderference`, `lightweight` |
| `blood` | 500, 6.50, 6.50, `COBALT` | 0.85, 50 | 400 | 0.7 / 1.3 / 5 | `splintering` (HEAD), `raging` |

Criterio:

- `gold` respeta el oro de vanilla: rapidísimo (12), poco duradero y **nivel de picota
  `STONE`** (`HarvestLevels.STONE = 0`), por debajo del resto de tier 2.
- `glass` es "cañón de cristal": durabilidad mínima, ataque alto, `jagged` (más daño
  según se desgasta) y encordado/binding pésimos.
- `blood` es el único tier 5 del fork: nivel `COBALT` como manyullyn, pero menos
  durabilidad y `Extra` altísimo (encantabilidad).
- `enderslime` usa el renombre de TC3 (purple → ender) y es la alternativa "ender" a
  knightslime, con menos ataque y más durabilidad.

### Detalle de implementación — por qué la integración va sin fluido

Los fluidos de la smeltery (`searedStone`, `glass`, `clay`, `blood`, `purpleSlime`) los
crea `TinkerFluids` **dentro del evento de registro de bloques**
(`TinkerFluids.registerBlocks`), que en Forge 1.12.2 ocurre **después** de
`FMLPreInitializationEvent`. `TinkerIntegration.preInit` corre en preinit, así que en ese
momento `TinkerFluids.glass` y compañía todavía son `null`.

Por eso se usa el mismo patrón que ya tenía `obsidian`:

1. `TinkerIntegration.preInit` crea la integración **sin fluido** (`integrate(material)`),
   lo que registra el material y lo marca como crafteable.
2. `TinkerMaterials.setupMaterials` (init, ya con los fluidos creados) enlaza el fluido:
   `material.setFluid(...)` + `setCraftable(true)` + `setCastable(true)`.
3. `MaterialIntegration.integrate()` (postInit) ve el fluido ya asignado en el material y
   registra fundido/vaciado de piezas (`registerToolpartMeltingCasting`).

Consecuencias:

- Estos cinco materiales **no** pasan por `registerOredictMeltingCasting` (no tienen
  sufijo de mena); su fundido/vaciado de materia prima es el que ya tenía
  `TinkerSmeltery` (arena → `glass`, barro → `clay`, slimeballs → `purpleSlime`/`blood`,
  piedra → `searedStone`).
- `gold` sí usa la vía completa porque su fluido se crea en `TinkerFluids.setupFluids()`
  (bloque estático), disponible ya en preinit:
  `integrate(TinkerMaterials.gold, TinkerFluids.gold, "Gold").toolforge()`.
  Sustituye a la integración anterior sólo-fluido (`integrate(TinkerFluids.gold, "Gold")`),
  que además ya aportaba oredict y toolforge.
- Los cinco materiales ligados a la smeltery se integran bajo `if(isSmelteryLoaded())`:
  sin el pulse de la smeltery no existen sus fluidos, así que el material queda oculto.

### Assets

- Render: `resources/assets/tconstruct/materials/{gold,searedstone,glass,clay,enderslime,blood}.json`
  (`gold` con `type: metal` + `suffix: metal_base` como el resto de metales; los otros
  cinco con `type: colored` y el color del fluido, igual que `stone`).
- Idioma: `material.<id>.name` (+ `material.<id>.prefix` en `es_es`) en `en_us.lang` y
  `es_es.lang`. **Ojo con la codificación**: los `.lang` están en **UTF-8 sin BOM**
  (los acentos son de 2 bytes, p. ej. `Estaño` = `C3 B1`). Escribirlos como Latin-1 deja
  bytes sueltos (`F1`) que rompen el parseo, y releerlos como Latin-1 y volver a
  guardarlos como UTF-8 los doble-codifica (`C3 83 C2 B1`). Al tocarlos por script:
  leerlos y escribirlos **siempre en UTF-8 sin BOM** y verificar después con un decode
  estricto.

### Fuera de este lote

- `scorchedStone` (tier 2 TC3): el fork tiene los bloques scorched pero **ningún fluido**
  de piedra scorched. Requiere crear el fluido + bloque fundido + recetas antes de poder
  añadir el material.
- `emerald` / `diamond`: tienen fluido, pero **no son materiales en TC3** (siguen siendo
  solo modificadores/gemas). Se dejan como estaban para no inventar contenido.

## 8. Lote tier 1 (TC3): `chorus`, `wool`, `bamboo`→`reed` y renders faltantes

Cierra los huecos del tier 1 de TC3 en [`materiales_tier.md`](materiales_tier.md#tier-1).
Ninguno necesita fluido: son materiales **crafteables** (`MaterialIntegration(material)`
sin fluido), así que se integran siempre, sin depender del pulse de la smeltery.

| Material | Insumo | Stats | Rasgos |
|----------|--------|-------|--------|
| `chorus` | `Items.CHORUS_FRUIT_POPPED` ×1 = 144 | Head(180, 3.00, 1.00, `STONE`), Handle(1.10, 20), Extra(60), Bow(1.0, 0.9, 1) | `enderference` |
| `wool` | `Blocks.WOOL` (wildcard de color) ×1 = 144 | Head(15, 1.00, 0.00, `STONE`), Extra(10) — sin handle | — |
| `reed` | `Items.REEDS` (ya existía) | ArrowShaft(1.5, 20) + **Bow(1.1, 0.95, 0)** nuevo | `breakable` |

Criterio (valores de TC3 cuando existen):

- `chorus` copia las stats de TC3: `HeadMaterialStats(180, 3.0f, 1.0f, STONE)` y el rasgo
  por defecto `enderference` (`MaterialTraitsDataProvider` línea 50). TC3 lo alimenta con
  **chorus fruit asada** (`Items.POPPED_CHORUS_FRUIT`, 1 item = 1 unidad); en 1.12 el
  item se llama `Items.CHORUS_FRUIT_POPPED` (mappings `snapshot_20170801`).
- `wool` en TC3 **solo** tiene `StatlessMaterialStats.ARROW_HEAD` (ni cabeza ni mango) y
  el rasgo `soft`, que pone el daño de proyectil a 0. El fork no tiene ese rasgo, así que
  se porta como cabeza de flecha débil (ataque 0.00, durabilidad 15) + `Extra` para poder
  usarlo de binding (`ORDER_BINDING`), y **sin rasgo**. TC3 además tiene 16 variantes por
  color; el fork usa el ítem con wildcard, una sola variante.
- `bamboo` (1.14+) no existe en 1.12. En TC3 no tiene stats de cabeza/mango: es
  **limb (arco) + arrow shaft**. Como el fork ya tenía `reed` (caña) como shaft, se le
  añaden stats de arco `BowMaterialStats(1.1f, 0.95f, 0f)` (TC3: `LimbMaterialStats(70,
  +0.1 draw, -0.05 velocity, -0.05 accuracy)`) en vez de crear un material duplicado.

### Cierre del tier 1 (usos de ammo)

Al comparar con `addAmmo()` de TC3 faltaban tres usos: **asta** para `cactus` y `chorus`,
y **pluma** para `paper`. En TC3 el asta/pluma son `StatlessMaterialStats` (sin números),
así que los valores son de balance propio, alineados con los ya existentes
(`wood` 1.0/0, `bone` 0.9/5, `blaze` 0.8/3, `reed` 1.5/20, `endrod` 0.7/1):

| Material | Stats añadidas | Referencia TC3 |
|----------|----------------|----------------|
| `cactus` | `ArrowShaftMaterialStats(0.85f, 2)` | `ARROW_SHAFT` (rasgo ammo `spiny`) |
| `chorus` | `ArrowShaftMaterialStats(0.9f, 3)` | `ARROW_SHAFT` (rasgo ammo `enderference`) |
| `paper`  | `FletchingMaterialStats(0.9f, 0.8f)` | `FLETCHING` (rasgo ammo `weak`) |

`horn` se queda fuera del port: TC3 lo fabrica con cuerno de cabra (`Items.GOAT_HORN`,
1.19+) y su única utilidad es `RepairStats.ribcage(275)` (armadura). No hay item
equivalente en 1.12.2 ni sistema de armadura en el fork → se marca **N/A**, no ❌.

### Renders que faltaban

`cactus`, `paper` y `reed` no tenían JSON, así que caían en el `MaterialRenderInfo.Default`
con un `log.warn`. Se añaden `resources/assets/tconstruct/materials/{cactus,paper,reed}.json`
(`type: colored`, con el color declarado en `mat(...)`), más `chorus.json` y `wool.json`.

> Ojo: `MaterialRenderInfoLoader` emite un warning por cada material sin JSON. Si se añade
> un material nuevo, hay que añadir su JSON o el log se llena.

## 9. Lote "ammo + bindings": puntas de flecha, bindings y cuerdas

Cierra otro bloque de ❌ de [`materiales_tier.md`](materiales_tier.md) usando **solo items
que ya existen en 1.12.2**: no hace falta ningún fluido ni aleación nueva. En TC3 todos
estos materiales son *arrow heads* (`StatlessMaterialStats.ARROW_HEAD`), *bindings*
(`BINDING`) o *bowstrings* (`BOWSTRING`), es decir **no tienen stats de cabeza ni de mango**.

| Material | Tier | Insumo (1 = 144) | Stats en el fork | Rasgo (TC3 → fork) |
|----------|------|------------------|------------------|--------------------|
| `leather` | 0 | `Items.LEATHER` | Extra(50) + `BowStringMaterialStats(1f)` | `tanned` → ninguno |
| `necroticbone` | 2 | `matNecroticBone` (oredict `boneWithered`) | Head(125, 4.00, 2.25, `IRON`), Handle(0.70, 0), Extra(60), Bow(0.9/1.05/2) | `necrotic` (robavida) → `poisonous` |
| `enderpearl` | 2 | `Items.ENDER_PEARL` | Head(60, 2.50, 2.00, `STONE`) | `enderporting` → `enderference` |
| `gunpowder` | 2 | `Items.GUNPOWDER` | Head(30, 2.00, 3.00, `STONE`) | `explosive` → `flammable` |
| `redstone` | 2 | `Items.REDSTONE` | Head(35, 3.00, 1.50, `STONE`) | `supercharged` → `shocking` |
| `amethyst` | 2 | oredict `gemAmethyst` | Head(70, 4.50, 3.50, `IRON`) | `crystalbound` → `jagged` |
| `quartz` | 3 | `Items.QUARTZ` | Head(45, 4.00, 2.50, `STONE`) | `keen` → `fractured` |
| `glowstone` | 3 | `Items.GLOWSTONE_DUST` | Head(40, 3.50, 2.00, `STONE`) | `spectral` → ninguno |
| `shulker` | 4 | `Items.SHULKER_SHELL` | Head(90, 4.50, 3.00, `IRON`) | `reclaim` → `hovering` |

Decisiones que conviene recordar:

- **El fork no restringe por pieza.** Basta `HeadMaterialStats` para que el material salga
  en el fabricante de partes para *todas* las cabezas, incluida la punta de flecha
  (`TinkerRegistry.addMaterialStats` le añade `ProjectileMaterialStats` solo
  automáticamente, línea 267). Por eso las puntas de flecha llevan `Head` con valores
  **bajos** (30–90 de durabilidad, nivel `STONE` salvo amatista/shulker) y sin mango: son
  materiales de flecha, no de pico.
- `leather` es el único fiel al 100 %: en TC3 tampoco tiene cabeza ni mango, solo binding y
  bowstring (`Extra` + `BowStringMaterialStats`).
- `necroticbone` sí tiene cabeza/mango/limb en TC3 (de ahí sus 4 stats); su rasgo `necrotic`
  es robavida y en el fork **solo existe como modificador** (`TinkerModifiers.modNecrotic`,
  declarado `Modifier`, no `ITrait`), así que se usa `poisonous`. Si se quiere el robavida
  exacto habría que añadir un `AbstractTrait` nuevo.
- `amethyst` es el único gateado por oredict: `integrate(TinkerMaterials.amethyst,
  "gemAmethyst")`, igual que `slime` con `slimecrystalGreen`. Sin Deeper Depths no aparece.
- `shulker` usa `Items.SHULKER_SHELL`, que **sí** existe en 1.12.2 (1.9+), así que no hace
  falta ningún mod externo.

Fuera de este lote por no existir el item en 1.12: `turtle` y `nautilus` (1.13+),
`phantom` (1.13+), `dragonScale`, `honey` (1.15+), `cheese`, `horn` (1.19+).

## 10. Lote "Nether Backport": `weepingVine` y `twistingVine`

TC3 tiene dos materiales de tier 2 hechos con **vides del Nether** (1.16+), que no existen
en vanilla 1.12. **Unseen's Nether Backport** (`modid: nb`, v0.7, depende de Nether-API)
las añade, así que se portan sin inventar nada:

| Material fork | Material TC3 | Item | Stats | Rasgo (TC3 → fork) |
|---|---|---|---|---|
| `weepingvine` | `weepingVine` | `nb:crimson_vine` (**Weeping Vines**) | Extra(45) + `BowStringMaterialStats(1f)` | `flamestance` → `flammable` |
| `twistingvine` | `twistingVine` | `nb:warped_vine` (**Twisting Vines**) | Extra(45) + `BowStringMaterialStats(1f)` | `entangled` → `freezing` |

En TC3 ambas son **solo `BINDING` + `BOWSTRING`** (ni cabeza ni mango), así que el port es
fiel: `ExtraMaterialStats` + `BowStringMaterialStats`, como `vine`/`leather`.

### Por qué no usamos `MaterialIntegration(material, oreRequirement)`

Es el patrón nuevo de integración condicional del fork (§7 de este documento y el lote de
ametista), pero **el mod no registra oredict para las vides**. Verificado extrayendo las
cadenas del jar: solo aparecen `ingotNetherite` y `blockNetherite` como nombres de
diccionario. Por eso:

1. **Gate en preinit por mod cargado**: `Loader.isModLoaded("nb")` en
   `TinkerIntegration.preInit`. Funciona en cualquier fase, a diferencia de consultar el
   oredict o los ítems (en preinit los ítems de otros mods aún no están registrados).
2. **Resolución del ítem en init**: `TinkerMaterials.getModItem("nb:crimson_vine")` usa
   `Item.getByNameOrId`, que ya devuelve el ítem porque `setupMaterials` corre en
   `FMLInitializationEvent` (posterior al evento de registro de ítems). Si devuelve null
   se avisa por log y el material se queda sin insumos: **el mod sigue arrancando** sin el
   backport instalado.
3. Ninguna dependencia en `build.gradle`: sigue la regla de §11.

> Nombres comprobados en `assets/nb/lang/en_us.lang` del jar:
> `tile.crimson_vine.name=Weeping Vines`, `tile.warped_vine.name=Twisting Vines`.
> Ojo: es al revés de lo que sugiere el prefijo (crimson→weeping, warped→twisting).

### Qué NO desbloquea este mod

`scorchedStone`, `blazewood`, `blazingBone`, `ichor`, `kobold`, `ancientHide` y
`jeweledHide` usan **contenido propio de TC3** (ladrillos scorched, bloodshroom, geodes de
icor…), no bloques del Nether 1.16: siguen ❌. El mod sí trae `nether_scrap`
(`nb:nether_scrap`) e `ingotNetherite`, con los que se podría portar `ancient` (tier 4,
`HeadMaterialStats(745, 7f, NETHERITE, 2.5f)`), pendiente de decisión de balance.

## 11. Lote "tier 2 (Nether/foundry)": `scorchedStone`

TC3 tiene piedra scorched como material de tier 2 (`ORDER_NETHER`), hecho con **sus propios
ladrillos/bloques scorched** y con **fluido propio**. El fork ya tenía los bloques scorched
(la foundry) pero los fundía en *piedra seared*, así que el port necesitaba un fluido nuevo:

| Pieza | Cambio |
|-------|--------|
| `TinkerFluids.scorchedStone` | `fluidStone("scorched_stone", 0x3a2f2c)` a 800 ºC + bloque fundido + cubo (bajo `isSmelteryLoaded()`) |
| `TinkerScorched.registerMeltingCasting` | Los bloques scorched pasan de fundirse en `searedStone` a hacerlo en `scorchedStone` (y se vacían con él) |
| `TinkerMaterials.scorchedstone` | Head(120, 4.50, 2.50, `IRON`), Handle(0.80, 20), Extra(60), rasgo `superheat` |
| Insumos | Ladrillo scorched = 72, bloque scorched = 288 (igual que los valores seared) |

Detalles de integración:

- La temperatura se deja en **800 ºC** (igual que la piedra seared) para no hacer
  inalcanzable el fundido en la smeltery; la foundry usa sangre de blaze (1800 ºC).
- El material se integra con `if(isScorchedLoaded())`, no con `isSmelteryLoaded()`: sus
  **insumos** son los bloques scorched, que viven en el pulse de la foundry
  (`pulsesRequired = TinkerSmeltery;TinkerIntegration`), no en el de la smeltery.
- `TinkerMaterials.setupMaterials` protege con `if(TinkerScorched.blockScorched != null)`
  por si se desactiva el pulse: sin bloques no hay insumo y el material queda vacío.
- `TinkerPulse.isScorchedLoaded()` es el helper nuevo para preguntar por ese pulse.

### Qué queda pendiente del tier 2 y por qué

| Material | Estado | Motivo |
|---|---|---|
| `slimeskin` | ❌ | Compuesto cuero + slime verde; en TC3 su único uso no-armadura es **cuerda de arco**. Portarlo exige un **item nuevo** (`matSlimeSkin`) con textura propia + receta de vaciado. |
| `slimewood` | N/A | TC3 usa sus 4 maderas de slime (greenheart/skyroot/bloodshroom/enderbark). El fork no tiene madera de slime: sus árboles usan slime congelado como tronco (`SlimeIslandGenerator`). |
| `turtle` | N/A | El escudo de tortuga **sí existe** (OceanicExpanse: `oe:turtle_scute`), pero en TC3 solo sirve para armadura (`RepairStats.shell(275)` + maille). |
| `nautilus` | N/A | La concha de náutilo **sí existe** (OceanicExpanse, Future MC, Fossils), pero en TC3 solo es armadura (`RepairStats.shell(350)`). |
| `honey` | **Implementado (2026-09-20)** | La botella de miel existe (Future MC: `futuremc:honey_bottle`) y el fork ya funde el fluido `honey`. En TC3 solo es slimesuit (`SlimeStats(200, 0)`); en el fork se portó como ligante (Extra 60) + BowString pegajosa + trait `tasty`. |
| `cheese` | N/A | Contenido propio de TC3 (`cheeseIngot`): no hay queso en 1.12 ni en el modpack. Su uso no-armadura sería cuerda de arco. |
| `phantom` | N/A | Membrana de phantom (1.13+), ausente; en TC3 solo es armadura/elytra. |

> Los items de tortuga/náutilo/miel se localizaron escaneando los `en_us.lang` de los jars
> instalados. Aunque existan, **no aportan materiales** mientras el fork no tenga
> armadura/slimesuit: solo se usarían si en el futuro se portan esos sistemas.

## 12. Lote "tier 3": aleaciones propias de TC3 (`slimesteel`, `roseGold`, `amethystBronze`)

Las tres aleaciones de tier 3 de TC3 son **contenido propio** (no vienen de oredict de otros
mods), así que el port necesita fluido + receta de aleación + material. Recetas de TC3
(`SmelteryRecipeProvider:1865-1882`, en unidades de TC3 donde 1 lingote = **90 mB**):

| Aleación | Receta TC3 | Receta en el fork (1 lingote = 144 mB) |
|---|---|---|
| `slimesteel` | hierro 90 + skyslime 250 + seared 250 → 180 | hierro 144 + `blueslime` 250 + `searedStone` 144 → **288** |
| `roseGold` | cobre 90 + oro 90 → 180 | cobre 144 + oro 144 → **288** (misma proporción) |
| `amethystBronze` | cobre 90 + amatista 100 → 90 | cobre 144 + amatista 144 → **288** |

Se usa 2→2 en amethyst bronze (TC3 es 2→1) para no penalizarla frente a las otras dos;
documentado a propósito.

| Material | Stats (copiadas de TC3) | Rasgo (TC3 → fork) |
|---|---|---|
| `slimesteel` | Head(1040, 6.00, 2.50, `DIAMOND`), Handle(1.20, 150), Extra(200), Bow(0.85/1.25/4) | `overcast`+`overslime` → `dense` |
| `rosegold` | Head(175, 9.00, 1.00, `STONE`), Handle(0.70, 0), Extra(100), Bow(1.15/0.75/2) + cuerda | `enhanced` → `established` |
| `amethystbronze` | Head(720, 7.00, 1.50, `DIAMOND`), Handle(1.05, 100), Extra(120), Bow(0.7/1.35/3) | `crumbling` → `crumbling` (HEAD) |

Detalles:

- **Oro rosado conserva el nivel de picota del oro** (`STONE`), como en TC3: es rapidísimo
  (9.0) pero no puede picar hierro. Comentario literal de TC3: *"gold mining level
  technically puts it in tier 0"*.
- **Fluido nuevo `amethyst`** (700 ºC) porque la aleación lo necesita y el fork no lo tenía.
  Se funde desde el oredict `gemAmethyst` (Deeper Depths); si el mod no está, la receta no
  coincide con nada y el fluido queda sin fuente, sin romper nada.
- **Los tres fluidos se crean en el bloque estático** de `TinkerFluids.setupFluids()` (no en
  el evento de registro de bloques) para que `MaterialIntegration` pueda enlazarlos ya en
  preinit, igual que `iron`/`gold`/`cobalt`.
- **Sin `oreSuffix` a propósito**: `integrate(material, fluid)` en lugar de
  `integrate(material, fluid, "Slimesteel")`. No hay lingotes/bloques de estas aleaciones en
  el oredict, así que no se registra fundido/vaciado de lingotes ni bloque de toolforge: se
  cuelan las piezas directamente desde la smeltery.

### Pendiente si se quieren lingotes

Para tener `ingotSlimesteel`/`blockSlimesteel` (y por tanto toolforge, vaciado de lingotes y
recetas completas) haría falta:

1. Ítems nuevos en `TinkerCommons.materials` (`addMeta`) con oredict en `TinkerOredict`.
2. Texturas `ingot_*` / `nugget_*` / `block_*`: se pueden derivar **re-coloreando** las del
   acero (técnica ya usada en el repo para seared→scorched).
3. Cambiar la integración a `integrate(material, fluid, "Slimesteel").toolforge()`.

### Qué queda pendiente del tier 3 y por qué

| Material | Estado | Motivo |
|---|---|---|
| `nahuatl` | N/A | Compuesto de TC3: madera + obsidiana fundida. Requiere **item nuevo**; además TC3 lo obtienen por barter de piglins (1.16+). |
| `darkthread` | N/A | Compuesto de TC3: cuerda + obsidiana fundida. Requiere **item nuevo**. |
| `ichorskin` | N/A | Compuesto cuero + **icor**, slime propio de TC3. |
| `ichor` | N/A | `ichorGeode`: geoda del slime de icor de TC3. |
| `magnetite` | N/A | Se fabrica con `STEEL_SHARD`, contenido propio de TC3. |
| `kobold` | N/A | Se fabrica con `COBALT_SHARD`, contenido propio de TC3. |
| `jadeite` | N/A | Compuesto blaze + esmeralda fundida; su único uso es `RepairStats.ribcage` (**armadura**). |

## 13. Lote "tier 4": dos aleaciones, `blazingBone` y `ancient`

Cuatro materiales más del tier 4 de TC3. Recetas de TC3 (`SmelteryRecipeProvider:1906-1924`,
1 lingote = 90 mB) reescaladas a 144 mB:

| Material | Receta TC3 | En el fork |
|---|---|---|
| `queensslime` | cobalto 90 + oro 90 + magma 250 → 180 | cobalto 144 + oro 144 + `magma` 250 → **288** |
| `hepatizon` | cobre 180 + cobalto 90 + cuarzo 100 → 180 | cobre 288 + cobalto 144 + `quartz` 144 → **288** |
| `blazingbone` | compuesto: hueso necrótico + sangre de blaze | **Ya existía**: `TinkerCommons.matBlazingBone` se cuela con `boneWithered` + 250 mB de sangre de blaze (`TinkerSmeltery`) |
| `ancient` | lingote/nugget de **chatarra de netherite** | crafteable con `nb:nether_scrap` (Nether Backport) |

Fluidos nuevos para las aleaciones: `magma` (400 ºC, desde crema de magma = 250 mB) y
`quartz` (700 ºC, desde el oredict `gemQuartz` = 144 mB). Ambos son **items de vanilla
1.12**, así que no dependen de ningún mod.

| Material | Stats (TC3) | Rasgo (TC3 → fork) |
|---|---|---|
| `queensslime` | Head(1650, 6.00, 2.00, `COBALT`), Handle(1.35, 250), Extra(350), Bow(0.9/1.15/5) | `overlord`+`overslime` → `slimeyBlue` |
| `hepatizon` | Head(975, 8.00, 2.50, `COBALT`), Handle(1.10, 120), Extra(150), Bow(1.25/0.9/4) | `momentum` → `momentum` |
| `blazingbone` | Head(530, 6.00, 3.00, `IRON`), Handle(0.85, 60), Extra(90), Bow(1.1/0.7/3) + asta | `conducting` → `shocking` |
| `ancient` | Head(745, 7.00, 2.50, `COBALT`) **sin mango ni binding** (como en TC3), Bow(0.95/1.1/4) | `vintage`/`worldbound` → `duritos` |

Notas:

- El nivel de picota de TC3 es `NETHERITE`; el fork no lo tiene, así que se mapea al más alto
  disponible, `HarvestLevels.COBALT` (4), igual que manyullyn y knightslime.
- `blazingbone` es el más barato del lote: no añade ningún ítem, solo el material sobre el
  hueso ardiente que el fork ya fabricaba (y que hasta ahora solo servía de asta).
- `ancient` usa el mismo patrón que las vides: gate `Loader.isModLoaded("nb")` en
  `TinkerIntegration.preInit` + resolución del ítem por id en `setupMaterials` (init) con
  `log.warn` si no está. Sin el backport, el material no se integra y no aparece.
- `queensslime`/`hepatizon` van con `isSmelteryLoaded()` y sin `oreSuffix`, igual que las
  aleaciones del tier 3: se cuelan las piezas, no hay lingotes.

### Qué queda del tier 4 y por qué

| Material | Estado | Motivo |
|---|---|---|
| `cinderslime` | N/A | Su aleación usa **icor** (slime propio de TC3). |
| `knightmetal` | **Implementado (2026-09-20)** | Integración por oredict `ingotKnightmetal` de **Twilight Forest** (mismo patrón que ironwood/steeleaf/fiery): Head(512, 8, 3, COBALT) + Handle(1.0, 90) + Extra(100) + Armor factor 40 (3/6/8/3, tough 2). Traits `insatiable`+`heavy` (TC3: valiant/stalwart). |
| `blazewood` | N/A | Compuesto **bloodshroom** (item de TC3) + sangre de blaze. |
| `jeweledHide` | N/A | Compuesto cuero + diamante fundido: el fork tiene el fluido, falta el **item**. |
| `ancientHide` | N/A | **Item propio de TC3** que se funde en `moltenDebris`. |
| `dragonScale` | N/A | `TinkerModifiers.dragonScale`, drop del dragón de TC3. |
| `knightly` | N/A | Se hace con `KNIGHTMETAL_SHARD`, contenido propio de TC3. |

## 14. Lote "compat + compuestos": 13 materiales más

Cierra **todo lo que era portable** según el informe de
[materiales pendientes](materiales_pendientes.md). Tres sublotes distintos.

### 14.1 Materiales de mods instalados (solo integración)

Escaneando los `en_us.lang` y las clases de los 90 jars instalados se encontró que el pack
**ya aporta** los ítems de cuatro materiales que figuraban como ❌/N/A:

| Material | Tier | Oredict | Mod | Stats (TC3) |
|---|---|---|---|---|
| `ironwood` | 2 | `ingotIronwood` | Twilight Forest | Head(512, 6.50, 2.00, `IRON`), Bow(0.95/1.10/2) |
| `steeleaf` | 3 | `ingotSteeleaf` | Twilight Forest | Head(200, 8.00, 3.00, `DIAMOND`), Bow(1.0/1.15/3) |
| `fiery` | 4 | `ingotFiery` | Twilight Forest | Head(1024, 8.00, 3.50, `COBALT`), Bow(0.9/1.4/5) |
| `dragonscale` | 4 | `dragonScale` | drop del **dragón del End** (propio) + Ice and Fire | Solo punta: Head(150, 5.50, 4.50, `IRON`) |

- Los tres metales usan `addCommonItems(...)` y **`integrate(material, "<oredict completo>")`**.
  Cuidado con el gate: el fork tiene dos sobrecargas parecidas y solo la de
  *oreRequirement* (`integrate(material, String)`) comprueba el oredict tal cual. Pasar el
  sufijo (`"Ironwood"`) **no funciona**: el oredict que existe es `ingotIronwood`.
- `dragonscale` va con `addItem("dragonScale", …)`, como `amethyst`, porque en TC3 su único
  uso no-armadura es `ARROW_HEAD`.

### 14.1.1 Escamas de dragón: drop del dragón del End

En TC3 las escamas las suelta el **dragón del End**, así que el fork hace lo mismo y deja de
depender de Ice and Fire:

- Ítem propio `TinkerCommons.matDragonScale` (`materials.addMeta(29, "dragon_scale")`) con
  textura `dragon_scale.png` (re-coloreada de `silky_jewel.png`).
- Drop añadido en `ToolEvents.onLootTableLoad` sobre `LootTableList.ENTITIES_ENDER_DRAGON`,
  igual que ya se hacía con el hueso necrótico de los wither skeleton:
  **6-12 escamas por dragón** (`RandomValueRange(6, 12)`), sin condiciones.
- Registradas en el **mismo oredict `dragonScale`** que usa Ice and Fire, así que las
  escamas de los dragones de hielo/fuego del mod **también sirven**; el material funciona
  con o sin el mod instalado.
- Con 144 mB por escama, una muerte da para 6-12 puntas de flecha; el dragón es
  reaparecible con cristales del End, así que no es un recurso de una sola vez.

### 14.2 Aleaciones de compat (fluidos nuevos)

Igual que el lote 7, recetas de TC3 reescaladas de 90 a 144 mB:

| Aleación | Receta en el fork | Stats (TC3) | Rasgo |
|---|---|---|---|
| `constantan` | cobre 144 + `nickel` 144 → 288 | Head(675, 7.50, 1.75, `DIAMOND`) | `shocking` |
| `invar` | hierro 288 + `nickel` 144 → 432 | Head(630, 5.50, 2.50, `DIAMOND`) | `stiff` |
| `pewter` | estaño 432 + `lead` 144 → 576 | Head(316, 3.50, 3.00, `DIAMOND`) | `heavy` |
| `nicrosil` | `nickel` 288 + `emerald` 144 + `quartz` 144 → 576 | Head(816, 6.00, 3.16, `COBALT`) | `duritos` |

Detalle: TC3 define **variantes** de `pewter` y `nicrosil` según qué metales tenga el pack
(plomo/estaño para pewter; cromo/níquel/esmeralda/estaño para nicrosil). No hay cromo en
1.12 ni en el pack, así que se implementa **una sola variante** con los fluidos que el fork
sí tiene (la que TC3 usa cuando falta cromo: níquel + esmeralda + cuarzo).

### 14.3 Compuestos de TC3 (items nuevos, meta 24-28)

Cinco materiales que en TC3 se obtienen **aplicando un fluido a un ítem** en la smeltery.
Requieren ítem propio, así que se añaden a `TinkerCommons.materials` (`ItemMetaDynamic`):

| Material | Ítem | Receta de vaciado | Stats en el fork |
|---|---|---|---|
| `slimeskin` | `matSlimeSkin` (24) | `leather` + 250 mB `greenSlime` | Extra(60) + cuerda |
| `nahuatl` | `matNahuatl` (25) | `plankWood` + 250 mB `obsidian` | Head(350, 4.50, 3.00, `DIAMOND`), mango, Bow |
| `darkthread` | `matDarkthread` (26) | `string` + 250 mB `obsidian` | Extra(140) + cuerda |
| `jeweledhide` | `matJeweledHide` (27) | `leather` + 666 mB `diamond` | Extra(250) + cuerda |
| `blazewood` | `matBlazewood` (28) | `firewood` + 200 mB `blazingBlood` | Extra(120) + asta(0.8, 5) |

Notas:

- **Huecos de meta**: `ItemMetaDynamic` registra modelo y textura por nombre, así que basta
  elegir un índice libre. Se usaron 24-28 (0-23 y 50 ya estaban ocupados).
- **Texturas generadas por re-coloreado** (`System.Drawing`, luminancia × color destino),
  técnica ya usada en el repo para seared→scorched: `slime_skin.png` y `darkthread.png` y
  `jeweled_hide.png` desde `silky_cloth.png`, `nahuatl.png` desde `lava_crystal.png` y
  `blazewood.png` desde `stone_rod.png`. Son funcionales, no definitivas: se pueden
  sustituir por arte propio sin tocar código.
- **Oredict**: `slimeskin`, `ingotNahuatl`, `darkthread`, `jeweledHide`, `blazewood`,
  registrados en `TinkerOredict` para que `integrate(material, "<oredict>")` los gatee.
- **Sustitución documentada**: TC3 hace `blazewood` con *bloodshroom*, una seta propia. El
  fork usa `TinkerCommons.firewood` en su lugar.

### 14.4 Correcciones de aleaciones + `bamboo` y `cheese`

Con las cantidades del libro de TC3 se corrigieron dos aleaciones y se pudieron cerrar dos
materiales más:

| Aleación | Antes | Ahora (según el libro) |
|---|---|---|
| `slimesteel` | hierro 144 + `blueslime` 250 + `searedStone` **144** → 288 | …+ `searedStone` **250** → 288 (1 ladrillo seared = 250 mB en TC3) |
| `amethystbronze` | cobre 144 + `amethyst` 144 → **288** | …→ **144**: TC3 es 2→1, se respeta aunque sea "con pérdida" |

`rosegold` (1+1→2), `queensslime` (Co+Au+magma→2) y `hepatizon` (2Cu+Co+Qz→2) ya coincidían
con el libro.

#### `bamboo` (tier 1) — Future MC
- **Future MC sí portea el bambú** (`BambooItem`, `BambooWorldGen`, oredict **`cropBamboo`**).
- Material `bamboo` con solo `BowMaterialStats(1.1f, 0.95f, 1f)` +
  `ArrowShaftMaterialStats(1.2f, 8)`: en TC3 no tiene ni cabeza ni mango
  (`Limb(70, +0.1, -0.05, -0.05)`, `Grip(-0.05, 0.05, 0.75)`).
- `integrate(bamboo, "cropBamboo")` → gate automático; sin Future MC no aparece.
- `reed` **no se toca**: sigue siendo el material de asta propio del fork (solo-fork).

#### `cheese` (tier 2) — leche + secadero
TC3: *"milk, pasarla a un molde de lingote y dejarla secar"*. El fork tiene las dos piezas:
`TinkerFluids.milk` y el secadero de `TinkerGadgets`.

1. Colar leche con molde de lingote → `matCheeseWet` (`materials.addMeta(30)`),
   vía `registerTableCasting(matCheeseWet, castIngot, TinkerFluids.milk, VALUE_Ingot)`.
2. Secadero → `matCheese` (`addMeta(31)`), con `registerDryingRecipe(..., 20*60*4)` en
   `TinkerGadgets`, junto al resto de recetas de secado.
3. Material `cheese`: `ExtraMaterialStats(111)` + `BowStringMaterialStats(1f)` (TC3:
   `BOWSTRING` + `laces(111)`); oredict `ingotCheese`.
- **Fuente de la leche**: el fluido `milk` del fork existía pero **no tenía ninguna fuente**
  (ni fundido ni receta). Se añade `MeltingRecipe(Items.MILK_BUCKET -> 1000 mB a 320 ºC)`.
  Ojo: `MeltingRecipe` no devuelve envases, así que **el cubo se consume** → 1000 mB / 144 =
  6 quesos por cubo. Es el único coste real del material.
- Nota: el secadero vive en el pulse `TinkerGadgets`; si se desactiva, el queso no se puede
  secar (igual que el cuero o el barro seco).

## 15. Reglas

- NUNCA exigir el mod en `build.gradle` (`compileOnly` no existe aquí; la
  integración es por oredict runtime). El mod debe **no crashear** si Future MC /
  Deeper Depths no están instalados.
- Si el oredict no existe, `MaterialIntegration` simplemente se salta
  (`isOredictPresent`/`oreRequirement`).
- `TinkerIntegration.PulseId` debe ejecutarse antes de registrar estas
  integraciones (ver `TConstruct.java:108`).