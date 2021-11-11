package br.com.listadecontatosmvvm.feature.listacontatos.repository

import android.annotation.SuppressLint
import br.com.listadecontatosmvvm.bases.BaseRepository
import br.com.listadecontatosmvvm.feature.listacontatos.model.ContatosVO
import br.com.listadecontatosmvvm.helpers.HelperDB
import br.com.listadecontatosmvvm.helpers.HelperDB.Companion.COLUMNS_ID
import br.com.listadecontatosmvvm.helpers.HelperDB.Companion.COLUMNS_NOME
import br.com.listadecontatosmvvm.helpers.HelperDB.Companion.COLUMNS_TELEFONE
import br.com.listadecontatosmvvm.helpers.HelperDB.Companion.TABLE_NAME
import java.lang.Exception
import java.sql.SQLDataException

open class ListaDeContatosRepository(
    helperDBPar: HelperDB? = null
) : BaseRepository(helperDBPar) {

    @SuppressLint("Range")
    open fun requestListaDeContatos(
        busca: String,
        onSucesso: ((List<ContatosVO>) -> Unit),
        onError: ((Exception) -> Unit)
    ) {
        try {
            val db = readableDatabase
            var lista = mutableListOf<ContatosVO>()
            var where: String? = null
            var args: Array<String> = arrayOf()
            where = "$COLUMNS_NOME LIKE ?"
            args = arrayOf("%$busca%")

            var cursor = db?.query(TABLE_NAME, null, where, args, null, null, null)
            if (cursor == null) {
                db?.close()
                onError(SQLDataException("Não foi possível fazer a query"))
                return
            }
            while (cursor.moveToNext()) {
                var contato = ContatosVO(
                    cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                    cursor.getString(cursor.getColumnIndex(COLUMNS_TELEFONE))
                )
                lista.add(contato)
            }
            db?.close()
            onSucesso(lista)
        //TODO Não deixar Exception genêrico em erros de BD igual abaixo, Exception para fins de estudos rápidos
        }catch (ex: Exception){
            onError(ex)
        }
    }


}