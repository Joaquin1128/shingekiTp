package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Fondo {
	private double x, y;
	private Image imagen;

	public Fondo() {
		this.x = 600;
		this.y = 300;
		this.imagen = Herramientas.cargarImagen("juego/fondo.jpg");
	}

	public void dibujarFondo(Entorno fondo) {
		fondo.dibujarImagen(this.imagen, this.x, this.y, 0, 1);
	}
	
	public void hasGanado() {
		this.imagen = Herramientas.cargarImagen("juego/hasganado.gif");
	}
	
	public void hasPerdido() {
		this.imagen = Herramientas.cargarImagen("juego/game-over.jpg");
	}
}
