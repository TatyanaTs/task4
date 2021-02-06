package task4;

import java.io.*;
import java.text.*;
import java.util.*;

public class task4 {
    static ArrayList<Check> timeInOut = new ArrayList<>();  // список прибытий/убытий
    static ArrayList<Period> periods = new ArrayList<>(); // интервалы максимального кол-ва посетителей

    public static void main(String[] args) throws Exception {
        String filePath = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            String[] timePeriod = line.split("[\\\\n\\s]+");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm");

            timeInOut.add(new Check(timeFormat.parse(timePeriod[0]), true));
            timeInOut.add(new Check(timeFormat.parse(timePeriod[1]), false));
        }
        reader.close();

        timeInOut.sort(new CheckComparator());

        int quantity = 0;
        int maxQuantity = quantity;
        for (int i = 0; i < timeInOut.size() - 1; i++) {
            Check check = timeInOut.get(i);
            quantity += (check.in_out) ? 1 : -1;
            if (maxQuantity < quantity) {
                maxQuantity = quantity;
            }
            periods.add(new Period(check.time, timeInOut.get(i + 1).time, quantity));
        }
        for (int i = periods.size() - 1; i > -1; i--) { // удаляем интервалы меньше максимального кол-ва
            if (periods.get(i).quantity < maxQuantity) {
                periods.remove(i);
            }
        }
        for (int i = periods.size() - 1; i > 0; i--) { // соединяем смежные интервалы
            if (periods.get(i).timeIn.equals(periods.get(i - 1).timeOut)) {
                periods.get(i - 1).timeOut = periods.get(i).timeOut;
                periods.remove(i);
            }
        }

        for (Period period : periods) {
            String h1 = "" + period.timeIn.getHours();
            String m1 = "" + period.timeIn.getMinutes();
            if (h1.length() == 1) {
                h1 = "0" + h1;
            }
            if (m1.length() == 1) {
                m1 = "0" + m1;
            }

            String h2 = "" + period.timeOut.getHours();
            String m2 = "" + period.timeOut.getMinutes();
            if (h2.length() == 1) {
                h2 = "0" + h2;
            }
            if (m2.length() == 1) {
                m2 = "0" + m2;
            }
            System.out.println(h1 + ":" + m1 + " " + h2 + ":" + m2);
        }
    }

    static class Check {
        Date time;
        Boolean in_out; // in - true, out - false

        Check(Date time, boolean in_out) {
            this.time = time;
            this.in_out = in_out;
        }
    }

    static class CheckComparator implements Comparator<Check> {

        @Override
        public int compare(Check case1, Check case2) {
            int result = case1.time.compareTo(case2.time);
            if (result != 0) {
                return result;
            }
            if (!case2.in_out) {
                return 1;  // если в один момент и входят и выходят, то впереди фиксится выход
            }
            return 0;
        }
    }

    static class Period {
        Date timeIn;
        Date timeOut;
        int quantity;

        Period(Date timeIn, Date timeOut, int quantity) {
            this.timeIn = timeIn;
            this.timeOut = timeOut;
            this.quantity = quantity;
        }
    }
}
