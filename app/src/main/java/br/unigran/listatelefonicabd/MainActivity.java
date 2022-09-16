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
                contato = dados.get(i);
                nome.setText(contato.getNome());
                telefone.setText(contato.getTelefone());
                dataNasc.setText(contato.getDataNasc());
                contatoDB.atualizar(lista);
            }
        });

        //ação de click longo, para remover o contato
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Deseja remover este contato?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                contatoDB.remover(dados.get(i).getId());
                                contatoDB.listar(dados);
                                contatoDB.atualizar(lista);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
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
        contatoDB.inserir(contato);

        //atualiza lista
        contatoDB.atualizar(lista);//pode dar erro, pois foi alterado o método atualizar()

        //limpa os dados de contato
        contato = null;

        //texto de aviso de que contato foi salvo
        Toast.makeText(this, "Contato salvo com sucesso!", Toast.LENGTH_SHORT).show();
    }

    //função para limpar os campos de entrada de dados caso seja pressionado o botão voltar
    private void limparCampos(){
        nome.setText("");//pode dar erro por ser null, substituir por: ""
        telefone.setText("");
        dataNasc.setText("");
        Toast.makeText(this, "Edição cancelada!", Toast.LENGTH_SHORT).show();
    }

    //função para limpar os campos de dados na edição, caso o botão de voltar seja pressionado
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        limparCampos();
    }
}