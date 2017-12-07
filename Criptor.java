package criptor;

public class Criptor {

    //Наш главный фрейм
    private static MainFrame frame;

    public static void main(String[] args) {
        //Стандартная инициализация
        //Выделил память
        //Вызвал инит
        //Сделал видимым
        frame = new MainFrame();
        getFrame().init();
        getFrame().setVisible(true);
    }

    //Получение фрейма
    public static MainFrame getFrame() {
        return frame;
    }

}
