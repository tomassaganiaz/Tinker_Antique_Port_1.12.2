# Guía de Armadura de Placas (TC3) — Documentada, implementación posterior

El fork **no tiene sistema de armadura** en el estado actual: solo existen
`slime_layer_1.png`, `slime_layer_1_overlay.png` y `piggyback_layer_1.png` en
`resources/assets/tconstruct/textures/models/armor/`. Esta guía documenta cómo portar
la armadura de placas de Tinkers' Construct 3 (plates, chestplate, leggings, boots)
sobre 1.12.2. La referencia 1.20.1 (`TinkersConstruct-1.20.1/`, de **solo lectura**)
proporciona el diseño conceptual; las plantillas de código reales están en la 1.12.2.

> Nota de alcance: en esta sesión SOLO se documenta. La implementación del código y
> las texturas queda pendiente de una iteración posterior.

## 1. Referencia TC3 (1.20.1, read-only) — qué imitar

Clases reales en la 1.20.1 (consulta, no modificar):

- `library/tools/definition/ModifiableArmorMaterial.java` — `IArmorMaterial` con
  `getResource(...)` de "travelers", "plate" y "slime".
- `library/tools/item/armor/ModifiableArmorItem.java` — `extends ArmorItem implements
  IModifiableDisplay`; base de todo item de armadura modificable.
- `library/tools/item/armor/MultilayerArmorItem.java` — `extends ModifiableArmorItem`;
  variantes multicapa que renderizan color por material (guantes/placas).
- `library/tools/item/armor/DummyArmorMaterial.java` — material de armadura por defecto.
- `tools/ArmorDefinitions.java` — conjuntos `TRAVELERS`, `PLATE`, `SLIMESUIT` y escudos
  (`TRAVELERS_SHIELD`, `PLATE_SHIELD`), vía `ToolDefinition`.
  > **Nota fork (2026-09-28):** todo el set `TRAVELERS` (`TRAVELERS_SHIELD` + `travelers_helmet/chestplate/leggings/boots`
  > y partes `travelers_plating_*`) **no se porta** — duplicado: otro mod del pack ya lo implementa funcional y sin bugs
  > para esta versión de Tinkers Antique (**bloqueado / ya implementado**, N/A). El fork solo registra `PLATE` + `SLIMESUIT`
  > y `plate_shield` (`TinkerArmor`, ToolForge).
- `tools/item/SlimeskullItem.java` y `tools/client/SlimeskullArmorModel.java` — ejemplo
  de armadura con modelo personalizado.

En TC3 la protección sale de stats de material por slot (armadura = multicapa de
`PlatingMaterialStats`), no de un único `IArmorMaterial` vanilla.

## 2. Plantillas reales del fork (1.12.2) que ya existen

- **`TinkersItem`** — `library/tinkering/TinkersItem.java:55` (`extends Item implements
  ITinkerable, IModifyable, IRepairable`). Base de herramientas: `buildItemFromStacks`,
  `buildItemNBT`, `buildTag(List<Material>)` (abstract), `validComponent`, `repair`,
  `addMaterialTraits`. La armadura debe extender esta clase, **no** `Item` a secas.
- **`ToolCore`** — `library/tools/ToolCore.java:66` (`extends TinkersItem implements
  IToolStationDisplay`); patrón de implementación a replicar para `ItemTinkersArmor`.
- **`ToolPart`** — `library/tools/ToolPart.java:36` (`extends MaterialItem implements
  IToolPart`). **No existe** un paquete `library/tools/part`: las partes viven en
  `library/tools/`. Registro en `tools/TinkerTools.java:183-217`
  (`registerToolParts` → `AbstractToolPulse.registerToolPart`) y en
  `TinkerRegistry.registerToolPart(IToolPart)` (`library/TinkerRegistry.java:368`).
