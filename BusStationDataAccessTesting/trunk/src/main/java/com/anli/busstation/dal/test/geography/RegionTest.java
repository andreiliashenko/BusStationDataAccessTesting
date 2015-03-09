package com.anli.busstation.dal.test.geography;

import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.geography.Region;
import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.geography.RegionProvider;
import com.anli.busstation.dal.interfaces.providers.geography.StationProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RegionTest extends BasicDataAccessTestSceleton<Region> {

    protected Map<BigInteger, Station> stations;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, GasLabel> gasLabels = getFixtureCreator().createGasLabelFixture(10, 5);

        Map<BigInteger, Model> models = getFixtureCreator().createModelFixture(20, 5, new ArrayList(gasLabels.values()));

        Map<BigInteger, TechnicalState> technicalStates = getFixtureCreator().createTechnicalStateFixture(30, 5);

        Map<BigInteger, Bus> buses = getFixtureCreator().createBusFixture(60, 10, new ArrayList(models.values()),
                new ArrayList(technicalStates.values()));

        Map<BigInteger, DriverSkill> driverSkills = getFixtureCreator().createDriverSkillFixture(40, 5);
        Map<BigInteger, MechanicSkill> mechanicSkills = getFixtureCreator().createMechanicSkillFixture(50, 5);

        Map<BigInteger, Employee> employees = new HashMap<>();
        employees.putAll(getFixtureCreator().createDriverFixture(70, 5, new ArrayList(driverSkills.values())));
        employees.putAll(getFixtureCreator().createMechanicFixture(80, 5, new ArrayList(mechanicSkills.values())));
        employees.putAll(getFixtureCreator().createSalesmanFixture(90, 5));

        stations = getFixtureCreator().createStationFixture(100, 10,
                new ArrayList(buses.values()), new ArrayList(employees.values()));
        for (Station station : stations.values()) {
            nullifyStationCollections(station);
        }
    }

    @Override
    protected BigInteger createEntityByProvider(Region regionSource) throws Exception {
        RegionProvider provider = getFactory().getProvider(RegionProvider.class);
        Region region = provider.create();
        region = provider.pullStations(region);
        region.setCode(regionSource.getCode());
        region.setName(regionSource.getName());
        region.getStations().clear();
        region.getStations().addAll(regionSource.getStations());
        region = provider.save(region);
        return region.getId();
    }

    @Override
    protected Region getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(RegionProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Region sourceRegion) throws Exception {
        RegionProvider provider = getFactory().getProvider(RegionProvider.class);
        Region region = provider.findById(id);
        region = provider.pullStations(region);
        region.setCode(sourceRegion.getCode());
        region.setName(sourceRegion.getName());
        region.getStations().clear();
        region.getStations().addAll(sourceRegion.getStations());
        provider.save(region);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        RegionProvider provider = getFactory().getProvider(RegionProvider.class);
        Region region = provider.findById(id);
        provider.remove(region);
    }

    @Override
    protected boolean hasCollections() {
        return true;
    }

    @Override
    protected Region pull(Region entity, int index) {
        return getFactory().getProvider(RegionProvider.class).pullStations(entity);
    }

    @Override
    protected List<Region> getPullEtalons() throws Exception {
        return getCreationTestSets();
    }

    protected abstract void nullifyStationCollections(Station station);

    @Override
    protected List<Region> getCreationTestSets() throws Exception {
        List<BigInteger> stationList1 = Arrays.asList(BigInteger.valueOf(101), BigInteger.valueOf(103));
        List<BigInteger> stationList2 = Arrays.asList(BigInteger.valueOf(105), BigInteger.valueOf(107),
                BigInteger.valueOf(108), BigInteger.valueOf(109));
        List<Region> testSets = new ArrayList<>(4);
        testSets.add(getNewRegion(BigInteger.ZERO, null, null, new ArrayList()));
        testSets.add(getNewRegion(BigInteger.ZERO, 0, "", new ArrayList()));
        testSets.add(getNewRegion(BigInteger.ZERO, 31, "Первая область", stationList1));
        testSets.add(getNewRegion(BigInteger.ZERO, 36, "Вторая область", stationList2));

        return testSets;
    }

    @Override
    protected List<Region> getUpdateTestSets() throws Exception {
        List<BigInteger> stationList1 = Arrays.asList(BigInteger.valueOf(100), BigInteger.valueOf(106));
        List<BigInteger> stationList2 = Arrays.asList(BigInteger.valueOf(104), BigInteger.valueOf(105), BigInteger.valueOf(109));
        List<Region> testSets = new ArrayList<>(4);
        testSets.add(getNewRegion(BigInteger.ZERO, 99, "Новая Область", stationList1));
        testSets.add(getNewRegion(BigInteger.ZERO, 188, "Новый Край", new ArrayList()));
        testSets.add(getNewRegion(BigInteger.ZERO, 0, "", stationList2));
        testSets.add(getNewRegion(BigInteger.ZERO, null, null, new ArrayList()));
        return testSets;
    }

    @Override
    protected List<List<Region>> performSearches() throws Exception {
        List<List<Region>> searchResult = new ArrayList<>(10);
        RegionProvider provider = getFactory().getProvider(RegionProvider.class);
        List<Region> resultCollection;
        List<BigInteger> stationList = Arrays.asList(BigInteger.valueOf(101), BigInteger.valueOf(104),
                BigInteger.valueOf(107));
        List<Integer> codeList = Arrays.asList(15, 20, 25, 30);

        resultCollection = provider.findByCode(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByCode(20);
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyCode(codeList);
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("");
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("искомый край");
        searchResult.add(resultCollection);
        resultCollection = provider.findByNameRegexp("АО");
        searchResult.add(resultCollection);
        resultCollection = provider.findByStation(getStationById(BigInteger.valueOf(105)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyStation(new ArrayList());
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyStation(getStationsByIds(stationList));
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(10);
        RegionProvider provider = getFactory().getProvider(RegionProvider.class);
        List<BigInteger> resultCollection;
        List<BigInteger> stationList = Arrays.asList(BigInteger.valueOf(101), BigInteger.valueOf(104),
                BigInteger.valueOf(107));
        List<Integer> codeList = Arrays.asList(15, 20, 25, 30);

        resultCollection = provider.collectIdsByCode(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByCode(20);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyCode(codeList);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("искомый край");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByNameRegexp("АО");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByStation(getStationById(BigInteger.valueOf(105)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyStation(new ArrayList());
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyStation(getStationsByIds(stationList));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<Region> getSearchTestSets() throws Exception {
        List<BigInteger> stationList1 = Arrays.asList(BigInteger.valueOf(100), BigInteger.valueOf(101),
                BigInteger.valueOf(102), BigInteger.valueOf(103));
        List<BigInteger> stationList2 = Arrays.asList(BigInteger.valueOf(104), BigInteger.valueOf(105), BigInteger.valueOf(106));
        List<BigInteger> stationList3 = Arrays.asList(BigInteger.valueOf(107), BigInteger.valueOf(108));
        List<BigInteger> stationList4 = Arrays.asList(BigInteger.valueOf(109));

        List<Region> testSets = new ArrayList<>(5);

        testSets.add(getNewRegion(BigInteger.ZERO, 11, "", stationList1));
        testSets.add(getNewRegion(BigInteger.ZERO, 15, "Одна область", stationList2));
        testSets.add(getNewRegion(BigInteger.ZERO, 20, "Первый АО", stationList3));
        testSets.add(getNewRegion(BigInteger.ZERO, 21, "Второй АО", new ArrayList()));
        testSets.add(getNewRegion(BigInteger.ZERO, 25, "искомый край", stationList4));
        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> indices = new ArrayList<>(10);

        indices.add(new int[]{});
        indices.add(new int[]{2});
        indices.add(new int[]{1, 2, 4});
        indices.add(new int[]{0});
        indices.add(new int[]{4});
        indices.add(new int[]{2, 3});
        indices.add(new int[]{1});
        indices.add(new int[]{});
        indices.add(new int[]{0, 1, 2});
        indices.add(new int[]{0, 1, 2, 3, 4});
        return indices;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Region getNewRegion(BigInteger id, Integer code, String name, List<BigInteger> stationList) {
        return getNewRegion(id, code, name, stationList, false);
    }

    protected abstract Region getNewRegion(BigInteger id, Integer code, String name,
            List<BigInteger> stationList, boolean load);

    protected List<Station> getStationsByIds(List<BigInteger> ids) {
        return getStationsByIds(ids, false);
    }

    protected List<Station> getStationsByIds(List<BigInteger> ids, boolean load) {
        List<Station> stationList = new ArrayList<>();
        for (BigInteger id : ids) {
            stationList.add(getStationById(id, load));
        }
        return stationList;
    }

    protected Station getStationById(BigInteger id) {
        return getStationById(id, false);
    }

    protected Station getStationById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(StationProvider.class).findById(id)
                : stations.get(id);
    }
}
