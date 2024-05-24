package com.example.miagenda.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
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
        notifyDataSetChanged();
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
        holder.bind(tarea);
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tareaTitulo;
        TextView tareaDescripcion;
        Button estadoTarea;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tareaTitulo = itemView.findViewById(R.id.tvTaskName);
            tareaDescripcion = itemView.findViewById(R.id.tvTaskNameLabel);
            estadoTarea = itemView.findViewById(R.id.estadoTask);
        }

        void bind(Tarea tarea) {
            tareaTitulo.setText(tarea.getTaskName());
            tareaDescripcion.setText(tarea.getTaskDesc());

            estadoTarea.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("taskName", tarea.getTaskName());
                bundle.putString("taskDesc", tarea.getTaskDesc());
                bundle.putString("startDate", tarea.getDateInitial());
                bundle.putString("dueDate", tarea.getDateLimit());
                bundle.putString("status", tarea.getEstado());
                bundle.putString("priority", tarea.getTask_level());
                Navigation.findNavController(v).navigate(R.id.myTask, bundle);
            });
        }
    }
}
