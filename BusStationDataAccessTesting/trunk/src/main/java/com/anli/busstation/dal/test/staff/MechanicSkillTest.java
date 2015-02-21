package com.anli.busstation.dal.test.staff;

import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicSkillProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class MechanicSkillTest extends BasicDataAccessTestSceleton<MechanicSkill> {

    @Override
    protected BigInteger createEntityByProvider(MechanicSkill sourceSkill) throws Exception {
        MechanicSkillProvider provider = getFactory().getProvider(MechanicSkillProvider.class);
        MechanicSkill skill = provider.create();
        skill.setName(sourceSkill.getName());
        skill.setMaxDiffLevel(sourceSkill.getMaxDiffLevel());
        provider.save(skill);
        return skill.getId();
    }

    @Override
    protected MechanicSkill getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(MechanicSkillProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, MechanicSkill sourceSkill) throws Exception {
        MechanicSkillProvider provider = getFactory().getProvider(MechanicSkillProvider.class);
        MechanicSkill skill = provider.findById(id);
        skill.setName(sourceSkill.getName());
        skill.setMaxDiffLevel(sourceSkill.getMaxDiffLevel());
        provider.save(skill);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        MechanicSkillProvider provider = getFactory().getProvider(MechanicSkillProvider.class);
        MechanicSkill skill = provider.findById(id);
        provider.remove(skill);
    }

    @Override
    protected List<MechanicSkill> getCreationTestSets() {
        List<MechanicSkill> testSets = new ArrayList<>(4);

        testSets.add(getNewMechanicSkill(BigInteger.ZERO, null, null));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "", 0));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "Created Name 1", 3));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "Created Name 2", 9));

        return testSets;
    }

    @Override
    protected List<MechanicSkill> getUpdateTestSets() {
        List<MechanicSkill> testSets = new ArrayList<>(4);

        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "Updated Name 1", 4));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "Updated Name 2", null));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, null, 11));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "", 0));

        return testSets;
    }

    @Override
    protected List<MechanicSkill> getSearchTestSets() {
        List<MechanicSkill> testSets = new ArrayList<>(8);

        testSets.add(getNewMechanicSkill(BigInteger.ZERO, null, null));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, null, 3));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "", 4));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "", 5));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "S1", 6));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "S2", 7));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "S3", 8));
        testSets.add(getNewMechanicSkill(BigInteger.ZERO, "S5", 9));

        return testSets;
    }

    @Override
    protected List<List<MechanicSkill>> performSearches() throws Exception {
        List<List<MechanicSkill>> searchResult = new ArrayList<>(17);

        MechanicSkillProvider provider = getFactory().getProvider(MechanicSkillProvider.class);
        List<MechanicSkill> resultCollection;

        resultCollection = provider.findByName(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("");
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("S1");
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("S4");
        searchResult.add(resultCollection);
        resultCollection = provider.findByNameRegexp("^S[0-4]$");
        searchResult.add(resultCollection);

        resultCollection = provider.findByMaxDiffLevelRange(null, true, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(5, false, 5, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(4, true, 4, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(3, false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(3, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(null, true, 6, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(null, false, 6, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(4, true, 7, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(4, true, 7, false);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(4, false, 7, true);
        searchResult.add(resultCollection);
        resultCollection = provider.findByMaxDiffLevelRange(4, false, 7, false);
        searchResult.add(resultCollection);

        resultCollection = provider.findAll();
        searchResult.add(resultCollection);

        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList(17);
        searchResult.add(new int[]{0, 1});
        searchResult.add(new int[]{2, 3});
        searchResult.add(new int[]{4});
        searchResult.add(new int[]{});
        searchResult.add(new int[]{4, 5, 6});

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
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList(17);
        MechanicSkillProvider provider = getFactory().getProvider(MechanicSkillProvider.class);
        List<BigInteger> resultCollection;

        resultCollection = provider.collectIdsByName(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("S1");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("S4");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByNameRegexp("^S[0-4]$");
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsByMaxDiffLevelRange(null, true, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(5, false, 5, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(4, true, 4, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(3, false, null, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(3, true, null, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(null, true, 6, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(null, false, 6, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(4, true, 7, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(4, true, 7, false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(4, false, 7, true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByMaxDiffLevelRange(4, false, 7, false);
        searchResult.add(resultCollection);

        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);

        return searchResult;
    }

    protected abstract MechanicSkill getNewMechanicSkill(BigInteger id, String name, Integer maxDiffLevel);

    @Override
    protected MechanicSkill nullifyCollections(MechanicSkill entity) {
        return entity;
    }
}
