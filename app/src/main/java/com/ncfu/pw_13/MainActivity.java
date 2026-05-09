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