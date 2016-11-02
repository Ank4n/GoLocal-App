package space.ankan.golocal.screens.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import space.ankan.golocal.R;
import space.ankan.golocal.core.AppConstants;
import space.ankan.golocal.model.channels.ChatMessage;

/**
 * Created by Ankan.
 */

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMessageListItemViewHolder> {

    private Context mContext;
    private ArrayList<ChatMessage> chats;
    private String userName;
    private SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("H:mm dd/MM", Locale.US);

    ChatAdapter(Context context, ArrayList<ChatMessage> list, String userName) {
        this.mContext = context;
        this.chats = list;
        this.userName = userName;
    }


    @SuppressWarnings("unused")
    public ChatMessage getItem(int position) {
        return chats.get(position);
    }

    @SuppressWarnings("unused")
    public ArrayList<ChatMessage> getItems() {
        return chats;
    }

    @Override
    public ChatAdapter.ChatMessageListItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view;

        view = LayoutInflater.from(mContext)
                .inflate(R.layout.chat_item, parent, false);

        return new ChatAdapter.ChatMessageListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.ChatMessageListItemViewHolder holder, int i) {
        ChatMessage item = chats.get(i);

        //outgoing message
        if (userName.equalsIgnoreCase(item.name)) {
            holder.incomingView.setVisibility(View.GONE);
            holder.outgoingView.setVisibility(View.VISIBLE);

            if (item.message.startsWith(AppConstants.FIREBASE_STORAGE_BASE_URL)) {
                holder.mMessageOut.setVisibility(View.GONE);
                holder.mImageOut.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(item.message).into(holder.mImageOut);
            } else
                holder.mMessageOut.setText(item.message);

            if (holder.mName != null)
                holder.mName.setText(item.name);

            holder.mTimeOut.setText(SIMPLE_DATE_FORMAT.format(item.timeStamp).toUpperCase());
        } else {
            holder.outgoingView.setVisibility(View.GONE);
            holder.incomingView.setVisibility(View.VISIBLE);

            if (item.message.startsWith(AppConstants.FIREBASE_STORAGE_BASE_URL)) {
                holder.mMessageIn.setVisibility(View.GONE);
                holder.mImageIn.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(item.message).into(holder.mImageIn);
            } else
                holder.mMessageIn.setText(item.message);

            if (holder.mName != null)
                holder.mName.setText(item.name);
            holder.mTimeIn.setText(SIMPLE_DATE_FORMAT.format(item.timeStamp).toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        if (chats == null)
            return 0;
        return chats.size();
    }

    int getCount() {
        return getItemCount();
    }

    public void add(ChatMessage message) {
        this.chats.add(message);
        this.notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void clear() {
        this.chats.clear();
    }

    @SuppressWarnings("unused")
    public void addAll(List<ChatMessage> messages) {
        this.chats.addAll(messages);
        this.notifyDataSetChanged();
    }

    class ChatMessageListItemViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final View incomingView;
        final View outgoingView;

        final TextView mName;
        final TextView mMessageIn;
        final TextView mTimeIn;
        final TextView mMessageOut;
        final TextView mTimeOut;
        final ImageView mImageOut;
        final ImageView mImageIn;


        ChatMessageListItemViewHolder(View view) {
            super(view);
            mView = view;
            incomingView = view.findViewById(R.id.chat_incoming);
            outgoingView = view.findViewById(R.id.chat_outgoing);
            mName = (TextView) view.findViewById(R.id.sender_name);
            mMessageIn = (TextView) view.findViewById(R.id.message_in);
            mImageIn = (ImageView) view.findViewById(R.id.image_in);
            mTimeIn = (TextView) view.findViewById(R.id.time_in);
            mMessageOut = (TextView) view.findViewById(R.id.message_out);
            mTimeOut = (TextView) view.findViewById(R.id.time_out);
            mImageOut = (ImageView) view.findViewById(R.id.image_out);

        }
    }
}