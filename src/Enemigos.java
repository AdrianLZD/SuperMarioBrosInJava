
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Enemigos extends Rectangle{
	private static final long serialVersionUID = 1L;
	
	private boolean movimiento,
					direccion,
					aplastable=true,
					colisiones=true,
					cayendo,
					muerto;
	private int id,
				spritePos,
				velocidad=2,
				velocidadCaida=6,
				contadorSprite,
				contador,
				contadorVolador,
				contadorFireball;
	private static ArrayList<Fireball> fireballs;
	@SuppressWarnings("unused")
	private static MakeSound efecto;
	
	public static BufferedImage[] sprite = {null, null, null, null, null, null, null, null, null, null, null, //10
											null, null, null, null, null, null, null, null, null, null}; //19 
	static {
		try {
			//Goomba and koopa
			sprite[1] = ImageIO.read(new File("images/enemies/goomba_derecha.png"));  
			sprite[2] = ImageIO.read(new File("images/enemies/goomba_izquierda.png"));  
			sprite[3] = ImageIO.read(new File("images/enemies/koopa_derecha1.png"));  
			sprite[4] = ImageIO.read(new File("images/enemies/koopa_derecha2.png"));  
			sprite[5] = ImageIO.read(new File("images/enemies/koopa_izquierda1.png"));  
			sprite[6] = ImageIO.read(new File("images/enemies/koopa_izquierda2.png"));
			
			//Dead Enemies
			sprite[7] = ImageIO.read(new File("images/enemies/goomba_aplastado.png"));
			sprite[8] = ImageIO.read(new File("images/enemies/koopa_shell.png"));
			sprite[9] = ImageIO.read(new File("images/enemies/goomba_volteado.png"));
			sprite[10] = ImageIO.read(new File("images/enemies/koopa_volteado.png"));
			sprite[11] = ImageIO.read(new File("images/enemies/koopa_shell_volteado.png"));
			
			//Piranha and flying koopa
			sprite[12] = ImageIO.read(new File("images/enemies/piranha_abierta.png"));
			sprite[13] = ImageIO.read(new File("images/enemies/piranha_cerrada.png"));
			sprite[14] = ImageIO.read(new File("images/enemies/koopa_volador1.png"));
			sprite[15] = ImageIO.read(new File("images/enemies/koopa_volador2.png"));
			
			//Bowser 
			sprite[16] = ImageIO.read(new File("images/enemies/bowser1.png"));
			sprite[17] = ImageIO.read(new File("images/enemies/bowser1.png"));
			sprite[18] = ImageIO.read(new File("images/enemies/bowser1.png"));
			sprite[19] = ImageIO.read(new File("images/enemies/bowser1.png"));
			
			
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar los sprites de los enemigos", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void iniciarContador() {
		this.contador=1;
	}
	public Enemigos(int x, int y, int id) {
		this.id=id;
		this.spritePos=id;
		if(this.id==14 || this.id==12 || this.id==16) {
			this.movimiento=false;
			if(this.id==12 || this.id==16) {
				this.colisiones=false;
			}

		}else{
			this.movimiento=true;
		}
		
		if(this.id==16) {
			Enemigos.fireballs = new ArrayList<Fireball>();
		}
		this.cayendo=false;
		this.direccion=false;
		this.contadorSprite=0;
		if(this.id!=0) {
			if(this.id==3) {
				this.setBounds(x, y-16, sprite[this.spritePos].getWidth(), sprite[this.spritePos].getHeight());
			}else if(this.id==12) {
				this.setBounds(x-24, y, sprite[this.spritePos].getWidth(), sprite[this.spritePos].getHeight());
			}else {
				this.setBounds(x, y, sprite[this.spritePos].getWidth(), sprite[this.spritePos].getHeight());
			}
		}
	}
	
	public void pinta(Graphics g) {
		if(this.id!=0) {
			g.drawImage(sprite[this.spritePos], this.x, this.y, GameNivel.ge.jp);
			if(this.id==16) {
				if(!Enemigos.fireballs.isEmpty()) {
					for(int i=0;i<Enemigos.fireballs.size();i++) {
						Enemigos.fireballs.get(i).pinta(g);
					}
				}
			}
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		if(this.id==14 && id==3) {
			this.cayendo=true;
			this.movimiento=true;
		}
		this.id=id;
	}
	
	public void setMovimiento(boolean movimiento) {
		this.movimiento=movimiento;
	}
	
	public boolean getMuerto() {
		return this.muerto;
	}
	
	public void matarEnemigo() {
		this.muerto=true;
	}
	
	public void revivirEnemigo() {
		this.muerto=false;
	}
	
	public int getSpritePos() {
		return this.spritePos;
	}
	
	public void setDireccion(boolean direccion) {
		this.direccion=direccion;
	}
	
	public boolean enemigoCollision(Point p, Bloque b, Enemigos e) {
		if(b.contains(p)) {
			return true;
		}else if(e.contains(p)) {
			return true;
		}else {
			return false;
		}
	}
	
	public void setAplastable(boolean aplastable) {
		this.aplastable=aplastable;
	}
	
	public void setSpritePos(int spritePos) {
		this.spritePos=spritePos;
	}
	
	public void tick(Bloque[][] b, Enemigos[][] e) {	
		
		
		//Eliminar enemigo si se cae
		if(this.y>1100) {
			this.id=0;
		}
		
		if(this.id!=0) {
			if(this.id==12) { //Piranha
				if(this.contadorVolador<39) {
					this.y-=2;
					this.contadorVolador++;
				}else if(this.contadorVolador<139) {
					this.contadorVolador++;
				}else if(this.contadorVolador<178) {
					this.y+=2;
					this.contadorVolador++;
				}else if(this.contadorVolador<278) {
					this.contadorVolador++;
				}else {
					this.contadorVolador=0;
				}
				
			}else if(this.id==14) { //Tortuga voladora
				if(this.contadorVolador<100) {
					this.y-=2;
					this.contadorVolador++;
				}else if(this.contadorVolador<200) {
					this.y+=2;
					this.contadorVolador++;
				}else {
					this.contadorVolador=0;
				}
				
			}else if(this.id==16) { //Bowser
				if(!Enemigos.fireballs.isEmpty()) {
					for(int i=0; i<Enemigos.fireballs.size();i++) {
						Enemigos.fireballs.get(i).tick(b, e, GameNivel.ge.estados.peek().getMario());
						if(Enemigos.fireballs.get(i).getId()==0) {
							Enemigos.fireballs.remove(i);
						}
					}
				}
				
				
				if(this.contadorFireball<100) {
					this.contadorFireball++;
				}else {
					Enemigos.fireballs.add(new Fireball(this.x, this.y+64, 8, false, GameNivel.ge));
					this.contadorFireball=0;
				}
				if(this.contadorVolador<200) {
					if(this.contador<50) {
						this.contador++;
					}else if(this.contador<90) {
						this.y-=6;
						this.contador++;
					}else if(this.contador<130){
						this.y+=6;
						this.contador++;
					}else if(this.contador<200){
						this.contador++;
					}else {
						this.contador=0;
					}
					this.x-=1;
					this.contadorVolador++;
					
				}else if(this.contadorVolador<400) {
					if(this.contador<50) {
						this.contador++;
					}else if(this.contador<80) {
						this.y-=6;
						this.contador++;
					}else if(this.contador<110){
						this.y+=6;
						this.contador++;
					}else if(this.contador<200){
						this.contador++;
					}else {
						this.contador=0;
					}
					this.x+=1;
					this.contadorVolador++;
				}else {
					this.contadorVolador=0;
				}
			}
			
				if(this.colisiones) { //Colisiones con los bloques
					for(int i=0;i<b.length;i++) {
						for(int j=0;j<b[0].length;j++) {
							if(b[i][j].getId()!=0 && b[i][j].getId()!=30) {
								if(b[i][j].getId()!=17) {
									//Enemigos con bloques
									//Lado izquierdo
									if((Collision.marioCollides(new Point(this.x, this.y+this.velocidad), b[i][j]) 
										|| Collision.marioCollides(new Point(this.x, this.y+this.height-this.velocidad-4), b[i][j]))
										&& !Collision.marioCollides(new Point(this.x+this.velocidad+3, this.y-3), b[i][j])) {
										if(this.id==9) {
											Enemigos.efecto=new MakeSound("audio/smb_bump.wav");
										}
											this.direccion=true;
									}
										
									//Lado derecho
									if((Collision.marioCollides(new Point(this.x+this.width, this.y+this.velocidad), b[i][j]) 
										|| Collision.marioCollides(new Point(this.x+this.width, this.y+this.height-this.velocidad-4), b[i][j]))
										&& !Collision.marioCollides(new Point(this.x+this.width-this.velocidad-3, this.y-3), b[i][j])) {
										if(this.id==9) {
											Enemigos.efecto=new MakeSound("audio/smb_bump.wav");
										}
											this.direccion=false;
										}
										
										//Debajo
									if((Collision.marioCollides(new Point(this.x+1, this.y+this.height+2), b[i][j]) 
										|| Collision.marioCollides(new Point(this.x+this.width-2, this.y+this.height+2), b[i][j]))
										&& !Collision.marioCollides(new Point(this.x-1, this.y+this.height-this.velocidadCaida-2), b[i][j]) 
										&& !Collision.marioCollides(new Point(this.x+this.width, this.y+this.height-this.velocidadCaida-2), b[i][j])) {
											this.y=b[i][j].y-this.height-6;
											this.cayendo=false;
									}else {
										if(this.id!=14) {
											this.cayendo=true;
										}
									}
								}
							}
						}
					}	
				}
				//Caparazon con enemigos
				if(this.id==9) {
					for(int i=0;i<e.length;i++) {
						for(int j=0;j<e[0].length;j++) {
							if(e[i][j].getId()!=0 && e[i][j].getId()!=9) {
								if(GameNivel.ge.estados.peek().getMario()!=null) {
									if(Math.abs(GameNivel.ge.estados.peek().getMario().x- e[i][j].x) < (GameNivel.ge.jp.getVentana().getWidth()/5)*3) { //Solo matar enemigos en pantalla
										if(Collision.enemigoCollides(new Point(this.x+2, this.y+2), e[i][j]) 
											|| Collision.enemigoCollides(new Point(this.x, this.y+this.height), e[i][j])
											|| Collision.enemigoCollides(new Point(this.x+this.width, this.y), e[i][j])
											|| Collision.enemigoCollides(new Point(this.x+this.width-2, this.y+this.height-2), e[i][j])) {
											if(e[i][j].aplastable) {
												Enemigos.efecto= new MakeSound("audio/smb_stomp.wav");
											}
											if(e[i][j].getId()==14) {
												e[i][j].setId(3);
											}
											e[i][j].aplastable=false;
											e[i][j].matarEnemigo();
											
										}
									}
								}
							}
						}
					}
				}
				
				
				if(!this.muerto) {
					if(this.movimiento) {
						if(this.direccion) {
							if(this.id==1) {
								this.spritePos=1;
							}else if(this.id==3) {
								if(this.contadorSprite<15) {
									this.spritePos=3;
									this.contadorSprite++;
								}else if(this.contadorSprite<30) {
									this.spritePos=4;
									this.contadorSprite++;
								}else {
									this.contadorSprite=0;
								}
							}
							if(this.id!=9) {
								this.x+=this.velocidad;
							}else {
								this.velocidad=6;
								this.x+=this.velocidad;
							}
						}else {
							if(this.id==1) {
								this.spritePos=2;
							}else if(this.id==3) {
								if(this.contadorSprite<15) {
									this.spritePos=5;
									this.contadorSprite++;
								}else if(this.contadorSprite<30) {
									this.spritePos=6;
									this.contadorSprite++;
								}else {
									this.contadorSprite=0;
								}
							}
							
							if(this.id!=9) {
								this.x-=this.velocidad;
							}else {
								this.velocidad=6;
								this.x-=this.velocidad;
							}
						}
					}else {
						if(this.id==14) { //Tortuga voladora
							if(this.contadorSprite<15) {
								this.spritePos=14;
								this.contadorSprite++;
							}else if(this.contadorSprite<30) {
								this.spritePos=15;
								this.contadorSprite++;
							}else {
								this.contadorSprite=0;
							}
						}else if(this.id==12) {
							if(this.contadorSprite<15) {
								this.spritePos=12;
								this.contadorSprite++;
							}else if(this.contadorSprite<30) {
								this.spritePos=13;
								this.contadorSprite++;
							}else {
								this.contadorSprite=0;
							}
						}
					}
				}else {
					//Muerto igual true
					if(this.id==1 || this.id==2) {
						if(this.aplastable) {
							this.spritePos=7;
							if(this.contador<30) {
								this.contador++;
							}else {
								this.id=0;
							}
						}else {
							this.spritePos=9;
							this.colisiones=false;
							if(this.contador<10) {
								this.y-=20;
								this.contador++;
							}else if(this.contador<45){
								this.contador++;
							}else {
								this.id=0;
							}
							
						}
					}else if(this.id==3 || this.id==4) {
						if(this.aplastable) {
							this.spritePos=8;
							this.setBounds(this.x, this.y+(sprite[3].getHeight()-sprite[8].getHeight()), sprite[this.spritePos].getWidth(), sprite[this.spritePos].getHeight());
							this.id=8;
							this.muerto=false;
							this.movimiento=false;
						}else {
							this.spritePos=10;
							this.colisiones=false;
							if(this.contador<10) {
								this.y-=20;
								this.contador++;
							}else if(this.contador<45){
								this.contador++;
							}else {
								this.id=0;
							}
						}
					}else if(this.id==8) {
						if(!this.aplastable) {
							this.spritePos=11;
							this.colisiones=false;
							if(this.contador<10) {
								this.y-=20;
								this.contador++;
							}else if(this.contador<45){
								this.contador++;
							}else {
								this.id=0;
							}
						}
					}else if(this.id==9) {
						if(this.aplastable) {
							this.id=8;
							this.muerto=false;
							this.movimiento=false;
						}else {
							this.spritePos=11;
							this.colisiones=false;
							if(this.contador<10) {
								this.y-=20;
								this.contador++;
							}else if(this.contador<45){
								this.contador++;
							}else {
								this.id=0;
							}
						}
					}
				}
				if(this.cayendo) {
					this.y+=this.velocidadCaida+3;	
				}
			}	
		}
}
