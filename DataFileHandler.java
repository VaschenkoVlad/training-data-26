import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.format.DateTimeParseException;

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
        int lineNumber = 0;

        // кодування UTF-8 задаємо явно, щоб читання не залежало від налаштувань системи
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                lineNumber++;
                // Видаляємо BOM на початку файлу та пробіли по краях рядка
                currentLine = currentLine.replaceAll("^\\uFEFF", "").trim();
                if (currentLine.isEmpty()) {
                    continue;
                }
                Duration parsedValue;
                try {
                    // Duration.parse читає формат ISO-8601, наприклад PT0H1M55S; окремий форматер не потрібен
                    parsedValue = Duration.parse(currentLine);
                } catch (DateTimeParseException parseException) {
                    throw new IOException("рядок " + lineNumber + " містить некоректну тривалість '" + currentLine + "'", parseException);
                }
                temporaryArray[currentIndex++] = parsedValue;
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося прочитати " + filePath + ": " + ioException.getMessage(), ioException);
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
        // FileWriter перезаписує файл при кожному запуску; кодування також UTF-8
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            for (Duration valueElement : valueArray) {
                fileWriter.write(valueElement.toString());
                fileWriter.newLine();
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося записати " + filePath + ": " + ioException.getMessage(), ioException);
        }
    }
}
