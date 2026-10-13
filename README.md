# Red asistencial — Algoritmos de grafos

Aplicación didáctica de **Algoritmos y Estructuras de Datos III** para trabajar
sobre una misma red ponderada no dirigida y comparar **BFS, DFS, Dijkstra,
Prim, Kruskal y Floyd-Warshall**.

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
| Floyd-Warshall | Caminos mínimos entre todos los pares de vértices | Sí |
| Prim | Árbol de expansión mínima creciendo desde un vértice inicial | Sí |
| Kruskal | Árbol de expansión mínima ordenando aristas globalmente y evitando ciclos | Sí |

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
- Floyd-Warshall devuelve una matriz completa de tiempos mínimos entre todos
  los pares; en esa matriz, `G → DI` también vale **15**.
- Prim construye el MST desde un origen:
  `G–CC (4)`, `CC–L (5)`, `L–DI (7)`, `DI–CS (6)`,
  `L–CN (8)`; costo total **30**.
- Kruskal obtiene el mismo costo total **30**, pero no depende de un vértice:
  ordena todas las aristas y acepta sólo las que no forman ciclos.

La comparación es intencional: **el MST no reemplaza a los caminos mínimos**.
Dijkstra minimiza un camino desde un origen; Floyd-Warshall calcula los caminos
mínimos entre todos los pares; Prim y Kruskal minimizan el costo total de
conectar la red.

## Proyecto Java

Requisitos: JDK 17 y Maven.

```bash
cd java
mvn clean test
mvn package
java -jar target/red-asistencial-grafos.jar
```

En Windows también se puede ejecutar `java/ejecutar.bat`.

La clase `GrafoPonderadoNoDirigido` contiene los seis algoritmos. Prim devuelve
un `ResultadoPrim`; Kruskal devuelve un `ResultadoKruskal` y utiliza `UnionFind`
para detectar si una arista produciría un ciclo; Floyd-Warshall devuelve un
`ResultadoFloydWarshall` con matriz de distancias y reconstrucción de caminos.
Si el grafo está desconectado, los algoritmos de conexión informan que no se
obtuvo un árbol de expansión completo, y Floyd-Warshall conserva infinito para
los pares sin camino.

## Simulador web

Abrir:

```text
simulador/index.html
```

No necesita servidor ni dependencias externas. Permite cargar grafos propios,
elegir algoritmo y comparar BFS, DFS, Dijkstra, Prim, Kruskal y Floyd-Warshall.
Para cada algoritmo muestra un bloque de código didáctico y una ejecución paso
a paso. En Floyd-Warshall incluye el ejemplo **A-B-C-D** de cuatro vértices y
permite avanzar por las **64 comparaciones del triple `for`**, observando `k`,
`i`, `j`, la matriz actual y las actualizaciones reales.

El ejemplo **Red asistencial** reproduce la red utilizada en clase, la variante
**Kruskal con ciclo** permite ver cómo Union-Find rechaza una arista que cerraría
un ciclo, y el ejemplo **Floyd manual A-B-C-D** muestra la lógica completa de
`dist[i][j]` contra `dist[i][k] + dist[k][j]`.

## Rama principal

`main` es la rama de trabajo canónica. La rama `master` se mantiene alineada
por compatibilidad con la primera carga del proyecto.
