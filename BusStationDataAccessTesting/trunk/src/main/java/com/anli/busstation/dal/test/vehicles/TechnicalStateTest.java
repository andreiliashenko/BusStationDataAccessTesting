package com.anli.busstation.dal.test.vehicles;

import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.vehicles.TechnicalStateProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class TechnicalStateTest extends BasicDataAccessTestSceleton<TechnicalState> {

    @Override
    protected BigInteger createEntityByProvider(TechnicalState sourceState) throws Exception {
        TechnicalStateProvider provider = getFactory().getProvider(TechnicalStateProvider.class);
        TechnicalState state = provider.create();

        state.setDescription(sourceState.getDescription());
        state.setDifficultyLevel(sourceState.getDifficultyLevel());
        provider.save(state);
        return state.getId();
    }

    @Override
    protected TechnicalState getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(TechnicalStateProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, TechnicalState sourceState) throws Exception {
        TechnicalStateProvider provider = getFactory().getProvider(TechnicalStateProvider.class);
        TechnicalState state = provider.findById(id);
        state.setDescription(sourceState.getDescription());
        state.setDifficultyLevel(sourceState.getDifficultyLevel());
        provider.save(state);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        TechnicalState state;
        TechnicalStateProvider provider = getFactory().getProvider(TechnicalStateProvider.class);
        state = provider.findById(id);
        provider.remove(state);
    }

    @Override
    protected List<TechnicalState> getCreationTestSets() {
        List<TechnicalState> testSets = new ArrayList<>(4);

        testSets.add(getNewState(BigInteger.ZERO, null, null));
        testSets.add(getNewState(BigInteger.ZERO, "", 0));
        testSets.add(getNewState(BigInteger.ZERO, "Created Description 1", 5));
        testSets.add(getNewState(BigInteger.ZERO, "Created Description 2", 2));

        return testSets;
    }

    @Override
    protected List<TechnicalState> getUpdateTestSets() {
        List<TechnicalState> testSets = new ArrayList<>(4);

        testSets.add(getNewState(BigInteger.ZERO, "Updated Description 1", 4));
        testSets.add(getNewState(BigInteger.ZERO, "Updated Description 2", null));
        testSets.add(getNewState(BigInteger.ZERO, null, 11));
        testSets.add(getNewState(BigInteger.ZERO, "", 0));

        return testSets;
    }

    @Override
    protected List<TechnicalState> getSearchTestSets() {
        List<TechnicalState> testSets = new ArrayList<>(8);

        testSets.add(getNewState(BigInteger.ZERO, null, null));
        testSets.add(getNewState(BigInteger.ZERO, null, 3));
        testSets.add(getNewState(BigInteger.ZERO, "", 4));
        testSets.add(getNewState(BigInteger.ZERO, "", 5));
        testSets.add(getNewState(BigInteger.ZERO, "DES1", 6));
        testSets.add(getNewState(BigInteger.ZERO, "DES2", 7));
        testSets.add(getNewState(BigInteger.ZERO, "DES3", 8));
        testSets.add(getNewState(BigInteger.ZERO, "DES5", 9));

        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(15);
        searchResult.add(new int[]{4, 5, 6});
        searchResult.add(new int[]{0});
        searchResult.add(new int[]{3});
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        searchResult.add(new int[]{3});
        searchResult.add(new int[]{});
        searchResult.add(new int[]{1, 2, 3, 4, 5, 6, 7});
        searchResult.add(new int[]{2, 3, 4, 5, 6, 7});
        searchResult.add(new int[]{1, 2, 3, 4});
        searchResult.add(new int[]{1, 2, 3});
        searchResult.add(new int[]{3, 4});
        searchResult.add(new int[]{3, 4, 5});
        searchResult.add(new int[]{2, 3, 4});
        searchResult.add(new int[]{2, 3, 4, 5});
        searchResult.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7});

        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    @Override
    protected List<List<TechnicalState>> performSearches() throws Exception {
        List<List<TechnicalState>> searchResult = new ArrayList(15);

        TechnicalStateProvider provider = getFactory().getProvider(TechnicalStateProvider.class);
        List<TechnicalState> resultCollection;

        resultCollection = provider.findByDescriptionRegexp("^DE\\S[0-4]");
        searchResult.add(resultCollection);

        resultCollection = provider.findByDifficultyLevel(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevel(5);
        searchResult.add(resultCollection);

        resultCollection = provider.findByDifficultyLevelRange(null, true, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(5, false, 5, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(4, true, 4, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(3, false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(3, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(null, true, 6, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(null, false, 6, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(4, true, 7, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(4, true, 7, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(4, false, 7, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByDifficultyLevelRange(4, false, 7, false);
        searchResult.add(resultCollection);

        resultCollection = provider.findAll();
        searchResult.add(resultCollection);

        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(15);

        TechnicalStateProvider provider = getFactory().getProvider(TechnicalStateProvider.class);
        List<BigInteger> resultCollection;

        resultCollection = provider.collectIdsByDescriptionRegexp("^DE\\S[0-4]");
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByDifficultyLevel(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevel(5);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByDifficultyLevelRange(null, true, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(5, false, 5, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(4, true, 4, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(3, false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(3, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(null, true, 6, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(null, false, 6, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(4, true, 7, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(4, true, 7, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(4, false, 7, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByDifficultyLevelRange(4, false, 7, false);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);

        return searchResult;
    }

    protected abstract TechnicalState getNewState(BigInteger id, String description, Integer diffLevel);

    @Override
    protected TechnicalState nullifyCollections(TechnicalState entity) {
        return entity;
    }
}
