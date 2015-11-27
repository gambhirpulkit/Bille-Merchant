package com.example.shubham.bille;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;


public class CreateBillRecyclerAdapter extends RecyclerView.Adapter<CreateListRowHolder> {

    Integer qty = 0;

   // itemQty =
    //Integer[] itemPrice;

    private List<CreateBillFeedItem> feedItemList;

    private Context mContext;
    Integer[] itemQty;
    String[] itemId;
    private List<CreateBillFeedItem> mModels;



    public CreateBillRecyclerAdapter(Context context, List<CreateBillFeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        //feedItemList = new ArrayList<>(feedItemList);

        this.mContext = context;
        itemQty = new Integer[feedItemList.size()];
        itemId = new String[feedItemList.size()];
        mModels = new ArrayList<>(feedItemList);
}


    @Override
    public CreateListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_create_bill, viewGroup, false);
        CreateListRowHolder mh = new CreateListRowHolder(v);


        return mh;
    }

    @Override
    public void onBindViewHolder(final CreateListRowHolder createListRowHolder, int i) {
        final CreateBillFeedItem feedItem = mModels.get(i);
            final Integer pos = createListRowHolder.getAdapterPosition();
       // createListRowHolder.bind(feedItem);
      //  createListRowHolder.bind(feedItem);
/*
        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);
*/
        String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);

        if(itemQty[pos] == null) {
            //qty = 0;
            itemQty[pos] = 0;
        }

        itemId[pos] = feedItem.getMenuId();
       // Log.d("id",itemId[0]);


        createListRowHolder.itemName.setTypeface(tf);
        createListRowHolder.itemPrice.setTypeface(tf);

        createListRowHolder.itemName.setText(Html.fromHtml(feedItem.getName()));
        createListRowHolder.itemPrice.setText(Html.fromHtml(feedItem.getPrice()));

        Log.d("pos", pos.toString());
        createListRowHolder.qty_show.setText(Html.fromHtml(itemQty[pos].toString()));

        createListRowHolder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = itemQty[pos];
                qty++;
                itemQty[pos] = qty;
                createListRowHolder.qty_show.setText(Html.fromHtml(itemQty[pos].toString()));

            }
        });
        createListRowHolder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = itemQty[pos];
                qty--;
                if (qty < 0) {
                    qty = 0;
                }
                itemQty[pos] = qty;
                createListRowHolder.qty_show.setText(Html.fromHtml(itemQty[pos].toString()));

            }
        });



    }



    @Override
    public int getItemCount() {
        return (null != mModels ? mModels.size() : 0);
    }

    public void animateTo(List<CreateBillFeedItem> models) {
      //  Log.d("list",""+feedItemList.size());
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<CreateBillFeedItem> newModels) {
       // Log.d("list0",""+feedItemList.size());
       for (int i = mModels.size() - 1; i >= 0; i--) {
            final CreateBillFeedItem feedItem = mModels.get(i);
            if (!newModels.contains(feedItem)) {
                Log.d("list4","removeitem working");
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<CreateBillFeedItem> newModels) {
       // Log.d("list1",""+feedItemList.size());
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final  CreateBillFeedItem feedItem = newModels.get(i);
            if (!mModels.contains(feedItem)) {
                Log.d("list4","animateadditem working");
                addItem(i, feedItem);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<CreateBillFeedItem> newModels) {
       // Log.d("list2",""+feedItemList.size());
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final CreateBillFeedItem feedItem = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(feedItem);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                Log.d("list4","animatemoveditem working");
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public CreateBillFeedItem removeItem(int position) {
       // Log.d("list3",""+feedItemList.size());
        final CreateBillFeedItem feedItem = mModels.remove(position);
        notifyItemRemoved(position);
        return feedItem;
    }

    public void addItem(int position, CreateBillFeedItem feedItem) {
        //Log.d("list4",""+feedItemList.size());
        mModels.add(position, feedItem);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        //Log.d("list5",""+feedItemList.size());
        final CreateBillFeedItem feedItem = mModels.remove(fromPosition);
        Log.d("list7","move item working");
        mModels.add(toPosition, feedItem);
        notifyItemMoved(fromPosition, toPosition);
    }

}