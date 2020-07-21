// Copyright Red Energy Limited 2017

package simplenem12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Simple test harness for trying out SimpleNem12Parser implementation
 */
public class TestHarness {

    @Test
    public void parseNem12File() {
        File simpleNem12File = new File(getClass().getClassLoader().getResource("SimpleNem12.csv").getFile());
        Collection<MeterRead> meterReads = new SimpleNem12ParserImpl().parseSimpleNem12(simpleNem12File);
        MeterRead read6123456789 = meterReads.stream().filter(mr -> mr.getNmi().equals("6123456789")).findFirst().get();
        assertEquals(-36.84, read6123456789.getTotalVolume().doubleValue());
        MeterRead read6987654321 = meterReads.stream().filter(mr -> mr.getNmi().equals("6987654321")).findFirst().get();
        assertEquals(14.33, read6987654321.getTotalVolume().doubleValue());

        Assertions.assertTrue(read6123456789.getVolumes().firstKey().compareTo(read6123456789.getVolumes().lastKey()) < 0);
        Assertions.assertTrue(read6987654321.getVolumes().firstKey().compareTo(read6987654321.getVolumes().lastKey()) < 0);
    }

    @Test
    public void testNem12File() {
        File simpleNem12File = new File(getClass().getClassLoader().getResource("TestNem12.csv").getFile());
        Collection<MeterRead> meterReads = new SimpleNem12ParserImpl().parseSimpleNem12(simpleNem12File);
        MeterRead read123456 = meterReads.stream().filter(mr -> mr.getNmi().equals("123456")).findFirst().get();
        MeterRead read1234567 = meterReads.stream().filter(mr -> mr.getNmi().equals("1234567")).findFirst().get();
        MeterRead read12345678 = meterReads.stream().filter(mr -> mr.getNmi().equals("12345678")).findFirst().get();
        MeterRead read123456789 = meterReads.stream().filter(mr -> mr.getNmi().equals("123456789")).findFirst().get();
        assertEquals(1.19, read123456.getTotalVolume().doubleValue());
        assertEquals(14.33, read1234567.getTotalVolume().doubleValue());
        Assertions.assertTrue(read12345678.getVolumes().lastKey().compareTo(read12345678.getVolumes().firstKey()) > 0);
        Assertions.assertTrue(read12345678.getVolumes().firstKey().compareTo(read12345678.getVolumes().lastKey()) < 0);

        assertEquals(6, read12345678.getVolumes().keySet().size());
    }


}
