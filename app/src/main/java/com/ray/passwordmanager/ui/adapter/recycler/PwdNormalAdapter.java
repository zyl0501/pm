package com.ray.passwordmanager.ui.adapter.recycler;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ray.passwordmanager.R;
import com.ray.passwordmanager.db.entity.PasswordEntity;
import com.ray.passwordmanager.ui.adapter.BasePMAdapter;
import com.ray.passwordmanager.ui.fragment.CheeseListFragment;

import java.util.List;

public class PwdNormalAdapter extends RecyclerView.Adapter<PwdNormalAdapter.PwdViewHolder> {

    private static final float EXPAND_DECELERATION = 1f;
    private static final int EXPAND_DURATION = 300;

    private List<PasswordEntity> mData;
    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private RecyclerView mListView;
    private DecelerateInterpolator mExpandInterpolator;

    public PwdNormalAdapter(RecyclerView listView) {
        this.mListView = listView;
        mDrawableBuilder = TextDrawable.builder().round();
        mExpandInterpolator = new DecelerateInterpolator(EXPAND_DECELERATION);
    }

    public void setData(List<PasswordEntity> data) {
        this.mData = data;
    }

    @Override
    public PwdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pwd_normal, parent, false);
        assert view != null;
        return new PwdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PwdViewHolder holder, int position) {
        PasswordEntity entity = mData.get(position);
        TextDrawable drawable = mDrawableBuilder.build(String.valueOf(entity.getUsername().charAt(0)), mColorGenerator.getColor(entity.getUsername()));
        holder.icon.setImageDrawable(drawable);
        holder.username.setText(entity.getUsername());
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandAlarm(holder, true);
            }
        });
    }

    private void expandAlarm(final PwdViewHolder itemHolder, boolean animate) {
        final int startingHeight = itemHolder.itemLayout.getHeight();
        itemHolder.msg.setVisibility(View.VISIBLE);

        // Add an onPreDrawListener, which gets called after measurement but before the draw.
        // This way we can check the height we need to animate to before any drawing.
        // Note the series of events:
        //  * expandArea is set to VISIBLE, which causes a layout pass
        //  * the view is measured, and our onPreDrawListener is called
        //  * we set up the animation using the start and end values.
        //  * the height is set back to the starting point so it can be animated down.
        //  * request another layout pass.
        //  * return false so that onDraw() is not called for the single frame before
        //    the animations have started.
        final ViewTreeObserver observer = mListView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // We don't want to continue getting called for every listview drawing.
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                // Calculate some values to help with the animation.
                final int endingHeight = itemHolder.itemLayout.getHeight();
                final int distance = endingHeight - startingHeight;
                final int collapseHeight = 180;

                // Set the height back to the start state of the animation.
                itemHolder.itemLayout.getLayoutParams().height = startingHeight;
                // To allow the expandArea to glide in with the expansion animation, set a
                // negative top margin, which will animate down to a margin of 0 as the height
                // is increased.
                // Note that we need to maintain the bottom margin as a fixed value (instead of
                // just using a listview, to allow for a flatter hierarchy) to fit the bottom
                // bar underneath.
                ViewGroup.MarginLayoutParams expandParams = (ViewGroup.MarginLayoutParams)
                        itemHolder.msg.getLayoutParams();
                expandParams.setMargins(0, -distance, 0, collapseHeight);
                itemHolder.itemLayout.requestLayout();

                // Set up the animator to animate the expansion.
                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f)
                        .setDuration(EXPAND_DURATION);
                animator.setInterpolator(mExpandInterpolator);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        Float value = (Float) animator.getAnimatedValue();

                        // For each value from 0 to 1, animate the various parts of the layout.
                        itemHolder.itemLayout.getLayoutParams().height =
                                (int) (value * distance + startingHeight);
                        ViewGroup.MarginLayoutParams expandParams = (ViewGroup.MarginLayoutParams)
                                itemHolder.msg.getLayoutParams();
                        expandParams.setMargins(
                                0, (int) -((1 - value) * distance), 0, collapseHeight);

                        itemHolder.itemLayout.requestLayout();
                    }
                });
                // Set everything to their final values when the animation's done.
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Set it back to wrap content since we'd explicitly set the height.
                        itemHolder.itemLayout.getLayoutParams().height =
                                ViewGroup.LayoutParams.WRAP_CONTENT;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // TODO we may have to deal with cancelations of the animation.
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                });
                animator.start();

                // Return false so this draw does not occur to prevent the final frame from
                // being drawn for the single frame before the animations start.
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class PwdViewHolder extends RecyclerView.ViewHolder {

        View itemLayout;
        ImageView icon;
        TextView username;
        TextView msg;

        public PwdViewHolder(View view) {
            super(view);
            itemLayout = view.findViewById(R.id.pwd_item);
            icon = (ImageView) view.findViewById(R.id.pwd_icon);
            username = (TextView) view.findViewById(R.id.pwd_username);
            msg = (TextView) view.findViewById(R.id.pwd_msg);
        }
    }

}
