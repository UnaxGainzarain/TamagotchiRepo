package org.cuatrovientos.unax.tamagotchi;

import java.util.Random;

public class Tamagotchi implements Runnable {
	private String name;
	private int eatSpeed;
	private boolean isAlive = true;
	private String currentState = "IDLE";
	private Random rand;
	
	private long startTime = System.currentTimeMillis();
	private int dirtiness =0;
	private long lastBathTime = System.currentTimeMillis();
	
	
	public Tamagotchi(String name, int eatSpeed) {
		super();
		this.name = name;
		this.eatSpeed = eatSpeed;
	}



	public void run() {
		System.out.println("He nacido");
		try {
			while(isAlive) {
				// 1. VIVIR: Simula el paso del tiempo
                Thread.sleep(1000); 
                
                if (currentState.equals("IDLE")) {
                	updateDirtiness();
                	checkLifeSpan();
                }
                
			}
		} catch (InterruptedException e) {
			isAlive = false;
			System.out.println(name + ": (ha sido interrunpido muere)");
			
		}
		System.out.println(name + "he muerto");
	}
	private void updateDirtiness() {
		if(System.currentTimeMillis()- lastBathTime >20000) {
			this.dirtiness ++;
			this.lastBathTime = System.currentTimeMillis();
		}
		if (dirtiness == 5) {
			System.out.println(name+ ": Empiezo a estas sucio");
		}
		if (dirtiness >= 10) {
			System.out.println(name+ ": Me muero por suciedad");
			isAlive = false;
		}
	}
	
	private void checkLifeSpan() {
		if (System.currentTimeMillis() - startTime > 300000) {
            System.out.println(name + ": Se acabó mi tiempo... adiós.");
            isAlive = false;
        }
	}
	
	private void play() {
		
	}
}

