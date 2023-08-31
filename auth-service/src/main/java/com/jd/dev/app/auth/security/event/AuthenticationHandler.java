package com.jd.dev.app.auth.security.event;

import com.jd.dev.app.auth.services.UserService;
import com.jd.dev.app.user.commons.models.entity.User;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHandler implements AuthenticationEventPublisher {
	private final Logger log = LoggerFactory.getLogger(AuthenticationHandler.class);
	private final UserService userService;

	public AuthenticationHandler(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		String errorMessage = "Error en el login: " + exception.getMessage();
		log.error(errorMessage);
		System.out.println(errorMessage);

		try {
			User user = userService.findByUsername(authentication.getName());
			user.setLoginAttempts(user.getLoginAttempts() + 1);

			if (user.getLoginAttempts() >= 3) {
				log.error("User were disabled: " + user.getUsername());
				user.setEnabled(Boolean.FALSE);
			}

			userService.update(user, user.getId());
		} catch (FeignException e) {
			log.error("Username doesn't exist" + authentication.getName());
		}
	}
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		if (authentication.getDetails() instanceof WebAuthenticationDetails) {
			return;
		}
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String message = "Success Login: " + userDetails.getUsername();
		log.info(message);

		User user = userService.findByUsername(authentication.getName());

		if (user.getLoginAttempts() > 0) {
			user.setLoginAttempts(0);
			userService.update(user, user.getId());
		}

	}

}
