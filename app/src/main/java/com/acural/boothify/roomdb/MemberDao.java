package com.acural.boothify.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.acural.boothify.model.MemberEntity;

import java.util.List;

@Dao
public interface MemberDao {

    @Insert
    void insert(MemberEntity member);

    @Query("SELECT * FROM members")
    List<MemberEntity> getAllMembers();

    @Delete
    void delete(MemberEntity member);
}