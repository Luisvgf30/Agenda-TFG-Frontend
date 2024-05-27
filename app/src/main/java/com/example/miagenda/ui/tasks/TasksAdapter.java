package com.example.miagenda.ui.tasks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Tarea;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Tarea> tareas;
    private PerfilAPI perfilAPI;
    private SessionManager sessionManager;

    public TasksAdapter(List<Tarea> tareas, SessionManager sessionManager) {
        this.tareas = tareas;
        this.sessionManager = sessionManager;
        this.perfilAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
        notifyDataSetChanged();
    }

    public void deleteTask(String username, String taskName, int position) {
        Call<Void> call = perfilAPI.deleteTask(username, taskName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    tareas.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, tareas.size());

                    Context context = sessionManager.getContext();
                    if (context != null) {
                        Toast.makeText(context, "Tarea eliminada correctamente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Context context = sessionManager.getContext();
                    if (context != null) {
                        Toast.makeText(context, "Error al eliminar la tarea", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Context context = sessionManager.getContext();
                if (context != null) {
                    Toast.makeText(context, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        TextView tareaEstado;
        TextView tareaLevel;
        Button estadoTarea;
        Button deleteTaskButton;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tareaTitulo = itemView.findViewById(R.id.tvTaskName);
            tareaEstado = itemView.findViewById(R.id.tvTimeRemaining);
            tareaLevel = itemView.findViewById(R.id.LevelCard);
            estadoTarea = itemView.findViewById(R.id.estadoTask);
            deleteTaskButton = itemView.findViewById(R.id.deleteTask);
        }

        void bind(Tarea tarea) {
            tareaTitulo.setText(tarea.getTaskName());
            tareaEstado.setText(tarea.getEstado());
            tareaLevel.setText(tarea.getTask_level());


            estadoTarea.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("taskName", tarea.getTaskName());
                bundle.putString("taskDesc", tarea.getTaskDesc());
                bundle.putString("startDate", tarea.getDateInitial());
                bundle.putString("dueDate", tarea.getDateLimit());
                bundle.putString("status", tarea.getEstado());
                bundle.putString("level", tarea.getTask_level());
                Log.d("TasksAdapter", "Level: " + tarea.getTask_level());

                Navigation.findNavController(v).navigate(R.id.myTask, bundle);
            });

            deleteTaskButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                String username = sessionManager.getUsername();
                if (username != null) {
                    deleteTask(username, tarea.getTaskName(), position);
                } else {
                    Context context = sessionManager.getContext();
                    if (context != null) {
                        Toast.makeText(context, "No se pudo obtener el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
