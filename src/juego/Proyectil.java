package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Proyectil {
	private double x, y;
	private Image imagen;
	private double angulo;

	public Proyectil(double x, double y, double angulo) {
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.imagen = Herramientas.cargarImagen("juego/proyectil.png");

	}

	public void dibujarProyectil(Entorno proyectil) {
		proyectil.dibujarImagen(this.imagen, this.x, this.y, this.angulo, 1);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void mover() {
		this.x += Math.cos(this.angulo) * 4;
		this.y += Math.sin(this.angulo) * 4;
	}
}	
	
	
	

