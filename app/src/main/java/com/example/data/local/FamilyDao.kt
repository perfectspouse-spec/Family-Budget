package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FamilyMember
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyDao {
    @Query("SELECT * FROM family_members ORDER BY id ASC")
    fun getAllMembers(): Flow<List<FamilyMember>>

    @Query("SELECT * FROM family_members WHERE id = :id LIMIT 1")
    suspend fun getMemberById(id: Long): FamilyMember?

    @Query("SELECT * FROM family_members WHERE isCurrentUser = 1 LIMIT 1")
    fun getCurrentUser(): Flow<FamilyMember?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: FamilyMember): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<FamilyMember>)

    @Update
    suspend fun updateMember(member: FamilyMember)

    @Delete
    suspend fun deleteMember(member: FamilyMember)

    @Query("UPDATE family_members SET isCurrentUser = 0")
    suspend fun clearCurrentUser()

    @Query("UPDATE family_members SET isCurrentUser = 1 WHERE id = :id")
    suspend fun setCurrentUser(id: Long)

    @Query("UPDATE family_members SET isApproved = :approved WHERE id = :id")
    suspend fun setMemberApproved(id: Long, approved: Boolean)

    @Query("SELECT COUNT(*) FROM family_members")
    suspend fun getMemberCount(): Int

    @Query("DELETE FROM family_members")
    suspend fun deleteAllMembers()
}
