package ru.poputi.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.poputi.jpa.RouteEntity;

@Service
@Transactional
public class RoutesDAO {

	@PersistenceContext
    private EntityManager em;
	
	//private Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public boolean createRoute(RouteEntity entity) {
		if (countRoutesCreatedTodayByUser(entity.getCreatorId()) < 5) {
			em.persist(entity);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly = true)
	private int countRoutesCreatedTodayByUser(String creatorId) {
		String sql = "SELECT COUNT(r) FROM RouteEntity r WHERE creatorId = :creatorId AND DAY(creationDate) = DAY(:creationDate)";
		Query q = em.createQuery(sql);
		q.setParameter("creatorId", creatorId);
		q.setParameter("creationDate", new Date());
		return (int)(long)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<RouteEntity> getRoutesByCreatorId(String creatorId) {
		Query q = null;
		String sql = "SELECT r FROM RouteEntity r WHERE isActive = true"; 
		if (creatorId != null) {
			sql += " AND creatorId = :creatorId ";
		} 
		sql += " ORDER BY creationDate DESC";
		q = em.createQuery(sql);
		if (creatorId != null) {
			q.setParameter("creatorId", creatorId);
		}
		return (List<RouteEntity>) q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<RouteEntity> getRoutesRecent() {
		Query q = null;
		String sql = "SELECT r FROM RouteEntity r WHERE isActive = true AND regDate >= :nowDate ORDER BY creationDate DESC"; 
		q = em.createQuery(sql);
		q.setMaxResults(5);
		q.setParameter("nowDate", new Date(), TemporalType.DATE);
		return (List<RouteEntity>) q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<RouteEntity> getRoutesByCityIds(String cityFrom, String cityTo, Date date, boolean is3days) {
		Query q = null;
		String sql = "SELECT r FROM RouteEntity r WHERE isActive = true AND (" + "(regFrom = :regFrom AND regTo = :regTo ) "
				+ " OR (regFrom = :regFrom AND throughCities LIKE :throughTo) "
				+ " OR (throughCities LIKE :throughFrom AND regTo = :regTo ) "
				+ " OR (throughCities LIKE :throughFrom AND throughCities LIKE :throughTo ) )"
				+ (is3days ? " AND (regDate BETWEEN :lowerDate AND :upperDate) " : " AND regDate = :date ")
				+ " ORDER BY regDate, regTime";
		q = em.createQuery(sql);
		q.setParameter("regFrom", cityFrom);
		q.setParameter("regTo", cityTo);
		if (!is3days) {
			q.setParameter("date", date);
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -3);
			q.setParameter("lowerDate", c.getTime(), TemporalType.DATE);
			c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 3);
			q.setParameter("upperDate", c.getTime(), TemporalType.DATE);
		}
		q.setParameter("throughTo", "%" + cityTo + "%");
		q.setParameter("throughFrom", "%" + cityFrom + "%");
		return (List<RouteEntity>) q.getResultList();
	}
	
	@Transactional(readOnly = true)
	public RouteEntity getRouteById(long id) {
		return em.find(RouteEntity.class, id);
	}
	
	public void updateRoute(RouteEntity entity) {
		em.merge(entity);
	}
	
	public void deactivateRoute(Long id) {
		RouteEntity entity = getRouteById(id);
		entity.setActive(false);
		updateRoute(entity);
	}
}
