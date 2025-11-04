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

		System.out.println("Cuantos tamagotchis quieres cuidar??");
		leer = sc.nextLine();
		int numTamagotchis = Integer.parseInt(leer);
		
		boolean exitPlay= true;
		while (exitPlay){
		// Ahora lanzamos los tamagotchis al mundo (poner pilas y lanzar)
		printMainMenu();
		leer = sc.nextLine();
		int mainChoice = Integer.parseInt(leer);
		switch (mainChoice) {
		case 1:
			System.out.println("Creamos tamagotchi");
			eatSpeed = rand.nextInt(5001) + 3000;
			System.out.println("Indica el nombre de tamagotchi");
			String nombre = sc.nextLine();
			boolean existe = false;
			for (Tamagotchi t : tamagotchiList) {
				if (t.getTamagotchiName().equals(nombre))
					;
				{
					existe = true;
					break;
				}
			}
			if (existe == false) {
				Tamagotchi t = new Tamagotchi(nombre, eatSpeed);
				tamagotchiList.add(t);
			} else {
				System.out.println("El nombre está ocupado");
			}
			break;
		case 2:
			System.out.println("Lanzamos tamagotchi primero elige el tamagotchi segun posicion en lista");
			int index = 0;
			for (Tamagotchi t : tamagotchiList) {
				System.out.println(index + ". " + t.getTamagotchiName());
			}
			leer = sc.nextLine();
			int choice = Integer.parseInt(leer);
			Thread tThread = new Thread(tamagotchiList.get(choice -1));
			threadList.add(tThread);
			tThread.start();
			break;
		case 3:
			System.out.println("Hacemos una accion con tamagotchi");
			int MenuChoice = Integer.parseInt(leer);

			while (areAnyAlive()) {
				printMenu();
				 leer = sc.nextLine();
				 choice = Integer.parseInt(leer);

				if (choice == 6)
					break;

				Tamagotchi target = selectTamagotchi(sc);

			}
			break;
		default:
			break;
		case 4:
			exitPlay =false;
		}
		}

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
	public static Tamagotchi selectTamagotchi(Scanner sc) {
		String leer = "";
		System.out.println("Qur tamagotchi quieres elegir?");
		for (int i = 0; i < tamagotchiList.size(); i++) {
			if (tamagotchiList.get(i).isAlive()) {
				System.out.println(tamagotchiList.get(i).getTamagotchiName());
			}
		}
		System.out.println("Introduce el número de tamagotchi que quieres");
		leer = sc.nextLine();
		int choice = Integer.parseInt(leer);
		if (choice > tamagotchiList.size()) {
			System.out.println("No has seleccionado una opción correcta");
			return null;
		}
		return tamagotchiList.get(choice);
	}

}
