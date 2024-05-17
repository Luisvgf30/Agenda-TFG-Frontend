// MyTaskFragment.java
package com.example.miagenda.ui.tasks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.miagenda.R;

public class MyTaskFragment extends Fragment {

    private String taskName;
    private String taskDesc;
    private String startDate;
    private String dueDate;
    private String status;
    private String priority;

    public MyTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskName = getArguments().getString("taskName");
            taskDesc = getArguments().getString("taskDesc");
            startDate = getArguments().getString("startDate");
            dueDate = getArguments().getString("dueDate");
            status = getArguments().getString("status");
            priority = getArguments().getString("priority");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nombreTarea = view.findViewById(R.id.nombreTarea);
        TextView descripcionTarea = view.findViewById(R.id.descripcionTarea);
        TextView fechaInicialTarea = view.findViewById(R.id.fechaInicialTarea);
        TextView fechaLimiteTarea = view.findViewById(R.id.fechaLimiteTarea);
        TextView estadoTarea = view.findViewById(R.id.estadoTarea);
        TextView prioridadTarea = view.findViewById(R.id.prioridadTarea);

        nombreTarea.setText(taskName);
        descripcionTarea.setText(taskDesc);
        fechaInicialTarea.setText(startDate);
        fechaLimiteTarea.setText(dueDate);
        estadoTarea.setText(status);
        prioridadTarea.setText(priority);

        view.findViewById(R.id.boton_atras).setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        view.findViewById(R.id.miTareaButton).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("username", "yourUsername");
            bundle.putString("taskName", taskName);
            Navigation.findNavController(v).navigate(R.id.action_myTaskFragment_to_editTasksFragment, bundle);
        });
    }
}
