package com.dawnlight.slidedialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by jose on 2019/01/11.
 */

public class SlideDialog extends FrameLayout {

    public static final int FADE_OUT_DISABLED = -1;
    private static final int FADE_TIME = 305;
    private static final int FADE_OUT_DEFAULT = 1_000;

    private final WeakReference<Activity> activityRef;
    private View quickDialogView;
    private View imageView;
    private String message;
    private int color;
    private int fadeOutTime = FADE_OUT_DEFAULT;
    private int dialogHeight;
    private boolean visible;
    private boolean animated;

    private SlideDialog(Context context) {

        super(context);
        init(context);
        this.activityRef = new WeakReference<>((Activity) context);
    }

    public static SlideDialog newInstance(Activity activity) {

        if (!SlideDialogUtil.validActivity(activity)) {
            return null;
        }

        return new SlideDialog(activity);
    }

    public SlideDialog message(String message) {

        this.message = message;
        return this;
    }

    public SlideDialog color(int color) {

        this.color = color;
        return this;
    }

    public SlideDialog animated(boolean animated) {

        this.animated = animated;
        return this;
    }

    public SlideDialog fadeOutTime(int fadeOutTime) {

        this.fadeOutTime = fadeOutTime;
        return this;
    }

    public void show() {

        if (activityRef != null && SlideDialogUtil.validActivity(activityRef.get())) {
            Activity activity = activityRef.get();

            if (message != null) {
                ((TextView) quickDialogView.findViewById(R.id.view_dialog_message)).setText(message);
            }

            if (color != 0) {
                quickDialogView.setBackgroundColor(activity.getResources().getColor(color));
            }

            visible = true;

            activity.runOnUiThread(() -> {
                if (SlideDialogUtil.validActivity(activity)) {
                    ((ViewGroup) activity.getWindow().getDecorView()).addView(this);

                }
            });
        }
    }

    public boolean isVisible() {
        return visible;
    }


    private void animateShow() {

        int statusBarHeight = getResources().getDimensionPixelSize(R.dimen.status_bar_height);

        //  1. Waits for rendering, measures size and makes invisible
        quickDialogView.animate().alpha(0).setDuration(1)
                .withEndAction(() -> {

                    //  Positions outside the window and makes visible

                    dialogHeight = quickDialogView.getHeight();
                    quickDialogView.setAlpha(1f);
                    quickDialogView.setY(-dialogHeight);

                    //  2. Animates entering window
                    quickDialogView.animate().y(statusBarHeight).setDuration(FADE_TIME)
                            .withEndAction(() -> {

                                //  3. Animates icon
                                if (animated) {
                                    imageView.animate().setDuration(600).rotationBy(360).setInterpolator(new BounceInterpolator()).start();
                                }

                                if (fadeOutTime >= 0) {
                                    //  4. Waits in screen
                                    quickDialogView.postOnAnimationDelayed(() -> {

                                        //  5. Animates exiting window
                                        fadeOutAnimation();
                                    }, fadeOutTime);
                                }
                            }).start();
                })
                .start();
    }

    private void remove() {

        ViewManager parent = (ViewManager) this.getParent();
        if (parent != null) {
            parent.removeView(this);
            visible = false;
        }
    }

    private void fadeOutAnimation() {

        //  5. Animates exiting window
        quickDialogView.animate().y(-dialogHeight).setDuration(FADE_TIME)
                .withEndAction(() -> {

                    //  6. Remove view
                    remove();
                })
                .start();
    }


    private void init(Context context) {

        View dialogView = inflate(context, R.layout.slide_dialog, this);

        quickDialogView = dialogView.findViewById(R.id.root_view_dialog);
        imageView = dialogView.findViewById(R.id.view_dialog_icon);
        quickDialogView.setAlpha(0);

        quickDialogView.setOnClickListener((View view) -> {
            fadeOutAnimation();
        });

        animateShow();
    }
}

