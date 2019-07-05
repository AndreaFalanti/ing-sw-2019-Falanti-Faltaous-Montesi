package it.polimi.se2019.util;

import org.junit.Test;

import java.net.URISyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class ResourceUtilsTest {
    @Test
    public void testLoadResource() {
        System.out.println(ResourceUtils.loadResource("/json/"));
    }

    @Test
    public void testWalk() {
        System.out.println(
                ResourceUtils.flatWalk("/json/")
                        .collect(Collectors.joining("\n"))
        );
    }

    @Test
    public void testIsDirectory() {
        System.out.println(getClass().getClassLoader().getResource("/json/weapons/real/machine_gun.json"));
    }
}
