package br.com.listadecontatosmvvm.feature.listacontatos.viewmodel

import br.com.listadecontatosmvvm.application.ContatoApplication
import br.com.listadecontatosmvvm.feature.listacontatos.model.ContatosVO
import br.com.listadecontatosmvvm.feature.listacontatos.repository.ListaDeContatosRepository
import br.com.listadecontatosmvvm.helpers.HelperDB
import java.lang.Exception

open class ListaDeContatosViewModel(
    var helperDB: HelperDB? = null,
    var repository: ListaDeContatosRepository? = ListaDeContatosRepository(helperDB)
) {


    fun getListaDeContatos(
        busca: String,
        onSucesso: ((List<ContatosVO>)->Unit),
        onError: ((Exception)->Unit)
    ){
        Thread(Runnable {
            Thread.sleep(1500)
            repository?.requestListaDeContatos(
                busca,
                onSucesso = {lista ->
                    onSucesso(lista)
                }, onError = { ex ->
                    onError(ex)
                }
            )
        }).start()
    }

}