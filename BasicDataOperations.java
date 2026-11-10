import java.time.Duration;

/** Координатор пошуку точного значення та значень, більших за задану межу. */
public class BasicDataOperations {
    static final String PATH_TO_DATA_FILE = "list/Duration.data";
    static final String SORTED_FILE_PREFIX = "list/Duration.sorted";

    public static void main(String[] args) {
        if (args.length != 2 && args.length != 3) {
            System.err.println("Використання: java BasicDataOperations <значення> <межа> [файл-даних]");
            System.err.println("Приклад: java BasicDataOperations PT0H1M36S PT2M");
            System.exit(1);
            return;
        }
        Duration searched;
        Duration threshold;
        try {
            searched = Duration.parse(args[0]);
            threshold = Duration.parse(args[1]);
            if (threshold.isNegative()) {
                throw new IllegalArgumentException("Межа не може бути від'ємною");
            }
        } catch (RuntimeException exception) {
            System.err.println("Некоректні аргументи: " + exception.getMessage());
            System.exit(1);
            return;
        }

        String file = args.length == 3 ? args[2] : PATH_TO_DATA_FILE;
        try {
            Duration[] values = DataFileHandler.loadArrayFromFile(file);
            System.out.println("Точне значення для пошуку: " + searched);
            System.out.println("Завантажено значень: " + values.length);
            System.out.println("LIST");
            new BasicDataOperationsUsingList(searched, values).executeDataOperations(threshold);
            System.out.println("SET");
            new BasicDataOperationsUsingSet(searched, values).executeDataAnalysis();
            System.out.println("QUEUE");
            new BasicDataOperationsUsingQueue(searched, values).runDataProcessing();
        } catch (Exception exception) {
            System.err.println("Помилка обробки даних: " + exception.getMessage());
            System.exit(1);
        }
    }
}
