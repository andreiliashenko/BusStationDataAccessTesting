package com.anli.busstation.dal.test;

import com.anli.busstation.dal.interfaces.entities.BSEntity;
import com.anli.busstation.dal.interfaces.factories.ProviderFactory;
import com.anli.integrationtesting.execution.TestExecutor;
import java.math.BigInteger;

public abstract class AbstractDataAccessTest implements TestExecutor {

    protected ModuleAccessor moduleAccessor;

    protected FixtureCreator getFixtureCreator() {
        return getModuleAccessor().getFixtureCreator();
    }

    protected ProviderFactory getFactory() {
        return getModuleAccessor().getProviderFactory();
    }

    protected void resetCaches() {
        getModuleAccessor().resetCaches();
    }

    protected void setEntityId(BSEntity entity, BigInteger id) {
        getModuleAccessor().setEntityId(entity, id);
    }

    protected ModuleAccessor getModuleAccessor() {
        if (moduleAccessor == null) {
            moduleAccessor = createModuleAccessor();
        }
        return moduleAccessor;
    }

    protected abstract ModuleAccessor createModuleAccessor();
}
