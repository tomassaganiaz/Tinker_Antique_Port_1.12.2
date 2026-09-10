# Estado actualizado del proyecto: port 1.20.1 → 1.12.2

Fecha: 2026-09-08
Fork: `TinkersAntique-1.12`
Referencia: `TinkersConstruct-1.20.1` (solo lectura)

## Verificación actual

Se ejecutó la compilación del proyecto con:

```powershell
cd "c:\Users\tomiz\Downloads\TinkersAntique-1.12\TinkersAntique-1.12"
./gradlew build
```

Resultado verificado por terminal:

- `BUILD SUCCESSFUL in 6m 15s`
- `25 actionable tasks: 19 executed, 6 up-to-date`

Eso confirma que el fork actual compila y que el núcleo del port no está roto. Sin embargo, eso no equivale a “parity total con TC3 1.20.1”.

---

## Resumen práctico

### Estado real del port

| Área | Estado real | Comentario |
|------|-------------|------------|
| Compilación general | ✅ Funcional | El proyecto compila correctamente |
| Materiales base / fluidos | ✅ En gran parte | Hay registro y estadísticas importantes |
| Smeltery / aleaciones | ✅ Funcional | Estructura principal portada |
| Foundry / scorched | ✅ Funcional | Hay bloques y multibloque portados |
| Metales / materiales tier medio/alto | ✅ Parcialmente | Hay integración, pero no todo está verificado al nivel TC3 |
| Armadura de placas / escudos | 🟡 Parcial | Hay clases e items, pero la parity real aún no está cerrada |
| JEI / UX de foundry / melter | 🟡 Parcial | Faltan algunos elementos de pulido y categorías |
| Texturas placeholder / finos visuales | 🟡 Parcial | Se documentan placeholders y gaps de arte |
| Parity total con 1.20.1 | ❌ No | Sigue siendo un port funcional, no un clon 1:1 |

---

## Lo que sí está portado y tiene evidencia en el código

### 1) Materiales y fluidos

Se observa la base del sistema en:

- `src/main/java/slimeknights/tconstruct/tools/TinkerMaterials.java`
- `src/main/java/slimeknights/tconstruct/shared/TinkerFluids.java`

Hay materiales registrados, fluidos y estadísticas de herramienta. El proyecto ya incluye un conjunto importante de materiales y aleaciones, con datos manejados desde código y/o registros de oredict.

### 2) Armadura

La armadura está presente al menos en una forma funcional:

- `src/main/java/slimeknights/tconstruct/tools/armor/TinkerArmor.java`
- `src/main/java/slimeknights/tconstruct/tools/armor/item/ArmorCore.java`
- `src/main/java/slimeknights/tconstruct/tools/armor/client/LayerTinkerArmor.java`
- `src/main/java/slimeknights/tconstruct/tools/modifiers/ModProtection.java`

Esto confirma que hay:

- piezas de armadura portadas
- tipos de piezas y material stats
- rendereo de capas de armadura
- modifier base de protección
- registro de armaduras y materiales

### 3) Foundry / scorched / melter

La base del sistema está implementada y se ve en:

- `src/main/java/slimeknights/tconstruct/foundry/block/BlockScorched.java`
- `src/main/java/slimeknights/tconstruct/foundry/block/BlockScorchedChannel.java`
- `src/main/java/slimeknights/tconstruct/foundry/block/BlockScorchedTank.java`
- `src/main/java/slimeknights/tconstruct/foundry/block/BlockMelter.java`
- `src/main/java/slimeknights/tconstruct/foundry/tileentity/TileScorchedFoundry.java`

Además la documentación del port confirma que la foundry y el melter están presentes como estructura funcional.

### 4) Smeltery / aleaciones

El clásico núcleo de la smeltery está presente en:

- `src/main/java/slimeknights/tconstruct/smeltery/TinkerSmeltery.java`

La documentación y el código dejan claro que la smeltery y las aleaciones principales ya están integradas y compilando.

---

## Lo que aún falta o no está cerrado al nivel de 1.20.1

### P0 — Lo más importante para cerrar parity

#### 1) Armadura y escudos: parcial, no “parity total”

La documentación lo deja claro en varios archivos:

- `docs/port/GUIDE_PORT_GAP_1.20.1.md`
- `docs/port/GUIDE_ARMOR.md`
- `docs/port/ARMOR_PROGRESS.md`

La idea clave es:

- el fork tiene una base de armadura
- pero la documentación aún marca la armadura como pendiente o de diseño / implementación escalonada
- la referencia 1.20.1 tiene varias capas adicionales de sistema, traits y variantes que no se pueden considerar resueltas solo porque compile

En otras palabras: está “implementado a nivel base”, pero no es todavía un port 1:1 a la armadura de TC3.

