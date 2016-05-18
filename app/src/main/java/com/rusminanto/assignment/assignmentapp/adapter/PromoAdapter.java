package com.rusminanto.assignment.assignmentapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rusminanto.assignment.assignmentapp.R;
import com.rusminanto.assignment.assignmentapp.model.Promo;

import java.util.List;

/**
 * Created by tyasrus on 18/05/16.
 *
 * recyclerview adapter to populate list of promo
 */
public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.ViewHolder> {

    private Context context;
    private List<Promo> promos;
    private OnPromoItemClickListener onPromoItemClickListener;

    public PromoAdapter(List<Promo> promos) {
        this.promos = promos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Promo promo = promos.get(position);
        holder.nameView.setText(promo.getName());
        holder.cityView.setText(promo.getCity());
    }

    @Override
    public int getItemCount() {
        return promos.size();
    }

    public void setOnPromoItemClickListener(OnPromoItemClickListener onPromoItemClickListener) {
        this.onPromoItemClickListener = onPromoItemClickListener;
    }

    public interface OnPromoItemClickListener {
        void OnPromoItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameView;
        private TextView cityView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.promo_name);
            cityView = (TextView) itemView.findViewById(R.id.promo_city);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onPromoItemClickListener != null) {
                onPromoItemClickListener.OnPromoItemClick(v, getAdapterPosition());
            }
        }
    }
}
