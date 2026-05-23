//package com.acural.boothify.roomdb;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import com.acural.boothify.model.MemberEntity;
//
//import java.util.List;
//
//@Dao
//public interface MemberDao {
//
//    @Insert
//    void insert(MemberEntity member);
//
//    @Query("SELECT * FROM members ORDER BY id DESC")
//    List<MemberEntity> getAllMembers();
//
//    @Delete
//    void delete(MemberEntity member);
//
//    @Update
//    void update(MemberEntity member);
//
//    @Query("SELECT COUNT(*) FROM members WHERE memberId = :memberId")
//    int checkMemberId(String memberId);
//
//    @Query("SELECT COUNT(*) FROM members")
//    int getTotalCount();
//
//    @Query("SELECT COUNT(*) FROM members WHERE createdDateTime = date('now','localtime')")
//    int getTodayCount();
//
//
//    @Query("SELECT COUNT(*) FROM members WHERE " +
//            "strftime('%Y-%m', createdDateTime) = " +
//            "strftime('%Y-%m', 'now','localtime')")
//    int getThisMonthCount();
//
//}

package com.acural.boothify.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.acural.boothify.model.MemberEntity;

import java.util.List;

@Dao
public interface MemberDao {

    @Insert
    void insert(MemberEntity member);

    @Query("SELECT * FROM members ORDER BY id DESC")
    List<MemberEntity> getAllMembers();

    @Delete
    void delete(MemberEntity member);

    @Update
    void update(MemberEntity member);

    @Query("SELECT COUNT(*) FROM members WHERE memberId = :memberId")
    int checkMemberId(String memberId);

    @Query("SELECT COUNT(*) FROM members")
    int getTotalCount();

    // ✅ Date part match karo (datetime string ke pehle 10 chars)
    @Query("SELECT COUNT(*) FROM members WHERE substr(createdDateTime,1,10) = date('now','localtime')")
    int getTodayCount();

    @Query("SELECT COUNT(*) FROM members WHERE " +
            "substr(createdDateTime,1,7) = strftime('%Y-%m','now','localtime')")
    int getThisMonthCount();

    // ✅ Existing members ki list
    @Query("SELECT * FROM members WHERE isExistingMember = 1 ORDER BY id DESC")
    List<MemberEntity> getExistingMembers();

    // ✅ New members ki list
    @Query("SELECT * FROM members WHERE isExistingMember = 0 ORDER BY id DESC")
    List<MemberEntity> getNewMembers();
}