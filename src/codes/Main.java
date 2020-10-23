package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Main {
	
	private static final int MAX_HILOS = 3;
	private static final int MAX_SEGUNDOS = 60;
	private static DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss"); //Static?
	

	public static void main(String[] args) {
		//Formato de hora para los mensajes por pantalla
		formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
		//Objeto cerrojo comun a los hilos
		Bandeja bandeja = new Bandeja(formatoHora);
		//Hilos secundarios declarados con nombre
		Thread fregador = new Thread(new Fregador(bandeja, formatoHora), "Fregador");
		Thread secador = new Thread(new Secador(bandeja, formatoHora), "Secador");
		Thread organizador = new Thread(new Organizador(bandeja, formatoHora), "Organizador");
		//Array de hilos secundarios
		Thread[] hilos = {fregador, secador, organizador};
		//Se inician los hilos
		for (int i = 0; i < MAX_HILOS; i++) {
			hilos[i].start();
		}
		
		try {
			//Hilo principal espera 60 segundos antes de interrumpir los hilos secundarios
			TimeUnit.SECONDS.sleep(MAX_SEGUNDOS);
			
			for (int i = 0; i < MAX_HILOS; i++) {
				hilos[i].interrupt();
			}
			//Se muestra por pantalla el mensaje CUMPLEAÑOS FELIZ
			System.out.printf("Hora %s: --Todos: CUMPLEAÑOS FELIZ!!\n", LocalDateTime.now().format(formatoHora).toString());
		} catch (InterruptedException e) {
			//Si por si algo interrumpe el hilo principal, se muestra un mensaje por pantalla
			System.out.println("ERROR -- CUMPLEAÑERO INTERRUMPIDO");
			e.printStackTrace();
		}
	}

}
