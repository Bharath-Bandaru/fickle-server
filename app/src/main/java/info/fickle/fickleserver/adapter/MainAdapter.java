package info.fickle.fickleserver.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import info.fickle.fickleserver.R;
import info.fickle.fickleserver.model.MainModel;

/**
 * Created by Bharath on 03/11/16.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.OfferViewHolder> {
    List<MainModel> mainModelList;

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView offer,live;
        ImageView stat;
        LinearLayout ll;
        OfferViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            ll = (LinearLayout) itemView.findViewById(R.id.llive);
            offer = (TextView) itemView.findViewById(R.id.dis_offer);
            stat = (ImageView) itemView.findViewById(R.id.stat);
            live = (TextView) itemView.findViewById(R.id.ststtt);
        }
    }
    public MainAdapter(List<MainModel> mainModelList){
        this.mainModelList = mainModelList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public MainModel getOfferItem(int pos){
        return mainModelList.get(pos);

    }
    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_item, viewGroup, false);
        OfferViewHolder pvh = new OfferViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(OfferViewHolder personViewHolder, int i) {


        personViewHolder.offer.setText(mainModelList.get(i).offerf);
        if(mainModelList.get(i).live) {
            personViewHolder.stat.setImageResource(R.drawable.live_on);
            personViewHolder.live.setVisibility(View.VISIBLE);
        }else{
            personViewHolder.stat.setImageResource(R.drawable.live_off);
            personViewHolder.live.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return mainModelList.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
