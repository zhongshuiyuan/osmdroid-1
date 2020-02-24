package com.huejie.osmdroid.model;


import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class CoreProjectRolePerson extends LitePalSupport implements Serializable {
    public long id;
    public long user_id;
    public long project_id;
    public long project_role_id;
    public long record_book_type_id;
    public String project_role_code;
    public String user_name;
}
