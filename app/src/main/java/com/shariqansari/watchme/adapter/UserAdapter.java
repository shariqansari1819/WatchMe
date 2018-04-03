package com.shariqansari.watchme.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shariqansari.watchme.R;
import com.shariqansari.watchme.pojo.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private ArrayList<User> arrayList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private OnUserClick onUserClick;

    public UserAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnUserClick(OnUserClick onUserClick) {
        this.onUserClick = onUserClick;
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.users_list_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = arrayList.get(position);
        holder.textViewUsername.setText(user.getUserName());
        Picasso.get()
                .load(user.getUserProfileImage())
                .placeholder(R.drawable.man)
                .into(holder.imageViewUser);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewUsername;
        ImageView imageViewUser;
        Button buttonAdd;

        UserHolder(View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUserNameUser);
            imageViewUser = itemView.findViewById(R.id.imageViewUsers);
            buttonAdd = itemView.findViewById(R.id.buttonSendRequestUser);

            buttonAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onUserClick != null) {
                onUserClick.onUserClick(v, getAdapterPosition(), arrayList.get(getAdapterPosition()).getUserId());
            }
        }
    }

    public interface OnUserClick {
        void onUserClick(View view, int position, String id);
    }
}
