package com.anli.busstation.dal.test.geography;

import com.anli.busstation.dal.interfaces.entities.geography.Road;
import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.providers.geography.RoadProvider;
import com.anli.busstation.dal.interfaces.providers.geography.StationProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class RoadTest extends BasicDataAccessTestSceleton<Road> {

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
    protected BigInteger createEntityByProvider(Road roadSource) throws Exception {
        RoadProvider provider = getFactory().getProvider(RoadProvider.class);
        Road road = provider.create();
        road.setAStation(roadSource.getAStation());
        road.setLength(roadSource.getLength());
        road.setRidePrice(roadSource.getRidePrice());
        road.setZStation(roadSource.getZStation());
        return provider.save(road).getId();
    }

    @Override
    protected Road getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(RoadProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Road roadSource) throws Exception {
        RoadProvider provider = getFactory().getProvider(RoadProvider.class);
        Road road = provider.findById(id);
        road.setAStation(roadSource.getAStation());
        road.setLength(roadSource.getLength());
        road.setRidePrice(roadSource.getRidePrice());
        road.setZStation(roadSource.getZStation());
        provider.save(road);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        RoadProvider provider = getFactory().getProvider(RoadProvider.class);
        provider.remove(provider.findById(id));
    }

    @Override
    protected List<Road> getCreationTestSets() throws Exception {
        List<Road> testSets = new ArrayList<>(4);
        testSets.add(getNewRoad(BigInteger.ZERO, null, null, null, null));
        testSets.add(getNewRoad(BigInteger.ZERO, null, 0, BigDecimal.valueOf(0), null));
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(103), 1232, BigDecimal.valueOf(567),
                BigInteger.valueOf(105)));
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(106), 468, BigDecimal.valueOf(321.39),
                BigInteger.valueOf(109)));
        return testSets;
    }

    @Override
    protected List<Road> getUpdateTestSets() throws Exception {
        List<Road> testSets = new ArrayList<>(4);
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(105), 705, BigDecimal.valueOf(0), null));
        testSets.add(getNewRoad(BigInteger.ZERO, null, null, BigDecimal.valueOf(500), BigInteger.valueOf(100)));
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(107), 0, null,
                BigInteger.valueOf(104)));
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(107), 1000, BigDecimal.valueOf(560.01),
                BigInteger.valueOf(108)));
        return testSets;
    }

    @Override
    protected List<Road> getSearchTestSets() throws Exception {
        List<Road> testSets = new ArrayList<>(5);
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(102), 333, BigDecimal.valueOf(111.11),
                BigInteger.valueOf(103)));
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(104), 450, BigDecimal.valueOf(120.50),
                BigInteger.valueOf(105)));
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(106), 2500, BigDecimal.valueOf(178),
                BigInteger.valueOf(107)));
        testSets.add(getNewRoad(BigInteger.ZERO, BigInteger.valueOf(107), 3300, BigDecimal.valueOf(205.32), null));
        testSets.add(getNewRoad(BigInteger.ZERO, null, 3310, BigDecimal.valueOf(222.33),
                BigInteger.valueOf(109)));
        return testSets;
    }

    @Override
    protected List<List<Road>> performSearches() throws Exception {
        List<List<Road>> searchResult = new ArrayList<>(13);
        RoadProvider provider = getFactory().getProvider(RoadProvider.class);
        List<Road> resultCollection;
        resultCollection = provider.findByAStation(getStationById(BigInteger.valueOf(102)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyAStation(getStationsByIds(Arrays.asList(BigInteger.valueOf(102),
                BigInteger.valueOf(105), BigInteger.valueOf(106))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByZStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyZStation(getStationsByIds(Arrays.asList(BigInteger.valueOf(105),
                BigInteger.valueOf(106), BigInteger.valueOf(109))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyZStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByStation(getStationById(BigInteger.valueOf(107)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyStation(Collections.<Station>emptyList());
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyStation(getStationsByIds(Arrays.asList(BigInteger.valueOf(105),
                BigInteger.valueOf(106), BigInteger.valueOf(109))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByLengthRange(450, false, 3300, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByRidePriceRange(BigDecimal.valueOf(120.50), true,
                BigDecimal.valueOf(222.33), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(13);
        RoadProvider provider = getFactory().getProvider(RoadProvider.class);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByAStation(getStationById(BigInteger.valueOf(102)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyAStation(getStationsByIds(Arrays.asList(BigInteger.valueOf(102),
                BigInteger.valueOf(105), BigInteger.valueOf(106))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByZStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyZStation(getStationsByIds(Arrays.asList(BigInteger.valueOf(105),
                BigInteger.valueOf(106), BigInteger.valueOf(109))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyZStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByStation(getStationById(BigInteger.valueOf(107)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyStation(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyStation(Collections.<Station>emptyList());
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyStation(getStationsByIds(Arrays.asList(BigInteger.valueOf(105),
                BigInteger.valueOf(106), BigInteger.valueOf(109))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByLengthRange(450, false, 3300, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByRidePriceRange(BigDecimal.valueOf(120.50), true,
                BigDecimal.valueOf(222.33), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> indices = new ArrayList<>(13);
        indices.add(new int[]{0});
        indices.add(new int[]{0, 2});
        indices.add(new int[]{3});
        indices.add(new int[]{1, 4});
        indices.add(new int[]{});
        indices.add(new int[]{2, 3});
        indices.add(new int[]{3, 4});
        indices.add(new int[]{});
        indices.add(new int[]{});
        indices.add(new int[]{1, 2, 4});
        indices.add(new int[]{1, 2});
        indices.add(new int[]{2, 3, 4});
        indices.add(new int[]{0, 1, 2, 3, 4});
        return indices;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    @Override
    protected Road nullifyCollections(Road entity) {
        return entity;
    }

    protected Road getNewRoad(BigInteger id, BigInteger aStationId, Integer length,
            BigDecimal ridePrice, BigInteger zStationId) {
        return getNewRoad(id, aStationId, length, ridePrice, zStationId, false);
    }

    protected abstract Road getNewRoad(BigInteger id, BigInteger aStationId, Integer length,
            BigDecimal ridePrice, BigInteger zStationId, boolean load);

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
}
