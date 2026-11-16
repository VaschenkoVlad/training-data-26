import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Клас BasicDataOperationsUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення.
 *
 * <p>Ключ - record {@link Cactus} з характеристиками кактуса, значення - власник (власниця) кактуса.</p>
 *
 * <p>Операції, які виконуються для кожного типу Map:</p>
 * <ul>
 *   <li>пошук за ключем;</li>
 *   <li>пошук за значенням (сортування списку пар + binarySearch);</li>
 *   <li>виведення пар ключ-значення на консоль до і після сортування;</li>
 *   <li>сортування за ключами;</li>
 *   <li>визначення найменшого й найбільшого ключа;</li>
 *   <li>додавання пари ключ-значення;</li>
 *   <li>видалення за ключем;</li>
 *   <li>видалення за значенням.</li>
 * </ul>
 */
public class BasicDataOperationsUsingMap {
    private static final Cactus KEY_TO_SEARCH_AND_DELETE = new Cactus("Опунція", 2.1);
    private static final Cactus KEY_TO_ADD = new Cactus("Ферокактус", 40.2);

    private static final String VALUE_TO_SEARCH_AND_DELETE = "Аліна";
    private static final String VALUE_TO_ADD = "Кирило";

    /**
     * Компаратор для сортування Map.Entry за значеннями String (ім'ям власника).
     * Використовує природний порядок рядків - String.compareTo().
     */
    private static final Comparator<Map.Entry<Cactus, String>> OWNER_COMPARATOR = Map.Entry.comparingByValue();

    private Hashtable<Cactus, String> hashtable;
    private TreeMap<Cactus, String> treeMap;

    /**
     * Record Cactus зберігає характеристики кактуса: рід (genus) і довжину колючок у мм (spineLength).
     *
     * <p>Record автоматично генерує:</p>
     * <ul>
     *   <li>приватні final поля genus і spineLength та канонічний конструктор;</li>
     *   <li>методи доступу genus() і spineLength();</li>
     *   <li>equals() і hashCode(), які враховують усі компоненти record (genus і spineLength).</li>
     * </ul>
     *
     * <p>Вручну реалізовано:</p>
     * <ul>
     *   <li>compareTo() - природний порядок: рід за зростанням, потім довжина колючок за зростанням;</li>
     *   <li>toString() - формат Cactus{genus='...', spineLength=...}, доповнений хеш-кодом.</li>
     * </ul>
     *
     * @param genus рід кактуса, наприклад "Мамілярія"
     * @param spineLength довжина колючок, мм
     */
    public record Cactus(String genus, Double spineLength) implements Comparable<Cactus> {

        /**
         * Компактний конструктор перевіряє, що характеристики задані.
         * Завдяки цьому в compareTo() не потрібно окремо обробляти значення null.
         */
        public Cactus {
            Objects.requireNonNull(genus, "genus не може бути null");
            Objects.requireNonNull(spineLength, "spineLength не може бути null");
        }

        /**
         * Порівнює цей кактус з іншим для визначення природного порядку сортування.
         *
         * <p>Критерій порівняння: рід (genus) за зростанням, а для однакових родів -
         * довжина колючок (spineLength) за зростанням.</p>
         *
         * <p>Метод використовується:</p>
         * <ul>
         *   <li>TreeMap - для автоматичного впорядкування ключів;</li>
         *   <li>Collections.sort() - для сортування списку ключів.</li>
         * </ul>
         *
         * <p>Метод узгоджений з equals(): compareTo() повертає 0 тоді й лише тоді,
         * коли genus і spineLength однакові, тобто коли equals() повертає true.</p>
         *
         * @param other кактус для порівняння
         * @return від'ємне число, якщо цей кактус іде раніше за other; 0, якщо вони рівні;
         *         додатне число, якщо цей кактус іде пізніше за other
         */
        @Override
        public int compareTo(Cactus other) {
            // Спочатку порівнюємо за родом (за зростанням)
            int genusComparison = this.genus.compareTo(other.genus);
            if (genusComparison != 0) {
                return genusComparison;
            }
            // Якщо роди однакові, порівнюємо за довжиною колючок (за зростанням)
            return this.spineLength.compareTo(other.spineLength);
        }

        /**
         * Повертає рядкове представлення кактуса.
         * Хеш-код додано, щоб у логах було видно, від чого залежить порядок елементів у Hashtable.
         *
         * @return рядок у форматі Cactus{genus='...', spineLength=..., hashCode=...}
         */
        @Override
        public String toString() {
            return "Cactus{genus='" + genus + "', spineLength=" + spineLength + ", hashCode=" + hashCode() + "}";
        }
    }

