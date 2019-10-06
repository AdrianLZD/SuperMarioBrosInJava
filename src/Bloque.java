//Jose Adrian Lozano Dominguez A01631017

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Bloque extends Rectangle {
	private static final long serialVersionUID = 1L;
	private int id,
				contador=0,
				contadorBloqueMonedas=0,
				spritePos;
	private boolean arriba=false,
					animacion=false;
	public static int blockSize = 64;
	protected static BufferedImage[] sprite =  {null, null, null, null, null, null, null, null, null, null, null, //10
												null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, //26
												null, null, null, null, null, null, null, null}; //34
	static {
		try {
			sprite[1] = ImageIO.read(new File("images/blocks/bloque_suelo.png"));  
			sprite[2] = ImageIO.read(new File("images/blocks/bloque_rompible.png")); 
			sprite[3] = ImageIO.read(new File("images/blocks/bloque_misterio.png"));
			sprite[4] = ImageIO.read(new File("images/blocks/bloque_solido.png"));
			sprite[5] = ImageIO.read(new File("images/blocks/bloque_tuberia_abajoi.png"));
			sprite[6] = ImageIO.read(new File("images/blocks/bloque_tuberia_abajod.png"));
			sprite[7] = ImageIO.read(new File("images/blocks/bloque_tuberia_arribai.png"));
			sprite[8] = ImageIO.read(new File("images/blocks/bloque_tuberia_arribad.png"));
			sprite[9] = ImageIO.read(new File("images/blocks/bloque_usado.png"));
			sprite[10] = ImageIO.read(new File("images/blocks/bloque_bandera_poste.png"));
			sprite[11] = ImageIO.read(new File("images/blocks/bloque_bandera_punta.png"));
			sprite[12] = sprite[2]; //Breakable block with many coins
			sprite[13] = sprite[3]; //Mistery block with poweup
			sprite[14] = sprite[2]; //Breakable block with life
			sprite[15] = ImageIO.read(new File("images/blocks/bloque_final.png")); //Next level block
			 //sprite[16]  //Breakable with powerup
			sprite[17] = ImageIO.read(new File("images/blocks/bloque_rompible_roto1.png"));
			sprite[18] = ImageIO.read(new File("images/blocks/bloque_rompible_roto2.png"));
			sprite[19] = ImageIO.read(new File("images/blocks/bloque_rompible_roto3.png"));
			
			//Dark blocks
			sprite[20] = ImageIO.read(new File("images/blocks/bloque_suelo_oscuro.png"));
			sprite[21] = ImageIO.read(new File("images/blocks/bloque_rompible_oscuro.png"));
			sprite[22] = ImageIO.read(new File("images/blocks/bloque_solido_oscuro.png"));
			
			//Flip pipes
			sprite[23]= ImageIO.read(new File("images/blocks/bloque_tuberia_volteada1.png"));
			sprite[24]= ImageIO.read(new File("images/blocks/bloque_tuberia_volteada2.png"));
			sprite[25]= ImageIO.read(new File("images/blocks/bloque_tuberia_volteada3.png"));
			sprite[26]= ImageIO.read(new File("images/blocks/bloque_tuberia_volteada4.png"));
			
			//Grass
			sprite[27]= ImageIO.read(new File("images/blocks/bloque_pasto_orilla_izquierda.png"));
			sprite[28]= ImageIO.read(new File("images/blocks/bloque_pasto.png"));
			sprite[29]= ImageIO.read(new File("images/blocks/bloque_pasto_orilla_derecha.png"));
			sprite[30]= ImageIO.read(new File("images/blocks/bloque_pasto_soporte.png"));
			
			//Empty block for collisions
			sprite[31]=null;
			
			//Castle blocks
			sprite[32] =ImageIO.read(new File("images/blocks/bloque_suelo_castillo.png"));
			sprite[33] =ImageIO.read(new File("images/blocks/bloque_puente.png"));
			//Block to close path on bowser level
			sprite[34] = sprite[32];
			
			
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar los sprites de los bloques", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	

	public Bloque(int x, int y, int id) {
		this.id=id;
		this.spritePos=this.id;
		this.setBounds(x, y, blockSize, blockSize);
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public void animarArriba() {
		this.y-=10;
		this.arriba=true;
	}
	public void animarAbajo() {
		if(this.arriba) {
			this.y+=10;
			this.arriba=false;
		}
		
	}
	
	public void tick() {
		if(this.id!=0) {
			this.spritePos=this.id;
		}
		
		if((this.id==2 || this.id==12 || this.id==9) && this.animacion) {
			if(this.contador<8) {
				this.y-=2;
				this.contador++;
			}else if(this.contador<16) {
				this.y+=2;
				this.contador++;
			}else {
				this.animacion=false;
				this.contador=0;
			}
		}
		
		if(this.contadorBloqueMonedas>0) {
			this.contadorBloqueMonedas++;
		}
		
		if(this.id==17) {
			if(this.contador<4) {
				this.spritePos=17;
				this.contador++;
			}else if(this.contador<8) {
				if(this.contador==4) {
					this.x-=8;
					this.y-=8;
				}
				this.spritePos=18;
				this.contador++;
			}else if(this.contador<12) {
				if(this.contador==8) {
					this.x-=8;
					this.y-=8;
				}
				this.spritePos=19;
				this.contador++;
			}else {
				this.id=0;
			}
		}
	}
	
	public void collision(Mario mario, Bloque[][] b, Objeto[][] o, int direction, int i, int j) {
		if(direction == 2) {
			if(this.id==34) {
				b[i-1][j-2].id=32;
				b[i-2][j-2].id=32;
				b[i-3][j-2].id=32;
				b[i-4][j-2].id=32;
				b[i-5][j-2].id=32;
			}
			
		}else if(direction == 3) {
			if(this.id==2) { //Bloque rompible sin nada
				if(mario.estado==0) {
					mario.efecto=new MakeSound("audio/smb_bump.wav");
					b[i][j].animacion=true;
				}else {
					b[i][j].id=17;
					mario.efecto = new MakeSound("audio/smb_breakblock.wav");
				}
			}else if(this.id==3){ //Bloque misterioso moneda
				this.id=9;
				this.animacion=true;
				o[i][j].setId(3);
				GameNivel.ge.sumarMoneda();
				GameNivel.ge.sumarScore(100);

			}else if(this.id==9) {
				mario.efecto=new MakeSound("audio/smb_bump.wav");

			}else if(this.id==12){  //Bloque con muchas monedas
				if(this.contadorBloqueMonedas==0) {
					this.iniciarContadorBloqueMonedas();
					this.setAnimacion(true);
					o[i][j] = new Objeto(this.x, this.y, 3);
				}else if(this.contadorBloqueMonedas<300) {
					this.setAnimacion(true);
					o[i][j] = new Objeto(this.x, this.y, 3);
					GameNivel.ge.sumarScore(100);
					GameNivel.ge.sumarMoneda();
				}else {
					o[i][j] = new Objeto(b[i][j].x, b[i][j].y, 3);
					b[i][j].setAnimacion(true);
					GameNivel.ge.sumarScore(100);
					GameNivel.ge.sumarMoneda();
					b[i][j].setId(9);

				}

			}else if(this.id==13) { //Bloque misterioso powerup
				this.id=9;
				this.animacion=true;
				if(mario.estado==0) {
					o[i][j].setId(1);
				}else if(mario.estado==1) {
					o[i][j].setId(4);
				}else if(mario.estado==2) {
					o[i][j].setId(4);
				}
			}else if(this.id==14) { //Bloque rompible con vida extra que no hace nada
				this.id=9;
				this.animacion = true;
				o[i][j].setId(2);
			}
		}else if(direction == 1) {
			if(this.id==10 || this.id==11) {
				mario.bloqueBandera=b[11][j];
				mario.bandera=o[2][j-1];
				mario.animacionFinal=true;
				if(GameNivel.ge.getNivelGlobal()==1) {
					mario.bloqueFinal=b[i][j+5];
				}else if(GameNivel.ge.getNivelGlobal()==2) {
					mario.bloqueFinal=b[i][j+4];
				}else if(GameNivel.ge.getNivelGlobal()==3) {
					mario.bloqueFinal=b[i][j+6];
				}else {
					mario.bloqueFinal=b[i][j+5];
				}
			}else if(this.id ==15) {
				mario.ganar = true;
			}else if(this.id == 23 || this.id==24) {
				if(GameNivel.ge.getNivelGlobal()==2) {
					mario.moviendoAlFinal = true;
				}
			}
			
		}
		
	}
	
	public void setAnimacion(boolean animacion) {
		this.animacion=animacion;
	}
	
	public int getContadorBloqueMonedas() {
		return this.contadorBloqueMonedas;
	}
	
	public void iniciarContadorBloqueMonedas() {
		this.contadorBloqueMonedas=1;
	}
	
	public void pinta(Graphics g) {
		if(this.id!=0) {
			if(GameNivel.ge.estados.peek().getMario()!=null) {
				if(this.id==1) {
					if(GameNivel.ge.estados.peek().getMario().getBloquesOscuros()) {
						this.spritePos=20;
					}
				}else if(this.id==2) {
					if(GameNivel.ge.estados.peek().getMario().getBloquesOscuros()) {
						this.spritePos=21;
					}
				}else if(this.id==4) {
					if(GameNivel.ge.estados.peek().getMario().getBloquesOscuros()) {
						this.spritePos=22;
					}
				}else if(this.id==12) {
					if(GameNivel.ge.estados.peek().getMario().getBloquesOscuros()) {
						this.spritePos=21;
					}
				}else if(this.id==14) {
					if(GameNivel.ge.estados.peek().getMario().getBloquesOscuros()) {
						this.spritePos=21;
					}
				}
			}
			g.drawImage(sprite[this.spritePos], this.x, this.y, GameNivel.ge.jp);
		}
	}
}
