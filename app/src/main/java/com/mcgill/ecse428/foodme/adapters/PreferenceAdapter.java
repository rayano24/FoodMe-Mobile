package com.mcgill.ecse428.foodme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcgill.ecse428.foodme.R;
import com.mcgill.ecse428.foodme.model.Preference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceAdapter.MyViewHolder> {
    private List<Preference> preferenceList;
    //holds our parent's context
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        protected TextView location, cuisine, price, sortBy;
        private PreferenceAdapter.ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            location = view.findViewById(R.id.preferenceLocation);
            cuisine = view.findViewById(R.id.preferenceCuisine);
            price = view.findViewById(R.id.preferencePrice);
            sortBy = view.findViewById(R.id.preferenceSortBy);

            //add the click listeners
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(PreferenceAdapter.ItemClickListener itemClickListener){
            this.clickListener = itemClickListener;
        }

        public void onClick(View view){
            clickListener.onClick(view, getLayoutPosition(),false);
        }
        public boolean onLongClick(View view){
            clickListener.onClick(view, getLayoutPosition(),true);
            return true;
        }
    }


    public PreferenceAdapter(List<Preference> preferenceList) {
        this.preferenceList = preferenceList;
    }

    @Override
    public PreferenceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_preferences, parent, false);


        return new PreferenceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    /**
    @Override
    public void onBindViewHolder(@NonNull PreferenceAdapter.MyViewHolder holder, int position) {
        Preference preference = preferenceList.get(position);
        holder.location.setText(preference.getLocation());
        holder.price.setText(preference.getPrice());
        holder.cuisine.setText(preference.getCuisine());
        holder.sortBy.setText(preference.getSortBy());

        //Create the click listeners
        holder.setClickListener(new PreferenceAdapter.ItemClickListener(){
            @Override
            public void onClick(View view, int position, boolean isLongClick){
                List<Preference> rList = preferenceList;
                if(isLongClick){
                    //for now, no additional functionality here
                    onClick(view, position, false);
                }
                else{
                    IndividualRestaurantFragment irf = new IndividualRestaurantFragment();

                    //prepare arguments
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", rList.get(position).getName());
                    bundle.putString("PRICE", rList.get(position).getPrice());
                    bundle.putString("CUISINE",rList.get(position).getCuisine());
                    bundle.putString("DISTANCE",rList.get(position).getDistance());
                    String[] address = rList.get(position).getAddress();
                    bundle.putString("ADDRESS1",address[0]);
                    bundle.putString("ADDRESS2",address[1]);
                    irf.setArguments(bundle);

                    //swap fragments
                    FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.frame_fragmentholder, irf, "IRF").commit();
                }
            }
        });
    }
    */


    @Override
    public int getItemCount() {
        return preferenceList.size();
    }

    public interface ItemClickListener{
        void onClick (View view, int position, boolean isLongClick);
    }
}
