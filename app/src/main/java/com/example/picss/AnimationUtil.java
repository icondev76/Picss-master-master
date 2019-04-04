package com.example.picss;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

public class AnimationUtil {

    public static void animate(RecyclerView.ViewHolder holder,boolean goingdown ){
        AnimatorSet animatorSet= new AnimatorSet();

        ObjectAnimator animatorTranslateY= ObjectAnimator.ofFloat(holder.itemView,"translationY",goingdown==true ? 200:-200,0);
        animatorTranslateY.setDuration(1000);

        ObjectAnimator animatorTranslateX= ObjectAnimator.ofFloat(holder.itemView,"alpha",0.2f,1);
        animatorTranslateY.setDuration(1000);

        //ObjectAnimator fadeOut = ObjectAnimator.ofFloat(holder.itemView, "alpha",  1f, .3f);
        //fadeOut.setDuration(1000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(holder.itemView, "alpha", .0f, 1f);
        fadeIn.setDuration(2000);

        animatorSet.play(fadeIn);//.after(fadeOut);

        //animatorSet.playTogether(animatorTranslateY,fadeIn);
        animatorSet.start();
    }

    }

