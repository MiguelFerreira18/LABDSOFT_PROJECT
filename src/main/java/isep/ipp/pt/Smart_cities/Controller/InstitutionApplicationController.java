package isep.ipp.pt.Smart_cities.Controller;
/* 
import isep.ipp.pt.Smart_cities.Model.UserModel.InstitutionApplication;
import isep.ipp.pt.Smart_cities.Service.InstitutionApplicationService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution-applications")
public class InstitutionApplicationController {

    @Autowired
    private InstitutionApplicationService service;

    @PostMapping
    public ResponseEntity<InstitutionApplication> createApplication(@Valid @RequestBody InstitutionApplication application) {
        if (application.getInstitutionName() == null || application.getInstitutionName().isEmpty()) {
            throw new IllegalArgumentException("Institution name must not be empty");
        }

        // Create the application using the service
        InstitutionApplication createdApplication = service.createApplication(application);

        // Return a response with the created application
        return ResponseEntity.ok(createdApplication);
    }


    @GetMapping("/{id}")
    public ResponseEntity<InstitutionApplication> getApplicationById(@PathVariable String id) {
        InstitutionApplication application = service.getApplicationById(id);
        return ResponseEntity.ok(application);
    }

    @GetMapping
    public ResponseEntity<List<InstitutionApplication>> getApplicationsByStatus(@RequestParam(required = false) String status,
                                                                                 @RequestParam(required = false) String userId) {
        if (status != null) {
            return ResponseEntity.ok(service.getApplicationsByStatus(status));
        } else if (userId != null) {
            return ResponseEntity.ok(service.getApplicationsByUserId(userId));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InstitutionApplication> updateApplicationStatus(@PathVariable String id,
                                                                           @RequestParam String status) {
        InstitutionApplication updatedApplication = service.updateStatus(id, status);
        return ResponseEntity.ok(updatedApplication);
    }
}
    */