# Red asistencial — Algoritmos de grafos

Aplicación didáctica de **Algoritmos y Estructuras de Datos III** para trabajar
sobre una misma red ponderada no dirigida y comparar **BFS, DFS, Dijkstra y Prim**.

## Estructura del repositorio

```text
.
├── java/                       # Proyecto Maven / Java 17
│   ├── pom.xml
│   ├── ejecutar.bat
│   └── src/
├── simulador/
│   └── index.html              # Simulador interactivo HTML/CSS/JavaScript
├── .github/workflows/java-ci.yml
└── README.md
```

Los archivos generados por Maven (`target/`, clases y JAR) no se versionan.

## Qué resuelve cada algoritmo

| Algoritmo | Problema principal | Usa pesos |
|---|---|---:|
| BFS | Recorrido por niveles; camino con menor cantidad de aristas en grafos no ponderados | No |
| DFS | Exploración profunda, componentes, ciclos y recursividad | No |
| Dijkstra | Camino de menor costo desde un origen | Sí |
| Prim | Árbol de expansión mínima: conectar toda la red con menor costo total | Sí |

## Red asistencial de referencia

Vértices: `CC`, `CN`, `CS`, `DI`, `G`, `L`.

Conexiones:

```text
CC--CN 12
CC--L   5
CC--G   4
CN--L   8
CN--DI 15
L--DI   7
L--CS  10
DI--CS  6
G--CS   9
```

Desde **Guardia (G)**:

- Dijkstra hacia Diagnóstico: `G → CS → DI`, costo **15**.
- Prim construye el MST:
  `G–CC (4)`, `CC–L (5)`, `L–DI (7)`, `DI–CS (6)`,
  `L–CN (8)`; costo total **30**.

La comparación es intencional: **el MST no reemplaza a Dijkstra**. Prim minimiza
el costo de la red completa; Dijkstra minimiza un camino desde un origen.

## Proyecto Java

Requisitos: JDK 17 y Maven.

```bash
cd java
mvn clean test
mvn package
java -jar target/red-asistencial-grafos.jar
```

En Windows también se puede ejecutar `java/ejecutar.bat`.

La clase `GrafoPonderadoNoDirigido` contiene los cuatro algoritmos. Prim
devuelve un `ResultadoPrim`; si el grafo está desconectado, informa que el
árbol obtenido cubre sólo la componente alcanzable.

## Simulador web

Abrir:

```text
simulador/index.html
```

No necesita servidor ni dependencias externas. Permite cargar grafos propios,
elegir origen y recorrer paso a paso BFS, DFS, Dijkstra o Prim. El ejemplo
**Red asistencial · Prim desde Guardia** reproduce el MST usado en clase.

## Rama principal

`main` es la rama de trabajo canónica. La rama `master` se mantiene alineada
por compatibilidad con la primera carga del proyecto.
