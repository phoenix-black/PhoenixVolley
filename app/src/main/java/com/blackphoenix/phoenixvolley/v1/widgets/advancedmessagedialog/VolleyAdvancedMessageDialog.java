package com.blackphoenix.phoenixvolley.v1.widgets.advancedmessagedialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackphoenix.phoenixvolley.R;


/**
 * Created by Praba on 08-02-2017.
 */

public abstract class VolleyAdvancedMessageDialog extends AlertDialog {

    private TextView textViewContent;
    VolleyAdvancedMessageDialogInterface progressDialogDataInterface;

    private String progressText = "Please Wait...";
    long WAIT_TIME = 180*1000;
    Context _context;

    int iconThemeResID = -1;

    public abstract void onInterfaceReady(VolleyAdvancedMessageDialogInterface dialogInterface);
    public abstract void onTimedOut();
    public abstract void onDismissed();

    public VolleyAdvancedMessageDialog(Context context, String text) {
        super(context, R.style.PxVolleyDialogTheme);
        this.progressText = text;
        this._context = context;
    }

    public VolleyAdvancedMessageDialog(Context context, int themeResId, String text) {
        super(context, themeResId);
        this.progressText = text;
        this._context = context;
    }

    public VolleyAdvancedMessageDialog(Context context, int themeResId) {
        super(context, R.style.PxVolleyDialogTheme);
        iconThemeResID = themeResId;
        this._context = context;
    }

    public VolleyAdvancedMessageDialog(Context context, int themeResId, String text, long timeout /* TIMEOUT in Milliseconds*/) {
        super(context, themeResId);
        this.progressText = text;
        this.WAIT_TIME = timeout;
        this._context = context;
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.volley_dialog_message);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        ImageView imageViewInner = (ImageView) findViewById(R.id.progress_smallInnerImage);
        textViewContent = (TextView)findViewById(R.id.dialogMessage_content);
        textViewContent.setText(progressText);

        progressDialogDataInterface = new VolleyAdvancedMessageDialogInterface() {
            @Override
            public void updateData(String newData) {
                textViewContent.setText(newData);
            }
        };


        //final ShapeDrawable drawable = ShapeDrawable.createFromResourceStream(_context.getResources(), R.drawable.v_shape_rounded_rect_view, wrapper.getTheme());


        final LinearLayout parentLayout = findViewById(R.id.dialog_message);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(iconThemeResID != -1) {
                final ContextThemeWrapper wrapper = new ContextThemeWrapper(_context, iconThemeResID);
                Drawable drawable = (Drawable) _context.getResources().getDrawable(R.drawable.v_shape_rounded_rect_view, wrapper.getTheme()); //(GradientDrawable) parentLayout.getBackground();
                parentLayout.setBackground(drawable);
            } else {
                final ContextThemeWrapper wrapper = new ContextThemeWrapper(_context, R.style.IconTheme);
                Drawable drawable = (Drawable) _context.getResources().getDrawable(R.drawable.v_shape_rounded_rect_view, wrapper.getTheme()); //(GradientDrawable) parentLayout.getBackground();
                parentLayout.setBackground(drawable);
            }

        }


        onInterfaceReady(progressDialogDataInterface);

        Button closeButton = findViewById(R.id.dialogMessage_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismissed();
                dismiss();
            }
        });

       /* RotateAnimation rotateAnimationInner = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimationInner.setRepeatCount(Animation.INFINITE);
        rotateAnimationInner.setInterpolator(new LinearInterpolator());
        rotateAnimationInner.setDuration(2000);


        RotateAnimation rotateAnimationOuter = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimationOuter.setRepeatCount(Animation.INFINITE);
        rotateAnimationOuter.setInterpolator(new LinearInterpolator());
        rotateAnimationOuter.setDuration(2000);

        imageViewInner.startAnimation(rotateAnimationInner);
        imageViewOuter.startAnimation(rotateAnimationOuter);*/


    }

    public VolleyAdvancedMessageDialogInterface getProgressDialogDataInterface(){
        return this.progressDialogDataInterface;
    }

    public boolean isInterfaceReady(){
        return progressDialogDataInterface!=null;
    }

    @Override
    public void show(){
        super.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isShowing()) {
                    Toast.makeText(_context, "Timed Out", Toast.LENGTH_SHORT).show();
                    onTimedOut();
                    dismiss();
                }
            }
        },WAIT_TIME);

    }

    public void setIconTheme(int themeID){
        this.iconThemeResID = themeID;
    }
/*
    public void setProgressDialogText(String message){
            if(message!=null){
                if(!message.matches("") || !message.equals("")) {
                    textViewContent.setText(message);
                    return;
                }
            }
            textViewContent.setText("Please Wait...");
    }*/
}
