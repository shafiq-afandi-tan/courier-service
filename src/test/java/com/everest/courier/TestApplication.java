package com.everest.courier;


import org.junit.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

public class TestApplication {
    @Test
    public void testMain_nullArgument() {
        try (MockedStatic<Utilities> utilities = mockStatic(Utilities.class)) {
            Application.main(null);
            utilities.verify(()->Utilities.showError("Null Argument"));
            utilities.verify(()->Utilities.exit(-1));
        }
    }

    @Test
    public void testMain_emptyArgument() {
        try (MockedStatic<Utilities> mocked = mockStatic(Utilities.class)) {
            Application.main(new String[]{});
            mocked.verify(()->Utilities.showError("Empty Argument"));
            mocked.verify(()->Utilities.showHelp());
            mocked.verify(()->Utilities.exit(-1));
        }
    }

    @Test
    public void testMain_missingArgument() {
        try (MockedStatic<Utilities> mocked = mockStatic(Utilities.class)) {
            Application.main(new String[]{"-i", "input.txt"});
            mocked.verify(()->Utilities.showError("Missing Argument"));
            mocked.verify(()->Utilities.showHelp());
            mocked.verify(()->Utilities.exit(-1));
        }
    }

    @Test
    public void testMain_invalidArgument() {
        try (MockedStatic<Utilities> mocked = mockStatic(Utilities.class)) {
            Application.main(new String[]{"-f", "input.txt", "-o", "output.txt"});
            mocked.verify(()->Utilities.showError("Invalid Argument"));
            mocked.verify(()->Utilities.showHelp());
            mocked.verify(()->Utilities.exit(-1));
        }
    }

    @Test
    public void testMain() {
        try (MockedStatic<Utilities> mocked = mockStatic(Utilities.class)) {
            Application.main(new String[]{"-i", "input.txt", "-o", "output.txt"});
            mocked.verify(()->Utilities.exit(0));
        }
    }

}
