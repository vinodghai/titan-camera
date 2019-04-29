package net.titan.camera.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by VINOD KUMAR on 12/24/2018.
 */
public class Camera2TextureView extends TextureView {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public Camera2TextureView(Context context) {
        super(context);
    }

    public Camera2TextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Camera2TextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAspectRatio(int width, int height) {
        if (width >= 0 && height >= 0) {
            mRatioWidth = width;
            mRatioHeight = height;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width > height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }
}
