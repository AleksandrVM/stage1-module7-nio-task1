package com.epam.mjc.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileReader {

    public static final String NEW_LINE = System.lineSeparator();
    public static final String DELIMITER = ":";
    private static final String MODE = "r";

    enum KeyProfile {NAME, AGE, EMAIL, PHONE}

    public Profile getDataFromFile(File file) {
        try (RandomAccessFile aFile = new RandomAccessFile(file, MODE);
             FileChannel channel = aFile.getChannel()) {
            int sizeFile = (int) aFile.length();
            ByteBuffer buffer = ByteBuffer.allocate(sizeFile);
            channel.read(buffer);
            String data = new String(buffer.array());
            return parse(data);
        } catch (java.io.IOException e) {
            throw new ReadingFileException(e);
        }
    }

    private Profile parse(String dataProfile) {
        Profile profile = new Profile();
        String[] lines = dataProfile.split(NEW_LINE);
        for (String line : lines) {
            String[] keyValue = line.split(DELIMITER, 2);
            String value = keyValue[1].trim();

            switch (getKeyProfile(keyValue[0])) {
                case NAME:
                    profile.setName(value);
                    break;
                case AGE: {
                    try {
                        profile.setAge(Integer.valueOf(value));
                    } catch (NumberFormatException e) {
                        throw new FileFormatException("Age value is not correct. Age: " + value, e);
                    }
                    break;
                }
                case EMAIL:
                    profile.setEmail(value);
                    break;
                case PHONE:
                    try {
                        profile.setPhone(Long.valueOf(value));
                    } catch (NumberFormatException e) {
                        throw new FileFormatException("Phone value is not correct. Phone:" + value, e);
                    }
                    break;
            }
        }
        return profile;
    }

    private KeyProfile getKeyProfile(String key) {
        try {
            return KeyProfile.valueOf(key.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FileFormatException("Unknown key: " + key, e);
        }
    }
}
