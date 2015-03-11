package com.anli.busstation.dal.test.additional;

import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.maintenance.BusRefuelling;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.traffic.Ride;
import com.anli.busstation.dal.interfaces.entities.traffic.Route;
import com.anli.busstation.dal.interfaces.entities.traffic.RoutePoint;
import com.anli.busstation.dal.interfaces.entities.traffic.Ticket;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.providers.geography.StationProvider;
import com.anli.busstation.dal.interfaces.providers.maintenance.BusRefuellingProvider;
import com.anli.busstation.dal.interfaces.providers.staff.DriverProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicSkillProvider;
import com.anli.busstation.dal.interfaces.providers.staff.SalesmanProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RideProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RoutePointProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.RouteProvider;
import com.anli.busstation.dal.interfaces.providers.traffic.TicketProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.GasLabelProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.ModelProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.TechnicalStateProvider;
import com.anli.busstation.dal.test.AbstractDataAccessTest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public abstract class ReferenceMutationTest extends AbstractDataAccessTest {

    @Override
    public void test() throws Exception {
        testSearchByEntity();
        testSearchByAnyEntity();
        testPull();
        testReferenceSave();
        testCollectionSave();
        testReferenceRemoval();
        testCollectionRemoval();
    }

    protected void testSearchByEntity() throws Exception {
        GasLabelProvider labelProvider = getFactory().getProvider(GasLabelProvider.class);
        GasLabel label = labelProvider.create();
        ModelProvider modelProvider = getFactory().getProvider(ModelProvider.class);
        String originalName = "Original name";
        BigDecimal originalPrice = BigDecimal.valueOf(55.89);
        label.setName(originalName);
        label.setPrice(originalPrice);
        label = labelProvider.save(label);

        Model model = modelProvider.create();
        model.setGasLabel(label);
        model = modelProvider.save(model);

        String mutatedName = "Mutated name";
        BigDecimal mutatedPrice = BigDecimal.valueOf(99.99);
        label.setName(mutatedName);
        label.setPrice(mutatedPrice);

        List<BigInteger> modelIdsByLabel = modelProvider.collectIdsByGasLabel(label);
        assertEquals(mutatedName, label.getName());
        assertEquals(mutatedPrice, label.getPrice());
        assertEquals(1, modelIdsByLabel.size());
        assertEquals(model.getId(), modelIdsByLabel.iterator().next());

        List<Model> modelsByLabel = modelProvider.findByGasLabel(label);
        assertEquals(mutatedName, label.getName());
        assertEquals(mutatedPrice, label.getPrice());
        assertEquals(1, modelsByLabel.size());
        Model actualModel = modelsByLabel.iterator().next();
        assertEquals(model, actualModel);

        GasLabel actualLabel = actualModel.getGasLabel();
        assertEquals(originalName, actualLabel.getName());
        assertEquals(originalPrice, actualLabel.getPrice());
    }

    protected void testSearchByAnyEntity() throws Exception {
        BusProvider busProvider = getFactory().getProvider(BusProvider.class);
        String originalPlateA = "OrPlA";
        String originalPlateB = "OrPlB";
        Bus busA = busProvider.create();
        busA.setPlate(originalPlateA);
        busA = busProvider.save(busA);
        Bus busB = busProvider.create();
        busB.setPlate(originalPlateB);
        busB = busProvider.save(busB);

        BusRefuellingProvider refuellingProvider = getFactory().getProvider(BusRefuellingProvider.class);
        BusRefuelling refuellingA = refuellingProvider.create();
        refuellingA.setBus(busA);
        refuellingA = refuellingProvider.save(refuellingA);
        BusRefuelling refuellingB = refuellingProvider.create();
        refuellingB.setBus(busB);
        refuellingB = refuellingProvider.save(refuellingB);
        BigDecimal originalPrice = BigDecimal.valueOf(55.89);

        String mutatedPlateA = "MtPlA";
        String mutatedPlateB = "MtPlB";
        busA.setPlate(mutatedPlateA);
        busB.setPlate(mutatedPlateB);

        List<BigInteger> refIdByBuses = refuellingProvider
                .collectIdsByAnyBus(Arrays.asList(busA, busB));
        assertEquals(mutatedPlateA, busA.getPlate());
        assertEquals(mutatedPlateB, busB.getPlate());
        assertEquals(2, refIdByBuses.size());
        assertTrue(refIdByBuses.contains(refuellingA.getId()));
        assertTrue(refIdByBuses.contains(refuellingB.getId()));

        List<BusRefuelling> refsByBuses = refuellingProvider.findByAnyBus(Arrays.asList(busA, busB));
        assertEquals(mutatedPlateA, busA.getPlate());
        assertEquals(mutatedPlateB, busB.getPlate());
        assertEquals(2, refsByBuses.size());
        assertTrue(refsByBuses.contains(refuellingA));
        assertTrue(refsByBuses.contains(refuellingB));

        BusRefuelling firstRef = refsByBuses.get(0);
        BusRefuelling secondRef = refsByBuses.get(1);
        BusRefuelling actualRefA = firstRef.equals(refuellingA) ? firstRef : secondRef;
        BusRefuelling actualRefB = firstRef.equals(refuellingB) ? firstRef : secondRef;
        Bus actualBusA = actualRefA.getBus();
        Bus actualBusB = actualRefB.getBus();
        assertEquals(originalPlateA, actualBusA.getPlate());
        assertEquals(originalPlateB, actualBusB.getPlate());
    }

    protected void testPull() throws Exception {
        BusProvider busProvider = getFactory().getProvider(BusProvider.class);
        Bus busA = busProvider.create();
        Bus busB = busProvider.create();
        Bus busC = busProvider.create();
        Employee employeeA = getFactory().getProvider(DriverProvider.class).create();
        Employee employeeB = getFactory().getProvider(MechanicProvider.class).create();
        Employee employeeC = getFactory().getProvider(SalesmanProvider.class).create();

        String originalName = "Original Name";
        BigDecimal originalLatitude = BigDecimal.valueOf(50);
        BigDecimal originalLongitude = BigDecimal.valueOf(60);
        StationProvider stationProvider = getFactory().getProvider(StationProvider.class);
        Station station = stationProvider.create();
        station = stationProvider.pullBuses(station);
        station = stationProvider.pullEmployees(station);
        BigInteger stationId = station.getId();
        station.setName(originalName);
        station.setLatitude(originalLatitude);
        station.setLongitude(originalLongitude);
        List<Bus> stationBuses = station.getBuses();
        stationBuses.add(busA);
        stationBuses.add(busB);
        stationBuses.add(busC);
        List<Employee> stationEmployees = station.getEmployees();
        stationEmployees.add(employeeA);
        stationEmployees.add(employeeB);
        stationEmployees.add(employeeC);
        stationProvider.save(station);

        station = stationProvider.findById(stationId);
        String mutatedName = "Mutated Name";
        BigDecimal mutatedLatitude = BigDecimal.valueOf(70);
        BigDecimal mutatedLongitude = BigDecimal.valueOf(80);
        station.setName(mutatedName);
        station.setLatitude(mutatedLatitude);
        station.setLongitude(mutatedLongitude);
        station = stationProvider.pullBuses(station);
        assertEquals(mutatedName, station.getName());
        assertEquals(mutatedLatitude, station.getLatitude());
        assertEquals(mutatedLongitude, station.getLongitude());

        station.getBuses().remove(0);
        station = stationProvider.pullEmployees(station);
        List<Bus> actualBuses = station.getBuses();
        assertEquals(mutatedName, station.getName());
        assertEquals(mutatedLatitude, station.getLatitude());
        assertEquals(mutatedLongitude, station.getLongitude());
        assertEquals(busB, actualBuses.get(0));
        assertEquals(busC, actualBuses.get(1));
    }

    protected void testReferenceSave() throws Exception {
        Integer originalLevel = 4;
        String originalName = "Original name";
        MechanicSkillProvider skillProvider = getFactory()
                .getProvider(MechanicSkillProvider.class);
        MechanicSkill skill = skillProvider.create();
        skill.setMaxDiffLevel(originalLevel);
        skill.setName(originalName);
        skill = skillProvider.save(skill);
        BigInteger skillId = skill.getId();

        MechanicProvider mechanicProvider = getFactory().getProvider(MechanicProvider.class);
        Mechanic mechanic = mechanicProvider.create();
        BigInteger mechanicId = mechanic.getId();
        Integer mutatedLevel = 5;
        String mutatedName = "Mutated name";
        skill.setMaxDiffLevel(mutatedLevel);
        skill.setName(mutatedName);
        mechanic.setSkill(skill);
        mechanicProvider.save(mechanic);
        Mechanic actualMechanic = mechanicProvider.findById(mechanicId);
        MechanicSkill skillFromMechanic = actualMechanic.getSkill();
        assertEquals(skillId, skillFromMechanic.getId());
        assertEquals(originalLevel, skillFromMechanic.getMaxDiffLevel());
        assertEquals(originalName, skillFromMechanic.getName());
        MechanicSkill actualSkill = skillProvider.findById(skillId);
        assertEquals(originalLevel, actualSkill.getMaxDiffLevel());
        assertEquals(originalName, actualSkill.getName());
    }

    protected void testCollectionSave() throws Exception {

        StationProvider stationProvider = getFactory().getProvider(StationProvider.class);
        Station stationA = stationProvider.create();
        Station stationB = stationProvider.create();
        Station stationC = stationProvider.create();
        Station stationD = stationProvider.create();
        RoutePointProvider pointProvider = getFactory().getProvider(RoutePointProvider.class);
        RoutePoint routePointA = pointProvider.create();
        routePointA.setStation(stationA);
        routePointA = pointProvider.save(routePointA);
        RoutePoint routePointB = pointProvider.create();
        routePointB.setStation(stationB);
        routePointB = pointProvider.save(routePointB);
        BigInteger pointAId = routePointA.getId();
        BigInteger pointBId = routePointB.getId();

        routePointA.setStation(stationC);
        routePointB.setStation(stationD);

        RouteProvider routeProvider = getFactory().getProvider(RouteProvider.class);
        Route route = routeProvider.create();
        route = routeProvider.pullRoutePoints(route);
        route.getRoutePoints().add(routePointA);
        route.getRoutePoints().add(routePointB);
        routeProvider.save(route);
        RoutePoint actualPointA = pointProvider.findById(pointAId);
        RoutePoint actualPointB = pointProvider.findById(pointBId);

        assertEquals(stationA, actualPointA.getStation());
        assertEquals(stationB, actualPointB.getStation());
    }

    protected void testReferenceRemoval() throws Exception {
        TechnicalStateProvider stateProvider = getFactory().getProvider(TechnicalStateProvider.class);
        TechnicalState state = stateProvider.create();
        BigInteger stateId = state.getId();
        BusProvider busProvider = getFactory().getProvider(BusProvider.class);
        Bus bus = busProvider.create();
        bus.setState(state);
        bus = busProvider.save(bus);
        busProvider.remove(bus);

        TechnicalState actualState = stateProvider.findById(stateId);
        assertNotNull(actualState);
    }

    protected void testCollectionRemoval() throws Exception {
        TicketProvider ticketProvider = getFactory().getProvider(TicketProvider.class);
        Ticket ticketA = ticketProvider.create();
        Ticket ticketB = ticketProvider.create();
        Ticket ticketC = ticketProvider.create();
        BigInteger ticketIdA = ticketA.getId();
        BigInteger ticketIdB = ticketB.getId();
        BigInteger ticketIdC = ticketC.getId();
        RideProvider rideProvider = getFactory().getProvider(RideProvider.class);
        Ride ride = rideProvider.create();
        ride = rideProvider.pullTickets(ride);
        ride.getTickets().addAll(Arrays.asList(ticketA, ticketB, ticketC));
        ride = rideProvider.save(ride);
        rideProvider.remove(ride);

        Ticket actualTicketA = ticketProvider.findById(ticketIdA);
        Ticket actualTicketB = ticketProvider.findById(ticketIdB);
        Ticket actualTicketC = ticketProvider.findById(ticketIdC);
        assertNotNull(actualTicketA);
        assertNotNull(actualTicketB);
        assertNotNull(actualTicketC);
    }
}
