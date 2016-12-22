package com.bytebuilding.affairmanager.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;

import java.util.Calendar;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.fragments.OfflineAffairFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.Item;
import com.bytebuilding.affairmanager.model.Separator;
import com.bytebuilding.affairmanager.utils.DateUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentOfflineAffairsAdapter extends AffairAdapter {

    public static final int AFFAIR_TYPE = 0;
    public static final int SEPARATOR_TYPE = 1;

    public CurrentOfflineAffairsAdapter(OfflineAffairFragment currentOfflineAffairFragment) {
        super(currentOfflineAffairFragment);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AFFAIR_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.affair_model, parent, false);

                View affairModelContainer = view.findViewById(R.id.affair_model_container);
                CircleImageView affairModelCircleImageView = (CircleImageView) view.findViewById(R.id.civ_affair_model);
                TextView affairModelTitle = (TextView) view.findViewById(R.id.affair_model_title);
                TextView affairModelDescription = (TextView) view.findViewById(R.id.affair_model_description);
                TextView affairModelDate = (TextView) view.findViewById(R.id.affair_model_date);
                TextView affairModelTime = (TextView) view.findViewById(R.id.affair_model_time);

                return new AffairViewHolder(view, affairModelContainer, affairModelCircleImageView, affairModelTitle,
                        affairModelDescription, affairModelDate, affairModelTime);
            case SEPARATOR_TYPE:
                View separator = LayoutInflater.from(parent.getContext()).inflate(R.layout.separator_model, parent,
                        false);
                TextView separatorTitle = (TextView) separator.findViewById(R.id.tv_separator);

                return new SeparatorViewholder(separator, separatorTitle);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);

        final Resources resources = holder.itemView.getResources();

        if (item.isAffair()) {
            holder.itemView.setEnabled(true);
            final Affair affair = (Affair) item;
            final AffairViewHolder affairViewHolder = (AffairViewHolder) holder;

            final View itemView = affairViewHolder.itemView;

            affairViewHolder.affairModelTitle.setText(affair.getTitle());
            affairViewHolder.affairModelDescription.setText(affair.getDescription());

            if (affair.getDate() != 0) {
                affairViewHolder.affairModelDate.setText(DateUtils.getDate(affair.getTimestamp()));
            } else {
                affairViewHolder.affairModelDate.setText("-");
            }

            if (affair.getTime() != 0) {
                affairViewHolder.affairModelTime.setText(DateUtils.getTime(affair.getTimestamp()));
            } else {
                affairViewHolder.affairModelTime.setText("-");
            }

            itemView.setVisibility(View.VISIBLE);

            affairViewHolder.affairModelCircleImageView.setEnabled(true);

            if (affair.getDate() != 0 && affair.getDate() < Calendar.getInstance().getTimeInMillis()) {
                itemView.setBackgroundColor(resources.getColor(R.color.color_primary_light));
            }

            affairViewHolder.affairModelCircleImageView.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
            affairViewHolder.affairModelCircleImageView.setColorFilter(resources.getColor(affair.getColor()));
            affairViewHolder.affairModelTitle.setTextColor(resources.getColor(R.color.color_primary_text));
            affairViewHolder.affairModelDescription.setTextColor(resources.getColor(R.color
                    .color_secondary_text));
            affairViewHolder.affairModelDate.setTextColor(resources.getColor(R.color.color_primary_text));
            affairViewHolder.affairModelTime.setTextColor(resources.getColor(R.color.color_primary_text));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOfflineAffairFragment().showAffairEditDialog(affair);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    /*Handler нужен для того, чтобы сработал Ripple-эффект*/
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getOfflineAffairFragment().deleteDialog(affairViewHolder.getLayoutPosition());
                        }
                    }, 1000);

                    return true;
                }
            });

            affairViewHolder.affairModelCircleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    affair.setStatus(Affair.STATUS_DONE);

                    affairViewHolder.affairModelCircleImageView.setEnabled(false);

                    getOfflineAffairFragment().mainOfflineActivity.dbHelper.getDbUpdateManager().status(affair
                            .getTimestamp(), Affair.STATUS_DONE);

                    affairViewHolder.affairModelTitle.setTextColor(resources.getColor(R.color.color_secondary_text));
                    affairViewHolder.affairModelDescription.setTextColor(resources.getColor(R.color
                            .color_secondary_text));
                    affairViewHolder.affairModelDate.setTextColor(resources.getColor(R.color.color_secondary_text));
                    affairViewHolder.affairModelTime.setTextColor(resources.getColor(R.color.color_secondary_text));
                    affairViewHolder.affairModelCircleImageView.setColorFilter(resources.getColor(android.R.color
                            .darker_gray));

                    ObjectAnimator flipAnimation = ObjectAnimator.ofFloat(affairViewHolder.affairModelCircleImageView,
                            "rotationY", -360f, 0f);

                    flipAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (affair.getStatus() == Affair.STATUS_DONE) {
                                affairViewHolder.affairModelCircleImageView.setImageResource(R.drawable.icon_done_affair);

                                ObjectAnimator animationGoneLeft = ObjectAnimator.ofFloat(itemView, "translationX", 0f,
                                        -350f);
                                ObjectAnimator animationGone = ObjectAnimator.ofFloat(itemView, "translationX", 0f,
                                        itemView.getWidth());
                                animationGone.setStartDelay(150);
                                animationGone.setDuration(200);

                                ObjectAnimator animationComeIn = ObjectAnimator.ofFloat(itemView,
                                        "translationX", itemView.getWidth(), 0f);
                                animationComeIn.setStartDelay(150);

                                animationGone.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        itemView.setVisibility(View.GONE);
                                        getOfflineAffairFragment().moveAffair(affair);
                                        removeItem(affairViewHolder.getLayoutPosition());
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });

                                AnimatorSet translations = new AnimatorSet();
                                translations.play(animationGoneLeft).before(animationGone);
                                translations.play(animationGone).before(animationComeIn);
                                translations.start();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    flipAnimation.start();
                }
            });
        } else {
            holder.itemView.setEnabled(false);

            Separator separator = (Separator) item;
            SeparatorViewholder separatorViewholder = (SeparatorViewholder) holder;

            separatorViewholder.separatorTextView.setText(resources.getString(separator.getSeparatorType()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isAffair()) {
            return AFFAIR_TYPE;
        } else {
            return SEPARATOR_TYPE;
        }
    }

}
