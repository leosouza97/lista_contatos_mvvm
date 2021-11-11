package br.com.listadecontatosmvvm.bases

import android.database.sqlite.SQLiteDatabase
import br.com.listadecontatosmvvm.helpers.HelperDB

open class BaseRepository(
    val helperDB: HelperDB? = null
) {

    val readableDatabase: SQLiteDatabase?
        get() {
            return helperDB?.readableDatabase
        }
    val writableDatabase: SQLiteDatabase?
        get() {
            return helperDB?.writableDatabase
        }
}