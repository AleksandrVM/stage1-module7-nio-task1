package com.epam.mjc.nio;

import java.io.IOException;

public class ReadingFileException extends RuntimeException {
    public ReadingFileException(Throwable e) {
        super(e);
    }
}
