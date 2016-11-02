package space.ankan.golocal.screens.chat;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import space.ankan.golocal.R;
import space.ankan.golocal.core.TwoPaneListener;
import space.ankan.golocal.model.channels.Channel;

/**
 * Created by ankan.
 * Adapter class for the channel list recycler view
 */
class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

    private final List<Channel> mValues;
    private Context mContext;
    private TwoPaneListener mTwoPaneListener;
    private int selected = -1;
    private Handler handler;

    ChannelsAdapter(List<Channel> items, Context c) {
        mValues = items;
        mContext = c;
        handler = new Handler();
    }

    void setTwoPaneListener(TwoPaneListener twoPaneListener) {
        mTwoPaneListener = twoPaneListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final Channel channel = mValues.get(position);
        holder.mName.setText(channel.name);
        holder.mLastMessage.setText(channel.lastMessage);
        holder.mTimeStamp.setText(DateUtils.getRelativeTimeSpanString(channel.timeStamp));
        if (channel.imageUrl != null)
            Picasso.with(mContext).load(channel.imageUrl).into(holder.mUserImage);
        else
            holder.mUserImage.setAlpha(0.6f);

        int bgColor = R.color.white;
        if (holder.mItem.isSelected)
            bgColor = R.color.item_selected_background;

        holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, bgColor));
        final int index = position;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String id = auth.getCurrentUser().getUid().equals(channel.userId1) ? channel.userId2 : channel.userId1;
                if (mTwoPaneListener != null && mTwoPaneListener.isTwoPane()) {
                    mTwoPaneListener.setupChatDetail(channel.channelId, channel.name, id);
                    configureSelection(index);
                } else
                    ChatActivity.createIntent(mContext, channel.channelId, channel.name, id);
            }
        });
    }

    private void configureSelection(int position) {
        if (selected != -1)
            mValues.get(selected).isSelected = false;
        selected = position;
        mValues.get(selected).isSelected = true;
        notifyDataSetChanged();
    }

    public void add(Channel channel) {
        mValues.add(channel);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Collections.sort(mValues);
                notifyDataSetChanged();
            }
        });

    }

    public void modify(Channel channel, String key) {
        for (Channel c : mValues) {
            if (c.channelId == null) continue;
            if (c.channelId.equals(key)) {
                c.lastMessage = channel.lastMessage;
                c.timeStamp = channel.timeStamp;
                break;
            }

        }
        Collections.sort(mValues);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mName;
        final TextView mLastMessage;
        final TextView mTimeStamp;
        final ImageView mUserImage;
        Channel mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.name);
            mLastMessage = (TextView) view.findViewById(R.id.last_message);
            mTimeStamp = (TextView) view.findViewById(R.id.time_in);
            mUserImage = (ImageView) view.findViewById(R.id.user_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
