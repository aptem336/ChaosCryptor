package criptor;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;

public class Except {

    //Ввод числа
    public static long inputNum(String text) {
        long num = 0;
        boolean isTrue = false;
        //Циклим пока всё не пройдёт хорошо (пока isTrue не равно true)
        while (!isTrue) {
            //Получаем строкуВвода из другой процедуры
            String numString = input(text);
            //Если строка пуста или не задана возвращаем -1
            if (numString == null || "".equals(numString)) {
                return -1;
            } else {
                //Пробуем!
                try {
                    //получить число из троки
                    num = Long.parseLong(numString);
                    //Не вышло!
                } catch (HeadlessException | NumberFormatException e) {
                    //Выводим сообщение
                    JOptionPane.showMessageDialog(null, "Ошибка ввода! Введите число");
                }
                //Если до сюда всё прошло хорошо и число меньше нуля
                if (num < 0) {
                    //выводим сообщение
                    JOptionPane.showMessageDialog(null, "Ошибка ввода! Число должно быть положительным");
                } else {
                    //если и тут всё в порядке
                    //выходим из цикла посредством задания логической переменной значение true
                    isTrue = true;
                }
            }
        }
        //Возвращаем число
        return num;
    }

    public static String input(String text) {
        String string;
        //Строка = то, что вернет окно ввода
        string = JOptionPane.showInputDialog(text);
        //Возращаем
        return string;
    }
}
