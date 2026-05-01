import java.util.Random;
import java.util.Scanner;

/**
 * La Mazmorra Infinita - Aventura Procedural
 * Implementación de menú, matriz de eventos y validación de entrada.
 */
public class MazmorraInfinita {

    // Constantes para la representación de eventos en la matriz (char) [3]
    private static final char USUARIO = 'U';
    private static final char ENEMIGO_COMUN = 'E';
    private static final char COFRE = 'C';
    private static final char LLAVE_MISTICA = 'L';
    private static final char TESORO_SUPREMO = 'T';
    private static final char VACIO = '.';
    private static final char TRAMPA = '#';

    // Propiedades del juego
    private static int vidasIniciales = 3; // Valor por defecto
    private static int tamanoMapa = 6; // Mapa 6x6 por defecto [9]
    private static char[][] mapa;
    private static int filaUsuario, columnaUsuario;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        // Bucle principal para el menú, se repite hasta que el usuario elija "Salir" [9]
        while (!salir) {
            mostrarMenuPrincipal();
            try {
                int opcion = obtenerOpcionValidada(sc, 1, 5); // Opciones 1 a 5

                // Manejo de la opción seleccionada mediante switch [10]
                switch (opcion) {
                    case 1:
                        iniciarNuevaPartida(sc);
                        break;
                    case 2:
                        configuracionJuego(sc);
                        break;
                    case 3:
                        mostrarInstrucciones();
                        break;
                    case 4:
                        mostrarCreditos();
                        break;
                    case 5:
                        System.out.println("Saliendo de la Mazmorra Infinita...");
                        salir = true;
                        break;
                }
            } catch (Exception e) {
                // Manejo de cualquier excepción no controlada en el menú principal
                System.out.println("\n[Error de sistema] Ha ocurrido un error inesperado: " + e.getMessage());
            }
        }
        sc.close();
    }

    // --- Funciones del Menú Principal ---

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== LA MAZMORRA INFINITA ===");
        System.out.println("1. Nueva Partida");
        System.out.println("2. Configuración (Vidas: " + vidasIniciales + ", Tamaño: " + tamanoMapa + "x" + tamanoMapa + ")");
        System.out.println("3. Instrucciones");
        System.out.println("4. Créditos");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Valida la entrada del usuario, maneja excepciones de tipo (NumberFormatException)
     * y lanza una excepción manual si la opción está fuera del rango permitido. [2, 4]
     */
    private static int obtenerOpcionValidada(Scanner sc, int min, int max) throws Exception {

        // El bucle se asegura de que se pida la entrada hasta que sea válida
        while (true) {
            try {
                // Leer la línea completa y hacer parsing (conversión de String a Int) [11, 12]
                String entrada = sc.next();
                int opcion = Integer.parseInt(entrada); // El parsing puede lanzar NumberFormatException [8]

                // Validación de rango
                if (opcion < min || opcion > max) {
                    // Lanzar excepción manualmente para opciones inválidas [4]
                    throw new IllegalArgumentException("Opción inválida. Debe ser entre " + min + " y " + max + ".");
                }
                return opcion;

            } catch (NumberFormatException e) {
                // Captura de entrada inválida (ej. letras en vez de números) [4, 8]
                System.out.println("¡Entrada inválida! Por favor, ingrese un número.");
                sc.nextLine(); // Consumir el resto de la línea para evitar bucles de scanner
            } catch (IllegalArgumentException e) {
                // Captura de la excepción lanzada manualmente por rango inválido
                System.out.println("¡Movimiento no válido! " + e.getMessage());
            }
        }
    }

    // --- Configuración ---

    private static void configuracionJuego(Scanner sc) {
        System.out.println("\n=== CONFIGURACIÓN ===");

        // 1. Número de vidas iniciales [9]
        System.out.println("Vidas (3, 4 o 5): " + vidasIniciales);
        System.out.print("Ingrese nuevas vidas: ");
        try {
            int nuevasVidas = obtenerOpcionValidada(sc, 3, 5);
            vidasIniciales = nuevasVidas;
            System.out.println("Vidas actualizadas a " + vidasIniciales + ".");
        } catch (Exception e) {
            System.out.println("[Error] No se pudo cambiar el número de vidas.");
        }

        // 2. Tamaño del mapa (Simplificado para este ejemplo) [9]
        System.out.println("Tamaño del mapa: (6, 8 o 10): " + tamanoMapa + "x" + tamanoMapa);
        System.out.print("Ingrese nuevo tamaño (6, 8 o 10): ");
        try {
            int nuevoTamano = obtenerOpcionValidada(sc, 6, 10);
            if (nuevoTamano == 6 || nuevoTamano == 8 || nuevoTamano == 10) {
                tamanoMapa = nuevoTamano;
                System.out.println("Tamaño del mapa actualizado a " + tamanoMapa + "x" + tamanoMapa + ".");
            } else {
                throw new IllegalArgumentException("El tamaño debe ser 6, 8 o 10.");
            }
        } catch (Exception e) {
            System.out.println("[Error] No se pudo cambiar el tamaño del mapa: " + e.getMessage());
        }

        // La dificultad se omitirá por brevedad, pero seguiría la misma lógica de validación [9]
    }

    // --- Lógica del Juego ---

    private static void iniciarNuevaPartida(Scanner sc) {
        System.out.println("\n--- INICIANDO PARTIDA ---");
        System.out.println("Generando mazmorra de " + tamanoMapa + "x" + tamanoMapa + "...");

        generarMapa();

        int vidasActuales = vidasIniciales;
        boolean llaveMisticaEncontrada = false;
        boolean finDePartida = false;

        filaUsuario = 0;
        columnaUsuario = 0;
        mapa[filaUsuario][columnaUsuario] = USUARIO;

        // Bucle de turnos del jugador [13]
        while (!finDePartida) {
            mostrarMapa();
            mostrarEstadoJugador(vidasActuales, llaveMisticaEncontrada);

            try {
                int opcionMovimiento = solicitarAccion(sc);

                if (opcionMovimiento >= 1 && opcionMovimiento <= 4) {
                    // Si es un movimiento, intentar mover y manejar el evento
                    moverJugador(opcionMovimiento);
                    char evento = ejecutarEvento(mapa[filaUsuario][columnaUsuario]);

                    if (evento == LLAVE_MISTICA) {
                        llaveMisticaEncontrada = true;
                    } else if (evento == TESORO_SUPREMO) {
                        if (llaveMisticaEncontrada) {
                            System.out.println("\n¡¡VICTORIA!! Has encontrado el Tesoro Supremo con la Llave Mística. 🏆");
                            finDePartida = true;
                        } else {
                            System.out.println("\n¡El Tesoro Supremo! Pero necesitas la Llave Mística para tomarlo. 🔑");
                            // Dejar al usuario en la casilla o reaccionar de otra forma
                        }
                    } else if (evento == ENEMIGO_COMUN || evento == TRAMPA) {
                        // Lógica simplificada de daño
                        vidasActuales--;
                        System.out.println("\n¡PUM! Has recibido daño. Vidas restantes: " + vidasActuales);
                    }

                    // Limpiar la casilla si no es un evento estático o el Tesoro
                    if (evento != TESORO_SUPREMO && evento != VACIO) {
                        mapa[filaUsuario][columnaUsuario] = USUARIO; // La casilla anterior ya fue limpiada en moverJugador
                    }


                } else if (opcionMovimiento == 7) {
                    finDePartida = true; // Abandonar partida [13]
                }

                if (vidasActuales <= 0) {
                    System.out.println("\n¡DERROTA! Has perdido todas tus vidas. 💀");
                    finDePartida = true; // Derrota [14]
                }

            } catch (IllegalArgumentException e) {
                System.out.println("[Error] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Error] Error al procesar el turno: " + e.getMessage());
            }
        }

        // Fin de partida y re-jugabilidad (simplificado) [14]
        System.out.println("\n--- FIN DE LA PARTIDA ---");
    }

    // --- Generación del Mapa ---

    /**
     * Genera la matriz bidimensional con eventos aleatorios. [3]
     */
    private static void generarMapa() {
        Random random = new Random();
        mapa = new char[tamanoMapa][tamanoMapa];

        // Llenar la matriz con casillas vacías ('.')
        for (int i = 0; i < tamanoMapa; i++) {
            for (int j = 0; j < tamanoMapa; j++) {
                mapa[i][j] = VACIO;
            }
        }

        // Definir una lista de eventos posibles (simplificado para este ejemplo)
        char[] eventos = {ENEMIGO_COMUN, ENEMIGO_COMUN, COFRE, COFRE, TRAMPA, VACIO, VACIO, VACIO};

        // Colocar eventos aleatorios (excluyendo el punto de inicio 0,0)
        for (int i = 0; i < tamanoMapa; i++) {
            for (int j = 0; j < tamanoMapa; j++) {
                if (i == 0 && j == 0) continue; // No modificar casilla inicial

                // Asignar un evento aleatorio basado en el array 'eventos'
                int indiceEvento = random.nextInt(eventos.length);
                mapa[i][j] = eventos[indiceEvento];
            }
        }

        // Asegurar la colocación de la Llave Mística y el Tesoro Supremo [3]
        colocarEventoUnico(random, LLAVE_MISTICA);
        colocarEventoUnico(random, TESORO_SUPREMO);
    }

    /**
     * Coloca un evento único en una posición vacía aleatoria. [3]
     */
    private static void colocarEventoUnico(Random random, char evento) {
        int rFila, rColumna;
        do {
            rFila = random.nextInt(tamanoMapa);
            rColumna = random.nextInt(tamanoMapa);
        } while (mapa[rFila][rColumna] != VACIO || (rFila == 0 && rColumna == 0)); // Búsqueda de posición vacía [15]

        mapa[rFila][rColumna] = evento;
    }

    // --- Sistema de Turnos y Movimiento ---

    private static int solicitarAccion(Scanner sc) throws Exception {
        System.out.println("\n=== ACCIONES ===");
        System.out.println("1. Mover arriba | 2. Mover abajo | 3. Mover izquierda | 4. Mover derecha");
        System.out.println("5. Ver estado | 6. Usar objeto | 7. Abandonar partida");
        System.out.print("Seleccione una opción: ");

        return obtenerOpcionValidada(sc, 1, 7);
    }

    private static void moverJugador(int direccion) throws IllegalArgumentException {
        int nuevaFila = filaUsuario;
        int nuevaColumna = columnaUsuario;

        final int limite = tamanoMapa - 1; // Máximo índice permitido

        // 1=Arriba, 2=Abajo, 3=Izquierda, 4=Derecha
        switch (direccion) {
            case 1: // Arriba
                if (filaUsuario == 0) throw new IllegalArgumentException("Movimiento inválido: No puedes salir del mapa por arriba."); // [4, 13]
                nuevaFila--;
                break;
            case 2: // Abajo
                if (filaUsuario == limite) throw new IllegalArgumentException("Movimiento inválido: No puedes salir del mapa por abajo."); // [4, 13]
                nuevaFila++;
                break;
            case 3: // Izquierda
                if (columnaUsuario == 0) throw new IllegalArgumentException("Movimiento inválido: No puedes salir del mapa por izquierda."); // [4, 13]
                nuevaColumna--;
                break;
            case 4: // Derecha
                if (columnaUsuario == limite) throw new IllegalArgumentException("Movimiento inválido: No puedes salir del mapa por derecha."); // [4, 13]
                nuevaColumna++;
                break;
            default:
                // Las opciones 5, 6, 7 son acciones, no movimientos, y ya se manejan fuera.
                return;
        }

        // Limpiar la posición anterior (dejar como VACIO)
        mapa[filaUsuario][columnaUsuario] = VACIO;

        // Actualizar la posición del usuario
        filaUsuario = nuevaFila;
        columnaUsuario = nuevaColumna;

        // Se ejecuta el evento sobre la casilla actual (se manejará fuera)
    }

    private static char ejecutarEvento(char casilla) {
        System.out.print("Has entrado en una casilla de evento: ");

        // Usar switch para determinar el evento (requerimiento 4. Sistema de Eventos) [16, 17]
        switch (casilla) {
            case ENEMIGO_COMUN:
                System.out.println("¡ENEMIGO COMÚN! Prepárate para luchar."); // Daño moderado [16]
                return ENEMIGO_COMUN;
            case COFRE:
                System.out.println("COFRE. Podría contener oro o ser una trampa oculta."); // Lógica de cofre [16]
                return COFRE;
            case LLAVE_MISTICA:
                System.out.println("¡Has encontrado la LLAVE MÍSTICA! Puedes abrir el Tesoro Supremo."); // Llave indispensable [16]
                // Se marca como VACIO o se deja como 'U' dependiendo de la lógica de re-uso
                return LLAVE_MISTICA;
            case TESORO_SUPREMO:
                System.out.println("¡TESORO SUPREMO! Si tienes la llave, ganas."); // Condición de victoria [16]
                return TESORO_SUPREMO;
            case TRAMPA:
                System.out.println("¡TRAMPA! Daño inmediato."); // Daño fijo inmediato [16]
                return TRAMPA;
            case VACIO:
            default:
                System.out.println("Casilla vacía. Puedes descansar un turno.");
                return VACIO;
        }
    }

    // --- Utilidades ---

    private static void mostrarMapa() {
        System.out.println("\n========== MAPA (" + tamanoMapa + "x" + tamanoMapa + ") ==========");
        for (int i = 0; i < tamanoMapa; i++) {
            for (int j = 0; j < tamanoMapa; j++) {
                System.out.print(mapa[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void mostrarEstadoJugador(int vidas, boolean llave) {
        System.out.println("\n--- ESTADO ---");
        System.out.println("Vidas: " + vidas + "/" + vidasIniciales);
        System.out.println("Llave Mística: " + (llave ? "SÍ" : "NO"));
        System.out.println("Posición: [" + filaUsuario + ", " + columnaUsuario + "]");
        System.out.println("U=" + USUARIO + " | E=" + ENEMIGO_COMUN + " | C=" + COFRE + " | L=" + LLAVE_MISTICA + " | T=" + TESORO_SUPREMO + " | #=" + TRAMPA);
    }

    private static void mostrarInstrucciones() {
        System.out.println("\n--- INSTRUCCIONES ---");
        System.out.println("Explora la mazmorra (" + tamanoMapa + "x" + tamanoMapa + ") para encontrar el Tesoro Supremo (T).");
        System.out.println("Debes obtener la Llave Mística (L) antes de alcanzar el Tesoro.");
        System.out.println("Cuidado con Enemigos (E) y Trampas (#).");
    }

    private static void mostrarCreditos() {
        System.out.println("\n--- CRÉDITOS ---");
        System.out.println("Desarrollado por Adrián.");
    }
}