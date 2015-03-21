package com.anli.busstation.dal.test.staff;

import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicSkillProvider;
import com.anli.busstation.dal.test.BasicDataAccessTestSceleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class MechanicTest extends BasicDataAccessTestSceleton<Mechanic> {

    protected Map<BigInteger, MechanicSkill> mechanicSkills;

    @Override
    protected void createPrerequisites() throws Exception {
        super.createPrerequisites();
        mechanicSkills = getFixtureCreator().createMechanicSkillFixture(50, 5);
    }

    @Override
    protected BigInteger createEntityByProvider(Mechanic sourceMechanic) throws Exception {
        MechanicProvider provider = getFactory().getProvider(MechanicProvider.class);
        Mechanic mechanic = provider.create();
        mechanic.setHiringDate(sourceMechanic.getHiringDate());
        mechanic.setName(sourceMechanic.getName());
        mechanic.setSalary(sourceMechanic.getSalary());
        mechanic.setSkill(sourceMechanic.getSkill());
        provider.save(mechanic);
        return mechanic.getId();
    }

    @Override
    protected Mechanic getEntityByProvider(BigInteger id) throws Exception {
        return getFactory().getProvider(MechanicProvider.class).findById(id);
    }

    @Override
    protected void updateEntityByProvider(BigInteger id, Mechanic sourceMechanic) throws Exception {
        MechanicProvider provider = getFactory().getProvider(MechanicProvider.class);
        Mechanic mechanic = (Mechanic) provider.findById(id);
        mechanic.setHiringDate(sourceMechanic.getHiringDate());
        mechanic.setName(sourceMechanic.getName());
        mechanic.setSalary(sourceMechanic.getSalary());
        mechanic.setSkill(sourceMechanic.getSkill());
        provider.save(mechanic);
    }

    @Override
    protected void deleteEntityByProvider(BigInteger id) throws Exception {
        MechanicProvider provider = getFactory().getProvider(MechanicProvider.class);
        Mechanic mechanic = (Mechanic) provider.findById(id);
        provider.remove(mechanic);
    }

    @Override
    protected List<Mechanic> getCreationTestSets() throws Exception {
        List<Mechanic> testSets = new ArrayList<>(4);
        testSets.add(getNewMechanic(BigInteger.ZERO, null, null, null, null));
        testSets.add(getNewMechanic(BigInteger.ZERO, "", BigDecimal.ZERO, new DateTime(0), null));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик Механикович", BigDecimal.valueOf(33000.01),
                new DateTime(1997, 11, 30, 0, 0, 0, 0), BigInteger.valueOf(51)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик 2", BigDecimal.valueOf(25555.29),
                new DateTime(2011, 9, 1, 0, 0, 0, 0), BigInteger.valueOf(52)));
        return testSets;
    }

    @Override
    protected List<Mechanic> getUpdateTestSets() throws Exception {
        List<Mechanic> testSets = new ArrayList<>(4);
        testSets.add(getNewMechanic(BigInteger.ZERO, "Обновленный механик 0", BigDecimal.valueOf(21111.99),
                new DateTime(2003, 3, 3, 0, 0, 0, 0), BigInteger.valueOf(50)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Обновленный механик 1", BigDecimal.valueOf(8000.44),
                new DateTime(2005, 12, 31, 0, 0, 0, 0), BigInteger.valueOf(54)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "", BigDecimal.ZERO, new DateTime(0),
                BigInteger.valueOf(52)));
        testSets.add(getNewMechanic(BigInteger.ZERO, null, null, null, null));
        return testSets;
    }

    @Override
    protected List<List<Mechanic>> performSearches() throws Exception {
        List<List<Mechanic>> searchResult = new ArrayList<>(8);
        MechanicProvider provider = getFactory().getProvider(MechanicProvider.class);
        List<Mechanic> resultCollection;
        resultCollection = provider.findBySkill(null);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySkill(getSkillById(BigInteger.valueOf(50)));
        searchResult.add(resultCollection);
        resultCollection = provider.findByAnySkill(Arrays.asList(getSkillById(BigInteger.valueOf(51)),
                getSkillById(BigInteger.valueOf(53))));
        searchResult.add(resultCollection);
        resultCollection = provider.findByName("Механик 0");
        searchResult.add(resultCollection);
        resultCollection = provider.findByNameRegexp("Механик [0-9][0-9]$");
        searchResult.add(resultCollection);
        resultCollection = provider.findByHiringDateRange(new DateTime(2000, 1, 1, 0, 0, 0, 0), false,
                new DateTime(2005, 4, 19, 0, 0, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.findBySalaryRange(BigDecimal.valueOf(7500), true,
                BigDecimal.valueOf(15579.99), false);
        searchResult.add(resultCollection);
        resultCollection = provider.findAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<List<BigInteger>> performCollectings() throws Exception {
        List<List<BigInteger>> searchResult = new ArrayList<>(8);
        MechanicProvider provider = getFactory().getProvider(MechanicProvider.class);
        List<BigInteger> resultCollection;
        resultCollection = provider.collectIdsBySkill(null);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySkill(getSkillById(BigInteger.valueOf(50)));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByAnySkill(Arrays.asList(getSkillById(BigInteger.valueOf(51)),
                getSkillById(BigInteger.valueOf(53))));
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByName("Механик 0");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByNameRegexp("Механик [0-9][0-9]$");
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsByHiringDateRange(new DateTime(2000, 1, 1, 0, 0, 0, 0), false,
                new DateTime(2005, 4, 19, 0, 0, 0, 0), true);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsBySalaryRange(BigDecimal.valueOf(7500), true,
                BigDecimal.valueOf(15579.99), false);
        searchResult.add(resultCollection);
        resultCollection = provider.collectIdsAll();
        searchResult.add(resultCollection);
        return searchResult;
    }

    @Override
    protected List<Mechanic> getSearchTestSets() throws Exception {
        List<Mechanic> testSets = new ArrayList<>(5);
        testSets.add(getNewMechanic(BigInteger.ZERO, "", BigDecimal.valueOf(6000), null,
                BigInteger.valueOf(50)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик 0", BigDecimal.valueOf(7499.99),
                new DateTime(2000, 1, 1, 0, 0, 0, 0), BigInteger.valueOf(51)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик Два", BigDecimal.valueOf(7500),
                new DateTime(2005, 4, 18, 0, 0, 0, 0), BigInteger.valueOf(52)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик 01", BigDecimal.valueOf(15579.99),
                new DateTime(2005, 4, 19, 0, 0, 0, 0), BigInteger.valueOf(53)));
        testSets.add(getNewMechanic(BigInteger.ZERO, "Механик 22", BigDecimal.valueOf(15580),
                new DateTime(2006, 4, 19, 0, 0, 0, 0), BigInteger.valueOf(54)));
        return testSets;
    }

    @Override
    protected List<int[]> getExpectedSearchSets() {
        List<int[]> searchResult = new ArrayList<>(8);
        searchResult.add(new int[]{});
        searchResult.add(new int[]{0});
        searchResult.add(new int[]{1, 3});
        searchResult.add(new int[]{1});
        searchResult.add(new int[]{3, 4});
        searchResult.add(new int[]{1, 2});
        searchResult.add(new int[]{3});
        searchResult.add(new int[]{0, 1, 2, 3, 4});
        return searchResult;
    }

    @Override
    protected List<int[]> getExpectedCollectingSets() {
        return getExpectedSearchSets();
    }

    protected Mechanic getNewMechanic(BigInteger id, String name, BigDecimal salary,
            DateTime hiringDate, BigInteger skillId) {
        return getNewMechanic(id, name, salary, hiringDate, skillId, false);
    }

    protected abstract Mechanic getNewMechanic(BigInteger id, String name, BigDecimal salary,
            DateTime hiringDate, BigInteger skillId, boolean load);

    protected MechanicSkill getSkillById(BigInteger skillId) {
        return getSkillById(skillId, false);
    }

    protected MechanicSkill getSkillById(BigInteger skillId, boolean load) {
        return load ? getFactory().getProvider(MechanicSkillProvider.class).findById(skillId)
                : mechanicSkills.get(skillId);
    }

    @Override
    protected Mechanic nullifyCollections(Mechanic entity) {
        return entity;
    }
}
