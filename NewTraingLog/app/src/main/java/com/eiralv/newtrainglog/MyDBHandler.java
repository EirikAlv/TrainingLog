package com.eiralv.newtrainglog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.eiralv.newtrainglog.Adapter.ListAdapterItem;
import com.eiralv.newtrainglog.Log.Logging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static java.time.LocalDate.parse;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "TreningsLog.db";
    //TABLE NAMES
    private static final String TABLE_PROGRAM = "Program";
    private static final String TABLE_LOGGING = "Logging";
    private static final String TABLE_OVELSE = "Ovelse";
    private static final String TABLE_PROGREG = "ProgOvelseReg";
    private static final String TABLE_MESURE = "Mesure";
    private static final String TABLE_HISTORY_PROGRAM = "HistoryProgram";
    private static final String TABLE_HISTORY_LOG = "HistoryLog";
    //COMMON COLUMN NAMES
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_OVELSE = "OvelseNavn";
    private static final String COLUMN_PROGRAM = "ProgramNavn";
    //LOGGING TABLE - COLUMN NAMES
    private static final String COLUMN_VEKT = "Vekt";
    private static final String COLUMN_REPS = "Reps";
    private static final String COLUMN_DATO = "Dato";
    private static final String COLUMN_WEIGHTMESURE = "WeightMesure";
    //MESURE COLUMN NAME
    private static final String COLUMN_MESUREMENT = "Mesurement";

    Context context;
    private String dato;

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "create table " + TABLE_PROGRAM + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_PROGRAM + " text unique);";
        String query2 = "create table " + TABLE_OVELSE + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_OVELSE + " text unique);";
        String query3 = "create table " + TABLE_PROGREG + "(" +
                COLUMN_PROGRAM + " text, " +
                COLUMN_OVELSE + " text );";
        String query4 = "create table " + TABLE_LOGGING + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_PROGRAM + " text, " +
                COLUMN_OVELSE + " text, " +
                COLUMN_VEKT + " text, " +
                COLUMN_REPS + " text, " +
                COLUMN_DATO + " text, " +
                COLUMN_WEIGHTMESURE + " text );";
        String query5 = "create table " + TABLE_MESURE + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_MESUREMENT + " text);";
        String query6 = "create table " + TABLE_HISTORY_PROGRAM + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_PROGRAM + " text unique);";
        String query7 = "create table " + TABLE_HISTORY_LOG + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_PROGRAM + " text, " +
                COLUMN_OVELSE + " text, " +
                COLUMN_VEKT + " text, " +
                COLUMN_REPS + " text, " +
                COLUMN_DATO + " text, " +
                COLUMN_WEIGHTMESURE + " text );";
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query6);
        db.execSQL(query7);
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESUREMENT, "kg");
        long svar = db.insert(TABLE_MESURE, null, values);
        if (svar == -1) {
            Toast.makeText(this.context, "something went wrong when saving mesure to db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PROGRAM);
        db.execSQL("drop table if exists " + TABLE_OVELSE);
        db.execSQL("drop table if exists " + TABLE_PROGREG);
        db.execSQL("drop table if exists " + TABLE_LOGGING);
        db.execSQL("drop table if exists " + TABLE_MESURE);
        db.execSQL("drop table if exists " + TABLE_HISTORY_PROGRAM);
        db.execSQL("drop table if exists " + TABLE_HISTORY_LOG);

        onCreate(db);
    }

    /**
     * @return Arraylist of all entries from table Exercises
     */
    public ArrayList<String> getAllExercises() {
        ArrayList<String> returnList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select " + COLUMN_OVELSE + " from " + TABLE_OVELSE;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            returnList.add(c.getString(0));
        }
        c.close();
        db.close();
        return returnList;
    }

    public void setMesure(String mesure) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "update " + TABLE_MESURE + " set " + COLUMN_MESUREMENT + "= \'" + mesure + "\'" + " where " +
                COLUMN_ID + "=" + "1";
        db.execSQL(query);
        db.close();
    }

    public String getMesure() {
        SQLiteDatabase db = getWritableDatabase();
        String dbString = "";
        String query = "select " + COLUMN_MESUREMENT + " from " + TABLE_MESURE + " where " +
                COLUMN_ID + "=" + "1";

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            dbString += c.getString(0);
        }
        c.close();
        db.close();
        return dbString;
    }

    public void addProgram(Program program) {
        SQLiteDatabase db = getWritableDatabase();
        //CHECK IF PROGRAM ALREADY EXISTS
        String query = "select * from " + TABLE_PROGRAM;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            if (program.getProgramNavn().equals(c.getString(1))) {
                db.close();
                c.close();
                return;
            }
        }
        c.close();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROGRAM, program.getProgramNavn());
        db.insert(TABLE_PROGRAM, null, values);
        db.close();
    }

    public void addHistoryProgram(String programName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROGRAM, programName);
        db.insert(TABLE_HISTORY_PROGRAM, null, values);
        db.close();
    }

    public void transferToHistoryLog(String programName) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "select * from " + TABLE_LOGGING + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            Logging newLog = new Logging(c.getString(1), c.getString(2), c.getString(3), c.getString(4)
                    , c.getString(6));
            newLog.setDato(c.getString(5));
            saveToHistoryLog(newLog);
        }
        c.close();
        db.close();
    }

    private void saveToHistoryLog(Logging newLog) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROGRAM, newLog.getProgramNavn());
        values.put(COLUMN_OVELSE, newLog.getOvelseNavn());
        values.put(COLUMN_VEKT, newLog.getVekt());
        values.put(COLUMN_REPS, newLog.getReps());
        values.put(COLUMN_DATO, newLog.getDato());
        values.put(COLUMN_WEIGHTMESURE, newLog.getMesure());
        long svar = db.insert(TABLE_HISTORY_LOG, null, values);
        if (svar == -1) {
            Toast.makeText(this.context, "something went wrong when saving logging to db", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void addExercise(Ovelse ovelse) {
        SQLiteDatabase db = getWritableDatabase();
        //CHECK IF EXERCISE ALREADY EXISTS
        String query = "select * from " + TABLE_OVELSE;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            if (ovelse.getOvelseNavn().equals(c.getString(1))) {
                db.close();
                c.close();
                return;
            }
        }
        c.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_OVELSE, ovelse.getOvelseNavn());
        long svar = db.insert(TABLE_OVELSE, null, values);
        if (svar == -1) {
            Toast.makeText(this.context, "something went wrong when saving ovelse to db", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void addProgOvelseReg(ProgOvelseReg progOvelseReg) {
        ArrayList<String> ovelser = privateGetExercisesPerProgram(progOvelseReg.getProgramNavn());
        for (String item : ovelser) {
            if (item.equals(progOvelseReg.getOvelseNavn())) {
                Toast.makeText(this.context, "This exercise is already registered to this program", Toast.LENGTH_LONG).show();
                return;
            }
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_OVELSE, progOvelseReg.getOvelseNavn());
        values.put(COLUMN_PROGRAM, progOvelseReg.getProgramNavn());
        SQLiteDatabase db = getWritableDatabase();

        long svar = db.insert(TABLE_PROGREG, null, values);
        if (svar == -1) {
            Toast.makeText(this.context, "something went wrong when saving progreg to db", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void saveToLogging(Logging logging) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROGRAM, logging.getProgramNavn());
        values.put(COLUMN_OVELSE, logging.getOvelseNavn());
        values.put(COLUMN_VEKT, logging.getVekt());
        values.put(COLUMN_REPS, logging.getReps());
        values.put(COLUMN_DATO, logging.getDato());
        values.put(COLUMN_WEIGHTMESURE, logging.getMesure());
        SQLiteDatabase db = getWritableDatabase();
        long svar = db.insert(TABLE_LOGGING, null, values);
        if (svar == -1) {
            Toast.makeText(this.context, "something went wrong when saving logging to db", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        //GET EVERYTHING FROM PROGOVELSEREG TABLE
        String query = "select * from " + TABLE_LOGGING;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            dbString += c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " " +
                    c.getString(4) + " " + c.getString(5) + "\n";
        }
        c.close();
        db.close();
        return dbString;
    }

    /**
     * @return list of all programs
     */
    public ArrayList<String> programToList() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_PROGRAM;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> historyProgramToList() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_HISTORY_PROGRAM;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> datesToList(String programName) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select " + COLUMN_DATO + " from " + TABLE_LOGGING + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"" + " group by " + COLUMN_DATO;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> historyDatesToList(String programName) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select " + COLUMN_DATO + " from " + TABLE_HISTORY_LOG + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"" + " group by " + COLUMN_DATO;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        db.close();
        return list;
    }

    /**
     * Debugging method to see what is in table_history_log
     */
    public void historyDatabaseToString() {
        String dbString = "";
        String progString = "";
        SQLiteDatabase db = getWritableDatabase();
        //GET EVERYTHING FROM PROGOVELSEREG TABLE
        String query = "select * from " + TABLE_HISTORY_LOG;
        String query2 = "select * from " + TABLE_HISTORY_PROGRAM;
        Cursor c = db.rawQuery(query, null);
        Cursor b = db.rawQuery(query2, null);
        while (c.moveToNext()) {
            dbString += c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " " +
                    c.getString(4) + " " + c.getString(5) + c.getString(6) + "\n";
        }
        c.close();
        while (b.moveToNext()) {
            progString += b.getString(1) + "\n";
        }
        db.close();
    }

    public ArrayList<LocalDate> getLocalDateToList(String programName) {
        ArrayList<String> list = datesToList(programName);
        list.addAll(historyDatesToList(programName));
        ArrayList<LocalDate> returnList = new ArrayList<>();
        for (String s : list) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                returnList.add(parse(s));
            }
        }
        return returnList;
    }

    /**
     * @param programName
     * @param date
     * @return String representing the date that was logged just prior to the date that is passed in, if there is no
     * prior log it will return null
     */
    public String getPriorLog(String programName, String date) {
        ArrayList<String> list = datesToList(programName);
        list.addAll(historyDatesToList(programName));
        String returnString = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate thisDate = parse(date);
            ArrayList<LocalDate> dates = new ArrayList<>();
            int index = 0;
            if (list.size() > 1 /*&& !list.get(0).equals(date)*/) {
                for (String s : list) {
                    dates.add(parse(s));
                }
                Collections.sort(dates);
                LocalDate currentDate = null;
                for (LocalDate localDate : dates) {
                    if (localDate.isEqual(thisDate)) {
                        currentDate = localDate;
                    }
                }
                index = dates.indexOf(currentDate);
            }
            if (index != 0) {
                returnString = dates.get(index - 1).toString();
            }
        }
        return returnString;
    }

    /**
     * @param programName
     * @param date
     * @return String representing the date that was logged next after the date that is passed in, if there is no
     * next log it will return null
     */
    public String getNextLog(String programName, String date) {
        ArrayList<String> list = datesToList(programName);
        ArrayList<String> historyList = historyDatesToList(programName);
        for (String s : historyList) {
            boolean found = false;
            for (int i = 0; i < list.size(); i++) {
                if(s.equals(list.get(i))) {
                    found = true;
                }
            }
            if(!found) {
                list.add(s);
            }
        }
        String returnString = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate thisDate = parse(date);
            ArrayList<LocalDate> dates = new ArrayList<>();
            int index = 0;
            if (list.size() > 1) {
                for (String s : list) {
                    dates.add(parse(s));
                }
                Collections.sort(dates);
                LocalDate currentDate = null;
                for (LocalDate localDate : dates) {
                    if (localDate.isEqual(thisDate)) {
                        currentDate = localDate;
                    }
                }
                index = dates.indexOf(currentDate);
            }
            if (index != dates.size() - 1 && dates.size() != 0) {
                returnString = dates.get(index + 1).toString();
            }
        }
        return returnString;
    }

    /**
     * @param program
     * @return String representing the most resent date that the program that is passed in has logged
     */
    public String mostResentLog(String program) {
        ArrayList<String> strings = datesToList(program);
        strings.addAll(historyDatesToList(program));
        ArrayList<LocalDate> datoer = new ArrayList<>();
        for (String s : strings) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                datoer.add(parse(s));
            }
        }
        return Collections.max(datoer).toString();
    }

    /**
     * @param programName
     * @param dato
     * @return arraylist containing distinct exercises that has been logged in a certain program on a certain date
     * both from the logging table and from history logging table
     */
    public ArrayList<String> getExercisePerProgramDate(String programName, String dato) {
        String query2 = "select distinct OvelseNavn from " + TABLE_LOGGING + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"" + " and " +
                COLUMN_DATO + "= \"" + dato + "\"";
        String query = "select distinct OvelseNavn from " + TABLE_HISTORY_LOG + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"" + " and " +
                COLUMN_DATO + "= \"" + dato + "\"";
        ArrayList<String> ovelser = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query2, null);
        while (cursor.moveToNext()) {
            ovelser.add(cursor.getString(0));
        }
        cursor.close();
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            boolean found = false;
            for (String s : ovelser) {
                if (s.equals(c.getString(0))){
                    found = true;
                }
            }
            if (!found) {
                ovelser.add(c.getString(0));
            }
        }
        c.close();
        db.close();
        return ovelser;
    }


    //only a helper method for loggingToList(), the same as getExercisesPerProgram() but without closing db
    private ArrayList<String> privateGetExercisesPerProgram(String progName) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_PROGREG + " where " + COLUMN_PROGRAM + "= \"" + progName + "\"";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        return list;
    }

    public ArrayList<String> getExercisesPerProgram(String progName) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_PROGREG + " where " + COLUMN_PROGRAM + "= \"" + progName + "\"";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<ListAdapterItem> getLogginPerExerciseDate(String exerciseName) {

        ArrayList<ListAdapterItem> list = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            dato = LocalDate.now().toString();
        }
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_LOGGING + " where " + COLUMN_OVELSE + "= \"" + exerciseName + "\"" +
                "and " + COLUMN_DATO + "= \"" + dato + "\"";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            list.add(new ListAdapterItem(c.getString(3), c.getString(4)));
        }

        c.close();
        db.close();
        return list;
    }

    /**
     * Delete everything from history_program and history_log tables
     *
     * @param programName
     */
    public void deleteHistoryProgram(String programName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_HISTORY_PROGRAM + " where " + COLUMN_PROGRAM + "= \"" + programName + "\";");
        db.execSQL("delete from " + TABLE_HISTORY_LOG + " where " + COLUMN_PROGRAM + "= \"" + programName + "\";");
        db.close();
    }

    public void deleteExercisesIfNotUsed(String programName) {
        ArrayList<String> testList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select distinct OvelseNavn from " + TABLE_LOGGING + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            testList.add(c.getString(0));
        }
        c.close();
        String query2 = "select distinct OvelseNavn from " + TABLE_HISTORY_LOG + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"";
        Cursor c2 = db.rawQuery(query2, null);
        while (c2.moveToNext()) {
            testList.add(c2.getString(0));
        }
        c2.close();
        db.close();

    }

    public void deleteAllLoggedLines(String programName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_LOGGING + " where " + COLUMN_PROGRAM + "= \"" + programName + "\";");
        db.close();
    }

    //method to delete a program and all its content
    public void deleteProgram(String program) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_PROGRAM + " where " + COLUMN_PROGRAM + "= \"" + program + "\";");
        db.execSQL("delete from " + TABLE_PROGREG + " where " + COLUMN_PROGRAM + "= \"" + program + "\";");
        db.execSQL("delete from " + TABLE_LOGGING + " where " + COLUMN_PROGRAM + "= \"" + program + "\";");
        db.close();
    }

    //METHOD TO DELETE EXERCISE FROM A SPECIFIED PROGRAM
    public void deleteExerciseFromProgram(String programName, String exerciseName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_PROGREG + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"" + " and " +
                COLUMN_OVELSE + "= \"" + exerciseName + "\"");
        db.close();
    }

    //METHOD TO DELETE A LOG SPECIFIED LOG LINE
    public void deleteLogLine(String programName, String exerciseTitle, ListAdapterItem item) {
        String line = item.getWeight() + item.getReps();
        line = line.replaceAll("\\s+|\\D", "");
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            dato = LocalDate.now().toString();
        }
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_LOGGING + " where " + COLUMN_PROGRAM + "= \"" + programName + "\"" + " and " +
                COLUMN_DATO + "= \"" + dato + "\"" + " and " + COLUMN_OVELSE + "= \"" + exerciseTitle + "\"";
        Cursor c = db.rawQuery(query, null);
        String output;
        while (c.moveToNext()) {
            output = c.getString(3) + c.getString(4);
            if (output.equals(line)) {
                db.execSQL("delete from " + TABLE_LOGGING + " where " + COLUMN_ID + "= \"" + c.getString(0) + "\"");
                return;
            }
        }
        c.close();
    }

    public ArrayList<ListAdapterItem> getLogLinePerExerciseDate(String exerciseName, String date) {
        ArrayList<ListAdapterItem> returnList = new ArrayList<>();

        this.dato = date;
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_LOGGING + " where " + COLUMN_OVELSE + "= \"" + exerciseName + "\"" +
                "and " + COLUMN_DATO + "= \"" + dato + "\"";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            returnList.add(new ListAdapterItem(c.getString(3) + " " + c.getString(6), c.getString(4) + " reps"));
        }

        c.close();
        db.close();

        return returnList;
    }

    public ArrayList<ListAdapterItem> getLogLinePerExerciseDateNoMesurement(String exerciseName, String date) {
        ArrayList<ListAdapterItem> returnList = new ArrayList<>();

        this.dato = date;
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_LOGGING + " where " + COLUMN_OVELSE + "= \"" + exerciseName + "\"" +
                "and " + COLUMN_DATO + "= \"" + dato + "\"";
        String query2 = "select * from " + TABLE_HISTORY_LOG + " where " + COLUMN_OVELSE + "= \"" + exerciseName + "\"" +
                "and " + COLUMN_DATO + "= \"" + dato + "\"";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            returnList.add(new ListAdapterItem(c.getString(3), c.getString(4)));
        }
        c.close();
        Cursor b = db.rawQuery(query2, null);
        while (b.moveToNext()) {
            returnList.add(new ListAdapterItem(b.getString(3), b.getString(4)));
        }
        b.close();
        db.close();

        return returnList;
    }

    public ArrayList<String> getMesurementPerExerciseDate(String exerciseName, String date) {
        ArrayList<String> returnList = new ArrayList<>();
        this.dato = date;
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + TABLE_LOGGING + " where " + COLUMN_OVELSE + "= \"" + exerciseName + "\"" +
                "and " + COLUMN_DATO + "= \"" + dato + "\"";
        String query2 = "select * from " + TABLE_HISTORY_LOG + " where " + COLUMN_OVELSE + "= \"" + exerciseName + "\"" +
                "and " + COLUMN_DATO + "= \"" + dato + "\"";
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            returnList.add(c.getString(6));
        }
        c.close();
        Cursor b = db.rawQuery(query2, null);
        while (b.moveToNext()) {
            returnList.add(b.getString(6));
        }
        b.close();
        db.close();

        return returnList;
    }


}











