package jp.mts.authaccess.rest.presentation.model;

import org.hibernate.validator.constraints.NotBlank;

import jp.mts.authaccess.application.UserAppService;
import jp.mts.authaccess.domain.model.User;

public class UserActivation {

	//input
	@NotBlank
	private String activationId;

	public void setActivationId(String activationId) {
		this.activationId = activationId;
	}

	//output
	private User user;

	public String getUserId() {
		return user.id().value();
	}

	//proccess
	public void activate(UserAppService userAppService) {
		user = userAppService.activateUser(activationId);
	}
}
