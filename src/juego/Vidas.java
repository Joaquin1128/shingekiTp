package juego;

import java.awt.Image;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;

public class Vidas {
	private double x, y;
	private Image imagen;
	
	public Vidas() {
		Random aleatorio = new Random();
		int min_x = 60;
		int max_x = 1100;
		int min_y = 80;
		int max_y = 100;
		double aux_x = aleatorio.nextInt(max_x - min_x) + min_x;
		double aux_y = aleatorio.nextInt(max_y - min_y) + min_y;
		this.x = aux_x;
		this.y = aux_y;
		this.imagen = Herramientas.cargarImagen("juego/vida.png");
	}
	
	public void dibujarVida(Entorno vida) {
		vida.dibujarImagen(this.imagen, this.x, this.y, 0, 1);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}