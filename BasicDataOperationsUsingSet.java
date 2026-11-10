import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Set;

/**
 * Клас BasicDataOperationsUsingSet реалізує операції з множиною TreeSet для Duration.
 *
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataAnalysis()} - Запускає аналіз даних.</li>
 *   <li>{@link #findInSet()} - Пошук значення в множині Duration.</li>
 *   <li>{@link #locateMinMaxInSet()} - Знаходить мінімальне і максимальне значення в множині.</li>
 *   <li>{@link #analyzeDuplicates()} - Порівнює кількість вхідних значень і елементів множини.</li>
 * </ul>
 */
public class BasicDataOperationsUsingSet {
    Duration valueToSearch;
    int sourceValueCount;
    Set<Duration> valueSet = new TreeSet<>();

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     *
     * @param valueToSearch Значення для пошуку
     * @param valueArray Масив Duration
     */
    BasicDataOperationsUsingSet(Duration valueToSearch, Duration[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.sourceValueCount = valueArray.length;
        this.valueSet = new TreeSet<>(Arrays.asList(valueArray));
    }

    /**
     * Запускає комплексний аналіз даних з використанням множини TreeSet.
     *
     * Метод виконує пошук, визначення граничних значень і аналіз повторів у множині Duration.
     */
    public void executeDataAnalysis() {
        findInSet();
        locateMinMaxInSet();
        analyzeDuplicates();

        // зберігаємо до файлу унікальні значення з TreeSet (без повторів); TreeSet уже впорядкований, сортування лишаємо для однакового вигляду файлів
        Duration[] sortedSetValues = valueSet.toArray(new Duration[0]);
        Arrays.sort(sortedSetValues);
        DataFileHandler.writeArrayToFile(sortedSetValues, BasicDataOperations.SORTED_FILE_PREFIX + ".set.data");
    }

    /**
     * Здійснює пошук конкретного значення в множині тривалостей.
     */
    private void findInSet() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в TreeSet<Duration>";
        long timeStart = System.nanoTime();

        // TreeSet тримає елементи у збалансованому червоно-чорному дереві, упорядкованому
        // за compareTo(). contains() спускається від кореня: на кожному вузлі одне порівняння
        // вирішує, йти ліворуч чи праворуч, тому перебирати всю множину не треба.
        // Висота дерева близько log2(n), отже пошук займає O(log n).
        boolean elementExists = this.valueSet.contains(valueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в TreeSet");
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в TreeSet.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в множині Duration.
     */
    private void locateMinMaxInSet() {
        if (valueSet == null || valueSet.isEmpty()) {
            System.out.println("TreeSet є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        // TreeSet зберігає елементи впорядкованими, тому найменше значення лежить у крайньому
        // лівому вузлі, а найбільше у крайньому правому: first() і last() дістали б їх за O(log n).
        // Collections.min() і Collections.max() цього не знають і обходять усю множину
        // ітератором, порівнюючи елементи через compareTo(); кожен виклик O(n).
        Duration minValue = Collections.min(valueSet);
        Duration maxValue = Collections.max(valueSet);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмального i максимального значення в TreeSet<Duration>");

        System.out.println("Найменше значення в TreeSet: " + minValue);
        System.out.println("Найбільше значення в TreeSet: " + maxValue);
    }

    /**
     * Порівнює кількість вхідних значень і елементів множини: різниця — це відкинуті повтори.
     */
    private void analyzeDuplicates() {
        System.out.println("Кiлькiсть вхiдних значень: " + sourceValueCount);
        System.out.println("Кiлькiсть елементiв в TreeSet: " + valueSet.size());
        System.out.println("Повторних входжень, вiдкинутих TreeSet: " + (sourceValueCount - valueSet.size()));
    }
}
