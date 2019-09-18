package com.victor.stockpesistentdata.ui.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.victor.stockpesistentdata.R;
import com.victor.stockpesistentdata.model.Item;
import com.victor.stockpesistentdata.util.PriceUtil;

import java.util.ArrayList;
import java.util.List;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockListViewHolder> {
    private final Context context;
    private final List<Item> items = new ArrayList<>();
    private ItemClickListener itemClickListener;
    private ItemRemoveListener itemRemoveListener = (item, position) -> {
    };

    public StockListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StockListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_item, parent, false);
        return new StockListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockListViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bindInformation(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemRemoveListener(ItemRemoveListener itemRemoveListener) {
        this.itemRemoveListener = itemRemoveListener;
    }

    public void add(Item item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void updateList(List<Item> result) {
        items.clear();
        items.addAll(result);
        notifyDataSetChanged();
    }

    public void update(int position, Item result) {
        items.set(position, result);
        notifyDataSetChanged();
    }

    public void delete(Item item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    class StockListViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemId;
        private final TextView itemName;
        private final TextView itemPrice;
        private final TextView itemQnt;
        private Item item;

        public StockListViewHolder(@NonNull View itemView) {
            super(itemView);

            itemId = itemView.findViewById(R.id.cardview_item_id);
            itemName = itemView.findViewById(R.id.cardview_item_name);
            itemPrice = itemView.findViewById(R.id.cardview_item_price);
            itemQnt = itemView.findViewById(R.id.cardview_item_qnt);

            editListener(itemView);
            deleteListener(itemView);

        }

        private void editListener(@NonNull View itemView) {
            itemView.setOnClickListener(view -> itemClickListener.onItemClick(item, getAdapterPosition()));
        }

        private void deleteListener(@NonNull View itemView) {
            itemView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
                new MenuInflater(context).inflate(R.menu.menu_remove_item, contextMenu);
                contextMenu.findItem(R.id.menu_remove_item)
                        .setOnMenuItemClickListener(menuItem -> {
                            itemRemoveListener.onItemClick(item, getAdapterPosition());
                            return true;
                        });
            });
        }

        public void bindInformation(Item item) {
            this.item = item;
            itemId.setText(item.getId());
            itemName.setText(item.getName());
            itemPrice.setText(PriceUtil.priceCurrency(item.getPrice()));
            itemQnt.setText(item.getQuantityString());
        }
    }

    public interface ItemClickListener {
        void onItemClick(Item item, int position);
    }

    public interface ItemRemoveListener {
        void onItemClick(Item item, int position);
    }
}
