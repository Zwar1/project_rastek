package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.*;
import com.example.userCrud.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeService {


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManualAttendanceLogRepository manualAttendanceLogRepository; // Pastikan repository ini diinjeksi

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    UserRepository userRepository;

    // Method to update last attendance information (check-in, check-out, work location, latitude, longitude)
    @Transactional
    public EmployeeRes updateLastAttendance(Long NIK, LocalDateTime checkIn, LocalDateTime checkOut,
                                            String workLocation, Double latitude, Double longitude, String attendanceStatus) {
        EmployeeEntity employee = employeeRepository.findFirstByNIK(NIK)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        String latestAttendanceStatus = manualAttendanceLogRepository
                .findTopByEmployeeNIKOrderByManualCheckInDesc(NIK)
                .map(ManualAttendanceLog::getAttendanceStatus)
                .orElse("NOT CHECK IN");

        employee.setLastCheckIn(checkIn);
        employee.setLastCheckOut(checkOut);
        employee.setLastWorkLocation(workLocation);
        employee.setLastLatitude(latitude);
        employee.setLastLongitude(longitude);
        employee.setAttendanceStatus(latestAttendanceStatus);

        employeeRepository.save(employee);

        ManualAttendanceLog attendanceLog = new ManualAttendanceLog();
        attendanceLog.setEmployee(employee);
        attendanceLog.setManualCheckIn(checkIn);
        attendanceLog.setManualCheckOut(checkOut);
        attendanceLog.setAttendanceStatus(attendanceStatus);

        manualAttendanceLogRepository.save(attendanceLog);

        return toEmployeeResponse(employee);
    }


    @Scheduled(cron = "0 0 0 * * ?") // Setiap pukul 00:00
    public void resetAttendanceStatus() {
        List<EmployeeEntity> employees = employeeRepository.findAll();

        for (EmployeeEntity employee : employees) {
            // Reset data absensi karyawan pada EmployeeEntity
            employee.setLastCheckIn(null);
            employee.setLastCheckOut(null);
            employee.setAttendanceStatus("NOT CHECK IN");

            employeeRepository.save(employee);
            System.out.println("Attendance reset for employee NIK: " + employee.getNIK());
        }
    }

    // Method to generate employee number (NIK)
    public long generateEmployeeNumber(LocalDate joinDate) {
        // Ambil 2 digit terakhir dari tahun
        String year = String.valueOf(joinDate.getYear()).substring(2);

        String month = String.format("%02d", joinDate.getMonthValue());
        System.out.println("Bulan yang digunakan: " + month);

        // Hitung jumlah karyawan yang sudah mendaftar pada bulan tersebut
        LocalDate startOfMonth = joinDate.withDayOfMonth(1);
        LocalDate endOfMonth = joinDate.withDayOfMonth(joinDate.lengthOfMonth());
        long employeeCount = employeeRepository.countByJoinDateBetween(startOfMonth, endOfMonth) + 1;
        System.out.println("Employee count for " + joinDate.getMonth() + " " + joinDate.getYear() + ": " + employeeCount);

        // Format urutan karyawan menjadi 2 digit
        String employeeOrder = String.format("%02d", employeeCount);

        // Gabungkan tahun, bulan, dan urutan untuk menghasilkan NIK
        String employeeNumberString = year + month + employeeOrder;

        // Debugging: Lihat bagaimana hasil pembentukan NIK
        System.out.println("Generated NIK: " + employeeNumberString);

        // Konversikan NIK menjadi Long
        return Long.parseLong(employeeNumberString);
    }


    @Transactional
    public EmployeeRes create(EmployeeReq request) {
        validationService.validate(request);

        EmployeeEntity employee = new EmployeeEntity();
        employee.setJoinDate(request.getJoinDate());  // Assuming joinDate is part of EmployeeReq

        // Generate NIK
        Long employeeNumber = generateEmployeeNumber(employee.getJoinDate());
        employee.setNIK(employeeNumber);

        employee.setName(request.getName());
        employee.setIdCard(request.getIdCard());
        employee.setNo_ktp(request.getNo_ktp());
        employee.setNPWP(request.getNPWP());
        employee.setKartuKeluarga(request.getKartuKeluarga());
        employee.setJenisKelamin(request.getJenisKelamin());
        employee.setTempatLahir(request.getTempatLahir());
        employee.setTanggalLahir(request.getTanggalLahir());
        employee.setAgama(request.getAgama());
        employee.setAlamatLengkap(request.getAlamatLengkap());
        employee.setAlamatDomisili(request.getAlamatDomisili());
        employee.setNoTelp(request.getNoTelp());
        employee.setKontakDarurat(request.getKontakDarurat());
        employee.setNoKontakDarurat(request.getNoKontakDarurat());
        employee.setEmailPribadi(request.getEmailPribadi());
        employee.setPendidikanTerakhir(request.getPendidikanTerakhir());
        employee.setJurusan(request.getJurusan());
        employee.setNamaUniversitas(request.getNamaUniversitas());
        employee.setNamaIbuKandung(request.getNamaIbuKandung());
        employee.setStatusPernikahan(request.getStatusPernikahan());
        employee.setJumlahAnak(request.getJumlahAnak());
        employee.setNomorRekening(request.getNomorRekening());
        employee.setBank(request.getBank());
        employee.setEmployeeStatus(request.getStatus());

        employeeRepository.save(employee);

        return toEmployeeResponse(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeRes get(Long NIK) {
        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(NIK)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));
        return toEmployeeResponse(employeeEntity);
    }

    @Transactional(readOnly = true)
    public List<EmployeeRes> getAllEmployee() {
        List<EmployeeEntity> employees = employeeRepository.findAll();
        return employees.stream().map(this::toEmployeeResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getByUserAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Pastikan username yang sedang login valid
        if (currentUsername == null || currentUsername.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user is logged in");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your session expired"));

        if (user.is_deleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is deleted");
        }

        EmployeeEntity employee = user.getEmployee();
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for this user");
        }

        // Ambil status absensi terakhir dari ManualAttendanceLog berdasarkan NIK
        String latestAttendanceStatus = manualAttendanceLogRepository
                .findTopByEmployeeNIKOrderByManualCheckInDesc(employee.getNIK())
                .map(ManualAttendanceLog::getAttendanceStatus)
                .orElse("NOT CHECK IN"); // Default jika tidak ditemukan

        // Set status absensi terbaru ke employeeRes
        EmployeeRes employeeRes = toEmployeeResponse(employee);

        // Menambahkan status absensi terakhir ke employeeRes
        employeeRes.setAttendanceStatus(latestAttendanceStatus);

        // Menambahkan informasi absensi lainnya jika diperlukan
        employeeRes.setLastCheckIn(employee.getLastCheckIn());
        employeeRes.setLastCheckOut(employee.getLastCheckOut());
        employeeRes.setLastWorkLocation(employee.getLastWorkLocation());
        employeeRes.setLastLatitude(employee.getLastLatitude());
        employeeRes.setLastLongitude(employee.getLastLongitude());
        employee.setAttendanceStatus(employeeRes.getAttendanceStatus());

        // Handle null employee data
        employeeRes = user.getEmployee() != null
                ? toEmployeeResponse(user.getEmployee())
                : null;

        List<RolesSimpleRes> roles = user.getRoles().stream()
                .map(role -> new RolesSimpleRes(role.getId(), role.getName()))
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(user.getId()) // Add user ID to response
                .username(user.getUsername())
                .email(user.getEmail())
                .created_at(user.getCreatedAt())
                .updated_at(user.getUpdatedAt())
                .created_by(user.getCreated_by())
                .updated_by(user.getUpdate_by())
                .roles(roles)
                .employee(employeeRes)
                .build();
    }

    @Transactional(readOnly = true)
    public List<Long> getAllEmployeeNIKs() {
        return employeeRepository.findAll().stream()
                .map(EmployeeEntity::getNIK)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getAllEmployeeName() {
        return employeeRepository.findAll().stream()
                .map(EmployeeEntity::getName)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long NIK) {
        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(NIK)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        cvRepository.deleteByEmployee_NIK(NIK);
        employeeRepository.delete(employeeEntity);
    }

    /**
     * Reset lastCheckIn & lastCheckOut setiap hari pukul 00:01 dan update dengan data baru.
     */
    @Transactional
    @Scheduled(cron = "0 1 0 * * *") // Jalankan setiap hari pukul 00:01
    public void resetAndUpdateAttendance() {
        LocalDate today = LocalDate.now();

        // Ambil semua karyawan
        List<EmployeeEntity> employees = employeeRepository.findAll();

        for (EmployeeEntity employee : employees) {
            Long nik = employee.getNIK();

            // Ambil check-in pertama dan check-out terakhir di hari ini dari repository
            Optional<LocalDateTime> firstCheckInOpt = manualAttendanceLogRepository.findFirstCheckIn(nik, today); // Gunakan instance repository
            Optional<LocalDateTime> lastCheckOutOpt = manualAttendanceLogRepository.findLastCheckOut(nik, today); // Gunakan instance repository

            // Reset data absensi harian
            employee.setLastCheckIn(null);
            employee.setLastCheckOut(null);

            // Jika ada check-in atau check-out baru, update employee
            firstCheckInOpt.ifPresent(employee::setLastCheckIn);
            lastCheckOutOpt.ifPresent(employee::setLastCheckOut);

            // Simpan perubahan ke database
            employeeRepository.save(employee);
        }
    }


    @Transactional
    public EmployeeRes update(UpdateEmployeeReq request) {
        validationService.validate(request);

        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        if (Objects.nonNull(request.getName()) && !request.getName().isEmpty()) {
            employeeEntity.setName(request.getName());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getIdCard()) && !request.getIdCard().describeConstable().isEmpty()) {
            employeeEntity.setIdCard(request.getIdCard());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getNo_ktp()) && !request.getNo_ktp().isEmpty()) {
            employeeEntity.setNo_ktp(request.getNo_ktp());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getNPWP()) && !request.getNPWP().isEmpty()) {
            employeeEntity.setNPWP(request.getNPWP());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getKartuKeluarga()) && !request.getKartuKeluarga().isEmpty()) {
            employeeEntity.setKartuKeluarga(request.getKartuKeluarga());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getJenisKelamin()) && !request.getJenisKelamin().isEmpty()) {
            employeeEntity.setJenisKelamin(request.getJenisKelamin());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getTempatLahir()) && !request.getTempatLahir().isEmpty()) {
            employeeEntity.setTempatLahir(request.getTempatLahir());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getTanggalLahir())) {
            employeeEntity.setTanggalLahir(request.getTanggalLahir());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getAgama()) && !request.getAgama().isEmpty()) {
            employeeEntity.setAgama(request.getAgama());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getAlamatLengkap()) && !request.getAlamatLengkap().isEmpty()) {
            employeeEntity.setAlamatLengkap(request.getAlamatLengkap());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getAlamatDomisili()) && !request.getAlamatDomisili().isEmpty()) {
            employeeEntity.setAlamatDomisili(request.getAlamatDomisili());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getNoTelp()) && !request.getNoTelp().isEmpty()) {
            employeeEntity.setNoTelp(request.getNoTelp());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getKontakDarurat()) && !request.getKontakDarurat().isEmpty()) {
            employeeEntity.setKontakDarurat(request.getKontakDarurat());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getNoKontakDarurat()) && !request.getNoKontakDarurat().isEmpty()) {
            employeeEntity.setNoKontakDarurat(request.getNoKontakDarurat());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getEmailPribadi()) && !request.getEmailPribadi().isEmpty()) {
            employeeEntity.setEmailPribadi(request.getEmailPribadi());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getPendidikanTerakhir()) && !request.getPendidikanTerakhir().isEmpty()) {
            employeeEntity.setPendidikanTerakhir(request.getPendidikanTerakhir());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getJurusan()) && !request.getJurusan().isEmpty()) {
            employeeEntity.setJurusan(request.getJurusan());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getNamaUniversitas()) && !request.getNamaUniversitas().isEmpty()) {
            employeeEntity.setNamaUniversitas(request.getNamaUniversitas());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getNamaIbuKandung()) && !request.getNamaIbuKandung().isEmpty()) {
            employeeEntity.setNamaIbuKandung(request.getNamaIbuKandung());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getStatusPernikahan()) && !request.getStatusPernikahan().isEmpty()) {
            employeeEntity.setStatusPernikahan(request.getStatusPernikahan());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getJumlahAnak()) && !request.getJumlahAnak().isEmpty()) {
            employeeEntity.setJumlahAnak(request.getJumlahAnak());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getNomorRekening()) && !request.getNomorRekening().isEmpty()) {
            employeeEntity.setNomorRekening(request.getNomorRekening());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getBank()) && !request.getBank().isEmpty()) {
            employeeEntity.setBank(request.getBank());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getStatus())) {
            employeeEntity.setEmployeeStatus(request.getStatus());
            employeeRepository.save(employeeEntity);
        }


        return toEmployeeResponse(employeeEntity);
    }

    // Update data employee only level
    @Transactional
    public EmployeeRes updateDataByEmployee(UpdateEmployeeKaryawanReq request) {
        validationService.validate(request);

        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        if (Objects.nonNull(request.getNoTelp()) && !request.getNoTelp().isEmpty()) {
            employeeEntity.setNoTelp(request.getNoTelp());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getKontakDarurat()) && !request.getKontakDarurat().isEmpty()) {
            employeeEntity.setKontakDarurat(request.getKontakDarurat());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getNoKontakDarurat()) && !request.getNoKontakDarurat().isEmpty()) {
            employeeEntity.setNoKontakDarurat(request.getNoKontakDarurat());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getAlamatDomisili()) && !request.getAlamatDomisili().isEmpty()) {
            employeeEntity.setAlamatDomisili(request.getAlamatDomisili());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getStatusPernikahan()) && !request.getStatusPernikahan().isEmpty()) {
            employeeEntity.setStatusPernikahan(request.getStatusPernikahan());
            employeeRepository.save(employeeEntity);
        }
        if (Objects.nonNull(request.getJumlahAnak()) && !request.getJumlahAnak().isEmpty()) {
            employeeEntity.setJumlahAnak(request.getJumlahAnak());
            employeeRepository.save(employeeEntity);
        }

        return toEmployeeResponse(employeeEntity);
    }

    @Transactional(readOnly = true)
    public List<EmployeeRes> getAllEmployeeWithInfo() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();

        return employeeEntities.stream()
                .map(this::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeRes getLastCreatedEmployee() {
        EmployeeEntity employeeEntity = employeeRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No employees found"));
        return toEmployeeResponse(employeeEntity);
    }

    @Transactional(readOnly = true)
    public List<EmployeeRes> getEmployeeWithJPU() {
        // Ambil semua data karyawan dari database
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();

        // Urutkan berdasarkan sequence kodeJabatan dan joinDate
        employeeEntities.sort(Comparator.comparing((EmployeeEntity e) -> e.getRiwayatJabatan().stream()
                        .map(r -> r.getId_jabatan().getSequence())
                        .min(Long::compareTo) // Ambil sequence terkecil jika karyawan memiliki beberapa riwayat jabatan
                        .orElse(Long.MAX_VALUE))
                .thenComparing(EmployeeEntity::getTanggalLahir));

        // Konversi hasil menjadi response DTO
        return employeeEntities.stream()
                .map(this::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    private EmployeeRes toEmployeeResponse(EmployeeEntity employeeEntity) {
        // Early return if employee is null
        if (employeeEntity == null) {
            return null;
        }

        List<RiwayatJabatanRes> riwayatJabatanResList = employeeEntity.getRiwayatJabatan().stream()
                .map(riwayatJabatan -> RiwayatJabatanRes.builder()
                        .id_riwayat(riwayatJabatan.getId())
                        .statusKontrak(riwayatJabatan.getStatusKontrak())
                        .tmt_awal(riwayatJabatan.getTmt_mulai())
                        .tmt_akhir(riwayatJabatan.getTmt_akhir())
                        .kontrakKedua(riwayatJabatan.getKontrakKedua())
                        .salary(riwayatJabatan.getSalary())
                        .kodeJabatan(JabatanRes.builder()
                                .kodeJabatan(riwayatJabatan.getId_jabatan().getKodeJabatan())
                                .namaJabatan(riwayatJabatan.getId_jabatan().getNamaJabatan())
                                .isAtasan(riwayatJabatan.getId_jabatan().isAtasan())
                                .sequence(riwayatJabatan.getId_jabatan().getSequence())
                                .departement(riwayatJabatan.getId_jabatan().getDepartementEntity() != null
                                        ? riwayatJabatan.getId_jabatan().getDepartementEntity().getDepartement_name() : null)
                                .division(riwayatJabatan.getId_jabatan().getDivisionEntity() != null
                                        ? riwayatJabatan.getId_jabatan().getDivisionEntity().getDivision_name() : null)
                                .build())
                        .build())
                .toList();

        // Handle Attachment mapping with null checks
        List<AttachmentRes> attachmentResList = Optional.ofNullable(employeeEntity.getAttachment())
                .map(attachments -> attachments.stream()
                        .map(attachment -> AttachmentRes.builder()
                                .attachment(attachment.getAttachment())
                                .build())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        // Handle CV mapping with null checks
        List<CVRes> cvResList = Optional.ofNullable(employeeEntity.getCv())
                .map(cvList -> cvList.stream()
                        .map(cv -> CVRes.builder()
                                .id(cv.getId())
                                .projectName(cv.getProjectName())
                                .projectRole(cv.getProjectRole())
                                .projectStart(cv.getProjectStart())
                                .projectEnd(cv.getProjectEnd())
                                .projectDescription(cv.getProjectDescription())
                                .build())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        // Build final response with null-safe accessors
        return EmployeeRes.builder()
                .NIK(employeeEntity.getNIK())
                .name(employeeEntity.getName())
                .idCard(employeeEntity.getIdCard())
                .no_ktp(employeeEntity.getNo_ktp())
                .NPWP(employeeEntity.getNPWP())
                .kartuKeluarga(employeeEntity.getKartuKeluarga())
                .jenisKelamin(employeeEntity.getJenisKelamin())
                .tempatLahir(employeeEntity.getTempatLahir())
                .tanggalLahir(employeeEntity.getTanggalLahir())
                .agama(employeeEntity.getAgama())
                .alamatLengkap(employeeEntity.getAlamatLengkap())
                .alamatDomisili(employeeEntity.getAlamatDomisili())
                .noTelp(employeeEntity.getNoTelp())
                .kontakDarurat(employeeEntity.getKontakDarurat())
                .noKontakDarurat(employeeEntity.getNoKontakDarurat())
                .emailPribadi(employeeEntity.getEmailPribadi())
                .pendidikanTerakhir(employeeEntity.getPendidikanTerakhir())
                .jurusan(employeeEntity.getJurusan())
                .namaUniversitas(employeeEntity.getNamaUniversitas())
                .namaIbuKandung(employeeEntity.getNamaIbuKandung())
                .statusPernikahan(employeeEntity.getStatusPernikahan())
                .jumlahAnak(employeeEntity.getJumlahAnak())
                .nomorRekening(employeeEntity.getNomorRekening())
                .bank(employeeEntity.getBank())
                .joinDate(employeeEntity.getJoinDate())
                .status(employeeEntity.getEmployeeStatus())
                .created_at(employeeEntity.getCreatedAt())
                .updated_at(employeeEntity.getUpdatedAt())
                .userProfile(Optional.ofNullable(employeeEntity.getUserProfile())
                        .map(UserProfile::getProfilePicture)
                        .orElse(null))
                .riwayatJabatan(riwayatJabatanResList)
                .attachment(attachmentResList)
                .cv(cvResList)
                .build();
    }
}
