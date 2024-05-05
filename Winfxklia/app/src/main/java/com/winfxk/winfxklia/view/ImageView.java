package com.winfxk.winfxklia.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.winfxk.winfxklia.R;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author: Winfx
 * date: 2024/5/3 21:23
 */
public class ImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final HandlerThread thread = new HandlerThread("MyImageView Thread");
    private static DisplayMetrics metrics = null;
    private volatile boolean isload = false;
    private static final Handler handler;
    private ImageViewListener listener;
    private static int densityDpi;

    static {
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    public ImageView(@NonNull @NotNull Context context) {
        this(context, null);
    }

    public ImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (metrics == null) {
            metrics = context.getResources().getDisplayMetrics();
            densityDpi = (int) (metrics.density * 160f);
        }
    }

    public void setImageURL(String url) {
        if (isload) return;
        isload = true;
        setImageResource(R.drawable.loading);
        handler.post(() -> this.run(url));
    }

    private void run(String string) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(string);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            int code = connection.getResponseCode();
            if (code == 200) {
                inputStream = connection.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                options.inSampleSize = calculateInSampleSize(options);
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                post(() -> {
                    if (listener != null) listener.onListener(true);
                    setImageBitmap(bitmap);
                });
            } else post(() -> {
                if (listener != null) listener.onListener(true);
                setImageResource(R.drawable.wifi_sb);
            });
        } catch (IOException e) {
            Log.w("ImageView", e);
            post(() -> {
                if (listener != null) listener.onListener(true);
                setImageResource(R.drawable.wifi_sb);
            });
        } finally {
            isload = false;
            if (connection != null) try {
                connection.disconnect();
            } catch (Exception e) {
                Log.w("ImageView", e);
            }
            if (inputStream != null) try {
                inputStream.close();
            } catch (Exception e) {
                Log.w("ImageView", e);
            }
        }
    }

    public ImageViewListener getListener() {
        return listener;
    }

    public void setListener(ImageViewListener listener) {
        this.listener = listener;
    }

    private int calculateInSampleSize(BitmapFactory.Options options) {
        int inSampleSize = 1;
        final int width = options.outWidth;
        final int height = options.outHeight;
        while ((width / inSampleSize) >= densityDpi && (height / inSampleSize) >= densityDpi)
            inSampleSize *= 2;
        return inSampleSize;
    }

    public interface ImageViewListener {
        void onListener(boolean isSucceed);
    }
}
