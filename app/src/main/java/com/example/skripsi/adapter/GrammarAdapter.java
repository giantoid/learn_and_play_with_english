package com.example.skripsi.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.R;
import com.example.skripsi.models.MGrammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GrammarAdapter extends RecyclerView.Adapter<GrammarAdapter.GrammarViewHolder> implements Filterable {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the Grammars in a list
    private List<MGrammar> mGrammarList;
    private List<MGrammar> filterList;
    private GrammarAdapterListener listener;

    //getting the context and Grammar list with constructor
    public GrammarAdapter(Context mCtx, List<MGrammar> Grammarlist, GrammarAdapterListener listener) {
        this.mCtx = mCtx;
        this.listener = listener;
        this.mGrammarList = Grammarlist;
        this.filterList = Grammarlist;
    }

    @Override
    public GrammarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.grammar_layout, null);
        return new GrammarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GrammarViewHolder holder, final int position) {
        //getting the Grammar of the specified position
        MGrammar MGrammar = filterList.get(position);

        //binding the data with the viewholder views
        holder.txtGrammar.setText(MGrammar.getGrammar());

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
                    filterList = mGrammarList;
                } else {
                    List<MGrammar> filteredList = new ArrayList<>();
                    for (MGrammar row : mGrammarList) {

                        if (row.getId().toLowerCase().contains(charString.toLowerCase()) || row.getGrammar().toLowerCase().contains(charString.toLowerCase())) {
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
                filterList = (ArrayList<MGrammar>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class GrammarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtGrammar;
//        ImageButton btnSpeaker;
        TextToSpeech TTS;

        public GrammarViewHolder(View itemView) {
            super(itemView);

            txtGrammar = itemView.findViewById(R.id.txtGrammar);
//            btnSpeaker = itemView.findViewById(R.id.btnSpeaker);

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

            txtGrammar.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                MGrammar MGrammar = listener.onGrammarSelected(filterList.get(position));

                String toSpeak = MGrammar.getGrammar();
                if (toSpeak.equals("")) {
                    Toast.makeText(mCtx, "Masukkan pesan", Toast.LENGTH_SHORT).show();

                } else {
                    TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        }
    }

    public interface GrammarAdapterListener {
        MGrammar onGrammarSelected(MGrammar mGrammar);
    }
}
