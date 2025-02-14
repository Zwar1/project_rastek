package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CompanyCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CompanyCalendarRepository extends JpaRepository<CompanyCalendar, Long> {
    CompanyCalendar findFirstById(Long id);

    boolean existsByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);
}
