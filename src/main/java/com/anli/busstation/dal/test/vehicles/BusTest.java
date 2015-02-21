package com.anli.busstation.dal.test.vehicles;

import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.ModelProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.TechnicalStateProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class BusTest extends BasicDataAccessTestSceleton<Bus> {

    protected Map<BigInteger, GasLabel> gasLabels;
    protected Map<BigInteger, Model> models;
    protected Map<BigInteger, TechnicalState> technicalStates;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        gasLabels = getFixtureCreator().createGasLabelFixture(10, 5);
        models = getFixtureCreator().createModelFixture(20, 5, new ArrayList(gasLabels.values()));
        technicalStates = getFixtureCreator().createTechnicalStateFixture(30, 5);
    }

    @Override
    protected BigInteger createEntityByProvider(Bus sourceBus) throws Exception {
        BusProvider provider = getFactory().getProvider(BusProvider.class);
        Bus bus = provider.create();
        bus.setModel(sourceBus.getModel());
        bus.setPlate(sourceBus.getPlate());
        bus.setState(sourceBus.getState());
        provider.save(bus);
        return bus.getId();
    }

    @Override
    protected Bus getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(BusProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Bus sourceBus) throws Exception {
        BusProvider provider = getFactory().getProvider(BusProvider.class);
        Bus bus = provider.findById(id);
        bus.setModel(sourceBus.getModel());
        bus.setPlate(sourceBus.getPlate());
        bus.setState(sourceBus.getState());
        provider.save(bus);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        BusProvider provider = getFactory().getProvider(BusProvider.class);
        Bus bus = provider.findById(id);
        provider.remove(bus);
    }

    @Override
    protected List<Bus> getCreationTestSets() throws Exception {
        List<Bus> testSets = new ArrayList<>(5);

        testSets.add(getNewBus(BigInteger.ZERO, null, null, null));
        testSets.add(getNewBus(BigInteger.ZERO, null, "", null));
        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(21), "а578да", null));
        testSets.add(getNewBus(BigInteger.ZERO, null, "с879ша", BigInteger.valueOf(32)));
        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(23), null, BigInteger.valueOf(34)));

        return testSets;
    }

    @Override
    protected List<Bus> getUpdateTestSets() throws Exception {
        List<Bus> testSets = new ArrayList<>(5);

        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(24), "е908уу", BigInteger.valueOf(30)));
        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(20), "а000вв", null));
        testSets.add(getNewBus(BigInteger.ZERO, null, "", null));
        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(20), null, BigInteger.valueOf(33)));
        testSets.add(getNewBus(BigInteger.ZERO, null, "", null));

        return testSets;
    }

    @Override
    protected List<List<Bus>> performSearches() throws Exception {
        List<List<Bus>> searchResult = new ArrayList<>(9);

        BusProvider provider = getFactory().getProvider(BusProvider.class);
        List<Bus> resultCollection;

        resultCollection = provider.findByModel(getModelById(BigInteger.valueOf(22)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByModel(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyModel(Arrays.asList(getModelById(BigInteger.valueOf(20)),
                getModelById(BigInteger.valueOf(23)), getModelById(BigInteger.valueOf(24))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByState(getStateById(BigInteger.valueOf(30)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByState(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnyState(Arrays.asList(getStateById(BigInteger.valueOf(33)),
                getStateById(BigInteger.valueOf(32))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByPlate("п109ец");
        searchResult.add(resultCollection);
        resultCollection = provider.findByPlate("");
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(9);

        BusProvider provider = getFactory().getProvider(BusProvider.class);
        List<BigInteger> resultCollection;

        resultCollection = provider.collectIdsByModel(getModelById(BigInteger.valueOf(22)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByModel(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyModel(Arrays.asList(getModelById(BigInteger.valueOf(20)),
                getModelById(BigInteger.valueOf(23)), getModelById(BigInteger.valueOf(24))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByState(getStateById(BigInteger.valueOf(30)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByState(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnyState(Arrays.asList(getStateById(BigInteger.valueOf(33)), getStateById(BigInteger.valueOf(32))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPlate("п109ец");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPlate("");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<Bus> getSearchTestSets() throws Exception {
        List<Bus> testSets = new ArrayList<>(5);
        testSets.add(getNewBus(BigInteger.ZERO, null, null, BigInteger.valueOf(30)));
        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(20), "", BigInteger.valueOf(32)));
        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(21), "п109ец", BigInteger.valueOf(33)));
        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(22), "п108ец", BigInteger.valueOf(34)));
        testSets.add(getNewBus(BigInteger.ZERO, BigInteger.valueOf(23), "fjl", BigInteger.valueOf(34)));
        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(9);
        searchResult.add(new int[]{3});
        searchResult.add(new int[]{0});
        searchResult.add(new int[]{1, 4});
        searchResult.add(new int[]{0});
        searchResult.add(new int[]{});
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{2});
        searchResult.add(new int[]{1});
        searchResult.add(new int[]{0, 1, 2, 3, 4});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Bus getNewBus(BigInteger id, BigInteger modelId, String plate, BigInteger stateId) {
        return getNewBus(id, modelId, plate, stateId, false);
    }

    protected abstract Bus getNewBus(BigInteger id, BigInteger modelId, String plate,
            BigInteger stateId, boolean load);

    protected Model getModelById(BigInteger id) {
        return getModelById(id, false);
    }

    protected Model getModelById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(ModelProvider.class).findById(id) : models.get(id);
    }

    protected TechnicalState getStateById(BigInteger id) {
        return getStateById(id, false);
    }

    protected TechnicalState getStateById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(TechnicalStateProvider.class).findById(id) : technicalStates.get(id);
    }

    @Override
    protected Bus nullifyCollections(Bus entity) {
        return entity;
    }
}
