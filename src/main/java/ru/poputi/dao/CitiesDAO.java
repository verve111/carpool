package ru.poputi.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.poputi.jpa.CityEntity;

@Service
@Transactional
public class CitiesDAO {

	@PersistenceContext
    private EntityManager em;
	
	@Transactional(readOnly = true)
	public long getCount() {
		return (long) em.createNamedQuery("CityEntity.count").getResultList().get(0);
	}
	
	/**
	 * Inserts the set (multiple) of the cities.
	 *
	 * @param set - cities set
	 */
	public void insertMap(Set<Map.Entry<Object, Object>> set) {
		for (Entry<Object, Object> entry : set) {
			CityEntity entity = new CityEntity();
			entity.setUniqueId((String) entry.getKey());
			entity.setName((String) entry.getValue());
			em.persist(entity);
		}
	}
	
	/**
	 * Gets all cities list.
	 * 
	 * @return - cities list
	 */
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<CityEntity> findAll() {
		return (List<CityEntity>) em.createQuery("SELECT e FROM CityEntity e").getResultList();
	}
}
