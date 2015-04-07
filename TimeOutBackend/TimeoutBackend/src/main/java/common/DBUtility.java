package common;

import com.google.appengine.api.utils.SystemProperty;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class DBUtility {
	public static Map<String, String> properties = new HashMap<>();

	public static Map<String, String> putProperties() {

		// For Cloud Usage
		if (SystemProperty.environment.value() ==
				SystemProperty.Environment.Value.Production) {
			properties.put("javax.persistence.jdbc.driver",
					"com.mysql.jdbc.GoogleDriver");
			properties.put("javax.persistence.jdbc.url",
					"jdbc:google:mysql://timeoutswe5743:timeoutdb3/demo?user=root");
			return properties;
		} 

		// For Local Usage
		properties.put("javax.persistence.jdbc.driver",
				"com.mysql.jdbc.Driver");
		properties.put("javax.persistence.jdbc.url",
				"jdbc:mysql://localhost:3306/demo?user=root?password=password");

		return properties;
	}

	public static EntityManager createEntityManager(){
		if(properties.isEmpty()) {
			Map<String, String> properties = DBUtility.putProperties();
		}
		return Persistence.createEntityManagerFactory(
				"Demo", properties).createEntityManager();
	}

	public static EntityManager startTranscation(){
		EntityManager em = createEntityManager();
		em.getTransaction().begin();
		return em;
	}

	public static void commitTransaction(EntityManager em){
		em.getTransaction().commit();
		em.close();
	}
}
