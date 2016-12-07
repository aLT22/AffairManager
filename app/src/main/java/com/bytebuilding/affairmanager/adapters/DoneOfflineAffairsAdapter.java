package com.bytebuilding.affairmanager.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.fragments.DoneOfflineAffairsFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.Item;
import com.bytebuilding.affairmanager.utils.DateUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoneOfflineAffairsAdapter extends AffairAdapter {


    public DoneOfflineAffairsAdapter(DoneOfflineAffairsFragment offlineAffairFragment) {
        super(offlineAffairFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.affair_model,
                parent, false);

        View affairModelContainer = view.findViewById(R.id.affair_model_container);
        CircleImageView affairModelCircleImageView = (CircleImageView) view.findViewById(R.id.civ_affair_model);

        TextView affairModelTitle = (TextView) view.findViewById(R.id.affair_model_title);
        TextView affairModelDescription = (TextView) view.findViewById(R.id.affair_model_description);
        TextView affairModelDate = (TextView) view.findViewById(R.id.affair_model_date);
        TextView affairModelTime = (TextView) view.findViewById(R.id.affair_model_time);

        return new AffairViewHolder(view, affairModelContainer, affairModelCircleImageView,
                affairModelTitle, affairModelDescription, affairModelDate, affairModelTime);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);

        if (item.isAffair()) {
            holder.itemView.setEnabled(true);
            final Affair affair = (Affair) item;
            final AffairViewHolder affairViewHolder = (AffairViewHolder) holder;

            final View itemView = affairViewHolder.itemView;
            final Resources resources = itemView.getResources();

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

            affairViewHolder.affairModelCircleImageView.setImageResource(R.drawable.icon_done_affair);
            affairViewHolder.affairModelTitle.setTextColor(resources.getColor(R.color.color_secondary_text));
            affairViewHolder.affairModelDescription.setTextColor(resources.getColor(R.color
                    .color_secondary_text));
            affairViewHolder.affairModelDate.setTextColor(resources.getColor(R.color.color_secondary_text));
            affairViewHolder.affairModelTime.setTextColor(resources.getColor(R.color.color_secondary_text));
            affairViewHolder.affairModelCircleImageView.setColorFilter(resources.getColor(android.R.color.darker_gray));

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
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
                    affair.setStatus(Affair.STATUS_CURRENT);

                    affairViewHolder.affairModelCircleImageView.setEnabled(false);

                    getOfflineAffairFragment().mainOfflineActivity.dbHelper.getDbUpdateManager().status(affair
                            .getTimestamp(), Affair.STATUS_CURRENT);

                    affairViewHolder.affairModelCircleImageView.setColorFilter(resources.getColor(affair.getColor()));
                    affairViewHolder.affairModelTitle.setTextColor(resources.getColor(R.color.color_primary_text));
                    affairViewHolder.affairModelDescription.setTextColor(resources.getColor(R.color
                            .color_secondary_text));
                    affairViewHolder.affairModelDate.setTextColor(resources.getColor(R.color.color_primary_text));
                    affairViewHolder.affairModelTime.setTextColor(resources.getColor(R.color.color_primary_text));

                    ObjectAnimator flipAnimation = ObjectAnimator.ofFloat(affairViewHolder.affairModelCircleImageView,
                            "rotationY", 180f, 0f);
                    affairViewHolder.affairModelCircleImageView.setImageResource(R.drawable
                            .ic_checkbox_blank_circle_white_48dp);

                    flipAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (affair.getStatus() != Affair.STATUS_DONE) {

                                ObjectAnimator animationGone = ObjectAnimator.ofFloat(itemView, "translationX", 0f,
                                        -itemView.getWidth());

                                ObjectAnimator animationComeIn = ObjectAnimator.ofFloat(itemView, "translationX",
                                        -itemView.getWidth(), 0f);

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
        }
    }
}
