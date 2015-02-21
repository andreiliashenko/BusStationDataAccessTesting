package com.anli.busstation.dal.test.staff;

import com.anli.busstation.dal.interfaces.entities.staff.Driver;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.staff.Salesman;
import com.anli.busstation.dal.interfaces.providers.staff.DriverProvider;
import com.anli.busstation.dal.interfaces.providers.staff.DriverSkillProvider;
import com.anli.busstation.dal.interfaces.providers.staff.EmployeeProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicSkillProvider;
import com.anli.busstation.dal.interfaces.providers.staff.SalesmanProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class EmployeeTest extends BasicDataAccessTestSceleton<Employee> {

    protected Map<BigInteger, DriverSkill> driverSkills;

    protected Map<BigInteger, MechanicSkill> mechanicSkills;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        driverSkills = getFixtureCreator().createDriverSkillFixture(40, 5);
        mechanicSkills = getFixtureCreator().createMechanicSkillFixture(50, 5);
    }

    protected BigInteger createDriverByProvider(Employee entity) throws Exception {
        DriverProvider provider = getFactory().getProvider(DriverProvider.class);
        Driver driver = provider.create();
        Driver sourceDriver = (Driver) entity;
        driver.setHiringDate(sourceDriver.getHiringDate());
        driver.setName(sourceDriver.getName());
        driver.setSalary(sourceDriver.getSalary());
        driver.setSkill(sourceDriver.getSkill());

        provider.save(driver);
        return driver.getId();
    }

    protected BigInteger createMechanicByProvider(Employee entity) throws Exception {
        MechanicProvider provider = getFactory().getProvider(MechanicProvider.class);
        Mechanic mechanic = provider.create();
        Mechanic sourceMechanic = (Mechanic) entity;
        mechanic.setHiringDate(sourceMechanic.getHiringDate());
        mechanic.setName(sourceMechanic.getName());
        mechanic.setSalary(sourceMechanic.getSalary());
        mechanic.setSkill(sourceMechanic.getSkill());

        provider.save(mechanic);
        return mechanic.getId();
    }

    protected BigInteger createSalesmanByProvider(Employee entity) throws Exception {
        SalesmanProvider provider = getFactory().getProvider(SalesmanProvider.class);
        Salesman salesman = provider.create();
        Salesman sourceSalesman = (Salesman) entity;
        salesman.setHiringDate(sourceSalesman.getHiringDate());
        salesman.setName(sourceSalesman.getName());
        salesman.setSalary(sourceSalesman.getSalary());
        salesman.setTotalSales(sourceSalesman.getTotalSales());

        provider.save(salesman);
        return salesman.getId();
    }

    @Override
    protected BigInteger createEntityByProvider(Employee entity) throws Exception {
        if (entity instanceof Driver) {
            return createDriverByProvider(entity);
        }
        if (entity instanceof Mechanic) {
            return createMechanicByProvider(entity);
        }
        if (entity instanceof Salesman) {
            return createSalesmanByProvider(entity);
        }
        throw new Exception("Incorrect entity");
    }

    @Override
    protected Employee getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(EmployeeProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Employee sourceEmployee) throws Exception {
        EmployeeProvider provider = getFactory().getProvider(EmployeeProvider.class);
        Employee employee = provider.findById(id);
        employee.setHiringDate(sourceEmployee.getHiringDate());
        employee.setName(sourceEmployee.getName());
        employee.setSalary(sourceEmployee.getSalary());
        if (sourceEmployee instanceof Salesman) {
            ((Salesman) employee).setTotalSales(((Salesman) sourceEmployee).getTotalSales());
        }
        if (sourceEmployee instanceof Driver) {
            ((Driver) employee).setSkill(((Driver) sourceEmployee).getSkill());
        }
        if (sourceEmployee instanceof Mechanic) {
            ((Mechanic) employee).setSkill(((Mechanic) sourceEmployee).getSkill());
        }
        provider.save(employee);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        EmployeeProvider provider = getFactory().getProvider(EmployeeProvider.class);
        Employee employee = provider.findById(id);
        provider.remove(employee);
    }

    @Override
    protected List<Employee> getCreationTestSets() throws Exception {
        List<Employee> testSets = new ArrayList<>(6);
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик Механикович", BigDecimal.valueOf(33000.01), new DateTime(1997, 11, 30, 0, 0, 0, 0), BigInteger.valueOf(51)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик 2", BigDecimal.valueOf(25555.29), new DateTime(2011, 9, 1, 0, 0, 0, 0), BigInteger.valueOf(52)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Driver 1", BigDecimal.valueOf(50000.49), new DateTime(1995, 5, 12, 0, 0, 0, 0), BigInteger.valueOf(40)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Driver 2", BigDecimal.valueOf(62300.20), new DateTime(2010, 12, 12, 0, 0, 0, 0), BigInteger.valueOf(45)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассир 1", BigDecimal.valueOf(20140.20), new DateTime(1992, 12, 31, 0, 0, 0, 0), Integer.valueOf(300)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассир 2", BigDecimal.valueOf(15205.77), new DateTime(2003, 2, 28, 0, 0, 0, 0), Integer.valueOf(400)));
        return testSets;
    }

    @Override
    protected List<Employee> getUpdateTestSets() throws Exception {
        List<Employee> testSets = new ArrayList<>(6);
        testSets.add(getNewMechanic(BigInteger.ZERO, "Обновленный механик 1", BigDecimal.valueOf(8000.44), new DateTime(2005, 12, 31, 0, 0, 0, 0), BigInteger.valueOf(54)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "", BigDecimal.ZERO, new DateTime(0), BigInteger.valueOf(52)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Updated Driver 1", BigDecimal.valueOf(8000.44), new DateTime(2014, 3, 30, 0, 0, 0, 0), BigInteger.valueOf(43)));
        testSets.add(getNewDriver(BigInteger.ZERO, null, null, null, null));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Новый Кассир 5", BigDecimal.valueOf(14041.56), new DateTime(1969, 11, 11, 0, 0, 0, 0), Integer.valueOf(20000)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Новый Кассир 6", BigDecimal.valueOf(8000.44), new DateTime(2013, 7, 30, 0, 0, 0, 0), Integer.valueOf(50)));
        return testSets;
    }

    @Override
    protected List<Employee> getSearchTestSets() throws Exception {
        List<Employee> testSets = new ArrayList<>(8);

        testSets.add(getNewDriver(BigInteger.ZERO, null, BigDecimal.valueOf(7000), new DateTime(1998, 7, 7, 0, 0, 0, 0), BigInteger.valueOf(40)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "", BigDecimal.valueOf(15300.24), new DateTime(1998, 7, 8, 0, 0, 0, 0), BigInteger.valueOf(50)));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Нужное имя", BigDecimal.valueOf(15300.25), new DateTime(2000, 3, 11, 0, 0, 0, 0), null));
        testSets.add(getNewSalesman(BigInteger.ZERO, "Кассир 3", BigDecimal.valueOf(18999.99), new DateTime(2000, 5, 21, 0, 0, 0, 0), Integer.valueOf(500)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Нужное имя", null, new DateTime(2005, 1, 1, 0, 0, 0, 0), BigInteger.valueOf(43)));
        testSets.add(getNewDriver(BigInteger.ZERO, "Водитель 7", BigDecimal.valueOf(20500.23), null, BigInteger.valueOf(44)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Нужное имя", BigDecimal.valueOf(25252.52), new DateTime(2007, 11, 10, 0, 0, 0, 0), null));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик 2", BigDecimal.valueOf(25252.53), new DateTime(2007, 11, 13, 0, 0, 0, 0), BigInteger.valueOf(52)));
        return testSets;
    }

    @Override
    protected List<List<Employee>> performSearches() throws Exception {
        List<List<Employee>> searchResult = new ArrayList<>(5);

        EmployeeProvider provider = getFactory().getProvider(EmployeeProvider.class);
        List<Employee> resultCollection;

        resultCollection = provider.findByName("Нужное имя");
        searchResult.add(resultCollection);
        resultCollection = provider.findByNameRegexp("[0-9]$");
        searchResult.add(resultCollection);
        resultCollection = provider.findByHiringDateRange(new DateTime(1998, 7, 7, 0, 0, 0, 0), false,
                new DateTime(2007, 11, 10, 0, 0, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySalaryRange(BigDecimal.valueOf(15300.25),
                true, BigDecimal.valueOf(25252.52), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(5);

        EmployeeProvider provider = getFactory().getProvider(EmployeeProvider.class);
        List<BigInteger> resultCollection;

        resultCollection = provider.collectIdsByName("Нужное имя");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByNameRegexp("[0-9]$");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByHiringDateRange(new DateTime(1998, 7, 7, 0, 0, 0, 0), false,
                new DateTime(2007, 11, 10, 0, 0, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySalaryRange(BigDecimal.valueOf(15300.25), true,
                BigDecimal.valueOf(25252.52), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(5);
        searchResult.add(new int[]{2, 4, 6});
        searchResult.add(new int[]{3, 5, 7});
        searchResult.add(new int[]{0, 1, 2, 3, 4});
        searchResult.add(new int[]{3, 5, 6});
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7});
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

    protected DriverSkill getDriverSkillById(BigInteger skillId) {
        return getDriverSkillById(skillId, false);
    }

    protected DriverSkill getDriverSkillById(BigInteger skillId, boolean load) {
        return load ? getFactory().getProvider(DriverSkillProvider.class).findById(skillId) : driverSkills.get(skillId);
    }

    protected Mechanic getNewMechanic(BigInteger id, String name, BigDecimal salary, DateTime hiringDate, BigInteger skillId) {
        return getNewMechanic(id, name, salary, hiringDate, skillId, false);
    }

    protected abstract Mechanic getNewMechanic(BigInteger id, String name, BigDecimal salary,
            DateTime hiringDate, BigInteger skillId, boolean load);

    protected MechanicSkill getMechanicSkillById(BigInteger skillId) {
        return getMechanicSkillById(skillId, false);
    }

    protected MechanicSkill getMechanicSkillById(BigInteger skillId, boolean load) {
        return load ? getFactory().getProvider(MechanicSkillProvider.class).findById(skillId) : mechanicSkills.get(skillId);
    }

    protected abstract Salesman getNewSalesman(BigInteger id, String name, BigDecimal salary,
            DateTime hiringDate, Integer totalSales);

    @Override
    protected Employee nullifyCollections(Employee entity) {
        return entity;
    }
}
