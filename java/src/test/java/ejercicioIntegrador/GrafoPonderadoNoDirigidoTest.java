package ejercicioIntegrador;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
 * Pruebas de caja negra para BFS, DFS, Dijkstra, Prim, Kruskal
 * y Floyd-Warshall sobre la misma red asistencial ponderada no dirigida.
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

                () -> assertTrue(
                        resultado.esArbolExpansionCompleto())
        );
    }

    @Test
    void kruskalConstruyeElMstEsperadoSinOrigen() {

        ResultadoKruskal resultado = grafo.kruskal();

        assertAll(
                () -> assertEquals(
                        List.of(
                                new AristaPonderada("CC", "G", 4),
                                new AristaPonderada("CC", "L", 5),
                                new AristaPonderada("CS", "DI", 6),
                                new AristaPonderada("DI", "L", 7),
                                new AristaPonderada("CN", "L", 8)),
                        resultado.getAristas()),

                () -> assertEquals(
                        30,
                        resultado.getCostoTotal()),

                () -> assertTrue(
                        resultado.esArbolExpansionCompleto())
        );
    }

    @Test
    void floydWarshallCalculaDistanciasMinimasEntreTodosLosPares() {

        ResultadoFloydWarshall resultado =
                grafo.floydWarshall();

        int[][] esperada = {
                {0, 12, 13, 12, 4, 5},
                {12, 0, 18, 15, 16, 8},
                {13, 18, 0, 6, 9, 10},
                {12, 15, 6, 0, 15, 7},
                {4, 16, 9, 15, 0, 9},
                {5, 8, 10, 7, 9, 0}
        };

        assertAll(
                () -> assertEquals(
                        List.of("CC", "CN", "CS", "DI", "G", "L"),
                        resultado.getVertices()),

                () -> assertArrayEquals(
                        esperada,
                        resultado.getDistancias()),

                () -> assertEquals(
                        15,
                        resultado.getDistancia("G", "DI")),

                () -> assertEquals(
                        9,
                        resultado.getDistancia("G", "L"))
        );
    }

    @Test
    void floydWarshallReconstruyeCaminosMinimos() {

        ResultadoFloydWarshall resultado =
                grafo.floydWarshall();

        assertAll(
                () -> assertEquals(
                        List.of("G", "CS", "DI"),
                        resultado.reconstruirCamino("G", "DI")),

                () -> assertEquals(
                        List.of("G", "CC", "L"),
                        resultado.reconstruirCamino("G", "L")),

                () -> assertEquals(
                        List.of("CC", "L", "DI"),
                        resultado.reconstruirCamino("CC", "DI"))
        );
    }

    @Test
    void algoritmosConOrigenRechazanUnVerticeInexistente() {

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
    void algoritmosFuncionanConUnVerticeAislado() {

        grafo.agregarVertice("CO");

        ResultadoBfs bfs =
                grafo.recorrerEnAnchura("CO");
        ResultadoDfs dfs =
                grafo.recorrerEnProfundidad("CO");
        ResultadoDijkstra dijkstra =
                grafo.dijkstra("CO");
        ResultadoPrim prim =
                grafo.prim("CO");
        ResultadoKruskal kruskal =
                grafo.kruskal();
        ResultadoFloydWarshall floyd =
                grafo.floydWarshall();

        assertAll(
                () -> assertEquals(
                        List.of("CO"),
                        bfs.getRecorrido()),
                () -> assertEquals(
                        List.of("CO"),
                        dfs.getRecorrido()),
                () -> assertEquals(
                        0,
                        dijkstra.getDistancia("CO")),
                () -> assertEquals(
                        List.of("CO"),
                        dijkstra.reconstruirCamino("CO")),
                () -> assertEquals(
                        0,
                        prim.getCostoTotal()),
                () -> assertFalse(
                        prim.esArbolExpansionCompleto()),
                () -> assertFalse(
                        kruskal.esArbolExpansionCompleto()),
                () -> assertEquals(
                        0,
                        floyd.getDistancia("CO", "CO")),
                () -> assertFalse(
                        floyd.existeCamino("CO", "CC")),
                () -> assertEquals(
                        List.of(),
                        floyd.reconstruirCamino("CO", "CC"))
        );
    }
}
