package com.anli.busstation.dal.test.maintenance;

import com.anli.busstation.dal.interfaces.entities.maintenance.BusRefuelling;
import com.anli.busstation.dal.interfaces.entities.maintenance.BusRepairment;
import com.anli.busstation.dal.interfaces.entities.maintenance.BusService;
import com.anli.busstation.dal.interfaces.entities.maintenance.StationService;
import com.anli.busstation.dal.interfaces.entities.maintenance.TechnicalAssignment;
import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusRefuellingProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusRepairmentProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.StationServiceProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.TechnicalAssignmentProvider;
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

public abstract class TechnicalAssignmentTest extends BasicDataAccessTestSceleton<TechnicalAssignment> {

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

    protected BigInteger createStationServiceByProvider(StationService sourceService) throws Exception {
        StationServiceProvider provider = getFactory().getProvider(StationServiceProvider.class);
        StationService service = provider.create();
        service.setBeginTime(sourceService.getBeginTime());
        service.setDescription(sourceService.getDescription());
        service.setEndTime(sourceService.getEndTime());
        service.setMechanic(sourceService.getMechanic());
        service.setServiceCost(sourceService.getServiceCost());
        service = provider.save(service);
        return service.getId();
    }

    @Override
    protected BigInteger createEntityByProvider(TechnicalAssignment entity) throws Exception {
        if (entity instanceof BusRefuelling) {
            return createBusRefuellingByProvider((BusRefuelling) entity);
        }
        if (entity instanceof BusRepairment) {
            return createBusRepairmentByProvider((BusRepairment) entity);
        }
        if (entity instanceof StationService) {
            return createStationServiceByProvider((StationService) entity);
        }
        throw new Exception("Incorrect entity");
    }

    @Override
    protected TechnicalAssignment getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(TechnicalAssignmentProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id,
            TechnicalAssignment sourceAssignment) throws Exception {
        TechnicalAssignmentProvider provider = getFactory().getProvider(TechnicalAssignmentProvider.class);
        TechnicalAssignment assignment = provider.findById(id);
        assignment.setBeginTime(sourceAssignment.getBeginTime());
        assignment.setEndTime(sourceAssignment.getEndTime());
        assignment.setMechanic(sourceAssignment.getMechanic());
        assignment.setServiceCost(sourceAssignment.getServiceCost());
        if (sourceAssignment instanceof BusService) {
            ((BusService) assignment).setBus(((BusService) sourceAssignment).getBus());
        }
        if (sourceAssignment instanceof BusRefuelling) {
            ((BusRefuelling) assignment).setVolume(((BusRefuelling) sourceAssignment).getVolume());
        }
        if (sourceAssignment instanceof BusRepairment) {
            ((BusRepairment) assignment).setExpendablesPrice(((BusRepairment) sourceAssignment)
                    .getExpendablesPrice());
        }
        if (sourceAssignment instanceof StationService) {
            ((StationService) assignment).setDescription(((StationService) sourceAssignment)
                    .getDescription());
        }
        provider.save(assignment);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        TechnicalAssignmentProvider provider = getFactory().getProvider(TechnicalAssignmentProvider.class);
        TechnicalAssignment assignment = provider.findById(id);
        provider.remove(assignment);
    }

