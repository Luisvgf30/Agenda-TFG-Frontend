package com.example.miagenda.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.miagenda.R;
import com.example.miagenda.api.Tarea;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Tarea> tareas;

    public TasksAdapter(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Tarea tarea = tareas.get(position);
        holder.tareaTitulo.setText(tarea.getTaskName());
        holder.tareaDescripcion.setText(tarea.getTaskDesc());
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tareaTitulo;
        TextView tareaDescripcion;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tareaTitulo = itemView.findViewById(R.id.tvTaskName);
            tareaDescripcion = itemView.findViewById(R.id.tvTaskNameLabel);
        }
    }
}
