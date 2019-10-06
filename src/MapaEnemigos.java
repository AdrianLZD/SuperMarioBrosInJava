
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class MapaEnemigos {
	private String path;
	private int width,
				height;
	private Enemigos[][] enemigos;
	
	public MapaEnemigos(String path) {
		this.path=path;
		this.cargarObjetos();
	}
	
	public void pinta(Graphics g) {
		for(int i=0;i<this.enemigos.length;i++) {
			for(int j=0;j<this.enemigos[0].length;j++) {
				if(this.enemigos[i][j].getId()!=0) {
					this.enemigos[i][j].pinta(g);
				}
			}
		}
	}
	
	public void cargarObjetos() {
		String linea;
		StringTokenizer st;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(this.path)));
			this.width = Integer.parseInt(br.readLine());
			this.height = Integer.parseInt(br.readLine());
			this.enemigos = new Enemigos[this.height][this.width];
			for(int i=0;i<this.height;i++) {
				linea = br.readLine();
				st = new StringTokenizer(linea);
				for(int j=0;j<this.width;j++) {
					this.enemigos[i][j]=new Enemigos(j*Objeto.objectSize,i*Objeto.objectSize,Integer.parseInt(st.nextToken()));
				}
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	public Enemigos[][] getEnemigos(){
		return this.enemigos;
	}
	
	public void tick(Bloque[][] b, Mario mario) {
		for(int i=0;i<this.enemigos.length;i++) {
			for (int j=0;j<this.enemigos[0].length;j++) {
				if(this.enemigos[i][j].getId()!=0) {
					if(this.enemigos[i][j].getId()==9) {
						this.enemigos[i][j].tick(b, this.enemigos);
					}else {
						if(Math.abs(mario.x- this.enemigos[i][j].x) < (JuegoPanel.ge.jp.getVentana().getWidth()/5)*3
							|| this.enemigos[i][j].x < JuegoPanel.ge.jp.getVentana().getWidth() || this.enemigos[i][j].getMuerto()) {
							this.enemigos[i][j].tick(b, this.enemigos);
						}
					}
					
				}
			}
		}
	}
}
