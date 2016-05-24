package com.ckt.io.wifidirect.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

@SuppressLint("NewApi")
public class BitmapUtils {

	public static float dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return  (dpValue * scale + 0.5f);
	}
	public static Bitmap getCircleBitmap(Context context, Bitmap src, float radius) {
		radius = dipTopx(context, radius);
		int w = src.getWidth();
		int h = src.getHeight();
		int canvasW = Math.round(radius * 2);
		Bitmap bitmap = Bitmap.createBitmap(canvasW, canvasW,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		Path path = new Path();
		path.addCircle(radius, radius, radius, Path.Direction.CW);
		canvas.clipPath(path);

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		Rect srcRect = new Rect(0, 0, w, h);
		Rect dstRect = new Rect(0, 0, canvasW, canvasW);

		canvas.drawBitmap(src, srcRect, dstRect, paint);

		return bitmap;
	}

	public static Bitmap blur(Context context, Bitmap bkg) {
		long startMs = System.currentTimeMillis();
		float radius = 20;

		bkg = small(bkg);
		Bitmap bitmap = bkg.copy(bkg.getConfig(), true);

		final RenderScript rs = RenderScript.create(context);
		final Allocation input = Allocation.createFromBitmap(rs, bkg,
				Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
				Element.U8_4(rs));
		script.setRadius(radius);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(bitmap);

		bitmap = big(bitmap);
		
		return bitmap;
		
		/*setBackground(new BitmapDrawable(context.getResources(), bitmap));
		rs.destroy();
		Log.d("zhangle", "blur take away:"
				+ (System.currentTimeMillis() - startMs) + "ms");*/
	}

	public static Bitmap blur(Context context, Drawable drawable) {
		return blur(context, drawable2Bitmap(drawable));
	}

	//drawable转bitmap
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	private static Bitmap big(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(4f, 4f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	private static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.25f, 0.25f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}
}
