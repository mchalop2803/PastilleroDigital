package activities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import java.util.List;

public class MultiDotSpan implements LineBackgroundSpan {

    private final List<Integer> colors;

    public MultiDotSpan(List<Integer> colors) {
        this.colors = colors;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint,
                               int left, int right, int top,
                               int baseline, int bottom,
                               CharSequence text,
                               int start, int end, int lnum) {

        float centerX = (left + right) / 2f;
        float y = bottom + 10;

        float spacing = 14f;
        float startX = centerX - ((colors.size() - 1) * spacing) / 2f;

        Paint p = new Paint();
        p.setAntiAlias(true);

        for (int i = 0; i < colors.size(); i++) {
            p.setColor(colors.get(i));
            canvas.drawCircle(startX + (i * spacing), y, 5f, p);
        }
    }
}