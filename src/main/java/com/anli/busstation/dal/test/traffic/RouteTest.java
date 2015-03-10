package com.anli.busstation.dal.test.traffic;

import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.traffic.Ride;
import com.anli.busstation.dal.interfaces.entities.traffic.RidePoint;
import com.anli.busstation.dal.interfaces.entities.traffic.RideRoad;
import com.anli.busstation.dal.interfaces.entities.traffic.Route;
import com.anli.busstation.dal.interfaces.entities.traffic.RoutePoint;
import com.anli.busstation.dal.interfaces.entities.traffic.Ticket;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.traffic.RideProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RoutePointProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RouteProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class RouteTest extends BasicDataAccessTestSceleton<Route> {

    protected Map<BigInteger, RoutePoint> routePoints;
    protected Map<BigInteger, Ride> rides;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, GasLabel> gasLabels = getFixtureCreator().createGasLabelFixture(10, 5);
        Map<BigInteger, Model> models = getFixtureCreator().createModelFixture(20, 5,
                new ArrayList(gasLabels.values()));
        Map<BigInteger, TechnicalState> technicalStates = getFixtureCreator().createTechnicalStateFixture(30, 5);
        Map<BigInteger, Bus> buses = getFixtureCreator().createBusFixture(60, 10, new ArrayList(models.values()),
                new ArrayList(technicalStates.values()));
        Map<BigInteger, Station> stations = getFixtureCreator().createStationFixture(100, 10,
                new ArrayList<Bus>(0), new ArrayList<Employee>(0));
        for (Station station : stations.values()) {
            nullifyStationCollections(station);
        }
        routePoints = getFixtureCreator().createRoutePointFixture(110, 10,
                new ArrayList(stations.values()));
        rides = getFixtureCreator().createRideFixture(160, 10, new ArrayList<>(buses.values()),
                new ArrayList<RidePoint>(0), new ArrayList<RideRoad>(0), new ArrayList<Ticket>(0));
        for (Ride ride : rides.values()) {
            nullifyRideCollections(ride);
        }
    }

    @Override
    protected BigInteger createEntityByProvider(Route sourceRoute) throws Exception {
        RouteProvider provider = getFactory().getProvider(RouteProvider.class);
        Route route = provider.create();
        route = provider.pullRoutePoints(route);
        route = provider.pullRides(route);
        route.setNumCode(sourceRoute.getNumCode());
        route.setTicketPrice(sourceRoute.getTicketPrice());
        route.getRoutePoints().clear();
        route.getRoutePoints().addAll(sourceRoute.getRoutePoints());
        route.getRides().clear();
        route.getRides().addAll(sourceRoute.getRides());
        return provider.save(route).getId();
    }

    @Override
    protected Route getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(RouteProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Route sourceRoute) throws Exception {
        RouteProvider provider = getFactory().getProvider(RouteProvider.class);
        Route route = provider.findById(id);
        route = provider.pullRoutePoints(route);
        route = provider.pullRides(route);
        route.setNumCode(sourceRoute.getNumCode());
        route.setTicketPrice(sourceRoute.getTicketPrice());
        route.getRoutePoints().clear();
        route.getRoutePoints().addAll(sourceRoute.getRoutePoints());
        route.getRides().clear();
        route.getRides().addAll(sourceRoute.getRides());
        provider.save(route);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        RouteProvider provider = getFactory().getProvider(RouteProvider.class);
        provider.remove(provider.findById(id));
    }

    @Override
    protected boolean hasCollections() {
        return true;
    }

    @Override
    protected Route pull(Route route, int index) {
        RouteProvider provider = getFactory().getProvider(RouteProvider.class);
        if (index == 0 || index == 3) {
            route = provider.pullRoutePoints(route);
            return provider.pullRides(route);
        } else if (index == 1) {
            return provider.pullRoutePoints(route);
        } else if (index == 2) {
            return provider.pullRides(route);
        }
        throw new RuntimeException();
    }

    @Override
    protected List<Route> getPullEtalons() throws Exception {
        List<Route> etalons = getCreationTestSets();
        nullifyRides(etalons.get(1));
        nullifyRoutePoints(etalons.get(2));
        return etalons;
    }

    @Override
    protected List<Route> getCreationTestSets() throws Exception {
        List<Route> testSets = new ArrayList<>(4);
        testSets.add(getNewRoute(BigInteger.ZERO, null, null, new ArrayList<BigInteger>(),
                new ArrayList<BigInteger>()));
        testSets.add(getNewRoute(BigInteger.ZERO, "", BigDecimal.valueOf(0),
                Arrays.asList(BigInteger.valueOf(110), BigInteger.valueOf(111)),
                Arrays.asList(BigInteger.valueOf(160), BigInteger.valueOf(162), BigInteger.valueOf(161))));
        testSets.add(getNewRoute(BigInteger.ZERO, "789", BigDecimal.valueOf(123.33),
                Arrays.asList(BigInteger.valueOf(117), BigInteger.valueOf(116), BigInteger.valueOf(115)),
                Arrays.asList(BigInteger.valueOf(163), BigInteger.valueOf(168))));
        testSets.add(getNewRoute(BigInteger.ZERO, "10369", BigDecimal.valueOf(331.50),
                Arrays.asList(BigInteger.valueOf(119)), Arrays.asList(BigInteger.valueOf(169),
                        BigInteger.valueOf(166))));
        return testSets;
    }

    @Override
    protected List<Route> getUpdateTestSets() throws Exception {
        List<Route> testSets = new ArrayList<>(4);
        testSets.add(getNewRoute(BigInteger.ZERO, "", BigDecimal.valueOf(743.40),
                Arrays.asList(BigInteger.valueOf(112)),
                Arrays.asList(BigInteger.valueOf(164), BigInteger.valueOf(163))));
        testSets.add(getNewRoute(BigInteger.ZERO, "12", BigDecimal.valueOf(0),
                Arrays.asList(BigInteger.valueOf(111)),
                Arrays.asList(BigInteger.valueOf(161), BigInteger.valueOf(160), BigInteger.valueOf(162))));
        testSets.add(getNewRoute(BigInteger.ZERO, null, BigDecimal.valueOf(250),
                Arrays.asList(BigInteger.valueOf(116), BigInteger.valueOf(117)),
                Arrays.asList(BigInteger.valueOf(169), BigInteger.valueOf(168))));
        testSets.add(getNewRoute(BigInteger.ZERO, "579", null,
                Arrays.asList(BigInteger.valueOf(119), BigInteger.valueOf(110)),
                new ArrayList<BigInteger>()));
        return testSets;
    }

    @Override
    protected List<Route> getSearchTestSets() throws Exception {
        List<Route> testSets = new ArrayList<>(4);
        testSets.add(getNewRoute(BigInteger.ZERO, "120", BigDecimal.valueOf(111.11),
                Arrays.asList(BigInteger.valueOf(110), BigInteger.valueOf(111)),
                Arrays.asList(BigInteger.valueOf(162), BigInteger.valueOf(163))));
        testSets.add(getNewRoute(BigInteger.ZERO, "121", BigDecimal.valueOf(122.74),
                Arrays.asList(BigInteger.valueOf(112), BigInteger.valueOf(113)),
                Arrays.asList(BigInteger.valueOf(164), BigInteger.valueOf(165))));
        testSets.add(getNewRoute(BigInteger.ZERO, "", BigDecimal.valueOf(178.99),
                Arrays.asList(BigInteger.valueOf(114), BigInteger.valueOf(115)),
                Arrays.asList(BigInteger.valueOf(166), BigInteger.valueOf(167))));
        testSets.add(getNewRoute(BigInteger.ZERO, null, BigDecimal.valueOf(179),
                Arrays.asList(BigInteger.valueOf(116), BigInteger.valueOf(117)),
                Arrays.asList(BigInteger.valueOf(168), BigInteger.valueOf(169))));
        return testSets;

    }

    @Override
    protected List<List<Route>> performSearches() throws Exception {
        RouteProvider provider = getFactory().getProvider(RouteProvider.class);
        List<List<Route>> searchResult = new ArrayList<>(8);
        List<Route> resultCollection;
        resultCollection = provider.findByNumCode("121");
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyNumCode(Arrays.asList("", "120"));
        searchResult.add(resultCollection);
        resultCollection = provider.findByRoutePoint(getRoutePointById(BigInteger.valueOf(116)));
        searchResult.add(resultCollection);
        resultCollection = provider
                .findByAnyRoutePoint(getRoutePointsByIds(Arrays.asList(BigInteger.valueOf(111),
                                        BigInteger.valueOf(117), BigInteger.valueOf(119))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByRide(getRideById(BigInteger.valueOf(167)));
        searchResult.add(resultCollection);
        resultCollection = provider
                .findByAnyRide(getRidesByIds(Arrays.asList(BigInteger.valueOf(161),
                                        BigInteger.valueOf(163), BigInteger.valueOf(165))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByTicketPriceRange(BigDecimal.valueOf(122.74), false,
                BigDecimal.valueOf(179), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        RouteProvider provider = getFactory().getProvider(RouteProvider.class);
        List<List<BigInteger>> searchResult = new ArrayList<>(8);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByNumCode("121");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyNumCode(Arrays.asList("", "120"));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByRoutePoint(getRoutePointById(BigInteger.valueOf(116)));
        searchResult.add(resultCollection);
        resultCollection = provider
                .collectIdsByAnyRoutePoint(getRoutePointsByIds(Arrays.asList(BigInteger.valueOf(111),
                                        BigInteger.valueOf(117), BigInteger.valueOf(119))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByRide(getRideById(BigInteger.valueOf(167)));
        searchResult.add(resultCollection);
        resultCollection = provider
                .collectIdsByAnyRide(getRidesByIds(Arrays.asList(BigInteger.valueOf(161),
                                        BigInteger.valueOf(163), BigInteger.valueOf(165))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByTicketPriceRange(BigDecimal.valueOf(122.74), false,
                BigDecimal.valueOf(179), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> sets = new ArrayList<>(9);
        sets.add(new int[]{1});
        sets.add(new int[]{0, 2});
        sets.add(new int[]{3});
        sets.add(new int[]{0, 3});
        sets.add(new int[]{2});
        sets.add(new int[]{0, 1});
        sets.add(new int[]{1, 2});
        sets.add(new int[]{0, 1, 2, 3});
        return sets;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Route getNewRoute(BigInteger id, String numCode, BigDecimal ticketPrice,
            List<BigInteger> routePoints, List<BigInteger> rides) {
        return getNewRoute(id, numCode, ticketPrice, routePoints, rides, false);
    }

    protected abstract Route getNewRoute(BigInteger id, String numCode, BigDecimal ticketPrice,
            List<BigInteger> routePoints, List<BigInteger> rides, boolean load);

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
        return load ? getFactory().getProvider(RoutePointProvider.class).findById(id) : routePoints.get(id);
    }

    protected List<Ride> getRidesByIds(List<BigInteger> ids) {
        return getRidesByIds(ids, false);
    }

    protected List<Ride> getRidesByIds(List<BigInteger> ids, boolean load) {
        List<Ride> rideList = new ArrayList<>();
        for (BigInteger id : ids) {
            rideList.add(getRideById(id, load));
        }
        return rideList;
    }

    protected Ride getRideById(BigInteger id) {
        return getRideById(id, false);
    }

    protected Ride getRideById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(RideProvider.class).findById(id) : rides.get(id);
    }

    protected abstract void nullifyRoutePoints(Route route);

    protected abstract void nullifyRides(Route route);

    protected abstract void nullifyStationCollections(Station station);

    protected abstract void nullifyRideCollections(Ride ride);
}
