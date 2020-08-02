package com.example.portfoliomanagement.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.Update;

import com.example.portfoliomanagement.model.InvestmentStatement;

import java.util.List;

@Dao
public
interface InvestmentStatementDao {
    @Query("SELECT * FROM investment_statement order by investment_date desc")
    List<InvestmentStatement> getAllDateDESC();

    @Query("SELECT * FROM investment_statement order by investment_date asc")
    List<InvestmentStatement> getAllDateASC();

    @Query("SELECT * FROM investment_statement where investment_date between :dateStr AND :dateEnd order by investment_date desc")
    List<InvestmentStatement> getAllBetweenStartAndEndDate(long dateStr, long dateEnd);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InvestmentStatement> investmentStatements);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(InvestmentStatement investmentStatement);

    @Delete
    void delete(InvestmentStatement investmentStatement);
}
