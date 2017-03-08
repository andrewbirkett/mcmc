package org.nobugs.mcmc.diagnostics;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class Coda implements Tracer, Closeable {
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private final OutputStream outputStream;

    public Coda(Path filename) throws IOException {
        this.outputStream = new BufferedOutputStream(new FileOutputStream(filename.toFile()));
    }

    public void update(double[] parameters) {
        try {
            String string = StringUtils.join(parameters, ',') + "\n";
            IOUtils.write(string, outputStream, CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
