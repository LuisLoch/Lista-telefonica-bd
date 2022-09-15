package br.unigran.listatelefonicabd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

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


    }
}