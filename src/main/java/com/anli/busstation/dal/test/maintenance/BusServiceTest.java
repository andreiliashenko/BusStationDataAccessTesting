package com.anli.busstation.dal.test.maintenance;

import com.anli.busstation.dal.interfaces.entities.maintenance.BusRefuelling;
import com.anli.busstation.dal.interfaces.entities.maintenance.BusRepairment;
import com.anli.busstation.dal.interfaces.entities.maintenance.BusService;
import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusRefuellingProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusRepairmentProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusServiceProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class BusServiceTest extends BasicDataAccessTestSceleton<BusService> {
    
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
    
    protected BigInteger createBusRefuellingByProvider(BusRefuelling sourceRefuelling) throws Exception {
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
    
    protected BigInteger createBusRepairmentByProvider(BusRepairment sourceRepairment) throws Exception {
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
    protected BigInteger createEntityByProvider(BusService entity) throws Exception {
        if (entity instanceof BusRefuelling) {
            return createBusRefuellingByProvider((BusRefuelling) entity);
        }
        if (entity instanceof BusRepairment) {
            return createBusRepairmentByProvider((BusRepairment) entity);
        }
        throw new Exception("Incorrect entity");
    }
    
    @Override
    protected BusService getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(BusServiceProvider.class).findById(id);
    }
    
    @Override
    protected void updateEntityByProvider(BigInteger id, BusService sourceService) throws Exception {
        BusServiceProvider provider = getFactory().getProvider(BusServiceProvider.class);
        BusService service = provider.findById(id);
        service.setBeginTime(sourceService.getBeginTime());
        service.setBus(sourceService.getBus());
        service.setEndTime(sourceService.getEndTime());
        service.setMechanic(sourceService.getMechanic());
        service.setServiceCost(sourceService.getServiceCost());
        if (sourceService instanceof BusRefuelling) {
            ((BusRefuelling) service).setVolume(((BusRefuelling) sourceService).getVolume());
        }
        if (sourceService instanceof BusRepairment) {
            ((BusRepairment) service).setExpendablesPrice(((BusRepairment) sourceService).getExpendablesPrice());
        }
        provider.save(service);
    }
    
    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        BusServiceProvider provider = getFactory().getProvider(BusServiceProvider.class);
        BusService service = provider.findById(id);
        provider.remove(service);
    }
    
    @Override
    protected List<BusService> getCreationTestSets() throws Exception {
        List<BusService> testSets = new ArrayList<>(4);
        testSets.add(getNewRefuelling(BigInteger.ZERO, null, null, null, null, null, null));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(55), new DateTime(2015, 3, 11, 23, 59, 0, 0),
                new DateTime(2015, 3, 12, 11, 33, 0, 0), BigDecimal.valueOf(11111.11), BigInteger.valueOf(67), 1000));
        testSets.add(getNewRepairment(BigInteger.ZERO, null, null, null, BigDecimal.ZERO, null, BigDecimal.ZERO));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(50), new DateTime(1992, 4, 25, 15, 21, 0, 0),
                new DateTime(1993, 1, 1, 18, 33, 0, 0), BigDecimal.valueOf(5550.99), BigInteger.valueOf(64), BigDecimal.valueOf(4401.32)));
        return testSets;
    }
    
    @Override
    protected List<BusService> getUpdateTestSets() throws Exception {
        List<BusService> testSets = new ArrayList<>(4);
        testSets.add(getNewRefuelling(BigInteger.ZERO, null, new DateTime(2015, 12, 19, 13, 37, 0, 0),
                new DateTime(2015, 10, 1, 16, 34, 0, 0), null, BigInteger.valueOf(64), 222));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(51), new DateTime(2000, 7, 15, 7, 45, 0, 0),
                new DateTime(2000, 8, 18, 11, 11, 0, 0), BigDecimal.valueOf(200000), null, null));
        testSets.add(getNewRepairment(BigInteger.ZERO, null, new DateTime(2005, 2, 28, 10, 42, 0, 0),
                new DateTime(2005, 2, 28, 13, 0, 0, 0), null, BigInteger.valueOf(67), BigDecimal.valueOf(1000.10)));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(53), new DateTime(2000, 7, 15, 7, 45, 0, 0),
                new DateTime(2000, 8, 18, 11, 11, 0, 0), BigDecimal.valueOf(200000), null, null));
        return testSets;
    }
    
    @Override
    protected List<BusService> getSearchTestSets() throws Exception {
        List<BusService> testSets = new ArrayList<>(6);
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(53), new DateTime(2014, 5, 9, 2, 44, 0, 0),
                new DateTime(2015, 1, 1, 1, 1, 0, 0), BigDecimal.valueOf(1234.56), BigInteger.valueOf(60), 100));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(53), new DateTime(2014, 5, 9, 2, 45, 0, 0),
                new DateTime(2015, 1, 1, 1, 2, 0, 0), BigDecimal.valueOf(3356.79), BigInteger.valueOf(61), BigDecimal.valueOf(600.25)));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(53), new DateTime(2014, 5, 9, 3, 15, 0, 0),
                new DateTime(2015, 1, 1, 1, 3, 0, 0), BigDecimal.valueOf(5560.55), BigInteger.valueOf(61), 756));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(54), new DateTime(2014, 5, 9, 8, 20, 0, 0),
                new DateTime(2015, 1, 1, 2, 1, 0, 0), BigDecimal.valueOf(5987.01), BigInteger.valueOf(65), BigDecimal.valueOf(700.75)));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(59), new DateTime(2014, 5, 9, 8, 21, 0, 0),
                new DateTime(2015, 1, 1, 5, 6, 0, 0), BigDecimal.valueOf(6789.02), BigInteger.valueOf(68), 1500));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(59), new DateTime(2014, 5, 10, 2, 45, 0, 0),
                new DateTime(2015, 1, 2, 5, 6, 0, 0), BigDecimal.valueOf(7002.12), BigInteger.valueOf(67), BigDecimal.valueOf(3333.33)));
        return testSets;
    }
    
    @Override
    protected List<List<BusService>> performSearches() throws Exception {
        List<List<BusService>> searchResult = new ArrayList<>(8);
        BusServiceProvider provider = getFactory().getProvider(BusServiceProvider.class);
        List<BusService> resultCollection;
        resultCollection = provider.findByBus(getBusById(BigInteger.valueOf(61)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyBus(getBusesByIds(Arrays.asList(BigInteger.valueOf(63),
                BigInteger.valueOf(64), BigInteger.valueOf(65), BigInteger.valueOf(67))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByMechanic(getMechanicById(BigInteger.valueOf(53)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyMechanic(getMechanicsByIds(Arrays.asList(BigInteger.valueOf(54), 
                BigInteger.valueOf(59))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByBeginTimeRange(new DateTime(2014, 5, 9, 2, 45, 0, 0), false, 
                new DateTime(2014, 5, 9, 8, 20, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByEndTimeRange(new DateTime(2015, 1, 1, 1, 1, 0, 0), true, 
                new DateTime(2015, 1, 1, 5, 6, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByServiceCostRange(BigDecimal.valueOf(5560.55), false, 
                BigDecimal.valueOf(7002.12), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }
    
    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(8);
        BusServiceProvider provider = getFactory().getProvider(BusServiceProvider.class);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByBus(getBusById(BigInteger.valueOf(61)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyBus(getBusesByIds(Arrays.asList(BigInteger.valueOf(63),
                BigInteger.valueOf(64), BigInteger.valueOf(65), BigInteger.valueOf(67))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMechanic(getMechanicById(BigInteger.valueOf(53)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyMechanic(getMechanicsByIds(Arrays.asList(BigInteger.valueOf(54), 
                BigInteger.valueOf(59))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByBeginTimeRange(new DateTime(2014, 5, 9, 2, 45, 0, 0), false, 
                new DateTime(2014, 5, 9, 8, 20, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByEndTimeRange(new DateTime(2015, 1, 1, 1, 1, 0, 0), true, 
                new DateTime(2015, 1, 1, 5, 6, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByServiceCostRange(BigDecimal.valueOf(5560.55), false, 
                BigDecimal.valueOf(7002.12), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }
    
    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(9);
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{3, 5});
        searchResult.add(new int[]{0, 1, 2});
        searchResult.add(new int[]{3, 4, 5});
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{1, 2, 3, 4});
        searchResult.add(new int[]{2, 3, 4});
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5});
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
    
    protected abstract BusRefuelling getNewRefuelling(BigInteger id, BigInteger mechanicId, DateTime beginTime,
            DateTime endTime, BigDecimal cost, BigInteger busId, Integer volume, boolean load);
    
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
    protected BusService nullifyCollections(BusService entity) {
        return entity;
    }
}
