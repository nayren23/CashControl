package BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DatabasePrincipale extends SQLiteOpenHelper {

    protected static final String TAG = "SQLite";

    // Database Version
    protected static final int DATABASE_VERSION = 1;

    public DatabasePrincipale(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


}
