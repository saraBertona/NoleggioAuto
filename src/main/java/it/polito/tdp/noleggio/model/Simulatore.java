package it.polito.tdp.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.PriorityQueue;

import it.polito.tdp.noleggio.model.Event.EventType;

public class Simulatore {
	
	// Parametri di ingresso
	private int NC ; //numero auto
	private Duration T_IN = Duration.ofMinutes(10) ; //intervallo tra arrivo clienti DEVO SETTARLO
	private Duration T_TRAVEL = Duration.ofHours(1) ; // 1, 2, o 3 volte tanto //DEVO SETTARLO
	
	// Valori calcolati in uscita
	private int nClientiTot ; //DEVO SOLO FARE GET, NON MODIFICABILE DA ESTERNO
	private int nClientiInsoddisfatti ;//"
	
	// Stato del mondo
	private int autoDisponibili ;
	
	// Coda degli eventi
	private PriorityQueue<Event> queue ;
	
	// Costruttore
	public Simulatore(int NC) {
		this.NC = NC ;
		this.queue = new PriorityQueue<>();
		this.autoDisponibili = NC ;
	}
	
	public void run() {
		
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll() ; //OTTIENE E RIMUOVE LA TESTA DELLA CODA, OPPURE N ULL SE VUOTA MA QUA L'HO SCELTA NOT EMPTY
			processEvent(e);
		}
		
	}
	
	public void caricaEventi() {
		LocalTime ora = LocalTime.of(8, 0) ;
		while(ora.isBefore(LocalTime.of(16,0))) {
			this.queue.add(new Event(ora, EventType.NUOVO_CLIENTE)) ;
			ora = ora.plus(T_IN) ;
		}
	}

	private void processEvent(Event e) {
		
		switch(e.getType()) {
		case NUOVO_CLIENTE:
			this.nClientiTot++ ;
			if(this.autoDisponibili>0) {
				this.autoDisponibili-- ;
				int ore = (int)(Math.random()*3)+1;
				
				//??????????????????????????????????????
				LocalTime oraRientro = e.getTime().plus(Duration.ofHours(ore * T_TRAVEL.toHours())) ; //mi rientrerà tra ..quest'ora
				this.queue.add(new Event(oraRientro, EventType.AUTO_RESTITUITA));
			} else {
				this.nClientiInsoddisfatti++ ;
			}
			break;
		case AUTO_RESTITUITA:
			this.autoDisponibili++ ;
				
			break;
		}
	}

	public int getnClientiTot() {
		return nClientiTot;
	}

	public int getnClientiInsoddisfatti() {
		return nClientiInsoddisfatti;
	}

	public void setNC(int nC) {
		NC = nC;
	}

	public void setT_IN(Duration t_IN) {
		T_IN = t_IN;
	}

	public void setT_TRAVEL(Duration t_TRAVEL) {
		T_TRAVEL = t_TRAVEL;
	}
	
	
	

}
