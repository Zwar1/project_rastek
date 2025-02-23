//package com.example.userCrud.Service;
//
//import com.example.userCrud.Entity.AttendanceLog;
//import com.example.userCrud.Repository.AttendanceLogRepository;
//import jakarta.xml.soap.*;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class AttendanceService {
//
//    private static final String DEVICE_IP = "192.168.12.2";  // IP Mesin Fingerprint
//    private static final String DEVICE_KEY = "123456";       // Key Mesin Fingerprint
//    private static final String SOAP_URL = "http://" + DEVICE_IP + "/iWsService";
//
//    private final AttendanceLogRepository attendanceLogRepository;
//
//    public AttendanceService(AttendanceLogRepository attendanceLogRepository) {
//        this.attendanceLogRepository = attendanceLogRepository;
//    }
//
//    /**
//     * Mengambil data absensi dari mesin fingerprint dan menyimpannya ke database.
//     */
//    public List<AttendanceLog> fetchAttendanceLogs() throws SOAPException, IOException {
//        SOAPMessage request = createSoapRequest();
//        SOAPMessage response = sendSoapRequest(request);
//        return parseSoapResponse(response);
//    }
//
//    /**
//     * Membuat request SOAP untuk mengambil log absensi.
//     */
//    private SOAPMessage createSoapRequest() throws SOAPException {
//        MessageFactory messageFactory = MessageFactory.newInstance();
//        SOAPMessage soapMessage = messageFactory.createMessage();
//        SOAPPart soapPart = soapMessage.getSOAPPart();
//
//        SOAPEnvelope envelope = soapPart.getEnvelope();
//        SOAPBody body = envelope.getBody();
//
//        SOAPElement getAttLog = body.addChildElement("GetAttLog");
//        SOAPElement argComKey = getAttLog.addChildElement("ArgComKey");
//        argComKey.addTextNode(DEVICE_KEY);
//
//        SOAPElement arg = getAttLog.addChildElement("Arg");
//        SOAPElement pin = arg.addChildElement("PIN");
//        pin.addTextNode("All");
//
//        soapMessage.saveChanges();
//        return soapMessage;
//    }
//
//    /**
//     * Mengirim request SOAP ke mesin fingerprint.
//     */
//    private SOAPMessage sendSoapRequest(SOAPMessage soapMessage) throws SOAPException, IOException {
//        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
//        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
//        SOAPMessage response = soapConnection.call(soapMessage, SOAP_URL);
//        soapConnection.close();
//        return response;
//    }
//
//    /**
//     * Memproses respons SOAP dan menyimpan log absensi ke database.
//     */
//    private List<AttendanceLog> parseSoapResponse(SOAPMessage response) throws SOAPException, IOException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        response.writeTo(outputStream);
//        String rawResponse = outputStream.toString();
//
//        List<AttendanceLog> logs = new ArrayList<>();
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        String[] entries = rawResponse.split("<Row>");
//        for (String entry : entries) {
//            String pin = extractData(entry, "<PIN>", "</PIN>");
//            String datetime = extractData(entry, "<DateTime>", "</DateTime>");
//            String status = extractData(entry, "<Status>", "</Status>");
//
//            if (!pin.isEmpty() && !datetime.isEmpty() &&
