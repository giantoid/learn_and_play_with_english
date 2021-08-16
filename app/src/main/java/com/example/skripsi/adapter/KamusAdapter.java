package com.example.skripsi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.KamusIsi;
import com.example.skripsi.R;
import com.example.skripsi.models.MKamus;

import java.util.ArrayList;
import java.util.List;

public class KamusAdapter extends RecyclerView.Adapter<KamusAdapter.KamusViewHolder> implements Filterable {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the Kamuss in a list
    private List<MKamus> mKamusList;
    private List<MKamus> filterList;
    private KamusAdapterListener listener;

    //getting the context and Kamus list with constructor
    public KamusAdapter(Context mCtx, List<MKamus> Kamuslist, KamusAdapterListener listener) {
        this.mCtx = mCtx;
        this.listener = listener;
        this.mKamusList = Kamuslist;
        this.filterList = Kamuslist;
    }

    @Override
    public KamusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.kamus_layout, null);
        return new KamusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KamusViewHolder holder, final int position) {
        //getting the Kamus of the specified position
        MKamus MKamus = filterList.get(position);

        //binding the data with the viewholder views
        holder.btn_kamus.setText(MKamus.getKategori());

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
                    filterList = mKamusList;
                } else {
                    List<MKamus> filteredList = new ArrayList<>();
                    for (MKamus row : mKamusList) {

                        if (row.getId().toLowerCase().contains(charString.toLowerCase()) || row.getKategori().toLowerCase().contains(charString.toLowerCase())) {
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
                filterList = (ArrayList<MKamus>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class KamusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       TextView btn_kamus;

        public KamusViewHolder(View itemView) {
            super(itemView);

            btn_kamus = itemView.findViewById(R.id.btn_kamus);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                MKamus MKamus = listener.onKamusSelected(filterList.get(position));

                Intent intent = new Intent(mCtx, KamusIsi.class);
                String itmId = MKamus.getId();
                intent.putExtra("id", itmId);
                intent.putExtra("judul", MKamus.getKategori());
                mCtx.startActivity(intent);
            }
        }
    }

    public interface KamusAdapterListener {
        MKamus onKamusSelected(MKamus mKamus);
    }
}
