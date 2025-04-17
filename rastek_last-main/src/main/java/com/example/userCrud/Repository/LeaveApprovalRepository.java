package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CompanyLeave;
import com.example.userCrud.Entity.EmployeeAnnual;
import com.example.userCrud.Entity.LeaveApproval;
import com.example.userCrud.Entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    Optional<LeaveApproval> findFirstById(Long id);

    List<LeaveApproval> findByLeaveRequest(LeaveRequest leaveRequest);

    List<LeaveApproval> findByLeaveRequestAndApprover_NIK(LeaveRequest leaveRequest, Long approverNIK);

    // Mencari semua LeaveRequest di mana employee adalah approver
    @Query("SELECT la.leaveRequest FROM LeaveApproval la WHERE la.approver.NIK = :nik")
    List<LeaveRequest> findLeaveRequestsByApprover(@Param("nik") Long nik);
}
