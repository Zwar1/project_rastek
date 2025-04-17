package com.example.userCrud.Repository;

import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    Optional<LeaveRequest> findFirstById(Long id);

    List<LeaveRequest> findByEmployee(EmployeeEntity employee);
}
