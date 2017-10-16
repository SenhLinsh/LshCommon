package com.linsh.lshapp.common.view;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.linsh.lshapp.common.R;
import com.linsh.lshutils.utils.Basic.LshLogUtils;

public class LoadingView extends FrameLayout {

    private static final int ANIMATION_DURATION = 500;
    private static float mDistance = 200;

    private ShapeLoadingView mShapeLoadingView;
    private ImageView mIndicationIm;
    private TextView mLoadTextView;

    public float factor = 1.2f;
    private int mTextAppearance;
    private String mLoadText;
    private AnimatorSet mUpAnimatorSet;
    private AnimatorSet mDownAnimatorSet;

    private boolean isAnimating;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initAttr(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mLoadText = typedArray.getString(R.styleable.LoadingView_loadingText);
        mTextAppearance = typedArray.getResourceId(R.styleable.LoadingView_loadingTextAppearance, -1);
        typedArray.recycle();
    }

    private void init() {
        mDistance = dip2px(54f);

//        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_view, this);
        View.inflate(getContext(), R.layout.layout_loading_view, this);
        mShapeLoadingView = (ShapeLoadingView) findViewById(R.id.shapeLoadingView);
        mIndicationIm = (ImageView) findViewById(R.id.indication);
        mLoadTextView = (TextView) findViewById(R.id.promptTV);

        if (mTextAppearance != -1) {
            mLoadTextView.setTextAppearance(getContext(), mTextAppearance);
        }
        setLoadingText(mLoadText);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        startLoading(200);
    }

    private Runnable mFreeFallRunnable = new Runnable() {
        @Override
        public void run() {
            isAnimating = true;
            freeFall();
        }
    };

    public void startLoading(long delay) {
        if (mDownAnimatorSet != null && mDownAnimatorSet.isRunning()) {
            return;
        }
        this.removeCallbacks(mFreeFallRunnable);
        if (delay > 0) {
            this.postDelayed(mFreeFallRunnable, delay);
        } else {
            this.post(mFreeFallRunnable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }

    public void stopLoading() {
        isAnimating = false;

        if (mUpAnimatorSet != null) {
            if (mUpAnimatorSet.isRunning()) {
                mUpAnimatorSet.cancel();
            }
            mUpAnimatorSet.removeAllListeners();
            mUpAnimatorSet = null;
        }
        if (mDownAnimatorSet != null) {
            if (mDownAnimatorSet.isRunning()) {
                mDownAnimatorSet.cancel();
            }
            mDownAnimatorSet.removeAllListeners();
            mDownAnimatorSet = null;
        }
        this.removeCallbacks(mFreeFallRunnable);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startLoading(200);
        } else {
            stopLoading();
        }
    }

    public void setLoadingText(CharSequence loadingText) {
        if (TextUtils.isEmpty(loadingText)) {
            mLoadTextView.setVisibility(GONE);
        } else {
            mLoadTextView.setVisibility(VISIBLE);
        }
        mLoadTextView.setText(loadingText);
    }

    // 上抛
    public void upThrow() {
        // 不是动画状态, 则不进行动画
        if (!isAnimating) {
            return;
        }

        LshLogUtils.d("LoadingView", "upThrow");
        if (mUpAnimatorSet == null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY", mDistance, 0);
            ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 0.2f, 1);

            ObjectAnimator objectAnimator1 = null;
            switch (mShapeLoadingView.getShape()) {
                case SHAPE_RECT:
                    objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, -120);
                    break;
                case SHAPE_CIRCLE:
                    objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                    break;
                case SHAPE_TRIANGLE:
                    objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                    break;
            }
            objectAnimator.setDuration(ANIMATION_DURATION);
            objectAnimator1.setDuration(ANIMATION_DURATION);
            objectAnimator.setInterpolator(new DecelerateInterpolator(factor));
            objectAnimator1.setInterpolator(new DecelerateInterpolator(factor));
            mUpAnimatorSet = new AnimatorSet();
            mUpAnimatorSet.setDuration(ANIMATION_DURATION);
            mUpAnimatorSet.playTogether(objectAnimator, objectAnimator1, scaleIndication);

            mUpAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    freeFall();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        mUpAnimatorSet.start();
    }

    // 下落
    public void freeFall() {
        // 不是动画状态, 则不进行动画
        if (!isAnimating) {
            return;
        }

        LshLogUtils.d("LoadingView", "freeFall");
        if (mDownAnimatorSet == null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY", 0, mDistance);
            ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 1, 0.2f);

            objectAnimator.setDuration(ANIMATION_DURATION);
            objectAnimator.setInterpolator(new AccelerateInterpolator(factor));
            mDownAnimatorSet = new AnimatorSet();
            mDownAnimatorSet.setDuration(ANIMATION_DURATION);
            mDownAnimatorSet.playTogether(objectAnimator, scaleIndication);
            mDownAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mShapeLoadingView.changeShape();
                    upThrow();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        mDownAnimatorSet.start();
    }

    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}