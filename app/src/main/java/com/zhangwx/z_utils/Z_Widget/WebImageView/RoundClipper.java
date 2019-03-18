package com.zhangwx.z_utils.Z_Widget.WebImageView;

import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;


public class RoundClipper {
	private int mSavedCount;
	private int mSavedFlags;
	private DrawFilter mSavedFilter;
	private Xfermode mSavedXfermode;
	private final boolean mCircle;
	private final DrawFilter mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	private final Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

	public RoundClipper(boolean circle) {
		mCircle = circle;
	}

	public void beginClip(Canvas canvas, RectF bounds, Paint paint, float radius) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mSavedCount = canvas.saveLayer(bounds.left, bounds.top, bounds.right, bounds.bottom, null);
		}
		mSavedFlags = paint.getFlags();
		mSavedFilter = canvas.getDrawFilter();
		mSavedXfermode = paint.getXfermode();
		canvas.setDrawFilter(mDrawFilter);

//		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.clipRect(bounds);
		if (mCircle)
			canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius, paint);
		else
			canvas.drawRoundRect(bounds, radius, radius, paint);
		canvas.restore();

		paint.setAntiAlias(true);
		paint.setXfermode(mXfermode);
	}

	public void endClip(Canvas canvas, Paint paint) {
		paint.setFlags(mSavedFlags);
		paint.setXfermode(mSavedXfermode);
		canvas.setDrawFilter(mSavedFilter);
		canvas.restoreToCount(mSavedCount);
	}
}
