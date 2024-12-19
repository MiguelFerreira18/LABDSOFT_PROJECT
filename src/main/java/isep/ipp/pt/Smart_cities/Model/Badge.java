package isep.ipp.pt.Smart_cities.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import isep.ipp.pt.Smart_cities.Model.UserModel.User;




@Entity
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private LocalDateTime completionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BadgeCategory category;

    @ManyToOne(optional = false)
    private User user;

    private String iconPath; 

public void  setIconPath(String st){
this.iconPath=st;
}
    
}