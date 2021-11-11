package br.com.listadecontatosmvvm

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.listadecontatosmvvm.feature.listacontatos.model.ContatosVO
import br.com.listadecontatosmvvm.feature.listacontatos.repository.ListaDeContatosRepository
import br.com.listadecontatosmvvm.feature.listacontatos.viewmodel.ListaDeContatosViewModel
import br.com.listadecontatosmvvm.helpers.HelperDB

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ViewModelTest {

    lateinit var repositoryMock: ListaDeContatosRepository
    var viewModel: ListaDeContatosViewModel? = null

    @Before
    fun setUp() {
    }

    fun setupMockOnSucesso(){
        repositoryMock = Mockito.mock(ListaDeContatosRepository::class.java)
        Mockito.`when`(
            repositoryMock.requestListaDeContatos(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
            )
        ).thenAnswer {
            val onSucesso = it.arguments[1] as ((List<ContatosVO>)->Unit)
            var list: MutableList<ContatosVO> = mutableListOf()
            list.add(ContatosVO(1,"Teste 1","123456"))
            list.add(ContatosVO(1,"Teste 2","123456"))
            list.add(ContatosVO(1,"Teste 3","123456"))
            onSucesso(list)
            ""
        }
        viewModel = ListaDeContatosViewModel(
            repository = repositoryMock
        )
    }

    fun setupMockOnError(){
        repositoryMock = Mockito.mock(ListaDeContatosRepository::class.java)
        Mockito.`when`(
            repositoryMock.requestListaDeContatos(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
            )
        ).thenAnswer {
            val onError = it.arguments[2] as ((Exception)->Unit)
            onError(Exception("Erro"))
            ""
        }
        viewModel = ListaDeContatosViewModel(
            repository = repositoryMock
        )
    }

    @Test
    fun testViewModelOnError() {
        setupMockOnError()
        val lock = CountDownLatch(1);
        var exception: Exception? = null
        viewModel?.getListaDeContatos(
            "teste",
            onSucesso = { list ->
                fail("Caiu no onSucesso")
                lock.countDown()
            },
            onError = {
                exception = it
                lock.countDown()
            }
        )
        lock.await(200000, TimeUnit.MILLISECONDS)
        assertNotNull(exception)
    }

    @Test
    fun testViewModelOnSucesso() {
        setupMockOnSucesso()
        val lock = CountDownLatch(1);
        var lista: List<ContatosVO>? = null
        viewModel?.getListaDeContatos(
            "teste",
            onSucesso = { list ->
                lista = list
                lock.countDown()
            },
            onError = {
                fail("Retornou excessão do repositorio!")
            }
        )
        lock.await(200000, TimeUnit.MILLISECONDS)
        assertNotNull(lista)
        assertFalse(lista!!.isEmpty())
        assertEquals(lista!!.size,3)
    }
}