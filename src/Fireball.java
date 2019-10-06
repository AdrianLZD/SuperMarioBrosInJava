
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Fireball extends Rectangle{
	private static final long serialVersionUID = 1L;
	
	private int id,
				contadorSalto=0,
				contadorGravedad=0,
				contadorSprite,
				spritePos;
	
	private boolean activarSonido=true,
					activarSonidoMuerte=true,
					cayendo=true,
					direccion,
					jumping=false;
	@SuppressWarnings("unused")
	private static MakeSound efecto;
	private static final int velocidadCaida=6;
	private static final int velocidad=11;
	
	private static BufferedImage[] sprite = {null, null, null, null, null, null, null, null, null, null};
	
	static {
		try {
			//Mario Fireballs
			sprite[1]= ImageIO.read(new File("images/mario/fireball1.png"));
			sprite[2]= ImageIO.read(new File("images/mario/fireball2.png"));
			sprite[3]= ImageIO.read(new File("images/mario/fireball3.png"));
			sprite[4]= ImageIO.read(new File("images/mario/fireball4.png"));
			sprite[5]= ImageIO.read(new File("images/mario/fireball_explosion1.png"));
			sprite[6]= ImageIO.read(new File("images/mario/fireball_explosion2.png"));
			sprite[7]= ImageIO.read(new File("images/mario/fireball_explosion3.png"));
			
			//Enemies Fireballs
			sprite[8] = ImageIO.read(new File("images/objects/fuego1.png"));
			sprite[9] = ImageIO.read(new File("images/objects/fuego2.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar el sprite de la bola de fuego", "Error", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	
	public Fireball(int x, int y, int id, boolean direccion, GameEstado ge) {
		this.id=id;
		this.spritePos=id;
		this.direccion=direccion;
		this.setBounds(x, y, sprite[this.id].getWidth(), sprite[this.id].getHeight());
	}
	
	public void tick(Bloque[][] b, Enemigos[][] e, Mario mario) {
		
		//Hacer sonido
		if(this.activarSonido) {
			if(this.id==1) {
				Fireball.efecto=new MakeSound("audio/smb_fireball.wav");
			}else {
				Fireball.efecto=new MakeSound("audio/smb_bowserfire.wav");
			}
			this.activarSonido=false;
		}
		
		//Bolas que matan a mario
		if(this.id==8) {
			if(Collision.outlineCollides(mario.getBounds(), this.getBounds())) {
				if(mario.getDelayEnemigo()==0) {
					if(mario.getEstado()==0) {
						mario.marioPerdio();
					}else {
						mario.marioPerdioPoder();
					}
				}
			}
		}
		
		
		
		//Colisiones 
		for(int i=0;i<b.length;i++) {
			for(int j=0;j<b[0].length;j++) {
				//Bloques
				if(b[i][j].getId()!=0 && b[i][j].getId()!=30 && b[i][j].getId()!=31) {
					//Bola con el suelo
					if((Collision.marioCollides(new Point(this.x+1, this.y+this.height+2), b[i][j]) 
							|| Collision.marioCollides(new Point(this.x+this.width-2, this.y+this.height+2), b[i][j]))
							&& !Collision.marioCollides(new Point(this.x-1, this.y+this.height-velocidadCaida-2), b[i][j]) 
							&& !Collision.marioCollides(new Point(this.x+this.width, this.y+this.height-velocidadCaida-2), b[i][j])) {
						this.y=b[i][j].y-this.height-6;
						this.cayendo=false;
						this.jumping=true;
						this.contadorGravedad=0;
						this.contadorSalto=0;
					}

					//Laterales
					if(this.id!=5) {
						if(Collision.marioCollides(new Point(this.x+this.width, this.y), b[i][j])
								|| Collision.marioCollides(new Point(this.x, this.y), b[i][j])
								|| Collision.marioCollides(new Point(this.x, this.y+this.height-velocidadCaida-2), b[i][j])
								|| Collision.marioCollides(new Point(this.x+this.width, this.y+this.height-velocidadCaida-2), b[i][j])) {
							this.y-=sprite[7].getHeight()/2;
							if(this.direccion) {
								this.x-=sprite[7].getWidth()/2;
							}
							if(this.activarSonidoMuerte) {
								Fireball.efecto=new MakeSound("audio/smb_bump.wav");
								this.activarSonidoMuerte=false;
							}

							this.id=5;
							this.jumping=false;
							this.cayendo=false;
							this.contadorSprite=0;
						}
					}
				}

				//enemigos
				if(this.id!=5) {
					if(e[i][j].getId()!=0 && e[i][j].getId()!=16) {
						if(Collision.enemigoCollides(new Point(this.x+this.width, this.y), e[i][j])
								|| Collision.enemigoCollides(new Point(this.x, this.y), e[i][j])
								|| Collision.enemigoCollides(new Point(this.x, this.y+this.height-velocidadCaida-2), e[i][j])
								|| Collision.enemigoCollides(new Point(this.x+this.width, this.y+this.height-velocidadCaida-2), e[i][j])) {
							if(this.activarSonidoMuerte) {
								Fireball.efecto= new MakeSound("audio/smb_kick.wav");
								this.activarSonidoMuerte=false;
								this.contadorSprite=0;
							}
							if(e[i][j].getId()==14) {
								//Golpear tortura voladora
								GameNivel.ge.sumarScore(100);
								e[i][j].setId(3);
							}else if(e[i][j].getId()==12){
								this.id=5;
								e[i][j].setSpritePos(13);
							}else {
								this.id=5;
								e[i][j].setAplastable(false);
								GameNivel.ge.sumarScore(100);
								e[i][j].matarEnemigo();
							}
						}
					}
				}
			}
		}
		
		//Salto
		if(this.jumping) {
			if(this.contadorSalto<5) {
				this.y-=velocidadCaida;
				this.contadorSalto++;
			}else if(this.contadorSalto<15){
				this.y-=velocidadCaida-3;
				this.contadorSalto++;
			}else if(this.contadorSalto<20) {
				this.y-=velocidadCaida-6;
				this.contadorSalto++;
			}else {
				this.jumping=false;
				this.cayendo=true;
				this.contadorSalto=0;
			}
		}
		
		//Cambiar sprite
		if(this.id==1) {
			if(this.contadorSprite<3) {
				this.spritePos=1;
				this.contadorSprite++;
			}else if(this.contadorSprite<6){
				this.spritePos=2;
				this.contadorSprite++;
			}else if(this.contadorSprite<9){
				this.spritePos=3;
				this.contadorSprite++;
			}else if(this.contadorSprite<12){
				this.spritePos=4;
				this.contadorSprite++;
			}else {
				this.contadorSprite=0;
			}
		}
		
		if(this.id==5){
			if(this.contadorSprite<3) {
				this.spritePos=5;
				this.contadorSprite++;
			}else if(this.contadorSprite<6) {
				this.spritePos=6;
				this.contadorSprite++;
			}else if(this.contadorSprite<9) {
				this.spritePos=7;
				this.contadorSprite++;
			}else {
				this.id=0;
			}
		}
		
		if(this.id==8) {
			if(this.contadorSprite<10) {
				this.spritePos=8;
				this.contadorSprite++;
			}else if(this.contadorSprite<20) {
				this.spritePos=9;
				this.contadorSprite++;
			}else {
				this.contadorSprite=0;
			}
		}
		
		
		//Movimiento
		if(this.id==1) {
			//Mover en eje x
			if(this.direccion) {
				this.x+=velocidad;
			}else {
				this.x-=velocidad;
			}
			
			//Gravedad
			if(this.cayendo) {
				if(this.contadorGravedad<5) {
					this.y+=velocidadCaida-6;
					this.contadorGravedad++;
				}else if(this.contadorGravedad<15){
					this.y+=velocidadCaida-3;
					this.contadorSalto++;
				}else {
					this.y+=velocidadCaida;
				}
			}
		}
		
		
		if(this.id==8) {
			this.x-=5;
		}
		
		
	}
	
	public int getId() {
		return this.id;
	}
	
	public void pinta(Graphics g) {
		g.drawImage(sprite[this.spritePos], this.x, this.y, GameNivel.ge.jp);
	}
}
