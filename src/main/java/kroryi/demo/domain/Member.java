package kroryi.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member {
    @Id
    private String mid;

    private String name;

    private String password;

    private String email;

    private boolean retirement;

    private String social;

    private String nickname;

    private String profileImage;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (this.mid == null || this.mid.isEmpty()) {
            this.mid = UUID.randomUUID().toString();
        }
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }

    public void changeRetire(boolean retirement) {
        this.retirement = retirement;
    }

    public void addRole(MemberRole role) {
        this.roleSet.add(role);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(String newSocial) {
        this.social = newSocial;
    }

    public Member update(String email,String nickname,String profileImage){
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        return this;
    }

    public String getRoleKey(){
        return this.roleSet.stream()
                .findFirst()
                .map(Enum::name)
                .orElse("EMP");
    }
}
