package com.anli.busstation.dal.test.additional;

import com.anli.busstation.dal.exceptions.ConsistencyException;
import com.anli.busstation.dal.interfaces.entities.maintenance.StationService;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.maintenance.StationServiceProvider;
import com.anli.busstation.dal.interfaces.providers.staff.DriverSkillProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicSkillProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.TechnicalStateProvider;
import com.anli.busstation.dal.test.AbstractDataAccessTest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public abstract class TransactionTest extends AbstractDataAccessTest {

    @Override
    public void test() throws Exception {
        testDetachedModification();
        testUncommitedSearch();
        testRollback();
    }

    protected void testDetachedModification() throws NotSupportedException, SystemException,
            RollbackException, HeuristicMixedException, HeuristicRollbackException {
        UserTransaction transaction = getTransaction();
        TechnicalStateProvider provider = getFactory().getProvider(TechnicalStateProvider.class);
        String originalDescription = "Original Description";
        String mutatedDescription = "Mutated Description";
        Integer originalLevel = 3;
        Integer mutatedLevel = 5;
        TechnicalState state = provider.create();
        BigInteger stateId = state.getId();
        state.setDescription(originalDescription);
        state.setDifficultyLevel(originalLevel);
        provider.save(state);
        transaction.begin();
        state = provider.findById(stateId);
        state.setDescription(mutatedDescription);
        state = provider.save(state);
        state.setDifficultyLevel(mutatedLevel);
        transaction.commit();
        state = provider.findById(stateId);
        assertEquals(mutatedDescription, state.getDescription());
        assertEquals(originalLevel, state.getDifficultyLevel());
    }

    protected void testUncommitedSearch() throws NotSupportedException, SystemException,
            RollbackException, HeuristicMixedException, HeuristicRollbackException {
        DriverSkillProvider provider = getFactory().getProvider(DriverSkillProvider.class);
        DriverSkill skill = provider.create();
        String originalName = "Original Name";
        String mutatedName = "Mutated Name";
        skill.setName(originalName);
        skill = provider.save(skill);
        UserTransaction transaction = getTransaction();
        transaction.begin();
        skill.setName(mutatedName);
        provider.save(skill);
        List<DriverSkill> mutatedList = provider.findByName(mutatedName);
        List<DriverSkill> originalList = provider.findByName(originalName);
        assertTrue(mutatedList.contains(skill));
        assertFalse(originalList.contains(skill));
        transaction.commit();
    }

    protected void testRollback() throws NotSupportedException, SystemException, HeuristicMixedException,
            HeuristicRollbackException {
        MechanicProvider mechProvider = getFactory().getProvider(MechanicProvider.class);
        MechanicSkillProvider skillProvider = getFactory().getProvider(MechanicSkillProvider.class);
        StationServiceProvider serviceProvider = getFactory().getProvider(StationServiceProvider.class);
        Mechanic mechanic = mechProvider.create();
        BigInteger mechId = mechanic.getId();
        MechanicSkill skill = skillProvider.create();
        BigInteger skillId = skill.getId();
        String originalName = "Original Name";
        String mutatedName = "Mutated Name";
        BigDecimal originalSalary = BigDecimal.valueOf(10000);
        BigDecimal mutatedSalary = BigDecimal.valueOf(15000);
        mechanic.setName(originalName);
        mechanic.setSalary(originalSalary);
        mechanic.setSkill(skill);
        mechanic = mechProvider.save(mechanic);
        UserTransaction transaction = getTransaction();
        transaction.begin();
        BigInteger serviceId = serviceProvider.create().getId();
        mechanic.setName(mutatedName);
        mechanic = mechProvider.save(mechanic);
        skillProvider.remove(skill);
        mechanic.setSalary(mutatedSalary);
        try {
            mechProvider.save(mechanic);
            throw new AssertionError("ConsistencyException expected");
        } catch (ConsistencyException ex) {
            assertTrue(ex.getEntities().size() == 1);
            assertEquals(skill, ex.getEntities().iterator().next());
        }
        try {
            transaction.commit();
            throw new AssertionError("RollbackException expected");
        } catch (RollbackException ex) {

        }
        mechanic = mechProvider.findById(mechId);
        assertEquals(originalName, mechanic.getName());
        assertTrue(originalSalary.compareTo(mechanic.getSalary()) == 0);
        StationService service = serviceProvider.findById(serviceId);
        assertNull(service);
        skill = skillProvider.findById(skillId);
        assertNotNull(skill);
        assertEquals(skill, mechanic.getSkill());
    }

    protected UserTransaction getTransaction() {
        try {
            return InitialContext.doLookup("java:comp/UserTransaction");
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
