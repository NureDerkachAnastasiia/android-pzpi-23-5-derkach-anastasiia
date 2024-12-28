package anastasiia.derkach.nure.labtask4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final List<Integer> iconResourceIds; // Список ресурсов иконок
    private final OnIconClickListener onIconClickListener;

    public interface OnIconClickListener {
        void onIconClick(int resourceId); // Метод, который вызывается при выборе иконки
    }

    public GalleryAdapter(List<Integer> iconResourceIds, OnIconClickListener onIconClickListener) {
        this.iconResourceIds = iconResourceIds;
        this.onIconClickListener = onIconClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_icon, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int resourceId = iconResourceIds.get(position);
        holder.imageView.setImageResource(resourceId);
        holder.itemView.setOnClickListener(v -> onIconClickListener.onIconClick(resourceId));
    }

    @Override
    public int getItemCount() {
        return iconResourceIds.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iconImageView);
        }
    }
}
