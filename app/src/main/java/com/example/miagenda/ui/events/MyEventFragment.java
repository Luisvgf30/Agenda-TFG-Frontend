package com.example.miagenda.ui.events;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.miagenda.R;
import com.example.miagenda.api.Evento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyEventFragment extends Fragment {

    public MyEventFragment() {
        // Required empty public constructor
    }

    public static MyEventFragment newInstance(Evento evento) {
        MyEventFragment fragment = new MyEventFragment();
        Bundle args = new Bundle();
        args.putSerializable("evento", evento);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_event, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Evento evento = (Evento) bundle.getSerializable("evento");
            if (evento != null) {
                TextView nombreEventoTextView = view.findViewById(R.id.nombreMiEventoTextView);
                TextView descripcionEventoTextView = view.findViewById(R.id.descripcionMiEventoTextView);
                CalendarView fechaEventoCalendarView = view.findViewById(R.id.fechaMiEventoTextView);

                nombreEventoTextView.setText(evento.getName_event());
                descripcionEventoTextView.setText(evento.getEvent_desc());

                // Convertir la fecha de texto a milisegundos
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = sdf.parse(evento.getEvent_date());
                    if (date != null) {
                        long millis = date.getTime();
                        fechaEventoCalendarView.setDate(millis);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        Button editarEvento = view.findViewById(R.id.miEventoButton);
        editarEvento.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            Bundle args = new Bundle();
            args.putString("oldEventName", ((TextView) view.findViewById(R.id.nombreMiEventoTextView)).getText().toString());
            args.putString("eventDesc", ((TextView) view.findViewById(R.id.descripcionMiEventoTextView)).getText().toString());
            navController.navigate(R.id.editEvent, args);
        });

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }
}
