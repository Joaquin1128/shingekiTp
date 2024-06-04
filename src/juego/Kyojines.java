package juego;

import java.awt.Image;
import java.util.Random;
import entorno.Entorno;
import entorno.Herramientas;

public class Kyojines {
	private double angulo;
	private double x, y;
	private Image imagen;

	public Kyojines(double x, double y) {
		Random aleatorio = new Random();
		this.x = x;
		this.y = y;
		this.imagen = Herramientas.cargarImagen("juego/titan.png");
		this.angulo = aleatorio.nextDouble();
	}
	
	public Kyojines() {
		Random aleatorio = new Random();
		int min_x = 180;
		int max_x = 950;
		int min_y = 100;
		int max_y = 150;
		double aux_x = aleatorio.nextInt(max_x - min_x) + min_x;
		double aux_y = aleatorio.nextInt(max_y - min_y) + min_y;
		this.x = aux_x;
		this.y = aux_y;
		this.imagen = Herramientas.cargarImagen("juego/titan.png");
		this.angulo = aleatorio.nextDouble();
	}

	public void dibujarKyojin(Entorno kyojin) {
		kyojin.dibujarImagen(this.imagen, this.x, this.y, this.angulo, 0.9);
	}

	public void moverse() {
		if (this.x > 50 && this.x < 1100 && this.y > 50 && this.y <= 550) {
			this.x += Math.cos(this.angulo) * 0.3;
			this.y += Math.sin(this.angulo) * 0.3;

		} else {
			this.x -= Math.cos(this.angulo) * 1;
			this.y -= Math.sin(this.angulo) * 1;
			this.angulo += (Herramientas.radianes(180.0));
		}
	}

	public void rotar() {
		this.angulo += (Herramientas.radianes(180.0));
	}

	public void cambiarAngulo(double x2, double y2) {
		this.angulo = Math.atan2(y2 - this.y, x2 - this.x);
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getAngulo() {
		return this.angulo;
	}

	public void setAngulo(double angulo) {
		this.angulo = angulo;
	}
}