
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JuegoPanel extends JPanel implements Runnable, KeyListener{
	private static final long serialVersionUID = 1L;
	
	
	private boolean isRunning = false;
	private Thread hilo;
	public static GameEstado ge;
	private Ventana ventana;
	
	private static long delayObjetivo = 1000/60;
	private long startTime,
				elapsedTime,
				delay;
	
	
	public JuegoPanel(Ventana ventana) {
		this.isRunning=true;
		JuegoPanel.ge = new GameEstado(this, ventana);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.hilo = new Thread(this);
		this.hilo.start();
		this.ventana = ventana;
		this.setPreferredSize(new Dimension(this.ventana.getWidth(), 896));
		
	}
	
	public Ventana getVentana() {
		return this.ventana;
	}
	
	public void setTamano(Dimension tamano) {
		this.setPreferredSize(tamano);
	}

	
	public void tick() {
		JuegoPanel.ge.tick();
	}
	
	public GameEstado getGe() {
		return JuegoPanel.ge;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setClip(this.ventana.getScroll(), this.getY(), this.ventana.getWidth(), this.ventana.getHeight());
		JuegoPanel.ge.pinta(g);
	}
	
	public void run() {
		
		
		while(isRunning) {
			this.startTime = System.nanoTime();
			this.tick();
			this.repaint();
			this.elapsedTime = System.nanoTime() - this.startTime;
			this.delay = delayObjetivo - this.elapsedTime /1000000;
			
			if(this.delay<=0) {
				this.delay = 16;
			}
			try {
				Thread.sleep(this.delay);
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog(this, "Ha habido un error en el thread", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void keyPressed(KeyEvent arg0) {	
		JuegoPanel.ge.keyPressed(arg0.getKeyCode());
	}

	
	public void keyReleased(KeyEvent arg0) {
		JuegoPanel.ge.keyReleased(arg0.getKeyCode());
	}

	
	public void keyTyped(KeyEvent arg0) {}

	
	
}
