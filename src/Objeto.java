
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Objeto extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private boolean movimiento,
					activarSonido;
	public static final int objectSize = 64;
	private int velocidad = 3,
				contador=0,
				spritePos;
	private boolean cayendo,
					direccion=true;
	
	private AffineTransform at,
							atImage;
	public Shape outline;
	
	
	@SuppressWarnings("unused")
	private MakeSound efecto;
	protected static BufferedImage[] sprite =  {null, null, null, null, null, null, null, null, null, null, null }; //10
	static {
		try {
			sprite[1] = ImageIO.read(new File("images/objects/objeto_hongo_normal.png")); 
			sprite[2] = ImageIO.read(new File("images/objects/objeto_hongo_vida.png"));  
			sprite[3] = ImageIO.read(new File("images/objects/objeto_moneda.png")); 
			sprite[4] = ImageIO.read(new File("images/objects/objeto_flor_fuego.png"));
			sprite[5] = ImageIO.read(new File("images/objects/objeto_bandera.png"));
			sprite[6] = sprite[3]; //Moneda estatica
			
			//Tira de fuego
			sprite[7] = ImageIO.read(new File("images/objects/tira_fuego1.png"));
			sprite[8] = ImageIO.read(new File("images/objects/fuego1.png"));
			sprite[9] = ImageIO.read(new File("images/objects/fuego2.png"));
			sprite[10] = ImageIO.read(new File("images/objects/martillo.png"));
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar los sprites de los objetos", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Objeto(int x, int y, int id) {
		this.id=id;
		this.activarSonido=true;
		if(this.id==5) {
			this.setBounds(x+28, y, objectSize, objectSize);
		}else if(this.id==7) {
			this.setBounds(x+24, y+16, sprite[7].getWidth(), sprite[7].getHeight());
			this.at = AffineTransform.getTranslateInstance(0, 0);
			this.atImage = AffineTransform.getTranslateInstance(this.x, this.y);
			this.outline=this.at.createTransformedShape(this);
		}else if(this.id==8) {
			this.setBounds(x, y, sprite[8].getWidth(), sprite[8].getHeight());
			this.spritePos=8;
		}else {
			this.setBounds(x, y, objectSize, objectSize);
		}
		
	}
	
	public void setId(int id) {
		this.id=id;
		if(this.id==3) {
			this.contador=0;
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public void collision(Mario mario, Bloque[][] b, Enemigos[][] e, int i, int j) {
		//Power Ups
		if(this.id==1) { //Hongo normal
			mario.cambiarEstado(1);
			mario.cambiandoSprite=false;
			this.efecto=new MakeSound("audio/smb_powerup.wav");
			GameNivel.ge.sumarScore(1000);
			this.id=0;

		}else if(this.id==2) { //Hongo de vida
			GameNivel.ge.estados.get(GameNivel.ge.estados.size()-2).agregarVida();
			this.efecto=new MakeSound("audio/smb_1up.wav");
			GameNivel.ge.sumarScore(1000);
			this.id=0;

		}else if(this.id==4) { //Flor de fuego
			mario.cambiarEstado(2);
			mario.cambiandoSprite=false;
			this.efecto=new MakeSound("audio/smb_powerup.wav");
			GameNivel.ge.sumarScore(1000);
			this.id=0;

		}else if(this.id==5) { //Si tocas la bandera te dan una vida
			if(!mario.tocoBandera) {
				GameNivel.ge.sumarScore(1000);
				GameNivel.ge.estados.get(GameNivel.ge.estados.size()-2).agregarVida();
				this.efecto=new MakeSound("audio/smb_powerup.wav");
				mario.tocoBandera=true;
			}

		}else if(this.id==6) { //Moneda estatica
			this.efecto=new MakeSound("audio/smb_coin.wav");
			GameNivel.ge.sumarMoneda();
			GameNivel.ge.sumarScore(100);
			this.id=0;

		}else if(this.id==7) { //Tira fuego
			if(mario.delayEnemigo==0) {
				if(mario.estado==0) {
					mario.perder=true;
				}else {
					this.efecto=new MakeSound("audio/smb_powerdown.wav");
					mario.delayEnemigo=150;
					mario.cambiandoSprite=false;
					mario.cambiarEstado(0);
				}
			}

			//Fuego que se mueve
		}else if(this.id==8){
			if(mario.delayEnemigo==0) {
				if(mario.estado==0) {
					mario.perder=true;
				}else {
					this.efecto=new MakeSound("audio/smb_powerdown.wav");
					mario.delayEnemigo=150;
					mario.cambiandoSprite=false;
					mario.cambiarEstado(0);
				}
			}

		}else if(this.id==10){ //Martillo para ganar
			if(mario.y+mario.height<this.y+this.height) { //Solo activar martillo si estas a su altura
				int contadorPuente =0;
				mario.bloqueBandera=b[i+1][j];
				mario.puente= new Bloque[15];
				mario.paredMovible = new Bloque[6];
				mario.paredMovible[0]= b[i][j+1];
				mario.paredMovible[1]= b[i][j+2];
				mario.paredMovible[2]= b[i-1][j+1];
				mario.paredMovible[3]= b[i-1][j+2];
				mario.paredMovible[4]= b[i-2][j+1];
				mario.paredMovible[5]= b[i-2][j+2];
				for(int k=0; k<e.length;k++) {
					for(int l=0; l<e[0].length;l++) {
						if(e[k][l].getId()==16) {
							mario.bowser=e[k][l]; //Se define a bowser
						}

						if(b[k][l].getId()==33) {
							mario.puente[contadorPuente++]=b[k][l];
						}
					}
				}
				mario.moviendoAlFinal=true;
				mario.contador=0;
			}
		}				
	}
	
	public void tick(Bloque[][] b) {
		//Hongo
		if(this.id==1 || this.id==2) {
			if(this.contador<32) {
				if(this.activarSonido) {
					this.efecto = new MakeSound("audio/smb_powerup_appears.wav");
					this.activarSonido=false;
				}
				this.y-=2;
				this.contador++;
			}else {
				this.movimiento=true;
			}
			if(this.movimiento) {
				for(int i=0;i<b.length;i++) {
					for(int j=0;j<b[0].length;j++) {
						if(b[i][j].getId()!=0 && b[i][j].getId()!=30 && b[i][j].getId()!=31 && b[i][j].getId()!=17) { //No chocar con bloques vacios
							//Lado izquierdo
							if((Collision.marioCollides(new Point(this.x, this.y+this.velocidad), b[i][j]) 
									|| Collision.marioCollides(new Point(this.x, this.y+this.height-this.velocidad-4), b[i][j]))
									&& !Collision.marioCollides(new Point(this.x+this.velocidad+3, this.y-3), b[i][j])) {
								this.direccion=true;
							}

							//Lado derecho
							if((Collision.marioCollides(new Point(this.x+this.width, this.y+this.velocidad), b[i][j]) 
									|| Collision.marioCollides(new Point(this.x+this.width, this.y+this.height-this.velocidad-4), b[i][j]))
									&& !Collision.marioCollides(new Point(this.x+this.width-this.velocidad, this.y-3), b[i][j])) {
								this.direccion=false;
							}

							//Debajo
							if((Collision.marioCollides(new Point(this.x+1, this.y+this.height+2), b[i][j]) 
									|| Collision.marioCollides(new Point(this.x+this.width-2, this.y+this.height+2), b[i][j]))
									&& !Collision.marioCollides(new Point(this.x-1, this.y+this.height-this.velocidad-2), b[i][j]) 
									&& !Collision.marioCollides(new Point(this.x+this.width, this.y+this.height-this.velocidad-2), b[i][j])) {
								this.y=b[i][j].y-this.height-8;
								this.cayendo=false;
							}else {
								this.cayendo=true;
							}
						}
					}
				}	
						
				if(this.direccion) {
					this.x+=this.velocidad;
				}else {
					this.x-=this.velocidad;
				}
						
				if(this.cayendo) {
					this.y+=velocidad+3;	
				}
			}
			
			
		}
		
		//Moneda que desaparece
		if(this.id==3) {
			if(this.contador<10) {
				this.y-=8;
				this.contador++;
			}else if(this.contador<50) {
				this.id=0;
				this.efecto = new MakeSound("audio/smb_coin.wav");
				//Sumar puntos
			}
		}
		
		//Flor de fuego
		if(this.id==4) {
			if(this.contador<32) {
				if(this.activarSonido) {
					this.efecto = new MakeSound("audio/smb_powerup_appears.wav");
					this.activarSonido=false;
				}
				this.y-=2;
				this.contador++;
			}
		}
		
		//Tira de fuego
		if(this.id==7) {
			this.at.rotate(Math.toRadians(1.5), this.x+8, this.y+8);
			this.atImage.rotate(Math.toRadians(1.5), 8, 8);
			this.outline= this.at.createTransformedShape(this.getBounds());
		}
		
		//Fuego que se mueve salu2
		if(this.id==8) {
			if(this.contador<10) {
				this.spritePos=8;
				this.contador++;
			}else if(this.contador<20) {
				this.spritePos=9;
				this.contador++;
			}else {
				this.contador=0;
			}
			this.x-=6;
		}
	}
	
	
	public void pinta(Graphics g) {
		if(this.id!=0) {
			if(this.id!=7) {
				if(this.id==8) {
					g.drawImage(sprite[this.spritePos], this.x, this.y, GameNivel.ge.jp);
				}else {
					g.drawImage(sprite[this.id], this.x, this.y, GameNivel.ge.jp);
				}
			}else {
				Graphics2D g2d = (Graphics2D)g;
				g2d.drawImage(sprite[7],this.atImage, GameNivel.ge.jp);				
			}
		}
	}
}
