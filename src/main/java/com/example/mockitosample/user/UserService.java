package com.example.mockitosample.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final PasswordValidator passwordValidator;
	private final UserRepository userRepository;

	public UserService(PasswordValidator passwordValidator, UserRepository userRepository) {
		this.passwordValidator = passwordValidator;
		this.userRepository = userRepository;
	}

	public Long signUp(User user) throws IllegalArgumentException {
		if (!passwordValidator.check(user.getPassword())) {
			throw new IllegalArgumentException();
		}

		return userRepository.save(user);
	}
}
