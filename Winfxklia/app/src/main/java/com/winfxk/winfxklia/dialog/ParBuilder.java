package com.winfxk.winfxklia.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import com.winfxk.winfxklia.R;
import com.winfxk.winfxklia.view.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Winfx
 * date: 2024/5/5 16:33
 */
public class ParBuilder extends Dialog implements ImageView.ImageViewListener {
    private final static List<View.OnClickListener> allListeners = new ArrayList<>();
    private final static Handler handler = new Handler(Looper.getMainLooper());
    private final List<Button> buttons = new ArrayList<>();
    private final ProgressBar progressBar;
    private final Animation iconAnim;
    private final LinearLayout line1;
    private final TextView message;
    private boolean isShow = false;
    private final Context context;
    private final ImageView icon;
    private final TextView title;
    private Type type;

    public ParBuilder(@NonNull Context context) {
        this(context, Type.Info);
    }

    public ParBuilder(@NonNull Context context, Type type) {
        this(context, "提示", "", type);
    }

    public ParBuilder(@NonNull Context context, String title, String message) {
        this(context, title, message, Type.Info);
    }

    public ParBuilder(@NonNull Context context, String title, String message, Type type) {
        super(context, R.style.progress_dialog);
        setContentView(R.layout.progress_dialog);
        if (type.equals(Type.Image)) throw new IllegalArgumentException("Type cannot be Image");
        iconAnim = AnimationUtils.loadAnimation(context, R.anim.progress_dialog_icon);
        getWindow().setWindowAnimations(R.style.progress_dialog);
        this.progressBar = findViewById(R.id.progressBar);
        this.message = findViewById(R.id.textView3);
        this.title = findViewById(R.id.textView1);
        icon = findViewById(R.id.imageView1);
        line1 = findViewById(R.id.line1);
        this.message.setText(message);
        this.title.setText(title);
        this.context = context;
        setCancelable(false);
        this.type = type;
    }

    public ParBuilder addButton(String text) {
        return addButton(text, null);
    }

    public ParBuilder addButton(String text, View.OnClickListener listener) {
        Button button = getButton(text);
        button.setOnClickListener(v -> onClick(v, listener));
        if (!isShow) buttons.add(button);
        else line1.addView(button);
        return this;
    }

    public void show() {
        if (isShow) throw new IllegalStateException("Dialog show already");
        handler.post(() -> {
            for (Button button : buttons) line1.addView(button);
            setType(type);
            super.show();
            isShow = true;
        });
    }

    public ParBuilder setMessage(String message) {
        handler.post(() -> this.message.setText(message));
        return this;
    }

    public ParBuilder setTitle(String title) {
        handler.post(() -> this.title.setText(title));
        return this;
    }

    public ParBuilder setIcon(Type type) {
        if (type.equals(Type.Image)) throw new IllegalArgumentException("Type cannot be Image");
        int id;
        switch (type) {
            case Info:
                id = R.drawable.info;
                break;
            case Warn:
                id = R.drawable.warn;
                break;
            case Error:
                id = R.drawable.error;
                break;
            case Succeed:
                id = R.drawable.succeed;
                break;
            default:
                id = R.drawable.info;
                break;
        }
        handler.post(() -> {
            if (type.equals(Type.Empty)) {
                progressBar.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
            } else if (type.equals(Type.Progress)) {
                icon.setVisibility(View.GONE);
                progressBar.startAnimation(iconAnim);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                icon.startAnimation(iconAnim);
                this.icon.setVisibility(View.VISIBLE);
                this.icon.setImageResource(id);
            }
        });
        return this;
    }

    public ParBuilder setIcon(String url) {
        handler.post(() -> {
            if (type.equals(Type.Empty) || type.equals(Type.Progress)) {
                progressBar.setVisibility(View.GONE);
                icon.setVisibility(View.VISIBLE);
            }
            this.type = Type.Image;
            icon.setImageURL(url);
        });
        return this;
    }

    public ParBuilder setIcon(File file) throws IOException {
        return setIcon(Files.newInputStream(file.toPath()));
    }

    public ParBuilder setIcon(InputStream is) {
        return setIcon(BitmapFactory.decodeStream(is));
    }

    public ParBuilder setIcon(Bitmap icon) {
        this.type = Type.Image;
        handler.post(() -> {
            if (type.equals(Type.Empty) || type.equals(Type.Progress)) {
                this.progressBar.setVisibility(View.GONE);
                this.icon.setVisibility(View.VISIBLE);
            }
            this.icon.startAnimation(iconAnim);
            this.icon.setImageBitmap(icon);
        });
        return this;
    }

    public ParBuilder setType(Type type) {
        if (type.equals(Type.Image)) throw new IllegalArgumentException("Type cannot be Image");
        return setIcon(type);
    }

    public ParBuilder removeAllClickListener(View.OnClickListener listener) {
        if (listener == null) return this;
        allListeners.remove(listener);
        return this;
    }

    public ParBuilder clearAllClickListener() {
        allListeners.clear();
        return this;
    }

    public ParBuilder addAllClickListener(View.OnClickListener listener) {
        if (listener == null) return this;
        allListeners.add(listener);
        return this;
    }

    private void onClick(View view, View.OnClickListener listener) {
        if (listener != null) listener.onClick(view);
        for (View.OnClickListener listener1 : allListeners)
            if (listener1 != null) listener1.onClick(view);
        this.dismiss();
    }

    private Button getButton(String text) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        layoutParams.setMargins(0, 5, 0, 0);
        Button button = new Button(context, null);
        button.setText(text);
        button.setTextColor(Color.BLACK);
        button.setBackground(AppCompatResources.getDrawable(context, R.drawable.dialog_button));
        button.setLayoutParams(layoutParams);
        button.setPadding(0, 0, 0, 0);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.buttonBarButtonStyle, outValue, true);
        button.getContext().setTheme(outValue.resourceId);
        return button;
    }

    public String getMessage() {
        return message.getText().toString();
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public Type getType() {
        return type;
    }

    @Override
    public void onListener(boolean isSucceed) {
        if (!isSucceed) icon.setVisibility(View.GONE);
        else icon.startAnimation(iconAnim);
    }

    public static enum Type {
        Warn, Error, Info, Progress, Image, Succeed, Empty;
    }
}
