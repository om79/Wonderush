package com.programize.wonderush.Utilities.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.programize.wonderush.R;

public class DrawView extends View {

    public DrawView(Context context) {
        super(context);
    }


    public void onDraw(Canvas c){
        super.onDraw(c);

        Paint paint = new Paint();

        //draw background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.blue_dark));
        paint.setAntiAlias(true);
        c.drawPaint(paint);

        //draw line
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(0, 0);
        path.lineTo((c.getWidth()) / 14, c.getHeight());
        path.lineTo((c.getWidth() * 2)  / 14, 15);
        path.lineTo((c.getWidth() * 3)  / 14, c.getHeight());
        path.lineTo((c.getWidth() * 4)  / 14, 5);
        path.lineTo((c.getWidth() * 5)  / 14, c.getHeight());
        path.lineTo((c.getWidth() * 6)  / 14, 10);
        path.lineTo((c.getWidth() * 7)  / 14, c.getHeight());
        path.lineTo((c.getWidth() * 8)  / 14, 2);
        path.lineTo((c.getWidth() * 9)  / 14, c.getHeight());
        path.lineTo((c.getWidth() * 10) / 14, 10);
        path.lineTo((c.getWidth() * 11) / 14, c.getHeight());
        path.lineTo((c.getWidth() * 12) / 14, 4);
        path.lineTo((c.getWidth() * 13) / 14, c.getHeight());
        path.lineTo((c.getWidth() * 14) / 14, 0);
        path.moveTo(0, 0);
        path.close();

        //draw line in layout
        paint.setStrokeWidth(5);
        paint.setColor(getResources().getColor(R.color.white2));
        paint.setAntiAlias(true);
        c.drawPath(path, paint);





    }

}