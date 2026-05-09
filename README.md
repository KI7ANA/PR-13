<div align="center">

# Отчёт

</div>

<div align="center">

## Практическая работа №13

</div>

<div align="center">

## Обработка жестов

</div>

**Выполнил:**  
Ткачев Сергей Юрьевич  
**Курс:** 2  
**Группа:** ИНС-б-о-24-2  
**Направление:** ИПИНЖ (Институт перспективной инженерии)  
**Профиль:** Информационные системы и технологии  

---

### Цель работы

Изучить механизмы обработки сенсорных жестов в Android. Научиться создавать собственные обработчики для распознавания свайпов, долгого нажатия, двойного касания и других движений пальца по экрану. Интегрировать обработку жестов в игровое приложение, разрабатываемое в рамках практических работ.

---

### Ход работы

#### Задание 1: Создание проекта и подготовка интерфейса

1. Был открыт Android Studio и создан новый проект с шаблоном **Empty Views Activity**.
2. Проекту было дано имя `PW_13`.
3. В качестве языка программирования был выбран **Java**.
4. Package name проекта был задан как:

```text
com.ncfu.pw_13
```

5. Согласно варианту 5 необходимо реализовать обработку жестов для приложения по теме **«Морской бой»**.
6. В приложении свайпы используются для размещения корабля горизонтально или вертикально.
7. Также были добавлены дополнительные жесты:
- долгое нажатие — очистка размещения;
- двойное касание — сброс позиции корабля.

8. В файле `activity_main.xml` был создан интерфейс приложения.
9. На экран были добавлены:
- заголовок приложения;
- инструкция для пользователя;
- игровое поле;
- объект корабля;
- поле статуса;
- подсказка по доступным жестам.

#### Листинг 1. Содержимое файла `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center_horizontal"
    android:padding="16dp">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Морской бой: обработка жестов"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="16dp" />

    <TextView
        android:id="@+id/tvInstruction"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Сделайте свайп по кораблю"
        android:textSize="18sp"
        android:gravity="center"
        android:layout_marginBottom="24dp" />

    <FrameLayout
        android:id="@+id/gameField"
        android:layout_width="match_parent"
        android:layout_height="420dp"
        android:background="#E3F2FD"
        android:layout_marginBottom="16dp">

        <TextView
            android:id="@+id/shipView"
            android:layout_width="160dp"
            android:layout_height="70dp"
            android:layout_gravity="center"
            android:background="#2196F3"
            android:gravity="center"
            android:text="КОРАБЛЬ"
            android:textColor="#FFFFFF"
            android:textStyle="bold"
            android:textSize="16sp" />

    </FrameLayout>

    <TextView
        android:id="@+id/tvStatus"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Статус: ожидание жеста"
        android:textSize="16sp"
        android:gravity="center"
        android:layout_marginBottom="12dp" />

    <TextView
        android:id="@+id/tvHint"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Свайп влево/вправо — горизонтально\nСвайп вверх/вниз — вертикально\nДолгое нажатие — очистить\nДвойное касание — сброс позиции"
        android:textSize="14sp"
        android:gravity="center" />

