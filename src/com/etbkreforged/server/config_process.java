package com.etbkreforged.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class config_process {
    private static void writeCfg() {
        File configFile = new File("config.cfg");
        try {
            // 创建新文件
            configFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 配置文件内容
        String cfg = """  
                EasyTimerBackup Server Config  
                v0.0.1  
                =================================================================  
                Do not change the number of lines in the configuration file!  
                Otherwise the read will fail!  
                =================================================================  
                
                Server Port (10)
                ------Port------  
                                           
                ----------------  
                
                Backup File Path (18)
                
                Example:  
                C:\\Folder\\  (/home/Folder/)
                -------Config-------  
                                           
                -------------------- 
                   
                """;

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"))) {
            writer.println(cfg);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void isFileExists() {
        File configFile = new File("config.cfg");
        if (!configFile.exists()) {
            System.out.println("INFO: Generating configuration file...");
            writeCfg();
            System.exit(0);
        }
    }
    //read config
    private static String f_read_config(int lineToRead, String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                if (lineNumber == lineToRead) {
                    return line;
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
    public static String get_config(String target) {
        isFileExists();
        String config_file_path = "config.cfg";

        // Mapping of targets to configuration line numbers
        Map<String, Integer> targetMap = new HashMap<>();
        targetMap.put("port", 10);
        targetMap.put("path", 18);


        Integer lineNumber = targetMap.get(target);

        // If the target is not recognized, return null
        if (lineNumber == null) {
            return null;
        }

        String readed_data = f_read_config(lineNumber, config_file_path);

        // Check if readed_data is empty
        if (readed_data == null || readed_data.isEmpty()) {
            System.out.println("ERROR: The read data is empty!");
            System.exit(1); // Terminate the program
        }

        return readed_data;
    }

}
