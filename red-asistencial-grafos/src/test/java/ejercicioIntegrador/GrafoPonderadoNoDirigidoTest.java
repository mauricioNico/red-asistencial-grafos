package ejercicioIntegrador;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de caja negra para:
 *
 * - BFS
 * - DFS recursivo
 * - Dijkstra
 *
 * Todos los algoritmos trabajan sobre
 * el mismo grafo ponderado no dirigido.
 */
class GrafoPonderadoNoDirigidoTest {

    private GrafoPonderadoNoDirigido grafo;

    /**
     * Antes de cada prueba se construye
     * nuevamente la red asistencial.
     */
    @BeforeEach
    void prepararRed() {

        grafo = new GrafoPonderadoNoDirigido();

        /*
         * Vértices de la red.
         */
        grafo.agregarVertice("CC");
        grafo.agregarVertice("CN");
        grafo.agregarVertice("CS");
        grafo.agregarVertice("DI");
        grafo.agregarVertice("G");
        grafo.agregarVertice("L");

        /*
         * Conexiones ponderadas.
         *
         * Los pesos representan minutos
         * de traslado.
         */
        grafo.agregarArista("CC", "CN", 12);
        grafo.agregarArista("CC", "L", 5);
        grafo.agregarArista("CC", "G", 4);

        grafo.agregarArista("CN", "L", 8);
        grafo.agregarArista("CN", "DI", 15);

        grafo.agregarArista("L", "DI", 7);
        grafo.agregarArista("L", "CS", 10);

        grafo.agregarArista("DI", "CS", 6);

        grafo.agregarArista("G", "CS", 9);
    }

    // =========================================================
    // BFS
    // =========================================================

    @Test
    void bfsDesdeGuardiaDevuelveOrdenNivelesYAristas() {

        ResultadoBfs resultado =
                grafo.recorrerEnAnchura("G");

        assertAll(

                () -> assertEquals(

                        List.of(
                                "G",
                                "CC",
                                "CS",
                                "CN",
                                "L",
                                "DI"
                        ),

                        resultado.getRecorrido()
                ),

                () -> assertEquals(

                        List.of(

                                List.of("G"),

                                List.of(
                                        "CC",
                                        "CS"
                                ),

                                List.of(
                                        "CN",
                                        "L",
                                        "DI"
                                )
                        ),

                        resultado.getNiveles()
                ),

                () -> assertEquals(

                        List.of(

                                new AristaRecorrido(
                                        "G",
                                        "CC"
                                ),

                                new AristaRecorrido(
                                        "G",
                                        "CS"
                                ),

                                new AristaRecorrido(
                                        "CC",
                                        "CN"
                                ),

                                new AristaRecorrido(
                                        "CC",
                                        "L"
                                ),

                                new AristaRecorrido(
                                        "CS",
                                        "DI"
                                )
                        ),

                        resultado.getAristas()
                )
        );
    }

    // =========================================================
    // DFS
    // =========================================================

    @Test
    void dfsDesdeGuardiaDevuelveOrdenYAristas() {

        ResultadoDfs resultado =
                grafo.recorrerEnProfundidad("G");

        assertAll(

                () -> assertEquals(

                        List.of(
                                "G",
                                "CC",
                                "CN",
                                "DI",
                                "CS",
                                "L"
                        ),

                        resultado.getRecorrido()
                ),

                () -> assertEquals(

                        List.of(

                                new AristaRecorrido(
                                        "G",
                                        "CC"
                                ),

                                new AristaRecorrido(
                                        "CC",
                                        "CN"
                                ),

                                new AristaRecorrido(
                                        "CN",
                                        "DI"
                                ),

                                new AristaRecorrido(
                                        "DI",
                                        "CS"
                                ),

                                new AristaRecorrido(
                                        "CS",
                                        "L"
                                )
                        ),

                        resultado.getAristas()
                )
        );
    }

    // =========================================================
    // COMPARACIÓN BFS / DFS
    // =========================================================

    @Test
    void bfsYDfsAlcanzanLosMismosVertices() {

        ResultadoBfs bfs =
                grafo.recorrerEnAnchura("CC");

        ResultadoDfs dfs =
                grafo.recorrerEnProfundidad("CC");

        assertEquals(

                new TreeSet<>(
                        bfs.getRecorrido()
                ),

                new TreeSet<>(
                        dfs.getRecorrido()
                )
        );
    }

    // =========================================================
    // DIJKSTRA
    // =========================================================

    /**
     * Verifica las distancias mínimas calculadas
     * desde Guardia.
     */
    @Test
    void dijkstraDesdeGuardiaCalculaDistanciasMinimas() {

        ResultadoDijkstra resultado =
                grafo.dijkstra("G");

        Map<String, Integer> distanciasEsperadas =
                Map.of(
                        "G", 0,
                        "CC", 4,
                        "CS", 9,
                        "L", 9,
                        "DI", 15,
                        "CN", 16
                );

        assertEquals(
                distanciasEsperadas,
                resultado.getDistancias()
        );
    }

    /**
     * Verifica los predecesores utilizados
     * para construir los caminos mínimos.
     */
    @Test
    void dijkstraDesdeGuardiaCalculaPredecesores() {

        ResultadoDijkstra resultado =
                grafo.dijkstra("G");

        Map<String, String> predecesoresEsperados =
                Map.of(
                        "CC", "G",
                        "CS", "G",
                        "L", "CC",
                        "DI", "CS",
                        "CN", "CC"
                );

        assertEquals(
                predecesoresEsperados,
                resultado.getPredecesores()
        );
    }

