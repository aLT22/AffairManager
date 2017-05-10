package com.bytebuilding.affairmanager.adapters.realm;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.fragments.drawer.UserAffairsFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;
import com.bytebuilding.affairmanager.utils.DateUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Turkin A. on 03.05.17.
 */

public class RealmUserAffairsAdapter extends RealmRecyclerViewAdapter<UserAffair, RealmUserAffairsAdapter.UserAffairsViewHolder> {

    private UserAffairsFragment userAffairsFragment;

    private Context context;

    private Realm realm;

    public RealmUserAffairsAdapter(@Nullable OrderedRealmCollection<UserAffair> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public RealmUserAffairsAdapter.UserAffairsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.affair_model, parent, false);

        userAffairsFragment = UserAffairsFragment.newInstance();

        context = parent.getContext();

        return new UserAffairsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RealmUserAffairsAdapter.UserAffairsViewHolder holder, final int position) {
        final UserAffair userAffair = getItem(position);

        realm = Realm.getDefaultInstance();

        holder.data = userAffair;

        final Resources resources = holder.itemView.getResources();

        holder.userAffairTitle.setText(holder.data.getTitle());
        holder.userAffairDescription.setText(holder.data.getDescription());
        if (holder.data.getDate() != 0) {
            holder.userAffairDate.setText(DateUtils.getDate(holder.data.getTimestamp()));
        } else {
            holder.userAffairDate.setText("-");
        }

        if (holder.data.getTime() != 0) {
            holder.userAffairTime.setText(DateUtils.getTime(holder.data.getTimestamp()));
        } else {
            holder.userAffairTime.setText("-");
        }

        holder.userAffairPriority.setEnabled(true);

        if (holder.data.getDate() != 0 && holder.data.getDate() < Calendar.getInstance().getTimeInMillis()) {
            holder.userAffairContainer.setBackgroundColor(resources.getColor(R.color.color_primary_light));
        }

        holder.userAffairPriority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
        holder.userAffairPriority.setColorFilter(resources.getColor(userAffair.getColor()));

        holder.userAffairTitle.setTextColor(resources.getColor(R.color.color_primary_text));

        holder.userAffairDescription.setTextColor(resources.getColor(R.color.color_secondary_text));

        holder.userAffairDate.setTextColor(resources.getColor(R.color.color_primary_text));

        holder.userAffairTime.setTextColor(resources.getColor(R.color.color_primary_text));

        holder.userAffairContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAffairsFragment.seeDetails(userAffair, context);
            }
        });

        holder.userAffairContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deleteDialog(position);
                    }
                }, 1000);

                OfflineNotificationHelper.getInstance().doneAlarm(userAffair.getTimestamp());

                return true;
            }
        });

        holder.userAffairPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAffair.getStatus() == Affair.STATUS_CURRENT) {
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            userAffair.setStatus(Affair.STATUS_DONE);
                            realm.copyToRealmOrUpdate(userAffair);
                            RealmResults<UserAffair> results = realm.where(UserAffair.class).findAll();
                            results.sort("status", Sort.ASCENDING);
                            realm.copyToRealmOrUpdate(results);
                        }
                    });

                    holder.userAffairPriority.setEnabled(false);

                    holder.userAffairTitle.setTextColor(resources.getColor(R.color.color_secondary_text));
                    holder.userAffairDescription.setTextColor(resources.getColor(R.color.color_secondary_text));
                    holder.userAffairDate.setTextColor(resources.getColor(R.color.color_secondary_text));
                    holder.userAffairTime.setTextColor(resources.getColor(R.color.color_secondary_text));
                    holder.userAffairPriority.setColorFilter(resources.getColor(android.R.color.darker_gray));

                    ObjectAnimator flipAnimation = ObjectAnimator.ofFloat(holder.userAffairPriority, "rotationY", -360f, 0f);

                    OfflineNotificationHelper.getInstance().doneAlarm(userAffair.getTimestamp());

                    flipAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (userAffair.getStatus() == Affair.STATUS_DONE) {
                                holder.userAffairPriority.setImageResource(R.drawable.icon_done_affair);
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
                } else {
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            userAffair.setStatus(Affair.STATUS_CURRENT);
                            realm.copyToRealmOrUpdate(userAffair);
                            RealmResults<UserAffair> results = realm.where(UserAffair.class).findAll();
                            results.sort("status", Sort.ASCENDING);
                            realm.copyToRealmOrUpdate(results);
                        }
                    });

                    holder.userAffairPriority.setEnabled(false);

                    holder.userAffairTitle.setTextColor(resources.getColor(R.color.color_secondary_text));
                    holder.userAffairDescription.setTextColor(resources.getColor(R.color.color_secondary_text));
                    holder.userAffairDate.setTextColor(resources.getColor(R.color.color_secondary_text));
                    holder.userAffairTime.setTextColor(resources.getColor(R.color.color_secondary_text));
                    holder.userAffairPriority.setColorFilter(resources.getColor(android.R.color.darker_gray));

                    ObjectAnimator flipAnimation = ObjectAnimator.ofFloat(holder.userAffairPriority, "rotationY", -360f, 0f);

                    if (userAffair.getDate() != 0 || userAffair.getTime() != 0) {
                        OfflineNotificationHelper.getInstance().setReceiver(userAffair);
                    }

                    flipAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (userAffair.getStatus() != Affair.STATUS_DONE) {
                                holder.userAffairPriority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
                                //holder.userAffairPriority.setColorFilter(resources.getColor(userAffair.getColor()));
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

            }
        });
    }

    public void deleteDialog(final int position) {
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.MyDialogTheme));

        deleteDialog.setMessage(R.string.delete_dialog_message);
        deleteDialog.setIcon(R.drawable.titlecalendar_icon);

        final UserAffair item = RealmUserAffairsAdapter.this.getItem(position);

        final long timestamp = item.getTimestamp();

        deleteDialog.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<UserAffair> results = realm.where(UserAffair.class)
                                .equalTo("timestamp", timestamp)
                                .findAll();
                        results.deleteAllFromRealm();
                    }
                });
                RealmUserAffairsAdapter.this.notifyItemRemoved(position);
                realm.close();

                OfflineNotificationHelper.getInstance().doneAlarm(timestamp);

                Toast.makeText(context.getApplicationContext(), R.string.toast_delete_dialog_accept, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        deleteDialog.setNegativeButton(R.string.button_decline, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(context.getApplicationContext(), R.string.toast_delete_dialog_cancel, Toast.LENGTH_SHORT).show();
            }
        });

        deleteDialog.show();
    }

    public class UserAffairsViewHolder extends RecyclerView.ViewHolder {

        public UserAffair data;

        @BindView(R.id.affair_model_container)
        View userAffairContainer;

        @BindView(R.id.civ_affair_model)
        CircleImageView userAffairPriority;

        @BindView(R.id.affair_model_title)
        TextView userAffairTitle;

        @BindView(R.id.affair_model_description)
        TextView userAffairDescription;

        @BindView(R.id.affair_model_date)
        TextView userAffairDate;

        @BindView(R.id.affair_model_time)
        TextView userAffairTime;


        public UserAffairsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
