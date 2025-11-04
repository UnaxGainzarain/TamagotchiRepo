package org.cuatrovientos.unax.tamagotchi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Cuidador {
	private static List<Tamagotchi> tamagotchiList = new ArrayList<>();
	private static List<Thread> threadList = new ArrayList<>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		Random rand = new Random();
		String leer;
		int eatSpeed = 1;

		System.out.println("¿Cuántos Tamagotchis quieres cuidar (X)?");
		int numTamagotchis = 0;
		try {
			leer = sc.nextLine();
			numTamagotchis = Integer.parseInt(leer);
			if (numTamagotchis <= 0) {
				System.out.println("Debes crear al menos uno.");
				sc.close();
				return; // Salir
			}
		} catch (NumberFormatException e) {
			System.out.println("Número inválido. Saliendo.");
			sc.close();
			return; // Salir
		}

		// (R1) "Por lo tanto al arrancarse deberá ponerles las pilas y lanzarlos"
		for (int i = 0; i < numTamagotchis; i++) {
			System.out.println("--- Creando Tamagotchi " + (i + 1) + "/" + numTamagotchis + " ---");
			eatSpeed = rand.nextInt(5001) + 3000;

			System.out.println("Indica el nombre del Tamagotchi:");
			String nombre = sc.nextLine();

			// Comprobamos si el nombre existe
			boolean existe = false;
			for (Tamagotchi t : tamagotchiList) {
				if (t.getTamagotchiName().equals(nombre)) {
					existe = true;
					break;
				}
			}

			if (existe) {
				System.out.println("El nombre está ocupado. Inténtalo de nuevo.");
				i--; // <<< Importante: Repite esta iteración
				continue; // <<< Salta al siguiente ciclo del 'for'
			}

			// Si no existe, lo creamos y lanzamos
			Tamagotchi t = new Tamagotchi(nombre, eatSpeed);
			Thread tThread = new Thread(t);
			tThread.setName(nombre); // VINCULAMOS POR NOMBRE (para la func. kill)

			tamagotchiList.add(t);
			threadList.add(tThread);

			tThread.start(); // ¡Lanzado al mundo!
			System.out.println(nombre + " ha sido lanzado al mundo.");
		}

		System.out.println("\n--- ¡Todos los Tamagotchis están vivos! ---");

		while (areAnyAlive()) {
			printMenu(); // Esta es la función del menú de ACCIONES

			int choice = 0;
			try {
				leer = sc.nextLine();
				choice = Integer.parseInt(leer);
			} catch (NumberFormatException e) {
				System.out.println("Error: Debes introducir un número.");
				continue; // Vuelve a mostrar el menú de acciones
			}

			if (choice == 6) {
				System.out.println("Abandonando a los Tamagotchis...");
				break; // Sale del 'while (areAnyAlive())'
			}

			Tamagotchi target = selectTamagotchi(sc);
			if (target == null)
				continue; // Si la selección falla, vuelve al menú

			// El switch de acciones
			switch (choice) {
			case 1:
				target.feed();
				break;
			case 2:
				target.clean();
				break;
			case 3:
				target.play(sc);
				break;
			case 4:
				killTamagotchi(target);
				break;
			case 5:
				System.out.println(target.getStatus());
				break;
			default:
				System.out.println("Opción no válida.");
				break;
			}
		}

		// Fin del juego
		System.out.println("Todos tus Tamagotchis han muerto o has abandonado.");
		System.out.println("Interrumpiendo hilos restantes...");
		for (Thread t : threadList) {
			t.interrupt();
		}
		sc.close();
	}

	// Función imprimir lista principal
	private static void printMainMenu() {
		System.out.println("1. Crear tamagotchi");
		System.out.println("2. Lanzar tamagotchi");
		System.out.println("3. Hacer una accion con el tamagotchi");
		System.out.println("4. Salir del juego");
	}

	// Funcion para inprimir menú
	private static void printMenu() {
		System.out.println("\n--- ACCIONES DEL CUIDADOR ---");
		System.out.println("1. Alimentar a un Tamagotchi");
		System.out.println("2. Limpiar a un Tamagotchi");
		System.out.println("3. Jugar con un Tamagotchi");
		System.out.println("4. Matar a un Tamagotchi");
		System.out.println("5. Ver estado de un Tamagotchi");
		System.out.println("6. Salir (Abandonar)");
		System.out.print("Elige una opción: ");
	}

	// Función para saber si hay tamagotchis vivos
	public static boolean areAnyAlive() {
		for (Tamagotchi t : tamagotchiList) {
			if (t.isAlive()) {
				return true;
			}
		}
		return false;
	}

	// Función para seleccionar tamagotchi. Se sacan por pantalla todos los
	// tamagotchis y el usuario seleccioina uno que es el que la función devuelve
	private static Tamagotchi selectTamagotchi(Scanner sc) {
		String leer = "";
		System.out.println("Que tamagotchi quieres elegir?");
		for (int i = 0; i < tamagotchiList.size(); i++) {
			if (tamagotchiList.get(i).isAlive()) {
				System.out.println((i + 1) + ". " + tamagotchiList.get(i).getTamagotchiName());
			}
		}
		System.out.println("Introduce el número de tamagotchi que quieres");
		int choice = 0;
		try {
			leer = sc.nextLine();
			choice = Integer.parseInt(leer);
		} catch (NumberFormatException e) {
			System.out.println("Has itroducido un número incorrecto");
			e.toString();
		}

		if (choice > tamagotchiList.size() || choice <= 0) {
			System.out.println("No has seleccionado una opción correcta");
			return null;
		}
		return tamagotchiList.get(choice - 1);
	}

	// Función para mata tamagotchi elegido primero ejecutamos el método kill
	// despues finalizamos el hilo que corresponde a ese tamagotchi
	private static void killTamagotchi(Tamagotchi t) {
		t.kill();
		if (!t.isAlive()) {
			Thread threadToKill = null;

			//Buscamos el hilo que se quiere matar pos su nombre
			for (Thread th : threadList) {
				if (th.getName().equals(t.getTamagotchiName())) {
					threadToKill = th;
					break;
				}
			}
			if (threadToKill != null) {
				// Interrunpimos el hilo
	            threadToKill.interrupt();
	        }
		}

	}
}
