package com.bytebuilding.affairmanager.adapters.realm;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.MainOnlineActivity;
import com.bytebuilding.affairmanager.fragments.drawer.UserGroupsFragment;
import com.bytebuilding.affairmanager.model.realm.User;
import com.bytebuilding.affairmanager.model.realm.UserGroup;
import com.bytebuilding.affairmanager.utils.CryptoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Turkin A. on 11.05.17.
 */

public class RealmUserGroupsAdapter extends RealmRecyclerViewAdapter<UserGroup, RealmUserGroupsAdapter.UserGroupsViewHolder> {

    private UserGroupsFragment userGroupsFragment;

    private Context context;

    private Realm realm;

    public RealmUserGroupsAdapter(@Nullable OrderedRealmCollection<UserGroup> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public UserGroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_model, parent, false);

        userGroupsFragment = UserGroupsFragment.newInstance();

        context = parent.getContext();

        return new UserGroupsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserGroupsViewHolder holder, int position) {
        final UserGroup userGroup = getItem(position);

        holder.data = userGroup;

        holder.title.setText(holder.data.getUserGroupName());
        holder.description.setText(holder.data.getUserGroupDescription());
        holder.login.setText(CryptoUtils.decrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, holder.data.getUsers().first().getUserLogin()));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(userGroupsFragment.getView(), holder.title.getText().toString(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public class UserGroupsViewHolder extends RecyclerView.ViewHolder {

        private UserGroup data;

        @BindView(R.id.group_container)
        View container;

        @BindView(R.id.group_title)
        TextView title;

        @BindView(R.id.group_description)
        TextView description;

        @BindView(R.id.group_creator_login)
        TextView login;

        public UserGroupsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
