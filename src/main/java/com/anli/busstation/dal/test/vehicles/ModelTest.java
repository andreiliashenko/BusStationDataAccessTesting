package com.anli.busstation.dal.test.vehicles;

import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.providers.vehicles.GasLabelProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.ModelProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class ModelTest extends BasicDataAccessTestSceleton<Model> {

    protected Map<BigInteger, GasLabel> gasLabels;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        gasLabels = getFixtureCreator().createGasLabelFixture(10, 5);
    }

    @Override
    protected BigInteger createEntityByProvider(Model sourceModel) throws Exception {
        ModelProvider provider = getFactory().getProvider(ModelProvider.class);
        Model model = provider.create();
        model.setGasLabel(sourceModel.getGasLabel());
        model.setGasRate(sourceModel.getGasRate());
        model.setName(sourceModel.getName());
        model.setSeatsNumber(sourceModel.getSeatsNumber());
        model.setTankVolume(sourceModel.getTankVolume());

        provider.save(model);

        return model.getId();
    }

    @Override
    protected Model getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(ModelProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Model sourceModel) throws Exception {
        ModelProvider provider = getFactory().getProvider(ModelProvider.class);
        Model model = provider.findById(id);
        model.setGasLabel(sourceModel.getGasLabel());
        model.setGasRate(sourceModel.getGasRate());
        model.setName(sourceModel.getName());
        model.setSeatsNumber(sourceModel.getSeatsNumber());
        model.setTankVolume(sourceModel.getTankVolume());
        provider.save(model);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        ModelProvider provider = getFactory().getProvider(ModelProvider.class);
        Model model = provider.findById(id);
        provider.remove(model);
    }

    @Override
    protected List<Model> getCreationTestSets() throws Exception {
        List<Model> testSets = new ArrayList<>(3);

        testSets.add(getNewModel(BigInteger.ZERO, null, null, null, null, null));
        testSets.add(getNewModel(BigInteger.ZERO, null, BigDecimal.ZERO, "", 0, 0));
        testSets.add(getNewModel(BigInteger.ZERO, BigInteger.valueOf(11), BigDecimal.valueOf(45.89), "Model 1", 22, 150));

        return testSets;
    }

    @Override
    protected List<Model> getUpdateTestSets() {
        List<Model> testSets = new ArrayList<>(4);

        testSets.add(getNewModel(BigInteger.ZERO, BigInteger.valueOf(13), BigDecimal.valueOf(10.1), "Model 3", 79, 100));
        testSets.add(getNewModel(BigInteger.ZERO, null, new BigDecimal("56.146"), "Model 4", null, 156));
        testSets.add(getNewModel(BigInteger.ZERO, BigInteger.valueOf(13), null, null, null, null));

        return testSets;
    }

    @Override
    protected List<List<Model>> performSearches() throws Exception {
        List<List<Model>> searchResult = new ArrayList<>(9);

        ModelProvider provider = getFactory().getProvider(ModelProvider.class);
        List<Model> resultCollection;

        resultCollection = provider.findByGasLabel(getLabelById(BigInteger.valueOf(14)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByGasLabel(null);
        searchResult.add(resultCollection);

        resultCollection = provider.findByAnyGasLabel(Arrays.asList(getLabelById(BigInteger.valueOf(12)),
                getLabelById(BigInteger.valueOf(13))));
        searchResult.add(resultCollection);

        resultCollection = provider.findByGasRateRange(BigDecimal.valueOf(10.66), true,
                BigDecimal.valueOf(12.5), false);
        searchResult.add(resultCollection);

        resultCollection = provider.findByName("Model 10");
        searchResult.add(resultCollection);

        resultCollection = provider.findByNameRegexp("[0-9]{2}$");
        searchResult.add(resultCollection);

        resultCollection = provider.findBySeatsNumberRange(30, false, 37, true);
        searchResult.add(resultCollection);

        resultCollection = provider.findByTankVolumeRange(100, true, 125, false);
        searchResult.add(resultCollection);

        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(9);

        ModelProvider provider = getFactory().getProvider(ModelProvider.class);
        List<BigInteger> resultCollection;

        resultCollection = provider.collectIdsByGasLabel(getLabelById(BigInteger.valueOf(14)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByGasLabel(null);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByAnyGasLabel(Arrays.asList(getLabelById(BigInteger.valueOf(12)),
                getLabelById(BigInteger.valueOf(13))));
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByGasRateRange(BigDecimal.valueOf(10.66), true,
                BigDecimal.valueOf(12.5), false);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByName("Model 10");
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByNameRegexp("[0-9]{2}$");
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsBySeatsNumberRange(30, false, 37, true);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByTankVolumeRange(100, true, 125, false);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<Model> getSearchTestSets() {
        List<Model> testSets = new ArrayList<>(6);

        testSets.add(getNewModel(BigInteger.ZERO, BigInteger.valueOf(12), BigDecimal.valueOf(10), "Model 10", 29, 89));
        testSets.add(getNewModel(BigInteger.ZERO, BigInteger.valueOf(13), BigDecimal.valueOf(10.66), "Model 12", 30, 94));
        testSets.add(getNewModel(BigInteger.ZERO, BigInteger.valueOf(14), BigDecimal.valueOf(11.1), "Model 14", 33, 100));
        testSets.add(getNewModel(BigInteger.ZERO, BigInteger.valueOf(14), BigDecimal.valueOf(12.5), "Model", 36, 125));
        testSets.add(getNewModel(BigInteger.ZERO, null, BigDecimal.valueOf(13.44), "ModelLast", 37, 125));
        testSets.add(getNewModel(BigInteger.ZERO, null, BigDecimal.valueOf(13.50), null, 38, 130));

        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(9);
        searchResult.add(new int[]{2, 3});
        searchResult.add(new int[]{4, 5});
        searchResult.add(new int[]{0, 1});
        searchResult.add(new int[]{2, 3});
        searchResult.add(new int[]{0});
        searchResult.add(new int[]{0, 1, 2});
        searchResult.add(new int[]{1, 2, 3});
        searchResult.add(new int[]{3, 4});
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Model getNewModel(BigInteger id, BigInteger gasLabelId, BigDecimal gasRate,
            String name, Integer seatsNumber, Integer tankVolume) {
        return getNewModel(id, gasLabelId, gasRate, name, seatsNumber, tankVolume, false);
    }

    protected abstract Model getNewModel(BigInteger id, BigInteger gasLabelId, BigDecimal gasRate,
            String name, Integer seatsNumber, Integer tankVolume, boolean load);

    protected GasLabel getLabelById(BigInteger id) {
        return getLabelById(id, false);
    }

    protected GasLabel getLabelById(BigInteger id, boolean load) {
        return load ? getFactory().getProvider(GasLabelProvider.class).findById(id) : gasLabels.get(id);
    }

    @Override
    protected Model nullifyCollections(Model entity) {
        return entity;
    }
}
