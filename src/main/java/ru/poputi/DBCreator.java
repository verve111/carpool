package ru.poputi;

import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ru.poputi.dao.CitiesDAO;

public class DBCreator {

	@Autowired
	private CitiesDAO citiesDao;

	@Resource(name = "poputiProps")
	private Properties props;

	private Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public void init() {
		if (citiesDao.getCount() == 0) {
			log.info("Cities insertion started.....");
			Map<Object, Object> map = new TreeMap<Object, Object>(new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					return ((String) props.get((String) o1)).compareTo((String) props.get((String) o2));
				}
			});
			map.putAll(props);
			citiesDao.insertMap(map.entrySet());
			log.info("Cities insertion finished, " + props.entrySet().size() + " cities inserted");
		}
		// init freemarker config
		log.info("Freemarker initialization started");
		CommonUtils.getFreemarkerTemplate(null);
		log.info("Freemarker initialization finished");
	}

	public Properties getCitiesMap() {
		return props;
	}
}
