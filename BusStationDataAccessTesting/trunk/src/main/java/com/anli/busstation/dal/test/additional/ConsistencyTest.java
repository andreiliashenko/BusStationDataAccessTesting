package com.anli.busstation.dal.test.additional;

import com.anli.busstation.dal.exceptions.ConsistencyException;
import com.anli.busstation.dal.interfaces.entities.BSEntity;
import com.anli.busstation.dal.interfaces.entities.geography.Region;
import com.anli.busstation.dal.interfaces.entities.geography.Road;
import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.maintenance.StationService;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.geography.RegionProvider;
import com.anli.busstation.dal.interfaces.providers.geography.RoadProvider;
import com.anli.busstation.dal.interfaces.providers.geography.StationProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.StationServiceProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.ModelProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.TechnicalStateProvider;
import com.anli.busstation.dal.test.AbstractDataAccessTest;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public abstract class ConsistencyTest extends AbstractDataAccessTest {

    @Override
    public void test() throws Exception {
        testInconsistentReferenceSave();
        testInconsistentReferenceSave();
        testRemovedEntitySave();
        testRemovedEntityRemoval();
    }

    protected void testInconsistentReferenceSave() throws Exception {
        ModelProvider modelProvider = getFactory().getProvider(ModelProvider.class);
        TechnicalStateProvider stateProvider = getFactory().getProvider(TechnicalStateProvider.class);
        Model model = modelProvider.create();
        TechnicalState state = stateProvider.create();
        BusProvider busProvider = getFactory().getProvider(BusProvider.class);
        Bus bus = busProvider.create();
        bus.setPlate("Plate");
        bus.setModel(model);
        bus.setState(state);
        stateProvider.remove(state);
        modelProvider.remove(model);

        try {
            busProvider.save(bus);
            throw new AssertionError(ConsistencyException.class.getName() + " expected");
        } catch (ConsistencyException ex) {
            Collection<BSEntity> inconsistent = ex.getEntities();
            assertEquals(2, inconsistent.size());
            assertTrue(inconsistent.contains(model));
            assertTrue(inconsistent.contains(state));
            Bus actualBus = busProvider.findById(bus.getId());
            assertNull(actualBus.getPlate());
        }
    }

    protected void testInconsistentCollectionSave() throws Exception {
        StationProvider stationProvider = getFactory().getProvider(StationProvider.class);
        Station stationA = stationProvider.create();
        Station stationB = stationProvider.create();
        Station stationC = stationProvider.create();
        RegionProvider regionProvider = getFactory().getProvider(RegionProvider.class);
        Region region = regionProvider.create();
        region = regionProvider.pullStations(region);
        region.getStations().addAll(Arrays.asList(stationA, stationB, stationC));
        region.setName("Name");
        stationProvider.remove(stationA);
        stationProvider.remove(stationC);

        try {
            regionProvider.save(region);
            throw new AssertionError(ConsistencyException.class.getName() + " expected");
        } catch (ConsistencyException ex) {
            Collection<BSEntity> inconsistent = ex.getEntities();
            assertEquals(2, inconsistent.size());
            assertTrue(inconsistent.contains(stationA));
            assertTrue(inconsistent.contains(stationC));
            Region actualRegion = regionProvider.findById(region.getId());
            assertNull(actualRegion.getName());
        }
    }

    protected void testRemovedEntitySave() throws Exception {
        RoadProvider roadProvider = getFactory().getProvider(RoadProvider.class);
        Road road = roadProvider.create();
        road.setLength(5000);
        roadProvider.remove(road);
        try {
            roadProvider.save(road);
            throw new AssertionError(ConsistencyException.class.getName() + " expected");
        } catch (ConsistencyException ex) {
            Collection<BSEntity> inconsistent = ex.getEntities();
            assertEquals(1, inconsistent.size());
            assertEquals(road, inconsistent.iterator().next());
            Road removedRoad = roadProvider.findById(road.getId());
            assertNull(removedRoad);
        }
    }

    protected void testRemovedEntityRemoval() throws Exception {
        StationServiceProvider serviceProvider = getFactory().getProvider(StationServiceProvider.class);
        StationService service = serviceProvider.create();
        serviceProvider.remove(service);
        try {
            serviceProvider.remove(service);
            throw new AssertionError(ConsistencyException.class.getName() + " expected");
        } catch (ConsistencyException ex) {
            Collection<BSEntity> inconsistent = ex.getEntities();
            assertEquals(1, inconsistent.size());
            assertEquals(service, inconsistent.iterator().next());
            StationService removedService = serviceProvider.findById(service.getId());
            assertNull(removedService);
        }
    }
}
