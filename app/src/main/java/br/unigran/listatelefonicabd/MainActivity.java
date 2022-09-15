package br.unigran.listatelefonicabd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //criação de conexão
        db = new DBHelper(this);

        //mapeamento dos campos da tela
        nome = findViewById(idNome);
        telefone = findViewById(idTelefone);
        dataNasc = findViewById(idDataNasc);
        lista = findViewById(idLista)

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
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Deseja remover o contato?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                contatoDB.remover(dados.get(i).getId());
                                contatoDB.listar(dados);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create().show();
            }
        });
    }
}