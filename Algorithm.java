package criptor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Algorithm {

    //Поля класса название соответствует смыслу
    private static int size;
    private static int height;
    private static int width;
    private static long seed;
    private static int countIter;

    private static boolean[] cells;
    private static byte[] noiseBytes;
    private static byte[] inputBytes;
    private static byte[] sumaryBytes;

    private static File inputFile;
    private static File outputFile;

    private static String inputText;

    //Получить неоюходимые значения для кодировки текста
    //try = попробовать
    //catch(ошибка) = при данной ошибке делать то что внутри
    public static boolean getValuesForText() {
        //Получить входную строку
        inputText = MainFrame.getInputText().getText();
        //Если она пуста или = нуль - вылететь со значением false
        if ("".equals(inputText) || inputText == null) {
            return false;
        }
        //берем размер от массива байтов от входной строки
        size = inputText.getBytes().length;
        return getBasedValues();
    }

    //Получем необходимые значения для кодирования изображения (Уже пояснял)
    //try = попробовать
    //catch(ошибка) = при данной ошибке делать то что внутри
    public static boolean getValuesForImage() {
        try {
            try {
                inputFile = MainFrame.getInputFile();
                size = (int) inputFile.length();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Входящий файл не задан! \nДля выбора файла кликните на \nсоответсвующюю миниатюру");
                return false;
            }
            try {
                outputFile = MainFrame.getOutputFile();
                outputFile.canRead();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Исходящий файл не задан! \nДля выбора файла кликните на \nсоответсвующюю миниатюру");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "IterCount - числовое значения!");
            return false;
        }
        return getBasedValues();
    }

    private static boolean getBasedValues() {
        //Получим ширину и высоту примерно, как для квардрата
        width = (int) Math.sqrt(size);
        height = size / width;
        if (size % width != 0) {
            height++;
        }
        //Семя переменная для генерация Random
        seed = Except.inputNum("seed");
        //Если Except.inputNum вернул -1, что означает нажатие cancel
        if (seed == -1) {
            //Выведем окно об утсановке стандартного значения и
            JOptionPane.showMessageDialog(null, "Seed получит стандартное значение 50!");
            //Установим его
            seed = 50;
        }
        try {
            //Получим количество итерация
            countIter = Integer.parseInt(MainFrame.getIterCountText().getText());
        } catch (NumberFormatException e) {
            //Если не вышло выводим сообщение и вылетаем со значением false
            JOptionPane.showMessageDialog(null, "IterCount - числовое значения!");
            return false;
        }
        return true;
    }

    //Метод - начать шифрование
    public static void criptText() {
        //Вызов генерации "шума"
        getTextBytes();
        generateNoise(false);
    }

    //Начать шифрование теперь для теста
    public static void cryptImage() {
        getImageBytes();
        generateNoise(true);
    }

    //Получить байтовый массив из текста
    private static void getTextBytes() {
        inputBytes = inputText.getBytes();
    }

    //Получить байтовый массив изображения
    private static void getImageBytes() {
        //Новый массив
        inputBytes = new byte[size];
        //Пытаемся создать файловый поток
        try (FileInputStream fis = new FileInputStream(inputFile);) {
            //Читаем биты в inputBytes
            fis.read(inputBytes);
            //При ошибке ...
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Файл не найден!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка чтения!");
        }
        //Пытаемся установаить изображение от входного файла
        try {
            MainFrame.getInputPicture().setImage(ImageIO.read(inputFile));
            Criptor.getFrame().repaint();
        } catch (IOException ex) {
        }
    }

//<editor-fold defaultstate="collapsed" desc="noiseGenerate">
    //Генерация шума
    private static void generateNoise(boolean isImage) {
        //Ячейки в массиве БИТОВ! можно представить в виде логической переменной, где 1=true 0=false
        cells = new boolean[size * 6];
        //Получаем рандом от "семени" для каждого семени свой ряд! В этом и смысл
        Random random = new Random(seed);
        for (int i = 0; i < cells.length; i++) {
            //Если элемент в строке рандома > 0.5 cells[i] = true и наоборот
            cells[i] = random.nextDouble() > 0.5d;
        }
        //Запускаем новый поток для того чтобы не торомзить главный
        new Thread() {
            @Override
            public void run() {
                //Столько раз, сколь равно количсевто итераций
                for (int i = 0; i <= countIter; i++) {
                    //Если это изображение
                    if (isImage) {
                        //Выполняем итерацию
                        iterate();
                    }
                    //Значение прогресс бара уставналиваем в ссответвующее положение
                    MainFrame.getProgressBar().setValue((int) ((double) i / countIter * 100));
                    //Обновим
                    Criptor.getFrame().repaint();
                }
                //Пока шум генерируется не завершаем только менем прогрессбар и только когда щум готов
                //Если изображение завершаем изображение
                if (isImage) {
                    finishImage();
                } else {
                    finishText();
                }
            }
            //Запуск поток
        }.start();
    }

    //Итерация
    private static void iterate() {
        //Новый массив с новыми зачениями cells
        boolean[] newCells = new boolean[cells.length];
        //Для каждой ячейки
        for (int i = 0; i < cells.length; i++) {
            //Проверяем будет ли он жив
            newCells[i] = checkToLive(i);
        }
        //ячейки = новые ячейки
        cells = newCells;
    }

    //Проверяем ячейку на жизнь в будущем
    private static boolean checkToLive(int i) {
        //Все возможные смещения для ожной координаты
        int[] diffs = {-1, 0, 1};
        //Переходим от индекса к координатам
        int cX = i / width;
        int cY = i % width;
        //Количество соседей
        int neighboursCount = 0;
        //Для всех возможных комбинаций свдвигов
        for (int xdiff : diffs) {
            for (int ydiff : diffs) {
                //Если нет смещения пропускаем
                if (ydiff == 0 && xdiff == 0) {
                    continue;
                }
                //Ставим значение ячейки со сдвигом
                int newX = cX + xdiff + width;
                int newY = cY + ydiff + height;
                //(newX % width) чтобы при вылезании за массив значение изменилось например
                //Размер 12 у нас получилось 13
                //13%12=1 - не вылезли
                //Переходим обратно к индексам
                if (cells[((newX % width) * width) + (newY % height)]) {
                    neighboursCount++;
                }
            }
        }
        //Вернём true если условия выживания выполнены и false иначе
        return (cells[i] && (neighboursCount == 2 || neighboursCount == 3)) || (!cells[i] && neighboursCount == 3);
    }
//</editor-fold>

    //Завершим начатое
    private static void finishText() {
        //Тут думаю объяснять ничего не надо всё видно по названиям
        getNoiseBytes();
        getSumBytes();
        writeText();
    }

    private static void finishImage() {
        //Тут думаю объяснять ничего не надо всё видно по названиям
        getNoiseBytes();
        getSumBytes();
        writeImage();
        //Перезагрузим фрейм, чтоб отрисовать загруженное
        Criptor.getFrame().repaint();
    }

    //Получить массив байтов шума
    private static void getNoiseBytes() {
        //Новое изображение
        BufferedImage bufferedImage = new BufferedImage(width, height, TYPE_INT_RGB);
        //Новый массив
        noiseBytes = new byte[size];
        //Счетчик
        int counter = 0;
        //Строка для считывания
        String byteString = "";
        //Для всех ячеек
        for (int i = 0; i < cells.length; i++) {
            //Если ячейка = true 
            if (cells[i]) {
                //К строке добавляем единицу (переход от boolean к БИТУ)
                byteString += 1;
            } else {
                byteString += 0;
            }
            //Если чсило считанных ячеек кратно 6
            if (i % 6 == 0 && i != 0) {
                //Получаем байт от байтовой строки
                noiseBytes[counter] = Byte.parseByte(byteString, 2);
                //Зачение цвета - грдиент серого
                float color = Math.abs((float) (noiseBytes[counter]) / 128f);
                //Ставим пиксель
                bufferedImage.setRGB(counter % width, counter / width, new Color(color, color, color).getRGB());
                //Очищаем строку байта
                byteString = "";
                //Увеличиваем счётчик
                counter++;
            }
        }
        //Ставим изображение куда надо
        MainFrame.getNoisePicture().setImage(bufferedImage);
    }

    private static void getSumBytes() {
        //Приравниваю для удобства, чтоб потом можно было писать ^=
        sumaryBytes = inputBytes;
        for (int i = 0; i < size; i++) {
            //байт суммы = байт суммы (который ранее был скопирован с входного XOR байт шума
            sumaryBytes[i] ^= noiseBytes[i];
        }
    }

    //Получить сумму входных байтов и шума
    private static void writeText() {
        //Строка выхода
        String outputText = "";
        for (int i = 0; i < sumaryBytes.length; i++) {
            //Добавляем к строуке преобразование байта в символ
            outputText += (char) sumaryBytes[i];
        }
        //Выводим строку выхода на соответствующий компонент
        MainFrame.getOutputText().setText(outputText);
    }

    //Получить массив суммы байтов (уже пояснял)
    private static void writeImage() {
        //Файловый поток Поток данных 
        try (FileOutputStream fos = new FileOutputStream(outputFile); DataOutputStream dos = new DataOutputStream(fos)) {
            //Для всех байтов
            for (int i = 0; i < sumaryBytes.length; i++) {
                //Пишем байт
                dos.writeByte(sumaryBytes[i]);
            }
        } catch (IOException ex) {
        }
        try {
            //Пытаемся установить изображение
            MainFrame.getOutputPicture().setImage(ImageIO.read(outputFile));
        } catch (IOException ex) {
        }
    }
}
