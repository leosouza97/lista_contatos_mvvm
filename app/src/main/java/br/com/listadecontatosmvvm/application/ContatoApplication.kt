package br.com.listadecontatosmvvm.application

import android.app.Application
import br.com.listadecontatosmvvm.helpers.HelperDB

class ContatoApplication : Application() {

    var helperDB: HelperDB? = null
        private set

    companion object {
        lateinit var instance: ContatoApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }

}