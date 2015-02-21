package com.anli.busstation.dal.test.maintenance;

import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.maintenance.StationService;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.StationServiceProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class StationServiceTest extends BasicDataAccessTestSceleton<StationService> {

    protected Map<BigInteger, Mechanic> mechanics;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        Map<BigInteger, MechanicSkill> mechanicSkills = getFixtureCreator().createMechanicSkillFixture(50, 5);
        mechanics = getFixtureCreator().createMechanicFixture(50, 10, new ArrayList(mechanicSkills.values()));
    }

    @Override
    protected BigInteger createEntityByProvider(StationService sourceService) throws Exception {
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
    protected StationService getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(StationServiceProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, StationService sourceService) throws Exception {
        StationServiceProvider provider = getFactory().getProvider(StationServiceProvider.class);
        StationService service = provider.findById(id);
        service.setBeginTime(sourceService.getBeginTime());
        service.setDescription(sourceService.getDescription());
        service.setEndTime(sourceService.getEndTime());
        service.setMechanic(sourceService.getMechanic());
        service.setServiceCost(sourceService.getServiceCost());
        provider.save(service);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        StationServiceProvider provider = getFactory().getProvider(StationServiceProvider.class);
        StationService service = provider.findById(id);
        provider.remove(service);
    }

    @Override
    protected List<StationService> getCreationTestSets() throws Exception {
        List<StationService> testSets = new ArrayList<>(4);
        testSets.add(getNewService(BigInteger.ZERO, null, null, null, null, null));
        testSets.add(getNewService(BigInteger.ZERO, null, null, null, BigDecimal.ZERO, ""));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(51), new DateTime(1991, 1, 1, 15, 20, 0, 0),
                new DateTime(1991, 1, 1, 16, 30, 0, 0), BigDecimal.valueOf(5550.99), "Помывка"));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(50), new DateTime(1998, 1, 1, 14, 0, 0, 0),
                new DateTime(2001, 6, 5, 12, 15, 0, 0), BigDecimal.valueOf(70730.29), "Реставрация"));

        return testSets;
    }

    @Override
    protected List<StationService> getUpdateTestSets() throws Exception {
        List<StationService> testSets = new ArrayList<>(4);
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(51), new DateTime(2007, 11, 30, 8, 5, 0, 0),
                new DateTime(2007, 11, 30, 10, 25, 0, 0), BigDecimal.valueOf(10000), "Уборка"));
        testSets.add(getNewService(BigInteger.ZERO, null, new DateTime(2009, 12, 12, 9, 30, 0, 0),
                new DateTime(2009, 12, 12, 17, 0, 0, 0), null, "Осмотр"));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(55), new DateTime(2000, 7, 15, 7, 45, 0, 0),
                new DateTime(2000, 8, 18, 11, 11, 0, 0), BigDecimal.valueOf(200000), ""));
        testSets.add(getNewService(BigInteger.ZERO, null, null,
                null, null, null));

        return testSets;
    }

    @Override
    protected List<List<StationService>> performSearches() throws Exception {
        List<List<StationService>> searchResult = new ArrayList<>(8);
        StationServiceProvider provider = getFactory().getProvider(StationServiceProvider.class);
        List<StationService> resultCollection;
        List<BigInteger> mechanicList = Arrays.asList(BigInteger.valueOf(53), BigInteger.valueOf(55),
                BigInteger.valueOf(56));

        resultCollection = provider.findByMechanic(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMechanic(getMechanicById(BigInteger.valueOf(52)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyMechanic(getMechanicsByIds(mechanicList));
        searchResult.add(resultCollection);
        resultCollection = provider.findByBeginTimeRange(new DateTime(2014, 2, 15, 16, 0, 0, 0), true,
                new DateTime(2014, 4, 1, 12, 10, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByEndTimeRange(new DateTime(2014, 2, 20, 8, 40, 0, 0), false,
                new DateTime(2014, 11, 7, 9, 0, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByServiceCostRange(BigDecimal.valueOf(5000), true,
                BigDecimal.valueOf(25000), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDescriptionRegexp("[Оо]дин");
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList(8);
        StationServiceProvider provider = getFactory().getProvider(StationServiceProvider.class);
        List<BigInteger> resultCollection;
        List<BigInteger> mechanicList = Arrays.asList(BigInteger.valueOf(53), BigInteger.valueOf(55),
                BigInteger.valueOf(56));

        resultCollection = provider.collectIdsByMechanic(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMechanic(getMechanicById(BigInteger.valueOf(52)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyMechanic(getMechanicsByIds(mechanicList));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByBeginTimeRange(new DateTime(2014, 2, 15, 16, 0, 0, 0), true, new DateTime(2014, 4, 1, 12, 10, 0, 0), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByEndTimeRange(new DateTime(2014, 2, 20, 8, 40, 0, 0), false, new DateTime(2014, 11, 7, 9, 0, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByServiceCostRange(BigDecimal.valueOf(5000), true, BigDecimal.valueOf(25000), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDescriptionRegexp("[Оо]дин");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<StationService> getSearchTestSets() throws Exception {
        List<StationService> testSets = new ArrayList<>(5);
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(50), new DateTime(2014, 2, 15, 16, 0, 0, 0),
                new DateTime(2014, 2, 15, 20, 15, 0, 0), null, "Один два три"));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(51), new DateTime(2014, 2, 19, 8, 40, 0, 0),
                new DateTime(2014, 2, 20, 8, 40, 0, 0), BigDecimal.valueOf(5000), ""));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(52), new DateTime(2014, 4, 1, 12, 10, 0, 0),
                null, BigDecimal.valueOf(5500), null));
        testSets.add(getNewService(BigInteger.ZERO, BigInteger.valueOf(53), null,
                new DateTime(2014, 7, 27, 21, 50, 0, 0), BigDecimal.valueOf(25000), "Четыре пять шесть"));
        testSets.add(getNewService(BigInteger.ZERO, null, new DateTime(2014, 11, 7, 8, 0, 0, 0),
                new DateTime(2014, 11, 7, 9, 0, 0, 0), BigDecimal.valueOf(100000), "Семь восемь девять десять"));

        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList(8);
        searchResult.add(new int[]{4});
        searchResult.add(new int[]{2});
        searchResult.add(new int[]{3});
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{1, 3});
        searchResult.add(new int[]{2, 3});
        searchResult.add(new int[]{0});
        searchResult.add(new int[]{0, 1, 2, 3, 4});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

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

    @Override
    protected StationService nullifyCollections(StationService entity) {
        return entity;
    }
}
