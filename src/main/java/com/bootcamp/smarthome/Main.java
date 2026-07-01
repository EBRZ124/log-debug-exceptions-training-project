package com.bootcamp.smarthome;

import com.bootcamp.smarthome.controller.HomeController;
import com.bootcamp.smarthome.device.Device;
import com.bootcamp.smarthome.device.SmartLight;
import com.bootcamp.smarthome.device.SmartLock;
import com.bootcamp.smarthome.device.SmartThermostat;
import com.bootcamp.smarthome.exception.HomeAutomationException;
import com.bootcamp.smarthome.exception.InvalidValueException;
import com.bootcamp.smarthome.exception.InvalidCommandException;
/**
 * Entry point for the Smart Home Controller demo.
 *
 * Registers several smart devices, then runs a series of scenarios that
 * exercise normal operation as well as edge cases and error conditions.
 */
public class Main {

    public static void main(String[] args) {

        HomeController homeController = new HomeController();

        System.out.println("=== Setting up devices ===");
        homeController.addDevice(new SmartLight("LIGHT_01",   "Living Room Light",  true));
        homeController.addDevice(new SmartLight("LIGHT_02",   "Bedroom Light",      true));
        homeController.addDevice(new SmartThermostat("THERMO_01", "Main Thermostat", true));
        homeController.addDevice(new SmartLock("LOCK_01",    "Front Door Lock",    true,  "4321"));
        homeController.addDevice(new SmartLight("LIGHT_03",   "Kitchen Light",      false)); // offline
        homeController.addDevice(new SmartThermostat("THERMO_02", "Bedroom Thermostat", true));
        homeController.addDevice(new SmartLight("LIGHT_04",   "Hallway Light",      true));
        homeController.addDevice(new SmartLock("LOCK_02",    "Back Door Lock",     true,  "9999"));
        // Array is now FULL (8/8 devices)

        homeController.printAllDevices();

        System.out.println("\n=== Scenario 1: Normal device commands ===");
        try {
            homeController.sendCommand("LIGHT_01 TURN_ON");
            homeController.sendCommand("LIGHT_02 TURN_ON");
            homeController.sendCommand("THERMO_01 SET_TEMP 22.5");
        } catch (HomeAutomationException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Scenario 2: Set brightness ===");
        try {
            homeController.sendCommand("LIGHT_01 SET_BRIGHTNESS 80");
        } catch (HomeAutomationException e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Scenario 3: Invalid temperature ===");
        // This test calls setTemperature() directly to isolate temperature validation.
        try {
            Device found = homeController.findDevice("THERMO_01");
            SmartThermostat mainThermostat = (SmartThermostat) found;
            mainThermostat.setTemperature(99.0);
        } catch (InvalidValueException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Scenario 4: Offline device ===");
        // LIGHT_03 is offline — command should be skipped with a warning
        try {
            homeController.sendCommand("LIGHT_03 TURN_ON");
        } catch (HomeAutomationException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Scenario 5: Unlock with correct PIN ===");
        // Direct call to validatePin() to demonstrate intended correct behaviour
        // (going through sendCommand() would strip the PIN via BUG-LG-2).
        try {
            Device foundLock = homeController.findDevice("LOCK_01");
            SmartLock frontDoor = (SmartLock) foundLock;
            frontDoor.validatePin("4321");
        } catch (InvalidCommandException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Scenario 6: Unlock with null PIN ===");
        try {
            homeController.sendCommand("LOCK_02 UNLOCK");
        } catch (HomeAutomationException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== Scenario 7: Find non-existent device ===");
        try {
            homeController.sendCommand("SENSOR_99 TURN_ON");
        } catch (HomeAutomationException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== All scenarios complete ===");
        homeController.printAllDevices();
    }
}
