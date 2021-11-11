package br.com.listadecontatosmvvm.feature.listacontatos.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.listadecontatosmvvm.R
import br.com.listadecontatosmvvm.bases.BaseActivity
import br.com.listadecontatosmvvm.feature.contato.view.ContatoActivity
import br.com.listadecontatosmvvm.feature.listacontatos.adapter.ContatoAdapter
import br.com.listadecontatosmvvm.feature.listacontatos.viewmodel.ListaDeContatosViewModel
import br.com.listadecontatosmvvm.helpers.HelperDB
import kotlinx.android.synthetic.main.activity_main.*

class ListaDeContatosActivity : BaseActivity() {

    private var adapter: ContatoAdapter? = null

    var viewModel: ListaDeContatosViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Todo Existem frameworks para realizar as injeções de dependencias, no if abaixo, estamos fazendo de forma manual e nativa
//        if (viewModel == null)viewModel = ListaDeContatosViewModel(
//            ListaDeContatosRepository(
//                HelperDB(this)
//            )
//        )
        if(!intent.getBooleanExtra("isMock",false))viewModel = ListaDeContatosViewModel(
            HelperDB(this)
        )
        setupToolBar(toolBar, "Lista de contatos",false)
        setupListView()
        setupOnClicks()
    }

    private fun setupOnClicks(){
        fab.setOnClickListener { onClickAdd() }
        ivBuscar.setOnClickListener { onClickBuscar() }
    }

    private fun setupListView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        if(!intent.getBooleanExtra("isMock",false))onClickBuscar()
    }

    private fun onClickAdd(){
        val intent = Intent(this, ContatoActivity::class.java)
        startActivity(intent)
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this, ContatoActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    fun onClickBuscar(){
        val busca = etBuscar.text.toString()
        progress.visibility = View.VISIBLE
        viewModel?.getListaDeContatos(
            busca,
            onSucesso = { listaFiltrada ->
                runOnUiThread {
                    adapter = ContatoAdapter(this,listaFiltrada) {onClickItemRecyclerView(it)}
                    recyclerView.adapter = adapter
                    progress.visibility = View.GONE
                    Toast.makeText(this,"Buscando por $busca", Toast.LENGTH_SHORT).show()
                }
            }, onError = { ex ->
                runOnUiThread{
                    progress.visibility = View.GONE
                    AlertDialog.Builder(this)
                        .setTitle("Atenção")
                        .setMessage("Não foi possível completar sua solicitação")
                        .setPositiveButton("OK") { alert, i ->
                            alert.dismiss()
                        }.show()
                }
            }
        )
    }

}