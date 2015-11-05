package com.wafflestudio.siksha.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Gyu Kang on 2015-10-23.
 */
public class Animations {
    private static ValueAnimator makeSlideAnimator(final View view, int start, int end, int duration) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private static Object makeCircularRevealAnimator(final View view, int duration, boolean reverse) {
        int x = view.getLeft() + view.getRight();
        int y = view.getTop();
        int radius = Math.max(view.getWidth(), view.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator supportAnimator = ViewAnimationUtils.createCircularReveal(view, x, y, 0, radius);
            supportAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            supportAnimator.setDuration(duration);

            return reverse ? supportAnimator.reverse() : supportAnimator;
        } else {
            Animator animator = reverse ?
                    android.view.ViewAnimationUtils.createCircularReveal(view, x, y, radius, 0) :
                    android.view.ViewAnimationUtils.createCircularReveal(view, x, y, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(duration);

            return animator;
        }
    }

    public static Object makeRevealAnimator(final View view, int duration) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator supportRevealAnimator = (SupportAnimator) makeCircularRevealAnimator(view, duration, false);
            supportRevealAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationEnd() {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {
                }
            });
            return supportRevealAnimator;
        } else {
            Animator revealAnimator = (Animator) makeCircularRevealAnimator(view, duration, false);
            revealAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            return revealAnimator;
        }
    }

    public static ValueAnimator makeExpandAnimator(final View view, int duration) {
        view.setVisibility(View.INVISIBLE);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        ValueAnimator slideAnimator = makeSlideAnimator(view, 0, view.getMeasuredHeight(), duration);
        slideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        return slideAnimator;
    }

    public static Object makeConcealAnimator(final View view, int duration) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator supportAnimator = (SupportAnimator) makeCircularRevealAnimator(view, duration, true);
            supportAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationEnd() {
                    view.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {
                }
            });
            return supportAnimator;
        }
        else {
            Animator animator = (Animator) makeCircularRevealAnimator(view, duration, true);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });
            return animator;
        }
    }

    public static ValueAnimator makeCollapseAnimator(final View view, int duration) {
        view.setVisibility(View.VISIBLE);

        final ValueAnimator slideAnimator = makeSlideAnimator(view, view.getHeight(), 0, duration);
        slideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        return slideAnimator;
    }

    public static ObjectAnimator makeRotateAnimator(final View view, float startDegrees, float endDegrees, int duration, boolean infinite) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, startDegrees, endDegrees);
        rotateAnimator.setDuration(duration);

        if (infinite)
            rotateAnimator.setRepeatCount(Animation.INFINITE);

        return rotateAnimator;
    }

    public static ObjectAnimator makeFadeAnimator(final View view, float startAlpha, float endAlpha, int duration) {
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, startAlpha, endAlpha);
        fadeAnimator.setDuration(duration);

        return fadeAnimator;
    }

    public static void clear(final View view) {
        view.clearAnimation();
    }
}
