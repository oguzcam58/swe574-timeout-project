package helpers;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import common.DBUtility;
import common.ErrorMessages;
import common.ResponseHeader;
import entity.Session;
import entity.User;

public class ServiceHelper {

	public static void setResponseHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Set-Cookie");
    }
	
	public static User getSessionUser(EntityManager em, String cookie) {
		Session result = (Session) em
				.createQuery("FROM Session S WHERE S.cookie = :cookie")
				.setParameter("cookie", cookie).getSingleResult();
		return result.getUser();
	}

	public static void authorize(User user) throws Exception {
		if (user == null) {
			throw new Exception(ErrorMessages.notAuthorized);
		}
		
	}

}