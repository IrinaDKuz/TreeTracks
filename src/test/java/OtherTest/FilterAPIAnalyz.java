package OtherTest;

import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterAPIAnalyz {

    @Test
    public static void test() {
        // Пути к файлам
        String file1Path = "fileWithId";
        String file2Path = "fileWithId2";
        String outputPath = "differences.txt";

        try {
            // Чтение файлов
            List<List<Integer>> arraysFile1 = readArraysFromFile(file1Path);
            List<List<Integer>> arraysFile2 = readArraysFromFile(file2Path);

            // Проверка и запись различий
            compareAndWriteDifferences(arraysFile1, arraysFile2, outputPath);

            System.out.println("Сравнение завершено. Различия записаны в файл " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для чтения массивов из файла
    public static List<List<Integer>> readArraysFromFile(String filePath) throws IOException {
        List<List<Integer>> arrays = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                // Пропускаем пустые строки
                line = line.trim();
                if (line.startsWith("[") && line.endsWith("]")) {
                    line = line.substring(1, line.length() - 1).trim();
                }

                // Пропускаем пустые строки
                if (line.trim().isEmpty()) {
                    line = "0";
                }

                String[] elements = line.split(",");
                List<Integer> array = new ArrayList<>();
                for (String element : elements) {
                    array.add(Integer.parseInt(element.trim()));
                }
                arrays.add(array);
            }
        }
        return arrays;
    }

    // Метод для сравнения массивов и записи различий в новый файл
    public static void compareAndWriteDifferences(List<List<Integer>> arraysFile1,
                                                  List<List<Integer>> arraysFile2, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            int maxLength = Math.max(arraysFile1.size(), arraysFile2.size());

            for (int i = 0; i < maxLength; i++) {
                List<Integer> array1 = (i < arraysFile1.size()) ? arraysFile1.get(i) : new ArrayList<>();
                List<Integer> array2 = (i < arraysFile2.size()) ? arraysFile2.get(i) : new ArrayList<>();

                // Сортируем массивы для сравнения без учета порядка
                Collections.sort(array1);
                Collections.sort(array2);

                // Сравниваем массивы
                if (!array1.equals(array2)) {
                    // Записываем различия
                    writer.write("Строка " + (i + 1) + " различия:\n");
                    writer.write("Массив 1: " + array1 + "\n");
                    writer.write("Массив 2: " + array2 + "\n\n");
                }
            }
        }
    }
}