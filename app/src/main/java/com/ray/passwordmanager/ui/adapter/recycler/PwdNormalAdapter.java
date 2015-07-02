package com.ray.passwordmanager.ui.adapter.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ray.passwordmanager.R;
import com.ray.passwordmanager.db.entity.PasswordEntity;

import java.util.List;

public class PwdNormalAdapter extends RecyclerView.Adapter<PwdNormalAdapter.PwdViewHolder> {

    private static final String TAG = PwdNormalAdapter.class.getSimpleName();

    private static final long INVALID_ID = -1;
    private static final float EXPAND_DECELERATION = 1f;
    private static final float COLLAPSE_DECELERATION = 0.7f;
    private static final int EXPAND_DURATION = 300;
    private static final int COLLAPSE_DURATION = 250;

    private List<PasswordEntity> mData;
    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private RecyclerView mListView;
    private Interpolator mExpandInterpolator;
    private Interpolator mCollapseInterpolator;
    //用于控制动画过程中的点击
    private boolean isAnimating = false;

    private long mExpandedId;
    private PwdViewHolder mExpandedItemHolder;

    public PwdNormalAdapter(RecyclerView listView) {
        this.mListView = listView;
        mDrawableBuilder = TextDrawable.builder().round();
        mExpandInterpolator = new DecelerateInterpolator(EXPAND_DECELERATION);
        mCollapseInterpolator = new DecelerateInterpolator(COLLAPSE_DECELERATION);
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
        final PasswordEntity entity = mData.get(position);
        TextDrawable drawable = mDrawableBuilder.build(String.valueOf(entity.getUsername().charAt(0)), mColorGenerator.getColor(entity.getUsername()));
        holder.passwordEntity = entity;
        holder.icon.setImageDrawable(drawable);
        holder.username.setText(entity.getUsername());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAnimating){
                    return;
                }
                if (isExpanded(entity)) {
                    // Is expanded, make collapse call.
                    collapseItem(holder, true);
                } else {
                    // Is collapsed, make expand call.
                    expandItem(holder, true);
                }
            }
        });
        boolean expanded = isExpanded(entity);
        if (expanded) {
            mExpandedItemHolder = holder;
        }
        holder.expandArea.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }

    private boolean isExpanded(PasswordEntity entity) {
        return mExpandedId == entity.getId();
    }

    private void expandItem(final PwdViewHolder itemHolder, boolean animate) {
        // Skip animation later if item is already expanded
        animate &= mExpandedId != itemHolder.passwordEntity.getId();

        if (mExpandedItemHolder != null
                && mExpandedItemHolder != itemHolder
                && mExpandedId != itemHolder.passwordEntity.getId()) {
            // Only allow one item to expand at a time.
            collapseItem(mExpandedItemHolder, animate);
        }

        bindExpandArea(itemHolder, itemHolder.itemLayout);

        mExpandedId = itemHolder.passwordEntity.getId();
        mExpandedItemHolder = itemHolder;

        final int startingHeight = itemHolder.itemLayout.getHeight();
        itemHolder.expandArea.setVisibility(View.VISIBLE);

        if (!animate) {
            // Set the "end" layout and don't do the animation.
            return;
        }


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
                final int collapseHeight = 0;

                // Set the height back to the start state of the animation.
                itemHolder.itemLayout.getLayoutParams().height = startingHeight;
                // To allow the expandArea to glide in with the expansion animation, set a
                // negative top margin, which will animate down to a margin of 0 as the height
                // is increased.
                // Note that we need to maintain the bottom margin as a fixed value (instead of
                // just using a listview, to allow for a flatter hierarchy) to fit the bottom
                // bar underneath.
                ViewGroup.MarginLayoutParams expandParams = (ViewGroup.MarginLayoutParams)
                        itemHolder.expandArea.getLayoutParams();
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
                                itemHolder.expandArea.getLayoutParams();
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
                        isAnimating = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // TODO we may have to deal with cancelations of the animation.
                        isAnimating = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimating = true;
                    }
                });
                animator.start();

                // Return false so this draw does not occur to prevent the final frame from
                // being drawn for the single frame before the animations start.
                return false;
            }
        });
    }

    private void collapseItem(final PwdViewHolder itemHolder, boolean animate) {
        mExpandedId = INVALID_ID;
        mExpandedItemHolder = null;

        // Save the starting height so we can animate from this value.
        final int startingHeight = itemHolder.itemLayout.getHeight();

        // Set the expand area to gone so we can measure the height to animate to.
        itemHolder.expandArea.setVisibility(View.GONE);

        if (!animate) {
            // Set the "end" layout and don't do the animation.
            return;
        }

        // Add an onPreDrawListener, which gets called after measurement but before the draw.
        // This way we can check the height we need to animate to before any drawing.
        // Note the series of events:
        //  * expandArea is set to GONE, which causes a layout pass
        //  * the view is measured, and our onPreDrawListener is called
        //  * we set up the animation using the start and end values.
        //  * expandArea is set to VISIBLE again so it can be shown animating.
        //  * request another layout pass.
        //  * return false so that onDraw() is not called for the single frame before
        //    the animations have started.
        final ViewTreeObserver observer = mListView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                // Calculate some values to help with the animation.
                final int endingHeight = itemHolder.itemLayout.getHeight();
                final int distance = endingHeight - startingHeight;

                // Re-set the visibilities for the start state of the animation.
                itemHolder.expandArea.setVisibility(View.VISIBLE);

                // Set up the animator to animate the expansion.
                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f)
                        .setDuration(COLLAPSE_DURATION);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        Float value = (Float) animator.getAnimatedValue();

                        // For each value from 0 to 1, animate the various parts of the layout.
                        itemHolder.itemLayout.getLayoutParams().height =
                                (int) (value * distance + startingHeight);
                        ViewGroup.MarginLayoutParams expandParams = (ViewGroup.MarginLayoutParams)
                                itemHolder.expandArea.getLayoutParams();
                        expandParams.setMargins(
                                0, (int) (value * distance), 0, 0);
                        itemHolder.msg.setAlpha(value);

                        itemHolder.itemLayout.requestLayout();
                    }
                });
                animator.setInterpolator(mCollapseInterpolator);
                // Set everything to their final values when the animation's done.
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Set it back to wrap content since we'd explicitly set the height.
                        itemHolder.itemLayout.getLayoutParams().height =
                                ViewGroup.LayoutParams.WRAP_CONTENT;

                        ViewGroup.MarginLayoutParams expandParams = (ViewGroup.MarginLayoutParams)
                                itemHolder.expandArea.getLayoutParams();
                        expandParams.setMargins(0, 0, 0, 0);

                        itemHolder.expandArea.setVisibility(View.GONE);
                        isAnimating = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // TODO we may have to deal with cancelations of the animation.
                        isAnimating = false;
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimating = true;
                    }
                });
                animator.start();

                return false;
            }
        });
    }

    private void bindExpandArea(PwdViewHolder itemHolder, View itemLayout) {
        itemHolder.msg.setText(itemHolder.passwordEntity.getPassword());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class PwdViewHolder extends RecyclerView.ViewHolder {

        View itemLayout;
        View expandArea;
        ImageView icon;
        TextView username;
        TextView msg;

        PasswordEntity passwordEntity;

        public PwdViewHolder(View view) {
            super(view);
            itemLayout = view.findViewById(R.id.pwd_item);
            expandArea = view.findViewById(R.id.expand_area);
            icon = (ImageView) view.findViewById(R.id.pwd_icon);
            username = (TextView) view.findViewById(R.id.pwd_username);
            msg = (TextView) view.findViewById(R.id.pwd_msg);
        }
    }

}
