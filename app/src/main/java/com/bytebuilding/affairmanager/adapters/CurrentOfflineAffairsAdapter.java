package com.bytebuilding.affairmanager.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.fragments.OfflineAffairFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.Item;
import com.bytebuilding.affairmanager.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentOfflineAffairsAdapter extends AffairAdapter {

    private List<Item> items = new ArrayList<>();

    public static final int AFFAIR_TYPE = 0;
    public static final int SEPARATOR_TYPE = 1;

    public CurrentOfflineAffairsAdapter(OfflineAffairFragment currentOfflineAffairFragment) {
        super(currentOfflineAffairFragment);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AFFAIR_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.affair_model,
                        parent, false);
                View affairModelContainer = view.findViewById(R.id.affair_model_container);

                CircleImageView affairModelCircleImageView = (CircleImageView) view
                        .findViewById(R.id.civ_affair_model);

                TextView affairModelTitle = (TextView) view.findViewById(R.id.affair_model_title);
                TextView affairModelDescription = (TextView) view.findViewById(R.id
                        .affair_model_description);
                TextView affairModelDate = (TextView) view.findViewById(R.id
                        .affair_model_date);
                TextView affairModelTime = (TextView) view.findViewById(R.id.affair_model_time);

                return new AffairViewHolder(view, affairModelContainer, affairModelCircleImageView,
                        affairModelTitle, affairModelDescription, affairModelDate, affairModelTime);
            case SEPARATOR_TYPE:

                break;
            default:
                return null;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);

        if (item.isAffair()) {
            holder.itemView.setEnabled(true);
            final Affair  affair = (Affair) item;
            final AffairViewHolder affairViewHolder = (AffairViewHolder) holder;

            final View itemView = affairViewHolder.itemView;
            final Resources resources = itemView.getResources();

            affairViewHolder.affairModelTitle.setText(affair.getTitle());
            affairViewHolder.affairModelDescription.setText(affair.getDescription());

            if (affair.getPriority() == 0) {
                affairViewHolder.affairModelCircleImageView.setBackgroundColor(Color.GREEN);
            } else if (affair.getPriority() == 1) {
                affairViewHolder.affairModelCircleImageView.setBackgroundColor(Color.YELLOW);
            } else {
                affairViewHolder.affairModelCircleImageView.setBackgroundColor(Color.RED);
            }

            if (affair.getDate() != 0) {
                affairViewHolder.affairModelDate.setText(DateUtils.getDate(affair.getTimestamp()));
            } else {
                affairViewHolder.affairModelDate.setText("Дата не указана");
            }

            if (affair.getTime() != 0) {
                affairViewHolder.affairModelTime.setText(DateUtils.getTime(affair.getTimestamp()));
            } else {
                affairViewHolder.affairModelTime.setText("Время не указано");
            }

            affairViewHolder.affairModelCircleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    affair.setStatus(Affair.STATUS_DONE);

                    //affairViewHolder.affairModelCircleImageView.setBackgroundColor(Color.GRAY);
                    affairViewHolder.affairModelTitle.setTextColor(resources.getColor(R.color.color_secondary_text));
                    affairViewHolder.affairModelDescription.setTextColor(resources.getColor(R.color
                            .color_secondary_text));
                    affairViewHolder.affairModelDate.setTextColor(resources.getColor(R.color.color_secondary_text));
                    affairViewHolder.affairModelTime.setTextColor(resources.getColor(R.color.color_secondary_text));

                    ObjectAnimator flipAnimation = ObjectAnimator.ofFloat(affairViewHolder.affairModelCircleImageView,
                            "rotationY", -180f, 0f);

                    flipAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (affair.getStatus() == Affair.STATUS_DONE) {
                                affairViewHolder.affairModelCircleImageView.setBackgroundColor(Color.GRAY);

                                ObjectAnimator animationGone = ObjectAnimator.ofFloat(affairViewHolder
                                        .affairModelCircleImageView, "translationX", 0f, itemView.getWidth());

                                ObjectAnimator animationComeIn = ObjectAnimator.ofFloat(affairViewHolder
                                        .affairModelCircleImageView, "translationX", itemView.getWidth(), 0f);

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

            itemView.setVisibility(View.VISIBLE);

            itemView.setBackgroundColor(resources.getColor(R.color.md_blue_grey_50));

            affairViewHolder.affairModelTitle.setTextColor(resources.getColor(R.color.color_primary_text));
            //affairViewHolder.affairModelCircleImageView.setColorFilter(resources.getColor(affair.getPriority()));
        } else {
            holder.itemView.setEnabled(false);
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
