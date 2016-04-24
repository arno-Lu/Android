package com.example.mechrevo.measuretest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mechrevo on 2016/4/24.
 */
public class MeasureExampleView extends View {

    public MeasureExampleView(Context context){
        super(context);
    }
    public MeasureExampleView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }
    @Override
    protected void onMeasure(int widthMeasure,int heightMeasure){
        setMeasuredDimension(measureWidth(widthMeasure),measureHeight(heightMeasure));
    }

    private int measureWidth(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode ==MeasureSpec.EXACTLY){
                result =specSize;
    }else{
            result = 400;
            if(specMode ==MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode ==MeasureSpec.EXACTLY){
            result =specSize;
        }else{
            result = 200;
            if(specMode ==MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }
}
