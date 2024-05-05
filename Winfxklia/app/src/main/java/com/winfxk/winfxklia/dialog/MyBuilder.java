package com.winfxk.winfxklia.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import com.winfxk.winfxklia.R;
import com.winfxk.winfxklia.Tool;
import com.winfxk.winfxklia.view.ImageView;
import com.winfxk.winfxklia.view.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Winfx
 * date: 2024/5/3 22:12
 */
public class MyBuilder extends Dialog {
    public static int[] Animations = {R.style.alert_dialog1, R.style.alert_dialog2};
    private final List<View.OnClickListener> allListeners = new ArrayList<>();
    private final List<Button> buttons = new ArrayList<>();
    private final RelativeLayout view;
    private final LinearLayout line2;
    private boolean isShow = false;
    private final TextView message;
    private final Context context;
    private final ImageView icon;
    private final TextView title;

    public MyBuilder(@NonNull Context context, IconState state) {
        this(context, state, context.getString(R.string.info), "");
    }

    public MyBuilder(@NonNull Context context) {
        this(context, context.getString(R.string.info), "");
    }

    public MyBuilder(@NonNull Context context, String title, String message) {
        this(context, R.drawable.info, title, message);
    }

    public MyBuilder(@NonNull Context context, IconState state, String title, String message) {
        this(context, state, title, message, false);
    }

    public MyBuilder(@NonNull Context context, IconState state, String title, String message, boolean Cancelable) {
        this(context, state.getIcon(), title, message, Cancelable);
    }

    public MyBuilder(@NonNull Context context, int IconID, String title, String message) {
        this(context, IconID, title, message, false);
    }

    public MyBuilder(@NonNull Context context, int IconID, String title, String message, boolean Cancelable) {
        super(context, Animations[Tool.getRand(0, Animations.length - 1)]);
        this.context = context;
        setContentView(R.layout.alert_dialog);
        this.icon = findViewById(R.id.imageView1);
        this.title = findViewById(R.id.textView1);
        this.view = findViewById(R.id.view);
        this.message = findViewById(R.id.textView3);
        this.line2 = findViewById(R.id.line2);
        this.icon.setImageResource(IconID);
        this.title.setText(title);
        this.message.setText(message);
        setCancelable(Cancelable);
        getWindow().setWindowAnimations(Animations[Tool.getRand(0, Animations.length - 1)]);
    }


    @Override
    public void show() {
        if (isShow) throw new IllegalStateException("Dialog show already");
        if (buttons.isEmpty()) buttons.add(getButton(context.getString(R.string.confirm)));
        for (Button button : buttons) line2.addView(button);
        super.show();
        isShow = true;
    }

    public MyBuilder removeAllClickListener(View.OnClickListener listener) {
        if (listener == null) return this;
        allListeners.remove(listener);
        return this;
    }

    public MyBuilder clearAllClickListener() {
        allListeners.clear();
        return this;
    }

    public MyBuilder addAllClickListener(View.OnClickListener listener) {
        if (listener == null) return this;
        allListeners.add(listener);
        return this;
    }

    public MyBuilder setView(View view) {
        this.view.removeAllViews();
        this.view.addView(view);
        return this;
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public Drawable getIcon() {
        return icon.getDrawable();
    }

    public String getMessage() {
        return message.getText().toString();
    }

    public Button getButton(int index) {
        if (index >= buttons.size()) return null;
        return buttons.get(index);
    }

    public List<Button> getButtons() {
        return new ArrayList<>(buttons);
    }

    public MyBuilder setIcon(Bitmap icon) {
        this.icon.setImageBitmap(icon);
        return this;
    }

    public MyBuilder setIcon(IconState state) {
        return setIcon(state.getIcon());
    }

    public MyBuilder setIcon(int icon) {
        this.icon.setImageResource(icon);
        return this;
    }

    public MyBuilder setIcon(String url) {
        this.icon.setImageURL(url);
        return this;
    }

    public MyBuilder setIcon(File file) throws IOException {
        return setIcon(BitmapFactory.decodeStream(Files.newInputStream(file.toPath())));
    }

    public MyBuilder setMessage(int messageId) {
        this.message.setText(context.getString(messageId));
        return this;
    }

    public MyBuilder setMessage(String message) {
        this.message.setText(message);
        return this;
    }

    public MyBuilder setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    @Override
    public void setTitle(int titleId) {
        title.setText(context.getString(titleId));
    }

    public MyBuilder addButton(String text) {
        return addButton(text, null);
    }

    public MyBuilder addButton(String text, View.OnClickListener listener) {
        Button button = getButton(text);
        button.setOnClickListener(v -> onClick(v, listener));
        if (!isShow) buttons.add(button);
        else line2.addView(button);
        return this;
    }

    private void onClick(View v, View.OnClickListener listener) {
        if (listener != null) listener.onClick(v);
        for (View.OnClickListener listener1 : allListeners)
            if (listener1 != null) listener1.onClick(v);
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

    public enum IconState {
        INFO(R.drawable.info),
        WARNING(R.drawable.warn),
        ERROR(R.drawable.error),
        SUCCESS(R.drawable.succeed);
        private final int icon;

        IconState(int icon) {
            this.icon = icon;
        }

        public int getIcon() {
            return icon;
        }
    }
}
