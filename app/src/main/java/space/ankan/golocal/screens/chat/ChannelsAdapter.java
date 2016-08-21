package space.ankan.golocal.screens.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import space.ankan.golocal.R;
import space.ankan.golocal.model.channels.Channel;

import java.util.Collections;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

    private final List<Channel> mValues;
    Context mContext;

    public ChannelsAdapter(List<Channel> items, Context c) {
        mValues = items;
        mContext = c;
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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String id = auth.getCurrentUser().getUid().equals(channel.userId1) ? channel.userId2 : channel.userId1;
                ChatActivity.createIntent(mContext, channel.channelId, channel.name, id);
            }
        });
    }

    public void add(Channel channel) {
        mValues.add(channel);
        //FIXME this is going to be really slow
        Collections.sort(mValues);
        notifyDataSetChanged();
    }

    public void modify(Channel channel, String key) {
        for (Channel c : mValues) {
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
