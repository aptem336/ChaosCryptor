package criptor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

//Переопределенная JPanel для отрисоки изображения
public class ImagePanel extends JPanel {

    //Собственно изображение данной панели
    private BufferedImage image;

    //Конструктор без аргументов => mage = null
    public ImagePanel() {
        this.image = null;
    }

    //Метод установить изображение аргумент само изображения - просто приравниваем
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    //Переопределенный метод отрисовки
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Если изображения не нуль
        if (getImage() != null) {
            //Рисуем изображение
            g.drawImage(getImage(), 0, 0, getWidth(), getHeight(), null);
        }
    }

    //Получение изображения данной панели
    public BufferedImage getImage() {
        return image;
    }
}
