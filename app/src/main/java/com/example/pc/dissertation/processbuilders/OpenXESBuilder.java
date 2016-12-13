package com.example.pc.dissertation.processbuilders;

import android.database.Cursor;
import android.os.Environment;

import com.example.pc.dissertation.db.daos.StructuredLogDAO;
import com.example.pc.dissertation.db.tables.StructuredLogTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class OpenXESBuilder {
    public static final String ACTIVITY_NAME = "name";
    public static String DEFAULT_XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    //Elements
    public static String XML_OPEN_ELEMENT_LOG = "<log xes.version=\"1.0\" xes.features=\"nested-attributes\" openxes.version=\"1.0RC7\" xmlns=\"http://www.xes-standard.org/\">";
    public static String XML_NECESSURRY_EXTENTION_PART = "\t<extension name=\"Organizational\" prefix=\"org\" uri=\"http://www.xes-standard.org/org.xesext\"/>\n" +
            "\t<extension name=\"Time\" prefix=\"time\" uri=\"http://www.xes-standard.org/time.xesext\"/>\n" +
            "\t<extension name=\"Concept\" prefix=\"concept\" uri=\"http://www.xes-standard.org/concept.xesext\"/>\n" +
            "\t<global scope=\"trace\">\n" +
            "\t\t<string key=\"concept:name\" value=\"__INVALID__\"/>\n" +
            "\t</global>\n" +
            "\t<global scope=\"event\">\n" +
            "\t\t<string key=\"concept:name\" value=\"__INVALID__\"/>\n" +
            "\t\t<string key=\"lifecycle:transition\" value=\"complete\"/>\n" +
            "\t</global>\n" +
            "\t<string key=\"source\" value=\"Dissertation testing\"/>\n" +
            "\t<string key=\"concept:name\" value=\"Diss.mxml\"/>";
    public static String XML_CLOSE_ELEMENT_LOG = "</log>";
    public static String XML_OPEN_ELEMENT_TRACE = "<trace>";
    public static String XML_CLOSE_ELEMENT_TRACE = "</trace>";
    public static String XML_OPEN_ELEMENT_EVENT = "<event>";
    public static String XML_CLOSE_ELEMENT_EVENT = "</event>";

    // Attributes
    public static String XML_KEY_PREFIX_ORG = "org:";
    public static String XML_KEY_PREFIX_CONCEPT = "concept:";
    public static String XML_KEY_PREFIX_TIME = "time:";

    //Extra elements
    public  static String XML_NEW_LINE = "\n";
    public  static String XML_TAB = "\t";
    
    // Types
    public static String STRING_DATA = "<string ";
    public static String DATE_DATA = "<date ";
    public static String CLOSE_ELEMENT = "  />";
    public static String EVENT_ATR_KEY = "  key=\"";
    public static String EVENT_ATR_VAL = "  value=\"";


    public static void generateLog() throws IOException {
        Cursor logCursor = StructuredLogDAO.getAllRows();
        File openXESFile = new File(Environment.getExternalStorageDirectory()
                .getPath() + File.separatorChar + "Dis" + File.separatorChar + "Result.xes");
        if (openXESFile.exists()) {
            openXESFile.delete();
        }
        openXESFile.getParentFile().mkdirs();
        openXESFile.createNewFile();

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(openXESFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bufferedWriter.write(DEFAULT_XML_HEADER);
        bufferedWriter.write(XML_OPEN_ELEMENT_LOG + XML_NEW_LINE);
        bufferedWriter.write(XML_NECESSURRY_EXTENTION_PART + XML_NEW_LINE);

        int currentProcess = 0;
        int activityNameIndex = 0;
        int processIndex = 0;
        int userIndex = 0;
        int userRoleIndex = 0;
        int objectIndex = 0;
        int timestampIndex = 0;
        int statusIndex = 0;
        while (logCursor.moveToNext()) {
            if (logCursor.getPosition() == 0) {
                processIndex = logCursor.getColumnIndex(StructuredLogTable.PROCESS);
                activityNameIndex = logCursor.getColumnIndex(StructuredLogTable.ACTIVITY);
                userIndex = logCursor.getColumnIndex(StructuredLogTable.USER);
                userRoleIndex = logCursor.getColumnIndex(StructuredLogTable.USER_ROLE);
                objectIndex = logCursor.getColumnIndex(StructuredLogTable.RESOURCE);
                timestampIndex = logCursor.getColumnIndex(StructuredLogTable.TIMESTAMP);
                statusIndex = logCursor.getColumnIndex(StructuredLogTable.STATUS);
                currentProcess = logCursor.getInt(processIndex);
                bufferedWriter.write(XML_TAB + XML_OPEN_ELEMENT_TRACE + XML_NEW_LINE);
            }

            if (currentProcess != logCursor.getInt(processIndex)) {
                currentProcess = logCursor.getInt(processIndex);
                bufferedWriter.write(XML_TAB + XML_CLOSE_ELEMENT_TRACE + XML_NEW_LINE);
                bufferedWriter.write(XML_TAB + XML_OPEN_ELEMENT_TRACE + XML_NEW_LINE);
            }

            bufferedWriter.write(XML_TAB + XML_TAB + XML_OPEN_ELEMENT_EVENT + XML_NEW_LINE);
            insertStringData(bufferedWriter, STRING_DATA, XML_KEY_PREFIX_CONCEPT + ACTIVITY_NAME,
                    logCursor.getString(activityNameIndex));
            insertStringData(bufferedWriter, STRING_DATA, StructuredLogTable.USER,
                    logCursor.getString(userIndex));
            insertStringData(bufferedWriter, STRING_DATA, StructuredLogTable.USER_ROLE,
                    logCursor.getString(userRoleIndex));
            insertStringData(bufferedWriter, STRING_DATA, XML_KEY_PREFIX_ORG + StructuredLogTable.RESOURCE,
                    logCursor.getString(objectIndex));
            insertStringData(bufferedWriter, DATE_DATA, XML_KEY_PREFIX_TIME + StructuredLogTable.TIMESTAMP,
                    logCursor.getString(timestampIndex));
            insertStringData(bufferedWriter, STRING_DATA, StructuredLogTable.STATUS,
                    logCursor.getString(statusIndex));
            bufferedWriter.write(XML_TAB + XML_TAB + XML_CLOSE_ELEMENT_EVENT + XML_NEW_LINE);
        }
        bufferedWriter.write(XML_TAB + XML_CLOSE_ELEMENT_TRACE + XML_NEW_LINE);
        bufferedWriter.write(XML_CLOSE_ELEMENT_LOG);
        bufferedWriter.close();
    }

    private static void insertStringData(BufferedWriter bufferedWriter, String elementType,
            String key, String val) throws IOException {
        bufferedWriter.write(
                XML_TAB + XML_TAB + XML_TAB + elementType + EVENT_ATR_KEY + key + "\"" + EVENT_ATR_VAL + val + "\"" + CLOSE_ELEMENT  + XML_NEW_LINE);
    }
}
