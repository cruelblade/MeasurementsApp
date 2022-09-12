package ru.alishev.springcourse.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.FirstRestApp.models.Measurement;
import ru.alishev.springcourse.FirstRestApp.models.Sensor;
import ru.alishev.springcourse.FirstRestApp.repositories.MeasurementsRepository;
import ru.alishev.springcourse.FirstRestApp.util.SensorException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsService sensorsService) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsService = sensorsService;
    }

    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    public Measurement findOne(int id) {
        return measurementsRepository.findById(id).orElse(null);
    }

    @Transactional
    public void addMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementsRepository.save(measurement);
    }

    public void enrichMeasurement(Measurement measurement) {
        Optional<Sensor> sensor = sensorsService.findByName(measurement.getSensor().getName());

        if (sensor.isPresent()){
            measurement.setSensor(sensor.get());
        } else {
            throw new SensorException("Сенсор не найден");
        }
    }
}
