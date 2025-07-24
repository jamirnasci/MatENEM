package com.japps.matenem.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.japps.matenem.entities.Desempenho

class DataBase(context: Context): SQLiteOpenHelper(context, "mydb.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val sql: String = """
            CREATE TABLE desempenho(
                iddesempenho INTEGER PRIMARY KEY AUTOINCREMENT,
                data TEXT NOT NULL,
                quant_questoes INTEGER NOT NULL,
                aproveitamento REAL NOT NULL  
            );
        """.trimIndent()
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS desempenho")
        onCreate(db)
    }

    fun insertDesempenho(desempenho: Desempenho): Boolean{
        val db = writableDatabase
        val data = ContentValues().apply {
            put("data", desempenho.data)
            put("quant_questoes", desempenho.quant_questoes)
            put("aproveitamento", desempenho.aproveitamento)
        }
        val result = db.insert("desempenho", null, data)
        return result != -1L
    }
    fun listDesempenho(): List<Desempenho>{
        val lista = mutableListOf<Desempenho>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM desempenho", null)

        if(cursor.moveToFirst()){
            do {
                val iddesempenho: Int = cursor.getInt(cursor.getColumnIndexOrThrow("iddesempenho"))
                val data: String = cursor.getString(cursor.getColumnIndexOrThrow("data"))
                val quant_questoes: Int = cursor.getInt(cursor.getColumnIndexOrThrow("quant_questoes"))
                val aproveitamento: Float = cursor.getFloat(cursor.getColumnIndexOrThrow("aproveitamento"))
                lista.add(Desempenho(iddesempenho, data, quant_questoes, aproveitamento))
            }while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
    fun deleteDesempenho(id: Int): Boolean{
        val db = writableDatabase
        val affectedLines: Int = db.delete("desempenho", "iddesempenho = ?", arrayOf(id.toString()))
        return affectedLines > 0
    }
}