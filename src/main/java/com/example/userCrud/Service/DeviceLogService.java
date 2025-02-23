//package com.example.userCrud.Service;
//
//import com.example.userCrud.Dto.DeviceLogResponse;
//import com.example.userCrud.Entity.DeviceLog;
//import com.example.userCrud.Repository.DeviceLogRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class DeviceLogService {
//
//    @Autowired
//    private DeviceLogRepository deviceLogRepository;
//
//    private String deviceIp = "192.168.12.2";  // Ganti sesuai dengan IP perangkat
//    private String key = "123456";  // Ganti sesuai dengan key perangkat
//
//    // Method to fetch logs from the device
//    public List<DeviceLogResponse> getLogsFromDevice(String pin) {
//        String soapRequest = String.format(
//                "<GetAttLog>" +
//                        "<ArgComKey xsi:type=\"xsd:integer\">%s</ArgComKey>" +
//                        "<Arg><PIN xsi:type=\"xsd:integer\">%s</PIN></Arg>" +
//                        "</GetAttLog>", key, pin.equals("All") ? "All" : pin
//        );
//
//        // Send SOAP request and get response
//        RestTemplate restTemplate = new RestTemplate();
//        // Implement SOAP call (you would need to integrate actual SOAP call logic here)
//        String response = ""; // response from the SOAP call (SOAP response would need to be set here)
//
//        // Parse and save the device logs from the response
//        return parseAndSaveDeviceLogResponse(response, pin);
//    }
//
//    // Method to parse SOAP response and save logs to the database
//    public List<DeviceLogResponse> parseAndSaveDeviceLogResponse(String response, String pin) {
//        List<DeviceLogResponse> deviceLogResponses = new ArrayList<>();
//
//        // Implement logic to parse the SOAP response string
//        String[] logEntries = response.split("\r\n");  // Split the response into entries
//        for (String entry : logEntries) {
//            String status = parseData(entry, "<Status>", "</Status>");
//            String datetime = parseData(entry, "<DateTime>", "</DateTime>");
//            String parsedPin = parseData(entry, "<PIN>", "</PIN>");
//
//            if (parsedPin.equals(pin) || pin.equals("All")) {
//                // Convert String datetime to LocalDateTime
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                LocalDateTime parsedDateTime = LocalDateTime.parse(datetime, formatter);
//
//                // Create DeviceLog entity for each entry
//                DeviceLog deviceLog = new DeviceLog();
//                deviceLog.setPin(parsedPin);
//                deviceLog.setAttendanceStatus(status);  // Status kehadiran
//                deviceLog.setCheckIn(parsedDateTime);   // Use parsed LocalDateTime for check-in time
//                deviceLog.setCheckOut(parsedDateTime);  // For now, using the same datetime for check-out (adjust if needed)
//                deviceLog.setTotalHours(0.0);  // Placeholder, replace with real total hours calculation if needed
//
//                // Save to database using DeviceLogRepository
//                deviceLogRepository.save(deviceLog);  // Save each log entry to the database
//
//                // Create DeviceLogResponse for each entry
//                DeviceLogResponse deviceLogResponse = new DeviceLogResponse();
//                deviceLogResponse.setPin(parsedPin);
//                deviceLogResponse.setAttendanceStatus(status);  // Set attendance status
//                deviceLogResponse.setCheckIn(parsedDateTime);   // Set check-in time
//                deviceLogResponse.setCheckOut(parsedDateTime);  // Set check-out time (adjust if needed)
//
//                deviceLogResponses.add(deviceLogResponse);  // Add the response DTO to the list
//            }
//        }
//
//        return deviceLogResponses;  // Return the list of device log responses
//    }
//
//    // Utility method to extract data from XML response
//    private String parseData(String data, String startTag, String endTag) {
//        String result = "";
//        int start = data.indexOf(startTag);
//        if (start != -1) {
//            int end = data.indexOf(endTag, start);
//            if (end != -1) {
//                result = data.substring(start + startTag.length(), end);
//            }
//        }
//        return result;
//    }
//
//    // Method to fetch all logs from the database and return as DeviceLogResponse
//    public List<DeviceLogResponse> getAllDeviceLogs() {
//        List<DeviceLog> deviceLogs = deviceLogRepository.findAll();
//        List<DeviceLogResponse> deviceLogResponses = new ArrayList<>();
//        for (DeviceLog deviceLog : deviceLogs) {
//            DeviceLogResponse deviceLogResponse = new DeviceLogResponse();
//            deviceLogResponse.setPin(deviceLog.getPin());
//            deviceLogResponse.setAttendanceStatus(deviceLog.getAttendanceStatus());
//            deviceLogResponse.setCheckIn(deviceLog.getCheckIn());  // Assuming check-in time is saved correctly
//            deviceLogResponse.setCheckOut(deviceLog.getCheckOut());  // Assuming check-out time is saved correctly
//            deviceLogResponses.add(deviceLogResponse);
//        }
//        return deviceLogResponses;
//    }
//}
