# Reglas del proyecto — TinkersAntique (fork Tinkers' Construct 1.12.2)

Reglas obligatorias para todo trabajo sobre este código. Si una regla contradice
una instrucción puntual, acláralo antes de proceder.

## Principios de diseño (SIEMPRE)

1. **SOLID**: todo código nuevo o refactorizado debe respetar los cinco principios
   (SRP, OCP, LSP, ISP, DIP). Cada clase tiene UNA responsabilidad clara.
2. **Evitar clases dios**: ninguna clase debe acumular responsabilidades de varios
   dominios. Si una clase supera ~400-500 líneas de lógica o mezcla dominios
   (registro + datos + lógica), divídela por responsabilidad.
3. **Evitar clases dentro de otras clases**: no crear clases, interfaces ni enums
   anidadas. Extraerlas a archivos top-level en el paquete correspondiente.
4. **No duplicar código**: extraer a helpers/utilities reutilizables en vez de
   copiar y pegar. Preferir composición sobre herencia cuando aplique.

## Convenciones del refactor (patrón ya establecido en el repo)

- **Preservar la API pública**: cuando se divide una clase muy usada (p. ej.
  `TinkerRegistry`, `ToolHelper`), dejar la clase original como **fachada** con
  delegados delgados a las nuevas clases especializadas. Los call sites NO cambian.
- **Paquete igual para minimizar fricción**: al extraer, preferir el mismo paquete
  de la clase original salvo que exista un subpaquete más natural.
- **`import static`** para mover código que referencia muchos campos estáticos de
  la clase original (ej. `import static ...TinkerRegistry.log;`).
- **Métodos públicos usados externamente**: quedan como delegados; los métodos
  privados/package-private internos se mueven con su lógica.
- **No romper el build**: tras cada cambio ejecutar:
  ```powershell
  .\gradlew.bat build --offline --console=plain
  ```
- **Validación runtime**: si el cambio toca registro/inicialización (pulsos,
  recetas, config, modelos), validar con `gradlew runClient` (debe cargar el mundo
  sin crash).

## Buenas prácticas generales

- Respetar el estilo existente (2 espacios, llaves en la misma línea, sin
  comentarios innecesarios; los comentarios existentes en español/inglés se
  conservan).
- Los archivos fuente del repo están en **UTF-8**: leer/escribir Java con encoding
  UTF-8 explícito (no usar `Get-Content`/`Set-Content` de PowerShell 5.1, que usan
  Cp1252 y corrompen acentos/em-dashes).
- No commitear sin que `git status` esté limpio de cambios accidentales (logs,
  `run/`, `build/` están en `.gitignore`).
- No tocar `TinkersConstruct-1.20.1/` (referencia de lectura, read-only).