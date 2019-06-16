package it.polimi.se2019.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {
    @Test
    public void testToSnakeCase() {
        assertEquals(
                "lock_rifle",
                StringUtils.toSnakeCase("Lock rifle")
        );
    }
}
