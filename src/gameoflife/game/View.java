package gameoflife.game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class View extends JFrame implements MouseListener
{
    // start attributes

    private Model model;
    private JComboBox PresetForms = new JComboBox();
    private JButton NextButton = new JButton();
    private JButton StartButton = new JButton();
    private JComboBox Speed = new JComboBox();
    private JLabel Generation = new JLabel();
    private boolean[][] cell = new boolean[50][30];
    
    private int speed = 250;
    private int generation = 0;
    
    String imgStrGray = "resources/images/Gray.png";
    private ImageIcon grayIcon = createImageIcon(imgStrGray);
    
    String imgStrYellow = "resources/images/Yellow.png";
    private ImageIcon yellowIcon = createImageIcon(imgStrYellow);
    
    private JLabel[][] icons = new JLabel[50][30];
    
    boolean running = false;

    public View(String title)
    {
        // frame-initialisation

        super(title);

        for (int i = 0; i < 50; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                cell[i][j] = false;
            }
        }

        if (grayIcon == null || yellowIcon == null)
        {
            JOptionPane.showMessageDialog(null, "images not found",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        int frameWidth = 700;
        int frameHeight = 500;
        setSize(frameWidth, frameHeight);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;

        setLocation(x, y);
        setResizable(false);
        Container cp = getContentPane();
        cp.setLayout(null);
        
        // start of components

        setTitle("Game Of Life");

        PresetForms.setBounds(8, 432, 217, 25);
        PresetForms.insertItemAt("Clear", 0);
        PresetForms.insertItemAt("Glider", 1);
        PresetForms.insertItemAt("Small Exploder", 2);
        PresetForms.insertItemAt("Exploder", 3);
        PresetForms.insertItemAt("Ten Cell Row", 4);
        PresetForms.insertItemAt("Lightweight Spaceship", 5);
        PresetForms.insertItemAt("Tumbler", 6);
        PresetForms.insertItemAt("Gosper Glider Gun", 7);
        PresetForms.setSelectedIndex(0);
        PresetForms.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent evt)
            {
                PresetForms_ActionPerformed(evt);
            }
        });
        cp.add(PresetForms);

        NextButton.setBounds(232, 432, 100, 25);
        NextButton.setText("Next");
        NextButton.setMargin(new Insets(2, 2, 2, 2));
        NextButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent evt)
            {
                NextButton_ActionPerformed(evt);
            }
        });
        cp.add(NextButton);

        StartButton.setBounds(336, 432, 100, 25);
        StartButton.setText("Start");
        StartButton.setMargin(new Insets(2, 2, 2, 2));
        StartButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent evt)
            {
                StartButton_ActionPerformed(evt);
            }
        });
        cp.add(StartButton);

        Speed.setBounds(440, 432, 97, 25);
        Speed.insertItemAt("Fast", 0);
        Speed.insertItemAt("Normal", 1);
        Speed.insertItemAt("Slow", 2);
        Speed.setSelectedIndex(1);
        Speed.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent evt)
            {
                Speed_ActionPerformed(evt);
            }
        });
        cp.add(Speed);

        Generation.setBounds(544, 432, 147, 25);
        Generation.setText("Generation: " + generation);
        cp.add(Generation);

        int k = 0;
        int l = 0;

        for (int i = 0; i < 50; i++, k += 13)
        {
            for (int j = 0; j < 30; j++, l += 13)
            {
                icons[i][j] = new JLabel("", grayIcon, JLabel.CENTER);
                icons[i][j].setBounds(24 + k, 20 + l, 11, 11);
                cp.add(icons[i][j]);
                icons[i][j].addMouseListener(this);
            }
            l = 0;
        }
        // end of components

        setVisible(true);
    }

    // start of methods
    protected ImageIcon createImageIcon(String path)
    {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null)
        {
            return new ImageIcon(imgURL);
        }
        else
        {
            return null;
        }
    }

    public void NextButton_ActionPerformed(ActionEvent evt)
    {
        this.checkLabels();
        model.setCells(cell);
        model.nextStep();
        generation++;
        Generation.setText("Generation: " + generation);
    }

    public void StartButton_ActionPerformed(ActionEvent evt)
    {
        if ((StartButton.getText()).equals("Start"))
        {
            NextButton.setEnabled(false);
            PresetForms.setEnabled(false);
            Speed.setEnabled(false);
            StartButton.setText("Stop");

            final View view = this;

            running = true;

            new Thread()
            {

                @Override
                public void run()
                {
                    while (running)
                    {
                        view.checkLabels();
                        model.setCells(cell);
                        model.nextStep();
                        
                        generation++;
                        Generation.setText("Generation: " + generation);
                        
                        long millisToWait = speed;
                        long millis = System.currentTimeMillis();
                        while ((System.currentTimeMillis() - millis) < millisToWait)
                        {
                            // Do nothing
                        }
                    }
                }
            }.start();


        }
        else
        {
            running = false;
            NextButton.setEnabled(true);
            PresetForms.setEnabled(true);
            Speed.setEnabled(true);
            StartButton.setText("Start");
        }
    }
    
    public void Speed_ActionPerformed(ActionEvent evt)
    {
        if (((String) Speed.getSelectedItem()).equals("Fast"))
        {
            speed = 50;
        }
        if (((String) Speed.getSelectedItem()).equals("Normal"))
        {
            speed = 250;
        }
        if (((String) Speed.getSelectedItem()).equals("Slow"))
        {
            speed = 500;
        }
    }
    
    public void PresetForms_ActionPerformed(ActionEvent evt)
    {
        if (((String) PresetForms.getSelectedItem()).equals("Clear"))
        {
            this.clear();
        }
        if (((String) PresetForms.getSelectedItem()).equals("Glider"))
        {
            this.setGlider();
        }
        if (((String) PresetForms.getSelectedItem()).equals("Small Exploder"))
        {
            this.setSmallExploder();
        }
        if (((String) PresetForms.getSelectedItem()).equals("Exploder"))
        {
            this.setExploder();
        }
        if (((String) PresetForms.getSelectedItem()).equals("Ten Cell Row"))
        {
            this.set10CellRow();
        }
        if (((String) PresetForms.getSelectedItem()).equals("Lightweight Spaceship"))
        {
            this.setLightweightSpaceship();
        }
        if (((String) PresetForms.getSelectedItem()).equals("Tumbler"))
        {
            this.setTumbler();
        }
        if (((String) PresetForms.getSelectedItem()).equals("Gosper Glider Gun"))
        {
            this.setGosperGliderGun();
        }
    }

    public void setCell(boolean[][] newCells)
    {
        cell = newCells;
    }

    public void setLabels()
    {
        for (int i = 0; i < 50; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                if (cell[i][j] == true)
                {
                    icons[i][j].setIcon(yellowIcon);
                }
                else
                {
                    icons[i][j].setIcon(grayIcon);
                }
            }
        }
    }

    public void checkLabels()
    {
        for (int i = 0; i < 50; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                if (icons[i][j].getIcon() == grayIcon)
                {
                    cell[i][j] = false;
                }
                if (icons[i][j].getIcon() == yellowIcon)
                {
                    cell[i][j] = true;
                }
            }
        }
    }

    public void clear()
    {
        generation = 0;
        Generation.setText("Generation:" + generation);
        for (int i = 0; i < 50; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                icons[i][j].setIcon(grayIcon);
            }
        }
    }

    public void setGlider()
    {
        generation = 0;
        Generation.setText("Generation:" + generation);
        
        clear();
        icons[24][13].setIcon(yellowIcon);
        icons[25][14].setIcon(yellowIcon);
        icons[25][15].setIcon(yellowIcon);
        icons[24][15].setIcon(yellowIcon);
        icons[23][15].setIcon(yellowIcon);
    }

    public void setSmallExploder()
    {
        generation = 0;
        Generation.setText("Generation:" + generation);
        
        clear();
        icons[24][13].setIcon(yellowIcon);
        icons[24][14].setIcon(yellowIcon);
        icons[23][14].setIcon(yellowIcon);
        icons[25][14].setIcon(yellowIcon);
        icons[23][15].setIcon(yellowIcon);
        icons[25][15].setIcon(yellowIcon);
        icons[24][16].setIcon(yellowIcon);
    }

    public void setExploder()
    {
        generation = 0;
        Generation.setText("Generation:" + generation);
        
        clear();
        icons[22][12].setIcon(yellowIcon);
        icons[22][13].setIcon(yellowIcon);
        icons[22][14].setIcon(yellowIcon);
        icons[22][15].setIcon(yellowIcon);
        icons[22][16].setIcon(yellowIcon);
        icons[24][12].setIcon(yellowIcon);
        icons[24][16].setIcon(yellowIcon);
        icons[26][12].setIcon(yellowIcon);
        icons[26][13].setIcon(yellowIcon);
        icons[26][14].setIcon(yellowIcon);
        icons[26][15].setIcon(yellowIcon);
        icons[26][16].setIcon(yellowIcon);

    }

    public void set10CellRow()
    {
        generation = 0;
        Generation.setText("Generation:" + generation);
        
        clear();
        icons[20][14].setIcon(yellowIcon);
        icons[21][14].setIcon(yellowIcon);
        icons[22][14].setIcon(yellowIcon);
        icons[23][14].setIcon(yellowIcon);
        icons[24][14].setIcon(yellowIcon);
        icons[25][14].setIcon(yellowIcon);
        icons[26][14].setIcon(yellowIcon);
        icons[27][14].setIcon(yellowIcon);
        icons[28][14].setIcon(yellowIcon);
        icons[29][14].setIcon(yellowIcon);
    }

    public void setLightweightSpaceship()
    {
        generation = 0;
        Generation.setText("Generation:" + generation);
        
        clear();
        icons[22][16].setIcon(yellowIcon);
        icons[22][14].setIcon(yellowIcon);
        icons[23][13].setIcon(yellowIcon);
        icons[24][13].setIcon(yellowIcon);
        icons[25][13].setIcon(yellowIcon);
        icons[26][13].setIcon(yellowIcon);
        icons[26][14].setIcon(yellowIcon);
        icons[26][15].setIcon(yellowIcon);
        icons[25][16].setIcon(yellowIcon);
    }

    public void setTumbler()
    {
        generation = 0;
        Generation.setText("Generation:" + generation);
        
        clear();
        icons[22][12].setIcon(yellowIcon);
        icons[23][12].setIcon(yellowIcon);
        icons[22][13].setIcon(yellowIcon);
        icons[23][13].setIcon(yellowIcon);
        icons[23][14].setIcon(yellowIcon);
        icons[23][15].setIcon(yellowIcon);
        icons[23][16].setIcon(yellowIcon);
        icons[22][17].setIcon(yellowIcon);
        icons[21][17].setIcon(yellowIcon);
        icons[21][16].setIcon(yellowIcon);
        icons[21][15].setIcon(yellowIcon);
        icons[25][12].setIcon(yellowIcon);
        icons[26][12].setIcon(yellowIcon);
        icons[25][13].setIcon(yellowIcon);
        icons[26][13].setIcon(yellowIcon);
        icons[25][14].setIcon(yellowIcon);
        icons[25][15].setIcon(yellowIcon);
        icons[25][16].setIcon(yellowIcon);
        icons[26][17].setIcon(yellowIcon);
        icons[27][17].setIcon(yellowIcon);
        icons[27][16].setIcon(yellowIcon);
        icons[27][15].setIcon(yellowIcon);
    }

    public void setGosperGliderGun()
    {
        generation = 0;
        Generation.setText("Generation:" + generation);
        
        clear();
        // rect left
        icons[6][9].setIcon(yellowIcon);
        icons[7][9].setIcon(yellowIcon);
        icons[6][10].setIcon(yellowIcon);
        icons[7][10].setIcon(yellowIcon);

        // rect right
        icons[14][10].setIcon(yellowIcon);
        icons[14][11].setIcon(yellowIcon);
        icons[15][11].setIcon(yellowIcon);
        icons[16][10].setIcon(yellowIcon);
        icons[16][9].setIcon(yellowIcon);
        icons[15][9].setIcon(yellowIcon);

        icons[22][11].setIcon(yellowIcon);
        icons[22][12].setIcon(yellowIcon);
        icons[22][13].setIcon(yellowIcon);
        icons[23][11].setIcon(yellowIcon);
        icons[24][12].setIcon(yellowIcon);

        // rechts oben neben viereck
        icons[28][9].setIcon(yellowIcon);
        icons[28][8].setIcon(yellowIcon);
        icons[29][9].setIcon(yellowIcon);
        icons[30][8].setIcon(yellowIcon);
        icons[30][7].setIcon(yellowIcon);
        icons[29][7].setIcon(yellowIcon);

        // rechts oben viereck
        icons[41][7].setIcon(yellowIcon);
        icons[40][7].setIcon(yellowIcon);
        icons[40][8].setIcon(yellowIcon);
        icons[41][8].setIcon(yellowIcon);

        // rechts angel
        icons[41][14].setIcon(yellowIcon);
        icons[42][14].setIcon(yellowIcon);
        icons[41][15].setIcon(yellowIcon);
        icons[41][16].setIcon(yellowIcon);
        icons[43][15].setIcon(yellowIcon);

        //
        icons[32][19].setIcon(yellowIcon);
        icons[31][19].setIcon(yellowIcon);
        icons[30][19].setIcon(yellowIcon);
        icons[30][20].setIcon(yellowIcon);
        icons[31][21].setIcon(yellowIcon);
    }

    public void setModel(Model newModel)
    {
        model = newModel;
    }
    // end of methods

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            for (int i = 0; i < 50; i++)
            {
                for (int j = 0; j < 30; j++)
                {
                    if (icons[i][j] == e.getSource())
                    {
                        icons[i][j].setIcon(yellowIcon);
                    }
                }
            }
        }

        if (e.getButton() == MouseEvent.BUTTON3)
        {
            for (int i = 0; i < 50; i++)
            {
                for (int j = 0; j < 30; j++)
                {
                    if (icons[i][j] == e.getSource())
                    {
                        icons[i][j].setIcon(grayIcon);
                    }
                }
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }
}