package com.example.pc.dissertation.db.tables;

/**
 * Created by PC on 02.12.2016.
 */

public interface DAOClass<T extends AppModel> {

    String create();

    T read();

    String udate();

    String delete();

}
