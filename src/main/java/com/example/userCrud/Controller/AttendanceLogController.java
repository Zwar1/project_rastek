//package com.example.userCrud.Controller;
//import com.example.userCrud.Service.AttendanceLogService;
//import com.example.userCrud.Controller.AttendanceLogController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class AttendanceLogController {
//
//    private final AttendanceLogService attendanceLogService;
//
//    @Autowired
//    public AttendanceLogController(AttendanceLogService attendanceLogService) {
//        this.attendanceLogService = attendanceLogService;
//    }
//
//    @GetMapping(path = "attendance")
//    public List<Map<String, Object>> getAttendanceLogs() {
//        try {
//            return attendanceLogService.getAttendanceLogs();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return List.of(Map.of("status", "error", "message", "Unable to fetch attendance logs"));
//        }
//    }
//}
