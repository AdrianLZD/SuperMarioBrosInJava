
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

public class Ventana extends JFrame{
	private static final long serialVersionUID = 1L;
	
	
	public JScrollPane scrollP;
	private Dimension screenSize;
	public JuegoPanel jp;
	
	
	public Ventana() {
		super("Super Mario Bros");
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension((int)(this.screenSize.getWidth()), 896));
		JuegoPanel jp = new JuegoPanel(this);
		this.jp = jp;
		//this.add(jp, BorderLayout.CENTER);
		
		//Scrolling
		this.scrollP = new JScrollPane(jp);
		this.scrollP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.scrollP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        InputMap inputMap = this.scrollP.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "do-nothing");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "do-nothing");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "do-nothing");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "do-nothing");
		this.add(this.scrollP,BorderLayout.SOUTH);
		
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void setScroll(int posX) {
		this.scrollP.getHorizontalScrollBar().setValue(posX);
	}
	
	public int getScroll() {
		return this.scrollP.getHorizontalScrollBar().getValue();
	}
	
	public JScrollPane getScrollP() {
		return this.scrollP;
	}
	
	
	@SuppressWarnings("unused")
	public static void main (String[] args) {
		Ventana ventana = new Ventana();
	}
}
