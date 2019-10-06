
import java.awt.Graphics;

public class GameNivel {
	public static GameEstado ge;
	protected Mario mario;
	protected Map mapa;
	protected MapObject mapaObjetos;
	protected MapaEnemigos mapaEnemigo;
	protected MakeSound cancion;
	protected int contadorTime;
	protected int timer;
	
	public GameNivel(GameEstado ge) {
		GameNivel.ge = ge;
		this.start();
	}
	
	public Mario getMario() {
		return this.mario;
	}
	
	public void start() {
		
	}
	public void tick() {
		
	}
	
	public void pinta(Graphics g) {
		
	}
	
	public void keyPressed(int k) {
		
	}
	
	public void keyReleased(int k) {
		
	}
	
	public void stopMusica() {
		
	}
	
	public void marioMurio(int marioXPos, int marioYPos, int scrollPos) {
		
	}
	
	public void changeNivel() {
		
	}
	
	public void agregarVida() {
		
	}
	
	public void reiniciarContadorTime() {
	}
	
	public void defineNivel() {
		
	}
	
	public boolean getMarioMurio() {
		return false;
	}
	
	public void setCancion(String path) {
		this.cancion=new MakeSound(path);
	}
	
}
