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
    // Constructor for the BarChartView class
    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Retrieve layout height and width attributes from XML
        String layout_height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        String layout_width = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
        // Remove "dip" from the attribute values and calculate density
        layout_height = layout_height.replace("dip", "");
        layout_width = layout_width.replace("dip", "");
        float density = getResources().getDisplayMetrics().density;
        // Set the height and width using density-adjusted values
        HEIGHT = density * Float.parseFloat(layout_height);
        WIDTH = density * Float.parseFloat(layout_width);
        Log.d(TAG, "WIDTH:" + WIDTH + "," + originX);
        Log.d(TAG, "HEIGHT:" + HEIGHT + "," + originY);
        // Initialize Paint object for drawing
        paint = new Paint();
    }
    // Setter method to set the dailyList data and trigger a redraw
    public void setdailyList(List<daily> dailyList) {
        this.dailyList = dailyList;
        invalidate();
    }
    // Override the onDraw method to handle the drawing of the bar chart
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Check if the dailyList is not null
        if (dailyList != null) {
            // Calculate the width of each bar in the chart
            float barWidth = (float) getWidth() / dailyList.size();
            // Define the spacing between the bars
            float spacing = 5;
            float xAxisOffset = 40;
            float yAxisOffset = 30;
            // Iterate over the daily data to draw the bars and legends
            for (int i = 0; i < dailyList.size(); i++) {
                daily data = dailyList.get(i);
                float left = i * barWidth + spacing + yAxisOffset + 25;
                float top = getHeight() - getUvHeight(data.getUvi()) - xAxisOffset;
                float right = left + barWidth - 2 * spacing - 30;
                float bottom = getHeight() - xAxisOffset;
                // Set the color of the bar based on the UV index
                paint.setColor(getColorForUvIndex(data.getUvi()));
                // Draw the bar on the canvas
                canvas.drawRect(left, top, right, bottom, paint);
                // Draw the legend on the X-axis
                paint.setColor(Color.BLACK);
                paint.setTextSize(40);
                //canvas.drawText(date(data.getDt()), left, getHeight() - 10, paint);
                canvas.drawText(date(data.getDt()), left, getHeight() + 40 - xAxisOffset, paint); // Ajusta el valor "20" según tus necesidades
            }
            // Draw the legend and lines on the Y-axis
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            System.out.println("Valor de getHeight: " + getHeight());
            drawYAxisLabelAndLine(canvas, "0", getHeight());
            drawYAxisLabelAndLine(canvas, "2", getHeight() - getUvHeight(2) + 30 - xAxisOffset);
            drawYAxisLabelAndLine(canvas, "5", getHeight() - getUvHeight(5) + 30 - xAxisOffset);
            drawYAxisLabelAndLine(canvas, "7", getHeight() - getUvHeight(7) + 30 - xAxisOffset);
            drawYAxisLabelAndLine(canvas, "10", getHeight() - getUvHeight(10) + 30 - xAxisOffset);
            drawYAxisLabelAndLine(canvas, "11+", getHeight() - getUvHeight(11) + 30 - xAxisOffset);
            // Draw the legend on the X-axi
        }
    }
    // Method to draw a label and a horizontal line on the Y-axis
    private void drawYAxisLabelAndLine(Canvas canvas, String text, float y) {
        // Draw the label text at the specified position
        canvas.drawText(text, 20, y, paint);

        // Draw a horizontal line on the Y-axis, from the left end to the right end of the canvas
        canvas.drawLine(0, y, getWidth(), y, paint);
    }

    // Method to get the height of the bar based on the UV index
    private float getUvHeight(double uvIndex) {
        // You can implement logic here to convert the UV index into a bar height.
        // This is just an example and may need adjustment for your use case.
        return (float) getHeight() * (float) uvIndex / 11;
    }
    // Method to get the color for the UV index
    private int getColorForUvIndex(double uvIndex) {
        // You can implement logic here to assign a color based on the UV index.
        // This is just an example and may need adjustment for your use case.
        if (uvIndex <= 2) {
            return Color.parseColor("#08A3E2");
        } else if (uvIndex <= 5) {
            return Color.parseColor("800080"); //purple
        } else if (uvIndex <= 7) {
            return Color.RED;
        } else if (uvIndex <= 10) {
            return Color.BLUE;
        } else {
            return Color.parseColor("#4DEEEE"); //blue sky
        }
    }
    // Override the onTouchEvent method to handle touch events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Print a message to the console when a bar is touched
            System.out.println("Bar touched!");
            return true;
        }
        return super.onTouchEvent(event);
    }

    // Method to format the date based on timestamp
    private String date(long timestamp){
        java.util.Date time=new java.util.Date((long)timestamp*1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Lima"));
        String formattedDate = sdf.format(time);
        System.out.println("Fecha formateada: " + formattedDate);
        return formattedDate;
    }
}
