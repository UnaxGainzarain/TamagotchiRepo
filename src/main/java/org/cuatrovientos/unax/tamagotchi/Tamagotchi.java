package org.cuatrovientos.unax.tamagotchi;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Tamagotchi implements Runnable {
	private String name;
	private int eatSpeed;
	private boolean isAlive = true;
	private String currentState = "IDLE";
	private Random rand;
	String leer;

	private long startTime = System.currentTimeMillis();
	private int dirtiness = 0;
	private long lastBathTime = System.currentTimeMillis();

	public Tamagotchi(String name, int eatSpeed) {
		super();
		this.name = name;
		this.eatSpeed = eatSpeed;
	}

	public void run() {
		System.out.println("He nacido");
		try {
			while (isAlive) {
				// 1. VIVIR: Simula el paso del tiempo
				Thread.sleep(1000);
				synchronized (this) {
					if (currentState.equals("IDLE")) {
						updateDirtiness();
						checkLifeSpan();
					}
				}
			}
		} catch (InterruptedException e) {
			isAlive = false;
			System.out.println(name + ": (ha sido interrunpido muere)");

		}
		System.out.println(name + "he muerto");
	}

	// Funcion para actualizar la suciedad miramos hace cuanto a sido el último baño
	// y si han sido más de 20000 añadimos un punto de suciedad
	private void updateDirtiness() {
		if (System.currentTimeMillis() - lastBathTime > 20000) {
			this.dirtiness++;
			this.lastBathTime = System.currentTimeMillis();
		}
		if (dirtiness == 5) {
			System.out.println(name + ": Empiezo a estas sucio");
		}
		if (dirtiness >= 10) {
			System.out.println(name + ": Me muero por suciedad");
			isAlive = false;
		}
	}

	// Función para chekear la vida que tiene el tamagotchi, si se acabo el tiempo
	// se muere
	private void checkLifeSpan() {
		if (System.currentTimeMillis() - startTime > 300000) {
			System.out.println(name + ": Se acabó mi tiempo... adiós.");
			isAlive = false;
		}
	}

	public synchronized void feed() {
		if (!currentState.equals("IDLE") || !isAlive) {
			System.out.println(name + ": No puedo comer ahora.");
			return;
		}

		currentState = "EATING";
		System.out.println(name + ": ¡Empiezo a comer!");
		try {
			Thread.sleep(eatSpeed); // Cada uno come a su ritmo
		} catch (InterruptedException e) {
			/* ... */ }
		System.out.println(name + ": ¡Terminé de comer!");
		currentState = "IDLE";
	}

	public synchronized void play(Scanner sc) {
		// Nos aseguramos de que el tamagotchi esté en reposo y vivo para poner jujar
		if (!currentState.equals("IDLE") || !isAlive) {
			System.out.println(name + "No me puedo jugar por que estoy muerto");
			return;
		}
		currentState = "PLAYING";
		rand = new Random();
		int n1 = rand.nextInt(5);
		int n2 = rand.nextInt(5);
		int result = n1 + n2;
		boolean answeredCorrectly = false;
		while (!answeredCorrectly) {
			System.out.println(name + ": Cual es la suma entre " + n1 + "+" + n2);
			leer = sc.nextLine();
			// Intentamos leer la suma del usuario por consola y si no es númerico
			// controlamos excepcion
			try {
				int resultUser = Integer.parseInt(leer);
				if (resultUser == result) {
					System.out.println("Correcto que bien juegas makina");
					answeredCorrectly = true;
				} else {
					System.out.println("Incorrecto sigue jugando");
				}
			} catch (Exception e) {
				System.out.println("No has introducido un número");
			}
		}
		currentState = "IDLE";

	}

	// Función para matar al tamagotchi primero nos aseguramos de que este libre y
	// ponemos la vaiable se vivo en false
	public synchronized void kill() {
		if (currentState.equals("IDLE")) {
			System.out.println(name + ": El cuidador me ha mandado a dormir...");
			isAlive = false;
			// Si el hilo está en Thread.sleep(1000) en el run(),
			// necesitamos un Thread.interrupt() desde el cuidador para despertarlo
			// y que pueda terminar su bucle 'while(isAlive)'.
		} else {
			System.out.println(name + ": ¡No puedes matarme! Estoy ocupado (" + currentState + ")");
		}
	}

	// Función para lavar al tamagotchi nos aseguramos de que este libre y vivo lo
	// ponemos en estado de lavando y detenemos el hilo durante 5 segundos
	public synchronized void clean() {
		if (!currentState.equals("IDLE") || !isAlive) {
			System.out.println(name + ": No me puedo limpiar ahora.");
			return;
		}

		currentState = "CLEANING";
		System.out.println(name + ": Tomando un baño...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			/* ... */ }

		this.dirtiness = 0;
		this.lastBathTime = System.currentTimeMillis();
		System.out.println(name + ": ¡Estoy limpio! [Suciedad: 0]");
		currentState = "IDLE";
	}

	// Funcion para devolver el estado devolvemos los parametros en un String
	public synchronized String getStatus() {
		if (!isAlive) {
			return name + " (Muerto) ";
		}
		long ageInSeconds = (System.currentTimeMillis() - startTime) / 1000;
		return name + " | " + "Tiempo: " + ageInSeconds + "s / 300s | " + "Suciedad: " + dirtiness + "/10 | "
				+ "Estado: " + currentState;
	}

	// Getters
	public synchronized boolean isAlive() {
		return isAlive;
	}

	public String getTamagotchiName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tamagotchi other = (Tamagotchi) obj;
		return Objects.equals(name, other.name);
	}

}
