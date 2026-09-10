# Guía de Port — Visión General (docs + núcleo)

## 1. Objetivo

Adaptar el fork Tinkers' Antique (Tinkers' Construct 1.12.2) incorporando la
funcionalidad característica de Tinkers' Construct 3:

1. **Foundry "scorched"**: multibloque propio (nuevos bloques/tanques/drenajes
   `scorched`), separado de la smeltery, que **solo** quema sangre de blaze como
   combustible y **no** realiza aleaciones.
2. **Metales nuevos** (netherite, cobre, amatista) integrados **solo** como insumos
   de las modificaciones que los requieren — no como outils propios por defecto.
3. **Armadura de placas** (full TC3 armor): documentada para implementación posterior.

## 2. Arquitectura del mod (estado actual)

El mod usa el sistema de **pulses** de Mantle: cada subsistema es una clase con
anotación `@Pulse(id = "...")` registrada en el bloque estático de
`TConstruct.java` (`src/main/java/slimeknights/tconstruct/TConstruct.java:94-127`).

Orden de registro (importante para dependencias):

```
TinkerCommons (bloques/items base: searedBrick, tierra)
TinkerWorld   (minerales comunes: cobre, estaño, aluminio)
TinkerTools
TinkerHarvestTools
TinkerMeleeWeapons
TinkerRangedWeapons
TinkerModifiers
TinkerSmeltery      <- smeltery, tanques seared, combustibles, fundido
TinkerGadgets
TinkerOredict       <- oredict de items añadidos antes
TinkerIntegration   <- integra materiales fluidos/aleaciones/fundido
TinkerScorched      <- foundry scorched + melter (después de la integración)
TinkerFluids
TinkerMaterials
+ plugins
```

### Dependencia clave para la foundry

`TinkerIntegration` tiene `pulsesRequired = TinkerSmeltery.PulseId`
(`TinkerIntegration.java`). La foundry necesita `TinkerSmeltery` (tanques,
registro de combustible, casting) y **debe registrarse después** de
`TinkerIntegration` si usa `MaterialIntegration`, o bien usar
`pulsesRequired = { TinkerSmeltery.PulseId, TinkerIntegration.PulseId }`.

## 3. Punto crítico de build: JEI 4.16.+ y ForgeGradle

- `build.properties` tenía `jei_version=4.16.+`, lo que resuelve a
  `jei_1.12.2-4.16.5.1030.jar`, **compilado con Java 21** (class file major 65).
- ForgeGradle `2.3-1.0.8` usa un ASM antiguo que lanza
  `IllegalArgumentException` en `TaskSingleDeobfBin.deobfClass` al intentar leer
  esa clase: *"no error message"* en `org.objectweb.asm.ClassReader`.
- **Solución aplicada**: fijar `jei_version=4.15.0.297` (compilado con Java 8,
  major 52). El `deobf` compila y la API son compatibles para la integración actual.

```properties
# build.properties (cambiado)
jei_version=4.15.0.297
```

Verificar con:

```powershell
$env:JAVA_HOME = (Get-Command java).Source | Split-Path | Split-Path   # JDK 8
.\gradlew.bat compileJava --console=plain
```

## 4. Decisiones de diseño

| Decisión | Elección | Motivo |
|----------|----------|--------|
| Origen netherite | Future MC vía oredict `ingotNetherite` | Sin dep dura; integración condicional |
| Origen cobre | Deeper Depths vía oredict `ingotCopper` | El mod ya genera cobre natural |
| Origen amatista | Deeper Depths vía oredict `gemAmethyst` | Igual; no worldgen propio |
| Foundry | Multibloque propio "scorched", sin aleaciones, solo blaze blood | Réplica fiel TC3 |
| Armadura | Solo documentar en esta sesión | Scope "docs + núcleo" |

## 5. Checklist global

- [x] Build verificado: `compileJava` con JEI 4.15.0.297 (Java 8).
- [x] Foundry: bloques scorched, controlador, TileEntity, solo blaze blood.
- [x] Melter: fundidor 1×1 con tanque y GUI.
- [x] Metales condicionales: netherite / cobre / amatista vía oredict (mods externos).
- [x] Metales tier 1: estaño y aluminio como materiales completos (mena, fluido, stats, casting).
- [ ] Armadura de placas: guías escritas (implementación posterior).

## 6. Referencias de archivos

| Tema | Archivo |
|------|---------|
| Registro de pulses | `src/main/java/slimeknights/tconstruct/TConstruct.java:94-127` |
| Registro de combustibles smeltery | `src/main/java/slimeknights/tconstruct/smeltery/TinkerSmeltery.java:357-360` |
| Lógica de combustible | `src/main/java/slimeknights/tconstruct/smeltery/tileentity/TileHeatingStructureFuelTank.java` |
| Lógica smeltery | `src/main/java/slimeknights/tconstruct/smeltery/tileentity/TileSmeltery.java` |
| Multibloque | `src/main/java/slimeknights/tconstruct/smeltery/multiblock/MultiblockSmeltery.java`, `MultiblockTinker.java` |
| Bloques plantilla | `.../smeltery/block/BlockSmelteryController.java`, `BlockTank.java`, `BlockSeared.java`, `BlockMultiblockController.java` |
| Fluidos | `src/main/java/slimeknights/tconstruct/shared/TinkerFluids.java` (`blazingBlood`, helper `fluidMetal`) |
| Integración condicional | `src/main/java/slimeknights/tconstruct/TinkerIntegration.java`, `.../library/MaterialIntegration.java` |
| Worldgen existente | `src/main/java/slimeknights/tconstruct/shared/worldgen/OverworldOreGenerator.java` (cobre ya), `.../world/TinkerWorld.java` |