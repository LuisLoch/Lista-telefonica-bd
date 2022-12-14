package bancoDeDados;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;//possivel erro
import java.util.Locale;

import br.unigran.listatelefonicabd.Contato;

public class ContatoDB {
    private DBHelper db;
    private SQLiteDatabase conexao;

    //Construtor da classe
    public ContatoDB(DBHelper db){ this.db = db; }

    //Método inserir, para adicionar um novo contato ou editar um já existente selecionado na lista da interface principal
    public int inserir(Contato contato, boolean editarCancelado){
        conexao = db.getWritableDatabase();

        SimpleDateFormat formatoData = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);

        if(contato.getNome().isEmpty() || contato.getNome()==null || contato.getTelefone().isEmpty() || contato.getNome()==null)
            return 1;
        else{
            if(comparaString(contato.getDataNasc())){
                ContentValues valores = new ContentValues();
                valores.put("nome", contato.getNome());
                valores.put("telefone", contato.getTelefone());
                valores.put("dataNasc", contato.getDataNasc());

                //Se o contato a ser inserido possuir id maior que zero, ou seja, já existir, atualiza o contato. Se não insere um novo contato
                if(editarCancelado == false && contato.getId() != null)
                    conexao.update("telefones", valores, "id=?", new String[]{contato.getId().toString()});
                else
                    conexao.insertOrThrow("telefones", null, valores);

                conexao.close();

                return 0;
            }else{
                return 2;
            }
        }
    }

    //Método remover, para excluir um contato do banco de dados e depois atualizar a lista
    public void remover(int id){
        conexao = db.getWritableDatabase();
        conexao.delete("telefones", "id=?", new String[]{id+""});
    }

    //Método para atualizar a lista passada como parâmetro
    public void atualizar(ListView lista){
        ((ArrayAdapter) lista.getAdapter()).notifyDataSetChanged(); //"notifica" o adapter da lista de que os dados estão desatualizados
    }

    //Método para listar os dados dentro da ListView da interface principal
    public void listar(List dados){
        dados.clear();
        conexao = db.getReadableDatabase();
        String names[] = {"id", "nome", "telefone", "dataNasc"};
        Cursor query = conexao.query("telefones", names, null, null, null, null, "id");//consulta ordenada por id de cada linha

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

    public boolean comparaString(String s){
        String f[] = s.split("/");
        if(Integer.parseInt(f[0])<=31 && Integer.parseInt(f[0])>=1 && Integer.parseInt(f[1])>=1 && Integer.parseInt(f[1])<=12 && Integer.parseInt(f[2])>=1900 && Integer.parseInt(f[2])<=2022)
            return true;
        else
            return false;
    }
}
