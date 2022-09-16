package br.unigran.listatelefonicabd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bancoDeDados.ContatoDB;
import bancoDeDados.DBHelper;

public class MainActivity extends AppCompatActivity {
    EditText nome, telefone, dataNasc;
    List<Contato> dados;
    ListView lista;
    DBHelper db;
    ContatoDB contatoDB;
    Contato contato;
    boolean editarCancelado = false; //variável que garante que o editar não seja efetuado depois do usuário clicar o botão de voltar para cancelar edição
    boolean longClickAtivo = false; //variável que garante que o onItemLongClick e onItemClick não sejam ativados ao mesmo tempo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //criação de conexão
        db = new DBHelper(this);

        //mapeamento dos campos da tela
        nome = findViewById(R.id.idNome);
        telefone = findViewById(R.id.idTelefone);
        dataNasc = findViewById(R.id.idDataNasc);
        lista = findViewById(R.id.idLista);

        //alocação de lista
        dados = new ArrayList();

        //criação e vinculamento de adapter de lista
        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dados);
        lista.setAdapter(adapter);

        contatoDB = new ContatoDB(db);

        //listagem inicial
        contatoDB.listar(dados);

        acoes();
    }

    private void acoes() {
        //ação de click normal, para editar o contato
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!longClickAtivo){
                    editarCancelado = false; //caso o usuário clique no item da lista, editar cancelado se torna false, permitindo a edição do contato clicado
                    contato = dados.get(i);
                    nome.setText(contato.getNome());
                    telefone.setText(contato.getTelefone());
                    dataNasc.setText(contato.getDataNasc());
                    contatoDB.atualizar(lista);
                }
            }
        });

        //ação de click longo, para remover o contato
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longClickAtivo = true;
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Deseja remover este contato?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                contatoDB.remover(dados.get(i).getId());
                                contatoDB.listar(dados);
                                contatoDB.atualizar(lista);
                                longClickAtivo = false;
                                Toast.makeText(getApplicationContext(), "Contato removido!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                longClickAtivo = false;
                            }
                        })
                        .create().show();
                return false;
            }
        });
    }

    //função para o botão de salvar na interface
    public void salvar(View view){
        //se o contato estiver como nulo, cria novo contato sem nenhum atributo definido
        if(contato==null) contato = new Contato();

        //preenche os dados de contato com os dados dos campos de texto
        contato.setNome(nome.getText().toString());
        contato.setTelefone(telefone.getText().toString());
        contato.setDataNasc(dataNasc.getText().toString());

        //insere e salva o contato no banco de dados do contato
        contatoDB.inserir(contato, editarCancelado);

        //apresenta lista após inserção de dados
        contatoDB.listar(dados);

        //atualiza lista
        contatoDB.atualizar(lista);//pode dar erro, pois foi alterado o método atualizar()

        //limpa os dados de contato
        contato = null;

        //limpa os campos de entrada de dados
        limparCampos();

        //texto de aviso de que contato foi salvo
        Toast.makeText(this, "Contato salvo com sucesso!", Toast.LENGTH_SHORT).show();
    }

    //função para limpar os campos de entrada de dados caso seja pressionado o botão voltar
    private void limparCampos(){
        nome.setText(null);//pode dar erro por ser null, substituir por: ""
        telefone.setText(null);
        dataNasc.setText(null);
    }

    //função para limpar os campos de dados na edição, caso o botão de voltar seja pressionado
    @Override
    public void onBackPressed(){
        editarCancelado = true; //caso o usuário clique no botão voltar, editar cancelado se torna true, bloqueando a edição co contato
        limparCampos();
        Toast.makeText(this, "Edição cancelada!", Toast.LENGTH_SHORT).show();
    }
}