package isep.ipp.pt.Smart_cities.Service;
/* 
import isep.ipp.pt.Smart_cities.Model.UserModel.InstitutionApplication;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import isep.ipp.pt.Smart_cities.Respository.InstitutionApplicationRepository;
import isep.ipp.pt.Smart_cities.Respository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionApplicationService {
    
    
    @Autowired
    private InstitutionApplicationRepository applicationRepository;

    @Autowired
    private UserRepo userRepository;

    public InstitutionApplication createApplication(InstitutionApplication application) {
        String userId = application.getUserId();
        
        application.setUserId(userId);
        return applicationRepository.save(application);
    }



    public InstitutionApplication getApplicationById(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + id));
    }

    public List<InstitutionApplication> getApplicationsByStatus(String status) {
        return applicationRepository.findByStatus(status);
    }

    public List<InstitutionApplication> getApplicationsByUserId(String userId) {
        return applicationRepository.findByUser_Id(userId);
    }

    public InstitutionApplication updateStatus(String id, String newStatus) {
        InstitutionApplication application = getApplicationById(id);
        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
}
*/