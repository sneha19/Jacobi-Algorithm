import java.awt.*;
import javax.swing.*;
public class JacobiFrame extends JFrame 
{
	public static final Dimension win = new Dimension(700, 500);
	public static final Dimension dis = new Dimension(350, 350);
	
	public JacobiFrame() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(win);
		setTitle("Sneha Ganesh - Jacobi Algorithm");
		getContentPane().add(new JacobiPanel(dis));
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) 
	{
		new JacobiFrame();
	}
}