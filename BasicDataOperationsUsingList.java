import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Клас BasicDataOperationsUsingList реалізує операції з колекціями типу Vector для даних Duration.
 *
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив елементів Duration.</li>
 *   <li>{@link #findInArray()} - Здійснює пошук елемента в масиві Duration.</li>
 *   <li>{@link #locateMinMaxInArray()} - Визначає найменше і найбільше значення в масиві.</li>
 *   <li>{@link #sortList()} - Сортує колекцію List з Duration.</li>
 *   <li>{@link #findInList()} - Пошук конкретного значення в списку.</li>
 *   <li>{@link #locateMinMaxInList()} - Пошук мінімального і максимального значення в списку.</li>
 * </ul>
 */
public class BasicDataOperationsUsingList {
    private boolean listSorted = false;
    private boolean arraySorted = false;
    private Duration valueToSearch;
    private Duration[] valueArray;
    private List<Duration> valueList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     *
     * @param valueToSearch Значення для пошуку
     * @param valueArray Масив Duration
     */
    BasicDataOperationsUsingList(Duration valueToSearch, Duration[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.valueArray = valueArray.clone();
        this.valueList = new Vector<>(Arrays.asList(valueArray));
    }

    /**
     * Виконує комплексні операції з структурами даних.
     *
     * Метод завантажує масив і список об'єктів Duration,
     * здійснює сортування та пошукові операції.
     */
    public void executeDataOperations(Duration threshold) {
        // спочатку працюємо з колекцією List
        System.out.println("List: до сортування");
        analyzeCondition(valueList, listSorted, threshold);
        findInList();
        locateMinMaxInList();

        sortList();
        System.out.println("List: після сортування");
        analyzeCondition(valueList, listSorted, threshold);

        findInList();
        locateMinMaxInList();

        // потім обробляємо масив тривалостей
        System.out.println("Масив: до сортування");
        analyzeCondition(valueArray, arraySorted, threshold);
        findInArray();
        locateMinMaxInArray();

        performArraySorting();
        System.out.println("Масив: після сортування");
        analyzeCondition(valueArray, arraySorted, threshold);

        findInArray();
        locateMinMaxInArray();

        // зберігаємо до окремого файлу відсортований вміст Vector
        DataFileHandler.writeArrayToFile(valueList.toArray(new Duration[0]), BasicDataOperations.SORTED_FILE_PREFIX + ".list.data");
    }

    /**
     * Упорядковує масив об'єктів Duration за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    void performArraySorting() {
        long timeStart = System.nanoTime();

        Arrays.sort(valueArray);
        arraySorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування масиву Duration[]");
    }

    /**
     * Здійснює пошук конкретного значення в масиві тривалостей.
     */
    void findInArray() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в масивi Duration[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position = -1;
        if (arraySorted) {
            position = Arrays.binarySearch(valueArray, valueToSearch);
        } else {
            for (int i = 0; i < valueArray.length; i++) {
                if (valueArray[i].compareTo(valueToSearch) == 0) {
                    position = i;
                    break;
                }
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві тривалостей.
     */
    void locateMinMaxInArray() {
        if (valueArray == null || valueArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "визначення мiнiмального i максимального значення в масивi Duration[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        Duration minValue = valueArray[0];
        Duration maxValue = valueArray[0];

        if (arraySorted) {
            // у відсортованому масиві граничні значення лежать на його кінцях
            maxValue = valueArray[valueArray.length - 1];
        } else {
            // Duration не має isBefore/isAfter, тому порівнюємо через compareTo()
            for (Duration currentValue : valueArray) {
                if (currentValue.compareTo(minValue) < 0) {
                    minValue = currentValue;
                }
                if (currentValue.compareTo(maxValue) > 0) {
                    maxValue = currentValue;
                }
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Шукає конкретне значення тривалості в колекції Vector.
     */
    void findInList() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в Vector<Duration>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position = -1;
        if (listSorted) {
            position = Collections.binarySearch(valueList, valueToSearch);
        } else {
            int index = 0;
            for (Duration value : valueList) {
                if (value.compareTo(valueToSearch) == 0) {
                    position = index;
                    break;
                }
                index++;
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в Vector за позицією: " + position);
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в Vector.");
        }
    }

    /**
     * Визначає найменше і найбільше значення в колекції Vector з тривалостями.
     */
    void locateMinMaxInList() {
        if (valueList == null || valueList.isEmpty()) {
            System.out.println("Колекція Vector є пустою або не ініціалізованою.");
            return;
        }

        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "визначення мiнiмального i максимального значення в Vector<Duration>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        Duration minValue;
        Duration maxValue;

        if (listSorted) {
            // у відсортованому списку граничні значення лежать на його кінцях
            minValue = valueList.get(0);
            maxValue = valueList.get(valueList.size() - 1);
        } else {
            minValue = Collections.min(valueList);
            maxValue = Collections.max(valueList);
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    /**
     * Упорядковує колекцію List з об'єктами Duration за зростанням.
     * Відстежує та виводить час виконання операції сортування.
     */
    void sortList() {
        long timeStart = System.nanoTime();

        Collections.sort(valueList);
        listSorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування Vector<Duration>");
    }

    // BEGIN CONDITION
    /** вибірка даних; sorted=true лише для даних, упорядкованих за зростанням. */
    public int analyzeCondition(Duration[] values, boolean sorted, Duration threshold) {
        if (threshold.isNegative()) throw new IllegalArgumentException("Межа не може бути від'ємною");
        String conditionDescription = "тривалість більша за " + threshold;
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("Масив: вибірка даних " + phase + ". Умова: " + conditionDescription);
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "вибірка даних: масив Duration[], " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Duration[] matches = new Duration[values.length];
        int count = 0;
        int inspected = 0;
        if (!sorted) {
            // Невпорядковані дані: перевіряємо кожне входження.
            // Duration не має isBefore/isAfter, тому порівнюємо через compareTo().
            for (Duration value : values) {
                inspected++;
                if (value.compareTo(threshold) > 0) matches[count++] = value;
            }
        } else {
            // Етап 1. Бінарним пошуком знаходимо перший елемент, строго більший за межу.
            // Масив упорядкований, тому спочатку йдуть елементи, не більші за threshold, а після них решта.
            // Шукаємо індекс, де закінчується перша частина, і з кожним кроком
            // удвічі звужуємо проміжок [low; high), у якому цей індекс може бути.
            int low = 0;
            int high = values.length;
            while (low < high) {
                // середина проміжку; зсув >>> 1 ділить на 2 без переповнення int
                int mid = (low + high) >>> 1;
                inspected++;
                // values[mid] <= threshold: шуканий індекс лежить правіше mid, тож відкидаємо ліву половину разом із mid;
                // інакше values[mid] вже підходить, тож лишаємо mid і відкидаємо праву половину
                if (values[mid].compareTo(threshold) <= 0) low = mid + 1; else high = mid;
            }
            // Тепер low == high: це перший елемент > threshold (або values.length, якщо таких немає).
            // Знадобилось близько log2(n) порівнянь замість перегляду всіх менших елементів.

            // Етап 2. Від знайденої позиції всі наступні елементи теж більші за межу,
            // бо масив упорядкований за зростанням. Верхньої межі в умові немає,
            // тому просто забираємо хвіст масиву без додаткових порівнянь.
            for (int i = low; i < values.length; i++) {
                inspected++;
                matches[count++] = values[i];
            }
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        // Друк результатів не входить у виміряний час пошуку.
        System.out.println("Переглянуто елементів: " + inspected);
        for (int i = 0; i < count; i++) System.out.println(matches[i]);
        System.out.println("У масиві знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }

    /** вибірка даних; sorted=true лише для даних, упорядкованих за зростанням. */
    public int analyzeCondition(List<Duration> values, boolean sorted, Duration threshold) {
        if (threshold.isNegative()) throw new IllegalArgumentException("Межа не може бути від'ємною");
        String conditionDescription = "тривалість більша за " + threshold;
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("List: вибірка даних " + phase + ". Умова: " + conditionDescription);
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "вибірка даних: Vector<Duration>, " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Duration[] matches = new Duration[values.size()];
        int count = 0;
        int inspected = 0;
        if (!sorted) {
            // Невпорядковані дані: перевіряємо кожне входження.
            // Duration не має isBefore/isAfter, тому порівнюємо через compareTo().
            for (Duration value : values) {
                inspected++;
                if (value.compareTo(threshold) > 0) matches[count++] = value;
            }
        } else {
            // Етап 1. Бінарним пошуком знаходимо перший елемент, строго більший за межу.
            // Список упорядкований, тому спочатку йдуть елементи, не більші за threshold, а після них решта.
            // Шукаємо індекс, де закінчується перша частина, і з кожним кроком
            // удвічі звужуємо проміжок [low; high), у якому цей індекс може бути.
            // Доступ get(mid) швидкий (O(1)) для Vector, як і для ArrayList; для LinkedList він повільний.
            int low = 0;
            int high = values.size();
            while (low < high) {
                // середина проміжку; зсув >>> 1 ділить на 2 без переповнення int
                int mid = (low + high) >>> 1;
                inspected++;
                // values.get(mid) <= threshold: шуканий індекс лежить правіше mid, тож відкидаємо ліву половину разом із mid;
                // інакше values.get(mid) вже підходить, тож лишаємо mid і відкидаємо праву половину
                if (values.get(mid).compareTo(threshold) <= 0) low = mid + 1; else high = mid;
            }
            // Тепер low == high: це перший елемент > threshold (або values.size(), якщо таких немає).
            // Знадобилось близько log2(n) порівнянь замість перегляду всіх менших елементів.

            // Етап 2. Від знайденої позиції всі наступні елементи теж більші за межу,
            // бо список упорядкований за зростанням. Верхньої межі в умові немає,
            // тому просто забираємо хвіст списку без додаткових порівнянь.
            java.util.ListIterator<Duration> cursor0 = values.listIterator(low);
            while (cursor0.hasNext()) {
                Duration value = cursor0.next();
                inspected++;
                matches[count++] = value;
            }
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        // Друк результатів не входить у виміряний час пошуку.
        System.out.println("Переглянуто елементів: " + inspected);
        for (int i = 0; i < count; i++) System.out.println(matches[i]);
        System.out.println("У List знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }
    // END CONDITION
}
