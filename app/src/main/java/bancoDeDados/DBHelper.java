package bancoDeDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper (@Nullable Context context){
        super(context, "BancoTelefones", null, 1);
    }

    //método onCreate para quando iniciar o aplicativo pela primeira vez, criar a tabela de telefones e os respectivos campos
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE telefones(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(50)," +
                "telefone VARCHAR(15)," +
                "dataNasc VARCHAR(10))");
    }

    //método onUpgrade, vazio
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
}
