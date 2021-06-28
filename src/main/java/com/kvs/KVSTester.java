package com.kvs;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class KVSTester {
    private static final String PREFIX_INPUT_FILE = "input";

    public static void main(String[] args) {

        try {
            ClassLoader classLoader = KVSTester.class.getClassLoader();

            for (int i = 1; i <= 2; i++) {

                // Create VersionedStore object
                KeyValueStore kvs = new KeyValueStore();
                Path path = Paths.get("files", PREFIX_INPUT_FILE + i + ".txt");
                System.out.println("path "+path);
                InputStream inputStream = classLoader.getResourceAsStream(path.toString());
                System.out.println("inputstream "+inputStream);
                System.setIn(inputStream);


                // Read from stdin
                Scanner sc = new Scanner(System.in);
                while (sc.hasNextLine()) { // check by line
                    String operator = sc.next();
                    // Check operation first
                    if (operator.equals("PUT")) {
                        String key = sc.next();
                        String value = sc.next();
                        kvs.put(key, value);
                    } else { // the GET case
                        String key = sc.next();
                        kvs.get(key);

                    }
                }
                sc.close();
                System.out.println();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}