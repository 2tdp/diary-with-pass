package com.note.mydiary.diarywithlock.journalwithlock.data;

import android.content.Context;

import com.note.mydiary.diarywithlock.journalwithlock.model.EmojiModel;

import java.io.IOException;
import java.util.ArrayList;

public class DataEmoji {

    public static ArrayList<EmojiModel> getEmoji(Context context) {
        ArrayList<EmojiModel> lstEmoji = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("emoji");
            for (String s : f) lstEmoji.add(new EmojiModel(-1, s, "emoji", false));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstEmoji;
    }
}