    /**
     * Конструктор, який ініціалізує об'єкт готовими даними.
     *
     * @param hashtable Hashtable з початковими даними (ключ: Cactus, значення: власник)
     * @param treeMap TreeMap з початковими даними (ключ: Cactus, значення: власник)
     */
    BasicDataOperationsUsingMap(Hashtable<Cactus, String> hashtable, TreeMap<Cactus, String> treeMap) {
        this.hashtable = hashtable;
        this.treeMap = treeMap;
    }

    /**
     * Виконує комплекс операцій з Hashtable і TreeMap: пошук, виведення, сортування, додавання, видалення.
     */
    public void executeDataOperations() {
        // Спочатку працюємо з Hashtable
        System.out.println("========= Операції з Hashtable =========");
        System.out.println("Початковий розмір Hashtable: " + hashtable.size());

        // Пошук до сортування
        findByKeyInHashtable();
        findByValueInHashtable();
        findMinMaxKeyInHashtable();

        printHashtable();
        sortHashtable();
        printHashtable();

        // Пошук після сортування
        findByKeyInHashtable();
        findByValueInHashtable();
        findMinMaxKeyInHashtable();

        addEntryToHashtable();

        removeByKeyFromHashtable();
        removeByValueFromHashtable();

        System.out.println("Кінцевий розмір Hashtable: " + hashtable.size());

        // Потім обробляємо TreeMap
        System.out.println("\n\n========= Операції з TreeMap =========");
        System.out.println("Початковий розмір TreeMap: " + treeMap.size());

        findByKeyInTreeMap();
        findByValueInTreeMap();
        findMinMaxKeyInTreeMap();

        printTreeMap();

        addEntryToTreeMap();

        removeByKeyFromTreeMap();
        removeByValueFromTreeMap();

        System.out.println("Кінцевий розмір TreeMap: " + treeMap.size());
    }

    // ===== Методи для Hashtable =====

    /**
     * Виводить вміст Hashtable.
     * Hashtable не гарантує жодного порядку елементів: він залежить від hashCode() ключів.
     */
    private void printHashtable() {
        System.out.println("\n=== Пари ключ-значення в Hashtable ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Cactus, String> entry : hashtable.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в Hashtable");
    }

