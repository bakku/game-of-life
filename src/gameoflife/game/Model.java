package gameoflife.game;

public class Model
{

    private int countNeighbours;
    private boolean[][] cell;
    private int[][] die;
    private View view;

    public Model()
    {
        cell = new boolean[50][30];
        for (int i = 0; i < 50; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                cell[i][j] = false;
            }
        }
        
        die = new int[50][30];
    }

    public void setView(View nView)
    {
        view = nView;
    }

    public void nextStep()
    {
        for (int i = 0; i < 50; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                die[i][j] = 1;
            }
        }

        for (int i = 0; i < 50; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                countNeighbours = 0;

                if (i > 0)
                {
                    if (cell[i - 1][j] == true)
                    {
                        countNeighbours++;
                    }
                }

                if (i > 0 && j < 29)
                {
                    if (cell[i - 1][j + 1] == true)
                    {
                        countNeighbours++;
                    }
                }

                if (i > 0 && j > 0)
                {
                    if (cell[i - 1][j - 1] == true)
                    {
                        countNeighbours++;
                    }
                }

                if (j < 29)
                {
                    if (cell[i][j + 1] == true)
                    {
                        countNeighbours++;
                    }
                }

                if (j > 0)
                {
                    if (cell[i][j - 1] == true)
                    {
                        countNeighbours++;

                    }
                }

                if (i < 49 && j < 29)
                {
                    if (cell[i + 1][j + 1] == true)
                    {
                        countNeighbours++;
                    }
                }

                if (i < 49)
                {
                    if (cell[i + 1][j] == true)
                    {
                        countNeighbours++;
                    }
                }

                if (i < 49 && j > 0)
                {
                    if (cell[i + 1][j - 1] == true)
                    {
                        countNeighbours++;
                    }
                }


                if (cell[i][j] == true)
                {
                    // death

                    if (countNeighbours == 0 || countNeighbours == 1)
                    {
                        die[i][j] = 0;
                    }

                    if (countNeighbours >= 4)
                    {
                        die[i][j] = 0;
                    }
                }
                // lives
                else
                {
                    if (countNeighbours == 3)
                    {
                        die[i][j] = 2;
                    }
                }
            }
        }
        
        for (int i = 0; i < 50; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                if (die[i][j] == 0)
                {
                    cell[i][j] = false;
                }
                if (die[i][j] == 2)
                {
                    cell[i][j] = true;
                }
            }
        }

        view.setCell(cell);
        view.setLabels();
    }

    public void setCells(boolean[][] newCells)
    {
        cell = newCells;
    }
}
