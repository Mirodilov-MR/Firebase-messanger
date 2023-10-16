package com.example.firebase_messanger.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
     fun getUserById(userId: String): UserEntity

    @Query("SELECT * FROM users")
     fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE fullName LIKE :query")
     fun searchUsers(query: String): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertUsers(users: List<UserEntity>)

    @Query("DELETE FROM users")
     fun deleteAllUsers()
}

