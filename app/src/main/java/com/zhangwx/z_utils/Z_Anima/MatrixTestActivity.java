package com.zhangwx.z_utils.Z_Anima;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.zhangwx.z_utils.R;

public class MatrixTestActivity extends AppCompatActivity implements OnTouchListener {
    public static final String TAG = "MatrixTestActivity";
    private TransformMatrixView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        view = new TransformMatrixView(this);
        view.setScaleType(ImageView.ScaleType.MATRIX);
        view.setOnTouchListener(this);

        setContentView(view);
    }

    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            Matrix matrix = new Matrix();
            // 输出图像的宽度和高度(126 x 126)
            Log.e(TAG, "image size: width x height = " + view.getImageBitmap().getWidth() + " x " + view.getImageBitmap().getHeight());
            // 1. 平移
//            translate(matrix, view);
            // 2. 旋转(围绕图像的中心点)
//            rotateOne(matrix, view);
            // 3. 旋转(围绕坐标原点) + 平移(效果同2)
//            rotateTwo(matrix, view);
            // 4. 缩放
//            scale(matrix, view);
            // 5. 错切 - 水平
            skewHorizontal(matrix, view);
            // 6. 错切 - 垂直
//            skewVertical(matrix, view);
            // 7. 错切 - 水平 + 垂直
//            skewBoth(matrix, view);
            // 8. 对称 - 水平
//            flipHorizontal(matrix, view);
            // 9. 对称 - 垂直
//            flipVertical(matrix, view);
            // 10. 对称(对称轴为直线y = x)
//            flip_y_x(matrix, view);
            view.invalidate();
        }
        return true;
    }

    private void translate(Matrix matrix, TransformMatrixView view) {
        // 1. 平移
        matrix.postTranslate(view.getImageBitmap().getWidth(), view.getImageBitmap().getHeight());
        // 在x方向平移view.getImageBitmap().getWidth()，在y轴方向view.getImageBitmap().getHeight()
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void rotateOne(Matrix matrix, TransformMatrixView view) {
        // 2. 旋转(围绕图像的中心点)
        matrix.setRotate(45f, view.getImageBitmap().getWidth() / 2f, view.getImageBitmap().getHeight() / 2f);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(view.getImageBitmap().getWidth() * 1.5f, 0f);
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void rotateTwo(Matrix matrix, TransformMatrixView view) {
        // 3. 旋转(围绕坐标原点) + 平移(效果同2)
        matrix.setRotate(45f);
        matrix.preTranslate(-1f * view.getImageBitmap().getWidth() / 2f, -1f * view.getImageBitmap().getHeight() / 2f);
        matrix.postTranslate((float) view.getImageBitmap().getWidth() / 2f, (float) view.getImageBitmap().getHeight() / 2f);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(view.getImageBitmap().getWidth() * 1.5f, 0f);
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void scale(Matrix matrix, TransformMatrixView view) {
        // 4. 缩放
        matrix.setScale(2f, 2f);
        printMatrix(matrix);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(view.getImageBitmap().getWidth(), view.getImageBitmap().getHeight());
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void skewHorizontal(Matrix matrix, TransformMatrixView view) {
        // 5. 错切 - 水平
        matrix.setSkew(0.5f, 0f);
        printMatrix(matrix);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(view.getImageBitmap().getWidth(), 0f);
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void skewVertical(Matrix matrix, TransformMatrixView view) {
        // 6. 错切 - 垂直
        matrix.setSkew(0f, 0.5f);
        printMatrix(matrix);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(0f, view.getImageBitmap().getHeight());
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void skewBoth(Matrix matrix, TransformMatrixView view) {
        // 7. 错切 - 水平 + 垂直
        matrix.setSkew(0.5f, 0.5f);
        printMatrix(matrix);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(view.getImageBitmap().getWidth(), view.getImageBitmap().getHeight());
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void flipHorizontal(Matrix matrix, TransformMatrixView view) {
        // 8. 对称 (水平对称)
        float matrix_values[] = {1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
        matrix.setValues(matrix_values);
        printMatrix(matrix);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(0f, view.getImageBitmap().getHeight() * 2f);
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void flipVertical(Matrix matrix, TransformMatrixView view) {
        // 9. 对称 - 垂直
        float matrix_values[] = {-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
        matrix.setValues(matrix_values);
        printMatrix(matrix);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(view.getImageBitmap().getHeight() * 2f, 0f);
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void flip_y_x(Matrix matrix, TransformMatrixView view) {
        // 10. 对称(对称轴为直线y = x)
        float matrix_values[] = {0f, -1f, 0f, -1f, 0f, 0f, 0f, 0f, 1f};
        matrix.setValues(matrix_values);
        printMatrix(matrix);
        // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
        matrix.postTranslate(
                view.getImageBitmap().getHeight() + view.getImageBitmap().getWidth(),
                view.getImageBitmap().getHeight() + view.getImageBitmap().getWidth());
        view.setImageMatrix(matrix);
        printMatrix(matrix);
    }

    private void printMatrix(Matrix matrix) {
        float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);
        for (int i = 0; i < 3; ++i) {
            String temp = new String();
            for (int j = 0; j < 3; ++j) {
                temp += matrixValues[3 * i + j] + "\t";
            }
            Log.e(TAG, temp);
        }
    }

    static class TransformMatrixView extends AppCompatImageView {
        private Bitmap bitmap;
        private Matrix matrix;

        public TransformMatrixView(Context context) {
            super(context);
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            matrix = new Matrix();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // 画出原图像
            canvas.drawBitmap(bitmap, 0, 0, null);
            // 画出变换后的图像
            canvas.drawBitmap(bitmap, matrix, null);
            super.onDraw(canvas);
        }

        @Override
        public void setImageMatrix(Matrix matrix) {
            this.matrix.set(matrix);
            super.setImageMatrix(matrix);
        }

        public Bitmap getImageBitmap() {
            return bitmap;
        }
    }
}
