package com.schedule.repository;

import com.schedule.repository.entity.Conference;
import com.schedule.repository.exception.TransactionManagerException;
import com.schedule.repository.value.ConferenceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

public class ConferenceRepositoryImpl implements ConferenceRepositoryCustom {
	@Autowired
	private EntityManagerFactory emf;

	@Transactional
	@Override
	public void makeConferenceBookedStatus(Long conferenceId, String conferenceTitle, Long memberId) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();

			Conference conference = em.find(Conference.class, conferenceId, LockModeType.PESSIMISTIC_WRITE);
			conference.setConferenceTitle(conferenceTitle);
			conference.setStatus(ConferenceStatus.BOOKED);
			conference.setMemberId(memberId);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw new TransactionManagerException("[!] Rollback complete." + e.getMessage(), e);
		} finally {
			em.close();
		}
	}
}
