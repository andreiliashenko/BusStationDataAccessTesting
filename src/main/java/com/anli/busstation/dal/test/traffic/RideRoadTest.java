package com.anli.busstation.dal.test.traffic;

import com.anli.busstation.dal.interfaces.entities.geography.Road;
import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.staff.Driver;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.traffic.RideRoad;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.providers.geography.RoadProvider;
import com.anli.busstation.dal.interfaces.providers.staff.DriverProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RideRoadProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class RideRoadTest extends BasicDataAccessTestSceleton<RideRoad> {

    protected Map<BigInteger, Driver> drivers;
    protected Map<BigInteger, Road> roads;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, Station> stations = getFixtureCreator().createStationFixture(100, 10,
                new ArrayList<Bus>(0), new ArrayList<Employee>(0));
        for (Station station : stations.values()) {
            nullifyStationCollections(station);
        }
        Map<BigInteger, DriverSkill> skills = getFixtureCreator().createDriverSkillFixture(40, 5);
        drivers = getFixtureCreator().createDriverFixture(70, 10, new ArrayList<>(skills.values()));
        roads = getFixtureCreator().createRoadFixture(120, 10, new ArrayList<>(stations.values()));
    }

    @Override
    protected BigInteger createEntityByProvider(RideRoad sourceRoad) throws Exception {
        RideRoadProvider provider = getFactory().getProvider(RideRoadProvider.class);
        RideRoad rideRoad = provider.create();
        rideRoad.setDriver(sourceRoad.getDriver());
        rideRoad.setRoad(sourceRoad.getRoad());
        return provider.save(rideRoad).getId();
    }

    @Override
    protected RideRoad getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(RideRoadProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, RideRoad sourceRoad) throws Exception {
        RideRoadProvider provider = getFactory().getProvider(RideRoadProvider.class);
        RideRoad rideRoad = provider.findById(id);
        rideRoad.setDriver(sourceRoad.getDriver());
        rideRoad.setRoad(sourceRoad.getRoad());
        provider.save(rideRoad);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        RideRoadProvider provider = getFactory().getProvider(RideRoadProvider.class);
        provider.remove(provider.findById(id));
    }

    @Override
    protected List<RideRoad> getCreationTestSets() throws Exception {
        List<RideRoad> testSets = new ArrayList<>(3);
        testSets.add(getNewRideRoad(BigInteger.ZERO, null, null));
        testSets.add(getNewRideRoad(BigInteger.ZERO, BigInteger.valueOf(70), BigInteger.valueOf(122)));
        testSets.add(getNewRideRoad(BigInteger.ZERO, BigInteger.valueOf(75), BigInteger.valueOf(127)));
        return testSets;
    }

    @Override
    protected List<RideRoad> getUpdateTestSets() throws Exception {
        List<RideRoad> testSets = new ArrayList<>(3);
        testSets.add(getNewRideRoad(BigInteger.ZERO, BigInteger.valueOf(71), BigInteger.valueOf(125)));
        testSets.add(getNewRideRoad(BigInteger.ZERO, null, BigInteger.valueOf(123)));
        testSets.add(getNewRideRoad(BigInteger.ZERO, BigInteger.valueOf(77), null));
        return testSets;
    }

    @Override
    protected List<RideRoad> getSearchTestSets() throws Exception {
        List<RideRoad> testSets = new ArrayList<>(5);
        testSets.add(getNewRideRoad(BigInteger.ZERO, BigInteger.valueOf(70), BigInteger.valueOf(121)));
        testSets.add(getNewRideRoad(BigInteger.ZERO, BigInteger.valueOf(72), BigInteger.valueOf(124)));
        testSets.add(getNewRideRoad(BigInteger.ZERO, BigInteger.valueOf(73), null));
        testSets.add(getNewRideRoad(BigInteger.ZERO, null, BigInteger.valueOf(125)));
        testSets.add(getNewRideRoad(BigInteger.ZERO, BigInteger.valueOf(72), BigInteger.valueOf(125)));
        return testSets;
    }

    @Override
    protected List<List<RideRoad>> performSearches() throws Exception {
        RideRoadProvider provider = getFactory().getProvider(RideRoadProvider.class);
        List<List<RideRoad>> searchResult = new ArrayList<>(6);
        List<RideRoad> resultCollection;
        resultCollection = provider.findByDriver(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDriver(getDriverById(BigInteger.valueOf(72)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyDriver(getDriversByIds(Arrays.asList(BigInteger.valueOf(70),
                BigInteger.valueOf(71), BigInteger.valueOf(73))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByRoad(getRoadById(BigInteger.valueOf(125)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyRoad(getRoadsByIds(Arrays.asList(BigInteger.valueOf(120),
                BigInteger.valueOf(121), BigInteger.valueOf(124))));
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        RideRoadProvider provider = getFactory().getProvider(RideRoadProvider.class);
        List<List<BigInteger>> searchResult = new ArrayList<>(6);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByDriver(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDriver(getDriverById(BigInteger.valueOf(72)));
        searchResult.add(resultCollection);
        resultCollection
                = provider.collectIdsByAnyDriver(getDriversByIds(Arrays.asList(BigInteger.valueOf(70),
                                        BigInteger.valueOf(71), BigInteger.valueOf(73))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByRoad(getRoadById(BigInteger.valueOf(125)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyRoad(getRoadsByIds(Arrays.asList(BigInteger.valueOf(120),
                BigInteger.valueOf(121), BigInteger.valueOf(124))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> indices = new ArrayList<>(6);
        indices.add(new int[]{3});
        indices.add(new int[]{1, 4});
        indices.add(new int[]{0, 2});
        indices.add(new int[]{3, 4});
        indices.add(new int[]{0, 1});
        indices.add(new int[]{0, 1, 2, 3, 4});
        return indices;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected RideRoad getNewRideRoad(BigInteger id, BigInteger driverId,
            BigInteger roadId) {
        return getNewRideRoad(id, driverId, roadId, false);
    }

    protected abstract RideRoad getNewRideRoad(BigInteger id, BigInteger driverId,
            BigInteger roadId, boolean load);

    protected List<Driver> getDriversByIds(List<BigInteger> ids) {
        return getDriversByIds(ids, false);
    }

    protected List<Driver> getDriversByIds(List<BigInteger> ids, boolean load) {
        List<Driver> driverList = new ArrayList<>();
        for (BigInteger id : ids) {
            driverList.add(getDriverById(id, load));
        }
        return driverList;
    }

    protected Driver getDriverById(BigInteger id) {
        return getDriverById(id, false);
    }

    protected Driver getDriverById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(DriverProvider.class).findById(id) : drivers.get(id);
    }

    protected List<Road> getRoadsByIds(List<BigInteger> ids) {
        return getRoadsByIds(ids, false);
    }

    protected List<Road> getRoadsByIds(List<BigInteger> ids, boolean load) {
        List<Road> roadList = new ArrayList<>();
        for (BigInteger id : ids) {
            roadList.add(getRoadById(id, load));
        }
        return roadList;
    }

    protected Road getRoadById(BigInteger id) {
        return getRoadById(id, false);
    }

    protected Road getRoadById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(RoadProvider.class).findById(id) : roads.get(id);
    }

    protected abstract void nullifyStationCollections(Station station);

    @Override
    protected RideRoad nullifyCollections(RideRoad entity) {
        return entity;
    }
}
