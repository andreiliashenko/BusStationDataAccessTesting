package com.anli.busstation.dal.test.traffic;

import com.anli.busstation.dal.interfaces.entities.geography.Road;
import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.staff.Driver;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.staff.Salesman;
import com.anli.busstation.dal.interfaces.entities.traffic.Ride;
import com.anli.busstation.dal.interfaces.entities.traffic.RidePoint;
import com.anli.busstation.dal.interfaces.entities.traffic.RideRoad;
import com.anli.busstation.dal.interfaces.entities.traffic.RoutePoint;
import com.anli.busstation.dal.interfaces.entities.traffic.Ticket;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.traffic.RidePointProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RideProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RideRoadProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.TicketProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class RideTest extends BasicDataAccessTestSceleton<Ride> {

    protected Map<BigInteger, Bus> buses;
    protected Map<BigInteger, RidePoint> ridePoints;
    protected Map<BigInteger, RideRoad> rideRoads;
    protected Map<BigInteger, Ticket> tickets;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, GasLabel> gasLabels = getFixtureCreator().createGasLabelFixture(10, 5);
        Map<BigInteger, Model> models = getFixtureCreator().createModelFixture(20, 5,
                new ArrayList(gasLabels.values()));
        Map<BigInteger, TechnicalState> technicalStates = getFixtureCreator().createTechnicalStateFixture(30, 5);
        Map<BigInteger, DriverSkill> skills = getFixtureCreator().createDriverSkillFixture(40, 5);
        buses = getFixtureCreator().createBusFixture(60, 10, new ArrayList(models.values()),
                new ArrayList(technicalStates.values()));
        Map<BigInteger, Driver> drivers = getFixtureCreator().createDriverFixture(70, 10, new ArrayList<>(skills.values()));
        Map<BigInteger, Salesman> salesmen = getFixtureCreator().createSalesmanFixture(90, 10);
        Map<BigInteger, Station> stations = getFixtureCreator().createStationFixture(100, 10,
                new ArrayList<Bus>(0), new ArrayList<Employee>(0));
        for (Station station : stations.values()) {
            nullifyStationCollections(station);
        }
        Map<BigInteger, RoutePoint> routePoints = getFixtureCreator().createRoutePointFixture(110, 10,
                new ArrayList(stations.values()));
        Map<BigInteger, Road> roads = getFixtureCreator().createRoadFixture(120, 10,
                new ArrayList<>(stations.values()));
        ridePoints = getFixtureCreator().createRidePointFixture(130, 10, new ArrayList<>(routePoints.values()));
        rideRoads = getFixtureCreator().createRideRoadFixture(140, 10, new ArrayList<>(drivers.values()),
                new ArrayList<>(roads.values()));
        tickets = getFixtureCreator().createTicketFixture(150, 10, new ArrayList<>(ridePoints.values()),
                new ArrayList<>(salesmen.values()));
    }

    @Override
    protected BigInteger createEntityByProvider(Ride sourceRide) throws Exception {
        RideProvider provider = getFactory().getProvider(RideProvider.class);
        Ride ride = provider.create();
        ride = provider.pullRidePoints(ride);
        ride = provider.pullRideRoads(ride);
        ride = provider.pullTickets(ride);
        ride.setBus(sourceRide.getBus());
        ride.getRidePoints().clear();
        ride.getRidePoints().addAll(sourceRide.getRidePoints());
        ride.getRideRoads().clear();
        ride.getRideRoads().addAll(sourceRide.getRideRoads());
        ride.getTickets().clear();
        ride.getTickets().addAll(sourceRide.getTickets());
        return provider.save(ride).getId();
    }

    @Override
    protected Ride getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(RideProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Ride sourceRide) throws Exception {
        RideProvider provider = getFactory().getProvider(RideProvider.class);
        Ride ride = provider.findById(id);
        ride = provider.pullRidePoints(ride);
        ride = provider.pullRideRoads(ride);
        ride = provider.pullTickets(ride);
        ride.setBus(sourceRide.getBus());
        ride.getRidePoints().clear();
        ride.getRidePoints().addAll(sourceRide.getRidePoints());
        ride.getRideRoads().clear();
        ride.getRideRoads().addAll(sourceRide.getRideRoads());
        ride.getTickets().clear();
        ride.getTickets().addAll(sourceRide.getTickets());
        provider.save(ride);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        RideProvider provider = getFactory().getProvider(RideProvider.class);
        provider.remove(provider.findById(id));
    }

    @Override
    protected boolean hasCollections() {
        return true;
    }

    @Override
    protected Ride pull(Ride ride, int index) {
        RideProvider provider = getFactory().getProvider(RideProvider.class);
        if (index == 0) {
            ride = provider.pullTickets(ride);
            return provider.pullRideRoads(ride);
        } else if (index == 1) {
            return provider.pullTickets(ride);
        } else if (index == 2) {
            return provider.pullRideRoads(ride);
        } else if (index == 3) {
            return provider.pullRidePoints(ride);
        }
        return null;
    }

    @Override
    protected List<Ride> getPullEtalons() throws Exception {
        List<Ride> rides = getCreationTestSets();
        nullifyRidePoints(rides.get(0));
        nullifyRidePoints(rides.get(1));
        nullifyRideRoads(rides.get(1));
        nullifyRidePoints(rides.get(2));
        nullifyTickets(rides.get(2));
        nullifyRideRoads(rides.get(3));
        nullifyTickets(rides.get(3));
        return rides;
    }

    @Override
    protected List<Ride> getCreationTestSets() throws Exception {
        List<Ride> testSets = new ArrayList<>(4);
        testSets.add(getNewRide(BigInteger.ZERO, null, new ArrayList<BigInteger>(),
                new ArrayList<BigInteger>(), new ArrayList<BigInteger>()));
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(60), Arrays.asList(BigInteger.valueOf(131),
                BigInteger.valueOf(130)), Arrays.asList(BigInteger.valueOf(146), BigInteger.valueOf(145),
                        BigInteger.valueOf(147)), Arrays.asList(BigInteger.valueOf(153))));
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(62), Arrays.asList(BigInteger.valueOf(132),
                BigInteger.valueOf(135), BigInteger.valueOf(134)), Arrays.asList(BigInteger.valueOf(140)),
                Arrays.asList(BigInteger.valueOf(154), BigInteger.valueOf(151))));
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(62), Arrays.asList(BigInteger.valueOf(138),
                BigInteger.valueOf(139), BigInteger.valueOf(133)), Arrays.asList(BigInteger.valueOf(142),
                        BigInteger.valueOf(144)), Arrays.asList(BigInteger.valueOf(159), BigInteger.valueOf(156),
                        BigInteger.valueOf(158))));
        return testSets;
    }

    @Override
    protected List<Ride> getUpdateTestSets() throws Exception {
        List<Ride> testSets = new ArrayList<>(4);
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(68), Arrays.asList(BigInteger.valueOf(137),
                BigInteger.valueOf(138), BigInteger.valueOf(133)), Arrays.asList(BigInteger.valueOf(143),
                        BigInteger.valueOf(144)), new ArrayList<BigInteger>()));
        testSets.add(getNewRide(BigInteger.ZERO, null, new ArrayList<BigInteger>(),
                Arrays.asList(BigInteger.valueOf(145), BigInteger.valueOf(144), BigInteger.valueOf(147)),
                Arrays.asList(BigInteger.valueOf(153), BigInteger.valueOf(157))));
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(68), Arrays.asList(BigInteger.valueOf(132),
                BigInteger.valueOf(135), BigInteger.valueOf(134)), Arrays.asList(BigInteger.valueOf(140), BigInteger.valueOf(141)),
                Arrays.asList(BigInteger.valueOf(155), BigInteger.valueOf(154))));
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(62), Arrays.asList(BigInteger.valueOf(139),
                BigInteger.valueOf(136), BigInteger.valueOf(130)), Arrays.asList(BigInteger.valueOf(142),
                        BigInteger.valueOf(149)), Arrays.asList(BigInteger.valueOf(159), BigInteger.valueOf(156),
                        BigInteger.valueOf(158))));
        return testSets;
    }

    @Override
    protected List<Ride> getSearchTestSets() throws Exception {
        List<Ride> testSets = new ArrayList<>(4);
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(60), Arrays.asList(BigInteger.valueOf(130),
                BigInteger.valueOf(133), BigInteger.valueOf(134)), Arrays.asList(BigInteger.valueOf(142),
                        BigInteger.valueOf(145)), new ArrayList<BigInteger>()));
        testSets.add(getNewRide(BigInteger.ZERO, null, new ArrayList<BigInteger>(),
                Arrays.asList(BigInteger.valueOf(141), BigInteger.valueOf(146), BigInteger.valueOf(147)),
                Arrays.asList(BigInteger.valueOf(158), BigInteger.valueOf(157))));
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(61), Arrays.asList(BigInteger.valueOf(132),
                BigInteger.valueOf(135), BigInteger.valueOf(139)), Arrays.asList(BigInteger.valueOf(140)),
                Arrays.asList(BigInteger.valueOf(155), BigInteger.valueOf(152))));
        testSets.add(getNewRide(BigInteger.ZERO, BigInteger.valueOf(65), Arrays.asList(BigInteger.valueOf(136),
                BigInteger.valueOf(131)), Arrays.asList(BigInteger.valueOf(143),
                        BigInteger.valueOf(149)), Arrays.asList(BigInteger.valueOf(159), BigInteger.valueOf(150),
                        BigInteger.valueOf(156))));
        return testSets;
    }

    @Override
    protected List<List<Ride>> performSearches() throws Exception {
        RideProvider provider = getFactory().getProvider(RideProvider.class);
        List<List<Ride>> searchResult = new ArrayList<>(9);
        List<Ride> resultCollection;
        resultCollection = provider.findByBus(getBusById(BigInteger.valueOf(60)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyBus(getBusesByIds(Arrays.asList(BigInteger.valueOf(60),
                BigInteger.valueOf(63), BigInteger.valueOf(61))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByRidePoint(getRidePointById(BigInteger.valueOf(131)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyRidePoint(getRidePointsByIds(Arrays.asList(BigInteger.valueOf(132),
                BigInteger.valueOf(133))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByRideRoad(getRideRoadById(BigInteger.valueOf(146)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyRideRoad(getRideRoadsByIds(Arrays.asList(BigInteger.valueOf(145),
                BigInteger.valueOf(148), BigInteger.valueOf(147))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByTicket(getTicketById(BigInteger.valueOf(152)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyTicket(getTicketsByIds(Arrays.asList(BigInteger.valueOf(155), BigInteger.valueOf(156))));
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        RideProvider provider = getFactory().getProvider(RideProvider.class);
        List<List<BigInteger>> searchResult = new ArrayList<>(9);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByBus(getBusById(BigInteger.valueOf(60)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyBus(getBusesByIds(Arrays.asList(BigInteger.valueOf(60),
                BigInteger.valueOf(63), BigInteger.valueOf(61))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByRidePoint(getRidePointById(BigInteger.valueOf(131)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyRidePoint(getRidePointsByIds(Arrays.asList(BigInteger.valueOf(132),
                BigInteger.valueOf(133))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByRideRoad(getRideRoadById(BigInteger.valueOf(146)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyRideRoad(getRideRoadsByIds(Arrays.asList(BigInteger.valueOf(145),
                BigInteger.valueOf(148), BigInteger.valueOf(147))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByTicket(getTicketById(BigInteger.valueOf(152)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyTicket(getTicketsByIds(Arrays.asList(BigInteger.valueOf(155),
                BigInteger.valueOf(156))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> sets = new ArrayList<>(9);
        sets.add(new int[]{0});
        sets.add(new int[]{0, 2});
        sets.add(new int[]{3});
        sets.add(new int[]{0, 2});
        sets.add(new int[]{1});
        sets.add(new int[]{0, 1});
        sets.add(new int[]{2});
        sets.add(new int[]{2, 3});
        sets.add(new int[]{0, 1, 2, 3});
        return sets;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Ride getNewRide(BigInteger id, BigInteger busId, List<BigInteger> ridePoints,
            List<BigInteger> rideRoads, List<BigInteger> tickets) {
        return getNewRide(id, busId, ridePoints, rideRoads, tickets, false);
    }

    protected abstract Ride getNewRide(BigInteger id, BigInteger busId, List<BigInteger> ridePoints,
            List<BigInteger> rideRoads, List<BigInteger> tickets, boolean load);

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

    protected List<RidePoint> getRidePointsByIds(List<BigInteger> ids) {
        return getRidePointsByIds(ids, false);
    }

    protected List<RidePoint> getRidePointsByIds(List<BigInteger> ids, boolean load) {
        List<RidePoint> ridePointList = new ArrayList<>();
        for (BigInteger id : ids) {
            ridePointList.add(getRidePointById(id, load));
        }
        return ridePointList;
    }

    protected RidePoint getRidePointById(BigInteger id) {
        return getRidePointById(id, false);
    }

    protected RidePoint getRidePointById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(RidePointProvider.class).findById(id) : ridePoints.get(id);
    }

    protected List<RideRoad> getRideRoadsByIds(List<BigInteger> ids) {
        return getRideRoadsByIds(ids, false);
    }

    protected List<RideRoad> getRideRoadsByIds(List<BigInteger> ids, boolean load) {
        List<RideRoad> rideRoadList = new ArrayList<>();
        for (BigInteger id : ids) {
            rideRoadList.add(getRideRoadById(id, load));
        }
        return rideRoadList;
    }

    protected RideRoad getRideRoadById(BigInteger id) {
        return getRideRoadById(id, false);
    }

    protected RideRoad getRideRoadById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(RideRoadProvider.class).findById(id) : rideRoads.get(id);
    }

    protected List<Ticket> getTicketsByIds(List<BigInteger> ids) {
        return getTicketsByIds(ids, false);
    }

    protected List<Ticket> getTicketsByIds(List<BigInteger> ids, boolean load) {
        List<Ticket> ticketList = new ArrayList<>();
        for (BigInteger id : ids) {
            ticketList.add(getTicketById(id, load));
        }
        return ticketList;
    }

    protected Ticket getTicketById(BigInteger id) {
        return getTicketById(id, false);
    }

    protected Ticket getTicketById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(TicketProvider.class).findById(id) : tickets.get(id);
    }

    protected abstract void nullifyRidePoints(Ride ride);

    protected abstract void nullifyRideRoads(Ride ride);

    protected abstract void nullifyTickets(Ride ride);

    protected abstract void nullifyStationCollections(Station station);
}
