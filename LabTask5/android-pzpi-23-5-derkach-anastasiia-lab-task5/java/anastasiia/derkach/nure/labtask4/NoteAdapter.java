package anastasiia.derkach.nure.labtask4;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final List<Note> notes;
    private final Context context;
    private final OnNoteClickListener onNoteClickListener;
    private final OnNoteMenuClickListener onNoteMenuClickListener;

    public NoteAdapter(Context context, List<Note> notes, OnNoteClickListener onNoteClickListener,
                       OnNoteMenuClickListener onNoteMenuClickListener) {
        this.context = context;
        this.notes = notes;
        this.onNoteClickListener = onNoteClickListener;
        this.onNoteMenuClickListener = onNoteMenuClickListener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        NoteViewHolder viewHolder = new NoteViewHolder(itemView);

        // Регистрация контекстного меню для каждого элемента
        itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            MenuInflater inflater = new MenuInflater(context);
            inflater.inflate(R.menu.menu_context, menu);
            menu.setHeaderTitle("Выберите действие");

            menu.findItem(R.id.action_edit).setOnMenuItemClickListener(item -> {
                onNoteMenuClickListener.onEditNote(viewHolder.getAdapterPosition());
                return true;
            });

            menu.findItem(R.id.action_delete).setOnMenuItemClickListener(item -> {
                onNoteMenuClickListener.onDeleteNote(viewHolder.getAdapterPosition());
                return true;
            });
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.textViewTitle.setText(note.getTitle());
        holder.textViewTime.setText(note.getFormattedTime());
        holder.imageViewNoteIcon.setImageResource(note.getIconResourceId());

        // Установка иконки в зависимости от уровня приоритета
        switch (note.getPriorityLevel()) {
            case LOW:
                holder.imageViewPriority.setImageResource(R.drawable.ic_priority_low);
                break;
            case MEDIUM:
                holder.imageViewPriority.setImageResource(R.drawable.ic_priority_middle);
                break;
            case HIGH:
                holder.imageViewPriority.setImageResource(R.drawable.ic_priority_high);
                break;
        }

        int textColor = getTextColorBasedOnTheme();
        holder.textViewTitle.setTextColor(textColor);
        holder.textViewTime.setTextColor(textColor);

        // Обработка клика на элемент
        holder.itemView.setOnClickListener(v -> onNoteClickListener.onNoteClick(note));
    }

    private int getTextColorBasedOnTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String currentTheme = preferences.getString("theme", "light");

        if (currentTheme.equals("dark")) {
            return context.getResources().getColor(R.color.textColorDark); // Белый цвет для темной темы
        } else {
            return context.getResources().getColor(R.color.textColorLight); // Черный цвет для светлой темы
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewTime;
        ImageView imageViewNoteIcon, imageViewPriority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewNoteTitle);
            textViewTime = itemView.findViewById(R.id.textViewNoteTime);
            imageViewNoteIcon = itemView.findViewById(R.id.imageViewNoteIcon);
            imageViewPriority = itemView.findViewById(R.id.imageViewPriority);
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public void updateNotes(List<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }
    public interface OnNoteMenuClickListener {
        void onEditNote(int position);
        void onDeleteNote(int position);
    }
}

