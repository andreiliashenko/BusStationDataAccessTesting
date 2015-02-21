package com.anli.busstation.dal.test.geography;

import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.interfaces.providers.staff.EmployeeProvider;
import com.anli.busstation.dal.interfaces.providers.geography.StationProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StationTest extends BasicDataAccessTestSceleton<Station> {

    protected Map<BigInteger, Bus> buses;
    protected Map<BigInteger, Employee> employees;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, GasLabel> gasLabels = getFixtureCreator().createGasLabelFixture(10, 5);

        Map<BigInteger, Model> models = getFixtureCreator().createModelFixture(20, 5,
                new ArrayList(gasLabels.values()));

        Map<BigInteger, TechnicalState> technicalStates = getFixtureCreator().createTechnicalStateFixture(30, 5);

        buses = getFixtureCreator().createBusFixture(60, 10, new ArrayList(models.values()),
                new ArrayList(technicalStates.values()));

        Map<BigInteger, DriverSkill> driverSkills = getFixtureCreator().createDriverSkillFixture(40, 5);
        Map<BigInteger, MechanicSkill> mechanicSkills = getFixtureCreator().createMechanicSkillFixture(50, 5);

        employees = new HashMap<>();
        employees.putAll(getFixtureCreator().createDriverFixture(70, 5, new ArrayList(driverSkills.values())));
        employees.putAll(getFixtureCreator().createMechanicFixture(80, 5, new ArrayList(mechanicSkills.values())));
        employees.putAll(getFixtureCreator().createSalesmanFixture(90, 5));
    }

    @Override
    protected BigInteger createEntityByProvider(Station sourceStation) throws Exception {
        StationProvider provider = getFactory().getProvider(StationProvider.class);
        Station station = provider.create();
        station = provider.pullBuses(station);
        station = provider.pullEmployees(station);
        station.setLatitude(sourceStation.getLatitude());
        station.setLongitude(sourceStation.getLongitude());
        station.setName(sourceStation.getName());
        station.getBuses().clear();
        station.getBuses().addAll(sourceStation.getBuses());
        station.getEmployees().clear();
        station.getEmployees().addAll(sourceStation.getEmployees());
        provider.save(station);
        return station.getId();
    }

    @Override
    protected Station getEntityByProvider(BigInteger id) throws Exception {
        StationProvider provider = getFactory().getProvider(StationProvider.class);
        Station station = provider.findById(id);
        return station;
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Station sourceStation) throws Exception {
        StationProvider provider = getFactory().getProvider(StationProvider.class);
        Station station = provider.findById(id);
        station = provider.pullBuses(station);
        station = provider.pullEmployees(station);
        station.setLatitude(sourceStation.getLatitude());
        station.setLongitude(sourceStation.getLongitude());
        station.setName(sourceStation.getName());
        station.getBuses().clear();
        station.getBuses().addAll(sourceStation.getBuses());
        station.getEmployees().clear();
        station.getEmployees().addAll(sourceStation.getEmployees());
        provider.save(station);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        StationProvider provider = getFactory().getProvider(StationProvider.class);
        Station station = provider.findById(id);
        provider.remove(station);
    }

    @Override
    protected boolean hasCollections() {
        return true;
    }

    @Override
    protected Station pull(Station entity, int index) {
        if (index == 0 || index == 2) {
            return getFactory().getProvider(StationProvider.class).pullBuses(entity);
        }
        if (index == 1 || index == 3) {
            return getFactory().getProvider(StationProvider.class).pullEmployees(entity);
        }
        return entity;
    }

    @Override
    protected List<Station> getPullEtalons() throws Exception {
        List<Station> stations = getCreationTestSets();
        nullifyEmployees(stations.get(0));
        nullifyBuses(stations.get(1));
        nullifyEmployees(stations.get(2));
        nullifyBuses(stations.get(3));
        return stations;
    }

    protected abstract void nullifyBuses(Station station);

    protected abstract void nullifyEmployees(Station station);

    @Override
    protected List<Station> getCreationTestSets() throws Exception {
        List<BigInteger> busList1 = Arrays.asList(BigInteger.valueOf(60));
        List<BigInteger> busList2 = Arrays.asList(BigInteger.valueOf(61), BigInteger.valueOf(62), BigInteger.valueOf(63));
        List<BigInteger> empList1 = Arrays.asList(BigInteger.valueOf(70), BigInteger.valueOf(80));
        List<BigInteger> empList2 = Arrays.asList(BigInteger.valueOf(71), BigInteger.valueOf(72), BigInteger.valueOf(81), BigInteger.valueOf(90));
        List<Station> testSets = new ArrayList<>(4);
        testSets.add(getNewStation(BigInteger.ZERO, null, null, null, new ArrayList(), new ArrayList()));
        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", new ArrayList(), new ArrayList()));
        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.valueOf(56.6789), BigDecimal.valueOf(78.8894), "Станция 3", busList1, empList1));
        testSets.add(getNewStation(BigInteger.ZERO, null, null, null, busList2, empList2));

        return testSets;
    }

    @Override
    protected List<Station> getUpdateTestSets() throws Exception {
        List<BigInteger> busList1 = Arrays.asList(BigInteger.valueOf(63));
        List<BigInteger> busList2 = Arrays.asList(BigInteger.valueOf(64), BigInteger.valueOf(65), BigInteger.valueOf(68));
        List<BigInteger> empList1 = Arrays.asList(BigInteger.valueOf(73), BigInteger.valueOf(84), BigInteger.valueOf(92));
        List<BigInteger> empList2 = Arrays.asList(BigInteger.valueOf(74), BigInteger.valueOf(80), BigInteger.valueOf(91), BigInteger.valueOf(93));
        List<Station> testSets = new ArrayList<>(4);
        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.valueOf(99.9909), BigDecimal.ZERO, "Новая Станция", busList1, new ArrayList()));
        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.valueOf(89.3456), BigDecimal.valueOf(83.3444), "Очередная", busList2, empList1));
        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "Станция 3", new ArrayList(), empList2));
        testSets.add(getNewStation(BigInteger.ZERO, null, null, null, new ArrayList(), new ArrayList()));

        return testSets;
    }

    @Override
    protected List<List<Station>> performSearches() throws Exception {
        List<List<Station>> searchResult = new ArrayList<>(9);
        StationProvider provider = getFactory().getProvider(StationProvider.class);
        List<Station> resultCollection;
        List<BigInteger> busList = Arrays.asList(BigInteger.valueOf(61), BigInteger.valueOf(62), BigInteger.valueOf(68));
        List<BigInteger> employeeList = Arrays.asList(BigInteger.valueOf(71), BigInteger.valueOf(82), BigInteger.valueOf(84), BigInteger.valueOf(93));

        resultCollection = provider.findByBus(getBusById(BigInteger.valueOf(63)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyBus(getBusesByIds(busList));
        searchResult.add(resultCollection);
        resultCollection = provider.findByEmployee(getEmployeeById(BigInteger.valueOf(93)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyEmployee(getEmployeesByIds(employeeList));
        searchResult.add(resultCollection);
        resultCollection = provider.findByLatitudeRange(BigDecimal.valueOf(60.601), false, BigDecimal.valueOf(80.345), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByLongitudeRange(BigDecimal.valueOf(105.4567), true, BigDecimal.valueOf(189.345), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("Нужное имя");
        searchResult.add(resultCollection);
        resultCollection = provider.findByNameRegexp("[0-9][0-9][0-9]");
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(9);
        StationProvider provider = getFactory().getProvider(StationProvider.class);
        List<BigInteger> resultCollection;
        List<BigInteger> busList = Arrays.asList(BigInteger.valueOf(61), BigInteger.valueOf(62), BigInteger.valueOf(68));
        List<BigInteger> employeeList = Arrays.asList(BigInteger.valueOf(71), BigInteger.valueOf(82), BigInteger.valueOf(84), BigInteger.valueOf(93));

        resultCollection = provider.collectIdsByBus(getBusById(BigInteger.valueOf(63)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyBus(getBusesByIds(busList));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByEmployee(getEmployeeById(BigInteger.valueOf(93)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyEmployee(getEmployeesByIds(employeeList));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByLatitudeRange(BigDecimal.valueOf(60.601), false,
                BigDecimal.valueOf(80.345), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByLongitudeRange(BigDecimal.valueOf(105.4567), true,
                BigDecimal.valueOf(189.345), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("Нужное имя");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByNameRegexp("[0-9][0-9][0-9]");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<Station> getSearchTestSets() throws Exception {
        List<BigInteger> busList1 = Arrays.asList(BigInteger.valueOf(61), BigInteger.valueOf(62), BigInteger.valueOf(63));
        List<BigInteger> busList2 = Arrays.asList(BigInteger.valueOf(64), BigInteger.valueOf(65), BigInteger.valueOf(66));
        List<BigInteger> busList3 = Arrays.asList(BigInteger.valueOf(67), BigInteger.valueOf(68), BigInteger.valueOf(69));

        List<BigInteger> empList1 = Arrays.asList(BigInteger.valueOf(71), BigInteger.valueOf(81), BigInteger.valueOf(91), BigInteger.valueOf(92));
        List<BigInteger> empList2 = Arrays.asList(BigInteger.valueOf(72), BigInteger.valueOf(73), BigInteger.valueOf(82), BigInteger.valueOf(93));
        List<BigInteger> empList3 = Arrays.asList(BigInteger.valueOf(74), BigInteger.valueOf(83), BigInteger.valueOf(84), BigInteger.valueOf(94));
        List<Station> testSets = new ArrayList<>(4);

        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.valueOf(49.500), BigDecimal.valueOf(95.89), "ЫПЛДОЫпп", busList1, empList1));
        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.valueOf(60.600), BigDecimal.valueOf(105.4567), "Хорошее имя", busList2, empList2));
        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.valueOf(60.601), BigDecimal.valueOf(150.1111), "Плохое имя", busList3, new ArrayList()));
        testSets.add(getNewStation(BigInteger.ZERO, BigDecimal.valueOf(80.345), BigDecimal.valueOf(189.345), "Нужное имя", new ArrayList(), empList3));

        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(9);

        searchResult.add(new int[]{0});
        searchResult.add(new int[]{0, 2});
        searchResult.add(new int[]{1});
        searchResult.add(new int[]{0, 1, 3});
        searchResult.add(new int[]{2});
        searchResult.add(new int[]{2, 3});
        searchResult.add(new int[]{3});
        searchResult.add(new int[]{});
        searchResult.add(new int[]{0, 1, 2, 3});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Station getNewStation(BigInteger id, BigDecimal latitude, BigDecimal longitude, String name, List<BigInteger> busList, List<BigInteger> employeeList) {
        return getNewStation(id, latitude, longitude, name, busList, employeeList, false);
    }

    protected abstract Station getNewStation(BigInteger id, BigDecimal latitude, BigDecimal longitude, String name,
            List<BigInteger> busList, List<BigInteger> employeeList, boolean load);

    protected List<Bus> getBusesByIds(List<BigInteger> ids) {
        return getBusesByIds(ids, false);
    }

    protected List<Bus> getBusesByIds(List<BigInteger> ids, boolean load) {
        List<Bus> busList = new ArrayList<>();
        for (BigInteger id : ids) {
            busList.add(getBusById(id, load));
        }
        return busList;
    }

    protected Bus getBusById(BigInteger id) {
        return getBusById(id, false);
    }

    protected Bus getBusById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(BusProvider.class).findById(id) : buses.get(id);
    }

    protected List<Employee> getEmployeesByIds(List<BigInteger> ids) {
        return getEmployeesByIds(ids, false);
    }

    protected List<Employee> getEmployeesByIds(List<BigInteger> ids, boolean load) {
        List<Employee> employeeList = new ArrayList<>();
        for (BigInteger id : ids) {
            employeeList.add(getEmployeeById(id, load));
        }
        return employeeList;
    }

    protected Employee getEmployeeById(BigInteger id) {
        return getEmployeeById(id, false);
    }

    protected Employee getEmployeeById(BigInteger id, boolean load) {
        return load ? (Employee) getFactory().getProvider(EmployeeProvider.class).findById(id) : employees.get(id);
    }
}
