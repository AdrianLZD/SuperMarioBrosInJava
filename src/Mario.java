
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Mario extends Rectangle {
	private static final long serialVersionUID = 1L;

	private ArrayList<Fireball> fireballs;
	private Ventana ventana;
	private boolean moverDerecha,
					moverIzquierda,
					jumping,
					canJump,
					cayendo = true,
					canMD = true,
					canMI=true,
					canScroll,
					direccion=true,
					moviendo,
					sonidoJump = true,
					activarControles = true,
					miniJump,
					colisiones=true,
					detenerJuego,
					canSpawnFireball=true,
					disparando,
					sonidoFewTime=true,
					bloquesOscuros,
					canRestartGame;
	public boolean animacionFinal,
				   ganar,
				   moviendoAlFinal,
				   tocoBandera,
				   perder,
				   cambiandoSprite = true;
	private int contadorJump,
				contadorMiniJump,
				previousKey,
				scrollPos,
				contadorPerder,
				delayFireball,
				inicialPosX,
				inicialPosY,
				spritePos,
				contadorTuberia;
	public int estado,
			   delayEnemigo,
			   contador;
	public MakeSound efecto;
	public static final int velocidad = 6;
	private static final int velocidadSalto=6;
	public Bloque bloqueBandera,
				  bloqueFinal;
	public Objeto bandera;
	public Bloque[] puente,
					paredMovible;
	public Enemigos bowser;


	private static BufferedImage[] sprite =  {null, null, null, null, null, null, null, null, null, null, null, //10
			null, null, null, null, null, null, null, null, null, null, //20
			null, null, null, null, null, null, null, null, null, null, //30
			null, null, null, null, null, null, null}; //37
	static {
		try {
			//Idle Sprites
			sprite[0] = ImageIO.read(new File("images/mario/mario_pequeño_derecha.png"));
			sprite[1] = ImageIO.read(new File("images/mario/mario_pequeño_derecha_salto.png"));
			sprite[2] = ImageIO.read(new File("images/mario/mario_pequeño_izquierda.png"));
			sprite[3] = ImageIO.read(new File("images/mario/mario_pequeño_izquierda_salto.png"));
			sprite[4] = ImageIO.read(new File("images/mario/mario_grande_derecha.png"));
			sprite[5] = ImageIO.read(new File("images/mario/mario_grande_derecha_salto.png"));
			sprite[6] = ImageIO.read(new File("images/mario/mario_grande_izquierda.png"));
			sprite[7] = ImageIO.read(new File("images/mario/mario_grande_izquierda_salto.png"));
			sprite[8] = ImageIO.read(new File("images/mario/mario_fuego_derecha.png"));
			sprite[9] = ImageIO.read(new File("images/mario/mario_fuego_derecha_salto.png"));
			sprite[10] = ImageIO.read(new File("images/mario/mario_fuego_izquierda.png"));
			sprite[11] = ImageIO.read(new File("images/mario/mario_fuego_izquierda_salto.png"));


			//Walking sprites
			sprite[12] = ImageIO.read(new File("images/mario/mario_pequeño_derecha_caminando1.png"));
			sprite[13] = ImageIO.read(new File("images/mario/mario_pequeño_derecha_caminando2.png"));
			sprite[14] = ImageIO.read(new File("images/mario/mario_pequeño_izquierda_caminando1.png"));
			sprite[15] = ImageIO.read(new File("images/mario/mario_pequeño_izquierda_caminando2.png"));
			sprite[16] = ImageIO.read(new File("images/mario/mario_grande_derecha_caminando1.png"));
			sprite[17] = ImageIO.read(new File("images/mario/mario_grande_derecha_caminando2.png"));
			sprite[18] = ImageIO.read(new File("images/mario/mario_grande_izquierda_caminando1.png"));
			sprite[19] = ImageIO.read(new File("images/mario/mario_grande_izquierda_caminando2.png"));
			sprite[20] = ImageIO.read(new File("images/mario/mario_fuego_derecha_caminando1.png"));
			sprite[21] = ImageIO.read(new File("images/mario/mario_fuego_derecha_caminando2.png"));
			sprite[22] = ImageIO.read(new File("images/mario/mario_fuego_izquierda_caminando1.png"));
			sprite[23] = ImageIO.read(new File("images/mario/mario_fuego_izquierda_caminando2.png"));

			//Transition Sprites
			sprite[24] = ImageIO.read(new File("images/mario/mario_transicion_derecha.png"));
			sprite[25] = ImageIO.read(new File("images/mario/mario_transicion_izquierda.png"));
			sprite[26] = ImageIO.read(new File("images/mario/mario_muerto.png"));

			//*
			//**
			//***
			//DO NOT USE SPRITE 27
			//***
			//**
			//*

			//Shooting Sprites
			sprite[28] = ImageIO.read(new File("images/mario/mario_fuego_derecha_disparando.png"));
			sprite[29] = ImageIO.read(new File("images/mario/mario_fuego_izquierda_disparando.png"));
			sprite[30] = ImageIO.read(new File("images/mario/mario_fuego_derecha_salto_disparando.png"));
			sprite[31] = ImageIO.read(new File("images/mario/mario_fuego_izquierda_salto_disparando.png"));

			//Finished level sprites
			sprite[32] = ImageIO.read(new File("images/mario/mario_pequeño_cayendo.png"));
			sprite[33] = ImageIO.read(new File("images/mario/mario_pequeño_cayendo2.png"));
			sprite[34] = ImageIO.read(new File("images/mario/mario_grande_cayendo.png"));
			sprite[35] = ImageIO.read(new File("images/mario/mario_grande_cayendo2.png"));
			sprite[36] = ImageIO.read(new File("images/mario/mario_fuego_cayendo.png"));
			sprite[37] = ImageIO.read(new File("images/mario/mario_fuego_cayendo2.png"));



		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar el sprite de Mario", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}

	public Mario(int x, int y,  Ventana ventana) {
		this.estado=0;
		this.fireballs = new ArrayList<Fireball>();
		this.delayEnemigo=0;
		this.spritePos=0;
		this.inicialPosX=x;
		this.inicialPosY=y;
		this.setBounds(x, y, sprite[this.spritePos].getWidth(), sprite[this.spritePos].getHeight());
		this.ventana = ventana;
	}

	public void tick(Bloque[][] b, Objeto[][] o, Enemigos[][] e) {
		if(ganar) {
			this.colisiones=false;
			this.detenerJuego=true;
			this.activarControles=false;
			this.canJump=false;
			this.moviendo=false;
			this.ventana.jp.getGe().estados.peek().stopMusica(); 
			this.tocoBandera=false;
			this.contadorTuberia=0;
			if(this.contadorJump<380 || this.ventana.jp.getGe().getTimer()>0) {
				if(this.ventana.jp.getGe().getTimer()>0) {
					this.ventana.jp.getGe().setTimer(this.ventana.jp.getGe().getTimer()-1);
					if(this.contador>12) {
						this.contador++;
					}else {
						this.efecto=new MakeSound("audio/smb_coin.wav");
						this.contador=0;
					}
					this.ventana.jp.getGe().sumarScore(10);
				}
				this.contadorJump++;
			}else {
				this.ganar=false;
				this.colisiones=true;
				this.detenerJuego=false;  			//Mario vuelve al estado jugable
				this.activarControles=true;
				this.canJump=true;
				this.animacionFinal=false;
				this.cambiandoSprite=true;
				this.contadorJump=0;
				this.ventana.jp.getGe().estados.peek().changeNivel(); //Se cambia el nivel al siguiente

			}
		}else if(animacionFinal){ //Mario inicia la animacion de la bandera
			this.ventana.jp.getGe().setIniciarTimer(false);
			this.activarAnimacionFinal();
		}else if(this.moviendoAlFinal) { //Mario toco una tuberia que lo lleva al final de un nivel
			this.moverAlFinal();
		}else {

			//Perder porque se acabo el tiempo
			if(this.ventana.jp.getGe().getTimer()<1) {
				this.perder=true;
			}else if(this.ventana.jp.getGe().getTimer()<60) {
				if(this.sonidoFewTime) {
					this.efecto=new MakeSound("audio/smb_warning.wav");	
					this.sonidoFewTime=false;
				}
			}
			//Bolas de fuego
			if(!this.fireballs.isEmpty()) {
				for(int i=0; i<this.fireballs.size();i++) {
					if(this.fireballs.get(i).getId()!=0) {
						if(Math.abs(this.x-this.fireballs.get(i).x)<this.ventana.getWidth()/2+this.ventana.getWidth()/10) {
							this.fireballs.get(i).tick(b, e, this);
						}else {
							this.fireballs.remove(i);
						}
					}else {
						this.fireballs.remove(i);
					}
				}
			}

			//Reiniciar delayFireball
			if(this.delayFireball!=0) {
				this.delayFireball--;
			}

			//Colisiones
			if(this.colisiones) {
				this.colisiones(b, o, e);
			}

			//Reiniciar delay enemigo
			if(this.delayEnemigo>0) {
				this.delayEnemigo--;
			}

			//Mario perdio
			if(this.perder) {
				if(this.contadorPerder==0) {
					this.colisiones=false;
					this.detenerJuego=true;
					this.activarControles=false;
					this.canJump=false;
					this.moviendo=false;
					this.efecto= new MakeSound("audio/smb_mariodie.wav");
					this.ventana.jp.getGe().estados.peek().stopMusica();
					this.ventana.jp.getGe().setIniciarTimer(false);
					this.contadorPerder=1;
					this.tocoBandera=false;
					this.contadorTuberia=0;
				}else if(this.contadorPerder<25) {
					this.y-=6;
					this.contadorPerder++;
				}else if(this.contadorPerder<175) {
					this.y+=8;
					this.contadorPerder++;
				}else {
					this.contadorPerder=0;
					this.ventana.jp.getGe().estados.push(this.ventana.jp.getGe().estados.get(this.ventana.jp.getGe().estados.size()-2)); //Se empuja la info screen
					this.ventana.jp.getGe().estados.peek().marioMurio(this.inicialPosX, this.inicialPosY, this.scrollPos); //Se hacen los cambios necesarios en mario					
				}
			}

			//Mario se cayo
			if(this.y>1050) {
				this.perder=true;
			}else {
				//this.perder=false;
			}

			//Mover Pantalla
			if(this.x>(this.ventana.getWidth()/5)*2) {
				this.canScroll=true;
			}else {
				this.canScroll=false;
			}


			//Controlar a Mario
			if(this.activarControles) {
				this.marioControles();
			}

			//Gravedad
			if(!this.detenerJuego) {
				if(this.cayendo)  {
					this.y+=velocidadSalto+3;
					this.canJump=false;
				}
			}


			//Cambiar sprite
			if(this.cambiandoSprite) {
				this.cambiarSprite();
			}else {
				this.animacionSprite();
			}
		}
	}

	private void marioControles() {
		//Derecha
		if(this.moverDerecha) {
			if(this.canMD) {
				this.direccion=true;
				this.moviendo=true;
				this.x+=velocidad;
				if(this.canScroll) {
					this.ventana.setScroll(this.ventana.getScroll()+velocidad);
				}
			}else {
				this.ventana.setScroll(this.ventana.getScroll());
			}

		}else {
			if(!this.moverIzquierda) {
				this.moviendo=false;
			}
		}


		//Izquierda
		if(this.moverIzquierda) {
			if(this.canMI) {
				this.direccion=false;
				this.x-=velocidad;
				this.moviendo=true;
				if(this.canScroll) {
					this.ventana.setScroll(this.ventana.getScroll()-velocidad);
				}
			}
		}else {
			if(!this.moverDerecha) {
				this.moviendo=false;
			}	
		}

		//Saltar
		if(this.jumping) {
			if(this.miniJump) {
				this.cayendo=false;
				if(this.contadorMiniJump<28 ) {
					this.y= this.y-velocidadSalto;
					this.contadorMiniJump++;
				}else {
					this.contadorMiniJump=0;
					this.cayendo=true;
					this.miniJump=false;
					this.jumping=false;
				}
			}else {
				this.cayendo=false;
				if(this.canJump) {
					if(this.sonidoJump) {
						this.efecto = new MakeSound("audio/smb_jump-super.wav");
						this.sonidoJump=false;
					}
					if(this.contadorJump<2 ) {
						this.y= this.y-velocidadSalto;
					}else if(this.contadorJump<31) {
						this.y= this.y-velocidadSalto-3;
					}
					else {
						this.canJump=false;
						this.cayendo=true;
					}
				}else {
					this.cayendo=true;
				}
				this.contadorJump++;
			}
		}else {
			this.miniJump=false;
			this.sonidoJump=true;
		}	
	}

	private void cambiarSprite() {
		if(this.perder) {
			this.spritePos=26;
		}else {

			if(this.jumping) {
				if(this.miniJump) {
					if(this.estado==0) {
						if(this.direccion) {
							if(this.delayEnemigo==0) {
								this.spritePos=1;
							}else {
								if(this.contador<5) {
									this.spritePos=1;
									this.contador++;
								}else if(this.contador<10) {
									this.spritePos=27;
									this.contador++;
								}else {
									this.contador=0;
								}
							}
						}else {
							if(this.delayEnemigo==0) {
								this.spritePos=3;
							}else {
								if(this.contador<5) {
									this.spritePos=3;
									this.contador++;
								}else if(this.contador<10) {
									this.spritePos=27;
									this.contador++;
								}else {
									this.contador=0;
								}
							}
						}
					}else if(this.estado==1) {
						if(this.direccion) {
							this.spritePos=5;
						}else {
							this.spritePos=7;
						}
					}else if(this.estado==2) {
						if(this.direccion) {
							this.spritePos=9;
						}else {
							this.spritePos=11;
						}
					}
				}else {
					if(this.canJump) {
						if(this.estado==0) { //pequeño
							if(this.direccion) {
								if(this.delayEnemigo==0) {
									this.spritePos=1;
								}else {
									if(this.contador<5) {
										this.spritePos=1;
										this.contador++;
									}else if(this.contador<10) {
										this.spritePos=27;
										this.contador++;
									}else {
										this.contador=0;
									}
								}
							}else {
								if(this.delayEnemigo==0) {
									this.spritePos=3;
								}else {
									if(this.contador<5) {
										this.spritePos=3;
										this.contador++;
									}else if(this.contador<10) {
										this.spritePos=27;
										this.contador++;
									}else {
										this.contador=0;
									}
								}
							}
						}else if(this.estado==1) { //grande
							if(this.direccion) {
								this.spritePos=5;
							}else {
								this.spritePos=7;
							}
						}else if(this.estado==2) { //Fuego
							if(this.direccion) {
								if(this.disparando) {
									if(this.contador<12) {
										this.spritePos=30;
										this.contador++;
									}else {
										this.contador=0;
										this.disparando=false;

									}
								}else {
									this.spritePos=9;
								}
							}else {
								if(this.disparando) {
									if(this.contador<12) {
										this.spritePos=31;
										this.contador++;
									}else {
										this.contador=0;
										this.disparando=false;
									}
								}else {
									this.spritePos=11;
								}
							}
						}
					}
				}
			}else {
				//Mario no esta brincando
				if(this.estado==0) {
					if(direccion) {
						if(moviendo) {
							if(this.contador<5) {
								this.spritePos=13;
								this.contador++;
							}else if(this.contador<10) {
								if(this.delayEnemigo==0) {
									this.spritePos=12;
								}else {
									this.spritePos=27;
								}
								this.contador++;
							}else {
								this.contador=0;
							}
						}else {
							if(this.delayEnemigo==0) {
								this.spritePos=0;
							}else {
								if(this.contador<5) {
									this.spritePos=0;
									this.contador++;
								}else if(this.contador<10) {
									this.spritePos=27;
									this.contador++;
								}else {
									this.contador=0;
								}
							}
						}
					}else {
						if(moviendo) {
							if(this.contador<5) {
								this.spritePos=14;
								this.contador++;
							}else if(this.contador<10) {
								if(this.delayEnemigo==0) {
									this.spritePos=15;
								}else {
									this.spritePos=27;
								}
								this.contador++;
							}else {
								this.contador=0;
							}
						}else {
							if(this.delayEnemigo==0) {
								this.spritePos=2;
							}else {
								if(this.contador<5) {
									this.spritePos=2;
									this.contador++;
								}else if(this.contador<10) {
									this.spritePos=27;
									this.contador++;
								}else {
									this.contador=0;
								}
							}
						}
					}
				}else if(this.estado==1) {
					if(direccion) {
						if(moviendo) {
							if(this.contador<5) {
								this.spritePos=16;
								this.contador++;
							}else if(this.contador<10) {
								this.spritePos=17;
								this.contador++;
							}else {
								this.contador=0;
							}
						}else {
							this.spritePos=4;
						}
					}else {
						if(moviendo) {
							if(this.contador<5) {
								this.spritePos=18;
								this.contador++;
							}else if(this.contador<10) {
								this.spritePos=19;
								this.contador++;
							}else {
								this.contador=0;
							}
						}else {
							this.spritePos=6;
						}
					}
				}else if(this.estado==2) { //Mario fuego
					if(direccion) {
						if(moviendo) {
							if(this.disparando) {
								if(this.contador<5) {
									this.spritePos=20;
									this.contador++;
								}else if(this.contador<10) {
									this.spritePos=28;
									this.contador++;
								}else {
									this.disparando=false;
									this.contador=0;
								}
							}else {
								if(this.contador<5) {
									this.spritePos=20;
									this.contador++;
								}else if(this.contador<10) {
									this.spritePos=21;
									this.contador++;
								}else {
									this.contador=0;
								}
							}
						}else {
							if(this.disparando) {
								if(this.contador<10) {
									this.spritePos=28;
									this.contador++;
								}else {
									this.contador=0;
									this.disparando=false;
								}

							}else {
								this.spritePos=8;
							}
						}
					}else {
						if(moviendo) {
							if(this.disparando) {
								if(this.contador<5) {
									this.spritePos=22;
									this.contador++;
								}else if(this.contador<10) {
									this.spritePos=29;
									this.contador++;
								}else {
									this.disparando=false;
									this.contador=0;
								}
							}else {
								if(this.contador<5) {
									this.spritePos=22;
									this.contador++;
								}else if(this.contador<10) {
									this.spritePos=23;
									this.contador++;
								}else {
									this.contador=0;
								}
							}
						}else {
							if(this.disparando) {
								if(this.contador<10) {
									this.spritePos=29;
									this.contador++;
								}else {
									this.contador=0;
									this.disparando=false;
								}

							}else {
								this.spritePos=10;
							}
						}
					}
				}
			}
		}
	}

	private void animacionSprite() {
		if(this.estado==0) {
			if(this.activarControles) {
				this.setBounds(x, y-64, sprite[this.spritePos].getWidth(), sprite[4].getHeight());
			}
			this.activarControles=false;
			if(this.contador<9) {
				if(this.direccion) {
					this.spritePos=24;
				}else {
					this.spritePos=25;
				}
				this.contador++;
			}else if(this.contador<18) {
				if(this.direccion) {
					this.spritePos=4;
				}else {
					this.spritePos=6;
				}
				this.contador++;
			}else if(this.contador<27) {
				if(this.direccion) {
					this.spritePos=24;
				}else {
					this.spritePos=25;
				}
				this.contador++;
			}else if(this.contador<36) {
				if(this.direccion) {
					this.spritePos=4;
				}else {
					this.spritePos=6;
				}
				this.contador++;
			}else if(this.contador<45) {
				if(this.direccion) {
					this.spritePos=24;
				}else {
					this.spritePos=25;
				}
				this.contador++;
			}else {
				this.setBounds(x, y+64, sprite[this.spritePos].getWidth(), sprite[0].getHeight());
				this.contador=0;
				this.cambiandoSprite=true;
				this.activarControles=true;
			}
		}else if(this.estado==1) {
			this.activarControles=false;
			this.detenerJuego=true;
			if(this.contador<9) {
				if(this.direccion) {
					this.spritePos=24;
				}else {
					this.spritePos=25;
				}
				this.contador++;
			}else if(this.contador<18) {
				if(this.direccion) {
					this.spritePos=4;
				}else {
					this.spritePos=6;
				}
				this.contador++;
			}else if(this.contador<27) {
				if(this.direccion) {
					this.spritePos=24;
				}else {
					this.spritePos=25;
				}
				this.contador++;
			}else if(this.contador<36) {
				if(this.direccion) {
					this.spritePos=4;
				}else {
					this.spritePos=6;
				}
				this.contador++;
			}else if(this.contador<45) {
				if(this.direccion) {
					this.spritePos=24;
				}else {
					this.spritePos=25;
				}
				this.contador++;

			}else {
				this.contador=0;
				this.cambiandoSprite=true;
				this.activarControles=true;
				this.detenerJuego=false;
			}
		}else if(this.estado==2) {
			this.activarControles=false;
			this.detenerJuego=true;
			if(this.contador<9) {
				if(this.direccion) {
					this.spritePos=8;
				}else {
					this.spritePos=10;
				}
				this.contador++;
			}else if(this.contador<18) {
				if(this.direccion) {
					this.spritePos=4;
				}else {
					this.spritePos=6;
				}
				this.contador++;
			}else if(this.contador<27) {
				if(this.direccion) {
					this.spritePos=8;
				}else {
					this.spritePos=10;
				}
				this.contador++;
			}else if(this.contador<36) {
				if(this.direccion) {
					this.spritePos=4;
				}else {
					this.spritePos=6;
				}
				this.contador++;
			}else if(this.contador<45) {
				if(this.direccion) {
					this.spritePos=8;
				}else {
					this.spritePos=10;
				}
				this.contador++;

			}else {
				this.contador=0;
				this.cambiandoSprite=true;
				this.activarControles=true;
				this.detenerJuego=false;
			}
		}
	}

	public void cambiarEstado(int estado) {
		if(this.estado==0 && (estado==1 || estado==2)){
			this.y-=Bloque.blockSize-2;
		}else if((this.estado==1 || this.estado==2) && estado==0) {
			this.cayendo=true;
		}
		this.estado=estado;
		if(this.estado==0) {
			this.spritePos=0;
		}else if(this.estado==1) {
			this.spritePos=4;
		}else if(this.estado==2) {
			this.spritePos=8;
		}
		this.setBounds(x, y, sprite[this.spritePos].getWidth(), sprite[this.spritePos].getHeight());
	}

	private void colisiones(Bloque[][] b, Objeto[][] o, Enemigos[][] e) {

		//i es el eje y
		//j es el eje x
		for(int i=0;i<b.length;i++) {
			for(int j=0;j<b[0].length;j++) {	

				//Bloques
				if(Math.abs(this.x-b[i][j].x)<this.ventana.getWidth()) {
					if(b[i][j].getId()!=0 && b[i][j].getId()!=17 &&b[i][j].getId()!=31 && b[i][j].getId()!=30) { //No chocar con bloques vacios

						//Left mario
						if((Collision.marioCollides(new Point(this.x, this.y+velocidad), b[i][j]) 
								|| Collision.marioCollides(new Point(this.x, this.y+this.height-velocidad-4), b[i][j]))
								&& !Collision.marioCollides(new Point(this.x+velocidad+3, this.y-3), b[i][j])) {
							this.x=b[i][j].x+b[i][j].width+2;
							this.canMI=false;
							if(this.canScroll) {
								this.ventana.setScroll(this.ventana.getScroll()+velocidad);
							}
						}else {
							this.canMI=true;
						}


						//Right mario
						if((Collision.marioCollides(new Point(this.x+this.width, this.y+velocidad), b[i][j]) 
								|| Collision.marioCollides(new Point(this.x+this.width, this.y+this.height-velocidad-4), b[i][j]))
								&& !Collision.marioCollides(new Point(this.x+this.width-velocidad, this.y-3), b[i][j])) {
							b[i][j].collision(this, b,o, 1, i, j);
							this.x=b[i][j].x-this.width-2;
							this.canMD=false;
							if(this.canScroll) {
								this.ventana.setScroll(this.ventana.getScroll()-velocidad);
							}
						}else {
							this.canMD=true;
						}

						//Down mario
						if((Collision.marioCollides(new Point(this.x+1, this.y+this.height+2), b[i][j]) 
								|| Collision.marioCollides(new Point(this.x+this.width-2, this.y+this.height+2), b[i][j]))
								&& !Collision.marioCollides(new Point(this.x-1, this.y+this.height-velocidadSalto-2), b[i][j]) 
								&& !Collision.marioCollides(new Point(this.x+this.width, this.y+this.height-velocidadSalto-2), b[i][j])) {
							b[i][j].collision(this, b,o, 2, i, j);
							this.y=b[i][j].y-this.height-8;
							this.cayendo=false;
							this.canJump=true;
						}else {
							this.cayendo=true;

						}


						//Top mario
						if(Collision.marioCollides(new Point(this.x+velocidad+3, this.y+1), b[i][j]) 
								|| Collision.marioCollides(new Point(this.x+this.width-velocidad, this.y-1), b[i][j])) {
							this.y=b[i][j].y+b[i][j].height+2;
							this.jumping = false;
							this.cayendo=true;
							b[i][j].collision(this, b,o, 3, i, j);
						}
						//}
						//}
						//}
					}
				}
				
				//Eliminar objetos lejanos
				if(o[i][j].getId()!=0) {
					if(Math.abs(this.x-o[i][j].x)>this.ventana.getWidth()) {
						if(o[i][j].getId()!=5 && o[i][j].getId()!=6 && o[i][j].getId()!=7 && o[i][j].getId()!=8 && o[i][j].getId()!=10) { //No eliminar la bandera, monedas, tiras de fuego, fuego
							o[i][j].setId(0);
						}
					}
				}

				//Objetos
				if(Math.abs(this.x-o[i][j].x)<300) {
					if(o[i][j].getId()!=0 && o[i][j].getId()!=7) {
						if(Collision.objetoCollides(this.getBounds(), o[i][j])) {
							o[i][j].collision(this, b, e, i, j);
						}
					}else if(o[i][j].getId()==7) {
						if(Collision.outlineCollides(this.getBounds(), o[i][j].outline)) {
							o[i][j].collision(this, b, e, i, j);
						}
					}
				}

				//Enemigos
				if(e[i][j].getId()!=0) {

					if(e[i][j].getId()==14) { //Tortuga Voladora

						//Matar a mario con tortuga voladora
						if((Collision.enemigoCollides(new Point(this.x+2, this.y+2), e[i][j]) 
								|| Collision.enemigoCollides(new Point(this.x+2, this.y+this.height-10), e[i][j])
								|| Collision.enemigoCollides(new Point(this.x+this.width-2, this.y+2), e[i][j])
								|| Collision.enemigoCollides(new Point(this.x+this.width-2, this.y+this.height-10), e[i][j]))
								&& !e[i][j].getMuerto()) {
							if(this.estado==0) {
								if(!e[i][j].getMuerto()) {
									if(this.delayEnemigo==0) {
										this.perder=true;
									}
								}

							}else if(this.estado==1 || this.estado==2) {
								if(!e[i][j].getMuerto()) {
									if(this.delayEnemigo==0) {
										this.efecto=new MakeSound("audio/smb_powerdown.wav");
										this.delayEnemigo=150;
										this.cambiandoSprite=false;
										this.cambiarEstado(0);
									}
								}	
							}
						}


						//Matar tortuga voladora
						if((Collision.enemigoCollides(new Point(this.x+2,this.y+this.height+5), e[i][j]) 
								|| Collision.enemigoCollides(new Point(this.x+this.width-2,this.y+this.height+5), e[i][j]))
								&& !Collision.enemigoCollides(new Point(this.x+2, this.y+this.height-12), e[i][j])
								&& !Collision.enemigoCollides(new Point(this.x+this.width-2, this.y+this.height-12), e[i][j])) {
							if(this.delayEnemigo==0) {
								this.jumping=true;
								this.miniJump=true;
								this.contadorMiniJump=0;
								this.efecto=new MakeSound("audio/smb_stomp.wav");
								this.ventana.jp.getGe().sumarScore(100);
								this.y=e[i][j].y-this.height-8;
								e[i][j].y-=8;
								e[i][j].setId(3);
							}
						}
					}else {

						//Bajar vida de mario
						if((Collision.enemigoCollides(new Point(this.x+2, this.y+2), e[i][j]) 
								|| Collision.enemigoCollides(new Point(this.x+2, this.y+this.height-10), e[i][j])
								|| Collision.enemigoCollides(new Point(this.x+this.width-2, this.y+2), e[i][j])
								|| Collision.enemigoCollides(new Point(this.x+this.width-2, this.y+this.height-10), e[i][j]))
								&& !e[i][j].getMuerto()) {
							if(this.estado==0) {
								if(e[i][j].getId()==8) { //Caparazon 
									e[i][j].setId(9);
									e[i][j].revivirEnemigo();
									if(this.x+ sprite[this.spritePos].getWidth()/2 <= e[i][j].x + e[i][j].getWidth()/2) {
										e[i][j].setDireccion(true);
										e[i][j].x+=32;
									}else {
										e[i][j].setDireccion(false);
										e[i][j].x-=32;
									}
									this.efecto=new MakeSound("audio/smb_kick.wav");
									e[i][j].setMovimiento(true);


								}else {
									if(!e[i][j].getMuerto()) {
										if(this.delayEnemigo==0) {
											this.perder=true;
										}
									}
								}
							}else if(this.estado==1 || this.estado==2) {
								if(e[i][j].getId()==8) { //Caparazon 
									e[i][j].setId(9);
									e[i][j].revivirEnemigo();
									if(this.x+ sprite[this.spritePos].getWidth()/2 <= e[i][j].x + e[i][j].getWidth()/2) {
										e[i][j].setDireccion(true);
										e[i][j].x+=32;
									}else {
										e[i][j].setDireccion(false);
										e[i][j].x-=32;
									}
									e[i][j].setMovimiento(true);
									this.efecto=new MakeSound("audio/smb_kick.wav");

								}else {
									if(!e[i][j].getMuerto()) {
										if(this.delayEnemigo==0) {
											this.efecto=new MakeSound("audio/smb_powerdown.wav");
											this.delayEnemigo=150;
											this.cambiandoSprite=false;
											this.cambiarEstado(0);
										}
									}
								}
							}
						}



						//Matar enemigo

						if((Collision.enemigoCollides(new Point(this.x+2,this.y+this.height+2), e[i][j]) 
								|| Collision.enemigoCollides(new Point(this.x+this.width-2,this.y+this.height+2), e[i][j]))
								&& !Collision.enemigoCollides(new Point(this.x+2, this.y+this.height-8), e[i][j])
								&& !Collision.enemigoCollides(new Point(this.x+this.width-2, this.y+this.height-8), e[i][j])) {
							if(e[i][j].getId()!=16) {
								if(e[i][j].getId()==8) { 			//Caparazon quieto
									this.jumping=true;
									this.miniJump=true;
									this.contadorMiniJump=0;
									this.efecto=new MakeSound("audio/smb_stomp.wav");
									this.y=e[i][j].y-this.height-8;
									e[i][j].setId(9);
									e[i][j].revivirEnemigo();
									if(this.x+ sprite[this.spritePos].getWidth()/2 <= e[i][j].x + e[i][j].getWidth()/2) {
										e[i][j].setDireccion(true);
									}else {
										e[i][j].setDireccion(false);
									}
									e[i][j].setMovimiento(true);
								}else {
									if(this.delayEnemigo==0) {
										if(e[i][j].getId()==12) { //Piranha
											if(this.estado==0) {
												if(this.delayEnemigo==0) {
													this.perder=true;
												}
											}else if(this.estado==1 || this.estado==2) {
												if(this.delayEnemigo==0) {
													this.efecto=new MakeSound("audio/smb_powerdown.wav");
													this.delayEnemigo=150;
													this.cambiandoSprite=false;
													this.cambiarEstado(0);
												}
											}
										}
									}
									if(!e[i][j].getMuerto()) {
										if(this.delayEnemigo==0) {
											this.jumping=true;
											this.miniJump=true;
											this.contadorMiniJump=0;
											this.efecto=new MakeSound("audio/smb_stomp.wav");
											this.y=e[i][j].y-this.height-8;
											this.ventana.jp.getGe().sumarScore(100);
											e[i][j].matarEnemigo();
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void activarAnimacionFinal() {

		//Desactivar cosas y mario brinca al poste aun si esta debajo de el
		if(this.colisiones) {
			this.x+=34;
			if(this.estado==0) {
				this.spritePos=32;
			}else if(this.estado==1) {
				this.spritePos=34;
			}else if(this.estado==2) {
				this.spritePos=36;
			}
			if(this.y+this.height>this.bloqueBandera.y) {
				this.y-=64;
			}
			this.ventana.jp.getGe().estados.peek().stopMusica();
			this.efecto = new MakeSound("audio/smb_stageclear.wav");
			this.colisiones=false;
			this.detenerJuego=true;
			this.activarControles=false;
			this.canJump=false;
			this.moviendo=false;
			this.cambiandoSprite=false;
			this.contador=0;
			this.contadorJump=0;
		}

		//Mover mario y la bandera hacia abajo
		if(this.estado==0) {
			if(this.y+this.height<this.bloqueBandera.y) {
				this.y+=5;
			}
		}else {
			if(this.y+this.height<this.bloqueBandera.y+9) {
				this.y+=5;
			}
		}

		//La bandera llego al suelo y mario cambia de lado
		if(this.contador==0) {
			if(this.bandera.y+this.bandera.height<this.bloqueBandera.y+9) {
				this.bandera.y+=5;
			}else {
				this.x+=40;
				this.contador++;
				if(this.estado==0) {
					this.spritePos=33;
				}else if(this.estado==1) {
					this.spritePos=35;
				}else if(this.estado==2) {
					this.spritePos=37;
				}
			}
		}

		//Mario se queda en el poste unos momentos
		if(this.contador>0 && this.contador<15) {
			this.contador++;

			//Mario hace el cambio de sprite a saltar y salta
		}else if(this.contador>0 && this.contador<30) {
			if(this.estado==0) {
				this.spritePos=1;
			}else if(this.estado==1) {
				if(this.contador==15) {
					this.y-=20;
				}
				this.spritePos=5;
			}else if(this.estado==2) {
				if(this.contador==15) {
					this.y-=20;
				}
				this.spritePos=9;
			}
			this.x+=velocidad-2;
			this.y-=velocidad;

			this.contador++;

			//Mario empieza a caer del salto
		}else if(this.contador>0 && this.contador<42) {
			this.x+=velocidad-2;
			this.y+=velocidad;
			this.contador++;


			//Mario empieza a caminar y termina el nivel
		}else if(this.contador>0 && this.contador<100){
			if(this.estado==0) {
				if(this.contadorJump<5) {
					this.spritePos=12;
					this.contadorJump++;
				}else if(this.contadorJump<10) {
					this.spritePos=13;
					this.contadorJump++;
				}else {
					this.contadorJump=0;
				}
			}else if(this.estado==1) {
				if(this.contadorJump<7) {
					this.spritePos=16;
					this.contadorJump++;
				}else if(this.contadorJump<14) {
					this.spritePos=17;
					this.contadorJump++;
				}else {
					this.contadorJump=0;
				}
			}else if(this.estado==2) {
				if(this.contadorJump<7) {
					this.spritePos=20;
					this.contadorJump++;
				}else if(this.contadorJump<14) {
					this.spritePos=21;
					this.contadorJump++;
				}else {
					this.contadorJump=0;
				}
			}
			this.x+=velocidad-4;

			if(this.x>this.bloqueFinal.x+32) {
				this.spritePos=27;
				this.contadorJump=0;
				this.contador=0;
				this.ganar=true;
			}
		}


	}

	public int getEstado() {
		return this.estado;
	}

	public void spawnFireball() {
		if(this.delayFireball==0) {
			this.disparando=true;
			if(this.direccion) {
				this.fireballs.add(new Fireball(this.x+this.width, this.y+this.height/2, 1, this.direccion, this.ventana.jp.getGe()));
			}else {
				this.fireballs.add(new Fireball(this.x, this.y+this.height/2, 1, this.direccion, this.ventana.jp.getGe()));
			}
		}
	}

	public void pinta(Graphics g) {
		g.drawImage(sprite[this.spritePos], this.x, this.y, this.ventana.jp);
		if(!this.fireballs.isEmpty()) {
			for(int i=0; i<this.fireballs.size();i++) {
				this.fireballs.get(i).pinta(g);
			}
		}
	}

	private void moverAlFinal() {
		if(this.colisiones) {
			this.ventana.jp.getGe().estados.peek().stopMusica();
			this.colisiones=false;
			this.detenerJuego=true;
			this.activarControles=false;
			this.canJump=false;
			this.moviendo=false;
			this.cambiandoSprite=false;
			this.jumping=false;
		}

		if(this.ventana.jp.getGe().getNivelGlobal()==2) {
			if(this.contadorTuberia==0) {
				this.efecto = new MakeSound("audio/smb_pipe.wav");
				this.contadorTuberia++;
				this.spritePos=27;
			}else if(this.contadorTuberia<50) {
				this.x+=3;
				this.contadorTuberia++;
			}else if(this.contadorTuberia==50) {
				this.scrollPos+=1956;
				this.ventana.setScroll(this.ventana.getScroll()+1956);
				this.x+=1736;
				if(this.estado==0) {
					this.y=576;
				}else {
					this.y=512;
				}
				this.contadorTuberia++;
				this.bloquesOscuros=false;
			}else if(this.contadorTuberia==51) {
				this.ventana.jp.getGe().estados.peek().setCancion("audio/cancion-lvl1.wav");
				this.colisiones=true;
				this.detenerJuego=false;
				this.activarControles=true;
				this.canJump=true;
				this.moviendo=true;
				this.cambiandoSprite=true;
				this.moviendoAlFinal=false;
			}
		}else if(this.ventana.jp.getGe().getNivelGlobal()==4) {

			if(this.ventana.jp.getGe().getIniciarTimer()) {
				this.ventana.jp.getGe().setIniciarTimer(false);
				if(this.spritePos==27) {
					if(this.estado==0) {
						this.spritePos=0;
					}else if(this.estado==1) {
						this.spritePos=4;
					}else if(this.estado==2) {
						this.spritePos=8;
					}
				}

			}
			if(this.contadorTuberia<4) { //El puente se elimina
				this.contadorTuberia++;
			}else if(this.contadorTuberia<20){
				if(this.contador<15) {
					this.puente[this.contador++].setId(0);
					this.contadorTuberia=0;
				}else {
					this.contadorTuberia=20;
				}
			}else if(this.contadorTuberia==20 || this.contadorTuberia==21) { //Bowser cae y mario caen
				for (int i=0;i<this.paredMovible.length;i++) {
					this.paredMovible[i].setId(0);
				}
				if(this.contadorTuberia==20) {
					this.efecto= new MakeSound("audio/smb_bowserfalls.wav");
					this.contadorTuberia++;
				}
				if(this.bowser.y<1000 || this.y+this.height<this.bloqueBandera.y) {
					if(this.bowser.y<1000) {
						this.bowser.y+=8;
					}
					if(this.y+this.height<this.bloqueBandera.y) {
						this.y+=6;
					}
				}else {
					this.contadorTuberia=22;
				}

			}else if(this.contadorTuberia==22 || this.contadorTuberia==23) { //Mario comienza a caminar

				if(this.contadorTuberia==22) { //Sonido de ganar
					this.efecto=new MakeSound("audio/smb_world_clear.wav");
					this.contadorTuberia=23;
					this.contador=0;
				}
				if(this.x>this.bloqueBandera.x+192) { //Mario cae un poco mas
					if(this.y+this.height<this.bloqueBandera.y+256) {
						this.y+=6;
					}
				}

				if(this.x<this.bloqueBandera.x+960) { //Mario comienza a caminar hasta toad

					if(this.estado==0) { //Animar mairo pequeño
						if(this.contador<5) {
							this.spritePos=12;
							this.contador++;
						}else if(this.contador<10) {
							this.spritePos=13;
							this.contador++;
						}else {
							this.contador=0;
						}
					}else if(this.estado==1) {
						if(this.contador<5) {
							this.spritePos=16;
							this.contador++;
						}else if(this.contador<10) {
							this.spritePos=17;
							this.contador++;
						}else {
							this.contador=0;
						}
					}else if(this.estado==2) {
						if(this.contador<5) {
							this.spritePos=20;
							this.contador++;
						}else if(this.contador<10) {
							this.spritePos=21;
							this.contador++;
						}else {
							this.contador=0;
						}
					}
					this.x+=5;
					this.ventana.setScroll(this.ventana.getScroll()+5);
				}else {
					this.contadorTuberia=24;
				}

			}else if(this.contadorTuberia==24) {

				if(this.estado==0) {
					this.spritePos=0;
				}else if(this.estado==1) {
					this.spritePos=4;
				}else if(this.estado==2) {
					this.spritePos=8;
				}
				this.canRestartGame=true;
			}
		}


	}

	public void setScrollPos(int scrollPos) {this.scrollPos=scrollPos;}

	public void setInicialPos(int inicialPosX, int inicialPosY) {
		this.inicialPosX=inicialPosX;
		this.inicialPosY=inicialPosY;
	}

	public void marioPerdio() {
		this.perder=true;
	}

	public void marioPerdioPoder() {
		this.efecto=new MakeSound("audio/smb_powerdown.wav");
		this.delayEnemigo=150;
		this.cambiandoSprite=false;
		this.cambiarEstado(0);
	}

	public boolean getDetenerJuego() {
		return this.detenerJuego;
	}

	public boolean getBloquesOscuros() {
		return this.bloquesOscuros;
	}

	public Mario getMario() {
		return this;
	}

	public int getDelayEnemigo() {
		return this.delayEnemigo;
	}

	public void setBloquesOscuros(boolean bloquesOscuros) {
		this.bloquesOscuros=bloquesOscuros;
	}

	public void keyPressed(int k) {
		if(k==KeyEvent.VK_RIGHT) {
			this.moverDerecha = true;
			this.previousKey=k;

		}
		if(k==KeyEvent.VK_LEFT) {
			this.moverIzquierda=true;
			this.previousKey=k;
		}
		if(k==KeyEvent.VK_UP) {
			if(this.previousKey==KeyEvent.VK_RIGHT) {
				this.moverDerecha=true;
			}else if(this.previousKey==KeyEvent.VK_LEFT) {
				this.moverIzquierda=true;
			}
			this.jumping = true;
		}

		if(k==KeyEvent.VK_DOWN) {
			//this.x=10000;
			//this.y=100;
			//this.scrollPos=10000;
			//this.ventana.setScroll(this.ventana.getScroll()+this.scrollPos);
		}

		if(k==KeyEvent.VK_SPACE) {
			if(this.estado==2) {
				if(this.canSpawnFireball && this.delayFireball==0) {
					this.spawnFireball();
					this.canSpawnFireball=false;
					this.delayFireball=15;
				}
			}
		}

		if(k==KeyEvent.VK_ENTER) {
			if(this.canRestartGame) {
				for(int i=0;i<3;i++) {
					this.ventana.jp.getGe().estados.remove(this.ventana.jp.getGe().estados.peek());
				}
				this.ventana.setScroll(0);
				this.ventana.jp.getGe().setNivelGlobal(0);
				this.ventana.jp.getGe().setTimer(0);
				this.ventana.jp.getGe().setMonedas(0);
				this.ventana.jp.getGe().setScore(0);
				this.ventana.jp.getGe().estados.push(new Menu(this.ventana.jp.getGe()));
			}
		}
	}

	public void keyReleased(int k) {
		if(k==KeyEvent.VK_RIGHT) {
			this.previousKey=0;
			this.moverDerecha = false;
		}
		if(k==KeyEvent.VK_LEFT) {
			this.previousKey=0;
			this.moverIzquierda=false;
		}
		if(k==KeyEvent.VK_UP) { 
			if(!this.miniJump) {
				this.jumping = false;
			}
			this.cayendo=true;
			this.contadorJump=0;
		}

		if(k==KeyEvent.VK_SPACE) {
			this.canSpawnFireball=true;
		}

	}
}