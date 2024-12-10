package com.example.project_1_java.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class copyDbHelper {
    private static final String DB_NAME = "Address";
    private final Context context;

    public copyDbHelper(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void copyDatabase(File dbFile) throws IOException {
        InputStream inputStream = context.getAssets().open(DB_NAME);
        FileOutputStream outputStream = new FileOutputStream(dbFile);
        byte[] buffer = new byte[1024];
        while (inputStream.read(buffer)> 0) {
            outputStream.write(buffer);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
