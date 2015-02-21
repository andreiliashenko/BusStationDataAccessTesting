package com.anli.busstation.dal.test.maintenance;

import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.maintenance.BusRepairment;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusRepairmentProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class BusRepairmentTest extends BasicDataAccessTestSceleton<BusRepairment> {

    protected Map<BigInteger, Mechanic> mechanics;
    protected Map<BigInteger, Bus> buses;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, GasLabel> gasLabels = getFixtureCreator().createGasLabelFixture(10, 5);
        Map<BigInteger, Model> models = getFixtureCreator().createModelFixture(20, 5,
                new ArrayList(gasLabels.values()));
        Map<BigInteger, TechnicalState> technicalStates = getFixtureCreator().createTechnicalStateFixture(30, 5);
        buses = getFixtureCreator().createBusFixture(60, 10, new ArrayList(models.values()),
                new ArrayList(technicalStates.values()));
        Map<BigInteger, MechanicSkill> mechanicSkills = getFixtureCreator().createMechanicSkillFixture(50, 5);
        mechanics = getFixtureCreator().createMechanicFixture(50, 10, new ArrayList(mechanicSkills.values()));
    }

    @Override
    protected BigInteger createEntityByProvider(BusRepairment sourceRepairment) throws Exception {
        BusRepairmentProvider provider = getFactory().getProvider(BusRepairmentProvider.class);
        BusRepairment repairment = provider.create();
        repairment.setBeginTime(sourceRepairment.getBeginTime());
        repairment.setBus(sourceRepairment.getBus());
        repairment.setEndTime(sourceRepairment.getEndTime());
        repairment.setMechanic(sourceRepairment.getMechanic());
        repairment.setServiceCost(sourceRepairment.getServiceCost());
        repairment.setExpendablesPrice(sourceRepairment.getExpendablesPrice());
        repairment = provider.save(repairment);
        return repairment.getId();
    }

    @Override
    protected BusRepairment getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(BusRepairmentProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, BusRepairment sourceRepairment) throws Exception {
        BusRepairmentProvider provider = getFactory().getProvider(BusRepairmentProvider.class);
        BusRepairment repairment = provider.findById(id);
        repairment.setBeginTime(sourceRepairment.getBeginTime());
        repairment.setBus(sourceRepairment.getBus());
        repairment.setEndTime(sourceRepairment.getEndTime());
        repairment.setMechanic(sourceRepairment.getMechanic());
        repairment.setServiceCost(sourceRepairment.getServiceCost());
        repairment.setExpendablesPrice(sourceRepairment.getExpendablesPrice());
        provider.save(repairment);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        BusRepairmentProvider provider = getFactory().getProvider(BusRepairmentProvider.class);
        BusRepairment repairment = provider.findById(id);
        provider.remove(repairment);
    }

    @Override
    protected List<BusRepairment> getCreationTestSets() throws Exception {
        List<BusRepairment> testSets = new ArrayList<>(4);
        testSets.add(getNewRepairment(BigInteger.ZERO, null, null, null, null, null, null));
        testSets.add(getNewRepairment(BigInteger.ZERO, null, null, null, BigDecimal.ZERO, null, BigDecimal.ZERO));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(50), new DateTime(1992, 4, 25, 15, 21, 0, 0),
                new DateTime(1993, 1, 1, 18, 33, 0, 0), BigDecimal.valueOf(5550.99), BigInteger.valueOf(64), BigDecimal.valueOf(4401.32)));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(57), new DateTime(2006, 11, 11, 23, 55, 0, 0),
                new DateTime(2006, 6, 17, 18, 59, 0, 0), BigDecimal.valueOf(70730.29), BigInteger.valueOf(66), BigDecimal.valueOf(25999.98)));
        return testSets;
    }

    @Override
    protected List<BusRepairment> getUpdateTestSets() throws Exception {
        List<BusRepairment> testSets = new ArrayList<>(4);
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(59), new DateTime(1999, 10, 31, 17, 7, 0, 0),
                new DateTime(1999, 12, 31, 23, 59, 0, 0), BigDecimal.valueOf(10666.66), BigInteger.valueOf(68), BigDecimal.valueOf(9000)));
        testSets.add(getNewRepairment(BigInteger.ZERO, null, new DateTime(2005, 2, 28, 10, 42, 0, 0),
                new DateTime(2005, 2, 28, 13, 0, 0, 0), null, BigInteger.valueOf(67), BigDecimal.valueOf(1000.10)));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(53), new DateTime(2000, 7, 15, 7, 45, 0, 0),
                new DateTime(2000, 8, 18, 11, 11, 0, 0), BigDecimal.valueOf(200000), null, null));
        testSets.add(getNewRepairment(BigInteger.ZERO, null, null,
                null, null, null, null));
        return testSets;
    }

    @Override
    protected List<List<BusRepairment>> performSearches() throws Exception {
        List<List<BusRepairment>> searchResult = new ArrayList<>(9);
        BusRepairmentProvider provider = getFactory().getProvider(BusRepairmentProvider.class);
        List<BusRepairment> resultCollection;
        List<BigInteger> mechanicList = Arrays.asList(BigInteger.valueOf(50), BigInteger.valueOf(51),
                BigInteger.valueOf(52));
        List<BigInteger> busList = Arrays.asList(BigInteger.valueOf(61), BigInteger.valueOf(65),
                BigInteger.valueOf(66), BigInteger.valueOf(68));

        resultCollection = provider.findByMechanic(getMechanicById(BigInteger.valueOf(50)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyMechanic(getMechanicsByIds(mechanicList));
        searchResult.add(resultCollection);
        resultCollection = provider.findByBeginTimeRange(new DateTime(2014, 5, 9, 2, 45, 0, 0), false,
                new DateTime(2014, 5, 9, 8, 20, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByEndTimeRange(new DateTime(2014, 5, 9, 3, 10, 0, 0), true,
                new DateTime(2014, 5, 9, 10, 35, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByServiceCostRange(BigDecimal.valueOf(9812.99), false, BigDecimal.valueOf(20000.01), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByBus(getBusById(BigInteger.valueOf(63)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyBus(getBusesByIds(busList));
        searchResult.add(resultCollection);
        resultCollection = provider.findByExpendablesPriceRange(BigDecimal.valueOf(500.50), true,
                BigDecimal.valueOf(2222.22), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(9);
        BusRepairmentProvider provider = getFactory().getProvider(BusRepairmentProvider.class);
        List<BigInteger> resultCollection;
        List<BigInteger> mechanicList = Arrays.asList(BigInteger.valueOf(50), BigInteger.valueOf(51),
                BigInteger.valueOf(52));
        List<BigInteger> busList = Arrays.asList(BigInteger.valueOf(61), BigInteger.valueOf(65),
                BigInteger.valueOf(66), BigInteger.valueOf(68));

        resultCollection = provider.collectIdsByMechanic(getMechanicById(BigInteger.valueOf(50)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyMechanic(getMechanicsByIds(mechanicList));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByBeginTimeRange(new DateTime(2014, 5, 9, 2, 45, 0, 0), false,
                new DateTime(2014, 5, 9, 8, 20, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByEndTimeRange(new DateTime(2014, 5, 9, 3, 10, 0, 0), true,
                new DateTime(2014, 5, 9, 10, 35, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByServiceCostRange(BigDecimal.valueOf(9812.99), false, BigDecimal.valueOf(20000.01), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByBus(getBusById(BigInteger.valueOf(63)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyBus(getBusesByIds(busList));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByExpendablesPriceRange(BigDecimal.valueOf(500.50), true, BigDecimal.valueOf(2222.22), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<BusRepairment> getSearchTestSets() throws Exception {
        List<BusRepairment> testSets = new ArrayList<>(5);
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(50), new DateTime(2014, 5, 9, 1, 15, 0, 0),
                new DateTime(2014, 5, 9, 2, 30, 0, 0), BigDecimal.valueOf(1111.11), BigInteger.valueOf(61), BigDecimal.valueOf(500.50)));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(52), new DateTime(2014, 5, 9, 2, 45, 0, 0),
                new DateTime(2014, 5, 9, 3, 10, 0, 0), BigDecimal.valueOf(3356.79), BigInteger.valueOf(63), BigDecimal.valueOf(600.25)));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(54), new DateTime(2014, 5, 9, 5, 55, 0, 0),
                new DateTime(2014, 5, 9, 7, 0, 0, 0), BigDecimal.valueOf(5987.01), BigInteger.valueOf(65), BigDecimal.valueOf(700.75)));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(56), new DateTime(2014, 5, 9, 8, 20, 0, 0),
                new DateTime(2014, 5, 9, 10, 35, 0, 0), BigDecimal.valueOf(9812.99), BigInteger.valueOf(67), BigDecimal.valueOf(2222.22)));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(58), new DateTime(2014, 5, 9, 12, 0, 0, 0),
                new DateTime(2014, 5, 9, 16, 5, 0, 0), BigDecimal.valueOf(20000), BigInteger.valueOf(69), BigDecimal.valueOf(3333.33)));
        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList(9);

        searchResult.add(new int[]{0});
        searchResult.add(new int[]{0, 1});
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{2, 3});
        searchResult.add(new int[]{3, 4});
        searchResult.add(new int[]{1});
        searchResult.add(new int[]{0, 2});
        searchResult.add(new int[]{1, 2, 3});
        searchResult.add(new int[]{0, 1, 2, 3, 4});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected BusRepairment getNewRepairment(BigInteger id, BigInteger mechanicId, DateTime beginTime,
            DateTime endTime, BigDecimal cost, BigInteger busId, BigDecimal price) {
        return getNewRepairment(id, mechanicId, beginTime, endTime, cost, busId, price, false);
    }

    protected abstract BusRepairment getNewRepairment(BigInteger id, BigInteger mechanicId, DateTime beginTime,
            DateTime endTime, BigDecimal cost, BigInteger busId, BigDecimal price, boolean load);

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
    protected BusRepairment nullifyCollections(BusRepairment entity) {
        return entity;
    }
}
