package gameoflife.game;

public class Controller {
    private Model model;
    private View view;
    
    public Controller()
    {
        model = new Model();
        view = new View("Game of Life");
        
        view.setModel(model);
        model.setView(view);
    }
}