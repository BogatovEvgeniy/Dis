package com.example.pc.dissertation.db.daos;

import com.example.pc.dissertation.db.AppModel;

public interface DAOClass<T extends AppModel> {

    String create();

    T read();

    String udate();

    String delete();

}
