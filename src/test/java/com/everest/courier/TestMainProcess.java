package com.everest.courier;


import com.everest.courier.Exceptions.EverestException;
import com.everest.courier.Exceptions.UserInputException;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestMainProcess {
    private void testMain_argumentVoid(String[] args) {
        Wrapper mockedWrapper = mock(Wrapper.class);
        Factory mockedFactory = mock(Factory.class);
        when(mockedFactory.getWrapper()).thenReturn(mockedWrapper);
        new MainProcess(mockedFactory, args).run(null);
        verify(mockedWrapper).showVersion();
        verify(mockedWrapper).showCopyRight();
        verify(mockedWrapper).showHelp();
        verify(mockedWrapper).exit(-1);
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
        Factory mockedFactory = mock(Factory.class);
        Wrapper mockedWrapper = mock(Wrapper.class);
        when(mockedFactory.getWrapper()).thenReturn(mockedWrapper);
        new MainProcess(mockedFactory, args).run(null);
        verify(mockedWrapper).showError(msg);
        verify(mockedWrapper).showHelp();
        verify(mockedWrapper).exit(-1);
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
    public void testMain_inputFileNotFound() throws FileNotFoundException {
        Wrapper mockedWrapper = mock(Wrapper.class);
        Factory mockedFactory = mock(Factory.class);
        when(mockedFactory.getWrapper()).thenReturn(mockedWrapper);
        FileNotFoundException exception = new FileNotFoundException();
        when(mockedWrapper.getInputLines("input.txt")).thenThrow(exception);
        new MainProcess(mockedFactory, new String[]{"cost", "-i", "input.txt", "-o", "output.txt"}).run(null);
        verify(mockedWrapper).getInputLines("input.txt");
        verify(mockedWrapper).showError(exception);
        verify(mockedWrapper).exit(-1);
    }

    @Test
    public void testMain_inputFileEmpty() throws FileNotFoundException, EverestException {
        Wrapper mockedWrapper = mock(Wrapper.class);
        Factory mockedFactory = mock(Factory.class);
        when(mockedFactory.getWrapper()).thenReturn(mockedWrapper);
        when(mockedWrapper.getInputLines("input.txt")).thenReturn(new ArrayList<>());
        when(mockedFactory.getContext(eq(ServiceType.COST), anyList())).thenThrow(new UserInputException("User Input Empty"));
        new MainProcess(mockedFactory, new String[]{"cost", "-i", "input.txt", "-o", "output.txt"}).run(null);
        verify(mockedWrapper).getInputLines("input.txt");
        verify(mockedWrapper).showError("User Input Empty");
        verify(mockedWrapper).exit(-1);
    }

    @Test
    public void testMain_invalidInputLines() throws FileNotFoundException, EverestException {
        List<String> lines = new ArrayList<>();
        lines.add("junk");
        String msg = "Invalid format - field base_delivery_cost";
        Wrapper mockedWrapper = mock(Wrapper.class);
        Factory mockedFactory = mock(Factory.class);
        Parser mockedParser = mock(Parser.class);
        when(mockedFactory.getWrapper()).thenReturn(mockedWrapper);
        when(mockedFactory.getParser()).thenReturn(mockedParser);
        when(mockedWrapper.getInputLines("input.txt")).thenReturn(lines);
        when(mockedFactory.getContext(ServiceType.COST, lines)).thenThrow(new UserInputException(msg));
        new MainProcess(mockedFactory, new String[]{"cost", "-i", "input.txt", "-o", "output.txt"}).run(null);
        verify(mockedWrapper).getInputLines("input.txt");
        verify(mockedFactory).getContext(ServiceType.COST, lines);
        verify(mockedWrapper).showError(msg);
        verify(mockedWrapper).exit(-1);
    }

    @Test
    public void testMain() throws IOException, EverestException, ParseException {
        List<String> iLines = new ArrayList<>();
        iLines.add("100 1");
        iLines.add("PKG1 50 30");
        ResultItem item = new ResultItem("PKG1", new BigDecimal(0), new BigDecimal(750));
        Factory mockedFactory = mock(Factory.class);
        Wrapper mockedWrapper = mock(Wrapper.class);
        when(mockedFactory.getWrapper()).thenReturn(mockedWrapper);
        Parser mockedParser = mock(Parser.class);
        when(mockedFactory.getParser()).thenReturn(mockedParser);
        when(mockedWrapper.getInputLines("input.txt")).thenReturn(iLines);
        CostProcess mockedProcess = mock(CostProcess.class);
        when(mockedFactory.getProcess(ServiceType.COST)).thenReturn(mockedProcess);
        Context context = new Context();
        context.type = ServiceType.COST;
        context.resultItems.add(item);
        when(mockedFactory.getContext(ServiceType.COST, iLines)).thenReturn(context);
        String strOfferCodes = "";
        when(mockedWrapper.getConfiguration()).thenReturn(strOfferCodes);
        new MainProcess(mockedFactory, new String[]{"cost", "-i", "input.txt", "-o", "output.txt"}).run(null);
        verify(mockedWrapper).getInputLines("input.txt");
        verify(mockedFactory).getProcess(ServiceType.COST);
        verify(mockedFactory).getContext(ServiceType.COST, iLines);
        verify(mockedWrapper).getConfiguration();
        verify(mockedParser).buildConfiguration(context, strOfferCodes);
        verify(mockedWrapper).showInputs(context);
        verify(mockedProcess).run(context);
        verify(mockedWrapper).showResults(context);
        verify(mockedWrapper).saveOutput("output.txt", context);
        verify(mockedWrapper).exit(0);
    }

}
