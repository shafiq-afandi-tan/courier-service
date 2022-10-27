package com.everest.courier;


import org.junit.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

public class CostEstimatorTest {
    @Test
    public void testApplicationMain() {
        try (MockedStatic<Utilities> utilities = mockStatic(Utilities.class)) {
            Application.main(new String[]{});
            utilities.verify(()->Utilities.exit(-1));
        }
    }
}
