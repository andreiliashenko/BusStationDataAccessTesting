package com.anli.busstation.dal.test.staff;

import com.anli.busstation.dal.interfaces.entities.staff.Salesman;
import com.anli.busstation.dal.interfaces.providers.staff.SalesmanProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public abstract class SalesmanTest extends BasicDataAccessTestSceleton<Salesman> {

    @Override
    protected BigInteger createEntityByProvider(Salesman sourceSalesman) throws Exception {
        SalesmanProvider provider = getFactory().getProvider(SalesmanProvider.class);
        Salesman salesman = provider.create();
        salesman.setHiringDate(sourceSalesman.getHiringDate());
        salesman.setName(sourceSalesman.getName());
        salesman.setSalary(sourceSalesman.getSalary());
        salesman.setTotalSales(sourceSalesman.getTotalSales());
        provider.save(salesman);
        return salesman.getId();
    }

    @Override
    protected Salesman getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(SalesmanProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Salesman sourceSalesman) throws Exception {
        SalesmanProvider provider = getFactory().getProvider(SalesmanProvider.class);
        Salesman salesman = provider.findById(id);
        salesman.setHiringDate(sourceSalesman.getHiringDate());
        salesman.setName(sourceSalesman.getName());
        salesman.setSalary(sourceSalesman.getSalary());
        salesman.setTotalSales(sourceSalesman.getTotalSales());
        provider.save(salesman);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        SalesmanProvider provider = getFactory().getProvider(SalesmanProvider.class);
        Salesman salesman = provider.findById(id);
        provider.remove(salesman);
    }

    @Override
    protected List<Salesman> getCreationTestSets() throws Exception {
        List<Salesman> testSets = new ArrayList<>(4);
        testSets.add(getNewSalesman(BigInteger.ZERO, null, null, null, null));
        testSets.add(getNewSalesman(BigInteger.ZERO, "", BigDecimal.ZERO, new DateTime(0),
                Integer.valueOf(0)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассир 1", BigDecimal.valueOf(20140.20),
                new DateTime(1992, 12, 31, 0, 0, 0, 0), Integer.valueOf(300)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассир 2", BigDecimal.valueOf(15205.77),
                new DateTime(2003, 2, 28, 0, 0, 0, 0), Integer.valueOf(400)));
        return testSets;
    }

    @Override
    protected List<Salesman> getUpdateTestSets() throws Exception {
        List<Salesman> testSets = new ArrayList<>(4);
        testSets.add(getNewSalesman(BigInteger.ZERO, "Новый Кассир 5", BigDecimal.valueOf(14041.56),
                new DateTime(1969, 11, 11, 0, 0, 0, 0), Integer.valueOf(20000)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Новый Кассир 6", BigDecimal.valueOf(8000.44),
                new DateTime(2013, 7, 30, 0, 0, 0, 0), Integer.valueOf(50)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "", BigDecimal.ZERO, new DateTime(0),
                Integer.valueOf(0)));
        testSets.add(getNewSalesman(BigInteger.ZERO, null, null, null, null));
        return testSets;
    }

    @Override
    protected List<List<Salesman>> performSearches() throws Exception {
        List<List<Salesman>> searchResult = new ArrayList<>(6);
        SalesmanProvider provider = getFactory().getProvider(SalesmanProvider.class);
        List<Salesman> resultCollection;
        resultCollection = provider.findByTotalSalesRange(Integer.valueOf(100), false,
                Integer.valueOf(900), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("Кассир 12");
        searchResult.add(resultCollection);
        resultCollection = provider.findByNameRegexp("ша");
        searchResult.add(resultCollection);
        resultCollection = provider.findByHiringDateRange(new DateTime(2000, 1, 2, 0, 0, 0, 0), true,
                new DateTime(2007, 5, 9, 0, 0, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySalaryRange(BigDecimal.valueOf(21500), false,
                BigDecimal.valueOf(33333.33), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(6);
        SalesmanProvider provider = getFactory().getProvider(SalesmanProvider.class);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByTotalSalesRange(Integer.valueOf(100), false,
                Integer.valueOf(900), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("Кассир 12");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByNameRegexp("ша");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByHiringDateRange(new DateTime(2000, 1, 2, 0, 0, 0, 0), true,
                new DateTime(2007, 5, 9, 0, 0, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySalaryRange(BigDecimal.valueOf(21500), false,
                BigDecimal.valueOf(33333.33), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<Salesman> getSearchTestSets() throws Exception {
        List<Salesman> testSets = new ArrayList<>(5);
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассир 10", BigDecimal.valueOf(18500.42),
                new DateTime(2000, 1, 2, 0, 0, 0, 0), null));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассирша 11", BigDecimal.valueOf(21499.99),
                new DateTime(2000, 1, 3, 0, 0, 0, 0), Integer.valueOf(100)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассир 12", BigDecimal.valueOf(21500),
                new DateTime(2003, 10, 31, 0, 0, 0, 0), Integer.valueOf(300)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Елена Михайлова", BigDecimal.valueOf(33333.33),
                new DateTime(2007, 5, 9, 0, 0, 0, 0), Integer.valueOf(900)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассирша 14", BigDecimal.valueOf(33333.34),
                new DateTime(2007, 5, 10, 0, 0, 0, 0), Integer.valueOf(901)));
        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(6);
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{2});
        searchResult.add(new int[]{1, 4});
        searchResult.add(new int[]{1, 2, 3});
        searchResult.add(new int[]{2});
        searchResult.add(new int[]{0, 1, 2, 3, 4});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected abstract Salesman getNewSalesman(BigInteger id, String name, BigDecimal salary,
            DateTime hiringDate, Integer totalSales);

    @Override
    protected Salesman nullifyCollections(Salesman entity) {
        return entity;
    }
}
