package com.example.shubham.bille;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by SHUBHAM on 05-11-2015.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {


    private List<FeedItem> feedItemList;

    private Context mContext;

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup,false);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);

/*
        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);
*/

        feedListRowHolder.customerName.setText(Html.fromHtml(feedItem.getTitle()));
        feedListRowHolder.billAmount.setText(Html.fromHtml(feedItem.getPrice()));


        final String a,b;
        a = feedItem.getTitle();
        b = feedItem.getPrice();



        feedListRowHolder.setClickListener(new FeedListRowHolder.ClickListener(){
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                    // View v at position pos is long-clicked.
                } else {

                        Intent billdes = new Intent(mContext,BillDescription.class);
                        billdes.putExtra("cusname",a);
                        billdes.putExtra("billamt",b);
                        mContext.startActivity(billdes);

                    // View v at position pos is clicked.
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }



}