- **NBT de herramientas** — claves en `library/utils/Tags.java`: `BASE_DATA="TinkerData"`,
  `BASE_MATERIALS="Materials"`, `BASE_MODIFIERS="Modifiers"`, `BASE_USED_MODIFIERS`,
  `PART_MATERIAL="Material"`, `TOOL_TRAITS="Traits"`, `TOOL_DATA="Stats"`, `DURABILITY`,
  `FREE_MODIFIERS`, `BROKEN`. Helpers: `TagUtil`, `TinkerUtil`, `ToolBuilder`, `ToolHelper`
  (todos en `library/utils/`). **No existe** una clase `NBTUtil`.
- **Registro de crafting** — `TinkerRegistry.registerToolCrafting()` /
  `registerToolForgeCrafting()` (`library/TinkerRegistry.java:385-404`).
- **GUI station** — `tools/common/inventory/ContainerTinkerStation.java` (`extends
  ContainerMultiModule`) y `tools/common/client/GuiTinkerStation.java` (`extends
  GuiMultiModule`). Están en `tools/common/`, **no** en `library/tools/`.
- **Modificadores** — `library/modifiers/Modifier.java:35` (`extends RecipeMatchRegistry
  implements IModifier`), interfaz `IModifier`; registro en `tools/TinkerModifiers.java`
  (añadidos por `TinkerRegistry.registerModifier`).
- **Traits por material** — `tools/TinkerTraits.java` + asociación en
  `tools/TinkerMaterials.java` (`Material.addTrait(ITrait, type)`). **No existe**
  `MaterialTraits` ni `Characteristic` (son conceptos de TC3/1.20.1).
- **Materiales** — 41 JSON en `resources/assets/tconstruct/materials/*.json`
  (listing exacto, ver §5). Clase stats base: `library/materials/Material.java` +
  `IMaterialStats`/`MaterialTypes`.
- **Stats de herramientas** — ejemplos en `library/materials/` (`HeadMaterialStats`,
  `HandleMaterialStats`, etc.) como plantilla para crear `ArmorMaterialStats`.

## 3. Clases base necesarias (diseño del port)

### 3.1 `ItemTinkersArmor extends TinkersItem`

- Hereda de `TinkersItem` (⇒ `ITinkerable`/`IModifyable`/`IRepairable`), con
  `PartMaterialType[]` por slot (cabeza/pecho/piernas/pies) como
  `ToolCore.requiredComponents`.
- Implementa la interfaz vanilla de armadura (`isValidArmor`, `getColorFromItemStack`)
  y material usa `IArmorMaterial` dinámico; protección calculada desde
  `Material.getStats(ArmorMaterialStats)` de cada slot
  (`getArmorDisplay`/`damageAmount` = suma de stats de slot, estilo 1.20.1).
- `buildTag(List<Material>)` serializa stats por slot en `Tags.*`.

### 3.2 Partes (`ToolPart` reutilizado)

- Nuevas partes como subclases de `ToolPart` registradas con
  `TinkerRegistry.registerToolPart`: `platingHelmet`, `platingChest`, `platingLegs`,
  `platingBoots` (equivalentes de "placa de pecho/piernas..." de TC3).
- Costes de material: `Material.VALUE_Ingot * n` (como `TinkerTools.java`).

### 3.3 NBT y render

- NBT por slot: `Tags.BASE_MATERIALS` (+ por parte `Tags.PART_MATERIAL`), durabilidad en
  `Tags.DURABILITY`, stats calculados en `Tags.TOOL_DATA`, modificadores en
  `Tags.BASE_MODIFIERS`/`Tags.TOOL_MODIFIERS` (todo vía `TagUtil`).
- Render de capas de color por material: implementar propio
  (`LayerArmor` de vanilla solo soporta `IArmorMaterial` estático) — sobre el patrón de
  `MultilayerArmorItem` de la referencia 1.20.1.
- Texturas por slot en `resources/assets/tconstruct/textures/models/armor/`.

### 3.4 Modificaciones en armadura

