package com.anli.busstation.dal.test.traffic;

import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.traffic.RoutePoint;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.providers.geography.StationProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RoutePointProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class RoutePointTest extends BasicDataAccessTestSceleton<RoutePoint> {

    protected Map<BigInteger, Station> stations;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        stations = getFixtureCreator().createStationFixture(100, 10,
                new ArrayList<Bus>(0), new ArrayList<Employee>(0));
        for (Station station : stations.values()) {
            nullifyStationCollections(station);
        }
    }

    @Override
    protected BigInteger createEntityByProvider(RoutePoint sourcePoint) throws Exception {
        RoutePointProvider provider = getFactory().getProvider(RoutePointProvider.class);
        RoutePoint routePoint = provider.create();
        routePoint.setStation(sourcePoint.getStation());
        return provider.save(routePoint).getId();
    }

    @Override
    protected RoutePoint getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(RoutePointProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, RoutePoint sourcePoint) throws Exception {
        RoutePointProvider provider = getFactory().getProvider(RoutePointProvider.class);
        RoutePoint routePoint = provider.findById(id);
        routePoint.setStation(sourcePoint.getStation());
        provider.save(routePoint);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        RoutePointProvider provider = getFactory().getProvider(RoutePointProvider.class);
        provider.remove(provider.findById(id));
    }

    @Override
    protected List<RoutePoint> getCreationTestSets() throws Exception {
        List<RoutePoint> testSets = new ArrayList<>(3);
        testSets.add(getNewRoutePoint(BigInteger.ZERO, null));
        testSets.add(getNewRoutePoint(BigInteger.ZERO, BigInteger.valueOf(100)));
        testSets.add(getNewRoutePoint(BigInteger.ZERO, BigInteger.valueOf(101)));
        return testSets;
    }

    @Override
    protected List<RoutePoint> getUpdateTestSets() throws Exception {
        List<RoutePoint> testSets = new ArrayList<>(3);
        testSets.add(getNewRoutePoint(BigInteger.ZERO, BigInteger.valueOf(102)));
        testSets.add(getNewRoutePoint(BigInteger.ZERO, BigInteger.valueOf(103)));
        testSets.add(getNewRoutePoint(BigInteger.ZERO, null));
        return testSets;
    }

    @Override
    protected List<RoutePoint> getSearchTestSets() throws Exception {
        List<RoutePoint> testSets = new ArrayList<>(5);
        testSets.add(getNewRoutePoint(BigInteger.ZERO, BigInteger.valueOf(101)));
        testSets.add(getNewRoutePoint(BigInteger.ZERO, BigInteger.valueOf(102)));
        testSets.add(getNewRoutePoint(BigInteger.ZERO, BigInteger.valueOf(103)));
        testSets.add(getNewRoutePoint(BigInteger.ZERO, null));
        testSets.add(getNewRoutePoint(BigInteger.ZERO, BigInteger.valueOf(104)));
        return testSets;
    }

    @Override
    protected List<List<RoutePoint>> performSearches() throws Exception {
        RoutePointProvider provider = getFactory().getProvider(RoutePointProvider.class);
        List<List<RoutePoint>> searchResult = new ArrayList<>(5);
        List<RoutePoint> resultCollection;
        resultCollection = provider.findByStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByStation(getStationById(BigInteger.valueOf(103)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyStation(Collections.<Station>emptyList());
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyStation(getStationsByIds(Arrays.asList(BigInteger.valueOf(101),
                BigInteger.valueOf(104), BigInteger.valueOf(107))));
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        RoutePointProvider provider = getFactory().getProvider(RoutePointProvider.class);
        List<List<BigInteger>> searchResult = new ArrayList<>(5);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByStation(getStationById(BigInteger.valueOf(103)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyStation(Collections.<Station>emptyList());
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyStation(getStationsByIds(Arrays.asList(BigInteger.valueOf(101),
                BigInteger.valueOf(104), BigInteger.valueOf(107))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> indices = new ArrayList<>(5);
        indices.add(new int[]{3});
        indices.add(new int[]{2});
        indices.add(new int[]{});
        indices.add(new int[]{0, 4});
        indices.add(new int[]{0, 1, 2, 3, 4});
        return indices;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected RoutePoint getNewRoutePoint(BigInteger id, BigInteger stationId) {
        return getNewRoutePoint(id, stationId, false);
    }

    protected abstract RoutePoint getNewRoutePoint(BigInteger id, BigInteger stationId, boolean load);

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
        if (load) {
            return getFactory().getProvider(StationProvider.class).findById(id);
        } else {
            return stations.get(id);
        }
    }

    protected abstract void nullifyStationCollections(Station station);

    @Override
    protected RoutePoint nullifyCollections(RoutePoint entity) {
        return entity;
    }
}
