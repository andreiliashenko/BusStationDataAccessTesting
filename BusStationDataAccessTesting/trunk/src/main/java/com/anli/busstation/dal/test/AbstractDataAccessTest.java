package com.anli.busstation.dal.test;

import com.anli.busstation.dal.interfaces.factories.ProviderFactory;
import com.anli.integrationtesting.execution.TestExecutor;

public abstract class AbstractDataAccessTest implements TestExecutor {
    
    protected abstract FixtureCreator getFixtureCreator();
    
    protected abstract ProviderFactory getFactory();
}
