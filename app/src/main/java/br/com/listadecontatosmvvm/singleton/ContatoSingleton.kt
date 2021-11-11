package br.com.listadecontatosmvvm.singleton

import br.com.listadecontatosmvvm.feature.listacontatos.model.ContatosVO

object ContatoSingleton {
    var lista: MutableList<ContatosVO> = mutableListOf()
}