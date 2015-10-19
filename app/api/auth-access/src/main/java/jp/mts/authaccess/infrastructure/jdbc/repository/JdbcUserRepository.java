package jp.mts.authaccess.infrastructure.jdbc.repository;

import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserBuilder;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.domain.model.UserRepository;
import jp.mts.authaccess.domain.model.UserStatus;
import jp.mts.authaccess.infrastructure.jdbc.model.UserActivationModel;
import jp.mts.authaccess.infrastructure.jdbc.model.UserModel;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {

	@Override
	public void save(User aUser) {
		UserModel userModel = UserModel.findFirst("user_id = ?", aUser.id().value());
		if(userModel == null){
			userModel = new UserModel();
		}
		userModel.set(
			"user_id",  aUser.id().value(),
			"email",    aUser.email(),
			"name",     aUser.name(),
			"password", aUser.encryptedPassword(),
			"status",   aUser.status().name());
		userModel.saveIt();
		
		if (aUser.status() == UserStatus.ACTIVE) {
			UserActivationModel.delete("user_id = ?", aUser.id().value());
		}
	}

	@Override
	public User findById(UserId userId) {
		UserModel userModel = UserModel.findFirst("user_id = ?", userId.value());
		return fromDbToDomain(userModel);
	}

	@Override
	public User findByAuthCredential(UserId userId, String encryptedPassword) {
		UserModel userModel = UserModel.findFirst("user_id = ? and password = ?", userId.value(), encryptedPassword);
		return fromDbToDomain(userModel);
	}
	
	private User fromDbToDomain(UserModel userModel){
		if(userModel == null) return null;

		return new UserBuilder(
			new User(
				new UserId(userModel.getString("user_id")), 
				userModel.getString("email"), 
				userModel.getString("password"),
				userModel.getString("name")))
			.setStatus(UserStatus.ACTIVE)
			.get();
	}
	
}
