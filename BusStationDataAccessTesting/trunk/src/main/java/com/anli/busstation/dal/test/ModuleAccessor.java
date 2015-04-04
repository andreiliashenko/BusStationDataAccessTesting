package com.anli.busstation.dal.test;

import com.anli.busstation.dal.interfaces.entities.BSEntity;
import com.anli.busstation.dal.interfaces.factories.ProviderFactory;
import java.math.BigInteger;

public interface ModuleAccessor {

    ProviderFactory getProviderFactory();
    
    FixtureCreator getFixtureCreator();
    
    void resetCaches();
    
    void setEntityId(BSEntity entity, BigInteger id);
}
