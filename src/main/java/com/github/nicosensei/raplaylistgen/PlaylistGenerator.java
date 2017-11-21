package com.github.nicosensei.raplaylistgen;

import com.github.nicosensei.raplaylistgen.dat.DatEntry;
import com.github.nicosensei.raplaylistgen.dat.DatFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public final class PlaylistGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(PlaylistGenerator.class);

    public static void main(final String[] args) throws IOException, XMLStreamException {

        final Properties props = new Properties();
        props.load(new FileReader(new File(args[0])));

        final RomSet romSet = new RomSet("fba_cps", props.getProperty("source"));
        final DatFile datFile = new DatFile(props.getProperty("dat"), romSet);

        final File thumbnailsRootDir = new File(props.getProperty("thumbnails"));
        if (!thumbnailsRootDir.exists() || !thumbnailsRootDir.isDirectory() || !thumbnailsRootDir.canRead()) {
            throw new IOException(thumbnailsRootDir.getAbsolutePath() + " is not a readable directory!");
        }

        final List<String> thumbsLookupOrder = Arrays.asList(props.getProperty("thumbnails_lookup").split(","));
        final ArrayList<File> thumbDirs = new ArrayList<>(thumbsLookupOrder.size());
        thumbsLookupOrder.forEach(s -> thumbDirs.add(new File(thumbnailsRootDir, s)));
        for (final File d : thumbDirs) {
            if (!d.exists() || !d.isDirectory() || !d.canRead()) {
                throw new IOException(d.getAbsolutePath() + " is not a readable directory!");
            }
        }

        for (final DatEntry rom : datFile.values()) {
            final String hit = lookupThumbnail(rom, thumbDirs);
        }

//        final File targetFolder = new File(props.getProperty("target"));
    }

    private static String lookupThumbnail(
            final DatEntry datEntry,
            final List<File> thumbnailsDirs) {
        final String gameFullName = datEntry.getDescription();
        final Iterator<File> dirs = thumbnailsDirs.iterator();
        while (dirs.hasNext()) {
            final File[] hits = dirs.next().listFiles((d, n) ->  (gameFullName + ".png").equals(n));
            if (hits.length > 0) {
                LOG.debug("[{}] {} > {}", datEntry.getName(), gameFullName, hits[0].getName());
                return hits[0].getAbsolutePath();
            }
        }
        LOG.debug("[{}] {} > ???", datEntry.getName(), gameFullName);
        return null;
    }

}
