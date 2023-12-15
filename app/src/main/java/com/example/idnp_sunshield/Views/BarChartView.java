package com.example.idnp_sunshield.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.idnp_sunshield.Models.daily;

import java.util.List;

public class BarChartView extends View {
    private static final String TAG = "BarChartView";
    private Paint paint;
    private List<daily> dailyList;
    private float WIDTH;
    private float HEIGHT;
    private float originY;
    private float originX;
    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        String layout_height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        String layout_width = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        layout_height = layout_height.replace("dip", "");
        layout_width = layout_width.replace("dip", "");
        float density = getResources().getDisplayMetrics().density;
        HEIGHT = density * Float.parseFloat(layout_height);
        WIDTH = density * Float.parseFloat(layout_width);
        Log.d(TAG, "WIDTH:" + WIDTH + "," + originX);
        Log.d(TAG, "HEIGHT:" + HEIGHT + "," + originY);
        paint = new Paint();
    }
    public void setdailyList(List<daily> dailyList) {
        this.dailyList = dailyList;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Verifica si la lista de datos diarios no es nula
        if (dailyList != null) {
            // Calcula el ancho de cada barra en el gráfico
            float barWidth = (float) getWidth() / dailyList.size();
            // Define el espacio entre las barras
            float spacing = 5;

            // Itera sobre los datos diarios para dibujar las barras y las leyendas
            for (int i = 0; i < dailyList.size(); i++) {
                daily data = dailyList.get(i);
                float left = i * barWidth + spacing;
                float top = getHeight() - getUvHeight(data.getUvi());
                float right = left + barWidth - 2 * spacing;
                float bottom = getHeight();

                // Establece el color de la barra según el índice UV
                paint.setColor(getColorForUvIndex(data.getUvi()));
                // Dibuja la barra en el lienzo
                canvas.drawRect(left, top, right, bottom, paint);

                // Dibuja la leyenda del eje X
                paint.setColor(Color.BLACK);
                paint.setTextSize(35);
                canvas.drawText(date(data.getDt()), left, getHeight() - 10, paint);
            }

            // Dibuja la leyenda y las líneas del eje Y
            paint.setColor(Color.BLACK);
            paint.setTextSize(60);
            drawYAxisLabelAndLine(canvas, "0", getHeight());
            drawYAxisLabelAndLine(canvas, "2", getHeight() - getUvHeight(2));
            drawYAxisLabelAndLine(canvas, "5", getHeight() - getUvHeight(5));
            drawYAxisLabelAndLine(canvas, "7", getHeight() - getUvHeight(7));
            drawYAxisLabelAndLine(canvas, "10", getHeight() - getUvHeight(10));
            drawYAxisLabelAndLine(canvas, "11+", getHeight() - getUvHeight(11));
        }
    }
    // Método para dibujar una etiqueta y una línea horizontal en el eje Y
    private void drawYAxisLabelAndLine(Canvas canvas, String text, float y) {
        // Dibuja el texto de la etiqueta en la posición especificada
        canvas.drawText(text, 0, y, paint);

        // Configura el grosor de la línea que se va a dibujar a continuación
        paint.setStrokeWidth(2);

        // Dibuja una línea horizontal en el eje Y, desde el extremo izquierdo hasta el extremo derecho del canvas
        canvas.drawLine(0, y, getWidth(), y, paint);
    }
    private float getUvHeight(double uvIndex) {
        // Aquí puedes implementar la lógica para convertir el índice UV en una altura de barra.
        // Este es solo un ejemplo y puede que necesites ajustarlo para tu caso de uso.
        return (float) getHeight() * (float) uvIndex / 11;
    }
    private int getColorForUvIndex(double uvIndex) {
        // Aquí puedes implementar la lógica para asignar un color basado en el índice UV.
        // Este es solo un ejemplo y puede que necesites ajustarlo para tu caso de uso.
        if (uvIndex <= 2) {
            return Color.MAGENTA;
        } else if (uvIndex <= 5) {
            return Color.YELLOW;
        } else if (uvIndex <= 7) {
            return Color.RED;
        } else if (uvIndex <= 10) {
            return Color.BLUE;
        } else {
            return Color.CYAN;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("Barra tocada!");
            return true;
        }
        return super.onTouchEvent(event);
    }
    private String date(long timestamp){
        java.util.Date time=new java.util.Date((long)timestamp*1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Lima"));
        String formattedDate = sdf.format(time);
        System.out.println("Fecha formateada: " + formattedDate);
        return formattedDate;
    }
}
