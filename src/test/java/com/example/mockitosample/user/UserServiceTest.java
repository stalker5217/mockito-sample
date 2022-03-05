package com.example.mockitosample.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	PasswordValidator passwordValidator;

	@Mock
	UserRepository userRepository;

	@Test
	@DisplayName("회원 가입 서비스")
	void signUpTest() {
		// given
		User user = new User();
		user.setId("my_id");
		user.setPassword("my_password");

		given(passwordValidator.check(anyString())).willReturn(true);
		given(userRepository.save(user)).willReturn(1L);

		UserService userService = new UserService(passwordValidator, userRepository);

		// when
		Long result = userService.signUp(user);

		// then
		assertThat(result).isEqualTo(1L);
	}

	@Test
	@DisplayName("회원 가입 서비스 취약한 패스워드")
	void signUpWithWeakPasswordTest() {
	    // given
		User user = new User();
		user.setId("my_id");
		user.setPassword("my_password");

		given(passwordValidator.check(anyString())).willReturn(false);

		UserService userService = new UserService(passwordValidator, userRepository);

	    // when
		Throwable thrown = catchThrowable(() -> {
			userService.signUp(user);
		});

	    // then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
		then(passwordValidator).should(times(1)).check(user.getPassword());
		then(userRepository).should(never()).save(user);
	}
}