package com.ejada.meetingroomreservation.auth.model;

import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class AppUserDetail implements UserDetails {

	private User user;
	private Long id;
	private String fullName;
	private String userName;
	private String password;
	private List<PermissionDTO> permissions;
	private String email;
	private List<SimpleGrantedAuthority> authorities;
	private boolean isEnabled;
	private boolean isCredentialsNonExpired;
	private boolean isAccountNonLocked;
	private boolean isAccountNonExpired;

	public AppUserDetail() {
		super();
	}

	public AppUserDetail(User user, List<PermissionDTO> permissions) {
		super();
		this.id = user.getId();
		this.user = user;
		this.fullName = user.getFullname();
		this.userName = user.getUsername();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.permissions = permissions;
		this.isEnabled = true;
		this.isCredentialsNonExpired = true;
		this.isAccountNonExpired = true;
		this.isAccountNonLocked = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (int i = 0; i < getPermissions().size(); i++) {
			authorities.add(new SimpleGrantedAuthority(getPermissions().get(i).getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		isCredentialsNonExpired = credentialsNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		isAccountNonLocked = accountNonLocked;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		isAccountNonExpired = accountNonExpired;
	}

	public User getUsr() {
		return user;
	}

	public void setUsr(User user) {
		this.user = user;
	}
}
