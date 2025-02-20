package com.example.userCrud.Service;

import com.example.userCrud.Dto.LeaveEvidenceReq;
import com.example.userCrud.Dto.LeaveEvidenceRes;
import com.example.userCrud.Entity.LeaveEvidence;
import com.example.userCrud.Entity.LeaveRequest;
import com.example.userCrud.Repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LeaveEvidenceService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private ImageStoreService imageStoreService;

    @Transactional
    public LeaveEvidenceRes addEvidence(LeaveEvidenceReq request){
        validationService.validate(request);

        LeaveRequest leaveRequest = leaveRequestRepository.findFirstById(request.getRequestId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request Not Found"));

        String evidences = imageStoreService.uploadEvidence(request.getEvidence());

        LeaveEvidence evidence = new LeaveEvidence();

        evidence.setEvidence(evidences);

        leaveRequest.getEvidences().add(evidence);

        return LeaveEvidenceRes.builder()
                .evidence(evidence.getEvidence())
                .build();
    }
}