    /**
     * Verifica la reconstrucción del camino
     * mínimo desde Guardia hasta Diagnóstico.
     *
     * G -> CS -> DI
     *
     * 9 + 6 = 15 minutos
     */
    @Test
    void dijkstraReconstruyeCaminoMinimoDeGuardiaADiagnostico() {

        ResultadoDijkstra resultado =
                grafo.dijkstra("G");

        assertAll(

                () -> assertEquals(

                        List.of(
                                "G",
                                "CS",
                                "DI"
                        ),

                        resultado.reconstruirCamino(
                                "DI"
                        )
                ),

                () -> assertEquals(

                        15,

                        resultado.getDistancia(
                                "DI"
                        )
                )
        );
    }

    /**
     * Verifica otro camino para demostrar
     * que Dijkstra puede consultar distintos
     * destinos después de una única ejecución.
     *
     * G -> CC -> CN
     *
     * 4 + 12 = 16 minutos
     */
    @Test
    void dijkstraReconstruyeCaminoDeGuardiaACentroNorte() {

        ResultadoDijkstra resultado =
                grafo.dijkstra("G");

        assertAll(

                () -> assertEquals(

                        List.of(
                                "G",
                                "CC",
                                "CN"
                        ),

                        resultado.reconstruirCamino(
                                "CN"
                        )
                ),

                () -> assertEquals(

                        16,

                        resultado.getDistancia(
                                "CN"
                        )
                )
        );
    }

    // =========================================================
    // ORIGEN INEXISTENTE
    // =========================================================

    @Test
    void losTresAlgoritmosRechazanUnOrigenInexistente() {

        assertAll(

                () -> assertThrows(

                        IllegalArgumentException.class,

                        () -> grafo.recorrerEnAnchura(
                                "OESTE"
                        )
                ),

                () -> assertThrows(

                        IllegalArgumentException.class,

                        () -> grafo.recorrerEnProfundidad(
                                "OESTE"
                        )
                ),

                () -> assertThrows(

                        IllegalArgumentException.class,

                        () -> grafo.dijkstra(
                                "OESTE"
                        )
                )
        );
    }

    // =========================================================
    // VÉRTICE AISLADO
    // =========================================================

    @Test
    void losTresAlgoritmosFuncionanConUnVerticeAislado() {

        grafo.agregarVertice("CO");

        ResultadoBfs bfs =
                grafo.recorrerEnAnchura("CO");

        ResultadoDfs dfs =
                grafo.recorrerEnProfundidad("CO");

        ResultadoDijkstra dijkstra =
                grafo.dijkstra("CO");

        assertAll(

                /*
                 * BFS
                 */
                () -> assertEquals(

                        List.of("CO"),

                        bfs.getRecorrido()
                ),

                () -> assertEquals(

                        List.of(
                                List.of("CO")
                        ),

                        bfs.getNiveles()
                ),

                () -> assertEquals(

                        List.of(),

                        bfs.getAristas()
                ),

                /*
                 * DFS
                 */
                () -> assertEquals(

                        List.of("CO"),

                        dfs.getRecorrido()
                ),

                () -> assertEquals(

                        List.of(),

                        dfs.getAristas()
                ),

                /*
                 * Dijkstra
                 */
                () -> assertEquals(

                        0,

                        dijkstra.getDistancia(
                                "CO"
                        )
                ),

                () -> assertEquals(

                        List.of("CO"),

                        dijkstra.reconstruirCamino(
                                "CO"
                        )
                ),

                /*
                 * Desde CO no existe camino
                 * hasta CC.
                 */
                () -> assertEquals(

                        List.of(),

                        dijkstra.reconstruirCamino(
                                "CC"
                        )
                )
        );
    }

    // =========================================================
    // INDEPENDENCIA ENTRE EJECUCIONES
    // =========================================================

    @Test
    void lasEjecucionesNoConservanEstadoAnterior() {

        /*
         * Primera ejecución desde CC.
         */
        ResultadoBfs primerBfs =
                grafo.recorrerEnAnchura("CC");

        ResultadoDfs primerDfs =
                grafo.recorrerEnProfundidad("CC");

        ResultadoDijkstra primerDijkstra =
                grafo.dijkstra("CC");

        /*
         * Ejecutamos los algoritmos
         * desde otro vértice.
         */
        grafo.recorrerEnAnchura("G");

        grafo.recorrerEnProfundidad("G");

        grafo.dijkstra("G");

        /*
         * Volvemos a ejecutar desde CC.
         */
        ResultadoBfs segundoBfs =
                grafo.recorrerEnAnchura("CC");

        ResultadoDfs segundoDfs =
                grafo.recorrerEnProfundidad("CC");

        ResultadoDijkstra segundoDijkstra =
                grafo.dijkstra("CC");

        assertAll(

                () -> assertEquals(

                        primerBfs.getRecorrido(),

                        segundoBfs.getRecorrido()
                ),

                () -> assertEquals(

                        primerDfs.getRecorrido(),

                        segundoDfs.getRecorrido()
                ),

                () -> assertEquals(

                        primerDijkstra.getDistancias(),

                        segundoDijkstra.getDistancias()
                ),

                () -> assertEquals(

                        primerDijkstra.getPredecesores(),

                        segundoDijkstra.getPredecesores()
                )
        );
    }
}
