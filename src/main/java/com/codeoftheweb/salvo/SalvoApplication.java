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
	public CommandLineRunner initData(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, ShipRepository shipRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo) {
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

			Game game1 = new Game(date1);
			Game game2 = new Game(date2);
			Game game3 = new Game(date3);
			Game game4 = new Game(date3);
			Game game5 = new Game(date3);
			Game game6 = new Game(date4);


			GamePlayer firstGame1Player = new GamePlayer(game1, player1, date4);
			GamePlayer secondGame1Player = new GamePlayer(game1, player2, date4);

			GamePlayer firstGame2Player = new GamePlayer(game2, player1, date5);
			GamePlayer secondGame2Player =new GamePlayer(game2, player2, date5);


			GamePlayer firstGame3Player = new GamePlayer(game3, player2, date3);
			GamePlayer secondGame3Player = new GamePlayer(game3, player4, date1);


			GamePlayer firstGame4Player = new GamePlayer(game4, player2, date2);
			GamePlayer secondGame4Player = new GamePlayer(game4, player1, date3);

			GamePlayer firstGame5Player = new GamePlayer(game5, player4, date4);
			GamePlayer secondGame5Player = new GamePlayer(game5, player1, date5);

			GamePlayer firstGame6Player = new GamePlayer(game6, player3, date1);





			Ship destroyer = new Ship("destroyer", Arrays.asList("B1", "B2", "B3", "B4"));
			Ship boat = new Ship("boat", Arrays.asList("C2"));


			Ship sailboat = new Ship("sailboat", Arrays.asList("G5", "G6"));
			Ship cruiser = new Ship("cruiser", Arrays.asList("A1", "A2", "A3", "A4", "A5"));

			Salvo g1Gp1shot1 = new Salvo(Arrays.asList("A1", "A2", "A3"), 1);
			Salvo g1Gp1shot2 = new Salvo(Arrays.asList("C1", "C7", "B9"), 2);

			Salvo g1Gp2shot1 = new Salvo(Arrays.asList("B4", "G5", "G7"), 1);
			Salvo g1Gp2shot2 = new Salvo(Arrays.asList("C2", "C3", "C7"), 2);




			Score g1Gp1Score = new Score(1.0);
			Score g1Gp2Score = new Score(0.0);
			Score g2Gp1Score = new Score(0.5);
			Score g2Gp2Score = new Score(0.5);
			Score g3Gp1Score = new Score(1.0);
			Score g3Gp2Score = new Score(0.0);





			firstGame1Player.addShip(destroyer);
			firstGame1Player.addShip(boat);
			firstGame1Player.addSalvo(g1Gp1shot1);
			firstGame1Player.addSalvo(g1Gp1shot2);



			secondGame1Player.addShip(sailboat);
			secondGame1Player.addShip(cruiser);
			secondGame1Player.addSalvo(g1Gp2shot1);
			secondGame1Player.addSalvo(g1Gp2shot2);


//			Game ONE
			player1.addScore(g1Gp1Score);
			game1.addScore(g1Gp1Score);

			player2.addScore(g1Gp2Score);
			game1.addScore(g1Gp2Score);


//			Game TWO
			player1.addScore(g2Gp1Score);
			game2.addScore(g2Gp1Score);

			player2.addScore(g2Gp2Score);
			game2.addScore(g2Gp2Score);


//			Game Three
			player2.addScore(g3Gp1Score);
			game3.addScore(g3Gp1Score);

			player4.addScore(g3Gp2Score);
			game3.addScore(g3Gp2Score);


			gameRepo.save(game1);
			gameRepo.save(game2);
			gameRepo.save(game3);
			gameRepo.save(game4);
			gameRepo.save(game5);
			gameRepo.save(game6);


			playerRepo.save(player1);
			playerRepo.save(player2);
			playerRepo.save(player3);
			playerRepo.save(player4);


			gamePlayerRepo.save(firstGame1Player);
			gamePlayerRepo.save(secondGame1Player);

			gamePlayerRepo.save(firstGame2Player);
			gamePlayerRepo.save(secondGame2Player);

			gamePlayerRepo.save(firstGame3Player);
			gamePlayerRepo.save(secondGame3Player);

			gamePlayerRepo.save(firstGame4Player);
			gamePlayerRepo.save(secondGame4Player);

			gamePlayerRepo.save(firstGame5Player);
			gamePlayerRepo.save(secondGame5Player);

			gamePlayerRepo.save(firstGame6Player);



			shipRepo.save(cruiser);
			shipRepo.save(destroyer);
			shipRepo.save(boat);
			shipRepo.save(sailboat);

			salvoRepo.save(g1Gp1shot1);
			salvoRepo.save(g1Gp1shot2);
			salvoRepo.save(g1Gp2shot1);
			salvoRepo.save(g1Gp2shot2);

			scoreRepo.save(g1Gp1Score);
			scoreRepo.save(g1Gp2Score);
			scoreRepo.save(g2Gp1Score);
			scoreRepo.save(g2Gp2Score);
			scoreRepo.save(g3Gp1Score);
			scoreRepo.save(g3Gp2Score);

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

