package com.anli.busstation.dal.test.vehicles;

import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.providers.vehicles.GasLabelProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class GasLabelTest extends BasicDataAccessTestSceleton<GasLabel> {

    @Override
    protected BigInteger createEntityByProvider(GasLabel sourceLabel) throws Exception {
        GasLabelProvider provider = getFactory().getProvider(GasLabelProvider.class);
        GasLabel gasLabel = provider.create();
        gasLabel.setName(sourceLabel.getName());
        gasLabel.setPrice(sourceLabel.getPrice());
        provider.save(gasLabel);

        return gasLabel.getId();
    }

    @Override
    protected GasLabel getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(GasLabelProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, GasLabel sourceLabel) throws Exception {
        GasLabelProvider provider = getFactory().getProvider(GasLabelProvider.class);
        GasLabel gasLabel = provider.findById(id);
        gasLabel.setName(sourceLabel.getName());
        gasLabel.setPrice(sourceLabel.getPrice());
        provider.save(gasLabel);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        GasLabel gasLabel;
        GasLabelProvider provider = getFactory().getProvider(GasLabelProvider.class);
        gasLabel = provider.findById(id);
        provider.remove(gasLabel);
    }

    @Override
    protected List<GasLabel> getCreationTestSets() {
        List<GasLabel> testSets = new ArrayList<>(4);

        testSets.add(getNewGasLabel(BigInteger.ZERO, null, null));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "", BigDecimal.ZERO));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "Created Name 1", BigDecimal.valueOf(25.26)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "Created Name 2", new BigDecimal("34.33")));
        return testSets;
    }

    @Override
    protected List<GasLabel> getUpdateTestSets() {
        List<GasLabel> testSets = new ArrayList<>(4);

        testSets.add(getNewGasLabel(BigInteger.ZERO, "Updated Name 1", BigDecimal.valueOf(1.12)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "Updated Name 2", null));
        testSets.add(getNewGasLabel(BigInteger.ZERO, null, new BigDecimal("25.27")));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "", BigDecimal.ZERO));

        return testSets;
    }

    @Override
    protected List<List<GasLabel>> performSearches() throws Exception {
        List<List<GasLabel>> searchResult = new ArrayList(17);

        GasLabelProvider provider = getFactory().getProvider(GasLabelProvider.class);
        List<GasLabel> resultCollection;

        resultCollection = provider.findByName(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("");
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("N1");
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("N2");
        searchResult.add(resultCollection);
        resultCollection = provider.findByNameRegexp("^N[0-9]$");
        searchResult.add(resultCollection);

        resultCollection = provider.findByPriceRange(null, true, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(25), false,
                BigDecimal.valueOf(25.00), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(24.99), true,
                new BigDecimal("24.99"), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(23.25), false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(23.25), true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(null, true, BigDecimal.valueOf(27.15), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(null, false, new BigDecimal("27.15"), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(23), true,
                BigDecimal.valueOf(27.00), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(23.00), true,
                BigDecimal.valueOf(27), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(23.00), false,
                BigDecimal.valueOf(27.00), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByPriceRange(BigDecimal.valueOf(23), false,
                BigDecimal.valueOf(27), false);
        searchResult.add(resultCollection);

        resultCollection = provider.findAll();
        searchResult.add(resultCollection);

        return searchResult;
    }

    @Override
    protected List<GasLabel> getSearchTestSets() {
        List<GasLabel> testSets = new ArrayList<>(12);

        testSets.add(getNewGasLabel(BigInteger.ZERO, null, null));
        testSets.add(getNewGasLabel(BigInteger.ZERO, null, BigDecimal.valueOf(20)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "", BigDecimal.valueOf(23)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "", BigDecimal.valueOf(23.25)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "N1", BigDecimal.valueOf(24.11)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "N1", BigDecimal.valueOf(24.99)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "N3", BigDecimal.valueOf(25)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "N7", BigDecimal.valueOf(26.66)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "NN8", BigDecimal.valueOf(27)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "NN99", BigDecimal.valueOf(27.15)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "NN91", BigDecimal.valueOf(27.16)));
        testSets.add(getNewGasLabel(BigInteger.ZERO, "NN92", BigDecimal.valueOf(28.3)));

        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(17);
        searchResult.add(new int[]{0, 1});
        searchResult.add(new int[]{2, 3});
        searchResult.add(new int[]{4, 5});
        searchResult.add(new int[]{});
        searchResult.add(new int[]{4, 5, 6, 7});
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
        searchResult.add(new int[]{6});
        searchResult.add(new int[]{});
        searchResult.add(new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11});
        searchResult.add(new int[]{4, 5, 6, 7, 8, 9, 10, 11});
        searchResult.add(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        searchResult.add(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        searchResult.add(new int[]{3, 4, 5, 6, 7});
        searchResult.add(new int[]{3, 4, 5, 6, 7, 8});
        searchResult.add(new int[]{2, 3, 4, 5, 6, 7});
        searchResult.add(new int[]{2, 3, 4, 5, 6, 7, 8});
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(17);

        GasLabelProvider provider = getFactory().getProvider(GasLabelProvider.class);
        List<BigInteger> resultCollection;

        resultCollection = provider.collectIdsByName(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("N1");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("N2");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByNameRegexp("^N[0-9]$");
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByPriceRange(null, true, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(25), false,
                BigDecimal.valueOf(25.00), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(24.99), true,
                new BigDecimal("24.99"), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(23.25), false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(23.25), true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(null, true, BigDecimal.valueOf(27.15), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(null, false, new BigDecimal("27.15"), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(23), true,
                BigDecimal.valueOf(27.00), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(23.00), true,
                BigDecimal.valueOf(27), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(23.00), false,
                BigDecimal.valueOf(27.00), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByPriceRange(BigDecimal.valueOf(23), false,
                BigDecimal.valueOf(27), false);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);

        return searchResult;
    }

    protected abstract GasLabel getNewGasLabel(BigInteger newId, String name, BigDecimal price);

    @Override
    protected GasLabel nullifyCollections(GasLabel entity) {
        return entity;
    }
}
