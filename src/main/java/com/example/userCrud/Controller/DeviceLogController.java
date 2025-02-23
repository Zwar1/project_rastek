//package com.example.userCrud.Controller;
//
//import com.example.userCrud.Dto.DeviceLogResponse;
//import com.example.userCrud.Service.DeviceLogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/device-log")
//public class DeviceLogController {
//
//    @Autowired
//    private DeviceLogService deviceLogService;
//
//    // Endpoint to fetch logs from the device by PIN
//    @GetMapping("/get-logs/{pin}")
//    public List<DeviceLogResponse> getLogs(@PathVariable String pin) {
//        // Fetch logs from the device based on the PIN
//        return deviceLogService.getLogsFromDevice(pin);
//    }
//
//    // Endpoint to fetch all device logs from the database
//    @GetMapping("/get-all-logs")
//    public List<DeviceLogResponse> getAllLogs() {
//        return deviceLogService.getAllDeviceLogs();
//    }
//}
