# Por qué las texturas aparecen magenta/negra (missingno) – TinkersAntique 1.12

## Qué es la textura magenta/negra
Es el `missingno` de Minecraft/Forge. Aparece cuando el juego no encuentra la textura o el modelo que debería mostrar. No es un error de la imagen en sí, sino de **registro / ruta / caché**.

## Caso 1: Obsidian Pane (crash `Casting Recipe is missing an output`)

### Causa
1. `TinkerCommons.blockObsidianPane` se registró como `BlockPane` pero el `blockstate` era un `cube_all` simple (`forge_marker:1` con `variants: normal`). `BlockPane` espera 16 variantes `north/south/east/west/up/down` – el loader lanza `MissingVariantException`.
2. El `ItemBlock` del pane no se registró en `Register<Item>`. Entonces `new ItemStack(blockObsidianPane,1)` devolvía `ItemStack.EMPTY`. `CastingRecipe.<init>` comprueba `if(output.isEmpty()) throw new TinkerAPIException("Casting Recipe is missing an output!")` → crash en `postInit`.
3. Además la blockstate con `forge_marker` es para `EnumBlock`, no para un `Block` simple.

### Fix aplicado
- Cambiado `BlockObsidianPane extends Block` (cubo sólido) para que coincida con el `blockstate` simple:
  ```json
  { "variants": { "normal": { "model": "tconstruct:obsidian_pane" } } }
  ```
- Modelo `block/cube_all` con `textures.all = tconstruct:blocks/obsidian_pane` (16×16 PNG POT).
- Registro explícito del `ItemBlock` en `TinkerCommons.registerItems`:
  ```java
  if(blockObsidianPane != null) registerItemBlock(registry, blockObsidianPane);
  ```
- Receta de fundición con guard `if(blockObsidianPane != null)` para no crear una receta con output vacío aunque el bloque falle.

> El bloque sigue siendo un cubo. Si se quiere un *pane* fino real hay que hacer un `BlockPane` con su `blockstate` de conexiones y sus modelos `pane_north/south/...`.

## Caso 2: 5 Reinforced (magenta en JEI e inventario)

### Causa
1. **Meta fuera de rango del atlas del `ItemMetaDynamic`**. Se añadieron como `materials.addMeta(32-36, "reinforcement_iron"...)` (metas 32-36). `ItemMetaDynamic` genera el atlas en `onModelRegister` y, si el `run/` está cacheado, las nuevas variantes no se añaden y quedan como `missingno`.
2. **Ruta de textura vs item**. Los 5 se añadieron primero a `materials` (`materials:32-36`) cuya textura se busca en `textures/items/materials/reinforcement_iron.png` (flat, `items` en plural). Las texturas originales 1.20 están en `textures/item/materials/reinforcement/iron.png` (`item` en singular + subcarpeta). Al copiar solo a una ruta, la otra quedaba vacía y el `ModelBakery` no la encontraba.
3. **Build incremental**. `processResources` no copió los PNG nuevos porque el `build/` no se limpió. El `jar` seguía sin las 5 texturas y el `run/assets` mantenía el atlas viejo (magenta).

### Fix aplicado
- Copia de las 5 texturas originales 1.20 (16×16, POT, RGBA) a **ambas rutas**:
  - `assets/tconstruct/textures/items/materials/reinforcement_iron.png` (flat, 1.12)
  - `assets/tconstruct/textures/item/materials/reinforcement/iron.png` (fallback 1.20)
- Movimiento de los 5 de `materials:32-36` a `ingots:13-17` (rango usado y probado para `ingot_hepatizon:11`/`queensslime:12`). `ItemTinker` de lingotes sí resuelve `ingot_reinforcement_iron.png` sin colisión con `materials`.
- Duplicado de los PNG como `ingot_reinforcement_iron.png` (y gold/cobalt/seared/obsidian) para que `ingots` los encuentre.
- Lang duplicado:
  - `item.tconstruct.materials.reinforcement_iron.name` (compatibilidad vieja)
  - `item.tconstruct.ingots.reinforcement_iron.name` (nueva, la que usa el `ingots` render)
- Limpieza obligatoria: `gradlew clean` + borrar `run/` antes de `runClient` para forzar `ModelLoader` y `TextureAtlas` a regenerar el atlas.

## Requisitos generales de texturas en 1.12
- **Tamaño**: potencias de 2 (16×16, 32×32). Tu `obsidian_pane` era 64×64 y se reescaló a 16×16; si no es POT el mipmapping falla.
- **Formato**: PNG 32-bit RGBA, sin interlazado, sin perfil ICC.
- **Ruta exacta**: distingue `items` vs `item` (1.12 usa `items`). Un `s` mal puesto = `missingno`.
- **Nombre**: minúsculas, `ingot_foo` vs `foo` no son intercambiables. `ingots.addMeta(13,"reinforcement_iron")` busca `ingot_reinforcement_iron.png`.
- **Registro**: todo `Block` necesita su `ItemBlock` en `Register<Item>`, si no `new ItemStack(block)` es `EMPTY` y rompe las recetas de `CastingRecipe`.
- **Caché**: `run/` guarda `textureAtlas` y `modelBakery`. Si añades texturas nuevas sin borrar `run/`, el cliente sigue viendo el atlas viejo magenta.

## Cómo verificar que se arregló
1. `gradlew clean build` → `build/resources/main/assets/tconstruct/textures/.../reinforcement_*.png` deben existir y pesar ~500-700 B.
2. `jar tf build/libs/*.jar | grep reinforcement` debe listar las 10 (5 flat + 5 ingot).
3. Borrar `run/` → `gradlew runClient` → JEI debe mostrar los 5 sin magenta y sin `MissingVariantException` en `logs/latest.log`.
