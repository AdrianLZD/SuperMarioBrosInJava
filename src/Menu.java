
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Menu extends GameNivel {
	
	private static String[] opciones = {"Play", "Credits", "Exit"};
	private int opcionActual = 0;
	private static BufferedImage 	logo=null,
									fondo=null;
	
	static {
		try {
			fondo=ImageIO.read(new File("images/levels/menu_fondo.png"));
			logo=ImageIO.read(new File("images/levels/logo.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ha habido un error al cargar las imagenes del menu", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	public Menu(GameEstado ge) {
		super(ge);
		ge.jp.setTamano(new Dimension(ge.ventana.getWidth(), 896));
		if(ge.ventana.getScrollP()!=null) {
			ge.ventana.setScroll(1);
		}
	}
	
	public void pinta(Graphics g) {
		g.drawImage(fondo, 0, 0, JuegoPanel.ge.jp);
		g.drawImage(logo, JuegoPanel.ge.ventana.getWidth()/2-JuegoPanel.ge.ventana.getWidth()/6, 250, JuegoPanel.ge.jp);
		
		for(int i=0;i<opciones.length;i++) {
			if(i==this.opcionActual) {
				g.setFont(new Font("Arial", Font.BOLD, 50));
				g.setColor(Color.GREEN);
			}else {
				g.setFont(new Font("Arial", Font.PLAIN, 50));
				g.setColor(Color.WHITE);
			}
			g.drawString(opciones[i], JuegoPanel.ge.jp.getWidth()/2 -100, 620 + i*50);
			
		}
		
	}
	
	public void keyPressed(int k) {
		if(k==KeyEvent.VK_UP) {
			this.opcionActual--;
			if(this.opcionActual<0) {
				this.opcionActual=opciones.length-1;
			}
		}
		if(k==KeyEvent.VK_DOWN) {
			this.opcionActual++;
			if(this.opcionActual>opciones.length-1) {
				this.opcionActual=0;
			}
		}
		
		if(k==KeyEvent.VK_ENTER) {
			if(this.opcionActual ==0) {
				//jugar
				JuegoPanel.ge.sumarNivel();
				JuegoPanel.ge.estados.push(new InfoScreen(1, 100, 664, 5, 0, JuegoPanel.ge)); //Empezar en nivel 1
				//this.ge.estados.push(new InfoScreen(1, 100, 334, 5, 0, this.ge)); //Empezar en nivel 4
			}else if(this.opcionActual ==1) {
				//Hecho por
				JOptionPane.showMessageDialog(null, "Game made by Adrian Lozano", "Credits", JOptionPane.INFORMATION_MESSAGE);
			}else if(this.opcionActual==2) {
				//Salir
				System.exit(0);
			}
		}
	}
	
	public void keyReleased(int k) {
		
	}

}
