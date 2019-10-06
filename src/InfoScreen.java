
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class InfoScreen extends GameNivel{
	private int vidas,
				marioXPos,
				marioYPos,
				scrollPos,
				contador;
	private boolean nivelCreado,
					marioMurio;
	private static BufferedImage marioSprite = null;
	@SuppressWarnings("unused")
	private MakeSound efecto;
	static {
		try {
			marioSprite = ImageIO.read(new File("images/mario/mario_pequeño_derecha.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar la imagen de Mario", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public InfoScreen(int nivel,int marioXPos, int marioYPos, int vidas, int scrollPos, GameEstado ge) {
		super(ge);		
		this.vidas=vidas;
		this.contadorTime=0;
		this.marioXPos=marioXPos;
		this.marioYPos=marioYPos;
		this.scrollPos=scrollPos;
	}
	
	public boolean getMarioMurio() {
		return this.marioMurio;
	}
	
	public void marioMurio(int marioXPos, int marioYPos, int scrollPos) {
		this.vidas--;
		this.marioXPos=marioXPos;
		this.marioYPos=marioYPos;
		this.scrollPos=scrollPos;
		this.contadorTime=0;
		JuegoPanel.ge.ventana.setScroll(0);
		JuegoPanel.ge.setTimer(360);
		this.marioMurio=true;
	}
	
	public void agregarVida() {
		this.vidas++;
	}
	
	public void changeNivel() {
		//JuegoPanel.ge.ventana.setScroll(0);
		//this.nivelGlobal++;
		//this.mundo=((this.nivelGlobal-1)/4)+1;
		//this.nivel=((this.nivelGlobal-1)%4)+1;
	}
	
	public void tick() {
		if(this.vidas>0) {
			if(this.contadorTime<125) {
				this.contadorTime++;
			}else {
				if(JuegoPanel.ge.getNivelGlobal()==1) {
					if(!this.nivelCreado) {
						JuegoPanel.ge.estados.push(new Nivel(this.marioXPos,this.marioYPos,this.scrollPos, JuegoPanel.ge.ventana));
						this.nivelCreado=true;
					}else {
						JuegoPanel.ge.estados.remove(JuegoPanel.ge.estados.size()-1); //Se quita la nueva infoScreen
						if(this.marioMurio) {
							JuegoPanel.ge.ventana.setScroll(this.scrollPos); //Se coloca el scroller donde debe
							JuegoPanel.ge.estados.remove(JuegoPanel.ge.estados.size()-1);//Se quita el nivel viejo
							JuegoPanel.ge.estados.push(new Nivel(this.marioXPos,this.marioYPos,this.scrollPos,JuegoPanel.ge.ventana)); //Se hace un nuevo nivel
							this.marioMurio=false;
						}else {
							JuegoPanel.ge.estados.peek().defineNivel(); //Se cambia de nivel
						}
						
					}
				}else if(JuegoPanel.ge.getNivelGlobal()==2) {
					this.reiniciarNivel();
				}else if(JuegoPanel.ge.getNivelGlobal()==3) {
					this.reiniciarNivel();
				}else if(JuegoPanel.ge.getNivelGlobal()==4) {
					this.reiniciarNivel();
				}
			}
		}else {
			if(this.contador<300) {
				if(this.contador==0) {
					this.efecto= new MakeSound("smb_gameover.wav");
				}
				this.contador++;
			}else {
				for(int i=0;i<4;i++) {
					JuegoPanel.ge.estados.remove(JuegoPanel.ge.estados.peek()); //Se quita todo lo actual
				}
				JuegoPanel.ge.ventana.setScroll(0);
				JuegoPanel.ge.setNivelGlobal(0);
				JuegoPanel.ge.setTimer(0);
				JuegoPanel.ge.setMonedas(0);
				JuegoPanel.ge.setScore(0);
				JuegoPanel.ge.estados.push(new Menu(JuegoPanel.ge));
			}
			
		}
	}
	
	public void reiniciarContadorTime() {
		this.contadorTime=0;
	}
	
	private void reiniciarNivel() {
		JuegoPanel.ge.estados.remove(JuegoPanel.ge.estados.size()-1); //Se quita la infoScreen nueva
		if(this.marioMurio) {
			JuegoPanel.ge.ventana.setScroll(this.scrollPos); //Se coloca el scroller donde debe
			JuegoPanel.ge.estados.remove(JuegoPanel.ge.estados.size()-1);//Se quita el nivel viejo
			JuegoPanel.ge.estados.push(new Nivel(this.marioXPos,this.marioYPos,this.scrollPos, JuegoPanel.ge.ventana)); //Se hace un nuevo nivel
			this.marioMurio=false;
		}else {
			JuegoPanel.ge.estados.peek().defineNivel(); //Se cambia de nivel
		}
	}
	
	public void pinta(Graphics g) {
		//Fondo negro
		g.setColor(Color.black);
		g.fillRect(0, 0, JuegoPanel.ge.ventana.getWidth()+200, JuegoPanel.ge.ventana.getHeight());
		
		g.setColor(Color.white);
		g.setFont(new Font("Helvetica", Font.BOLD, 35)); 
		//Informacion del nivel y mundo
		g.drawString("WORLD", JuegoPanel.ge.ventana.getWidth()/2-JuegoPanel.ge.ventana.getWidth()/10+JuegoPanel.ge.ventana.getWidth()/36, JuegoPanel.ge.ventana.getHeight()/2-JuegoPanel.ge.ventana.getHeight()/10);
		g.drawString(JuegoPanel.ge.getMundo()+" -", JuegoPanel.ge.ventana.getWidth()/2 +JuegoPanel.ge.ventana.getWidth()/32, JuegoPanel.ge.ventana.getHeight()/2-JuegoPanel.ge.ventana.getHeight()/10);
		g.drawString(" "+JuegoPanel.ge.getNivelGlobal(), JuegoPanel.ge.ventana.getWidth()/2+JuegoPanel.ge.ventana.getWidth()/35 +JuegoPanel.ge.ventana.getWidth()/32, JuegoPanel.ge.ventana.getHeight()/2-JuegoPanel.ge.ventana.getHeight()/10);
		
		//Mario y sus vidas
		g.drawString(this.vidas+"", JuegoPanel.ge.ventana.getWidth()/2+JuegoPanel.ge.ventana.getWidth()/30, JuegoPanel.ge.ventana.getHeight()/2);
		g.drawString("X", JuegoPanel.ge.ventana.getWidth()/2, JuegoPanel.ge.ventana.getHeight()/2);
		g.drawImage(marioSprite, JuegoPanel.ge.ventana.getWidth()/2-JuegoPanel.ge.ventana.getWidth()/20, JuegoPanel.ge.ventana.getHeight()/2-(marioSprite.getHeight()/3)*2, JuegoPanel.ge.jp);
		
	}

}
