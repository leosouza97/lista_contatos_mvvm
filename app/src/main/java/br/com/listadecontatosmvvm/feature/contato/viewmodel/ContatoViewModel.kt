package br.com.listadecontatosmvvm.feature.contato.viewmodel

import br.com.listadecontatosmvvm.feature.contato.repository.ContatoRepository
import br.com.listadecontatosmvvm.feature.listacontatos.model.ContatosVO
import br.com.listadecontatosmvvm.helpers.HelperDB

class ContatoViewModel(
    helperDB: HelperDB? = null,
    var repository: ContatoRepository? = ContatoRepository(helperDB)
) {

    fun getContato(
        idContato: Int,
        onSucesso: ((ContatosVO) -> Unit),
        onError: ((Exception) -> Unit)
    ) {
        Thread(Runnable {
            Thread.sleep(1500)
            repository?.getContato(
                idContato,
                onSucesso = { contato ->
                    if (contato == null) onError(Exception("Não foi possível completar sua solicitação"))
                    else onSucesso(contato)
                }, onError = {
                    onError(it)
                }
            )
        }).start()
    }

    fun saveContato(
        contatosVO: ContatosVO,
        isUpdate: Boolean,
        onSucesso: (() -> Unit),
        onError: ((Exception) -> Unit)
    ){
        Thread(Runnable {
            Thread.sleep(1500)
            if(isUpdate) {
                repository?.updateContato(
                    contatosVO,
                    onSucesso = {
                        onSucesso()
                    }, onError = {
                        onError(it)
                    }
                )
            }else{
                repository?.saveContato(
                    contatosVO,
                    onSucesso = {
                        onSucesso()
                    }, onError = {
                        onError(it)
                    }
                )
            }
        }).start()
    }

    fun deleteContato(
        idContato: Int,
        onSucesso: (() -> Unit),
        onError: ((Exception) -> Unit)
    ){
        Thread(Runnable {
            Thread.sleep(1500)
            repository?.deleteContato(
                idContato,
                onSucesso = {
                    onSucesso()
                }, onError = {
                    onError(it)
                }
            )
        }).start()
    }
}