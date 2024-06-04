package juego;

import java.awt.Image;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;

public class Obstaculo {
	private double x, y;
	private Image imagen;

	public Obstaculo(double x, double y) {
		this.x = x;
		this.y = y;
		this.imagen = Herramientas.cargarImagen("juego/casa.png");
	}

	public void dibujar(Entorno casa) {
		casa.dibujarImagen(this.imagen, this.x, this.y, 0, 0.1);
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
