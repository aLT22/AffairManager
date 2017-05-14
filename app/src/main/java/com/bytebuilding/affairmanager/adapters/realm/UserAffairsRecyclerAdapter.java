package com.bytebuilding.affairmanager.adapters.realm;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.MainOnlineActivity;
import com.bytebuilding.affairmanager.fragments.drawer.UserAffairsFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;
import com.bytebuilding.affairmanager.utils.DateUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Turkin A. on 13.05.17.
 */

public class UserAffairsRecyclerAdapter extends RecyclerView.Adapter<UserAffairsRecyclerAdapter.ViewHolder> {

    private static final String TAG = UserAffairsRecyclerAdapter.class.getSimpleName();

    private List<UserAffair> mList;

    private UserAffairsFragment userAffairsFragment;

    //private Realm realm;

    private Context context;

    public UserAffairsRecyclerAdapter(UserAffairsFragment userAffairsFragment) {
        this.mList = new ArrayList<>();
        this.userAffairsFragment = UserAffairsFragment.newInstance();
        context = userAffairsFragment.getActivity();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.affair_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserAffair item = mList.get(position);

        holder.data = item;

        final Resources resources = holder.itemView.getResources();

        fillAffairWidgets(holder, resources);

        setLayoutAffairByStatus(holder, item, resources);

        holder.userAffairContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAffairsFragment.seeDetails(item, context);
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

                OfflineNotificationHelper.getInstance().doneAlarm(item.getTimestamp());

                return true;
            }
        });

        holder.userAffairPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getStatus() == Affair.STATUS_CURRENT) {
                    setDoneStatus(item, holder, resources);
                } else {
                    setCurrentStatus(item, holder, resources);
                }

            }
        });
    }

    public UserAffair getUserAffair(int position) {
        return this.mList.get(position);
    }

    public void addUserAffair(UserAffair userAffair) {
        mList.add(userAffair);
        notifyDataSetChanged();
    }

    public void addUserAffair(int position, UserAffair userAffair) {
        mList.add(position, userAffair);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position <= getItemCount()) {
            mList.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeAllItems() {
        if (getItemCount() != 0) {
            mList = new ArrayList<>();
            notifyDataSetChanged();
        }
    }

    private void setLayoutAffairByStatus(ViewHolder holder, UserAffair userAffair, Resources resources) {
        if (userAffair.getStatus() == Affair.STATUS_CURRENT) {
            holder.userAffairPriority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
            holder.userAffairPriority.setColorFilter(resources.getColor(userAffair.getColor()));

            holder.userAffairTitle.setTextColor(resources.getColor(R.color.color_primary_text));

            holder.userAffairDescription.setTextColor(resources.getColor(R.color.color_secondary_text));

            holder.userAffairDate.setTextColor(resources.getColor(R.color.color_primary_text));

            holder.userAffairTime.setTextColor(resources.getColor(R.color.color_primary_text));
        } else if (userAffair.getStatus() == Affair.STATUS_DONE){
            holder.userAffairPriority.setImageResource(R.drawable.icon_done_affair);

            holder.userAffairTitle.setTextColor(resources.getColor(R.color.color_secondary_text));
            holder.userAffairDescription.setTextColor(resources.getColor(R.color.color_secondary_text));
            holder.userAffairDate.setTextColor(resources.getColor(R.color.color_secondary_text));
            holder.userAffairTime.setTextColor(resources.getColor(R.color.color_secondary_text));
            holder.userAffairPriority.setColorFilter(resources.getColor(android.R.color.darker_gray));
        }
    }

    private void setCurrentStatus(final UserAffair userAffair, final ViewHolder holder, final Resources resources) {
        holder.userAffairPriority.setEnabled(false);

        userAffairsFragment.updateStatusAffair(userAffair.getTimestamp(), Affair.STATUS_CURRENT);
        userAffair.setStatus(Affair.STATUS_CURRENT);

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
                    holder.userAffairPriority.setColorFilter(resources.getColor(userAffair.getColor()));

                    //holder.userAffairPriority.setEnabled(true);
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

    private void setDoneStatus(final UserAffair userAffair, final ViewHolder holder, Resources resources) {
        holder.userAffairPriority.setEnabled(false);

        userAffairsFragment.updateStatusAffair(userAffair.getTimestamp(), Affair.STATUS_DONE);
        userAffair.setStatus(Affair.STATUS_DONE);

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

                    //holder.userAffairPriority.setEnabled(true);
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

    private void fillAffairWidgets(ViewHolder holder, Resources resources) {
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
    }

    public void deleteDialog(final int position) {
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.MyDialogTheme));

        deleteDialog.setMessage(R.string.delete_dialog_message);
        deleteDialog.setIcon(R.drawable.titlecalendar_icon);

        final UserAffair item = UserAffairsRecyclerAdapter.this.mList.get(position);

        final long timestamp = item.getTimestamp();

        deleteDialog.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /*Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<UserAffair> results = realm.where(UserAffair.class)
                                .equalTo("timestamp", timestamp)
                                .findAll();
                        results.deleteAllFromRealm();
                    }
                });
                realm.close();*/

                mList.remove(position);
                UserAffairsRecyclerAdapter.this.notifyDataSetChanged();

                userAffairsFragment.deleteAffairFromFirebase(timestamp);

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

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}