</LinearLayout>
```

<div align="center">

<img width="387" height="794" alt="Снимок экрана 2026-05-09 225025" src="https://github.com/user-attachments/assets/13acef2b-cef1-411f-bb91-35727bdc382b" />

*Рисунок 1. Главный экран приложения для обработки жестов*

</div>

---

#### Задание 2: Создание класса OnSwipeTouchListener

1. В проекте был создан новый Java-класс `OnSwipeTouchListener`.
2. Класс реализует интерфейс `View.OnTouchListener`.
3. Для распознавания жестов используется класс `GestureDetector`.
4. Внутри класса был создан вложенный класс `GestureListener`, который наследуется от `GestureDetector.SimpleOnGestureListener`.
5. Для распознавания свайпов был переопределён метод `onFling()`.
6. Для обработки дополнительных жестов были добавлены:
- `onLongPress()` — долгое нажатие;
- `onDoubleTap()` — двойное касание;
- `onScroll()` — перемещение пальца по экрану.

#### Листинг 2. Код файла `OnSwipeTouchListener.java`

```java
package com.ncfu.pw_13;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            onDoubleClick();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            onLongClick();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            onScrollGesture();
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;

            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD &&
                            Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {

                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }

                        result = true;
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD &&
                            Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {

                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }

                        result = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    public void onSwipeRight() { }

    public void onSwipeLeft() { }

    public void onSwipeTop() { }

    public void onSwipeBottom() { }

    public void onLongClick() { }

    public void onDoubleClick() { }

    public void onScrollGesture() { }
}
```

---

#### Задание 3: Применение обработчика жестов

1. В файле `MainActivity.java` была получена ссылка на объект `shipView`.
2. Для объекта корабля был установлен обработчик `OnSwipeTouchListener`.
3. Для каждого жеста была реализована отдельная реакция:
- свайп вправо — горизонтальное размещение корабля и перемещение вправо;
- свайп влево — горизонтальное размещение корабля и перемещение влево;
- свайп вверх — вертикальное размещение корабля и перемещение вверх;
- свайп вниз — вертикальное размещение корабля и перемещение вниз;
- долгое нажатие — очистка размещения;
- двойное касание — сброс позиции корабля.

4. Для визуальной обратной связи изменяется:
- положение корабля;
- размер корабля;
- цвет корабля;
- текст внутри корабля;
- текст статуса.

#### Листинг 3. Код файла `MainActivity.java`

```java
package com.ncfu.pw_13;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView shipView;
    private TextView tvStatus;

    private float startX;
    private float startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shipView = findViewById(R.id.shipView);
        tvStatus = findViewById(R.id.tvStatus);

        startX = shipView.getX();
        startY = shipView.getY();

        shipView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                setHorizontalShip();
                moveShip(80, 0);
                tvStatus.setText("Статус: свайп вправо — корабль размещён горизонтально");
                Toast.makeText(MainActivity.this, "Свайп вправо", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                setHorizontalShip();
                moveShip(-80, 0);
                tvStatus.setText("Статус: свайп влево — корабль размещён горизонтально");
                Toast.makeText(MainActivity.this, "Свайп влево", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeTop() {
                setVerticalShip();
                moveShip(0, -80);
                tvStatus.setText("Статус: свайп вверх — корабль размещён вертикально");
                Toast.makeText(MainActivity.this, "Свайп вверх", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeBottom() {
                setVerticalShip();
                moveShip(0, 80);
                tvStatus.setText("Статус: свайп вниз — корабль размещён вертикально");
                Toast.makeText(MainActivity.this, "Свайп вниз", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick() {
                shipView.setBackgroundColor(Color.GRAY);
                shipView.setText("ОЧИЩЕНО");
                tvStatus.setText("Статус: долгое нажатие — размещение очищено");
                Toast.makeText(MainActivity.this, "Долгое нажатие", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDoubleClick() {
                resetShip();
                tvStatus.setText("Статус: двойное касание — позиция корабля сброшена");
                Toast.makeText(MainActivity.this, "Двойное касание", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrollGesture() {
                shipView.setAlpha(0.8f);
            }
        });
    }

    private void setHorizontalShip() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(180, 70);
        params.leftMargin = (int) shipView.getX();
        params.topMargin = (int) shipView.getY();

        shipView.setLayoutParams(params);
        shipView.setBackgroundColor(Color.rgb(33, 150, 243));
        shipView.setText("ГОРИЗОНТАЛЬНО");
        shipView.setAlpha(1.0f);
    }

    private void setVerticalShip() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(80, 180);
        params.leftMargin = (int) shipView.getX();
        params.topMargin = (int) shipView.getY();

        shipView.setLayoutParams(params);
        shipView.setBackgroundColor(Color.rgb(76, 175, 80));
        shipView.setText("ВЕРТ.");
        shipView.setAlpha(1.0f);
    }

    private void moveShip(float dx, float dy) {
        shipView.setX(shipView.getX() + dx);
        shipView.setY(shipView.getY() + dy);
    }

    private void resetShip() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(160, 70);
        params.gravity = android.view.Gravity.CENTER;

        shipView.setLayoutParams(params);
        shipView.setBackgroundColor(Color.rgb(33, 150, 243));
        shipView.setText("КОРАБЛЬ");
        shipView.setAlpha(1.0f);
    }
}
```

---

#### Задание 4: Обработка горизонтальных свайпов

1. Горизонтальные свайпы используются для размещения корабля в горизонтальном положении.
2. При свайпе вправо корабль перемещается вправо.
3. При свайпе влево корабль перемещается влево.
4. После распознавания жеста изменяется статус приложения.
5. Пользователю также показывается всплывающее сообщение `Toast`.

<div align="center">

<img width="388" height="779" alt="Снимок экрана 2026-05-09 225237" src="https://github.com/user-attachments/assets/ca72289d-ba91-4b66-b80d-bb7db326d6ca" />

*Рисунок 2. Горизонтальное размещение корабля после свайпа вправо или влево*

</div>

---

#### Задание 5: Обработка вертикальных свайпов

1. Вертикальные свайпы используются для размещения корабля в вертикальном положении.
2. При свайпе вверх корабль перемещается вверх.
3. При свайпе вниз корабль перемещается вниз.
4. После жеста корабль меняет размер и цвет.
5. В статусе отображается направление свайпа и результат действия.

<div align="center">

<img width="396" height="799" alt="Снимок экрана 2026-05-09 225247" src="https://github.com/user-attachments/assets/d7f4d609-2241-486c-a14e-6c3f3f641677" />

*Рисунок 3. Вертикальное размещение корабля после свайпа вверх или вниз*

</div>

---

#### Задание 6: Обработка дополнительных жестов

1. В приложение были добавлены дополнительные жесты.
2. Долгое нажатие используется для очистки размещения корабля.
3. При долгом нажатии корабль становится серым, а текст внутри него меняется на **ОЧИЩЕНО**.
4. Двойное касание используется для сброса позиции корабля.
5. При двойном касании корабль возвращается в исходное состояние.

<div align="center">

<img width="383" height="794" alt="image" src="https://github.com/user-attachments/assets/8815e1a7-ac05-4b87-809c-fe7522869c06" />

*Рисунок 4. Очистка размещения корабля при долгом нажатии*

</div>

<div align="center">

<img width="381" height="795" alt="Снимок экрана 2026-05-09 225325" src="https://github.com/user-attachments/assets/e8f232ed-edc2-432e-b5d2-c0fe3bcf66b0" />

*Рисунок 5. Сброс позиции корабля при двойном касании*

</div>

---

#### Задание 7: Тестирование приложения

1. Приложение было запущено на эмуляторе.
2. Был проверен главный экран приложения.
3. Был выполнен свайп вправо по кораблю.
4. Был выполнен свайп влево по кораблю.
5. Был выполнен свайп вверх по кораблю.
6. Был выполнен свайп вниз по кораблю.
7. Было проверено долгое нажатие.
8. Было проверено двойное касание.
9. Все жесты были успешно распознаны, а объект на экране изменялся в соответствии с выбранным действием.

---

### Вывод

В результате выполнения практической работы были изучены механизмы обработки сенсорных жестов в Android. Был создан собственный обработчик `OnSwipeTouchListener`, использующий `GestureDetector` для распознавания свайпов, долгого нажатия, двойного касания и прокрутки.

В соответствии с вариантом 5 обработка жестов была реализована для приложения **«Морской бой»**. Свайпы вправо и влево используются для горизонтального размещения корабля, а свайпы вверх и вниз — для вертикального размещения. Также были добавлены дополнительные действия: долгое нажатие очищает размещение, а двойное касание сбрасывает позицию корабля.

В ходе работы были получены навыки обработки сенсорных событий, изменения положения элементов интерфейса и предоставления визуальной обратной связи пользователю. Цель практической работы была достигнута.

---

### Ответы на контрольные вопросы

1. **Что такое MotionEvent? Какие основные типы событий actions в нём существуют?**

   `MotionEvent` — это класс, который содержит информацию о сенсорном событии на экране. Он хранит данные о координатах касания, движении пальца и типе действия.

   Основные типы событий:
- `ACTION_DOWN` — палец коснулся экрана;
- `ACTION_MOVE` — палец перемещается по экрану;
- `ACTION_UP` — палец отпущен;
- `ACTION_CANCEL` — событие было отменено системой.

---

2. **Для чего используется класс GestureDetector? В чём его преимущество перед обработкой сырых MotionEvent?**

   `GestureDetector` используется для распознавания более сложных жестов, например свайпов, двойного касания, долгого нажатия и прокрутки.

   Его преимущество заключается в том, что он сам анализирует последовательность событий `MotionEvent` и вызывает нужные методы обработчика. Благодаря этому не нужно вручную вычислять все координаты, скорость движения и длительность касания.

---

3. **Какой метод GestureDetector отвечает за распознавание быстрого смахивания свайпа? Какие параметры он принимает?**

   За распознавание быстрого смахивания отвечает метод `onFling()`.

   Пример метода:

   ```java
   public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
   ```

   Параметры:
- `e1` — событие начала жеста;
- `e2` — событие окончания жеста;
- `velocityX` — скорость движения по оси X;
- `velocityY` — скорость движения по оси Y.

---

4. **Зачем в методе onDown() необходимо возвращать true?**

   В методе `onDown()` нужно возвращать `true`, чтобы система понимала, что обработчик заинтересован в дальнейшей обработке жеста.

   Если вернуть `false`, то последующие события, например `onFling()`, `onLongPress()` или `onDoubleTap()`, могут не сработать.

   Пример:

   ```java
   @Override
   public boolean onDown(MotionEvent event) {
       return true;
   }
   ```

---

5. **Как отличить горизонтальный свайп от вертикального? Какие параметры для этого используются?**

   Чтобы отличить горизонтальный свайп от вертикального, нужно сравнить смещение пальца по оси X и по оси Y.

   В коде вычисляются значения:

   ```java
   float diffX = e2.getX() - e1.getX();
   float diffY = e2.getY() - e1.getY();
   ```

   Если `Math.abs(diffX) > Math.abs(diffY)`, значит свайп горизонтальный.  
   Если `Math.abs(diffY) > Math.abs(diffX)`, значит свайп вертикальный.

---

6. **Что такое пороговые значения threshold и зачем они нужны при распознавании жестов?**

   Пороговые значения — это минимальные значения расстояния и скорости, которые должен преодолеть жест, чтобы он был распознан как свайп.

   Они нужны для того, чтобы случайные короткие движения пальца не распознавались как полноценный жест.

   В данной работе использовались два порога:

   ```java
   private static final int SWIPE_THRESHOLD = 100;
   private static final int SWIPE_VELOCITY_THRESHOLD = 100;
   ```

   `SWIPE_THRESHOLD` отвечает за минимальное расстояние свайпа, а `SWIPE_VELOCITY_THRESHOLD` — за минимальную скорость движения.

---

7. **Как заставить View реагировать на сенсорные события? Какой слушатель для этого используется?**

   Чтобы элемент интерфейса реагировал на сенсорные события, нужно установить для него слушатель `View.OnTouchListener`.

   Пример:

   ```java
   shipView.setOnTouchListener(new OnSwipeTouchListener(this) {
       @Override
       public void onSwipeRight() {
           // действие при свайпе вправо
       }
   });
   ```

   После этого элемент будет получать события касания и передавать их обработчику.

---

8. **Какие ещё жесты можно распознать с помощью GestureDetector? Назовите не менее трёх.**

   С помощью `GestureDetector` можно распознавать разные жесты:
- `onSingleTapUp()` — одиночное касание;
- `onDoubleTap()` — двойное касание;
- `onLongPress()` — долгое нажатие;
- `onScroll()` — прокрутка или перемещение пальца;
- `onFling()` — быстрый свайп;
- `onShowPress()` — короткое удержание перед отпусканием пальца.
