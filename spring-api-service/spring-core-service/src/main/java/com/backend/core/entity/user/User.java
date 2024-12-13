package com.backend.core.entity.user;

import com.backend.core.entity.role.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @ManyToMany(fetch = FetchType.EAGER) // 역할을 즉시 로드
    @JoinTable(
            name = "user_roles", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "user_id"), // User와 연결
            inverseJoinColumns = @JoinColumn(name = "role_id") // Role과 연결
    )
    private Set<Role> roles = new HashSet<>(); // 사용자 역할 목록

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void addRole(Role userRole) {
        this.roles.add(userRole);
    }
}
