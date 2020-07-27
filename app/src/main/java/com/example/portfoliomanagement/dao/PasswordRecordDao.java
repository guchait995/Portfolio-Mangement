package com.example.portfoliomanagement.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.portfoliomanagement.model.InvestmentStatement;
import com.example.portfoliomanagement.model.PasswordRecord;

import java.util.List;

@Dao
public
interface PasswordRecordDao {

    @Query("SELECT * FROM password_record order by application asc")
    List<PasswordRecord> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PasswordRecord> passwordRecords);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(PasswordRecord passwordRecord);

    @Delete
    void delete(PasswordRecord passwordRecord);
}
