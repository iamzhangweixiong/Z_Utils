package com.zhangwx.z_utils.Z_Widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 这是一个可以播放Gif图片的ImageView,其中有四个方法：
 * setGitFilePath()、setGifImageResource()、setGifImageBitmap()、setGifBytes()可供调用。
 * 一般情况下，在设置完gif图片后，需要调用startAnimation()来启动Gif动画，然后调用stopAnimation()可以终止动画
 * (=====请在GifImageView不显示的时候,或者Activity onPause的情况下及时终止动画，以节省资源======)
 */
public class GifImageView extends ImageView implements Runnable {
    private static final String TAG = "GifDecoderView";
    private GifDecoder gifDecoder;
    private Bitmap tmpBitmap;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean animating;
    private boolean shouldClear;
    private Thread animationThread;

    private int gifTimes = 0;//动画播放次数，-1代表无限循环
    private int sleepSeconds = 0;

    private final Runnable updateToFirstFrame = new Runnable() {
        @Override
        public void run() {
            if (gifDecoder != null && gifDecoder.getFrame(0) != null) {
                setImageBitmap(gifDecoder.getFrame(0));
            }
        }
    };

    private final Runnable updateResults = new Runnable() {
        @Override
        public void run() {
            if (animating && tmpBitmap != null && !tmpBitmap.isRecycled()) {
                setImageBitmap(tmpBitmap);
            }
        }
    };

    private final Runnable cleanupRunnable = new Runnable() {
        @Override
        public void run() {
            tmpBitmap = null;
            gifDecoder = null;
            animationThread = null;
            shouldClear = false;
        }
    };

    public GifImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public GifImageView(final Context context) {
        super(context);
    }

    public boolean setGifFirstFrame(String path) {
        if (gifDecoder == null) {
            try {
                gifDecoder = new GifDecoder();
                gifDecoder.read(new FileInputStream(new File(path)));
            } catch (final OutOfMemoryError e) {
                gifDecoder = null;
                Log.e(TAG, e.getMessage(), e);
            } catch (FileNotFoundException e) {
                gifDecoder = null;
                Log.e(TAG, e.getMessage(), e);
            }
        }
        if (gifDecoder != null) {
            Bitmap bitmap = gifDecoder.getFrame(0);
            if (bitmap != null) {
                setImageBitmap(bitmap);
                return true;
            }
        }

        return false;
    }


    public void setGitFilePath(String path) throws Exception {
        if (!TextUtils.isEmpty(path)) {
            setInputStream(new FileInputStream(new File(path)));
        } else {
        }
    }

    public void setGifImageResource(int resId) throws Exception {
        setInputStream(getResources().openRawResource(resId));
    }


    private void setInputStream(InputStream is) {
        gifDecoder = new GifDecoder();
        try {
            gifDecoder.read(is);
        } catch (final OutOfMemoryError e) {
            gifDecoder = null;
            Log.e(TAG, e.getMessage(), e);
            return;
        }

        if (canStart()) {
            animationThread = new Thread(this);
            animationThread.start();
        }
    }


    /**
     * 播放times次 暂停seconds秒
     *
     * @param times
     * @param seconds
     */
    public void startAnimation(int times, int seconds) {
        gifTimes = times;
        sleepSeconds = seconds;

        animating = true;

        if (canStart()) {
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    public void startAnimation() {
        animating = true;

        if (canStart()) {
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    public boolean isAnimating() {
        return animating;
    }

    public void stopAnimation() {
        animating = false;

        if (animationThread != null) {
            animationThread.interrupt();
            animationThread = null;
        }
    }

    public void clear() {
        animating = false;
        shouldClear = true;
        stopAnimation();
        handler.post(cleanupRunnable);
    }

    private boolean canStart() {
        return animating && gifDecoder != null && animationThread == null;
    }


    @Override
    public void run() {
        if (shouldClear) {
            handler.post(cleanupRunnable);
            return;
        }
        if (gifDecoder == null) {
            return;
        }
        int count = 0;
        final int n = gifDecoder.getFrameCount();
        if (n < 1) {
            return;
        }
        do {
            if (!waitAnim) {
                continue;
            }
            count++;
            for (int i = 0; i < n; i++) {
                if (!animating || gifDecoder == null) {
                    break;
                }
                tmpBitmap = gifDecoder.getFrame(i);
                int delay = gifDecoder.getDelay(i);
                handler.post(updateResults);
                try {
                    Thread.sleep(delay > 0 ? delay : 300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (count >= gifTimes && sleepSeconds != 0) {
                waitAnim = false;
                handler.post(updateToFirstFrame);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitAnim = true;
                    }
                }, sleepSeconds * 1000);
                count = 0;
            }
        } while (animating);
    }

    private boolean waitAnim = true;
}