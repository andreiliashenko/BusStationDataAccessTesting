package com.anli.busstation.dal.test.staff;

import com.anli.busstation.dal.interfaces.entities.staff.Driver;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.providers.staff.DriverProvider;
import com.anli.busstation.dal.interfaces.providers.staff.DriverSkillProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class DriverTest extends BasicDataAccessTestSceleton<Driver> {

    protected Map<BigInteger, DriverSkill> driverSkills;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        driverSkills = getFixtureCreator().createDriverSkillFixture(40, 5);
    }

    @Override
    protected BigInteger createEntityByProvider(Driver sourceDriver) throws Exception {
        DriverProvider provider = getFactory().getProvider(DriverProvider.class);
        Driver driver = provider.create();
        driver.setHiringDate(sourceDriver.getHiringDate());
        driver.setName(sourceDriver.getName());
        driver.setSalary(sourceDriver.getSalary());
        driver.setSkill(sourceDriver.getSkill());

        provider.save(driver);
        return driver.getId();
    }

    @Override
    protected Driver getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(DriverProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Driver sourceDriver) throws Exception {
        DriverProvider provider = getFactory().getProvider(DriverProvider.class);
        Driver driver = provider.findById(id);
        driver.setHiringDate(sourceDriver.getHiringDate());
        driver.setName(sourceDriver.getName());
        driver.setSalary(sourceDriver.getSalary());
        driver.setSkill(sourceDriver.getSkill());
        provider.save(driver);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        DriverProvider provider = getFactory().getProvider(DriverProvider.class);
        Driver driver = provider.findById(id);
        provider.remove(driver);
    }

    @Override
    protected List<Driver> getCreationTestSets() throws Exception {
        List<Driver> testSets = new ArrayList<>(4);
        testSets.add(getNewDriver(BigInteger.ZERO, null, null, null, null));
        testSets.add(getNewDriver(BigInteger.ZERO, "", BigDecimal.ZERO, new DateTime(0), null));
        testSets.add(getNewDriver(BigInteger.ZERO, "Driver 1", BigDecimal.valueOf(50000.49), new DateTime(1995, 5, 12, 0, 0, 0, 0), BigInteger.valueOf(40)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Driver 2", BigDecimal.valueOf(62300.20), new DateTime(2010, 12, 12, 0, 0, 0, 0), BigInteger.valueOf(45)));

        return testSets;
    }

    @Override
    protected List<Driver> getUpdateTestSets() throws Exception {
        List<Driver> testSets = new ArrayList<>(4);
        testSets.add(getNewDriver(BigInteger.ZERO, "Updated Driver 0", BigDecimal.valueOf(15000), new DateTime(1999, 9, 11, 0, 0, 0, 0), BigInteger.valueOf(44)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Updated Driver 1", BigDecimal.valueOf(8000.44), new DateTime(2014, 3, 30, 0, 0, 0, 0), BigInteger.valueOf(43)));
        testSets.add(getNewDriver(BigInteger.ZERO, "", BigDecimal.ZERO, new DateTime(0), BigInteger.valueOf(42)));
        testSets.add(getNewDriver(BigInteger.ZERO, null, null, null, null));

        return testSets;
    }

    @Override
    protected List<List<Driver>> performSearches() throws Exception {
        List<List<Driver>> searchResult = new ArrayList<>(8);

        DriverProvider provider = getFactory().getProvider(DriverProvider.class);
        List<Driver> resultCollection;

        resultCollection = provider.findBySkill(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySkill(getSkillById(BigInteger.valueOf(41)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnySkill(Arrays.asList(getSkillById(BigInteger.valueOf(40)),
                getSkillById(BigInteger.valueOf(42)), getSkillById(BigInteger.valueOf(43))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("Driver Name 4");
        searchResult.add(resultCollection);
        resultCollection = provider.findByNameRegexp("^Dr");
        searchResult.add(resultCollection);
        resultCollection = provider.findByHiringDateRange(new DateTime(1996, 6, 6, 0, 0, 0, 0), true,
                new DateTime(2000, 1, 25, 0, 0, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySalaryRange(BigDecimal.valueOf(15125.50), false,
                BigDecimal.valueOf(26777.89), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(8);

        DriverProvider provider = getFactory().getProvider(DriverProvider.class);
        List<BigInteger> resultCollection;

        resultCollection = provider.collectIdsBySkill(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySkill(getSkillById(BigInteger.valueOf(41)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnySkill(Arrays.asList(getSkillById(BigInteger.valueOf(40)),
                getSkillById(BigInteger.valueOf(42)), getSkillById(BigInteger.valueOf(43))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("Driver Name 4");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByNameRegexp("^Dr");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByHiringDateRange(new DateTime(1996, 6, 6, 0, 0, 0, 0), true,
                new DateTime(2000, 1, 25, 0, 0, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySalaryRange(BigDecimal.valueOf(15125.50), false,
                BigDecimal.valueOf(26777.89), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<Driver> getSearchTestSets() throws Exception {
        List<Driver> testSets = new ArrayList<>(5);

        testSets.add(getNewDriver(BigInteger.ZERO, null, BigDecimal.valueOf(14000), null, null));
        testSets.add(getNewDriver(BigInteger.ZERO, "Driver Name", BigDecimal.valueOf(15125.50), new DateTime(1995, 1, 1, 0, 0, 0, 0), BigInteger.valueOf(40)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Driver Name 4", BigDecimal.valueOf(16500.22), new DateTime(1996, 6, 6, 0, 0, 0, 0), BigInteger.valueOf(41)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Unusual Name", BigDecimal.valueOf(26777.89), new DateTime(1997, 12, 31, 0, 0, 0, 0), BigInteger.valueOf(42)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Driver Name 7", BigDecimal.valueOf(40000), new DateTime(2000, 1, 25, 0, 0, 0, 0), BigInteger.valueOf(44)));

        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(8);
        searchResult.add(new int[]{0});
        searchResult.add(new int[]{2});
        searchResult.add(new int[]{1, 3});
        searchResult.add(new int[]{2});
        searchResult.add(new int[]{1, 2, 4});
        searchResult.add(new int[]{3, 4});
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{0, 1, 2, 3, 4});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Driver getNewDriver(BigInteger id, String name, BigDecimal salary, DateTime hiringDate, BigInteger skillId) {
        return getNewDriver(id, name, salary, hiringDate, skillId, false);
    }

    protected abstract Driver getNewDriver(BigInteger id, String name, BigDecimal salary, DateTime hiringDate,
            BigInteger skillId, boolean load);

    protected DriverSkill getSkillById(BigInteger skillId) {
        return getSkillById(skillId, false);
    }

    protected DriverSkill getSkillById(BigInteger skillId, boolean load) {
        return load ? getFactory().getProvider(DriverSkillProvider.class).findById(skillId) : driverSkills.get(skillId);
    }

    @Override
    protected Driver nullifyCollections(Driver entity) {
        return entity;
    }
}
