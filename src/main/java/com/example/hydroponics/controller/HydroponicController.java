package com.example.hydroponics.controller;


import com.example.hydroponics.sensor.HumiditySensor;
import com.example.hydroponics.sensor.Sensor;
import com.example.hydroponics.sensor.TempSensor;
import com.example.hydroponics.sensor.TimeSensor;
import com.example.hydroponics.service.HydroponicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hydroponic")
public class HydroponicController {
    private final HydroponicService hydroponicService; // di

    @Autowired
    public HydroponicController(HydroponicService hydroponicService) {
        this.hydroponicService = hydroponicService;
    }
    @GetMapping("/checkSensors")
    public ResponseEntity<String> checkSensors() throws IOException {
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(new TempSensor());
        sensors.add(new HumiditySensor());
        sensors.add(new TimeSensor());
        hydroponicService.checkSensors(sensors);
        return ResponseEntity.ok("Ok");
    }
}
