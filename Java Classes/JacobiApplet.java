import java.awt.*;
import javax.swing.*;
public class JacobiApplet extends JApplet 
{
    Dimension app = new Dimension(700, 500);
    Dimension dis = new Dimension(350, 350);
    
    public void start() 
    {
        setSize(app);
        this.getContentPane().add(new JacobiPanel(dis));
    }
}