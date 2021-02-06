package task4; //java -jar task4.jar test4.txt

import java.io.*;
import java.text.*;
import java.util.*;

public class task4 {
    static Date[] timeIn;
    static Date[] timeOut;

    public static void main(String[] args) throws Exception {
        String filePath = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int lines = 0;
        while (reader.readLine() != null) {
            lines++; //подсчет количества чисел для массива
        }
        reader.close();
        timeIn = new Date[lines];
        timeOut = new Date[lines];

        Scanner scanner = new Scanner(new File(filePath));
        int n = 0;
        while (n < lines) {
            String[] timePeriod = scanner.nextLine().split("[\\\\n\\s]+");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm");
            timeIn[n] = timeFormat.parse(timePeriod[0]);
            timeOut[n] = timeFormat.parse(timePeriod[1]);
            n++;
        }
        scanner.close();
        Arrays.sort(timeIn);
        Arrays.sort(timeOut);

        int a = 0; //индекс для массива timeIn
        int b = 0; //индекс для массива timeOut
        int maxCount = 0; //максимальное количество посетителей
        int count = 1; //количество посетителей
        int fromIndex = 0;
        int toIndex = 0;
        if (timeIn[0] !=null) {
            Date time = timeIn[a];
            while (a < timeIn.length && b < timeOut.length && (a != timeIn.length-1)) {
                int ts1 = time.compareTo(timeIn[a + 1]);
                int ts2 = timeOut[b].compareTo(timeIn[a + 1]);
                if (ts1 == 0) {
                    ++count;
                    if (count > maxCount) {
                        maxCount = count;
                        fromIndex = a;
                        toIndex = b;
                    }
                    ++a;
                } else if (ts2 == 0) {
                    ++a;
                    ++b;
                    time = timeIn[a];
                } else if (ts2 < 0) {
                    --count;
                    time = timeOut[b];
                    ++b;
                } else if (ts2 > 0) {
                    ++count;
                    time = timeIn[a+1];
                    if (count > maxCount) {
                        maxCount = count;
                        fromIndex = a+1;
                        toIndex = b;
                    }
                    ++a;
                }
            }
        for (int i = b; i < timeOut.length; i++) {
            --count;
        }
        System.out.println("Максимальное количество посетителей - " + maxCount +
                    " человек были в интервале с " + timeIn[fromIndex] + " по " + timeOut[toIndex]);
        } else {
            System.out.println("Посетителей не было.");
        }
    }
}
