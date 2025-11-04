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
				if (t.getTamagotchiName().equals(nombre)){
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
				index +=1;
				System.out.println(index + ". " + t.getTamagotchiName());
			}
			leer = sc.nextLine();
			int choice = Integer.parseInt(leer);
			Thread tThread = new Thread(tamagotchiList.get(choice -1));
			threadList.add(tThread);
			tThread.start();
			break;
		case 3:
			while (areAnyAlive()) {
			    printMenu();
			    leer = sc.nextLine();
			    choice = Integer.parseInt(leer);

			    if (choice == 6)
			        break; // Sale del 'while (areAnyAlive())'

			    Tamagotchi target = selectTamagotchi(sc);
			    
			    // Si el usuario eligió un tamagotchi inválido, volvemos a empezar el bucle
			    if (target == null) continue;

			    // <<< ¡¡¡ESTO ES LO QUE TE FALTA!!! >>>
			    switch (choice) {
			        case 1: 
			            target.feed(); 
			            break;
			        case 2: 
			            target.clean(); 
			            break;
			        case 3: 
			            target.play(sc); // Pasa el scanner
			            break;
			        case 4: 
			            killTamagotchi(target); 
			            break;
			        case 5: 
			            System.out.println(target.getStatus()); 
			            break;
			    }
			}
			break;
		default:
			break;
		case 4:
			System.out.println("Adioss, Interrumpiendo todos los hilos");
		    for (Thread t : threadList) {
		        t.interrupt(); // <<< BUENA PRÁCTICA
		    }
			exitPlay =false;
			break;
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
	private static Tamagotchi selectTamagotchi(Scanner sc) {
		String leer = "";
		System.out.println("Que tamagotchi quieres elegir?");
		for (int i = 0; i < tamagotchiList.size(); i++) {
			if (tamagotchiList.get(i).isAlive()) {
				System.out.println((i + 1) + ". " + tamagotchiList.get(i).getTamagotchiName());			}
		}
		System.out.println("Introduce el número de tamagotchi que quieres");
		int choice =0;
		try {
			leer = sc.nextLine();
			choice = Integer.parseInt(leer);
		}catch (NumberFormatException e) {
			System.out.println("Has itroducido un número incorrecto");
		    e.toString();
		}
		
		if (choice > tamagotchiList.size() || choice <=0) {
			System.out.println("No has seleccionado una opción correcta");
			return null;
		}
		return tamagotchiList.get(choice -1);
	}
	
	// Función para mata tamagotchi elegido primero ejecutamos el método kill despues finalizamos el hilo que corresponde a ese tamagotchi
	private static void killTamagotchi(Tamagotchi t) {
		t.kill();
		if (!t.isAlive()) {
            // Buscamos el HILO correspondiente a ese Tamagotchi
            int index = tamagotchiList.indexOf(t);
            //Rompemos el hilo correspondiente
            threadList.get(index).interrupt();
        }
	}

}