    /**
     * Сортує Hashtable за ключами.
     * Використовує Collections.sort() з природним порядком Cactus (Cactus.compareTo())
     * і перезаписує hashtable новою Hashtable, заповненою у відсортованому порядку.
     *
     * <p>Увага: Hashtable не зберігає порядок додавання, тому після сортування порядок
     * виведення не зміниться. Зберегти відсортований порядок може LinkedHashMap.</p>
     */
    private void sortHashtable() {
        long timeStart = System.nanoTime();

        // Створюємо список ключів і сортуємо за природним порядком Cactus
        List<Cactus> sortedKeys = new ArrayList<>(hashtable.keySet());
        Collections.sort(sortedKeys);

        // Створюємо нову Hashtable і додаємо пари у порядку відсортованих ключів
        Hashtable<Cactus, String> sortedHashtable = new Hashtable<>();
        for (Cactus key : sortedKeys) {
            sortedHashtable.put(key, hashtable.get(key));
        }

        // Перезаписуємо оригінальну hashtable
        hashtable = sortedHashtable;

        PerformanceTracker.displayOperationTime(timeStart, "сортування Hashtable за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в Hashtable.
     * Використовує Cactus.hashCode() для вибору кошика та Cactus.equals() для порівняння ключів.
     */
    void findByKeyInHashtable() {
        long timeStart = System.nanoTime();

        boolean found = hashtable.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в Hashtable");

        if (found) {
            String value = hashtable.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в Hashtable.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в Hashtable.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInHashtable() {
        long timeStart = System.nanoTime();

        // Створюємо список пар та сортуємо його за значеннями
        List<Map.Entry<Cactus, String>> entries = new ArrayList<>(hashtable.entrySet());
        Collections.sort(entries, OWNER_COMPARATOR);

        // Тимчасова пара для пошуку: ключ не потрібен, порівнюється лише значення.
        // До Hashtable вона не додається, тому null як ключ тут допустимий.
        Map.Entry<Cactus, String> searchEntry = new AbstractMap.SimpleEntry<>(null, VALUE_TO_SEARCH_AND_DELETE);

        int position = Collections.binarySearch(entries, searchEntry, OWNER_COMPARATOR);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в Hashtable");

        if (position >= 0) {
            Map.Entry<Cactus, String> foundEntry = entries.get(position);
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Кактус: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в Hashtable.");
        }
    }

    /**
     * Визначає найменший і найбільший ключі в Hashtable.
     * Hashtable не зберігає порядок ключів, тому потрібен перебір усіх ключів - O(n),
     * і сортування Hashtable цього не змінює.
     */
    void findMinMaxKeyInHashtable() {
        if (hashtable.isEmpty()) {
            System.out.println("Hashtable порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        Cactus minKey = Collections.min(hashtable.keySet());
        Cactus maxKey = Collections.max(hashtable.keySet());

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в Hashtable");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    /**
     * Додає новий запис до Hashtable.
     */
    void addEntryToHashtable() {
        long timeStart = System.nanoTime();

        hashtable.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до Hashtable");

        System.out.println("Додано новий запис: кактус='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з Hashtable за ключем.
     */
    void removeByKeyFromHashtable() {
        long timeStart = System.nanoTime();

        String removedValue = hashtable.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з Hashtable");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з Hashtable за значенням.
     * Ключі спочатку збираються в список, бо remove() під час перебору entrySet()
     * спричинив би ConcurrentModificationException.
     */
    void removeByValueFromHashtable() {
        long timeStart = System.nanoTime();

        List<Cactus> keysToRemove = new ArrayList<>();
        for (Map.Entry<Cactus, String> entry : hashtable.entrySet()) {
            if (VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (Cactus key : keysToRemove) {
            hashtable.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з Hashtable");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для TreeMap =====

    /**
     * Виводить вміст TreeMap.
     * TreeMap завжди впорядкована за ключами (рід за зростанням, довжина колючок за зростанням).
     */
    private void printTreeMap() {
        System.out.println("\n=== Пари ключ-значення в TreeMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Cactus, String> entry : treeMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в TreeMap");
    }

    /**
     * Здійснює пошук елемента за ключем в TreeMap.
     * Використовує Cactus.compareTo() для навігації по червоно-чорному дереву.
     */
    void findByKeyInTreeMap() {
        long timeStart = System.nanoTime();

        boolean found = treeMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в TreeMap");

        if (found) {
            String value = treeMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в TreeMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в TreeMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInTreeMap() {
        long timeStart = System.nanoTime();

        List<Map.Entry<Cactus, String>> entries = new ArrayList<>(treeMap.entrySet());
        Collections.sort(entries, OWNER_COMPARATOR);

        Map.Entry<Cactus, String> searchEntry = new AbstractMap.SimpleEntry<>(null, VALUE_TO_SEARCH_AND_DELETE);

        int position = Collections.binarySearch(entries, searchEntry, OWNER_COMPARATOR);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в TreeMap");

        if (position >= 0) {
            Map.Entry<Cactus, String> foundEntry = entries.get(position);
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Кактус: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в TreeMap.");
        }
    }

    /**
     * Визначає найменший і найбільший ключі в TreeMap.
     * TreeMap реалізує SequencedMap (Java 21) і впорядкована за Cactus.compareTo(),
     * тому перший і останній записи - це найменший і найбільший ключі; перебір не потрібен.
     */
    void findMinMaxKeyInTreeMap() {
        if (treeMap.isEmpty()) {
            System.out.println("TreeMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        Cactus minKey = treeMap.firstEntry().getKey();
        Cactus maxKey = treeMap.lastEntry().getKey();

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в TreeMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    /**
     * Додає новий запис до TreeMap.
     */
    void addEntryToTreeMap() {
        long timeStart = System.nanoTime();

        treeMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до TreeMap");

        System.out.println("Додано новий запис: кактус='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з TreeMap за ключем.
     */
    void removeByKeyFromTreeMap() {
        long timeStart = System.nanoTime();

        String removedValue = treeMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з TreeMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з TreeMap за значенням.
     */
    void removeByValueFromTreeMap() {
        long timeStart = System.nanoTime();

        List<Cactus> keysToRemove = new ArrayList<>();
        for (Map.Entry<Cactus, String> entry : treeMap.entrySet()) {
            if (VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (Cactus key : keysToRemove) {
            treeMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з TreeMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    /**
     * Головний метод для запуску програми.
     */
    public static void main(String[] args) {
        // Початкові дані (ключ: Cactus, значення: власник або власниця).
        // Map.of() створює незмінну Map, тому з неї створюються копії, які можна змінювати.
        Map<Cactus, String> cacti = Map.of(
            new Cactus("Мамілярія", 5.5), "Аліна",
            new Cactus("Ехінокактус", 30.0), "Роман",
            new Cactus("Опунція", 2.1), "Злата",
            new Cactus("Мамілярія", 8.2), "Владислав",
            new Cactus("Астрофітум", 0.5), "Аліна",
            new Cactus("Цереус", 12.4), "Матвій",
            new Cactus("Опунція", 3.6), "Вероніка",
            new Cactus("Ребуція", 4.0), "Ярослава",
            new Cactus("Гімнокаліціум", 9.3), "Давид",
            new Cactus("Ехінопсис", 15.7), "Лілія"
        );

        Hashtable<Cactus, String> hashtable = new Hashtable<>(cacti);
        TreeMap<Cactus, String> treeMap = new TreeMap<>(cacti);

        // Створюємо об'єкт і виконуємо операції
        BasicDataOperationsUsingMap operations = new BasicDataOperationsUsingMap(hashtable, treeMap);
        operations.executeDataOperations();
    }
}
