Практична робота №3 «Робота з Map в Java» з дисципліни "Програмування мовою Java". Варіант 5: Ващенко Владислав Олександрович.

- [Головна сторінка навчального проєкту](https://github.com/chdbc-samples/training-data-26)

## Завдання

```text
Назва record-класу рослини: Cactus (кактус)
Характеристики рослини: рід (String genus), довжина колючок, мм (Double spineLength)

Сортування:
- рід - за зростанням
- довжина колючок - за зростанням

Map<рослина, власник (власниця)>:
  Cactus{genus='Мамілярія', spineLength=5.5} -> Аліна
  Cactus{genus='Ехінокактус', spineLength=30.0} -> Роман
  Cactus{genus='Опунція', spineLength=2.1} -> Злата
  Cactus{genus='Мамілярія', spineLength=8.2} -> Владислав
  Cactus{genus='Астрофітум', spineLength=0.5} -> Аліна
  Cactus{genus='Цереус', spineLength=12.4} -> Матвій
  Cactus{genus='Опунція', spineLength=3.6} -> Вероніка
  Cactus{genus='Ребуція', spineLength=4.0} -> Ярослава
  Cactus{genus='Гімнокаліціум', spineLength=9.3} -> Давид
  Cactus{genus='Ехінопсис', spineLength=15.7} -> Лілія

Ключ/значення для додавання: Cactus{genus='Ферокактус', spineLength=40.2} -> Кирило
Ключ для пошуку і видалення: Cactus{genus='Опунція', spineLength=2.1}
Значення для пошуку і видалення: Аліна
Порівняти продуктивність: TreeMap з Hashtable
```

## Як запустити

Скомпілювати й виконати з кореня репозиторію (Windows PowerShell):

```powershell
chcp 65001
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

javac *.java
java BasicDataOperationsUsingMap
```

## Що змінено відносно прикладу

Record `Oak` замінено на `Cactus(String genus, Double spineLength)`. `compareTo()`
порівнює рід за зростанням, а для однакового роду довжину колючок теж за
зростанням. Обидва порівняння йдуть через `compareTo()`, тому метод повертає 0
лише для однакових кактусів, як і `equals()`.

Блок `HashMap` перейменовано для `Hashtable`: поле, методи
`printHashtable()`, `sortHashtable()`, `findByKeyInHashtable()` та інші, назви
операцій у логах. Блок `TreeMap` лишився без змін. Компаратор пар за значенням
тепер називається `OWNER_COMPARATOR`, бо значення це власник кактуса.

## Результати запуску

Початковий розмір обох Map 10, кінцевий 8: додано одну пару, видалено пару з
ключем Опунція 2.1 і дві пари з власницею Аліна.

Найменший ключ Астрофітум 0.5, найбільший Цереус 12.4, однакові в обох Map до
і після сортування. `TreeMap` виводить пари в порядку ключів, а `Hashtable` і
після сортування виводить їх у порядку, що залежить від `hashCode()`.
