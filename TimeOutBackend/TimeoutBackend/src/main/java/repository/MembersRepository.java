package repository;

import helpers.ValidationHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entity.Action;
import entity.ActionUser;
import entity.User;
import enums.ActionUserStatus;
import enums.PrivacyType;

public class MembersRepository {

	EntityManager em;

	public MembersRepository(EntityManager em) {
		this.em = em;
	}

	public List<ActionUser> getMembersOfAction(Action action) {
		String hql = "FROM ActionUser AU WHERE AU.action = :action";
		Query query = em.createQuery(hql);
		query.setParameter("action", action);
		List<ActionUser> result = query.getResultList();

		return result;
	}
	
	public boolean checkUserIsMemberOfAction(Action action, User user, ActionUserStatus aut) {
		String hql = "FROM ActionUser AU WHERE AU.action = :action AND AU.user = :user "
				+ "AND AU.actionUserStatus = :actionUserStatus";
		Query query = em.createQuery(hql);
		query.setParameter("action", action)
		.setParameter("user", user)
		.setParameter("actionUserStatus", aut);
		List<ActionUser> result = query.getResultList();
		
		if (result == null || result.size() == 0)
			return false;

		return true;
	}

	public void insertInvitedPeople(String invitedPeopleString, Action action) {
		if (ValidationHelper.isNullOrWhitespace(invitedPeopleString))
			return;
		
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Integer>>() {
		}.getType();
		ArrayList<Integer> t = gson.fromJson(invitedPeopleString, listType);
		List<Integer> invitedPeople = t;

		for (int i = 0; i < invitedPeople.size(); i++) {

			ActionUser actionUser = new ActionUser();
			Query query = em
					.createQuery("FROM User U WHERE U.userId = :userId");
			query.setParameter("userId", invitedPeople.get(i).longValue());
			actionUser.setUser((User) query.getSingleResult());

			actionUser.setAction(action);
			if (action.getPrivacy() == PrivacyType.PUBLIC.toString()) {
				actionUser.setActionUserStatus(ActionUserStatus.MEMBER);
			} else {
				actionUser.setActionUserStatus(ActionUserStatus.INVITED);
			}
			em.persist(actionUser);
		}

	}

	public void acceptInvitation(User user, Action action) {
		Query query = em
				.createQuery("FROM ActionUser AU WHERE AU.user = :user AND"
						+ "AU.action = :action");
		query.setParameter("user", user).setParameter("action", action);

		ActionUser actionUser = (ActionUser) query.getSingleResult();
		actionUser.setActionUserStatus(ActionUserStatus.MEMBER);
		em.persist(actionUser);
	}
}
