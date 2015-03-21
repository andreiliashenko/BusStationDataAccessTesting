package com.anli.busstation.dal.test.traffic;

import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.traffic.RidePoint;
import com.anli.busstation.dal.interfaces.entities.traffic.RoutePoint;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.providers.traffic.RidePointProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RoutePointProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class RidePointTest extends BasicDataAccessTestSceleton<RidePoint> {

    protected Map<BigInteger, RoutePoint> routePoints;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, Station> stations = getFixtureCreator().createStationFixture(100, 10,
                new ArrayList<Bus>(0), new ArrayList<Employee>(0));
        for (Station station : stations.values()) {
            nullifyStationCollections(station);
        }
        routePoints = getFixtureCreator().createRoutePointFixture(110, 10,
                new ArrayList(stations.values()));
    }

    @Override
    protected BigInteger createEntityByProvider(RidePoint sourcePoint) throws Exception {
        RidePointProvider provider = getFactory().getProvider(RidePointProvider.class);
        RidePoint ridePoint = provider.create();
        ridePoint.setArrivalTime(sourcePoint.getArrivalTime());
        ridePoint.setDepartureTime(sourcePoint.getDepartureTime());
        ridePoint.setRoutePoint(sourcePoint.getRoutePoint());
        return provider.save(ridePoint).getId();
    }

    @Override
    protected RidePoint getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(RidePointProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, RidePoint sourcePoint) throws Exception {
        RidePointProvider provider = getFactory().getProvider(RidePointProvider.class);
        RidePoint ridePoint = provider.findById(id);
        ridePoint.setArrivalTime(sourcePoint.getArrivalTime());
        ridePoint.setDepartureTime(sourcePoint.getDepartureTime());
        ridePoint.setRoutePoint(sourcePoint.getRoutePoint());
        provider.save(ridePoint);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        RidePointProvider provider = getFactory().getProvider(RidePointProvider.class);
        provider.remove(provider.findById(id));
    }

    @Override
    protected List<RidePoint> getCreationTestSets() throws Exception {
        List<RidePoint> testSets = new ArrayList<>(4);
        testSets.add(getNewRidePoint(BigInteger.ZERO, null, null, null));
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(0), new DateTime(0), null));
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(2015, 7, 7, 15, 50, 0, 0),
                new DateTime(2015, 7, 7, 19, 30, 0, 0), BigInteger.valueOf(111)));
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(2015, 8, 11, 23, 45, 0, 0),
                new DateTime(2015, 8, 12, 2, 10, 0, 0), BigInteger.valueOf(113)));
        return testSets;
    }

    @Override
    protected List<RidePoint> getUpdateTestSets() throws Exception {
        List<RidePoint> testSets = new ArrayList<>(4);
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(0),
                new DateTime(2015, 9, 30, 16, 25, 0, 0), BigInteger.valueOf(115)));
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(2015, 10, 11, 0, 0, 0, 0), null, null));
        testSets.add(getNewRidePoint(BigInteger.ZERO, null,
                new DateTime(0), BigInteger.valueOf(117)));
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(2016, 1, 1, 17, 35, 0, 0),
                new DateTime(2016, 1, 2, 19, 55, 0, 0), BigInteger.valueOf(118)));
        return testSets;
    }

    @Override
    protected List<RidePoint> getSearchTestSets() throws Exception {
        List<RidePoint> testSets = new ArrayList<>(5);
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(2015, 1, 1, 0, 0, 0, 0),
                new DateTime(2015, 1, 1, 12, 30, 0, 0), BigInteger.valueOf(115)));
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(2015, 1, 2, 6, 15, 0, 0),
                new DateTime(2015, 1, 2, 10, 55, 0, 0), BigInteger.valueOf(115)));
        testSets.add(getNewRidePoint(BigInteger.ZERO, null,
                new DateTime(2015, 1, 3, 11, 40, 0, 0), BigInteger.valueOf(116)));
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(2015, 1, 4, 19, 20, 0, 0),
                null, BigInteger.valueOf(117)));
        testSets.add(getNewRidePoint(BigInteger.ZERO, new DateTime(2015, 1, 5, 22, 30),
                new DateTime(2015, 1, 4, 23, 50, 0, 0), BigInteger.valueOf(119)));
        return testSets;
    }

    @Override
    protected List<List<RidePoint>> performSearches() throws Exception {
        RidePointProvider provider = getFactory().getProvider(RidePointProvider.class);
        List<List<RidePoint>> searchResult = new ArrayList<>(5);
        List<RidePoint> resultCollection;
        resultCollection = provider.findByArrivalTimeRange(new DateTime(2015, 1, 1, 0, 0, 0, 0), true,
                new DateTime(2015, 1, 4, 19, 20, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDepartureTimeRange(new DateTime(2015, 1, 2, 10, 55, 0, 0), false,
                new DateTime(2015, 1, 4, 23, 50, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByRoutePoint(getRoutePointById(BigInteger.valueOf(115)));
        searchResult.add(resultCollection);
        resultCollection
                = provider.findByAnyRoutePoint(getRoutePointsByIds(Arrays.asList(BigInteger.valueOf(116),
                                        BigInteger.valueOf(117), BigInteger.valueOf(118))));
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        RidePointProvider provider = getFactory().getProvider(RidePointProvider.class);
        List<List<BigInteger>> searchResult = new ArrayList<>(5);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByArrivalTimeRange(new DateTime(2015, 1, 1, 0, 0, 0, 0), true,
                new DateTime(2015, 1, 4, 19, 20, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDepartureTimeRange(new DateTime(2015, 1, 2, 10, 55, 0, 0),
                false, new DateTime(2015, 1, 4, 23, 50, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByRoutePoint(getRoutePointById(BigInteger.valueOf(115)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyRoutePoint(getRoutePointsByIds(Arrays
                .asList(BigInteger.valueOf(116),
                        BigInteger.valueOf(117), BigInteger.valueOf(118))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> indices = new ArrayList<>(5);
        indices.add(new int[]{1, 3});
        indices.add(new int[]{1, 2});
        indices.add(new int[]{0, 1});
        indices.add(new int[]{2, 3});
        indices.add(new int[]{0, 1, 2, 3, 4});
        return indices;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected RidePoint getNewRidePoint(BigInteger id, DateTime arrivalTime,
            DateTime departureTime, BigInteger routePointId) {
        return getNewRidePoint(id, arrivalTime, departureTime, routePointId, false);
    }

    protected abstract RidePoint getNewRidePoint(BigInteger id, DateTime arrivalTime,
            DateTime departureTime, BigInteger routePointId, boolean load);

    protected List<RoutePoint> getRoutePointsByIds(List<BigInteger> ids) {
        return getRoutePointsByIds(ids, false);
    }

    protected List<RoutePoint> getRoutePointsByIds(List<BigInteger> ids, boolean load) {
        List<RoutePoint> routePointList = new ArrayList<>();
        for (BigInteger id : ids) {
            routePointList.add(getRoutePointById(id, load));
        }
        return routePointList;
    }

    protected RoutePoint getRoutePointById(BigInteger id) {
        return getRoutePointById(id, false);
    }

    protected RoutePoint getRoutePointById(BigInteger id, boolean load) {
        if (load) {
            return getFactory().getProvider(RoutePointProvider.class).findById(id);
        } else {
            return routePoints.get(id);
        }
    }

    protected abstract void nullifyStationCollections(Station station);

    @Override
    protected RidePoint nullifyCollections(RidePoint entity) {
        return entity;
    }
}
