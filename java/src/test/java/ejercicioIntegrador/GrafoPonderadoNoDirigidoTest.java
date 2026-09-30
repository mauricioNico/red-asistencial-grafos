package ejercicioIntegrador;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de caja negra para BFS, DFS, Dijkstra y Prim
 * sobre la misma red asistencial ponderada no dirigida.
 */
class GrafoPonderadoNoDirigidoTest {

    private GrafoPonderadoNoDirigido grafo;

    @BeforeEach
    void prepararRed() {

        grafo = new GrafoPonderadoNoDirigido();

        grafo.agregarVertice("CC");
        grafo.agregarVertice("CN");
        grafo.agregarVertice("CS");
        grafo.agregarVertice("DI");
        grafo.agregarVertice("G");
        grafo.agregarVertice("L");

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

    @Test
    void bfsDesdeGuardiaDevuelveOrdenNivelesYAristas() {

        ResultadoBfs resultado =
                grafo.recorrerEnAnchura("G");

        assertAll(
                () -> assertEquals(
                        List.of(
                                "G", "CC", "CS",
                                "CN", "L", "DI"),
                        resultado.getRecorrido()),

                () -> assertEquals(
                        List.of(
                                List.of("G"),
                                List.of("CC", "CS"),
                                List.of("CN", "L", "DI")),
                        resultado.getNiveles()),

                () -> assertEquals(
                        List.of(
                                new AristaRecorrido("G", "CC"),
                                new AristaRecorrido("G", "CS"),
                                new AristaRecorrido("CC", "CN"),
                                new AristaRecorrido("CC", "L"),
                                new AristaRecorrido("CS", "DI")),
                        resultado.getAristas())
        );
    }

    @Test
    void dfsDesdeGuardiaDevuelveOrdenYAristas() {

        ResultadoDfs resultado =
                grafo.recorrerEnProfundidad("G");

        assertAll(
                () -> assertEquals(
                        List.of(
                                "G", "CC", "CN",
                                "DI", "CS", "L"),
                        resultado.getRecorrido()),

                () -> assertEquals(
                        List.of(
                                new AristaRecorrido("G", "CC"),
                                new AristaRecorrido("CC", "CN"),
                                new AristaRecorrido("CN", "DI"),
                                new AristaRecorrido("DI", "CS"),
                                new AristaRecorrido("CS", "L")),
                        resultado.getAristas())
        );
    }

    @Test
    void bfsYDfsAlcanzanLosMismosVertices() {

        ResultadoBfs bfs =
                grafo.recorrerEnAnchura("CC");

        ResultadoDfs dfs =
                grafo.recorrerEnProfundidad("CC");

        assertEquals(
                new TreeSet<>(bfs.getRecorrido()),
                new TreeSet<>(dfs.getRecorrido()));
    }

    @Test
    void dijkstraDesdeGuardiaCalculaDistanciasMinimas() {

        ResultadoDijkstra resultado =
                grafo.dijkstra("G");

        Map<String, Integer> esperadas =
                Map.of(
                        "G", 0,
                        "CC", 4,
                        "CS", 9,
                        "L", 9,
                        "DI", 15,
                        "CN", 16);

        assertEquals(
                esperadas,
                resultado.getDistancias());
    }

    @Test
    void dijkstraDesdeGuardiaCalculaPredecesores() {

        ResultadoDijkstra resultado =
                grafo.dijkstra("G");

        assertEquals(
                Map.of(
                        "CC", "G",
                        "CS", "G",
                        "L", "CC",
                        "DI", "CS",
                        "CN", "CC"),
                resultado.getPredecesores());
    }

    @Test
    void dijkstraReconstruyeCaminoMinimoDeGuardiaADiagnostico() {

        ResultadoDijkstra resultado =
                grafo.dijkstra("G");

        assertAll(
                () -> assertEquals(
                        List.of("G", "CS", "DI"),
                        resultado.reconstruirCamino("DI")),
                () -> assertEquals(
                        15,
                        resultado.getDistancia("DI"))
        );
    }

    @Test
    void primDesdeGuardiaConstruyeElMstEsperado() {

        ResultadoPrim resultado =
                grafo.prim("G");

        assertAll(
                () -> assertEquals(
                        List.of(
                                new AristaPonderada("G", "CC", 4),
                                new AristaPonderada("CC", "L", 5),
                                new AristaPonderada("L", "DI", 7),
                                new AristaPonderada("DI", "CS", 6),
                                new AristaPonderada("L", "CN", 8)),
                        resultado.getAristas()),

                () -> assertEquals(
                        30,
                        resultado.getCostoTotal()),

                () -> assertEquals(
                        5,
                        resultado.getAristas().size()),

                () -> assertTrue(
                        resultado.esArbolExpansionCompleto())
        );
    }

    @Test
    void primYDijkstraResuelvenProblemasDistintos() {

        ResultadoDijkstra dijkstra =
                grafo.dijkstra("G");

        ResultadoPrim prim =
                grafo.prim("G");

        assertAll(
                () -> assertEquals(
                        List.of("G", "CS", "DI"),
                        dijkstra.reconstruirCamino("DI")),
                () -> assertEquals(
                        15,
                        dijkstra.getDistancia("DI")),
                () -> assertEquals(
                        30,
                        prim.getCostoTotal())
        );
    }

    @Test
    void losCuatroAlgoritmosRechazanUnOrigenInexistente() {

        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> grafo.recorrerEnAnchura("OESTE")),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> grafo.recorrerEnProfundidad("OESTE")),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> grafo.dijkstra("OESTE")),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> grafo.prim("OESTE"))
        );
    }

    @Test
    void losCuatroAlgoritmosFuncionanConUnVerticeAislado() {

        grafo.agregarVertice("CO");

        ResultadoBfs bfs =
                grafo.recorrerEnAnchura("CO");
        ResultadoDfs dfs =
                grafo.recorrerEnProfundidad("CO");
        ResultadoDijkstra dijkstra =
                grafo.dijkstra("CO");
        ResultadoPrim prim =
                grafo.prim("CO");

        assertAll(
                () -> assertEquals(
                        List.of("CO"),
                        bfs.getRecorrido()),
                () -> assertEquals(
                        List.of(List.of("CO")),
                        bfs.getNiveles()),
                () -> assertEquals(
                        List.of(),
                        bfs.getAristas()),

                () -> assertEquals(
                        List.of("CO"),
                        dfs.getRecorrido()),
                () -> assertEquals(
                        List.of(),
                        dfs.getAristas()),

                () -> assertEquals(
                        0,
                        dijkstra.getDistancia("CO")),
                () -> assertEquals(
                        List.of("CO"),
                        dijkstra.reconstruirCamino("CO")),
                () -> assertEquals(
                        List.of(),
                        dijkstra.reconstruirCamino("CC")),

                () -> assertEquals(
                        0,
                        prim.getCostoTotal()),
                () -> assertEquals(
                        List.of(),
                        prim.getAristas()),
                () -> assertFalse(
                        prim.esArbolExpansionCompleto())
        );
    }

    @Test
    void primDetectaQueUnGrafoConAisladoNoTieneMstCompleto() {

        grafo.agregarVertice("CO");

        ResultadoPrim resultado =
                grafo.prim("G");

        assertAll(
                () -> assertEquals(
                        30,
                        resultado.getCostoTotal()),
                () -> assertEquals(
                        6,
                        resultado.getCantidadVerticesAlcanzados()),
                () -> assertEquals(
                        7,
                        resultado.getCantidadVerticesTotales()),
                () -> assertFalse(
                        resultado.esArbolExpansionCompleto())
        );
    }

    @Test
    void lasEjecucionesNoConservanEstadoAnterior() {

        ResultadoBfs primerBfs =
                grafo.recorrerEnAnchura("CC");
        ResultadoDfs primerDfs =
                grafo.recorrerEnProfundidad("CC");
        ResultadoDijkstra primerDijkstra =
                grafo.dijkstra("CC");
        ResultadoPrim primerPrim =
                grafo.prim("CC");

        grafo.recorrerEnAnchura("G");
        grafo.recorrerEnProfundidad("G");
        grafo.dijkstra("G");
        grafo.prim("G");

        ResultadoBfs segundoBfs =
                grafo.recorrerEnAnchura("CC");
        ResultadoDfs segundoDfs =
                grafo.recorrerEnProfundidad("CC");
        ResultadoDijkstra segundoDijkstra =
                grafo.dijkstra("CC");
        ResultadoPrim segundoPrim =
                grafo.prim("CC");

        assertAll(
                () -> assertEquals(
                        primerBfs.getRecorrido(),
                        segundoBfs.getRecorrido()),
                () -> assertEquals(
                        primerDfs.getRecorrido(),
                        segundoDfs.getRecorrido()),
                () -> assertEquals(
                        primerDijkstra.getDistancias(),
                        segundoDijkstra.getDistancias()),
                () -> assertEquals(
                        primerDijkstra.getPredecesores(),
                        segundoDijkstra.getPredecesores()),
                () -> assertEquals(
                        primerPrim.getAristas(),
                        segundoPrim.getAristas()),
                () -> assertEquals(
                        primerPrim.getCostoTotal(),
                        segundoPrim.getCostoTotal())
        );
    }
}