#### 2) JEI / UX del foundry y el melter

La documentación menciona explícitamente faltantes de pulido:

- foundry category
- melter category
- texturas placeholder en foundry `drain/gauge/window`

Esto indica que la lógica funciona, pero la experiencia final no está cerrada como en la 1.20.1.

#### 3) Texturas placeholder / arte final

La documentación de port señala que todavía hay piezas visuales que no están reemplazadas por arte real de la referencia 1.20.1:

- `scorched_drain_*`
- `scorched_gauge_side`
- `scorched_window_*`

Esto no rompe compilación, pero sí deja el juego con un aspecto incompleto frente a la referencia moderna.

### P1 — Omisiones de contenido funcional

#### 4) Materiales y variantes dependientes de mods o sistemas no portados

En varios documentos se mencionan materiales que se quedaron fuera por compatibilidad o inexistencia del sistema equivalente en 1.12:

- `bamboo` / `reed`
- materiales con dependencia de armadura / slimesuit
- materiales vencidos por versión o por falta de item del modpack
- materiales parcialmente dependientes de Nether Backport / Future MC / Deeper Depths

El diagnóstico de la documentación es correcto: no todo el catálogo de TC3 se puede portar “sin más” a 1.12.2.

#### 5) Modificadores y traits completos de la referencia

Hay evidencia de que muchos modifiers ya están registrados y portados:

- `ModProtection`
- `ModTwin`
- `ModSpilling`
- `ModWetting`
- `ModSlurping`
- `ModShattering`
- `ModSlippery`
- `ModPyroclastic`
- `ModChip`
- `ModZooming`

Pero la referencia 1.20.1 tiene más variedad y más integración con el sistema de armadura y herramientas. El port actual parece estar en un estado “funcional y documentado”, no “equivalente final”.

### P2 — Pulido final y revisión del juego

#### 6) Verificación de runtime / gameplay real

Lo que falta ahora no es tanto “código base”, sino:

- testeo del gameplay real
- revisión de recipes de armadura
- validación de tooltips y stats
- validación de JEI / smeltery / foundry / melter en partida
- comprobación de materiales en runtime con mods reales del pack

La compilación da seguridad de que compila, pero no de que todo el gameplay esté terminado.

---

## Prioridad real de trabajo

### P0 — Cerrar la base de gameplay

1. Resolver la armadura / escudos como sistema completo y consistente
2. Verificar la integridad de las estadísticas de armadura y protección
3. Revisar si los modifiers reales de TC3 1.20.1 están presentes y activos

### P1 — Pulir el contenido visible

1. Completar JEI para foundry / melter
2. Sustituir los placeholders de texturas scorched
3. Validar recipes y materiales no portables por compatibilidad

### P2 — Correcciones de balance y estabilidad

1. Revisión de materiales dependientes de mods externos
2. Validar herramientas/armaduras/traits en juego real
3. Determinar qué parte del catálogo no aplica a 1.12 y dejarlo documentado como `N/A`

---

## Conclusión honesta

El proyecto está en un estado mucho mejor que un fork “vacío”:

- compila
- tiene base de materiales, fluidos, smeltery, foundry y armadura registrada
- ya tiene varias piezas muy relevantes del port realizadas

Pero el estado real no es “100% parity con TC3 1.20.1”.

La frase más honesta es:

> Este fork está en un estado funcional de port intermedio: ya tiene la base estructural y muchos sistemas básicos implementados, pero aún faltan varias capas de integración, pulido visual y validación de parity con la referencia de 1.20.1.

---

## Archivos relevantes para seguir revisando

- `docs/port/GUIDE_PORT_GAP_1.20.1.md`
- `docs/port/GUIDE_ARMOR.md`
- `docs/port/ARMOR_PROGRESS.md`
- `docs/port/FOUNDRY_PROGRESS.md`
- `docs/port/GUIDE_MATERIALS.md`
- `src/main/java/slimeknights/tconstruct/tools/armor/TinkerArmor.java`
- `src/main/java/slimeknights/tconstruct/tools/TinkerMaterials.java`
- `src/main/java/slimeknights/tconstruct/foundry/block/BlockScorched.java`
- `src/main/java/slimeknights/tconstruct/foundry/block/BlockMelter.java`
- `src/main/java/slimeknights/tconstruct/tools/modifiers/ModProtection.java`

---

## Resumen corto para el equipo

- ✅ Hay una base sólida que compila
- ✅ Foundry, smeltery, materiales y piezas de armadura existen
- 🟡 Faltan varias capas de parity/notas de juego respecto a TC3 1.20.1
- ❌ No se puede decir que el port esté completo ni 1:1 con la referencia moderna
