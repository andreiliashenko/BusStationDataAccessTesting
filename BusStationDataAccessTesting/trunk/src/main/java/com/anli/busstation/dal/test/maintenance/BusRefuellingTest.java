package com.anli.busstation.dal.test.maintenance;

import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.maintenance.BusRefuelling;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusRefuellingProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class BusRefuellingTest extends BasicDataAccessTestSceleton<BusRefuelling> {

    protected Map<BigInteger, Mechanic> mechanics;
    protected Map<BigInteger, Bus> buses;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, GasLabel> gasLabels = getFixtureCreator().createGasLabelFixture(10, 5);
        Map<BigInteger, Model> models = getFixtureCreator().createModelFixture(20, 5,
                new ArrayList(gasLabels.values()));
        Map<BigInteger, TechnicalState> technicalStates
                = getFixtureCreator().createTechnicalStateFixture(30, 5);
        buses = getFixtureCreator().createBusFixture(60, 10, new ArrayList(models.values()),
                new ArrayList(technicalStates.values()));
        Map<BigInteger, MechanicSkill> mechanicSkills
                = getFixtureCreator().createMechanicSkillFixture(50, 5);
        mechanics = getFixtureCreator().createMechanicFixture(50, 10,
                new ArrayList(mechanicSkills.values()));
    }

    @Override
    protected BigInteger createEntityByProvider(BusRefuelling sourceRefuelling) throws Exception {
        BusRefuellingProvider provider = getFactory().getProvider(BusRefuellingProvider.class);
        BusRefuelling refuelling = provider.create();
        refuelling.setBeginTime(sourceRefuelling.getBeginTime());
        refuelling.setBus(sourceRefuelling.getBus());
        refuelling.setEndTime(sourceRefuelling.getEndTime());
        refuelling.setMechanic(sourceRefuelling.getMechanic());
        refuelling.setServiceCost(sourceRefuelling.getServiceCost());
        refuelling.setVolume(sourceRefuelling.getVolume());
        refuelling = provider.save(refuelling);
        return refuelling.getId();
    }

    @Override
    protected BusRefuelling getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(BusRefuellingProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, BusRefuelling sourceRefuelling) throws Exception {
        BusRefuellingProvider provider = getFactory().getProvider(BusRefuellingProvider.class);
        BusRefuelling refuelling = provider.findById(id);
        refuelling.setBeginTime(sourceRefuelling.getBeginTime());
        refuelling.setBus(sourceRefuelling.getBus());
        refuelling.setEndTime(sourceRefuelling.getEndTime());
        refuelling.setMechanic(sourceRefuelling.getMechanic());
        refuelling.setServiceCost(sourceRefuelling.getServiceCost());
        refuelling.setVolume(sourceRefuelling.getVolume());
        provider.save(refuelling);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        BusRefuellingProvider provider = getFactory().getProvider(BusRefuellingProvider.class);
        BusRefuelling refuelling = provider.findById(id);
        provider.remove(refuelling);
    }

    @Override
    protected List<BusRefuelling> getCreationTestSets() throws Exception {
        List<BusRefuelling> testSets = new ArrayList<>(4);
        testSets.add(getNewRefuelling(BigInteger.ZERO, null, null, null, null, null, null));
        testSets.add(getNewRefuelling(BigInteger.ZERO, null, null, null, BigDecimal.ZERO, null, 0));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(51),
                new DateTime(1994, 8, 7, 19, 29, 0, 0), new DateTime(2000, 5, 8, 1, 0, 0, 0),
                BigDecimal.valueOf(3978.98), BigInteger.valueOf(66), 495));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(55),
                new DateTime(2015, 3, 11, 23, 59, 0, 0), new DateTime(2015, 3, 12, 11, 33, 0, 0),
                BigDecimal.valueOf(11111.11), BigInteger.valueOf(67), 1000));
        return testSets;
    }

    @Override
    protected List<BusRefuelling> getUpdateTestSets() throws Exception {
        List<BusRefuelling> testSets = new ArrayList<>(4);
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(55),
                new DateTime(2016, 8, 29, 15, 24, 0, 0), new DateTime(2016, 2, 3, 22, 12, 0, 0),
                BigDecimal.valueOf(789.09), BigInteger.valueOf(60), 30));
        testSets.add(getNewRefuelling(BigInteger.ZERO, null, new DateTime(2015, 12, 19, 13, 37, 0, 0),
                new DateTime(2015, 10, 1, 16, 34, 0, 0), null, BigInteger.valueOf(64), 222));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(51),
                new DateTime(2000, 7, 15, 7, 45, 0, 0), new DateTime(2000, 8, 18, 11, 11, 0, 0),
                BigDecimal.valueOf(200000), null, null));
        testSets.add(getNewRefuelling(BigInteger.ZERO, null, null, null, null, null, null));
        return testSets;
    }

    @Override
    protected List<List<BusRefuelling>> performSearches() throws Exception {
        List<List<BusRefuelling>> searchResult = new ArrayList<>(9);
        BusRefuellingProvider provider = getFactory().getProvider(BusRefuellingProvider.class);
        List<BusRefuelling> resultCollection;
        List<BigInteger> mechanicList = Arrays.asList(BigInteger.valueOf(50), BigInteger.valueOf(51),
                BigInteger.valueOf(53), BigInteger.valueOf(54));
        List<BigInteger> busList = Arrays.asList(BigInteger.valueOf(64), BigInteger.valueOf(65),
                BigInteger.valueOf(68));
        resultCollection = provider.findByMechanic(getMechanicById(BigInteger.valueOf(57)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyMechanic(getMechanicsByIds(mechanicList));
        searchResult.add(resultCollection);
        resultCollection = provider.findByBeginTimeRange(new DateTime(2015, 1, 1, 1, 1, 0, 0), true,
                new DateTime(2015, 1, 1, 5, 6, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByEndTimeRange(new DateTime(2015, 1, 1, 7, 8, 0, 0), false,
                new DateTime(2015, 1, 1, 15, 17, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByServiceCostRange(BigDecimal.valueOf(7890.12), true,
                BigDecimal.valueOf(20123.45), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByBus(getBusById(BigInteger.valueOf(64)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyBus(getBusesByIds(busList));
        searchResult.add(resultCollection);
        resultCollection = provider.findByVolumeRange(100, false, 1023, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(9);
        BusRefuellingProvider provider = getFactory().getProvider(BusRefuellingProvider.class);
        List<BigInteger> resultCollection;
        List<BigInteger> mechanicList = Arrays.asList(BigInteger.valueOf(50), BigInteger.valueOf(51),
                BigInteger.valueOf(53), BigInteger.valueOf(54));
        List<BigInteger> busList = Arrays.asList(BigInteger.valueOf(64), BigInteger.valueOf(65),
                BigInteger.valueOf(68));
        resultCollection = provider.collectIdsByMechanic(getMechanicById(BigInteger.valueOf(57)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyMechanic(getMechanicsByIds(mechanicList));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByBeginTimeRange(new DateTime(2015, 1, 1, 1, 1, 0, 0),
                true, new DateTime(2015, 1, 1, 5, 6, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByEndTimeRange(new DateTime(2015, 1, 1, 7, 8, 0, 0), false,
                new DateTime(2015, 1, 1, 15, 17, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByServiceCostRange(BigDecimal.valueOf(7890.12), true,
                BigDecimal.valueOf(20123.45), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByBus(getBusById(BigInteger.valueOf(64)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyBus(getBusesByIds(busList));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByVolumeRange(100, false, 1023, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<BusRefuelling> getSearchTestSets() throws Exception {
        List<BusRefuelling> testSets = new ArrayList<>(5);
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(51),
                new DateTime(2015, 1, 1, 1, 1, 0, 0), new DateTime(2015, 1, 1, 2, 2, 0, 0),
                BigDecimal.valueOf(1234.56), BigInteger.valueOf(60), 100));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(53),
                new DateTime(2015, 1, 1, 3, 3, 0, 0), new DateTime(2015, 1, 1, 4, 4, 0, 0),
                BigDecimal.valueOf(7890.12), BigInteger.valueOf(62), 389));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(55),
                new DateTime(2015, 1, 1, 5, 6, 0, 0), new DateTime(2015, 1, 1, 7, 8, 0, 0),
                BigDecimal.valueOf(14567.89), BigInteger.valueOf(64), 756));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(57),
                new DateTime(2015, 1, 1, 9, 10, 0, 0), new DateTime(2015, 1, 1, 11, 12, 0, 0),
                BigDecimal.valueOf(20123.45), BigInteger.valueOf(66), 1023));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(59),
                new DateTime(2015, 1, 1, 13, 14, 0, 0), new DateTime(2015, 1, 1, 15, 16, 0, 0),
                BigDecimal.valueOf(67890.02), BigInteger.valueOf(68), 1500));
        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(9);
        searchResult.add(new int[]{3});
        searchResult.add(new int[]{0, 1});
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{2, 3, 4});
        searchResult.add(new int[]{2, 3});
        searchResult.add(new int[]{2});
        searchResult.add(new int[]{2, 4});
        searchResult.add(new int[]{0, 1, 2});
        searchResult.add(new int[]{0, 1, 2, 3, 4});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected BusRefuelling getNewRefuelling(BigInteger id, BigInteger mechanicId, DateTime beginTime,
            DateTime endTime, BigDecimal cost, BigInteger busId, Integer volume) {
        return getNewRefuelling(id, mechanicId, beginTime, endTime, cost, busId, volume, false);
    }

    protected abstract BusRefuelling getNewRefuelling(BigInteger id, BigInteger mechanicId,
            DateTime beginTime, DateTime endTime, BigDecimal cost, BigInteger busId,
            Integer volume, boolean load);

    protected Mechanic getMechanicById(BigInteger id) {
        return getMechanicById(id, false);
    }

    protected Mechanic getMechanicById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(MechanicProvider.class).findById(id) : mechanics.get(id);
    }

    protected List<Mechanic> getMechanicsByIds(List<BigInteger> ids) {
        return getMechanicsByIds(ids, false);
    }

    protected List<Mechanic> getMechanicsByIds(List<BigInteger> ids, boolean load) {
        List<Mechanic> mechanicList = new ArrayList<>();
        for (BigInteger id : ids) {
            mechanicList.add(getMechanicById(id, load));
        }
        return mechanicList;
    }

    protected Bus getBusById(BigInteger id) {
        return getBusById(id, false);
    }

    protected Bus getBusById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(BusProvider.class).findById(id) : buses.get(id);
    }

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

    @Override
    protected BusRefuelling nullifyCollections(BusRefuelling entity) {
        return entity;
    }
}
