package space.ankan.golocal.screens.chat;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import space.ankan.golocal.R;
import space.ankan.golocal.model.channels.ChatMessage;
import space.ankan.golocal.model.kitchens.Dish;

/**
 * Created by anurag on 18-Dec-15.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMessageListItemViewHolder> {

    private Context mContext;
    private ArrayList<ChatMessage> chats;
    private String userName;
    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a");

    public ChatAdapter(Context context, ArrayList<ChatMessage> list, String userName) {
        this.mContext = context;
        this.chats = list;
        this.userName = userName;
    }


    public ChatMessage getItem(int position) {
        return chats.get(position);
    }

    public ArrayList<ChatMessage> getItems() {
        return chats;
    }

    @Override
    public ChatAdapter.ChatMessageListItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_chat_message, parent, false);
        return new ChatAdapter.ChatMessageListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.ChatMessageListItemViewHolder holder, int i) {
        ChatMessage item = chats.get(i);

        holder.mMessage.setText(item.message);
        if (userName.equals(item.name)) {
            holder.mName.setText("Me:");
            holder.mIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_black));

        } else {
            holder.mName.setText(item.name);
            holder.mIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_blue));

        }
        holder.mTime.setText(SIMPLE_DATE_FORMAT.format(item.timeStamp).toUpperCase());

    }

    @Override
    public int getItemCount() {
        if (chats == null)
            return 0;
        return chats.size();
    }

    public int getCount() {
        return getItemCount();
    }

    public void add(ChatMessage message) {
        this.chats.add(message);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.chats.clear();
    }

    public void addAll(List<ChatMessage> messages) {
        if (!this.chats.isEmpty())
            return;
        this.chats.addAll(messages);
        this.notifyDataSetChanged();
    }

    public class ChatMessageListItemViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mName;
        public final TextView mMessage;
        public final TextView mTime;
        public final ImageView mIcon;

        public ChatMessageListItemViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.sender_name);
            mMessage = (TextView) view.findViewById(R.id.message);
            mTime = (TextView) view.findViewById(R.id.time);
            mIcon = (ImageView) view.findViewById(R.id.chat_icon);

        }
    }
}