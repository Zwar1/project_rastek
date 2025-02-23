    package com.example.userCrud.Service;

    import com.example.userCrud.Dto.ManualAttendanceLogResponse;
    import com.example.userCrud.Entity.ManualAttendanceLog;
    import com.example.userCrud.Entity.EmployeeEntity;
    import com.example.userCrud.Repository.ManualAttendanceLogRepository;
    import com.example.userCrud.Repository.EmployeeRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import jakarta.annotation.PostConstruct;
    import java.time.*;
    import java.time.temporal.ChronoUnit;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    public class ManualAttendanceLogService {

        @Autowired
        private ManualAttendanceLogRepository manualAttendanceLogRepository;

        @Autowired
        private EmployeeRepository employeeRepository;

        private static final LocalTime START_WORK_TIME = LocalTime.of(9, 0);
        private static final LocalTime LATE_THRESHOLD = LocalTime.of(10, 0);
        private static final LocalTime CHECKOUT_LIMIT = LocalTime.of(17, 0);

        @PostConstruct
        public void init() {
            initializeAttendanceLog();
        }

        @Transactional
        public void initializeAttendanceLog() {
            List<EmployeeEntity> employees = employeeRepository.findAll();
            LocalTime CHECKOUT_LIMIT = LocalTime.of(17, 0); // Batas waktu jam 5 sore
            LocalDate today = LocalDate.now();

            for (EmployeeEntity employee : employees) {
                boolean exists = manualAttendanceLogRepository.existsByEmployeeNIK(employee.getNIK());

                if (!exists) {
                    System.out.println("Processing Employee NIK: " + employee.getNIK());
                    System.out.println("Employee Name: " + employee.getName());

                    // Cek apakah karyawan sudah melakukan check-in hari ini
                    Optional<ManualAttendanceLog> existingLogOpt = manualAttendanceLogRepository.findFirstByEmployeeNIKAndManualCheckInAfter(
                            employee.getNIK(), today.atStartOfDay());

                    // Jika tidak ada check-in, buat log dengan status "NOT CHECKED IN"
                    if (existingLogOpt.isEmpty()) {
                        ManualAttendanceLog log = new ManualAttendanceLog();
                        log.setEmployee(employee);
                        log.setEmployeeName(employee.getName()); // Pastikan tidak null
                        log.setManualCheckIn(null);
                        log.setManualCheckOut(null);
                        log.setManualTotalHours(null);
                        log.setWorkLocation(null);
                        log.setLatitude(null);
                        log.setLongitude(null);
                        log.setAttendanceStatus("NOT CHECK IN");

                        manualAttendanceLogRepository.save(log);
                        System.out.println("Log saved with 'NOT CHECK IN' status for NIK: " + employee.getNIK());
                    }

                    // Cek apakah sudah melewati jam 5 sore dan karyawan belum check-in
                    LocalTime currentTime = LocalTime.now();
                    if (currentTime.isAfter(CHECKOUT_LIMIT)) {
                        if (existingLogOpt.isEmpty()) {
                            ManualAttendanceLog absentLog = new ManualAttendanceLog();
                            absentLog.setEmployee(employee);
                            absentLog.setEmployeeName(employee.getName());
                            absentLog.setManualCheckIn(null);
                            absentLog.setManualCheckOut(null);
                            absentLog.setManualTotalHours(null);
                            absentLog.setWorkLocation(null);
                            absentLog.setLatitude(null);
                            absentLog.setLongitude(null);
                            absentLog.setAttendanceStatus("ABSENT");

                            manualAttendanceLogRepository.save(absentLog);
                            System.out.println("Log saved with 'ABSENT' status for NIK: " + employee.getNIK());
                        }
                    }
                }
            }
        }





        @Transactional
        public ManualAttendanceLogResponse manualCheckIn(Long nik, String workLocation, Double latitude, Double longitude) {
            EmployeeEntity employee = employeeRepository.findById(nik)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with NIK: " + nik));

            LocalDateTime now = LocalDateTime.now();
            LocalDate today = now.toLocalDate();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            LocalTime currentTime = now.toLocalTime();

            if (isBreakTime(dayOfWeek, currentTime)) {
                throw new IllegalStateException("Tidak bisa check-in selama jam istirahat.");
            }

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                throw new IllegalStateException("Tidak bisa melakukan absensi pada hari Sabtu dan Minggu.");
            }

            ManualAttendanceLog existingLog = manualAttendanceLogRepository
                    .findFirstByEmployeeNIKAndManualCheckInIsNotNullAndManualCheckOutIsNullAndManualCheckInAfter(nik, today.atStartOfDay())
                    .orElse(null);

            if (existingLog != null) {
                throw new IllegalStateException("Employee has already checked in and not checked out yet.");
            }

            LocalDateTime checkInTime = now.toLocalTime().isBefore(START_WORK_TIME) ? LocalDateTime.of(today, START_WORK_TIME) : now;

            // Simpan di tabel ManualAttendanceLog
            ManualAttendanceLog log = new ManualAttendanceLog();
            log.setEmployee(employee);
            log.setManualCheckIn(checkInTime);
            log.setWorkLocation(workLocation);
            log.setLatitude(latitude);
            log.setLongitude(longitude);
            log.setManualTotalHours(null);
            log.setAttendanceStatus(currentTime.isAfter(LATE_THRESHOLD) ? "LATE" : "PRESENT");

            manualAttendanceLogRepository.save(log);

            // Simpan juga di tabel EmployeeEntity
            employee.setLastCheckIn(checkInTime);
            employee.setLastWorkLocation(workLocation);
            employee.setLastLatitude(latitude);
            employee.setLastLongitude(longitude);

            employeeRepository.save(employee);

            return convertToResponse(log);
        }

        @Transactional
        public ManualAttendanceLogResponse manualCheckOut(Long nik) {
            ManualAttendanceLog log = manualAttendanceLogRepository
                    .findFirstByEmployeeNIKAndManualCheckInIsNotNullAndManualCheckOutIsNullAndManualCheckInAfter(nik, LocalDate.now().atStartOfDay())
                    .orElseThrow(() -> new IllegalArgumentException("No active attendance log found for NIK: " + nik));

            LocalDateTime checkOutTime = LocalDateTime.now();

            if (log.getManualCheckOut() != null) {
                throw new IllegalStateException("Employee has already checked out.");
            }

            if (checkOutTime.toLocalTime().isBefore(CHECKOUT_LIMIT)) {
                log.setAttendanceStatus("IZIN");
            } else if (!"LATE".equals(log.getAttendanceStatus())) {
                log.setAttendanceStatus("PRESENT");
            }

            log.setManualCheckOut(checkOutTime);
            log.setManualTotalHours(calculateTotalHours(log.getManualCheckIn(), checkOutTime));

            manualAttendanceLogRepository.save(log);

            // Simpan juga di tabel EmployeeEntity
            EmployeeEntity employee = log.getEmployee();
            employee.setLastCheckOut(checkOutTime);
            employeeRepository.save(employee);

            return convertToResponse(log);
        }

        private boolean isBreakTime(DayOfWeek dayOfWeek, LocalTime currentTime) {
            LocalTime breakStart = LocalTime.of(12, 0);
            LocalTime breakEnd = LocalTime.of(13, 0);

            if (dayOfWeek == DayOfWeek.FRIDAY) {
                breakStart = LocalTime.of(11, 30);
                breakEnd = LocalTime.of(13, 0);
            }

            return !currentTime.isBefore(breakStart) && !currentTime.isAfter(breakEnd);
        }

        private String calculateTotalHours(LocalDateTime checkIn, LocalDateTime checkOut) {
            DayOfWeek dayOfWeek = checkIn.getDayOfWeek();
            LocalTime checkInTime = checkIn.toLocalTime();
            LocalTime checkOutTime = checkOut.toLocalTime();
            long totalMinutes = 0;

            if ((dayOfWeek == DayOfWeek.FRIDAY && checkInTime.isAfter(LocalTime.of(14, 0))) ||
                    (dayOfWeek != DayOfWeek.FRIDAY && checkInTime.isAfter(LocalTime.of(13, 0)))) {
                totalMinutes = ChronoUnit.MINUTES.between(checkIn, checkOut);
            } else {
                if (checkInTime.isBefore(LocalTime.of(12, 0))) {
                    LocalTime morningEnd = LocalTime.of(12, 0);
                    LocalTime morningStart = checkInTime.isBefore(LocalTime.of(9, 0)) ? LocalTime.of(9, 0) : checkInTime;
                    LocalTime validEnd = checkOutTime.isBefore(morningEnd) ? checkOutTime : morningEnd;
                    totalMinutes += ChronoUnit.MINUTES.between(morningStart, validEnd);
                }

                LocalTime afternoonStart = (dayOfWeek == DayOfWeek.FRIDAY) ? LocalTime.of(15, 0) : LocalTime.of(13, 0);
                LocalTime afternoonEnd = LocalTime.of(17, 0);

                if (checkOutTime.isAfter(afternoonStart)) {
                    LocalTime validStart = checkInTime.isAfter(afternoonStart) ? checkInTime : afternoonStart;
                    LocalTime validEnd = checkOutTime.isBefore(afternoonEnd) ? checkOutTime : afternoonEnd;
                    totalMinutes += ChronoUnit.MINUTES.between(validStart, validEnd);
                }
            }

            return formatHoursAndMinutes(totalMinutes);
        }

        private String formatHoursAndMinutes(long totalMinutes) {
            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;
            return hours + " Jam " + minutes + " Menit";
        }
        // 🔹 Mendapatkan semua log absensi berdasarkan NIK
        public List<ManualAttendanceLogResponse> getAllAttendanceLogsByNIK(Long nik) {
            List<ManualAttendanceLog> logs = manualAttendanceLogRepository.findByEmployeeNIK(nik);

            if (logs.isEmpty()) {
                throw new IllegalArgumentException("Tidak ada data absensi untuk NIK: " + nik);
            }

            return logs.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }
            // 🔹 Mendapatkan semua log absensi tanpa filter tanggal
            public List<ManualAttendanceLogResponse> getPureAllAttendanceLogs() {
                List<ManualAttendanceLog> logs = manualAttendanceLogRepository.findAll();

                return logs.stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());
            }
        // 🔹 Mendapatkan log absensi dalam rentang tanggal tertentu berdasarkan NIK
        public List<ManualAttendanceLogResponse> getAttendanceLogs(Long nik, LocalDate startDate, LocalDate endDate) {
            List<ManualAttendanceLog> logs = manualAttendanceLogRepository
                    .findByEmployeeNIKAndManualCheckInBetween(nik, startDate.atStartOfDay(), endDate.atTime(23, 59));

            return logs.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }

        // 🔹 Mendapatkan semua log absensi dalam rentang tanggal tertentu
        public List<ManualAttendanceLogResponse> getAllAttendanceLogsInRange(LocalDate startDate, LocalDate endDate) {
            List<ManualAttendanceLog> logs = manualAttendanceLogRepository
                    .findByManualCheckInBetween(startDate.atStartOfDay(), endDate.atTime(23, 59));

            return logs.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }

        // 🔹 Mendapatkan log absensi hari ini
        public List<ManualAttendanceLogResponse> getTodayAttendanceLogs() {
            LocalDate today = LocalDate.now();
            List<ManualAttendanceLog> logs = manualAttendanceLogRepository
                    .findByManualCheckInBetween(today.atStartOfDay(), today.atTime(23, 59));

            return logs.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }

        // 🔹 Konversi `ManualAttendanceLog` ke `ManualAttendanceLogResponse`
        private ManualAttendanceLogResponse convertToResponse(ManualAttendanceLog log) {
            return new ManualAttendanceLogResponse(
                    log.getEmployee().getIdCard(),
                    log.getEmployee().getNIK(),
                    log.getEmployee().getName(),
                    log.getManualCheckIn(),
                    log.getManualCheckOut(),
                    log.getAttendanceStatus(),
                    log.getManualTotalHours(),
                    log.getWorkLocation(),
                    log.getLatitude(),
                    log.getLongitude()
            );
        }
    }