    @Override
    protected List<TechnicalAssignment> getCreationTestSets() throws Exception {
        List<TechnicalAssignment> testSets = new ArrayList<>(6);
        testSets.add(getNewRefuelling(BigInteger.ZERO, null, null, null, null, null, null));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(55),
                new DateTime(2015, 3, 11, 23, 59, 0, 0), new DateTime(2015, 3, 12, 11, 33, 0, 0),
                BigDecimal.valueOf(11111.11), BigInteger.valueOf(67), 1000));
        testSets.add(getNewRepairment(BigInteger.ZERO, null, null, null, BigDecimal.ZERO,
                null, BigDecimal.ZERO));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(50),
                new DateTime(1992, 4, 25, 15, 21, 0, 0), new DateTime(1993, 1, 1, 18, 33, 0, 0),
                BigDecimal.valueOf(5550.99), BigInteger.valueOf(64), BigDecimal.valueOf(4401.32)));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(51),
                new DateTime(1991, 1, 1, 15, 20, 0, 0), new DateTime(1991, 1, 1, 16, 30, 0, 0),
                BigDecimal.valueOf(5550.99), "Помывка"));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(50),
                new DateTime(1998, 1, 1, 14, 0, 0, 0), new DateTime(2001, 6, 5, 12, 15, 0, 0),
                BigDecimal.valueOf(70730.29), "Реставрация"));
        return testSets;
    }

    @Override
    protected List<TechnicalAssignment> getUpdateTestSets() throws Exception {
        List<TechnicalAssignment> testSets = new ArrayList<>(6);
        testSets.add(getNewRefuelling(BigInteger.ZERO, null, new DateTime(2015, 12, 19, 13, 37, 0, 0),
                new DateTime(2015, 10, 1, 16, 34, 0, 0), null, BigInteger.valueOf(64), 222));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(51),
                new DateTime(2000, 7, 15, 7, 45, 0, 0), new DateTime(2000, 8, 18, 11, 11, 0, 0),
                BigDecimal.valueOf(200000), null, null));
        testSets.add(getNewRepairment(BigInteger.ZERO, null, new DateTime(2005, 2, 28, 10, 42, 0, 0),
                new DateTime(2005, 2, 28, 13, 0, 0, 0), null, BigInteger.valueOf(67),
                BigDecimal.valueOf(1000.10)));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(53),
                new DateTime(2000, 7, 15, 7, 45, 0, 0), new DateTime(2000, 8, 18, 11, 11, 0, 0),
                BigDecimal.valueOf(200000), null, null));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(51),
                new DateTime(2007, 11, 30, 8, 5, 0, 0), new DateTime(2007, 11, 30, 10, 25, 0, 0),
                BigDecimal.valueOf(10000), "Уборка"));
        testSets.add(getNewService(BigInteger.ZERO, null, new DateTime(2009, 12, 12, 9, 30, 0, 0),
                new DateTime(2009, 12, 12, 17, 0, 0, 0), null, "Осмотр"));
        return testSets;
    }

    @Override
    protected List<TechnicalAssignment> getSearchTestSets() throws Exception {
        List<TechnicalAssignment> testSets = new ArrayList<>(9);
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(54),
                new DateTime(2015, 7, 15, 0, 45, 0, 0), new DateTime(2012, 1, 1, 1, 1, 0, 0),
                BigDecimal.valueOf(1234.56), BigInteger.valueOf(60), 100));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(55),
                new DateTime(2015, 7, 15, 0, 46, 0, 0), new DateTime(2013, 3, 1, 8, 20, 0, 0),
                BigDecimal.valueOf(3356.79), BigInteger.valueOf(61), BigDecimal.valueOf(600.25)));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(55), null,
                new DateTime(2013, 3, 1, 8, 21, 0, 0), BigDecimal.valueOf(10000), "Четыре пять шесть"));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(56),
                new DateTime(2015, 7, 23, 11, 11, 0, 0), new DateTime(2013, 3, 1, 9, 30, 0, 0),
                BigDecimal.valueOf(10500.33), BigInteger.valueOf(61), 756));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(57),
                new DateTime(2015, 9, 30, 14, 05, 0, 0), new DateTime(2013, 3, 1, 12, 25, 0, 0),
                BigDecimal.valueOf(11111.11), BigInteger.valueOf(65), BigDecimal.valueOf(700.75)));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(58),
                new DateTime(2015, 10, 11, 12, 10, 0, 0), new DateTime(2013, 3, 2, 8, 20, 0, 0),
                BigDecimal.valueOf(15000.39), "Четыре пять шесть"));
        testSets.add(getNewRefuelling(BigInteger.ZERO, BigInteger.valueOf(58),
                new DateTime(2015, 10, 11, 12, 11, 0, 0), new DateTime(2013, 3, 2, 8, 20, 0, 0),
                BigDecimal.valueOf(15000.40), BigInteger.valueOf(68), 1500));
        testSets.add(getNewRepairment(BigInteger.ZERO, BigInteger.valueOf(59),
                new DateTime(2015, 10, 11, 12, 12, 0, 0), new DateTime(2013, 3, 2, 10, 15, 0, 0),
                BigDecimal.valueOf(15000.41), BigInteger.valueOf(67), BigDecimal.valueOf(3333.33)));
        testSets.add(getNewService(BigInteger.ZERO, null, new DateTime(2015, 10, 11, 12, 15, 0, 0),
                new DateTime(2013, 3, 2, 10, 20, 0, 0), null, "Семь восемь девять десять"));
        return testSets;
    }

    @Override
    protected List<List<TechnicalAssignment>> performSearches() throws Exception {
        TechnicalAssignmentProvider provider = getFactory()
                .getProvider(TechnicalAssignmentProvider.class);
        List<List<TechnicalAssignment>> searchResult = new ArrayList<>(6);
        List<TechnicalAssignment> resultCollection;
        resultCollection = provider.findByMechanic(getMechanicById(BigInteger.valueOf(55)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyMechanic(getMechanicsByIds(Arrays.asList(BigInteger.valueOf(54),
                BigInteger.valueOf(56), BigInteger.valueOf(57), BigInteger.valueOf(58))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByBeginTimeRange(new DateTime(2015, 7, 15, 0, 45, 0, 0), true,
                new DateTime(2015, 10, 11, 12, 10, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByEndTimeRange(new DateTime(2013, 3, 1, 8, 20, 0, 0), false,
                new DateTime(2013, 3, 2, 10, 20, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByServiceCostRange(BigDecimal.valueOf(10000), true,
                BigDecimal.valueOf(15000.40), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        TechnicalAssignmentProvider provider = getFactory()
                .getProvider(TechnicalAssignmentProvider.class);
        List<List<BigInteger>> searchResult = new ArrayList<>(6);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByMechanic(getMechanicById(BigInteger.valueOf(55)));
        searchResult.add(resultCollection);
        resultCollection
                = provider.collectIdsByAnyMechanic(getMechanicsByIds(Arrays.asList(BigInteger.valueOf(54),
                                        BigInteger.valueOf(56), BigInteger.valueOf(57),
                                        BigInteger.valueOf(58))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByBeginTimeRange(new DateTime(2015, 7, 15, 0, 45, 0, 0), true,
                new DateTime(2015, 10, 11, 12, 10, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByEndTimeRange(new DateTime(2013, 3, 1, 8, 20, 0, 0), false,
                new DateTime(2013, 3, 2, 10, 20, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByServiceCostRange(BigDecimal.valueOf(10000), true,
                BigDecimal.valueOf(15000.40), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(6);
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{0, 3, 4, 5, 6});
        searchResult.add(new int[]{1, 3, 4, 5});
        searchResult.add(new int[]{1, 2, 3, 4, 5, 6, 7});
        searchResult.add(new int[]{3, 4, 5, 6});
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
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

    protected BusRepairment getNewRepairment(BigInteger id, BigInteger mechanicId, DateTime beginTime,
            DateTime endTime, BigDecimal cost, BigInteger busId, BigDecimal price) {
        return getNewRepairment(id, mechanicId, beginTime, endTime, cost, busId, price, false);
    }

    protected abstract BusRepairment getNewRepairment(BigInteger id, BigInteger mechanicId,
            DateTime beginTime, DateTime endTime, BigDecimal cost, BigInteger busId,
            BigDecimal price, boolean load);

    protected StationService getNewService(BigInteger id, BigInteger mechanicId, DateTime beginTime,
            DateTime endTime, BigDecimal cost, String description) {
        return getNewService(id, mechanicId, beginTime, endTime, cost, description, false);
    }

    protected abstract StationService getNewService(BigInteger id, BigInteger mechanicId, DateTime beginTime,
            DateTime endTime, BigDecimal cost, String description, boolean load);

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
    protected TechnicalAssignment nullifyCollections(TechnicalAssignment entity) {
        return entity;
    }
}
