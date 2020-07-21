package simplenem12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleNem12ParserImpl implements SimpleNem12Parser {
    private static final String FIRST_LINE = "100";
    private static final String LAST_LINE = "900";
    private static final String METER_BLOCK_START = "200";
    private static final String METER_VOLUME_START = "300";

    /**
     * Parses Simple NEM12 file.
     *
     * @param simpleNem12File file in Simple NEM12 format
     * @return Collection of <code>MeterRead</code> that represents the data in the given file.
     */
    @Override
    public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) {
        String line = "";
        final String delimiter = ",";
        final List<MeterRead> meterReadingsList = new ArrayList<>();
        MeterRead meterRead = null;
        int lineCount = 0;
        boolean endOfFile = false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(simpleNem12File));
            while ((line = br.readLine()) != null) {
                if (endOfFile) {// error if file has more readings past 900 RecordType
                    System.out.println("Meter reading file is not in expected format, RecordType 900 must be the last line");
                    return null;
                }
                lineCount++;
                String[] lineDataArray = line.split(delimiter);

                //error if 100 is not the First Line of the file
                if ((lineCount > 1 && FIRST_LINE.equalsIgnoreCase(lineDataArray[0]))
                        || (lineCount == 1 && !FIRST_LINE.equalsIgnoreCase(lineDataArray[0]))) {
                    System.out.println("Meter reading file is not in expected format, Record Type 100 should be the first line");
                    return null;
                }
                switch (lineDataArray[0]) {
                    case FIRST_LINE: {
                        break;
                    }
                    case METER_BLOCK_START: {
                        meterRead = new MeterRead(lineDataArray[1], EnergyUnit.valueOf(lineDataArray[2]));
                        meterReadingsList.add(meterRead);
                        break;
                    }
                    case METER_VOLUME_START: {
                        if (meterRead != null) {
                            meterRead.getVolumes().put(LocalDate.parse(lineDataArray[1], DateTimeFormatter.BASIC_ISO_DATE),
                                    new MeterVolume(new BigDecimal(lineDataArray[2]), Quality.valueOf(lineDataArray[3])));
                        } else {
                            System.out.println("Cannot process a meter volume without a meter read block");
                            return null;
                        }
                        break;
                    }
                    case LAST_LINE: {
                        endOfFile = true;
                        break;
                    }
                    default: {
                        System.out.println("Meter reading file is not in expected format, unsupported start of line");
                        return null;
                    }
                }
            }
            if (!endOfFile) {//error if file has no RecordType 900 in the last line
                System.out.println("Meter reading file is not in expected format, RecordType 900 must be the last line");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return meterReadingsList;
    }
}
