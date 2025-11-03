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
		
		System.out.println("Cuantos tamagotchis quieres cuidar??");
		leer = sc.nextLine();
		int numTamagotchis = Integer.parseInt(leer);
		
		// Ahora lanzamos los tamagotchis al mundo (poner pilas y lanzar)
		// Segun cuantos tagotchis pida el usuario lanzamos uno por cada tamagotchi
		for (int i =0 ; i < numTamagotchis; i++) {
			int eatSpeed = rand.nextInt(5001)+ 3000;
			
			Tamagotchi t = new Tamagotchi("Tama-" + i, eatSpeed);
			tamagotchiList.add(t);
			Thread tThread = new Thread(t);
			threadList.add(tThread);
			tThread.start();
		}
		
		// Bucle principal del cuidador
		
		while (areAnyAlive()) {
			
			
		}
		
		
	}
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
	
	public static boolean areAnyAlive() {
		for (Tamagotchi t: tamagotchiList) {
			if(t.isAlive()) {
				return true;
			}
			}
		return false;
		}
	}

}
