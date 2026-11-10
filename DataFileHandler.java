import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

/**
 * Клас DataFileHandler управляє роботою з файлами даних Duration.
 */
public class DataFileHandler {
    /**
     * Завантажує масив об'єктів Duration з файлу.
     *
     * @param filePath Шлях до файлу з даними.
     * @return Масив об'єктів Duration.
     */
    public static Duration[] loadArrayFromFile(String filePath) {
        Duration[] temporaryArray = new Duration[1000];
        int currentIndex = 0;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                // Видаляємо можливі невидимі символи та BOM
                currentLine = currentLine.trim().replaceAll("^\\uFEFF", "");
                if (!currentLine.isEmpty()) {
                    // Duration.parse читає формат ISO-8601, наприклад PT0H1M55S; окремий форматер не потрібен
                    Duration parsedValue = Duration.parse(currentLine);
                    temporaryArray[currentIndex++] = parsedValue;
                }
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося прочитати " + filePath, ioException);
        }

        Duration[] resultArray = new Duration[currentIndex];
        System.arraycopy(temporaryArray, 0, resultArray, 0, currentIndex);

        return resultArray;
    }

    /**
     * Зберігає масив об'єктів Duration у файл.
     *
     * @param valueArray Масив об'єктів Duration.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(Duration[] valueArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            for (Duration valueElement : valueArray) {
                fileWriter.write(valueElement.toString());
                fileWriter.newLine();
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося записати " + filePath, ioException);
        }
    }
}
