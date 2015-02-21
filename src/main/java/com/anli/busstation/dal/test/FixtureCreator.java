package com.anli.busstation.dal.test;

import com.anli.busstation.dal.interfaces.entities.BSEntity;
import com.anli.busstation.dal.interfaces.entities.vehicles.Bus;
import com.anli.busstation.dal.interfaces.entities.staff.Driver;
import com.anli.busstation.dal.interfaces.entities.staff.DriverSkill;
import com.anli.busstation.dal.interfaces.entities.staff.Employee;
import com.anli.busstation.dal.interfaces.entities.vehicles.GasLabel;
import com.anli.busstation.dal.interfaces.entities.staff.Mechanic;
import com.anli.busstation.dal.interfaces.entities.staff.MechanicSkill;
import com.anli.busstation.dal.interfaces.entities.vehicles.Model;
import com.anli.busstation.dal.interfaces.entities.staff.Salesman;
import com.anli.busstation.dal.interfaces.entities.geography.Station;
import com.anli.busstation.dal.interfaces.entities.vehicles.TechnicalState;
import com.anli.busstation.dal.interfaces.factories.ProviderFactory;
import com.anli.busstation.dal.interfaces.providers.vehicles.BusProvider;
import com.anli.busstation.dal.interfaces.providers.staff.DriverProvider;
import com.anli.busstation.dal.interfaces.providers.staff.DriverSkillProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.GasLabelProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicProvider;
import com.anli.busstation.dal.interfaces.providers.staff.MechanicSkillProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.ModelProvider;
import com.anli.busstation.dal.interfaces.providers.staff.SalesmanProvider;
import com.anli.busstation.dal.interfaces.providers.geography.StationProvider;
import com.anli.busstation.dal.interfaces.providers.vehicles.TechnicalStateProvider;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public abstract class FixtureCreator {

    protected abstract ProviderFactory getFactory();

    protected abstract void setIdManually(BSEntity entity, BigInteger id);

    public Map<BigInteger, GasLabel> createGasLabelFixture(int minId, int count) {
        GasLabelProvider glProvider = getFactory().getProvider(GasLabelProvider.class);
        Map<BigInteger, GasLabel> gasLabels = new HashMap<>();
        for (int i = 0; i < count; i++) {
            GasLabel gasLabel = glProvider.findById(BigInteger.valueOf(minId + i));
            if (gasLabel == null) {
                gasLabel = glProvider.create();
            }
            gasLabel.setName("GL" + i);
            gasLabel.setPrice(BigDecimal.valueOf(30 + i));
            gasLabel = glProvider.save(gasLabel);
            setIdManually(gasLabel, BigInteger.valueOf(minId + i));
            gasLabels.put(gasLabel.getId(), gasLabel);
        }
        return gasLabels;
    }

    public Map<BigInteger, TechnicalState> createTechnicalStateFixture(int minId, int count) {
        TechnicalStateProvider sProvider = getFactory().getProvider(TechnicalStateProvider.class);
        Map<BigInteger, TechnicalState> technicalStates = new HashMap<>();
        for (int i = 0; i < count; i++) {
            TechnicalState state = sProvider.findById(BigInteger.valueOf(minId + i));
            if (state == null) {
                state = sProvider.create();
            }
            state.setDescription("Description " + i);
            state.setDifficultyLevel(i);
            state = sProvider.save(state);
            setIdManually(state, BigInteger.valueOf(minId + i));
            technicalStates.put(state.getId(), state);
        }
        return technicalStates;
    }

    public Map<BigInteger, Model> createModelFixture(int minId, int count, List<GasLabel> gasLabels) {
        ModelProvider mProvider = getFactory().getProvider(ModelProvider.class);
        Map<BigInteger, Model> models = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Model model = mProvider.findById(BigInteger.valueOf(minId + i));
            if (model == null) {
                model = mProvider.create();
            }
            model.setGasLabel(gasLabels.isEmpty() ? null : gasLabels.get(i % gasLabels.size()));
            model.setGasRate(BigDecimal.valueOf(50 + i * 10));
            model.setName("Model " + i);
            model.setSeatsNumber(i * 10);
            model.setTankVolume(i * 15);
            model = mProvider.save(model);
            setIdManually(model, BigInteger.valueOf(minId + i));
            models.put(model.getId(), model);
        }
        return models;
    }

    public Map<BigInteger, DriverSkill> createDriverSkillFixture(int minId, int count) {
        DriverSkillProvider dsProvider = getFactory().getProvider(DriverSkillProvider.class);
        Map<BigInteger, DriverSkill> driverSkills = new HashMap<>();
        for (int i = 0; i < count; i++) {
            DriverSkill skill = dsProvider.findById(BigInteger.valueOf(minId + i));
            if (skill == null) {
                skill = dsProvider.create();
            }
            skill.setName("Skill " + i);
            skill.setMaxPassengers(20 + i * 3);
            skill.setMaxRideLength(1000 * i);
            skill = dsProvider.save(skill);
            setIdManually(skill, BigInteger.valueOf(minId + i));
            driverSkills.put(skill.getId(), skill);
        }
        return driverSkills;
    }

    public Map<BigInteger, MechanicSkill> createMechanicSkillFixture(int minId, int count) {
        MechanicSkillProvider msProvider = getFactory().getProvider(MechanicSkillProvider.class);
        Map<BigInteger, MechanicSkill> mechanicSkills = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            MechanicSkill skill = msProvider.findById(BigInteger.valueOf(minId + i));
            if (skill == null) {
                skill = msProvider.create();
            }
            skill.setName("Skill " + i);
            skill.setMaxDiffLevel(i * 2);
            skill = msProvider.save(skill);
            setIdManually(skill, BigInteger.valueOf(minId + i));
            mechanicSkills.put(skill.getId(), skill);
        }
        return mechanicSkills;
    }

    public Map<BigInteger, Bus> createBusFixture(int minId, int count, List<Model> models,
            List<TechnicalState> states) {
        BusProvider busProvider = getFactory().getProvider(BusProvider.class);
        Map<BigInteger, Bus> buses = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Bus bus = busProvider.findById(BigInteger.valueOf(minId + i));
            if (bus == null) {
                bus = busProvider.create();
            }
            bus.setModel(models.isEmpty() ? null : models.get(i % models.size()));
            bus.setPlate("P" + i * 100 + "LT");
            bus.setState(states.isEmpty() ? null : states.get(i % states.size()));
            bus = busProvider.save(bus);
            setIdManually(bus, BigInteger.valueOf(minId + i));
            buses.put(bus.getId(), bus);
        }
        return buses;
    }

    public Map<BigInteger, Driver> createDriverFixture(int minId, int count,
            List<DriverSkill> skills) {
        DriverProvider driverProvider = getFactory().getProvider(DriverProvider.class);
        Map<BigInteger, Driver> drivers = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Driver driver = driverProvider.findById(BigInteger.valueOf(minId + i));
            if (driver == null) {
                driver = driverProvider.create();
            }
            driver.setHiringDate(new DateTime(1990 + i, i % 11 + 1, i % 29 + 1, 0, 0, 0, 0));
            driver.setName("Driver " + i);
            driver.setSalary(BigDecimal.valueOf(i * 1000));
            driver.setSkill(skills.isEmpty() ? null : skills.get(i % skills.size()));
            driver = driverProvider.save(driver);
            setIdManually(driver, BigInteger.valueOf(minId + i));
            drivers.put(driver.getId(), driver);
        }
        return drivers;
    }

    public Map<BigInteger, Mechanic> createMechanicFixture(int minId, int count, List<MechanicSkill> skills) {
        MechanicProvider mechanicProvider = getFactory().getProvider(MechanicProvider.class);
        Map<BigInteger, Mechanic> mechanics = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Mechanic mechanic = mechanicProvider.findById(BigInteger.valueOf(minId + i));
            if (mechanic == null) {
                mechanic = mechanicProvider.create();
            }
            mechanic.setHiringDate(new DateTime(1990 + i, i % 11 + 1, i % 29 + 1, 0, 0, 0, 0));
            mechanic.setName("Mechanic " + i);
            mechanic.setSalary(BigDecimal.valueOf(i * 1000));
            mechanic.setSkill(skills.isEmpty() ? null : skills.get(i % skills.size()));
            mechanic = mechanicProvider.save(mechanic);
            setIdManually(mechanic, BigInteger.valueOf(minId + i));
            mechanics.put(mechanic.getId(), mechanic);
        }
        return mechanics;
    }

    public Map<BigInteger, Salesman> createSalesmanFixture(int minId, int count) {
        SalesmanProvider salesmanProvider = getFactory().getProvider(SalesmanProvider.class);
        Map<BigInteger, Salesman> salesmen = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Salesman salesman = salesmanProvider.findById(BigInteger.valueOf(minId + i));
            if (salesman == null) {
                salesman = salesmanProvider.create();
            }
            salesman.setHiringDate(new DateTime(1990 + i, i % 11 + 1, i % 29 + 1, 0, 0, 0, 0));
            salesman.setName("Mechanic " + i);
            salesman.setSalary(BigDecimal.valueOf(i * 1000));
            salesman.setTotalSales(i * 150);
            salesman = salesmanProvider.save(salesman);
            setIdManually(salesman, BigInteger.valueOf(minId + i));
            salesmen.put(salesman.getId(), salesman);
        }
        return salesmen;
    }

    public Map<BigInteger, Station> createStationFixture(int minId, int count,
            List<Bus> buses, List<Employee> employees) {
        StationProvider stationProvider = getFactory().getProvider(StationProvider.class);
        Map<BigInteger, Station> stations = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Station station = stationProvider.findById(BigInteger.valueOf(minId + i));
            if (station == null) {
                station = stationProvider.create();
            }
            station = stationProvider.pullBuses(station);
            station = stationProvider.pullEmployees(station);
            station.setLatitude(BigDecimal.valueOf(i * 10));
            station.setLongitude(BigDecimal.valueOf(i * 25));
            station.setName("Station " + i);
            station.getBuses().clear();
            if (!buses.isEmpty()) {
                station.getBuses().addAll(buses.subList(i % buses.size(), i + 1));
            }
            station.getEmployees().clear();
            if (!employees.isEmpty()) {
                station.getEmployees().addAll(employees.subList(i % employees.size(), i + 1));
            }
            station = stationProvider.save(station);
            setIdManually(station, BigInteger.valueOf(minId + i));
            stations.put(station.getId(), station);
        }
        return stations;
    }
}
