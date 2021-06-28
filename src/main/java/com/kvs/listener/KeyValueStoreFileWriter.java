package com.kvs.listener;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is the implementation of KeyValueStoreListener.
 * This class writes the expired keys to a file defined in the
 * path FILE_STORE_PATH+ "expiredKeys"+System.currentTimeMillis()+".csv";
 *
 */
public class KeyValueStoreFileWriter implements  KeyValueStoreListener{
    private String FILE_STORE_PATH="g:/";
    private String fileStoreName;
    private String fileStoreDirectory;
    private File fileStore;
    private long currentFilePointer=0;

    /**
     * Constructor sets the default path for the expired path and creates the file
     */
    public KeyValueStoreFileWriter()
    {
        try {
            setDefaultPath();
            createStoreFile();
        } catch (Exception e) {
            System.out.println("Exception occurred while storing expired keys to file "+e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Method that calles when the key has expired in the key store
     * @param key
     * @param value
     * @throws Exception
     */
    @Override
    public void onKeyExpired(Object key, Object value) throws Exception {
        writeToFile(key,value);

    }

    /**
     * This method writes the expired key and value to the file
     * @param key
     * @param value
     * @throws Exception
     */
    private synchronized void writeToFile(Object key, Object value) throws Exception {
        RandomAccessFile fileAccess = new RandomAccessFile(this.FILE_STORE_PATH, "rw");
        fileAccess.seek(this.getCurrentFilePointer());
        String toBeWritten = key+":"+value+"\n";
        fileAccess.write(toBeWritten.getBytes());
        Long pointerLocation = fileAccess.getFilePointer();
        this.updateCurrentFilePointer(pointerLocation);
        fileAccess.close();
    }

    /**
     * Returns the current pointer position in the file
     * @return
     */
    private synchronized Long getCurrentFilePointer() {
        return this.currentFilePointer;
    }

    /**
     * updates the pointer position in the file
     * @param pointer
     */
    private synchronized void updateCurrentFilePointer(Long pointer) {
        this.currentFilePointer=pointer;
    }

    /**
     * Sets the default path of the expired keys file
     */
    private synchronized void setDefaultPath() {
        this.fileStoreName="expiredKeys"+System.currentTimeMillis()+".csv";
        String osType = System.getProperty("os.name");
        if("Linux".equals(osType)) {
            this.FILE_STORE_PATH="."+File.separator+this.fileStoreName;
        }
        this.FILE_STORE_PATH="."+File.separator+this.fileStoreName;
    }

    /**
     * Creates a file to store the expired keys
     * @throws Exception
     */
    private synchronized void createStoreFile() throws Exception {
        if(this.FILE_STORE_PATH!=null) {
            this.fileStore = new File(this.FILE_STORE_PATH);
            try {
                fileStore.createNewFile();
                this.fileStoreDirectory = (fileStore.getParent()==null)?".":fileStore.getParent();
            }catch(IOException e) {
                e.printStackTrace();
                throw e;
            }
        }else {
            System.out.println("Error! File store not ready/initialized!");
            throw new Exception ("Error! File store not ready/initialized!");
        }
    }

}
