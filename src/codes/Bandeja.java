package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Bandeja {

	private final int LIMITE_SUCIOS = 20;
	private final int LIMITE_LIMPIOS = 20;
	private final int LIMITE_SECOS = 20;
	private final int LIMITE_ALACENA = 20;
	private final int LIMITE_PLATOS = 20;
	//Lista de tipo Plato que representan las bandejas de platos
	private List<Plato> bandeja_sucios = new ArrayList<Plato>();
	private List<Plato> bandeja_limpios = new ArrayList<>();
	private List<Plato> bandeja_secos = new ArrayList<>();
	private List<Plato> alacena = new ArrayList<Plato>();
	
/*	private LinkedList<Plato> bandeja_sucios = new LinkedList<Plato>();
	private LinkedList<Plato> bandeja_limpios = new LinkedList<>();
	private LinkedList<Plato> bandeja_secos = new LinkedList<>();
	private LinkedList<Plato> alacena = new LinkedList<Plato>(); Posible?
*/	
	//Formato de hora para los mensajes por pantalla
	private DateTimeFormatter formatoHora;
	
	public Bandeja(DateTimeFormatter formatoHora) {
		this.formatoHora = formatoHora;
		
		for (int i = 0; i < LIMITE_PLATOS; i++) {
			bandeja_sucios.add(new Plato(i));
		}
	}
	
	public Plato cogerPlatoSucio() throws InterruptedException {
		//Metodo que ejecutan los hilos para obtener un plato de la bandeja de platos sucios
		Plato plato;
		
		synchronized (this) {
			//Bloque cerrojo para no malversar los datos
			
			//Se elimina el primer plato de la lista de platos sucios
			plato = bandeja_sucios.remove(0);
			
			if (bandeja_sucios.isEmpty()) {
				//Si ya no quedan platos sucios el hilo se interrumpe porque ya no queda mas trabajo por hacer
				System.out.printf("Hora %s: %s avisa de que ya no hay mas platos sucios......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				Thread.currentThread().interrupt();
			}
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha recogido el plato #%d de la bandeja de platos sucios......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se reactivan los hilos dormidos
			notifyAll();
			//Se devuelve el plato eliminado de la lista de platos sucios
			return plato;
		}
	}

	public void colocarEnBandejaLimpios(Plato plato) throws InterruptedException {
		//Metodo que ejecutan los hilos para colocar un plato en la bandeja de platos limpios
		synchronized (this) {
			//Bloque cerrojo que impide que otros hilos accedan mientras otro ya este dentro
			while (bandeja_limpios.size() >= LIMITE_LIMPIOS) {
				//Si la bandeja de platos limpios esta llena el hilo se duerme hasta que haya hueco
				System.out.printf("Hora %s: %s esta esperando a que haya hueco en la bandeja de platos limpios......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				wait();
			}
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha colocado el plato #%d en la bandeja de platos limpios......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se reactivan los hilos dormidos
			notifyAll();
			//Se coloca el plato en la lista de platos limpios
			bandeja_limpios.add(plato);
		}
	}

	public Plato cogerPlatoLimpio() throws InterruptedException {
		Plato plato;
		
		synchronized (this) {
			//Bloque cerrojo que impide que otros hilos accedan mientras otro ya este dentro
			while (bandeja_limpios.isEmpty()) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya platos en la bandeja de platos limpios......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				wait();
			}
			//Se elimina el primer plato de la lista de platos sucios
			plato = bandeja_limpios.remove(0);
			//plato = bandeja_limpios.remove(bandeja_limpios.size()); Posible?
			
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha recogido el plato #%d de la bandeja de platos limpios......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se reactivan los hilos dormidos
			notifyAll();
			//Se devuelve el plato eliminado de la lista de platos limpios
			return plato;
		}
	}

	public void colocarEnBandejaSecos(Plato plato) throws InterruptedException {
		synchronized (this) {
			//Bloque cerrojo que impide que otros hilos accedan mientras otro ya este dentro
			while (alacena.size() >= LIMITE_ALACENA) {
				//Si la bandeja de platos secos esta llena el hilo se duerme hasta que haya hueco
				System.out.printf("Hora %s: %s avisa de que la bandeja de platos secos esta llena......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				wait();
			}
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha colocado el plato #%d en la bandeja de platos secos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se reactivan los hilos dormidos
			notifyAll();
			//Se coloca el plato en la lista de platos secos
			alacena.add(plato);
		}
	}

	public Plato cogerPlatoSeco() throws InterruptedException {
		Plato plato;
		synchronized (this) {
			//Bloque cerrojo que impide que otros hilos accedan mientras otro ya este dentro
			while (bandeja_secos.isEmpty()) {
				//Si la bandeja de platos secos esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya platos en la bandeja de platos secos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				wait();
			}
			//Se elimina el primer plato de la lista de platos sucios
			plato = bandeja_secos.remove(0);
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha recogido el plato #%d de la bandeja de platos secos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se reactivan los hilos dormidos
			notifyAll();
			//Se devuelve el plato eliminado de la lista de platos limpios
			return plato;
		}
	}

	public void colocarEnAlacena(Plato plato) throws InterruptedException {
		synchronized (this) {
			//Bloque cerrojo que impide que otros hilos accedan mientras otro ya este dentro
			if (alacena.size() >= LIMITE_ALACENA) {
				//Si ya no quedan platos sucios el hilo se interrumpe porque ya no queda mas trabajo por hacer
				System.out.printf("Hora %s: %s avisa de que la alacena de platos esta llena......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				Thread.currentThread().interrupt();
			}
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: $s ha colocado el plato #%d en la alacena......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se coloca el plato en la lista de platos secos
			alacena.add(plato);
		}
	}
	
	
}
