package com.anli.busstation.dal.test.staff;

import com.anli.busstation.dal.interfaces.providers.staff.DriverSkillProvider;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.factories.ProviderFactory;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class DriverSkillTest extends BasicDataAccessTestSceleton<DriverSkill> {

    @Override
    protected BigInteger createEntityByProvider(DriverSkill sourceSkill) throws Exception {
        ProviderFactory providerFactory = getFactory();
        DriverSkillProvider driverSkillProvider = providerFactory.getProvider(DriverSkillProvider.class);
        DriverSkill driverSkill = driverSkillProvider.create();
        driverSkill.setMaxPassengers(sourceSkill.getMaxPassengers());
        driverSkill.setMaxRideLength(sourceSkill.getMaxRideLength());
        driverSkill.setName(sourceSkill.getName());
        driverSkillProvider.save(driverSkill);
        return driverSkill.getId();
    }

    @Override
    protected DriverSkill getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(DriverSkillProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, DriverSkill sourceSkill) throws Exception {
        DriverSkill driverSkill;
        DriverSkillProvider provider = getFactory().getProvider(DriverSkillProvider.class);
        driverSkill = provider.findById(id);

        driverSkill.setMaxPassengers(sourceSkill.getMaxPassengers());
        driverSkill.setMaxRideLength(sourceSkill.getMaxRideLength());
        driverSkill.setName(sourceSkill.getName());
        provider.save(driverSkill);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        DriverSkill driverSkill;
        DriverSkillProvider provider = getFactory().getProvider(DriverSkillProvider.class);
        driverSkill = provider.findById(id);
        provider.remove(driverSkill);
    }

    @Override
    protected List<DriverSkill> getCreationTestSets() {
        List<DriverSkill> testSets = new ArrayList<>(7);

        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Test name 1", 11, 1000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "", 22, 2000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, 33, 3000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, 0, 4000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, null, 5000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, null, 0));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, null, null));
        return testSets;
    }

    @Override
    protected List<DriverSkill> getUpdateTestSets() {
        List<DriverSkill> testSets = new ArrayList<>(7);

        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, 11, 1000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Updated name 2", 22, 2000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Updated name 3", null, 0));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, 23, null));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "", 0, 4000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, null, 5000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Updated name 7", 66, 6532));

        return testSets;
    }

    @Override
    protected List<List<DriverSkill>> performSearches() throws Exception {
        List<List<DriverSkill>> searchResult = new ArrayList<>(27);

        DriverSkillProvider provider = getFactory().getProvider(DriverSkillProvider.class);
        List<DriverSkill> resultCollection;
        resultCollection = provider.findByName(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("");
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("Name to be found");
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("Name not to be found");
        searchResult.add(resultCollection);

        resultCollection = provider.findByMaxPassengersRange(null, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(75, false, 75, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(80, true, 80, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(50, false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(60, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(null, true, 80, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(null, false, 70, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(40, true, 70, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(40, true, 70, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(40, false, 60, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxPassengersRange(40, false, 70, false);
        searchResult.add(resultCollection);

        resultCollection = provider.findByMaxRideLengthRange(null, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(1000, false, 1000, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(1100, true, 1100, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(900, false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(1200, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(null, true, 2000, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(null, false, 1900, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(900, true, 1400, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(1000, true, 1800, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(900, false, 1500, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxRideLengthRange(700, false, 1300, false);
        searchResult.add(resultCollection);

        resultCollection = provider.findAll();
        searchResult.add(resultCollection);

        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(27);

        searchResult.add(new int[]{0, 1, 2});
        searchResult.add(new int[]{3, 4});
        searchResult.add(new int[]{5, 6});
        searchResult.add(new int[]{});
        
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
        searchResult.add(new int[]{10});
        searchResult.add(new int[]{});
        searchResult.add(new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
        searchResult.add(new int[]{8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
        searchResult.add(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
        searchResult.add(new int[]{2, 3, 4, 5, 6, 7, 8});
        searchResult.add(new int[]{3, 4, 5, 6, 7, 8});
        searchResult.add(new int[]{3, 4, 5, 6, 7, 8, 9});
        searchResult.add(new int[]{2, 3, 4, 5, 6});
        searchResult.add(new int[]{2, 3, 4, 5, 6, 7, 8, 9});

        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
        searchResult.add(new int[]{5});
        searchResult.add(new int[]{});
        searchResult.add(new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
        searchResult.add(new int[]{11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
        searchResult.add(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});
        searchResult.add(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});
        searchResult.add(new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12});
        searchResult.add(new int[]{6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});
        searchResult.add(new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14});
        searchResult.add(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});;

        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});

        return searchResult;
    }

    @Override
    protected  List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    @Override
    protected List<DriverSkill> getSearchTestSets() {
        List<DriverSkill> testSets = new ArrayList<>(21);

        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, null, null));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, null, 700));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, null, 40, 844));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "", 41, 900));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "", 48, 950));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Name to be found", 50, 1000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Name to be found", 55, 1088));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 1", 60, 1099));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 2", 63, 1100));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 3", 70, 1167));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 4", 75, 1200));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 5", 80, 1300));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 6", 84, 1350));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 7", 86, 1400));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 8", 87, 1421));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 9", 88, 1500));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 10", 89, 1700));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 11", 90, 1800));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 12", 95, 1900));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 13", 96, 2000));
        testSets.add(getNewDriverSkill(BigInteger.ZERO, "Another name 14", 97, 2200));

        return testSets;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(27);

        DriverSkillProvider provider = getFactory().getProvider(DriverSkillProvider.class);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsByName(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("Name to be found");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("Name not to be found");
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByMaxPassengersRange(null, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(75, false, 75, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(80, true, 80, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(50, false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(60, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(null, true, 80, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(null, false, 70, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(40, true, 70, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(40, true, 70, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(40, false, 60, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxPassengersRange(40, false, 70, false);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByMaxRideLengthRange(null, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(1000, false, 1000, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(1100, true, 1100, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(900, false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(1200, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(null, true, 2000, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(null, false, 1900, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(900, true, 1400, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(1000, true, 1800, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(900, false, 1500, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxRideLengthRange(700, false, 1300, false);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);

        return searchResult;
    }

    protected abstract DriverSkill getNewDriverSkill(BigInteger newId, String name, Integer maxPass, Integer maxRideLength);

    @Override
    protected DriverSkill nullifyCollections(DriverSkill entity) {
        return entity;
    }
}
