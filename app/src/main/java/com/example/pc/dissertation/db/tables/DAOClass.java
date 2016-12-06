package com.example.pc.dissertation.db.tables;

public interface DAOClass<T extends AppModel> {

    String create();

    T read();

    String udate();

    String delete();

}
