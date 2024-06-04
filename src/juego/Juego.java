package juego;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Obstaculo[] casas;
	private Mikasa mikasa;
	private Fondo fondo;
	private Kyojines[] kyojin;
	private PocionTitan pocion;
	private Proyectil proyectil;
	private int titanMuertos = 0;
	private int time = 0;
	private Vidas vidas;
	private Puntos p;
	private int puntos = 0;
	private JefeTitan jefe;
	
	Juego() {		
		this.entorno = new Entorno(this, "Attack on Titan, Final Season - Grupo ... - v1", 1200, 600);
		this.fondo = new Fondo();
		this.mikasa = new Mikasa(entorno.getWidth() / 2, entorno.getHeight() / 2, 0, entorno);
		this.proyectil = null;
		this.pocion = null;
		this.p = null;
		this.vidas = null;
		this.jefe = null;
		crearCasas();
		crearKyojines();
		this.entorno.iniciar();
	}

	public void tick() {
		this.fondo.dibujarFondo(entorno);
		entorno.cambiarFont(Font.SERIF, 27, Color.white);
		entorno.escribirTexto("Kiojynes muertos: " + this.titanMuertos, (10),
				(entorno.getHeight() / 2) + 250);
		/*
		 * Obstaculos
		 */
		for(int i = 0; i < this.casas.length; i++) {
			if(this.casas[i] != null)
				this.casas[i].dibujar(entorno);
		}
		/*
		 * Poción, vidas (objeto), puntos
		 */
		crearVidas();
		if(this.vidas != null && this.mikasa != null) {
			this.vidas.dibujarVida(entorno);
			colisionVidas();
		}
		crearPocion();
		if(this.pocion != null && this.mikasa != null) {
			this.pocion.dibujarPocion(entorno);
			colisionPocion();
		}
		
		crearPuntos();
		if(this.p != null && this.mikasa != null) {
			this.p.dibujarPuntos(entorno);
			colisionPuntos();
		}

		/*
		 * Mikasa, interacciones
		 * */
		if(this.mikasa != null) {
			this.mikasa.dibujarMikasa(entorno);
			this.mikasa.modificarAngulo(entorno);
			this.mikasa.haciaAdelante(entorno);
			entorno.escribirTexto("Vidas: " + this.mikasa.getVidas(), entorno.getWidth() / 2, (entorno.getHeight() / 2) + 250);
			entorno.escribirTexto("Puntos: " + this.puntos, 300, (entorno.getHeight() / 2 + 250));
			quitarVidas();
			colisionMikasaJefe();
			colisionMikasaTitanKyojines();
		}
		
		for (int i = 0; i < this.casas.length; i++) {
			if(this.mikasa != null) {
				if (colision(mikasa.getX(), mikasa.getY(), this.casas[i].getX(), this.casas[i].getY(), 65))
					this.mikasa.pararObstaculo();
			}
		}
		
		
		/*
		 * Jefe, aparece cada 10 muertes de kyojines 
		 * */
		crearJefe();
		if(this.jefe != null && this.mikasa != null) {
			this.jefe.dibujarJefe(entorno);
			this.jefe.moverse();
			persecucionJefe();
			colisionJefeCasas();
			colisionJefeTitanes();
			entorno.escribirTexto("titan vidas " + this.jefe.getVidas(), 600, 300);
		}
		
		/*
		 * Proyectil
		 * */
		crearProyectil();
		
		/*
		 * Kyojines e interacciones
		 * */
		
		impactoEntreKyojines();
		impactoKyojinesCasas();
		persecucion();
		if(this.time % 500 == 0 && this.kyojin != null) {
			if (!(this.kyojin[0] == null && this.kyojin[1] == null && this.kyojin[2] == null && this.kyojin[3] == null && this.kyojin[4] == null && this.kyojin[5] == null))
				loopKyojines();
		}
		
		
		/*
		 * Victoria, derrota, y tiempo
		 * */
	
		ganaste();
		perdiste();
		
		if(this.time <= 70000) {
			if(this.mikasa != null && !(this.kyojin[0] == null && this.kyojin[1] == null && this.kyojin[2] == null && this.kyojin[3] == null && this.kyojin[4] == null && this.kyojin[5] == null && this.jefe == null)) {
				temporizador();
				this.entorno.escribirTexto("Tiempo: " + this.time , 1000, 550);
			}
		}
	}

	/*
	 * Métodos perder, ganas y colision
	 * */
	public void perdiste() {
		if(this.mikasa == null && (this.kyojin[0] != null || this.kyojin[1] != null || this.kyojin[2] != null || this.kyojin[3] != null || this.kyojin[4] != null || this.kyojin[5] != null || this.jefe != null) ) {
			this.fondo.hasPerdido();
			this.casas[0] = null;
			this.casas[1] = null;
			this.casas[2] = null;
			this.casas[3] = null;
		}
	}
	public void ganaste() {
		if (this.kyojin[0] == null && this.kyojin[1] == null && this.kyojin[2] == null && this.kyojin[3] == null && this.kyojin[4] == null && this.kyojin[5] == null && this.jefe == null) {
			this.fondo.hasGanado();
			this.mikasa = null;
			this.casas[0] = null;
			this.casas[1] = null;
			this.casas[2] = null;
			this.casas[3] = null;
		}
	}
	public static boolean colision(double x1, double y1, double x2, double y2, double dist) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) < dist * dist;
	}
	
	/*
	 *  Creación de kyojines, conlisiones con distintos objetos, loop kyojines
	 * */
	public void crearKyojines() {
		this.kyojin = new Kyojines[6];
		this.kyojin[0] = new Kyojines(160, 200);
		this.kyojin[1] = new Kyojines(80, 500);
		this.kyojin[2] = new Kyojines(480, 200);
		this.kyojin[3] = new Kyojines(280, 500);
		this.kyojin[4] = new Kyojines(880, 200);
		this.kyojin[5] = new Kyojines(700, 500);
	}
	
	public void impactoEntreKyojines() {
		for(int i = 0; i < this.kyojin.length-1; i++) {
			for(int j = i + 1; j < this.kyojin.length; j++) {
				if(this.kyojin[i] != null && this.kyojin[j] != null) {
					if(colision(this.kyojin[i].getX(), this.kyojin[i].getY(), this.kyojin[j].getX(), this.kyojin[j].getY(), 65)) {
						this.kyojin[i].rotar();
						this.kyojin[j].rotar();
					}
				}
			}
		}
	}
	
	public void impactoKyojinesCasas() {
		for(int i = 0; i < this.kyojin.length; i++) {
			for(int c = 0; c < this.casas.length; c++) {
				if(this.kyojin[i] != null && this.casas[c] != null) {
					if(colision(this.kyojin[i].getX(), this.kyojin[i].getY(), this.casas[c].getX(), this.casas[c].getY(), 65)) {
						this.kyojin[i].rotar();
					}
				}
			}
		}
	}
	
	public void persecucion() {
		for(int i = 0; i < this.kyojin.length; i++) {
			if(this.mikasa != null && this.kyojin[i] != null) {
				if(colision(this.kyojin[i].getX(), this.kyojin[i].getY(), this.mikasa.getX(), this.mikasa.getY(), 120) && this.mikasa.isTitan() == false) {
					this.kyojin[i].cambiarAngulo(this.mikasa.getX(), this.mikasa.getY());
				}
			}
		}
	}
	
	public void loopKyojines() {
		int pos = posicionLibre();
		if(pos != -1) {
			this.kyojin[pos] = new Kyojines();
		}
	}
	
	private int posicionLibre() {
		for (int i = 0; i < this.kyojin.length; i++) {
			if(this.kyojin[i] == null){
				return i;
			}
		}
		return -1;
	}

	/*
	 * Pseudo reloj
	 * */
	
	public void temporizador() {
		this.time++;
	}
	
	/*
	 * Casas (Obstaculos)
	 * */
	public void crearCasas() {
		this.casas = new Obstaculo[4];
		this.casas[0] = new Obstaculo(80, 150);
		this.casas[1] = new Obstaculo(700, 300);
		this.casas[2] = new Obstaculo(1000, 250);
		this.casas[3] = new Obstaculo(290, 400);
	}

	/*
	 * Objetos puntos, creación y colisiones
	 * */
	public void crearPuntos() {
		
		if(this.puntos % 100 == 0 && this.titanMuertos != 0 && this.p == null && this.mikasa != null) {
			this.p = new Puntos();
		}
		
	}
	
	public void colisionPuntos() {
		if((this.p != null && colision(this.mikasa.getX(),this.mikasa.getY(), this.p.getX(), this.p.getY(),65) && this.mikasa != null)){
			this.p = null;
			this.puntos += 60;
		}
	}
	

	/*
	 * Creación objeto jefe, colisiones
	 * */
	public void crearJefe() {
		if(this.titanMuertos % 10 == 0 && this.mikasa != null && this.jefe == null && this.titanMuertos != 0) {
			this.jefe = new JefeTitan();		
		}
	}
	
	public void persecucionJefe() {
		if(this.mikasa != null && this.jefe != null) {
			if(colision(this.jefe.getX(), this.jefe.getY(), this.mikasa.getX(), this.mikasa.getY(), 120) && this.mikasa.isTitan() == false) {
				this.jefe.cambiarAngulo(this.mikasa.getX(), this.mikasa.getY());
			}
		}
	}
	
	public void colisionJefeCasas() {
		for(int i = 0; i < this.casas.length; i++) {
			if(this.casas[i] != null) {
				if(colision(this.jefe.getX(), this.jefe.getY(), this.casas[i].getX(), this.casas[i].getY(), 65)) {
					this.jefe.rotar();
				}
			}
		}
	}
	
	public void colisionJefeTitanes() {
		for(int i = 0; i < this.kyojin.length; i++) {
			if(this.kyojin[i] != null) {
				if(colision(this.jefe.getX(), this.jefe.getY(), this.kyojin[i].getX(), this.kyojin[i].getY(), 65)) {
					this.jefe.rotar();
				}
			}
		}
	}

	/*
	 *  Creación objeto pociones y colisión, aparece cada 3 titanes muertos
	 * */
	public void crearPocion() {
		if(this.mikasa != null) {
			if( this.titanMuertos % 3 == 0 && this.titanMuertos != 0 && this.pocion == null && this.mikasa.isTitan() == false) {
				this.pocion = new PocionTitan();
			}
		}
	}
	
	public void colisionPocion() {
		if(this.pocion != null && colision(this.mikasa.getX(),this.mikasa.getY(), this.pocion.getX(), this.pocion.getY(),65) && this.mikasa != null){
			this.pocion = null;
			this.mikasa.setTitan(true);
			this.puntos += 5;
		}
	}
	
	/*
	 * Objeto vida, + vida de mikasa
	 * */
	public void crearVidas() {
		if(this.titanMuertos % 4 == 0 && this.titanMuertos != 0 && this.vidas == null &&  this.time % 50 == 0 && this.mikasa.getVidas() < 4) {
			this.vidas = new Vidas();
		}
	}
	
	public void colisionVidas() {
		if(this.vidas != null &&  colision(this.mikasa.getX(),this.mikasa.getY(), this.vidas.getX(), this.vidas.getY(),65) && this.mikasa != null) {
			this.vidas = null;
			this.mikasa.setVidas(this.mikasa.getVidas() + 1);
			this.puntos += 5;
		}
	}
	
	public void quitarVidas() {
		for(int i = 0; i < this.kyojin.length; i++) {
			if(this.mikasa.isTitan() == false && this.kyojin[i] != null && this.mikasa.getVidas() > 0) {
				if(colision(this.mikasa.getX(), this.mikasa.getY(), this.kyojin[i].getX(), this.kyojin[i].getY(), 65)) {
					this.mikasa.setVidas(this.mikasa.getVidas() - 1);
					this.mikasa.setX(this.entorno.getWidth()/2);
					this.mikasa.setY(this.entorno.getHeight()/2);
				}
			}
		}
		if(this.mikasa != null && this.jefe != null && this.mikasa.getVidas() > 0) {
			if(this.mikasa.colisionTitan(this.jefe.getX(), this.jefe.getY())) {
				this.mikasa.setVidas(this.mikasa.getVidas() - 1);
				this.mikasa.setX(this.entorno.getWidth()/2);
				this.mikasa.setY(this.entorno.getHeight()/2);
			}
			if(colision(this.mikasa.getX(), this.mikasa.getY(), this.jefe.getX(), this.jefe.getY(), 65)) {
				this.mikasa.setVidas(this.mikasa.getVidas() - 1);
				this.mikasa.setX(this.entorno.getWidth()/2);
				this.mikasa.setY(this.entorno.getHeight()/2);
			}
		}
	}

	/*
	 *  Creación proyectil, colisiones, movimientos
	 * */
	public void crearProyectil() {
		if(this.mikasa != null) {
			if (entorno.sePresiono(entorno.TECLA_ESPACIO) && this.proyectil == null && mikasa.isTitan() == false) {
				this.proyectil = new Proyectil(mikasa.getX(), mikasa.getY(), mikasa.getAngulo());
			}
	
			// Verifica si proyectil existe y si se encuentra dentro del rango del mapa
			else {
				if (this.proyectil != null && this.proyectil.getX() > 50 && this.proyectil.getX() < 1150
						&& this.proyectil.getY() > 50 && this.proyectil.getY() < 550) {
					this.proyectil.dibujarProyectil(entorno);
					this.proyectil.mover();
				}
				// Si se encuentra fuera del rango del mapa lo pone en null
				else {
					proyectil = null;
				}
			}
		}

		// Recorre el arreglo de casas y si colisiona con proyectil lo pone en null
		for (int i = 0; i < this.casas.length; i++) {
			if (this.proyectil != null) {
				if (colisionProyectil(this.casas[i].getX(), this.casas[i].getY())) {
					proyectil = null;
				}
			}
		}
		
	}

	/*
	 * Colision mikasa-kyojin, mikasa-jefe, proyectil-kyojin, proyectil-jefe
	 * */
	public void colisionMikasaTitanKyojines() {
		for (int i = 0; i < this.kyojin.length; i++) {
			if(this.kyojin[i] != null && this.mikasa != null) {
				this.kyojin[i].dibujarKyojin(entorno);
				this.kyojin[i].moverse();
				
				if (this.mikasa.colisionTitan(this.kyojin[i].getX(), this.kyojin[i].getY())) {
					this.kyojin[i] = null;
					this.titanMuertos++;
					this.puntos += 50;
				}
				
				if (proyectil != null) {
					if (colisionProyectil(this.kyojin[i].getX(), this.kyojin[i].getY())) {
						this.kyojin[i] = null;
						this.proyectil = null;
						this.titanMuertos++;
						this.puntos += 10;
					}
				}
					
				if(this.mikasa.isTitan() == false && this.kyojin[i] != null && this.mikasa.getVidas() == 0) {
					if(colision(this.mikasa.getX(), this.mikasa.getY(), this.kyojin[i].getX(), this.kyojin[i].getY(), 65)) {
						this.mikasa = null;
					}		
				}
			}
		}
	}
	
	public void colisionMikasaJefe() {
		if(this.mikasa != null && this.jefe != null) {
			if(colision(this.mikasa.getX(), this.mikasa.getY(), this.jefe.getX(), this.jefe.getY(), 65)  && this.mikasa.getVidas() == 0) {
				this.mikasa = null;
			}
			if(this.proyectil != null) {
				if (colisionProyectil(this.jefe.getX(), this.jefe.getY()) && this.jefe.getVidas() > 0) {
					this.jefe.setVidas(this.jefe.getVidas() - 1);
					this.proyectil = null;
				} else if (colisionProyectil(this.jefe.getX(), this.jefe.getY()) && this.jefe.getVidas() == 0) {
					this.proyectil = null;
					this.jefe = null;
					this.titanMuertos++;
				}	
			}
		}
	}
	
	public boolean colisionProyectil(double x, double y) {
		if (colision(this.proyectil.getX(), this.proyectil.getY(), x, y, 65) && this.proyectil != null) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();

	}
}
