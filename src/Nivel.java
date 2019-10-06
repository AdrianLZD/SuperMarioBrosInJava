
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Nivel extends GameNivel{
	@SuppressWarnings("unused")
	private boolean checkpointReached,
					marioMurio;
	private Ventana ventana;
	
	private BufferedImage fondoActual;
	private static BufferedImage 	fondo1,
									fondo2,
									fondo3,
									fondo4;
	static {
		try {
			fondo1 = ImageIO.read(new File("images/levels/nivel1.png"));
			fondo2 = ImageIO.read(new File("images/levels/nivel2.png"));
			fondo3 = ImageIO.read(new File("images/levels/nivel3.png"));
			fondo4 = ImageIO.read(new File("images/levels/nivel4.png"));
		}catch (IOException e){
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar el fondo", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Nivel(int posX, int posY, int scrollPos, Ventana ventana) {
		super(GameNivel.ge);		
		this.contadorTime=0;
		this.mario=new Mario(posX,posY,GameNivel.ge.jp.getVentana());
		this.defineNivel();
		this.ventana=ventana;
		this.ventana.setScroll(scrollPos);
		GameNivel.ge.setTimer(360);
		GameNivel.ge.setIniciarTimer(true);
	}
	
	public void changeNivel() {
		GameNivel.ge.setTimer(0);  //Timer reset
		GameNivel.ge.setIniciarTimer(false);  //Timer stop
		GameNivel.ge.sumarNivel(); 
		GameNivel.ge.ventana.setScroll(0); //Scroller reset
		this.stopMusica(); //Music off
		GameNivel.ge.estados.get(GameNivel.ge.estados.size()-2).reiniciarContadorTime(); //Infoscreen counter reset
		GameNivel.ge.estados.push(GameNivel.ge.estados.get(GameNivel.ge.estados.size()-2)); //New updated infoScreen
	}
	
	public void defineNivel() {
		if(GameNivel.ge.getNivelGlobal()==1) {
			this.checkpointReached=false;
			this.fondoActual=fondo1;
			GameNivel.ge.jp.setTamano(new Dimension(15360, 896));
			this.mapa = new Map("levels/nivel1.map");
			this.mapaObjetos = new MapObject("levels/nivel1Objetos.map");
			this.mapaEnemigo = new MapaEnemigos("levels/nivel1Enemigos.map");
			this.cancion = new MakeSound("audio/cancion-lvl1.wav");
		}else if(GameNivel.ge.getNivelGlobal()==2) {
			if(!GameNivel.ge.estados.peek().getMarioMurio()) { //Primera vez en el nivel
				this.mario.x=100; //se cambian las coordenadas de mario
				this.mario.y=300;
				this.mario.setInicialPos(100, 300); //El checkpoint va al inicio del nivel actual
				this.mario.setScrollPos(0); //El checkpoint del scroller se reinicia
			}
			this.checkpointReached=false;
			this.mario.setBloquesOscuros(true);
			this.fondoActual=fondo2;
			GameNivel.ge.setTimer(360);
			GameNivel.ge.setIniciarTimer(true);
			GameNivel.ge.jp.setTamano(new Dimension(16000, 896));
			this.mapa = new Map("levels/nivel2.map");
			this.mapaObjetos = new MapObject("levels/nivel2Objetos.map");
			this.mapaEnemigo = new MapaEnemigos("levels/nivel2Enemigos.map");
			this.cancion = new MakeSound("audio/cancion-lvl2.wav");
		}else if(GameNivel.ge.getNivelGlobal()==3) {
			if(!GameNivel.ge.estados.peek().getMarioMurio()) { //Primera vez en el nivel
				this.mario.x=228; //se cambian las coordenadas de mario
				this.mario.y=600;
				this.mario.setInicialPos(228, 600); //El checkpoint va al inicio del nivel actual
				this.mario.setScrollPos(0); //El checkpoint del scroller se reinicia
			}
			this.mario.setBloquesOscuros(false);
			this.fondoActual=fondo3;
			GameNivel.ge.setTimer(360);
			GameNivel.ge.setIniciarTimer(true);
			GameNivel.ge.jp.setTamano(new Dimension(10488, 896));
			this.mapa = new Map("levels/nivel3.map");
			this.mapaObjetos = new MapObject("levels/nivel3Objetos.map");
			this.mapaEnemigo = new MapaEnemigos("levels/nivel3Enemigos.map");
			this.cancion = new MakeSound("audio/cancion-lvl1.wav");
		}else if(GameNivel.ge.getNivelGlobal()==4) {
			if(!GameNivel.ge.estados.peek().getMarioMurio()) { //Primera vez en el nivel
				this.mario.x=100; //se cambian las coordenadas de mario
				this.mario.y=300;
				this.mario.setInicialPos(100, 334); //El checkpoint va al inicio del nivel actual
				this.mario.setScrollPos(0); //El checkpoint del scroller se reinicia
			}
			this.mario.setBloquesOscuros(false);
			this.fondoActual=fondo4;
			GameNivel.ge.setTimer(360);
			GameNivel.ge.setIniciarTimer(true);
			GameNivel.ge.jp.setTamano(new Dimension(11360, 896));
			this.mapa = new Map("levels/nivel4.map");
			this.mapaObjetos = new MapObject("levels/nivel4Objetos.map");
			this.mapaEnemigo = new MapaEnemigos("levels/nivel4Enemigos.map");
			this.cancion = new MakeSound("audio/cancion-lvl4.wav");
		}
	}
	
	
	public void tick() {
		this.mario.tick(this.mapa.getBloques(), this.mapaObjetos.getObjetos(), this.mapaEnemigo.getEnemigos());
		if(!this.checkpointReached) {
			if(GameNivel.ge.getNivelGlobal()==1 && this.mario.x>=5248) {
				this.mario.setScrollPos(4636);
				this.mario.setInicialPos(4636 + (GameNivel.ge.ventana.getWidth()/5)*2, 600);
				this.checkpointReached=true;
			}else if(GameNivel.ge.getNivelGlobal()==2 && this.mario.x>=7488) {
				this.mario.setScrollPos(6900);
				this.mario.setInicialPos(6900 + (GameNivel.ge.ventana.getWidth()/5)*2, 600);
				this.checkpointReached=true;
			}else if(GameNivel.ge.getNivelGlobal()==3) {
				this.mario.setScrollPos(0);
				this.mario.setInicialPos(228, 600);
				this.checkpointReached=true;
			}
		}
	
		if(!this.mario.getDetenerJuego()) {
			this.mapaEnemigo.tick(this.mapa.getBloques(), this.mario);
			this.mapaObjetos.tick(this.mapa.getBloques(), this.mario);
		}
		this.mapa.tick();
		
	}
	
	public void pinta(Graphics g) {
		g.drawImage(this.fondoActual, 0, -31,GameNivel.ge.jp);
		
		//Pintar los bloques que van de fondo
		for(int i=0; i<this.mapa.getBloques().length;i++) {
			for(int j=0; j<this.mapa.getBloques()[0].length;j++) {
				if(this.mapa.getBloques()[i][j].getId()==30 || this.mapa.getBloques()[i][j].getId()==1) { //Soporte pasto y suelo
					this.mapa.getBloques()[i][j].pinta(g);
				}
			}
		}
		
		//Pintar los enemigos
		this.mapaEnemigo.pinta(g);
		
		//Pintar los objetos que van atras
		for(int i=0; i<this.mapaObjetos.getObjetos().length;i++) {
			for(int j=0; j<this.mapaObjetos.getObjetos()[0].length;j++) {
				if(this.mapaObjetos.getObjetos()[i][j].getId()!=7 && this.mapaObjetos.getObjetos()[i][j].getId()!=8) { //Todos menos el fuego
					this.mapaObjetos.getObjetos()[i][j].pinta(g);
				}
			}
		}
		
		for(int i=0; i<this.mapa.getBloques().length;i++) {
			for(int j=0; j<this.mapa.getBloques()[0].length;j++) {
				if(this.mapa.getBloques()[i][j].getId()!=30 && this.mapa.getBloques()[i][j].getId()!=1) {
					this.mapa.getBloques()[i][j].pinta(g);
				}
			}
		}
		
		//Pintar objetos que van adelante
		for(int i=0; i<this.mapaObjetos.getObjetos().length;i++) {
			for(int j=0; j<this.mapaObjetos.getObjetos()[0].length;j++) {
				if(this.mapaObjetos.getObjetos()[i][j].getId()==7 || this.mapaObjetos.getObjetos()[i][j].getId()==8 ) {
					this.mapaObjetos.getObjetos()[i][j].pinta(g);
				}
			}
		}
		this.mario.pinta(g);
	}
	
	public void stopMusica() {
		this.cancion.stopMakeSound();
	}
	
	
	public void keyPressed(int k) {
		this.mario.keyPressed(k);
		
	}
	
	public void keyReleased(int k) {
		this.mario.keyReleased(k);
		
	}
	
	
}
