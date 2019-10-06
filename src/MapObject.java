
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class MapObject {
	private String path;
	private int width,
				height;
	private Objeto[][] objetos;
	
	
	
	public MapObject(String path) {
		this.path=path;
		this.cargarObjetos();
	}
	
	public void pinta(Graphics g) {
		for(int i=0;i<this.objetos.length;i++) {
			for(int j=0;j<this.objetos[0].length;j++) {
				this.objetos[i][j].pinta(g);
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
			this.objetos = new Objeto[this.height][this.width];
			for(int i=0;i<this.height;i++) {
				linea = br.readLine();
				st = new StringTokenizer(linea);
				for(int j=0;j<this.width;j++) {
					this.objetos[i][j]=new Objeto(j*Objeto.objectSize,i*Objeto.objectSize,Integer.parseInt(st.nextToken()));
				}
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	public Objeto[][] getObjetos(){
		return this.objetos;
	}
	
	public void tick(Bloque[][] b, Mario mario) {
		for(int i=0;i<this.objetos.length;i++) {
			for (int j=0;j<this.objetos[0].length;j++) {
				if(this.objetos[i][j].getId()!=0) {
					if(this.objetos[i][j].getId()==7 || this.objetos[i][j].getId()==8) { //No iniciar el fuego si mario no esta cerca
						if(Math.abs(mario.x- this.objetos[i][j].x) < (GameNivel.ge.jp.getVentana().getWidth()/5)*3 
							||this.objetos[i][j].x < GameNivel.ge.jp.getVentana().getWidth()) {
							this.objetos[i][j].tick(b);
						}
					}else {
						this.objetos[i][j].tick(b);
					}
				}
			}
		}
	}
}
