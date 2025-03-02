package Delivery;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class DeliveryTests {

    @DataProvider(name = "fragileOver30KmData")
    public Object[][] fragileOver30KmData() {
        return new Object[][]{
                {9999, false, true, "ordinary"},
                {30.01, true, true, "ordinary"}
        };
    }

    @Test(dataProvider = "fragileOver30KmData", expectedExceptions = IllegalArgumentException.class,
            description = "Checks that if the cargo is fragile and the distance is more than 30 km, an exception is thrown.", groups = {"smoke", "regress"})
    public void testFragileOver30Km(double distance, boolean isLarge, boolean isFragile, String workload) {
        DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload);
    }

    @DataProvider(name = "minDeliveryCostData")
    public Object[][] minDeliveryCostData() {
        return new Object[][]{
                {1, false, false, "ordinary", 400},
                {5, false, false, "ordinary", 400},
                {30, false, false, "ordinary", 400}
        };
    }

    @Test(dataProvider = "minDeliveryCostData", description = "Checks that if the calculated cost is less than the minimum, the minimum price is set.", groups = {"smoke", "regress"})
    public void testMinDeliveryCost(double distance, boolean isLarge, boolean isFragile, String workload, double expected) {
        double cost = DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload);
        assertEquals(cost, expected, "Expected minimum delivery cost");
    }

    @DataProvider(name = "testDifferentWorkloads")
    public Object[][] testDifferentWorkloads() {
        return new Object[][]{
                {10, false, true, "very high", 800},
                {11, false, true, "increased", 720},
                {3, true, true, "increased", 720},
                {2, true, true, "ordinary", 550},
                {0.1, false, true, "ordinary", 450},
                {31, false, false, "very high", 640},
                {31, true, false, "high", 700},
                {9999, false, false, "high", 560}
        };
    }

    @Test(dataProvider = "testDifferentWorkloads", description = "Checks the cost calculation for small fragile cargo with different loading conditions.", groups = {"smoke", "regress"})
    public void testDifferentWorkloads(double distance, boolean isLarge, boolean isFragile, String workload, double expected) {
        double cost = DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload);
        assertEquals(cost, expected, "Unexpected delivery cost");
    }

    @DataProvider(name = "negativeValuesData")
    public Object[][] negativeValuesData() {
        return new Object[][]{
                {-1, false, false, "ordinary"},
                {0, false, false, "ordinary"},
                {10, false, false, ""},
                {10, false, false, "invalid"}
        };
    }

    @Test(dataProvider = "negativeValuesData", expectedExceptions = IllegalArgumentException.class,
            description = "Proves that the method correctly handles incorrect input data.", groups = {"smoke", "regress"})
    public void testHandleNegativeValues(double distance, boolean isLarge, boolean isFragile, String workload) {
        DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload);
    }
}
