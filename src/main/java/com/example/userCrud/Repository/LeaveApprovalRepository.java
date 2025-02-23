package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CompanyLeave;
import com.example.userCrud.Entity.EmployeeAnnual;
import com.example.userCrud.Entity.LeaveApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    Optional<LeaveApproval> findFirstById(Long id);
}
