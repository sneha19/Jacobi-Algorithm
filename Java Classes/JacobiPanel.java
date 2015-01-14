import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.*;

import functionParser.*;
import edu.gatech.gth773s.gui.*;
import edu.gatech.gth773s.gui.math.*;
import edu.gatech.gth773s.math.*;

public class JacobiPanel extends JPanel implements ActionListener 
{   
    //private static final Color backgroundColor1 = new Color(1, 1, 1);
    //private static final Color backgroundColor2 = new Color(220, 219, 219);
    private static final Color backgroundColor2 = new Color(1,1,1);
    private static final Color backgroundColor1 = new Color(0,154,205);
    private static final Border borderConstant = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                                                 BorderFactory.createLineBorder(backgroundColor1.darker(), 1));
    
    private JButton sorted;
    private JButton unsorted;
    
    private JLabel steps;
    private JLabel times;
    
    private JLabel[][] panel2Final;
    private JLabel[] eigenValueFinal;
    
    private FunctionGraphPanel graphView;
    private OptionsPanel choice;
    
    private static final double constant = Math.pow(10, -9);
    
    public JacobiPanel(Dimension dimension) 
    {
        setBackground(backgroundColor1);
        setBorder(new CompoundBorder(borderConstant,new MatteBorder(25, 25, 25, 25, backgroundColor1)));
        setLayout(new BorderLayout());
        
        graphView = new FunctionGraphPanel(dimension);
        graphView.setBackground(backgroundColor2);
        graphView.setWindow(0, 30, 0, 15);
        
        choice = new OptionsPanel();
        choice.addActionListener(this);
        choice.setOpaque(false);
        
        choice.addLabel("     ");
        choice.addLabel("Sneha Ganesh's Jacobi Algorithm - MATH 2605 - Dr. Lin");
        choice.nextRow();
        choice.addLabel(" ");
        choice.nextRow();
        sorted = choice.addButton("Sorted Run");
        choice.addLabel("     ");
        unsorted = choice.addButton("Unsorted Run");

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(new JLabel("Original Matrix:"));
        
        JPanel panel2 = new JPanel();
        panel2.setOpaque(false);
        panel2.setLayout(new GridLayout(5, 5, 6, 6));
        panel2Final = new JLabel[5][5];
        
        for (int x=0; x < 5; x++)
        {
            for (int y=0; y < 5; y++)
            {
                panel2Final[x][y] = new JLabel();
                panel2.add(panel2Final[x][y]);
            }
        }
        
        panel.add(panel2);
        panel.add(Box.createVerticalGlue());
        
        JPanel panel3 = new JPanel();
        panel3.setOpaque(false);
        panel3.setLayout(new GridLayout(6, 1, 6, 3));
        panel3.add(new JLabel("Eigen Values x5:"));
        eigenValueFinal = new JLabel[5];
        
        for (int x=0; x < 5; x++)
        {
            eigenValueFinal[x] = new JLabel();
            panel3.add(eigenValueFinal[x]);
        }
        
        panel.add(panel3);
        panel.add(Box.createVerticalGlue());
        
        steps = new JLabel();
        panel.add(steps);
        panel.add(Box.createVerticalGlue());
        
        times = new JLabel();
        panel.add(times);
        panel.add(Box.createVerticalGlue());
        
        add(panel, BorderLayout.EAST);
        add(new FixedSizePanel(graphView), BorderLayout.CENTER);
        add(choice, BorderLayout.NORTH);
        
        jacobi(JacobiMethod.LARGEST_OFFDIAG);
    }
    
    public void jacobi(int big)
    {
        DecimalFormat rounded = new DecimalFormat("#.###");
        
        Matrix m = new Matrix(5, 5);
        m.fillRandomSymmetric();
        
        System.out.print("Matrix A: \n" + m + "\n \n Off(A): \n");
        
        for (int x=0; x < 5; x++)
        {
            for (int y=0; y < 5; y++)
            {
                panel2Final[x][y].setText(rounded.format(m.getValue(x, y)));
            }
        }
        
        graphView.removeAll();
        
        double timeOfRun = System.nanoTime();
        
        JacobiMethod jacobiMethod = new JacobiMethod(m, big);
        
        FunctionParser parser = new FunctionParser("x");
        Evaluator ends = new Evaluator(1);
        
        try
        {
			parser.parse("x * (log[9 / 10] / log[2.7182818]) + (log[" + m.offDiag() + "] / log[2.7182818])", ends);
		} 
		catch (FunctionParsingException e) 
		{
			System.err.println("Function couldn't be computed. Please try again...");
			System.exit(-1);
		}
        
        graphView.addFunction(ends, new Color(1, 125, 7), "");
        
        graphView.addStatPlot("result");
        
        double offValue;
        
        offValue = jacobiMethod.off();
        System.out.println(offValue);
        graphView.addStatPoint(new Vector2d(jacobiMethod.iterations(), Math.log(offValue)), "result");
        
        do 
        {
            jacobiMethod.jacobiStep();
            offValue = jacobiMethod.off();
            
            System.out.println(offValue);
            graphView.addStatPoint(new Vector2d(jacobiMethod.iterations(), Math.log(offValue)), "result");
        } 
        while (offValue > constant);
        
        timeOfRun = (System.nanoTime() - timeOfRun) / 1000000000;
        
        System.out.print("\n Eigen Values x5: \n");
        double[] arrayOne = jacobiMethod.diagonals();
        
        for (int x=0; x < arrayOne.length; x++)
        {
            eigenValueFinal[x].setText(rounded.format(arrayOne[x]));
            System.out.print(arrayOne[x] + " ");
        }
        
        steps.setText("Number of steps: " + jacobiMethod.iterations());
        times.setText("Total Time: " + timeOfRun + " seconds");
    }
    
    public void actionPerformed(ActionEvent event) 
    {
        if (event.getSource() == sorted)
        {
            jacobi(JacobiMethod.LARGEST_OFFDIAG);
        } 
        else if (event.getSource() == unsorted)
        {
            jacobi(JacobiMethod.UNSORTED);
        }
    }
}