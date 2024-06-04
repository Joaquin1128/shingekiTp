package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Mikasa {
	private double x, y;
	private double angulo;
	private Image imagen;
	private boolean titan;
	private int vidas;

	public Mikasa(double x, double y, double angulo, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.imagen = Herramientas.cargarImagen("juego/16.png");
		this.angulo = angulo;
		this.titan = false;
		this.vidas = 4;
	}

	public void dibujarMikasa(Entorno mikasa) {
		if (!this.titan)
			mikasa.dibujarImagen(imagen, this.x, this.y, this.angulo, 1);
		else {
			this.imagen = Herramientas.cargarImagen("juego/titan.png");
			mikasa.dibujarImagen(imagen, this.x, this.y, this.angulo, 1);
		}
	}

	public boolean colisionTitan(double x, double y) {
		if (colision(this.x, this.y, x, y, 65) && this.titan == true) {
			this.titan = false;
			return true;
		} else {
			return false;
		}
	}

	public void modificarAngulo(Entorno entorno) {
		if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			this.angulo += (Herramientas.radianes(2.0));
		}
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			this.angulo -= (Herramientas.radianes(2.0));
		}
	}

	public void pararObstaculo() {
		this.x -= Math.cos(this.angulo) * 2;
		this.y -= Math.sin(this.angulo) * 2;
	}

	public void haciaAdelante(Entorno entorno) {
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && this.x > 50 && this.x < 1150 && this.y > 50
				&& this.y < 550) {
			this.x += Math.cos(this.angulo) * 2;
			this.y += Math.sin(this.angulo) * 2;
			this.imagen = Herramientas.cargarImagen("juego/5.png");

		} else if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			this.x -= Math.cos(this.angulo) * 2;
			this.y -= Math.sin(this.angulo) * 2;
			this.angulo += (Herramientas.radianes(20.0));
			this.imagen = Herramientas.cargarImagen("juego/5.png");
		} else {
			this.imagen = Herramientas.cargarImagen("juego/16.png");
		}
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

	public boolean isTitan() {
		return titan;
	}

	public void setTitan(boolean titan) {
		this.titan = titan;
	}
	
	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

	public boolean colision(double x1, double y1, double x2, double y2, double dist) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) < dist * dist;
	}
}
