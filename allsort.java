import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class allsort {
    public static final Path path = Path.of("F:\\mass1.txt");
    public static final LongAdder selection = new LongAdder();
    public static final LongAdder insertion = new LongAdder();
    public static final LongAdder bubble = new LongAdder();

    public static int[] selectionSort(int[] sortArr) {

        for (int i = 0; i < sortArr.length; i++) {
            int pos = i;
            int min = sortArr[i];
            //цикл выбора наименьшего элемента
            for (int j = i + 1; j < sortArr.length; j++) {
                if (sortArr[j] < min) {
                    //pos - индекс наименьшего элемента
                    pos = j;
                    min = sortArr[j];
                }
                selection.increment();
            }
            sortArr[pos] = sortArr[i];
            //меняем местами наименьший с sortArr[i]
            sortArr[i] = min;
        }
        return sortArr;

    }
    public static int[] insertionSort(int[] sortArr) {
        int j;
        //сортировку начинаем со второго элемента, т.к. считается, что первый элемент уже отсортирован
        for (int i = 1; i < sortArr.length; i++) {
            //сохраняем ссылку на индекс предыдущего элемента
            int swap = sortArr[i];
            for (j = i; j > 0 && swap < sortArr[j - 1]; j--) {
                //элементы отсортированного сегмента перемещаем вперёд, если они больше элемента для вставки
                sortArr[j] = sortArr[j - 1];
                insertion.increment();
            }
            sortArr[j] = swap;
        }
        return sortArr;
    }
    public static int[] bublesort(int[] sortArr) {


        boolean isSorted = false;
        int buf;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < sortArr.length - 1; i++) {
                if (sortArr[i] > sortArr[i + 1]) {
                    isSorted = false;

                    buf = sortArr[i];
                    sortArr[i] = sortArr[i + 1];
                    sortArr[i + 1] = buf;
                    bubble.increment();
                }
            }
        }

        return sortArr;
    }

    public static void read(){

}
    public static void write(){
    }
    public static void main(String[] args) throws IOException {
        System.out.println("Выберите действие с файлом (1 - запись в файл; 2 - случайные значения; 3 - чтение ранее сохранённого значения):");
        Scanner sc = new Scanner(System.in);

        int action = sc.nextInt();

        sc.nextLine();

        int[] input;
        switch (action) {
            case 1 -> {
                System.out.println("Запишите собственнный массив:");
                    String line = sc.nextLine();

                    input = Arrays.stream(line.split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    String parse = Arrays.stream(input)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining(" "));
                    Files.writeString(path, parse);

            }
            case 2 -> {
                input = ThreadLocalRandom
                        .current()
                        .ints(10)
                        .toArray();
                String parse = Arrays.stream(input)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "));
                Files.writeString(path, parse);
            }
            case 3 -> {
                var s = Files.readString(path);


                input = Arrays.stream(s.split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }
            default -> throw new IllegalArgumentException();
        }


        System.out.println("Выберите метод сортировки (1 - пузырьками; 2 - выборками; 3 - вставками):");

        int number = sc.nextInt();

        if (number == 1) {
            System.out.println("~Использован метод пузьков~\n" +
                    "Изначальный массив: "+ Arrays.toString(input) +
                    "\nСортированный массив: " + Arrays.toString(bublesort(input)) +
                    "\nИтераций было выполнено: " + bubble.sum());

        }
        if (number == 2) {
            System.out.println("~Использован метод выборок~\n" +
                    "Изначальный массив: "+ Arrays.toString(input) +"" +
                    "\nСортированный массив: " + Arrays.toString(selectionSort(input)) +
                    "\nИтераций было выполнено: " + selection.sum());
        }
        if (number == 3) {
            System.out.println("~Использован метод вставок~\n" +
                    "Изначальный массив: "+ Arrays.toString(input) +
                    "\nСортированный массив: " + Arrays.toString(insertionSort(input)) +
                    "\nИтераций было выполнено: " + insertion.sum());
    }
        else {
}
    }
}

