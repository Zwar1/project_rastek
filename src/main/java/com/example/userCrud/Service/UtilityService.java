package com.example.userCrud.Service;

import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    public String parseData(String data, String startTag, String endTag) {
        data = " " + data;
        int start = data.indexOf(startTag);
        if (start != -1) {
            int end = data.indexOf(endTag, start + startTag.length());
            if (end != -1) {
                return data.substring(start + startTag.length(), end);
            }
        }
        return "";
    }
}
