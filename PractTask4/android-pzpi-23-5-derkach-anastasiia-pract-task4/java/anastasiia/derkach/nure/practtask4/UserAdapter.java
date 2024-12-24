package anastasiia.derkach.nure.practtask4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewAge;

        public UserViewHolder(View v) {
            super(v);
            textViewName = v.findViewById(R.id.textViewName);
            textViewAge = v.findViewById(R.id.textViewAge);
        }
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewAge.setText(String.valueOf(user.getAge()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
