package com.example.skripsi.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.R;
import com.example.skripsi.models.MNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.NumberViewHolder> implements Filterable {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the Numbers in a list
    private List<MNumber> mNumberList;
    private List<MNumber> filterList;
    private NumberAdapter.NumberAdapterListener listener;

    //getting the context and Number list with constructor
    public NumberAdapter(Context mCtx, List<MNumber> Numberlist, NumberAdapter.NumberAdapterListener listener) {
        this.mCtx = mCtx;
        this.listener = listener;
        this.mNumberList = Numberlist;
        this.filterList = Numberlist;
    }

    @Override
    public NumberAdapter.NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.number_layout, null);
        return new NumberAdapter.NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NumberAdapter.NumberViewHolder holder, final int position) {
        //getting the Number of the specified position
        MNumber MNumber = filterList.get(position);

        //binding the data with the viewholder views
        holder.txtNumber.setText(MNumber.getNumber());

    }


    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterList = mNumberList;
                } else {
                    List<MNumber> filteredList = new ArrayList<>();
                    for (MNumber row : mNumberList) {

                        if (row.getId().toLowerCase().contains(charString.toLowerCase()) || row.getNumber().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<MNumber>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtNumber;
        ImageButton btnSpeaker;
        TextToSpeech TTS;

        public NumberViewHolder(View itemView) {
            super(itemView);

            txtNumber = itemView.findViewById(R.id.txtNumber);
            btnSpeaker = itemView.findViewById(R.id.btnSpeaker);

            TTS = new TextToSpeech(mCtx, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                    TTS.setLanguage(Locale.US);
//                        TTS.setLanguage(new Locale("in_ID"));

                    } else {
                        Toast.makeText(mCtx, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            btnSpeaker.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                MNumber MNumber = listener.onNumberSelected(filterList.get(position));

                String toSpeak = txtNumber.getText().toString().trim();
                TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public interface NumberAdapterListener {
        MNumber onNumberSelected(MNumber mNumber);
    }
}
