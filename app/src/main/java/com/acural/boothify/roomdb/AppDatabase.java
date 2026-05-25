//package com.acural.boothify.roomdb;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import com.acural.boothify.model.MemberEntity;
//
//@Database(entities = {MemberEntity.class}, version = 3)
//public abstract class AppDatabase extends RoomDatabase {
//
//    public abstract MemberDao memberDao();
//
//    private static AppDatabase instance;
//
//    public static synchronized AppDatabase getInstance(Context context){
//
//        if(instance == null){
//
//            instance = Room.databaseBuilder(
//                            context.getApplicationContext(),
//                            AppDatabase.class,
//                            "member_database"
//                    )
//                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
//                    .build();
//        }
//
//        return instance;
//    }
//}

package com.acural.boothify.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.acural.boothify.model.MemberEntity;

@Database(entities = {MemberEntity.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MemberDao memberDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "member_database"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}