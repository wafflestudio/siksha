package com.wafflestudio.siksha.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.wafflestudio.siksha.R;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Gyu Kang on 2015-10-23.
 */
public class Animations {
    public static ValueAnimator makeSlideAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
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

    public static Object makeCircularRevealAnimator(final View view, boolean isReverse) {
        int x = (view.getLeft() + view.getRight());
        int y = view.getTop();
        int radius = Math.max(view.getWidth(), view.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator supportAnimator = ViewAnimationUtils.createCircularReveal(view, x, y, 0, radius);
            supportAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            supportAnimator.setDuration(500);

            return isReverse ? supportAnimator.reverse() : supportAnimator;
        } else {
            Animator animator = isReverse ?
                    android.view.ViewAnimationUtils.createCircularReveal(view, x, y, radius, 0) :
                    android.view.ViewAnimationUtils.createCircularReveal(view, x, y, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(500);

            return animator;
        }
    }

    public static void reveal(final View view) {
        view.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator supportRevealAnimator = (SupportAnimator) makeCircularRevealAnimator(view, false);
            supportRevealAnimator.start();
        } else {
            Animator revealAnimator = (Animator) makeCircularRevealAnimator(view, false);
            revealAnimator.start();
        }
    }

    public static void expand(final View view, final boolean willRevealAfter) {
        view.setVisibility(View.INVISIBLE);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        ValueAnimator slideAnimator = makeSlideAnimator(view, 0, view.getMeasuredHeight());
        slideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!willRevealAfter)
                    view.setVisibility(View.VISIBLE);
                else
                    reveal(view);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        slideAnimator.start();
    }

    public static void conceal(final View view, final ValueAnimator nextAnimator) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator supportAnimator = (SupportAnimator) makeCircularRevealAnimator(view, true);
            supportAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationEnd() {
                    view.setVisibility(View.INVISIBLE);
                    nextAnimator.start();
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {
                }
            });
            supportAnimator.start();
        }
        else {
            Animator animator = (Animator) makeCircularRevealAnimator(view, true);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                    nextAnimator.start();
                }
            });
            animator.start();
        }
    }

    public static void conceal(final View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator supportAnimator = (SupportAnimator) makeCircularRevealAnimator(view, true);
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
            supportAnimator.start();
        }
        else {
            Animator animator = (Animator) makeCircularRevealAnimator(view, true);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });
            animator.start();
        }
    }

    public static void collapse(final View view, final boolean willConcealBefore) {
        final ValueAnimator slideAnimator = makeSlideAnimator(view, view.getHeight(), 0);
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

        if (!willConcealBefore) {
            slideAnimator.start();
            return;
        }

        conceal(view, slideAnimator);
    }

    public static void rotate(final View view, float startDegrees, float endDegrees) {
        RotateAnimation rotateAnimation = new RotateAnimation(startDegrees, endDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setDuration(750);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);
    }
}
