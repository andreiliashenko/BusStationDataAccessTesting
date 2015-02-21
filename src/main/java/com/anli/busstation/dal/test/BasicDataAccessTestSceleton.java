package com.anli.busstation.dal.test;

import com.anli.busstation.dal.interfaces.entities.BSEntity;
import com.anli.busstation.dal.interfaces.factories.ProviderFactory;
import com.anli.integrationtesting.execution.TestExecutor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class BasicDataAccessTestSceleton<I extends BSEntity> implements TestExecutor {

    protected static volatile long currentId = 1001L;

    protected static synchronized BigInteger generateId() {
        BigInteger id = BigInteger.valueOf(currentId);
        currentId++;
        return id;
    }

    @Override
    public void test() throws Exception {
        clearStorageSpace();
        createPrerequisites();
        testCreation();
        testReading();
        testUpdate();
        testDeletion();
        clearStorageSpace();
        createPrerequisites();
        testPulling();
        clearStorageSpace();
        createPrerequisites();
        testCollecting();
        clearStorageSpace();
        createPrerequisites();
        testSearch();
    }

    protected abstract ProviderFactory getFactory();

    protected abstract BigInteger createEntityByProvider(I entity) throws Exception;

    protected abstract I getEntityByProvider(BigInteger id) throws Exception;

    protected abstract void updateEntityByProvider(BigInteger id, I data) throws Exception;

    protected abstract void deleteEntityByProvider(BigInteger id) throws Exception;

    protected abstract List<List<I>> performSearches() throws Exception;

    protected abstract List<List<BigInteger>> performCollectings() throws Exception;

    protected abstract BigInteger createEntityManually(I entity) throws Exception;

    protected abstract I getEntityManually(BigInteger id) throws Exception;

    protected abstract List<I> getCreationTestSets() throws Exception;

    protected abstract List<I> getUpdateTestSets() throws Exception;

    protected abstract List<I> getSearchTestSets() throws Exception;

    protected abstract List<int[]> getExpectedSearchSets();

    protected abstract List<int[]> getExpectedCollectingSets();

    protected abstract void setEntityId(I entity, BigInteger id);

    protected abstract FixtureCreator getFixtureCreator();

    protected abstract void clearStorageSpace() throws Exception;

    protected List<I> getPullEtalons() throws Exception {
        return Collections.emptyList();
    }

    protected I pull(I entity, int index) {
        return entity;
    }

    protected boolean hasCollections() {
        return false;
    }

    protected void createPrerequisites() throws Exception {
    }

    protected void testCreation() throws Exception {
        List<I> testEntities = getCreationTestSets();
        for (I testEntity : testEntities) {
            BigInteger id = createEntityByProvider(testEntity);
            setEntityId(testEntity, id);
            I actualEntity = getEntityManually(id);
            assertEntity(testEntity, actualEntity);
        }
    }

    protected void testReading() throws Exception {
        List<I> testEntities = getCreationTestSets();

        for (I testEntity : testEntities) {
            BigInteger id = createEntityManually(testEntity);
            setEntityId(testEntity, id);
            nullifyCollections(testEntity);
            I actualEntity = getEntityByProvider(id);
            assertEntity(testEntity, actualEntity);
        }
    }

    protected void testPulling() throws Exception {
        if (!hasCollections()) {
            return;
        }
        List<I> testEntities = getCreationTestSets();
        int index = 0;
        List<I> pullEtalons = getPullEtalons();
        Iterator<I> pullIter = pullEtalons.iterator();
        for (I testEntity : testEntities) {
            BigInteger id = createEntityManually(testEntity);
            I pullEtalon = pullIter.next();
            setEntityId(pullEtalon, id);
            I pulled = pull(getEntityByProvider(id), index);
            assertEntity(pullEtalon, pulled);
            index++;
        }
    }

    protected void testUpdate() throws Exception {
        List<I> creationEntities = getCreationTestSets();
        List<BigInteger> createdIds = new ArrayList<>(creationEntities.size());
        for (I creationEntity : creationEntities) {
            createdIds.add(createEntityManually(creationEntity));
        }
        List<I> testEntities = getUpdateTestSets();
        Iterator<I> testEntityIter = testEntities.iterator();
        Iterator<BigInteger> idIter = createdIds.iterator();
        while (testEntityIter.hasNext()) {
            BigInteger id = idIter.next();
            I testEntity = testEntityIter.next();
            setEntityId(testEntity, id);
            updateEntityByProvider(id, testEntity);
            I actualEntity = getEntityManually(id);
            assertEntity(testEntity, actualEntity);
        }
    }

    protected void testDeletion() throws Exception {
        List<I> creationEntities = getCreationTestSets();
        List<BigInteger> createdIds = new ArrayList<>(creationEntities.size());
        for (I creationEntity : creationEntities) {
            createdIds.add(createEntityManually(creationEntity));
        }
        for (BigInteger id : createdIds) {
            deleteEntityByProvider(id);
            BSEntity actualEntity = getEntityManually(id);
            if (actualEntity != null) {
                throw new AssertionError("Expected null Actual " + actualEntity);
            }
        }
    }

    protected void testSearch() throws Exception {
        List<I> searchEntities = getSearchTestSets();

        List<I> createdEntities = new ArrayList<>(searchEntities.size());

        for (I searchEntity : searchEntities) {
            createdEntities.add(getEntityManually(createEntityManually(searchEntity)));
        }

        List<List<I>> rawExpected = getEntitiesByIndices(getExpectedSearchSets(),
                createdEntities);
        List<List<I>> expected = new ArrayList<>(rawExpected.size());
        for (List<I> set : rawExpected) {
            expected.add(nullifyCollections(set));
        }
        List<List<I>> actual = performSearches();
        Iterator<List<I>> expectedIter = expected.iterator();
        Iterator<List<I>> actualIter = actual.iterator();
        int setNumber = 0;
        while (expectedIter.hasNext() && actualIter.hasNext()) {
            List<I> expectedList = expectedIter.next();
            List<I> actualList = actualIter.next();
            assertUnorderedList(expectedList, actualList, setNumber);
            setNumber++;
        }
        if (expectedIter.hasNext() || actualIter.hasNext()) {
            throw new AssertionError("Search set size Expected " + expected.size()
                    + "Actual" + actual.size());
        }
    }

    protected List<List<I>> getEntitiesByIndices(List<int[]> indices, List<I> entities) {
        List<List<I>> setList = new ArrayList<>(indices.size());
        for (int[] indexSet : indices) {
            List<I> entitySet = new ArrayList<>(indexSet.length);
            for (int index : indexSet) {
                entitySet.add(entities.get(index));
            }
            setList.add(entitySet);
        }
        return setList;
    }

    protected List<List<BigInteger>> getIdsByIndices(List<int[]> indices, List<BigInteger> ids) {
        List<List<BigInteger>> setList = new ArrayList<>(indices.size());
        for (int[] indexSet : indices) {
            List<BigInteger> idSet = new ArrayList<>(indexSet.length);
            for (int index : indexSet) {
                idSet.add(ids.get(index));
            }
            setList.add(idSet);
        }
        return setList;
    }

    protected void testCollecting() throws Exception {
        List<I> searchSets = getSearchTestSets();

        List<BigInteger> createdIds = new ArrayList<>(searchSets.size());

        for (I searchSet : searchSets) {
            createdIds.add(createEntityManually(searchSet));
        }

        List<List<BigInteger>> expected = getIdsByIndices(getExpectedCollectingSets(), createdIds);
        List<List<BigInteger>> actual = performCollectings();

        Iterator<List<BigInteger>> expectedIter = expected.iterator();
        Iterator<List<BigInteger>> actualIter = actual.iterator();
        int setNumber = 0;
        while (expectedIter.hasNext() && actualIter.hasNext()) {
            List<BigInteger> expectedList = expectedIter.next();
            List<BigInteger> actualList = actualIter.next();
            Collections.sort(expectedList);
            Collections.sort(actualList);
            if (!expectedList.equals(actualList)) {
                throw new AssertionError("Set number " + setNumber + "Expected " + expected + " Actual "
                        + actual);
            }
            setNumber++;
        }
        if (expectedIter.hasNext() || actualIter.hasNext()) {
            throw new AssertionError("Search set size Expected " + expected.size()
                    + "Actual" + actual.size());
        }
    }

    protected void assertUnorderedList(List<I> expected, List<I> actual, int setNumber) {
        Collections.sort(expected, new IdComparator());
        Collections.sort(actual, new IdComparator());
        Iterator<I> expectedIter = expected.iterator();
        Iterator<I> actualIter = actual.iterator();
        int count = 0;
        while (expectedIter.hasNext() && actualIter.hasNext()) {
            I expectedEntity = expectedIter.next();
            I acutalEntity = actualIter.next();
            if (!expectedEntity.deepEquals(acutalEntity)) {
                throw new AssertionError("Set number " + setNumber + "Expected " + expected + " Actual "
                        + actual + " on " + count + " item");
            }
            count++;
        }
        if (expectedIter.hasNext() || actualIter.hasNext()) {
            throw new AssertionError("Set number " + setNumber + "Expected " + expected + " Actual " + actual
                    + " Expected length " + expected.size() + " Actual length " + actual.size());
        }
    }

    protected void assertEntity(I expected, I actual) {
        boolean success;
        if (expected != null) {
            success = expected.deepEquals(actual);
        } else {
            success = (actual == null);
        }
        if (!success) {
            throw new AssertionError("Expected " + expected + " Actual " + actual);
        }
    }

    protected List<I> nullifyCollections(List<I> entities) {
        ArrayList<I> result = new ArrayList<>(entities.size());
        for (I entity : entities) {
            result.add(nullifyCollections(entity));
        }
        return result;
    }

    protected abstract I nullifyCollections(I entity);

    protected class IdComparator implements Comparator<I> {

        @Override
        public int compare(I first, I second) {
            return first.getId().compareTo(second.getId());
        }
    }
}
