package com.linsh.common.tools;

import androidx.annotation.NonNull;

import com.linsh.utilseverywhere.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/01
 *    desc   : 流文件读取器
 * </pre>
 */
public class StreamFileReader implements ILshFileReader {

    private BufferedReader reader;
    private String currentLine;

    public StreamFileReader(@NonNull File file) {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCurrentLine() {
        if (currentLine == null) {
            try {
                if (reader != null) {
                    currentLine = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                reader = null;
                currentLine = null;
            }
        }
        return currentLine;
    }

    @Override
    public boolean nextLine() {
        try {
            if (reader != null) {
                currentLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            reader = null;
            currentLine = null;
        }
        return currentLine != null;
    }

    @Override
    public boolean skipEmptyLine() {
        String line;
        boolean ret = false;
        while ((line = getCurrentLine()) != null && StringUtils.trimBlank(line).length() == 0 && nextLine()) {
            ret = true;
        }
        return ret;
    }

    public void release() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
