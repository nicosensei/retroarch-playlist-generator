package com.github.nicosensei.raplaylistgen.dat;

import com.github.nicosensei.raplaylistgen.RomSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public final class DatFile extends LinkedHashMap<String, DatEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(DatFile.class);

    private static final String GAME = "game";
    private static final String NAME = "name";
    private static final String DESC = "description";
    private static final String EMPTY = "";

    public DatFile(
            final String datFilePath,
            final RomSet filter)
            throws IOException, XMLStreamException {
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        final XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new FileReader(datFilePath));

        try {
            String lastElementName = null;
            String gameName = null;
            int eventType;
            while (reader.hasNext()) {
                eventType = reader.next();
                switch (eventType) {
                    case XMLStreamConstants.START_ELEMENT:
                        lastElementName = reader.getLocalName();
                        if (GAME.equals(lastElementName)) {
                            gameName = reader.getAttributeValue(EMPTY, NAME);
                        } else if (gameName != null && DESC.equals(lastElementName)) {
                            if (filter.contains(gameName)) {
                                final String desc = reader.getElementText();
                                LOG.info("Added [{}] {}", gameName, desc);
                                this.put(gameName, new DatEntry(gameName, desc));
                            }
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (GAME.equals(lastElementName)) {
                            gameName = null;
                        }
                        break;
                }
            }
            LOG.info("Loaded DAT file, kept {} entries", size());
        } finally {
            reader.close();
        }
    }

}
