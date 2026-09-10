# Documentación del Port — Tinkers' Antique 1.12.2

Este directorio documenta el port (fork) de Tinkers' Construct 1.12.2 hacia las
características de Tinkers' Construct 3 (TC3) solicitadas para este repositorio.

## Estado del proyecto

**Alcance elegido**: "Docs + núcleo". Se documenta el port completo y se implementa
el núcleo:

- **Foundry "scorched"** multibloque propio, solo con sangre de blaze como combustible,
  sin aleaciones.
- **Melter**: fundidor 1×1 con tanque propio, funde un objeto a la vez con combustible
  sólido y temperatura limitada.
- **Metales** (netherite, cobre, amatista) integrados condicionalmente vía oredict,
  solo para las modificaciones que lo requieren.
- **Metales tier 1**: estaño y aluminio como materiales completos de herramienta
  (mena overworld, fluido, stats, fundido/vaciado).
- **Materiales sobre fluidos existentes**: oro, piedra cocida (`seared`), cristal,
  arcilla, slime del End y sangre como materiales de herramienta (§7 de
  `GUIDE_MATERIALS.md`). Con `sangre` se completa el tier 5 de TC3.
- **Materiales tier 1 de TC3**: `chorus` y `wool` nuevos, `reed` ampliado a material de
  arco (equivalente de `bamboo`) y JSON de render para `cactus`, `paper` y `reed` (§8 de
  `GUIDE_MATERIALS.md`).
- **Ammo y bindings**: cuero, hueso necrótico, perla de ender, pólvora, redstone, amatista,
  cuarzo, glowstone y caparazón de shulker como materiales (§9 de `GUIDE_MATERIALS.md`),
  más el [anexo de categorías](materiales_tier.md#anexo-a--categorías-del-libro-de-tc3).
- **Vides del Nether**: `weepingVine` y `twistingVine` vía Unseen's Nether Backport
  (`nb:crimson_vine` / `nb:warped_vine`), con gate por mod cargado y sin dependencia
  obligatoria (§10 de `GUIDE_MATERIALS.md`).
- **Piedra scorched**: nuevo fluido `scorched_stone` + material de tier 2, con el
  fundido/vaciado de los bloques scorched movido de la piedra seared a la suya propia
  (§11 de `GUIDE_MATERIALS.md`).
- **Aleaciones de TC3 (tier 3)**: slimesteel, oro rosado y bronce de amatista, con fluidos
  nuevos y recetas de aleación en la smeltery (§12 de `GUIDE_MATERIALS.md`). El fluido de
  amatista se funde desde el oredict `gemAmethyst` de Deeper Depths.
- **Tier 4**: slime de la reina y hepatizon (aleaciones con fluidos nuevos de magma y cuarzo),
  hueso ardiente (sobre un ítem que el fork ya tenía) y ancient con chatarra de netherite del
  Nether Backport (§13 de `GUIDE_MATERIALS.md`).
- **Armadura de placas** de TC3: documentada con guías paso a paso, no implementada
  en esta sesión.

Fuentes de metales externos (vía oredict):

| Metal    | Mod origen    | Tag oredict                            |
|----------|---------------|----------------------------------------|
| Netherite| Future MC     | `ingotNetherite`                       |
| Cobre    | Deeper Depths | `ingotCopper`                          |
| Amatista | Deeper Depths | `gemAmethyst` / `shardAmethyst`        |

## Índice de guías

- [Guía de port — visión general](GUIDE_PORT_OVERVIEW.md) — arquitectura, pulses,
  registro, decisiones de diseño, checklist global.
- [Guía de la Foundry (scorched)](GUIDE_FOUNDRY.md) — multibloque propio tipo TC3,
  solo sangre de blaze, sin aleaciones. Implementación del núcleo.
- [Guía de metales e integración](GUIDE_MATERIALS.md) — netherite, cobre, amatista;
  `MaterialIntegration`, fluidos, fundido/vaciado, uso en modificaciones.
- [Guía de armadura de placas](GUIDE_ARMOR.md) — port completo de la armadura de TC3:
  clases base, NBT, modificaciones. Documentada para implementación posterior.
- [Guía de brecha de port (1.20.1 → 1.12.2)](GUIDE_PORT_GAP_1.20.1.md) — inventario de
  qué falta portar de TC3: armadura/escudos, modificadores nuevos, materiales,
  cambios estructurales; con priorización.
- [Materiales pendientes](materiales_pendientes.md) — informe de los 33 materiales que
  faltan (1 ❌ + 21 N/A + 11 compat ❌): por qué no están, qué ítem del modpack los
  desbloquearía y cómo implementarlos, ordenados por coste.

## Convenciones del repo

- Raíz del proyecto: `TinkersAntique-1.12/TinkersAntique-1.12` (este directorio).
- `TinkersConstruct-1.20.1/` (raíz del workspace): **solo referencia de lectura** para
  el port. No se permiten modificaciones en la versión 1.20.1; se usa únicamente como
  fuente al codear (texturas, JSON, lógica).
- Java 8 (Temurin). Forge `14.23.5.2847`. Mappings `snapshot_20170801`.
- Mantle `1.3.3.55`. JEI **fijado a `4.15.0.297`** (la rama `4.16.+` está compilada
  con Java 21 y rompe el `deobf` de ForgeGradle; ver `GUIDE_PORT_OVERVIEW.md`).
- Assets en carpeta raíz `resources/` (no en `src/main/resources`).
- Compilación de verificación: `gradlew compileJava` (con `JAVA_HOME` = JDK 8).