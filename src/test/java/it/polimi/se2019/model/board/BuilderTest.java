package it.polimi.se2019.model.board;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BuilderTest {
    Builder mTestBoard;
    Builder mTestBuilder;

    @Before
    void instantiate() throws FileNotFoundException {
    }

    @Test
    void testCombineRightDuplicateBoard() {
        otherBuilder = mTestBuilder.clone();

        Builder combinedBuilder = mTestBuilder.combineRight(otherBuilder.build());

        assertEquals(combinedBuilder,
                     );
    }
}
