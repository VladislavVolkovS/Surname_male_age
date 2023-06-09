import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.regex.*;
import java.util.regex.Matcher;
import java.util.Date;
import javax.swing.*;
public class Main {
    private static String writing_year(int age){
        if (age > 10 && age < 20){
            return " лет";
        }
        int a = age % 10;
        if (a == 0 || a >=5){
            return " лет";
        }
        else if (a==1){
            return " год";
        }
        else {
            return " года";
        }
    }
    private static char checkSymbol(char a, boolean big){
        if (big){
            return Character.toUpperCase(a);
        }
        else{
            return Character.toLowerCase(a);
        }
    }
    private static String checkSurname(String str){
        StringBuilder result = new StringBuilder();
        result.append(checkSymbol(str.charAt(0), true));
        for (int i = 1; i < str.length(); i++){
            result.append(checkSymbol(str.charAt(i), false));
        }
        return result.toString();
    }
    private static boolean check_Name(String str){
        for (int i = 0; i < str.length(); i++){
            if (!((str.charAt(i) >= 'а' && str.charAt(i) <= 'я') || (str.charAt(i) >= 'А' && str.charAt(i) <= 'Я'))){
                return true;
            }
        }
        return false;
    }
    private static boolean checkData(int day, int month, int year){
        boolean b1 = (month == 4 || month == 6 || month == 9 || month == 11) && day >= 1 && day <= 30;
        boolean b = (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day >= 1 && day <= 31;
        if ((year % 4 == 0 && year % 100 != 0 ) || (year % 400 == 0)){
            if (b){
                return true;
            }
            if (month == 2 && day <= 29 && day >= 1){
                return true;
            }
            return b1;
        }
        else {
            if (b){
                return true;
            }
            if (month == 2 && day <= 28 && day >= 1){
                return true;
            }
            return b1;
        }
    }
    private static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }
    private static Date GetBirthDay(String str) throws Exception {
        String regex = "(\\d{2}\\p{Punct}\\d{2}\\p{Punct}\\d{4})";
        Matcher m = Pattern.compile(regex).matcher(str);
        if (m.find()) {
            char first = str.charAt(2);
            char second = str.charAt(5);
            int day = Integer.parseInt(str.substring(0, 2).trim());
            int month = Integer.parseInt(str.substring(3, 5).trim());
            int year = Integer.parseInt(str.substring(6, 10).trim());
            if (!checkData(day,month,year)){
                throw new Exception("Дата некорректна! Проверьте введенные данные!");
            }
            return new SimpleDateFormat("dd"+ first + "MM" + second + "yyyy").parse(m.group(1));
        } else {
            throw new Exception("Некорректный формат даты!");
        }
    }
    public static void main(String[] args){
        JFrame window = new JFrame("ФИО Пол Возраст");
        window.getContentPane().setBackground(Color.GRAY);
        window.setSize(800, 500);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        Font font1 = new Font("Lucida Sans Unicode", Font.BOLD, 14);
        JLabel label1 = new JLabel("Введите фамилию, имя, отчество и дату рождения в формате день(dd), месяц(mm), год(yyyy)!");
        label1.setBounds(20, 160, 760, 30);
        label1.setFont(font1);
        label1.setForeground(Color.BLACK);
        window.add(label1);
        Font font2 = new Font("Verdana", Font.ITALIC, 12);
        JLabel label2 = new JLabel("(с) В качестве разделителей для даты можете использовать знаки пунктуации");
        label2.setFont(font2);
        label2.setForeground(Color.LIGHT_GRAY);
        label2.setBounds(120, 190, 600, 20);
        window.add(label2);
        JTextField first_Field = new JTextField("Иванов Иван Иванович 01.01.1999");
        first_Field.setHorizontalAlignment(JTextField.CENTER);
        first_Field.setBounds(250, 220, 300,50);
        first_Field.setBackground(Color.LIGHT_GRAY);
        window.add(first_Field);
        JButton button = new JButton("Определить");
        button.setBounds(300, 280, 200, 20);
        button.setBackground(Color.CYAN);
        button.setForeground(Color.BLACK);
        window.add(button);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String s = first_Field.getText();
                    String[] str = s.split(" ");
                    if (str.length != 4) {
                        throw new Exception("Некорретный формат ввода!");
                    }
                    if (check_Name(str[0])){
                        throw new Exception("Некорретный формат фамилии!");
                    }
                    if (check_Name(str[1])){
                        throw new Exception("Некорретный формат имени!");
                    }
                    if (check_Name(str[2])){
                        throw new Exception("Некорретный формат отчества!");
                    }
                    Date currentDate = new Date();
                    Date birthDate = GetBirthDay(str[3]);
                    if (birthDate.after(currentDate)) {
                        throw new Exception("Некорректная дата рождения(Вы ввели дату рождения в будущем)");
                    }
                    Instant currentInstant = currentDate.toInstant();
                    ZonedDateTime zonedCurrentTime = currentInstant.atZone(ZoneId.systemDefault());
                    LocalDate localCurrentTime = zonedCurrentTime.toLocalDate();
                    Instant instant = birthDate.toInstant();
                    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                    LocalDate localDateTime = zonedDateTime.toLocalDate();
                    int age = calculateAge(localDateTime, localCurrentTime);
                    if ((checkSymbol(str[2].charAt(str[2].length() - 2), false) == 'и') && (checkSymbol(str[2].charAt(str[2].length() - 1), false) == 'ч')) {
                        JOptionPane.showMessageDialog(null, checkSurname(str[0]) + ' ' + checkSymbol(str[1].charAt(0), true) + '.' + checkSymbol(str[2].charAt(0), true) + '.' + '\n' + "Пол: Мужской" + '\n' + "Возраст: " + age + writing_year(age), "Результат", JOptionPane.PLAIN_MESSAGE);
                    } else if ((checkSymbol(str[2].charAt(str[2].length() - 2), false) == 'н') && (checkSymbol(str[2].charAt(str[2].length() - 1), false) == 'а')) {
                        JOptionPane.showMessageDialog(null, checkSurname(str[0]) + ' ' + checkSymbol(str[1].charAt(0),true) + '.' + checkSymbol(str[2].charAt(0),true) + '.' + '\n' + "Пол: Женский" + '\n' + "Возраст: " + age + writing_year(age), "Результат", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        throw new Exception("Невозможно определить пол!");
                    }
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка!", JOptionPane.PLAIN_MESSAGE);
                }
            }
        };
        JRootPane rootPane = SwingUtilities.getRootPane(button);
        rootPane.setDefaultButton(button);
        button.addActionListener(actionListener);
        window.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        window.setVisible(true);
    }
}