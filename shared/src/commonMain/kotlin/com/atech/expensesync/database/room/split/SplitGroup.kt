package com.atech.expensesync.database.room.split

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.atech.expensesync.database.room.split.SplitType.EQUAL
import com.atech.expensesync.database.room.split.SplitType.PERCENTAGE
import com.atech.expensesync.database.room.split.SplitType.UNEQUAL
import com.atech.expensesync.utils.convertToDateFormat
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Group member
 * This data class is used to represent the group member
 * @property uid The uid
 * @property name The name
 * @property email The email
 * @property pic The pic
 * @constructor Create empty Group member
 */
@Serializable
data class GroupMember(
    val uid: String,
    val name: String,
    val email: String,
    val pic: String,
)

/**
 * Split type
 * This enum class is used to represent the split type
 * @constructor Create empty Split type
 * @property EQUAL Equal
 * @property UNEQUAL Unequal
 * @property PERCENTAGE Percentage
 * @see SplitMoney
 */
enum class SplitType {
    EQUAL,
    UNEQUAL,
    PERCENTAGE,
}

/**
 * Split money
 * This data class is used to represent the split money
 * @property amount The amount
 * @property paidBy The paid by
 * @property description The description
 * @property splitType The split type
 * @property created The created
 * @constructor Create empty Split money
 * @see SplitType
 * @see SplitGroup
 */
@Serializable
data class SplitMoney(
    val amount: Double,
    val paidBy: GroupMember,
    val description: String,
    val splitType: SplitType,
    val created: Long = System.currentTimeMillis(),
) {
    @get:Ignore
    val formatedDate: String
        get() = created.convertToDateFormat()
}


/**
 * Split group
 * This data class is used to represent the split group, which contains the group members and split money
 * @property name The name
 * @property description The description
 * @property groupMembers The group members
 * @property splitMoney The split money
 * @property created The created
 * @property path The path
 * @constructor Create empty Split group
 * @see GroupMember
 * @see SplitMoney
 * @see SplitType
 */
@Entity(tableName = "split_group")
@Serializable
data class SplitGroup(
    val name: String,
    val type: String,
    val defaultSplitType: SplitType = EQUAL,
    val whiteBoard: String = "",
    val groupMembers: List<GroupMember> = emptyList(),
    val splitMoney: List<SplitMoney> = emptyList(),
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    val path: String,
) {
    @get:Ignore
    val formatedDate: String
        get() = created.convertToDateFormat()

    @get:Ignore
    val totalAmount: Double
        get() = splitMoney.sumOf { it.amount }
}

/**
 * Group member list type converter
 * This class is used to convert the group member list to string and vice versa
 * @constructor Create empty Group member list type converter
 * @see GroupMember
 */
class GroupMemberListTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<GroupMember> = Json.decodeFromString(value)

    @TypeConverter
    fun toString(value: List<GroupMember>): String = Json.encodeToString(value)
}

/**
 * Split money list type converter
 * This class is used to convert the split money list to string and vice versa
 * @constructor Create empty Split money list type converter
 * @see SplitMoney
 */
class SplitMoneyListTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<SplitMoney> = Json.decodeFromString(value)

    @TypeConverter
    fun toString(value: List<SplitMoney>): String = Json.encodeToString(value)
}