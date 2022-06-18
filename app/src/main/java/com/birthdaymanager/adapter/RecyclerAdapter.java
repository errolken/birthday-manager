package com.birthdaymanager.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.birthdaymanager.core.EditBirthdayActivity;
import com.birthdaymanager.core.R;
import com.birthdaymanager.model.CardModel;
import com.birthdaymanager.util.DBUtils;

import java.util.List;
import java.util.Random;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<CardModel> birthdayList;
    private DBUtils db;
    private String bid;

    public RecyclerAdapter(List<CardModel> birthdayList, DBUtils db) {
        this.birthdayList = birthdayList;
        this.db = db;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        String day = birthdayList.get(position).getDay();
        String month = birthdayList.get(position).getMonth();
        String cardName = birthdayList.get(position).getCardName();
        String daysLeft = birthdayList.get(position).getDaysLeft();
        int clipImage = birthdayList.get(position).getClipImage();

        bid = birthdayList.get(position).getBid();

        holder.setData(bid, day, month, cardName, daysLeft, clipImage);
    }

    @Override
    public int getItemCount() {
        return birthdayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dayHolder;
        private TextView monthHolder;
        private TextView cardNameHolder;
        private Button cardBackgroundHolder;
        private TextView daysLeftHolder;
        private ImageButton clipImageHolder;
        private String bidHolder;

        private String[] birthdayMessages;

        private String fetchMessage(String name) {
            Random rand = new Random();
            birthdayMessages = new String[]{
                    "Count your life by smiles, not tears. Count your age by friends, not years. Happy birthday " + name + "!",
                    "Happy birthday " + name + "! I hope all your birthday wishes and dreams come true.",
                    "A wish for you on your birthday, whatever you ask may you receive, whatever you seek may you find, whatever you wish may it be fulfilled on your birthday and always. Happy birthday " + name + "!",
                    " Another adventure filled year awaits you. Welcome it by celebrating your birthday with pomp and splendor. Wishing you a very happy and fun-filled birthday " + name + "!",
                    "May the joy that you have spread in the past come back to you on this day. Wishing you a very happy birthday " + name + "!",
                    "Happy birthday! Your life is just about to pick up speed and blast off into the stratosphere. Wear a seat belt and be sure to enjoy the journey. Happy birthday " + name + "!",
                    "This birthday, I wish you abundant happiness and love. May all your dreams turn into reality and may lady luck visit your home today. Happy birthday " + name + "!",
                    "May you be gifted with life’s biggest joys and never-ending bliss. After all, you yourself are a gift to earth, so you deserve the best. Happy birthday " + name + "!",
                    "Count not the candles…see the lights they give. Count not the years, but the life you live. Wishing you a wonderful time ahead. Happy birthday " + name + "!",
                    "Forget the past; look forward to the future, for the best things are yet to come. Happy birthday " + name + "!",
                    "Your birthday is the first day of another 365-day journey. Be the shining thread in the beautiful tapestry of the world to make this year the best ever. Enjoy the ride " + name + "!",
                    "Be happy! Today is the day you were brought into this world to be a blessing and inspiration to the people around you! You are a wonderful person! May you be given more birthdays to fulfill all of your dreams. Happy birthday " + name + "!"
            };

            return birthdayMessages[rand.nextInt(12)];
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dayHolder = itemView.findViewById(R.id.day);
            monthHolder = itemView.findViewById(R.id.month);
            cardNameHolder = itemView.findViewById(R.id.card_name);
            clipImageHolder = itemView.findViewById(R.id.clip_image);
            daysLeftHolder = itemView.findViewById(R.id.days_left);
            cardBackgroundHolder = itemView.findViewById(R.id.card_bg);

            clipImageHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String dbName = birthdayList.get(position).getCardName();
                    String message = fetchMessage(dbName);
                    Log.d("message", message);

                    Context context = itemView.getContext();

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Birthday message", message);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(context, "Birthday message copied to clipboard", Toast.LENGTH_LONG).show();

//                    int position = getAdapterPosition();
//                    String dbBid = birthdayList.get(position).getBid();
//                    if (isUpdated) {
//                        if(smsImageHolder.isChecked()) {
//                            smsImageHolder.setChecked(true);
//                            Toast.makeText(v.getContext(), "SMS notification enabled", Toast.LENGTH_SHORT).show();
//                        } else {
//                            smsImageHolder.setChecked(false);
//                            Toast.makeText(v.getContext(), "SMS notification disabled", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(v.getContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
//                    }
                }
            });

            cardBackgroundHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int position = getAdapterPosition();
                    String dbBid = birthdayList.get(position).getBid();
                    Intent it = new Intent(context, EditBirthdayActivity.class);
                    Log.d("bid", dbBid);
                    it.putExtra("bid", dbBid);
                    context.startActivity(it);
                }
            });
        }

        public void setData(String bid, String day, String month, String cardName, String daysLeft, int clipImage) {
            bidHolder = bid;
            dayHolder.setText(day);
            monthHolder.setText(month);
            cardNameHolder.setText(cardName + "'s Birthday");
            daysLeftHolder.setText(daysLeft);
            clipImageHolder.setImageResource(clipImage);
        }
    }
}
