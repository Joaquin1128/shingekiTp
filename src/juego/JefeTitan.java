package juego;

import java.awt.Image;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;

public class JefeTitan {
	private double angulo;
	private double x, y;
	private int vidas;
	private Image imagen;
	
	public JefeTitan() {
		
		this.x = 1000;
		this.y = 100;
		this.imagen = Herramientas.cargarImagen("juego/jefe.png");
		this.angulo = 90;
		this.vidas = 5;
	}
	
	public void dibujarJefe(Entorno jefe) {
		jefe.dibujarImagen(this.imagen, this.x, this.y, this.angulo, 1.2);
	}
	
	public void moverse() {
		if (this.x > 50 && this.x < 1100 && this.y > 50 && this.y <= 550) {
			this.x += Math.cos(this.angulo) * 1;
			this.y += Math.sin(this.angulo) * 1;
		} else {
			this.x -= Math.cos(this.angulo) * 1;
			this.y -= Math.sin(this.angulo) * 1;
			this.angulo += (Herramientas.radianes(45.0));
		}
	}

	public void rotar() {
		this.angulo += (Herramientas.radianes(90.0));
	}

	public void cambiarAngulo(double x2, double y2) {
		this.angulo = Math.atan2(y2 - this.y, x2 - this.x);
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

	public int getVidas() {
		return this.vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}
}
