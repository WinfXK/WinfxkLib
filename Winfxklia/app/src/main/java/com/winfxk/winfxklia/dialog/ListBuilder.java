package com.winfxk.winfxklia.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import com.winfxk.winfxklia.R;
import com.winfxk.winfxklia.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Winfx
 * date: 2024/5/4 8:58
 */
public class ListBuilder extends Dialog {
    private final static List<View.OnClickListener> allListeners = new ArrayList<>();
    private final static List<Button> buttons = new ArrayList<>();
    private View.OnClickListener cancelListener;
    private final LinearLayout view;
    private boolean isShow = false;
    private final Context context;
    private final Button cancel;

    public ListBuilder(@NonNull Context context) {
        this(context, false);
    }

    public ListBuilder(@NonNull Context context, boolean Cancelable) {
        this(context, Cancelable, (Item) null);
    }

    public ListBuilder(@NonNull Context context, boolean Cancelable, Item... items) {
        super(context, R.style.listDialog);
        this.context = context;
        setContentView(R.layout.list_dialog);
        setCancelable(Cancelable);
        cancel = findViewById(R.id.button1);
        view = findViewById(R.id.line1);
        cancel.setOnClickListener(this::onCancelClick);
        if (items != null) for (Item item : items)
            if (item != null) addButton(item.getText(), item.getListener());
        getWindow().setWindowAnimations(R.style.listDialog);
    }

    public Button getButton(int index) {
        if (index >= buttons.size()) return null;
        return buttons.get(index);
    }

    @Override
    public void show() {
        if (isShow) throw new IllegalStateException("Dialog show already");
        view.removeAllViews();
        for (Button button : buttons) view.addView(button);
        super.show();
        isShow = true;
    }

    public ListBuilder removeAllClickListener(View.OnClickListener listener) {
        if (listener == null) return this;
        allListeners.remove(listener);
        return this;
    }

    public ListBuilder clearAllClickListener() {
        allListeners.clear();
        return this;
    }

    public ListBuilder addAllClickListener(View.OnClickListener listener) {
        if (listener == null) return this;
        allListeners.add(listener);
        return this;
    }

    public List<Button> getButtons() {
        return new ArrayList<>(buttons);
    }

    private void onCancelClick(View v) {
        if (cancelListener != null) cancelListener.onClick(v);
        dismiss();
    }

    public ListBuilder setCancel(String cancel) {
        this.cancel.setText(cancel);
        return this;
    }

    public ListBuilder setCancel(String cancel, View.OnClickListener listener) {
        this.cancel.setText(cancel);
        this.cancelListener = listener;
        return this;
    }

    public ListBuilder addButton(String text) {
        return addButton(text, null);
    }

    public ListBuilder addButton(String text, View.OnClickListener listener) {
        Button button = getButton(text);
        button.setOnClickListener(v -> onClick(v, listener));
        if (!isShow) buttons.add(button);
        else view.addView(button);
        return this;
    }

    private void onClick(View view, View.OnClickListener listener) {
        if (listener != null) listener.onClick(view);
        for (View.OnClickListener listener1 : allListeners)
            if (listener1 != null) listener1.onClick(view);
        this.dismiss();
    }

    private Button getButton(String text) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 1, 0, 1);
        Button button = new Button(context, null);
        button.setText(text);
        button.setTextColor(Color.BLUE);
        button.setTextSize(17);
        button.setTypeface(button.getTypeface(), Typeface.BOLD);
        button.setBackground(AppCompatResources.getDrawable(context, R.drawable.listdialog_button));
        button.setLayoutParams(layoutParams);
        button.setHeight(40);
        button.setPadding(0, 0, 0, 0);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.buttonBarButtonStyle, outValue, true);
        button.getContext().setTheme(outValue.resourceId);
        return button;
    }

    public static class Item {
        private final String text;
        private final View.OnClickListener listener;

        public Item(String text, View.OnClickListener listener) {
            this.text = text;
            this.listener = listener;
        }

        public View.OnClickListener getListener() {
            return listener;
        }

        public String getText() {
            return text;
        }
    }
}
