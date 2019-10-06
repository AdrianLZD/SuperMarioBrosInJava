
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class GameEstado {
	public JuegoPanel jp;
	public Stack<GameNivel> estados;
	public Ventana ventana;
	private int monedas,
				nivel,
				mundo,
				nivelGlobal,
				score,
				timer,
				controladorTimer;
	private boolean iniciarTimer;
	@SuppressWarnings("unused")
	private MakeSound efecto;
	
	private static BufferedImage miniCoin=null;
	static {
		try {
			miniCoin = ImageIO.read(new File("images/objects/mini_coin.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar la moneda en la barra de informacion", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public GameEstado(JuegoPanel jp, Ventana ventana){
		this.jp = jp;
		this.ventana=ventana;
		this.estados = new Stack<GameNivel>();
		this.estados.push(new Menu(this));
		this.monedas=0;
		this.score=0;
		this.nivelGlobal=0;
		this.controladorTimer=0;
		this.mundo=((this.nivelGlobal-1)/4)+1;
		this.nivel=((this.nivelGlobal-1)%4)+1;
	}
	
	public void tick() {
		this.estados.peek().tick();
		if(this.iniciarTimer) {
			if(this.controladorTimer<60) {
				this.controladorTimer++;
			}else {
				this.timer--;
				this.controladorTimer=0;
			}
		}
		
	}
	
	public void setIniciarTimer(boolean iniciarTimer) {
		this.iniciarTimer=iniciarTimer;
	}
	
	public boolean getIniciarTimer() {
		return this.iniciarTimer;
	}
	
	public void sumarNivel() {
		this.nivelGlobal++;
		this.mundo=((this.nivelGlobal-1)/4)+1;
		this.nivel=((this.nivelGlobal-1)%4)+1;
	}
	
	public void setTimer(int timer) {
		this.timer=timer;
	}
	
	public int getTimer() {
		return this.timer;
	}
	
	public void cambiarNivel(int nivelGlobal) {
		this.nivelGlobal=nivelGlobal;
		this.mundo=((this.nivelGlobal-1)/4)+1;
		this.nivel=((this.nivelGlobal-1)%4)+1;
	}
	
	public void sumarMoneda() {
		this.monedas++;
		if(this.monedas>=100) {
			this.estados.get(this.estados.size()-2).agregarVida();
			this.efecto= new MakeSound("audio/smb_1up.wav");
			this.monedas=0;
		}
	}
	
	public void sumarScore(int score) {
		this.score+=score;
	}
	
	public void setScore(int score) {
		this.score=score;
	}
	
	public void setMonedas(int monedas) {
		this.monedas=monedas;
	}
	
	public void pinta(Graphics g) {
		this.estados.peek().pinta(g);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Helvetica", Font.BOLD, 35)); 
		//Mario y su score
		g.drawString("MARIO", this.ventana.getScroll() + this.ventana.getWidth()/6, 100);
		if(this.score/10==0) {
			g.drawString("00000"+this.score, this.ventana.getScroll() + this.ventana.getWidth()/6, 135);
		}else if(this.score/100==0) {
			g.drawString("0000"+this.score, this.ventana.getScroll() + this.ventana.getWidth()/6, 135);
		}else if(this.score/1000==0) {
			g.drawString("000"+this.score, this.ventana.getScroll() + this.ventana.getWidth()/6, 135);
		}else if(this.score/10000==0) {
			g.drawString("00"+this.score, this.ventana.getScroll() + this.ventana.getWidth()/6, 135);
		}else if(this.score/100000==0) {
			g.drawString("0"+this.score, this.ventana.getScroll() + this.ventana.getWidth()/6, 135);
		}else {
			g.drawString(""+this.score, this.ventana.getScroll() + this.ventana.getWidth()/6, 135);
		}
		
		
		//Monedas 
		g.drawImage(miniCoin, this.ventana.getScroll()+this.ventana.getWidth()/2-this.ventana.getWidth()/7, 135-miniCoin.getHeight(), this.jp);
		g.setFont(new Font("Helvetica", Font.PLAIN, 35)); 
		g.drawString("X", this.ventana.getScroll()+this.ventana.getWidth()/2-this.ventana.getWidth()/8, 135);
		g.setFont(new Font("Helvetica", Font.BOLD, 35)); 
		if((this.monedas/10)==0){
			g.drawString("0"+this.monedas, this.ventana.getScroll()+this.ventana.getWidth()/2-this.ventana.getWidth()/10, 135);
		}else {
			g.drawString(this.monedas+"", this.ventana.getScroll()+this.ventana.getWidth()/2-this.ventana.getWidth()/10, 135);
		}
		
		//Mundo y nivel
		g.drawString("WORLD", this.ventana.getScroll()+this.ventana.getWidth()/2+this.ventana.getWidth()/12, 100);
		if(this.nivelGlobal!=0) {
			g.drawString(this.mundo+ " -", this.ventana.getScroll()+this.ventana.getWidth()/2+this.ventana.getWidth()/12, 135);
			g.drawString(" "+this.nivel, this.ventana.getScroll()+this.ventana.getWidth()/2+this.ventana.getWidth()/9, 135);
		}
		
		//Timer
		g.drawString("TIME", this.ventana.getScroll()+this.ventana.getWidth()/2+this.ventana.getWidth()/4, 100);
		g.drawString(this.timer+"", this.ventana.getScroll()+this.ventana.getWidth()/2+this.ventana.getWidth()/4, 135);
		
		
	}
	
	
	public void keyPressed(int k) {
		this.estados.peek().keyPressed(k);	
	}
	
	public void keyReleased(int k) {
		this.estados.peek().keyReleased(k);
	}
	
	public int getNivelGlobal() {
		return this.nivelGlobal;
	}
	
	public int getMundo() {
		return this.mundo;
	}
	
	public void setNivelGlobal(int nivelGlobal) {
		this.nivelGlobal=nivelGlobal;
	}
	

}
