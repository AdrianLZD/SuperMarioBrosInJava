
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class MakeSound {
	private Clip sonido;
	
	public MakeSound(String file) {
		try {
            this.sonido = AudioSystem.getClip();
            this.sonido.open(AudioSystem.getAudioInputStream(new File(file)));
            this.sonido.start();
            if(file.equals("audio/cancion-lvl1.wav") || file.equals("audio/cancion-lvl2.wav") || file.equals("audio/cancion-lvl4.wav")) {
            	this.sonido.loop(Clip.LOOP_CONTINUOUSLY);
            }
 
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(null, "Ha habido un error al cargar los sonidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	public void stopMakeSound() {
		if(this.sonido!=null) {
			this.sonido.stop();
		}
	}
	
	
}

