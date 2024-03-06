package cz.mendelu.pef.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(var text: String){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null


}
