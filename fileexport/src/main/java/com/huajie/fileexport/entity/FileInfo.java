package com.huajie.fileexport.entity;

import java.io.Serializable;

/**
 * Created by guc on 2017/9/6.
 */

public class FileInfo implements Serializable{
    public String fileName;
    public int fileType;
    public String filePath;
    public int position;
    public boolean isSelect;
    public String fileSize;
    public boolean hasChilds;
}
