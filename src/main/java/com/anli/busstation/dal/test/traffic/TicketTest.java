package com.anli.busstation.dal.test.traffic;

import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.staff.Salesman;
import com.anli.busstation.dal.interfaces.entities.traffic.RidePoint;
import com.anli.busstation.dal.interfaces.entities.traffic.RoutePoint;
import com.anli.busstation.dal.interfaces.entities.traffic.Ticket;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.providers.staff.SalesmanProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RidePointProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.TicketProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class TicketTest extends BasicDataAccessTestSceleton<Ticket> {

    protected Map<BigInteger, RidePoint> ridePoints;
    protected Map<BigInteger, Salesman> salesmen;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        salesmen = getFixtureCreator().createSalesmanFixture(90, 10);
        Map<BigInteger, Station> stations = getFixtureCreator().createStationFixture(100, 10,
                new ArrayList<Bus>(0), new ArrayList<Employee>(0));
        for (Station station : stations.values()) {
            nullifyStationCollections(station);
        }
        Map<BigInteger, RoutePoint> routePoints = getFixtureCreator().createRoutePointFixture(110, 10,
                new ArrayList(stations.values()));
        ridePoints = getFixtureCreator().createRidePointFixture(130, 10, new ArrayList(routePoints.values()));
    }

    @Override
    protected BigInteger createEntityByProvider(Ticket sourceTicket) throws Exception {
        TicketProvider provider = getFactory().getProvider(TicketProvider.class);
        Ticket ticket = provider.create();
        ticket.setArrivalPoint(sourceTicket.getArrivalPoint());
        ticket.setCustomerName(sourceTicket.getCustomerName());
        ticket.setDeparturePoint(sourceTicket.getDeparturePoint());
        ticket.setPrice(sourceTicket.getPrice());
        ticket.setSaleDate(sourceTicket.getSaleDate());
        ticket.setSalesman(sourceTicket.getSalesman());
        ticket.setSeat(sourceTicket.getSeat());
        ticket.setSold(sourceTicket.isSold());
        return provider.save(ticket).getId();
    }

    @Override
    protected Ticket getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(TicketProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Ticket sourceTicket) throws Exception {
        TicketProvider provider = getFactory().getProvider(TicketProvider.class);
        Ticket ticket = provider.findById(id);
        ticket.setArrivalPoint(sourceTicket.getArrivalPoint());
        ticket.setCustomerName(sourceTicket.getCustomerName());
        ticket.setDeparturePoint(sourceTicket.getDeparturePoint());
        ticket.setPrice(sourceTicket.getPrice());
        ticket.setSaleDate(sourceTicket.getSaleDate());
        ticket.setSalesman(sourceTicket.getSalesman());
        ticket.setSeat(sourceTicket.getSeat());
        ticket.setSold(sourceTicket.isSold());
        provider.save(ticket);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        TicketProvider provider = getFactory().getProvider(TicketProvider.class);
        provider.remove(provider.findById(id));
    }

    @Override
    protected List<Ticket> getCreationTestSets() throws Exception {
        List<Ticket> testSets = new ArrayList<>(4);
        testSets.add(getNewTicket(BigInteger.ZERO, null, null, null, null,
                null, null, null, false));
        testSets.add(getNewTicket(BigInteger.ZERO, null, "", null, BigDecimal.valueOf(0),
                new DateTime(0), null, 0, false));
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(130), "Pupkin", BigInteger.valueOf(131),
                BigDecimal.valueOf(120.33), new DateTime(2015, 3, 11, 15, 24, 43, 0), BigInteger.valueOf(92), 15, true));
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(132), "Сидоров", BigInteger.valueOf(134),
                BigDecimal.valueOf(251.99), new DateTime(2015, 2, 18, 17, 0, 33, 0), BigInteger.valueOf(97), 4, true));
        return testSets;
    }

    @Override
    protected List<Ticket> getUpdateTestSets() throws Exception {
        List<Ticket> testSets = new ArrayList<>(4);
        testSets.add(getNewTicket(BigInteger.ZERO, null, "", BigInteger.valueOf(131),
                BigDecimal.valueOf(0), new DateTime(2015, 7, 28, 9, 10, 1, 0), BigInteger.valueOf(95), 44, false));
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(130), "Stephens", BigInteger.valueOf(131),
                null, new DateTime(2015, 3, 11, 15, 24, 43, 0), BigInteger.valueOf(92), 15, true));
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(137), "Johnson", BigInteger.valueOf(131),
                BigDecimal.valueOf(302.11), null, null, 24, false));
        testSets.add(getNewTicket(BigInteger.ZERO, null, null, BigInteger.valueOf(134),
                null, new DateTime(0), BigInteger.valueOf(90), 0, true));
        return testSets;
    }

    @Override
    protected List<Ticket> getSearchTestSets() throws Exception {
        List<Ticket> testSets = new ArrayList<>();
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(131), "", BigInteger.valueOf(132),
                BigDecimal.valueOf(120.55), new DateTime(2015, 7, 3, 23, 59, 59, 0), null, 10, true));
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(131), "Good name", BigInteger.valueOf(134),
                BigDecimal.valueOf(201.11), new DateTime(2015, 7, 4, 0, 45, 7, 0), BigInteger.valueOf(93), 15, true));
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(132), null, BigInteger.valueOf(135),
                BigDecimal.valueOf(284.47), null, BigInteger.valueOf(95), null, true));
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(132), "Bad name", BigInteger.valueOf(139),
                BigDecimal.valueOf(333.99), new DateTime(2015, 7, 4, 2, 32, 12, 0), BigInteger.valueOf(96), 20, false));
        testSets.add(getNewTicket(BigInteger.ZERO, BigInteger.valueOf(134), "Something", BigInteger.valueOf(130),
                BigDecimal.valueOf(334.0), new DateTime(2015, 7, 8, 17, 33, 59, 0), BigInteger.valueOf(97), 21, false));
        return testSets;
    }

    @Override
    protected List<List<Ticket>> performSearches() throws Exception {
        TicketProvider provider = getFactory().getProvider(TicketProvider.class);
        List<List<Ticket>> searchResult = new ArrayList<>(16);
        List<Ticket> resultCollection;
        resultCollection = provider.findByArrivalPoint(getRidePointById(BigInteger.valueOf(131)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyArrivalPoint(getRidePointsByIds(Arrays.asList(BigInteger.valueOf(132),
                BigInteger.valueOf(133), BigInteger.valueOf(134))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByDeparturePoint(getRidePointById(BigInteger.valueOf(132)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyDeparturePoint(getRidePointsByIds(Arrays.asList(BigInteger.valueOf(133),
                BigInteger.valueOf(134), BigInteger.valueOf(135))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByCustomerName("Good name");
        searchResult.add(resultCollection);
        resultCollection = provider.findByCustomerNameRegexp("name$");
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(120.55), true, BigDecimal.valueOf(333.99), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySaleDateRange(new DateTime(2015, 7, 4, 2, 32, 12, 0), false,
                new DateTime(2015, 7, 8, 17, 34, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySalesman(getSalesmanById(BigInteger.valueOf(96)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnySalesman(getSalesmenByIds(Arrays.asList(BigInteger.valueOf(95),
                BigInteger.valueOf(96), BigInteger.valueOf(99))));
        searchResult.add(resultCollection);
        resultCollection = provider.findBySeat(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySeat(21);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySeatRange(10, false, 21, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySold(true);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySold(false);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        TicketProvider provider = getFactory().getProvider(TicketProvider.class);
        List<List<BigInteger>> searchResult = new ArrayList<>(16);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByArrivalPoint(getRidePointById(BigInteger.valueOf(131)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyArrivalPoint(getRidePointsByIds(Arrays.asList(BigInteger.valueOf(132),
                BigInteger.valueOf(133), BigInteger.valueOf(134))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDeparturePoint(getRidePointById(BigInteger.valueOf(132)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyDeparturePoint(getRidePointsByIds(Arrays.asList(BigInteger.valueOf(133),
                BigInteger.valueOf(134), BigInteger.valueOf(135))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByCustomerName("Good name");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByCustomerNameRegexp("name$");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(120.55), true,
                BigDecimal.valueOf(333.99), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySaleDateRange(new DateTime(2015, 7, 4, 2, 32, 12, 0), false,
                new DateTime(2015, 7, 8, 17, 34, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySalesman(getSalesmanById(BigInteger.valueOf(96)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnySalesman(getSalesmenByIds(Arrays.asList(BigInteger.valueOf(95),
                BigInteger.valueOf(96), BigInteger.valueOf(99))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySeat(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySeat(21);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySeatRange(10, false, 21, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySold(true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySold(false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> sets = new ArrayList<>(16);
        sets.add(new int[]{0, 1});
        sets.add(new int[]{2, 3, 4});
        sets.add(new int[]{0});
        sets.add(new int[]{1, 2});
        sets.add(new int[]{1});
        sets.add(new int[]{1, 3});
        sets.add(new int[]{1, 2, 3});
        sets.add(new int[]{3, 4});
        sets.add(new int[]{3});
        sets.add(new int[]{2, 3});
        sets.add(new int[]{2});
        sets.add(new int[]{4});
        sets.add(new int[]{0, 1, 3});
        sets.add(new int[]{0, 1, 2});
        sets.add(new int[]{3, 4});
        sets.add(new int[]{0, 1, 2, 3, 4});
        return sets;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Ticket getNewTicket(BigInteger id, BigInteger arrivalPointId,
            String customerName, BigInteger departurePointId, BigDecimal price,
            DateTime saleDate, BigInteger salesmanId, Integer seat, boolean sold) {
        return getNewTicket(id, arrivalPointId, customerName, departurePointId, price, saleDate,
                salesmanId, seat, sold, false);
    }

    protected abstract Ticket getNewTicket(BigInteger id, BigInteger arrivalPointId,
            String customerName, BigInteger departurePointId, BigDecimal price,
            DateTime saleDate, BigInteger salesmanId, Integer seat, boolean sold, boolean load);

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
        if (load) {
            return getFactory().getProvider(RidePointProvider.class).findById(id);
        } else {
            return ridePoints.get(id);
        }
    }

    protected List<Salesman> getSalesmenByIds(List<BigInteger> ids) {
        return getSalesmenByIds(ids, false);
    }

    protected List<Salesman> getSalesmenByIds(List<BigInteger> ids, boolean load) {
        List<Salesman> salesmenList = new ArrayList<>();
        for (BigInteger id : ids) {
            salesmenList.add(getSalesmanById(id, load));
        }
        return salesmenList;
    }

    protected Salesman getSalesmanById(BigInteger id) {
        return getSalesmanById(id, false);
    }

    protected Salesman getSalesmanById(BigInteger id, boolean load) {
        if (load) {
            return getFactory().getProvider(SalesmanProvider.class).findById(id);
        } else {
            return salesmen.get(id);
        }
    }

    protected abstract void nullifyStationCollections(Station station);

    @Override
    protected Ticket nullifyCollections(Ticket entity) {
        return entity;
    }
}
