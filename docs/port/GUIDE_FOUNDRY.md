# Guía de la Foundry (scorched) + Melter — Núcleo implementado

Réplica fiel de la foundry de Tinkers' Construct 3 sobre 1.12.2: un multibloque de
bloques **scorched** separado de la smeltery, que **solo** acepta **sangre de blaze**
(`blazingBlood`) como combustible y **no** realiza aleaciones. Incluye el **Melter**
(fundidor 1×1), la pieza de fundido temprano de TC3.

Estado: implementado, compilado y empaquetado. Pendiente de pulido: categoría JEI de
la foundry y texturas placeholder de drain/gauge/window (ver `GUIDE_PORT_GAP_1.20.1.md` §2.6).

## 1. Cómo está implementado (diseño real)

### 1.1 Pulse `TinkerScorched`

Clase `src/main/java/slimeknights/tconstruct/foundry/TinkerScorched.java`, anotada
`@Pulse(id = "TinkerScorched")` y registrada en `TConstruct.java:110` **después** de
`TinkerIntegration` (necesita `TinkerSmeltery` y `TinkerIntegration` como
`pulsesRequired`). Usa el bus de eventos de Mantle:

- `@SubscribeEvent registerBlocks`: `blockScorched`, `scorchedTank`, `scorchedIO`,
  `foundryController`, `melter` + TE `TileScorchedFoundry` / `TileMelter`.
- `@SubscribeEvent registerItems`: ItemBlocks con meta (enum / `ItemTank`).
- `@SubscribeEvent registerModels` → `FoundryClientProxy.registerModels()`.
- `@Subscribe preInit/init/postInit`: recetas de horno y fundido/vaciado.

### 1.2 Bloques (`slimeknights.tconstruct.foundry.block`)

| Clase | Base | Registro | Notas |
|-------|------|----------|-------|
| `BlockScorched` | `BlockEnumSmeltery` (como `BlockSeared`) | `block_scorched` | 8 tipos: `STONE, COBBLE, BRICK, BRICK_CRACKED, BRICK_FANCY, BRICK_SQUARE, ROAD, CREEPER` |
| `BlockScorchedTank` | `BlockTank` (ItemBlock `ItemTank`) | `scorched_tank` | Tipos `tank/gauge/window`; almacena líquido y suministra combustible |
| `BlockScorchedIO` | `BlockSmelteryIO` | `scorched_drain` | Entrada/salida de fluido, mantiene interacción de cubos |
| `BlockScorchedController` | `BlockSmelteryController` | `foundry_controller` | TE `TileScorchedFoundry`; **sin GUI** |
| `BlockMelter` | `BlockInventoryTinkers` | `melter` | TE `TileMelter`; abre GUI |

### 1.3 TileEntity

- `TileScorchedFoundry extends TileSmeltery` (`.../foundry/tileentity/TileScorchedFoundry.java`):
  - `alloyAlloys()` vacío → **nunca alea** (los metales se mantienen separados).
  - `consumeFuel()` / `getFuelDisplay()` / `hasBlazeBlood()` restringidos a
    `TinkerFluids.blazingBlood` en tanques `scorched_tank`; cualquier otra fluido se ignora.
  - `updateStructureInfo()` busca tanques scorched (no seared).
  - `inventoryTitle = "gui.foundry.name"`.
- `TileMelter extends TileInventory` (`IInventoryGui`, `ISmelteryTankHandler`, …):
  - 2 slots: `INPUT` (solo con receta de fundido de temperatura ≤ 550) y `FUEL`
    (combustible sólido vía `TileEntityFurnace.getItemBurnTime`).
  - Tanque interno `SmelteryTank` de `Material.VALUE_Ingot * 4` (4 lingotes).
  - `MAX_TEMPERATURE = 550`: los materiales de más temperatura necesitan la smeltery/foundry.
  - Network: `SmelteryFluidUpdatePacket` + `TileProcessingPacket` (sonido de calentamiento).

### 1.4 Multibloque

`MultiblockScorched extends MultiblockSmeltery` (`.../foundry/multiblock/MultiblockScorched.java`),
misma forma que la smeltery (mín. 3×3 base, hasta 5×5, altura 2-3) pero aceptando solo
bloques `scorchedTank` + `validScorchedBlocks` (`blockScorched`).

### 1.5 GUI / render client

- `FoundryClientProxy` (`.../foundry/FoundryClientProxy.java`): registra modelos item
  (enum por meta, `inventory` para melter/controlador, variants `tank/gauge/window`),
  y liga el TESR `SmelteryRenderer` a `TileScorchedFoundry` (renderiza el metal fundido
  dentro del multibloque, como la smeltery).
- Melter: `GuiMelter`/`ContainerMelter` (patrón `IInventoryGui` como `TileSearedFurnace`),
  tooltips de error reutilizando `gui.smeltery.progress.*` (`no_recipe`, `no_fuel`, `no_space`).

## 2. Combustible y registros

- La smeltery ya registra los combustibles en `TinkerSmeltery.registerSmelteryFuel()`
  (`TinkerSmeltery.java:357-360`): lava y `blazingBlood` (50→150). La foundry lo
  reutiliza y **restringe su tanque de combustible a `blazingBlood`**.
- `TinkerScorched.registerFurnaceRecipes()`: horno `scorched brick → brick_cracked` (0.1 XP).
- `TinkerScorched.registerMeltingCasting()`: scorched se funde a `searedStone`; vaciado
  de lingote (brick) en mesa y de bloque (stone) en pileta.

## 3. Recursos (JSON)

| Recurso | Archivos |
|---------|----------|
| Blockstates | `blockstates/block_scorched.json`, `scorched_tank.json`, `scorched_drain.json`, `foundry_controller.json`, `melter.json` |
| Modelos tanque | `models/block/tank/{tank,gauge,window}.json` + `tank_knob.json`, `models/block/tank/*/[0-8].json` (misma estructura que upstream) |
| Modelo melter | `models/block/melter.json` (usa `tconstruct:blocks/smeltery/melter_side`) |
| Recetas | `recipes/foundry/` — 12: 8 variantes scorched + `foundry_controller`, `foundry_drain`, `foundry_tank`, `melter` |
| Lang | `en_us.lang` (sección `# Foundry (scorched)`) y `es_es.lang` (misma sección + `gui.smeltery.progress.no_fuel`) |

### Texturas

- Copias reales de 1.20.1: `scorched_brick`, `scorched_road`, `scorched_stone`,
  `scorched_tank_side/top`, `scorched_controller_active/inactive` (animada 16×32).
- Variantes derivadas por **re-coloreado** seared→scorched (color-transfer por media/σ
  del par de referencia bricks): `scorched_cobble`, `scorched_brick_cracked/fancy/square`,
  `scorched_creeper`, `scorched_paver`.
- Placeholders grises (pending): `scorched_drain_back/front`, `scorched_gauge_side`,
  `scorched_window_side/top`.

## 4. Verificación

```powershell
$env:JAVA_HOME = (Get-Command java).Source | Split-Path | Split-Path
.\gradlew.bat build
```

Comprobado:
- El controlador solo consume blaze blood como fuel (lava → rechazada).
- No se genera ningún metal nuevo por aleación dentro de la foundry.
- El fundido funciona igual que la smeltery (mismo `TinkerRegistry`).
- El melter funde un objeto a la vez con combustible sólido y muestra tooltips de error.
- Feature check del jar: `ALL TEXTURE REFERENCES OK` (1208 referencias resueltas).