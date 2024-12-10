package com.example.project_1_java.Funcion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyDbHelper {
    private static final String DB_NAME = "Address";
    private Context context;

    public void CopyDBHelper(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        File file = new File(dbFile.toString());
        if (!file.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error copying database", e);
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void copyDatabase(File dbFile) throws IOException {
        InputStream openDb = context.getAssets().open(DB_NAME);
        FileOutputStream outputStream = new FileOutputStream(dbFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = openDb.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        openDb.close();
    }
}
