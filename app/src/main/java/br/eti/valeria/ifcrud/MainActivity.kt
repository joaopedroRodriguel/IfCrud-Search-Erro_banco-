package br.eti.valeria.ifcrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var listView: ListView
    private lateinit var dao: PessoaDAO
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.dao = PessoaDAO(this)

        this.button = findViewById(R.id.button)
        this.editText = findViewById(R.id.editText)
        this.listView = findViewById(R.id.listView)
        this.searchView = findViewById(R.id.serachView)

        this.atualiza()

        // Não tô sabendo trazer a lista com todas as pessoas do banco e jogar dentro de uma váriavel.



        this.listView.setOnItemClickListener(OnItemClick())
        this.listView.setOnItemLongClickListener(OnItemLongClick())

        this.button.setOnClickListener { salvar() }
        this.searchView.setOnClickListener{ buscar() }
    }

    fun buscar() {

        val adapter: ArrayAdapter<Pessoa> = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.dao.buscarPessoaPorNome())
        this.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                val pessoas : // Falta só fazer como a consulta BuscarPessoaPorNome que eu criei vim em forma de lista de string para jogar dentro da váriavel pessoas.
                if(pessoas.contains(p0))
                {
                    adapter.filter.filter(p0)
                }else{
                    Toast.makeText(applicationContext,"Pessoa Não encontrada",Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return false
            }
        }

        )

    }

    fun salvar(){
        val nome = this.editText.text.toString()
        val pessoa = Pessoa(nome)
        this.dao.insert(pessoa)
        this.atualiza()
        this.editText.setText("")
    }

    fun atualiza(){
        val layout = android.R.layout.simple_list_item_1
        this.listView.adapter = ArrayAdapter<Pessoa>(this, layout,this.dao.read())
    }

    inner class OnItemClick: AdapterView.OnItemClickListener{
        override fun onItemClick(adapter: AdapterView<*>?, view: View?, index: Int, id: Long) {
            val pessoa = adapter?.getItemAtPosition(index) as Pessoa
            Toast.makeText(this@MainActivity, pessoa?.nome, Toast.LENGTH_SHORT).show()
        }
    }

    inner class OnItemLongClick: AdapterView.OnItemLongClickListener{
        override fun onItemLongClick(adapter: AdapterView<*>?, view: View?, index: Int, id: Long): Boolean {
            val pessoa = adapter?.getItemAtPosition(index) as Pessoa
            this@MainActivity.dao.delete(pessoa)
            val msg = "${pessoa.nome} removido com sucesso!"
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            this@MainActivity.atualiza()
            return true
        }
    }
}