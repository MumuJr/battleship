package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.dao.*;
import com.codeoftheweb.salvo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.Optional;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepo, PostRepository postRepo, ShipRepository shipRepo, ForCommentsRepository forCommentsRepo, ScoreRepository scoreRepo) {
		return (args) -> {

			Player player1 = new Player("Jack", "Bauer", "j.bauer@ctu.gov", encoder.encode("24"));
			Player player2 = new Player("Chloe", "O'Brian", "c.obrian@ctu.gov", encoder.encode("42"));
			Player player3 = new Player("Kim", "Bauer", "kim_bauer@gmail.com", encoder.encode("kb"));
			Player player4  = new Player("Tony", "Almeida", "t.almeida@ctu.gov", encoder.encode("mole"));


			LocalDateTime date1 = LocalDateTime.now();
			LocalDateTime date2 = date1.plusHours(1);
			LocalDateTime date3 = date1.plusHours(2);
			LocalDateTime date4 = date1.plusHours(3);
			LocalDateTime date5 = date1.plusHours(4);

			Post post1 = new Post(date1, "What do you think?");
			Post post2 = new Post(date2, "This is happening...");
			Post post3 = new Post(date3, "Can't believe this hasn't been talked about");
			Post post4 = new Post(date3, "Do you agree here?");
			Post post5 = new Post(date3, "New information has come out...");
			Post post6 = new Post(date4, "Excited to see what comes from this");

			ForComments forComment1 = new ForComments("I AGREE WITH THIS POST", player1);
			ForComments forComment2 = new ForComments("I ALSO AGREE WITH THIS POST", player2);

			post1.addForComment(forComment1);
			post1.addForComment(forComment2);

			Score upVote = new Score(1.0);
			Score downVote = new Score(-1.0);


//			Game ONE
			player1.addScore(upVote);
			post1.addScore(upVote);

			player2.addScore(downVote);
			post1.addScore(downVote);


//			Game TWO
			player1.addScore(upVote);
			post2.addScore(downVote);

			player2.addScore(upVote);
			post2.addScore(upVote);


//			Game Three
			player2.addScore(downVote);
			player2.addScore(downVote);
			post3.addScore(downVote);

			player4.addScore(upVote);
			post3.addScore(downVote);


			postRepo.save(post1);
			postRepo.save(post2);
			postRepo.save(post3);
			postRepo.save(post4);
			postRepo.save(post5);
			postRepo.save(post6);

			playerRepo.save(player1);
			playerRepo.save(player2);
			playerRepo.save(player3);
			playerRepo.save(player4);


			scoreRepo.save(upVote);
			scoreRepo.save(downVote);

			forCommentsRepo.save(forComment1);
			forCommentsRepo.save(forComment2);

		};
	}
}


@Configuration
	class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		PlayerRepository playerRepository;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(inputName-> {
				Optional<Player> player = playerRepository.findByUserName(inputName);
				if (player.isPresent()) {
					return new User(player.get().getUserName(), player.get().getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + inputName);
				}
			});
		}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/games",
						"/web/**",
						"/api/players",
						"/favicon.ico")
				.permitAll()
				.anyRequest()
				.hasAuthority("USER");


		http.formLogin()
				.loginPage("/api/login")
				.usernameParameter("username")
				.passwordParameter("password");

		http.logout().logoutUrl("/api/logout");


			// turn off checking for CSRF tokens
			http.csrf().disable();

			// if user is not authenticated, just send an authentication failure response
			http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if login is successful, just clear the flags asking for authentication
			http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

			// if login fails, just send an authentication failure response
			http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if logout is successful, just send a success response
			http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		}

		private void clearAuthenticationAttributes(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
		}
	}

