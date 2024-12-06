package com.in28minutes.rest.webservices.restful_web_services.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList<>();
	private static int counterId = 0;
	
	static {
		users.add(new User(++counterId, "Swapnil Wadhankar", LocalDate.now().minusYears(25)));
		users.add(new User(++counterId, "Sushant Thakare", LocalDate.now().minusYears(24)));
		users.add(new User(++counterId, "Shreyas Ghonge", LocalDate.now().minusYears(26)));
		users.add(new User(++counterId, "Shreyanshu Deshmukh", LocalDate.now().minusYears(40)));
	}
	
	public List<User> findAll(){
		return users;
	}

	public User findOne(int id) {
		Predicate<? super User> predicate = user -> user.getId() == id;
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
	
	public void deleteById(int id) {
		Predicate<? super User> predicate = user -> user.getId() == id;
		users.removeIf(predicate);
		
	}
	
	public User save(User user) {
		user.setId(++counterId);
		users.add(user);
		return user;
	}
}
