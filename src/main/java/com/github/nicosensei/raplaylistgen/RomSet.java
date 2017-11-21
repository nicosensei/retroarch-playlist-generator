package com.github.nicosensei.raplaylistgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public final class RomSet extends HashSet<String> {

    private static final Logger LOG = LoggerFactory.getLogger(RomSet.class);

    public final String ZIP_EXT = ".zip";

    private final String id;

    private final String root;

    public RomSet(final String id, final String folderPath) throws IOException {
        this.id = id;

        final File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory() || !folder.canRead()) {
            throw new IOException(folderPath + " is not a readable folder!");
        }

        this.root = folder.getAbsolutePath();

        Arrays.asList(folder.listFiles(f ->  f.isFile() && f.getName().endsWith(ZIP_EXT)))
            .forEach(f -> this.add(f.getName().replaceFirst("\\" + ZIP_EXT, "")));

        LOG.info("Found {} roms in {}", size(), root);
    }

    public String getId() {
        return id;
    }

    public String getRoot() {
        return root;
    }

}
