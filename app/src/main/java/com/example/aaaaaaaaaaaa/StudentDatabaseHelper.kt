package com.example.aaaaaaaaaaaa

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Student(
    val id: Int,
    val name: String,
    val age: Int
)

class StudentDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "student.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    age INTEGER NOT NULL
                )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS students")
        onCreate(db)
    }

    fun addStudent(name: String, age: Int) {
        val values = ContentValues().apply {
            put("name", name)
            put("age", age)
        }

        writableDatabase.insert("students", null, values)
    }

    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()

        val cursor = readableDatabase.rawQuery(
            "SELECT id, name, age FROM students ORDER BY ID DESC",
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                val student = Student(
                    id = it.getInt(it.getColumnIndexOrThrow("id")),
                    name = it.getString(it.getColumnIndexOrThrow("name")),
                    age = it.getInt(it.getColumnIndexOrThrow("age"))
                )

                students.add(student)
            }
        }
        return students
    }

    fun updateStudent(id: Int, name: String, age: Int) {
        val values = ContentValues().apply {
            put("name", name)
            put("age", age)
        }

        writableDatabase.update(
            "students",
            values, "id = ?", arrayOf(id.toString())
        )
    }

    fun deleteStudent(id: Int) {
        writableDatabase.delete(
            "students", "id = ?", arrayOf(id.toString())
        )
    }
}