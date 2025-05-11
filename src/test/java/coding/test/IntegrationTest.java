package coding.test;

import coding.test.model.ClientId;
import coding.test.model.Stock;
import coding.test.model.Symbol;
import coding.test.sellside.NasdaqNaive;
import coding.test.sellside.SubServiceImpl;
import coding.test.user.Client;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IntegrationTest {

    private final static ClientId cId1 = new ClientId("c1");
    private final static ClientId cId2 = new ClientId("c2");
    private final static ClientId cId3 = new ClientId("c1");

    private final static Symbol sA = new Symbol("A");
    private final static Symbol sB = new Symbol("B");
    private final static Symbol sC = new Symbol("C");
    private final static Symbol sD = new Symbol("D");
    private final static Symbol sE = new Symbol("E");
    private final static Symbol sF = new Symbol("F");

    private Client c1;
    private Client c2;
    private Client c3;

    private SubServiceImpl subService;

    private NasdaqNaive nasdaq;

    @Before
    public void setUp() {

        nasdaq = new NasdaqNaive();
        subService = new SubServiceImpl(nasdaq);

        c1 = new Client(cId1, subService);
        c2 = new Client(cId2, subService);
        c3 = new Client(cId3, subService);

        c1.start(Arrays.asList(sA, sB));
        c2.start(Arrays.asList(sB, sC));
        c3.start(Arrays.asList(sD));
    }

    @Test
    public void testEndToEndHappyPath() {

        Stock stA1 = new Stock(sA, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stB1 = new Stock(sB, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stC1 = new Stock(sC, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stD1 = new Stock(sD, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stE1 = new Stock(sE, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stF1 = new Stock(sF, null, LocalDateTime.of(2020, 1, 10, 10, 10));

        Stock stA2 = new Stock(sA, null, LocalDateTime.of(2020, 1, 10, 10, 11));
        Stock stC2 = new Stock(sC, null, LocalDateTime.of(2020, 1, 10, 10, 11));

        nasdaq.update(stA1);
        nasdaq.update(stB1);
        nasdaq.update(stC1);
        nasdaq.update(stD1);
        nasdaq.update(stE1);
        nasdaq.update(stF1);

        nasdaq.update(stA2);
        nasdaq.update(stC2);

        // data check @ server level
        assertEquals(stA2, subService.getStock(sA));
        assertEquals(stB1, subService.getStock(sB));
        assertEquals(stC2, subService.getStock(sC));
        assertEquals(stD1, subService.getStock(sD));
        assertNull(subService.getStock(sE));
        assertNull(subService.getStock(sF));

        // data check @ client level
        assertEquals(stA2, c1.getStock(sA));
        assertEquals(stB1, c1.getStock(sB));
        assertEquals(stB1, c2.getStock(sB));
        assertEquals(stC2, c2.getStock(sC));
        assertEquals(stD1, c3.getStock(sD));
        assertNull(c1.getStock(sC));
        assertNull(c1.getStock(sD));
        assertNull(c2.getStock(sA));
        assertNull(c2.getStock(sD));
        assertNull(c3.getStock(sA));
        assertNull(c3.getStock(sB));
        assertNull(c3.getStock(sC));
    }

    @Test
    public void testEndToEndClientDisconnectScenario1() {

        Stock stA1 = new Stock(sA, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stB1 = new Stock(sB, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stC1 = new Stock(sC, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stD1 = new Stock(sD, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stE1 = new Stock(sE, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stF1 = new Stock(sF, null, LocalDateTime.of(2020, 1, 10, 10, 10));

        Stock stA2 = new Stock(sA, null, LocalDateTime.of(2020, 1, 10, 10, 11));
        Stock stC2 = new Stock(sC, null, LocalDateTime.of(2020, 1, 10, 10, 11));

        nasdaq.update(stA1);
        nasdaq.update(stB1);
        nasdaq.update(stC1);
        nasdaq.update(stD1);
        nasdaq.update(stE1);
        nasdaq.update(stF1);

        nasdaq.update(stA2);
        nasdaq.update(stC2);

        c3.stop();

        Stock stD2 = new Stock(sD, null, LocalDateTime.of(2020, 1, 10, 10, 11));

        nasdaq.update(stD2);

        // data check @ server level
        assertEquals(stA2, subService.getStock(sA));
        assertEquals(stB1, subService.getStock(sB));
        assertEquals(stC2, subService.getStock(sC));
        assertNull(subService.getStock(sD));            //  no longer receiving updates
        assertNull(subService.getStock(sE));
        assertNull(subService.getStock(sF));

        // data check @ client level
        assertEquals(stA2, c1.getStock(sA));
        assertEquals(stB1, c1.getStock(sB));
        assertEquals(stB1, c2.getStock(sB));
        assertEquals(stC2, c2.getStock(sC));
        assertNull(c3.getStock(sD));                    //  no longer receiving updates
        assertNull(c1.getStock(sC));
        assertNull(c1.getStock(sD));
        assertNull(c2.getStock(sA));
        assertNull(c2.getStock(sD));
        assertNull(c3.getStock(sA));
        assertNull(c3.getStock(sB));
        assertNull(c3.getStock(sC));
    }

    @Test
    public void testEndToEndClientDisconnectScenario2() {

        Stock stA1 = new Stock(sA, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stB1 = new Stock(sB, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stC1 = new Stock(sC, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stD1 = new Stock(sD, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stE1 = new Stock(sE, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stF1 = new Stock(sF, null, LocalDateTime.of(2020, 1, 10, 10, 10));

        Stock stA2 = new Stock(sA, null, LocalDateTime.of(2020, 1, 10, 10, 11));

        nasdaq.update(stA1);
        nasdaq.update(stB1);
        nasdaq.update(stC1);
        nasdaq.update(stD1);
        nasdaq.update(stE1);
        nasdaq.update(stF1);

        nasdaq.update(stA2);

        c2.stop();

        Stock stC2 = new Stock(sC, null, LocalDateTime.of(2020, 1, 10, 10, 11));
        Stock stD2 = new Stock(sD, null, LocalDateTime.of(2020, 1, 10, 10, 11));

        nasdaq.update(stC2);
        nasdaq.update(stD2);

        // data check @ server level
        assertEquals(stA2, subService.getStock(sA));
        assertEquals(stB1, subService.getStock(sB));
        assertEquals(stD2, subService.getStock(sD));
        assertNull(subService.getStock(sC));            //  no longer receiving updates for C
        assertNull(subService.getStock(sE));
        assertNull(subService.getStock(sF));

        // data check @ client level
        assertEquals(stA2, c1.getStock(sA));
        assertEquals(stB1, c1.getStock(sB));
        assertNull(c2.getStock(sB));                    //  no longer receiving updates for B
        assertNull(c2.getStock(sC));                    //  no longer receiving updates for C
        assertEquals(stD2, c3.getStock(sD));
        assertNull(c1.getStock(sC));
        assertNull(c1.getStock(sD));
        assertNull(c2.getStock(sA));
        assertNull(c2.getStock(sD));
        assertNull(c3.getStock(sA));
        assertNull(c3.getStock(sB));
        assertNull(c3.getStock(sC));
    }
}