- Reutilizar `library/modifiers/Modifier` + `TinkerModifiers.java` (p. ej. el ya portado
  `ingotNetherite → modReinforced`, `TinkerModifiers.java:225-240`).
- Traits: añadir un nuevo identifier de stats (`"armor"`) y asociar traits vía
  `Material.addTrait(trait, "armor")`, registrados en `TinkerTraits` (patrón existente);
  no usar el concepto 1.20.1 de "Characteristic".

## 4. Pasos de implementación (orden recomendado)

1. **Stats**: crear `ArmorMaterialStats implements IMaterialStats` + registrar el
   identifier en `MaterialTypes`/`library/materials/`.
2. **Partes**: `platingHelmet/Chest/Legs/Boots` como `ToolPart` +
   `registerToolPart` (`TinkerTools.java`, patrón `registerToolParts`).
3. **Materiales**: `TinkerMaterials.java` — añadir stats de armadura a los materiales
   deseados (ver §5).
4. **Item**: `ItemTinkersArmor extends TinkersItem` con slots por parte y `buildTag`.
5. **Recetas**: ensamblaje en la Tinker Station vía `TinkerRegistry.registerToolForgeCrafting`.
6. **GUI**: `ContainerArmorStation`/`GuiArmorStation` (patrón de
   `ContainerTinkerStation`/`GuiTinkerStation` en `tools/common/`).
7. **Modificaciones**: aplicar `Modifier` sobre `ItemTinkersArmor` (mismo framework).
8. **Render client**: capas de color por material (según `MultilayerArmorItem`) + modelos
   JSON y texturas en `textures/models/armor/`.

## 5. Materiales sugeridos para armadura

Los **43** materiales existentes (`resources/assets/tconstruct/materials/*.json`):
`alubrass, alumite, aluminum, ardite, blaze, bloodbone, blueslime, bone, bronze, cobalt,
copper, electrum, endrod, endstone, feather, firewood, flint, ice, iron, knightslime,
lead, leaf, magmaslime, manyullyn, netherrack, obsidian, pigiron, prismarine, reed,
silver, slime, slimeleaf_blue, slimeleaf_orange, slimeleaf_purple, slimevine_blue,
slimevine_purple, sponge, steel, stone, string, tin, vine, wood`.

Para armadura (metales, igual que en TC3): `iron`, `copper`, `bronze`, `steel`, `silver`,
`lead`, `tin`, `aluminum`, `cobalt`, `manyullyn`, `knightslime`, `pigiron`, `alumite`…
**No existen** JSON de `leather` ni `gold` en el fork. El meta de stats de armadura es
nuevo: **no existe `ArmorMaterialStats`** en la 1.12.2 (habrá que crearla copiando el
patrón de `HeadMaterialStats`).

## 6. Estado actual en el repo (bloqueadores)

- **No existe** `ItemTinkersArmor` ni paquete `library/tools/armor` (ni en
  `library/tools/`, ni en `tinkering/`).
- **Sí existen** las plantillas de §2: `TinkersItem`, `ToolCore`, `ToolPart`, `Tags`,
  `ContainerTinkerStation`/`GuiTinkerStation`, `Modifier`+`IModifier`,
  `TinkerTraits`/`TinkerMaterials`, 41 materiales JSON.
- **No existen** texturas de placas: `textures/models/armor/` solo tiene
  `slime_layer_1.png`, `slime_layer_1_overlay.png` y `piggyback_layer_1.png`.

## 7. Verificación propuesta

- `gradlew.bat compileJava` con `JAVA_HOME` = JDK 8 (ver `GUIDE_PORT_OVERVIEW.md`).
- GUI ensambla platos de material y muestra stats/protección por slot.
- El jugador equipa el item completo (4 slots) y el render usa capas de color por material.
- Las modificaciones ya portadas (`ingotNetherite` → reforzado, `TinkerModifiers.java:225-240`)
  funcionan sobre armadura igual que sobre herramientas.