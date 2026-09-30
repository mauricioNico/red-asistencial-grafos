# Red asistencial: BFS y DFS recursivo

Proyecto Maven para construir interactivamente un grafo ponderado no dirigido
mediante su matriz de adyacencia y ejecutar BFS y DFS recursivo sobre el mismo
origen.

## Organización

- `App`: carga los vértices y la matriz ponderada, muestra la lista de
  adyacencia y presenta ambos recorridos.
- `GrafoPonderadoNoDirigido`: estructura, validaciones, BFS y DFS.
- `ResultadoBfs`: recorrido, niveles y aristas de BFS.
- `ResultadoDfs`: recorrido y aristas de DFS.
- `AristaRecorrido`: arista utilizada para descubrir un vértice.
- `GrafoPonderadoNoDirigidoTest`: pruebas de caja negra de ambos algoritmos.

Los pesos representan minutos de traslado. Permanecen almacenados, pero BFS y
DFS no los utilizan para decidir el recorrido.

Durante la carga se ingresa solamente la mitad superior de la matriz. Un valor
`0` representa ausencia de conexión y un valor positivo representa los minutos.
La diagonal y la mitad simétrica se completan automáticamente.

## Compilar y probar

```bash
mvn clean package
```

## Ejecutar

```bash
java -jar target/red-asistencial-grafos-clase1609.jar
```

En Windows también puede utilizarse `ejecutar.bat`.
