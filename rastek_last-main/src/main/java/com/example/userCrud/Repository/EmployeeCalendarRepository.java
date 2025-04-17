package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CompanyCalendar;
import com.example.userCrud.Entity.EmployeeCalendar;
import com.example.userCrud.Entity.EmployeeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EmployeeCalendarRepository extends JpaRepository<EmployeeCalendar, Long> {
    CompanyCalendar findFirstById(Long id);
    Optional<EmployeeCalendar> findByEmployeeNIK(Long nik);;

    boolean existsByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);
}
