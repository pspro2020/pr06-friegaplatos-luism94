package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Organizador implements Runnable {
	
	private int MIN_DURACION = 1;
	//Objeto cerrojo recibido por parametro del constructor
	private Bandeja bandeja;
	//Formato de hora recibido por parametro del constructor
	private DateTimeFormatter formatoHora;
	
	public Organizador(Bandeja bandeja, DateTimeFormatter formatoHora) {
		this.bandeja = bandeja;
		this.formatoHora = formatoHora;
	}

	@Override
	public void run() {
		//Metodo que contiene las instrucciones del hilo secundario a ejecutar
		Plato plato;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				//El hilo coge un plato de la bandeja de platos secos
				plato = bandeja.cogerPlatoSeco();
			} catch(InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido al Organizador mientras cogia un plato......\n", LocalDateTime.now().format(formatoHora).toString());
				return;
			}

			try {
				//El hilo coloca el plato en la alacena de platos
				bandeja.colocarEnAlacena(plato);
			} catch(InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido al Organizador mientras colocaba un plato......\n", LocalDateTime.now().format(formatoHora).toString());
				return;
			}
		}
	}

}
