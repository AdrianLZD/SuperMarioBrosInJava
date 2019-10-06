
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Map {
	private String path;
	private int width,
				height;
	private Bloque[][] bloques;
	
	public Map(String path) {
		this.path=path;
		this.cargarMapa();
	}
	
	public void pinta(Graphics g) {
		for(int i=0;i<this.bloques.length;i++) {
			for(int j=0;j<this.bloques[0].length;j++) {
				this.bloques[i][j].pinta(g);
			}
		}
	}
	
	public void tick() {
		for(int i=0;i<this.bloques.length;i++) {
			for(int j=0;j<this.bloques[0].length;j++) {
				this.bloques[i][j].tick();
			}
		}
	}
	
	public void cargarMapa() {
		String linea;
		StringTokenizer st;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(this.path)));
			this.width = Integer.parseInt(br.readLine());
			this.height = Integer.parseInt(br.readLine());
			this.bloques = new Bloque[this.height][this.width];
			for(int i=0;i<this.height;i++) {
				linea = br.readLine();
				st = new StringTokenizer(linea);
				for(int j=0;j<this.width;j++) {
					this.bloques[i][j]=new Bloque(j*Bloque.blockSize,i*Bloque.blockSize,Integer.parseInt(st.nextToken()));
				}
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public Bloque[][] getBloques(){
		return this.bloques;
	}
}
