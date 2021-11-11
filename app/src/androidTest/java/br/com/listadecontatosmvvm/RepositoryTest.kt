package br.com.listadecontatosmvvm

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.listadecontatosmvvm.feature.listacontatos.model.ContatosVO
import br.com.listadecontatosmvvm.feature.listacontatos.repository.ListaDeContatosRepository
import br.com.listadecontatosmvvm.helpers.HelperDB

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    lateinit var repository: ListaDeContatosRepository

    @Before
    fun setUp(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        repository = ListaDeContatosRepository(
            HelperDB(appContext)
        )
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("br.com.listadecontatosmvvm", appContext.packageName)
    }

    @Test
    fun repositoryTest(){
        var lista: List<ContatosVO>? = null
        var lock = CountDownLatch(1)
        repository?.requestListaDeContatos(
            "teste",
            onSucesso = { listaResult ->
                lista = listaResult
                lock.countDown()
            }, onError = {
                fail("Não deveria ter retornado erro")
            }
        )
        lock.await(3000,TimeUnit.MILLISECONDS)
        assertNotNull(lista)
        assertTrue(lista!!.isNotEmpty())
        assertEquals(2, lista!!.size)
    }

    @Test
    fun testViewModel() {
        val lock = CountDownLatch(1);
        var lista: List<ContatosVO>? = null
        repository?.requestListaDeContatos(
            "teste",
            onSucesso = { list ->
                lista = list
                lock.countDown()
            },
            onError = {
                fail("Retornou excessão do repositorio!")
            }
        )
        lock.await(2000, TimeUnit.MILLISECONDS)
        assertNotNull(lista)
        assertFalse(lista!!.isEmpty())
        assertEquals(lista!!.size,2)
    }
}