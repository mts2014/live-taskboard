package jp.mts.authaccess.infrastructure.repository.jdbc;

import java.util.UUID;

import jp.mts.authaccess.domain.model.Auth;
import jp.mts.authaccess.domain.model.AuthId;
import jp.mts.authaccess.domain.model.AuthRepository;
import jp.mts.authaccess.domain.model.UserId;
import jp.mts.authaccess.infrastructure.repository.jdbc.model.AuthModel;

public class JdbcAuthRepository implements AuthRepository {

	@Override
	public AuthId newAuthId() {
		return new AuthId(UUID.randomUUID().toString().replaceAll("\\-", ""));
	}

	@Override
	public void save(Auth auth) {
		AuthModel model = AuthModel.findFirst("auth_id = ?", auth.id().value());
		if(model == null){
			model = new AuthModel();
		}
		model.set(
			"auth_id", auth.id().value(),
			"user_id", auth.userId().value());
		model.saveIt();
	}

	public Auth findById(AuthId authId) {
		AuthModel model = AuthModel.findFirst("auth_id = ?", authId.value());
		return new Auth(
				new AuthId(model.getString("auth_id")),
				new UserId(model.getString("user_id")));
	}

}
