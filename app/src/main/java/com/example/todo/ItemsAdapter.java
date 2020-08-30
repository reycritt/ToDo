package com.example.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Responsible for displaying data from model into a row in RecyclerView
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    public interface OnClickListener {
        void OnItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    List<String> items;
    List<String> notes;
    List<String> priorItems;

    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    int run = 0;

    public ItemsAdapter(List<String> items, List<String> notes, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.notes = notes;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
        priorItems = new ArrayList<>();

        //System.out.println(items);

        //int itemLength = items.size();

        //System.out.println("LOOKY HERE: " + items.get(0).charAt(1));
        //prioritySort();

    }

    public void prioritySort () {
        for (int index = 0; index < items.size(); index++) {
            if ((items.get(index).charAt(0) == '(' && items.get(index).charAt(1) == '*') && (items.get(index).charAt(2) >= 48 && items.get(index).charAt(0) <= 57)) {
                //priorItems.add(items.remove(index));
                //index--;
            } else {
                priorItems.add(items.remove(index));
                index--;
            }
        }

        System.out.println("Prior: " + priorItems);
        System.out.println("Items: " + items);
        Collections.sort(items);
        items.addAll(priorItems);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout inflator to inflate view, using a predesigned list given from android
        //(bare in mind a custom list can be created by making a new layout.xml)
        View todoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        /*
        Collections.sort(items);
        System.out.println("Running: " + run);
        run++;
        //This is where the sorting should go; it runs 3 times during first load
        //Upon further studying, nope :(
         */

        //Wrap if inside a ViewHolder and return it
        return new ViewHolder(todoView);
    }

    //Binds data to particular ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab item at position
        String item = items.get(position);
        //String note = notes.get(position);
        //Bind item into specified ViewHolder
        holder.bind(item);
        //holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //This class is a container to provide easy access to vies that represent
    //items of the list
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        /*
        Update view inside the ViewHolder with data "String item"
         */
        public void bind(String item) {
            tvItem.setText(item);

            tvItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    clickListener.OnItemClicked(getAdapterPosition());
                }
            });

            tvItem.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    //Notify listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
