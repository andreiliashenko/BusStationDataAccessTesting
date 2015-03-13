package com.anli.busstation.dal.test.additional;

import com.anli.busstation.dal.interfaces.entities.BSEntity;
import com.anli.busstation.dal.interfaces.providers.BSEntityProvider;
import com.anli.busstation.dal.interfaces.providers.geography.RoadProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusRepairmentProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.GasLabelProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.ModelProvider;
import com.anli.busstation.dal.test.AbstractDataAccessTest;
import com.anli.integrationtesting.execution.MultithreadScheduler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class ThreadingTest extends AbstractDataAccessTest {

    protected class Creator implements Callable<Object> {

        protected final BSEntityProvider provider;
        protected final int count;
        
        public Creator(BSEntityProvider provider, int count) {
            this.provider = provider;
            this.count = count;
        }
        
        @Override
        public Object call() throws Exception {
            ArrayList<BSEntity> created = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                created.add(provider.create());
            }
            return created;
        }
    }
    
    @Override
    public void test() throws Exception {
        testIdGeneration();
    }

    protected void testIdGeneration() throws Exception {
        List<Callable<Object>> callables = new ArrayList<>(10);
        callables.add(new Creator(getFactory().getProvider(GasLabelProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(GasLabelProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(ModelProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(ModelProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(MechanicProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(MechanicProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(BusRepairmentProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(BusRepairmentProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(RoadProvider.class), 10));
        callables.add(new Creator(getFactory().getProvider(RoadProvider.class), 10));
        MultithreadScheduler scheduler = new MultithreadScheduler(10);
        scheduler.executeParallel(callables);
    }
}
