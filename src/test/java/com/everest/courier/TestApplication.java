package com.everest.courier;


import com.everest.courier.Exceptions.UserInputException;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestApplication {
    private void testMain_argumentVoid(String[] args) {
        try (MockedStatic<Wrapper> mocked = mockStatic(Wrapper.class)) {
            Application.main(new String[]{});
            mocked.verify(()-> Wrapper.showVersion());
            mocked.verify(()-> Wrapper.showCopyRight());
            mocked.verify(()-> Wrapper.showHelp());
            mocked.verify(()-> Wrapper.exit(-1));
        }
    }


    @Test
    public void testMain_nullArgument() {
        testMain_argumentVoid(null);
    }

    @Test
    public void testMain_emptyArgument() {
        testMain_argumentVoid(new String[] {});
    }

    private void testMain_argument(String[] args, String msg) {
        try (MockedStatic<Wrapper> mocked = mockStatic(Wrapper.class)) {
            Application.main(args);
            mocked.verify(()-> Wrapper.showError(msg));
            mocked.verify(()-> Wrapper.showHelp());
            mocked.verify(()-> Wrapper.exit(-1));
        }
    }

    @Test
    public void testMain_invalidContextArgument() {
        testMain_argument(new String[]{"-i", "input.txt"}, "Invalid Argument");
    }

    @Test
    public void testMain_missingArgument() {
        testMain_argument(new String[]{"cost", "-i", "input.txt"}, "Missing Argument");
    }

    @Test
    public void testMain_missingArgumentValue() {
        testMain_argument(new String[]{"cost", "-i", "-o", "output.txt"}, "Missing Argument Value");
    }

    @Test
    public void testMain_invalidArgument() {
        testMain_argument(new String[]{"cost", "-f", "input.txt", "-o", "output.txt"}, "Invalid Argument");
    }

    @Test
    public void testMain_inputFileNotFound() {
        try (MockedStatic<Wrapper> mocked = mockStatic(Wrapper.class)) {
            mocked.when(()-> Wrapper.getInputLines("input.txt")).thenThrow(new FileNotFoundException());
            Application.main(new String[]{"cost", "-i", "input.txt", "-o", "output.txt"});
            mocked.verify(()-> Wrapper.getInputLines("input.txt"));
            mocked.verify(()-> Wrapper.showError(any(FileNotFoundException.class)));
            mocked.verify(()-> Wrapper.exit(-1));
        }
    }

    @Test
    public void testMain_inputFileEmpty() {
        try (MockedStatic<Wrapper> mocked = mockStatic(Wrapper.class)) {
            mocked.when(()-> Wrapper.getInputLines("input.txt")).thenReturn(new ArrayList<>());
            Application.main(new String[]{"cost", "-i", "input.txt", "-o", "output.txt"});
            mocked.verify(()-> Wrapper.getInputLines("input.txt"));
            mocked.verify(()-> Wrapper.showError("User Input Empty"));
            mocked.verify(()-> Wrapper.exit(-1));
        }

    }

    @Test
    public void testMain_invalidInputLines() {
        List<String> lines = new ArrayList<>();
        lines.add("junk");
        String msg = "Invalid format - field base_delivery_cost";
        try (MockedStatic<Wrapper> mocked = mockStatic(Wrapper.class);
        MockedStatic<Parser> mockedUtilities = mockStatic(Parser.class)) {
            mocked.when(()-> Wrapper.getInputLines("input.txt")).thenReturn(lines);
            mockedUtilities.when(()-> Parser.parseCostContext(lines)).thenThrow(new UserInputException(msg));
            Application.main(new String[]{"cost", "-i", "input.txt", "-o", "output.txt"});
            mocked.verify(()-> Wrapper.getInputLines("input.txt"));
            mockedUtilities.verify(()-> Parser.parseCostContext(lines));
            mocked.verify(()-> Wrapper.showError(msg));
            mocked.verify(()-> Wrapper.exit(-1));
        }
    }

    @Test
    public void testMain() {
        List<String> iLines = new ArrayList<>();
        iLines.add("100 1");
        iLines.add("PKG1 50 30");
        CostItem costItem = new CostItem("PKG1", new BigDecimal(0), new BigDecimal(750));
        try (MockedStatic<Wrapper> mockedWrapper = mockStatic(Wrapper.class);
        MockedStatic<Parser> mockedParser = mockStatic(Parser.class);
        MockedStatic<Factory> mockedUtilities = mockStatic(Factory.class)) {
            mockedWrapper.when(()->Wrapper.getInputLines("input.txt")).thenReturn(iLines);
            CostContext context = new CostContext();
            context.costItems.add(costItem);
            mockedParser.when(()->Parser.parseCostContext(iLines)).thenReturn(context);
            String strOfferCodes = "";
            mockedWrapper.when(Wrapper::getConfiguration).thenReturn(strOfferCodes);
            Process mockedProcess = mock(Process.class);
            mockedUtilities.when(()-> Factory.getProcess("cost")).thenReturn(mockedProcess);
            Application.main(new String[]{"cost", "-i", "input.txt", "-o", "output.txt"});
            mockedWrapper.verify(()->Wrapper.getInputLines("input.txt"));
            mockedParser.verify(()->Parser.parseCostContext(iLines));
            mockedWrapper.verify(Wrapper::getConfiguration);
            mockedParser.verify(()->Parser.buildConfiguration(context, strOfferCodes));
            mockedWrapper.verify(()->Wrapper.showInputs(context));
            verify(mockedProcess).run(context);
            mockedWrapper.verify(()->Wrapper.showResults(context));
            mockedWrapper.verify(()->Wrapper.saveOutput("output.txt", context));
            mockedWrapper.verify(()->Wrapper.exit(0));
        }
    }

}
