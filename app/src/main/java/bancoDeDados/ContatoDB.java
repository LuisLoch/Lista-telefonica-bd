package bancoDeDados;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import java.util.List;//possive erro

import br.unigran.listatelefonicabd.Contato;

public class ContatoDB {
    private DBHelper db;
    private SQLiteDatabase conexao;

    public ContatoDB(DBHelper db){ this.db = db; }

    public void inserir(Contato contato){
        conexao = db.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", contato.getNome());
        valores.put("telefone", contato.getTelefone());
        valores.put("dataNasc", contato.getDataNasc());
        if(contato.getId()>0)
            conexao.update("telefones", valores, "id=?", new String[]{contato.getId().toString()});
        else
            conexao.insertOrThrow("telefones", null, valores);
        conexao.close();
    }

    public void remover(int id){
        conexao = db.getWritableDatabase();
        conexao.delete("telefones", "id=?", new String[]{id+""});
    }

    public void atualizar(ListView lista){ lista.invalidateViews(); }

    public void listar(List dados){
        dados.clear();
        conexao = db.getReadableDatabase();
        String names[] = {"id", "nome", "telefone", "dataNasc"};
        Cursor query = conexao.query("telefones", names, null, null, null, null, "id");

        while (query.moveToNext()){
            Contato contato = new Contato();
            contato.setId(Integer.parseInt(query.getString(0)));
            contato.setNome(query.getString(1));
            contato.setTelefone(query.getString(2));
            contato.setDataNasc(query.getString(3));
            dados.add(contato);
        }

        conexao.close();
    }
}
