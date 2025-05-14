package com.example.userCrud.Service;

import com.example.userCrud.Entity.PermissionsEntity;
import com.example.userCrud.Repository.PermissionsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionInitializer {

    @Autowired
    private PermissionsRepository permissionsRepository;

    @PostConstruct
    public void initi() {
         // Employees Permissions
         createOrUpdatePermission("Employees", "View", "Get Employee Data");
         createOrUpdatePermission("Employees", "Detailed","View", "Get Employee Data With Details");
         createOrUpdatePermission("Employees", "All","View", "View Employee List");
         createOrUpdatePermission("Employees", "Self", "View", "View Own Details");
         createOrUpdatePermission("Employees", "Add", "Add New Employee");
         createOrUpdatePermission("Employees", "Edit", "Edit Employee Details");
         createOrUpdatePermission("Employees", "Delete", "Delete Employee");
         createOrUpdatePermission("Employees", "CV", "View", "View Employee CV");
         createOrUpdatePermission("Employees", "CV", "All", "Create, Read, Update, Delete Employee CV");

         // Attendance Permissions
         createOrUpdatePermission("Attendance", "Daily Status", "View", "View Daily Status Details"); // HR
         createOrUpdatePermission("Attendance", "Daily Status", "All", "Create, Read, Update, Delete Daily Status Details");
         createOrUpdatePermission("Attendance", "Daily Menu", "View", "View Daily Attendance of Employee for Today"); // HR
         createOrUpdatePermission("Attendance", "Daily Menu", "All", "Create, Read, Update, Delete Daily Status Details");
         createOrUpdatePermission("Attendance", "My Attendance", "View", "View Own Attendance History");
         createOrUpdatePermission("Attendance", "Overall Attendance", "View", "View Overall Attendance History of Employees"); // HR
         createOrUpdatePermission("Attendance", "Report", "Generate", "Generate Attendance Report"); // HR

         // Leaves Permissions
         createOrUpdatePermission("Leaves", "Leave Request", "View", "View Leave Requests");
         createOrUpdatePermission("Leaves", "Leave Request", "Add", "Submit Leave Request");
         createOrUpdatePermission("Leaves", "Leave Type", "Add", "Add Leave Type");
         createOrUpdatePermission("Leaves", "Leave Type", "Edit", "Edit Leave Type");
         createOrUpdatePermission("Leaves", "Leave Type", "View", "View Leave Type");
         createOrUpdatePermission("Leaves", "My Leave Request", "View", "View Own Leave Request History");
         createOrUpdatePermission("Leaves", "Employee Leave", "Add", "Submit Leave for Employee Without Waiting for Approval");
         createOrUpdatePermission("Leaves", "Approval", "Add", "Approve Leave for Employee");

         // Clients Permissions
         // Leads
         createOrUpdatePermission("Clients", "Leads", "View", "View Leads List");
         createOrUpdatePermission("Clients", "Leads", "Add", "Add New Lead");
         createOrUpdatePermission("Clients", "Leads", "Approve/Reject", "Approve or Reject Leads");
         createOrUpdatePermission("Clients", "Leads", "Delete", "Delete Leads");

         // Client
         createOrUpdatePermission("Clients", "Client Data", "View", "View Client Data List");
         createOrUpdatePermission("Clients", "Client Details", "View", "View Details per Client");
         createOrUpdatePermission("Clients", "Client Details", "Edit", "Edit Details per Client");

         // Projects
         createOrUpdatePermission("Clients", "Project", "View", "View List of Projects");
         createOrUpdatePermission("Clients", "Project", "Add", "Add New Project");
         createOrUpdatePermission("Clients", "Project Details", "View", "View Project Details");
         createOrUpdatePermission("Clients", "Project Details", "Edit", "Edit Project Details");
         createOrUpdatePermission("Clients", "Project Status", "Edit", "Edit Project Status");

         // Tasks
         createOrUpdatePermission("Clients", "Task", "Add", "Add New Task");
         createOrUpdatePermission("Clients", "Task", "View", "View List of Task");
         createOrUpdatePermission("Clients", "Task Details", "View", "View Details per Task");
         createOrUpdatePermission("Clients", "Task Details", "Edit", "Edit Task Details");
         createOrUpdatePermission("Clients", "Task Status", "Edit", "Edit Task Status");
         createOrUpdatePermission("Clients", "Files", "Upload", "Upload File");
         createOrUpdatePermission("Clients", "Files", "Delete", "Delete File");
         createOrUpdatePermission("Clients", "Files", "View", "View File List and Data");
         createOrUpdatePermission("Clients", "Discussion", "Access", "Access Discussion");
         createOrUpdatePermission("Clients", "Bug Report", "Add", "Add Bug Report");
         createOrUpdatePermission("Clients", "Bug Report", "View", "View Bug Report");
         createOrUpdatePermission("Clients", "Bug Report", "Delete", "Delete Bug Report");

         // Company Permissions
         createOrUpdatePermission("Company", "Department", "Add", "Add New Department");
         createOrUpdatePermission("Company", "Department", "View", "View Department and its details");
         createOrUpdatePermission("Company", "Department", "Edit", "Edit Department details and delete department");
         createOrUpdatePermission("Company", "Division", "Add", "Add New Division");
         createOrUpdatePermission("Company", "Division", "View", "View Division and its details");
         createOrUpdatePermission("Company", "Division", "Edit", "Edit Division details and delete division");

         // Role Permissions
         createOrUpdatePermission("Role", "All Features", "Access", "Access All Role Features");

         // Calendar Permissions
         createOrUpdatePermission("Calendar", "Company Calendar", "View", "View Company Calendar");
         createOrUpdatePermission("Calendar", "Company Calendar", "All", "CRUD function for Company Calendar");
         createOrUpdatePermission("Calendar", "Personal Calendar", "All", "CRUD function for Personal Calendar");
//         createPermission("Calendar", "View", "Leave Calendar", "View Leave Calendar");
     }

   private void createOrUpdatePermission(String category, String action, String description) {
        String permissionKey = generatePermissionKey(category, null, action);
        Optional<PermissionsEntity> existingPermission = permissionsRepository.findByPermissionKey(permissionKey);

        if (existingPermission.isPresent()) {
            PermissionsEntity permissions = existingPermission.get();
            permissions.setCategory(category);
            permissions.setAction(action);
            permissions.setDescription(description);
            permissionsRepository.save(permissions);
        } else {
            PermissionsEntity permissions = new PermissionsEntity();
            permissions.setCategory(category);
            permissions.setAction(action);
            permissions.setDescription(description);
            permissions.setPermissionKey(permissionKey);
            permissionsRepository.save(permissions);
        }
    }

    private void createOrUpdatePermission(String category, String subCategory, String action, String description) {
        String permissionKey = generatePermissionKey(category, subCategory, action);
        Optional<PermissionsEntity> existingPermission = permissionsRepository.findByPermissionKey(permissionKey);

        if (existingPermission.isPresent()) {
            PermissionsEntity permissions = existingPermission.get();
            permissions.setCategory(category);
            permissions.setSubCategory(subCategory);
            permissions.setAction(action);
            permissions.setDescription(description);
            permissionsRepository.save(permissions);
        } else {
            PermissionsEntity permissions = new PermissionsEntity();
            permissions.setCategory(category);
            permissions.setSubCategory(subCategory);
            permissions.setAction(action);
            permissions.setDescription(description);
            permissions.setPermissionKey(permissionKey);
            permissionsRepository.save(permissions);
        }
    }

    private String generatePermissionKey(String category, String subCategory, String action) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(category.toUpperCase()).append(":");
        if (subCategory != null) {
            keyBuilder.append(subCategory.toUpperCase()).append(":");
        }
        keyBuilder.append(action.toUpperCase());
        return keyBuilder.toString();
    }
}
